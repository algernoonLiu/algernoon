/**
 * Coyyright 2001 by seasky <www.seasky.cn>.
 */

package zmyth.util;

import java.io.File;

import zlib.set.Comparator;
import zlib.set.ObjectArray;
import zlib.set.SetKit;

/**
 * 类说明：文件监视器条目
 * 
 * @version 1.0
 * @author hy
 */

/** 监听条目类 */
class FileMonitorItem
{

	/* static fields */
	/** 文件名比较器 */
	static final Comparator comp=new FileNameComparator();

	/* fields */
	/** 所在的文件监视器 */
	FileMonitor monitor;
	/** 文件名称 */
	String name;
	/** 文件 */
	File file;
	/** 当前是否存在 */
	boolean exist;
	/** 文件最后修改时间 */
	long lastTime;
	/** 监听器列表 */
	ObjectArray listenerList;
	/** 目录内的文件 */
	File[] files;
	/** 目录内的文件最后修改时间 */
	long[] filesLastTime;

	/* constructors */
	/** 构造一个监听条目对象 */
	FileMonitorItem(FileMonitor monitor,String name)
	{
		this.monitor=monitor;
		this.name=name;
		file=new File(name);
		listenerList=new ObjectArray();
		exist=file.exists();
		if(!exist) return;
		lastTime=file.lastModified();
		openDirectory();
	}
	/* methods */
	/** 打开目录 */
	void openDirectory()
	{
		if(!file.isDirectory()) return;
		files=file.listFiles();
		SetKit.sort(files,comp);
		filesLastTime=new long[files.length];
		for(int i=0;i<files.length;i++)
			filesLastTime[i]=files[i].lastModified();
	}
	/** 检查文件是否被修改 */
	void checkModified()
	{
		boolean b=file.exists();
		if(exist)
		{
			if(b)
			{
				long time=file.lastModified();
				if(time!=lastTime)
				{
					lastTime=time;
					monitor.fire(name,FileMonitor.MODIFY,listenerList
						.getArray());
				}
				if(files!=null) compareFiles();
			}
			else
			{
				exist=b;
				monitor
					.fire(name,FileMonitor.DELETE,listenerList.getArray());
				if(files!=null)
				{
					Object[] array=listenerList.getArray();
					for(int i=files.length-1;i>=0;i--)
					{
						monitor.fire(name,files[i].getName(),
							FileMonitor.DELETE,array);
					}
					files=null;
					filesLastTime=null;
				}
			}
		}
		else if(b)
		{
			exist=b;
			monitor.fire(name,FileMonitor.ADD,listenerList.getArray());
			openDirectory();
			if(files!=null)
			{
				Object[] array=listenerList.getArray();
				for(int i=files.length-1;i>=0;i--)
				{
					monitor.fire(name,files[i].getName(),FileMonitor.ADD,
						array);
				}
			}
		}
	}
	/** 比较文件目录中的文件是否被修改、增加和删除 */
	void compareFiles()
	{
		File[] array1=files;
		long[] times1=filesLastTime;
		File[] array2=file.listFiles();
		SetKit.sort(array2,comp);
		long[] times2=new long[array2.length];
		for(int i=0;i<array2.length;i++)
			times2[i]=array2[i].lastModified();
		// 进行两个排序数组的比较
		int i=0,j=0,iLen=array1.length,jLen=array2.length;
		while(i<iLen&&j<jLen)
		{
			int c=comp.compare(array1[i],array2[j]);
			if(c==Comparator.COMP_GRTR)
			{
				monitor.fire(name,array2[i].getName(),FileMonitor.ADD,
					listenerList.getArray());
				j++;
			}
			else if(c==Comparator.COMP_LESS)
			{
				monitor.fire(name,array1[i].getName(),FileMonitor.DELETE,
					listenerList.getArray());
				i++;
			}
			else
			{
				if(times1[i]!=times2[j])
					monitor.fire(name,array1[i].getName(),
						FileMonitor.MODIFY,listenerList.getArray());
				i++;
				j++;
			}
		}
		files=array2;
		filesLastTime=times2;
	}

}