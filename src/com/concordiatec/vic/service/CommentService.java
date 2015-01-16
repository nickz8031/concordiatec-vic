package com.concordiatec.vic.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import android.content.Context;
import com.concordiatec.vic.R;
import com.concordiatec.vic.constant.ApiURL;
import com.concordiatec.vic.inf.VicServiceInterface;
import com.concordiatec.vic.listener.VicResponseHandler;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.Comment;
import com.concordiatec.vic.model.User;
import com.concordiatec.vic.util.HttpUtil;
import com.concordiatec.vic.util.LogUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CommentService extends HttpUtil implements VicServiceInterface {
	public static CommentService cs;
	private Context context;
	
	public CommentService(Context context){
		this.context = context;
	}
	
	/**
	 * 댓글 쓰기
	 * @param comment
	 * @param listener
	 */
	public void writeComment( Comment comment , VicResponseListener listener ){
		if( comment != null && comment.getArticleId() > 0 ){
			RequestParams params = new RequestParams();
			
			params.put("article", comment.getArticleId());
			params.put("user", comment.getWriterId());
			params.put("comment", comment.getContent());
			params.put("reply", comment.getReplyId());
			
			post(ApiURL.COMMENT_WRITE, params, new VicResponseHandler(listener));
			
		}else{
			listener.onFailure(0, context.getString(R.string.err_request_data_invalidate));
		}
	}
	
	public void likeComment(int commentId , int usrId , VicResponseListener listener){
		if( usrId > 0 && commentId > 0 ){
			RequestParams params = new RequestParams();
			params.put("id", commentId);
			params.put("user", usrId);
			post(ApiURL.COMMENT_LIKE, params, new VicResponseHandler(listener));
		}else{
			listener.onFailure(0, context.getString(R.string.err_request_data_invalidate));
		}
	}
	
	
	/**
	 * 댓글 목록 가져오기
	 * @param articleId
	 * @param listener
	 */
	public void getComments(int articleId , VicResponseListener listener) {
		getComments(articleId , 0 , listener);
	}
	/**
	 * 댓글 목록 가져오기
	 * @param articleId
	 * @param listener
	 */
	public void getComments(int articleId , int lastCommentId , VicResponseListener listener) {
		RequestParams params = new RequestParams();
		if( articleId > 0 ){
			params.put("article", articleId);
		}else{
			listener.onFailure(0, context.getString(R.string.err_request_data_invalidate));
			LogUtil.show(context.getString(R.string.err_request_data_invalidate));
			return;
		}
		if( lastCommentId > 0 ){
			params.put("comment", lastCommentId);
		}
		
		User loginUser = new UserService(context).getLoginUser();
		if( loginUser != null ){
			params.put("user", loginUser.usrId);
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
		
		cmt.setReplyId(getIntValue(map.get("replied_comment")));
		cmt.setReplyWhose(getIntValue(map.get("replied_user")));
		cmt.setReplyWhoseName(map.get("replied_username").toString());
		
		cmt.setPlusCount(getIntValue(map.get("plus_count")));
		
		boolean isPlus = false;
		if( getIntValue(map.get("is_plus")) == 1 ){
			isPlus = true;
		}
		cmt.setPlus(isPlus);
		
		return cmt;
	}
}
