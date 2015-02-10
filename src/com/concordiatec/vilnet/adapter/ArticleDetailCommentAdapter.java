package com.concordiatec.vilnet.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bumptech.glide.Glide;
import com.concordiatec.vilnet.R;
import com.concordiatec.vilnet.UserActivity;
import com.concordiatec.vilnet.inf.IVicClickableSpan;
import com.concordiatec.vilnet.model.Comment;
import com.concordiatec.vilnet.util.StringUtil;
import com.concordiatec.vilnet.util.TimeUtil;
import com.concordiatec.vilnet.widget.CircleImageView;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ArticleDetailCommentAdapter extends BaseAdapter {
	private List<Comment> data;
	private Map<Integer, View> viewMap;
	private LayoutInflater inflater;
	private Context context;
	public ArticleDetailCommentAdapter(Context context, List<Comment> data) {
		super();
		this.context = context;
		this.data = data;
		this.inflater = LayoutInflater.from(context);
		this.viewMap = new HashMap<Integer, View>();
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Comment getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return data.get(position).getId();
	}
	
	public void deleteData( int position ){
		updateData(null , position);
	}
	
	public void updateData( Comment comment , int position ){
		if( comment != null ){
			data.set(position, comment);
		}else{
			data.remove(position);
		}
		viewMap.remove(position);
		notifyDataSetChanged();
	}
	
	public void clear(){
		this.data.clear();
		viewMap.clear();
	}
	
	public void setData( List<Comment> data ){
		this.data = data;
		notifyDataSetChanged();
	}
	
	public void addData( Comment cmt ){
		if(cmt != null){
			this.data.add(cmt);
		}
	}
	
	public void addData( List<Comment> lcmt ){
		if(lcmt != null && lcmt.size()>0 ){
			for (int i = 0; i < lcmt.size(); i++) {
				addData( lcmt.get(i) );
			}
			notifyDataSetChanged();
		}
	}
	
	public int getLastRecordId(){
		if( this.data.size()>0 ){
			return this.data.get(this.data.size()-1).getId();
		}else{
			return 0;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (viewMap.get(position) == null) {
			Comment comment = (Comment) getItem(position);
			
			convertView = inflater.inflate(R.layout.li_article_detail_comment, parent, false);
			CommentHolder.commentorPhoto = (CircleImageView) convertView.findViewById(R.id.ar_d_commentor_photo);
			CommentHolder.content = (TextView) convertView.findViewById(R.id.ar_d_comment_content);
			CommentHolder.pastTime = (TextView) convertView.findViewById(R.id.ar_d_comment_time);
			CommentHolder.name = (TextView) convertView.findViewById(R.id.ar_d_commentor_name);
			
			String content = comment.getContent().trim();
			if( comment.getPlusCount() > 0 ){
				content = content + " +" + comment.getPlusCount();
				
			}			
			if( comment.getReplyWhose() > 0 ){
				content = "@"+comment.getReplyWhoseName() + " " + content;
			}
			SpannableString span = new SpannableString(content);
			final int replyTarget = comment.getReplyWhose();
			if( replyTarget > 0 ){
				StringUtil.setClickableText(span, 0, comment.getReplyWhoseName().length()+1, new IVicClickableSpan() {
					@Override
					public void onClick() {
						Intent intent = new Intent(context , UserActivity.class);
						intent.putExtra("user_id", replyTarget);
						intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						context.startActivity(intent);
					}
				});
			}
			if( comment.getPlusCount() > 0 ){
				String tmpPlusCount = "+"+comment.getPlusCount();
				int startPos = content.length() - tmpPlusCount.length();
				StringUtil.setTextColor(span, context.getResources().getColor(R.color.theme_wrap_color), startPos, content.length());
				StringUtil.setBoldText(span, startPos, content.length());
			}
			CommentHolder.content.setText( span );
			if( comment.getReplyId() > 0 ){
				CommentHolder.content.setMovementMethod(LinkMovementMethod.getInstance());
				CommentHolder.content.setFocusable(false);
			}			
			Glide.with(context).load(comment.getWriterPhotoURL()).crossFade().into(CommentHolder.commentorPhoto);
			CommentHolder.pastTime.setText( TimeUtil.getTimePast( context, comment.getPastTime() ) );
			CommentHolder.name.setText( comment.getWriterName() );
			
			viewMap.put(position, convertView);
		}else{
			convertView = viewMap.get(position);
		}
		
		return convertView;
	}
	
	private static class CommentHolder {
		static CircleImageView commentorPhoto;
		static TextView name;
		static TextView pastTime;
		static TextView content;
		
	}
}
