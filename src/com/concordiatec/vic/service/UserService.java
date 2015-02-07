package com.concordiatec.vic.service;

import android.content.Context;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.concordiatec.vic.constant.ApiURL;
import com.concordiatec.vic.helper.BroadHelper;
import com.concordiatec.vic.listener.VicResponseHandler;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.LocalUser;
import com.concordiatec.vic.tools.Tools;
import com.concordiatec.vic.util.EncryptUtil;
import com.concordiatec.vic.util.HttpUtil;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.StringUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.loopj.android.http.RequestParams;

public class UserService extends HttpUtil{
	private Context context;
	
	public UserService( Context context ){
		this.context = context;
	}
	
	public void signup( LocalUser user , VicResponseListener listener ){
		if( user != null ){
			RequestParams params = new RequestParams();
			params.put("email", user.email);
			params.put("password", user.pwd);
			params.put("name", user.name);
			params.put("sex", user.sex);
			
			post(ApiURL.SIGNUP, params, new VicResponseHandler(listener));
		}
	} 
	
	public void getUserInfo( int userId , VicResponseListener listener ){
		RequestParams params = new RequestParams();
		params.put("id", userId);
		post(ApiURL.USER_INFO, params, new VicResponseHandler(listener));
	}
	
	/**
	 * login
	 * @param account
	 * @param pwd
	 * @param listener
	 */
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
	/**
	 * login user information from location
	 * @return
	 */
	public LocalUser getLoginUser(){
		return new Select().from(LocalUser.class).executeSingle();
	}
	
	public void login( LocalUser usr ){
		clearData();
		usr.save();
		BroadHelper.sendOnlineBroad(context , true);
	}
	
	
	public void logout(){
		clearData();
		BroadHelper.sendOnlineBroad(context , false);
	}
	
	private void clearData(){
		new Delete().from(LocalUser.class).where("Id > ?", 0).execute();
	}

	@Override
	public LocalUser mapToModel(LinkedTreeMap<String, Object> map) {
		LocalUser user = new LocalUser();
		user.name = map.get("name").toString();
		user.sex = getIntValue(map.get("sex"));
		if( map.get("photo") != null && map.get("photo").toString().length() > 0 ){
			user.photo = getServerImgPath(getIntValue(map.get("id"))) + map.get("photo").toString();
		}
		user.email = map.get("email").toString();
		user.usrId = getIntValue(map.get("id").toString());
		user.isShop = getIntValue(map.get("is_shop")) == 1 ? true:false;
		
		if(user.isShop){
			user.shopId = Tools.getIntValue(map.get("shop_id"));
			user.shopPhone = map.get("shop_phone").toString();
			user.shopAddr1 = map.get("shop_addr1").toString();
			user.shopAddr2 = map.get("shop_addr2").toString();
			user.shopLng = Tools.getDoubleValue( map.get("shop_lng") );
			user.shopLat = Tools.getDoubleValue( map.get("shop_lat") );
			user.shopLikeCount = Tools.getIntValue( map.get("shop_likes") );
			user.shopShareCount = Tools.getIntValue( map.get("shop_shares") );
			user.shopScoreCount = Tools.getIntValue( map.get("score_count") );
			user.isOpen = getIntValue(map.get("shop_open")) == 1 ? true:false;
			user.isPause = getIntValue(map.get("shop_pause")) == 1 ? true:false;
			user.groupName = map.get("group_name").toString();
		}
		return user;
	}
	
	
	
}
