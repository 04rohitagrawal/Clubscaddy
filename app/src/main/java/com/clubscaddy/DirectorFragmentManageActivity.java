package com.clubscaddy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.clubscaddy.BackGroundServies.AlarmReceiver;
import com.clubscaddy.Bean.UserPics;
import com.clubscaddy.Interface.FragmentBackResponseListener;
import com.clubscaddy.Interface.ModelManager;
import com.clubscaddy.fragment.AddMatchMakerFragment;
import com.clubscaddy.fragment.ReservationWebView;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.SqlListe;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.fragment.AddEventFragment;
import com.clubscaddy.fragment.AddGroupFragment;
import com.clubscaddy.fragment.AddNewsFeedFragment;
import com.clubscaddy.fragment.BroadCastDetailFrageMent;
import com.clubscaddy.fragment.BroadCastHomeFragment;
import com.clubscaddy.fragment.ChangeClubMessageFragment;
import com.clubscaddy.fragment.ChangePasswordFragment;
import com.clubscaddy.fragment.CreateScoreFragment;
import com.clubscaddy.fragment.DirectorDashboardFragment;
import com.clubscaddy.fragment.DirectorSettingFragment;
import com.clubscaddy.fragment.DropInGroupHomeFragment;
import com.clubscaddy.fragment.EventDetailFrageMent;
import com.clubscaddy.fragment.ManageGroupFragment;
import com.clubscaddy.fragment.ManageRatingFragment;
import com.clubscaddy.fragment.ManageSportTypeFragment;
import com.clubscaddy.fragment.NewEventsFrageMent;
import com.clubscaddy.fragment.NewsFeedActivity;
import com.clubscaddy.fragment.NotificationsFragment;
import com.clubscaddy.fragment.ScoreListFragment;
import com.clubscaddy.fragment.ShowReservationFragment;
import com.clubscaddy.fragment.ShowScoreDetailFrageMent;
import com.clubscaddy.fragment.UserProfileActivity;
import com.clubscaddy.fragment.AddMemberFragment;
import com.clubscaddy.fragment.MembersDirectoryFragment;

import com.clubscaddy.fragment.UpdateClubLogoFragment;
import com.clubscaddy.utility.Validation;
;import okhttp3.internal.Util;

public class DirectorFragmentManageActivity extends AppCompatActivity
{

	String Tag = getClass().getName();
	private static Context mContext;
	private static Activity activity;
	public static FragmentManager mFragmentManager;
	public static Fragment mFragment;
	public static FragmentTransaction ft;
	public static ActionBar mActionBar;
	public static FragmentManager fM;
	public static final int DirectorDashboardFragment_id = 0;
	public static final int AddAdminFragment_id = 1;
	public static final int AddCoachFragment_id = 2;
	public static final int AdminListFragment_id = 3;
	public static final int EditAdminFragment_id = 4;
	public static final int DirectorSettingFragment_id = 5;
	public static final int CoachListFragment_id = 6;
	public static final int UpdateClubLogoFragment_id = 7;
	public static final int Add_Memeber_Fragment_id = 8;
	public static final int MembersListFragment_id = 9;
	public static final int ManageCourtReservationFragment_id = 10;
	public static final int CourtDetailFragment_id = 11;
	public static final int ChangePasswordFragment_id = 12;
	public static final int EditMyProfileFragment_id = 13;
	public static final int ShowProfileFragment_id = 14;
	public static final int ShowReservationFragment_id = 15;
	public static final int ShowMembersFragment_id = 16;
	public static final int ChangeClubMessaegFragment_id = 17;
	public static final int BulkBookingSelectionFragment_id = 18;
	public static final int BulkBookingVerifyFragment_id = 19;
	public static final int RecursiveBookingSelectionFragment_id = 20;
	public static final int DropInsGroupFragment_id = 21;
	public static final int AddGroupFragment_id = 22;
	public static final int AddDropInsFragment_id = 23;
	public static final int NotificationsFragment_id = 24;
	public static final int NewsFeedFragment_id = 25;
	public static final int AddNewsFeedFragment_id = 26;
	public static final int EventsHomeFragment_id = 27;
	public static final int AddEventsFragment_id = 28;
	public static final int MembersDirectoryFragment_id = 29;
	public static final int MembersDetail_id = 30;
	public static final int CreatScore_id = 31;
	public static final int ShowScoreDetail_id = 32;
	public static final int EventDetail_id = 33;

	public static final int BroadcastPolling_id = 34;
	public static final int BroadcastDetail_id = 35;
	public static final int PollingDetail_id = 36;


	public static final int manage_sport_type_id = 37;
	public static final int manage_rating_id = 38;

	public static final int score_list_fragment = 39;

	public static final int DropInsMatchMakerFragment_id = 40;
	public static final String DirectorDashboardFragment_tag = "DirectorDashboardFragment";
	public static final String AddAdminFragment_tag = "AddAdminFragment";

	public static final String AdminListFragment_tag = "AdminListFragment";
	public static final String DirectorSettingFragment_tag = "DirectorSettingFragment";

	public static final String AddCoachFragment_tag = "AddCoachFragment";
	public static final String CoachesListFragment_tag = "CoachListFragment";
	public static final String UpdateLogoFragment_tag = "UpdateLogoFragment";

	public static final String AddMemberFragment_tag = "AddMemberFragment";
	public static final String MemberListFragment_tag = "MemberListFragment";

	public static final String CourtDetailFragment_tag = "CourtDetailFragment";

	public static String ManageCourtReservationFragment_tag = "ManageCourtReservationFragment";
	public static String ChangePasswordFragment_tag = "ChangePasswordFragment";

	public static String EditMyProfileFragment_tag = "EditMyProfileFragment";
	public static String ShowProfileFragment_tag = "ShowProfileFragment_tag";

	public static String ShowReservationFragment_tag = "ShowReservationFragment";
	public static String ShowMembersFragment_tag = "ShowMembersFragment_tag";

	public static String ChangeClubMessaegFragment_tag = "ChangeClubMessaegFragment_tag";

	public static String BulkBookingSelectionFragment_tag = "BulkBookingSelectionFragment_tag";
	public static String BulkBookingVerifyFragment_tag = "BulkBookingVerifyFragment_tag";
	public static String RecursiveBookingSelectionFragment_tag = "RecursiveBookingVerifyFragment_tag";
	public static String DropInsGroupFragment_tag = "DropInsGroupFragment_tag";
	public static String AddGroupFragment_tag = "AddGroupFragment_tag";
	public static String AddDropInsFragment_tag = "AddDropInsFragment_tag";
	public static String NotificationsFragment_tag = "NotificationsFragment_tag";

	public static String NewsFeedFragment_tag = "NewsFeedFragment_tag";
	public static String AddNewsFeedFragment_tag = "AddNewsFeedFragment_tag";

	public static String EventsHomeFragment_tag = "EventsHomeFragment_tag";
	public static String AddEventsFragment_tag = "AddEventsFragment_tag";

	public static String MembersDirectoryFragment_tag = "MembersDirectoryFragment_tag";

	public static String MembersDetail_tag = "MembersDetail_tag";

	public static String CreatScore_tag = "CreatScore_tag";
	public static String ShowScoreDetail_tag = "ShowScoreDetail_tag";

	public static String EventDetail_tag = "EventDetail_tag";
	public static String BroadcastPolling_tag = "BroadcastPolling_tag";

	public static String BroadcastDetail_tag = "BroadcastDetail_tag";
	public static String PollingDetail_tag = "PollingDetail_tag";

	public static final String Manage_sport_type_tag = "manage_sport_type_tag";


	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;

	static UserProfileActivity	userProfileActivity ;


	static SessionManager sessionManager ;


public static   boolean isbackPress = true;
	public static ImageButton  backButton, uploadNewsOrEditProfile;
	public static	ImageButton logoutButton ;
	public static	TextView delete_all_btn;
	public static TextView actionbar_titletext;
	public static ImageView ad_icon_iv;

	//
	Timer timer;
	@Override
	public void onBackPressed() 
	{
		/*ShowReservationFragment myFragment = (ShowReservationFragment) getSupportFragmentManager().findFragmentByTag(ShowReservationFragment_tag);
		if (myFragment != null && myFragment.isVisible()) {
		   // add your code here
			Toast.makeText(getApplicationContext(), "call", 1).show();
			myFragment.hideKeyboard();
		}	
		else*/
		{
			super.onBackPressed();	
		}
  
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (switchAction != DirectorDashboardFragment_id)
		{
			return;
		}


		try
		{
			alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
					Calendar.getInstance().getTimeInMillis() ,1000*10, alarmIntent);

		}
		catch (Exception e)
		{

		}


	}
	 public static int switchAction = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.directordashboard_xml);
		mContext = this;
		activity= this;
		mActionBar = getSupportActionBar();
		mFragmentManager = getSupportFragmentManager();
		setActionBar();
		 fM =   getSupportFragmentManager();

		sessionManager = new SessionManager(DirectorFragmentManageActivity.this);






		//showNotification();
		/* int density= getResources().getDisplayMetrics().densityDpi;

		 switch(density)
		 {
		 case DisplayMetrics.DENSITY_LOW:
		    Toast.makeText(getApplicationContext(), "LDPI", Toast.LENGTH_SHORT).show();
		     break;
		 case DisplayMetrics.DENSITY_MEDIUM:
		      Toast.makeText(getApplicationContext(), "MDPI", Toast.LENGTH_SHORT).show();
		     break;
		 case DisplayMetrics.DENSITY_HIGH:
		     Toast.makeText(getApplicationContext(), "HDPI", Toast.LENGTH_SHORT).show();
		     break;
		 case DisplayMetrics.DENSITY_XHIGH:
		      Toast.makeText(getApplicationContext(), "XHDPI", Toast.LENGTH_SHORT).show();
		     break;
		 }*/
		alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
		alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 1255, intent, 0);
		alarmMgr.cancel(alarmIntent);
		//EditMyProfileFragment_id

		 switchAction = getIntent().getIntExtra("switchAction" , DirectorDashboardFragment_id);

		if (switchAction == DirectorDashboardFragment_id)
		{
			switchFragment(DirectorDashboardFragment_id);
			callAsynchronousTask();
		}
		else
		{
			switchFragment(switchAction);
		}

	}

	public static void setActionBar() {
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayUseLogoEnabled(false);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setCustomView(((Activity) mContext).getLayoutInflater().inflate(R.layout.director_actionbar_header_xml, null),
				new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

		backButton = (ImageButton) mActionBar.getCustomView().findViewById(R.id.actionbar_backbutton);
		logoutButton = (ImageButton) mActionBar.getCustomView().findViewById(R.id.actionbar_logoutbutton);

		ad_icon_iv = (ImageView) mActionBar.getCustomView().findViewById(R.id.ad_icon_iv);
		//
		logoutButton.setVisibility(View.GONE);
		uploadNewsOrEditProfile = (ImageButton) mActionBar.getCustomView().findViewById(R.id.upload_news);
		actionbar_titletext = (TextView) mActionBar.getCustomView().findViewById(R.id.actionbar_titletext);
		// addToCalender();


		Toolbar parent =(Toolbar) mActionBar.getCustomView().getParent();//first get parent toolbar of current action bar
		parent.setContentInsetsAbsolute(0,0);
		delete_all_btn =(TextView) mActionBar.getCustomView().findViewById(R.id.delete_all_btn);
		logoutButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doLogout();
			}
		});
	}

	public static void showBackButton() {
		if (backButton != null) {
			backButton.setVisibility(View.VISIBLE);
			uploadNewsOrEditProfile.setVisibility(View.GONE);
		}
	}

	public static void hideBackButton() {
		if (backButton != null)
			backButton.setVisibility(View.GONE);
	}

	public static void updateTitle(String title) {
		if (actionbar_titletext != null) {
			actionbar_titletext.setText(title);
		}

	}

	public static void hideLogout() {
		if (logoutButton != null) {
			logoutButton.setVisibility(View.GONE);
		}
	}

	public static void visibleLogout() {
		if (logoutButton != null) {
			logoutButton.setVisibility(View.GONE);
			logoutButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					doLogout();
				}
			});
		}
	}

	@SuppressLint("Recycle")
	public static void switchFragment(int fragment_id) {
		
		
		
		
		
			mFragment = null;
		ft = mFragmentManager.beginTransaction();
		switch (fragment_id) {
		case DirectorDashboardFragment_id:
		//Fragment	mFragment = DirectorDashboardFragment.getInstance(mFragmentManager);
		DirectorDashboardFragment mFragment1 = new DirectorDashboardFragment();
			if (mFragment1 != null) 
			{
				
				ft.replace(R.id.content_frame, mFragment1, DirectorDashboardFragment_tag);

				ft.commit();
			}
			break;
		case ShowReservationFragment_id:


			if (sessionManager.getResmoduleStatus().equals("1"))
			{
				mFragment = ShowReservationFragment.getInstance(mFragmentManager);

			}
			else
			{
				if (Validation.isStringNullOrBlank(sessionManager.getReservationLink()))
				{
					ShowUserMessage.showUserMessage(mContext , "This feature is not available for your club.");
				    return;
				}
				else
				{
					mFragment = new ReservationWebView();
				}
			}

			if (mFragment != null) {
				ft.replace(R.id.content_frame, mFragment, ShowReservationFragment_tag);
				ft.addToBackStack(null);
				ft.commit();
			}
			break;

		case NotificationsFragment_id:
			mFragment = new NotificationsFragment();
			if (mFragment != null) {
				ft.replace(R.id.content_frame, mFragment, NotificationsFragment_tag);
				ft.addToBackStack(null);
				ft.commit();
			}
			break;
			case AddDropInsFragment_id:
				mFragment = AddMatchMakerFragment.getInstance(mFragmentManager);
				if (mFragment != null) {
					ft.replace(R.id.content_frame, mFragment, AddDropInsFragment_tag);
					ft.addToBackStack(null);
					ft.commit();
				}
				break;
		case CreatScore_id:
			mFragment = CreateScoreFragment.getInstance(mFragmentManager);
			if (mFragment != null) {
				ft.replace(R.id.content_frame, mFragment, CreatScore_tag);
				ft.addToBackStack(null);
				ft.commit();
			}
			break;
			//score_list_fragment
		case score_list_fragment:
			mFragment = ScoreListFragment.getInstance(mFragmentManager);
			if (mFragment != null) {
				ft.replace(R.id.content_frame, mFragment, "Score_list_tag");
				ft.addToBackStack(null);
				ft.commit();
			}
			break;
		case BroadcastPolling_id:
			/*mFragment = BroadCastHomeFragment.getInstance(mFragmentManager);
			if (mFragment != null) {
				ft.replace(R.id.content_frame, mFragment, BroadcastPolling_tag);
				ft.addToBackStack(null);
				ft.commit();
			}*/

			BroadCastHomeFragment broadCastHomeFragment = new BroadCastHomeFragment();
			Bundle bundle = new Bundle();
			bundle.putString("status" , "1");
			broadCastHomeFragment.setArguments(bundle);
			ft.replace(R.id.content_frame, broadCastHomeFragment, BroadcastPolling_tag);
			ft.addToBackStack(null);
			ft.commit();

			break;
		case ShowScoreDetail_id:
			mFragment = ShowScoreDetailFrageMent.getInstance(mFragmentManager);
			if (mFragment != null) {
				ft.replace(R.id.content_frame, mFragment, ShowScoreDetail_tag);
				ft.addToBackStack(null);
				ft.commit();
			}
			break;
		case EventDetail_id:
			mFragment = EventDetailFrageMent.getInstance(mFragmentManager);
			if (mFragment != null) {
				ft.replace(R.id.content_frame, mFragment, EventDetail_tag);
				ft.addToBackStack(null);
				ft.commit();
			}
			break;

		case BroadcastDetail_id:
			mFragment = BroadCastDetailFrageMent.getInstance(mFragmentManager,1);
			if (mFragment != null) {
				ft.replace(R.id.content_frame, mFragment, BroadcastDetail_tag);
				ft.addToBackStack(null);
				ft.commit();
			}
			break;

		case PollingDetail_id:
			mFragment = BroadCastDetailFrageMent.getInstance(mFragmentManager,2);
			if (mFragment != null) {
				ft.replace(R.id.content_frame, mFragment, PollingDetail_tag);
				ft.addToBackStack(null);
				ft.commit();
			}
			break;


		case MembersDirectoryFragment_id:
			mFragment = MembersDirectoryFragment.getInstacne(mFragmentManager);
			if (mFragment != null) {
				ft.replace(R.id.content_frame, mFragment, MembersDirectoryFragment_tag);
				ft.addToBackStack(null);
				ft.commit();
			}
			break;
		case AddEventsFragment_id:



           AddEventFragment addEventFragment = new AddEventFragment();
			mFragmentManager.beginTransaction().replace(R.id.content_frame  , addEventFragment , "").addToBackStack("").commit();

			break;
		case EventsHomeFragment_id:
			mFragment = NewEventsFrageMent.getInstance(mFragmentManager);
			if (mFragment != null) {
				ft.replace(R.id.content_frame, mFragment, EventsHomeFragment_tag);
				ft.addToBackStack(null);
				ft.commit();
			}
			break;

		case AddNewsFeedFragment_id:
		Intent intent1 = new Intent(mContext , AddNewsFeedFragment.class);
		intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent1);
			break;
		case NewsFeedFragment_id:
			//Intent intent = new Intent(mContext , NewsFeedActivity.class);
			//mContext.startActivity(intent);
            mFragment =new NewsFeedActivity();
            if (mFragment != null) {
                ft.replace(R.id.content_frame, mFragment, "");
                ft.addToBackStack(null);
                ft.commit();
            }


			break;



			case DropInsMatchMakerFragment_id:
			mFragment = DropInGroupHomeFragment.getInstance(mFragmentManager);
			if (mFragment != null) {
				ft.replace(R.id.content_frame, mFragment, DropInsGroupFragment_tag);
				ft.addToBackStack(null);
				ft.commit();
			}




				break;



		case DropInsGroupFragment_id:
			/*mFragment = DropInGroupHomeFragment.getInstance(mFragmentManager);
			if (mFragment != null) {
				ft.replace(R.id.content_frame, mFragment, DropInsGroupFragment_tag);
				ft.addToBackStack(null);
				ft.commit();
			}*/

			ManageGroupFragment manageGroupFragment = new ManageGroupFragment();
			ft.replace(R.id.content_frame, manageGroupFragment, DropInsGroupFragment_tag);
			ft.addToBackStack(null);
			ft.commit();


			break;
		case AddGroupFragment_id:
			mFragment = new AddGroupFragment();
			if (mFragment != null) {
				ft.replace(R.id.content_frame, mFragment, AddGroupFragment_tag);
				ft.addToBackStack(null);
				ft.commit();
			}
			break;



		case EditMyProfileFragment_id:

			/*if (userProfileActivity != null)
			{
				userProfileActivity.onResume();
				return;
			}*/



			 	userProfileActivity =new UserProfileActivity();
			AQuery ad = new AQuery(mContext);

		     final	ProgressDialog pd = new ProgressDialog(mContext) ;
			pd.setMessage("Loading...");
			pd.setCancelable(false);

			if(Validation.isNetworkAvailable(mContext))
			{
				pd.show();
				try
				{
				HashMap<String, Object>params = new HashMap<String, Object>();
				params.put("user_club_id", SessionManager.getUser_Club_id(mContext));
				params.put("user_type", SessionManager.getUser_type(mContext));
				params.put("user_id", SessionManager.getUser_id(mContext));
				ad.ajax(WebService.editdirectorinfo, params, JSONObject.class, new AjaxCallback<JSONObject>()
						{
					@Override
					public void callback(String url, JSONObject object, AjaxStatus status) {
						// TODO Auto-generated method stub
						super.callback(url, object, status);
						Log.e("object", object+"");
						pd.dismiss();
						if(object != null)
						{
						try
						{
						MemberListBean bean = new MemberListBean();
						if(object.getString("user_gender").equals("1"))
						{
							bean.setUser_gender("Male");
						}
						else
						{
							bean.setUser_gender("Female");
						}

						JSONArray userPicsJsonArray = object.getJSONArray("user_pics");

						ArrayList<UserPics> userPicsArrayList = new ArrayList<UserPics>();

						for (int i = 0 ; i < userPicsJsonArray.length() ; i++)
						{
							UserPics userPics = new UserPics();
							JSONObject jsonObject = userPicsJsonArray.getJSONObject(i);
							userPics.setImageid(jsonObject.getInt("id"));
							userPics.setImage_thumb(jsonObject.getString("thumb"));
							userPics.setImage_url(jsonObject.getString("url"));
							userPicsArrayList.add(userPics);
						}


                        bean.setUserPicsArrayList(userPicsArrayList);
						bean.setInstragram_url(object.getString("instagram"));
						bean.setUser_type(object.getString("user_type"));
						bean.setUser_status(object.getString("status"));
						bean.setLinkedin_url(object.getString("linkedin"));
						bean.setUser_junior(object.getString("user_junior"));
						bean.setUser_club_id(SessionManager.getUser_Club_id(mContext));
						bean.setUser_rating(object.getString("user_rating"));
						bean.setUser_profilepic(object.getString("user_profilepic"));
						bean.setUser_email(object.getString("user_email"));
						bean.setTwitter_url(object.getString("twitter"));
						bean.setUser_first_name(object.getString("user_first_name"));
						bean.setUser_about_me(object.getString("addabout"));
						bean.setUser_phone(object.getString("user_phone"));
						bean.setFace_book_url(object.getString("facebookurl"));
						bean.setUser_last_name(object.getString("user_last_name"));
						bean.setUser_id(object.getString("user_id"));
						bean.setInstragramToken(object.optString("insta_key"));
							userProfileActivity.setInatanse(bean ,1);
							ft.replace(R.id.content_frame, userProfileActivity, EditMyProfileFragment_tag);
							ft.addToBackStack(null);

							if (switchAction == DirectorDashboardFragment_id)
							{

							}

							ft.commit();
						}
						catch(Exception e)
						{

						}
						}
						else
						{
						Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_LONG).show();
						}
					}
						});
				/*
				}	*/
				}
				catch(Exception e)
				{

				}

			}
			
			break;

	/*	case AddAdminFragment_id:
			mFragment = AddAdminFragment.getInstance(mFragmentManager);
			if (mFragment != null) {
				ft.replace(R.id.content_frame, mFragment, AddAdminFragment_tag);
				ft.addToBackStack(null);
				ft.commit();
			}
			break;*/


		case ChangePasswordFragment_id:
			mFragment = ChangePasswordFragment.getInstacne(mFragmentManager);
			if (mFragment != null) {
				ft.replace(R.id.content_frame, mFragment, ChangePasswordFragment_tag);
				ft.addToBackStack(null);
				ft.commit();
			}
			break;
		case ChangeClubMessaegFragment_id:
			ChangeClubMessageFragment	changeClubMessageFragment = new ChangeClubMessageFragment();
			if (changeClubMessageFragment != null) {
				ft.replace(R.id.content_frame, changeClubMessageFragment, ChangeClubMessaegFragment_tag);
				ft.addToBackStack(null);
				ft.commit();
			}
			break;



		case Add_Memeber_Fragment_id:
			mFragment = AddMemberFragment.getInstance(mFragmentManager);
			if (mFragment != null) {
				ft.replace(R.id.content_frame, mFragment, AddMemberFragment_tag);
				ft.addToBackStack(null);
				ft.commit();
			}
			break;
		case UpdateClubLogoFragment_id:
			mFragment = UpdateClubLogoFragment.getInstance(mFragmentManager);
			if (mFragment != null) {
				ft.replace(R.id.content_frame, mFragment, UpdateLogoFragment_tag);
				ft.addToBackStack(null);
				ft.commit();
			}
			break;





		case DirectorSettingFragment_id:
			mFragment = DirectorSettingFragment.getInstance(mFragmentManager);
			if (mFragment != null) {
				ft.replace(R.id.content_frame, mFragment, DirectorSettingFragment_tag);
				ft.addToBackStack(null);
				ft.commit();
			}
			break;
		
			
		case manage_sport_type_id:
			mFragment = ManageSportTypeFragment.getInstance(mFragmentManager);
			 //ManageSportTypeFragment sporttypemFragment = new ManageSportTypeFragment();
			if (mFragment != null) {
			
				fM.beginTransaction().replace(R.id.content_frame, mFragment, "Manage sport").addToBackStack(null).commit()  ;              //(R.id.content_frame, mFragment, Manage_sport_type_tag);
				/*fM.beginTransaction().addToBackStack(null);
				fM.beginTransaction().commit();*/
			}
			break;
		case manage_rating_id:
			 ManageRatingFragment ratingmFragment = new ManageRatingFragment();
		//	if (ratingmFragment != null)
			{
				fM.beginTransaction().replace(R.id.content_frame, ratingmFragment, "avvv").addToBackStack(null).commit()  ;              //(R.id.content_frame, mFragment, Manage_sport_type_tag);
				
			}
			break;
			
			
			
			
			
		}
	}

	public static void popBackStackFragment() {

		if (mFragmentManager != null) {
			mFragmentManager.popBackStack();
		}
	}

	public static void getCleanPopBackStack() {
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				while (mFragmentManager.getBackStackEntryCount() > 0) {
					try
					{
						mFragmentManager.popBackStackImmediate();
					}
					catch(Exception e)
					{

					}

				}
			}
		});

		SqlListe sqlListe = new SqlListe(mContext);
		sqlListe.deleteAllTable();

		SessionManager.clearSharePref(mContext);
		Intent I = new Intent(mContext, LoginActivity.class);
		mContext.startActivity(I);
		getActivityFinnish();
	}

	private static void getActivityFinnish() {
		((Activity) mContext).finish();
	}

	public static void doLogout() {
		
		
		
		
		
		
		
		
		
		
		
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

		// Setting Dialog Title
		alertDialog.setTitle(SessionManager.getClubName(activity));

		// Setting Dialog Message
		alertDialog.setMessage("Are you sure  you want to Logout?");

		;

		// Setting Positive "Yes" Button
		alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				getCleanPopBackStack();
			// Write your code here to invoke YES event
				dialog.dismiss();
			}
		});

		// Setting Negative "NO" Button
		alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,	int which) {
			// Write your code here to invoke NO event
			
			dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();
		
		
		/*Utill.showDialgonactivity("Do you want to Logout?",activity);
		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(R.layout.do_logout_xml);
		Button logout_yes = (Button) dialog.findViewById(R.id.logout_yes);
		Button logout_no = (Button) dialog.findViewById(R.id.logout_no);

		logout_yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		});

		logout_no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		//dialog.show();
*/	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
		fragment.onActivityResult(requestCode, resultCode, data);
	}

	// this method is used for go back from one to another screen.
	public static void backStackPress() {
		mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	@Override
	protected void onNewIntent(Intent intent) {

		if (switchAction != DirectorDashboardFragment_id)
		{
			return;
		}


		String userId = SessionManager.getUser_id(mContext);
		int extraString = intent.getIntExtra("notificationId",0);
		Bundle extra = intent.getExtras();
		if (extra != null) {
			int notificationId = extra.getInt("notificationId");

			if (userId != null) {
				if (notificationId != 0) {

					
					NotificationsFragment fragment = new NotificationsFragment(); 
					
					getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, NotificationsFragment_tag).addToBackStack(null).commit();
					
					
					
				} else {
					getCleanPopBackStack();
				}
			} else {
				finish();
				Intent intent1 = new Intent(DirectorFragmentManageActivity.this, LoginActivity.class);
				startActivity(intent1);
			}
		}
		super.onNewIntent(intent);
	}

	@Override
	protected void onResume() {
		String userId = SessionManager.getUser_id(mContext);
		int extraString = getIntent().getIntExtra("notificationId",1);
		Bundle extra = getIntent().getExtras();
		if (extra != null && extraString!=1) {
			int notificationId = extra.getInt("notificationId");

			if (userId != null)
			{
				if (notificationId != 0) {
					switchFragment(NotificationsFragment_id);
				} else {
					getCleanPopBackStack();
				}
			} else 
			{
				finish();
				Intent intent1 = new Intent(DirectorFragmentManageActivity.this, LoginActivity.class);
				startActivity(intent1);
			}
		}
		
		if (DirectorFragmentManageActivity.actionbar_titletext != null) 
		{
			
		//	DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.director_dashboard));
			
			/*if (SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_DIRECTOR))
			{
				//DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.director_dashboard));
					
			}
			else 
			{
				if (SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER)) 
				{
			//	DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.member_dashboard));
				 }	
			}*/
				

		}
		super.onResume();
	}


	boolean isDialogShowing ;

	ShowUserMessage showUserMessage ;
	public void callAsynchronousTask() {

		final Handler handler = new Handler();
		Timer timer = new Timer();
		TimerTask doAsynchronousTask = new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						try {
							GlobalValues.getModelManagerObj(mContext).getNotificationCount(new ModelManagerListener() {

								@Override
								public void onSuccess(String json) {
									// TODO Auto-generated method stub

                                  Log.e("json" , json+"");


									try
									{
										JSONObject jsonObject = new JSONObject(json);
										double appVersion = Double.parseDouble(jsonObject.getString("a_version"));

											if (AppConstants.currentVersion != appVersion && isDialogShowing  == false)
										{
											isDialogShowing = true;
											String versionType = jsonObject.getString("a_type") ;
											showUserMessage = new ShowUserMessage(DirectorFragmentManageActivity.this);

											if (versionType.equals("1"))
											{
												//showUserMessage.showVersionControlDialog(DirectorFragmentManageActivity.this);

											}
											else
											{
												/*showUserMessage.showDialogBoxWithYesNoButtonForVersionControl(DirectorFragmentManageActivity.this, new DialogBoxButtonListner() {
													@Override
													public void onYesButtonClick(DialogInterface dialog) {

													}

													@Override
													public void onNoButtonClick(DialogInterface dialog) {
														super.onNoButtonClick(dialog);




													}
												});*/

											}
											return;
										}
									}
									catch (Exception e)
									{

									}
								}

								@Override
								public void onError(String msg) {
									// TODO Auto-generated method stub

								}
							},DirectorFragmentManageActivity. this);
						} catch (Exception e) {

						}
					}
				});
			}
		};
		timer.schedule(doAsynchronousTask, 0, 3000); // execute in every 50000
		// ms

	}



	public static void showUploadButton() {
		uploadNewsOrEditProfile.setVisibility(View.VISIBLE);
		logoutButton.setVisibility(View.GONE);
	}

	public static void hideUploadButton() {
		uploadNewsOrEditProfile.setVisibility(View.GONE);
		logoutButton.setVisibility(View.GONE);
	}

	public static void showLogoutButton() {
		//uploadNewsOrEditProfile.setVisibility(View.GONE);
		logoutButton.setVisibility(View.GONE);
	}

	public static void hideLogoutButton() {
		//uploadNewsOrEditProfile.setVisibility(View.GONE);
		logoutButton.setVisibility(View.GONE);
	}

   ProgressDialog progressDialog ;

	public void  SwitcFragmentToUserInfoActivity(String userId)
    {



       if (userId.equals(SessionManager.getUser_id(getApplicationContext())))
        {


            switchFragment (EditMyProfileFragment_id);
            return;
        }





        progressDialog = new ProgressDialog(DirectorFragmentManageActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("user_club_id", SessionManager.getUser_Club_id(DirectorFragmentManageActivity.this));
        params.put("user_id", userId);
		params.put("user_type" , SessionManager.getUser_type(getApplicationContext()));
        ModelManager modelManager = new ModelManager(DirectorFragmentManageActivity.this);
        modelManager.getMemberDetail(DirectorFragmentManageActivity.this, params, new OnServerRespondingListener(DirectorFragmentManageActivity.this) {


            @Override
            public void onNetWorkError() {
                super.onNetWorkError();
                Utill.showDialg("NetWork error!" , DirectorFragmentManageActivity.this);
            }

            @Override
            public void onSuccess(JSONObject jsonObject) {
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(MemberListBean memberListBean) {
                super.onSuccess(memberListBean);
                progressDialog.dismiss();
                UserProfileActivity userProfileActivity = new UserProfileActivity();

                if (memberListBean.getUser_id().equals(SessionManager.getUser_id(getApplicationContext())))
                {
                    userProfileActivity.setInatanse(memberListBean ,1);
                }
                else
                {
                    userProfileActivity.setInatanse(memberListBean ,2);

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame , userProfileActivity ,"userProfileActivity").addToBackStack("").commit();


            }
        });



    }


	public void  SwitcFragmentToUserInfoActivityWithAdd(String userId ,final FragmentBackResponseListener fragmentBackListener)
	{


		Utill.hideKeybord(DirectorFragmentManageActivity.this);

		if (userId.equals(SessionManager.getUser_id(getApplicationContext())))
		{


			switchFragment (EditMyProfileFragment_id);
			return;
		}





		progressDialog = new ProgressDialog(DirectorFragmentManageActivity.this);
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);
		progressDialog.show();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_club_id", SessionManager.getUser_Club_id(DirectorFragmentManageActivity.this));
		params.put("user_id", userId);
		params.put("user_type" , SessionManager.getUser_type(getApplicationContext()));
		ModelManager modelManager = new ModelManager(DirectorFragmentManageActivity.this);
		modelManager.getMemberDetail(DirectorFragmentManageActivity.this, params, new OnServerRespondingListener(DirectorFragmentManageActivity.this) {


			@Override
			public void onNetWorkError() {
				super.onNetWorkError();
				Utill.showDialg("NetWork error!" , DirectorFragmentManageActivity.this);
			}

			@Override
			public void onSuccess(JSONObject jsonObject) {
				progressDialog.dismiss();
			}

			@Override
			public void onSuccess(MemberListBean memberListBean) {
				super.onSuccess(memberListBean);
				progressDialog.dismiss();
				UserProfileActivity userProfileActivity = new UserProfileActivity();
                Bundle bundle = new Bundle();
				bundle.putSerializable("fragmentBackListenerabc" ,fragmentBackListener);
				userProfileActivity.setArguments(bundle);
				if (memberListBean.getUser_id().equals(SessionManager.getUser_id(getApplicationContext())))
				{
					userProfileActivity.setInatanse(memberListBean ,1);
				}
				else
				{
					userProfileActivity.setInatanse(memberListBean ,2);

				}
				getSupportFragmentManager().beginTransaction().add(R.id.content_frame , userProfileActivity ,"userProfileActivity").addToBackStack("").commit();


			}
		});



	}




}
