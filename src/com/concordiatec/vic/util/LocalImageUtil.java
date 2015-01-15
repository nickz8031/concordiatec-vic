package com.concordiatec.vic.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class LocalImageUtil {
	private Context context;
	private List<String> exceptFiles;

	public LocalImageUtil(Context context) {
		this.context = context;
	}
	
	public LocalImageUtil(Context context , List<String> exceptFiles) {
		this.exceptFiles = exceptFiles;
		this.context = context;
	}

	/**
	 * 获取全部图片地址
	 * 
	 * @return
	 */
	public ArrayList<String> listAlldir() {
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		Uri uri = intent.getData();
		ArrayList<String> list = new ArrayList<String>();
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
		while (cursor.moveToNext()) {
			String path = new File( cursor.getString(0) ).getAbsolutePath();
			if( exceptFiles.contains(path) ) continue;
			list.add( path );
		}
		return list;
	}	

	public String getfileinfo(String data) {
		String filename[] = data.split("/");
		if (filename != null) { return filename[filename.length - 2]; }
		return null;
	}
}
