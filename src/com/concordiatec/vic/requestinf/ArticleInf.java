//package com.concordiatec.vic.requestinf;
//
//import java.util.Map;
//import com.concordiatec.vic.model.ResData;
//import retrofit.Callback;
//import retrofit.http.FieldMap;
//import retrofit.http.FormUrlEncoded;
//import retrofit.http.Multipart;
//import retrofit.http.POST;
//import retrofit.http.Part;
//import retrofit.http.PartMap;
//import retrofit.mime.TypedFile;
//
//public interface ArticleInf {
//	@FormUrlEncoded
//	@POST("/?r=article")
//	void getArticles(@FieldMap Map<String, String> map, Callback<ResData> response);
//	
//	@FormUrlEncoded
//	@POST("/?r=article/view")
//	void getDetail(@FieldMap Map<String, String> map , Callback<ResData> response);
//	
//	@FormUrlEncoded
//	@POST("/?r=article/like")
//	void likeArticle(@FieldMap Map<String, String> map , Callback<ResData> response);
//	
//	@Multipart
//	@FormUrlEncoded
//	@POST("/uploadTest.php")
//	void writeArticle( @PartMap @FieldMap Map<String, Object> data , Callback<ResData> response );
//	
////	@Multipart
////	@POST("/uploadTest.php")
////	void writeArticle( @PartMap Map<String, TypedFile> files , @FieldMap Map<String, String> info , Callback<String> response );
//}
