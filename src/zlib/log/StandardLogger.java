/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zlib.log;

/**
 * ��˵������׼��¼��
 * 
 * @version 1.0
 * @author hy
 */

public class StandardLogger extends Logger
{

	/* static fields */
	/** ϵͳ����ʱ�� */
	public static final long START_TIME=System.currentTimeMillis();

	/** ��¼�Ǽǿ��ر��� */
	public static boolean trace=true,debug=true,info=true,warn=true,
					error=true,fatal=true;

	/* fields */
	/** ���� */
	String name;

	/* constructors */
	/** �����ֹ����¼�� */
	protected StandardLogger(String name)
	{
		this.name=name;
	}
	/* methods */
	/** ���ټ����Ƿ�� */
	public boolean isTraceEnabled()
	{
		return trace;
	}
	/** ���ټ�¼ */
	public void trace(Object message)
	{
		System.out.println((System.currentTimeMillis()-START_TIME)+" ["
			+Thread.currentThread().getName()+"] TRACE "+name+" - "+message);
	}
	/** �����쳣��¼ */
	public void trace(Object message,Throwable t)
	{
		System.err.println((System.currentTimeMillis()-START_TIME)+" ["
			+Thread.currentThread().getName()+"] TRACE "+name+" - "+message);
		if(t!=null) t.printStackTrace();
	}
	/** ���Լ����Ƿ�� */
	public boolean isDebugEnabled()
	{
		return debug;
	}
	/** ���Լ�¼ */
	public void debug(Object message)
	{
		System.out.println((System.currentTimeMillis()-START_TIME)+" ["
			+Thread.currentThread().getName()+"] DEBUG "+name+" - "+message);
	}
	/** �����쳣��¼ */
	public void debug(Object message,Throwable t)
	{
		System.err.println((System.currentTimeMillis()-START_TIME)+" ["
			+Thread.currentThread().getName()+"] DEBUG "+name+" - "+message);
		if(t!=null) t.printStackTrace();
	}
	/** ��Ϣ�����Ƿ�� */
	public boolean isInfoEnabled()
	{
		return info;
	}
	/** ��Ϣ��¼ */
	public void info(Object message)
	{
		System.out.println((System.currentTimeMillis()-START_TIME)+" ["
			+Thread.currentThread().getName()+"] INFO "+name+" - "+message);
	}
	/** ��Ϣ�쳣��¼ */
	public void info(Object message,Throwable t)
	{
		System.err.println((System.currentTimeMillis()-START_TIME)+" ["
			+Thread.currentThread().getName()+"] INFO "+name+" - "+message);
		if(t!=null) t.printStackTrace();
	}
	/** ���漶���Ƿ�� */
	public boolean isWarnEnabled()
	{
		return warn;
	}
	/** �����¼ */
	public void warn(Object message)
	{
		System.out.println((System.currentTimeMillis()-START_TIME)+" ["
			+Thread.currentThread().getName()+"] WARN "+name+" - "+message);
	}
	/** �����쳣��¼ */
	public void warn(Object message,Throwable t)
	{
		System.err.println((System.currentTimeMillis()-START_TIME)+" ["
			+Thread.currentThread().getName()+"] WARN "+name+" - "+message);
		if(t!=null) t.printStackTrace();
	}
	/** ���󼶱��Ƿ�� */
	public boolean isErrorEnabled()
	{
		return error;
	}
	/** �����¼ */
	public void error(Object message)
	{
		System.out.println((System.currentTimeMillis()-START_TIME)+" ["
			+Thread.currentThread().getName()+"] ERROR "+name+" - "+message);
	}
	/** �����쳣��¼ */
	public void error(Object message,Throwable t)
	{
		System.err.println((System.currentTimeMillis()-START_TIME)+" ["
			+Thread.currentThread().getName()+"] ERROR "+name+" - "+message);
		if(t!=null) t.printStackTrace();
	}
	/** ���ش��󼶱��Ƿ�� */
	public boolean isFatalEnabled()
	{
		return fatal;
	}
	/** ���ش����¼ */
	public void fatal(Object message)
	{
		System.out.println((System.currentTimeMillis()-START_TIME)+" ["
			+Thread.currentThread().getName()+"] FATAL "+name+" - "+message);
	}
	/** ���ش����쳣��¼ */
	public void fatal(Object message,Throwable t)
	{
		System.err.println((System.currentTimeMillis()-START_TIME)+" ["
			+Thread.currentThread().getName()+"] FATAL "+name+" - "+message);
		if(t!=null) t.printStackTrace();
	}

}