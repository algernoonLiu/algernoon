/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zmyth.log;

import zlib.log.Logger;

/**
 * 类说明：记录器
 * 
 * @version 1.0
 * @author hy
 */

public class Log4jLogger extends Logger
{

	/* fields */
	/** log4j记录器 */
	org.apache.log4j.Logger logger;

	/* constructors */
	/** 用指定的log4j记录器构造记录器 */
	protected Log4jLogger(org.apache.log4j.Logger logger)
	{
		this.logger=logger;
	}
	/* methods */
	/** 跟踪级别是否打开 */
	public boolean isTraceEnabled()
	{
		return logger.isDebugEnabled();
	}
	/** 跟踪记录 */
	public void trace(Object message)
	{
		logger.debug(message);
	}
	/** 跟踪异常记录 */
	public void trace(Object message,Throwable t)
	{
		logger.debug(message,t);
	}
	/** 调试级别是否打开 */
	public boolean isDebugEnabled()
	{
		return logger.isDebugEnabled();
	}
	/** 调试记录 */
	public void debug(Object message)
	{
		logger.debug(message);
	}
	/** 调试异常记录 */
	public void debug(Object message,Throwable t)
	{
		logger.debug(message,t);
	}
	/** 信息级别是否打开 */
	public boolean isInfoEnabled()
	{
		return logger.isInfoEnabled();
	}
	/** 信息记录 */
	public void info(Object message)
	{
		logger.info(message);
	}
	/** 信息异常记录 */
	public void info(Object message,Throwable t)
	{
		logger.info(message,t);
	}
	/** 警告级别是否打开 */
	public boolean isWarnEnabled()
	{
		return true;
	}
	/** 警告记录 */
	public void warn(Object message)
	{
		logger.warn(message);
	}
	/** 警告异常记录 */
	public void warn(Object message,Throwable t)
	{
		logger.warn(message,t);
	}
	/** 错误级别是否打开 */
	public boolean isErrorEnabled()
	{
		return true;
	}
	/** 错误记录 */
	public void error(Object message)
	{
		logger.error(message);
	}
	/** 错误异常记录 */
	public void error(Object message,Throwable t)
	{
		logger.error(message,t);
	}
	/** 严重错误级别是否打开 */
	public boolean isFatalEnabled()
	{
		return true;
	}
	/** 严重错误记录 */
	public void fatal(Object message)
	{
		logger.fatal(message);
	}
	/** 严重错误异常记录 */
	public void fatal(Object message,Throwable t)
	{
		logger.fatal(message,t);
	}

}