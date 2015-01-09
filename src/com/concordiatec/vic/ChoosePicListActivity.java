package com.concordiatec.vic;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.concordiatec.vic.adapter.ChooseImageAdapter;
import com.concordiatec.vic.adapter.ChooseImageAdapter.OnItemClickClass;
import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.inf.ChooseImageCallback;
import com.concordiatec.vic.util.FileTraversal;
import com.concordiatec.vic.util.LocalImageUtil;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class ChoosePicListActivity extends SubPageSherlockActivity {
	private Bundle bundle;
	private FileTraversal fileTraversal;
	private GridView imgGridView;
	private LinearLayout selectedLayout;
	private LocalImageUtil util;
	private RelativeLayout selectedGroup;
	private HashMap<Integer, ImageView> hashImage;
	private ArrayList<String> fileList;
	private ChooseImageAdapter imgsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_choose_pic_list);
		imgGridView = (GridView) findViewById(R.id.imageListGrid);
		bundle = getIntent().getExtras();
		fileTraversal = bundle.getParcelable("data");
		imgsAdapter = new ChooseImageAdapter(this, fileTraversal.fileContent, onItemClickClass);
		imgGridView.setAdapter(imgsAdapter);
		selectedLayout = (LinearLayout) findViewById(R.id.selected_image_layout);
		selectedGroup = (RelativeLayout) findViewById(R.id.selectedLayout);
		hashImage = new HashMap<Integer, ImageView>();
		fileList = new ArrayList<String>();
		util = new LocalImageUtil(this);
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

	class BottomImgIcon implements OnItemClickListener {
		int index;

		public BottomImgIcon(int index) {
			this.index = index;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		}
	}

	@SuppressLint("NewApi")
	public ImageView iconImage(String filepath, int index, CheckBox checkBox) throws FileNotFoundException {
		LinearLayout.LayoutParams params = new LayoutParams(selectedGroup.getMeasuredHeight() - 10, selectedGroup.getMeasuredHeight() - 10);
		ImageView imageView = new ImageView(this);
		imageView.setLayoutParams(params);
		float alpha = 100;
		imageView.setAlpha(alpha);
		util.imgExcute(imageView, imgCallBack, filepath);
		imageView.setOnClickListener(new ImgOnclick(filepath, checkBox));
		return imageView;
	}
	ChooseImageCallback imgCallBack = new ChooseImageCallback() {
		@Override
		public void resultImgCall(ImageView imageView, Bitmap bitmap) {
			imageView.setImageBitmap(bitmap);
		}
	};

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
			selectedLayout.removeView(arg0);
			fileList.remove(filepath);
		}
	}
	ChooseImageAdapter.OnItemClickClass onItemClickClass = new OnItemClickClass() {
		@Override
		public void OnItemClick(View v, int Position, CheckBox checkBox) {
			String filapath = fileTraversal.fileContent.get(Position);
			if (checkBox.isChecked()) {
				checkBox.setChecked(false);
				selectedLayout.removeView(hashImage.get(Position));
				fileList.remove(filapath);
			} else {
				try {
					checkBox.setChecked(true);
					ImageView imageView = iconImage(filapath, Position, checkBox);
					if (imageView != null) {
						hashImage.put(Position, imageView);
						fileList.add(filapath);
						selectedLayout.addView(imageView);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	};

	public void tobreak(View view) {
		finish();
	}

	/**
	 * FIXME 亲只需要在这个方法把选中的文档目录已list的形式传过去即可
	 * 
	 * @param view
	 */
	public void sendfiles() {
		Intent intent = new Intent(this, ChoosePicActivity.class);
		Bundle bundle = new Bundle();
		bundle.putStringArrayList("files", fileList);
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		finish();
	}
}