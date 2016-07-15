package com.algernoon.test;

import com.algernoon.utils.HttpUtils;

public class HttpUtilsTest {

	public static void main(String[] args) {
		for (int i = 0; i < 30 ; i ++) {
			HttpUtils.doGet("https://www.baidu.com/");
		}
	}
}
