package com.clubscaddy;


import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;


import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import com.bumptech.glide.util.Util;
import com.clubscaddy.BackGroundServies.BackgroundService;
import com.clubscaddy.BackGroundServies.FetchMemberListInBack;
import com.clubscaddy.Bean.LoginBean;
import com.clubscaddy.Bean.UserClub;
import com.clubscaddy.Interface.DialogBoxButtonListner;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.UserPermision;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.Server.WebService;

import com.clubscaddy.utility.SqlListe;
import com.clubscaddy.R;

public class SplashActivity extends AppCompatActivity {



	// Splash screen timer
	private static int SPLASH_TIME_OUT = 3000;
	Context mContext;
	Intent I;
	AQuery aQuery;

     SqlListe sqlListe;
	;


	PendingIntent pendingIntent;
	ProgressDialog pd;



SessionManager sessionManager ;
	ShowUserMessage showUserMessage ;



	;
	private void searchByGPS() {
		/*mYahooWeather.setNeedDownloadIcons(true);
		mYahooWeather.setUnit(YahooWeather.UNIT.FAHRENHEIT);
		mYahooWeather.setSearchMode(YahooWeather.SEARCH_MODE.GPS);
		mYahooWeather.queryYahooWeatherByGPS(getApplicationContext(), this);*/
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getSupportActionBar().hide();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_xml);
		Display  display= ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = display.getWidth(); 
		int height = display.getHeight(); 
		AppConstants.setViewPortWidth(width);
		AppConstants.setViewPortHeight(height);

		sessionManager = new SessionManager();
		sessionManager.getUser_device_new_token(getApplicationContext());
		getAppKeyHash();

		sqlListe = new SqlListe(getApplicationContext());





       // Toast.makeText(getApplicationContext() ,"time  "+currentDate.get(Calendar.HOUR_OF_DAY),1).show();

		/* Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");

        Calendar cal = Calendar.getInstance();
        long startTime = cal.getTimeInMillis();
        long endTime = cal.getTimeInMillis()  + 60 * 60 * 1000;

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,endTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

        intent.putExtra(Events.TITLE, "Neel Birthday");
        intent.putExtra(Events.DESCRIPTION,  "This is a sample description");
        intent.putExtra(Events.EVENT_LOCATION, "My Guest House");
        intent.putExtra(Events.RRULE, "FREQ=YEARLY");

        startActivity(intent);
*/













		mContext = this;
		aQuery = new AQuery(mContext);

		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1TZSsixaeFQyIRKDsbmZZj32O2ebTIGAmcA9dd9AABTNUPO11ADdxf6RAV7tKDfb0NRI2V6OavbrUPSAreHVdwOsJ5uJd736bJR4+x+ukR6zC8uHO2j/xLHy01sX7tfyMyX1/RK5LnjYnA71Yle64RkHek/lbwzpUv/yfrHGmEhRHOIQZ9Xek9pnUdet4yoArQmu136yqkl1juMDIEAaHR3WG5L24qwAbtDUS4hcIlE6rSaKQdO9mt3o7jVSffh32LK4Bo5hML3/r3lRkJ5zy+URDYN3y+1qWyveXWbntnF8YNfQub8ae8aZjjtylzHOtkDVxopAFDmFcJlUHOT4bwIDAQAB";

		// Some sanity checks to see if the developer (that's you!) really followed the
		// instructions to run this sample (don't put these checks on your app!)
		if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) 
		{
			throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
		}
		if (getPackageName().startsWith("com.example")) 
		{
			throw new RuntimeException("Please change the sample's package name! See README.");
		}

		// Create the helper, passing it our context and the public key to verify signatures with



		/*Club Type Info

		1) Free Club
		2) Demo Club
		3) Purchsed Club 		*/


/*		User Login App

	   1 ) It is in demo periode
	   2)  Over Demo
	   3)  Purchesed 	*/











               SessionManager.getUser_Club_id(getApplicationContext());



		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("user_email", SessionManager.getUser_email(mContext));

		if(sessionManager.getAppLoginType(getApplicationContext())== 2)
		{
			param.put("fb", sessionManager.getUserFacebookLink(getApplicationContext()));
		}

		param.put("user_password", SessionManager.getUser_Password(mContext));
		param.put("user_device_token", sessionManager.getUser_Device_Token(getApplicationContext()));
		param.put("user_device_type", "1");
		String value = SessionManager.getUser_id(mContext)+"";
		Log.e("string", value+"");
		//if(!Validation.isStringNullOrBlank(SessionManager.getUser_email(getApplicationContext())) &&(!Validation.isStringNullOrBlank(SessionManager.getUser_Password(getApplicationContext()))|| sessionManager.getAppLoginType(getApplicationContext())==2))
		{
			if(Validation.isNetworkAvailable(getApplicationContext()))
			{
				aQuery.ajax(WebService.login, param, JSONObject.class, new AjaxCallback<JSONObject>()
				{

					@Override
					public void callback(String url, final JSONObject object, AjaxStatus status) {
						// TODO Auto-generated method stub
						super.callback(url, object, status);

						if(object != null)
						{

							try
							{

								double appVersion =
										Double.parseDouble(object.getString("a_version"));




								if (AppConstants.currentVersion != appVersion)
								{

									showUserMessage = new ShowUserMessage(SplashActivity.this);

									String versionType = object.getString("a_type");

									if (versionType.equals("1"))
									{
										showUserMessage.showVersionControlDialog(SplashActivity.this);

									} else {
										showUserMessage.showDialogBoxWithYesNoButtonForVersionControl(SplashActivity.this, new DialogBoxButtonListner() {
											@Override
											public void onYesButtonClick(DialogInterface dialog)
											{

											}

											@Override
											public void onNoButtonClick(DialogInterface dialog) {
												super.onNoButtonClick(dialog);


												if (object.optBoolean("status"))
												{

													parseData(object);

												}
												else
												{
													Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
													startActivity(intent);
													finish();												}



											}
										});

									}
								}

								else
								{
									if(object.getBoolean("status"))
									{
										parseData(object);

									}
									else
									{
										movetonext();

									}

								}




							}
							catch (Exception e)
							{
								Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
								startActivity(intent);
								finish();
							}




						}
						else
						{
							Utill.showDialg(  getResources().getString(R.string.check_internet_connection) ,SplashActivity.this );
						}


					}

				});	
			}
			else
			{
				ShowUserMessage.showUserMessage(SplashActivity.this , "Please connect to Internet");
			}

		}




		/**/
	}



	public void parseData(JSONObject object)
	{
		saveData(object);

		Calendar calender = Calendar.getInstance();
		Calendar today = Calendar.getInstance();
		calender.set(Calendar.DATE, Integer.parseInt(SessionManager.getReminderdays(getApplicationContext()).split(" ")[1]));
		calender.set(Calendar.YEAR, Integer.parseInt(SessionManager.getReminderdays(getApplicationContext()).split(" ")[3]));
		calender.set(Calendar.MONTH, AppConstants.getMonthIndex(SessionManager.getReminderdays(getApplicationContext()).split(" ")[2])-1);
		Log.e("calender", calender.get(Calendar.DATE)+" "+calender.get(Calendar.MONTH)+" "+calender.get(Calendar.YEAR));

		//63625478399999
		long diff = calender.getTimeInMillis() - today.getTimeInMillis();

		AppConstants.setAutorenewdate(calender);
		long days = diff / (24 * 60 * 60 * 1000);

		;

		if (sessionManager.getUserLoginAppInfo(getApplicationContext()) ==1 )
		{
			if(sessionManager.getClubTypeInfo(getApplicationContext()) != 3)
			{
				movetonextlogin("Your trial period of this app expires on "+SessionManager.getReminderdays(getApplicationContext()));

			}
			else
			{
				movetonext();
			}

		}
		else
		{
			if(sessionManager.getClubTypeInfo(getApplicationContext()) == 2)
			{
				showDialogforfinish("Your demo for this app is over");
			}
			else
			{



				if(sessionManager.getLoginStep(getApplicationContext())==0)
				{
					I = new Intent(mContext, LoginActivity.class);
					startActivity(I);
					finish();
					return;
				}

				if(sessionManager.getLoginStep(getApplicationContext())==1)
				{
					I = new Intent(mContext, ClubListActivity.class);
					startActivity(I);
					finish();
					return;
				}
				if(sessionManager.getLoginStep(getApplicationContext())==2)
				{
					I = new Intent(mContext, ProfileWizardActivity.class);
					startActivity(I);
					finish();
					return;
				}

				if(sessionManager.getLoginStep(getApplicationContext())==3)
				{
					I = new Intent(mContext, AddMobileNumberActivity.class);
					startActivity(I);
					finish();
					return;
				}

				if(sessionManager.getLoginStep(getApplicationContext())==4)
				{
					I = new Intent(mContext, AddSocialNetWorkActivity.class);
					startActivity(I);
					finish();
					return;
				}

				if(sessionManager.getLoginStep(getApplicationContext())==5)
				{
					I = new Intent(mContext, AddReviewActivity.class);
					startActivity(I);
					finish();
					return;
				}

				if(sessionManager.getLoginStep(getApplicationContext())==6)
				{
					I = new Intent(mContext,BackgroundService.class);
					//	startService(I);

					if(SessionManager.getUser_type(mContext)!=null) {
						if (SessionManager.getUser_type(mContext).equals(AppConstants.USER_TYPE_DIRECTOR)) {
							I = new Intent(mContext, DirectorFragmentManageActivity.class);
							startActivity(I);
							finish();
						} else if (SessionManager.getUser_type(mContext).equals(AppConstants.USER_TYPE_MOBILE_ADMIN)) {
							I = new Intent(mContext, DirectorFragmentManageActivity.class);
							startActivity(I);
							finish();
						} else
						if (SessionManager.getUser_type(mContext).equals(AppConstants.USER_TYPE_MEMBER)) {
							I = new Intent(mContext, DirectorFragmentManageActivity.class);
							//I = new Intent(mContext,MemberFragmenttManagerActivity.class);
							startActivity(I);
							finish();
						}
						else {
							if (SessionManager.getUser_type(mContext).equals(AppConstants.USER_TYPE_COACH)) {
								I = new Intent(mContext, DirectorFragmentManageActivity.class);
								//I = new Intent(mContext,MemberFragmenttManagerActivity.class);
								startActivity(I);
								finish();
							}
						}
					}
				}

			}


		}
















		Log.e("response ", object+"");


	}




	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() { 
		super.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}





	public void showDialogforfinish(String msg)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this).create();

		// Setting Dialog Title
		alertDialog.setTitle(getResources().getString(R.string.app_name));
		alertDialog.setCancelable(false);
		// Setting Dialog Message
		alertDialog.setMessage(msg);

		// Setting Icon to Dialog


		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {





				finish();







				// Write your code here to execute after dialog closed
				//    Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
			}
		});

		// Showing Alert Message
		alertDialog.show();







	}

	@SuppressWarnings("deprecation")
	public void movetonextlogin(String msg)
	{







		AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this).create();

		// Setting Dialog Title
		alertDialog.setTitle(getResources().getString(R.string.app_name));
		alertDialog.setCancelable(false);
		// Setting Dialog Message
		alertDialog.setMessage(msg);

		// Setting Icon to Dialog


		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {




				if(sessionManager.getLoginStep(getApplicationContext()) == 1)
				{
					Intent intent = new Intent(getApplicationContext() , ClubListActivity.class);
					startActivity(intent);
					finish();

					return;
				}

				if(sessionManager.getLoginStep(getApplicationContext()) == 2)
				{
					Intent intent = new Intent(getApplicationContext() , ProfileWizardActivity.class);
					startActivity(intent);

					finish();
					return;
				}
				if(sessionManager.getLoginStep(getApplicationContext()) == 3)
				{
					Intent intent = new Intent(getApplicationContext() , AddMobileNumberActivity.class);
					startActivity(intent);

					finish();
					return;
				}
				if(sessionManager.getLoginStep(getApplicationContext()) == 4)
				{
					Intent intent = new Intent(getApplicationContext() , AddSocialNetWorkActivity.class);
					startActivity(intent);

					finish();
					return;
				}

				if(sessionManager.getLoginStep(getApplicationContext()) == 5)
				{
					Intent intent = new Intent(getApplicationContext() , AddReviewActivity.class);
					startActivity(intent);

					finish();
					return;
				}


				if(sessionManager.getLoginStep(getApplicationContext()) == 6)
				{

					if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_DIRECTOR))
						//		I = new Intent(mContext,DirectorFragmentManageActivity.class);
						I = new Intent(mContext,DirectorFragmentManageActivity.class);
					else if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MOBILE_ADMIN)){
						I = new Intent(mContext,DirectorFragmentManageActivity.class);
					}else if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_COACH)){
						I = new Intent(mContext,DirectorFragmentManageActivity.class);
					}
					else if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER)){
						//I = new Intent(mContext,MemberFragmenttManagerActivity.class);
						I = new Intent(mContext,DirectorFragmentManageActivity.class);
					}
					else {
						I = new Intent(mContext,DirectorFragmentManageActivity.class);
						//I = new Intent(mContext,DirectorFragmentManageActivity.class);
					}
					startActivity(I);
					finish();
					return;
				}












				// Write your code here to execute after dialog closed
				//    Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
			}
		});

		// Showing Alert Message
		alertDialog.show();









	}
	public void movetonext()
	{














		if(sessionManager.getLoginStep(getApplicationContext()) == 0)
		{
			Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
			startActivity(intent);
			finish();

			return;
		}





		if(sessionManager.getLoginStep(getApplicationContext()) == 1)
		{
			Intent intent = new Intent(getApplicationContext() , ClubListActivity.class);
			startActivity(intent);
			finish();

			return;
		}

		if(sessionManager.getLoginStep(getApplicationContext()) == 2)
		{
			Intent intent = new Intent(getApplicationContext() , ProfileWizardActivity.class);
			startActivity(intent);

			finish();
			return;
		}
		if(sessionManager.getLoginStep(getApplicationContext()) == 3)
		{
			Intent intent = new Intent(getApplicationContext() , AddMobileNumberActivity.class);
			startActivity(intent);

			finish();
			return;
		}
		if(sessionManager.getLoginStep(getApplicationContext()) == 4)
		{
			Intent intent = new Intent(getApplicationContext() , AddSocialNetWorkActivity.class);
			startActivity(intent);

			finish();
			return;
		}

		if(sessionManager.getLoginStep(getApplicationContext()) == 5)
		{
			Intent intent = new Intent(getApplicationContext() , AddReviewActivity.class);
			startActivity(intent);

			finish();
			return;
		}


		if(sessionManager.getLoginStep(getApplicationContext()) == 6)
		{

			if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_DIRECTOR))
				//		I = new Intent(mContext,DirectorFragmentManageActivity.class);
				I = new Intent(mContext,DirectorFragmentManageActivity.class);
			else if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MOBILE_ADMIN)){
				I = new Intent(mContext,DirectorFragmentManageActivity.class);
			}else if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_COACH)){
				I = new Intent(mContext,DirectorFragmentManageActivity.class);
			}
			else if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER)){
				//I = new Intent(mContext,MemberFragmenttManagerActivity.class);
				I = new Intent(mContext,DirectorFragmentManageActivity.class);
			}
			else {
				I = new Intent(mContext,DirectorFragmentManageActivity.class);
				//I = new Intent(mContext,DirectorFragmentManageActivity.class);
			}
			startActivity(I);
			finish();
			return;
		}


	}



	public  void saveData(JSONObject object)
	{


		try {


			JSONArray userClub_json_array = object.getJSONArray("user_club_info");
			SqlListe sqlListe = new SqlListe(mContext);
			sqlListe.deleteAllClub();
			for(int i = 0 ; i < userClub_json_array.length() ;i++)
			{
				JSONObject userClub_json_array_item = 	userClub_json_array.getJSONObject(i);
				UserClub userClub = new UserClub();
				userClub.setUser_id(userClub_json_array_item.getInt("user_id"));
				userClub.setUser_club_id(userClub_json_array_item.getInt("user_club_id"));
				userClub.setUser_type(userClub_json_array_item.getInt("user_type"));
				userClub.setUser_first_name(userClub_json_array_item.getString("user_first_name"));
				userClub.setUser_last_name(userClub_json_array_item.getString("user_last_name"));
				userClub.setUser_email(userClub_json_array_item.getString("user_email"));
				userClub.setUser_phone(userClub_json_array_item.getString("user_phone"));
				userClub.setUser_rating(userClub_json_array_item.getString("user_rating"));
				userClub.setUser_gender(userClub_json_array_item.getInt("user_gender"));
				userClub.setUser_junior(userClub_json_array_item.getInt("user_junior"));
				userClub.setUser_profilepic(userClub_json_array_item.getString("user_profilepic"));
				userClub.setUser_cost_per_hour(userClub_json_array_item.getInt("user_cost_per_hour"));
				userClub.setUser_device_token(userClub_json_array_item.getString("user_device_token"));
				userClub.setUser_statusl(userClub_json_array_item.getInt("user_status"));
				userClub.setUser_about(userClub_json_array_item.getString("user_about"));
				userClub.setUser_instagram(userClub_json_array_item.getString("user_instagram"));
				userClub.setUser_facebook(userClub_json_array_item.getString("user_facebook"));
				userClub.setSport_name((userClub_json_array_item.getString("sport_name")));
				userClub.setUser_twitteter(userClub_json_array_item.getString("user_twitteter"));
				userClub.setClubtype(userClub_json_array_item.getInt("clubtype"));
				userClub.setUser_expired_staus(userClub_json_array_item.getInt("user_expired_staus"));
				userClub.setClub_name(userClub_json_array_item.getString("user_club_name"));
				userClub.setUser_registereddate(userClub_json_array_item.getString("user_registereddate"));
				userClub.setUser_changeclub(userClub_json_array_item.getInt("user_changeclub"));
				userClub.setUser_login_app(userClub_json_array_item.getInt("user_login_app"));
				userClub.setClub_id(userClub_json_array_item.getInt("club_id"));
				userClub.setClub_name(userClub_json_array_item.getString("club_name"));
				userClub.setClub_address(userClub_json_array_item.getString("club_address"));
				userClub.setClub_country(userClub_json_array_item.getString("club_country"));
				userClub.setClub_status_change_date(userClub_json_array_item.getString("club_status_change_date"));

				try
				{
					userClub.setClublat(userClub_json_array_item.getLong("clublat"));
					userClub.setClublong(userClub_json_array_item.getLong("clublong"));
				}
				catch (Exception e)
				{

				}

				userClub.setClub_zip_code(userClub_json_array_item.getInt("club_zip_code"));
				userClub.setClub_num_of_courts(userClub_json_array_item.getInt("club_num_of_courts"));
				userClub.setClub_status_message(userClub_json_array_item.getString("club_status_message"));
				userClub.setClub_logo(userClub_json_array_item.getString("club_logo"));
				userClub.setClub_sport_id(userClub_json_array_item.getString("club_sport_id"));
				userClub.setClub_ratting_show(userClub_json_array_item.getInt("club_ratting_show"));
				userClub.setClub_status(userClub_json_array_item.getInt("club_status"));
				userClub.setPurchase_expiring(userClub_json_array_item.getString("purchase_expiring"));
				userClub.setDemo_period(userClub_json_array_item.getInt("demo_period"));
				userClub.setClub_tempreture(userClub_json_array_item.getInt("club_tempreture"));
				userClub.setClub_temp_date(userClub_json_array_item.getString("club_temp_date"));
				userClub.setClub_temp_id(userClub_json_array_item.getInt("club_temp_id"));
				userClub.setClub_temp_opencourts(userClub_json_array_item.getInt("club_temp_opencourts"));
				userClub.setClub_rating(userClub_json_array_item.getString("club_rating"));
				userClub.setClub_rating_type(userClub_json_array_item.getInt("club_rating_type"));
				userClub.setClub_score_view(Integer.parseInt(userClub_json_array_item.getString("club_score_view")));
				userClub.setSport_player(userClub_json_array_item.getInt("sport_player"));
				userClub.setSport_field_name(userClub_json_array_item.getString("sport_field_name"));

				userClub.setCurrencyCode(userClub_json_array_item.optString("currencyCode"));

				sqlListe.setUserClubInfo(userClub);

			}





			LoginBean loginBean = new LoginBean();
			UserClub userClub = sqlListe.getClubById(Integer.parseInt(SessionManager.getUser_Club_id(getApplicationContext())));

			loginBean.setUser_profilepic(SessionManager.getUserProfilePic(getApplicationContext()));

			loginBean.setRemaining_days(SessionManager.getReminderdays(getApplicationContext()));
			loginBean.setSixmonth_price(sessionManager.getSixMonthPrice(getApplicationContext()));
			loginBean.setOneyearprice(sessionManager.getOneYearPrice(getApplicationContext()));
			loginBean.setUser_password(SessionManager.getUser_Password(getApplicationContext()));
			loginBean.setUser_email(userClub.getUser_email());
			//loginBean.setUser_password(mObj.optString("user_password"));
			loginBean.setUser_device_token(userClub.getUser_device_token());
			loginBean.setUser_device_type(userClub.getUser_type()+"");
			loginBean.setUserid(userClub.getUser_id()+"");

			loginBean.setUser_club_id(userClub.getUser_club_id()+"");
			loginBean.setClub_logo(userClub.getClub_logo());
			loginBean.setClub_status_message(userClub.getClub_status_message());
			loginBean.setUser_type(userClub.getUser_type()+"");
			loginBean.setUser_first_name(userClub.getUser_first_name());
			loginBean.setUser_last_name(userClub.getUser_last_name());
			loginBean.setClubname(userClub.getClub_name());
			loginBean.setUser_login_app(userClub.getUser_login_app()+"");
			loginBean.setClubtype(userClub.getClubtype()+"");
			loginBean.setUser_id(userClub.getUser_id()+"");
			loginBean.setMobileNumber(userClub.getUser_phone());
			loginBean.setUser_facebook(userClub.getUser_facebook());
			loginBean.setUser_twitteter(userClub.getUser_twitteter());
			loginBean.setUser_instagram(userClub.getUser_instagram());
			loginBean.setUser_linkedin(userClub.getUser_linkedin());
			loginBean.setUser_login_app(userClub.getUser_login_app()+"");
			loginBean.setUser_profilepic(userClub.getUser_profilepic());
			loginBean.setClub_rating(userClub.getClub_rating());
			loginBean.setClub_score_view(userClub.getClub_score_view());
			loginBean.setClub_rating_type(userClub.getClub_rating_type());
			loginBean.setUser_rating(userClub.getUser_rating());
			loginBean.setSport_name(userClub.getSport_name());
			loginBean.setUser_sport_type(userClub.getSport_type());
			loginBean.setSport_player(userClub.getSport_player());
			loginBean.setSport_field_name(userClub.getSport_field_name());
			loginBean.setClub_status_change_date(userClub.getClub_status_change_date());

			loginBean.setCurrencyCode(userClub.getCurrencyCode());

			//Toast.makeText(mContext, "sport_type === "+mObj.optString("sport_type"), 1).show();
			SessionManager.onSaveUser(getApplicationContext(), loginBean);












		}
		catch (Exception e)
		{
			//Toast.makeText(getApplicationContext() , e.getMessage() ,1).show();
		}




	}







	private void getAppKeyHash() {
		try {

			String packageName = getPackageName() ;
			PackageInfo info = getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md;

				md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String something = new String(Base64.encode(md.digest(), Base64.DEFAULT));
				Log.d("Hash key", something);
			}
		} catch (PackageManager.NameNotFoundException e1) {
			// TODO Auto-generated catch block
			Log.e("name not found", e1.toString());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			Log.e("no such an algorithm", e.toString());
		} catch (Exception e) {
			Log.e("exception", e.toString());
		}

	}


}
