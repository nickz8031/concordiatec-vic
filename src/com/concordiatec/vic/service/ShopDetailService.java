package com.concordiatec.vic.service;

import android.content.Context;
import com.concordiatec.vic.constant.ApiURL;
import com.concordiatec.vic.listener.VicResponseHandler;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.LocalUser;
import com.concordiatec.vic.util.HttpUtil;
import com.concordiatec.vic.util.LogUtil;
import com.loopj.android.http.RequestParams;

public class ShopDetailService extends HttpUtil{
	private Context context;
	public ShopDetailService(Context context) {
		this.context = context;
	}
	
	public void getDetail(int shopId, VicResponseListener listener) {
		RequestParams params = new RequestParams();
		if (shopId > 0) {
			params.put("id", shopId);
		} else {
			LogUtil.show("error request[ no shop id.]");
			return;
		}
		LocalUser loginUser = new UserService(context).getLoginUser();
		if (loginUser != null) {
			params.put("user", loginUser.usrId);
		}
		post(ApiURL.SHOP_DETAIL, params, new VicResponseHandler(listener));
	}
}
