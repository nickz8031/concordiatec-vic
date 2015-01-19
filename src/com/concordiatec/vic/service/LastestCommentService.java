package com.concordiatec.vic.service;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.concordiatec.vic.inf.IVicService;
import com.concordiatec.vic.model.LastestComment;
import com.concordiatec.vic.util.HttpUtil;
import com.google.gson.internal.LinkedTreeMap;

public class LastestCommentService extends HttpUtil implements IVicService {
	public static LastestCommentService lcs;
	private Context context;
	
	public LastestCommentService(Context context){
		this.context = context;
	}
	@Override
	public List<LastestComment> mapListToModelList(ArrayList<LinkedTreeMap<String, Object>> list) {
		if( list.size() == 0 ){
			return null;
		}
		List<LastestComment> cList = new ArrayList<LastestComment>();
		for( LinkedTreeMap<String, Object> map : list ){
			cList.add( mapToModel( map ) );
		}
		return cList;
	}

	@Override
	public LastestComment mapToModel(LinkedTreeMap<String, Object> map) {
		LastestComment cmt = new LastestComment();
		
		cmt.setCommentId( getIntValue(map.get("comment_id")) );
		cmt.setUserId( getIntValue( map.get("user_id") ) );
		cmt.setUserName( map.get("user_name").toString() );
		String pUrl = getServerImgPath(cmt.getUserId()) + map.get("user_photo").toString();
		cmt.setUserPhoto( pUrl );
		cmt.setCommentText( map.get("comment_text").toString() );
		cmt.setPlusCount( getIntValue(map.get("plus_count")) );
		int replyWhose = getIntValue( map.get("replied_user") );
		if( replyWhose > 0 ){
			cmt.setReplyWhose( replyWhose );
			cmt.setReplyWhoseName( map.get("replied_username").toString().trim() );
		}
		
		
		return cmt;
	}
	
	public static LastestCommentService single( Context context ){
		if( lcs == null ){
			lcs = new LastestCommentService(context);
		}
		return lcs;
	}
}
