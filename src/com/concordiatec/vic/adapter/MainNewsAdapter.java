package com.concordiatec.vic.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.util.StringUtil;
import com.concordiatec.vic.util.TimeUtil;
import com.concordiatec.vic.widget.CircleImageView;
import com.concordiatech.vic.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("UseSparseArrays")
public class MainNewsAdapter extends VicBaseAdapter {
	private List<Article> data;
	private Map<Integer, View> viewMap;
	private LayoutInflater inflater;
	private Context context;
//	private ImageLoader mImageLoader;

	public MainNewsAdapter(Context context, List<Article> data) {
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
	public Article getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (viewMap.get(position) == null) {
			Article apData = getItem(position);
			convertView = inflater.inflate(R.layout.main_news_list_item, parent, false);
			
			NewsHolder.storeInfoLayout = (RelativeLayout) convertView.findViewById(R.id.store_info_layout);
			NewsHolder.writerName = (TextView) convertView.findViewById(R.id.news_writer_name);
			NewsHolder.writeTime = (TextView) convertView.findViewById(R.id.news_write_time);
			NewsHolder.content = (TextView) convertView.findViewById(R.id.news_content);
			NewsHolder.likeCount = (TextView) convertView.findViewById(R.id.news_like_btn);
			NewsHolder.commentCount = (TextView) convertView.findViewById(R.id.news_comment_btn);
			NewsHolder.writerPhoto = (CircleImageView) convertView.findViewById(R.id.news_writer_photo);
			NewsHolder.coverImage = (ImageView) convertView.findViewById(R.id.news_content_img);
			
			if( StringUtil.isEmpty(apData.getShopName()) ){
				NewsHolder.storeInfoLayout.setVisibility(View.GONE);
			}else {
				NewsHolder.storeName = (TextView) convertView.findViewById(R.id.news_store_name);
				NewsHolder.storeAddress = (TextView) convertView.findViewById(R.id.news_store_addr);
				
				NewsHolder.storeName.setText( apData.getShopName() );
				NewsHolder.storeAddress.setText( apData.getShopAddr() );
			}
			
			NewsHolder.writerName.setText( apData.getWriterName() );
			NewsHolder.writeTime.setText( TimeUtil.getTimePast( context, apData.getPastTime() ) );
			NewsHolder.content.setText( apData.getContent() );
			NewsHolder.likeCount.setText( apData.getLikeCount()+"" );
			NewsHolder.commentCount.setText( apData.getCommentCount()+"" );
			
			Glide.with(context).load(apData.getWriterPhotoURL()).crossFade().into(NewsHolder.writerPhoto);
			Glide.with(context).load(apData.getCoverImageURL()).crossFade().into(NewsHolder.coverImage);
			
			viewMap.put(position, convertView);
			convertView.startAnimation(animation);
		} else {
			convertView = viewMap.get(position);
		}
		return convertView;
	}

	@SuppressWarnings("unused")
	private static class NewsHolder {
		static TextView storeName;
		static TextView storeAddress;
		static TextView writerName;
		static TextView writeTime;
		static TextView content;
		static TextView likeCount;
		static TextView commentCount;
		static ImageView storeType;
		static CircleImageView writerPhoto;
		static ImageView coverImage;
		
		static LinearLayout commentorPhotosLayout;
		static RelativeLayout commentContentsLayout;
		static RelativeLayout storeInfoLayout;
		
		
	}
}
