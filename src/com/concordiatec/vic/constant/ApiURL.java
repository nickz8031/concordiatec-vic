package com.concordiatec.vic.constant;

public class ApiURL {
	public static final String URL_PREFIX = "/?r=";
	public static final String ARTICLE_LIST = URL_PREFIX + "article";
	public static final String ARTICLE_DETAIL = URL_PREFIX + "article/view";
	public static final String ARTICLE_LIKE = URL_PREFIX + "article/like";

	public static final String ARTICLE_WRITE = URL_PREFIX + "article/create";
	public static final String ARTICLE_DELETE = URL_PREFIX + "article/delete";
	public static final String ARTICLE_EDIT = URL_PREFIX + "article/edit";

	public static final String COMMENT_LIST = URL_PREFIX + "comment";
	public static final String COMMENT_WRITE = URL_PREFIX + "comment/create";
	public static final String COMMENT_EDIT = URL_PREFIX + "comment/edit";
	public static final String COMMENT_DELETE = URL_PREFIX + "comment/delete";
	public static final String COMMENT_LIKE = URL_PREFIX + "comment/plus";
	
	public static final String LOGIN = URL_PREFIX + "site/login";
	public static final String SIGNUP = URL_PREFIX + "site/join";
	
	public static final String COUPON_LIST = URL_PREFIX + "coupon";
	public static final String COUPON_DETAIL = URL_PREFIX + "coupon/view";
	public static final String COUPON_LIKE = URL_PREFIX + "coupon/like";
	
	public static final String SHOP_LIST = URL_PREFIX + "shop";
	public static final String SHOP_DETAIL = URL_PREFIX + "shop/view";
	public static final String SHOP_LIKE = URL_PREFIX + "shop/like";
	
	public static final String USER_INFO = URL_PREFIX + "user/info";
	
	public static final String ACCOUNT_MODIFY_PASSWORD = URL_PREFIX + "account/changePassword";
	public static final String ACCOUNT_MODIFY_GENDER = URL_PREFIX + "account/changeSex";
	public static final String ACCOUNT_MODIFY_NICK = URL_PREFIX + "account/changeName";
	

	public static final String REPORT_COMMENT = URL_PREFIX + "report/comment";
}
