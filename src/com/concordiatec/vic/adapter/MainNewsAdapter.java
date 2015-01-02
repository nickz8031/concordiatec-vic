package com.concordiatec.vic.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.model.LastestComment;
import com.concordiatec.vic.tools.ImageViewPreload;
import com.concordiatec.vic.util.StringUtil;
import com.concordiatec.vic.util.TimeUtil;
import com.concordiatec.vic.widget.CircleImageView;
import com.concordiatec.vic.R;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class MainNewsAdapter extends VicBaseAdapter {
	private List<Article> data;
	private Map<Integer, View> viewMap;
	private LayoutInflater inflater;
	private Context context;
	private Resources res;
	private ImageViewPreload viewPreload;

	public MainNewsAdapter(Context context, List<Article> data) {
		super();
		this.context = context;
		this.res = context.getResources();
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
		View view = convertView;
		NewsHolder holder;
		if (view == null) {
			view = inflater.inflate(R.layout.main_news_list_item, parent, false);
			holder = new NewsHolder();
			
			holder.storeInfoLayout = (RelativeLayout) view.findViewById(R.id.store_info_layout);
			holder.storeName = (TextView) view.findViewById(R.id.news_store_name);
			holder.storeAddress = (TextView) view.findViewById(R.id.news_store_addr);

			holder.writerPhoto = (CircleImageView) view.findViewById(R.id.news_writer_photo);
			holder.writerName = (TextView) view.findViewById(R.id.news_writer_name);
			holder.writeTime = (TextView) view.findViewById(R.id.news_write_time);
			
			holder.content = (TextView) view.findViewById(R.id.news_content);
			holder.coverImage = (ImageView) view.findViewById(R.id.news_content_img);
			
			holder.likeCount = (TextView) view.findViewById(R.id.news_like_btn);
			holder.commentCount = (TextView) view.findViewById(R.id.news_comment_btn);
			
			holder.commentorPhotosLayout = (LinearLayout) view.findViewById(R.id.news_commentor_photos);
			holder.commentLayout = (RelativeLayout) view.findViewById(R.id.display_comment_layout);
			holder.commentFlip = (ViewFlipper) view.findViewById(R.id.display_comment_content);
			
			//viewMap.put(position, convertView);
			view.setTag(holder);
			view.startAnimation(animation);
		} else {
			//convertView = viewMap.get(position);
			holder = (NewsHolder) view.getTag();
		}
		
		Article apData = getItem(position);
		if( StringUtil.isEmpty(apData.getShopName()) ){
			holder.storeInfoLayout.setVisibility(View.GONE);
		}else {
			holder.storeName.setText( apData.getShopName() );
			holder.storeAddress.setText( apData.getShopAddr() );
		}
		
		Glide.with(context)
		.load(apData.getWriterPhotoURL())
		.crossFade()
		.into(holder.writerPhoto);
		
		
		holder.writerName.setText( apData.getWriterName() );
		holder.writeTime.setText( TimeUtil.getTimePast( context, apData.getPastTime() ) );
		holder.content.setText( apData.getContent() );
		holder.likeCount.setText( apData.getLikeCount()+"" );
		holder.commentCount.setText( apData.getCommentCount()+"" );
		//set cover imageView height
		if( apData.getCoverImageWidth() > 0 && apData.getCoverImageHeight() > 0 ){
			LayoutParams layoutParams = new LayoutParams( 
												LayoutParams.MATCH_PARENT , 
												getImageViewHeight(apData.getCoverImageWidth(), apData.getCoverImageHeight()
										) );
			holder.coverImage.setLayoutParams((RelativeLayout.LayoutParams)layoutParams);
		}
		Glide.with(context).load(apData.getCoverImageURL()).crossFade().into(holder.coverImage);
		
		List<LastestComment> lastestComments = apData.getLatestComments();
		if( lastestComments != null && lastestComments.size() > 0 ){
			for (int i = 0; i < lastestComments.size(); i++) {
				
				LastestComment c = lastestComments.get(i);
				//commenter photo
				CircleImageView iView = new CircleImageView(context);
				int cpSize = (int) res.getDimension(R.dimen.mni_ctrl_commentor_photo_width);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(cpSize , cpSize);
				params.setMargins(0, 0, (int) res.getDimension(R.dimen.mni_ctrl_commentor_photo_margin), 0);
				iView.setLayoutParams(params);
				holder.commentorPhotosLayout.addView(iView);
				Glide.with(context)
				.load(c.getUserPhoto())
				.crossFade()
				.into(iView);
				
				//comment content
				TextView tv = new TextView(context);
				tv.setBackgroundResource(android.R.color.white);
				tv.setText( c.getCommentText() );
				holder.commentFlip.addView(tv);
			}
			
			//display
			holder.commentorPhotosLayout.setVisibility(View.VISIBLE);
			holder.commentLayout.setVisibility(View.VISIBLE);
		}
		
		this.clearAnimation(position);
		return view;
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
	class NewsHolder {
		TextView storeName;
		TextView storeAddress;
		TextView writerName;
		TextView writeTime;
		TextView content;
		TextView likeCount;
		TextView commentCount;
		ImageView storeType;
		CircleImageView writerPhoto;
		ImageView coverImage;
		
		LinearLayout commentorPhotosLayout;
		RelativeLayout commentLayout;
		ViewFlipper commentFlip;
		RelativeLayout storeInfoLayout;
	}
}
