package com.concordiatec.vic.service;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.concordiatec.vic.constant.ApiURL;
import com.concordiatec.vic.inf.VicServiceInterface;
import com.concordiatec.vic.listener.VicResponseHandler;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.Comment;
import com.concordiatec.vic.model.User;
import com.concordiatec.vic.util.HttpUtil;
import com.concordiatec.vic.util.LogUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.loopj.android.http.RequestParams;

public class CommentService extends HttpUtil implements VicServiceInterface {
	public static CommentService cs;
	private Context context;
	
	public CommentService(Context context){
		this.context = context;
	}
	
	public void getComments(VicResponseListener listener , int articleId) {
		getComments(listener, articleId , 0);
	}
	
	public void getComments(VicResponseListener listener , int articleId , int lastCommentId) {
		RequestParams params = new RequestParams();
		if( articleId > 0 ){
			params.put("article_id", articleId);
		}else{
			LogUtil.show("error request[ no detail article id.]");
			return;
		}
		if( lastCommentId > 0 ){
			params.put("comment_id", lastCommentId);
		}
		
		User loginUser = new UserService(context).getLoginUser();
		if( loginUser != null ){
			params.put("user_id", loginUser.usrId);
		}
		
		post(ApiURL.COMMENT_LIST, params, new VicResponseHandler(listener));
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
