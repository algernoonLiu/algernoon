/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zlib.log;

/**
 * ��˵������¼������
 * 
 * @version 1.0
 * @author hy
 */

public class LogFactory
{

	/* static fields */
	/** �ռ�¼�� */
	public static final Logger NULL=new Logger();
	/** ��ǰ�ļ�¼������ */
	protected static LogFactory factory=new StandardLogFactory();

	/* static methods */
	/** ��õ�ǰ�ļ�¼������ */
	public static LogFactory getFactory()
	{
		return factory;
	}
	/** ���ָ����ļ�¼�� */
	public static Logger getLogger(Class clazz)
	{
		return getLogger(clazz.getName());
	}
	/** ���ָ�����Ƶļ�¼�� */
	public static Logger getLogger(String name)
	{
		if(factory==null) return NULL;
		return factory.getInstance(name);
	}

	/* methods */
	/** ���ָ�����Ƶļ�¼��ʵ�� */
	public Logger getInstance(String name)
	{
		return NULL;
	}

}