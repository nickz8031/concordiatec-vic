package com.concordiatec.vilnet.service;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.concordiatec.vilnet.constant.ApiURL;
import com.concordiatec.vilnet.listener.VicResponseHandler;
import com.concordiatec.vilnet.listener.VicResponseListener;
import com.concordiatec.vilnet.model.Article;
import com.concordiatec.vilnet.model.ArticleImages;
import com.concordiatec.vilnet.model.LocalUser;
import com.concordiatec.vilnet.util.HttpUtil;
import com.concordiatec.vilnet.util.LogUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.loopj.android.http.RequestParams;

public class ArticleDetailService extends HttpUtil{
	private Context context;

	public ArticleDetailService(Context context) {
		this.context = context;
	}

	/**
	 * parse Map data to java bean model which from detail
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Article mapToModel(LinkedTreeMap<String, Object> map) {
		Article article = new Article();
		if (map.get("id") != null) article.setId(getIntValue(map.get("id")));
		if (map.get("kind") != null) article.setKind(getIntValue(map.get("kind")));
		if (map.get("effected_id") != null) article.setEffectId(getIntValue(map.get("effected_id")));
		if (map.get("content") != null) article.setContent(map.get("content").toString());
		if (map.get("pasttime") != null) article.setPastTime(getIntValue(map.get("pasttime")));
		if (map.get("writer_id") != null) article.setWriterId(getIntValue(map.get("writer_id")));
		if (map.get("writer_name") != null) article.setWriterName(map.get("writer_name").toString());
		String serverPath = getServerImgPath(article.getWriterId());
		if (map.get("writer_photo") != null) {
			String pUrl = serverPath + map.get("writer_photo").toString();
			article.setWriterPhotoURL(pUrl);
		}
		if (map.get("images") != null) {
			ArrayList<LinkedTreeMap<String, Object>> imgList = (ArrayList<LinkedTreeMap<String, Object>>) map.get("images");
			List<ArticleImages> tImageList = ArticleDetailImageService.single(context, serverPath).mapListToModelList(imgList);
			article.setImages(tImageList);
			int minHeight = 0;
			if (tImageList.size() > 0) {
				minHeight = tImageList.get(0).getHeight();
				for (int i = 1; i < tImageList.size(); i++) {
					if (minHeight > tImageList.get(i).getHeight()) {
						minHeight = tImageList.get(i).getHeight();
					}
				}
			}
			article.setMinHeight(minHeight);
		}
		if (map.get("shop_id") != null) article.setShopId(getIntValue(map.get("shop_id")));
		if (map.get("shop_addr") != null) article.setShopAddr(map.get("shop_addr").toString());
		if (map.get("shop_group") != null) article.setShopGroupId(getIntValue(map.get("shop_group")));
		if (map.get("shop_name") != null) article.setShopName(map.get("shop_name").toString());
		if (map.get("like_count") != null) article.setLikeCount(getIntValue(map.get("like_count")));
		if (map.get("comment_count") != null) article.setCommentCount(getIntValue(map.get("comment_count")));
		if (map.get("is_like") != null) {
			boolean isLike = false;
			if ( getIntValue(map.get("is_like")) == 1 ) {
				isLike = true;
			}
			article.setLike(isLike);
		}
		return article;
	}

	/**
	 * get article detail
	 * 
	 * @param listener
	 * @param articleId
	 *            article id
	 */
	public void getDetail(int articleId, VicResponseListener listener) {
		RequestParams params = new RequestParams();
		if (articleId > 0) {
			params.put("id", articleId);
		} else {
			LogUtil.show("error request[ no detail article id.]");
			return;
		}
		LocalUser loginUser = new UserService(context).getLoginUser();
		if (loginUser != null) {
			params.put("user", loginUser.usrId);
		}
		post(ApiURL.ARTICLE_DETAIL, params, new VicResponseHandler(listener));
	}
}
