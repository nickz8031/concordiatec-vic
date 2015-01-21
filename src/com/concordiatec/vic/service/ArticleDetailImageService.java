package com.concordiatec.vic.service;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.concordiatec.vic.inf.IVicService;
import com.concordiatec.vic.model.ArticleImages;
import com.concordiatec.vic.util.HttpUtil;
import com.google.gson.internal.LinkedTreeMap;

public class ArticleDetailImageService extends HttpUtil implements IVicService {
	
	private static ArticleDetailImageService adis;
	private String serverPath;
//	private Context context;
	public ArticleDetailImageService(Context context , String serverPath) {
		this.serverPath = serverPath;
//		this.context = context;
	}

	@Override
	public List<ArticleImages> mapListToModelList(ArrayList<LinkedTreeMap<String, Object>> list) {
		List<ArticleImages> models = new ArrayList<ArticleImages>();
		for( LinkedTreeMap<String, Object> tree : list ){
			models.add( mapToModel( tree ) );
		}
		return models;
	}

	@Override
	public ArticleImages mapToModel(LinkedTreeMap<String, Object> map) {
		ArticleImages aImages = new ArticleImages();
		String imgURL = serverPath + map.get("name").toString();
		aImages.setId( getIntValue(map.get("id")) );
		aImages.setName( imgURL );
		aImages.setWidth( getIntValue(map.get("width")) );
		aImages.setHeight( getIntValue(map.get("height")) );
		return aImages;
	}
	
	/**
	 * get singleton
	 * @param context
	 * @return
	 */
	public static ArticleDetailImageService single( Context context , String serverPath ) {
		if (adis == null) {
			adis = new ArticleDetailImageService(context , serverPath);
		}
		adis.serverPath = serverPath;
		return adis;
	}
}
