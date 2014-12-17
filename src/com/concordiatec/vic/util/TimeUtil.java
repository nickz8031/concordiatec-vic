package com.concordiatec.vic.util;

public class TimeUtil {
	public static String getUnixTimestamp() {
		return (System.currentTimeMillis()+"").substring(0, 10);
	}
	public static String getUnixTimestampMills() {
		return (System.currentTimeMillis()+"");
	}
	
	/**
	 * like 4 hours ago
	 * @param pastTime
	 * @return
	 */
	public static String getTimePast( int pastTime ){
		return ""+pastTime;
	}
}
