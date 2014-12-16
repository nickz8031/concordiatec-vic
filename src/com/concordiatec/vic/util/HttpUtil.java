/**
 * use volley library
 * author : nick.z
 */
package com.concordiatec.vic.util;

import java.util.Map;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class HttpUtil extends Volley{
	public static HttpUtil client;
	public RequestQueue mQueue;
	
	public static HttpUtil getInstance( Context context ){
		if( client == null ){
			client = new HttpUtil();
			client.mQueue = Volley.newRequestQueue( context );
		}
		return client;
	}
	
	public static JSONObject MapToJSONObject( Map<String, String> map ){
		if( map == null ) return null;
		return new JSONObject(map);
	}
	
	
	public void doPost( String url ){
		doPost(url , null , null , null);
	}
	
	public void doPost( String url , Map<String, String> params ){
		doPost(url , params , null , null);		
	}
	
	public void doPost( String url , Map<String, String> params , Response.Listener<String> responseListener ){
		doPost(url , params , responseListener , null);		
	}
	
	public void doPost( String url , Response.Listener<String> responseListener ){
		doPost(url , null , responseListener , null);		
	}
	
	public void doPost(String url , Map<String, String> params , Response.Listener<String> responseListener , Response.ErrorListener errorListener) {
		final Map<String, String> postParams = params;
		StringRequest stringRequest = new StringRequest(Method.POST , url,  responseListener, errorListener){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return postParams;
			}
		
		};  
		mQueue.add(stringRequest);
	}
	
	/**
	 * do get request
	 * @param url
	 * request address
	 */
	public void doGet( String url ){
		doGet(url, null, null, null);
	}
	/**
	 * do get request
	 * @param url
	 * request address
	 * @param jsonRequest 
	 * request parameters
	 */
	public void doGet( String url , JSONObject jsonRequest ){
		doGet(url, jsonRequest, null, null);
	}

	/**
	 * do get request
	 * @param url
	 * request address
	 * @param listener
	 * response listener
	 */
	public void doGet( String url , Listener<JSONObject> listener  ){
		doGet(url, null, listener, null);
	}
	
	/**
	 * do get request
	 * @param url
	 * request address
	 * @param jsonRequest
	 * request parameters
	 * @param listener
	 * response listener
	 */
	public void doGet( String url , JSONObject jsonRequest , Listener<JSONObject> listener ){
		doGet(url, jsonRequest, listener, null);
	}
	
	
	/**
	 * do get request
	 * @param url
	 * request address
	 * @param listener
	 * response listener
	 * @param errorListener
	 * Error listener if error with response
	 */
	public void doGet( String url , Listener<JSONObject> listener , ErrorListener errorListener ){
		doGet(url, null, listener, errorListener);
	}
	
	/**
	 * do get request
	 * @param url
	 * request address
	 * @param jsonRequest
	 * request parameters
	 * @param listener
	 * response listener
	 * @param errorListener
	 * Error listener if error with response
	 */
	public void doGet( String url , JSONObject jsonRequest , Listener<JSONObject> listener , ErrorListener errorListener ){
		mQueue.add( new JsonObjectRequest(Method.GET, url , jsonRequest , listener , errorListener) );  
		mQueue.start();
	}
	
	
}
