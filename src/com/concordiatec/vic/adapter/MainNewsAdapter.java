package com.concordiatec.vic.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.tools.ImageViewPreload;
import com.concordiatec.vic.util.StringUtil;
import com.concordiatec.vic.util.TimeUtil;
import com.concordiatec.vic.widget.CircleImageView;
import com.concordiatec.vic.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

@SuppressLint("UseSparseArrays")
public class MainNewsAdapter extends VicBaseAdapter {
	private List<Article> data;
	private Map<Integer, View> viewMap;
	private LayoutInflater inflater;
	private Context context;
	private ImageViewPreload viewPreload;

	public MainNewsAdapter(Context context, List<Article> data) {
		super();
		this.context = context;
		this.data = data;
		this.inflater = LayoutInflater.from(context);
		this.viewMap = new HashMap<Integer, View>();
		this.viewPreload = new ImageViewPreload(context);
	}
	
	public void clear(){
		this.data.clear();
	}
	
	public void setData( List<Article> data ){
		this.data = data;
		notifyDataSetChanged();
	}
	
	public void addData( Article data ){
		if(data != null){
			this.data.add(data);
		}
	}
	
	public void addData( List<Article> data ){
		if(data != null && data.size()>0 ){
			for (int i = 0; i < data.size(); i++) {
				addData( data.get(i) );
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
	public int getCount() {
		return data.size();
	}

	@Override
	public Article getItem(int position) {
		return data.get(position);
	}
	
	public Article getItem(long position) {
		return data.get((int)position);
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
			//set cover imageView height
			if( apData.getCoverImageWidth() > 0 && apData.getCoverImageHeight() > 0 ){
				LayoutParams layoutParams = new LayoutParams( 
													LayoutParams.MATCH_PARENT , 
													getImageViewHeight(apData.getCoverImageWidth(), apData.getCoverImageHeight()
											) );
				NewsHolder.coverImage.setLayoutParams((RelativeLayout.LayoutParams)layoutParams);
			}
			
			
			Glide.with(context).load(apData.getWriterPhotoURL()).crossFade().into(NewsHolder.writerPhoto);
			Glide.with(context).load(apData.getCoverImageURL()).crossFade().into(NewsHolder.coverImage);
			
			viewMap.put(position, convertView);
			convertView.startAnimation(animation);
		} else {
			convertView = viewMap.get(position);
		}
		this.clearAnimation(position);
		return convertView;
	}
	
	private void clearAnimation(int viewPos){
		for (int i = 0; i < viewMap.size(); i++) {
			if( i == viewPos ){
				continue;
			}
			viewMap.get(i).clearAnimation();
		}
	}
	
	/**
	 * get content cover image height
	 * @param oldWidth
	 * @param oldHeight
	 * @return
	 */
	private int getImageViewHeight( int w , int h ){
		float marginHor = context.getResources().getDimension(R.dimen.mni_layout_margin_horizontal) * 2;
		float adjustMargin = context.getResources().getDimension(R.dimen.mni_layout_border_width) * 2;
		return Math.round(viewPreload.viewHeight(w, h, ( marginHor+adjustMargin ) ) );
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
