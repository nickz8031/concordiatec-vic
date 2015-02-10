package com.concordiatec.vilnet.service;

import android.content.Context;
import com.concordiatec.vilnet.constant.ApiURL;
import com.concordiatec.vilnet.listener.VicResponseHandler;
import com.concordiatec.vilnet.listener.VicResponseListener;
import com.concordiatec.vilnet.model.LocalUser;
import com.concordiatec.vilnet.util.HttpUtil;
import com.concordiatec.vilnet.util.LogUtil;
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
