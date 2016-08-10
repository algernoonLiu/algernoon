/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zlib.log;

/**
 * ��˵������׼��¼������
 * 
 * @version 1.0
 * @author hy
 */

public class StandardLogFactory extends LogFactory
{

	/* static methods */
	/** ����׼��¼����������Ϊ��׼���� */
	public static void configure()
	{
		factory=new StandardLogFactory();
	}

	/* methods */
	/** ���ָ�����Ƶļ�¼��ʵ�� */
	public Logger getInstance(String name)
	{
		return new StandardLogger(name);
	}

}