/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zmyth.orm;

import zlib.field.FieldObject;
import zlib.field.Fields;
import zlib.set.Selector;

/**
 * 类说明：持久化类
 * 
 * @version 1.0
 * @author hy
 */

public class Persistence
{

	/* static fields */
	/** 返回值的常量定义 */
	public static final int EXCEPTION=-1,RESULTLESS=0,OK=1,ADD=2;

	/* constructors */
	/** 禁止外部构造 */
	protected Persistence()
	{
	}

	/* methods */
	/** 设置指定域和域数据 */
	public int set(FieldObject key,Fields fields)
	{
		return EXCEPTION;
	}
	/** 取出指定域和域数据 */
	public int get(FieldObject key,Fields fields)
	{
		return EXCEPTION;
	}
	/** 设置并取出指定域和域数据 */
	public int update(FieldObject key,Fields setFields,Fields getFields)
	{
		return EXCEPTION;
	}
	/** 删除指定域 */
	public int delete(FieldObject key)
	{
		return EXCEPTION;
	}
	/** 删除并取出指定域和域数据 */
	public int remove(FieldObject key,Fields fields)
	{
		return EXCEPTION;
	}
	/** 选择方法， 用指定的选择器对象选出符合条件的域数据 */
	public int select(Selector selector)
	{
		return EXCEPTION;
	}

}