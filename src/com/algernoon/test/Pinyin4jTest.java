package com.algernoon.test;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Pinyin4jTest {
	
	public static void main(String[] args) throws BadHanyuPinyinOutputFormatCombination {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();  
		format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);  
		format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE); 
		String[] pinyinArray =PinyinHelper.toHanyuPinyinStringArray('绿',format);  
		for(int i = 0; i < pinyinArray.length; ++i) {  
		     System.out.println(pinyinArray[i]);  
		}  
	}
	
}
