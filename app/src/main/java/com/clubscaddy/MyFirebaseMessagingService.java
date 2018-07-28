package com.clubscaddy;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;

import com.clubscaddy.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.fragment.NotificationsFragment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by administrator on 23/11/16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
       /* // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
     Map<String ,String> map =  remoteMessage.getData();
String message = map.get("price");
        //Log.e( "Notification Message Body: " , ""+message );


    // Toast.makeText(getApplicationContext() ,message+"",1).show();

        ShowUserMessage.showUserMessage(getApplicationContext() ,remoteMessage.getData()+"");


        try {
            try {
                JSONObject json = new JSONObject(message);
                ShortcutBadger.applyCount(getApplicationContext(), Integer.parseInt(json.getString("badge") ));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }// getExtras().getString("badge")

        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

//{"message":"Event: A new event 'kratika' has been added","status":"true"}
        // displayMessage(context, message);
        // notifies user
        try {
           // String msg = remoteMessage.getData().toString();

            JSONObject jsonObject = new JSONObject(remoteMessage.getData().toString()).getJSONObject("price");


            generateNotification(getApplicationContext(), jsonObject.getString("message"));
        }
        catch (Exception e)
        {
            generateNotification(getApplicationContext(), e.getMessage());

        }

    }



    private static void generateNotification(Context context, String message) {








        int requestId = (int) System.currentTimeMillis();
        if(SessionManager.getUser_id(context) == null){
            Utill.showToast(context, "Please Login in clubscaddy.");
            return ;
        }

        int icon = R.drawable.launcher;
        long when = System.currentTimeMillis();
        JSONObject json;
        try {
            json =  new JSONObject(message);
            message = json.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(DirectorFragmentManageActivity.mFragmentManager!=null){
            Fragment currentFragment = DirectorFragmentManageActivity.mFragmentManager.findFragmentByTag(DirectorFragmentManageActivity.NotificationsFragment_tag);
            if(currentFragment!=null && currentFragment.isVisible()){
                NotificationsFragment f = (NotificationsFragment) currentFragment;
                f.getNotifications();
                return;
            }

        }











        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String title = context.getString(R.string.app_name);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(icon)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setSound(alarmSound)
                        .setAutoCancel(true);



        Intent notificationIntent = new Intent(context, DirectorFragmentManageActivity.class);
        notificationIntent.putExtra("notificationId", requestId);

        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, (int) when, notificationIntent, 0);
        mBuilder.setContentIntent(intent);
        notificationManager.notify((int)System.currentTimeMillis(), mBuilder.build());
        ;

    }

}
