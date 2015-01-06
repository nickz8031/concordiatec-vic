package com.concordiatec.vic.service;

import java.util.Map;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Context;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.requestinf.ArticleInf;
import com.concordiatec.vic.util.HttpUtil;
import com.concordiatec.vic.util.ResponseUtil;

public class ArticleService extends HttpUtil {
	private Context context;
	public ArticleService( Context context ) {
		this.context = context;
	}
	
	public void likeArticle( int usrId , int articleId , VicResponseListener listener ){
		if( usrId > 0 && articleId > 0 ){
			final VicResponseListener lis = listener;
			ArticleInf ai = restAdapter.create(ArticleInf.class);
			Map<String, String> postParams = getAuthMap();
			postParams.put("user_id", usrId+"");
			postParams.put("article_id", articleId+"");
			ai.likeArticle(postParams, new Callback<ResData>() {
				@Override
				public void success(ResData arg0, Response arg1) {
					ResponseUtil.processResp(arg0, lis);
				}
				@Override
				public void failure(RetrofitError arg0) {
					lis.onFailure(arg0.getMessage());
				}
			});
		}
		
		
	}
}
