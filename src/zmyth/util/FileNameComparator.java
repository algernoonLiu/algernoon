/**
 * Coyyright 2001 by seasky <www.seasky.cn>.
 */

package zmyth.util;

import java.io.File;

import zlib.set.Comparator;

/**
 * 类说明：文件名比较器
 * 
 * @version 1.0
 * @author hy
 */

public class FileNameComparator implements Comparator
{

	/* methods */
	/** 比较方法，返回比较结果常数 */
	public int compare(Object o1,Object o2)
	{
		int c=((File)o1).getAbsolutePath().compareTo(
			((File)o2).getAbsolutePath());
		if(c>0) return COMP_GRTR;
		if(c<0) return COMP_LESS;
		return COMP_EQUAL;
	}

}