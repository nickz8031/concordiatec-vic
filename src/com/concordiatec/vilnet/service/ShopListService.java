package com.concordiatec.vilnet.service;

import java.util.Map;
import android.content.Context;
import com.concordiatec.vilnet.constant.ApiURL;
import com.concordiatec.vilnet.listener.VicResponseHandler;
import com.concordiatec.vilnet.listener.VicResponseListener;
import com.concordiatec.vilnet.model.LocalUser;
import com.concordiatec.vilnet.util.HttpUtil;
import com.loopj.android.http.RequestParams;

public class ShopListService extends HttpUtil{
	private Context context;

	public ShopListService(Context context) {
		this.context = context;
	}

	/**
	 * get coupon list
	 * 
	 * @param p
	 * @param listener
	 */
	public void getShops(Map<String, String> p, VicResponseListener listener) {
		RequestParams params = new RequestParams();
		LocalUser loginUser = new UserService(context).getLoginUser();
		if (loginUser != null) {
			params.put("user", loginUser.usrId);
		}
		if (p != null && p.size() > 0) {
			for (Map.Entry<String, String> entry : p.entrySet()) {
				params.put(entry.getKey(), entry.getValue());
			}
		}
		post(ApiURL.SHOP_LIST, params, new VicResponseHandler(listener));
	}

	public void getShops(VicResponseListener listener) {
		getShops(null, listener);
	}
}
