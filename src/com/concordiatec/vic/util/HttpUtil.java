package com.concordiatec.vic.util;

import java.util.HashMap;
import java.util.Map;
import com.concordiatec.vic.tools.Tools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpUtil {

	public static final String API_SIGN = "9d9e4b192895dd26a8ac6258294c3443";
	public static final String API_URL = "http://demo1.remyjell.com";
	public static final String API_IMG_URL = "http://image.remyjell.com";
	
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		AsyncHttpClient httpClient = new AsyncHttpClient();
		params.put("sign", getEncodedAuthKey());
		params.put("token", getToken());
		httpClient.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.addHeader("User-Agent", "android");
		params.put("sign", getEncodedAuthKey());
		params.put("token", getToken());
		httpClient.post(getAbsoluteUrl(url), params, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return API_URL + relativeUrl;
	}
	
	public static String getEncodedAuthKey(){
		return EncryptUtil.SHA1(API_SIGN + getToken());
	}
	
	public static String getToken(){
		return TimeUtil.getUnixTimestamp();
	}
	
	public static Map<String, String> getAuthMap(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("sign", getEncodedAuthKey());
		params.put("token", getToken());
		return params;
	}
	public static String getServerImgPath( int writerId , String fileName ){
		return API_IMG_URL + "/" + writerId + "/" + fileName;
	}
	public static String getServerImgPath( int writerId ){
		return getServerImgPath(writerId , "");
	}
	public static int getIntValue( Object object ){
		return Tools.getIntValue(object);
	}
}
