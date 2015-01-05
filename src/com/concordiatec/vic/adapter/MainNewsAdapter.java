package com.concordiatec.vic.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.R.color;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.model.LastestComment;
import com.concordiatec.vic.tools.ImageViewPreload;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.StringUtil;
import com.concordiatec.vic.util.TimeUtil;
import com.concordiatec.vic.widget.CircleImageView;
import com.concordiatec.vic.widget.CustomViewFlipper;
import com.concordiatec.vic.widget.CustomViewFlipper.OnFlipListener;
import com.concordiatec.vic.ArticleDetailActivity;
import com.concordiatec.vic.R;
import com.concordiatec.vic.R.anim;
import android.R.bool;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ViewFlipper;

@SuppressLint("UseSparseArrays")
public class MainNewsAdapter extends VicBaseAdapter {
	private List<Article> data;
	private Map<Integer, View> viewMap;
	private LayoutInflater inflater;
	private Context context;
	private ImageViewPreload viewPreload;
	private Resources res;

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
			
			//click comment icon move to detail activity
			NewsHolder.commentCount.setOnClickListener(new CommentIconClickListener(apData.getId()));
			

			if( apData.isLike() ){
				likeArticle(NewsHolder.likeCount);
				NewsHolder.likeCount.setTag(true);
			}
			
			//click up icon do up process
			NewsHolder.likeCount.setOnClickListener(new LikeIconClickListener(apData.getId()));
			
			
			//set cover imageView height
			if( apData.getCoverImageWidth() > 0 && apData.getCoverImageHeight() > 0 ){
				LayoutParams layoutParams = new LayoutParams( 
													LayoutParams.MATCH_PARENT , 
													getImageViewHeight(apData.getCoverImageWidth(), apData.getCoverImageHeight()
											) );
				NewsHolder.coverImage.setLayoutParams((RelativeLayout.LayoutParams)layoutParams);
			}
			
			//writer profile photo
			Glide.with(context).load(apData.getWriterPhotoURL()).crossFade().into(NewsHolder.writerPhoto);
			//article cover image
			Glide.with(context).load(apData.getCoverImageURL()).crossFade().into(NewsHolder.coverImage);
			
			List<LastestComment> lastestComments = apData.getLatestComments();
			//if has comments
			if( lastestComments != null && lastestComments.size() > 0 ){
				
				NewsHolder.commentorPhotosLayout = (LinearLayout) convertView.findViewById(R.id.news_commentor_photos);
				NewsHolder.commentLayout = (RelativeLayout) convertView.findViewById(R.id.display_comment_layout);
				NewsHolder.commentFlip = (CustomViewFlipper) convertView.findViewById(R.id.display_comment_content);
				
				for (int i = 0; i < lastestComments.size(); i++) {
					LastestComment c = lastestComments.get(i);
					//commenter photo
					CircleImageView iView = new CircleImageView(context);
					int cpSize = (int) res.getDimension(R.dimen.mni_ctrl_commentor_photo_width);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(cpSize , cpSize);
					params.setMargins(0, 0, (int) res.getDimension(R.dimen.mni_ctrl_commentor_photo_margin), 0);
					iView.setLayoutParams(params);
					iView.setBorderColor(res.getColor(R.color.effect_color));
					Glide.with(context)
					.load(c.getUserPhoto())
					.crossFade()
					.into(iView);
					
					NewsHolder.commentorPhotosLayout.addView(iView);
					final ViewGroup cpl = NewsHolder.commentorPhotosLayout;

					setCmtPhotoBorder( cpl , 0 );
					
					SpannableString span = new SpannableString(c.getUserName() + " " + c.getCommentText());
					span.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, c.getUserName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					TextView tvContent = new TextView(context);
					tvContent.setText( span );
					tvContent.setGravity(Gravity.CENTER_VERTICAL);
					NewsHolder.commentFlip.addView(tvContent);
					NewsHolder.commentFlip.setOnFlipListener(new OnFlipListener() {
						@Override
						public void onShowPrevious(CustomViewFlipper flipper) {}
						@Override
						public void onShowNext(CustomViewFlipper flipper) {
							setCmtPhotoBorder( cpl ,  flipper.getDisplayedChild() );
						}
					});
					
				}
				//stop auto flip if there is only one comment
				if( lastestComments.size() == 1 ){
					NewsHolder.commentFlip.stopFlipping();
					NewsHolder.commentFlip.setAutoStart(false);
				}
				//display
				NewsHolder.commentorPhotosLayout.setVisibility(View.VISIBLE);
				NewsHolder.commentLayout.setVisibility(View.VISIBLE);
			}
			
			viewMap.put(position, convertView);
			convertView.startAnimation(animation);
		} else {
			convertView = viewMap.get(position);
		}
		this.clearAnimation(position);
		return convertView;
	}
	
	/**
	 * click comment icon move to detail activity
	 * @author Nick.z
	 */
	private final class CommentIconClickListener implements OnClickListener{
		private int articleId;
		public CommentIconClickListener( int articleId ) {
			this.articleId = articleId;
		}
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context , ArticleDetailActivity.class);
			intent.putExtra("article_id", articleId);
			context.startActivity(intent);
		}
		
	}
	
	/**
	 * click like button 
	 * @author Nick.z
	 */
	private final class LikeIconClickListener implements OnClickListener{
		private int articleId;
		public LikeIconClickListener( int articleId ) {
			this.articleId = articleId;
			
		}
		@Override
		public void onClick(View v) {
			final TextView t = (TextView)v;
			int likeCount = Integer.parseInt(t.getText().toString());
			if( v.getTag() == null ){
				t.setText((likeCount+1) + "");
				likeArticle(t);
				activeLikeAnimation(t);
				t.setTag(true);
			}else{
				if( likeCount > 0 ){
					t.setText((likeCount-1) + "");
				}
				dislikeArticle(t);
				t.setTag(null);
			}
		}
	}
	
	/**
	 * like action
	 * @param v
	 */
	private void likeArticle( View v ){
		TextView t = (TextView) v;
		t.setTextColor(Color.WHITE);
		t.setBackgroundResource(R.drawable.news_ctrl_btn_active);
		Drawable drawable = res.getDrawable(R.drawable.ic_action_thumb_up_white);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		t.setCompoundDrawables(drawable, null, null, null);
	}
	/**
	 * dislike action
	 * @param v
	 */
	private void dislikeArticle( View v ){
		TextView t = (TextView) v;
		t.setTextColor( res.getColor(R.color.mni_ctrl_btn_text) );
		t.setBackgroundResource(R.drawable.news_ctrl_btn_selector);
		Drawable drawable = res.getDrawable(R.drawable.ic_action_thumb_up);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		t.setCompoundDrawables(drawable, null, null, null);
	}
	
	/**
	 * start animation with like action
	 * @param v
	 */
	private void activeLikeAnimation( View v ){
		final View target = v;
		final Animation toBig = AnimationUtils.loadAnimation(context, R.anim.big_scale);
		final Animation toNormal = AnimationUtils.loadAnimation(context, R.anim.small_scale);
		target.setAnimation(toBig);
		toBig.start();
		toBig.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				target.clearAnimation();
				target.setAnimation(toNormal);
				toNormal.start();
			}
		});
	}
	
	
	/**
	 * set commenter photo border with comment text flip
	 * @param wrap
	 * @param position
	 */
	private void setCmtPhotoBorder( ViewGroup wrap , int position ){
		int borderWidth = 2;
		for (int i = 0; i < wrap.getChildCount(); i++) {
			CircleImageView civ = (CircleImageView) wrap.getChildAt(i);
			if( i == position ){
				civ.setBorderWidth(borderWidth);
			}else{
				civ.setBorderWidth(0);
			}
		}
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
		static RelativeLayout commentLayout;
		static CustomViewFlipper commentFlip;
		static RelativeLayout storeInfoLayout;
	}
}
