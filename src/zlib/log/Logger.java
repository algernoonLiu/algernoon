/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zlib.log;

/**
 * ��˵������¼��
 * 
 * @version 1.0
 * @author hy
 */

public class Logger
{

	/* constructors */
	/** ��ֹ�ⲿ���� */
	protected Logger()
	{
	}
	/* methods */
	/** ���ټ����Ƿ�� */
	public boolean isTraceEnabled()
	{
		return false;
	}
	/** ���ټ�¼ */
	public void trace(Object message)
	{
	}
	/** �����쳣��¼ */
	public void trace(Object message,Throwable t)
	{
	}
	/** ���Լ����Ƿ�� */
	public boolean isDebugEnabled()
	{
		return false;
	}
	/** ���Լ�¼ */
	public void debug(Object message)
	{
	}
	/** �����쳣��¼ */
	public void debug(Object message,Throwable t)
	{
	}
	/** ��Ϣ�����Ƿ�� */
	public boolean isInfoEnabled()
	{
		return false;
	}
	/** ��Ϣ��¼ */
	public void info(Object message)
	{
	}
	/** ��Ϣ�쳣��¼ */
	public void info(Object message,Throwable t)
	{
	}
	/** ���漶���Ƿ�� */
	public boolean isWarnEnabled()
	{
		return false;
	}
	/** �����¼ */
	public void warn(Object message)
	{
	}
	/** �����쳣��¼ */
	public void warn(Object message,Throwable t)
	{
	}
	/** ���󼶱��Ƿ�� */
	public boolean isErrorEnabled()
	{
		return false;
	}
	/** �����¼ */
	public void error(Object message)
	{
	}
	/** �����쳣��¼ */
	public void error(Object message,Throwable t)
	{
	}
	/** ���ش��󼶱��Ƿ�� */
	public boolean isFatalEnabled()
	{
		return false;
	}
	/** ���ش����¼ */
	public void fatal(Object message)
	{
	}
	/** ���ش����쳣��¼ */
	public void fatal(Object message,Throwable t)
	{
	}

}