package com.concordiatec.vic.util;

import java.util.HashMap;
import java.util.Map;
import com.activeandroid.ActiveAndroid;
import retrofit.RestAdapter;

public class HttpUtil {
	public static final String API_SIGN = "9d9e4b192895dd26a8ac6258294c3443";
	
	public static final String API_URL = "http://demo1.remyjell.com";
	public static final String API_IMG_URL = "http://image.remyjell.com";
	protected RestAdapter restAdapter;
	public HttpUtil() {
		this.restAdapter = buildConnection();
	}

	/**
	 * build http connection to api
	 * @param url
	 * @return
	 */
	protected RestAdapter buildConnection() {
		return new RestAdapter.Builder().setEndpoint( API_URL ).build();
	}
	
	public static String getEncodedAuthKey(){
		return EncryptUtil.SHA1(API_SIGN + HttpUtil.getToken());
	}
	
	public static String getToken(){
		return TimeUtil.getUnixTimestamp();
	}
	
	public static Map<String, String> getAuthMap(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("sign", HttpUtil.getEncodedAuthKey());
		params.put("token", HttpUtil.getToken());
		return params;
	}
	
	protected String getServerImgPath( int writerId , String fileName ){
		return API_IMG_URL + "/" + writerId + "/" + fileName;
	}
	protected String getServerImgPath( int writerId ){
		return getServerImgPath(writerId , "");
	}
	
	protected int getIntValue( Object object ){
		return Double.valueOf(object.toString()).intValue();
	}
	
}


