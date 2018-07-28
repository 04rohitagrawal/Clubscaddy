package com.clubscaddy;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.clubscaddy.utility.SessionManager;

/**
 * Created by administrator on 23/11/16.
 */

public class FirebaseIDService extends FirebaseInstanceIdService
{
    private static final String TAG = "FirebaseIDService";
SessionManager sessionManager ;

//fMYYhNYI-8I:APA91bHaostpqFev6tAxcHdwS08TcHmO9xyfFpS6lUxDyzyKASrs5m-TDivhPYd7r8hsfhjofG0x0YLP0WFYxEh18bey7JalEcOUMOMgUclUfyeI1rpSQko7zaptIGGxtAH7HfE7DP19
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
       SessionManager.setUserDeviceToken(getApplicationContext() ,refreshedToken);
       sessionManager = new SessionManager();
        sessionManager.setUser_device_new_token(getApplicationContext() , refreshedToken);


        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }
}
