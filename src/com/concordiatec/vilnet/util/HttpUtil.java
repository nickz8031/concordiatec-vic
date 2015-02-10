package com.concordiatec.vilnet.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.concordiatec.vilnet.inf.IVicService;
import com.concordiatec.vilnet.tools.Tools;
import com.google.gson.internal.LinkedTreeMap;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpUtil implements IVicService {

	public static final String API_SIGN = "9d9e4b192895dd26a8ac6258294c3443";
	public static final String API_URL = "http://api.vilnet.co.kr";
	public static final String API_IMG_URL = "http://img.vilnet.co.kr";
	
	public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.addHeader("User-Agent", "android");
		params.put("sign", getEncodedAuthKey());
		params.put("token", getToken());
		httpClient.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.addHeader("User-Agent", "android");
		params.put("sign", getEncodedAuthKey());
		params.put("token", getToken());
		httpClient.post(getAbsoluteUrl(url), params, responseHandler);
	}

	public String getAbsoluteUrl(String relativeUrl) {
		return API_URL + relativeUrl;
	}
	
	public String getEncodedAuthKey(){
		return EncryptUtil.SHA1(API_SIGN + getToken());
	}
	
	public String getToken(){
		return TimeUtil.getUnixTimestamp();
	}
	
	public Map<String, String> getAuthMap(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("sign", getEncodedAuthKey());
		params.put("token", getToken());
		return params;
	}
	public String getServerImgPath( int writerId , String fileName ){
		return API_IMG_URL + "/" + writerId + "/" + fileName;
	}
	public String getServerImgPath( int writerId ){
		return getServerImgPath(writerId , "");
	}
	public int getIntValue( Object object ){
		return Tools.getIntValue(object);
	}

	@Override
	public List<?> mapListToModelList(ArrayList<LinkedTreeMap<String, Object>> list) {
		return null;
	}

	@Override
	public Object mapToModel(LinkedTreeMap<String, Object> map) {
		return null;
	}
}
