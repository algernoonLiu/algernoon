/**
 * Coyyright 2001 by seasky <www.seasky.cn>.
 */

package zlib.io;

/**
 * ��˵�������ֽڻ���ķ�ʽ���л�ָ������Ľӿ�
 * 
 * @version 1.0
 * @author hy
 */

public interface BytesWriter
{

	/** ��ָ���������л����ֽڻ����� */
	public void bytesWrite(Object obj,ByteBuffer data);

}