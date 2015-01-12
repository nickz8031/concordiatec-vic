package com.concordiatec.vic.listener;

import java.io.IOException;
import java.net.URI;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import android.os.Message;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.tools.Tools;
import com.concordiatec.vic.util.ResponseUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

public class VicResponseHandler extends AsyncHttpResponseHandler {
	private VicResponseListener lis;
	public VicResponseHandler( VicResponseListener listener ) {
		this.lis = listener;
	}
	@Override
	public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
		if( lis != null ){
			lis.onFailure(statusCode , new String(responseBody) );
		}
		
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
		if( lis != null ){
			ResData data = ResponseUtil.vicProcessResp(responseBody);
			if( data == null ){
				lis.onFailure(statusCode, new String(responseBody));
			}else{
				if( Tools.getIntValue(data.getStatus()) > 0 ){
					lis.onError(data);
				}else{
					lis.onSuccess(data);
				}
			}
		}
	}
	

	@Override
	public void onProgress(int bytesWritten, int totalSize) {
		if( lis != null ){
			lis.onProgress(bytesWritten, totalSize);
		}
	}
	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		super.onCancel();
	}
	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		super.onFinish();
	}
	@Override
	public void onRetry(int retryNo) {
		// TODO Auto-generated method stub
		super.onRetry(retryNo);
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
}
