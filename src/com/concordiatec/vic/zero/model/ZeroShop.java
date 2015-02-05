package com.concordiatec.vic.zero.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.concordiatec.vic.util.Converter;

@SuppressWarnings("serial")
public class ZeroShop implements Serializable {

	private String jsonObj;
	private String jsonImages = "";

	private int id = 0;
	private int zone = 0;
	private int owner = 0;
	private int group = 0;
	private int likeCount = 0;
	private int shareCount = 0;
	private int fee = 0;
	private int opend = 0;
	
	private boolean isLike = false;
	private boolean isOpen = true;
	
	private String name = "";
	private String photo = "";
	private String addr1 = "";
	private String addr2 = "";
	private String phone = "";
	private String working = "";
	private String zoneName = "";
	private String groupName = "";
	
	
	private double lng = 0;
	private double lat = 0;

	private List<ZeroShopImage> images;
	private List<String> intro;
	
	private boolean isPrice = false;
	private boolean isFood = false;
	private boolean isKind = false;
	private boolean isCLean = false;
	
	private int scoreCount = 0;

	private double scoreAverage = 0;
	private double scorePrice = 0;
	private double scoreFood = 0;
	private double scoreKind = 0;
	private double scoreClean = 0;

	public ZeroShop(String data) {
		jsonObj = data;
		try {
			JSONObject obj = new JSONObject(jsonObj);

			id = obj.getInt("shop_id");
			owner = obj.getInt("user_id");
			zone = obj.getInt("area_id");
			group = obj.getInt("group_id");
			likeCount = obj.getInt("like_count");
			shareCount = obj.getInt("share_count");
			fee = obj.getInt("shop_fee");
			opend = obj.getInt("shop_created");
			
			isLike = obj.getInt("is_like") > 0 ? true : false;
			isOpen = obj.getInt("shop_open") > 0 ? true : false;
			
			name = obj.getString("user_name");
			photo = obj.getString("user_photo");
			addr1 = obj.getString("shop_addr1");
			addr2 = obj.getString("shop_addr2");
			phone = obj.getString("shop_phone");
			working = obj.getString("shop_working");
			zoneName = obj.getString("area_name");
			groupName = obj.getString("group_name");
			
			lng = obj.getDouble("shop_lng");
			lat = obj.getDouble("shop_lat");

			isPrice = obj.getInt("group_price") > 0 ? true : false;
			isFood = obj.getInt("group_food") > 0 ? true : false;
			isKind = obj.getInt("group_kind") > 0 ? true : false;
			isCLean = obj.getInt("group_clean") > 0 ? true : false;
			
			scoreCount = obj.getInt("score_count");

			scoreAverage = obj.getDouble("score_average");
			scorePrice = obj.getDouble("score_price");
			scoreFood = obj.getDouble("score_food");
			scoreKind = obj.getDouble("score_kind");
			scoreClean = obj.getDouble("score_clean");
			
			intro = new ArrayList<String>();
			
			JSONArray iArr = new JSONArray(obj.getString("shop_intro"));
			if(iArr.length() > 0){
				for (int i = 0; i < iArr.length(); i++) {
					intro.add(iArr.getString(i));
				}
			}
			
			images = new ArrayList<ZeroShopImage>();
			
			jsonImages = obj.getString("images");
			JSONArray arr = new JSONArray(jsonImages);
			if (arr.length() > 0) {
				for (int i = 0; i < arr.length(); i++) {
					JSONObject img = new JSONObject(arr.getString(i));
					images.add(new ZeroShopImage(img.getInt("id"), img
							.getString("name"), owner));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public int getId() {
		return id;
	}

	public int getOwner() {
		return owner;
	}

	public int getGroup() {
		return group;
	}
	
	public boolean isPrice() {
		return isPrice;
	}

	public boolean isFood() {
		return isFood;
	}

	public boolean isKind() {
		return isKind;
	}

	public boolean isCLean() {
		return isCLean;
	}
	
	public int getLikeCount() {
		return likeCount;
	}

	public int getShareCount() {
		return shareCount;
	}
	
	public boolean isLike() {
		return isLike;
	}

	public String getName() {
		return name;
	}

	public String getPhoto() {
		return photo;
	}
	
	public String getPhotoUrl(){
		return "http://image.remyjell.com/" + owner + "/" + photo;
	}
	
	public String getAddr2() {
		return addr2;
	}

	public String getPhone() {
		return phone;
	}

	public double getLng() {
		return lng;
	}

	public double getLat() {
		return lat;
	}

	public List<ZeroShopImage> getImages() {
		return images;
	}

	public int getScoreCount() {
		return scoreCount;
	}
	
	public String getScoreCountMsg(){
		return "("+scoreCount+"명 참여)";
	}
	
	public float getScoreAverage() {
		return (float) scoreAverage;
	}

	public float getScorePrice() {
		return (float) scorePrice;
	}

	public float getScoreFood() {
		return (float) scoreFood;
	}

	public float getScoreKind() {
		return (float) scoreKind;
	}

	public float getScoreClean() {
		return (float) scoreClean;
	}
	
	public String getScoreAverageStr() {
		return Double.toString(scoreAverage);
	}

	public String getScorePriceStr() {
		return Double.toString(scorePrice);
	}

	public String getScoreFoodStr() {
		return Double.toString(scoreFood);
	}

	public String getScoreKindStr() {
		return Double.toString(scoreKind);
	}

	public String getScoreCleanStr() {
		return Double.toString(scoreClean);
	}

	public String getJsonObj() {
		return jsonObj;
	}

	public String getJsonImages() {
		return jsonImages;
	}
	
	public String getDistance(Object systemService ){
		LocationManager manager = (LocationManager)systemService;
		Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		float[] results = new float[3];
		Location.distanceBetween(lng, lat, location.getLongitude(),location.getLatitude(), results);
		if(results[0] > 1000){
			return (int)(results[0]/1000) + "km";
		}else{
			return (int)results[0]+ "m";
		}
	}

	public void setShopIntro(Activity context, LinearLayout layout) {
		if(intro!= null && intro.size() > 0){
			for (int i = 0; i < intro.size(); i++) {
				
				TextView text = new TextView(context);
				LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
				text.setLayoutParams(params);
				text.setTextSize(12);
				text.setPadding(0, 0, 0, Converter.dip2px(context, 5));
				text.setTextColor(0xff666666);
				text.setText(intro.get(i));
				
				layout.addView(text);
			}
		}
	}
	
}
