package com.concordiatec.vilnet;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import com.concordiatec.vilnet.R;
import com.concordiatec.vilnet.base.SubPageSherlockActivity;
import com.concordiatec.vilnet.constant.Constant;
import com.concordiatec.vilnet.listener.SimpleVicResponseListener;
import com.concordiatec.vilnet.model.Comment;
import com.concordiatec.vilnet.model.ResData;
import com.concordiatec.vilnet.service.CommentService;
import com.concordiatec.vilnet.util.NotifyUtil;
import com.concordiatec.vilnet.util.ProgressUtil;
import com.concordiatec.vilnet.util.StringUtil;

public class ArticleCommentEditActivity extends SubPageSherlockActivity {
	private Comment comment;
	private EditText contentEdit;
	private ImageView sendCommentButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView( R.layout.activity_article_comment_edit );
		if( getIntent().hasExtra("comment")){
			comment = (Comment) getIntent().getSerializableExtra("comment");
			contentEdit = (EditText) findViewById(R.id.e_comment_input); 
			sendCommentButton = (ImageView) findViewById(R.id.e_send_comment);
			contentEdit.setText( comment.getContent().trim() );
			if( comment.getReplyWhose() > 0 ){
				Drawable drawable = StringUtil.getCommentNameTagDrawable(this, comment.getReplyWhoseName());
				SpannableString spanString = new SpannableString(comment.getReplyWhoseName());
				StringUtil.setImageSpan( spanString , drawable, 0, spanString.length());
				contentEdit.getEditableText().insert(0, spanString);
			}
			contentEdit.setSelection(contentEdit.length());
			
			sendCommentButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String writedContent = contentEdit.getText().toString().trim();
					int replyTargetId = getCommentReplyId();
					if( replyTargetId > 0 ) writedContent = writedContent.substring(comment.getWriterName().length()).trim();
					
					if( writedContent.length() == 0 ){
						NotifyUtil.toast(ArticleCommentEditActivity.this, getString(R.string.comment_cannot_be_null));
						return;
					}else if( writedContent.equals(comment.getContent()) ){
						NotifyUtil.toast(ArticleCommentEditActivity.this, getString(R.string.content_not_changed));
						return;
					}
					ProgressUtil.show(ArticleCommentEditActivity.this);
					if( replyTargetId > 0 ) comment.setReplyId( replyTargetId );
					comment.setWriterId( comment.getWriterId() );
					comment.setContent( writedContent );
					
					new CommentService(ArticleCommentEditActivity.this).editComment(comment, new SimpleVicResponseListener(){
						@Override
						public void onSuccess(ResData data) {
							NotifyUtil.toast(ArticleCommentEditActivity.this, getString(R.string.comment_edit_succed));
							Intent d = new Intent();
							d.putExtra("edit_comment", comment);
							setResult(Constant.COMMENT_EDIT_SUCCED, d);
							finish();
						}
					});
				}
			});
			
		}else{
			NotifyUtil.toast(this, getString(R.string.no_data));
		}
		
	}
	
	private int getCommentReplyId(){
		if( comment.getReplyId() > 0 ){
			String content = contentEdit.getText().toString().trim();
			if( content.length() > 0 ){
				String eq = content.substring( 0 , comment.getReplyWhoseName().length() );
				if( eq.equals( comment.getReplyWhoseName() ) ){
					return comment.getReplyId();
				}
			}
		}
		return 0;
		
	}
}
