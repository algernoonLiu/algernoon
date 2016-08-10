/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zmyth.orm;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import zlib.log.LogFactory;
import zlib.log.Logger;

/**
 * 类说明：数据库连接代理类，包装了数据库连接接口
 * 
 * @version 1.0
 * @author hy
 */

class ConnectionProxy implements Connection
{

	/* static fields */
	/** 日志记录 */
	private static final Logger log=LogFactory
		.getLogger(ConnectionProxy.class);

	/* fields */
	/** 数据库连接 */
	Connection connection;
	/** 是否使用 */
	boolean used;
	/** 使用次数 */
	int usedCount;
	/** 最后使用时间 */
	long usedTime;

	/* constructors */
	/** 用实际的数据库连接构造一个数据库连接代理对象 */
	public ConnectionProxy(Connection c)
	{
		if(c==null)
			throw new IllegalArgumentException(getClass().getName()
				+" <init>, null Connection");
		connection=c;
		usedTime=System.currentTimeMillis();
		if(log.isInfoEnabled())
			log.info("init, "+super.toString()+"["+connection+"]");
	}
	/* properties */
	/** 判断连接是否使用 */
	public synchronized boolean isUsed()
	{
		return used;
	}
	/** 获得连接的使用次数 */
	public int getUsedCount()
	{
		return usedCount;
	}
	/** 获得连接的最后使用时间 */
	public long getUsedTime()
	{
		return usedTime;
	}
	/** 判断连接是否可以使用 */
	public boolean isActive()
	{
		try
		{
			return !connection.isClosed();
		}
		catch(Exception e)
		{
		}
		return false;
	}
	/** 代理方法 */
	public boolean isClosed() throws SQLException
	{
		return connection.isClosed();
	}
	/** 代理方法 */
	public boolean isReadOnly() throws SQLException
	{
		return connection.isReadOnly();
	}
	/** 代理方法 */
	public void setReadOnly(boolean b) throws SQLException
	{
		connection.setReadOnly(b);
	}
	/** 代理方法 */
	public boolean getAutoCommit() throws SQLException
	{
		return connection.getAutoCommit();
	}
	/** 代理方法 */
	public void setAutoCommit(boolean b) throws SQLException
	{
		connection.setAutoCommit(b);
	}
	/** 代理方法 */
	public String getCatalog() throws SQLException
	{
		return connection.getCatalog();
	}
	/** 代理方法 */
	public void setCatalog(String catalog) throws SQLException
	{
		connection.setCatalog(catalog);
	}
	/** 代理方法 */
	public int getHoldability() throws SQLException
	{
		return connection.getHoldability();
	}
	/** 代理方法 */
	public void setHoldability(int holdability) throws SQLException
	{
		connection.setHoldability(holdability);
	}
	/** 代理方法 */
	public int getTransactionIsolation() throws SQLException
	{
		return connection.getTransactionIsolation();
	}
	/** 代理方法 */
	public void setTransactionIsolation(int level) throws SQLException
	{
		connection.setTransactionIsolation(level);
	}
	/** 代理方法 */
	public Map getTypeMap() throws SQLException
	{
		return connection.getTypeMap();
	}
	/** 代理方法 */
	public void setTypeMap(Map map) throws SQLException
	{
		connection.setTypeMap(map);
	}
	/** 代理方法 */
	public DatabaseMetaData getMetaData() throws SQLException
	{
		return connection.getMetaData();
	}
	/** 代理方法 */
	public SQLWarning getWarnings() throws SQLException
	{
		return connection.getWarnings();
	}
	/** 代理方法 */
	public Savepoint setSavepoint() throws SQLException
	{
		return connection.setSavepoint();
	}
	/** 代理方法 */
	public Savepoint setSavepoint(String name) throws SQLException
	{
		return connection.setSavepoint(name);
	}
	/* methods */
	/** 使用方法 */
	public synchronized boolean use()
	{
		if(used) return false;
		used=true;
		usedCount++;
		usedTime=System.currentTimeMillis();
		if(log.isDebugEnabled())
			log.debug("use, "+super.toString()+"[usedCount="+usedCount
				+", usedTime="+usedTime+", "+connection+"]");
		return true;
	}
	/** 代理方法 */
	public String nativeSQL(String sql) throws SQLException
	{
		return connection.nativeSQL(sql);
	}
	/** 代理方法 */
	public void commit() throws SQLException
	{
		connection.commit();
	}
	/** 代理方法 */
	public void rollback() throws SQLException
	{
		connection.rollback();
	}
	/** 代理方法 */
	public void clearWarnings() throws SQLException
	{
		connection.clearWarnings();
	}
	/** 代理方法 */
	public Statement createStatement() throws SQLException
	{
		return connection.createStatement();
	}
	/** 代理方法 */
	public Statement createStatement(int resultSetType,
		int resultSetConcurrency) throws SQLException
	{
		return connection
			.createStatement(resultSetType,resultSetConcurrency);
	}
	/** 代理方法 */
	public Statement createStatement(int resultSetType,
		int resultSetConcurrency,int resultSetHoldability)
		throws SQLException
	{
		return connection.createStatement(resultSetType,
			resultSetConcurrency,resultSetHoldability);
	}
	/** 代理方法 */
	public PreparedStatement prepareStatement(String sql)
		throws SQLException
	{
		return connection.prepareStatement(sql);
	}
	/** 代理方法 */
	public PreparedStatement prepareStatement(String sql,
		int autoGeneratedKeys) throws SQLException
	{
		return connection.prepareStatement(sql,autoGeneratedKeys);
	}
	/** 代理方法 */
	public PreparedStatement prepareStatement(String sql,int resultSetType,
		int resultSetConcurrency) throws SQLException
	{
		return connection.prepareStatement(sql,resultSetType,
			resultSetConcurrency);
	}
	/** 代理方法 */
	public PreparedStatement prepareStatement(String sql,int resultSetType,
		int resultSetConcurrency,int resultSetHoldability)
		throws SQLException
	{
		return connection.prepareStatement(sql,resultSetType,
			resultSetConcurrency,resultSetHoldability);
	}
	/** 代理方法 */
	public PreparedStatement prepareStatement(String sql,int[] columnIndexes)
		throws SQLException
	{
		return connection.prepareStatement(sql,columnIndexes);
	}
	/** 代理方法 */
	public PreparedStatement prepareStatement(String sql,String[] columnNames)
		throws SQLException
	{
		return connection.prepareStatement(sql,columnNames);
	}
	/** 代理方法 */
	public CallableStatement prepareCall(String sql) throws SQLException
	{
		return connection.prepareCall(sql);
	}
	/** 代理方法 */
	public CallableStatement prepareCall(String sql,int resultSetType,
		int resultSetConcurrency) throws SQLException
	{
		return connection
			.prepareCall(sql,resultSetType,resultSetConcurrency);
	}
	/** 代理方法 */
	public CallableStatement prepareCall(String sql,int resultSetType,
		int resultSetConcurrency,int resultSetHoldability)
		throws SQLException
	{
		return connection.prepareCall(sql,resultSetType,
			resultSetConcurrency,resultSetHoldability);
	}
	/** 代理方法 */
	public void rollback(Savepoint savepoint) throws SQLException
	{
		connection.rollback(savepoint);
	}
	/** 代理方法 */
	public void releaseSavepoint(Savepoint savepoint) throws SQLException
	{
		connection.releaseSavepoint(savepoint);
	}
	/** 代理方法 */
	public synchronized void close() throws SQLException
	{
		used=false;
		if(log.isDebugEnabled()) log.debug("close, "+super.toString());
	}
	/** 销毁方法 */
	public synchronized void destroy()
	{
		boolean old=used;
		used=true;
		try
		{
			connection.close();
		}
		catch(SQLException e)
		{
		}
		if(log.isInfoEnabled())
			log.info("destroy, "+super.toString()+"[used="+old
				+", usedCount="+usedCount+", usedTime="+usedTime+", "
				+connection+"]");
	}
	/* common methods */
	public String toString()
	{
		return super.toString()+"[used="+used+", usedCount="+usedCount
			+", usedTime="+usedTime+", "+connection+"]";
	}
	public <T>T unwrap(Class<T> iface) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}
	public boolean isWrapperFor(Class<?> iface) throws SQLException
	{
		// TODO Auto-generated method stub
		return false;
	}
//	@Override
//	public void setTypeMap(Map<String,Class<?>> map) throws SQLException
//	{
//		// TODO Auto-generated method stub
//		
//	}
	public Clob createClob() throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}
	public Blob createBlob() throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}
	public NClob createNClob() throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}
	public SQLXML createSQLXML() throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}
	public boolean isValid(int timeout) throws SQLException
	{
		// TODO Auto-generated method stub
		return false;
	}
	public void setClientInfo(String name,String value)
		throws SQLClientInfoException
	{
		// TODO Auto-generated method stub
		
	}
	public void setClientInfo(Properties properties)
		throws SQLClientInfoException
	{
		// TODO Auto-generated method stub
		
	}
	public String getClientInfo(String name) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}
	public Properties getClientInfo() throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}
	public Array createArrayOf(String typeName,Object[] elements)
		throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}
	public Struct createStruct(String typeName,Object[] attributes)
		throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setSchema(String schema) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getSchema() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void abort(Executor executor) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getNetworkTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

}