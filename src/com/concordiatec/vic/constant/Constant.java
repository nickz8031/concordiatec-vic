package com.concordiatec.vic.constant;

public class Constant {
	public static final boolean DEBUG = true;
	public static final String ONLINE_BROAD_ACTION = "com.concordiatec.vic.online";
	public static final String ONLINE_BROAD_INTENT_KEY = "vic_online";
	public static final int MAX_UPLOAD_FILE_COUNTS = 8;
	public static int SURPLUS_UPLOAD_COUNTS = MAX_UPLOAD_FILE_COUNTS;
	
	public static void initSurplus(){
		SURPLUS_UPLOAD_COUNTS = MAX_UPLOAD_FILE_COUNTS;
	}
}
