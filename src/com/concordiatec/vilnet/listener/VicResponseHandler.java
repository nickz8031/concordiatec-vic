package com.concordiatec.vilnet.listener;


import org.apache.http.Header;
import com.concordiatec.vilnet.model.ResData;
import com.concordiatec.vilnet.tools.Tools;
import com.concordiatec.vilnet.util.ResponseUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class VicResponseHandler extends AsyncHttpResponseHandler {
	private VicResponseListener lis;
	public VicResponseHandler( VicResponseListener listener ) {
		this.lis = listener;
	}
	@Override
	public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
		if( lis != null ){
			lis.onFailure(statusCode , throwable.getMessage() );
		}
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
		if( lis != null ){
			ResData data = ResponseUtil.vicProcessResp(responseBody);
			if( data == null ){
				lis.onFailure(statusCode, new String(responseBody));
			}else{
				int status = Tools.getIntValue(data.getStatus());
				switch (status) {
					case 0:
						lis.onSuccess(data);
						break;
					case 1:
						lis.onEmptyResponse();
						break;
					default:
						lis.onFailure(status , new String(responseBody));
						break;
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
