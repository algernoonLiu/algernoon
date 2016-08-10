/**
 * Coyyright 2001 by seasky <www.seasky.cn>.
 */

package zlib.io;


/**
 * ��˵���� �ֽڻ�����ֲ߳̾�����
 * 
 * @version 1.0
 * @author hy
 */

public final class ByteBufferThreadLocal extends ThreadLocal
{

	/* static fields */
	/** Ψһ��ʵ�� */
	private static final ThreadLocal instance=new ByteBufferThreadLocal();

	/* static methods */
	/** ��õ�ǰ�̵߳��ֽڻ��� */
	public static ByteBuffer getByteBuffer()
	{
		return (ByteBuffer)instance.get();
	}

	/* methods */
	/** ��ʼ����ǰ�ֲ߳̾��������ֽڻ��� */
	protected Object initialValue()
	{
		return new ByteBuffer();
	}

}