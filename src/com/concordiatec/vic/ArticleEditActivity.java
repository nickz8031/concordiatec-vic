package com.concordiatec.vic;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.listener.SimpleVicResponseListener;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.model.Comment;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.service.ArticleService;
import com.concordiatec.vic.service.CommentService;
import com.concordiatec.vic.util.NotifyUtil;
import com.concordiatec.vic.util.ProgressUtil;
import com.concordiatec.vic.util.StringUtil;

public class ArticleEditActivity extends SubPageSherlockActivity {
	private Article article;
	private EditText contentEdit;
	private ImageView sendButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView( R.layout.activity_article_comment_edit );
		if( getIntent().hasExtra("article")){
			article = (Article) getIntent().getSerializableExtra("article");
			contentEdit = (EditText) findViewById(R.id.e_comment_input); 
			sendButton = (ImageView) findViewById(R.id.e_send_comment);
			contentEdit.setText( article.getContent().trim() );
			contentEdit.setSelection(contentEdit.length());
			sendButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String writedContent = contentEdit.getText().toString().trim();
					if( writedContent.length() == 0 ){
						NotifyUtil.toast(ArticleEditActivity.this, getString(R.string.comment_cannot_be_null));
						return;
					}else if( writedContent.equals(article.getContent()) ){
						NotifyUtil.toast(ArticleEditActivity.this, getString(R.string.content_not_changed));
						return;
					}
					article.setContent( writedContent );
					
					ProgressUtil.show(ArticleEditActivity.this);
					new ArticleService(ArticleEditActivity.this).editArticle(article, new SimpleVicResponseListener(){
						@Override
						public void onSuccess(ResData data) {
							NotifyUtil.toast(ArticleEditActivity.this, getString(R.string.comment_edit_succed));
							setResult(RESULT_OK, new Intent().putExtra("edit_article", article) );
							finish();
						}
					});
				}
			});
			
		}else{
			NotifyUtil.toast(this, getString(R.string.no_data));
		}
		
	}
	
}
