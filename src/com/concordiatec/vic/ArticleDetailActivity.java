package com.concordiatec.vic;

import java.util.ArrayList;
import java.util.List;
import com.actionbarsherlock.view.Menu;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.adapter.ArticleDetailCommentAdapter;
import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.listener.SimpleVicResponseListener;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.model.ArticleImages;
import com.concordiatec.vic.model.Comment;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.model.User;
import com.concordiatec.vic.service.ArticleDetailService;
import com.concordiatec.vic.service.CommentService;
import com.concordiatec.vic.service.UserService;
import com.concordiatec.vic.tools.Tools;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.NotifyUtil;
import com.concordiatec.vic.util.ProgressUtil;
import com.concordiatec.vic.util.StringUtil;
import com.concordiatec.vic.util.TimeUtil;
import com.concordiatec.vic.widget.CircleImageView;
import com.concordiatec.vic.widget.TagView;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.internal.LinkedTreeMap;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ArticleDetailActivity extends SubPageSherlockActivity{
	private List<Comment> comments;
	private ArticleDetailCommentAdapter adapter;
	private ListView commentList;
	private EditText commentContent;
	private View contentView;
	private ArticleDetailService detailService;
	private CommentService commentService;
	private int articleId;
	private ScrollView contentScroll;
	private View sendCommentBtn;
	
	private Comment clickedComment;
	private int replyTargetId;
	private boolean isRefresh;
	protected TextView content;
	protected int clickedPosition;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.article_detail);
		
		contentScroll = (ScrollView) findViewById(R.id.ar_d_content_scroll);
		
		contentView = LayoutInflater.from(this).inflate(R.layout.article_detail_header, null);
		
		commentList = (ListView)findViewById(R.id.ar_d_comment_list);
		commentContent = (EditText)findViewById(R.id.ar_d_comment_input);
		
		sendCommentBtn = findViewById(R.id.send_comment);
		articleId = getIntent().getIntExtra("article_id", 0);

		detailService = new ArticleDetailService(this);
		commentService = new CommentService(this);
		initListView();
		
		sendCommentBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String writedContent = commentContent.getText().toString().trim();
				
				if( replyTargetId > 0 ) writedContent = writedContent.substring(clickedComment.getWriterName().length()).trim();
				
				if( writedContent.length() == 0 ){
					NotifyUtil.toast(ArticleDetailActivity.this, getString(R.string.comment_cannot_be_null));
					return;
				}
				User loginUser = checkLogin(ArticleDetailActivity.this);
				ProgressUtil.show(ArticleDetailActivity.this);
				
				Comment comment = new Comment();
				if( replyTargetId > 0 ) comment.setReplyId( replyTargetId );
				comment.setArticleId( articleId );
				comment.setWriterId( loginUser.usrId );
				comment.setContent( writedContent );
				int lastId = 0;
				if( adapter != null ){
					lastId = adapter.getLastRecordId();
				}
				commentService.writeComment(comment , lastId, new SimpleVicResponseListener(){
					@Override
					public void onSuccess(ResData data) {
						List<Comment> cmts = commentService.mapListToModelList((ArrayList<LinkedTreeMap<String, Object>>) data.getData());
						adapter.addData(cmts);
						commentContent.clearFocus();
						commentContent.setText(null);
						ProgressUtil.dismiss();
						NotifyUtil.toast(ArticleDetailActivity.this, getString(R.string.comment_succed));
					}
				});
			}
		});
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
		//get detail contents
		getArticleContents();
		//get article comments
		getComments();
	}

	private void refreshComments(){
		if( adapter != null ){
			adapter.clear();
		}
		this.isRefresh = true;
		getComments();
	}
	
	private void getArticleContents(){
		ProgressUtil.show(this);
		detailService.getDetail(articleId , new SimpleVicResponseListener() {
			@Override
			@SuppressWarnings("unchecked")
			public void onSuccess(ResData data) {
				content = (TextView) contentView.findViewById(R.id.news_content);
				Article detail = detailService.mapToModel( (LinkedTreeMap<String,Object>)data.getData() );
				CircleImageView imageView = (CircleImageView) contentView.findViewById(R.id.news_writer_photo);
				TextView writerName = (TextView) contentView.findViewById(R.id.news_writer_name);
				TextView writeTime = (TextView) contentView.findViewById(R.id.news_write_time);
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
				content.setText( detail.getContent().trim() );
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
				ProgressUtil.dismiss();
			}
		});
	}

	
	private void getComments(){
		commentService.getComments(articleId , new SimpleVicResponseListener() {
			@Override
			public void onSuccess(ResData data) {
				setCommentsAdapterData( data.getData() );
			}
			@Override
			public void onFailure(int httpResponseCode, String responseBody) {
				NotifyUtil.toast(getApplicationContext(), getString(R.string.failed_to_request_data));
			}
			@Override
			public void onEmptyResponse() {
				commentList.setVisibility(View.GONE);
				contentScroll.setVisibility(View.VISIBLE);
				contentScroll.addView(contentView);
			}
		});
		
	}

	private void setCommentsAdapterData(Object data){
		comments = commentService.mapListToModelList((ArrayList<LinkedTreeMap<String, Object>>) data);
		if( comments != null && comments.size() > 0 ){
			if( isRefresh ){
				adapter.setData(comments);
				isRefresh = false;
				return;
			}
			adapter = new ArticleDetailCommentAdapter(ArticleDetailActivity.this , comments);
			commentList.addHeaderView( contentView );
			commentList.setAdapter( adapter );
			commentList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					clickedPosition = position-1;
					clickedComment = adapter.getItem(clickedPosition);
					ArticleDetailActivity.this.openContextMenu( parent );
				}
				
			});
		}
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
	private final static int CONTEXT_COMMENT_SHOW_PLUS_MEMBER = 8;
	private final static int CONTEXT_COMMENT_EDIT = 6;
	private final static int CONTEXT_COMMENT_DELETE = 7;

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

		menu.setHeaderTitle(R.string.comment_context_title);
		User loginUser = new UserService(this).getLoginUser();
		//+1 혹은 취소
		if( clickedComment.isPlus() ){
			menu.add(0, CONTEXT_COMMENT_PLUS_CANCEL, CONTEXT_COMMENT_PLUS_CANCEL, R.string.comment_plus_cancel);
		}else {
			menu.add(0, CONTEXT_COMMENT_PLUS, CONTEXT_COMMENT_PLUS, R.string.comment_plus);
		}
		//복사
		menu.add(0, CONTEXT_COMMENT_CONTENT_COPY, CONTEXT_COMMENT_CONTENT_COPY, R.string.comment_copy);
		
		if( clickedComment.getWriterId() == loginUser.usrId ){
			//수정
			menu.add(0, CONTEXT_COMMENT_EDIT, CONTEXT_COMMENT_EDIT, R.string.comment_edit);
			//삭제
			menu.add(0, CONTEXT_COMMENT_DELETE, CONTEXT_COMMENT_DELETE, R.string.comment_delete);
		}else{
			//답글
			menu.add(0, CONTEXT_COMMENT_REPLY, CONTEXT_COMMENT_REPLY, R.string.comment_reply);
			//신고
			menu.add(0, CONTEXT_COMMENT_REPORT, CONTEXT_COMMENT_REPORT, R.string.comment_report);
		}
		
		if( clickedComment.getPlusCount() > 0 ){
			menu.add(0, CONTEXT_COMMENT_SHOW_PLUS_MEMBER, CONTEXT_COMMENT_SHOW_PLUS_MEMBER, R.string.comment_show_plus_member);
		}
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		User loginUser = checkLogin(ArticleDetailActivity.this);
		final int ctrlId = item.getItemId(); 
		switch (ctrlId) {
			case CONTEXT_COMMENT_PLUS:
			case CONTEXT_COMMENT_PLUS_CANCEL:
				//공감 +1/ 취소
				commentService.likeComment(clickedComment.getId(), loginUser.usrId, new SimpleVicResponseListener(){
					@Override
					public void onSuccess(ResData data) {
						if( ctrlId == CONTEXT_COMMENT_PLUS ){
							clickedComment.setPlus(true);
							clickedComment.setPlusCount( clickedComment.getPlusCount()+1 );
						}else{
							clickedComment.setPlus(false);
							clickedComment.setPlusCount( clickedComment.getPlusCount()-1 );
						}
						adapter.updateData(clickedComment, clickedPosition);
					}
				});
				break;
			case CONTEXT_COMMENT_REPLY:
				if( commentContent.getText().toString().trim().length() > 0 ){
					NotifyUtil.showDialogWithPositive(this, getString(R.string.comment_cancel), new DialogInterface.OnClickListener() {  
				        @Override  
				        public void onClick(DialogInterface dialog, int which) {
				        	setReplyTarget();
				        }  
				    });
				}else{
					setReplyTarget();
				}
				break;
			case CONTEXT_COMMENT_CONTENT_COPY:
				//내용 복사
				ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				cmb.setPrimaryClip(ClipData.newPlainText(null, clickedComment.getContent().trim()));  
				NotifyUtil.toast(this, getString(R.string.copy_content_succed));
				break;
			case CONTEXT_COMMENT_REPORT:
				//신고
				break;
			case CONTEXT_COMMENT_SHOW_PLUS_MEMBER:
				//공감한 사람 보기
				break;
			case CONTEXT_COMMENT_DELETE:
				//삭제
				NotifyUtil.showDialogWithPositive(this, getString(R.string.sure_to_delete), new DialogInterface.OnClickListener() {  
			        @Override  
			        public void onClick(DialogInterface dialog, int which) {
			        	
			        }  
			    });
				break;
			case CONTEXT_COMMENT_EDIT:
				//수정
				break;
			default:
				break;
		}
		return true;
	}
	private void setReplyTarget(){
		replyTargetId = clickedComment.getId();
		commentContent.setText(null);
    	//답글
		TagView tagView = new TagView(ArticleDetailActivity.this);
		TagView.Tag tag = new TagView.Tag(clickedComment.getWriterName(), getResources().getColor(R.color.background_color_alpha) );
		tagView.setTextColor(getResources().getColor(R.color.light_font));
		tagView.setTextSize(14);
		tagView.setTagCornerRadius(2);
		tagView.setTagPaddingHor(20);
		tagView.setTagPaddingVert(10);
		tagView.setSingleTag(tag);
		Bitmap bm = Tools.convertViewToBitmap(tagView);
		
		Drawable drawable = new BitmapDrawable(getResources(), bm);
		drawable.setBounds(0 , 10 , drawable.getIntrinsicWidth()+10, drawable.getIntrinsicHeight()+10);
		
		SpannableString spanString = new SpannableString(clickedComment.getWriterName());
		
		spanString.setSpan(new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE), 0 , spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		Editable aEditable = commentContent.getEditableText();
		aEditable.insert(0, spanString);						
		commentContent.requestFocus();
		commentContent.requestFocusFromTouch();
	}
	
}
	
