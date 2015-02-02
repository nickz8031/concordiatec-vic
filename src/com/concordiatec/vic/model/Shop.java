package com.concordiatec.vic.model;

import java.io.Serializable;
import java.util.List;

public class Shop extends VicModel implements Serializable {
	private int id;
	private int areaId;
	private String areaName;
	private ShopGroup group;
	private int shopUserId;
	private String shopUserName;
	private String shopUserPhoto;
	private int shopFee;
	private int shopPhone;
	private List<String> shopIntro;
	private String shopWorking;
	private String shopAddr1;
	private String shopAddr2;
	private double shopLng;
	private double shopLat;
	private int scoreCount;
	private int likeCount;
	private int shareCount;
	private double distance;
	private boolean isLike;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAreaId() {
		return areaId;
	}
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public int getShopUserId() {
		return shopUserId;
	}
	public void setShopUserId(int shopUserId) {
		this.shopUserId = shopUserId;
	}
	public String getShopUserName() {
		return shopUserName;
	}
	public void setShopUserName(String shopUserName) {
		this.shopUserName = shopUserName;
	}
	public String getShopUserPhoto() {
		return shopUserPhoto;
	}
	public void setShopUserPhoto(String shopUserPhoto) {
		this.shopUserPhoto = shopUserPhoto;
	}
	public int getShopFee() {
		return shopFee;
	}
	public void setShopFee(int shopFee) {
		this.shopFee = shopFee;
	}
	public int getShopPhone() {
		return shopPhone;
	}
	public void setShopPhone(int shopPhone) {
		this.shopPhone = shopPhone;
	}
	public List<String> getShopIntro() {
		return shopIntro;
	}
	public void setShopIntro(List<String> shopIntro) {
		this.shopIntro = shopIntro;
	}
	public String getShopWorking() {
		return shopWorking;
	}
	public void setShopWorking(String shopWorking) {
		this.shopWorking = shopWorking;
	}
	public String getShopAddr1() {
		return shopAddr1;
	}
	public void setShopAddr1(String shopAddr1) {
		this.shopAddr1 = shopAddr1;
	}
	public String getShopAddr2() {
		return shopAddr2;
	}
	public void setShopAddr2(String shopAddr2) {
		this.shopAddr2 = shopAddr2;
	}
	public double getShopLng() {
		return shopLng;
	}
	public void setShopLng(double shopLng) {
		this.shopLng = shopLng;
	}
	public double getShopLat() {
		return shopLat;
	}
	public void setShopLat(double shopLat) {
		this.shopLat = shopLat;
	}
	public int getScoreCount() {
		return scoreCount;
	}
	public void setScoreCount(int scoreCount) {
		this.scoreCount = scoreCount;
	}
	public int getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
	public int getShareCount() {
		return shareCount;
	}
	public void setShareCount(int shareCount) {
		this.shareCount = shareCount;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public boolean isLike() {
		return isLike;
	}
	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}
	public ShopGroup getGroup() {
		return group;
	}
	public void setGroup(ShopGroup group) {
		this.group = group;
	}
	
}
