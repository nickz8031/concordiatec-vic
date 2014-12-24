/**
 * 쿠폰 모델 클래스
 */
package com.concordiatec.vic.model;


public class Coupon extends Model {
	/**
	 * 쿠폰 아이디
	 */
	private int id;
	/**
	 * 쿠폰 타이틀
	 */
	private String couponTitle;
	/**
	 * 쿠폰 사용가능 시작 시간
	 */
	private int startTime;
	/**
	 * 쿠폰 마감시간
	 */
	private int endTime;
	/**
	 * 쿠폰 가격(?)
	 */
	private int listPrice;
	/**
	 * 쿠폰 실제 가격( 할인 후 가격 )
	 */
	private int price;
	/**
	 * 쿠폰 발행 수
	 */
	private int totalCount;
	/**
	 * 쿠폰 잔여 량
	 */
	private int surplusCount;
	/**
	 * 쿠폰 발행 가게	
	 */
	private Shop shop;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCouponTitle() {
		return couponTitle;
	}
	public void setCouponTitle(String couponTitle) {
		this.couponTitle = couponTitle;
	}
	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	public int getEndTime() {
		return endTime;
	}
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	public int getListPrice() {
		return listPrice;
	}
	public void setListPrice(int listPrice) {
		this.listPrice = listPrice;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getSurplusCount() {
		return surplusCount;
	}
	public void setSurplusCount(int surplusCount) {
		this.surplusCount = surplusCount;
	}
	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
	}
		
}
