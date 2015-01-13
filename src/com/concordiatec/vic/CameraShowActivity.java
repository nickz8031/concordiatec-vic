package com.concordiatec.vic;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.tools.Tools;
import com.concordiatec.vic.util.NotifyUtil;

public class CameraShowActivity extends SubPageSherlockActivity {
	public final static int SURE_TO_USE = 1;
	private String photoPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		super.onCreate(savedInstanceState);
		hideStatusBar(true);
		setContentView(R.layout.activity_camera_show);
		
		ImageView mImageView = (ImageView) findViewById(R.id.iv_photo);
		photoPath = getIntent().getStringExtra("photo");
		if (photoPath != null) {
			int angel = Tools.getBitmapDegree(photoPath);
			if (angel > 0) {
				Bitmap bm = BitmapFactory.decodeFile(photoPath);
				bm = Tools.rotateBitmapByDegree(bm, angel);
				mImageView.setImageBitmap(bm);
			}
			new PhotoViewAttacher(mImageView);
		} else {
			NotifyUtil.toast(this, getString(R.string.error_with_show_image));
			finish();
		}
	}

	private void hideStatusBar(boolean isHide) {
		if (isHide) {
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			getWindow().setAttributes(lp);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			WindowManager.LayoutParams attr = getWindow().getAttributes();
			attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().setAttributes(attr);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.confirm_take_photo, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if( item.getItemId() == R.id.confirm_choose ){
			sureToUse();
		}
		return true;
	}

	private void sureToUse() {
		Intent intent = new Intent(this , ArticleWriteActivity.class);
		intent.putExtra("take_photo", photoPath);
		setResult(SURE_TO_USE , intent);
		finish();
	}
}
