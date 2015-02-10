package com.concordiatec.vilnet.listener;

import com.concordiatec.vilnet.model.ResData;
import com.concordiatec.vilnet.util.LogUtil;

public class SimpleVicResponseListener implements VicResponseListener {
	@Override
	public void onFailure(int httpResponseCode, String responseBody) {
		LogUtil.showInfo("Status : "+ httpResponseCode);
		LogUtil.showInfo("Response Body : "+ responseBody);
	}

	@Override
	public void onSuccess(ResData data) {}

	@Override
	public void onProgress(int written, int totalSize) {}
	
	@Override
	public void onEmptyResponse() {
		LogUtil.showInfo("No more data");
	}
}
