package com.concordiatec.vilnet.util;

import java.text.SimpleDateFormat;
import java.util.Locale;
import com.concordiatec.vilnet.R;
import android.content.Context;

@SuppressWarnings("unused")
public class TimeUtil {
	
	private final static int MINUTE_SECONDS = 60;
	private final static int HOUR_SECONDS = 60 * MINUTE_SECONDS;
	private final static int DAY_SECONDS = 24 * HOUR_SECONDS;
	private final static int MONTH_SECONDS = 30 * DAY_SECONDS;
	private final static int YEAR_SECONDS = (12 * MONTH_SECONDS) + (8 * DAY_SECONDS);
	
	public static String getUnixTimestamp() {
		return (System.currentTimeMillis()+"").substring(0, 10);
	}
	public static String getUnixTimestampMills() {
		return (System.currentTimeMillis()+"");
	}
	
	/**
	 * like 4 hours ago
	 * @param pastSeconds
	 * @return
	 */
	public static String getTimePast( Context context, int pastSeconds ){
		String dateString = "";
		int minutues = pastSeconds / MINUTE_SECONDS;
		if( minutues > 0 && minutues < 60 ){
			dateString = minutues + context.getResources().getString(R.string.time_format_string_minute);
		}else if( minutues >= 60 && minutues < 1440 ){
			int days = pastSeconds / 3600;
			dateString = days + context.getResources().getString(R.string.time_format_string_hour);
		}else if( minutues >= 1440 && minutues < 10080 ){
			int days = pastSeconds / 86400;
			dateString = days + context.getResources().getString(R.string.time_format_string_day);
		}else if( minutues >= 10080 ){
			long startTime = System.currentTimeMillis() - ( pastSeconds * 1000 );
			SimpleDateFormat sdf = new SimpleDateFormat( "yyyy.MM.dd" , Locale.KOREA );
			dateString = sdf.format(startTime);
		}else{
			//min < 0
			dateString = pastSeconds + context.getResources().getString(R.string.time_format_string_second); 
		}
		
		return dateString;
	}
}
