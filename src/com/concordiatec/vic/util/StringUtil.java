package com.concordiatec.vic.util;

public class StringUtil {
	public static boolean isEmpty( Object str ){
		return (str == null || "".equals(str.toString()));		
	}
}
