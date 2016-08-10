/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zlib.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * ��˵�������������
 * 
 * @version 1.0
 * @author hy
 */

public abstract class BufferOutputStream extends OutputStream
{

	/* static fields */
	/** Ĭ�ϵĳ�ʼ�����С��2k */
	public static final int CAPACITY=2048;

	/* fields */
	/** �ֽڻ��� */
	ByteBuffer buffer;
	/** д�볤�� */
	int length;
	/** ���رձ�־ */
	boolean closed;

	/* constructors */
	/** ��Ĭ�ϵĻ����С����һ����������� */
	public BufferOutputStream()
	{
		this(CAPACITY);
	}
	/** ��ָ���Ļ����С����һ����������� */
	public BufferOutputStream(int capacity)
	{
		if(capacity<1)
			throw new IllegalArgumentException(getClass().getName()
				+" <init>, invalid capatity:"+capacity);
		buffer=new ByteBuffer(capacity);
		length=0;
	}
	/* properties */
	/** �õ�������ݻ� */
	public int capacity()
	{
		return buffer.capacity();
	}
	/** ���û�����ݻ���ֻ�������ݻ� */
	public void setCapacity(int len)
	{
		buffer.setCapacity(len);
	}
	/** �õ���ǰ������ֽڳ��� */
	public int length()
	{
		return buffer.length();
	}
	/** ����Ѿ�д����ֽڳ��� */
	public int getWriteLength()
	{
		return length;
	}
	/** �ж��Ƿ����ر� */
	public boolean isClosed()
	{
		return closed;
	}
	/* methods */
	/** ���÷��� */
	public void reset()
	{
		buffer.clear();
		length=0;
		closed=false;
	}
	/** ˢ�����ݷ��� */
	public abstract void flush(byte[] data,int offset,int length)
		throws IOException;
	/** д��һ���ֽ� */
	public void write(int b) throws IOException
	{
		if(closed)
			throw new IOException(getClass().getName()+" write, closed");
		length++;
		buffer.writeByte(b);
		if(buffer.top()>=buffer.capacity()) flush();
	}
	/** д��ָ�����ֽ����� */
	public void write(byte[] data,int offset,int length) throws IOException
	{
		if(closed)
			throw new IOException(getClass().getName()+" write, closed");
		this.length+=length;
		if(length>buffer.capacity())
		{
			flush();
			flush(data,offset,length);
		}
		else if(length>buffer.capacity()-buffer.top())
		{
			flush();
			buffer.write(data,offset,length);
		}
		else
			buffer.write(data,offset,length);
	}
	/** ˢ�·��� */
	public void flush() throws IOException
	{
		if(buffer.top()<=0) return;
		flush(buffer.getArray(),0,buffer.top());
		buffer.clear();
	}
	/** �رշ��� */
	public void close() throws IOException
	{
		closed=true;
		flush();
	}

}