package com.clubscaddy.BackGroundServies;

import java.util.HashMap;

import org.json.JSONObject;


import com.clubscaddy.shortcutburgerdata.ShortcutBadger;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.Server.HttpRequest1;
import com.clubscaddy.Server.WebService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class BackgroundService extends Service{

	String Tag = getClass().getName();
	String regId="";
	Handler handler;
	SessionManager sessionManager ;

	HttpRequest1 httpRequest ;



	@Override
	public void onCreate() {
		super.onCreate();



		handler = new Handler();

		
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, final int flags, int startId) {
		super.onStartCommand(intent, flags, startId);


		Log.e("on date" , "oncreate");

		HashMap<String ,Object>params = new HashMap<String ,Object>();

		httpRequest = new HttpRequest1(getApplicationContext());
		/*nameValuePairs.add(new BasicNameValuePair("", );
		nameValuePairs.add(new BasicNameValuePair("", );
		nameValuePairs.add(new BasicNameValuePair("", );
		nameValuePairs.add(new BasicNameValuePair("", "1"));*/

		sessionManager = new SessionManager();
		params.put("notifications_reciever_id" , SessionManager.getUser_id(getApplicationContext()));

		//Toast.makeText(getApplicationContext() ,SessionManager.getUserBack_device_new_token(getApplicationContext())+"",1 ).show();

		//params.put("user_password" , SessionManager.getUser_Password(this));

		params.put("user_club_id" , SessionManager.getUser_Club_id(getApplicationContext()));

		httpRequest.getResponse(WebService.getNotificationCount, params, new OnServerRespondingListener(getApplicationContext()) {
			@Override
			public void onSuccess(JSONObject mObj) {

				try
				{

					//{"user_password":null,"user_device_token":"","user_device_type":"1","user_email":"sona.ypsilon@gmail.com","msg":"Missing parameter","status":"false"}

                    Log.e("response" , mObj.toString()+"");


					try
					{
						int unreadCount = mObj.getInt("unread");
						AppConstants.setUser_news(mObj.getString("user_news"));
						AppConstants.setNotificationCount(unreadCount+"",activity);
						try {
							ShortcutBadger.applyCount(getApplicationContext(), unreadCount);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//user_news
					}
					catch (Exception e)
					{

					}





















																														}
				catch (Exception e)
				{

				}


			}
		}) ;






		return 0;
	}

	@Override
	public void onDestroy() {

		super.onDestroy();

	}










	public abstract class OnServerRespondingListener
	{
		Context activity;

		public OnServerRespondingListener(Context activity)
		{
			this.activity = activity;
		}

		public abstract void onSuccess(JSONObject jsonObject);
		public void onConnectionError()
		{

			//ShowUserMessage.showUserMessage(activity ,"Server Connection Problem!");
		}
		public void onNetWorkError()
		{
			//ShowUserMessage.showUserMessage(activity ,"Network error!");
		}
		public  void internetConnectionProble()
		{
			///ShowUserMessage.showUserMessage(activity ,"Please connect to internet!");


		}

	}








}
