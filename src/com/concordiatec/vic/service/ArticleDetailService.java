package com.concordiatec.vic.service;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.concordiatec.vic.constant.ApiURL;
import com.concordiatec.vic.inf.VicServiceInterface;
import com.concordiatec.vic.listener.VicResponseHandler;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.model.ArticleImages;
import com.concordiatec.vic.model.User;
import com.concordiatec.vic.util.HttpUtil;
import com.concordiatec.vic.util.LogUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.loopj.android.http.RequestParams;

public class ArticleDetailService extends HttpUtil implements VicServiceInterface {

	private static ArticleDetailService ads;
	private Context context;
	
	public ArticleDetailService( Context context ) {
		this.context = context;
	}
	
	@Override
	public List<Article> mapListToModelList(ArrayList<LinkedTreeMap<String, Object>> list) {
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
		
		String serverPath = getServerImgPath(article.getWriterId());
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
	public void getDetail(int articleId ,VicResponseListener listener){
		RequestParams params = new RequestParams();
		if( articleId > 0 ){
			params.put("id", articleId);
		}else{
			LogUtil.show("error request[ no detail article id.]");
			return;
		}
		User loginUser = new UserService(context).getLoginUser();
		if( loginUser != null ){
			params.put("user", loginUser.usrId);
		}
		post(ApiURL.ARTICLE_DETAIL, params, new VicResponseHandler(listener));
		
	}	
}



