package com.concordiatec.vic.zero.model;

import java.io.Serializable;

public class ZeroShopImage implements Serializable {
	
	private int id;
	private int user;
	private String name;
	
	public ZeroShopImage(int id, String name, int user) {
		this.id = id;
		this.name = name;
		this.user = user;
	}
	
	public int getId(){
		return id;
	}
	
	public int getUser(){
		return user;
	}
	
	public String getUrl(){
		String src =  "http://image.remyjell.com/" + user + "/" + name;
		return src;
	}
}
