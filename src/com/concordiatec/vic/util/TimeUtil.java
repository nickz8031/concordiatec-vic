package com.concordiatec.vic.util;

public class TimeUtil {
	public static String getUnixTimestamp() {
		return (System.currentTimeMillis()+"").substring(0, 10);
	}
	public static String getUnixTimestampMills() {
		return (System.currentTimeMillis()+"");
	}
}
