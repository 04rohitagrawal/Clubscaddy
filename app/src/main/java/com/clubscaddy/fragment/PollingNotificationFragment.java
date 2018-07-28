package com.clubscaddy.fragment;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.Bean.NotificationsBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.Server.WebService;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class PollingNotificationFragment extends Fragment
{
  View View ;
  TextView polling_msg_tv;
  public static String  broadCastId;
  TextView delete_notification_btn,yes_btn ,no_btn;
  AQuery aQuery;
  NotificationsBean bean;
  public void setInstanse(NotificationsBean bean)
  {
	this.bean = bean;  
  }
  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View = inflater.inflate(R.layout.polling_notification_fragment_datial_layout, null);
		polling_msg_tv = (TextView) View.findViewById(R.id.polling_msg_tv);
		
		delete_notification_btn = (TextView) View.findViewById(R.id.delete_notification_btn);
		yes_btn = (TextView) View.findViewById(R.id.yes_btn);
		no_btn = (TextView) View.findViewById(R.id.no_btn);
		
		delete_notification_btn.setOnClickListener(listener);
		no_btn.setOnClickListener(listener);
		yes_btn.setOnClickListener(listener);
		
		aQuery = new AQuery(getActivity());
		getBroadCastDetail();
		return View;
	}
	
	
	
	public OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(v.getId() == R.id.delete_notification_btn)
			{
				
				
				
				if (Utill.isNetworkAvailable(getActivity())) 
				{
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
					 
			        // Setting Dialog Title
			        alertDialog.setTitle(SessionManager.getClubName(getActivity()));
			 
			        // Setting Dialog Message
			        alertDialog.setMessage("Are you sure you want delete this notification?");
			 
			        // Setting Icon to Dialog
			       
			 
			        // Setting Positive "Yes" Button
			        alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog,int which) {
			 
			            	
			            	Utill.showProgress(getActivity());
							
							
							Map<String, String>params= new HashMap<String, String>();
							params.put("notifications_id", bean.getNotifications_id());
							aQuery.ajax(WebService.deletenotification, params, JSONObject.class, new AjaxCallback<JSONObject>() {
								@Override
								public void callback(String url, JSONObject object, AjaxStatus status) {
									
									Utill.hideProgress();
									
									int code = status.getCode();
									
									
									if (code == AjaxStatus.NETWORK_ERROR) 
									{
									 ShowUserMessage.showUserMessage(getActivity() , "NetWork error!");
									//Toast.makeText(getActivity(), "NetWork earror!", Toast.LENGTH_LONG).show();
										return;
									} 
									else 
									if(code == AjaxStatus.TRANSFORM_ERROR) 
									{
										//return;

										ShowUserMessage.showUserMessage(getActivity() , "NetWork error!");

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
										ShowUserMessage.showMessageForFragmeneWithBack(getActivity() ,object.getString("message") );
									//getActivity().getSupportFragmentManager().popBackStack();
									}
									else
									{
										ShowUserMessage.showUserMessage(getActivity() , object.getString("message"));
									}
										
									}
									catch(Exception e)
									{
										
									}
									}
									else {
										Utill.showNetworkError(getActivity());
									}

								}
							});	
			            	 dialog.cancel();
			                 }
			        });
			 
			        // Setting Negative "NO" Button
			        alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			            // Write your code here to invoke NO event
			           // Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
			            dialog.cancel();
			            }
			        });
			 
			        // Showing Alert Message
			        alertDialog.show();	
				}
				else
				{
					ShowUserMessage.showUserMessage(getActivity() , "Please connect to internet.");
				}
				
				
			}
			if(v.getId() == R.id.yes_btn)
			{
				pollingReplly("1");	
			}
			if(v.getId() == R.id.no_btn)
			{
				pollingReplly("2");
			}
			
			
		}
	};
	
	void pollingReplly(String status){
		if (Utill.isNetworkAvailable(getActivity())) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("broadcast_id",broadCastId);
			params.put("user_id",SessionManager.getUser_id(getActivity()));
			params.put("broadcast_reply",status);
			Utill.showProgress(getActivity());
		
			
			GlobalValues.getModelManagerObj(getActivity()).sendPollingReply(params,new ModelManagerListener() {
				@SuppressWarnings("deprecation")
				@Override
				public void onSuccess(String json)
				{
					Utill.hideProgress();
				Log.e("json", json+"")	;
				
				try
				{
				JSONObject jsonObj = new JSONObject(json);	
				if(jsonObj.getString("status").equalsIgnoreCase("true"))
				{
					final	AlertDialog alertDialog = new AlertDialog.Builder(
			                getActivity()).create();

			// Setting Dialog Title
			alertDialog.setTitle(SessionManager.getClubName(getActivity()));

			// Setting Dialog Message/message
			alertDialog.setMessage(jsonObj.getString("message"));
					alertDialog.setCancelable(false);
			// Setting Icon to Dialog


			// Setting OK Button
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			        // Write your code here to execute after dialog closed
			                
			        	yes_btn.setVisibility(View.GONE);
			        	no_btn.setVisibility(View.GONE);
			        	alertDialog.dismiss();
			        }
			});

			// Showing Alert Message
			alertDialog.show();	
				}
				
				
				}
				catch(Exception e)
				{
					Utill.showDialg(e.getMessage(), getActivity());
				}
				
				}

				@Override
				public void onError(String msg) {
					ShowUserMessage.showUserMessage(getActivity(), msg);
					Utill.hideProgress();
				}
			});
		} else 
		{
			Utill.showNetworkError(getActivity());
		}
	
	}
	void getBroadCastDetail(){
		if (Utill.isNetworkAvailable(getActivity())) {
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("notifications_id",broadCastId);
			params.put("user_id",SessionManager.getUser_id(getActivity()));
			params.put("notification_type", bean.getNotification_type());
			Utill.showProgress(getActivity());
			String webUrl = WebService.get_broadcast_detail;
			webUrl = WebService.notificationdetail;
			
			
			
			
			
			
			Utill.showProgress(getActivity());
			HashMap<String, String>param = new HashMap<String, String>();
			param.put("notifications_id", bean.getNotifications_id());
			param.put("notification_type", bean.getNotification_type());
			param.put("user_id", SessionManager.getUser_id(getActivity()));
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
						if(object != null)
						{
							//polling_msg_tv.setText(object.getString("notification_message"));
						
							String sender_name = "Sender name";
							String time = "time";
							String msg = "" ;
							try
							{
								msg = object.getString("notification_message");
								
								sender_name	=object.getString("sender");
								time =object.getString("notificationtime");
							}
							catch(Exception e)
							{
								
							}
							
							
							polling_msg_tv.setText("Sender  :  "+sender_name+"\n\n"+"Message  :  "+msg+"\n\nDate  :  "+time);
							
							
							
							DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.notifications_details));
							
							//Toast.makeText(context, text, duration)
							//reply 1 
								if(object.getString("notification_type").equals(AppConstants.NOTIFICATION_BROADCAST)|| !object.getString("broadcastreply_reply").equals("3"))
							{
								yes_btn.setVisibility(View.GONE);
								no_btn.setVisibility(View.GONE);
							}
							else
							{
								no_btn.setVisibility(View.VISIBLE);
								yes_btn.setVisibility(View.VISIBLE);
								yes_btn.setOnClickListener(listener);
								no_btn.setOnClickListener(listener);
							}
						}
					}
					catch(Exception e)
					{
					Toast.makeText(getActivity(), e.getMessage(), 1).show();	
					}
					
				}
					});
			
			
		}
		else
		{
		Utill.showDialg(getActivity().getResources().getString(R.string.check_internet_connection), getActivity());	
		}
			
			
			
			
			
			
			
			
			
			
			
			
			/*if(broadCastBean.getBroadcast_type().equalsIgnoreCase(AppConstants.BROADCAST)){
				webUrl = WebService.get_broadcast_detail;
			}else{
				webUrl = WebService.get_pollingdetail;
			}
		sff	*//*GlobalValues.getModelManagerObj(getActivity()).getBroadCastDetail(webUrl,params,new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					//broadCastDetailBean = JsonUtility.parseBroadCastDetail(json,getActivity());
					//setDataToView(broadCastDetailBean);
					Log.e("json", json+"");
					Toast.makeText(getActivity(), json, 1).show();
					if(json != null)
					{
						polling_msg_tv.setText(broadCastDetailBean.getBroadcast_msg());
						DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.notifications_details));
						if(broadCastDetailBean.isAnsweredByMe() || broadCastDetailBean.getBroadcast_type().equalsIgnoreCase(AppConstants.BROADCAST))
						{
							yes_btn.setVisibility(View.GONE);
							no_btn.setVisibility(View.GONE);
						}
						else
						{
							no_btn.setVisibility(View.VISIBLE);
							yes_btn.setVisibility(View.VISIBLE);
							yes_btn.setOnClickListener(listener);
							no_btn.setOnClickListener(listener);
						}
					}
					
					
					
					Utill.hideProgress();
				}

				@Override
				public void onError(String msg) {
					ShowUserMessage.showUserMessage(getActivity(), msg);
					Utill.hideProgress();
				}
			});
		} else {
			Utill.showNetworkError(getActivity());
		}*/
	}
	
}
