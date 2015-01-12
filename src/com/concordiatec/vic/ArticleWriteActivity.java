package com.concordiatec.vic;

import java.io.File;
import java.util.ArrayList;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.filter.ArticleContentFilter;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.service.ArticleService;
import com.concordiatec.vic.tools.Tools;
import com.concordiatec.vic.util.EncryptUtil;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.NotifyUtil;
import com.concordiatec.vic.util.ProgressUtil;
import com.concordiatec.vic.util.TimeUtil;

public class ArticleWriteActivity extends SubPageSherlockActivity{
	private final static int REQUEST_TAKE_PHOTO = 1;
	private final static int REQUEST_CHOOSE_PHOTO = 2;
	
	private ArrayList<String> picsList;
	
	private View takePhotoBtn;
	private View choosePhotoBtn;
	private LinearLayout attachLayout;
	private EditText writeContent;
	private CheckBox commentOff;
	private CheckBox shareOff;
	
	private String currentPhotoPath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artile_write);

		setTitle(getString(R.string.write_articles));
		
		attachLayout = (LinearLayout) findViewById(R.id.attach_images_scroll_layout);
		
		writeContent = (EditText) findViewById(R.id.write_content);
		picsList = new ArrayList<String>();
		takePhotoBtn = findViewById(R.id.take_photo);
		choosePhotoBtn = findViewById(R.id.choose_photo);
		commentOff = (CheckBox) findViewById(R.id.article_comment_off);
		shareOff = (CheckBox) findViewById(R.id.article_share_off);
		
		takePhotoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// start camera
				Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intentCamera.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, getPhotoUri());
                startActivityForResult(intentCamera, REQUEST_TAKE_PHOTO);
			}
		});
		
		choosePhotoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// show gallery
				Intent intent = new Intent(ArticleWriteActivity.this , ChoosePicActivity.class);
				intent.putStringArrayListExtra("selected_pics", picsList);
				startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
				
			}
		});
		
	}
	
	private Uri getPhotoUri(){
		String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/"+ getResources().getString(R.string.app_name) 
				+ "/"+ EncryptUtil.MD5(TimeUtil.getUnixTimestampMills()) 
				+ ".jpg" ;
		this.currentPhotoPath = imageFilePath;
		File imageFile = new File(imageFilePath); 
		return Uri.fromFile(imageFile);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CHOOSE_PHOTO:
				if (resultCode == RESULT_OK) { 
	                try { 
	                	Bundle bundle = data.getExtras();
	                	if (bundle.getStringArrayList("files")!=null) {
	                		ArrayList<String> uriList= bundle.getStringArrayList("files");
	        				for( String filePath : uriList ){
	        					picsList.add(filePath);
	        					ImageView iView = new ImageView(this);
	        					Glide.with(this).load(filePath).into(iView);
	        	                iView.setLayoutParams( getLP() );
	        	                iView.setScaleType(ScaleType.CENTER_CROP);
	    	                    attachLayout.addView(iView);
	        				}
	        			}
	                } catch (Exception e) { 
	                    e.printStackTrace();
	                } 
	            }
			break;
		case REQUEST_TAKE_PHOTO:
				File f = new File(this.currentPhotoPath);
				if( f.exists() ){
					f = null;
					picsList.add(this.currentPhotoPath);
					ImageView iView = new ImageView(this);
					Glide.with(this).load(this.currentPhotoPath).into(iView);
		            iView.setLayoutParams( getLP() );
		            iView.setScaleType(ScaleType.CENTER_CROP);
		            attachLayout.addView(iView);
				}
			break;
		default:
			break;
		}
        super.onActivityResult(requestCode, resultCode, data);
    }
	
	private LinearLayout.LayoutParams getLP(){
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, 30 , 0);
		return params;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.write_article, menu);
		return true;
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if( item.getItemId() == R.id.send_article_btn ){
			ProgressUtil.show(this);
			String content = writeContent.getText().toString();
			int shopId = 2;
			boolean isAllowComment = !commentOff.isChecked();			
			
			ArticleService articleService = new ArticleService(this);

			Article article = new Article();
			article.setShopId(2);
			article.setContent( content );
			article.setIsAllowComment(isAllowComment);
			
			//simple filter
			article = ArticleContentFilter.single(this).filterWrite(article, picsList);
			
			articleService.writeArticle(article, picsList, new VicResponseListener() {
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
				}
				@Override
				public void onError(ResData data) {
					ProgressUtil.dismiss();
					NotifyUtil.toast(ArticleWriteActivity.this, data.getMsg());
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
