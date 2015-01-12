package com.concordiatec.vic.tools;

import android.webkit.MimeTypeMap;


public class Tools {
	
	public static int getIntValue( Object object ){
		return Double.valueOf(object.toString()).intValue();
	} 
	
	public static String getMimeType(String url){
	    String type = null;
	    String extension = MimeTypeMap.getFileExtensionFromUrl(url);
	    if (extension != null) {
	        MimeTypeMap mime = MimeTypeMap.getSingleton();
	        type = mime.getMimeTypeFromExtension(extension);
	    }
	    return type;
	}
}
