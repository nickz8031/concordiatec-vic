package com.concordiatec.vilnet.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import org.apache.http.Header;
import android.content.Context;
import com.concordiatec.vilnet.constant.ApiURL;
import com.concordiatec.vilnet.listener.VicResponseHandler;
import com.concordiatec.vilnet.listener.VicResponseListener;
import com.concordiatec.vilnet.model.Article;
import com.concordiatec.vilnet.model.LocalUser;
import com.concordiatec.vilnet.tools.Tools;
import com.concordiatec.vilnet.util.HttpUtil;
import com.concordiatec.vilnet.util.LogUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ArticleService extends HttpUtil {
	private UserService uService;
	public ArticleService( Context context ) {
		uService = new UserService(context);
	}
	
	public void deleteArticle( int articleId , VicResponseListener listener ){
		LocalUser loginUser = uService.getLoginUser();
		if( loginUser != null ){
			RequestParams params = new RequestParams();
			params.put("id", articleId);
			params.put("user", loginUser.usrId);
			post(ApiURL.ARTICLE_DELETE, params, new VicResponseHandler(listener));
		}
	}
	
	public void editArticle(Article article , VicResponseListener listener){
		LocalUser loginUser = uService.getLoginUser();
		if( loginUser != null ){
			RequestParams params = new RequestParams();
			params.put("id", article.getId());
			params.put("user", loginUser.usrId);
			params.put("content", article.getContent());
			post(ApiURL.ARTICLE_EDIT, params, new VicResponseHandler(listener));
		}
	}
	
	public void writeArticle( Article article , List<String> files , VicResponseListener listener ){
		LocalUser loginUser = uService.getLoginUser();
		if( files != null && files.size() > 0 && loginUser != null ){
			RequestParams params = new RequestParams();
			int d = 0;
			for (int i = 0 ; i < files.size(); i++) {
				String filePath = files.get(i);
				if(Tools.saveCompressBitmap(filePath)){
					File file = new File(filePath);
					if( file.exists() ){
						try {
							params.put(i+"", file);
							d++;
						} catch (FileNotFoundException e) {}
					}
				}
			}
			if( d == 0 ) return;
			params.put("writer", loginUser.usrId);
			params.put("comment", ( article.isAllowComment() ? 1 : 0 ));
			params.put("shop", article.getShopId());
			params.put("content", article.getContent());
			
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
		LocalUser loginUser = uService.getLoginUser();		
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
