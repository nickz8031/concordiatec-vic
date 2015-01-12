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
	public void writeArticle( Article article , List<String> filesPath , VicResponseListener listener ){
		
		LogUtil.show(filesPath.toString());
		
		User loginUser = uService.getLoginUser();
		if( loginUser != null ){
			RequestParams params = new RequestParams();
			
			params.put("writer", loginUser.usrId);
			params.put("comment", ( article.isAllowComment() ? 1 : 0 ));
			params.put("shop", article.getShopId());
			params.put("content", article.getContent());
			
			int d = 0 ;
			if( filesPath != null && filesPath.size() > 0 ){
				
				for (int i = 0; i < filesPath.size(); i++) {
					File f = new File(filesPath.get(i));
					if( f.exists() ){
						try {
							params.put(d+"", f);
							d++;
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}			
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
