package com.clubscaddy.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.clubscaddy.Bean.NotificationsBean;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.DialogBoxButtonListner;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.R;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by administrator on 8/6/17.
 */

public class CoachBookingNotification extends Fragment
{
    View convertView ;
    NotificationsBean notificationsBean ;
    EditText msg_edittxt;
    HttpRequest httpRequest ;
    TextView delete_notification_btn;
    ShowUserMessage showUserMessage ;


    public void setInstanse(NotificationsBean notificationsBean)
    {
      this.notificationsBean = notificationsBean ;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.coach_notificatin_layout , null);
        DirectorFragmentManageActivity.updateTitle("Notification Detail");
        msg_edittxt = (EditText) convertView.findViewById(R.id.msg_edittxt);
        httpRequest = new HttpRequest(getActivity());
        delete_notification_btn = (TextView) convertView.findViewById(R.id.delete_notification_btn);
        delete_notification_btn.setOnClickListener(onClickListener);
        showUserMessage = new ShowUserMessage(getActivity());

        setNoticationDetailOnView();


        return convertView;
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            if (v.getId() == R.id.delete_notification_btn)
            {
                showUserMessage.showDialogBoxWithYesNoButton("Are you sure you want delete this notification?", new DialogBoxButtonListner() {
                    @Override
                    public void onYesButtonClick(DialogInterface dialog) {
                        dialog.cancel();
                        deleteNotification();

                    }
                });
            }
        }
    };



     public  void deleteNotification()
     {
         HashMap<String, Object> params= new HashMap<String, Object>();
         params.put("notifications_id", notificationsBean.getNotifications_id());
         httpRequest.getResponse(getActivity(), WebService.deletenotification, params, new OnServerRespondingListener(getActivity()) {
             @Override
             public void onSuccess(JSONObject jsonObject)
             {
                 if (jsonObject.optBoolean("status"))
                 {
                     ShowUserMessage.showMessageForFragmeneWithBack(getActivity() , jsonObject.optString("message"));
                 }
                 else
                 {
                    ShowUserMessage.showUserMessage(getActivity() , jsonObject.optString("message"));
                 }

             }
         });

     }

    public void  setNoticationDetailOnView()
    {
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("notifications_id", notificationsBean.getNotifications_id());
        params.put("notification_type", notificationsBean.getNotification_type());
        params.put("user_id", SessionManager.getUserId(getActivity()));
        httpRequest.getResponse(getActivity(), WebService.notificationdetail, params, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject) {

                Log.e("jsonObject" , jsonObject+"");


                if (jsonObject.optBoolean("status"))
                {

                    String senderName = jsonObject.optString("sender");
                    String message = jsonObject.optString("notification_message");
                    String notification_reason = jsonObject.optString("notification_reason");
                    String notificationtime = jsonObject.optString("notificationtime");

                    if(Validation.isStringNullOrBlank(notification_reason))
                    {
                        msg_edittxt.setText("Sender Name : "+senderName+"\n\nMessage : "+message+"\n\nDate : "+notificationtime);

                    }
                    else
                    {
                        msg_edittxt.setText("Sender Name : "+senderName+"\n\nMessage : "+message+"\n\nReason : "+notification_reason+"\n\nDate : "+notificationtime);

                    }




                }
                else
                {
                    ShowUserMessage.showUserMessage(getActivity() , jsonObject.optString("message"));
                }



            }
        });





    }


}
