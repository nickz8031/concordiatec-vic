package com.concordiatec.vic;

import java.util.ArrayList;
import java.util.List;
import com.actionbarsherlock.view.Menu;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.adapter.ArticleDetailCommentAdapter;
import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.constant.Constant;
import com.concordiatec.vic.listener.SimpleVicResponseListener;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.model.ArticleImages;
import com.concordiatec.vic.model.Comment;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.model.User;
import com.concordiatec.vic.service.ArticleDetailService;
import com.concordiatec.vic.service.ArticleService;
import com.concordiatec.vic.service.CommentService;
import com.concordiatec.vic.service.UserService;
import com.concordiatec.vic.tools.ImageViewPreload;
import com.concordiatec.vic.tools.Tools;
import com.concordiatec.vic.util.NotifyUtil;
import com.concordiatec.vic.util.ProgressUtil;
import com.concordiatec.vic.util.StringUtil;
import com.concordiatec.vic.util.TimeUtil;
import com.concordiatec.vic.widget.CircleImageView;
import com.google.gson.internal.LinkedTreeMap;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ArticleDetailActivity extends SubPageSherlockActivity {
	private final static int CONTEXT_COMMENT_PLUS = 1;
	private final static int CONTEXT_COMMENT_PLUS_CANCEL = 2;
	private final static int CONTEXT_COMMENT_REPLY = 3;
	private final static int CONTEXT_COMMENT_CONTENT_COPY = 4;
	private final static int CONTEXT_COMMENT_REPORT = 5;
	private final static int CONTEXT_COMMENT_EDIT = 6;
	private final static int CONTEXT_COMMENT_DELETE = 7;
	private final static int CONTEXT_COMMENT_SHOW_PLUS_MEMBER = 8;
	private ListView commentList;
	private EditText commentContent;
	private View contentView;
	private View sendCommentBtn;
	private TextView content;
	protected TextView commentCount;
	protected TextView likeShareText;
	private ArticleDetailService detailService = new ArticleDetailService(this);
	private CommentService commentService = new CommentService(this);
	private ArticleService aService = new ArticleService(this);
	private ArticleDetailCommentAdapter adapter;
	private List<Comment> comments;
	protected Article detail;
	private Comment clickedComment;
	private int articleId;
	private int replyTargetId;
	private boolean isRefresh;
	protected int clickedPosition;
	protected int currentCommentCount = 0;
	protected List<ArticleImages> imgs;
	private ArrayList<String> imgList;
	private com.actionbarsherlock.view.MenuItem deleteArticleMenu;
	private com.actionbarsherlock.view.MenuItem editArticleMenu;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		comments = null;
		adapter = null;
		commentList = null;
		contentView = null;
		detailService = null;
		commentService = null;
		aService = null;
		sendCommentBtn = null;
		clickedComment = null;
		content = null;
		detail = null;
		likeShareText = null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_detail);
		contentView = LayoutInflater.from(this).inflate(R.layout.article_detail_header, null);
		commentList = (ListView) findViewById(R.id.ar_d_comment_list);
		commentContent = (EditText) findViewById(R.id.ar_d_comment_input);
		sendCommentBtn = findViewById(R.id.send_comment);
		articleId = getIntent().getIntExtra("article_id", 0);
		imgList = new ArrayList<String>();
		initListView();
		sendCommentBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String writedContent = commentContent.getText().toString().trim();
				if (replyTargetId > 0) writedContent = writedContent.substring(clickedComment.getWriterName().length()).trim();
				if (writedContent.length() == 0) {
					NotifyUtil.toast(ArticleDetailActivity.this, getString(R.string.comment_cannot_be_null));
					return;
				}
				User loginUser = checkLogin(ArticleDetailActivity.this);
				if (loginUser == null) { return; }
				ProgressUtil.show(ArticleDetailActivity.this);
				Comment comment = new Comment();
				if( replyTargetId > 0 ) comment.setReplyId( replyTargetId );
				comment.setArticleId(articleId);
				comment.setWriterId(loginUser.usrId);
				comment.setContent(writedContent);
				int lastId = 0;
				if (adapter != null) {
					lastId = adapter.getLastRecordId();
				}
				
				commentService.writeComment(comment, lastId, new SimpleVicResponseListener() {
					@Override
					public void onSuccess(ResData data) {						
						if( adapter != null ){
							List<Comment> cmts = commentService.mapListToModelList((ArrayList<LinkedTreeMap<String, Object>>) data.getData());
							adapter.addData(cmts);
						}else{
							setCommentsAdapterData(data.getData());
						}
						commentContent.clearFocus();
						commentContent.setText(null);
						currentCommentCount++;
						commentCount.setText(currentCommentCount + "");
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

	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		// 새로 고침
		case R.id.ar_d_menu_refresh:
			recreate();
			break;
		// 글 수정
		case R.id.ar_d_menu_edit:
			Intent intent = new Intent(this, ArticleEditActivity.class);
			intent.putExtra("article", detail);
			startActivityForResult(intent, Constant.ARTICLE_EDIT);
			break;
		// 글 삭제
		case R.id.ar_d_menu_delete:
			ProgressUtil.show(this);
			aService.deleteArticle(detail.getId(), new SimpleVicResponseListener() {
				@Override
				public void onSuccess(ResData data) {
					NotifyUtil.toast(ArticleDetailActivity.this, getString(R.string.delete_succed));
					setResult(Constant.ARTICLE_DELETE_SUCCED);
					finish();
				}
			});
			break;
		case R.id.ar_d_menu_ignore:
			break;
		// 글 신고
		case R.id.ar_d_menu_report:
			break;
		// 내용 복사
		case R.id.ar_d_menu_copy_text:
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case Constant.COMMENT_EDIT: // 댓글 수정 성공시
			if (resultCode == Constant.COMMENT_EDIT_SUCCED && data.hasExtra("edit_comment")) {
				Comment editComment = (Comment) data.getExtras().get("edit_comment");
				adapter.updateData(editComment, clickedPosition);
			}
			break;
		case Constant.ARTICLE_EDIT: // 원글내용 수정 성공시
			if (resultCode == RESULT_OK && data.hasExtra("edit_article")) {
				Article editDetail = (Article) data.getExtras().get("edit_article");
				content.setText(editDetail.getContent());
				setResult(Constant.ARTICLE_EDIT_SUCCED, new Intent().putExtra("edit_article", editDetail.getContent()));
			}
			break;
		default:
			break;
		}
	}

	/**
	 * initialize content view
	 */
	private void initListView() {
		TextView footerPadding = new TextView(this);
		int height = (int) getResources().getDimension(R.dimen.detail_comment_input);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, height);
		footerPadding.setLayoutParams(params);
		commentList.addFooterView(footerPadding);
		registerForContextMenu(commentList);
		// get detail contents
		getArticleContents();

		// get article comments
		getComments();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		editArticleMenu = menu.findItem(R.id.ar_d_menu_edit);
		deleteArticleMenu = menu.findItem(R.id.ar_d_menu_delete);
		return true;
	}

	private void getArticleContents() {
		ProgressUtil.show(this);
		detailService.getDetail(articleId, new SimpleVicResponseListener() {
			@Override
			@SuppressWarnings("unchecked")
			public void onSuccess(ResData data) {
				detail = detailService.mapToModel((LinkedTreeMap<String, Object>) data.getData());
				
				content = (TextView) contentView.findViewById(R.id.news_content);
				commentCount = (TextView) contentView.findViewById(R.id.news_comment_btn);
				likeShareText = (TextView) contentView.findViewById(R.id.ar_d_like_share_text);
				CircleImageView imageView = (CircleImageView) contentView.findViewById(R.id.news_writer_photo);
				TextView writerName = (TextView) contentView.findViewById(R.id.news_writer_name);
				TextView writeTime = (TextView) contentView.findViewById(R.id.news_write_time);
				TextView likeCount = (TextView) contentView.findViewById(R.id.news_like_btn);
				RelativeLayout contentImgWrap = (RelativeLayout) contentView.findViewById(R.id.news_content_image_layout);
				ImageView contentImg = (ImageView) contentView.findViewById(R.id.news_content_img);
				User loginUser = new UserService(ArticleDetailActivity.this).getLoginUser();
				if (loginUser == null || loginUser.usrId != detail.getWriterId()) {
					editArticleMenu.setVisible(false);
					deleteArticleMenu.setVisible(false);
				}
				if (!StringUtil.isEmpty(detail.getShopName())) {
					TextView storeName = (TextView) contentView.findViewById(R.id.news_store_name);
					TextView storeAddr = (TextView) contentView.findViewById(R.id.news_store_addr);
					storeName.setText(detail.getShopName());
					storeAddr.setText(detail.getShopAddr());
				} else {
					RelativeLayout storeInfoLayout = (RelativeLayout) contentView.findViewById(R.id.store_info_layout);
					storeInfoLayout.setVisibility(View.GONE);
				}
				currentCommentCount = detail.getCommentCount();
				Glide.with(ArticleDetailActivity.this).load(detail.getWriterPhotoURL()).crossFade().into(imageView);
				writerName.setText(detail.getWriterName());
				String timePast = TimeUtil.getTimePast(getApplicationContext(), detail.getPastTime());
				writeTime.setText(timePast);
				content.setText(detail.getContent().trim());
				likeCount.setText(detail.getLikeCount() + "");
				if (detail.isLike()) {
					setLike(likeCount);
					likeCount.setTag(true);
				}
				commentCount.setText(detail.getCommentCount() + "");
				setLikeShareCount();
				imgs = detail.getImages();
				if (imgs.size() > 1) {
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentImgWrap.getLayoutParams();
					HorizontalScrollView contentScroll = (HorizontalScrollView) contentView.findViewById(R.id.content_img_hor_scroll);
					LinearLayout contentImgLayout = (LinearLayout) contentView.findViewById(R.id.content_img_layout);
					LinearLayout imgTitleLayout = (LinearLayout) contentView.findViewById(R.id.news_content_image_title);
					TextView tView1 = (TextView) contentView.findViewById(R.id.news_content_image_title_1);
					TextView tView2 = (TextView) contentView.findViewById(R.id.news_content_image_title_2);
					tView1.setText(timePast);
					tView2.setText(String.format(getString(R.string.format_detail_img_multi_count), imgs.size()));
					DisplayMetrics dMetrics = Tools.getDisplayMetrics(ArticleDetailActivity.this, getResources().getDimension(R.dimen.mni_layout_border_width));
					int maxWidth = Tools.getMultiImgMaxW(dMetrics.widthPixels);
					int displayMinHeight = Tools.getMinHeight(getApplicationContext());
					float minHeight = detail.getMinHeight();
					if (minHeight < displayMinHeight) minHeight = displayMinHeight;
					float scale, rWidth, rHeight;
					// 최대 허용 너비보다 더 큰 이미지가 존재한다면 최소높이 다시 구해야 됨.
					for (ArticleImages articleImages : imgs) {
						// 이미지 경로 저장
						imgList.add(articleImages.getName());
						// 보여질 높이를 최소높이로 설정
						rHeight = minHeight;
						// minHeight보다 작은것 존재하지 않음
						scale = minHeight / articleImages.getHeight();
						rWidth = articleImages.getWidth() * scale;
						// 폭이 제한된것보다 크다면
						if (rWidth > maxWidth) {
							scale = maxWidth / rWidth;
							rHeight = rHeight * scale;
							if (minHeight > rHeight) minHeight = rHeight;
						}
					}
					// imageView 생성 및 삽입
					int i = 1;
					for (ArticleImages articleImages : imgs) {
						rHeight = minHeight;
						scale = minHeight / articleImages.getHeight();
						rWidth = articleImages.getWidth() * scale;
						ImageView imView = new ImageView(getApplicationContext());
						layoutParams.height = (int) minHeight;
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Tools.getIntValue(rWidth), Tools.getIntValue(rHeight));
						if (i < imgs.size()) params.setMargins(0, 0, 20, 0);
						imView.setLayoutParams(params);
						imView.setAdjustViewBounds(true);
						Glide.with(getApplicationContext()).load(articleImages.getName()).thumbnail(0.3f).crossFade().into(imView);
						contentImgLayout.addView(imView);
						i++;
					}
					contentImgWrap.setLayoutParams(layoutParams);
					contentImgLayout.setOnClickListener(new PhotoClickListener());
					imgTitleLayout.setVisibility(View.VISIBLE);
					contentScroll.setVisibility(View.VISIBLE);
				} else {
					ArticleImages img = imgs.get(0);
					// 경로 저장 상세보기 위함
					imgList.add(imgs.get(0).getName());
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, getImageViewHeight(img.getWidth(), img.getHeight()));
					// show image
					Glide.with(getApplicationContext()).load(imgs.get(0).getName()).thumbnail(0.3f).crossFade().into(contentImg);
					contentImg.setLayoutParams(params);
					contentImg.setVisibility(View.VISIBLE);
					contentImg.setOnClickListener(new PhotoClickListener());
				}
				commentCount.setOnClickListener(new CommentBtnClickListener());
				likeCount.setOnClickListener(new LikeButtonClickListener());
				
				commentList.addHeaderView(contentView);
				commentList.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						clickedPosition = position - 1;
						clickedComment = adapter.getItem(clickedPosition);
						ArticleDetailActivity.this.openContextMenu(parent);
					}
				});
				
				ProgressUtil.dismiss();
			}
		});
	}

	private final class PhotoClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (!imgList.isEmpty()) {
				Intent intent = new Intent(ArticleDetailActivity.this, ArticleImagesActivity.class);
				intent.putStringArrayListExtra("photo_paths", imgList);
				startActivity(intent);
			}
		}
	}

	/**
	 * get content imageView height
	 * 
	 * @param oldWidth
	 * @param oldHeight
	 * @return
	 */
	private int getImageViewHeight(int w, int h) {
		float adjustMargin = getResources().getDimension(R.dimen.mni_layout_border_width) * 2;
		return Math.round(new ImageViewPreload(this).viewHeight(w, h, adjustMargin));
	}

	private void getComments() {
		commentService.getComments(articleId, new SimpleVicResponseListener() {
			@Override
			public void onSuccess(ResData data) {
				setCommentsAdapterData(data.getData());
			}

			@Override
			public void onFailure(int httpResponseCode, String responseBody) {
				NotifyUtil.toast(getApplicationContext(), getString(R.string.failed_to_request_data));
			}

			@Override
			public void onEmptyResponse() {
				setCommentsAdapterData(null);
			}
		});
	}

	private void setCommentsAdapterData(Object data) {
		if( data == null ){
			commentList.setAdapter(null);
		}else{
			comments = commentService.mapListToModelList((ArrayList<LinkedTreeMap<String, Object>>) data);
			if (comments != null && comments.size() > 0) {
				if (isRefresh) {
					adapter.setData(comments);
					isRefresh = false;
					return;
				}
				adapter = new ArticleDetailCommentAdapter(ArticleDetailActivity.this, comments);
				commentList.setAdapter(adapter);
			}
		}
	}

	/**
	 * comment button click action
	 * 
	 * @author Nick.Z
	 *
	 */
	private final class CommentBtnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			focusCommContent();
		}
	}

	/**
	 * request focus to Comment content Edit text
	 */
	private void focusCommContent() {
		commentContent.requestFocus();
		commentContent.requestFocusFromTouch();
		InputMethodManager imm = (InputMethodManager) commentContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * 좋아요 버튼 클릭시 실행액션
	 * 
	 * @author Nick.Z
	 *
	 */
	private final class LikeButtonClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v.getTag() == null) {
				activeLikeAnimation(v);
			}
			likeArticle(v, articleId);
		}
	}

	/**
	 * like Article 좋아요
	 * 
	 * @param v
	 */
	private void likeArticle(View v, int articleId) {
		final TextView t = (TextView) v;
		final boolean isLike = (v.getTag() == null);
		int likeCount = Integer.parseInt(t.getText().toString());
		if (isLike) {
			t.setText((likeCount + 1) + "");
			setLike(t);
			t.setTag(true);
		} else {
			if (likeCount > 0) {
				t.setText((likeCount - 1) + "");
			}
			setDislike(t);
			t.setTag(null);
		}
		aService.likeArticle(articleId, new SimpleVicResponseListener() {
			@Override
			public void onSuccess(ResData data) {
				if (isLike) {
					detail.setLikeCount(detail.getLikeCount() + 1);
				} else {
					if (detail.getLikeCount() > 0) {
						detail.setLikeCount(detail.getLikeCount() - 1);
					}
				}
				setLikeShareCount();
			}
		});
	}

	/**
	 * 좋아요 액션
	 * 
	 * @param v
	 */
	private void setLike(View v) {
		TextView t = (TextView) v;
		t.setTextColor(Color.WHITE);
		t.setBackgroundResource(R.drawable.news_ctrl_btn_active);
		Drawable drawable = getResources().getDrawable(R.drawable.ic_action_thumb_up_white);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		t.setCompoundDrawables(drawable, null, null, null);
	}

	/**
	 * 좋아요 취소 액션
	 * 
	 * @param v
	 */
	private void setDislike(View v) {
		TextView t = (TextView) v;
		t.setTextColor(getResources().getColor(R.color.mni_ctrl_btn_text));
		t.setBackgroundResource(R.drawable.news_ctrl_btn_selector);
		Drawable drawable = getResources().getDrawable(R.drawable.ic_action_thumb_up);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		t.setCompoundDrawables(drawable, null, null, null);
	}

	/**
	 * 몇명이 좋아하고 몇명이 다시 공유함.
	 */
	private void setLikeShareCount() {
		likeShareText.setText(String.format(getResources().getString(R.string.format_detail_like_share), detail.getLikeCount(), detail.getShareCount()));
	}

	/**
	 * 좋아요 클릭시 진행되는 애니메이션 효과
	 * 
	 * @param v
	 */
	private void activeLikeAnimation(View v) {
		final View target = v;
		final Animation toBig = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.big_scale);
		final Animation toNormal = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.small_scale);
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
	 * 댓글 클릭시 나오는 메뉴
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.setHeaderTitle(R.string.comment_context_title);
		User loginUser = new UserService(this).getLoginUser();
		if (loginUser != null) {
			// 공감 혹은 취소
			if (clickedComment.isPlus()) {
				menu.add(0, CONTEXT_COMMENT_PLUS_CANCEL, CONTEXT_COMMENT_PLUS_CANCEL, R.string.comment_plus_cancel);
			} else {
				menu.add(0, CONTEXT_COMMENT_PLUS, CONTEXT_COMMENT_PLUS, R.string.comment_plus);
			}
			if (clickedComment.getWriterId() == loginUser.usrId) {
				// 수정
				menu.add(0, CONTEXT_COMMENT_EDIT, CONTEXT_COMMENT_EDIT, R.string.menu_edit);
				// 삭제
				menu.add(0, CONTEXT_COMMENT_DELETE, CONTEXT_COMMENT_DELETE, R.string.menu_delete);
			} else {
				// 답글
				menu.add(0, CONTEXT_COMMENT_REPLY, CONTEXT_COMMENT_REPLY, R.string.comment_reply);
				// 신고
				menu.add(0, CONTEXT_COMMENT_REPORT, CONTEXT_COMMENT_REPORT, R.string.menu_report);
			}
		}
		// 복사
		menu.add(0, CONTEXT_COMMENT_CONTENT_COPY, CONTEXT_COMMENT_CONTENT_COPY, R.string.comment_copy);
		if (clickedComment.getPlusCount() > 0) {
			menu.add(0, CONTEXT_COMMENT_SHOW_PLUS_MEMBER, CONTEXT_COMMENT_SHOW_PLUS_MEMBER, R.string.comment_show_plus_member);
		}
	}

	/**
	 * 댓글 클릭시 액션 진행
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final User loginUser = checkLogin(ArticleDetailActivity.this);
		final int ctrlId = item.getItemId();
		switch (ctrlId) {
		case CONTEXT_COMMENT_PLUS:
		case CONTEXT_COMMENT_PLUS_CANCEL:
			// 공감 +1/ 취소
			commentService.likeComment(clickedComment.getId(), loginUser.usrId, new SimpleVicResponseListener() {
				@Override
				public void onSuccess(ResData data) {
					if (ctrlId == CONTEXT_COMMENT_PLUS) {
						clickedComment.setPlus(true);
						clickedComment.setPlusCount(clickedComment.getPlusCount() + 1);
					} else {
						clickedComment.setPlus(false);
						clickedComment.setPlusCount(clickedComment.getPlusCount() - 1);
					}
					adapter.updateData(clickedComment, clickedPosition);
				}
			});
			break;
		case CONTEXT_COMMENT_REPLY:
			if (commentContent.getText().toString().trim().length() > 0) {
				NotifyUtil.showDialogWithPositive(this, getString(R.string.comment_cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						setReplyTarget();
					}
				});
			} else {
				setReplyTarget();
			}
			break;
		case CONTEXT_COMMENT_CONTENT_COPY:
			// 내용 복사
			ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			cmb.setPrimaryClip(ClipData.newPlainText(null, clickedComment.getContent().trim()));
			NotifyUtil.toast(this, getString(R.string.copy_content_succed));
			break;
		case CONTEXT_COMMENT_REPORT:
			// 신고
			break;
		case CONTEXT_COMMENT_SHOW_PLUS_MEMBER:
			// 공감한 사람 보기
			break;
		case CONTEXT_COMMENT_DELETE:
			// 삭제
			NotifyUtil.showDialogWithPositive(this, getString(R.string.sure_to_delete), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ProgressUtil.show(ArticleDetailActivity.this);
					commentService.deleteComment(clickedComment.getId(), loginUser.usrId, new SimpleVicResponseListener() {
						@Override
						public void onSuccess(ResData data) {
							adapter.deleteData(clickedPosition);
							if (currentCommentCount > 1) {
								currentCommentCount--;
								commentCount.setText(currentCommentCount + "");
							}
							NotifyUtil.toast(ArticleDetailActivity.this, getString(R.string.delete_succed));
							ProgressUtil.dismiss();
						}

						@Override
						public void onFailure(int httpResponseCode, String responseBody) {
							ProgressUtil.dismiss();
							NotifyUtil.toast(ArticleDetailActivity.this, getString(R.string.failed_to_request_data));
						}
					});
				}
			});
			break;
		case CONTEXT_COMMENT_EDIT:
			// 수정
			Intent intent = new Intent(this, ArticleCommentEditActivity.class);
			intent.putExtra("comment", clickedComment);
			startActivityForResult(intent, Constant.COMMENT_EDIT);
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * 답글 시 이름 태그 생성삽입
	 */
	private void setReplyTarget() {
		replyTargetId = clickedComment.getId();
		commentContent.setText(null);
		// 답글
		Drawable drawable = StringUtil.getCommentNameTagDrawable(this, clickedComment.getWriterName());
		SpannableString spanString = new SpannableString(clickedComment.getWriterName());
		StringUtil.setImageSpan(spanString, drawable, 0, spanString.length());
		commentContent.getEditableText().insert(0, spanString);
		focusCommContent();
	}
}
