package com.concordiatec.vic.util;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;

//文件的类
@SuppressLint("ParcelCreator")
public class FileTraversal implements Parcelable {
	public String fileName;//所属图片的文件名称
	public List<String> fileContent=new ArrayList<String>();
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(fileName);
		dest.writeList(fileContent);
	}
	
	public static final Parcelable.Creator<FileTraversal> CREATOR=new Creator<FileTraversal>() {
		
		@Override
		public FileTraversal[] newArray(int size) {
			return null;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public FileTraversal createFromParcel(Parcel source) {
			FileTraversal ft=new FileTraversal();
			ft.fileName= source.readString();
			ft.fileContent= source.readArrayList(FileTraversal.class.getClassLoader());
			
			return ft;
		}
		
		
	};
}