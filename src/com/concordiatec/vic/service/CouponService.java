package com.concordiatec.vic.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import android.content.Context;
import com.concordiatec.vic.constant.ApiURL;
import com.concordiatec.vic.inf.IVicService;
import com.concordiatec.vic.listener.VicResponseHandler;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.Coupon;
import com.concordiatec.vic.model.User;
import com.concordiatec.vic.tools.Tools;
import com.concordiatec.vic.util.HttpUtil;
import com.concordiatec.vic.util.LogUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CouponService extends HttpUtil implements IVicService  {
	private Context context;
	public CouponService( Context context ) {
		this.context = context;
	}
	public void getCoupons(Map<String, String> p , VicResponseListener listener) {
		RequestParams params = new RequestParams();
		User loginUser = new UserService(context).getLoginUser();
		if( loginUser != null ){
			params.put("user", loginUser.usrId);
		}
		if( p != null && p.size() > 0 ){
			for (Map.Entry<String, String> entry : p.entrySet()) {
				params.put(entry.getKey(), entry.getValue());
			}
		}
		post(ApiURL.COUPON_LIST, params, new VicResponseHandler(listener));
	}
	
	public void getCoupons(VicResponseListener listener) {
		getCoupons(null , listener);
	}
	@Override
	public List<Coupon> mapListToModelList(ArrayList<LinkedTreeMap<String, Object>> list) {
		List<Coupon> models = new ArrayList<Coupon>();
		for( LinkedTreeMap<String, Object> tree : list ){
			models.add( mapToModel( tree ) );
		}
		return models;
	}
	@Override
	public Coupon mapToModel(LinkedTreeMap<String, Object> map) {
		Coupon coupon = new Coupon();
		if( map.get("id") != null ) coupon.setId( getIntValue(map.get("coupon_id")) );
		if( map.get("user_id") != null ) coupon.setUserId( getIntValue(map.get("user_id")) );
		if( map.get("name") != null ) coupon.setName( map.get("name").toString() );
		if( map.get("image") != null ){
			String imgUrl = getServerImgPath(coupon.getUserId() , map.get("image").toString());
			coupon.setImage( imgUrl );
		}
		if( map.get("width") != null ) coupon.setImageWidth( getIntValue(map.get("width")) );
		if( map.get("height") != null ) coupon.setImageHeight( getIntValue(map.get("height")) );
		
		if( map.get("price") != null ) coupon.setPrice( getIntValue(map.get("discount_price")) );
		if( map.get("listprice") != null ) coupon.setListPrice( getIntValue(map.get("listprice")) );
		if( map.get("quantity") != null ) coupon.setQuantity( getIntValue(map.get("quantity")) );
		if( map.get("total") != null ) coupon.setTotal( getIntValue(map.get("total")) );
		if( map.get("sell_start") != null ) coupon.setStartTime( map.get("sell_start").toString() );
		if( map.get("sell_end") != null ) coupon.setEndTime( map.get("sell_end").toString() );
		if( map.get("use_start") != null ) coupon.setUseStart( map.get("use_start").toString() );
		if( map.get("use_end") != null ) coupon.setUseEnd( map.get("use_end").toString() );
		if( map.get("sold_out") != null ) coupon.setSurPlusTime( getIntValue(map.get("sold_out")) );
		if( map.get("kind") != null ) coupon.setKind( getIntValue(map.get("kind")) );
		if( map.get("kind_name") != null ) coupon.setKindName( map.get("kind_name").toString() );
		if( map.get("like_count") != null ) coupon.setLikeCount( getIntValue(map.get("like_count")) );
		if( map.get("share_count") != null ) coupon.setShareCount( getIntValue(map.get("share_count")) );
		if( map.get("group_id") != null ) coupon.setGroupId( getIntValue(map.get("group_id")) );
		if( map.get("group_name") != null ) coupon.setGroupName( map.get("group_name").toString() );

		if( map.get("shop_id") != null ) coupon.setShopId( getIntValue(map.get("shop_id")) );
		coupon.setShopName( map.get("shop_name").toString() );
		if( map.get("shop_photo") != null ){
			String imgUrl = getServerImgPath(coupon.getUserId() , map.get("shop_photo").toString());
			coupon.setShopPhoto( imgUrl );
		}
		if( map.get("notice") != null ) coupon.setNotice( (List<String>) map.get("notice") );
		if( map.get("shop_addr1") != null ) coupon.setShopAddr1( map.get("shop_addr1").toString() );
		if( map.get("shop_addr2") != null ) coupon.setShopAddr2( map.get("shop_addr2").toString() );
		if( map.get("lng") != null ) coupon.setShopLongitude( Tools.getDoubleValue(map.get("lng")) );
		if( map.get("lat") != null ) coupon.setShopLatitude( Tools.getDoubleValue(map.get("lat")) );
		
		boolean flag = false;
		if( map.get("have") != null && getIntValue(map.get("have")) == 1 ){
			flag = true;
		}
		coupon.setKeep( flag );		
		flag = false;
		if( map.get("is_like") != null && getIntValue(map.get("is_like")) == 1 ){
			flag = true;
		}
		coupon.setLike( flag );		
		return coupon;
		
	}
	
	
	
}
