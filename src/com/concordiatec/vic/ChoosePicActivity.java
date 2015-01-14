package com.concordiatec.vic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.concordiatec.vic.adapter.ChoosePicFolderListAdapter;
import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.util.FileTraversal;
import com.concordiatec.vic.util.LocalImageUtil;

public class ChoosePicActivity extends SubPageSherlockActivity implements OnItemClickListener {
	private ListView listView;
	private LocalImageUtil util;
	private List<FileTraversal> localList;
	private ChoosePicFolderListAdapter listAdapter;
	private ArrayList<String> selectedPics;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_pic);
		setTitle(getString(R.string.choose_pic));
		
		selectedPics = getIntent().getStringArrayListExtra("selected_pics");
		
		listView = (ListView) findViewById(R.id.folder_lsit);
		util = new LocalImageUtil(this , selectedPics);
		localList = util.LocalImgFileList();
		List<HashMap<String, String>> listdata = new ArrayList<HashMap<String, String>>();
		if (localList != null) {
			for (int i = 0; i < localList.size(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("filecount", localList.get(i).fileContent.size() + getString(R.string.unit_zang));
				map.put("imgpath", localList.get(i).fileContent.get(0) == null ? null : (localList.get(i).fileContent.get(0)));
				map.put("filename", localList.get(i).fileName);
				listdata.add(map);
			}
		}
		listAdapter = new ChoosePicFolderListAdapter(this, listdata);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this, ChoosePicListActivity.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable("data", localList.get(position));
		intent.putExtras(bundle);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivityForResult(intent , 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( requestCode == 0 && resultCode == RESULT_OK ){
			Intent intent = new Intent(this, ArticleWriteActivity.class);
			intent.putExtras(data.getExtras());
			setResult(RESULT_OK, intent);
			finish();
		}
	}
}
