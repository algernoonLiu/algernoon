/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zlib.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * ��˵��������������
 * 
 * @version 1.0
 * @author hy
 */

public abstract class BufferInputStream extends InputStream
{

	/* static fields */
	/** Ĭ�ϵĳ�ʼ�����С��2k */
	public static final int CAPACITY=2048;

	/* fields */
	/** �ֽڻ������� */
	byte[] buffer;
	/** ��дλ�� */
	int r,w;
	/** ��д���� */
	int rLength,wLength;
	/** ���ݽ�����־ */
	boolean over;
	/** ���رձ�־ */
	boolean closed;

	/* constructors */
	/** ��Ĭ�ϵĻ����С����һ������������ */
	public BufferInputStream()
	{
		this(CAPACITY);
	}
	/** ��ָ���Ļ����С����һ������������ */
	public BufferInputStream(int capacity)
	{
		if(capacity<1)
			throw new IllegalArgumentException(getClass().getName()
				+" <init>, invalid capatity:"+capacity);
		buffer=new byte[capacity];
		r=w=0;
		rLength=wLength=0;
		over=false;
	}
	/* properties */
	/** �õ�������ݻ� */
	public int capacity()
	{
		return buffer.length;
	}
	/** ���û�����ݻ���ֻ�������ݻ� */
	public void setCapacity(int len)
	{
		int c=buffer.length;
		if(len>c)
		{
			for(;c<len;c=(c<<1)+1)
				;
			byte[] temp=new byte[c];
			System.arraycopy(buffer,r,temp,0,w-r);
			buffer=temp;
			w-=r;
			r=0;
		}
		else if(w+len>c)
		{
			if(r<w) System.arraycopy(buffer,r,buffer,0,w-r);
			w-=r;
			r=0;
		}
	}
	/** �õ����Զ������ֽڻ��泤�� */
	public int length()
	{
		return w-r;
	}
	/** ����Ѿ��������ֽڳ��� */
	public int getReadLength()
	{
		return rLength;
	}
	/** ����Ѿ�д����ֽڳ��� */
	public int getWriteLength()
	{
		return wLength;
	}
	/** �ж��Ƿ����ݽ��� */
	public boolean isOver()
	{
		return over;
	}
	/** �ж��Ƿ����ر� */
	public boolean isClosed()
	{
		return closed;
	}
	/* methods */
	/** д��һ���ֽڵ����� */
	public void write(byte b)
	{
		if(buffer.length<w+1) setCapacity(w+CAPACITY);
		buffer[w++]=b;
		wLength++;
	}
	/** д��ָ�����ֽ����鵽���� */
	public void write(byte[] data)
	{
		write(data,0,data.length);
	}
	/** д��ָ�����ֽ����鵽���� */
	public void write(byte[] data,int offset,int length)
	{
		if(buffer.length<w+length) setCapacity(w+length);
		System.arraycopy(data,offset,buffer,w,length);
		w+=length;
		wLength+=length;
	}
	/** ���ݽ������� */
	public void over()
	{
		over=true;
	}
	/** ���÷��� */
	public void reset()
	{
		r=w=0;
		rLength=wLength=0;
		over=false;
		closed=false;
	}
	/** ��仺�淽�� */
	public abstract void buffer() throws IOException;
	/** ����һ���ֽ� */
	public int read() throws IOException
	{
		while(true)
		{
			if(closed)
				throw new IOException(getClass().getName()+" read, closed");
			if(r>=w)
			{
				if(over) return -1;
				buffer();
			}
			int len=length();
			if(len<=0) continue;
			rLength++;
			return buffer[r++]&0xff;
		}
	}
	/** �������ݵ�ָ�����ֽ������� */
	public int read(byte data[],int offset,int length) throws IOException
	{
		if(closed)
			throw new IOException(getClass().getName()+" read, closed");
		if(length<=0) return 0;
		if(r>=w)
		{
			if(over) return -1;
			buffer();
		}
		int len=length();
		if(len<=0) return 0;
		if(len>length) len=length;
		System.arraycopy(buffer,r,data,offset,len);
		r+=len;
		rLength+=len;
		return len;
	}
	/** ����ָ�����ȵ����� */
	public long skip(long length) throws IOException
	{
		if(closed)
			throw new IOException(getClass().getName()+" skip, closed");
		if(length<=0) return 0;
		if(r>=w)
		{
			if(over) return -1;
			buffer();
		}
		int len=length();
		if(len<=0) return 0;
		if(len>length) len=(int)length;
		r+=len;
		rLength+=len;
		return len;
	}
	/** ��������������£����Զ������ֽ��� */
	public int available() throws IOException
	{
		if(closed)
			throw new IOException(getClass().getName()+" available, closed");
		return length();
	}
	/** �رշ��� */
	public void close() throws IOException
	{
		closed=true;
	}

}