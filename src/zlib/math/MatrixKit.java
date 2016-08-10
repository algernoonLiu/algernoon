/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zlib.math;

import zlib.text.CharBuffer;

/**
 * ��˵��������⺯��
 * 
 * @version 1.0
 * @author hy
 */

public final class MatrixKit
{

	/* static methods */
	/* double static methods */
	/** ��þ����ָ���� */
	public static double[] getRow(double[][] matrix,int row)
	{
		double[] array=new double[matrix.length];
		System.arraycopy(matrix[row],0,array,0,array.length);
		return array;
	}
	/** ��þ����ָ���� */
	public static double[] getColumn(double[][] matrix,int col)
	{
		double[] array=new double[matrix[0].length];
		for(int i=array.length;i>=0;i--)
			array[i]=matrix[i][col];
		return array;
	}
	/** ת�÷��� */
	public static double[][] transpose(double[][] matrix)
	{
		double[][] result=new double[matrix[0].length][matrix.length];
		for(int r=matrix.length-1;r>=0;r--)
		{
			for(int c=matrix[0].length-1;c>=0;c--)
				result[c][r]=matrix[r][c];
		}
		return result;
	}
	/* �������㣬Ҫ�����������������ͬ������������ */
	/** �ӷ�������matrix1����������� */
	public static void add(double[][] matrix1,double[][] matrix2)
	{
		for(int r=matrix1.length-1;r>=0;r--)
		{
			for(int c=matrix1[0].length-1;c>=0;c--)
				matrix1[r][c]+=matrix2[r][c];
		}
	}
	/** ����������matrix1����������� */
	public static void sub(double[][] matrix1,double[][] matrix2)
	{
		for(int r=matrix1.length-1;r>=0;r--)
		{
			for(int c=matrix1[0].length-1;c>=0;c--)
				matrix1[r][c]-=matrix2[r][c];
		}
	}
	/** �˷�������matrix1����������� */
	public static void mul(double[][] matrix1,double[][] matrix2)
	{
		for(int r=matrix1.length-1;r>=0;r--)
		{
			for(int c=matrix1[0].length-1;c>=0;c--)
				matrix1[r][c]*=matrix2[r][c];
		}
	}
	/** ����������matrix1����������� */
	public static void div(double[][] matrix1,double[][] matrix2)
	{
		for(int r=matrix1.length-1;r>=0;r--)
		{
			for(int c=matrix1[0].length-1;c>=0;c--)
				matrix1[r][c]/=matrix2[r][c];
		}
	}
	/* ��������(scalar operations) */
	/** �ӷ�������matrix����������� */
	public static void add(double[][] matrix,double d)
	{
		for(int r=matrix.length-1;r>=0;r--)
		{
			for(int c=matrix[0].length-1;c>=0;c--)
				matrix[r][c]+=d;
		}
	}
	/** ����������matrix����������� */
	public static void sub(double[][] matrix,double d)
	{
		for(int r=matrix.length-1;r>=0;r--)
		{
			for(int c=matrix[0].length-1;c>=0;c--)
				matrix[r][c]-=d;
		}
	}
	/** �˷�������matrix����������� */
	public static void mul(double[][] matrix,double d)
	{
		for(int r=matrix.length-1;r>=0;r--)
		{
			for(int c=matrix[0].length-1;c>=0;c--)
				matrix[r][c]*=d;
		}
	}
	/** ����������matrix����������� */
	public static void div(double[][] matrix,double d)
	{
		for(int r=matrix.length-1;r>=0;r--)
		{
			for(int c=matrix[0].length-1;c>=0;c--)
				matrix[r][c]/=d;
		}
	}
	/**
	 * ����˷����㣬Ҫ�����A������������ھ���B��������
	 * ���A=[m][n],B=[n][p],C=A*B����C=[m][p]��
	 * i=1~m,k=1~p,j=1~n,��C[i][k]Ԫ�ص���A[i][j]*B[j][k]�ж�j���ۼӡ�
	 */
	public static double[][] mulMatrix(double[][] matrix1,double[][] matrix2)
	{
		double[][] result=new double[matrix1.length][matrix2[0].length];
		for(int r=matrix1.length-1;r>=0;r--)
		{
			for(int c=matrix2[0].length-1;c>=0;c--)
			{
				for(int i=matrix1[0].length-1;i>=0;i--)
				{
					result[r][c]+=matrix1[r][i]*matrix2[i][c];
				}
			}
		}
		return result;
	}
	/** ��������ֱ�﷽�� */
	public static String toString(double[][] matrix)
	{
		if(matrix.length==0) return "matrix=[]";
		if(matrix[0].length==0) return "matrix=["+matrix.length+"][]";
		CharBuffer cb=new CharBuffer();
		cb.append("matrix=");
		cb.append('[').append(matrix.length).append(']');
		cb.append('[').append(matrix[0].length).append(']');
		cb.append('\n');
		for(int r=0;r<matrix.length;r++)
		{
			cb.append('[');
			for(int c=0;c<matrix[0].length;c++)
				cb.append(matrix[r][c]).append(' ');
			cb.setTop(cb.top()-1);
			cb.append(']').append('\n');
		}
		return cb.getString();
	}

	/* float static methods */
	/** ��þ����ָ���� */
	public static float[] getRow(float[][] matrix,int row)
	{
		float[] array=new float[matrix.length];
		System.arraycopy(matrix[row],0,array,0,array.length);
		return array;
	}
	/** ��þ����ָ���� */
	public static float[] getColumn(float[][] matrix,int col)
	{
		float[] array=new float[matrix[0].length];
		for(int i=array.length;i>=0;i--)
			array[i]=matrix[i][col];
		return array;
	}
	/** ת�÷��� */
	public static float[][] transpose(float[][] matrix)
	{
		float[][] result=new float[matrix[0].length][matrix.length];
		for(int r=matrix.length-1;r>=0;r--)
		{
			for(int c=matrix[0].length-1;c>=0;c--)
				result[c][r]=matrix[r][c];
		}
		return result;
	}
	/* �������㣬Ҫ�����������������ͬ������������ */
	/** �ӷ�������matrix1����������� */
	public static void add(float[][] matrix1,float[][] matrix2)
	{
		for(int r=matrix1.length-1;r>=0;r--)
		{
			for(int c=matrix1[0].length-1;c>=0;c--)
				matrix1[r][c]+=matrix2[r][c];
		}
	}
	/** ����������matrix1����������� */
	public static void sub(float[][] matrix1,float[][] matrix2)
	{
		for(int r=matrix1.length-1;r>=0;r--)
		{
			for(int c=matrix1[0].length-1;c>=0;c--)
				matrix1[r][c]-=matrix2[r][c];
		}
	}
	/** �˷�������matrix1����������� */
	public static void mul(float[][] matrix1,float[][] matrix2)
	{
		for(int r=matrix1.length-1;r>=0;r--)
		{
			for(int c=matrix1[0].length-1;c>=0;c--)
				matrix1[r][c]*=matrix2[r][c];
		}
	}
	/** ����������matrix1����������� */
	public static void div(float[][] matrix1,float[][] matrix2)
	{
		for(int r=matrix1.length-1;r>=0;r--)
		{
			for(int c=matrix1[0].length-1;c>=0;c--)
				matrix1[r][c]/=matrix2[r][c];
		}
	}
	/* ��������(scalar operations) */
	/** �ӷ�������matrix����������� */
	public static void add(float[][] matrix,float f)
	{
		for(int r=matrix.length-1;r>=0;r--)
		{
			for(int c=matrix[0].length-1;c>=0;c--)
				matrix[r][c]+=f;
		}
	}
	/** ����������matrix����������� */
	public static void sub(float[][] matrix,float f)
	{
		for(int r=matrix.length-1;r>=0;r--)
		{
			for(int c=matrix[0].length-1;c>=0;c--)
				matrix[r][c]-=f;
		}
	}
	/** �˷�������matrix����������� */
	public static void mul(float[][] matrix,float f)
	{
		for(int r=matrix.length-1;r>=0;r--)
		{
			for(int c=matrix[0].length-1;c>=0;c--)
				matrix[r][c]*=f;
		}
	}
	/** ����������matrix����������� */
	public static void div(float[][] matrix,float f)
	{
		for(int r=matrix.length-1;r>=0;r--)
		{
			for(int c=matrix[0].length-1;c>=0;c--)
				matrix[r][c]/=f;
		}
	}
	/**
	 * ����˷����㣬Ҫ�����A������������ھ���B��������
	 * ���A=[m][n],B=[n][p],C=A*B����C=[m][p]��
	 * i=1~m,k=1~p,j=1~n,��C[i][k]Ԫ�ص���A[i][j]*B[j][k]�ж�j���ۼӡ�
	 */
	public static float[][] mulMatrix(float[][] matrix1,float[][] matrix2)
	{
		float[][] result=new float[matrix1.length][matrix2[0].length];
		for(int r=matrix1.length-1;r>=0;r--)
		{
			for(int c=matrix2[0].length-1;c>=0;c--)
			{
				for(int i=matrix1[0].length-1;i>=0;i--)
				{
					result[r][c]+=matrix1[r][i]*matrix2[i][c];
				}
			}
		}
		return result;
	}
	/** ��������ֱ�﷽�� */
	public static String toString(float[][] matrix)
	{
		if(matrix.length==0) return "matrix=[]";
		if(matrix[0].length==0) return "matrix=["+matrix.length+"][]";
		CharBuffer cb=new CharBuffer();
		cb.append("matrix=");
		cb.append('[').append(matrix.length).append(']');
		cb.append('[').append(matrix[0].length).append(']');
		cb.append('\n');
		for(int r=0;r<matrix.length;r++)
		{
			cb.append('[');
			for(int c=0;c<matrix[0].length;c++)
				cb.append(matrix[r][c]).append(' ');
			cb.setTop(cb.top()-1);
			cb.append(']').append('\n');
		}
		return cb.getString();
	}

	/* constructors */
	private MatrixKit()
	{
	}

}