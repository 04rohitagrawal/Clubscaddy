package com.clubscaddy.fragment;

import java.util.Calendar;

import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.SessionManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AutoRenewFragment extends Fragment
{
	TextView auto_renew_date;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.auto_renew_date, null);


		DirectorFragmentManageActivity.updateTitle("App purchase status");
		auto_renew_date = (TextView) view.findViewById(R.id.auto_renew_date);

		try
		{
			auto_renew_date.setText(AppConstants.getAppDate(AppConstants.getAutorenewdate()));

		}
		catch (Exception e)
		{

		}

		if(SessionManager.getClubType(getActivity()).equals("1"))
		{
			if(SessionManager.getUserLoginApp(getActivity()).equals("1"))
			{

				Calendar calender = Calendar.getInstance();
				Calendar today = Calendar.getInstance();

				calender.set(Calendar.DATE, Integer.parseInt(SessionManager.getReminderdays(getActivity()).split(" ")[1]));
				calender.set(Calendar.YEAR, Integer.parseInt(SessionManager.getReminderdays(getActivity()).split(" ")[3]));
				calender.set(Calendar.MONTH,AppConstants.getMonthIndex(SessionManager.getReminderdays(getActivity()).split(" ")[2])-1);
				Log.e("calender", calender.get(Calendar.DATE)+" "+calender.get(Calendar.MONTH)+" "+calender.get(Calendar.YEAR));

				//63625478399999
				long diff = calender.getTimeInMillis() - today.getTimeInMillis();

				AppConstants.setAutorenewdate(calender);
				long days = diff / (24 * 60 * 60 * 1000);
			//	auto_renew_date.setText("You are using the app in trial period. Your trial period will expire on "+SessionManager.getReminderdays(getActivity())+" After that you need to purchase a subcription to use this app.");
				auto_renew_date.setText("Your trial period of this app expires on "+SessionManager.getReminderdays(getActivity()));

				/* movetonextlogin("Your trial period of this app expires on "+AppConstants.getAppDate(calender));


				return;*/
			}	 
			else
			{

				SessionManager sessionManager = new SessionManager();
				Calendar purchasedate =	 AppConstants.getCalenderFromAppDate(sessionManager.getAppPurchaseDate(getActivity()));
				purchasedate.add(Calendar.MONTH, sessionManager.getTimePeriode(getActivity()));
				auto_renew_date.setText("You purchased this app for "+sessionManager.getTimePeriode(getActivity())+ " month on "+sessionManager.getAppPurchaseDate(getActivity())+" .Your purchase of this app will be renewed on "+AppConstants.getAppDate(purchasedate));

			}
		}
		if(SessionManager.getClubType(getActivity()).equals("2"))
		{
			auto_renew_date.setText("Your trial period of this app expires on "+SessionManager.getReminderdays(getActivity()));

			//auto_renew_date.setText("You are using the app in demo period. App purchase is not applicable to you . Your demo period will expire on "+SessionManager.getReminderdays(getActivity()));
		}
		if(SessionManager.getClubType(getActivity()).equals("3"))
		{

		}
		return view;
	}


}
