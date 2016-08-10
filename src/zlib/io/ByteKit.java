/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zlib.io;

/**
 * ��˵���� �ֽڼ��ֽ�����ķ���������
 * 
 * @version 1.0
 * @author hy
 */

public final class ByteKit
{

	/* static fields */
	/** ����Ϣ */
	public static final String toString=ByteKit.class.getName();

	/* static methods */
	/** ���ֽ�������ָ��λ�ö���һ������ֵ */
	public static boolean readBoolean(byte[] bytes,int pos)
	{
		return bytes[pos]!=0;
	}
	/** ���ֽ�������ָ��λ�ö���һ���ֽ� */
	public static byte readByte(byte[] bytes,int pos)
	{
		return bytes[pos];
	}
	/** ���ֽ�������ָ��λ�ö���һ���޷����ֽ� */
	public static int readUnsignedByte(byte[] bytes,int pos)
	{
		return bytes[pos]&0xff;
	}
	/** ���ֽ�������ָ��λ�ö���һ���ַ� */
	public static char readChar(byte[] bytes,int pos)
	{
		return (char)readUnsignedShort(bytes,pos);
	}
	/** ���ֽ�������ָ��λ�ö���һ���ַ�����λ��ǰ����λ�ں� */
	public static char readChar_(byte[] bytes,int pos)
	{
		return (char)readUnsignedShort_(bytes,pos);
	}
	/** ���ֽ�������ָ��λ�ö���һ����������ֵ */
	public static short readShort(byte[] bytes,int pos)
	{
		return (short)readUnsignedShort(bytes,pos);
	}
	/** ���ֽ�������ָ��λ�ö���һ����������ֵ����λ��ǰ����λ�ں� */
	public static short readShort_(byte[] bytes,int pos)
	{
		return (short)readUnsignedShort_(bytes,pos);
	}
	/** ���ֽ�������ָ��λ�ö���һ���޷��Ŷ�������ֵ */
	public static int readUnsignedShort(byte[] bytes,int pos)
	{
		return (bytes[pos+1]&0xff)+((bytes[pos]&0xff)<<8);
	}
	/** ���ֽ�������ָ��λ�ö���һ���޷��Ŷ�������ֵ����λ��ǰ����λ�ں� */
	public static int readUnsignedShort_(byte[] bytes,int pos)
	{
		return ((bytes[pos+1]&0xff)<<8)+(bytes[pos]&0xff);
	}
	/** ���ֽ�������ָ��λ�ö���һ��������ֵ */
	public static int readInt(byte[] bytes,int pos)
	{
		return ((bytes[pos+3]&0xff))+((bytes[pos+2]&0xff)<<8)
			+((bytes[pos+1]&0xff)<<16)+((bytes[pos]&0xff)<<24);
	}
	/** ���ֽ�������ָ��λ�ö���һ��������ֵ����λ��ǰ����λ�ں� */
	public static int readInt_(byte[] bytes,int pos)
	{
		return ((bytes[pos+3]&0xff)<<24)+((bytes[pos+2]&0xff)<<16)
			+((bytes[pos+1]&0xff)<<8)+((bytes[pos]&0xff));
	}
	/** ���ֽ�������ָ��λ�ö���һ��������ֵ */
	public static float readFloat(byte[] bytes,int pos)
	{
		return Float.intBitsToFloat(readInt(bytes,pos));
	}
	/** ���ֽ�������ָ��λ�ö���һ��������ֵ����λ��ǰ����λ�ں� */
	public static float readFloat_(byte[] bytes,int pos)
	{
		return Float.intBitsToFloat(readInt_(bytes,pos));
	}
	/** ���ֽ�������ָ��λ�ö���һ����������ֵ */
	public static long readLong(byte[] bytes,int pos)
	{
		return (bytes[pos+7]&0xffL)+((bytes[pos+6]&0xffL)<<8)
			+((bytes[pos+5]&0xffL)<<16)+((bytes[pos+4]&0xffL)<<24)
			+((bytes[pos+3]&0xffL)<<32)+((bytes[pos+2]&0xffL)<<40)
			+((bytes[pos+1]&0xffL)<<48)+((bytes[pos]&0xffL)<<56);
	}
	/** ���ֽ�������ָ��λ�ö���һ����������ֵ����λ��ǰ����λ�ں� */
	public static long readLong_(byte[] bytes,int pos)
	{
		return ((bytes[pos+7]&0xffL)<<56)+((bytes[pos+6]&0xffL)<<48)
			+((bytes[pos+5]&0xffL)<<40)+((bytes[pos+4]&0xffL)<<32)
			+((bytes[pos+3]&0xffL)<<24)+((bytes[pos+2]&0xffL)<<16)
			+((bytes[pos+1]&0xffL)<<8)+(bytes[pos]&0xffL);
	}
	/** ���ֽ�������ָ��λ�ö���һ��˫������ֵ */
	public static double readDouble(byte[] bytes,int pos)
	{
		return Double.longBitsToDouble(readLong(bytes,pos));
	}
	/** ���ֽ�������ָ��λ�ö���һ��˫������ֵ����λ��ǰ����λ�ں� */
	public static double readDouble_(byte[] bytes,int pos)
	{
		return Double.longBitsToDouble(readLong_(bytes,pos));
	}
	/** ������̬���ȵ��ֽڳ��� */
	public static int getReadLength(byte b)
	{
		int n=b&0xff;
		if(n>=0x80) return 1;
		if(n>=0x40) return 2;
		if(n>=0x20) return 4;
		throw new IllegalArgumentException(toString
			+" getReadLength, invalid number:"+n);
	}
	/**
	 * ������̬���ȣ� ���ݴ�С���ö�̬���ȣ����������£����Ϊ512M��
	 * <li>1xxx xxxx��ʾ��0~0x80�� 0~128B��</li>
	 * <li>01xx xxxx xxxx xxxx��ʾ��0~0x4000��
	 * 0~16K��</li>
	 * <li>001x xxxx xxxx xxxx xxxx xxxx
	 * xxxx xxxx��ʾ��0~0x20000000�� 0~512M��</li>
	 */
	public static int readLength(byte[] data,int pos)
	{
		int n=data[pos]&0xff;
		if(n>=0x80)
			return n-0x80;
		else if(n>=0x40)
			return (n<<8)+(data[pos+1]&0xff)-0x4000;
		else if(n>=0x20)
			return (n<<24)+((data[pos+1]&0xff)<<16)+((data[pos+2]&0xff)<<8)
				+(data[pos+3]&0xff)-0x20000000;
		else
			throw new IllegalArgumentException(toString
				+" readLength, invalid number:"+n);
	}
	/** д��һ������ֵ���ֽ�������ָ��λ�� */
	public static void writeBoolean(boolean b,byte[] bytes,int pos)
	{
		bytes[pos]=(byte)(b?1:0);
	}
	/** д��һ���ֽ����ֽ�������ָ��λ�� */
	public static void writeByte(byte b,byte[] bytes,int pos)
	{
		bytes[pos]=b;
	}
	/** ���ֽ�������ָ��λ��д��һ���ַ� */
	public static void writeChar(char c,byte[] bytes,int pos)
	{
		writeShort((short)c,bytes,pos);
	}
	/** д��һ���ַ����ֽ�������ָ��λ�ã���λ��ǰ����λ�ں� */
	public static void writeChar_(char c,byte[] bytes,int pos)
	{
		writeShort_((short)c,bytes,pos);
	}
	/** д��һ����������ֵ���ֽ�������ָ��λ�� */
	public static void writeShort(short s,byte[] bytes,int pos)
	{
		bytes[pos]=(byte)(s>>>8);
		bytes[pos+1]=(byte)s;
	}
	/** д��һ����������ֵ���ֽ�������ָ��λ�ã���λ��ǰ����λ�ں� */
	public static void writeShort_(short s,byte[] bytes,int pos)
	{
		bytes[pos]=(byte)s;
		bytes[pos+1]=(byte)(s>>>8);
	}
	/** д��һ��������ֵ���ֽ�������ָ��λ�� */
	public static void writeInt(int i,byte[] bytes,int pos)
	{
		bytes[pos]=(byte)(i>>>24);
		bytes[pos+1]=(byte)(i>>>16);
		bytes[pos+2]=(byte)(i>>>8);
		bytes[pos+3]=(byte)i;
	}
	/** ���ֽ�������ָ��λ��д��һ��������ֵ����λ��ǰ����λ�ں� */
	public static void writeInt_(int i,byte[] bytes,int pos)
	{
		bytes[pos]=(byte)i;
		bytes[pos+1]=(byte)(i>>>8);
		bytes[pos+2]=(byte)(i>>>16);
		bytes[pos+3]=(byte)(i>>>24);
	}
	/** д��һ��������ֵ���ֽ�������ָ��λ�� */
	public static void writeFloat(float f,byte[] bytes,int pos)
	{
		writeInt(Float.floatToIntBits(f),bytes,pos);
	}
	/** д��һ��������ֵ���ֽ�������ָ��λ�ã���λ��ǰ����λ�ں� */
	public static void writeFloat_(float f,byte[] bytes,int pos)
	{
		writeInt_(Float.floatToIntBits(f),bytes,pos);
	}
	/** д��һ����������ֵ���ֽ�������ָ��λ�� */
	public static void writeLong(long l,byte[] bytes,int pos)
	{
		bytes[pos]=(byte)(l>>>56);
		bytes[pos+1]=(byte)(l>>>48);
		bytes[pos+2]=(byte)(l>>>40);
		bytes[pos+3]=(byte)(l>>>32);
		bytes[pos+4]=(byte)(l>>>24);
		bytes[pos+5]=(byte)(l>>>16);
		bytes[pos+6]=(byte)(l>>>8);
		bytes[pos+7]=(byte)l;
	}
	/** д��һ����������ֵ���ֽ�������ָ��λ�ã���λ��ǰ����λ�ں� */
	public static void writeLong_(long l,byte[] bytes,int pos)
	{
		bytes[pos]=(byte)l;
		bytes[pos+1]=(byte)(l>>>8);
		bytes[pos+2]=(byte)(l>>>16);
		bytes[pos+3]=(byte)(l>>>24);
		bytes[pos+4]=(byte)(l>>>32);
		bytes[pos+5]=(byte)(l>>>40);
		bytes[pos+6]=(byte)(l>>>48);
		bytes[pos+7]=(byte)(l>>>56);
	}
	/** д��һ��˫������ֵ���ֽ�������ָ��λ�� */
	public static void writeDouble(double d,byte[] bytes,int pos)
	{
		writeLong(Double.doubleToLongBits(d),bytes,pos);
	}
	/** д��һ��˫������ֵ���ֽ�������ָ��λ�ã���λ��ǰ����λ�ں� */
	public static void writeDouble_(double d,byte[] bytes,int pos)
	{
		writeLong_(Double.doubleToLongBits(d),bytes,pos);
	}
	/**
	 * д�붯̬���ȣ� ���ݴ�С���ö�̬���ȣ����������£����Ϊ512M��
	 * <li>1xxx xxxx��ʾ��0~0x80�� 0~128B��</li>
	 * <li>01xx xxxx xxxx xxxx��ʾ��0~0x4000��
	 * 0~16K��</li>
	 * <li>001x xxxx xxxx xxxx xxxx xxxx
	 * xxxx xxxx��ʾ��0~0x20000000�� 0~512M��</li>
	 */
	public static int writeLength(int len,byte[] bytes,int pos)
	{
		if(len>=0x20000000||len<0)
			throw new IllegalArgumentException(toString
				+" writeLength, invalid len:"+len);
		if(len>=0x4000)
		{
			writeInt(len+0x20000000,bytes,pos);
			return 4;
		}
		else if(len>=0x80)
		{
			writeShort((short)(len+0x4000),bytes,pos);
			return 2;
		}
		else
		{
			writeByte((byte)(len+0x80),bytes,pos);
			return 1;
		}
	}
	/** ��ָ�����ֽ�����ת��ΪISO-8859-1��ʽ���ַ��� */
	public static String readISO8859_1(byte[] data)
	{
		return readISO8859_1(data,0,data.length);
	}
	/** ��ָ�����ֽ�����ת��ΪISO-8859-1��ʽ���ַ��� */
	public static String readISO8859_1(byte[] data,int pos,int len)
	{
		char[] array=new char[len];
		for(int i=pos+len-1,j=array.length-1;i>=pos;i--,j--)
			array[j]=(char)data[i];
		return new String(array);
	}
	/** ��ָ�����ַ���ת��ΪISO-8859-1��ʽ���ֽ����� */
	public static byte[] writeISO8859_1(String str)
	{
		return writeISO8859_1(str,0,str.length());
	}
	/** ��ָ�����ַ���ת��ΪISO-8859-1��ʽ���ֽ����� */
	public static byte[] writeISO8859_1(String str,int index,int len)
	{
		byte[] data=new byte[len];
		writeISO8859_1(str,index,len,data,0);
		return data;
	}
	/** ��ָ�����ַ���ת��ΪISO-8859-1��ʽ���ֽ����� */
	public static void writeISO8859_1(String str,int index,int len,
		byte[] data,int pos)
	{
		int c;
		for(int i=index+len-1,j=pos+len-1;i>=index;i--,j--)
		{
			c=str.charAt(i);
			data[j]=(c>256)?63:(byte)c;
		}
	}
	/** ��ָ�����ַ�����ת��ΪISO-8859-1��ʽ���ֽ����� */
	public static void writeISO8859_1(char[] chars,int index,int len,
		byte[] data,int pos)
	{
		int c;
		for(int i=index+len-1,j=pos+len-1;i>=index;i--,j--)
		{
			c=chars[i];
			data[j]=(c>256)?63:(byte)c;
		}
	}
	/** ��ָ����UTF8��ʽ���ֽ�����ת��Ϊ�ַ���������null��ʾʧ�� */
	public static String readUTF(byte[] data)
	{
		char[] array=new char[data.length];
		int n=readUTF(data,0,data.length,array);
		return (n>=0)?new String(array,0,n):null;
	}
	/** ��ָ����UTF8��ʽ���ֽ�����ת��Ϊ�ַ���������null��ʾʧ�� */
	public static String readUTF(byte[] data,int pos,int length)
	{
		char[] array=new char[length];
		int n=readUTF(data,pos,length,array);
		return (n>=0)?new String(array,0,n):null;
	}
	/**
	 * ��ָ����UTF8��ʽ���ֽ�����ת��Ϊ�ַ�����
	 * ���ض������ַ�����-1��ʾʧ��
	 */
	public static int readUTF(byte[] data,int pos,int length,char[] array)
	{
		int i,c,cc,ccc;
		int n=0,end=pos+length;
		while(pos<end)
		{
			c=data[pos]&0xff;
			i=c>>4;
			if(i<8)
			{
				// 0xxx xxxx
				pos++;
				array[n++]=(char)c;
			}
			else if(i==12||i==13)
			{
				// 110x xxxx 10xx xxxx
				pos+=2;
				if(pos>end) return -1;
				cc=data[pos-1];
				if((cc&0xC0)!=0x80) return -1;
				array[n++]=(char)(((c&0x1f)<<6)|(cc&0x3f));
			}
			else if(i==14)
			{
				// 1110 xxxx 10xx xxxx 10xx
				// xxxx
				pos+=3;
				if(pos>end) return -1;
				cc=data[pos-2];
				ccc=data[pos-1];
				if(((cc&0xC0)!=0x80)||((ccc&0xC0)!=0x80)) return -1;
				array[n++]=(char)(((c&0x0f)<<12)|((cc&0x3f)<<6)|(ccc&0x3f));
			}
			else
				// 10xx xxxx 1111 xxxx
				return -1;
		}
		return n;
	}
	/** ���ָ�����ַ���ת��ΪUTF8��ʽ���ֽ����ݵĳ��� */
	public static int getUTFLength(String str,int index,int len)
	{
		int utfLen=0;
		int c;
		for(int i=index;i<len;i++)
		{
			c=str.charAt(i);
			if((c>=0x0001)&&(c<=0x007f))
				utfLen++;
			else if(c>0x07ff)
				utfLen+=3;
			else
				utfLen+=2;
		}
		return utfLen;
	}
	/** ���ָ�����ַ�����ת��ΪUTF8��ʽ���ֽ����ݵĳ��� */
	public static int getUTFLength(char[] chars,int index,int len)
	{
		int utfLen=0;
		int c;
		for(int i=index;i<len;i++)
		{
			c=chars[i];
			if((c>=0x0001)&&(c<=0x007f))
				utfLen++;
			else if(c>0x07ff)
				utfLen+=3;
			else
				utfLen+=2;
		}
		return utfLen;
	}
	/** ��ָ�����ַ���ת��ΪUTF8��ʽ���ֽ����� */
	public static byte[] writeUTF(String str)
	{
		return writeUTF(str,0,str.length());
	}
	/** ��ָ�����ַ���ת��ΪUTF8��ʽ���ֽ����� */
	public static byte[] writeUTF(String str,int index,int len)
	{
		byte[] data=new byte[getUTFLength(str,index,len)];
		writeUTF(str,index,len,data,0);
		return data;
	}
	/** ��ָ�����ַ���ת��ΪUTF8��ʽ���ֽ����� */
	public static void writeUTF(String str,int index,int len,byte[] data,
		int pos)
	{
		int c;
		for(int i=index;i<len;i++)
		{
			c=str.charAt(i);
			if((c>=0x0001)&&(c<=0x007f))
			{
				data[pos++]=(byte)c;
			}
			else if(c>0x07ff)
			{
				data[pos++]=(byte)(0xe0|((c>>12)&0x0f));
				data[pos++]=(byte)(0x80|((c>>6)&0x3f));
				data[pos++]=(byte)(0x80|(c&0x3f));
			}
			else
			{
				data[pos++]=(byte)(0xc0|((c>>6)&0x1f));
				data[pos++]=(byte)(0x80|(c&0x3f));
			}
		}
	}
	/** ��ָ�����ַ�����ת��ΪUTF8��ʽ���ֽ����� */
	public static void writeUTF(char[] chars,int index,int len,byte[] data,
		int pos)
	{
		int c;
		for(int i=index;i<len;i++)
		{
			c=chars[i];
			if((c>=0x0001)&&(c<=0x007f))
			{
				data[pos++]=(byte)c;
			}
			else if(c>0x07ff)
			{
				data[pos++]=(byte)(0xe0|((c>>12)&0x0f));
				data[pos++]=(byte)(0x80|((c>>6)&0x3f));
				data[pos++]=(byte)(0x80|(c&0x3f));
			}
			else
			{
				data[pos++]=(byte)(0xc0|((c>>6)&0x1f));
				data[pos++]=(byte)(0x80|(c&0x3f));
			}
		}
	}

	/* constructors */
	private ByteKit()
	{
	}

}