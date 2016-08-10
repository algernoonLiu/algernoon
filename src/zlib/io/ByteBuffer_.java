/**
 * Coyyright 2001 by seasky <www.seasky.cn>.
 */

package zlib.io;

/**
 * ��˵�����ֽڻ����࣬�ֽڲ�����λ��ǰ����λ�ں�
 * 
 * @version 1.0
 * @author hy
 */

public final class ByteBuffer_ extends ByteBuffer
{

	/* constructors */
	/** ��Ĭ�ϵĴ�С����һ���ֽڻ������ */
	public ByteBuffer_()
	{
		this(CAPACITY);
	}
	/** ��ָ���Ĵ�С����һ���ֽڻ������ */
	public ByteBuffer_(int capacity)
	{
		super(capacity);
	}
	/** ��ָ�����ֽ����鹹��һ���ֽڻ������ */
	public ByteBuffer_(byte[] data)
	{
		super(data);
	}
	/** ��ָ�����ֽ����鹹��һ���ֽڻ������ */
	public ByteBuffer_(byte[] data,int index,int length)
	{
		super(data,index,length);
	}
	/* read methods */
	/** ����һ���޷��ŵĶ�������ֵ */
	public int readUnsignedShort()
	{
		int pos=offset;
		offset+=2;
		return ((array[pos+1]&0xff)<<8)+(array[pos]&0xff);
	}
	/** ����һ��������ֵ */
	public int readInt()
	{
		int pos=offset;
		offset+=4;
		return ((array[pos+3]&0xff)<<24)+((array[pos+2]&0xff)<<16)
			+((array[pos+1]&0xff)<<8)+(array[pos]&0xff);
	}
	/** ����һ����������ֵ */
	public long readLong()
	{
		int pos=offset;
		offset+=8;
		return ((array[pos+7]&0xffL)<<56)+((array[pos+6]&0xffL)<<48)
			+((array[pos+5]&0xffL)<<40)+((array[pos+4]&0xffL)<<32)
			+((array[pos+3]&0xffL)<<24)+((array[pos+2]&0xffL)<<16)
			+((array[pos+1]&0xffL)<<8)+(array[pos]&0xffL);
	}
	/* write methods */
	/** д��һ����������ֵ */
	public void writeShort(int s)
	{
		int pos=top;
		if(array.length<pos+2) setCapacity(pos+CAPACITY);
		array[pos]=(byte)s;
		array[pos+1]=(byte)(s>>>8);
		top+=2;
	}
	/** д��һ��������ֵ */
	public void writeInt(int i)
	{
		int pos=top;
		if(array.length<pos+4) setCapacity(pos+CAPACITY);
		array[pos]=(byte)i;
		array[pos+1]=(byte)(i>>>8);
		array[pos+2]=(byte)(i>>>16);
		array[pos+3]=(byte)(i>>>24);
		top+=4;
	}
	/** д��һ����������ֵ */
	public void writeLong(long l)
	{
		int pos=top;
		if(array.length<pos+8) setCapacity(pos+CAPACITY);
		array[pos]=(byte)l;
		array[pos+1]=(byte)(l>>>8);
		array[pos+2]=(byte)(l>>>16);
		array[pos+3]=(byte)(l>>>24);
		array[pos+4]=(byte)(l>>>32);
		array[pos+5]=(byte)(l>>>40);
		array[pos+6]=(byte)(l>>>48);
		array[pos+7]=(byte)(l>>>56);
		top+=8;
	}
	/** ����Ƿ�Ϊ��ͬ���͵�ʵ�� */
	public boolean checkClass(Object obj)
	{
		return (obj instanceof ByteBuffer_);
	}

}