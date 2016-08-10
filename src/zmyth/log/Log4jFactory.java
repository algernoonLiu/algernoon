/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zmyth.log;

import zlib.log.LogFactory;
import zlib.log.Logger;

/**
 * 类说明：log4j记录器工厂
 * 
 * @version 1.0
 * @author hy
 */

public class Log4jFactory extends LogFactory
{

	/* static methods */
	/** 将log4j记录器工厂配置为标准工厂 */
	public static void configure()
	{
		factory=new Log4jFactory();
	}

	/* methods */
	/** 获得指定名称的记录器实例 */
	public Logger getInstance(String name)
	{
		return new Log4jLogger(org.apache.log4j.Logger.getLogger(name));
	}

}