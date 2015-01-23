package com.concordiatec.vic.constant;

public class Constant {
	public static final boolean DEBUG = true;
	public static final String NMAP_API_KEY = "0a98324e77992f4ea706b4becc7915a1";
	public static final String ONLINE_BROAD_ACTION = "com.concordiatec.vic.online";
	public static final String ONLINE_BROAD_INTENT_KEY = "vic_online";
	public static final int MAX_UPLOAD_FILE_COUNTS = 8;
	public static int SURPLUS_UPLOAD_COUNTS = MAX_UPLOAD_FILE_COUNTS;
	
	public static int MAX_HEIGHT_WHEN_MORE_THAN_ONE = 500;

	public static final int ONLINE_BROAD_RESULT_CODE = 10001;
	
	public static final int EDIT_ARTICLE_SUCCED = 15000;
	
	
	public static void initSurplus(){
		SURPLUS_UPLOAD_COUNTS = MAX_UPLOAD_FILE_COUNTS;
	}
}
