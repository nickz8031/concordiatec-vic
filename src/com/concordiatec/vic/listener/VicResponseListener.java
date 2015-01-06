package com.concordiatec.vic.listener;

import com.concordiatec.vic.model.ResData;

public interface VicResponseListener {
	public void onFailure(String reason);

	public void onSuccess(Object data);

	public void onError(ResData error);
}
