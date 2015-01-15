package com.concordiatec.vic.listener;

import com.concordiatec.vic.model.ResData;

public class SimpleVicResponseListener implements VicResponseListener {
	@Override
	public void onFailure(int httpResponseCode, String responseBody) {}

	@Override
	public void onSuccess(ResData data) {}

	@Override
	public void onProgress(int written, int totalSize) {}
	
	@Override
	public void onEmptyResponse() {}
}
