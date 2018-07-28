package com.clubscaddy.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.clubscaddy.Bean.EventBean;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.ScoreListViewAdapter;
import com.clubscaddy.Bean.ScoreListBean;
import com.clubscaddy.custumview.InternetConnection;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class ScoreListFragment extends Fragment
{
	static ScoreListFragment mFragment;
	ArrayList<ScoreListBean> socre_list;
	View rootview ;
	ListView score_list_view;
	AQuery aQuery ;
	ImageButton add_score_btn;
	String event_id;
	String event_name;
	boolean isMemberasDirector;


	HttpRequest httpRequest ;

	ArrayList<MemberListBean>eventModetororMemberList ;
	EventBean eventBean;

	public void setEventId(String event_id , String event_name , ArrayList<MemberListBean>eventModetororMemberList , EventBean eventBean ,boolean isMemberasDirector)
	{
		this.event_id = event_id ;
		this.isMemberasDirector = isMemberasDirector ;
		this.event_name = event_name ;
		this.eventModetororMemberList = eventModetororMemberList ;
		this.eventBean = eventBean ;
	}


	
	public static Fragment getInstance(FragmentManager mFragManager) 
	{
		if (mFragment == null) 
		{
			mFragment = new ScoreListFragment();
		}
		return mFragment;
	}


	@Override
	public void onDestroyView() {
		super.onDestroyView();
		DirectorFragmentManageActivity.logoutButton.setVisibility(View.GONE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		rootview = inflater.inflate(R.layout.score_list_fragment, null);
		DirectorFragmentManageActivity.updateTitle(event_name+" "+ "Scores");
		aQuery = new AQuery(getActivity());
		
		score_list_view = (ListView) rootview.findViewById(R.id.score_list_view);
		
		add_score_btn = (ImageButton) rootview.findViewById(R.id.add_score_btn);
		httpRequest = new HttpRequest(getActivity());

		DirectorFragmentManageActivity.logoutButton.setImageDrawable(getResources().getDrawable(R.drawable.email_send));

		DirectorFragmentManageActivity.logoutButton.setOnClickListener(sendEmailListener);


		
		add_score_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				FragmentManager ft = getActivity().getSupportFragmentManager();
			CreateScoreFragment 	mFragment = new CreateScoreFragment();
				mFragment.setEventName(event_name ,eventBean);
				if (mFragment != null) {
					ft.beginTransaction().replace(R.id.content_frame, mFragment, "CreatScore_tag").addToBackStack(null).commit();

				}
				
				
				
			}
		});
		
		score_list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
			{
				// TODO Auto-generated method stub
				//DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.CreatScore_id);
				ScoreDetailFrageMent fragment = new ScoreDetailFrageMent();
				fragment.setEventBean(eventBean , eventModetororMemberList);
				ScoreDetailFrageMent.scoreId = socre_list.get(position).getScore_id();
				
				
				getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.content_frame, fragment, "fragment").commit();
				
			}
		});
		
		if(InternetConnection.isInternetOn(getActivity()))
		{
			HashMap<String, String>params = new HashMap<String, String>();
			params.put("event_id", event_id);
			Utill.showProgress(getActivity());
			aQuery.ajax(WebService.eventresult, params, JSONObject.class, new AjaxCallback<JSONObject>()
					{
				@Override
				public void callback(String url, JSONObject object, AjaxStatus status) {
					// TODO Auto-generated method stub
					super.callback(url, object, status);
					Utill.hideProgress();
					if(status.getCode() == 200)
					{
						
						//Utill.showDialg(object.toString(), getActivity());
            socre_list =	JsonUtility.parseScoreList(object.toString());




						if (socre_list.size() != 0)
						{

							if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_DIRECTOR) ||isMemberasDirector)
							{
								DirectorFragmentManageActivity.logoutButton.setVisibility(View.VISIBLE);
							}
							else
							{
								DirectorFragmentManageActivity.logoutButton.setVisibility(View.GONE);
							}


						}
						else
						{
							DirectorFragmentManageActivity.logoutButton.setVisibility(View.GONE);
						}
				
						
						ScoreListViewAdapter adapter = new ScoreListViewAdapter(socre_list, getActivity());
				
						score_list_view.setAdapter(adapter);
						
						//Utill.showDialg("Size   "+socre_list.size(), getActivity());
					}
					else
					{
						if(status.getCode() == AjaxStatus.NETWORK_ERROR)
						{
						Utill.showDialg(getString(R.string.no_internet_connection), getActivity());	
						}	
					}
					
					
				}
					});	
		}
		else
		{
			DirectorFragmentManageActivity.logoutButton.setVisibility(View.GONE);
		Utill.showDialg(getString(R.string.check_internet_connection), getActivity());	
		}
		
		
		
		
		return rootview;
	}
	OnClickListener addToBack = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				DirectorFragmentManageActivity.popBackStackFragment();
			} catch (Exception e) {
				ShowUserMessage.showUserMessage(getActivity(), e.toString());
			}
		}
	};


	OnClickListener sendEmailListener = new OnClickListener() {
		@Override
		public void onClick(View v) {




			AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

			// Setting Dialog Title
			alertDialog.setTitle(SessionManager.getClubName(getActivity()));

			// Setting Dialog Message
			alertDialog.setMessage("Do you want to send the scores for this event to your email?");

			// Setting Icon to Dialog

//sendReply(bean, status);
			// Setting Positive "Yes" Button
			alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int which) {

					//bean.getNotifications_id()



					sendScoreToEmail();
					dialog.cancel();

					// Write your code here to invoke YES event
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
		}
	};


	public void sendScoreToEmail()
	{
		HashMap<String , Object>params = new HashMap<String , Object>();
		params.put("user_id" , SessionManager.getUser_id(getActivity()));
		params.put("event_id" , event_id);
		httpRequest.getResponse(getActivity(), WebService.mail_score, params, new OnServerRespondingListener(getActivity()) {
			@Override
			public void onSuccess(JSONObject jsonObject) {


				try
				{
					ShowUserMessage.showDialogOnActivity(getActivity() , jsonObject.getString("message"));
				}
				catch (Exception e)
				{

				}



			}
		});
	}


}