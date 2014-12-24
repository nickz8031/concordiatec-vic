package com.concordiatec.vic.service;

import java.util.Map;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import com.concordiatec.vic.base.HttpBase;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.request.ArticleInf;
import com.concordiatec.vic.util.LogUtil;
import com.google.gson.internal.LinkedTreeMap;

public class ArticleService extends HttpBase {
	public static ArticleService ars;
	
	public void getArticles(VicResponseListener listener) {
		final VicResponseListener lis = listener;
		ArticleInf ai = restAdapter.create(ArticleInf.class);
		Map<String, String> params = getAuthMap();
		ai.getArticles(params, new Callback<ResData>() {
			@Override
			public void success(ResData data, Response arg1) {
				if (data.getStatus().equals("0")) {
					lis.onResponse(data.getData());
				} else {
					LogUtil.show(data.getMsg() + " [code:" + data.getStatus() + "]");
				}
			}

			@Override
			public void failure(RetrofitError err) {
				LogUtil.show(err.getMessage());
			}
		});
	}

	public void mapToBean(LinkedTreeMap<String, String> map) {
		Article article = new Article();
		article.setId(Integer.valueOf(map.get("id")));
		article.setKind(Integer.valueOf(map.get("kind")));
		article.setEffectId(Integer.valueOf(map.get("effected_id")));
		article.setContent(map.get("content"));
		article.setPastTime(Integer.valueOf(map.get("pasttime")));
		article.setWriterId(Integer.valueOf(map.get("writer_id")));
		article.setWriterName(map.get("writer_name"));
		article.setWriterPhotoURL( this.getServerImgPath(article.getWriterId()) + map.get("writer_photo"));
		article.setCoverImageURL( this.getServerImgPath(article.getWriterId()) + map.get("image") );
		article.setShopId( Integer.valueOf(map.get("shop_id")) );
		article.setShopAddr( map.get("shop_addr") );
		article.setShopGroupId( Integer.valueOf(map.get("shop_group")) );
		article.setShopName( map.get("shop_name") );
		article.setLikeCount( Integer.valueOf( map.get("like_count") ) );
		article.setCommentCount( Integer.valueOf( map.get("comment_count") ) );
//		article.setLatestComments( CommentService.single().mapListToBeanList( map.get("lastest_comments") ) );
	}
	
	

	public static ArticleService single() {
		if (ars == null) {
			ars = new ArticleService();
		}
		return ars;
	}
}
