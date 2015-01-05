package com.concordiatec.vic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Context;
import com.concordiatec.vic.inf.VicServiceInterface;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.model.ArticleImages;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.requestinf.ArticleInf;
import com.concordiatec.vic.util.HttpUtil;
import com.concordiatec.vic.util.LogUtil;
import com.google.gson.internal.LinkedTreeMap;

public class ArticleDetailService extends HttpUtil implements VicServiceInterface {

	private static ArticleDetailService ads;
	private Context context;
	
	public ArticleDetailService( Context context ) {
		this.context = context;
	}
	
	@Override
	public List<Article> mapListToModelList(ArrayList<LinkedTreeMap<String, Object>> list) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * parse Map data to java bean model which from detail
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Article mapToModel(LinkedTreeMap<String, Object> map) {
		Article article = new Article();
		article.setId( getIntValue(map.get("id")) );
		article.setKind( getIntValue(map.get("kind")) );
		article.setEffectId( getIntValue(map.get("effected_id")) );
		article.setContent(map.get("content").toString());
		article.setPastTime( getIntValue(map.get("pasttime")) );
		article.setWriterId( getIntValue(map.get("writer_id")) );
		article.setWriterName( map.get("writer_name").toString() );
		
		String serverPath = this.getServerImgPath(article.getWriterId());
		String pUrl = serverPath + map.get("writer_photo").toString();
		article.setWriterPhotoURL( pUrl );
		
		ArrayList<LinkedTreeMap<String,Object>> imgList = (ArrayList<LinkedTreeMap<String,Object>>)map.get("images");
		List<ArticleImages> tImageList = ArticleDetailImageService.single(context, serverPath).mapListToModelList(imgList);
		article.setImages( tImageList );
		
		article.setShopId( getIntValue(map.get("shop_id")) );
		article.setShopAddr(map.get("shop_addr").toString());
		article.setShopGroupId( getIntValue(map.get("shop_group")) );
		article.setShopName(map.get("shop_name").toString());
		article.setLikeCount( getIntValue(map.get("like_count")) );
		article.setCommentCount( getIntValue(map.get("comment_count")) );
		
		boolean isLike = false;
		if( map.get("is_like").toString().trim().equals("1.0") ){
			isLike = true;
		}
		article.setLike( isLike );
		
		return article;
	}
	
	/**
	 * get article detail
	 * @param listener
	 * @param articleId article id
	 */
	public void getDetail(VicResponseListener listener ,int articleId){
		final VicResponseListener lis = listener;
		ArticleInf ai = restAdapter.create(ArticleInf.class);
		Map<String, String> postParams = getAuthMap();
		if( articleId > 0 ){
			postParams.put("article_id", articleId+"");
		}else{
			LogUtil.show("error request[ no detail article id.]");
			return;
		}
		ai.getDetail(postParams, new Callback<ResData>() {

			@Override
			public void failure(RetrofitError err) {
				LogUtil.show(err.getMessage());
			}
			@Override
			public void success(ResData data, Response arg1) {
				switch (Integer.valueOf( data.getStatus() )) {
					case 0: //successful
						lis.onResponse(data.getData());
						break;
					case 1: //no data
						lis.onResponseNoData();
						break;
					case 402: //no data
						lis.onResponseNoData();
						break;
					default:
						LogUtil.show(data.getMsg() + " [code:" + data.getStatus() + "]");
						break;
			}
		}});
	}
	
	/**
	 * get singleton
	 * @param context
	 * @return
	 */
	public static ArticleDetailService single( Context context ) {
		if (ads == null) {
			ads = new ArticleDetailService(context);
		}
		return ads;
	}
	
}



