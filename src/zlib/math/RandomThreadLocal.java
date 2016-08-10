/**
 * Coyyright 2001 by seasky <www.seasky.cn>.
 */

package zlib.math;

/**
 * ��˵���� ��������������ֲ߳̾�����
 * 
 * @version 1.0
 * @author hy
 */

public final class RandomThreadLocal extends ThreadLocal
{

	/* static fields */
	/** Ψһ��ʵ�� */
	private static final ThreadLocal instance=new RandomThreadLocal();

	/* static methods */
	/** ��õ�ǰ�̵߳��ֽڻ��� */
	public static Random getRandom()
	{
		return (Random)instance.get();
	}

	/* methods */
	/** ��ʼ����ǰ�ֲ߳̾��������ֽڻ��� */
	protected Object initialValue()
	{
		return new Random1();
	}

}