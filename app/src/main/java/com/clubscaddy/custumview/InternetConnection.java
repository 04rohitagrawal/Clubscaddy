package com.clubscaddy.custumview;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)

public class InternetConnection {

	public static boolean isInternetOn(Context context) {

		NetworkInfo localNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
		return (localNetworkInfo != null) && (localNetworkInfo.isConnected());

		// get Connectivity Manager object to check connection
		
	}
	public static boolean isNetworkAvailable(Context mContext) {
	    try {
	    	ConnectivityManager connec = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

	        NetworkInfo networkInfo = connec.getActiveNetworkInfo();
	        if (networkInfo != null && networkInfo.isConnected())
	            return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}
}
