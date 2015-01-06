package com.concordiatec.vic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Context;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.concordiatec.vic.inf.VicServiceInterface;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.model.User;
import com.concordiatec.vic.requestinf.UserInf;
import com.concordiatec.vic.util.EncryptUtil;
import com.concordiatec.vic.util.HttpUtil;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.ResponseUtil;
import com.concordiatec.vic.util.StringUtil;
import com.google.gson.internal.LinkedTreeMap;

public class UserService extends HttpUtil implements VicServiceInterface {
	private Context context;
	
	public UserService( Context context ){
		this.context = context;
	}
	public void doLogin(String account , String pwd , VicResponseListener listener){
		final VicResponseListener lis = listener;
		UserInf uInf = restAdapter.create(UserInf.class);
		Map<String, String> postParams = getAuthMap();
		if( StringUtil.isEmpty(account) || StringUtil.isEmpty(pwd) ){
			LogUtil.show("error[ account or password cannot empty.]");
			return;
		}
		postParams.put("email", account);
		postParams.put("password", EncryptUtil.EncPwd(pwd) );
		
		uInf.login(postParams, new Callback<ResData>() {
			@Override
			public void success(ResData data, Response arg1) {
				ResponseUtil.processResp(data, lis);
			}
			@Override
			public void failure(RetrofitError err) {
				lis.onFailure(err.getMessage());
			}
		});
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
