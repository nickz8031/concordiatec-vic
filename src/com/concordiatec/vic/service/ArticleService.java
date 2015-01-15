package com.concordiatec.vic.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import android.content.Context;
import com.concordiatec.vic.constant.ApiURL;
import com.concordiatec.vic.listener.VicResponseHandler;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.model.User;
import com.concordiatec.vic.util.HttpUtil;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.ProgressUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ArticleService extends HttpUtil {
	private Context context;
	private UserService uService;
	public ArticleService( Context context ) {
		this.context = context;
		uService = new UserService(context);
	}
	public void writeArticle( Article article , List<File> files , VicResponseListener listener ){
		
		User loginUser = uService.getLoginUser();
		if( loginUser != null ){
			RequestParams params = new RequestParams();
			
			params.put("writer", loginUser.usrId);
			params.put("comment", ( article.isAllowComment() ? 1 : 0 ));
			params.put("shop", article.getShopId());
			params.put("content", article.getContent());
			if( files != null && files.size() > 0 ){
				for (int i = 0; i < files.size(); i++) {
					try {
						params.put(i+"", files.get(i));
					} catch (FileNotFoundException e) {}
				}
			}else{
				return;
			}
//			post(ApiURL.ARTICLE_WRITE, params, new AsyncHttpResponseHandler() {
//				@Override
//				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//					// TODO Auto-generated method stub
//					LogUtil.show("success : " +new String(arg2));
//				}
//				
//				@Override
//				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//					// TODO Auto-generated method stub
//					LogUtil.show("failure : " + arg3.getMessage());
//				}
//			});
			post(ApiURL.ARTICLE_WRITE, params, new VicResponseHandler(listener));
			
		}
	}

	public void writeArticle( Article article , VicResponseListener listener ){
		writeArticle(article, null, listener);
	}
	
	/**
	 * 좋아요
	 * @param usrId
	 * @param articleId
	 * @param listener
	 */
	public void likeArticle( int articleId , VicResponseListener listener ){
		User loginUser = uService.getLoginUser();		
		if( loginUser!=null && articleId > 0  ){
			RequestParams params = new RequestParams();
			params.put("user", loginUser.usrId);
			params.put("id", articleId);
			post(ApiURL.ARTICLE_LIKE, params, new VicResponseHandler(listener));
		}
	}
	
	/**
	 * 좋아요
	 * @param usrId
	 * @param articleId
	 * @param listener
	 */
	public void likeArticle( int articleId ){
		likeArticle(articleId , null);
	}
}
