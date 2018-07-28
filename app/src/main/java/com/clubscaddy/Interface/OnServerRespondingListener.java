package com.clubscaddy.Interface;

import android.app.Activity;


import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.R;

import org.json.JSONObject;


public abstract class OnServerRespondingListener
{
    Activity activity;

    public OnServerRespondingListener(Activity activity)
    {
        this.activity = activity;
    }

 public abstract void onSuccess(JSONObject jsonObject);
 public void onConnectionError()
 {

     ShowUserMessage.showUserMessage(activity ,activity.getResources().getString(R.string.check_internet_connection));
 }
 public void onNetWorkError()
 {
     ShowUserMessage.showUserMessage(activity ,"Network error!");
 }
 public  void internetConnectionProble()
 {
     ShowUserMessage.showUserMessage(activity ,"Please connect to internet!");


 }

    public  void onSuccess(MemberListBean memberListBean)
    {

    }

}
