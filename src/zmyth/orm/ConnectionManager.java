/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zmyth.orm;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.util.Properties;

import zlib.log.LogFactory;
import zlib.log.Logger;
import zlib.set.ObjectArray;
import zmyth.timer.TimerCenter;
import zmyth.timer.TimerEvent;
import zmyth.timer.TimerListener;

/**
 * 类说明：数据库连接管理器类， 缓存数据库连接，并负责清理超时的连接。
 * 
 * @version 1.0
 * @author hy
 */

public class ConnectionManager implements TimerListener
{

	/* static fields */
	/** 常量定义，默认的初始连接数，默认的最大连接数 */
	public static final int INIT_SIZE=2,MAX_SIZE=20;
	/** 常量定义，默认的连接超时时间（3分钟） */
	public static final int TIMEOUT=3*60*1000;

	/** 日志记录 */
	private static final Logger log=LogFactory
		.getLogger(ConnectionManager.class);

	/* fields */
	/** 连接属性 */
	private Properties properties=new Properties();
	/** 连接驱动 */
	private String driver;
	/** 连接地址 */
	private String url;
	/** 初始连接数 */
	private int initSize=INIT_SIZE;
	/** 最大连接数 */
	private int maxSize=MAX_SIZE;
	/** 连接超时时间 */
	private int timeout=TIMEOUT;
	/** 连接的最大使用次数 */
	private int maxUsedCount;
	/** 连接是否为自动提交 */
	private boolean autoCommit=true;
	/** 在取出连接时，是否先判断连接可用 */
	private boolean check=true;

	/** 连接数组 */
	private ObjectArray connections=new ObjectArray();

	/** collate定时器，60秒 */
	TimerEvent collateTimerEvent=new TimerEvent(this,"collate",60*1000);

	/* properties */
	/** 获得当前的连接数 */
	public int size()
	{
		return connections.size();
	}
	/** 获得连接属性 */
	public Properties getProperties()
	{
		return properties;
	}
	/** 获得连接驱动 */
	public String getDriver()
	{
		return driver;
	}
	/** 设置连接驱动 */
	public void setDriver(String driver)
	{
		this.driver=driver;
	}
	/** 获得连接地址 */
	public String getURL()
	{
		return url;
	}
	/** 设置连接地址 */
	public void setURL(String url)
	{
		this.url=url;
	}
	/** 获得连接的字符编码 */
	public String getCharacterEncoding()
	{
		return properties.getProperty("characterEncoding");
	}
	/** 设置连接的字符编码 */
	public void setCharacterEncoding(String encoding)
	{
		if(encoding==null) encoding=System.getProperty("file.encoding");
		properties.setProperty("useUnicode","ISO-8859-1"
			.equalsIgnoreCase(encoding)?"FALSE":"TRUE");
		properties.setProperty("characterEncoding",encoding);
	}
	/** 获得初始连接数 */
	public int getInitSize()
	{
		return initSize;
	}
	/** 设置初始连接数 */
	public void setInitSize(int size)
	{
		initSize=size;
	}
	/** 获得最大连接数 */
	public int getMaxSize()
	{
		return maxSize;
	}
	/** 设置最大连接数 */
	public void setMaxSize(int size)
	{
		maxSize=size;
	}
	/** 获得连接的超时时间 */
	public int getTimeout()
	{
		return timeout;
	}
	/** 设置连接的超时时间 */
	public void setTimeout(int timeout)
	{
		this.timeout=timeout;
	}
	/** 获得连接的最大使用次数 */
	public int getMaxUsedCount()
	{
		return maxUsedCount;
	}
	/** 设置连接的最大使用次数 */
	public void setMaxUsedCount(int count)
	{
		maxUsedCount=count;
	}
	/** 判断连接是否为自动提交 */
	public boolean isAutoCommit()
	{
		return autoCommit;
	}
	/** 设置连接是否为自动提交 */
	public void setAutoCommit(boolean b)
	{
		autoCommit=b;
	}
	/** 取出连接时是否先判断连接可用（实际是由jdbc驱动发出ping） */
	public boolean isCheck()
	{
		return check;
	}
	/** 设置取出连接时是否先判断连接可用 */
	public void setCheck(boolean b)
	{
		check=b;
	}
	/** 获得collate定时器 */
	public TimerEvent getCollateTimerEvent()
	{
		return collateTimerEvent;
	}
	/* method */
	/** 初始化方法 */
	public void init()
	{
		if(driver==null||driver.length()==0)
			throw new IllegalArgumentException(super.toString()
				+" init, null driver");
		if(url==null||url.length()==0)
			throw new IllegalArgumentException(super.toString()
				+" init, null url");
		Connection con=null;
		try
		{
			// 加载jdbc驱动程序
			Class.forName(driver).newInstance();
			con=DriverManager.getConnection(url,properties);
			DatabaseMetaData md=con.getMetaData();
			// 根据数据库可得到的最大连接数mn调整初始和最大连接数
			int mn=md.getMaxConnections();
			if(mn>0&&mn<initSize) initSize=mn;
			if(mn>0&&mn<maxSize) maxSize=mn;
		}
		catch(Exception e)
		{
			if(log.isWarnEnabled())
				log.warn("init, driver="+driver+", url="+url,e);
			throw new RuntimeException(super.toString()+" init, driver="
				+driver+", url="+url,e);
		}
		finally
		{
			try
			{
				if(con!=null) con.close();
			}
			catch(Exception e)
			{
			}
		}

		Connection c;
		Object[] array=new Object[initSize];
		int j=0;
		for(int i=array.length-1;i>=0;i--)
		{
			c=createConnection();
			if(c==null) continue;
			array[j++]=new ConnectionProxy(c);
		}
		connections.add(array,0,j);
		TimerCenter.getMinuteTimer().add(collateTimerEvent);
		if(log.isInfoEnabled()) log.info("init, "+this);
	}
	/** 得到连接池的正在运行的连接数 */
	public int getRunningCount()
	{
		Object[] array=connections.getArray();
		ConnectionProxy cp=null;
		int n=0;
		for(int i=array.length-1;i>=0;i--)
		{
			cp=(ConnectionProxy)array[i];
			if(cp.isUsed()) n++;
		}
		return n;
	}
	/** 创建连接，如果连接失败，则返回空 */
	public Connection createConnection()
	{
		Connection c=null;
		try
		{
			c=DriverManager.getConnection(url,properties);
			c.setAutoCommit(autoCommit);
		}
		catch(Exception e)
		{
			if(log.isWarnEnabled())
				log.warn("createConnection, url="+url,e);
		}
		return c;
	}
	/** 从连接池中得到连接的方法，如果池满或连接失败，则抛出无效状态异常 */
	public Connection getConnection()
	{
		Object[] array=connections.getArray();
		ConnectionProxy cp=null;
		int i=array.length-1;
		for(;i>=0;i--)
		{
			cp=(ConnectionProxy)array[i];
			if(cp.isUsed()) continue;
			if(check&&!cp.isActive()) continue;
			if(!cp.use()) continue;
			break;
		}
		if(i>=0) return cp;
		if(array.length>=maxSize)
			throw new IllegalStateException(super.toString()
				+" getConnection, overflow");
		Connection c=createConnection();
		if(c==null)
			throw new IllegalStateException(super.toString()
				+" getConnection, driver="+driver+", url="+url);
		cp=new ConnectionProxy(c);
		cp.use();
		connections.add(cp);
		return cp;
	}
	/** 定时事件的监听接口方法 */
	public void onTimer(TimerEvent ev)
	{
		if(ev==collateTimerEvent) collate(ev.getCurrentTime());
	}
	/** 整理方法 */
	public void collate(long time)
	{
		time-=timeout;
		Object[] array=connections.getArray();
		ConnectionProxy cp;
		for(int i=array.length-1;i>=0;i--)
		{
			cp=(ConnectionProxy)array[i];
			// 如果连接被关闭
			if(!cp.isActive())
			{
			}
			// 如果连接超时
			else if(time>cp.getUsedTime())
			{
			}
			else if(cp.isUsed())
			{
				continue;
			}
			// 如果连接的使用次数不超过最大使用次数
			else if(maxUsedCount<=0||cp.getUsedCount()<maxUsedCount)
			{
				continue;
			}
			// 关闭连接并移除
			connections.remove(cp);
			cp.destroy();
		}
		if(log.isInfoEnabled()) log.info("collate, "+this);
	}
	/** 关闭连接池的方法，将所有连接都关闭 */
	public void close()
	{
		TimerCenter.getMinuteTimer().remove(collateTimerEvent);
		Object[] array=connections.getArray();
		connections.clear();
		for(int i=array.length-1;i>=0;i--)
			((ConnectionProxy)array[i]).destroy();
		if(log.isInfoEnabled()) log.info("close, "+this);
	}
	/* common methods */
	public String toString()
	{
		return super.toString()+"[driver="+driver+", url="+url+", run="
			+getRunningCount()+", size="+size()+", maxSize="+maxSize
			+", timeout="+timeout+", maxUsedCount="+maxUsedCount
			+", autoCommit="+autoCommit+", check="+check+"]";
	}

}