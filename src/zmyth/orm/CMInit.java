/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zmyth.orm;

import java.util.Properties;

import zlib.text.TextKit;
import zmyth.xlib.File;
import zmyth.xlib.FileFactory;

/**
 * 类说明：数据库连接管理器的配置类
 * 
 * @version 1.0
 * @author hy
 */

public class CMInit
{

	/* method */
	/** 初始化方法 */
	public void init(String argFile,ConnectionManager cm)
	{
		File file=FileFactory.getFile(argFile);
		if(file==null)
			throw new IllegalArgumentException(this
				+" init, file not found, "+argFile);
		byte[] data=file.read();
		file.destroy();
		String str=new String(data);
		Properties properties=cm.getProperties();
		String[] strs=TextKit.splitLine(str);
		int index;
		for(int i=0;i<strs.length;i++)
		{
			str=strs[i];
			if(str.length()==0) continue;
			str=str.trim();
			if(str.charAt(0)=='#') continue;
			index=str.indexOf('=');
			if(index<0) continue;
			properties.put(str.substring(0,index),str.substring(index+1));
		}

		str=properties.getProperty("user");
		if(str==null)
			throw new IllegalArgumentException(this+" init, null user");
		str=properties.getProperty("password");
		if(str==null)
			throw new IllegalArgumentException(this+" init, null password");
		str=properties.getProperty("driver");
		if(str==null)
			throw new IllegalArgumentException(this+" init, null driver");
		cm.setDriver(str);
		str=properties.getProperty("url");
		if(str==null)
			throw new IllegalArgumentException(this+" init, null url");
		cm.setURL(str);
		str=properties.getProperty("characterEncoding");
		if(str!=null) cm.setCharacterEncoding(str);

		if((str=properties.getProperty("initSize"))!=null)
			cm.setInitSize(Integer.parseInt(str));
		if((str=properties.getProperty("maxSize"))!=null)
			cm.setMaxSize(Integer.parseInt(str));
		if((str=properties.getProperty("timeout"))!=null)
			cm.setTimeout(Integer.parseInt(str));
		if((str=properties.getProperty("maxUsedCount"))!=null)
			cm.setMaxUsedCount(Integer.parseInt(str));
		if((str=properties.getProperty("autoCommit"))!=null)
			cm.setAutoCommit(!str.equals("false"));
		if((str=properties.getProperty("check"))!=null)
			cm.setCheck(!str.equals("false"));
		cm.init();
	}

}