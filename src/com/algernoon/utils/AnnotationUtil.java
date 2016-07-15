package com.algernoon.utils;

import com.algernoon.annotations.Desc;
import com.algernoon.constants.Constant;

public class AnnotationUtil {

	public static String getDesc(int RetCode) {
		String s = "";
		try {
		    s = Constant.class.getDeclaredField("R" + RetCode).getAnnotation(Desc.class).desc();
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return s;
	}
}
