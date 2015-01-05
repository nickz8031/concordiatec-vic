package com.concordiatec.vic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.concordiatec.vic.inf.VicServiceInterface;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.model.User;
import com.concordiatec.vic.requestinf.UserInf;
import com.concordiatec.vic.util.EncryptUtil;
import com.concordiatec.vic.util.HttpUtil;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.StringUtil;
import com.google.gson.internal.LinkedTreeMap;

public class LoginService extends HttpUtil implements VicServiceInterface {
	private Context context;
	
	public LoginService( Context context ){
		this.context = context;
		ActiveAndroid.initialize(context);
	}
	public void doLogin(VicResponseListener listener ,String account , String pwd){
		final VicResponseListener lis = listener;
		UserInf uInf = restAdapter.create(UserInf.class);
		Map<String, String> postParams = getAuthMap();
		if( StringUtil.isEmpty(account) || StringUtil.isEmpty(pwd) ){
			LogUtil.show("error[ account or password cannot empty.]");
			return;
		}
		postParams.put("email", account);
		postParams.put("pwd", EncryptUtil.EncPwd(pwd) );
		
		uInf.login(postParams, new Callback<ResData>() {
			@Override
			public void success(ResData data, Response arg1) {
				switch (Integer.valueOf( data.getStatus() )) {
					case 0: //successful
						lis.onResponse(data.getData());
						break;
					case 1: //no data
						lis.onResponseNoData();
						break;
					case 402: //no data
						lis.onResponseNoData();
						break;
					default:
						LogUtil.show(data.getMsg() + " [code:" + data.getStatus() + "]");
						break;
				}
			}
			
			@Override
			public void failure(RetrofitError err) {
				LogUtil.show(err.getMessage());
			}
		});
	}
	
	public User getLoginUser(){
		return new Select().from(User.class).executeSingle();
	}
	
	@Override
	public List<User> mapListToModelList(ArrayList<LinkedTreeMap<String, Object>> list) {
		return null;
	}
	@Override
	public User mapToModel(LinkedTreeMap<String, Object> map) {
		return null;
	}
	
	
	
}
