package com.concordiatec.vic.zero.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ZeroReview {

	private int id=0;
	private int shopId=0;
	private int userId=0;
	private int articleId=0;
	
	private String userName="";
	private String userPhoto="";
	private String content="";
	private String created="";
	
	private double average=0;
	private int price=0;
	private int food=0;
	private int kind=0;
	private int clean=0;
	
	public ZeroReview(String jsonString){
//		tv.setText(jsonString);
		try {
			JSONObject obj = new JSONObject(jsonString);
			
			id = obj.getInt("review_id");
			shopId = obj.getInt("shop_id");
			userId = obj.getInt("user_id");
			articleId = obj.getInt("article_id");
			
			userName = obj.getString("user_name");
			userPhoto = obj.getString("user_photo");
			content = obj.getString("content");
			created = obj.getString("review_added");
			
			average = obj.getDouble("review_average");
			price = obj.getInt("review_price");
			food = obj.getInt("review_food");
			kind = obj.getInt("review_kind");
			clean = obj.getInt("review_clean");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	public int getId() {
		return id;
	}

	public int getShopId() {
		return shopId;
	}

	public int getUserId() {
		return userId;
	}

	public int getArticleId() {
		return articleId;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserPhoto() {
		return userPhoto;
	}

	public String getContent() {
		return content;
	}
	
	public String getCreated() {
		return created;
	}
	
	public String getAverageStr() {
		return Double.toString(average);
	}

	public float getPrice() {
		return (float)price;
	}

	public float getFood() {
		return  (float)food;
	}

	public float getKind() {
		return  (float)kind;
	}

	public float getClean() {
		return (float) clean;
	}

	public String getPhotoUrl() {
		return "http://image.remyjell.com/" + userId + "/" + userPhoto;
	}

}
