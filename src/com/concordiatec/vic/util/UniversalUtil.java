package com.concordiatec.vic.util;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import android.content.Context;
import android.graphics.Bitmap;

public class UniversalUtil {
	public static ImageLoaderConfiguration getUniversalConfig( Context context ){
		 return new ImageLoaderConfiguration  
			    .Builder(context)  
			    .threadPoolSize(4)//线程池内加载的数量  
			    .threadPriority(Thread.NORM_PRIORITY - 2)  
			    .denyCacheImageMultipleSizesInMemory()  
			    .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现  
			    .memoryCacheSize(10 * 1024 * 1024)
			    .tasksProcessingOrder(QueueProcessingType.LIFO)
			    .imageDownloader(new BaseImageDownloader(context, 10 * 1000, 40 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间  
			    .build();//开始构建  
	}
	
	public static DisplayImageOptions getUniversalDisplayOption(){
		return new DisplayImageOptions.Builder()
		.bitmapConfig(Bitmap.Config.RGB_565)
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
		.displayer(new FadeInBitmapDisplayer(300))
		.build();
	}
}
