package com.concordiatec.vic.request;

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
}