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
	
	//activity request & result code
	public final static int ARTICLE_EDIT = 4001;
	public final static int ARTICLE_EDIT_SUCCED = 4002;
	
	public final static int ARTICLE_DELETE_SUCCED = 6002;
	
	public final static int COMMENT_EDIT = 5001;
	public final static int COMMENT_EDIT_SUCCED = 5002;
	
	//detail page request
	public final static int DETAIL_ACTIVITY_REQUEST = 0; //article detail
	public final static int COUPON_DETAIL_ACTIVITY_REQUEST = 10; //coupon detail
	public final static int SHOP_DETAIL_ACTIVITY_REQUEST = 100; //shop detail
	//result & request code end;
	
	public static void initSurplus(){
		SURPLUS_UPLOAD_COUNTS = MAX_UPLOAD_FILE_COUNTS;
	}
}
