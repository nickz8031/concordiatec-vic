package com.concordiatec.vic;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.adapter.PhotoShowGridAdapter;
import com.concordiatec.vic.adapter.PhotoShowGridAdapter.OnDataChangedListener;
import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.constant.Constant;
import com.concordiatec.vic.listener.SimpleVicResponseListener;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.model.User;
import com.concordiatec.vic.service.ArticleService;
import com.concordiatec.vic.service.UserService;
import com.concordiatec.vic.tools.Route;
import com.concordiatec.vic.tools.Tools;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.NotifyUtil;
import com.concordiatec.vic.util.ProgressUtil;
import com.concordiatec.vic.widget.CircleImageView;

public class ArticleWriteActivity extends SubPageSherlockActivity{
	
	private ArrayList<File> picsList;
	
	private TextView articleStore;
	private TextView articleStoreAddr;
	private ImageView articleStoreType;
	private CircleImageView articleStoreImg;
	private View choosePhotoBtn;
	private GridView attachList;
	private EditText writeContent;
	private CheckBox commentOff;
	private CircleImageView profilePhoto;
	
	private PhotoShowGridAdapter adapter;
	private User loginUser;
	private TextView userName;
	private int photoRowCount = 0;
	
	@Override
	protected void onDestroy() {
		picsList.clear();
		picsList = null;
		articleStore = null;
		articleStoreAddr = null;
		articleStoreType = null;
		articleStoreImg=null;
		choosePhotoBtn = null;
		userName = null;
		attachList = null;
		writeContent = null;
		commentOff = null;
		adapter = null;
		loginUser = null;
		profilePhoto = null;
		setContentView(R.layout.null_layout);
		super.onDestroy();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		loginUser = new UserService(this).getLoginUser();
		if( loginUser == null ){
			Route.moveTo(this, LoginActivity.class);
			return;
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_write);
		setTitle(getString(R.string.write_articles));
		Constant.initSurplus();
		
		picsList = new ArrayList<File>();
		attachList = (GridView) findViewById(R.id.attach_images_scroll);
		writeContent = (EditText) findViewById(R.id.write_content);
		articleStore = (TextView) findViewById(R.id.article_store);
		articleStoreAddr = (TextView) findViewById(R.id.article_store_addr);
		articleStoreType = (ImageView) findViewById(R.id.article_store_type_img);
		articleStoreImg = (CircleImageView) findViewById(R.id.article_store_img);
		profilePhoto = (CircleImageView) findViewById(R.id.profile_photo);
		userName = (TextView) findViewById(R.id.profile_name);
		
		userName.setText(loginUser.name);
		Glide.with(this).load(loginUser.photo).into(profilePhoto);
		
		if( loginUser.isShop ){
			articleStore.setText(loginUser.name);
			Glide.with(this).load(loginUser.photo).into(articleStoreImg);
			articleStoreType.setImageResource(R.drawable.demo_store_type);
			articleStoreType.setVisibility(View.VISIBLE);
		
			articleStoreAddr.setText(loginUser.shopAddr1 + loginUser.shopAddr2);
			articleStoreAddr.setVisibility(View.VISIBLE);
		}
		choosePhotoBtn = findViewById(R.id.choose_photo);
		commentOff = (CheckBox) findViewById(R.id.article_comment_off);
		choosePhotoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if( isExceed() ) return;
				// show gallery
				Intent intent = new Intent(ArticleWriteActivity.this , ChoosePicListActivity.class);
				ArrayList<String> selectedFiles;
				if( adapter != null ){
					selectedFiles = adapter.getAllData();
				}else{
					selectedFiles = new ArrayList<String>();
				}
				intent.putStringArrayListExtra("selected_pics", selectedFiles);
				startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
				
			}
		});
		
		adapter = new PhotoShowGridAdapter(this , new OnDataChangedListener() {
			@Override
			public void onDataChange() {
				int rowInfo[] = new int[2];
				rowInfo[0] = adapter.getCount() % attachList.getNumColumns();
				rowInfo[1] = adapter.getCount() / attachList.getNumColumns();
				if( attachList.getTag() != null ){
					int[] ro = (int[]) attachList.getTag();
					if( Arrays.equals(ro, rowInfo) ){
						return;
					}
				}
				attachList.setTag(rowInfo);
				int s = rowInfo[1];
				
				if( rowInfo[0] > 0 ){
					s++;
				}
				
				photoRowCount = s;
				
				LayoutParams params = (LayoutParams) attachList.getLayoutParams();
				params.height = Tools.getIntValue( (
						getResources().getDimension(R.dimen.ar_w_choosed_photo_size) +
						getResources().getDimension(R.dimen.ar_w_choosed_photo_grid_horspace)
						) * s );
				attachList.setLayoutParams(params);
			}
		});
		attachList.setAdapter( adapter );
		
	}
		
	private boolean isExceed(){
		if( Constant.SURPLUS_UPLOAD_COUNTS == 0 ){
			NotifyUtil.toast(this, getString(R.string.outnumbering_allowed_count));
			return true;
		}
		return false;
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( requestCode == REQUEST_CHOOSE_PHOTO ){
			if (resultCode == RESULT_OK) { 
		        try { 
		        	Bundle bundle = data.getExtras();
		        	if (bundle.getStringArrayList("files")!=null) {
		        		ArrayList<String> uriList= bundle.getStringArrayList("files");
						for( String filePath : uriList ){
							File f = new File(filePath);
							if( f.exists() ){
								picsList.add(f);
								adapter.addData(filePath);
							}
						}
					}
		        } catch (Exception e) { 
		            e.printStackTrace();
		        } 
		    }else if( resultCode == REQUEST_TAKE_SURE ){
		    	String pPath = data.getStringExtra("take_photo");
				LogUtil.show(pPath);
				File f = new File(pPath);
				if( f.exists() ){
					picsList.add(f);
					adapter.addData(pPath);
				}
		    }
		}		
        super.onActivityResult(requestCode, resultCode, data);
    }	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.write_article, menu);
		return true;
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if( item.getItemId() == R.id.send_article_btn ){
			String content = writeContent.getText().toString();
			if( content.length() == 0 ){
				NotifyUtil.toast(this, getString(R.string.content_cannot_be_null));
				return true;
			}
			
			ProgressUtil.show(this);	
			int shopId = 2;
			boolean isAllowComment = !commentOff.isChecked();			
			
			ArticleService articleService = new ArticleService(this);

			Article article = new Article();
			article.setShopId(shopId);
			article.setContent( content );
			article.setIsAllowComment(isAllowComment);
			
			articleService.writeArticle(article, picsList, new SimpleVicResponseListener(){
				@Override
				public void onSuccess(ResData data) {
					ProgressUtil.dismiss();
					NotifyUtil.toast(ArticleWriteActivity.this, getString(R.string.write_article_succed));
					finish();
				}
				@Override
				public void onFailure(int httpResponseCode, String responseBody) {
					ProgressUtil.dismiss();
					NotifyUtil.toast(ArticleWriteActivity.this, getString(R.string.write_article_failed));
					LogUtil.show("Status : "+ httpResponseCode);
					LogUtil.show("Response Body : "+ responseBody);
				}
				@Override
				public void onProgress(int written, int totalSize) {
					double percent = (totalSize > 0) ? (written * 1.0 / totalSize) * 100 : -1;
					ProgressUtil.setText( Tools.getIntValue(percent) + " %" );
				}
				
			});
		}
		return true;
	}
}
