package com.clubscaddy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.androidquery.AQuery;

import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.custumview.ExpandableHeightListView;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.MemberListAdapter;
import com.clubscaddy.Bean.DropInBean;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.custumview.InternetConnection;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONObject;

public class MatchMakerDetailsFragment extends Fragment
{
	
	TextView total_invited_tv;
	TextView joined_tv;
	TextView accepted_tv;
	TextView rejected_tv;
	TextView declinedd_tv;
	TextView status_tv;
	TextView date_tv;
	TextView time_tv;
	TextView format_tv;
	TextView number_of_player_tv;
	View view ;
	ListView member_list_view;
	String dropin_id;
AQuery aQuery ;
	ScrollView scollview;
TextView delete_btn;
	TextView message_tv;
ArrayList<DropInBean>dropInList;
   public void setInstanse(String dropin_id)
   {
	this.dropin_id = dropin_id;   
   }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		view = inflater.inflate(R.layout.matchmaker_details_layout, null);

		scollview = (ScrollView) view.findViewById(R.id.scollview);

		if (DirectorFragmentManageActivity.actionbar_titletext != null) {
			DirectorFragmentManageActivity.updateTitle("View Match Maker");
		}

		total_invited_tv = (TextView) view.findViewById(R.id.total_invited_tv);
		
		joined_tv = (TextView) view.findViewById(R.id.joined_tv);
		
		accepted_tv = (TextView) view.findViewById(R.id.accepted_tv);
		
		rejected_tv = (TextView) view.findViewById(R.id.rejected_tv);
		
		declinedd_tv = (TextView) view.findViewById(R.id.declinedd_tv);
		
		status_tv = (TextView) view.findViewById(R.id.status_tv);
		
		date_tv = (TextView) view.findViewById(R.id.date_tv);
		
		time_tv = (TextView) view.findViewById(R.id.time_tv);
		
		format_tv = (TextView) view.findViewById(R.id.format_tv);


		message_tv = (TextView) view.findViewById(R.id.message_tv);

		
		number_of_player_tv = (TextView) view.findViewById(R.id.number_of_player_tv);
		
		member_list_view = (ListView) view.findViewById(R.id.member_list_view);
		
		delete_btn = (TextView) view.findViewById(R.id.delete_btn);

		if (DirectorFragmentManageActivity.backButton != null) {
			DirectorFragmentManageActivity.showBackButton();
			DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
		}



		delete_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

				// Setting Dialog Title
				alertDialog.setTitle(SessionManager.getClubName(getActivity()));

				// Setting Dialog Message
				alertDialog.setMessage("Are you sure you want delete this MatchMaker?");

				// Setting Icon to Dialog
				

				// Setting Positive "Yes" Button
				alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {

						try
						
						{
							deleteDropIns(dropInList.get(0));	
						}
						catch(Exception e)
						{
							
						}
						
						
						dialog.cancel();
					// Write your code here to invoke YES event
					//Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
					}
				});

				// Setting Negative "NO" Button
				alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,	int which) {
					// Write your code here to invoke NO event
					//Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
					dialog.cancel();
					}
				});

				// Showing Alert Message
				
				
				
				if(InternetConnection.isInternetOn(getActivity()))
				{
					
					
					alertDialog.show();
					
					
				}
				else
				{
				Utill.showDialg(getString(R.string.check_internet_connection), getActivity());	
				}	
			}
		});
		
		aQuery = new AQuery(getActivity());
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("dropin_id", dropin_id);
		
		if(InternetConnection.isInternetOn(getActivity()))
		{
			
			Utill.showProgress(getActivity());
			GlobalValues.getModelManagerObj(getActivity()).getDropInList(new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					
					Utill.hideProgress();
					
					Log.e("json", json+"");
					dropInList =	JsonUtility.parseDropInListItem(json);
					
					
					total_invited_tv.setText(dropInList.get(0).getTotal_invited_user()+dropInList.get(0).getTotal_joined_user()+dropInList.get(0).getTotal_acceptedback_user()+dropInList.get(0).getTotal_rejected_user()+dropInList.get(0).getDeclined_user()+"");
					joined_tv.setText(dropInList.get(0).getTotal_joined_user()+dropInList.get(0).getTotal_acceptedback_user()+dropInList.get(0).getTotal_rejected_user()+"");
					accepted_tv.setText(dropInList.get(0).getTotal_acceptedback_user()+"");
					rejected_tv.setText(dropInList.get(0).getTotal_rejected_user()+dropInList.get(0).getDeclined_user()+"");
					//declinedd_tv.setText(dropInList.get(0).getDeclined_user()+"");

					message_tv.setText(dropInList.get(0).getDropin_desc()+"");
					status_tv.setText("Status : "+dropInList.get(0).getOpenstatus()+"");
					
					date_tv.setText("Date     : "+dropInList.get(0).getDropin_date());
					
					time_tv.setText("Time     : "+dropInList.get(0).getDropin_time());
					
					
					if(dropInList.get(0).getDropin_formate().equals("1"))
					{
						format_tv.setText("Format : Single");	
					}
					else
					{
						format_tv.setText("Format : Double");	
					}
					//Toast.makeText(getActivity(), "size  "+dropInList.get(0).getMemeberList().size(), 1).show();
					
					number_of_player_tv.setText("Number of players needed : "+dropInList.get(0).getNum_of_players());
					
					//scollview.scrollTo(0,0);

				//	scollview.fullScroll(View.FOCUS_UP);//if you move at the end of the scroll

					//scollview.pageScroll(View.FOCUS_UP);

					try
					{
						member_list_view.setAdapter(new MemberListAdapter(getActivity(), dropInList.get(0).getMemeberList()));
						ExpandableHeightListView.getListViewSize(member_list_view);

						scollview.postDelayed(new Runnable() {
							@Override
							public void run() {
								scollview.fullScroll(ScrollView.FOCUS_UP);
							}
						}, 100);
					}
					catch (Exception e)
					{

					}

					
				/*	dropInList = 
					Utill.hideProgress();
					ExpandableListAdapter adapter = new ExpandableListAdapter(mContext, dropInList,new onClickDeleteDropIn());
					groupListView.setVisibility(View.GONE);
					dropInExpandableListView.setVisibility(View.VISIBLE);
					dropInExpandableListView.setAdapter(adapter);
					if (dropInList != null && dropInList.size() >= 1000) {
						addGroup.setVisibility(View.GONE);
					} else {
						addGroup.setVisibility(View.VISIBLE);
					}*/

					Utill.hideProgress();
				}

				@Override
				public void onError(String msg) {
					ShowUserMessage.showUserMessage(getActivity(), msg);
					Utill.hideProgress();
				}
			} ,params);
			
			
		}
		else
		{
		Utill.showDialg(getString(R.string.check_internet_connection), getActivity());	
		}
		
		return view;
	}
	
	void deleteDropIns(DropInBean bean) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dropin_id",bean.getDropin_id());
		params.put("dropin_user_id",SessionManager.getUser_id(getActivity()));
		if (Utill.isNetworkAvailable(getActivity())) {
			Utill.showProgress(getActivity());
			GlobalValues.getModelManagerObj(getActivity()).deleteDropIn(params, new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					Utill.hideProgress();
					try
					{
						JSONObject jsonObject = new JSONObject(json);

						ShowUserMessage.showMessageForFragmeneWithBack(getActivity() , jsonObject.getString("message"));
					}
					catch (Exception e)
					{

					}

				}

				@Override
				public void onError(String msg) {
					ShowUserMessage.showUserMessage(getActivity(), msg);
					Utill.hideProgress();
				}
			});
		} else {
			Utill.showNetworkError(getActivity());
		}

	}


	View.OnClickListener addToBack = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				DirectorFragmentManageActivity.popBackStackFragment();
			} catch (Exception e) {
				ShowUserMessage.showUserMessage(getActivity(), e.toString());
			}
		}
	};

	
	
	
}
