/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zlib.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * ��˵���������������������
 * 
 * @version 1.0
 * @author hy
 */

public final class IOKit
{

	/* static fields */
	/** ����Ϣ */
	public static final String toString=IOKit.class.getName();
	/** Ĭ��һ�����ݵĳ�ʼ������С */
	public static final int LINE_CAPACITY=64;
	/** Ĭ�ϵ���������Ļ���������С */
	private static final int DEFAULT_BUFFER_SIZE=8192;

	/* static methods */
	/* ������ */
	/** �������������ݴ��뵽������� */
	public static void io(InputStream in,OutputStream out)
		throws IOException
	{
		io(in,out,DEFAULT_BUFFER_SIZE);
	}
	/** �������������ݴ��뵽������У�ʹ��ָ���Ļ����С */
	public static void io(InputStream in,OutputStream out,int bufferSize)
		throws IOException
	{
		byte[] buffer=new byte[bufferSize];
		int amount;
		while((amount=in.read(buffer))>=0)
			out.write(buffer,0,amount);
	}
	/**
	 * ������̬���ȣ� ���ݴ�С���ö�̬���ȣ����������£����Ϊ512M��
	 * <li>1xxx xxxx��ʾ��0~0x80��0~128B��</li>
	 * <li>01xx xxxx xxxx xxxx��ʾ��0~0x4000��0~16K��</li>
	 * <li>001x xxxx xxxx xxxx xxxx xxxx xxxx xxxx��ʾ��0~0x20000000��0~512M��</li>
	 */
	public static int readLength(DataInputStream dis) throws IOException
	{
		int n=dis.readUnsignedByte();
		if(n>=0x80)
			return n-0x80;
		else if(n>=0x40)
			return (n<<8)+dis.readUnsignedByte()-0x4000;
		else if(n>=0x20)
			return (n<<24)+(dis.readUnsignedByte()<<16)
				+dis.readUnsignedShort()-0x20000000;
		else
			throw new IllegalArgumentException(toString
				+" readLength, invalid number:"+n);
	}
	/** д�붯̬���ȣ�����д����ֽڳ��� */
	public static int writeLength(DataOutputStream dos,int len)
		throws IOException
	{
		if(len>=0x20000000||len<0)
			throw new IllegalArgumentException(toString
				+" writeLength, invalid len:"+len);
		if(len>=0x4000)
		{
			dos.writeInt(len+0x20000000);
			return 4;
		}
		else if(len>=0x80)
		{
			dos.writeShort(len+0x4000);
			return 2;
		}
		else
		{
			dos.writeByte(len+0x80);
			return 1;
		}
	}
	/** ��ָ�����������ж�ȡһ������ */
	public static byte[] readLine(InputStream is) throws IOException
	{
		byte[] data=new byte[LINE_CAPACITY];
		int top=0;
		int r=0;
		while((r=is.read())>=0)
		{
			if(r=='\n') break;
			data[top++]=(byte)r;
			if(top==data.length)
			{
				byte[] temp=new byte[2*top];
				System.arraycopy(data,0,temp,0,top);
				data=temp;
			}
		}
		if(top>0&&data[top-1]=='\r') top--;
		if(r<0&&top==0) return null;
		if(top<data.length)
		{
			byte[] temp=new byte[top];
			System.arraycopy(data,0,temp,0,top);
			data=temp;
		}
		return data;
	}
	/** ��ָ�����������ж�ȡһ���ֽ����� */
	public static byte[] readData(InputStream is,int len) throws IOException
	{
		if(len==0) return new byte[0];
		byte[] data=new byte[len>0?len:2048];
		int top=0;
		int r=0;
		while((r=is.read(data,top,data.length-top))>0)
		{
			top+=r;
			if(len<0)
			{
				if(top==data.length)
				{
					byte[] temp=new byte[2*top];
					System.arraycopy(data,0,temp,0,top);
					data=temp;
				}
			}
			else if(top==len) break;
		}
		if(top<=0) return null;
		if(top<data.length)
		{
			byte[] temp=new byte[top];
			System.arraycopy(data,0,temp,0,top);
			data=temp;
		}
		return data;
	}
	/** ��ָ�����������з��ͻ��з� */
	public static void writeLine(OutputStream os) throws IOException
	{
		os.write((byte)'\r');
		os.write((byte)'\n');
	}

	/* ����� */
	/** д�붯̬���� */
	public static int writeLength(java.nio.ByteBuffer data,int len)
	{
		if(len>=0x20000000||len<0)
			throw new IllegalArgumentException(toString
				+" writeLength, invalid len:"+len);
		if(len>=0x4000)
		{
			data.putInt(len+0x20000000);
			return 4;
		}
		else if(len>=0x80)
		{
			data.putShort((short)(len+0x4000));
			return 2;
		}
		else
		{
			data.put((byte)(len+0x80));
			return 1;
		}
	}
	/** ������̬���ȵ��ֽڳ��� */
	public static int getReadLength(int n)
	{
		if(n>=0x80) return 1;
		if(n>=0x40) return 2;
		if(n>=0x20) return 4;
		throw new IllegalArgumentException(toString
			+" getReadLength, invalid number:"+n);
	}

	/* constructors */
	private IOKit()
	{
	}

}