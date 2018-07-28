package com.clubscaddy.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clubscaddy.ClubListActivity;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.instragram.InstagramSession;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.DialogBoxButtonListner;
import com.clubscaddy.R;
import com.clubscaddy.Bean.user_preferences_bean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.instragram.InstagramApp;
import com.clubscaddy.utility.SqlListe;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class DirectorSettingFragment extends Fragment {
	public static final int MY_PROFILE = 1;
	public static final int MY_PREFERENCES = 2;
	public static final int ADVANCE = 3;
	public static int CURRENT_SCREEN = MY_PROFILE;

	//my profile View
	TextView ChangePassworTV,MyProfilTV;

	private InstagramApp mApp;
	TextView change_password_tv;
	TextView privacy_option_btn;
	TextView contact_support;
	TextView manage_admin;
	TextView manage_club_logo;
	TextView change_club_message;
	TextView manage_support_type;
	TextView manage_autorenew;
	TextView change_club_tv;
	View view1 ,view2 ,view3 ,view4,view5;
	View change_club_view ;
	SqlListe sqlListe ;
	TextView logout_tv;
	TextView manage_coaches_tv;
	TextView no_show_report_tv;
	TextView manage_reservation_tv;

    SessionManager sessionManager ;
	TextView classification_setting_tv;
	View classification_setting_view;
	TextView contact_instragm_tv;

	ShowUserMessage showUserMessage ;

	View no_show_report_tv_line ,manage_coaches_tv_line ,manage_admin_line;


	// setting preferences view
	LinearLayout doNotSendAnyNotificationLL, doNotShowMyNumberLL, doNotShowMyEmailLL, doNotShowMyRatingLl, doNotShowMyWinPerLL, doNotShowResultLL;
	CheckBox doNotSendAnyNotificationCB, doNotShowMyNumberCB, doNotShowMyEmailCB, doNotShowMyRatingCB, doNotShowMyWinPerCB, doNotShowResultCB;

	String Tag = getClass().getName();
	private static FragmentManager mFragmentManager;
	private static Fragment mFragment;
	Context mContext;
	DirectorFragmentManageActivity directorDashBoard;





	public static Fragment getInstance(FragmentManager mFrgManager) {
		if (mFragment == null) {
			mFragment = new DirectorSettingFragment();
		}
		return mFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.e(Tag, "onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(Tag, "onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.director_setting_frg, container, false);
		Log.e(Tag, "onCreateView");
		mContext = getActivity();
		directorDashBoard = (DirectorFragmentManageActivity) mContext;
		sqlListe = new SqlListe(getActivity());
		sessionManager = new SessionManager(getActivity());

		TextView vrsion_code_tv = (TextView) rootView.findViewById(R.id.vrsion_code_tv);
		vrsion_code_tv.setText("Version "+AppConstants.currentVersion1);


		showUserMessage = new ShowUserMessage(getActivity());

		mApp = new InstagramApp(getActivity(), AppConstants.CLIENT_ID,
				AppConstants.CLIENT_SECRET, AppConstants.CALLBACK_URL);

		if (DirectorFragmentManageActivity.actionbar_titletext != null) {
			DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.action_settings));
		}

		if(DirectorFragmentManageActivity.backButton!=null){
			DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
			DirectorFragmentManageActivity.showBackButton();
		}




		manage_reservation_tv  = (TextView) rootView.findViewById(R.id.manage_reservation_tv);
		no_show_report_tv = (TextView) rootView.findViewById(R.id.no_show_report_tv);		change_password_tv = (TextView) rootView.findViewById(R.id.change_password_tv);
		privacy_option_btn = (TextView) rootView.findViewById(R.id.privacy_option_btn);
		contact_support = (TextView) rootView.findViewById(R.id.contact_support);
		manage_admin = (TextView) rootView.findViewById(R.id.manage_admin);
		manage_club_logo = (TextView) rootView.findViewById(R.id.manage_club_logo);
		change_club_message = (TextView) rootView.findViewById(R.id.change_club_message);
		manage_support_type = (TextView) rootView.findViewById(R.id.manage_support_type);

		manage_autorenew =(TextView) rootView.findViewById(R.id.manage_autorenew);
		manage_coaches_tv = (TextView) rootView.findViewById(R.id.manage_coaches_tv);

		logout_tv =(TextView) rootView.findViewById(R.id.logout_tv);

		contact_instragm_tv = (TextView) rootView.findViewById(R.id.contact_instragm_tv);
		//logout_tv
		change_club_tv =(TextView) rootView.findViewById(R.id.change_club_tv);
		change_club_view =(View) rootView.findViewById(R.id.change_club_view);

		classification_setting_tv =(TextView) rootView.findViewById(R.id.classification_setting_tv);
		classification_setting_view =(View) rootView.findViewById(R.id.classification_setting_view);


		no_show_report_tv_line = rootView.findViewById(R.id.no_show_report_tv_line);

		manage_coaches_tv_line = rootView.findViewById(R.id.manage_coaches_tv_line);

		manage_admin_line = rootView.findViewById(R.id.manage_admin_line);



		if (sessionManager.isInStragramStatus(getActivity()))
		{
			contact_instragm_tv.setText("Disconnect with instagram");
		}
		else
		{
			contact_instragm_tv.setText("Connect with instagram");
		}


		if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER) == false)
		{
			classification_setting_tv.setVisibility(View.VISIBLE);
			classification_setting_view.setVisibility(View.VISIBLE);
		}
		else
		{
			classification_setting_tv.setVisibility(View.GONE);
			classification_setting_view.setVisibility(View.GONE);
		}


		if(sqlListe.getAllClub().size() == 1)
		{
			change_club_tv.setVisibility(View.GONE);
			change_club_view.setVisibility(View.GONE);
		}


		view1 = rootView.findViewById(R.id.view1);;
		view2 =rootView.findViewById(R.id.view2);
		view3=rootView.findViewById(R.id.view3);
		view4 = rootView.findViewById(R.id.view4);
		view5 =rootView.findViewById(R.id.view5);


		change_password_tv.setOnClickListener(onclicklistener);
		privacy_option_btn.setOnClickListener(onclicklistener);
		contact_support.setOnClickListener(onclicklistener);
		manage_admin.setOnClickListener(onclicklistener);
		manage_club_logo.setOnClickListener(onclicklistener);
		change_club_message.setOnClickListener(onclicklistener);
		manage_support_type.setOnClickListener(onclicklistener);
		manage_autorenew.setOnClickListener(onclicklistener);
		contact_instragm_tv.setOnClickListener(onclicklistener);

		change_club_tv.setOnClickListener(onclicklistener);
		logout_tv.setOnClickListener(onclicklistener);
		manage_coaches_tv.setOnClickListener(onclicklistener);

		manage_reservation_tv.setOnClickListener(onclicklistener);

		classification_setting_tv.setOnClickListener(onclicklistener);

		no_show_report_tv.setOnClickListener(onclicklistener);

		if(SessionManager.getClubType(getActivity()).equals("3"))
		{
			manage_autorenew.setVisibility(View.GONE);
		}

		if(SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_DIRECTOR) == true || SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER) == true)
		{
			privacy_option_btn.setVisibility(View.VISIBLE);
			//	manage_autorenew.setVisibility(View.GONE);
		}
		else
		{
			privacy_option_btn.setVisibility(View.GONE);
		}


		if(SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_DIRECTOR) == false)
		{
			manage_coaches_tv.setVisibility(View.GONE);
			manage_coaches_tv_line.setVisibility(View.GONE);
			//	manage_autorenew.setVisibility(View.GONE);
		}
		else
		{
			manage_coaches_tv.setVisibility(View.VISIBLE);
			manage_coaches_tv_line.setVisibility(View.VISIBLE);

		}
		if(SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_DIRECTOR) == true || SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MOBILE_ADMIN) == true)
		{
			no_show_report_tv.setVisibility(View.VISIBLE);
			no_show_report_tv_line.setVisibility(View.VISIBLE);

		}
		else
		{
			no_show_report_tv.setVisibility(View.GONE);
			no_show_report_tv_line.setVisibility(View.GONE);

		}

		privacy_option_btn.setVisibility(View.VISIBLE);
		if(SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MOBILE_ADMIN))
		{
			manage_support_type	.setVisibility(View.GONE);
			manage_admin.setVisibility(View.GONE);
			//	view1.setVisibility(View.GONE);
			//view4.setVisibility(View.GONE);
			manage_admin_line.setVisibility(View.GONE);

		}

		if(SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER) || SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_COACH))
		{
			manage_support_type	.setVisibility(View.GONE);
			manage_admin.setVisibility(View.GONE);
			change_club_message.setVisibility(View.GONE);
			manage_club_logo.setVisibility(View.GONE);
			//view1.setVisibility(View.GONE);
			view2.setVisibility(View.GONE);
			view3.setVisibility(View.GONE);
			view4.setVisibility(View.GONE);
		}


		return rootView;
	}





	// setting advance view
	TextView manageCoachesTV, manageAdminsTV,manageMember, manageClubLogoTV, manageCourtReservationTV, generalSettings,manageClubMessage;
	TextView manage_sport_type ;
	TextView manage_rating;
	void initAdvanceView(View view) {
		manageCoachesTV = (TextView) view.findViewById(R.id.manage_coaches_tv);
		manageAdminsTV = (TextView) view.findViewById(R.id.manage_admin_tv);
		manageClubLogoTV = (TextView) view.findViewById(R.id.manage_clublogo_tv);
		manageCourtReservationTV = (TextView) view.findViewById(R.id.manage_court_reservation_tv);
		generalSettings = (TextView) view.findViewById(R.id.general_setting);
		manageMember = (TextView) view.findViewById(R.id.manage_member_tv);
		manageClubMessage = (TextView) view.findViewById(R.id.manage_club_message);
		manage_sport_type = (TextView) view.findViewById(R.id.manage_sport_type);
		manage_rating = (TextView) view.findViewById(R.id.manage_rating);

		manageCoachesTV.setOnClickListener(advanceClickListener);
		manageAdminsTV.setOnClickListener(advanceClickListener);
		manageClubLogoTV.setOnClickListener(advanceClickListener);
		manageCourtReservationTV.setOnClickListener(advanceClickListener);
		generalSettings.setOnClickListener(advanceClickListener);
		manageMember.setOnClickListener(advanceClickListener);
		manageClubMessage.setOnClickListener(advanceClickListener);
		manage_sport_type.setOnClickListener(advanceClickListener);
		manage_rating.setOnClickListener(advanceClickListener);
	}

	OnClickListener advanceClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int viewId = v.getId();
			switch (viewId) {
				case R.id.manage_coaches_tv:
					DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.CoachListFragment_id);
					break;
				case R.id.manage_admin_tv:
					DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.AdminListFragment_id);
					break;
				case R.id.manage_member_tv:
					DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.MembersListFragment_id);
					break;
				case R.id.manage_clublogo_tv:
					DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.UpdateClubLogoFragment_id);
					break;
				case R.id.manage_court_reservation_tv:
					DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.ManageCourtReservationFragment_id);
					break;
				case R.id.general_setting:
				/*DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.ShowReservationFragment_id);*/
					break;
				case R.id.manage_club_message:
					DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.ChangeClubMessaegFragment_id);
					break;

				case R.id.manage_sport_type:
					DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.manage_sport_type_id);
					break;
				case R.id.manage_rating:
					DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.manage_rating_id);
					break;
				default:
					break;
			}

		}
	};


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e(Tag, "onActivityCreated");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.e(Tag, "onStart");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(Tag, "onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.e(Tag, "onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.e(Tag, "onStop");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.e(Tag, "onDestroyView");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e(Tag, "onDestrou");
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}









	public class setPreferenceListener {
		public void onSuccess(String msg) {
			//ShowUserMessage.showUserMessage(mContext, msg);
		}

		public void onError(String msg) {

		}
	}

	user_preferences_bean preferenceBean;

	public class preferenceListener {
		public void onSuccess(String msg, user_preferences_bean preBean) {
			preferenceBean = preBean;
			setSettingCheack();

		}

		public void onError(String msg) {

		}
	}

	void setSettingCheack() {
		if (preferenceBean.getUser_preferences_send_notification().equalsIgnoreCase("1"))
			doNotSendAnyNotificationCB.setChecked(true);
		else
			doNotSendAnyNotificationCB.setChecked(false);

		if (preferenceBean.getUser_preferences_show_phone().equalsIgnoreCase("1"))
			doNotShowMyNumberCB.setChecked(true);
		else
			doNotShowMyNumberCB.setChecked(false);

		if (preferenceBean.getUser_preferences_show_email().equalsIgnoreCase("1"))
			doNotShowMyEmailCB.setChecked(true);
		else
			doNotShowMyEmailCB.setChecked(false);

		if (preferenceBean.getUser_preferences_show_rating().equalsIgnoreCase("1"))
			doNotShowMyRatingCB.setChecked(true);
		else
			doNotShowMyRatingCB.setChecked(false);

		if (preferenceBean.getUser_preferences_show_win().equalsIgnoreCase("1"))
			doNotShowMyWinPerCB.setChecked(true);
		else
			doNotShowMyWinPerCB.setChecked(false);

		if (preferenceBean.getUser_preferences_show_result().equalsIgnoreCase("1"))
			doNotShowResultCB.setChecked(true);
		else
			doNotShowResultCB.setChecked(false);

	}

	void setPreferenceValues() {
		if (preferenceBean != null) {

		}
	}


	OnClickListener onclicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			switch(v.getId())
			{
				case R.id.change_password_tv:

					DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.ChangePasswordFragment_id);

					break;
				case R.id.privacy_option_btn:



					PrivacyOption privacyfragment = new PrivacyOption();
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, privacyfragment, "fragment").addToBackStack(null).commit();

					//DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.pr);

					break;
				case R.id.contact_support:

					ContactSupporortFRagment fragment = new ContactSupporortFRagment();
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "").addToBackStack(null).commit();
					break;
				case R.id.manage_admin:

					ManageADminFragment fragment1 = new ManageADminFragment();
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment1, "").addToBackStack(null).commit();


					break;
				case R.id.manage_club_logo:
					DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.UpdateClubLogoFragment_id);

					break;
				case R.id.change_club_message:
					DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.ChangeClubMessaegFragment_id);

					break;
				case R.id.manage_support_type:
					DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.manage_sport_type_id);

					break;
				case R.id.manage_autorenew:
					//DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.manage_sport_type_id);
					AutoRenewFragment fragment2 = new AutoRenewFragment();
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment2, "").addToBackStack(null).commit();

					break;


				case R.id.change_club_tv:
					//DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.manage_sport_type_id);
					Intent intent = new Intent(getActivity(), ClubListActivity.class);
					intent.putExtra("activityAction" ,2);
					startActivity(intent);


					break;





				case R.id.manage_reservation_tv:
					//DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.manage_sport_type_id);

					ManageReservationFragment manageReservationFragment = new ManageReservationFragment();
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, manageReservationFragment, "").addToBackStack(null).commit();

					break;



				case R.id.logout_tv:
					//DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.manage_sport_type_id);

					DirectorFragmentManageActivity.doLogout();
					break;

				case R.id.manage_coaches_tv:
					//DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.manage_sport_type_id);

					AddCoachesFragment addCoachesFragment = new AddCoachesFragment();
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame ,addCoachesFragment ,"").addToBackStack(null).commit();
					break;


				case R.id.classification_setting_tv:
					//DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.manage_sport_type_id);

					ClassifiedSettingFragment classifiedSettingFragment = new ClassifiedSettingFragment();
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame ,classifiedSettingFragment ,"classifiedSettingFragment").addToBackStack(null).commit();
					break;

				case R.id.no_show_report_tv:
					//DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.manage_sport_type_id);

					NoShowReportFragment noShowReportFragment = new NoShowReportFragment();
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame ,noShowReportFragment ,"").addToBackStack(null).commit();
					break;







				case R.id.contact_instragm_tv:
					//DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.manage_sport_type_id);


						connectORDisconnectInstram();

									break;
				//
			}



		}
	};




	OnClickListener addToBack = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				Utill.hideKeybord(getActivity());
				getActivity().getSupportFragmentManager().popBackStack();
			} catch (Exception e) {
				ShowUserMessage.showUserMessage(mContext, e.toString());
			}
		}
	};


	public  void connectORDisconnectInstram()
	{


		if (sessionManager.isInStragramStatus(getActivity()))
		{


			showUserMessage.showDialogBoxWithYesNoButton("Are you sure you want to disconnect from instagram?", new DialogBoxButtonListner() {
				@Override
				public void onYesButtonClick(DialogInterface dialog) {

					mApp.resetAccessToken();
					updateStatus("");
					contact_instragm_tv.setText("Connect with instagram");
				}
			});
		}
		else
		{
			mApp = new InstagramApp(getActivity(), AppConstants.CLIENT_ID,
					AppConstants.CLIENT_SECRET, AppConstants.CALLBACK_URL);
			mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

				@Override
				public void onSuccess() {
					// tvSummary.setText("Connected as " + mApp.getUserName());
					contact_instragm_tv.setText("Disconnect with instagram");
					InstagramSession instagramSession = new InstagramSession(getActivity());
					updateStatus(instagramSession.getAccessToken());




				}

				@Override
				public void onFail(String error) {
					Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT)
							.show();
				}
			});
			mApp.authorize();
		}







	}

	public void  updateStatus(String insta_key)
	{
		HttpRequest httpRequest = new HttpRequest(getActivity());
		HashMap<String , Object> param = new HashMap<String, Object>();
		param.put("insta_key" ,insta_key);
		param.put("user_email" ,SessionManager.getUser_email(getActivity()));

		httpRequest.getResponse(getActivity(), WebService.add_insta_key, param, new OnServerRespondingListener(getActivity()) {
			@Override
			public void onSuccess(JSONObject jsonObject)
			{
				Utill.showDialg(jsonObject.optString("message")+"" , getActivity());

			}
		});
	}


}

