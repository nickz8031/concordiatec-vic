package com.concordiatec.vic;

import java.util.ArrayList;
import java.util.List;
import com.actionbarsherlock.view.Menu;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.ViewTarget;
import com.concordiatec.vic.adapter.ArticleDetailCommentAdapter;
import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.model.ArticleImages;
import com.concordiatec.vic.model.Comment;
import com.concordiatec.vic.service.ArticleDetailService;
import com.concordiatec.vic.service.ArticleListService;
import com.concordiatec.vic.service.CommentService;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.NotifyUtil;
import com.concordiatec.vic.util.TimeUtil;
import com.concordiatec.vic.widget.CircleImageView;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.internal.LinkedTreeMap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("InflateParams")
@SuppressWarnings("unused")
public class ArticleDetailActivity extends SubPageSherlockActivity{
	private List<Comment> comments;
	private ArticleDetailCommentAdapter adapter;
	private ListView commentList;
	private EditText commentContent;
	private View contentView;
	private ArticleDetailService detailService;
	private CommentService commentService;
	private int articleId;
	private LinearLayout progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.article_detail);
		
		progress = (LinearLayout) findViewById(R.id.detail_loading);
		
		commentList = (ListView)findViewById(R.id.ar_d_comment_list);
		commentContent = (EditText)findViewById(R.id.ar_d_comment_input);
		
		Intent intent = getIntent();
		articleId = intent.getIntExtra("article_id", 0);

		detailService = ArticleDetailService.single(this);
		commentService = CommentService.single(this);
		initListView();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.article_detail, menu);
		return true;
	}
	
	/**
	 * initialize content view
	 */
	private void initListView(){
		contentView = LayoutInflater.from(this).inflate(R.layout.article_detail_header, null);
		commentList.addHeaderView( contentView );
		TextView footerPadding = new TextView(this);
		int height = (int) getResources().getDimension(R.dimen.detail_comment_input);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, height);
		footerPadding.setLayoutParams(params);
		commentList.addFooterView(footerPadding);
		fillContent();
	}
	
	private void fillContent(){
		//get detail contents
		getArticleContents();
		//get article comments
		getComments();
		
	}
	
	private void getArticleContents(){
		detailService.getDetail(new VicResponseListener() {
			@Override
			public void onResponseNoData() {
				noDataToast(ArticleDetailActivity.this);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onResponse(Object data) {
				Article detail = detailService.mapToModel( (LinkedTreeMap<String,Object>)data );
				
				TextView storeName = (TextView) contentView.findViewById(R.id.news_store_name);
				TextView storeAddr = (TextView) contentView.findViewById(R.id.news_store_addr);
				CircleImageView imageView = (CircleImageView) contentView.findViewById(R.id.news_writer_photo);
				TextView writerName = (TextView) contentView.findViewById(R.id.news_writer_name);
				TextView writeTime = (TextView) contentView.findViewById(R.id.news_write_time);
				TextView content = (TextView) contentView.findViewById(R.id.news_content);
				TextView likeCount = (TextView) contentView.findViewById(R.id.news_like_btn);
				TextView commentCount = (TextView) contentView.findViewById(R.id.news_comment_btn);
				TextView likeShareText = (TextView) contentView.findViewById(R.id.ar_d_like_share_text);
				
				SliderLayout sliderLayout = (SliderLayout) contentView.findViewById(R.id.news_content_img_slider);
				ImageView contentImg = (ImageView) contentView.findViewById(R.id.news_content_img);
				
				storeName.setText( detail.getShopName() );
				storeAddr.setText( detail.getShopAddr() );
				
				Glide.with(ArticleDetailActivity.this).load(detail.getWriterPhotoURL()).crossFade().into(imageView);
				
				writerName.setText(detail.getWriterName());
				writeTime.setText( TimeUtil.getTimePast( ArticleDetailActivity.this, detail.getPastTime() ) );
				content.setText( detail.getContent() );
				likeCount.setText( detail.getLikeCount()+"" );
				commentCount.setText( detail.getCommentCount()+"" );
				String lst = likeShareText.getText().toString();
				likeShareText.setText( String.format(lst, detail.getLikeCount() , detail.getShareCount() ) );
				
				
				List<ArticleImages> imgs = detail.getImages();

				if( imgs.size() > 1 ){
					//make viewPager
					for (int i = 0; i < imgs.size(); i++) {
						DefaultSliderView dSliderView = new DefaultSliderView(ArticleDetailActivity.this);
						dSliderView.image(imgs.get(i).getName());
						sliderLayout.addSlider(dSliderView);
					}
					sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
					sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
					sliderLayout.stopAutoCycle();
					sliderLayout.setVisibility(View.VISIBLE);
				}else{
					//show image
					Glide.with(ArticleDetailActivity.this).load(imgs.get(0).getName()).into(contentImg);
					contentImg.setVisibility(View.VISIBLE);
				}
				progress.setVisibility(View.GONE);
			}
		}, articleId);
	}

	@SuppressWarnings("unchecked")
	private void getComments(){
		commentService.getComments(new VicResponseListener() {
			@Override
			public void onResponseNoData() {}
			@Override
			public void onResponse(Object data) {
				List<Comment> listComments = commentService.mapListToModelList((ArrayList<LinkedTreeMap<String, Object>>) data);
				commentList.setAdapter( new ArticleDetailCommentAdapter(ArticleDetailActivity.this , listComments) );
			}
		}, articleId);
	}
	
	/**
	 * initialize comments list
	 */
	private void initComments(){
		comments = new ArrayList<Comment>();
		for (int i = 0; i < 15; i++) {
			Comment c = new Comment();
			c.setId(i+1);
			comments.add(c);
		}
		
		adapter = new ArticleDetailCommentAdapter(this , comments);
		commentList.setAdapter(adapter);
	}
	
}
	
