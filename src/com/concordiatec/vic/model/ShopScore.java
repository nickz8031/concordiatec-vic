package com.concordiatec.vic.model;

import java.io.Serializable;
import java.util.List;

public class ShopScore extends VicModel implements Serializable {
	private int id;
	private int shopId;
	private double avg;
	private double price;
	private double food;
	private double kind;
	private double clean;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getShopId() {
		return shopId;
	}
	public void setShopId(int shopId) {
		this.shopId = shopId;
	}
	public double getAvg() {
		return avg;
	}
	public void setAvg(double avg) {
		this.avg = avg;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getFood() {
		return food;
	}
	public void setFood(double food) {
		this.food = food;
	}
	public double getKind() {
		return kind;
	}
	public void setKind(double kind) {
		this.kind = kind;
	}
	public double getClean() {
		return clean;
	}
	public void setClean(double clean) {
		this.clean = clean;
	}
	
}
