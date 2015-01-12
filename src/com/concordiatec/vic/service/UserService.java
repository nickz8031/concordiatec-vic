package com.concordiatec.vic.service;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.concordiatec.vic.constant.ApiURL;
import com.concordiatec.vic.inf.VicServiceInterface;
import com.concordiatec.vic.listener.VicResponseHandler;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.User;
import com.concordiatec.vic.util.EncryptUtil;
import com.concordiatec.vic.util.HttpUtil;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.StringUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.loopj.android.http.RequestParams;

public class UserService extends HttpUtil implements VicServiceInterface {
	private Context context;
	
	public UserService( Context context ){
		this.context = context;
	}
	public void doLogin(String account , String pwd , VicResponseListener listener){
		
		if( StringUtil.isEmpty(account) || StringUtil.isEmpty(pwd) ){
			LogUtil.show("error[ account or password cannot empty.]");
			return;
		}
		RequestParams params = new RequestParams();
		params.put("email", account);
		params.put("password", EncryptUtil.EncPwd(pwd) );
		
		post(ApiURL.LOGIN, params, new VicResponseHandler(listener));
		
	}
	
	public User getLoginUser(){
		return new Select().from(User.class).executeSingle();
	}
	
	public void login( User usr ){
		clearData();
		usr.save();
	}
	
	
	public void logout(){
		clearData();
	}
	
	private void clearData(){
		new Delete().from(User.class).where("Id > ?", 0).execute();
	}
	
	@Override
	public List<User> mapListToModelList(ArrayList<LinkedTreeMap<String, Object>> list) {
		return null;
	}
	@Override
	public User mapToModel(LinkedTreeMap<String, Object> map) {
		User user = new User();
		user.name = map.get("name").toString();
		user.photo = getServerImgPath(getIntValue(map.get("id"))) + map.get("photo").toString();
		user.email = map.get("email").toString();
		user.usrId = getIntValue(map.get("id").toString());
		return user;
	}
	
	
	
}
