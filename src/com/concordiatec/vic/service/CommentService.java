package com.concordiatec.vic.service;

import java.util.ArrayList;
import java.util.List;
import com.concordiatec.vic.base.HttpBase;
import com.concordiatec.vic.model.Comment;
import com.google.gson.internal.LinkedTreeMap;

public class CommentService extends HttpBase {
	public static CommentService cs;
	
	
	public List<Comment> mapListToBeanList( List<LinkedTreeMap<String, Object>> list ){
		List<Comment> cList = new ArrayList<Comment>();
		for( LinkedTreeMap<String, Object> map : list ){
			Comment cmt = new Comment();
			cmt.setId( Integer.valueOf(map.get("comment_id").toString()) );
			cmt.setWriterId( Integer.valueOf(map.get("user_id").toString()) );
			cmt.setWriterName( map.get("user_name").toString() );
			cmt.setWriterPhotoURL( map.get("user_photo").toString() );
			cmt.setContent( map.get("comment_text").toString() );
			cList.add(cmt);
		}
		return cList;
	}
	
	public static CommentService single(){
		if( cs == null ){
			cs = new CommentService();
		}
		return cs;
	}
}