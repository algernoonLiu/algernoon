/**
 * Coyyright 2001 by seasky <www.seasky.cn>.
 */

package zmyth.orm;

import zlib.set.Selector;

/**
 * 类说明：Sql选择器
 * 
 * @version 1.0
 * @author hy
 */

public interface SqlSelector extends Selector
{

	/** 获得sql语句 */
	public String getSql();

}