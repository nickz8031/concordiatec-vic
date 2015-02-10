package com.concordiatec.vilnet.service;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.concordiatec.vilnet.model.LastestComment;
import com.concordiatec.vilnet.util.HttpUtil;
import com.google.gson.internal.LinkedTreeMap;

public class LastestCommentService extends HttpUtil {
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
		
		if( map.get("comment_id") != null ) cmt.setCommentId( getIntValue(map.get("comment_id")) );
		if( map.get("user_id") != null ) cmt.setUserId( getIntValue( map.get("user_id") ) );
		if( map.get("user_name") != null ) cmt.setUserName( map.get("user_name").toString() );
		if( map.get("user_photo") != null ){
			String pUrl = getServerImgPath(cmt.getUserId()) + map.get("user_photo").toString();
			cmt.setUserPhoto( pUrl );
		}
		
		if( map.get("comment") != null ) cmt.setCommentText( map.get("comment").toString() );
		if( map.get("plus_count") != null ) cmt.setPlusCount( getIntValue(map.get("plus_count")) );
		
		if(map.get("replied_user") != null && getIntValue( map.get("replied_user")) > 0 ){
			int replyWhose = getIntValue( map.get("replied_user") );
			if( replyWhose > 0 ){
				cmt.setReplyWhose( replyWhose );
				cmt.setReplyWhoseName( map.get("replied_username").toString().trim() );
			}
		}
		
		return cmt;
	}
}
