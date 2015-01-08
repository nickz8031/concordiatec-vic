package com.concordiatec.vic;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.service.ArticleDetailService;
import com.concordiatec.vic.service.ArticleListService;
import com.concordiatec.vic.service.CommentService;
import com.concordiatec.vic.tools.ImageViewPreload;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.NotifyUtil;
import com.concordiatec.vic.util.StringUtil;
import com.concordiatec.vic.util.TimeUtil;
import com.concordiatec.vic.widget.CircleImageView;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.internal.LinkedTreeMap;
import android.R.menu;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
	private ScrollView contentScroll;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.article_detail);
		
		contentScroll = (ScrollView) findViewById(R.id.ar_d_content_scroll);
		
		progress = (LinearLayout) findViewById(R.id.detail_loading);
		
		contentView = LayoutInflater.from(this).inflate(R.layout.article_detail_header, null);
		
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
		TextView footerPadding = new TextView(this);
		int height = (int) getResources().getDimension(R.dimen.detail_comment_input);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, height);
		footerPadding.setLayoutParams(params);
		commentList.addFooterView(footerPadding);
		
		registerForContextMenu( commentList );
		
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
			
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object data) {
				Article detail = detailService.mapToModel( (LinkedTreeMap<String,Object>)data );
				
				CircleImageView imageView = (CircleImageView) contentView.findViewById(R.id.news_writer_photo);
				TextView writerName = (TextView) contentView.findViewById(R.id.news_writer_name);
				TextView writeTime = (TextView) contentView.findViewById(R.id.news_write_time);
				TextView content = (TextView) contentView.findViewById(R.id.news_content);
				TextView likeCount = (TextView) contentView.findViewById(R.id.news_like_btn);
				TextView commentCount = (TextView) contentView.findViewById(R.id.news_comment_btn);
				TextView likeShareText = (TextView) contentView.findViewById(R.id.ar_d_like_share_text);
				
				SliderLayout sliderLayout = (SliderLayout) contentView.findViewById(R.id.news_content_img_slider);
				ImageView contentImg = (ImageView) contentView.findViewById(R.id.news_content_img);
				
				if( !StringUtil.isEmpty(detail.getShopName()) ){
					TextView storeName = (TextView) contentView.findViewById(R.id.news_store_name);
					TextView storeAddr = (TextView) contentView.findViewById(R.id.news_store_addr);
					
					storeName.setText( detail.getShopName() );
					storeAddr.setText( detail.getShopAddr() );
				}else{
					RelativeLayout storeInfoLayout = (RelativeLayout) contentView.findViewById(R.id.store_info_layout);
					storeInfoLayout.setVisibility(View.GONE);
				}
				
				
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
					Glide.with(ArticleDetailActivity.this).load(imgs.get(0).getName()).crossFade().into(contentImg);
					contentImg.setVisibility(View.VISIBLE);
				}
				
				commentCount.setOnClickListener(new CommentBtnClickListener());
				
				
				progress.setVisibility(View.GONE);
			}

			@Override
			public void onError(ResData error) {}

			@Override
			public void onFailure(String reason) {}
		}, articleId);
	}

	@SuppressWarnings("unchecked")
	private void getComments(){
		commentService.getComments(new VicResponseListener() {
			@Override
			public void onSuccess(Object data) {
				List<Comment> listComments = commentService.mapListToModelList((ArrayList<LinkedTreeMap<String, Object>>) data);
				if( listComments != null && listComments.size() > 0 ){
					adapter = new ArticleDetailCommentAdapter(ArticleDetailActivity.this , listComments);
					commentList.addHeaderView( contentView );
					commentList.setAdapter( adapter );
					commentList.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							
							HashMap<String, Boolean> dataStatus = new HashMap<String, Boolean>();
							
							Comment cmt = adapter.getItem((int) id);
							
							if( cmt.isPlus() ){
								dataStatus.put("is_like", true);
							}
							if( cmt.getPlusCount() > 0 ){
								dataStatus.put("is_show_plus", true);
							}
							parent.setTag(dataStatus);
							ArticleDetailActivity.this.openContextMenu( parent );
						}
						
					});
				}
			}
			@Override
			public void onError(ResData error) {
				commentList.setVisibility(View.GONE);
				contentScroll.setVisibility(View.VISIBLE);
				contentScroll.addView(contentView);
			}
			@Override
			public void onFailure(String reason) {}
		}, articleId);
	}
	
	private final class CommentBtnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			commentContent.requestFocus();
			InputMethodManager imm = (InputMethodManager) commentContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
			
		}
	}
		
	private final static int CONTEXT_COMMENT_PLUS = 1;
	private final static int CONTEXT_COMMENT_PLUS_CANCEL = 2;
	private final static int CONTEXT_COMMENT_REPLY = 3;
	private final static int CONTEXT_COMMENT_CONTENT_COPY = 4;
	private final static int CONTEXT_COMMENT_REPORT = 5;
	private final static int CONTEXT_COMMENT_SHOW_PLUS_MEMBER = 6;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

		menu.setHeaderTitle(R.string.comment_context_title);
		HashMap<String, Boolean> status = (HashMap<String, Boolean>) v.getTag();
		//+1 혹은 취소
		if( status.containsKey("is_like") ){
			menu.add(0, CONTEXT_COMMENT_PLUS_CANCEL, CONTEXT_COMMENT_PLUS_CANCEL, R.string.comment_plus_cancel);
		}else {
			menu.add(0, CONTEXT_COMMENT_PLUS, CONTEXT_COMMENT_PLUS, R.string.comment_plus);
		}
		//답글
		menu.add(0, CONTEXT_COMMENT_REPLY, CONTEXT_COMMENT_REPLY, R.string.comment_reply);
		//복사
		menu.add(0, CONTEXT_COMMENT_CONTENT_COPY, CONTEXT_COMMENT_CONTENT_COPY, R.string.comment_copy);
		//신고
		menu.add(0, CONTEXT_COMMENT_REPORT, CONTEXT_COMMENT_REPORT, R.string.comment_report);
		
		if( status.containsKey("is_show_plus") ){
			menu.add(0, CONTEXT_COMMENT_SHOW_PLUS_MEMBER, CONTEXT_COMMENT_SHOW_PLUS_MEMBER, R.string.comment_show_plus_member);
		}
		
		
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		LogUtil.show(item.getItemId() + "");
		
		return true;
	}
}
	
