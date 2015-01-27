package com.concordiatec.vic.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import android.content.Context;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.concordiatec.vic.constant.ApiURL;
import com.concordiatec.vic.inf.IVicService;
import com.concordiatec.vic.listener.VicResponseHandler;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.User;
import com.concordiatec.vic.tools.Tools;
import com.concordiatec.vic.util.EncryptUtil;
import com.concordiatec.vic.util.HttpUtil;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.StringUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UserService extends HttpUtil implements IVicService {
	private Context context;
	
	public UserService( Context context ){
		this.context = context;
	}
	
	public void signup( User user , VicResponseListener listener ){
		if( user != null ){
			RequestParams params = new RequestParams();
			params.put("email", user.email);
			params.put("password", user.pwd);
			params.put("name", user.name);
			params.put("sex", user.sex);
			
			post(ApiURL.SIGNUP, params, new VicResponseHandler(listener));
		}
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
		user.sex = getIntValue(map.get("sex"));
		user.photo = getServerImgPath(getIntValue(map.get("id"))) + map.get("photo").toString();
		user.email = map.get("email").toString();
		user.usrId = getIntValue(map.get("id").toString());
		user.isShop = getIntValue(map.get("is_shop")) == 1 ? true:false;
		
		if(user.isShop){
			user.shopId = Tools.getIntValue(map.get("shop_id"));
			user.shopGroupId = Tools.getIntValue(map.get("group_id"));
			user.areaId = Tools.getIntValue(map.get("area_id"));
			user.shopFee = Tools.getIntValue(map.get("shop_fee"));
			user.shopPhone = map.get("shop_phone").toString();
			user.shopIntro = map.get("shop_intro").toString();
			user.shopAddr1 = map.get("shop_addr1").toString();
			user.shopAddr2 = map.get("shop_addr2").toString();
			user.shopLng = Tools.getDoubleValue( map.get("shop_lng") );
			user.shopLat = Tools.getDoubleValue( map.get("shop_lat") );
			user.shopScores = Tools.getIntValue( map.get("shop_scores") );
			user.shopLikeCount = Tools.getIntValue( map.get("shop_likes") );
			user.shopShareCount = Tools.getIntValue( map.get("shop_shares") );
			user.shopStatus = Tools.getIntValue( map.get("shop_status") );
			user.shopCreated = map.get("shop_created").toString();
		}
		return user;
	}
	
	
	
}
