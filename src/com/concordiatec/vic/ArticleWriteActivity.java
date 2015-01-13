package com.concordiatec.vic;

import java.io.File;
import java.util.ArrayList;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.adapter.PhotoShowGridAdapter;
import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.model.User;
import com.concordiatec.vic.service.ArticleService;
import com.concordiatec.vic.service.UserService;
import com.concordiatec.vic.tools.Route;
import com.concordiatec.vic.tools.Tools;
import com.concordiatec.vic.util.EncryptUtil;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.NotifyUtil;
import com.concordiatec.vic.util.ProgressUtil;
import com.concordiatec.vic.util.TimeUtil;
import com.concordiatec.vic.widget.CircleImageView;

public class ArticleWriteActivity extends SubPageSherlockActivity{
	private final static int REQUEST_TAKE_PHOTO = 1;
	private final static int REQUEST_CHOOSE_PHOTO = 2;
	private final static int REQUEST_TAKE_SURE = 3;
	
	private ArrayList<File> picsList;
	
	private TextView articleStore;
	private View takePhotoBtn;
	private View choosePhotoBtn;
	private GridView attachList;
	private EditText writeContent;
	private CheckBox commentOff;
	private CheckBox shareOff;
	
	private String currentPhotoPath;
	private PhotoShowGridAdapter adapter;
	private User loginUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		loginUser = new UserService(this).getLoginUser();
		if( loginUser == null ){
			Route.moveTo(this, LoginActivity.class);
			return;
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artile_write);
		setTitle(getString(R.string.write_articles));
		
		picsList = new ArrayList<File>();
		attachList = (GridView) findViewById(R.id.attach_images_scroll);
		writeContent = (EditText) findViewById(R.id.write_content);
		articleStore = (TextView) findViewById(R.id.article_store);
		
		CircleImageView profilePhoto = (CircleImageView) findViewById(R.id.profile_photo);
		Glide.with(this).load(loginUser.photo).into(profilePhoto);
		TextView userName = (TextView) findViewById(R.id.profile_name);
		userName.setText(loginUser.name);
		if( loginUser.isShop ){
			articleStore.setText(loginUser.name);
			articleStore.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.demo_store_type), null, null, null);
		}
		
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
				ArrayList<String> selectedFiles = new ArrayList<String>();
				for( File f : picsList ){
					selectedFiles.add( f.getAbsolutePath() );
				}
				intent.putStringArrayListExtra("selected_pics", selectedFiles);
				startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
				
			}
		});
		adapter = new PhotoShowGridAdapter(this);
		attachList.setAdapter( adapter );
		
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
	        					File f = new File(filePath);
	        					if( f.exists() ){
	        						picsList.add(f);
	        						adapter.addData(filePath);
	        					}
	        				}
	        				
	        				if( picsList.size() > attachList.getNumColumns() ){
    							LayoutParams params = (LayoutParams) attachList.getLayoutParams();
    							params.height = Tools.getIntValue( (
    									getResources().getDimension(R.dimen.ar_w_choosed_photo_size) +
    									getResources().getDimension(R.dimen.ar_w_choosed_photo_grid_horspace)
    									) * 2 );
    							attachList.setLayoutParams(params);
    						}
	        			}
	                } catch (Exception e) { 
	                    e.printStackTrace();
	                } 
	            }
			break;
		case REQUEST_TAKE_PHOTO:
			Intent intent = new Intent(this , CameraShowActivity.class);
			intent.putExtra("photo", this.currentPhotoPath);
			startActivityForResult(intent, REQUEST_TAKE_SURE);			
			break;
			
		case REQUEST_TAKE_SURE:
			if( getIntent().hasExtra("take_photo") ){
				String pPath = getIntent().getStringExtra("take_photo");
				File f = new File(pPath);
				if( f.exists() ){
					picsList.add(f);
					adapter.addData(pPath);
				}
			}
			break;
		default:
			break;
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
			ProgressUtil.show(this);
			String content = writeContent.getText().toString();
			int shopId = 2;
			boolean isAllowComment = !commentOff.isChecked();			
			
			ArticleService articleService = new ArticleService(this);

			Article article = new Article();
			article.setShopId(2);
			article.setContent( content );
			article.setIsAllowComment(isAllowComment);
			
			articleService.writeArticle(article, picsList, new VicResponseListener() {
				@Override
				public void onSuccess(ResData data) {
					ProgressUtil.dismiss();
					NotifyUtil.toast(ArticleWriteActivity.this, getString(R.string.write_article_succed));
					finish();
				}
				@Override
				public void onFailure(int httpResponseCode, String ErrorMessage) {
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
