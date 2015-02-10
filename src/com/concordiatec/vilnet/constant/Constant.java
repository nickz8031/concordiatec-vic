package com.concordiatec.vilnet.constant;

public class Constant {
	public static final boolean DEBUG = true;
	public static final int MAX_UPLOAD_FILE_COUNTS = 8;
	public static int SURPLUS_UPLOAD_COUNTS = MAX_UPLOAD_FILE_COUNTS;
	
	//article detail page images max height if more than one image
	public static int MAX_HEIGHT_WHEN_MORE_THAN_ONE = 500;

	//max comments count show in article list
	public static int MAX_LASTEST_COMMENT_COUNT = 3;
	
	public static final int ONLINE_BROAD_RESULT_CODE = 10001;
	
	//activity request & result code
	public final static int ARTICLE_EDIT = 4001;
	
	public final static int COMMENT_EDIT = 5001;
	public final static int COMMENT_EDIT_SUCCED = 5002;
	
	//result & request code end;
	
	public static void initSurplus(){
		SURPLUS_UPLOAD_COUNTS = MAX_UPLOAD_FILE_COUNTS;
	}
}
