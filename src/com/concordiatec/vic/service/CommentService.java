package com.concordiatec.vic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Context;
import com.concordiatec.vic.inf.VicServiceInterface;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.Comment;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.requestinf.CommentInf;
import com.concordiatec.vic.util.HttpUtil;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.ResponseUtil;
import com.google.gson.internal.LinkedTreeMap;

public class CommentService extends HttpUtil implements VicServiceInterface {
	public static CommentService cs;
	private Context context;
	
	public CommentService(Context context){
		this.context = context;
	}
	
	public void getComments(VicResponseListener listener , int articleId) {
		final VicResponseListener lis = listener;
		CommentInf commentInf = restAdapter.create(CommentInf.class);
		Map<String, String> postParams = getAuthMap();
		if( articleId > 0 ){
			postParams.put("article_id", articleId+"");
		}else{
			LogUtil.show("error request[ no detail article id.]");
			return;
		}
		commentInf.getComments(postParams, new Callback<ResData>() {
			@Override
			public void success(ResData data, Response arg1) {
				ResponseUtil.processResp(data, lis);
			}
			
			@Override
			public void failure(RetrofitError err) {
				lis.onFailure(err.getMessage());
			}
		});
		
	}
	

	@Override
	public List<Comment> mapListToModelList(ArrayList<LinkedTreeMap<String, Object>> list) {
		if( list.size() == 0 ){
			return null;
		}
		List<Comment> cList = new ArrayList<Comment>();
		for( LinkedTreeMap<String, Object> map : list ){
			Comment cmt = mapToModel( map );
			cList.add(cmt);
		}
		return cList;
	}

	@Override
	public Comment mapToModel(LinkedTreeMap<String, Object> map) {
		Comment cmt = new Comment();
		
		cmt.setId( getIntValue(map.get("id")) );
		cmt.setWriterId( getIntValue(map.get("user_id")) );
		cmt.setWriterName( map.get("name").toString() );
		cmt.setWriterPhotoURL( getServerImgPath( getIntValue(map.get("user_id")) , map.get("photo").toString()) );
		cmt.setContent( map.get("comment").toString() );
		cmt.setPastTime( getIntValue(map.get("pastime")) );
		cmt.setReplyId(getIntValue(map.get("reply_id")));
		cmt.setReplyWhose(getIntValue(map.get("reply_whose")));
		cmt.setReplyWhoseName(map.get("reply_whose_name").toString());
		cmt.setPlusCount(getIntValue(map.get("plus_count")));
		
		boolean isPlus = false;
		if( getIntValue(map.get("is_plus")) == 1 ){
			isPlus = true;
		}
		cmt.setPlus(isPlus);
		
		return cmt;
	}
	
	/**
	 * singleton
	 * @param context
	 * @return
	 */
	public static CommentService single(Context context){
		if( cs == null ){
			cs = new CommentService(context);
		}
		return cs;
	}
}
