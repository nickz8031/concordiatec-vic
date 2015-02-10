package com.concordiatec.vilnet.filter;

import android.content.Context;
import com.concordiatec.vilnet.model.Article;

public class ArticleContentFilter {
	private static ArticleContentFilter articleWriteFilter;
//	private Context context;
	public ArticleContentFilter(Context context) {
//		this.context = context;
	}
	
	public Article filterWrite( Article article ){
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
