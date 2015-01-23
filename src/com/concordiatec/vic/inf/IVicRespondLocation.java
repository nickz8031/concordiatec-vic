package com.concordiatec.vic.inf;

import com.nhn.android.maps.nmapmodel.NMapPlacemark;

public interface IVicRespondLocation {
	/**
	 * 좌표 획득 성공
	 * @param lat
	 * @param lng
	 */
	public void onSucced(double lat , double lng);
	/**
	 * 좌표 획득 실패
	 * @param reason
	 */
	public void onFailed(String reason);
	/**
	 * 위치 획득 성공
	 * @param placeMark
	 */
	public void onPlaceComplete(NMapPlacemark placeMark);
	/**
	 * 위치 획득 실패
	 * @param reason
	 */
	public void onPlaceError(String reason);
}
