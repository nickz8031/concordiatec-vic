package com.concordiatec.vic;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.concordiatec.vic.adapter.ChoosePicGridAdapter;
import com.concordiatec.vic.adapter.ChoosePicGridAdapter.OnItemClickClass;
import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.constant.Constant;
import com.concordiatec.vic.util.EncryptUtil;
import com.concordiatec.vic.util.LocalImageUtil;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.NotifyUtil;
import com.concordiatec.vic.util.TimeUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;

public class ChoosePicListActivity extends SubPageSherlockActivity {
	private GridView imgGridView;
	private ArrayList<String> selectedFiles;
	private ChoosePicGridAdapter imgsAdapter;
	private ArrayList<String> selectedPics;
	private List<String> filePathList;
	private LinearLayout takePhotoBtn;
	
	private int initSurplusCount;
	private String currentPhotoPath;
	
	@Override
	protected void onDestroy() {
		imgGridView = null;
		selectedFiles = null;
		selectedPics = null;
		imgsAdapter = null;
		filePathList = null;
		currentPhotoPath = null;
		super.onDestroy();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_pic_grid);
		
		imgGridView = (GridView) findViewById(R.id.imageListGrid);
		takePhotoBtn = (LinearLayout) findViewById(R.id.take_photo);
		initSurplusCount = Constant.SURPLUS_UPLOAD_COUNTS;
		setSelectedTitle(0);
		
		selectedPics = getIntent().getStringArrayListExtra("selected_pics");
		filePathList = new LocalImageUtil(this , selectedPics).listAlldir();
		
		imgsAdapter = new ChoosePicGridAdapter(this, filePathList, onItemClickClass);
		imgGridView.setAdapter(imgsAdapter);
		selectedFiles = new ArrayList<String>();
		
		takePhotoBtn.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
				if( isExceed() ) return;
				// start camera
				Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intentCamera.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, getPhotoUri());
	            startActivityForResult(intentCamera, REQUEST_TAKE_PHOTO);
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_TAKE_PHOTO:
				if( resultCode == Activity.RESULT_OK ){
					Intent intent = new Intent(this , CameraShowActivity.class);
					intent.putExtra("photo", this.currentPhotoPath);
					startActivityForResult(intent, REQUEST_TAKE_SURE);
				}
				break;
			case REQUEST_TAKE_SURE:
				if( resultCode == Activity.RESULT_OK ){
					Intent intent = new Intent(this , ArticleWriteActivity.class);
					intent.putExtras(data.getExtras());
					setResult(REQUEST_TAKE_SURE , intent);
					finish();
				}
				break;
			default:
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private boolean isExceed(){
		if( Constant.SURPLUS_UPLOAD_COUNTS == 0 ){
			NotifyUtil.toast(this, getString(R.string.outnumbering_allowed_count));
			return true;
		}
		return false;
	}
	
	private void setSelectedTitle( int selected ){
		setTitle( String.format( getString(R.string.format_surplus_total_count) , ""+selected , ""+ initSurplusCount) );
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.choose_pics, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if( item.getItemId() == R.id.choose_complete ){
			sendfiles();
		}
		return true;
	}
	class ImgOnclick implements OnClickListener {
		String filepath;
		CheckBox checkBox;

		public ImgOnclick(String filepath, CheckBox checkBox) {
			this.filepath = filepath;
			this.checkBox = checkBox;
		}

		@Override
		public void onClick(View arg0) {
			checkBox.setChecked(false);
			selectedFiles.remove(filepath);
		}
	}
	
	ChoosePicGridAdapter.OnItemClickClass onItemClickClass = new OnItemClickClass() {
		@Override
		public void OnItemClick(View v, int Position, CheckBox checkBox) {
			String filePath = filePathList.get(Position);
			if (checkBox.isChecked()) {
				checkBox.setChecked(false);
				selectedFiles.remove(filePath);
				setSelectedTitle( selectedFiles.size() );
			} else {
				if( selectedFiles.size() >= initSurplusCount ){
					NotifyUtil.toast(ChoosePicListActivity.this, getString(R.string.outnumbering_allowed_count));
					return;
				}
				if( fileExist(filePath) ){
					checkBox.setChecked(true);
					selectedFiles.add(filePath);
					setSelectedTitle( selectedFiles.size() );
				}
				
			}
		}
	};
	
	
	private Uri getPhotoUri(){
		String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/"+ getResources().getString(R.string.app_name) 
				+ "/"+ EncryptUtil.MD5(TimeUtil.getUnixTimestampMills()) 
				+ ".jpg" ;
		this.currentPhotoPath = imageFilePath;
		File imageFile = new File(imageFilePath); 
		return Uri.fromFile(imageFile);
	}
	
	private boolean fileExist(String path){
		File f = new File(path);
		return f.exists();
	}

	/**
	 * send data
	 * @param view
	 */
	public void sendfiles() {
		Intent intent = new Intent(this, ArticleWriteActivity.class);
		Bundle bundle = new Bundle();
		bundle.putStringArrayList("files", selectedFiles);
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		finish();
	}
}