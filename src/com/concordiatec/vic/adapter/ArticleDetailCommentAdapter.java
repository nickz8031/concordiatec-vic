package com.concordiatec.vic.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.concordiatec.vic.R;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.model.Comment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return data.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (viewMap.get(position) == null) {
			convertView = inflater.inflate(R.layout.article_detail_comment_list_item, parent, false);
			viewMap.put(position, convertView);
		}else{
			convertView = viewMap.get(position);
		}
		
		return convertView;
	}
	public void clear(){
		this.data.clear();
	}
	
	public void setData( List<Comment> data ){
		this.data = data;
		notifyDataSetChanged();
	}
	
	public void addData( Comment data ){
		if(data != null){
			this.data.add(data);
		}
	}
	
	public void addData( List<Comment> data ){
		if(data != null && data.size()>0 ){
			for (int i = 0; i < data.size(); i++) {
				addData( data.get(i) );
			}
			notifyDataSetChanged();
		}
	}
}
