package com.concordiatec.vilnet.util;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBUtil extends SQLiteOpenHelper {
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "vic_db";
	
	public DBUtil(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	public DBUtil(Context context, CursorFactory factory, DatabaseErrorHandler errorHandler) {
		super(context, DB_NAME, factory, DB_VERSION, errorHandler);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
}
