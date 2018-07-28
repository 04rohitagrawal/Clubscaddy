package com.clubscaddy.fragment;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.androidquery.AQuery;
import com.clubscaddy.Adapter.NotificationListAdapter;
import com.clubscaddy.Bean.Notification;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.Server.HttpRequest;

import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.NotificationAdapter;

import com.clubscaddy.Bean.NotificationsBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;

import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.Server.WebService;

public class NotificationsFragment extends Fragment {

	String Tag = getClass().getName();
	public static FragmentManager mFragmentManager;
	public static Fragment mFragment;
	Context mContext;
	ListView notification_list_view;
Spinner spinner ;
int selectedpos = 0;
AQuery aQuery,aQuery1;
ProgressDialog pd;


	HttpRequest httpRequest ;

ImageButton   open_spinner_btn;

	Notification notification ;

ArrayList<Notification>spinner_list = new ArrayList<Notification>();
	public static Fragment getInstance(FragmentManager mFragManager) {
		if (mFragment == null) {
			mFragment = new NotificationsFragment();
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
	SessionManager sessionManager ;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e(Tag, "onCreateView");
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.notifications, container, false);
		mFragmentManager = getActivity().getSupportFragmentManager();
		aQuery= new AQuery(getActivity());
		open_spinner_btn = (ImageButton) rootView.findViewById(R.id.open_spinner_btn);
		filter_notificationList.clear();
		httpRequest = new HttpRequest(getActivity());


		sessionManager = new SessionManager();
		
		aQuery1 = new AQuery(getActivity());
		spinner = (Spinner) rootView.findViewById(R.id.spinner);
		spinner_list.clear();


		notification = new Notification();
		notification.setNotificationHeading("All");
		notification.setNotificationType(0);
		spinner_list.add(notification);


		notification = new Notification();
		notification.setNotificationHeading("Match Maker");
		notification.setNotificationType(1);
		spinner_list.add(notification);

		notification = new Notification();
		notification.setNotificationHeading("Event");
		notification.setNotificationType(2);
		spinner_list.add(notification);

		if(sessionManager.getClub_score_show(getActivity()) == 1)
		{
			notification = new Notification();
			notification.setNotificationHeading("Score");
			notification.setNotificationType(4);
			spinner_list.add(notification);
		}





		notification = new Notification();
		notification.setNotificationHeading("Broadcast");
		notification.setNotificationType(5);
		spinner_list.add(notification);


		notification = new Notification();
		notification.setNotificationHeading("Polling");
		notification.setNotificationType(6);
		spinner_list.add(notification);

		notification = new Notification();
		notification.setNotificationHeading("General");
		notification.setNotificationType(7);
		spinner_list.add(notification);



		notification = new Notification();
		notification.setNotificationHeading("Coach");
		notification.setNotificationType(8);
		spinner_list.add(notification);


		notification = new Notification();
		notification.setNotificationHeading("League");
		notification.setNotificationType(9);
		spinner_list.add(notification);

		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.notification_item_spinner, spinner_list);

		NotificationListAdapter adapter = new NotificationListAdapter(getActivity() ,spinner_list , spinner);

		spinner.setAdapter(adapter);
		spinner.setPopupBackgroundDrawable(null);


		DirectorFragmentManageActivity.logoutButton.setVisibility(View.INVISIBLE);;
		DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.VISIBLE);	
		
		
		

		
		open_spinner_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				spinner.performClick();
			}
		});
	//	Toast.makeText(getActivity(), "filter list size "+filter_notificationList.size(), 1).show();;
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				// TODO Auto-generated method stub
				//Toast.makeText(getActivity(), "filter", 1).show();
				filter_notificationList.clear();
				selectedpos = pos;
				if(pos == 0)
				{
					filter_notificationList.addAll(notificationList);	
				}
				else
				{
				//	Log.e("abc", notificationList.toString());
					for(int i = 0 ; i<notificationList.size();i++)
					{
						//
						if(Integer.parseInt(notificationList.get(i).getNotification_type()) == spinner_list.get(pos).getNotificationType())
						{
							//Toast.makeText(getActivity(), "type "+notificationList.get(i).getNotification_type(), 1).show();
							filter_notificationList.add(notificationList.get(i))	;
						}

					}

					     	


					
				
				}
				//if(filter_notificationList.size()!=0)
				notification_list_view.setAdapter(new NotificationAdapter(getActivity() ,filter_notificationList ));

				//else
				{
					
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	/*spinner.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
			// TODO Auto-generated method stub
			filter_notificationList.clear();
			
			
			
			//setGroupAdapter
		}
	});*/
		mContext = getActivity();

		if (DirectorFragmentManageActivity.actionbar_titletext != null) {
			DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.notifications));
		}

		if (DirectorFragmentManageActivity.backButton != null) {
			DirectorFragmentManageActivity.showBackButton();
			DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
		}
		//DirectorFragmentManageActivity.logoutButton.setImageResource(android.R.color.transparent);
		DirectorFragmentManageActivity.delete_all_btn.setBackgroundResource(R.drawable.delete_all_layout);
		DirectorFragmentManageActivity.delete_all_btn.setText("Delete All");
		
		//logout
		
		DirectorFragmentManageActivity.delete_all_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

				// Setting Dialog Title
				alertDialog.setTitle(SessionManager.getClubName(getActivity()));

				// Setting Dialog Message
				alertDialog.setMessage("Are you sure you want to delete all notifications?");

				// Setting Icon to Dialog
			
//sendReply(bean, status);
				// Setting Positive "Yes" Button
				alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {
						
						//bean.getNotifications_id()
						pd = new ProgressDialog(getActivity());
						pd.setCancelable(false);
						pd.show();
						//sendReply(bean, "1");
						HashMap<String, Object>params= new HashMap<String, Object>();
						params.put("notifications_reciever_id", SessionManager.getUser_id(mContext));


						httpRequest.getResponse(getActivity(), WebService.deleteallnotification, params, new OnServerRespondingListener(getActivity()) {
							@Override
							public void onSuccess(JSONObject jsonObject)
							{
								pd.dismiss();

								try
								{
									if(Boolean.parseBoolean( jsonObject.getString("status"))==true)
									{

										ShowUserMessage.showMessageForFragmeneWithBack(getActivity()  ,jsonObject.getString("message"));


									}
									else
									{
                                     ShowUserMessage.showUserMessage(getActivity() ,jsonObject.getString("message") );
									}
								}
								catch (Exception e)
								{

								}


							}
						});





								}



					// Write your code here to invoke YES event

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
		});
		initializeView(rootView);
		
		

		getNotifications();
		DirectorFragmentManageActivity.showLogoutButton();
		return rootView;
	}

	void initializeView(View view) {
		notification_list_view = (ListView) view.findViewById(R.id.notification_list_view);




	}
	ProgressDialog progress;

	ArrayList<NotificationsBean> notificationList = new ArrayList<NotificationsBean>();
	ArrayList<NotificationsBean> filter_notificationList = new ArrayList<NotificationsBean>();
	public void getNotifications() {
		
		
		
		
		
		if (Validation.isNetworkAvailable(getActivity()))
		{
			progress = new ProgressDialog(getActivity());
			progress.setMessage("Please Wait..");
			progress.setCancelable(false);
			progress.show();
			GlobalValues.getModelManagerObj(mContext).getNotification(new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					progress.dismiss();
					
					notificationList.clear();
					filter_notificationList.clear();
					notificationList = JsonUtility.parseNotifications(json);
					
					//Toast.makeText(getActivity(), "sizeee    "+selectedpos, 1).show();
					
					if(selectedpos == 0)
					{
						filter_notificationList.addAll(notificationList);	
					}
					else
					{
						//Utill.showToast(mContext, msg);
						for(int i = 0 ; i<notificationList.size();i++)
						{
						//	
							if(Integer.parseInt(notificationList.get(i).getNotification_type()) == spinner_list.get(selectedpos).getNotificationType())
							{
								//Toast.makeText(getActivity(), "type "+notificationList.get(i).getNotification_type(), 1).show();
								filter_notificationList.add(notificationList.get(i))	;
							}
					     
					}
					}
					notification_list_view.setAdapter(new NotificationAdapter(getActivity() ,filter_notificationList ));
					notification_list_view.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							NotificationsBean bean = filter_notificationList.get(position);
							bean.setNotification_status("2");
							if(bean.getNotification_type().equalsIgnoreCase(AppConstants.NOTIFICATION_SCORE)){
								if(!Utill.isStringNullOrBlank(bean.getNotificationScoreId())){
									ShowScoreDetailFrageMent.scoreId = bean.getNotificationScoreId();
									ShowScoreDetailFrageMent mFragment = new ShowScoreDetailFrageMent();
									mFragment.setInstanse(bean.getNotifications_id(),filter_notificationList ,position);
									//	mFragment = ShowScoreDetailFrageMent.getInstance(mFragmentManager);
									if (mFragment != null)
									{
										getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mFragment, "ShowScoreDetail_tag").addToBackStack(null)
												.commit();
									}
									//	DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.ShowScoreDetail_id);
								}
								else
								{
									Utill.showToast(mContext, "Something going to wrong.");
								}
							}
							else
							if(bean.getNotification_type().equalsIgnoreCase(AppConstants.NOTIFICATION_BROADCAST))
							{

								BroadCastDetailFrageMent.broadCastId = bean.getNotificationCommonId();
								PollingNotificationFragment.broadCastId = bean.getNotificationCommonId();

								PollingNotificationFragment fragment = new PollingNotificationFragment();
								fragment.setInstanse(bean);
								getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "fragment").addToBackStack(null).commit();
							}
							if(bean.getNotification_type().equalsIgnoreCase(AppConstants.NOTIFICATION_POLLING))
							{
								PollingNotificationFragment.broadCastId = bean.getNotificationCommonId();
								BroadCastDetailFrageMent.broadCastId = bean.getNotificationCommonId();
								PollingNotificationFragment fragment = new PollingNotificationFragment();
								fragment.setInstanse(bean);
								getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "fragment").addToBackStack(null).commit();
								//DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.PollingDetail_id);
							}
							//Toast.makeText(getActivity(), "notificatiob tyoe "+bean.getNotification_type(), 1).show();
							if(bean.getNotification_type().equalsIgnoreCase(AppConstants.Match_Maker))
							{

								BroadCastDetailFrageMent.broadCastId = bean.getNotificationCommonId();



								MatchMakerShowNotificationPage mFragment = new MatchMakerShowNotificationPage();
								mFragment.setInstanse(bean.getNotifications_id(),filter_notificationList ,position);
								//mFragment.setInstanse(bean.getNotifications_id(),filter_notificationList ,position);
								//	mFragment = ShowScoreDetailFrageMent.getInstance(mFragmentManager);
								if (mFragment != null)
								{
									getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mFragment, "ShowScoreDetail_tag").addToBackStack(null)
											.commit();
								}

								//	DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.PollingDetail_id);
							}

							if(bean.getNotification_type().equalsIgnoreCase(AppConstants.NOTIFICATION_LINK))
							{

								BroadCastDetailFrageMent.broadCastId = bean.getNotificationCommonId();



								EventNotificationDetailsFragment mFragment = new EventNotificationDetailsFragment();
								mFragment.setInstanse(bean);
								//mFragment.setInstanse(bean.getNotifications_id(),filter_notificationList ,position);
								//	mFragment = ShowScoreDetailFrageMent.getInstance(mFragmentManager);
								if (mFragment != null)
								{
									getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mFragment, "ShowScoreDetail_tag").addToBackStack(null)
											.commit();
								}

								//	DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.PollingDetail_id);
							}

							if(bean.getNotification_type().equalsIgnoreCase(AppConstants.COACH_Notification))
							{
								CoachBookingNotification fragment = new CoachBookingNotification();
								fragment.setInstanse(bean);
								getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "fragment").addToBackStack(null).commit();

							}


							if(bean.getNotification_type().equalsIgnoreCase(AppConstants.General_Notification))
							{
								GeneralNotificatinFragment fragment = new GeneralNotificatinFragment();
								fragment.setInstanse(bean);
								getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "fragment").addToBackStack(null).commit();

							}

							if(bean.getNotification_type().equalsIgnoreCase(AppConstants.LEAGUE_Notification))
							{
								GeneralNotificatinFragment fragment = new GeneralNotificatinFragment();
								fragment.setInstanse(bean);
								getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "fragment").addToBackStack(null).commit();

							}

						}
					});
					Utill.hideProgress();
				}
				@Override
				public void onError(String msg) {
					ShowUserMessage.showUserMessage(mContext, msg);
					progress.dismiss();
				}
			},getActivity());
		} else {
			Utill.showNetworkError(mContext);
		}

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
	public void onDestroy() {
		super.onDestroy();
		Log.e(Tag, "onDestroy");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.e(Tag, "onDestroyView");
		DirectorFragmentManageActivity.logoutButton.setVisibility(View.INVISIBLE);;
		DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.INVISIBLE);;
	//	DirectorFragmentManageActivity.setActionBar();
		DirectorFragmentManageActivity.logoutButton.setVisibility(View.GONE);
		//logout
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

}
