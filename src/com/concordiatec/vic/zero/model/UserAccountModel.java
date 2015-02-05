package com.concordiatec.vic.zero.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

public class UserAccountModel  extends ZeroModel{
	
	private String id="0";
	private String email="";
	private String name="";
	private String photo="";
	private boolean isShop=false;
	
	public UserAccountModel(byte[] arg2,Activity activity) {
		setActivity(activity);
		
		JSONObject data = getObj(arg2);
		try {
			id = data.getString("id");
			email = data.getString("email");
			name = data.getString("name");
			photo = data.getString("photo");
			isShop = data.getBoolean("is_shop");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getPhoto() {
		return photo;
	}

	public boolean isShop() {
		return isShop;
	}
}
