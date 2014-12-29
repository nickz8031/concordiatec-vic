package com.concordiatec.vic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Context;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.base.HttpBase;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.request.ArticleInf;
import com.concordiatec.vic.util.LogUtil;
import com.google.gson.internal.LinkedTreeMap;

public class ArticleService extends HttpBase {
	public static ArticleService ars;
	private Context context;
	
	private ArticleService( Context context ){
		this.context = context;
	}
	
	public void getArticles(VicResponseListener listener) {
		getArticles(listener, null); 
	}
	
	public void getArticles(VicResponseListener listener ,Map<String, String> params) {
		final VicResponseListener lis = listener;
		ArticleInf ai = restAdapter.create(ArticleInf.class);
		Map<String, String> postParams = getAuthMap();
		if( params != null ){
			for (Map.Entry<String, String> entry : params.entrySet()) {
				postParams.put(entry.getKey(), entry.getValue());
			}
		}
		ai.getArticles(postParams, new Callback<ResData>() {
			@Override
			public void success(ResData data, Response arg1) {
				
				switch (Integer.valueOf( data.getStatus() )) {
				case 0: //successful
					lis.onResponse(data.getData());
					break;
				case 402: //no data
					lis.onResponseNoData();
					break;
				default:
					LogUtil.show(data.getMsg() + " [code:" + data.getStatus() + "]");
					break;
				}
			}

			@Override
			public void failure(RetrofitError err) {
				LogUtil.show(err.getMessage());
			}
		});
	}
	
	public List<Article> transListToModel( List<LinkedTreeMap<String, Object>> list ){
		List<Article> models = new ArrayList<Article>();
		for( LinkedTreeMap<String, Object> tree : list ){
			models.add( mapToModel( tree ) );
		}
		return models;
	}

	@SuppressWarnings("unchecked")
	public Article mapToModel(LinkedTreeMap<String, Object> map) {
		Article article = new Article();
		article.setId( getIntValue(map.get("id")) );
		article.setKind( getIntValue(map.get("kind")) );
		article.setEffectId( getIntValue(map.get("effected_id")) );
		article.setContent(map.get("content").toString());
		article.setPastTime( getIntValue(map.get("pasttime")) );
		article.setWriterId( getIntValue(map.get("writer_id")) );
		article.setWriterName( map.get("writer_name").toString() );
		
		String pUrl = this.getServerImgPath(article.getWriterId() , map.get("writer_photo").toString());
		String cUrl = this.getServerImgPath(article.getWriterId() , map.get("image").toString());
		
		article.setWriterPhotoURL( pUrl );
		article.setCoverImageURL( cUrl );
		
		article.setShopId( getIntValue(map.get("shop_id")) );
		article.setShopAddr(map.get("shop_addr").toString());
		article.setShopGroupId( getIntValue(map.get("shop_group")) );
		article.setShopName(map.get("shop_name").toString());
		article.setLikeCount( getIntValue(map.get("like_count")) );
		article.setCommentCount( getIntValue(map.get("comment_count")) );
		
		boolean isLike = false;
		if( map.get("is_like").toString().trim().equals("1") ){
			isLike = true;
		}
		article.setLike( isLike );
		
		article.setLatestComments(CommentService.single().mapListToModelList((ArrayList<LinkedTreeMap<String,Object>>)map.get("lastest_comments")));
		
		return article;
	}
	
	
	public static ArticleService single( Context context ) {
		if (ars == null) {
			ars = new ArticleService(context);
		}
		return ars;
	}
}
