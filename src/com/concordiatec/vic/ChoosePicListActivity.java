package com.concordiatec.vic;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.concordiatec.vic.adapter.ChoosePicGridAdapter;
import com.concordiatec.vic.adapter.ChoosePicGridAdapter.OnItemClickClass;
import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.constant.Constant;
import com.concordiatec.vic.util.FileTraversal;
import com.concordiatec.vic.util.NotifyUtil;
import java.io.File;
import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.GridView;

public class ChoosePicListActivity extends SubPageSherlockActivity {
	private Bundle bundle;
	private FileTraversal fileTraversal;
	private GridView imgGridView;
	private ArrayList<String> fileList;
	private ChoosePicGridAdapter imgsAdapter;
	
	private int initSurplusCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_pic_grid);
		
		imgGridView = (GridView) findViewById(R.id.imageListGrid);
		bundle = getIntent().getExtras();
		fileTraversal = bundle.getParcelable("data");
		initSurplusCount = Constant.SURPLUS_UPLOAD_COUNTS;
		setSelectedTitle(0);
		imgsAdapter = new ChoosePicGridAdapter(this, fileTraversal.fileContent, onItemClickClass);
		imgGridView.setAdapter(imgsAdapter);
		fileList = new ArrayList<String>();
	}
	private void setSelectedTitle( int selected ){
		int total = ( fileTraversal.fileContent != null ? fileTraversal.fileContent.size() : 0 );
		if( total > initSurplusCount ){
			total = initSurplusCount;
		}
		setTitle( String.format( getString(R.string.format_surplus_total_count) , ""+selected , ""+total) );
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
			fileList.remove(filepath);
		}
	}
	
	ChoosePicGridAdapter.OnItemClickClass onItemClickClass = new OnItemClickClass() {
		@Override
		public void OnItemClick(View v, int Position, CheckBox checkBox) {
			String filePath = fileTraversal.fileContent.get(Position);
			if (checkBox.isChecked()) {
				checkBox.setChecked(false);
				fileList.remove(filePath);
			} else {
				if( fileList.size() >= initSurplusCount ){
					NotifyUtil.toast(ChoosePicListActivity.this, getString(R.string.outnumbering_allowed_count));
					return;
				}
				if( fileExist(filePath) ){
					checkBox.setChecked(true);
					fileList.add(filePath);
					setSelectedTitle( fileList.size() );
				}
				
			}
		}
	};
	
	private boolean fileExist(String path){
		File f = new File(path);
		return f.exists();
	}

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