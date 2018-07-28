package com.clubscaddy.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Calendar;

import com.clubscaddy.Bean.LoginBean;
import com.clubscaddy.R;

public class SessionManager {

	
	String AppPurchaseDate ;
	
	public String sixmonth_price;
	public String oneyearprice;

	public int clubTypeInfo ;

	boolean inStragramStatus ;

	public SessionManager()
	{

	}
	SharedPreferences sharePref;
	SharedPreferences.Editor editor ;


	public String getLastDateOfMemberListUpdation()
	{

		String lastDateOfMemberListUpdation = sharePref.getString("lastDateOfMemberListUpdation", "");
		return lastDateOfMemberListUpdation;

	}
/////
	public void setLastDateOfMemberListUpdation(String lastDateOfMemberListUpdation)
	{
		editor.putString("lastDateOfMemberListUpdation", lastDateOfMemberListUpdation);
		editor.commit();	}

	String lastDateOfMemberListUpdation  ;




	public SessionManager(Context activity)
	{
		 sharePref = activity.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		editor = sharePref.edit() ;


	}



	public boolean isInStragramStatus(Context mContext)
	{
		boolean inStragramStatus = sharePref.getBoolean("inStragramStatus", false);
		return inStragramStatus;
	}

	public void setInStragramStatus(boolean inStragramStatus)
	{
		editor.putBoolean("inStragramStatus", inStragramStatus);
		editor.commit();
	}


	public String getCurrentTime() {

		String currentTime = sharePref.getString("currentTime", "2018-02-18 11:13:33");

		return currentTime;
	}

	public void setCurrentTime(String currentTime) {


		editor.putString("currentTime", currentTime);
		editor.commit();
	}

	String currentTime ;




	public int getClubTypeInfo(Context mContext) {

		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		int clubTypeInfo = sharePref.getInt("clubTypeInfo", 0);

		return clubTypeInfo;
	}
	public void setClubTypeInfo(Context context , int clubTypeInfo) {

		SharedPreferences sharePref = context.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putInt("clubTypeInfo", clubTypeInfo);
		editor.commit();

	}


	public int getUserLoginAppInfo(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		int userLoginApp = sharePref.getInt("userLoginApp", 0);

		return userLoginApp;
	}

	public void setUserLoginAppInfo(Context context , int userLoginApp) {
		SharedPreferences sharePref = context.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putInt("userLoginApp", userLoginApp);
		editor.commit();
	}

	int userLoginApp ;





	public void setSport_name(String sport_name) {
		this.sport_name = sport_name;
	}

	String sport_name;

	public boolean isSaveEventInCaleneder(Context mContext) {


		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		 isSaveEventInCaleneder = sharePref.getBoolean("isSaveEventInCaleneder", true);

		return isSaveEventInCaleneder;
	}

	public void setSaveEventInCaleneder(Context context,  boolean saveEventInCaleneder , String calenderEntryTitle , String entryDuration)
	{


		SharedPreferences sharePref = context.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putBoolean("isSaveEventInCaleneder", saveEventInCaleneder);
		editor.putString("calenderEntryTitle", calenderEntryTitle);
		editor.putString("entryDuration", entryDuration);
		editor.commit();



	}
	public String getCalenderEntryTitle(Context mContext)
	{
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String calenderEntryTitle = sharePref.getString("calenderEntryTitle", "");

		return calenderEntryTitle;
	}


	public String getAlaramTimeDuration(Context mContext)
	{
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String entryDuration = sharePref.getString("entryDuration", "60 Minut");

		return entryDuration;
	}


	public boolean isSaveEventInCaleneder = true;




	public int getAppLoginType(Context mContext) {


		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		int appLoginType = sharePref.getInt("appLoginType", 0);
		return appLoginType;


	}

	public void setAppLoginType(Context context , int appLoginType) {
		SharedPreferences sharePref = context.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putInt("appLoginType", appLoginType);
		editor.commit();
	}

	int appLoginType = 0 ;






	public int getLoginStep(Context mContext)
	{
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		int applevel = sharePref.getInt("applevel", 0);
     	return applevel;
	}

	public void setLoginStep(Context mContext , int applevel)
	{

		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putInt("applevel", applevel);
		editor.commit();

	}

	int applevel ;




	public void setUser_device_new_token(Context mContext,  String user_device_new_token)
	{

		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putString("user_device_new_token", user_device_new_token);
		editor.commit();


	}

	String user_device_new_token ;



	
	
	
	public void setSixMonthPrice(Context mContext,String price) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putString("sixmonthprice", price);
		editor.commit();
		
	}
	public void setOneYearPrice(Context mContext,String price) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putString("oneyearprice", price);
		editor.commit();
		
	}
	
	public String getSixMonthPrice(Context mContext) 
	{
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String price = sharePref.getString("sixmonthprice", AppConstants.getAppDate(Calendar.getInstance()));
		return price;
	}
	public String getOneYearPrice(Context mContext) 
	{
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String price = sharePref.getString("oneyearprice", AppConstants.getAppDate(Calendar.getInstance()));
		return price;
	}
	
	public String getAppPurchaseDate(Context mContext) 
	{
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String AppPurchaseDate = sharePref.getString("app_purchase_date", AppConstants.getAppDate(Calendar.getInstance()));
		return AppPurchaseDate;
	}


	public void setAppPurchaseDate(Context mContext,String appPurchaseDate) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putString("app_purchase_date", AppPurchaseDate);
		editor.commit();
		
	}


	public int getTimePeriode(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		int timePeriode = sharePref.getInt("timePeriode", 0);
		
		return timePeriode;
	}


	public void setTimePeriode(Context mContext,int timePeriode) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putInt("timePeriode", timePeriode);
		editor.commit();
		;
	}




	int timePeriode;


	public String getCurrencyCode(Activity activity)
	{

		SharedPreferences sharePref = activity.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String currencyCode = sharePref.getString("currencyCode", "");


		return currencyCode;
	}

	public void setCurrencyCode(Activity activity ,String currencyCode)
	{
		SharedPreferences sharePref = activity.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putString("currencyCode", currencyCode);
		editor.commit();


	}

	String currencyCode ;


	public static void onSaveUser(Context mContext, LoginBean loginBean) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putString("user_email", loginBean.getUser_email());
		editor.putString("user_password", loginBean.getUser_password());
		editor.putString("user_device_token", loginBean.getUser_device_token());
		editor.putString("user_device_type", loginBean.getUser_device_type());
		editor.putString("user_id", loginBean.getUser_id());
		editor.putString("user_club_id", loginBean.getUser_club_id());
		editor.putString("club_logo", loginBean.getClub_logo());
		editor.putString("club_status_message", loginBean.getClub_status_message());
		editor.putString("user_type", loginBean.getUser_type());
		editor.putString("user_first_name", loginBean.getUser_first_name());
		editor.putString("user_last_name", loginBean.getUser_last_name());
		editor.putString("user_profilepic", loginBean.getUser_profilepic());
		editor.putString("sport_type", loginBean.getUser_sportType());
		editor.putString("user_login_app", loginBean.getUser_login_app());
		editor.putString("clubname", loginBean.getClubname());
		editor.putString("remaining_days", loginBean.getRemaining_days());
		editor.putString("clubtype", loginBean.getClubtype());
		editor.putString("sixmonthprice", loginBean.getSixmonth_price());
		editor.putString("oneyearprice", loginBean.getOneyearprice());
		editor.putString("user_id" ,loginBean.getUser_id());

		editor.putString("club_rating" ,loginBean.getClub_rating()) ;
		editor.putInt("club_ratting_show" ,loginBean.getClub_ratting_show());
		editor.putString("user_rating" ,loginBean.getUser_rating());
        editor.putInt("club_rating_type" ,loginBean.getClub_rating_type());
		editor.putString("user_phone" ,loginBean.getMobileNumber());
		editor.putString("sport_name" ,loginBean.getSport_name());

		//editor.putString("lastDateOfMemberListUpdation" ,loginBean.getLastDateOfMemberListUpdation());


		if(Validation.isStringNullOrBlank(getUserFacebookLink1(mContext))) {

			editor.putString("user_facebook", loginBean.getUser_facebook());
		}
		editor.putString("user_linkedin" ,loginBean.getUser_linkedin());
		editor.putString("user_instagram" ,loginBean.getUser_instagram());
		editor.putString("user_twitteter" ,loginBean.getUser_twitteter());
		editor.putInt("club_score_view" ,loginBean.getClub_score_view());
		editor.putInt("sport_player" ,loginBean.getSport_player());
		editor.putString("sport_field_name" ,loginBean.getSport_field_name());
		editor.putString("club_status_change_date" ,loginBean.getClub_status_change_date());
		editor.putString("currencyCode", loginBean.getCurrencyCode());

		//clubname
	///	editor.putString("mobile", loginBean.getMobileNumber());
		editor.commit();
	}

	public String getClub_status_change_date(Context context) {
		SharedPreferences sharePref = context.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String club_status_change_date = sharePref.getString("club_status_change_date","" );


		return club_status_change_date;
	}

	public void setClub_status_change_date(Context mContext , String club_status_change_date)
	{

		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putString("club_status_change_date" ,club_status_change_date);
		editor.commit();
	}

	String club_status_change_date;



	public String getSportFiledName(Context mContext)
	{

		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String club_score_view = sharePref.getString("sport_field_name","" );

		return club_score_view;
	}


	public int getClub_score_show(Context mContext)
	{

		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		int club_score_view = sharePref.getInt("club_score_view",2 );

		return club_score_view;
	}

	int club_ratting_show ;
	
	public static void setPassword(Context mContext,String password) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putString("user_password", password);
		editor.commit();
	}
	public static String getClubType(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String clubType = sharePref.getString("clubtype", null);
		return clubType;
	}
	public static void setSportType(Context mContext,String sport_type) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putString("sport_type", sport_type);
		editor.commit();
	}



	public static void setClubLogo(Context mContext,String clublogo) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putString("club_logo",clublogo);
		editor.commit();
	}



	public static String getClubName(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_id = sharePref.getString("clubname", mContext.getResources().getString(R.string.app_name));
		return user_id;
	}
	public static String getClubMessage(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_id = sharePref.getString("club_status_message", "");
		return user_id;
	}
	
	public static void setClubMessage(Context mContext, String clubMessage) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putString("club_status_message", clubMessage);
		editor.commit();
	}
	

	public static String getFirstName(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_id = sharePref.getString("user_first_name", "");
		return user_id;
	}
	
	public static String getReminderdays(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_id = sharePref.getString("remaining_days", "");
		return user_id;
	}
	
	
	public static void setFirstName(Context mContext, String firstName) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putString("user_first_name", firstName);
		editor.commit();
	}




	public static String getUserProfilePic(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_id = sharePref.getString("user_profilepic", null);
		return user_id;
	}
	public static void setUserProfilePic(Context mContext, String firstName) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putString("user_profilepic", firstName);
		editor.commit();
	}

	public static void setMobileNumber(Context mContext, String number) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putString("mobile", number);
		editor.commit();
	}


	public static void setPhoneNumber(Context mContext, String number) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putString("user_phone", number);
		editor.commit();
	}




	public static String getMobileNumber(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_id = sharePref.getString("mobile","");
		return user_id;
	}

	public static String getPhoneNumber(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_phone = sharePref.getString("user_phone","");
		return user_phone;
	}


	public static String getUser_id(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_id = sharePref.getString("user_id", null);
		return user_id;
	}

	public static String getUser_type(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_id = sharePref.getString("user_type", null);
		return user_id;
	}

	public static String getUser_Club_id(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_id = sharePref.getString("user_club_id", "0");
		return user_id;
	}

	public static String getUser_email(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_id = sharePref.getString("user_email", null);
		return user_id;
	}

	public static String getUser_Password(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_id = sharePref.getString("user_password", null);
		return user_id;
	}

	public static String getUser_Device_Token(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_id = sharePref.getString("user_device_token", null);
		return user_id;
	}

	

	
	
	
	
	public static String getClub_Logo(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String club_logo = sharePref.getString("club_logo", null);
		return club_logo;
	}

	public static void setLogoUrl(Context mContext, String logoUrl) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putString("club_logo", logoUrl);
		editor.commit();
	}

	public static void setTempurature(Context mContext, String logoUrl) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putString("tempurature", logoUrl);
		editor.commit();
	}

	public static String getTempurature(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String club_logo = sharePref.getString("tempurature", "83F");
		return club_logo;
	}
	
	
	public static String getUserId(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_id = sharePref.getString("user_id", "");
		return user_id;
	}
	
	public static String getLastName(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_id = sharePref.getString("user_last_name", "");
		return user_id;
	}
	public static void setLastName(Context mContext, String firstName) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putString("user_last_name", firstName);
		editor.commit();
	}
	
	public static void clearSharePref(Context mContext) {
		SharedPreferences settings = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		
		
		String email = SessionManager.getUser_email(mContext);
		String token = SessionManager.getUser_Device_Token(mContext);


		settings.edit().clear().commit();
		Editor editor = settings.edit();
		editor.putString("user_email", email);
		editor.putString("user_device_token", token);
		editor.commit();
	}

	public static String getSport_type(Context mContext) {
		SharedPreferences settings = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		return settings.getString("sport_type", "1");
	}



	public static void setUserDeviceToken(Context mContext, String user_device_token) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		Editor editor = sharePref.edit();
		editor.putString("user_device_token", user_device_token);
		editor.commit();
	}

	public String getUser_device_new_token(Context mContext) {

		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_device_new_token = sharePref.getString("user_device_new_token", "");

		return user_device_new_token;
	}

	public static String getUserBack_device_new_token(Context mContext) {

		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_device_new_token = sharePref.getString("user_device_new_token", "");

		return user_device_new_token;
	}
	
	public static String getUserLoginApp(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_login_app = sharePref.getString("user_login_app", "");
		return user_login_app;
	}

/*



	editor.putInt("" ,loginBean.getClub_rating_type());
	*/



	public String getClubRatting(Context mContext) {

		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String club_rating = sharePref.getString("club_rating", "");
		return club_rating;
	}

	public String getUserMobileNo(Context mContext) {

		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_phone = sharePref.getString("user_phone", "");
		return user_phone;
	}



	public int getClubRattingShow(Context mContext) {

		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		int club_rating = sharePref.getInt("club_ratting_show", 1);
		return club_rating;
	}







	public String getUserRating(Context mContext) {

		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_rating = sharePref.getString("user_rating", "");
		return user_rating;
	}


	public int getUserRatingType(Context mContext) {

		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		int club_rating_type = sharePref.getInt("club_rating_type", 1);
		return club_rating_type;
	}

	public int getClubRatingType(Context mContext) {

		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		int club_rating_type = sharePref.getInt("club_rating_type", 1);
		return club_rating_type;
	}

	/*


	editor.putString("" ,loginBean.getUser_twitteter());*/
	public static String getUserFacebookLink1(Context mContext) {

		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_facebook = sharePref.getString("user_facebook", "");
		return user_facebook;
	}


	public String getUserFacebookLink(Context mContext) {

		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_facebook = sharePref.getString("user_facebook", "");
		return user_facebook;
	}


	public String getUserLinkedInLink(Context mContext) {

		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_linkedin = sharePref.getString("user_linkedin", "");
		return user_linkedin;
	}



	public String getUserInstragramLink(Context mContext) {

		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_instagram = sharePref.getString("user_instagram", "");
		return user_instagram;
	}

	public String getSportName(Context mContext) {

		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String sport_name = sharePref.getString("sport_name", "");
		return sport_name;
	}


	public String getUserTwitterLink(Context mContext) {

		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String user_twitteter = sharePref.getString("user_twitteter", "");
		return user_twitteter;
	}



	public  int getSportPlayerQty(Context mContext) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		int player_qty = sharePref.getInt("sport_player", 0);
		return player_qty-1;
	}

	String absPath ;

	public void setCameraPagePath(Context mContext , String path) {

		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharePref.edit();
		editor.putString("absPath", path);
		editor.commit();

	}



	public  String getCameraPagePath(Context mContext ) {
		SharedPreferences sharePref = mContext.getSharedPreferences("clubscaddy", Context.MODE_PRIVATE);
		String absPath = sharePref.getString("absPath", "");
		return absPath;
	}

    String resmoduleStatus ;
	String reservationLink ;

	public String getResmoduleStatus()
	{

		resmoduleStatus = sharePref.getString("resmoduleStatus","1");


		return resmoduleStatus;
	}

	public void setResmoduleStatus(String resmoduleStatus)
	{
		editor.putString("resmoduleStatus",resmoduleStatus);
		editor.commit();
	}

	public String getReservationLink() {

		reservationLink = sharePref.getString("reservationLink","");

		return reservationLink;
	}

	public void setReservationLink(String reservationLink) {
		editor.putString("reservationLink",reservationLink);
		editor.commit();
	}
}
