/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zlib.math;

/**
 * ��˵��������������������ñ���ͬ���㷨��
 * 
 * @version 1.0
 * @author hy
 */

public final class Random1 extends Random
{

	/* static fields */
	private static final int A=16807;
	private static final int M=2147483647;
	private static final int Q=127773;
	private static final int R=2836;
	private static final int MASK=123459876;

	/* constructors */
	/** �Ե�ǰ��ϵͳʱ����Ϊ���ӹ�������������� */
	public Random1()
	{
		super();
	}
	/** ��ָ�������ӹ�������������� */
	public Random1(int seed)
	{
		super(seed);
	}

	/* methods */
	/** ������������ */
	public int randomInt()
	{
		int r=seed;
		// ��ֹ����Ϊ0
		r^=MASK;
		int k=r/Q;
		r=A*(r-k*Q)-R*k;
		if(r<0) r+=M;
		seed=r;
		return r;
	}

}