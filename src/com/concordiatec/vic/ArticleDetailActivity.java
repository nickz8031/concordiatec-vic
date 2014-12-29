package com.concordiatec.vic;

import java.util.ArrayList;
import java.util.List;
import com.concordiatec.vic.adapter.ArticleDetailCommentAdapter;
import com.concordiatec.vic.base.BaseSherlockActivity;
import com.concordiatec.vic.model.Comment;
import android.R.id;
import android.os.Bundle;
import android.widget.ListView;

public class ArticleDetailActivity extends BaseSherlockActivity {
	private List<Comment> comments;
	private ArticleDetailCommentAdapter adapter;
	private ListView commentList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_detail);
		
		commentList = (ListView)findViewById(R.id.ar_d_comment_list);
		initComments();
	}
	
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
