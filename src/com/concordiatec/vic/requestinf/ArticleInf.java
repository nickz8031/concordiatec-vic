package com.concordiatec.vic.requestinf;

import java.util.Map;
import com.concordiatec.vic.model.ResData;
import retrofit.Callback;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface ArticleInf {
	@FormUrlEncoded
	@POST("/?r=article")
	void getArticles(@FieldMap Map<String, String> map, Callback<ResData> response);
	
	@FormUrlEncoded
	@POST("/?r=article/view")
	void getDetail(@FieldMap Map<String, String> map , Callback<ResData> response);
}
