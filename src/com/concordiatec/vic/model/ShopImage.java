package com.concordiatec.vic.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ShopImage extends VicModel implements Serializable {
	private int id;
	private String name;
	private int width;
	private int height;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
}
