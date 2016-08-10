/**
 * Coyyright 2001 by seasky <www.seasky.cn>.
 */

package zmyth.orm;

import zlib.set.ArrayList;
import zlib.set.Selector;

/**
 * 类说明：Sql域列列表选择器
 * 
 * @version 1.0
 * @author hy
 */

public class SqlFieldsListSelector implements SqlSelector
{

	/* fields */
	/** sql语句 */
	String sql;
	/** 域列列表 */
	ArrayList list=new ArrayList();

	/* constructors */
	/** 构造指定的sql语句的Sql域列列表选择器 */
	public SqlFieldsListSelector(String sql)
	{
		this.sql=sql;
	}
	/** 获得sql语句 */
	public String getSql()
	{
		return sql;
	}
	/** 获得域列列表 */
	public ArrayList getList()
	{
		return list;
	}
	/* methods */
	public int select(Object obj)
	{
		list.add(obj);
		return Selector.FALSE;
	}
	/* common methods */
	public String toString()
	{
		return super.toString()+"[sql="+sql+"]";
	}

}