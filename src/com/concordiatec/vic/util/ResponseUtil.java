package com.concordiatec.vic.util;

import com.concordiatec.vic.constant.ResponseStatus;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.ResData;

public class ResponseUtil {
	public static void processResp( ResData data , VicResponseListener listener ){
		if( Integer.valueOf( data.getStatus() ) == ResponseStatus.SUCCED ){
			listener.onSuccess(data.getData());
		}else{
			listener.onError(data);
		}
	}
}
