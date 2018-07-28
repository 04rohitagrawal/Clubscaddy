package com.clubscaddy.fragment;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.R;
import com.clubscaddy.Bean.NotificationsBean;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class GeneralNotificatinFragment extends Fragment
{
  View View ;
  TextView polling_msg_tv;
  public static String  broadCastId;
  TextView delete_notification_btn,yes_btn ,no_btn;


	HttpRequest httpRequest ;

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

		yes_btn.setVisibility(View.GONE);
		no_btn.setVisibility(View.GONE);

		httpRequest = new HttpRequest(getActivity());
		delete_notification_btn.setOnClickListener(listener);

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


							HashMap<String, Object>params= new HashMap<String, Object>();
							params.put("notifications_id", bean.getNotifications_id());




							httpRequest.getResponse(getActivity(), WebService.deletenotification, params, new OnServerRespondingListener(getActivity()) {
								@Override
								public void onSuccess(JSONObject jsonObject)
								{
									try
									{
										if( jsonObject.getBoolean("status") == true)
										{

                                       ShowUserMessage.showMessageForFragmeneWithBack(getActivity() , jsonObject.getString("message"));
										}
										else
										{
											ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("message"));
										}
									}
									catch (Exception e)
									{

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
				
				
			}
			
			
			
		}
	};
	
	

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
			HashMap<String, Object>param = new HashMap<String, Object>();
			param.put("notifications_id", bean.getNotifications_id());
			param.put("notification_type", bean.getNotification_type());
			param.put("user_id", SessionManager.getUser_Club_id(getActivity()));


			httpRequest.getResponse(getActivity(), WebService.notificationdetail, param, new OnServerRespondingListener(getActivity()) {
				@Override
				public void onSuccess(JSONObject jsonObject)
				{
					Utill.hideProgress();

					try
					{
						polling_msg_tv.setText("Sender  :  "+jsonObject.getString("sender")+"\n\n"+"Message  :  "+jsonObject.getString("notification_message")+"\n\nDate  : "+jsonObject.getString("notificationtime"));
						DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.notifications_details));

					}
					catch (Exception e)
					{

					}





					
				}
					});
			
			
		}
		else
		{
		Utill.showDialg(getActivity().getResources().getString(R.string.check_internet_connection), getActivity());	
		}
			
			
		
	}
	
}
