package com.clubscaddy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.Bean.EventBean;
import com.clubscaddy.Bean.NotificationsBean;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.custumview.CustomScrollView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EventNotificationDetailsFragment extends Fragment
{
	TextView event_name;
	TextView start_date;
	TextView end_date;
	TextView deadline_date;
	TextView event_cost;
	EditText description;
	View view;
	AQuery aQuery ;
	NotificationsBean bean ;
	TextView delete_notification_btn;
	ProgressDialog pd;
	String event_id = "";
	TextView more_detail_btn;
	CustomScrollView custum_croll_view;
	LinearLayout discription_layout;
	public  void setInstanse(NotificationsBean bean)
    {
		this.bean = bean;
	}
	
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	
	view = inflater.inflate(R.layout.event_notification_details_layout, null);

	discription_layout = (LinearLayout) view.findViewById(R.id.discription_layout);
	discription_layout.setVisibility(View.GONE);

	if (DirectorFragmentManageActivity.actionbar_titletext != null) {
		DirectorFragmentManageActivity.updateTitle(getString(R.string.notifications_details));
	}
	aQuery = new AQuery(getActivity());
	
	event_name = (TextView) view.findViewById(R.id.event_name);

	custum_croll_view = (CustomScrollView) view.findViewById(R.id.custum_croll_view);
	
	start_date = (TextView) view.findViewById(R.id.start_date);
	
	end_date = (TextView) view.findViewById(R.id.end_date);
	
	deadline_date = (TextView) view.findViewById(R.id.deadline_date);
	
	event_cost = (TextView) view.findViewById(R.id.event_cost);
	
	description = (EditText) view.findViewById(R.id.description);
	delete_notification_btn = (TextView) view.findViewById(R.id.delete_notification_btn);
	
	more_detail_btn = (TextView) view.findViewById(R.id.more_detail_btn);




	description.setOnTouchListener(new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			Log.e("action", event.getAction() + "");


				//Toast.makeText(getActivity() ,"action  " + event.getAction(),Toast.LENGTH_SHORT ).show();


			if (1 == event.getAction()) {
				custum_croll_view.setEnableScrolling(true);

			} else {

				custum_croll_view.setEnableScrolling(false);


			}

			return false;
		}
	});
	
	
	more_detail_btn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			getAllEventsByClub(event_id);
		}
	});
	
	
	
	delete_notification_btn.setOnClickListener(new OnClickListener() 
	{
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (Utill.isNetworkAvailable(getActivity())) 
			{
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

				// Setting Dialog Title
				alertDialog.setTitle(SessionManager.getClubName(getActivity()));

				// Setting Dialog Message
				alertDialog.setMessage("Are you sure you want delete this notification?");

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
						Map<String, String>params= new HashMap<String, String>();
						params.put("notifications_id", bean.getNotifications_id());
						aQuery.ajax(WebService.deletenotification, params, JSONObject.class, new AjaxCallback<JSONObject>() {
							@SuppressWarnings("deprecation")
							@Override
							public void callback(String url, JSONObject object, AjaxStatus status) {
								
								
								
								
								String result = object+"";
								Log.e("result", result);
								
								pd.dismiss();
								int code = status.getCode();
								
								
								if (code == AjaxStatus.NETWORK_ERROR) 
								{
									
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
									
									
									final	AlertDialog alertDialog1 = new AlertDialog.Builder(
							                getActivity()).create();

							// Setting Dialog Title
									alertDialog1.setTitle(SessionManager.getClubName(getActivity()));

							// Setting Dialog Message
									alertDialog1.setMessage(object.getString("message"));
									alertDialog1.setCancelable(false);

							// Setting Icon to Dialog


							// Setting OK Button
									alertDialog1.setButton("OK", new DialogInterface.OnClickListener() {
							        public void onClick(DialogInterface dialog, int which) {
							        // Write your code here to execute after dialog closed
							        	alertDialog1.dismiss();
							        	getActivity().getSupportFragmentManager().popBackStack();
							        }
							});

							// Showing Alert Message
									
									
									
									//
								if(Boolean.parseBoolean( object.getString("status"))==true)
								{
									alertDialog1.show();
								}
									
								}
								catch(Exception e)
								{
									
								}
								}

							}
						});
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
			
		}
	});
	
	
	if (Utill.isNetworkAvailable(getActivity()))
	{
		Utill.showProgress(getActivity());
		HashMap<String, String>param = new HashMap<String, String>();
		param.put("notifications_id", bean.getNotifications_id());
		param.put("notification_type", bean.getNotification_type());
		param.put("user_id", SessionManager.getUser_Club_id(getActivity()));
		aQuery.ajax(WebService.notificationdetail, param, JSONObject.class, new AjaxCallback<JSONObject>()
				{
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				// TODO Auto-generated method stub
				super.callback(url, object, status);
				
				
				
				
				
				
				Utill.hideProgress();//(getActivity());
				
				
				Log.e("object", object+"");
				
				
				
				try
				{
					
					event_id = object.getString("notifications_evennt_id");
					
					event_name.setText(object.getString("event_name"));
					start_date.setText(object.getString("event_startdate"));
					end_date.setText(object.getString("event_finishdate"));
					deadline_date.setText(object.getString("event_signup_deadline_date"));
					event_cost.setText(object.getString("event_cost"));
					description.setText(object.getString("event_description"));

					if (object.getString("event_description").length() == 0)
					{
						discription_layout.setVisibility(View.GONE);
					}
					else
					{
						discription_layout.setVisibility(View.VISIBLE);
					}


				}
				catch(Exception e)
				{
				//Toast.makeText(getActivity(), e.getMessage(), 1).show();	
				}
				
			}
				});
		
		
	}
	else
	{
	Utill.showDialg(getActivity().getResources().getString(R.string.check_internet_connection), getActivity());	
	}
	/**/
	return view;
}

ArrayList<EventBean> allEventsList;
private void getAllEventsByClub(String event_id) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("event_id", event_id);
	if (Utill.isNetworkAvailable(getActivity())) {
		Utill.showProgress(getActivity());
		GlobalValues.getModelManagerObj(getActivity()).getEventsList(new ModelManagerListener() {
			@Override
			public void onSuccess(String json) {
				Utill.hideProgress();
				try
				{
					allEventsList = JsonUtility.parseAllEventsListItem (json,getActivity());	
					EventDetailFrageMent frageMent = new EventDetailFrageMent();
					EventDetailFrageMent.eventBean = allEventsList.get(0);
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, frageMent, "frageMent").addToBackStack(null).commit();
				}
				catch(Exception e)
				{
				//Toast.makeText(getActivity(),e.getMessage(), 1).show();
				}
				
				
			//	Toast.makeText(getActivity(), "Size "+allEventsList.size(), 1).show();
				
				
				Log.e("json", json);
			}

			@Override
			public void onError(String msg) {
				ShowUserMessage.showUserMessage(getActivity(), msg);
				Utill.hideProgress();
			}
		},params);
	} else {
		Utill.showNetworkError(getActivity());
	}
}

}
