package com.concordiatec.vic.util;

import android.location.Location;

public class LocationUtil {
	/**
	 * return distance meters
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @return
	 */
	public double getDistanceMeters(double lat1, double lon1, double lat2, double lon2) {     
        float[] results=new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);     
        return results[0];     
    } 
}
