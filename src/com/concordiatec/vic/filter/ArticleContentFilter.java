package com.concordiatec.vic.filter;

import java.util.List;
import android.content.Context;
import com.concordiatec.vic.R;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.util.NotifyUtil;

public class ArticleContentFilter {
	private static ArticleContentFilter articleWriteFilter;
	private Context context;
	public ArticleContentFilter(Context context) {
		this.context = context;
	}
	
	public Article filterWrite( Article article , List<String> picList ){
		if( picList.size() == 0 ){ //사진은 적어도 한개
			NotifyUtil.toast(context, context.getResources().getString(R.string.select_image_least_one));
		}
		//filter content
		article.setContent( filterContent(article.getContent()) );
		return article;
	}
	
	private String filterContent( String content ){
		return content.trim();
	}
	
	/**
	 * singleton
	 * @return
	 */
	public static ArticleContentFilter single(Context context){
		if( articleWriteFilter == null ){
			articleWriteFilter = new ArticleContentFilter(context);
		}
		return articleWriteFilter;
	}
}
