package com.concordiatec.vilnet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.content.Context;
import com.concordiatec.vilnet.R;
import com.concordiatec.vilnet.constant.ApiURL;
import com.concordiatec.vilnet.listener.SimpleVicResponseListener;
import com.concordiatec.vilnet.listener.VicResponseHandler;
import com.concordiatec.vilnet.listener.VicResponseListener;
import com.concordiatec.vilnet.model.Coupon;
import com.concordiatec.vilnet.model.LocalUser;
import com.concordiatec.vilnet.model.Shop;
import com.concordiatec.vilnet.model.ShopGroup;
import com.concordiatec.vilnet.tools.Tools;
import com.concordiatec.vilnet.util.HttpUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.loopj.android.http.RequestParams;

public class CouponService extends HttpUtil{
	private Context context;
	public CouponService( Context context ) {
		this.context = context;
	}
	
	public void getCouponDetail( int id , VicResponseListener listener ){
		RequestParams params = new RequestParams();
		LocalUser loginUser = new UserService(context).getLoginUser();
		if( loginUser != null ){
			params.put("user", loginUser.usrId);
		}
		params.put("id", id);
		post(ApiURL.COUPON_DETAIL, params, new VicResponseHandler(listener));
	}
	
	/**
	 * get coupon list
	 * @param p
	 * @param listener
	 */
	public void getCoupons(Map<String, String> p , VicResponseListener listener) {
		RequestParams params = new RequestParams();
		LocalUser loginUser = new UserService(context).getLoginUser();
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
	
	public void downCoupon( int id , VicResponseListener listener ){
		LocalUser loginUser = new UserService(context).getLoginUser();
		if( loginUser != null ){
			RequestParams params = new RequestParams();
			params.put("user", loginUser.usrId);
			params.put("id", id);
			post(ApiURL.COUPON_DOWN, params, new VicResponseHandler(listener));
		}
	}
	
	public void likeCoupon( int id , VicResponseListener listener ){
		LocalUser loginUser = new UserService(context).getLoginUser();
		if( loginUser != null ){
			RequestParams params = new RequestParams();
			params.put("user", loginUser.usrId);
			params.put("id", id);
			post(ApiURL.COUPON_LIKE, params, new VicResponseHandler(listener));
		}
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
		if( map.get("coupon_id") != null ) coupon.setId( getIntValue(map.get("coupon_id")) );
		if( map.get("shop_id") != null ) coupon.setShopId( getIntValue(map.get("shop_id")) );
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
		if( map.get("notice") != null ) coupon.setNotice( (List<String>) map.get("notice") );
		
		Shop shop = new Shop();
		ShopGroup sGroup = new ShopGroup();
		if( map.get("group_id") != null ) sGroup.setId( getIntValue(map.get("group_id")) );
		if( map.get("group_name") != null ) sGroup.setName( map.get("group_name").toString() );
		
		shop.setGroup(sGroup);
		if( map.get("shop_name") != null ) shop.setShopUserName( map.get("shop_name").toString() );
		if( map.get("shop_photo") != null ){
			String imgUrl = getServerImgPath(coupon.getUserId() , map.get("shop_photo").toString());
			shop.setShopUserPhoto( imgUrl );
		}
		if( map.get("shop_addr1") != null ) shop.setShopAddr1( map.get("shop_addr1").toString() );
		if( map.get("shop_addr2") != null ) shop.setShopAddr2( map.get("shop_addr2").toString() );
		if( map.get("lng") != null ) shop.setShopLng( Tools.getDoubleValue(map.get("lng")) );
		if( map.get("lat") != null ) shop.setShopLat( Tools.getDoubleValue(map.get("lat")) );
		if( map.get("shop_intro") != null ) shop.setShopIntro( (List<String>) map.get("shop_intro") );
		coupon.setShop(shop);
		
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
	
	public static int getTagColor(Context context ,int kind){
		int color;
		switch ( kind ) {
			case 1:
				color = context.getResources().getColor(R.color.theme_color);
				break;
			default:
				color = context.getResources().getColor(R.color.effect_color);
				break;
		}
		return color;
	}
	
}
