package com.concordiatec.vilnet.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ArticleImages extends VicModel implements Serializable {
	@Override
	public String toString() {
		return "ArticleImages [id=" + id + ", name=" + name + ", width=" + width + ", height=" + height + "]";
	}
	private int id;
	private String name;
	private int width;
	private int height;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
