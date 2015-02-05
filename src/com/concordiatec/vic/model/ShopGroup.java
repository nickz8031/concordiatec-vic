package com.concordiatec.vic.model;

import java.io.Serializable;
@SuppressWarnings("serial")
public class ShopGroup extends VicModel implements Serializable {
	
	private int id;
	private int parentId;
	private String groupTree;
	private String name;
	private int fee;
	private int shopCount;
	private int sort;
	private int price;
	private int isFood;
	private int kind;
	private int hasClean;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getGroupTree() {
		return groupTree;
	}
	public void setGroupTree(String groupTree) {
		this.groupTree = groupTree;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getFee() {
		return fee;
	}
	public void setFee(int fee) {
		this.fee = fee;
	}
	public int getShopCount() {
		return shopCount;
	}
	public void setShopCount(int shopCount) {
		this.shopCount = shopCount;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getIsFood() {
		return isFood;
	}
	public void setIsFood(int isFood) {
		this.isFood = isFood;
	}
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	public int getHasClean() {
		return hasClean;
	}
	public void setHasClean(int hasClean) {
		this.hasClean = hasClean;
	}
	
}
