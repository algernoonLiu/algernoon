/**
 * Coyyright 2001 by seasky <www.seasky.cn>.
 */

package zmyth.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import zlib.field.BooleanField;
import zlib.field.ByteArrayField;
import zlib.field.ByteField;
import zlib.field.DoubleField;
import zlib.field.FieldObject;
import zlib.field.Fields;
import zlib.field.FloatField;
import zlib.field.IntField;
import zlib.field.LongField;
import zlib.field.ShortField;
import zlib.field.StringField;
import zlib.log.LogFactory;
import zlib.log.Logger;
import zlib.set.Selector;
import zlib.text.CharBuffer;
import zlib.text.CharBufferThreadLocal;

/**
 * 类说明：SQL函数库
 * 
 * @version 1.0
 * @author hy
 */

public final class SqlKit
{

	/* static fields */
	/** 库信息 */
	public static final String toString=SqlKit.class.getName();

	/** 提交类型的常量定义 */
	public static final int HAND_COMMIT=1,AUTO_COMMIT=2;

	/** 日志记录 */
	private static final Logger log=LogFactory.getLogger(SqlKit.class);

	/* static methods */
	/**
	 * 执行指定sql语句， 必须为INSERT、UPDATE或DELETE语句，
	 * 或者不返回任何内容的SQL语句
	 */
	public static void execute(ConnectionManager cm,String sql)
	{
		Connection c=cm.getConnection();
		try
		{
			execute(c,sql,true);
		}
		finally
		{
			close(c);
		}
	}
	/**
	 * 执行指定sql语句并决定本次是否进行事务提交，
	 * 必须为INSERT、UPDATE或DELETE语句，
	 * 或者不返回任何内容的SQL语句
	 */
	public static int execute(Connection c,String sql,boolean commit)
	{
		if(log.isInfoEnabled()) log.info("execute, sql="+sql);
		Statement st=null;
		int ac=0;
		try
		{
			ac=c.getAutoCommit()?AUTO_COMMIT:HAND_COMMIT;
			st=c.createStatement();
			int r=st.executeUpdate(sql);
			if(ac==HAND_COMMIT&&commit) c.commit();
			return r;
		}
		catch(SQLException e)
		{
			if(ac==HAND_COMMIT) rollback(c);
			if(log.isWarnEnabled())
				log.warn("execute error, sql="+sql+" "+c,e);
			throw new RuntimeException(toString+" execute, sql="+sql,e);
		}
		finally
		{
			close(st,null);
		}
	}
	/** 查询sql语句，返回单条结果，返回null表示无结果 */
	public static Fields query(ConnectionManager cm,String sql)
	{
		Connection c=cm.getConnection();
		try
		{
			return query(c,sql);
		}
		finally
		{
			close(c);
		}
	}
	/** 查询sql语句，返回单条结果，返回null表示无结果 */
	public static Fields query(Connection c,String sql)
	{
		if(log.isInfoEnabled()) log.info("query, sql="+sql);
		Statement st=null;
		ResultSet rs=null;
		try
		{
			st=c.createStatement();
			rs=st.executeQuery(sql);
			if(rs.next())
			{
				ResultSetMetaData rsmd=rs.getMetaData();
				FieldObject[] array=new FieldObject[rsmd.getColumnCount()];
				for(int i=0;i<array.length;i++)
					array[i]=SqlKit.getField(rs,rsmd,i+1);
				return new Fields(array);
			}
			return null;
		}
		catch(SQLException e)
		{
			if(log.isWarnEnabled())
				log.warn("query error, sql="+sql+" "+c,e);
			throw new RuntimeException(toString+" query, sql="+sql,e);
		}
		finally
		{
			close(st,rs);
		}
	}
	/** 查询sql语句，返回多条结果，返回null表示无结果 */
	public static Fields[] querys(ConnectionManager cm,String sql)
	{
		SqlFieldsListSelector selector=new SqlFieldsListSelector(sql);
		querys(cm,selector);
		if(selector.list.isEmpty()) return null;
		Fields[] temp=new Fields[selector.list.size()];
		selector.list.toArray(temp);
		return temp;
	}
	/** 使用sql选择器查询 */
	public static void querys(ConnectionManager cm,SqlSelector selector)
	{
		Connection c=cm.getConnection();
		try
		{
			querys(c,selector,true);
		}
		finally
		{
			close(c);
		}
	}
	/** 查询sql语句，返回多条结果，返回null表示无结果 */
	public static Fields[] querys(Connection c,String sql,boolean commit)
	{
		SqlFieldsListSelector selector=new SqlFieldsListSelector(sql);
		querys(c,selector,commit);
		if(selector.list.isEmpty()) return null;
		Fields[] temp=new Fields[selector.list.size()];
		selector.list.toArray(temp);
		return temp;
	}
	/** 使用sql选择器查询并决定本次是否进行事务提交 */
	public static void querys(Connection c,SqlSelector selector,
		boolean commit)
	{
		if(log.isInfoEnabled()) log.info("querys, "+selector);
		String sql=selector.getSql();
		Statement st=null;
		ResultSet rs=null;
		int ac=0;
		int t=-1;
		try
		{
			ac=c.getAutoCommit()?AUTO_COMMIT:HAND_COMMIT;
			st=c.createStatement();
			rs=st.executeQuery(sql);
			ResultSetMetaData rsmd;
			FieldObject[] array;
			while(rs.next())
			{
				rsmd=rs.getMetaData();
				array=new FieldObject[rsmd.getColumnCount()];
				for(int i=array.length-1;i>=0;i--)
					array[i]=SqlKit.getField(rs,rsmd,i+1);
				t=selector.select(new Fields(array));
				if(t==Selector.FALSE) continue;
				if(t==Selector.TRUE)
				{
					rs.deleteRow();
					continue;
				}
				if(t==Selector.TRUE_BREAK) rs.deleteRow();
				break;
			}
			if(ac==HAND_COMMIT&&commit) c.commit();
		}
		catch(SQLException e)
		{
			if(ac==HAND_COMMIT) rollback(c);
			if(log.isWarnEnabled())
				log.warn("querys error, sql="+sql+" "+c+" select="+t,e);
			throw new RuntimeException(toString+" querys, sql="+sql,e);
		}
		catch(RuntimeException e)
		{
			if(ac==HAND_COMMIT) rollback(c);
			if(log.isWarnEnabled())
				log.warn("querys error, sql="+sql+" "+c+" select="+t,e);
			throw e;
		}
		finally
		{
			close(st,rs);
		}
	}
	/** 获得查询结果集中指定列的域和数据 */
	public static FieldObject getField(ResultSet rs,ResultSetMetaData rsmd,
		int i) throws SQLException
	{
		int type=rsmd.getColumnType(i);
		if(type==Types.INTEGER)
		{
			IntField f=new IntField();
			f.name=rsmd.getColumnName(i);
			f.value=rs.getInt(i);
			return f;
		}
		if(type==Types.CHAR||type==Types.VARCHAR||type==Types.LONGVARCHAR
			||type==Types.CLOB)
		{
			StringField f=new StringField();
			f.name=rsmd.getColumnName(i);
			f.value=rs.getString(i);
			return f;
		}
		if(type==Types.BINARY||type==Types.VARBINARY
			||type==Types.LONGVARBINARY||type==Types.BLOB)
		{
			ByteArrayField f=new ByteArrayField();
			f.name=rsmd.getColumnName(i);
			f.value=rs.getBytes(i);
			return f;
		}
		if(type==Types.BIGINT)
		{
			LongField f=new LongField();
			f.name=rsmd.getColumnName(i);
			f.value=rs.getLong(i);
			return f;
		}
		if(type==Types.DOUBLE||type==Types.DECIMAL)
		{
			DoubleField f=new DoubleField();
			f.name=rsmd.getColumnName(i);
			f.value=rs.getDouble(i);
			return f;
		}
		if(type==Types.FLOAT)
		{
			FloatField f=new FloatField();
			f.name=rsmd.getColumnName(i);
			f.value=rs.getFloat(i);
			return f;
		}
		if(type==Types.SMALLINT)
		{
			ShortField f=new ShortField();
			f.name=rsmd.getColumnName(i);
			f.value=(short)rs.getInt(i);
			return f;
		}
		if(type==Types.BIT||type==Types.TINYINT)
		{
			ByteField f=new ByteField();
			f.name=rsmd.getColumnName(i);
			f.value=(byte)rs.getInt(i);
			return f;
		}
		if(type==Types.BOOLEAN)
		{
			BooleanField f=new BooleanField();
			f.name=rsmd.getColumnName(i);
			f.value=rs.getBoolean(i);
			return f;
		}
		StringField f=new StringField();
		f.name=rsmd.getColumnName(i);
		f.value=rs.getString(i);
		return f;
	}
	/** 事务回滚 */
	public static void rollback(Connection c)
	{
		try
		{
			c.rollback();
		}
		catch(Exception e)
		{
			if(log.isWarnEnabled()) log.warn("rollback error, "+c,e);
		}
	}
	/** 关闭语句和结果集 */
	public static void close(Statement st,ResultSet rs)
	{
		if(rs!=null)
		{
			try
			{
				rs.close();
			}
			catch(Exception e)
			{
			}
		}
		if(st!=null)
		{
			try
			{
				st.close();
			}
			catch(Exception e)
			{
			}
		}
	}
	/** 关闭连接 */
	public static void close(Connection c)
	{
		if(c!=null)
		{
			try
			{
				c.close();
			}
			catch(Exception e)
			{
			}
		}
	}
	/** 拼写sql语句 */
	public static String getSelectSqlString(String table,String condition)
	{
		return getSqlString("select *",table,condition,null,false,-1,0);
	}
	/** 拼写sql语句 */
	public static String getSelectSqlString(String table,String condition,
		String order,boolean desc)
	{
		return getSqlString("select *",table,condition,order,desc,-1,0);
	}
	/** 拼写sql语句 */
	public static String getSelectSqlString(String table,String condition,
		String order,boolean desc,int index,int length)
	{
		return getSqlString("select *",table,condition,order,desc,index,
			length);
	}
	/** 拼写sql语句 */
	public static String getSqlString(String action,String table,
		String condition,String order,boolean desc,int index,int length)
	{
		CharBuffer cb=CharBufferThreadLocal.getCharBuffer();
		cb.clear();
		cb.append(action).append(" from ").append(table);
		if(condition!=null&&condition.length()>0)
			cb.append(" where ").append(condition);
		if(order!=null&&order.length()>0)
		{
			cb.append(" order by ").append(order);
			if(desc) cb.append(" desc");
		}
		if(index>=0)
		{
			cb.append(" limit ").append(index);
			if(length>0) cb.append(',').append(length);
		}
		return cb.getString();
	}

	/* constructors */
	private SqlKit()
	{
	}

}