package com.concordiatec.vilnet.service;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.concordiatec.vilnet.R;
import com.concordiatec.vilnet.constant.ApiURL;
import com.concordiatec.vilnet.listener.VicResponseHandler;
import com.concordiatec.vilnet.listener.VicResponseListener;
import com.concordiatec.vilnet.model.Comment;
import com.concordiatec.vilnet.model.LocalUser;
import com.concordiatec.vilnet.util.HttpUtil;
import com.concordiatec.vilnet.util.LogUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.loopj.android.http.RequestParams;

public class CommentService extends HttpUtil{
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
	public void writeComment( Comment comment , int lastCommentId , VicResponseListener listener ){
		if( comment != null && comment.getArticleId() > 0 ){
			RequestParams params = new RequestParams();
			
			params.put("article", comment.getArticleId());
			params.put("user", comment.getWriterId());
			params.put("comment", comment.getContent());
			params.put("reply", comment.getReplyId());
			params.put("id", lastCommentId);
			
			post(ApiURL.COMMENT_WRITE, params, new VicResponseHandler(listener));
			
		}else{
			listener.onFailure(0, context.getString(R.string.err_request_data_invalidate));
		}
	}
	
	public void editComment(Comment comment , VicResponseListener listener){
		if( comment != null && comment.getId() > 0 ){
			RequestParams params = new RequestParams();
			
			params.put("id", comment.getId());
			params.put("user", comment.getWriterId());
			params.put("comment", comment.getContent());
			params.put("reply", comment.getReplyId());
			
			post(ApiURL.COMMENT_EDIT, params, new VicResponseHandler(listener));
			
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
	
	public void deleteComment(int commentId , int usrId , VicResponseListener listener){
		if( usrId > 0 && commentId > 0 ){
			RequestParams params = new RequestParams();
			params.put("id", commentId);
			params.put("user", usrId);
			post(ApiURL.COMMENT_DELETE, params, new VicResponseHandler(listener));
			
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
		
		LocalUser loginUser = new UserService(context).getLoginUser();
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
		if( map.get("article_id")!=null ) cmt.setArticleId( getIntValue( map.get("article_id") ) );
		if( map.get("comment_id")!=null ) cmt.setId( getIntValue(map.get("comment_id")) );
		if( map.get("user_id")!=null ) cmt.setWriterId( getIntValue(map.get("user_id")) );
		if( map.get("user_name")!=null ) cmt.setWriterName( map.get("user_name").toString() );
		if( map.get("user_photo")!=null ) cmt.setWriterPhotoURL( getServerImgPath( getIntValue(map.get("user_id")) , map.get("user_photo").toString()) );
		if( map.get("comment")!=null ) cmt.setContent( map.get("comment").toString() );
		if( map.get("pasttime")!=null ) cmt.setPastTime( getIntValue(map.get("pasttime")) );
		
		if( map.get("replied_user")!=null ) cmt.setReplyWhose(getIntValue(map.get("replied_user")));
		if( map.get("replied_username")!=null ) cmt.setReplyWhoseName(map.get("replied_username").toString());
		if( map.get("plus_count")!=null ) cmt.setPlusCount(getIntValue(map.get("plus_count")));
		
		boolean isPlus = false;
		if( map.get("is_plus")!=null && getIntValue(map.get("is_plus")) == 1 ){
			isPlus = true;
		}
		cmt.setPlus(isPlus);
		
		return cmt;
	}
}
