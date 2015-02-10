package com.concordiatec.vilnet.service;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.concordiatec.vilnet.model.ShopImage;
import com.concordiatec.vilnet.util.HttpUtil;
import com.google.gson.internal.LinkedTreeMap;

public class ShopImageService extends HttpUtil{
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
		if( map.get("id") != null ) s.setId( getIntValue(map.get("id")) );
		if( map.get("height") != null ) s.setHeight( getIntValue( map.get("height") ) );
		if( map.get("width") != null ) s.setWidth( getIntValue( map.get("width") ) );
		if( map.get("name") != null ) s.setName( map.get("name").toString() );
		return s;
	}}
