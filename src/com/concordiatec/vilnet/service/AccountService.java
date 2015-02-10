package com.concordiatec.vilnet.service;

import android.content.Context;
import com.concordiatec.vilnet.constant.ApiURL;
import com.concordiatec.vilnet.listener.VicResponseHandler;
import com.concordiatec.vilnet.listener.VicResponseListener;
import com.concordiatec.vilnet.model.LocalUser;
import com.concordiatec.vilnet.util.EncryptUtil;
import com.concordiatec.vilnet.util.HttpUtil;
import com.loopj.android.http.RequestParams;

public class AccountService extends HttpUtil {
	private UserService uService;
	public AccountService( Context context ) {
		uService = new UserService(context);
	}
	
	public void modifyPwd( String oldpwd , String newpwd , VicResponseListener listener ){
		LocalUser loginUser = uService.getLoginUser();
		if( loginUser != null ){
			RequestParams params = new RequestParams();
			params.put("id", loginUser.usrId);
			params.put("old", EncryptUtil.EncPwd(oldpwd) );
			params.put("new", EncryptUtil.EncPwd(newpwd) );
			post(ApiURL.ACCOUNT_MODIFY_PASSWORD, params, new VicResponseHandler(listener));
		}
	}
	
	public void modifyGender( int gender , VicResponseListener listener ){
		LocalUser loginUser = uService.getLoginUser();
		if( loginUser != null ){
			RequestParams params = new RequestParams();
			params.put("id", loginUser.usrId);
			params.put("sex", gender);
			post(ApiURL.ACCOUNT_MODIFY_GENDER, params, new VicResponseHandler(listener));
		}
	}
	
	public void modifyNickName( String nickName , VicResponseListener listener ){
		LocalUser loginUser = uService.getLoginUser();
		if( loginUser != null ){
			RequestParams params = new RequestParams();
			params.put("id", loginUser.usrId);
			params.put("name", nickName.trim());
			post(ApiURL.ACCOUNT_MODIFY_NICK, params, new VicResponseHandler(listener));
		}
	}
	
}
