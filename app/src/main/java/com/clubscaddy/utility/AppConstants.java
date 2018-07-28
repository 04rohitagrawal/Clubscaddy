package com.clubscaddy.utility;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.clubscaddy.Bean.GroupBean;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.Bean.NewsBean;
import com.clubscaddy.Bean.VerifyResponse;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.shortcutburgerdata.ShortcutBadger;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

/**
 * @author administrator
 *
 */
public class AppConstants {
	public static final String READ = "1";
	public static final String UNREAD = "2";
	public static DirectorFragmentManageActivity directorFragmentManageActivity = null;

	public static final String YES = "1";
	public static final String NO = "2";
	public static final String NOTANSWERED = "3";
	public static final String Match_Maker = "1";
	
	public static	ArrayList<NewsBean> newsList;
	
	public static final String  drop_in= "Match Maker"; 
	private static int viewPortWidth = 0;
	private static int viewPortHeight = 0;
	private static ArrayList<String> timeSlots;
	private static String numberOfOpenCourts;

	public static final double currentVersion  =4.0 ;

	public static final double currentVersion1  =4.0 ;

	public static final int AllMEMBERlIST = 0;
	public static final int ADMINLIST = 1;
	public static final int DIRECTORADMINLIST = 2;
	//public static final String READ = "1";
	//public static final String READ = "1";



	/*public static final String CLIENT_ID = "f932bc90b92c4143b2bcb23fdf73e03d";
	public static final String CLIENT_SECRET = "c69f0ab9037f45619b8d20a17b13edb2";
	public static final String CALLBACK_URL = "https://clubscaddy.com/";*/

	public static final String CLIENT_ID = "1f000ebaafb04acc88322ba24077dfd2";
	public static final String CLIENT_SECRET = "5710b44bb6e14d978c8456898ad62df8";
	public static final String CALLBACK_URL = "https://clubscaddy.com";


	public static int getUser_classified() {
		return user_classified;
	}

	public static void setUser_classified(int user_classified) {
		AppConstants.user_classified = user_classified;
	}

	public static int user_classified ;


	public static String getUser_news() {
		return user_news;
	}

	public static void setUser_news(String user_news) {
		AppConstants.user_news = user_news;
	}

	public static String user_news = "";




	public static String getNumberOfOpenCourts() {
		return numberOfOpenCourts;
	}

	public static void setNumberOfOpenCourts(String numberOfOpenCourts) {
		AppConstants.numberOfOpenCourts = numberOfOpenCourts;
	}

	public static String getNumberOfBookedCourts() {
		return numberOfBookedCourts;
	}
public static Calendar autorenewdate ;
	
	
	
	public static Calendar getAutorenewdate()
	{
		return autorenewdate;
	}


	public static void setAutorenewdate(Calendar autorenewdate1) 
	{
		autorenewdate = autorenewdate1;
	}
	public static void setNumberOfBookedCourts(String numberOfBookedCourts) {
		AppConstants.numberOfBookedCourts = numberOfBookedCourts;
	}

	private static String numberOfBookedCourts;
	private static String club_temp;
	public static String getClub_temp() {
		return club_temp;
	}

	public static void setClub_temp(String club_temp) {
		AppConstants.club_temp = club_temp;
	}

	public static String getClub_status_message() {
		return club_status_message;
	}

	public static void setClub_status_message(String club_status_message) {
		AppConstants.club_status_message = club_status_message;
	}

	private static String club_status_message;

	public static String club_status_change_date;
	
	
	public static ArrayList<String> getTimeSlots() {
		return timeSlots;
	}

	public static void setTimeSlots(ArrayList<String> timeSlots) {
		AppConstants.timeSlots = timeSlots;
	}

	public static int getViewPortWidth() {
		return viewPortWidth;
	}

	public static void setViewPortWidth(int viewPortWidth) {
		AppConstants.viewPortWidth = viewPortWidth;
	}

	public static int getViewPortHeight() {
		return viewPortHeight;
	}

	public static void setViewPortHeight(int viewPortHeight) {
		AppConstants.viewPortHeight = viewPortHeight;
	}

	private static String notificationCount = "0"; 

	public static String getNotificationCount() {
		return notificationCount;
	}

	public static void  setNotificationCount(String notificationCount,Context context) 
	{
		AppConstants.notificationCount = notificationCount;
	try {
		ShortcutBadger.applyCount(context, Integer.parseInt(notificationCount));
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

	public static final String[] courtMaxHr = { "01:00", "1:30", "02:00", "2:30", "03:00" };
	public static final String[] courtMinHr = { "00:30", "01:00" };
	public static final String[] startTimeForFirstBooking = { "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00",
			"12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30",
			"20:00", "20:30", "21:00", "21:30", "22:00" };

	public static final String[] BookingPerWeek = { "2", "3", "4", "5", "6", "7" };
	public static final String[] BookingPerDay = { "1", "2", "3" };

	public static final String[] AdvanceFarBooking = { "1", "2", "3" };
	public static final String[] RecursiveBookingBeforeMonth = { "1", "3", "6" };

	public static final String USER_TYPE_SITE_ADMIN = "1";
	public static final String USER_TYPE_DIRECTOR = "2";
	public static final String USER_TYPE_COACH = "3";
	public static final String USER_TYPE_MEMBER = "4";
	public static final String USER_TYPE_MOBILE_ADMIN = "5";
	
	
	public static final String NOTIFICATION_DROPINS = "1";
	public static final String NOTIFICATION_LINK = "2";
	public static final String NOTIFICATION_SCORE = "4";
	public static final String NOTIFICATION_BROADCAST = "5";
	public static final String NOTIFICATION_POLLING = "6";
	public static final String General_Notification = "7";
	public static final String  COACH_Notification = "8";
	public static final String  LEAGUE_Notification = "9";



	public static final String INVITATION_STATUS_INVITE = "1";
	public static final String INVITATION_STATUS_JOIN = "2";
	public static final String INVITATION_STATUS_DECLINE = "3";
	public static final String INVITATION_STATUS_ACCEPT = "4";
	public static final String INVITATION_STATUS_REJECT = "5";
	
	public static final String BROADCAST = "1";
	public static final String POLLING = "2";
	

	static ArrayList<VerifyResponse> verifyResponseList = new ArrayList<VerifyResponse>();
	static ArrayList<MemberListBean> membersList = new ArrayList<MemberListBean>();
	static ArrayList<GroupBean> groupList = new ArrayList<GroupBean>();

	/*public static ArrayList<MemberListBean> getMembersList() {
		return membersList;
	}

	public static void setMembersList(ArrayList<MemberListBean> membersList) {
		AppConstants.membersList = membersList;
	}
*/
	public static ArrayList<VerifyResponse> getVerifyResponseList() {
		return verifyResponseList;
	}

	public static void setVerifyResponseList(ArrayList<VerifyResponse> verifyResponseList) {
		AppConstants.verifyResponseList = verifyResponseList;
	}

	public static ArrayList<GroupBean> getGroupList() {
		return groupList;
	}

	public static void setGroupList(ArrayList<GroupBean> groupList) {
		AppConstants.groupList = groupList;
	}
	public static String getAppDate(Calendar c) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		//SimpleDateFormat  simpleDateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy h:mm a"); 
		//time = simpleDateFormat.format(time);
		String date=sdf.format(c.getTime())+" "+String.format("%01d", c.get(Calendar.DATE))+" "+android.text.format.DateFormat.format("MMMM", c.getTime())+" "+c.get(Calendar.YEAR)/*+" "+)*/;
		
		Log.e("Time ", date);
		return date;
	}



	public static String getAppDateFromCal(Calendar c) {
		//SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		SimpleDateFormat  sdf = new SimpleDateFormat("EEEE dd MMMM yyyy");
		//time = simpleDateFormat.format(time);


		String date=sdf.format(c.getTime()) ;//+" "+String.format("%01d", c.get(Calendar.DATE))+" "+android.text.format.DateFormat.format("MMMM", c.getTime())+" "+c.get(Calendar.YEAR)/*+" "+)*/;

		Log.e("Time ", date);
		return date;
	}


	
	public static String getAppDate(String date) {
		
		
		
		return date.split("")[1]+" "+ getMonthName(Integer.parseInt(date.split(" ")[2])) +"/"+date.split("/")[3];
	}
	public static Calendar getCalenderFromAppDate(String date)
	{
		Calendar calendar = Calendar.getInstance();
		try
		{
			calendar.set(Calendar.DATE, Integer.parseInt(date.split(" ")[1]));
			calendar.set(Calendar.MONTH,  getMonthIndex(date.split(" ")[2])-1);
			calendar.set(Calendar.YEAR, Integer.parseInt(date.split(" ")[3]));
			
		}
		catch(Exception e)
		{
			
		}
		return calendar;
		
	}
	
	
	public static String getDateInDDMMYYYYY(String date)
	{
		return date.split(" ")[1]+"-"+ getMonthIndex(date.split(" ")[2])+"-"+date.split(" ")[3];
	}
//2017/1/28
	public static String getDateInDDMMYYYYYWithServerSending(String date)
	{
		return date.split(" ")[3]+"/"+ getMonthIndex(date.split(" ")[2])+"/"+date.split(" ")[1];
	}


	public static String getMonthName(int monthinx)
	{
		
	if(monthinx ==1)	
	{
	return "January";	
	}
	if(monthinx ==2)	
	{
		return "February";	
	}
	if(monthinx ==3)	
	{
		return "March";	
	}
	if(monthinx ==4)	
	{
		return "April";	
	}
	if(monthinx ==5)	
	{
		return "May";
	}
	if(monthinx ==6)	
	{
		return "June";	
	}
	if(monthinx ==7)	
	{
		return "July";	
	}
	if(monthinx ==8)	
	{
		return "August";	
	}	
	
	
	if(monthinx ==9)	
	{
		return "September";	
	}
	if(monthinx ==10)	
	{
		return "October";	
	}
	
	if(monthinx ==11)	
	{
		return "November";	
	}
	if(monthinx ==12)	
	{
		return "December";	
	}
	return "";
	}
	public static int getMonthIndex(String month)
	{
		
	if(month.equalsIgnoreCase("January"))	
	{
	return 1;	
	}
	if(month.equalsIgnoreCase("February"))	
	{
		return 2;	
	}
	if(month.equalsIgnoreCase("March"))	
	{
		return 3;	
	}
	if(month.equalsIgnoreCase("April"))	
	{
		return 4;	
	}
	if(month.equalsIgnoreCase("May"))	
	{
		return 5;
	}
	if(month.equalsIgnoreCase("June"))	
	{
		return 6;	
	}
	if(month.equalsIgnoreCase("July"))	
	{
		return 7;	
	}
	if(month.equalsIgnoreCase("August"))	
	{
		return 8;	
	}	
	
	
	if(month.equalsIgnoreCase("September"))	
	{
		return 9;	
	}
	if(month.equalsIgnoreCase("October"))	
	{
		return 10;	
	}
	
	if(month.equalsIgnoreCase("November"))	
	{
		return 11;	
	}
	if(month.equalsIgnoreCase("December"))	
	{
		return 12;	
	}
	return 0;
	}
	public  static void hideSoftKeyboard(Activity activity) {
	    if(activity.getCurrentFocus()!=null) {
	        InputMethodManager inputMethodManager = (InputMethodManager)activity. getSystemService(activity.INPUT_METHOD_SERVICE);
	        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	    }
	}

	public static int getDeviceWidth(Activity activity)
	{
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width=dm.widthPixels;
		/*int height=dm.heightPixels;*/


		return width ;
	}



	public static int getDeviceHeight(Activity activity)
	{
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

		int height=dm.heightPixels;


		return height ;
	}
	public static String getDateInYYYYMMDD(Calendar calendar)
	{
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	String myDate =	simpleDateFormat.format(calendar.getTime());

				return myDate;
	}


	public static String getDateInYYYYMMDD(String dateInDDMMYYYY)
	{

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

		Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
		try {
			calendar.setTime(dateFormat.parse(dateInDDMMYYYY));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String myDate =	simpleDateFormat.format(calendar.getTime());

		return myDate;
	}

	public static  Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	}
	public static String getRealPathFromURI(Uri uri ,Activity activity) {
		Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
		cursor.moveToFirst();
		int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
		return cursor.getString(idx);
	}


	private void savePref(String str , Context mContext) {

		SharedPreferences sf = mContext.getSharedPreferences("LoginUser", 1);
		SharedPreferences.Editor editor = sf.edit();
		editor.putString("imageURI", str);
		editor.commit();
	}

}
