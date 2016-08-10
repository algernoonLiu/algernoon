/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zlib.io;

import java.io.IOException;

/**
 * ��˵�����ܵ�����������ȡʱ�����ݻ�����
 * 
 * @version 1.0
 * @author hy
 */

public class PipeInputStream extends BufferInputStream
{

	/* static fields */
	/** Ĭ�ϵĶ�ȡ��ʱʱ�䣬100���� */
	public static final int TIMEOUT=100;
	/** Ĭ�ϵļ����ȡ�̵߳������ͺ�ȣ�0.75f */
	public static final float HYSTERESIS=0.75f;

	/* fields */
	/** �����ȡ�̵߳����ݳ��ȷ�ֵ */
	int threshold;
	/** ��ȡ��ʱʱ�� */
	int timeout=TIMEOUT;
	/** �߳������� */
	Object lock=new Object();
	/** �쳣���� */
	IOException ioe=null;

	/* constructors */
	/** ��Ĭ�ϵĴ�С����һ���ֽڻ������ */
	public PipeInputStream()
	{
		this(CAPACITY,HYSTERESIS);
	}
	/** ��ָ���Ĵ�С����һ���ֽڻ������ */
	public PipeInputStream(int capacity)
	{
		this(capacity,HYSTERESIS);
	}
	/** ��ָ���Ĵ�С����һ���ֽڻ������ */
	public PipeInputStream(int capacity,float hysteresis)
	{
		super(capacity);
		if(hysteresis>1.0f) hysteresis=1.0f;
		threshold=(int)(capacity*hysteresis);
	}
	/* properties */
	/** ��ü����ȡ�̵߳����ݳ��ȷ�ֵ */
	public int getThreshold()
	{
		return threshold;
	}
	/** ���ü����ȡ�̵߳����ݳ��ȷ�ֵ */
	public void setThreshold(int t)
	{
		if(t>capacity()) t=capacity();
		threshold=t;
	}
	/** ��ö�ȡ��ʱʱ�� */
	public int getTimeout()
	{
		return timeout;
	}
	/** ���ö�ȡ��ʱʱ�� */
	public void setTimeout(int timeout)
	{
		this.timeout=timeout;
	}
	/** ����쳣���� */
	public IOException getIOException()
	{
		return ioe;
	}
	/** �����쳣���� */
	public void setIOException(IOException ioe)
	{
		this.ioe=ioe;
	}
	/** ���û�����ݻ���ֻ�������ݻ� */
	public void setCapacity(int len)
	{
		synchronized(lock)
		{
			int old=capacity();
			super.setCapacity(len);
			threshold=threshold*capacity()/old;
		}
	}
	/* methods */
	/** д��һ���ֽڵ����� */
	public void write(byte b)
	{
		synchronized(lock)
		{
			super.write(b);
			if(length()>threshold) lock.notify();
		}
	}
	/** д��ָ�����ֽ����鵽���� */
	public void write(byte[] data,int offset,int length)
	{
		synchronized(lock)
		{
			super.write(data,offset,length);
			if(length()>threshold) lock.notify();
		}
	}
	/** ���ݽ������� */
	public void over()
	{
		synchronized(lock)
		{
			super.over();
			lock.notify();
		}
	}
	/** ��仺�淽�� */
	public void buffer() throws IOException
	{
		synchronized(lock)
		{
			try
			{
				lock.wait(timeout);
			}
			catch(InterruptedException e)
			{
			}
		}
		if(isClosed())
			throw new IOException(getClass().getName()+" buffer, closed");
		if(ioe!=null) throw ioe;
	}
	/** ����һ���ֽ� */
	public int read() throws IOException
	{
		synchronized(lock)
		{
			return super.read();
		}
	}
	/** �������ݵ�ָ�����ֽ������� */
	public int read(byte data[],int offset,int length) throws IOException
	{
		synchronized(lock)
		{
			return super.read(data,offset,length);
		}
	}
	/** ����ָ�����ȵ����� */
	public long skip(long length) throws IOException
	{
		synchronized(lock)
		{
			return super.skip(length);
		}
	}
	/** ��������������£����Զ������ֽ��� */
	public int available() throws IOException
	{
		synchronized(lock)
		{
			return super.available();
		}
	}
	/** �رշ��� */
	public void close() throws IOException
	{
		synchronized(lock)
		{
			super.close();
			lock.notify();
		}
	}

}