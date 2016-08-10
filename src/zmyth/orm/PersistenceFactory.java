/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zmyth.orm;

/**
 * 类说明：持久器工厂
 * 
 * @version 1.0
 * @author hy
 */

public class PersistenceFactory
{

	/* static fields */
	/** 空持久器 */
	public static final Persistence NULL=new Persistence();
	/** 当前的持久器工厂 */
	protected static PersistenceFactory factory;

	/* static methods */
	/** 获得当前的持久器工厂 */
	public static PersistenceFactory getFactory()
	{
		return factory;
	}
	/** 获得指定类的持久器 */
	public static Persistence getPersistance(Class clazz)
	{
		return getPersistance(clazz.getName());
	}
	/** 获得指定名称的持久器 */
	public static Persistence getPersistance(String name)
	{
		if(factory==null) return NULL;
		return factory.getInstance(name);
	}

	/* methods */
	/** 获得指定名称的持久器实例 */
	public Persistence getInstance(String name)
	{
		return NULL;
	}

}