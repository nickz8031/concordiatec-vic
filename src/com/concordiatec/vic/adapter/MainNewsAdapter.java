package com.concordiatec.vic.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.util.TimeUtil;
import com.concordiatec.vic.widget.CircleImageView;
import com.concordiatech.vic.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainNewsAdapter extends BaseAdapter {
	
	private List<Article> data;
	private Animation animation;
	private Map<Integer, View> viewMap;
	private LayoutInflater inflater;
	private Context context;
	
	@SuppressLint("UseSparseArrays")
	public MainNewsAdapter(Context context, List<Article> data) {
		this.context = context;
		this.data = data;
		if( animation == null ){
			animation = new TranslateAnimation(
		            Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f,
		            Animation.RELATIVE_TO_SELF, 0.3f,Animation.RELATIVE_TO_SELF, 0.0f
		        );
			animation.setDuration(300);
		}
		inflater = LayoutInflater.from(context);
		
		viewMap = new HashMap<Integer, View>();		
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
		MainNewsHolder holder;
		if (viewMap.get(position) == null) {
			Article apData = getItem(position);
			convertView = inflater.inflate(R.layout.main_news_list_item , parent , false);
//			holder = new MainNewsHolder();
//			
//			holder.shopName.setText(apData.getShopName());
//			holder.shopAddress.setText(apData.getShopAddr());
//			
//			holder.writerName.setText(apData.getWriterName());
//			
//			Glide.with(context).load(apData.getWriterPhotoURL()).crossFade().into(holder.writerPhoto);
//			
//			holder.writeTime.setText( TimeUtil.getTimePast(apData.getPastTime()) );
//			holder.content.setText(apData.getContent());
//			Glide.with(context).load(apData.getCoverImageURL()).crossFade().into(holder.coverImage);
//			holder.likeCount.setText(apData.getLikeCount());
//			holder.commentCount.setText(apData.getCommentCount());
//			if( apData.getLatestComments() != null && apData.getLatestComments().size() > 0 ){
//				CircleImageView imageView;
//				LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.commentorPhotosLayout.getLayoutParams();
//				for (int i = 0; i < apData.getLatestComments().size(); i++) {
//					imageView = new CircleImageView(context);
//					//imageView.setIma
//				}
//			}
			
			viewMap.put(position, convertView);
			convertView.startAnimation(animation);
			
		}else{
			convertView = viewMap.get(position);
			//holder = (MainNewsHolder) convertView.getTag();
		}
		
		return convertView;
	}
	
	private static class MainNewsHolder {
		TextView shopName;
		TextView shopAddress;
		ImageView shopType;
		
		CircleImageView writerPhoto;
		TextView writerName;
		TextView writeTime;
		TextView content;
		ImageView coverImage;
		
		TextView likeCount;
		TextView commentCount;
		
		LinearLayout commentorPhotosLayout;
		RelativeLayout commentContentsLayout;
		
	}	
	
}
