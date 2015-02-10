package com.concordiatec.vilnet.util;

import com.concordiatec.vilnet.model.ResData;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class ResponseUtil {
	public static ResData vicProcessResp( byte[] body ){
		Gson gson = new Gson();
		String responseBody = new String(body);
		if( isGoodJson(responseBody) ){
			return gson.fromJson( responseBody , ResData.class);
		}
		return null;
	}
	
	/**
	 * check String is valid json
	 * @param json
	 * @return
	 */
    public static boolean isGoodJson(String json) {  
        if (StringUtil.isEmpty(json)) {  
            return false;  
        }  
        try {  
            new JsonParser().parse(json);  
            return true;  
        } catch (Exception e) {
            return false;  
        }  
    }
}
