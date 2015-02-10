package com.concordiatec.vilnet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import android.content.Context;
import com.bumptech.glide.Glide;
import com.concordiatec.vilnet.constant.ApiURL;
import com.concordiatec.vilnet.inf.IVicService;
import com.concordiatec.vilnet.listener.VicResponseHandler;
import com.concordiatec.vilnet.listener.VicResponseListener;
import com.concordiatec.vilnet.model.Article;
import com.concordiatec.vilnet.model.ArticleImages;
import com.concordiatec.vilnet.model.Comment;
import com.concordiatec.vilnet.model.LastestComment;
import com.concordiatec.vilnet.model.LocalUser;
import com.concordiatec.vilnet.model.ResData;
import com.concordiatec.vilnet.tools.Tools;
import com.concordiatec.vilnet.util.HttpUtil;
import com.concordiatec.vilnet.util.LogUtil;
import com.concordiatec.vilnet.util.ResponseUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

@SuppressWarnings("unused")
public class ArticleListService extends HttpUtil{
	public static ArticleListService ars;
	private Context context;
	
	public ArticleListService( Context context ){
		this.context = context;
	}
	/**
	 * get article list
	 * @param listener
	 * @param params post parameters
	 */
	public void getArticles(Map<String, String> p , VicResponseListener listener) {
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
		post(ApiURL.ARTICLE_LIST, params, new VicResponseHandler(listener));
	}
	
	/**
	 * get article list
	 * @param listener
	 * @param params post parameters
	 */
	public void getArticles(VicResponseListener listener) {
		getArticles(null, listener);
	}
	
	
		
	/**
	 * parse Map data to java bean model which from list data
	 * @param map
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Article mapToModel(LinkedTreeMap<String, Object> map) {
		Article article = new Article();
		if( map.get("id") != null ) article.setId( getIntValue(map.get("id")) );
		if( map.get("kind") != null ) article.setKind( getIntValue(map.get("kind")) );
		if( map.get("effected_id") != null ) article.setEffectId( getIntValue(map.get("effected_id")) );
		if( map.get("content") != null ) article.setContent(map.get("content").toString());
		if( map.get("pasttime") != null ) article.setPastTime( getIntValue(map.get("pasttime")) );
		if( map.get("writer_id") != null ) article.setWriterId( getIntValue(map.get("writer_id")) );
		if( map.get("writer_is_shop") != null ) article.setWriterIsShop( getIntValue(map.get("writer_is_shop"))>0 ? true : false );
		if( map.get("writer_shop_id") != null ) article.setWriterShopId( getIntValue(map.get("writer_shop_id")) );
		if( map.get("writer_name") != null ) article.setWriterName( map.get("writer_name").toString() );
		
		
		if( map.get("writer_photo") != null ){
			String pUrl = getServerImgPath(article.getWriterId() , map.get("writer_photo").toString());
			article.setWriterPhotoURL( pUrl );
		}
		if( map.get("width") != null ) article.setCoverImageWidth( getIntValue(map.get("width")) );
		if( map.get("height") != null ) article.setCoverImageHeight( getIntValue(map.get("height")) );
		
		if( map.get("image") != null ){
			String cUrl = getServerImgPath(article.getWriterId() , map.get("image").toString());
			article.setCoverImageURL( cUrl );
		} 
		
		if( map.get("shop_id") != null ) article.setShopId( getIntValue(map.get("shop_id")) );
		if( map.get("shop_addr") != null ) article.setShopAddr(map.get("shop_addr").toString());
		if( map.get("shop_group") != null ) article.setShopGroupId( getIntValue(map.get("shop_group")) );
		if( map.get("shop_name") != null ) article.setShopName(map.get("shop_name").toString());
		if( map.get("like_count") != null ) article.setLikeCount( getIntValue(map.get("like_count")) );
		if( map.get("comment_count") != null ) article.setCommentCount( getIntValue(map.get("comment_count")) );
		
		boolean isLike = false;
		if( map.get("is_like") != null && getIntValue(map.get("is_like")) == 1 ){
			isLike = true;
		}
		article.setLike( isLike );
		
		if( map.get("lastest_comments") != null ){
			ArrayList<LinkedTreeMap<String, Object>> commentList = (ArrayList<LinkedTreeMap<String,Object>>)map.get("lastest_comments");
			List<LastestComment> commList = new LastestCommentService(context).mapListToModelList( commentList );
			article.setLatestComments( commList );
		}
		
		return article;
	}

	@Override
	public List<Article> mapListToModelList(ArrayList<LinkedTreeMap<String, Object>> list) {
		List<Article> models = new ArrayList<Article>();
		for( LinkedTreeMap<String, Object> tree : list ){
			models.add( mapToModel( tree ) );
		}
		return models;
	}
}
