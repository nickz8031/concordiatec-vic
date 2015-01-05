package com.concordiatec.vic.requestinf;

import java.util.Map;
import retrofit.Callback;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import com.concordiatec.vic.model.ResData;

public interface UserInf {
	@FormUrlEncoded
	@POST("/?r=login")
	void login(@FieldMap Map<String, String> map, Callback<ResData> response);
}
