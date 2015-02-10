package com.concordiatec.vilnet.adapter;

import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.Glide;
import com.concordiatec.vilnet.R;
import com.concordiatec.vilnet.ArticleDetailActivity;
import com.concordiatec.vilnet.LoginActivity;
import com.concordiatec.vilnet.ShopDetailActivity;
import com.concordiatec.vilnet.UserActivity;
import com.concordiatec.vilnet.listener.VicSimpleAnimationListener;
import com.concordiatec.vilnet.model.Article;
import com.concordiatec.vilnet.model.Comment;
import com.concordiatec.vilnet.model.LastestComment;
import com.concordiatec.vilnet.service.ArticleService;
import com.concordiatec.vilnet.service.UserService;
import com.concordiatec.vilnet.tools.ImageViewPreload;
import com.concordiatec.vilnet.util.LogUtil;
import com.concordiatec.vilnet.util.StringUtil;
import com.concordiatec.vilnet.util.TimeUtil;
import com.concordiatec.vilnet.widget.CircleImageView;
import com.concordiatec.vilnet.widget.CustomViewFlipper;
import com.concordiatec.vilnet.widget.CustomViewFlipper.OnFlipListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

@SuppressLint("UseSparseArrays")
public class ArticlesAdapter extends VicBaseAdapter {
	private List<Article> data;
	private LayoutInflater inflater;
	private Activity context;
	private ImageViewPreload viewPreload;
	private Resources res;
	private ArticleService aService;
	private UserService uService;

	public ArticlesAdapter(Activity context, List<Article> data) {
		super();
		this.context = context;
		this.res = context.getResources();
		this.data = data;
		this.inflater = LayoutInflater.from(context);
		this.viewPreload = new ImageViewPreload(context);
		this.aService = new ArticleService(context);
		this.uService = new UserService(context);
	}
	public void articleStateChange( Article article ){
		for (int i = 0; i < data.size(); i++) {
			if( article.getId() == getItem(i).getId() ){
				this.data.set(i, article);
				notifyDataSetChanged();
				break;
			}
		}
	}
	public void commentStateChanged( Comment comment ){
		commentStateChanged(comment, false);
	}
	public void commentStateChanged( Comment comment , boolean isDelete ){
		for (int i = 0; i < data.size(); i++) {
			Article article = getItem(i);
			if( comment.getArticleId() == article.getId() ){
				for (int j = 0; j < article.getLatestComments().size(); j++) {
					List<LastestComment> lastestComment = article.getLatestComments();
					if( comment.getId() == lastestComment.get(j).getCommentId() ){
						if( isDelete ){
							lastestComment.remove(j);
							article.setCommentCount( article.getCommentCount() - 1 );
						}else{
							LastestComment tmp = new LastestComment();
							tmp.setCommentId( comment.getId() );
							tmp.setCommentText( comment.getContent() );
							tmp.setPlusCount( comment.getPlusCount() );
							tmp.setReplyWhose( comment.getReplyWhose() );
							tmp.setReplyWhoseName( comment.getReplyWhoseName() );
							tmp.setUserId( comment.getWriterId() );
							tmp.setUserName( comment.getWriterName() );
							tmp.setUserPhoto( comment.getWriterPhotoURL() );
							lastestComment.set(j, tmp);
						}
						article.setLatestComments( lastestComment );
						break;
					}
				}
				data.set(i, article);
				notifyDataSetChanged();
				break;
			}
		}
	}
	public void clear() {
		this.data.clear();
		notifyDataSetChanged();
	}

	public void deleteData(int position) {
		this.data.remove(position - 1);
		notifyDataSetChanged();
	}

	public void setData(List<Article> data) {
		this.data = data;
		notifyDataSetChanged();
	}

	public void updateData(Article article, int position) {
		this.data.set(position - 1, article);
		notifyDataSetChanged();
	}

	public void addData(Article data) {
		if (data != null) {
			this.data.add(data);
		}
	}

	public void addData(List<Article> data) {
		if (data != null && data.size() > 0) {
			for (int i = 0; i < data.size(); i++) {
				addData(data.get(i));
			}
			notifyDataSetChanged();
		}
	}

	public int getLastRecordId() {
		if (this.data.size() > 0) {
			return this.data.get(this.data.size() - 1).getId();
		} else {
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
		return data.get((int) position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NewsHolder holder;
		Article apData = getItem(position);
		List<LastestComment> lastestComments = apData.getLatestComments();
		if (convertView == null) {
			holder = new NewsHolder();
			convertView = inflater.inflate(R.layout.li_frag_articles, parent, false);
			holder.storeInfoLayout = (RelativeLayout) convertView.findViewById(R.id.store_info_layout);
			holder.writerName = (TextView) convertView.findViewById(R.id.news_writer_name);
			holder.writeTime = (TextView) convertView.findViewById(R.id.news_write_time);
			holder.content = (TextView) convertView.findViewById(R.id.news_content);
			holder.likeCount = (TextView) convertView.findViewById(R.id.news_like_btn);
			holder.commentCount = (TextView) convertView.findViewById(R.id.news_comment_btn);
			holder.writerPhoto = (CircleImageView) convertView.findViewById(R.id.news_writer_photo);
			holder.coverImage = (ImageView) convertView.findViewById(R.id.news_content_img);
			holder.storeName = (TextView) convertView.findViewById(R.id.news_store_name);
			holder.storeAddress = (TextView) convertView.findViewById(R.id.news_store_addr);
			holder.commentorPhotosLayout = (LinearLayout) convertView.findViewById(R.id.news_commentor_photos);
			holder.commentLayout = (RelativeLayout) convertView.findViewById(R.id.display_comment_layout);
			holder.commentFlip = (CustomViewFlipper) convertView.findViewById(R.id.display_comment_content);
			

			// convertView.startAnimation(animation);
			convertView.setTag(holder);
		} else {
			holder = (NewsHolder) convertView.getTag();
			// initialize
			holder.commentorPhotosLayout.removeAllViews();
			holder.commentFlip.removeAllViews();
			holder.commentFlip.setOnFlipListener(null);
			holder.commentorPhotosLayout.setVisibility(View.GONE);
			holder.commentLayout.setVisibility(View.GONE);
			holder.likeCount.setOnClickListener(null);
			holder.commentCount.setOnClickListener(null);
			holder.storeInfoLayout.setOnClickListener(null);
			holder.writerPhoto.setOnClickListener(null);
		}
		//bind click listener
		// click up icon do up process
		holder.likeCount.setOnClickListener(new AdapterClickListener(apData.getId()));
		//store infomation
		holder.storeInfoLayout.setOnClickListener(new AdapterClickListener(apData.getShopId()));
		//profile photo
		int profileClickId = apData.getWriterIsShop() ? apData.getWriterShopId() : apData.getWriterId();
		holder.writerPhoto.setOnClickListener( new AdapterClickListener( profileClickId , apData.getWriterIsShop() ) );
		//comment button
		holder.commentCount.setOnClickListener(new AdapterClickListener(apData.getId()));
		
		
		if (!StringUtil.isEmpty(apData.getShopName()) && holder.storeName != null && holder.storeAddress != null) {
			holder.storeName.setText(apData.getShopName());
			holder.storeAddress.setText(apData.getShopAddr());
		} else {
			holder.storeInfoLayout.setVisibility(View.GONE);
		}
		holder.writerName.setText(apData.getWriterName());
		holder.writeTime.setText(TimeUtil.getTimePast(context, apData.getPastTime()));
		holder.content.setText(apData.getContent());
		holder.likeCount.setText(apData.getLikeCount() + "");
		holder.commentCount.setText(apData.getCommentCount() + "");
		
		// set cover imageView height
		if (apData.getCoverImageWidth() > 0 && apData.getCoverImageHeight() > 0) {
			LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, getImageViewHeight(apData.getCoverImageWidth(), apData.getCoverImageHeight()));
			holder.coverImage.setLayoutParams((RelativeLayout.LayoutParams) layoutParams);
		}
		// writer profile photo
		Glide.with(context).load(apData.getWriterPhotoURL()).into(holder.writerPhoto);
		// article cover image
		Glide.with(context).load(apData.getCoverImageURL()).thumbnail(0.3f).into(holder.coverImage);
		
		if (apData.isLike()) {
			setLike(holder.likeCount);
			holder.likeCount.setTag(true);
		} else {
			setDislike(holder.likeCount);
			holder.likeCount.setTag(null);
		}
		
		// if has comments
		if (lastestComments != null && lastestComments.size() > 0) {
			for (int i = 0; i < lastestComments.size(); i++) {
				LastestComment c = lastestComments.get(i);
				// commenter photo
				CircleImageView iView = new CircleImageView(context);
				int cpSize = (int) res.getDimension(R.dimen.mni_ctrl_commentor_photo_width);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(cpSize, cpSize);
				params.setMargins(0, 0, (int) res.getDimension(R.dimen.mni_ctrl_commentor_photo_margin), 0);
				iView.setLayoutParams(params);
				iView.setBorderColor(res.getColor(R.color.effect_color));
				Glide.with(context).load(c.getUserPhoto()).crossFade().into(iView);
				holder.commentorPhotosLayout.addView(iView);
				final ViewGroup cpl = holder.commentorPhotosLayout;
				setCmtPhotoBorder(cpl, 0);
				String content = c.getCommentText().trim();
				if (c.getPlusCount() > 0) {
					content = content + " +" + c.getPlusCount();
				}
				if (c.getReplyWhose() > 0) {
					content = "@" + c.getReplyWhoseName() + " " + content;
				}
				content = c.getUserName() + " " + content;
				SpannableString span = new SpannableString(content);
				StringUtil.setBoldText(span, 0, c.getUserName().length());
				if (c.getReplyWhose() > 0) {
					int start = c.getUserName().length() + 1;
					int end = start + c.getReplyWhoseName().length() + 1;
					StringUtil.setTextColor(span, Color.BLUE, start, end);
				}
				if (c.getPlusCount() > 0) {
					String tmpPlusCount = "+" + c.getPlusCount();
					int startPos = content.length() - tmpPlusCount.length();
					int endPos = content.length();
					StringUtil.setTextColor(span, context.getResources().getColor(R.color.theme_wrap_color), startPos, endPos);
					StringUtil.setBoldText(span, startPos, endPos);
				}
				TextView tvContent = new TextView(context);
				tvContent.setText(span);
				tvContent.setGravity(Gravity.CENTER_VERTICAL);
				holder.commentFlip.addView(tvContent);
				holder.commentFlip.setOnFlipListener(new OnFlipListener() {
					@Override
					public void onShowPrevious(CustomViewFlipper flipper) {
					}

					@Override
					public void onShowNext(CustomViewFlipper flipper) {
						setCmtPhotoBorder(cpl, flipper.getDisplayedChild());
					}
				});
			}
			// stop auto flip if there is only one comment
			if (lastestComments.size() == 1) {
				holder.commentFlip.stopFlipping();
				holder.commentFlip.setAutoStart(false);
			}
			// display
			holder.commentorPhotosLayout.setVisibility(View.VISIBLE);
			holder.commentLayout.setVisibility(View.VISIBLE);
		}
		// this.clearAnimation(position);
		return convertView;
	}

	private final class AdapterClickListener implements OnClickListener {
		private int id;
		private boolean isShop;
		public AdapterClickListener(int id) {
			this(id, false);
		}
		public AdapterClickListener(int id , boolean isShop) {
			this.id = id;
			this.isShop = isShop;
		}

		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
				//가게 클릭
				case R.id.store_info_layout:
					intent = new Intent(context, ShopDetailActivity.class);
					intent.putExtra("shop_id", this.id);
					context.startActivity(intent);
					break;
				//소식 좋아요 클릭
				case R.id.news_like_btn:
					if (uService.getLoginUser() == null) {
						intent = new Intent(context, LoginActivity.class);
						context.startActivityForResult(intent, 0);
						return;
					}
					final TextView t = (TextView) v;
					if (v.getTag() == null) {
						activeLikeAnimation(t);
					}
					likeArticle(t, id);
					break;
				//댓글 클릭
				case R.id.news_comment_btn:
					intent = new Intent(context, ArticleDetailActivity.class);
					intent.putExtra("article_id", id);
					intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					context.startActivity(intent);
					break;
				//프로파일 이미지 클릭
				case R.id.news_writer_photo:
					if( isShop ){
						intent = new Intent( context , ShopDetailActivity.class );
						intent.putExtra("shop_id", id);
					}else{
						intent = new Intent( context , UserActivity.class );
						intent.putExtra("user_id", id);
					}
					context.startActivity(intent);
					break;
			}
		}
	}

	/**
	 * like action
	 * 
	 * @param v
	 */
	private void likeArticle(View v, int articleId) {
		final View view = v;
		final TextView t = (TextView) v;
		final boolean isLike = (v.getTag() == null);
		int likeCount = Integer.parseInt(t.getText().toString());
		if (isLike) {
			t.setText((likeCount + 1) + "");
			setLike(view);
			view.setTag(true);
		} else {
			if (likeCount > 0) {
				t.setText((likeCount - 1) + "");
			}
			setDislike(view);
			view.setTag(null);
		}
		aService.likeArticle(articleId);
	}

	private void setLike(View v) {
		TextView t = (TextView) v;
		t.setTextColor(Color.WHITE);
		t.setBackgroundResource(R.drawable.news_ctrl_btn_active);
		Drawable drawable = res.getDrawable(R.drawable.ic_action_thumb_up_white);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		t.setCompoundDrawables(drawable, null, null, null);
	}

	private void setDislike(View v) {
		TextView t = (TextView) v;
		t.setTextColor(res.getColor(R.color.mni_ctrl_btn_text));
		t.setBackgroundResource(R.drawable.news_ctrl_btn_selector);
		Drawable drawable = res.getDrawable(R.drawable.ic_action_thumb_up);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		t.setCompoundDrawables(drawable, null, null, null);
	}

	/**
	 * start animation with like action
	 * 
	 * @param v
	 */
	private void activeLikeAnimation(View v) {
		final View target = v;
		final Animation toBig = AnimationUtils.loadAnimation(context, R.anim.big_scale);
		final Animation toNormal = AnimationUtils.loadAnimation(context, R.anim.small_scale);
		target.setAnimation(toBig);
		toBig.start();
		toBig.setAnimationListener(new VicSimpleAnimationListener() {
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
	 * 
	 * @param wrap
	 * @param position
	 */
	private void setCmtPhotoBorder(ViewGroup wrap, int position) {
		int borderWidth = 2;
		for (int i = 0; i < wrap.getChildCount(); i++) {
			CircleImageView civ = (CircleImageView) wrap.getChildAt(i);
			if (i == position) {
				civ.setBorderWidth(borderWidth);
			} else {
				civ.setBorderWidth(0);
			}
		}
	}

	// private void clearAnimation(int viewPos){
	// if( viewMap == null ) return;
	// for (int i = 0; i < viewMap.size(); i++) {
	// if( i == viewPos ){
	// continue;
	// }
	// if( viewMap.get(i) != null ){
	// viewMap.get(i).clearAnimation();
	// }
	// }
	// }
	/**
	 * get content cover image height
	 * 
	 * @param oldWidth
	 * @param oldHeight
	 * @return
	 */
	private int getImageViewHeight(int w, int h) {
		float marginHor = context.getResources().getDimension(R.dimen.mni_layout_margin_horizontal) * 2;
		float adjustMargin = context.getResources().getDimension(R.dimen.mni_layout_border_width) * 2;
		return Math.round(viewPreload.viewHeight(w, h, (marginHor + adjustMargin)));
	}

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
		RelativeLayout commentContentsLayout;
		RelativeLayout commentLayout;
		CustomViewFlipper commentFlip;
		RelativeLayout storeInfoLayout;
	}
}
