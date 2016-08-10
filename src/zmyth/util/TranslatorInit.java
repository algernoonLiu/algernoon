/**
 * Copyright 2001 by seasky <www.seasky.cn>.
 */

package zmyth.util;

import zlib.text.TextKit;
import zlib.text.Translator;
import zmyth.xlib.File;
import zmyth.xlib.FileFactory;

/**
 * 类说明：文字转换器的配置类
 * 
 * @version 1.0
 * @author hy
 */

public class TranslatorInit
{

	/* method */
	/** 初始化方法 */
	public void init(String argFile,Translator translator)
	{
		File file=FileFactory.getFile(argFile);
		if(file==null)
			throw new IllegalArgumentException(this
				+" init, file not found, "+argFile);
		byte[] data=file.read();
		file.destroy();
		String str=new String(data);
		String[] strs=TextKit.splitLine(str);
		int index;
		for(int i=0;i<strs.length;i++)
		{
			if(strs[i].length()==0) continue;
			if(strs[i].charAt(0)=='#') continue;
			index=strs[i].indexOf('=');
			if(index<0) continue;
			str=TextKit.replaceAll(strs[i].substring(index+1),"\\n","\n");
			translator.addText(strs[i].substring(0,index),str);
		}
	}

}