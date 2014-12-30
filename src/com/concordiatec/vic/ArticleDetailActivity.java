package com.concordiatec.vic;

import java.util.ArrayList;
import java.util.List;
import com.actionbarsherlock.view.Menu;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.R.id;
import com.concordiatec.vic.adapter.ArticleDetailCommentAdapter;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.model.Comment;
import com.concordiatec.vic.service.ArticleService;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.NotifyUtil;
import com.concordiatec.vic.util.TimeUtil;
import com.concordiatec.vic.widget.CircleImageView;
import com.google.gson.internal.LinkedTreeMap;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ArticleDetailActivity extends SubPageSherlockActivity{
	private List<Comment> comments;
	private ArticleDetailCommentAdapter adapter;
	private ListView commentList;
	private EditText commentContent;
	private View contentView;
	private ArticleService detailService;
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

		detailService = ArticleService.single(this);
		initHeaderView();
		initComments();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.article_detail, menu);
		return true;
	}
	
	/**
	 * initialize content view
	 */
	private void initHeaderView(){
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
		detailService.getDetail(new VicResponseListener() {
			@Override
			public void onResponseNoData() {
				NotifyUtil.toast(ArticleDetailActivity.this, getString(R.string.no_data));
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(1000);
							finish();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
			
			@Override
			public void onResponse(Object data) {
				Article detail = detailService.mapToModelFromDetail( (LinkedTreeMap<String,Object>)data );
				
				TextView storeName = (TextView) contentView.findViewById(R.id.news_store_name);
				TextView storeAddr = (TextView) contentView.findViewById(R.id.news_store_addr);
				CircleImageView imageView = (CircleImageView) contentView.findViewById(R.id.news_writer_photo);
				TextView writerName = (TextView) contentView.findViewById(R.id.news_writer_name);
				TextView writeTime = (TextView) contentView.findViewById(R.id.news_write_time);
				TextView content = (TextView) contentView.findViewById(R.id.news_content);
				TextView likeCount = (TextView) contentView.findViewById(R.id.news_like_btn);
				TextView commentCount = (TextView) contentView.findViewById(R.id.news_comment_btn);
				TextView likeShareText = (TextView) contentView.findViewById(R.id.ar_d_like_share_text);
				
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
				
				new Thread( new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(500);
							progress.setVisibility(View.GONE);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} ).start();
				
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
	
