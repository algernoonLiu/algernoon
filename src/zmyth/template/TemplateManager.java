/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zmyth.template;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import zlib.event.ChangeAdapter;
import zlib.log.LogFactory;
import zlib.log.Logger;
import zmyth.timer.TimerCenter;
import zmyth.timer.TimerEvent;
import zmyth.timer.TimerListener;
import zmyth.util.FileMonitor;

/**
 * 类说明：模板管理器
 * 
 * @version 1.0
 * @author hy
 */

public class TemplateManager extends ChangeAdapter implements TimerListener
{

	/* static fields */
	/** 默认的超时时间为30分钟 */
	public static final int TIMEOUT=30*60*1000;

	/** 日志记录 */
	private static final Logger log=LogFactory
		.getLogger(TemplateManager.class);

	/* fields */
	/** 模板所在路径 */
	String path;
	/** 模板列表 */
	Map templateList=new HashMap();

	/** 超时时间 */
	int timeout=TIMEOUT;
	/** collate定时器，30秒 */
	TimerEvent collateTimerEvent=new TimerEvent(this,"collate",30*1000);

	/* properties */
	/** 获得模板所在路径 */
	public String getPath()
	{
		return path;
	}
	/** 获得模板的超时时间 */
	public int getTimeout()
	{
		return timeout;
	}
	/** 设置模板的超时时间 */
	public void setTimeout(int timeout)
	{
		this.timeout=timeout;
	}
	/** 获得collate定时器 */
	public TimerEvent getCollateTimerEvent()
	{
		return collateTimerEvent;
	}
	/* init */
	/** 初始化方法 */
	public void init(String path)
	{
		if(path==null)
			throw new IllegalArgumentException(this+" init, null path");
		if(this.path!=null)
			throw new IllegalStateException(this
				+" init, has already initialized");
		this.path=path;
		TimerCenter.getMinuteTimer().add(collateTimerEvent);
		if(log.isInfoEnabled()) log.info("init, path="+path);
	}
	/* methods */
	/** 获得指定名字的模板条目 */
	synchronized Item getItem(String name)
	{
		return (Item)templateList.get(name);
	}
	/** 获得指定名字的模板 */
	public synchronized Template get(String name)
	{
		Item i=(Item)templateList.get(name);
		return (i!=null)?i.template:null;
	}
	/** 添加指定名字的模板 */
	public synchronized void add(String name,Template template)
	{
		templateList.put(name,new Item(template));
	}
	/** 移除指定名字的模板 */
	public synchronized Template remove(String name)
	{
		Item i=(Item)templateList.remove(name);
		return (i!=null)?i.template:null;
	}
	/** 加载指定路径名（绝对路径名）的模板 */
	public Template load(String file)
	{
		File f=new File(file);
		Template t=new Template();
		try
		{
			t.read(new FileReader(f));
			if(log.isDebugEnabled()) log.debug("load, file="+file);
			return t;
		}
		catch(IOException e)
		{
			if(log.isWarnEnabled()) log.warn("load error, file="+file,e);
			return null;
		}
	}
	/**
	 * 获得一个指定名字（一般是路径名）模板，
	 * 如果不在缓存中，则从文件系统中重新载入
	 */
	public Template getTemplate(String name)
	{
		name=path+name;
		Item i=getItem(name);
		if(i!=null)
		{
			i.count++;
			i.activeTime=System.currentTimeMillis();
			return i.template;
		}
		Template t=load(name);
		if(t==null) return null;
		add(name,t);
		FileMonitor.add(name,this);
		return t;
	}
	/** 定时事件的监听方法 */
	public void onTimer(TimerEvent ev)
	{
		if(ev==collateTimerEvent) collate(ev.getCurrentTime());
	}
	/** 整理方法 */
	public synchronized void collate(long time)
	{
		time-=timeout;
		Map.Entry e;
		Iterator it=templateList.entrySet().iterator();
		while(it.hasNext())
		{
			e=(Map.Entry)it.next();
			if(time<((Item)e.getValue()).activeTime) continue;
			FileMonitor.remove((String)e.getKey(),this);
			it.remove();
		}
	}
	/** 对象改变方法 */
	public void change(Object source,int type,Object value)
	{
		if(source==FileMonitor.getInstance())
		{
			if(type==FileMonitor.MODIFY)
			{
				String name=(String)value;
				Template t=load(name);
				if(t==null) return;
				add(name,t);
			}
			else if(type==FileMonitor.DELETE)
			{
				remove((String)value);
			}
		}
	}
	/** 关闭方法 */
	public void close()
	{
		TimerCenter.getMinuteTimer().remove(collateTimerEvent);
		FileMonitor.remove(this);
		synchronized(this)
		{
			templateList.clear();
		}
		if(log.isInfoEnabled()) log.info("close, path="+path);
		path=null;
	}

	/* inner classes */
	/** 类说明：模板条目，包括模板和时间信息 */
	class Item
	{

		/* fields */
		/** 模板 */
		Template template;
		/** 调用次数 */
		int count=0;
		/** 活动时间 */
		long activeTime=0;
		/** 起始时间 */
		long startTime=0;

		/* constructors */
		/** 构建一个指定模板的模板条目 */
		Item(Template t)
		{
			template=t;
			activeTime=startTime=System.currentTimeMillis();
		}
	}

}