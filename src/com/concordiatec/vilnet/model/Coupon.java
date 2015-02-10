/**
 * 쿠폰 모델 클래스
 */
package com.concordiatec.vilnet.model;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Coupon extends VicModel implements Serializable {
	
	private int id;
	private int shopId;
	private int userId;
	private String name;
	private String image;
	private int imageWidth;
	private int imageHeight;
	private List<String> notice;
	private int price;
	private int listPrice;
	private int quantity;
	private int total;
	private String startTime;
	private String endTime;
	private String useStart;
	private String useEnd;
	private int surPlusTime;
	private int kind;
	private String kindName;
	private int likeCount;
	private int shareCount;
	
	private Shop shop;
	
	private boolean isKeep;
	private boolean isLike;

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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	public List<String> getNotice() {
		return notice;
	}

	public void setNotice(List<String> notice) {
		this.notice = notice;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getListPrice() {
		return listPrice;
	}

	public void setListPrice(int listPrice) {
		this.listPrice = listPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getUseStart() {
		return useStart;
	}

	public void setUseStart(String useStart) {
		this.useStart = useStart;
	}

	public String getUseEnd() {
		return useEnd;
	}

	public void setUseEnd(String useEnd) {
		this.useEnd = useEnd;
	}

	public int getSurPlusTime() {
		return surPlusTime;
	}

	public void setSurPlusTime(int surPlusTime) {
		this.surPlusTime = surPlusTime;
	}

	public int getKind() {
		return kind;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public String getKindName() {
		return kindName;
	}

	public void setKindName(String kindName) {
		this.kindName = kindName;
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
	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public boolean isKeep() {
		return isKeep;
	}

	public void setKeep(boolean isKeep) {
		this.isKeep = isKeep;
	}

	public boolean isLike() {
		return isLike;
	}

	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}
}
