package com.concordiatec.vic.zero.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.concordiatec.vic.R;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

public class ZeroModel {
	
	private Activity activity;
	private TextView text;
	private JSONArray mArr;
	private JSONObject mObj;
	
	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	public JSONArray getArr(byte[] arg) {
		try {
			JSONObject obj = new JSONObject(new String(arg));
			if(obj.getInt("status") > 0){
				setText(obj.getString("msg"));
			}else{
//				setText(obj.getString("data"));
				mArr = new JSONArray(obj.getString("data"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			setText("[JSONException] " + e.getMessage());
		}
		return mArr;
	}
	
	public JSONObject getObj(byte[] arg) {
		try {
			JSONObject obj = new JSONObject(new String(arg));
			if(obj.getInt("status") > 0){
				setText(obj.getString("msg"));
			}else{
//				setText(obj.getString("data"));
				mObj = new JSONObject(obj.getString("data"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			setText("[JSONException] " + e.getMessage());
		}
		return mObj;
	}
	
	private void setText(String msg) {
		// TODO Auto-generated method stub
		TextView text = getText();
		if(text != null){
			text.setText(msg);
		}else{
			Log.i("vilcock","Model (e) : " + msg);
		}
	}

	private TextView getText() {
		if(text == null){
			if(activity != null){
//				text = (TextView) activity.findViewById(R.id.text);
			}
		}
		return text;
	}
}
