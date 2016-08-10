/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zmyth.orm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import zlib.field.BooleanField;
import zlib.field.ByteArrayField;
import zlib.field.ByteField;
import zlib.field.CharField;
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

/**
 * 类说明：SQL持久化类
 * 
 * @version 1.0
 * @author hy
 */

public class SqlPersistence extends Persistence
{

	/* static fields */
	/** 空字节数组 */
	public static final byte[] NULL_BYTEARRAY=new byte[0];

	/** 日志记录 */
	private static final Logger log=LogFactory
		.getLogger(SqlPersistence.class);

	/* static methods */
	/** 更新结果集中指定域的数据 */
	public static void update(ResultSet rs,FieldObject field)
		throws SQLException
	{
		if(field instanceof StringField)
			rs.updateString(field.name,((StringField)field).value);
		else if(field instanceof ByteArrayField)
			rs.updateBytes(field.name,((ByteArrayField)field).value);
		else if(field instanceof BooleanField)
			rs.updateBoolean(field.name,((BooleanField)field).value);
		else if(field instanceof ByteField)
			rs.updateByte(field.name,((ByteField)field).value);
		else if(field instanceof ShortField)
			rs.updateShort(field.name,((ShortField)field).value);
		else if(field instanceof CharField)
			rs.updateShort(field.name,(short)((CharField)field).value);
		else if(field instanceof IntField)
			rs.updateInt(field.name,((IntField)field).value);
		else if(field instanceof LongField)
			rs.updateLong(field.name,((LongField)field).value);
		else if(field instanceof FloatField)
			rs.updateFloat(field.name,((FloatField)field).value);
		else if(field instanceof DoubleField)
			rs.updateDouble(field.name,((DoubleField)field).value);
	}
	/** 查询结果集中指定域的数据 */
	public static void query(ResultSet rs,FieldObject field)
		throws SQLException
	{
		if(field instanceof StringField)
			((StringField)field).value=rs.getString(field.name);
		else if(field instanceof ByteArrayField)
			((ByteArrayField)field).value=rs.getBytes(field.name);
		else if(field instanceof BooleanField)
			((BooleanField)field).value=rs.getBoolean(field.name);
		else if(field instanceof ByteField)
			((ByteField)field).value=rs.getByte(field.name);
		else if(field instanceof ShortField)
			((ShortField)field).value=rs.getShort(field.name);
		else if(field instanceof CharField)
			((CharField)field).value=(char)rs.getShort(field.name);
		else if(field instanceof IntField)
			((IntField)field).value=rs.getInt(field.name);
		else if(field instanceof LongField)
			((LongField)field).value=rs.getLong(field.name);
		else if(field instanceof FloatField)
			((FloatField)field).value=rs.getFloat(field.name);
		else if(field instanceof DoubleField)
			((DoubleField)field).value=rs.getDouble(field.name);
	}
	/** 获得sql语句 */
	public static void getSqlString(String sql,String table,FieldObject key,
		CharBuffer cb)
	{
		cb.append(sql).append(" from ").append(table);
		cb.append(" where ").append(key.name).append('=');
		if(key instanceof StringField)
			cb.append('\'').append(key.getValue()).append('\'');
		else
			cb.append(key.getValue());
	}

	/* fields */
	/** 数据库连接管理器 */
	ConnectionManager cm;
	/** 表名 */
	String table;

	/* properties */
	/** 获得数据库连接管理器 */
	public ConnectionManager getConnectionManager()
	{
		return cm;
	}
	/** 设置数据库连接管理器 */
	public void setConnectionManager(ConnectionManager cm)
	{
		this.cm=cm;
	}
	/** 获得表名 */
	public String getTable()
	{
		return table;
	}
	/** 设置表名 */
	public void setTable(String table)
	{
		this.table=table;
	}
	/* methods */
	/** 设置指定域和域数据 */
	public int set(FieldObject key,Fields fields)
	{
		CharBuffer cb=new CharBuffer();
		getSqlString("select *",table,key,cb);
		Connection c=null;
		Statement st=null;
		ResultSet rs=null;
		int ac=0;
		try
		{
			c=cm.getConnection();
			ac=cm.isAutoCommit()?SqlKit.AUTO_COMMIT:SqlKit.HAND_COMMIT;
			st=c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);
			rs=st.executeQuery(cb.getString());
			int t=OK;
			if(rs.next())
			{
				FieldObject[] array=fields.getArray();
				if(log.isInfoEnabled())
				{
					cb.append("; OK");
					for(int i=0;i<array.length;i++)
					{
						cb.append(' ');
						cb.append(array[i].name).append('=');
						cb.append(array[i].getValue());
						update(rs,array[i]);
					}
				}
				else
				{
					for(int i=0;i<array.length;i++)
						update(rs,array[i]);
				}
				rs.updateRow();
			}
			// 插入数据
			else
			{
				t=ADD;
				rs.moveToInsertRow();
				FieldObject[] array=fields.getArray();
				if(log.isInfoEnabled())
				{
					cb.append("; ADD");
					update(rs,key);
					cb.append(' ');
					cb.append(key.name).append('=');
					cb.append(key.getValue());
					for(int i=0;i<array.length;i++)
					{
						cb.append(' ');
						cb.append(array[i].name).append('=');
						cb.append(array[i].getValue());
						update(rs,array[i]);
					}
				}
				else
				{
					update(rs,key);
					for(int i=0;i<array.length;i++)
						update(rs,array[i]);
				}
				rs.insertRow();
			}
			if(ac==SqlKit.HAND_COMMIT) c.commit();
			if(log.isDebugEnabled()) log.debug("set, "+cb.getString());
			return t;
		}
		catch(Exception e)
		{
			if(ac==SqlKit.HAND_COMMIT) SqlKit.rollback(c);
			if(log.isWarnEnabled())
				log.warn("set error, "+cb.getString(),e);
			return EXCEPTION;
		}
		finally
		{
			SqlKit.close(st,rs);
			SqlKit.close(c);
		}
	}
	/** 取出指定域和域数据 */
	public int get(FieldObject key,Fields fields)
	{
		CharBuffer cb=new CharBuffer();
		getSqlString("select *",table,key,cb);
		Connection c=null;
		Statement st=null;
		ResultSet rs=null;
		try
		{
			c=cm.getConnection();
			st=c.createStatement();
			rs=st.executeQuery(cb.getString());
			int t=OK;
			if(rs.next())
			{
				FieldObject[] array=fields.getArray();
				if(array.length==0)
				{
					ResultSetMetaData rsmd=rs.getMetaData();
					array=new FieldObject[rsmd.getColumnCount()];
					for(int i=0;i<array.length;i++)
						array[i]=SqlKit.getField(rs,rsmd,i);
					fields.add(array);
				}
				else
				{
					if(log.isInfoEnabled())
					{
						cb.append("; OK");
						for(int i=0;i<array.length;i++)
						{
							cb.append(' ');
							cb.append(array[i].name).append('=');
							query(rs,array[i]);
							cb.append(array[i].getValue());
						}
					}
					else
					{
						for(int i=0;i<array.length;i++)
							query(rs,array[i]);
					}
				}
			}
			else
			{
				t=RESULTLESS;
				if(log.isInfoEnabled()) cb.append("; RESULTLESS");
			}
			if(log.isDebugEnabled()) log.debug("get, "+cb.getString());
			return t;
		}
		catch(Exception e)
		{
			if(log.isWarnEnabled())
				log.warn("get error, "+cb.getString(),e);
			return EXCEPTION;
		}
		finally
		{
			SqlKit.close(st,rs);
			SqlKit.close(c);
		}
	}
	/** 设置并取出指定域和域数据 */
	public int update(FieldObject key,Fields setFields,Fields getFields)
	{
		CharBuffer cb=new CharBuffer();
		getSqlString("select *",table,key,cb);
		Connection c=null;
		Statement st=null;
		ResultSet rs=null;
		int ac=0;
		try
		{
			c=cm.getConnection();
			ac=cm.isAutoCommit()?SqlKit.AUTO_COMMIT:SqlKit.HAND_COMMIT;
			st=c.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);
			rs=st.executeQuery(cb.getString());
			int t=OK;
			if(rs.next())
			{
				FieldObject[] array=getFields.getArray();
				if(log.isInfoEnabled())
				{
					cb.append("; OK");
					for(int i=0;i<array.length;i++)
					{
						cb.append(' ');
						cb.append(array[i].name).append('=');
						query(rs,array[i]);
						cb.append(array[i].getValue());
					}
				}
				else
				{
					for(int i=0;i<array.length;i++)
						query(rs,array[i]);
				}
				array=setFields.getArray();
				if(log.isInfoEnabled())
				{
					cb.append("; SET");
					for(int i=0;i<array.length;i++)
					{
						cb.append(' ');
						cb.append(array[i].name).append('=');
						cb.append(array[i].getValue());
						update(rs,array[i]);
					}
				}
				else
				{
					for(int i=0;i<array.length;i++)
						update(rs,array[i]);
				}
				rs.updateRow();
				if(ac==SqlKit.HAND_COMMIT) c.commit();
			}
			// 插入数据
			else
			{
				t=ADD;
				rs.moveToInsertRow();
				FieldObject[] array=setFields.getArray();
				if(log.isInfoEnabled())
				{
					cb.append("; ADD");
					update(rs,key);
					cb.append(' ');
					cb.append(key.name).append('=');
					cb.append(key.getValue());
					for(int i=0;i<array.length;i++)
					{
						cb.append(' ');
						cb.append(array[i].name).append('=');
						cb.append(array[i].getValue());
						update(rs,array[i]);
					}
				}
				else
				{
					update(rs,key);
					for(int i=0;i<array.length;i++)
						update(rs,array[i]);
				}
				rs.insertRow();
				if(ac==SqlKit.HAND_COMMIT) c.commit();
				array=getFields.getArray();
				if(log.isInfoEnabled())
				{
					cb.append("; GET");
					for(int i=0;i<array.length;i++)
					{
						cb.append(' ');
						cb.append(array[i].name).append('=');
						query(rs,array[i]);
						cb.append(array[i].getValue());
					}
				}
				else
				{
					for(int i=0;i<array.length;i++)
						query(rs,array[i]);
				}
			}
			if(log.isDebugEnabled()) log.debug("update, "+cb.getString());
			return t;
		}
		catch(Exception e)
		{
			if(ac==SqlKit.HAND_COMMIT) SqlKit.rollback(c);
			if(log.isWarnEnabled())
				log.warn("update error, "+cb.getString(),e);
			return EXCEPTION;
		}
		finally
		{
			SqlKit.close(st,rs);
			SqlKit.close(c);
		}
	}
	/** 删除指定域 */
	public int delete(FieldObject key)
	{
		CharBuffer cb=new CharBuffer();
		getSqlString("delete",table,key,cb);
		Connection c=null;
		Statement st=null;
		int ac=0;
		try
		{
			c=cm.getConnection();
			ac=cm.isAutoCommit()?SqlKit.AUTO_COMMIT:SqlKit.HAND_COMMIT;
			st=c.createStatement(ResultSet.TYPE_FORWARD_ONLY,
				ResultSet.CONCUR_UPDATABLE);
			int t=st.executeUpdate(cb.getString());
			if(t>0)
			{
				t=OK;
				if(log.isInfoEnabled()) cb.append("; OK");
			}
			else
			{
				t=RESULTLESS;
				if(log.isInfoEnabled()) cb.append("; RESULTLESS");
			}
			if(ac==SqlKit.HAND_COMMIT) c.commit();
			if(log.isDebugEnabled()) log.debug("delete, "+cb.getString());
			return t;
		}
		catch(Exception e)
		{
			if(ac==SqlKit.HAND_COMMIT) SqlKit.rollback(c);
			if(log.isWarnEnabled())
				log.warn("delete error, "+cb.getString(),e);
			return EXCEPTION;
		}
		finally
		{
			SqlKit.close(st,null);
			SqlKit.close(c);
		}
	}
	/** 删除并取出指定域和域数据 */
	public int remove(FieldObject key,Fields fields)
	{
		CharBuffer cb=new CharBuffer();
		getSqlString("select *",table,key,cb);
		Connection c=null;
		Statement st=null;
		ResultSet rs=null;
		try
		{
			c=cm.getConnection();
			st=c.createStatement(ResultSet.TYPE_FORWARD_ONLY,
				ResultSet.CONCUR_UPDATABLE);
			rs=st.executeQuery(cb.getString());
			int t=OK;
			if(rs.next())
			{
				FieldObject[] array=fields.getArray();
				if(log.isInfoEnabled())
				{
					cb.append("; OK");
					for(int i=0;i<array.length;i++)
					{
						cb.append(' ');
						cb.append(array[i].name).append('=');
						query(rs,array[i]);
						cb.append(array[i].getValue());
					}
				}
				else
				{
					for(int i=0;i<array.length;i++)
						query(rs,array[i]);
				}
				rs.deleteRow();
			}
			else
			{
				t=RESULTLESS;
				if(log.isInfoEnabled()) cb.append("; RESULTLESS");
			}
			if(log.isDebugEnabled()) log.debug("remove, "+cb.getString());
			return t;
		}
		catch(Exception e)
		{
			if(log.isWarnEnabled())
				log.warn("remove error, "+cb.getString(),e);
			return EXCEPTION;
		}
		finally
		{
			SqlKit.close(st,rs);
			SqlKit.close(c);
		}
	}
	/** 选择方法， 用指定的选择器对象选出符合条件的域数据 */
	public int select(Selector selector)
	{
		if(!(selector instanceof SqlSelector)) return RESULTLESS;
		try
		{
			SqlKit.querys(cm,(SqlSelector)selector);
			return OK;
		}
		catch(Exception e)
		{
			if(log.isWarnEnabled()) log.warn("select error, "+selector,e);
			return EXCEPTION;
		}
	}

}