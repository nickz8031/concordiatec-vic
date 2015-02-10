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
import com.concordiatec.vilnet.listener.SimpleVicResponseListener;
import com.concordiatec.vilnet.model.Article;
import com.concordiatec.vilnet.model.Comment;
import com.concordiatec.vilnet.model.ResData;
import com.concordiatec.vilnet.service.ArticleService;
import com.concordiatec.vilnet.service.CommentService;
import com.concordiatec.vilnet.util.NotifyUtil;
import com.concordiatec.vilnet.util.ProgressUtil;
import com.concordiatec.vilnet.util.StringUtil;

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
							NotifyUtil.toast(ArticleEditActivity.this, getString(R.string.edit_succed));
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
