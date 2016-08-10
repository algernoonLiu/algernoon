/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zlib.math;

import zlib.text.CharBuffer;

/**
 * ��˵���������
 * 
 * @version 1.0
 * @author hy
 */

public class Polygon
{

	/* static fields */
	/** Ĭ�ϵĳ�ʼ������С */
	public static final int CAPACITY=10;

	/* fields */
	/** �������� */
	float[] array;
	/** ��������ĳ��� */
	int top;
	/** �������� */
	float[] bounds;

	/* constructors */
	/** ��Ĭ�ϵĴ�С����һ���ն���� */
	public Polygon()
	{
		this(CAPACITY);
	}
	/** ��ָ���Ĵ�С����һ���ն���� */
	public Polygon(int capacity)
	{
		if(capacity<1)
			throw new IllegalArgumentException(getClass().getName()
				+" <init>, invalid capatity:"+capacity);
		array=new float[capacity];
		top=0;
	}
	/** ��ָ���������鹹��һ������� */
	public Polygon(float[] array)
	{
		if(array==null)
			throw new IllegalArgumentException(getClass().getName()
				+" <init>, null array");
		if((array.length%2)!=0)
			throw new IllegalArgumentException(getClass().getName()
				+" <init>, invalid array length:"+array.length);
		this.array=array;
		top=array.length;
	}
	/** ��ָ����������ͳ��ȹ���һ������� */
	public Polygon(float[] array,int len)
	{
		if(array==null)
			throw new IllegalArgumentException(getClass().getName()
				+" <init>, null array");
		if(len<0||(len%2)!=0||len>array.length)
			throw new IllegalArgumentException(getClass().getName()
				+" <init>, invalid length:"+len);
		this.array=array;
		top=len;
	}
	/** ���ƹ��췽�� */
	public Polygon(Polygon p)
	{
		array=new float[p.array.length];
		System.arraycopy(p.array,0,array,0,p.top);
		top=p.top;
		if(p.bounds==null) return;
		bounds=new float[p.bounds.length];
		System.arraycopy(p.bounds,0,bounds,0,bounds.length);
	}
	/* properties */
	/** �õ�����εĳ��� */
	public int size()
	{
		return top;
	}
	/** �õ�����ε��ݻ� */
	public int capacity()
	{
		return array.length;
	}
	/** �ж϶�����Ƿ��ǿ� */
	public boolean isEmpty()
	{
		return top==0;
	}
	/** ��ñ߽���� */
	public float[] getBounds()
	{
		if(bounds==null)
		{
			bounds=new float[]{-1,-1,-1,-1};
			for(int i=0;i<top;i+=2)
				updateBounds(array[i],array[i+1]);
		}
		return bounds;
	}
	/** �õ�����ε��������飬һ��ʹ��toArray()���� */
	public float[] getArray()
	{
		return array;
	}
	/* methods */
	/** ���ö���ε��ݻ���ֻ�������ݻ� */
	public void setCapacity(int len)
	{
		int c=array.length;
		if(len<=c) return;
		for(;c<len;c=(c<<1)+1)
			;
		float[] temp=new float[c];
		System.arraycopy(array,0,temp,0,top);
		array=temp;
	}
	/** ���ָ�����궥���ƫ���� */
	int indexOf(float x,float y)
	{
		for(int i=0;i<top;i+=2)
			if(x==array[i]&&y==array[i+1]) return i;
		return -1;
	}
	/** ���ָ������Ķ��� */
	public void add(float x,float y)
	{
		if(array.length<top+2) setCapacity(top+CAPACITY);
		array[top++]=x;
		array[top++]=y;
		if(bounds!=null) updateBounds(x,y);
	}
	/** �Ƴ�ָ������Ķ��� */
	public boolean remove(float x,float y)
	{
		int i=indexOf(x,y);
		if(i<0) return false;
		array[i+1]=array[--top];
		array[i]=array[--top];
		bounds=null;
		return true;
	}
	/**
	 * �ж϶�����Ƿ����ָ�����꣬n��ʾ�ܱ�����
	 * ����0��n-1��ʾ�ڱ��ϣ�0��ʾ��һ���˵�����һ���˵�ıߣ���
	 * ����n��ʾ����������-1��ʾ��������
	 */
	public int contain(float x,float y)
	{
		if(bounds!=null)
		{
			if(x<bounds[0]||y<bounds[1]) return -1;
			if(bounds[2]+bounds[0]<=x||bounds[3]+bounds[1]<=y) return -1;
		}
		int hits=0;
		float lastx=array[top-2],lasty=array[top-1];
		float curx,cury,leftx;
		float tx,ty;
		for(int i=0;i<top;lastx=curx,lasty=cury,i+=2)
		{
			curx=array[i];
			cury=array[i+1];
			if(x==curx&&y==cury) return i>>1;
			//ˮƽ��
			if(cury==lasty)
			{
				if(y==cury)
				{
					if(curx<lastx)
					{
						if(x<=lastx&&x>=curx) return i>>1;
					}
					else
					{
						if(x>=lastx&&x<=curx) return i>>1;
					}
				}
				continue;
			}
			//ȥ����ߵ��߶�
			if(curx<lastx)
			{
				if(x>lastx) continue;
				leftx=curx;
			}
			else
			{
				if(x>curx) continue;
				leftx=lastx;
			}
			if(cury<lasty)
			{
				//ȥ�����ཻ�ģ������������С�Ķ˵�
				if(y<cury||y>=lasty) continue;
				if(x<leftx)
				{
					hits++;
					continue;
				}
				ty=y-cury;
			}
			else
			{
				if(y<lasty||y>=cury) continue;
				if(x<leftx)
				{
					hits++;
					continue;
				}
				ty=y-cury;
			}
			tx=ty*(lastx-curx)/(lasty-cury)+curx-x;
			if(tx<MathKit.STANDARD_ERROR&&tx>-MathKit.STANDARD_ERROR)
				return i>>1;
			if(tx>0) hits++;
		}
		return ((hits&1)!=0)?top>>1:-1;
	}
	/** ת������ */
	public void translate(float x,float y)
	{
		for(int i=0;i<top;i+=2)
		{
			array[i]+=x;
			array[i+1]+=y;
		}
		if(bounds==null) return;
		bounds[0]+=x;
		bounds[2]+=x;
		bounds[1]+=y;
		bounds[3]+=y;
	}
	/** ��չ�߽���� */
	void updateBounds(float x,float y)
	{
		if(x<bounds[0]||bounds[0]<0)
			bounds[0]=x;
		if(x>bounds[2])
			bounds[2]=x;
		if(y<bounds[1]||bounds[1]<0)
			bounds[1]=y;
		if(y>bounds[3])
			bounds[3]=y;
	}
	/** �������� */
	public void clear()
	{
		top=0;
		bounds=null;
	}
	/* common methods */
	public String toString()
	{
		CharBuffer cb=new CharBuffer(super.toString());
		cb.append('[');
		for(int i=0;i<top;i+=2)
		{
			cb.append(array[i]).append(',');
			cb.append(array[i+1]).append(' ');
		}
		if(top>0) cb.setTop(cb.top()-1);
		cb.append(']');
		return cb.getString();
	}

}