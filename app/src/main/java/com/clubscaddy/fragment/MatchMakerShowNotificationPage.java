package com.clubscaddy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.Bean.NotificationsBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.custumview.InternetConnection;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.Server.WebService;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MatchMakerShowNotificationPage extends Fragment
{
	View page_view ;
	String notifications_id;
	ArrayList<NotificationsBean> filter_notificationList; 
	int position ;
	EditText msg_edittxt ;
	TextView accept_btn;
	TextView reject_btn;
	TextView delete_notification_btn;
	AQuery aQuery;
	ProgressDialog pd1 ;
	LinearLayout message_layout;
	EditText message_edit_tv;

	
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	page_view = inflater.inflate(R.layout.notification_subpage_layout, null);
	accept_btn = (TextView) page_view.findViewById(R.id.accept_btn);
	reject_btn = (TextView) page_view.findViewById(R.id.reject_btn);
	delete_notification_btn = (TextView) page_view.findViewById(R.id.delete_notification_btn);
	msg_edittxt = (EditText) page_view.findViewById(R.id.msg_edittxt);
	aQuery = new AQuery(getActivity());
	pd1 = new ProgressDialog(getActivity());
	pd1.setCancelable(false);

	message_layout = (LinearLayout) page_view.findViewById(R.id.message_layout);


	message_edit_tv = (EditText) page_view.findViewById(R.id.message_edit_tv);

	message_edit_tv.setClickable(false);
	message_edit_tv.setFocusable(false);


	NotificationsBean bean = filter_notificationList.get(position);
	if (DirectorFragmentManageActivity.actionbar_titletext != null) {
		DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.notifications_details));
	}

	if(bean.getNotification_type().equalsIgnoreCase(AppConstants.NOTIFICATION_DROPINS) && bean.getNotification_invitation_status().equalsIgnoreCase(AppConstants.INVITATION_STATUS_INVITE))
	{
		accept_btn.setText("Accept");;
		reject_btn.setText("Decline");;
	
		accept_btn.setVisibility(View.VISIBLE);
		reject_btn.setVisibility(View.VISIBLE);
		//reject_btn.setPadding(5, 10, 5, 10);
	}
	else 
		if(bean.getNotification_type().equalsIgnoreCase(AppConstants.NOTIFICATION_DROPINS) && bean.getNotification_invitation_status().equalsIgnoreCase(AppConstants.INVITATION_STATUS_JOIN))
		{
		accept_btn.setText("Accept");;
		reject_btn.setText("Do not accept");;
		//reject_btn.setPadding(5, 10, 5, 10);
		accept_btn.setVisibility(View.VISIBLE);
		reject_btn.setVisibility(View.VISIBLE);
	    }
	if(bean.getNotifications_action_status().equalsIgnoreCase("10"))
	{
		accept_btn.setVisibility(View.GONE);
		reject_btn.setVisibility(View.GONE);
	}
	if(InternetConnection.isInternetOn(getActivity()))
	{
				pd1.show();
		HashMap<String, String>params = new HashMap<String, String>();
		
		params.put("notifications_id", filter_notificationList.get(position).getNotifications_id());
		params.put("notification_type", filter_notificationList.get(position).getNotification_type());
		params.put("user_id",SessionManager.getUserId(getActivity()));
		
		aQuery.ajax(WebService.notificationdetail, params, JSONObject.class, new AjaxCallback<JSONObject>()
				{
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				// TODO Auto-generated method stub
				super.callback(url, object, status);
				pd1.dismiss();
				
				if(object != null)
				{
					try {
						if(object.getString("notification_type").equalsIgnoreCase(AppConstants.NOTIFICATION_DROPINS))
						
							//
							
							msg_edittxt.setText(object.getString("notification_message"));

						message_edit_tv.setText(object.getString("notifications_desc"));
						if (object.getString("notifications_desc").length() == 0)
						{
							message_layout.setVisibility(View.GONE);
						}
						else
						{
							message_layout.setVisibility(View.VISIBLE);
						}






					
						/*String sender_name = "Sender name";
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
							
						}*/
						
						
						//msg_edittxt.setText("Sender :"+sender_name+"\n\n"+"Message :"+time+"\n\n"+time);
						
					
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					Utill.showDialg(getString(R.string.check_internet_connection), getActivity());	
				}
				
			}
				});
		
			
	}
	else
	{
	Utill.showDialg(getString(R.string.check_internet_connection), getActivity());	
	}
	
	
	
	
	accept_btn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			NotificationsBean bean = filter_notificationList.get(position);
			if(bean.getNotification_type().equalsIgnoreCase(AppConstants.NOTIFICATION_DROPINS) && bean.getNotification_invitation_status().equalsIgnoreCase(AppConstants.INVITATION_STATUS_INVITE)){
				sendReply(bean, "2");
			}else if(bean.getNotification_type().equalsIgnoreCase(AppConstants.NOTIFICATION_DROPINS) && bean.getNotification_invitation_status().equalsIgnoreCase(AppConstants.INVITATION_STATUS_JOIN)){
				sendReply(bean, "4");
			}
		}
	});
	
	
	reject_btn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			NotificationsBean bean = filter_notificationList.get(position);
			if(bean.getNotification_type().equalsIgnoreCase(AppConstants.NOTIFICATION_DROPINS) && bean.getNotification_invitation_status().equalsIgnoreCase(AppConstants.INVITATION_STATUS_INVITE)){
				sendReply(bean, "3");
			}else if(bean.getNotification_type().equalsIgnoreCase(AppConstants.NOTIFICATION_DROPINS) && bean.getNotification_invitation_status().equalsIgnoreCase(AppConstants.INVITATION_STATUS_JOIN)){
				sendReply(bean, "5");
			}
		}
	});
	
	
	
	delete_notification_btn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			
			
			
			
			
			AlertDialog.Builder calertDialog = new AlertDialog.Builder(getActivity());

			// Setting Dialog Title
			calertDialog.setTitle(SessionManager.getClubName(getActivity()));

			// Setting Dialog Message
			calertDialog.setMessage("Are you sure you want delete this notification?");

			// Setting Icon to Dialog
		
//sendReply(bean, status);
			// Setting Positive "Yes" Button
			calertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int which) {
					
			
					dialog.cancel();
					
					
					
					
					
					pd1.show();
					//sendReply(bean, "1");
					Map<String, String>params= new HashMap<String, String>();
					params.put("notifications_id", notifications_id);
					aQuery.ajax(WebService.deletenotification, params, JSONObject.class, new AjaxCallback<JSONObject>() {
						@SuppressWarnings("deprecation")
						@Override
						public void callback(String url, JSONObject object, AjaxStatus status) {
							
							
							pd1.dismiss();
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
						                getActivity()).create();

						// Setting Dialog Title
						alertDialog.setTitle(SessionManager.getClubName(getActivity()));

						// Setting Dialog Message
						alertDialog.setMessage(object.getString("message"));
								alertDialog.setCancelable(false);
						// Setting Icon to Dialog


						// Setting OK Button
						alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
						        public void onClick(DialogInterface dialog, int which) {
						        // Write your code here to execute after dialog closed
						        	filter_notificationList.remove(position);
									
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
	return page_view;
}
public void setInstanse(String notifications_id, ArrayList<NotificationsBean> filter_notificationList, int position)
{
	// TODO Auto-generated method stub
	this.filter_notificationList = filter_notificationList;
	this.notifications_id = notifications_id;
	this.position = position;
	
}
void sendReply(NotificationsBean bean,String status) {
	//2 for join,3 for decline,4 for accept,5 for reject,6 for delete
//	1 for dropin creator 2 for dropin receiver�
	String senderStatus = "1";
	Map<String, Object> params = new HashMap<String, Object>();
	if(bean.getNotifications_sender_id().equalsIgnoreCase(SessionManager.getUser_id(getActivity()))){
		senderStatus = "1";
	}else{
		senderStatus = "2";
	} 
//	params.put("sender_status",senderStatus); 
	params.put("notifications_id",bean.getNotifications_id());
	params.put("notifications_reciever_id",SessionManager.getUser_id(getActivity())   );
	params.put("notification_dropin_id",bean.getNotification_dropin_id());
	params.put("notification_invitation_status",status);
//	params.put("dropin_member_user_id",bean.getDropin_member_user_id());
	
	
	
	if (Utill.isNetworkAvailable(getActivity())) {
		Utill.showProgress(getActivity());
		GlobalValues.getModelManagerObj(getActivity()).sendReply(params,new ModelManagerListener() {
			@Override
			public void onSuccess(String json) {
				
				
				String result = json;
				
				try
				{
					//{"message":"Match Maker Full","status":"true"}
					JSONObject obj = new JSONObject(result);
					//Toast.makeText(getActivity(), text, duration)
					
					if(obj.getString("status").equals("true"))
					{
						accept_btn.setVisibility(View.GONE);
						reject_btn.setVisibility(View.GONE);	
					}
					
					Utill.showDialg(obj.getString("message"), getActivity());
				}
				catch(Exception e)
				{
					Toast.makeText(getActivity(), e.getMessage(), 1).show();
				}
				
				Utill.hideProgress();
			}
			@Override
			public void onError(String msg) {
				
			//	Toast.makeText(getActivity(), "Earro", 1).show();
				Utill.hideProgress();
				Utill.showDialg(msg, getActivity());
			}
		});
	} else {
		
	}

}



}
