package com.concordiatec.vic.service;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.concordiatec.vic.constant.ApiURL;
import com.concordiatec.vic.listener.VicResponseHandler;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.LocalUser;
import com.concordiatec.vic.model.Shop;
import com.concordiatec.vic.model.ShopGroup;
import com.concordiatec.vic.model.ShopImage;
import com.concordiatec.vic.model.ShopScore;
import com.concordiatec.vic.tools.Tools;
import com.concordiatec.vic.util.HttpUtil;
import com.concordiatec.vic.util.LogUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.loopj.android.http.RequestParams;

public class ShopService extends HttpUtil {
	private Context context;
	public ShopService(Context context) {
		this.context = context;
	}
	
	public void likeShop(int shopId, VicResponseListener listener) {
		if (shopId > 0) {
			RequestParams params = new RequestParams();
			params.put("id", shopId);
			params.put("user", new UserService(context).getLoginUser().usrId);
			post(ApiURL.SHOP_LIKE, params, new VicResponseHandler(listener));
		}
	}
	
	@Override
	public List<Shop> mapListToModelList(ArrayList<LinkedTreeMap<String, Object>> list) {
		List<Shop> models = new ArrayList<Shop>();
		for (LinkedTreeMap<String, Object> tree : list) {
			models.add(mapToModel(tree));
		}
		return models;
	}

	@Override
	public Shop mapToModel(LinkedTreeMap<String, Object> map) {
		Shop s = new Shop();
		if (map.get("shop_id") != null) s.setId(getIntValue(map.get("shop_id")));
		if (map.get("area_id") != null) s.setAreaId(getIntValue(map.get("area_id")));
		if (map.get("group_id") != null) {
			ShopGroup sGroup = new ShopGroup();
			sGroup.setId(getIntValue(map.get("group_id")));
			if (map.get("group_name") != null) sGroup.setName(map.get("group_name").toString());
			if (map.get("group_price") != null) sGroup.setPrice(getIntValue(map.get("group_price")));
			if (map.get("group_food") != null) sGroup.setIsFood(getIntValue(map.get("group_food")));
			if (map.get("group_clean") != null) sGroup.setHasClean(getIntValue(map.get("group_clean")));
			s.setGroup(sGroup);
			sGroup = null;
		}
		if (map.get("user_id") != null) s.setShopUserId(getIntValue(map.get("user_id")));
		if (map.get("shop_fee") != null) s.setShopFee(getIntValue(map.get("shop_fee")));
		if (map.get("shop_phone") != null) s.setShopPhone(map.get("shop_phone").toString());
		if (map.get("shop_intro") != null) s.setShopIntro((List<String>) map.get("shop_intro"));
		if (map.get("shop_working") != null) s.setShopWorking(map.get("shop_working").toString());
		if (map.get("shop_addr1") != null) s.setShopAddr1(map.get("shop_addr1").toString());
		if (map.get("shop_addr2") != null) s.setShopAddr2(map.get("shop_addr2").toString());
		if (map.get("shop_lng") != null) s.setShopLng(Tools.getDoubleValue(map.get("shop_lng")));
		if (map.get("shop_lat") != null) s.setShopLat(Tools.getDoubleValue(map.get("shop_lat")));
		if (map.get("score_count") != null) s.setScoreCount(getIntValue(map.get("score_count")));
		if (map.get("like_count") != null) s.setLikeCount(getIntValue(map.get("like_count")));
		if (map.get("share_count") != null) s.setShareCount(getIntValue(map.get("share_count")));
		if (map.get("user_name") != null) s.setShopUserName(map.get("user_name").toString());
		
		if (map.get("user_photo") != null){
			String tmpString = getServerImgPath(s.getShopUserId() , map.get("user_photo").toString());
			s.setShopUserPhoto(tmpString);
		}
		
		if (map.get("area_name") != null) s.setAreaName(map.get("area_name").toString());
		if (map.get("distance") != null) s.setDistance(Tools.getDoubleValue(map.get("distance")));
		if (map.get("images") != null) {
			ShopImageService sImageService = new ShopImageService(context);
			List<ShopImage> imgs = sImageService.mapListToModelList((ArrayList<LinkedTreeMap<String, Object>>) map.get("images"));
			if (imgs != null && imgs.size() > 0) {
				for (int i = 0; i < imgs.size(); i++) {
					ShopImage sImage = imgs.get(i);
					String imgUrl = getServerImgPath(s.getShopUserId(), sImage.getName());
					sImage.setName(imgUrl);
					imgs.set(i, sImage);
				}
				s.setImages(imgs);
				s.setCoverImage(imgs.get(0));
			}
		}
		if (map.get("score_average") != null) {
			ShopScore score = new ShopScore();
			if (map.get("score_average") != null) score.setAvg(Tools.getDoubleValue(map.get("score_average")));
			if (map.get("score_price") != null) score.setPrice(Tools.getDoubleValue(map.get("score_price")));
			if (map.get("score_food") != null) score.setFood(Tools.getDoubleValue(map.get("score_food")));
			if (map.get("score_kind") != null) score.setKind(Tools.getDoubleValue(map.get("score_kind")));
			if (map.get("score_clean") != null) score.setKind(Tools.getDoubleValue(map.get("score_clean")));
			s.setScore(score);
			score = null;
		}
		boolean isLike = (map.get("is_like") != null && getIntValue(map.get("is_like")) > 0) ? true : false;
		s.setLike(isLike);
		return s;
	}
}
