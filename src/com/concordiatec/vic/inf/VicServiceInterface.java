package com.concordiatec.vic.inf;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.internal.LinkedTreeMap;

public interface VicServiceInterface {
	/**
	 * parse LinkedTreeMap list data to Custom JavaBean model which from Retrofit request transform
	 * @param list
	 * @return
	 */
	List<?> mapListToModelList( ArrayList<LinkedTreeMap<String, Object>> list);
	
	/**
	 * parse LinkedTreeMap data to JavaBean model which from Retrofit request transform
	 * @param map
	 * @return
	 */
	Object mapToModel(LinkedTreeMap<String, Object> map);
	
}