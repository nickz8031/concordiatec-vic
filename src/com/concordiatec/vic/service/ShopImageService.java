package com.concordiatec.vic.service;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.concordiatec.vic.inf.IVicService;
import com.concordiatec.vic.model.ShopImage;
import com.concordiatec.vic.util.HttpUtil;
import com.google.gson.internal.LinkedTreeMap;

public class ShopImageService extends HttpUtil implements IVicService {
	@SuppressWarnings("unused")
	private Context context;
	public ShopImageService( Context context ) {
		this.context = context;
	}

	
	@Override
	public List<ShopImage> mapListToModelList(ArrayList<LinkedTreeMap<String, Object>> list) {
		List<ShopImage> models = new ArrayList<ShopImage>();
		for( LinkedTreeMap<String, Object> tree : list ){
			models.add( mapToModel( tree ) );
		}
		return models;
	}

	@Override
	public ShopImage mapToModel(LinkedTreeMap<String, Object> map) {
		ShopImage s = new ShopImage();
		if( map.get("img_id") != null ) s.setId( getIntValue(map.get("img_id")) );
		if( map.get("shop_id") != null ) s.setShopId( getIntValue( map.get("shop_id") ) );
		if( map.get("img_height") != null ) s.setHeight( getIntValue( map.get("img_height") ) );
		if( map.get("img_width") != null ) s.setWidth( getIntValue( map.get("img_width") ) );
		if( map.get("img_name") != null ) s.setName( map.get("img_name").toString() );
		return s;
	}}
