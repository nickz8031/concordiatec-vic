package com.concordiatec.vic.constant;

public class RegPattern {
	public static final String NICK_NAME = "[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-z0-9]{2,10}";
	public static final String EMAIL = "[a-zA-Z0-9]?[a-zA-Z0-9_.-]+@[a-zA-Z0-9]+.[a-z]{2,5}";
	public static final String PASSWORD = "[\\s\\S]{6,20}";
	
}
