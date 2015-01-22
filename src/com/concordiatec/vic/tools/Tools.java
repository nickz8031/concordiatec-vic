package com.concordiatec.vic.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.concordiatec.vic.R;
import com.concordiatec.vic.util.EncryptUtil;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.StringUtil;
import com.concordiatec.vic.util.TimeUtil;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.webkit.MimeTypeMap;

public class Tools {
	/**
	 * get int value from object
	 * 
	 * @param object
	 * @return
	 */
	public static int getIntValue(Object object) {
		return Double.valueOf(object.toString()).intValue();
	}

	/**
	 * from float type
	 * 
	 * @param value
	 * @return
	 */
	public static int getIntValue(float value) {
		return Float.valueOf(value).intValue();
	}

	/**
	 * get double value from object
	 * 
	 * @param object
	 * @return
	 */
	public static double getDoubleValue(Object object) {
		return Double.valueOf(object.toString());
	}
	
	public static void delayExcute(Runnable runnable , int millis){
		new Handler(){}.postDelayed(runnable, millis);
	}
	
	
	public static Uri getCreatePhotoUri(Context context){
		String imageFilePath = createPhotoPath(context , null , "jpg");
		return Uri.fromFile(new File(imageFilePath));
	}
	
	public static String createPhotoPath(Context context , String folderName , String extName){
		String path = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/"+ context.getResources().getString(R.string.app_name);
		if( !StringUtil.isEmpty(folderName) ){
			path += "/" + folderName;
		}
		path += "/"+ EncryptUtil.MD5(TimeUtil.getUnixTimestampMills())+"."+extName;
		return path;
	}
	
	/**
	 * get display metrics
	 * 
	 * @param activity
	 * @param adjust
	 * @return
	 */
	public static DisplayMetrics getDisplayMetrics(Activity activity, float adjust) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		dm.widthPixels = (int) (dm.widthPixels - adjust);
		return dm;
	}

	public static DisplayMetrics getDisplayMetrics(Activity activity) {
		return getDisplayMetrics(activity, 0);
	}

	/**
	 * get max width when content has more than one image
	 * 
	 * @return
	 */
	public static int getMultiImgMaxW(int displayWidth) {
		return (int) (displayWidth * 0.8);
	}

	public static int getMinHeight(Context c) {
		return (int) c.getResources().getDimension(R.dimen.detail_image_min_height);
	}

	
	/**
	 * save bitmap to appointed path
	 * @param bitmap
	 * @param filePath
	 * @return
	 */
	public static boolean saveCompressBitmap( String filePath ){
		return saveCompressBitmap(null, filePath);
	}
	public static boolean saveCompressBitmap(Bitmap bitmap , String filePath){
		if( bitmap == null ){
			bitmap = BitmapFactory.decodeFile(filePath);
		}
		try {
			FileOutputStream fos = new FileOutputStream( filePath );
			String mime = getMimeType(filePath);
			if( mime == "image/jpeg" ){
				bitmap.compress(Bitmap.CompressFormat.JPEG, 30, fos);
			}else if( mime == "image/png" ){
				bitmap.compress(Bitmap.CompressFormat.PNG, 30, fos);
			}else{
				bitmap.recycle();
				fos.flush();
				fos.close();
				return false;
			}
			fos.flush();
			fos.close();
			bitmap.recycle();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	

	/**
	 * get sample size
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	// 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
	public static Bitmap createScaleBitmap(Bitmap src, int dstWidth, int dstHeight) {
		Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
		if (src != dst) { // 如果没有缩放，那么不回收
			src.recycle(); // 释放Bitmap的native像素数组
		}
		return dst;
	}

	/**
	 * create from thumb
	 * 
	 * @param res
	 *            android.content.Resource
	 * @param resId
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options); // 读取图片长款
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight); // 计算inSampleSize
		options.inJustDecodeBounds = false;
		Bitmap src = BitmapFactory.decodeResource(res, resId, options); // 载入一个稍大的缩略图
		return createScaleBitmap(src, reqWidth, reqHeight); // 进一步得到目标大小的缩略图
	}

	/**
	 * create thumb from sd card
	 * 
	 * @param pathName
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromFd(String pathName, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
		Bitmap src = BitmapFactory.decodeFile(pathName, options);
		return createScaleBitmap(src, reqWidth, reqHeight);
	}

	/**
	 * convert view to bitmap
	 * 
	 * @param view
	 * @return
	 */
	public static Bitmap convertViewToBitmap(View view) {
		MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * file mime type
	 * 
	 * @param url
	 * @return
	 */
	public static String getMimeType(String url) {
		String type = null;
		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		if (extension != null) {
			MimeTypeMap mime = MimeTypeMap.getSingleton();
			type = mime.getMimeTypeFromExtension(extension);
		}
		return type;
	}

	/**
	 * 读取图片的旋转的角度
	 *
	 * @param path
	 *            图片绝对路径
	 * @return 图片的旋转角度
	 */
	public static int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 将图片按照某个角度进行旋转
	 *
	 * @param bm
	 *            需要旋转的图片
	 * @param degree
	 *            旋转角度
	 * @return 旋转后的图片
	 */
	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;
		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}

	public static boolean isNetworkConnected(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) { return true; }
				}
			}
		} catch (Exception e) {
			LogUtil.show(e.getMessage());
		}
		return false;
	}
}
