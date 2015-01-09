package com.concordiatec.vic;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.actionbarsherlock.view.Menu;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.util.LogUtil;

@SuppressWarnings("deprecation")
public class ArticleWriteActivity extends SubPageSherlockActivity implements SurfaceHolder.Callback {
	private final static int REQUEST_TAKE_PHOTO = 1;
	private final static int REQUEST_CHOOSE_PHOTO = 2;
	private Camera mCamera;
	private SurfaceView mPreview;
	private View takePhotoBtn;
	private View choosePhotoBtn;
	private LinearLayout attachLayout;
	private EditText writeContent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_artile_write);

		setTitle(getString(R.string.write_articles));
		
		mPreview = (SurfaceView) findViewById(R.id.photo_surface);
		mPreview.getHolder().addCallback(this);
		mPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mCamera = Camera.open();
		
		attachLayout = (LinearLayout) findViewById(R.id.attach_images_scroll_layout);
		
		writeContent = (EditText) findViewById(R.id.write_content);
		
		takePhotoBtn = findViewById(R.id.take_photo);
		choosePhotoBtn = findViewById(R.id.choose_photo);
		
		takePhotoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// start camera
				Intent intentCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intentCamera, REQUEST_TAKE_PHOTO);
			}
		});
		
		choosePhotoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// show gallery
				Intent intent = new Intent(ArticleWriteActivity.this , ChoosePicActivity.class);
				startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
				
			}
		});
		
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CHOOSE_PHOTO:
				if (resultCode == RESULT_OK) { 
	                try { 
	                	Bundle bundle = data.getExtras();
	                	if (bundle.getStringArrayList("files")!=null) {
	                		//LogUtil.show(writeContent.get);
	                		ArrayList<String> uriList= bundle.getStringArrayList("files");
	        				for( String filePath : uriList ){
	        					ImageView iView = new ImageView(this);
	        					Glide.with(this).load(filePath).into(iView);
	    	                    attachLayout.addView(iView);
	        				}
	        			}
	                } catch (Exception e) { 
	                    e.printStackTrace();
	                } 
	            }
			break;
		case REQUEST_TAKE_PHOTO:
				if(data != null){
	                Bundle extras = data.getExtras();
	                Bitmap bitmap = (Bitmap) extras.get("data");
	                ImageView iView = new ImageView(this);
	                iView.setImageBitmap(bitmap);
	                attachLayout.addView(iView);
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
	public void onPause() {
		super.onPause();
		mCamera.stopPreview();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mCamera.release();
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera.setPreviewDisplay(mPreview.getHolder());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		Camera.Parameters params = mCamera.getParameters();
		List<Camera.Size> sizes = params.getSupportedPreviewSizes();
		Camera.Size selected = sizes.get(0);
		params.setPreviewSize(selected.width, selected.height);
		mCamera.setParameters(params);
		mCamera.setDisplayOrientation(90);
		mCamera.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {}
}
