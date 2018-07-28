package com.clubscaddy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.Bean.EventBean;
import com.clubscaddy.Bean.NotificationsBean;
import com.clubscaddy.Bean.ScoreBean;
import com.clubscaddy.Bean.ScoreNumberBean;
import com.clubscaddy.Bean.ScoreOpponentBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.Server.WebService;

public class ScoreDetailFrageMent extends Fragment {

	String Tag = getClass().getName();
	public static FragmentManager mFragmentManager;
	public static Fragment mFragment;
	Context mContext;
	ArrayList<MemberListBean>eventMedotororMemberList ;
	TextView copartnerTV;
	TextView eventName, scoreOwner,  opponent1TV, opponent2TV, set4TV, set5TV;
	EditText set1MeET, set1OppET, set2MeET, set2OppET, set3MeET, set3OppET, selectSetET, set4MeET, set4OppET, set5MeET, set5OppET;
	TextView myTeam, opponentTeam;
	TextView acceptTV,rejectTV;
	int position;
	AQuery aQuery;
	TextView own_end_symbol,opponent_end_symbol ,score_show_own_end_symbol ,score_show_opponent_end_symbol;
	TextView score_show_creator_tv,score_show_copartnerTV,score_show_opponent1_tv,score_show_opponent2_tv;
	ProgressDialog pd;
	ArrayList<NotificationsBean> filter_notificationList ;
String delete_notification_id ;
TextView delete_score_btn;
TextView edit_score_btn;
	EventBean eventBean;
	public static Fragment getInstance(FragmentManager mFragManager) {
		if (mFragment == null) {
			mFragment = new ScoreDetailFrageMent();
		}
		return mFragment;
	}





	public void setEventBean(EventBean eventBean ,ArrayList<MemberListBean>eventMedotororMemberList)
	{
            this.eventBean = eventBean ;
			this.eventMedotororMemberList = eventMedotororMemberList ;
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
		Log.e(Tag, "onCreateView");
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.score_details_layout, container, false);
		mContext = getActivity();
		aQuery = new AQuery(getActivity());
		if (DirectorFragmentManageActivity.actionbar_titletext != null) {
			DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.score_detail));
		}

		if (DirectorFragmentManageActivity.backButton != null) {
			DirectorFragmentManageActivity.showBackButton();
			DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
		}
		DirectorFragmentManageActivity.showLogoutButton();
		initializeView(rootView);
		setOnClickListener();
		getScoreDetails();
		
		
		
		

		


		delete_score_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				
				
				
				
				AlertDialog.Builder calertDialog = new AlertDialog.Builder(getActivity());

				// Setting Dialog Title
				calertDialog.setTitle(SessionManager.getClubName(mContext));

				// Setting Dialog Message
				calertDialog.setMessage("Are you sure want to delete this score?");

				// Setting Icon to Dialog
			
//sendReply(bean, status);
				// Setting Positive "Yes" Button
				calertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {
						
				
						dialog.cancel();
						
						
						
						
						pd = new ProgressDialog(getActivity());
						pd.setCancelable(false);
						pd.show();
						//sendReply(bean, "1");
						Map<String, String>params= new HashMap<String, String>();
						params.put("score_id", scoreId);
						params.put("score_user_id", SessionManager.getUser_id(getActivity()));
						
						
						aQuery.ajax(WebService.deletescore, params, JSONObject.class, new AjaxCallback<JSONObject>() {
							@SuppressWarnings("deprecation")
							@Override
							public void callback(String url, JSONObject object, AjaxStatus status) {
								
								
								pd.dismiss();
								Log.e("object", object+"");
								int code = status.getCode();
									
								if (code == AjaxStatus.NETWORK_ERROR) 
								{
								Utill.showDialg("NetWork earror!", getActivity());
								//Toast.makeText(getActivity(), "NetWork earror!", Toast.LENGTH_LONG).show();
									return;
								} 
								else 
								if(code == AjaxStatus.TRANSFORM_ERROR) 
								{
									//return;
								} 
								else 
								if(code == 200)
								{
								try
								{
									//Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
									
									//
								if(Boolean.parseBoolean( object.getString("status"))==true)
								{
									final	AlertDialog alertDialog = new AlertDialog.Builder(
							                mContext).create();

							// Setting Dialog Title
							alertDialog.setTitle(SessionManager.getClubName(mContext));

							// Setting Dialog Message
									alertDialog.setCancelable(false);
							alertDialog.setMessage(object.getString("message"));

							// Setting Icon to Dialog


							// Setting OK Button
							alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
							        public void onClick(DialogInterface dialog, int which) {
							        // Write your code here to execute after dialog closed

										
										getActivity().getSupportFragmentManager().popBackStack();
							        	alertDialog.dismiss();
							        }
							});

							// Showing Alert Message
							alertDialog.show();
										
								}
									
								}
								catch(Exception e)
								{
									
								}
								}

							}
						});
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
							}
				});

				// Setting Negative "NO" Button
				calertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,	int which) {
					// Write your code here to invoke NO event
						dialog.cancel();
					}
				});
				
				
				
				
				calertDialog.show();
				
				
				
				
				
				
				
				
				
				
				
				
				
				
			
			}
		});
		
		
		
		edit_score_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditScoreFragment fragment = new EditScoreFragment();
				fragment.setInstanse(scoreBean , eventBean);

				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "fragment").addToBackStack(null).commit();
			}
		});
		return rootView;
	}

	public static String scoreId = "";
	ScoreBean scoreBean;

	private void getScoreDetails() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("score_id", scoreId);
		params.put("notifications_id", delete_notification_id);

		if (Utill.isNetworkAvailable(getActivity())) {
			Utill.showProgress(mContext);
			
			
			HashMap<String, String>param = new HashMap<String, String>();
			
			param.put("score_id", scoreId);
			aQuery.ajax(WebService.get_score_detail, param, JSONObject.class, new AjaxCallback<JSONObject>()
			{
		@Override
		public void callback(String url, JSONObject object111, AjaxStatus status) {
			// TODO Auto-generated method stub
			super.callback(url, object111, status); 
			
			Utill.hideProgress();
			scoreBean = JsonUtility.parseScoreDetail(object111+"",getActivity());
			
			setDataToView(scoreBean);
			/*GlobalValues.getModelManagerObj(mContext).getScoreDetails(params, new ModelManagerListener() {

				@Override
				public void onSuccess(String json) {
					
					
					
					
					
					
					Utill.hideProgress();
				}

				@Override
				public void onError(String msg) {
					ShowUserMessage.showUserMessage(mContext, msg);
					Utill.hideProgress();
				}
			});*/
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		}
			
		});
			
			
			
		} else {
			Utill.showNetworkError(mContext);
		}
	}

	void setDataToView(ScoreBean scoreBean) {
		scoreOwner.setText(scoreBean.getCreater_name());



		if(SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER))
		{
			if (scoreBean.getScore_user_id().equals(SessionManager.getUser_id(getActivity())) || isUserExitsInMedotrorList(scoreBean.getScore_user_id()))
			{
				edit_score_btn.setVisibility(View.VISIBLE);
				delete_score_btn.setVisibility(View.VISIBLE);
			}
			else
			{
				edit_score_btn.setVisibility(View.GONE);
				delete_score_btn.setVisibility(View.GONE);
			}
		}
		else
		{
			edit_score_btn.setVisibility(View.VISIBLE);
			delete_score_btn.setVisibility(View.VISIBLE);
		}


      if(scoreBean.getEvent_name().equals("")|| scoreBean.getEvent_name() == "")
      {
    	  
      }
      else
      {
    	eventName.setText(scoreBean.getEvent_name());  
      }
		ArrayList<ScoreOpponentBean> opponents = scoreBean.getOpponentList();
		String ownerName = "";
		String opponent1 = "";
		String opponent2 = "";
		String copartner = "";
		ownerName = scoreBean.getCreater_name();
		copartner = scoreBean.getCopartner();
		if (Utill.isStringNullOrBlank(copartner)) {
			//copartnerTV.setVisibility(View.GONE);
			//myEnd.setVisibility(View.GONE);
		} else {
			copartnerTV.setText("" + copartner);
		}
		if (opponents != null) {
			opponent1 = opponents.get(0).getScore_opponent();
			opponent1TV.setText(opponent1);
			if (opponents.size() > 1) {
				opponent2 = opponents.get(1).getScore_opponent();
				opponent2TV.setText(opponent2);
				//opponentEnd.setVisibility(View.VISIBLE);
				opponent2TV.setVisibility(View.VISIBLE);
			} else {
				//opponentEnd.setVisibility(View.GONE);
				opponent2TV.setVisibility(View.GONE);
			}
		}
		


		
		score_show_creator_tv.setText(scoreBean.getCreater_name());
		
		if (Utill.isStringNullOrBlank(copartner)) {
			copartnerTV.setVisibility(View.GONE);
			opponent_end_symbol.setVisibility(View.GONE);
			own_end_symbol.setVisibility(View.GONE);
			score_show_copartnerTV.setVisibility(View.GONE);
			score_show_opponent_end_symbol.setVisibility(View.GONE);
		} else {
			score_show_copartnerTV.setText("" + copartner);
		}
		if (opponents != null) {
			opponent1 = opponents.get(0).getScore_opponent();
			score_show_opponent1_tv.setText(opponent1);
			if (opponents.size() > 1) {
				opponent2 = opponents.get(1).getScore_opponent();
				score_show_opponent2_tv.setText(opponent2);
				//opponentEnd.setVisibility(View.VISIBLE);
				score_show_opponent2_tv.setVisibility(View.VISIBLE);
			} else {
				score_show_own_end_symbol.setVisibility(View.GONE);
				score_show_opponent2_tv.setVisibility(View.GONE);
				
				
				score_show_own_end_symbol.setVisibility(View.GONE);
				score_show_opponent2_tv.setVisibility(View.GONE);
			}
		}
		
		
	/*	score_show_creator_tv = (TextView) view.findViewById(R.id.score_show_creator_tv);
		score_showo_pponent_end = (TextView) view.findViewById(R.id.score_showo_pponent_end);
		score_show_copartnerTV = (TextView) view.findViewById(R.id.score_show_copartnerTV);
		score_show_opponent1_tv = (TextView) view.findViewById(R.id.score_show_opponent1_tv);
		score_show_opponent2_tv = (TextView) view.findViewById(R.id.score_show_opponent2_tv);
	*/	
		/*String myTeamStr = "";
		if (Utill.isStringNullOrBlank(copartner)) {
			myTeamStr = ownerName;
		} else {
			myTeamStr = ownerName + "  " ;
		}
		String OpponentTeamStr = "";
		if (Utill.isStringNullOrBlank(opponent2)) {
			OpponentTeamStr = opponent1;
		} else {
			OpponentTeamStr = opponent1 + "  " ;
		}
		myTeam.setText("" + myTeamStr);*/
		//opponentTeam.setText("" + OpponentTeamStr);
		ArrayList<ScoreNumberBean> scoreNumbers = scoreBean.getScoreNumberList();
		for (int i = 0; i < scoreNumbers.size(); i++) {
			switch (i) {
			case 0:
				set1MeET.setText(scoreNumbers.get(i).getMy_score());
				set1OppET.setText(scoreNumbers.get(i).getScore_opponent1());
				break;
			case 1:
				set2MeET.setText(scoreNumbers.get(i).getMy_score());
				set2OppET.setText(scoreNumbers.get(i).getScore_opponent1());
				break;
			case 2:
				set3MeET.setText(scoreNumbers.get(i).getMy_score());
				set3OppET.setText(scoreNumbers.get(i).getScore_opponent1());
				break;
			case 3:
				set4TV.setVisibility(View.VISIBLE);
				set4MeET.setVisibility(View.VISIBLE);
				set4OppET.setVisibility(View.VISIBLE);
				
				set4MeET.setText(scoreNumbers.get(i).getMy_score());
				set4OppET.setText(scoreNumbers.get(i).getScore_opponent1());
				break;
			case 4:
				set5TV.setVisibility(View.VISIBLE);
				set5MeET.setVisibility(View.VISIBLE);
				set5OppET.setVisibility(View.VISIBLE);
				
				set5MeET.setText(scoreNumbers.get(i).getMy_score());
				set5OppET.setText(scoreNumbers.get(i).getScore_opponent1());;
				break;
			}
		}

	}

	void initializeView(View view) {
		eventName = (TextView) view.findViewById(R.id.event_name);

		scoreOwner = (TextView) view.findViewById(R.id.score_creator_tv);
		//copartnerTV = (TextView) view.findViewById(R.id.score_copartner);
		//myEnd = (TextView) view.findViewById(R.id.my_end);

		own_end_symbol = (TextView) view.findViewById(R.id.own_end_symbol);
		opponent_end_symbol = (TextView) view.findViewById(R.id.opponent_end_symbol);
		score_show_own_end_symbol = (TextView) view.findViewById(R.id.score_show_own_end_symbol);
		score_show_opponent_end_symbol = (TextView) view.findViewById(R.id.score_show_opponent_end_symbol);
		
		
		opponent1TV = (TextView) view.findViewById(R.id.opponent1_tv);
		opponent2TV = (TextView) view.findViewById(R.id.opponent2_tv);

		set1MeET = (EditText) view.findViewById(R.id.set1_me);
		set1OppET = (EditText) view.findViewById(R.id.set1_opposite);
		set2MeET = (EditText) view.findViewById(R.id.set2_me);
		set2OppET = (EditText) view.findViewById(R.id.set2_opposite);
		set3MeET = (EditText) view.findViewById(R.id.set3_me);
		set3OppET = (EditText) view.findViewById(R.id.set3_opposite);
		set4MeET = (EditText) view.findViewById(R.id.set4_me);
		set4OppET = (EditText) view.findViewById(R.id.set4_opposite);
		set5MeET = (EditText) view.findViewById(R.id.set5_me);
		set5OppET = (EditText) view.findViewById(R.id.set5_opposite);

		set4TV = (TextView) view.findViewById(R.id.set4tv);
		set5TV = (TextView) view.findViewById(R.id.set5tv);

		myTeam = (TextView) view.findViewById(R.id.my_team);
		opponentTeam = (TextView) view.findViewById(R.id.opponent_team);
		
		acceptTV = (TextView) view.findViewById(R.id.accept);
		rejectTV = (TextView) view.findViewById(R.id.reject);
		
		copartnerTV = (TextView) view.findViewById(R.id.copartnerTV);

		score_show_creator_tv = (TextView) view.findViewById(R.id.score_show_creator_tv);
		
		score_show_copartnerTV = (TextView) view.findViewById(R.id.score_show_copartnerTV);
		score_show_opponent1_tv = (TextView) view.findViewById(R.id.score_show_opponent1_tv);
		score_show_opponent2_tv = (TextView) view.findViewById(R.id.score_show_opponent2_tv);
		edit_score_btn = (TextView) view.findViewById(R.id.edit_score_btn);
		delete_score_btn = (TextView) view.findViewById(R.id.delete_score_btn);






		
	}

	void setOnClickListener() {
		acceptTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		rejectTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	OnClickListener onClicks = new OnClickListener() {

		@Override
		public void onClick(View v) {

		}
	};

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
	public void onDestroy() {
		super.onDestroy();
		Log.e(Tag, "onDestroy");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.e(Tag, "onDestroyView");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.e(Tag, "onDetach");
	}

	OnClickListener addToBack = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				DirectorFragmentManageActivity.popBackStackFragment();
			} catch (Exception e) {
				ShowUserMessage.showUserMessage(mContext, e.toString());
			}
		}
	};


	public boolean isUserExitsInMedotrorList(String userId)
	{
		for (int i = 0 ; i < eventMedotororMemberList.size() ;i++)
		{
			if (eventMedotororMemberList.get(i).getUser_id().equals(userId))
			{
				return true;
			}
		}

		return false ;
	}

	
}
