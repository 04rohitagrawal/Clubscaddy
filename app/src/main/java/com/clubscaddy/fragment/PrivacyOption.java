package com.clubscaddy.fragment;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.R;
import com.clubscaddy.Bean.user_preferences_bean;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.Server.WebService;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;


public class PrivacyOption extends Fragment
{
	HttpRequest httpRequest ;

	LinearLayout doNotSendAnyNotificationLL, doNotShowMyNumberLL, doNotShowMyEmailLL, doNotShowMyRatingLl, doNotShowMyWinPerLL, doNotShowResultLL;
	CheckBox doNotSendAnyNotificationCB, doNotShowMyNumberCB, doNotShowMyEmailCB, doNotShowMyRatingCB, doNotShowMyWinPerCB, doNotShowResultCB;

 @Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	// LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewMyProfile = inflater.inflate(R.layout.setting_preferences, null);
		DirectorFragmentManageActivity.updateTitle("Privacy Option");
		
		initPreferences(viewMyProfile);
	return viewMyProfile;
}
 void initPreferences(View view) {
		doNotSendAnyNotificationLL = (LinearLayout) view.findViewById(R.id.do_not_send_me_notification_ll);
		doNotShowMyNumberLL = (LinearLayout) view.findViewById(R.id.do_not_show_my_number_profile_ll);
		doNotShowMyEmailLL = (LinearLayout) view.findViewById(R.id.do_not_show_my_email_profile_ll);
		doNotShowMyRatingLl = (LinearLayout) view.findViewById(R.id.do_not_show_my_ratting_profile_ll);
		doNotShowMyWinPerLL = (LinearLayout) view.findViewById(R.id.do_not_show_my_win_per_profile_ll);
		doNotShowResultLL = (LinearLayout) view.findViewById(R.id.do_not_show_my_match_result_profile_ll);

		doNotSendAnyNotificationCB = (CheckBox) view.findViewById(R.id.do_not_send_me_notification_cheack);
		doNotShowMyNumberCB = (CheckBox) view.findViewById(R.id.do_not_show_my_number_profile_cheack);
		doNotShowMyEmailCB = (CheckBox) view.findViewById(R.id.do_not_show_my_email_profile_cheack);
		doNotShowMyRatingCB = (CheckBox) view.findViewById(R.id.do_not_show_my_ratting_profile_cheack);
		doNotShowMyWinPerCB = (CheckBox) view.findViewById(R.id.do_not_show_my_win_per_profile_cheack);
		doNotShowResultCB = (CheckBox) view.findViewById(R.id.do_not_show_my_match_result_profile_cheack);


	 httpRequest = new HttpRequest(getActivity());

		
		doNotSendAnyNotificationLL.setOnClickListener(PrefrenceClickListener);
		
		
		doNotShowMyEmailLL.setOnClickListener(PrefrenceClickListener);
		doNotShowMyNumberLL.setOnClickListener(PrefrenceClickListener);
		doNotShowMyRatingLl.setOnClickListener(PrefrenceClickListener);
		doNotShowMyWinPerLL.setOnClickListener(PrefrenceClickListener);
		doNotShowResultLL.setOnClickListener(PrefrenceClickListener);

	 doNotShowResultLL.setVisibility(View.GONE);
		getUserPreferences();
		//doNotShowMyWinPerLL.setVisibility(View.GONE);
	}
 void getUserPreferences()
 {
	 HashMap<String , Object> param = new HashMap<>();

	 param.put("user_preferences_user_id", SessionManager.getUser_id(getActivity()));

	 httpRequest.getResponse(getActivity(), WebService.get_user_preferences, param, new OnServerRespondingListener(getActivity()) {
		 @Override
		 public void onSuccess(JSONObject mObj)
		 {
			 try {
				 user_preferences_bean preBean = new user_preferences_bean();
				 if (mObj.optString("status").equals("true")) {
					 preBean.setUser_preferences_id(mObj.optString("user_preferences_id"));
					 preBean.setUser_preferences_user_id(mObj.optString("user_preferences_user_id"));
					 preBean.setUser_preferences_send_notification(mObj.optString("user_preferences_send_notification"));
					 preBean.setUser_preferences_show_email(mObj.optString("user_preferences_show_email"));
					 preBean.setUser_preferences_show_phone(mObj.optString("user_preferences_show_phone"));
					 preBean.setUser_preferences_show_rating(mObj.optString("user_preferences_show_rating"));
					 preBean.setUser_preferences_show_win(mObj.optString("user_preferences_show_win"));
					 preBean.setUser_preferences_show_result(mObj.optString("user_preferences_show_result"));
					 preferenceBean = preBean;
					 setSettingCheack();


				 } else {

					 Utill.showDialg(mObj.getString("msg") , getActivity());

				 }
			 } catch (JSONException e) {

			 }

		 }
	 });




	}
 OnClickListener PrefrenceClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int viewId = v.getId();
			switch (viewId) {
			case R.id.do_not_send_me_notification_ll:
				if (doNotSendAnyNotificationCB.isChecked())
				{
					doNotSendAnyNotificationCB.setChecked(false);
					updateUserPreference("user_preferences_send_notification", "1");
				} 
				else 
				{
					doNotSendAnyNotificationCB.setChecked(true);
					updateUserPreference("user_preferences_send_notification", "0");
				}
				break;
				//
			case R.id.do_not_send_me_notification_cheack:
				if (doNotSendAnyNotificationCB.isChecked()) {
					
					
					updateUserPreference("user_preferences_send_notification", "1");
				} else {
					
					updateUserPreference("user_preferences_send_notification", "0");
				}
				break;
				
				
				
			case R.id.do_not_show_my_number_profile_ll:
				if (doNotShowMyNumberCB.isChecked()) {
					doNotShowMyNumberCB.setChecked(false);
					updateUserPreference("user_preferences_show_phone", "1");
				} else {
					doNotShowMyNumberCB.setChecked(true);
					updateUserPreference("user_preferences_show_phone", "0");
				}
				break;
			
			
			
			
			
			
			case R.id.do_not_show_my_email_profile_ll:
				if (doNotShowMyEmailCB.isChecked())
				{
					doNotShowMyEmailCB.setChecked(false);
					updateUserPreference("user_preferences_show_email", "1");
				} 
				else 
				{
					doNotShowMyEmailCB.setChecked(true);
					updateUserPreference("user_preferences_show_email", "0");
				}
				break;
			case R.id.do_not_show_my_email_profile_cheack:
				if (doNotShowMyEmailCB.isChecked()) {
					
					updateUserPreference("user_preferences_show_email", "0");
				} else {
					
					updateUserPreference("user_preferences_show_email", "1");
				}
				break;
				
				
				
				
			case R.id.do_not_show_my_ratting_profile_ll:
				if (doNotShowMyRatingCB.isChecked()) {
					doNotShowMyRatingCB.setChecked(false);
					updateUserPreference("user_preferences_show_rating", "1");
				} else {
					doNotShowMyRatingCB.setChecked(true);
					updateUserPreference("user_preferences_show_rating", "0");
				}
				break;
			case R.id.do_not_show_my_win_per_profile_ll:
				if (doNotShowMyWinPerCB.isChecked()) {
					doNotShowMyWinPerCB.setChecked(false);
					updateUserPreference("user_preferences_show_win", "1");
				} else {
					doNotShowMyWinPerCB.setChecked(true);
					updateUserPreference("user_preferences_show_win", "0");
				}
				break;
			case R.id.do_not_show_my_match_result_profile_ll:
				if (doNotShowResultCB.isChecked()) {
					doNotShowResultCB.setChecked(false);
					updateUserPreference("user_preferences_show_result", "1");
				} else {
					doNotShowResultCB.setChecked(true);
					updateUserPreference("user_preferences_show_result", "0");
				}
				break;

				
				//do_not_show_my_email_profile_ll
			default:
				break;
			}

		}
	};
	void updateUserPreference(String name, String value)
	{
		HashMap<String , Object> param = new HashMap<>();
		param.put("user_preferences_user_id", SessionManager.getUser_id(getActivity()));
		param.put(name, value);

		httpRequest.getResponse(getActivity(), WebService.set_user_preferences, param, new OnServerRespondingListener(getActivity()) {
			@Override
			public void onSuccess(JSONObject jsonObject)
			{
             Log.e("jsonObject" ,jsonObject+"");
				try
				{
					if (jsonObject.getBoolean("status") == false)
					{
						Utill.showDialg(jsonObject.getString("message") , getActivity());
					}

				}
				catch (Exception e)
				{

				}

			}
		});






	}
	user_preferences_bean preferenceBean;

	void setSettingCheack() {
		if (preferenceBean.getUser_preferences_send_notification().equalsIgnoreCase("1"))
			doNotSendAnyNotificationCB.setChecked(false);
		else
			doNotSendAnyNotificationCB.setChecked(true);

		if (preferenceBean.getUser_preferences_show_phone().equalsIgnoreCase("1"))
			doNotShowMyNumberCB.setChecked(false);
		else
			doNotShowMyNumberCB.setChecked(true);

		if (preferenceBean.getUser_preferences_show_email().equalsIgnoreCase("1"))
			doNotShowMyEmailCB.setChecked(false);
		else
			doNotShowMyEmailCB.setChecked(true);

		if (preferenceBean.getUser_preferences_show_rating().equalsIgnoreCase("1"))
			doNotShowMyRatingCB.setChecked(false);
		else
			doNotShowMyRatingCB.setChecked(true);

		if (preferenceBean.getUser_preferences_show_win().equalsIgnoreCase("1"))
			doNotShowMyWinPerCB.setChecked(false);
		else
			doNotShowMyWinPerCB.setChecked(true);

		if (preferenceBean.getUser_preferences_show_result().equalsIgnoreCase("1"))
			doNotShowResultCB.setChecked(false);
		else
			doNotShowResultCB.setChecked(true);

	}





}
