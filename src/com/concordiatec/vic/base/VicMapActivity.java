package com.concordiatec.vic.base;

import com.concordiatec.vic.constant.Constant;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.NotifyUtil;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

@SuppressLint("HandlerLeak")
public class VicMapActivity extends NMapActivity {
	private NMapLocationManager mMapLocationManager;
	private NMapView mMapView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mMapLocationManager = new NMapLocationManager(this);
		mMapView = new NMapView(this);
		mMapView.setApiKey(Constant.NMAP_API_KEY);
		super.setMapDataProviderListener(new DataProviderLis());
	}
	
	protected void getMyLocation() {
		if (!mMapLocationManager.isMyLocationEnabled()) {
			boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);
			if (!isMyLocationEnabled) {
				NotifyUtil.toast(this, "Please enable a My Location source in system settings");
				Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(goToSettings);
				return;
			}else{
				delayGetMyLocation();
			}
		}else{
			if( mMapLocationManager.getMyLocation() == null ){
				delayGetMyLocation();
			}else{
				double lat = mMapLocationManager.getMyLocation().getLatitude();
				double lng = mMapLocationManager.getMyLocation().getLongitude();
				findPlacemarkAtLocation( lng , lat );
				mMapLocationManager.disableMyLocation();
				LogUtil.show("LAT : " + lat );
				LogUtil.show("LONG : " + lng );
			}
			
		}
		
	}
	
	private void delayGetMyLocation(){
		delayExcute(new Runnable() {
					@Override
					public void run() {
						getMyLocation();
					}
				} , 1000);
	}
	
	private void delayExcute(Runnable runnable , int millis){
		new Handler(){}.postDelayed(runnable, millis);
	}
	
	private final class DataProviderLis implements OnDataProviderListener{
		@Override
		public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError error) {
			if( placeMark !=null ){
				LogUtil.show( placeMark.toString() );
			}
			if (error != null) {
				LogUtil.show(error.toString());
				return;
			}
		}
	}
	
	
}

