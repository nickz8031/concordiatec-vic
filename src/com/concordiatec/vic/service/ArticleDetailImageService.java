package com.concordiatec.vic.service;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.base.HttpBase;
import com.concordiatec.vic.inf.VicServiceInterface;
import com.concordiatec.vic.model.ArticleImages;
import com.google.gson.internal.LinkedTreeMap;

public class ArticleDetailImageService extends HttpBase implements VicServiceInterface {
	
	private static ArticleDetailImageService adis;
	private String serverPath;
	private Context context;
	public ArticleDetailImageService(Context context , String serverPath) {
		this.serverPath = serverPath;
		this.context = context;
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
