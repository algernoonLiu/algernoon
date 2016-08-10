/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zlib.math;

/**
 * ��˵������ѧ�⺯�� ���Ǻ����ļ��㹫ʽ��
 * sin(x)=x-x^3/3!+x^5/5!-x^7/7!+x^9/9!-����
 * cos(x)=1-x^2/2!+x^4/4!-x^6/6!+x^8/8!+����
 * arctan(x)=x-x^3/3+x^5/5-x^7/7+x^9/9-����
 * arcsin(x)=x+1*x^3/2*3+1*3*x^5/2*4*5+1*3*5*x^7/2*4*6*7+����
 * 
 * @version 1.0
 * @author hy
 */

public final class MathKit
{

	/* static fields */
	/** ����Ϣ */
	public static final String toString=MathKit.class.getName();
	/** ˫����������С���� */
	public static final double DOUBLE_EPSILON=2.2204460492503131E-016D;
	/** ����������С���� */
	public static final float FLOAT_EPSILON=1.192093E-007F;
	/** Բ���� */
	public static final float PI=3.141592653589793238462643383279f;
	/**
	 * ŷ������Euler number������Ȼ�����ĵ�����
	 * ��n->��ʱ��(1+1/n)^n�ļ��ޣ���1/n!�ļ��ޡ�
	 */
	public static final float E=2.718281828459045235360287471352f;
	/** �ƽ�ָ��ʣ�Golden Section��((����5)-1)/2 */
	public static final float GS=1.618033988749894848204586834365f;
	/** �뱶Բ���� */
	public static final float HALF_PI=0.5f*PI;
	/** ˫��Բ���� */
	public static final float TWO_PI=2.0f*PI;
	/** Բ���ʵ��� */
	public static final float INV_PI=1.0f/PI;
	/** ˫��Բ���ʵ��� */
	public static final float INV_TWO_PI=1.0f/TWO_PI;
	/** �ǶȶԻ��ȵĶһ��� */
	public static final float DEG_TO_RAD=PI/180f;
	/** ���ȶԽǶȵĶһ��� */
	public static final float RAD_TO_DEG=180f/PI;

	/** �����׼��� */
	public static final float STANDARD_ERROR=0.00001f;

	/** ����Բ���ʣ��Ŵ�һ�ڱ� */
	public static final int PI_INT=314159265;
	/** �뱶����Բ���ʣ��Ŵ�һ�ڱ� */
	public static final int HALF_PI_INT=157079633;
	/** ˫������Բ���ʣ��Ŵ�һ�ڱ� */
	public static final int TWO_PI_INT=628318531;

	/** ����������� */
	private static Random random=new Random1();

	/* static methods */
	/** ��õ�ǰ������������� */
	public static Random getRandom()
	{
		return random;
	}
	/** ���õ�ǰ������������� */
	public static void setRandom(Random r)
	{
		random=r;
	}
	/** ������������ */
	public static int randomInt()
	{
		return random.randomInt();
	}
	/** ����������������Χ0��1֮�� */
	public static float randomFloat()
	{
		return random.randomFloat();
	}
	/** ���ָ����Χ��������� */
	public static int randomValue(int v1,int v2)
	{
		return random.randomValue(v1,v2);
	}
	/** ���ָ����Χ����������� */
	public static float randomValue(float v1,float v2)
	{
		return random.randomValue(v1,v2);
	}
	/** ���������㷨 */
	public static long sqrt(long x)
	{
		if(x<=0) return 0;
		if(x<=3) return 1;
		long a=0,b=x,m=x>>1,r=0;
		if(m>0xB504F333L) m=0xB504F333L;
		while(m>a)
		{
			r=m*m;
			if(r>x)
				b=m;
			else if(r<x)
				a=m;
			else
				break;
			m=(b+a)>>1;
		}
		return m;
	}
	/** ���������㷨������Ϊ���ȣ��Ŵ�10000�� */
	public static long sin(long x)
	{
		x=(x*10000)%TWO_PI_INT;
		if(x<0) x=x+TWO_PI_INT;
		if(x>PI_INT)
		{
			x=TWO_PI_INT-x;
			if(x>HALF_PI_INT) return -cos_((x-HALF_PI_INT)/10000);
			return -cos_((HALF_PI_INT-x)/10000);
		}
		if(x>HALF_PI_INT) return cos_((x-HALF_PI_INT)/10000);
		return cos_((HALF_PI_INT-x)/10000);
	}
	/** ���������㷨������Ϊ���ȣ��Ŵ�10000�� */
	public static long cos(long x)
	{
		x=(x*10000)%TWO_PI_INT;
		if(x<0) x=x+TWO_PI_INT;
		if(x>PI_INT) x=TWO_PI_INT-x;
		if(x>HALF_PI_INT) return -cos_((PI_INT-x)/10000);
		return cos_(x/10000);
	}
	/** ���������㷨������Ϊ���ȣ��Ŵ�10000��0��HALF_PI_INT/10000�� */
	private static long cos_(long x)
	{
		long xx=x*x;
		long xxx=xx*xx;
		return (10000000000000000L-50000000L*xx+xxx/24)/1000000000000L
			-(xxx/1000000000000L)*xx/72000000000L;
	}
	/* ���η��� */
	/** �ж��Ƿ����ָ�������� */
	public static boolean rectContain(float[] rect,float x,float y)
	{
		return (x>=rect[0]&&y>=rect[1]&&rect[2]>x&&rect[3]>y);
	}
	/**
	 * �߶��ཻ�ж��� ����ȷ��b1>=a1,b2>=a2��
	 * ����ֵ��1Ϊ��ȫ������2Ϊ��ȣ�3Ϊ��ȫ��������-1Ϊ��ȫ��������0Ϊ�ཻ��
	 */
	public static int lineCross(float a1,float b1,float a2,float b2)
	{
		if(a2>a1)
		{
			if(a2>=b1) return -1;
			if(b2<=b1) return 1;
			return 0;
		}
		else if(a2<a1)
		{
			if(b2<=a1) return -1;
			if(b2>=b1) return 3;
			return 0;
		}
		else
		{
			if(b2<b1) return 1;
			if(b2==b1) return 2;
			return 3;
		}
	}
	/**
	 * ����1�;���2���ཻ�ж��� ����ȷ��x1<x2,y1<y2,x3<x4,y3<y4��
	 * ����ֵ��1Ϊ��ȫ������2Ϊ��ȣ�3Ϊ��ȫ��������-1Ϊ��ȫ��������0Ϊ�ཻ��
	 */
	public static int rectCross(float x1,float y1,float x2,float y2,float x3,float y3,
		float x4,float y4)
	{
		int r1=lineCross(x1,x2,x3,x4);
		int r2=lineCross(y1,y2,y3,y4);
		if(r1<0||r2<0) return -1;
		if(r1==1&&r2==1) return 1;
		if(r1==2&&r2==2) return 2;
		if(r1==3&&r2==3) return 3;
		return 0;
	}
	/** ���μ��з��� */
	public static void rectIntersection(float[] rect,float x1,float y1,float x2,
		float y2)
	{
		if(rect[0]<x1) rect[0]=x1;
		if(rect[2]>x2) rect[2]=x2;
		if(rect[1]<y1) rect[1]=y1;
		if(rect[3]>y2) rect[3]=y2;
	}
	/** �������Ϸ��� */
	public static void rectUnion(float[] rect,float x1,float y1,float x2,float y2)
	{
		if(rect[0]>x1) rect[0]=x1;
		if(rect[2]<x2) rect[2]=x2;
		if(rect[1]>y1) rect[1]=y1;
		if(rect[3]<y2) rect[3]=y2;
	}
	/**
	 * ����p����p1��p2�����ɵ������ϵķ���
	 * ����ָ��p1Ϊԭ�㣬˳ʱ����ת����ʱ����ת�ܸ����ɨ��p��
	 * 
	 * @param x,y����ʾָ��p��
	 * @param x1,y1,x2,y2����ʾp1p2�������ɵ�ֱ��
	 * @return -1��ʾ˳ʱ�룬0��ʾ��ֱ���ϣ�1��ʾ��ʱ��
	 */
	public static int getDirection(double x,double y,double x1,double y1,
		double x2,double y2)
	{
		// ƽ����Y��
		if(x1==x2)
		{
			if(y1==y2)
				throw new IllegalArgumentException(toString
					+" getDirection, no line, "+x1+":"+y1);
			if(x>x1) return (y1>y2)?1:-1;
			if(x<x1) return (y2>y1)?1:-1;
			return 0;
		}
		// ԭ��һ��ֱ�߽�ƽ��ֳ�����2�����֣��Ѿ��ų���ƽ����Y����������
		// y-y1>(y2-y1)*(x-x1)/(x2-x1)
		// ��ʾ�õ���ֱ�ߵ��Ϸ��������߶��ԣ��Ϳ���ȷ����ת������
		double t1=(y-y1)*(x2-x1);
		double t2=(y2-y1)*(x-x1);
		if(t1>t2) return 1;
		if(t1<t2) return -1;
		return 0;
	}
	/**
	 * ����p����p1��p2�����ɵ��߶��ϵĴ��߽��㣬
	 * 
	 * @param p����ʾָ��p��
	 * @param x1,y1,x2,y2����ʾp1p2�������ɵ��߶�
	 * @return -1��ʾ�������߶��⣬0��ʾ�����ڶ˵��ϣ�1��ʾ�������߶���
	 */
	public static int letFallIntersection(double[] p,double x1,double y1,
		double x2,double y2)
	{
		double x=p[0],y=p[1];
		// ƽ����Y��
		if(x1==x2)
		{
			if(y1==y2)
				throw new IllegalArgumentException(toString
					+" letFallIntersection, no line, "+x1+":"+y1);
			p[0]=x1;
			p[1]=y;
			if(y==y1||y==y2) return 0;
			if(y2>y1) return (y<y2&&y>y1)?1:-1;
			return (y<y1&&y>y2)?1:-1;
		}
		double k=(y2-y1)/(x2-x1);
		double kk=k*k;
		double xx=(kk*x1+k*(y-y1)+x)/(kk+1);
		double yy=k*(xx-x1)+y1;
		p[0]=xx;
		p[1]=yy;
		if(yy==y1||yy==y2) return 0;
		if(y2>y1) return (yy<y2&&yy>y1)?1:-1;
		return (yy<y1&&yy>y2)?1:-1;
	}
	/**
	 * ���߶�֮��Ľ��㣬
	 * 
	 * @param ax,ay,bx,by����ʾab�������ɵ��߶�
	 * @param line����ʾָ���߶�
	 * @param force����ʾ��������ཻ���Ƿ�ǿ����⽻��
	 * @return ����-3��ʾƽ���޽��㣬 ����-2��ʾͬ���޽��㣬
	 *         ����-1��ʾ�����ཻ��ֱ�߽�����line�����У���
	 *         ����0��ʾ���ߣ������������line�����У���
	 *         ����1��ʾ�ཻ��������line�����У�
	 */
	public static int lineIntersect(double ax,double ay,double bx,double by,
		double[] line,boolean force)
	{
		double cx=line[0],cy=line[1],dx=line[2],dy=line[3];
		double x1,y1,x2,y2,x3,y3;
		if(!force)
		{
			// ���о��μ����ж�
			if(cx>dx)
			{
				x1=dx;
				x2=ax;
			}
			else
			{
				x1=cx;
				x2=dx;
			}
			if(cy>dy)
			{
				y1=cy;
				y2=dy;
			}
			else
			{
				y1=dy;
				y2=cy;
			}
			double x4,y4;
			if(ax>bx)
			{
				x3=bx;
				x4=ax;
			}
			else
			{
				x3=ax;
				x4=bx;
			}
			if(ay>by)
			{
				y3=ay;
				y4=by;
			}
			else
			{
				y3=by;
				y4=ay;
			}
			if(x3<x1) x3=x1;
			if(x4>x2) x4=x2;
			if(x3>x4) return -1;
			if(y3>y1) y3=y1;
			if(y4<y2) y4=y2;
			if(y3>y4) return -1;
		}
		x1=dx-cx;
		y1=dy-cy;
		x2=bx-ax;
		y2=by-ay;
		// �߶ο����ж�����Ҫ�ж��Ƿ��໥����
		// ����ʸ��������ж�c���d�������b��ķ���
		// ���b*c>0��ʾc��b����ʱ�뷽��b*c<0��ʾc��b��˳ʱ�뷽��b*c=0��ʾ����
		// x3=x1*(cy-ay)-(cx-ax)*y1;
		// y3=x1*(dy-ay)-(dx-ax)*y1;
		// if(x3>0&&y3>0) return -1;
		// if(x3<0&&y3<0) return -1;
		// x3=x2*(ay-cy)-(ax-cx)*y2;
		// y3=x2*(by-cy)-(bx-cx)*y2;
		// if(x3>0&&y3>0) return -1;
		// if(x3<0&&y3<0) return -1;

		// ����������ཻ��
		// ƽ����y��
		if(x1==0)
		{
			if(x2==0)
			{
				if(cx!=ax) return -3;
				// ���ߡ�ͬ��
				line[0]=cx;
				line[2]=cx;
				if(cy<dy)
				{
					y3=cy;
					cy=dy;
					dy=y3;
				}
				if(ay<by)
				{
					y3=ay;
					ay=by;
					by=y3;
				}
				line[1]=(ay>cy)?cy:ay;
				line[3]=(by>dy)?by:dy;
				if(line[1]<line[3]) return -2;
				return 0;
			}
			line[0]=cx;
			line[1]=(cx-ax)*y2/x2+ay;
			// �жϽ����Ƿ����߶���
			if(cy<dy)
			{
				if(line[1]<cy||line[1]>dy) return -1;
			}
			else
			{
				if(line[1]>cy||line[1]<dy) return -1;
			}
			return 1;
		}
		if(x2==0)
		{
			line[0]=ax;
			line[1]=(ax-cx)*y1/x1+cy;
			// �жϽ����Ƿ����߶���
			if(ay<by)
			{
				if(line[1]<ay||line[1]>by) return -1;
			}
			else
			{
				if(line[1]>ay||line[1]<by) return -1;
			}
			return 1;
		}
		// ƽ����x��
		if(y1==0)
		{
			if(y2==0)
			{
				if(cy!=ay) return -3;
				// ���ߡ�ͬ��
				line[1]=cy;
				line[3]=cy;
				if(cx>dx)
				{
					x3=cx;
					cx=dx;
					dx=x3;
				}
				if(ax>bx)
				{
					x3=ax;
					ax=bx;
					bx=x3;
				}
				line[0]=(ax<cx)?cx:ax;
				line[2]=(bx<dx)?bx:dx;
				if(line[0]>line[2]) return -2;
				return 0;
			}
			line[0]=(cy-ay)*x2/y2+ax;
			line[1]=cy;
			// �жϽ����Ƿ����߶���
			if(cx<dx)
			{
				if(line[0]<cx||line[0]>dx) return -1;
			}
			else
			{
				if(line[0]>cx||line[0]<dx) return -1;
			}
			return 1;
		}
		if(y2==0)
		{
			line[0]=(ay-cy)*x1/y1+cx;
			line[1]=ay;
			// �жϽ����Ƿ����߶���
			if(ax<bx)
			{
				if(line[0]<ax||line[0]>bx) return -1;
			}
			else
			{
				if(line[0]>ax||line[0]<bx) return -1;
			}
			return 1;
		}
		// б�ʾ������Ҳ�Ϊ0�����
		x3=y1/x1;
		y3=y2/x2;
		// б�����
		if(x3-y3<STANDARD_ERROR&&x3-y3>-STANDARD_ERROR)
		{
			// ƽ��
			x3=x3*(ax-cx)-ay+cy;
			if(x3>STANDARD_ERROR||x3<-STANDARD_ERROR) return -3;
			double x4,y4;
			// ���ߡ�ͬ��
			if(cx>dx)
			{
				x1=dx;
				y1=dy;
				x2=cx;
				y2=cy;
			}
			else
			{
				x1=cx;
				y1=cy;
				x2=dx;
				y2=dy;
			}
			if(ax>bx)
			{
				x3=bx;
				y3=by;
				x4=ax;
				y4=ay;
			}
			else
			{
				x3=ax;
				y3=ay;
				x4=bx;
				y4=by;
			}
			if(x3<x1)
			{
				line[0]=x1;
				line[1]=y1;
			}
			else
			{
				line[0]=x3;
				line[1]=y3;
			}
			if(x4<x2)
			{
				line[2]=x4;
				line[3]=y4;
			}
			else
			{
				line[2]=x2;
				line[3]=x2;
			}
			if(line[0]>line[2]) return -2;
			return 0;
		}
		line[0]=(x3*cx-y3*ax+ay-cy)/(x3-y3);
		line[1]=x3*(line[0]-cx)+cy;
		// �жϽ����Ƿ����߶���
		if(cx<dx)
		{
			if(line[0]<cx||line[0]>dx) return -1;
		}
		else
		{
			if(line[0]>cx||line[0]<dx) return -1;
		}
		if(ax<bx)
		{
			if(line[0]<ax||line[0]>bx) return -1;
		}
		else
		{
			if(line[0]>ax||line[0]<bx) return -1;
		}
		return 1;
	}

	/* constructors */
	private MathKit()
	{
	}

}