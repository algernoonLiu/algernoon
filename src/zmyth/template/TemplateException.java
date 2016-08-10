/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zmyth.template;

/**
 * 类说明：模板读入异常
 * 
 * @version 1.0
 * @author hy
 */

public class TemplateException extends RuntimeException
{

	/* static fields */
	/**
	 * <code>serialVersionUID</code> 的注释
	 */
	private static final long serialVersionUID=-6294900226942393533L;

	/* fields */
	/** 模板文档中错误所在的行和列 */
	protected int row,column;

	/* constructors */
	/** 构造指定异常消息的模板读入异常 */
	public TemplateException(String msg)
	{
		this(msg,null,-1,-1);
	}
	/** 构造指定引发异常的模板读入异常 */
	public TemplateException(Throwable t)
	{
		this(null,t,-1,-1);
	}
	/** 构造指定异常消息和引发异常的模板读入异常 */
	public TemplateException(String msg,Throwable t)
	{
		this(msg,t,-1,-1);
	}
	/** 构造指定异常消息和错误所在行列的模板读入异常 */
	public TemplateException(String msg,int row,int column)
	{
		this(msg,null,row,column);
	}
	/** 构造指定异常消息和引发异常及错误所在行列的模板读入异常 */
	public TemplateException(String msg,Throwable t,int row,int column)
	{
		super(msg,t);
		this.row=row;
		this.column=column;
	}
	/* properties */
	/** 获得模板文档中错误所在的行 */
	public int getRowNumber()
	{
		return row;
	}
	/** 获得模板文档中错误所在的列 */
	public int getColumnNumber()
	{
		return column;
	}

}