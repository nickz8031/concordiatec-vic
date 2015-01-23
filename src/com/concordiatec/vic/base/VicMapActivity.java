package com.concordiatec.vic.base;

import com.concordiatec.vic.constant.Constant;
import com.concordiatec.vic.inf.IVicRespondLocation;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.NotifyUtil;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
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
	private IVicRespondLocation respondListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mMapLocationManager = new NMapLocationManager(this);
		mMapView = new NMapView(this);
		mMapView.setApiKey(Constant.NMAP_API_KEY);
		super.setMapDataProviderListener(new DataProviderLis());
	}
	
	protected void getMyLocation( IVicRespondLocation lis ) {
		if( lis == null ) return;
		this.respondListener = lis;
		if (!mMapLocationManager.isMyLocationEnabled()) {
			boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);
			if (!isMyLocationEnabled) {
				String reason = "Please enable a My Location source in system settings";
				NotifyUtil.toast(this, reason);
				Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(goToSettings);
				lis.onFailed( reason );
				return;
			}else{
				delayGetMyLocation();
			}
		}else{
			NGeoPoint geoPoint = mMapLocationManager.getMyLocation();
			if( geoPoint == null ){
				delayGetMyLocation();
			}else{
				findPlacemarkAtLocation( geoPoint.getLongitude() , geoPoint.getLatitude() );		
				lis.onSucced(geoPoint.getLatitude() , geoPoint.getLongitude());		
				mMapLocationManager.disableMyLocation();
			}
			
		}
		
	}
	
	private void delayGetMyLocation(){
		if(Constant.DEBUG) {
			LogUtil.showDebug(getClass().getSimpleName() + ": Retry to get my location.");
		}
		delayExcute(new Runnable() {
					@Override
					public void run() {
						getMyLocation(respondListener);
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
				respondListener.onPlaceComplete(placeMark);
				if( Constant.DEBUG ){
					LogUtil.showDebug( placeMark.toString() );
				}
				
			}
			if (error != null) {
				if( Constant.DEBUG ){
					LogUtil.show( error.toString() );
				}
				respondListener.onPlaceError(error.toString());
				return;
			}
		}
	}
}

