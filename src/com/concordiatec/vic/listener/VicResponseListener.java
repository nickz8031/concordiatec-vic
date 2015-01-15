package com.concordiatec.vic.listener;

import com.concordiatec.vic.model.ResData;

public interface VicResponseListener {
	public void onFailure(int httpResponseCode , String responseBody);

	public void onSuccess(ResData data);
	
	public void onProgress( int written , int totalSize );
	
	public void onEmptyResponse();
}
