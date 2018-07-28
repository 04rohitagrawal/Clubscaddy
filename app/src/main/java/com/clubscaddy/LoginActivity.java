package com.clubscaddy;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.clubscaddy.BackGroundServies.FetchMemberListInBack;
import com.clubscaddy.Interface.DialogBoxButtonListner;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.utility.ShowUserMessage;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.clubscaddy.Bean.LoginBean;
import com.clubscaddy.Bean.UserClub;
import com.clubscaddy.R;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.custumview.InternetConnection;
import com.clubscaddy.custumview.KeyboadSetting;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;

import com.clubscaddy.utility.SqlListe;


public class LoginActivity extends AppCompatActivity {


    //FaceBook Login variable


    String face_book_link = "" ;


    static final String TAG = "LoginActivity";


    TextView tett ;



    boolean isFaceBookbtn = false;

    int mTank;
    // The helper object
    SqlListe sqlListe;
    LinearLayout login_with_face_book_tv;
    String Tag = getClass().getName();
    Context mContext;
    ImageView login_logo;
    EditText login_email, login_password;
    TextView login_forgotpw;
    TextView login_loginBtn;
    CheckBox term_condtion_check_box;
    TextView term_use_tv, sign_up_tv, privat_policy_tv;
    Intent I;

    HttpRequest httpRequest;


    OnClickListener addToLogin = new OnClickListener() {

        @Override
        public void onClick(View v) {
            verfiyLogin();
            //showDialgonActivity(getResources().getString(R.string.empty_email), LoginActivity.this);


        }
    };
    OnClickListener addToForgot = new OnClickListener() {

        @Override
        public void onClick(View v) {

            forgotPW();
        }
    };
    OnClickListener addTotermcond = new OnClickListener() {

        @Override
        public void onClick(View v) {


            Intent intent = new Intent(LoginActivity.this, TermandconditionActivity.class);

            if (v.getId() == R.id.term_use_tv) {
                intent.putExtra("status", "1");
            }
            if (v.getId() == R.id.privat_policy_tv) {
                intent.putExtra("status", "2");
            }
            startActivity(intent);
        }
    };
    OnClickListener signup = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, Signupactivity.class);
            startActivity(intent);
            //forgotPW();

        }
    };
    // Called when consumption is complete
        ProgressDialog pd;
    boolean isshowdialog;
    private CallbackManager callbackManager;
TextView bottom_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActionBar().hide();
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
        setContentView(R.layout.login_xml);

        bottom_tv = (TextView) findViewById(R.id.bottom_tv);

        tett = (TextView) findViewById(R.id.tett);
        tett.setText("Version "+AppConstants.currentVersion1);
		/*sqlListe = new SqlListe(this);

		UserClub userClub = new UserClub();
		sqlListe.setUserClubInfo();*/

        login_with_face_book_tv = (LinearLayout) findViewById(R.id.login_with_face_book_tv);

        httpRequest = new HttpRequest(this);
        callbackManager = CallbackManager.Factory.create();

        bottom_tv.setText("Copyright © 2018, Meera Technology LLC. All rights reserved.");
        login_with_face_book_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isFaceBookbtn = true;
                LoginManager.getInstance().logOut();
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));


            }
        });


        isFaceBookbtn = true;

        loadData();

        getAppKeyHash();




        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code

                                try {

                                    if (object.has("email")) {
                                        String email = object.getString("email");

                                        //Toast.makeText(getApplicationContext() , email ,1).show();

                                        face_book_link = object.getString("link");
                                        HashMap<String , Object > param = new   HashMap<String , Object >() ;
                                        param.put("user_email" ,  email);
                                        param.put("fb" ,face_book_link);
                                        param.put("user_device_token" ,SessionManager.getUser_Device_Token(getApplicationContext()));
                                        param.put("user_device_type" , "1");
                                        sendRequestForLogin(param);




                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,link");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error)
            {
                String s = error.getMessage();

                Log.e("s" , s);

            }
        });


        //


        mContext = this;
        try {
            getGCMId();
        } catch (Exception e) {

        }

        login_logo = (ImageView) findViewById(R.id.login_logo);
        login_email = (EditText) findViewById(R.id.login_email);
        login_email.setText(SessionManager.getUser_email(getApplicationContext()));
        login_password = (EditText) findViewById(R.id.login_password);
        login_forgotpw = (TextView) findViewById(R.id.login_forgotpw);
        login_loginBtn = (TextView) findViewById(R.id.login_loginBtn);

        term_condtion_check_box = (CheckBox) findViewById(R.id.term_condtion_check_box);
        term_use_tv = (TextView) findViewById(R.id.term_use_tv);
        privat_policy_tv = (TextView) findViewById(R.id.privat_policy_tv);

        sign_up_tv = (TextView) findViewById(R.id.sign_up_tv);

        login_loginBtn.setOnClickListener(addToLogin);
        login_forgotpw.setOnClickListener(addToForgot);
        term_use_tv.setOnClickListener(addTotermcond);
        privat_policy_tv.setOnClickListener(addTotermcond);

        Utill.setUnderLine(privat_policy_tv);
        Utill.setUnderLine(term_use_tv);

        sign_up_tv.setOnClickListener(signup);


        try {
            if (SessionManager.getClubType(getApplicationContext()).equals("2")) {
                /*Calendar calender = Calendar.getInstance();
				Calendar today = Calendar.getInstance();

				calender.set(Calendar.DATE, Integer.parseInt(SessionManager.getReminderdays(getApplicationContext()).split(" ")[1]));
				calender.set(Calendar.YEAR, Integer.parseInt(SessionManager.getReminderdays(getApplicationContext()).split(" ")[3]));
				calender.set(Calendar.MONTH,AppConstants.getMonthIndex(SessionManager.getReminderdays(getApplicationContext()).split(" ")[2])-1);
				Log.e("calender", calender.get(Calendar.DATE)+" "+calender.get(Calendar.MONTH)+" "+calender.get(Calendar.YEAR));

				//63625478399999
				long diff = calender.getTimeInMillis() - today.getTimeInMillis();

				AppConstants.setAutorenewdate(calender);
				long days = diff / (24 * 60 * 60 * 1000);


				if(days >0)
					movetonextlogin("Your trial period of this app expires on "+days+" days");
				else
				{
					showDialogforfinish("Your demo for this app is over");
				}*/
            }
            if (SessionManager.getClubType(getApplicationContext()).equals("1")) {
                if (SessionManager.getUserLoginApp(getApplicationContext()).equals("2") && (SessionManager.getUser_id(mContext) != null)) {

                    getInventry(false);


                    return;
                }

            }
        } catch (Exception e) {

        }



    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(Tag, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(Tag, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(Tag, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(Tag, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(Tag, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(Tag, "onDestroy");

    }

    private void getGCMId() {
		/*GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			GCMRegistrar.register(this, WebService.SENDER_ID);
		}if (GCMRegistrar.isRegisteredOnServer(this)) {
			// Skips registration.
			Log.e(Tag, "GCM already registered");
		}  */
    }

    private void clearEditText() {
        login_email.setText("");
        login_password.setText("");
    }

    private void verfiyLogin() {
        if (!Validation.isEmailAddress(login_email, true)) {
            showDialgonActivity(getResources().getString(R.string.correct_email));
        } else if (login_email.getText().toString().equals("")) {
            //ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.empty_email));

            //Utill.showDialgonActivity(getResources().getString(R.string.empty_email), LoginActivity.this);

        } else if (login_password.getText().toString().equals("")) {
            showDialgonActivity(getResources().getString(R.string.empty_password));
        } else {
            if (!term_condtion_check_box.isChecked()) {
                showDialgonActivity("Please accept terms of use and privacy policy");
            } else {
                KeyboadSetting.hideKeyboard(mContext, login_password);


                HashMap<String , Object > param = new   HashMap<String , Object >() ;
                param.put("user_email" ,  login_email.getText().toString());
                param.put("user_password" ,login_password.getText().toString());
                param.put("user_device_token" ,SessionManager.getUser_Device_Token(getApplicationContext()));
                param.put("user_device_type" , "1");
                sendRequestForLogin(param);
                        }
        }

    }

    private void forgotPW() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.forgotpw_xml);
        final EditText forgot_email = (EditText) dialog.findViewById(R.id.forgotpw_email);
        Button forgotpw_submit = (Button) dialog.findViewById(R.id.forgotpw_submit);
        Button forgotpw_cancel = (Button) dialog.findViewById(R.id.forgotpw_cancel);

        KeyboadSetting.hideKeyboard(mContext);
        KeyboadSetting.showKeyboard(mContext, forgot_email);

        forgotpw_submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!Validation.isEmailAddress(forgot_email, true)) {
                    showDialgonActivity(getResources().getString(R.string.correct_email));
                    //showDialgonactivity(getResources().getString(R.string.correct_email), LoginActivity.this);
                } else if (forgot_email.getText().toString().equals("")) {
                    showDialgonActivity(getResources().getString(R.string.correct_email));
                } else {
                    dialog.dismiss();

                    HashMap<String , Object> param = new HashMap<String, Object>();
                    param.put("user_email", forgot_email.getText().toString());
                    param.put("user_device_type", "1");
                    param.put("user_device_token", SessionManager.getUser_Device_Token(getApplicationContext()));
                    httpRequest.getResponse(LoginActivity.this, WebService.forgotpassword, param, new OnServerRespondingListener(LoginActivity.this) {
                        @Override
                        public void onSuccess(JSONObject jsonObject)
                        {
                            try
                            {
                                showDialgonActivity(jsonObject.getString("msg"));
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(getApplicationContext() , e.getMessage() , Toast.LENGTH_LONG).show();
                            }

                        }
                    });





                }
            }
        });

        forgotpw_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @SuppressWarnings("deprecation")
    public void showDialgonActivity(String msg) {

        final AlertDialog alertDialog = new AlertDialog.Builder(
                mContext ,AlertDialog.THEME_HOLO_LIGHT).create();

        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.app_name));

        // Setting Dialog Message
        alertDialog.setMessage(msg);

        // Setting Icon to Dialog


        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                alertDialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void onBuyGasButtonClicked(View view) {
        Log.d(TAG, "Buy gas button clicked.");


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener

        Log.d(TAG, "Launching purchase flow for gas.");

		/* TODO: for security, generate your payload here for verification. See the comments on
		 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
		 *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";


    }

    // User clicked the "Upgrade to Premium" button.
    public void onUpgradeAppButtonClicked(View arg0) {
        Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");
        setWaitScreen(true);

		/* TODO: for security, generate your payload here for verification. See the comments on
		 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
		 *        an empty string, but on a production app you should carefully generate this. */

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling



        if (isFaceBookbtn) {
            super.onActivityResult(requestCode, resultCode, data);
            isFaceBookbtn = false;
            callbackManager.onActivityResult(requestCode, resultCode, data);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);

        //Toast.makeText(getApplicationContext(), "resultCode "+resultCode, Toast.LENGTH_LONG).show();
        if (resultCode == -1) {
            AppConstants.setAutorenewdate(Calendar.getInstance());


            AQuery aQuery = new AQuery(getApplicationContext());

            if (InternetConnection.isInternetOn(getApplicationContext())) {

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("user_id", SessionManager.getUser_id(getApplicationContext()));
                params.put("club_id", SessionManager.getUser_Club_id(getApplicationContext()));


                aQuery.ajax(WebService.purchasedapp, params, JSONObject.class, new AjaxCallback<JSONObject>() {


                    @Override
                    public void callback(String url, JSONObject object, AjaxStatus status) {
                        // TODO Auto-generated method stub
                        super.callback(url, object, status);
                    }
                });
            }


            movetonext();
        }


    }



    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        //alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    void saveData() {

		/*
		 * WARNING: on a real application, we recommend you save data in a secure way to
		 * prevent tampering. For simplicity in this sample, we simply store the data using a
		 * SharedPreferences.
		 */

        SharedPreferences.Editor spe = getPreferences(MODE_PRIVATE).edit();
        spe.putInt("tank", mTank);
        spe.commit();
        Log.d(TAG, "Saved data: tank = " + String.valueOf(mTank));
    }

    void loadData() {
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        mTank = sp.getInt("tank", 2);
        Log.d(TAG, "Loaded data: tank = " + String.valueOf(mTank));
    }


    void setWaitScreen(boolean set) {

    }

    public void getInventry(boolean isshowdialog) {
        this.isshowdialog = isshowdialog;
        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("Processing...");
        pd.setCancelable(false);
        //pd.show();
    }



    /*
     */


    @SuppressWarnings("deprecation")
    public void movetonextlogin(String msg) {

        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.app_name));
        alertDialog.setCancelable(false);
        // Setting Dialog Message
        alertDialog.setMessage(msg);

        // Setting Icon to Dialog


        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                clearEditText();

                Intent intent = new Intent(getApplicationContext(), ProfileWizardActivity.class);
                intent.putExtra("action" ,1);
                startActivity(intent);

                finish();




				/*if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_DIRECTOR))
					//		I = new Intent(mContext,DirectorFragmentManageActivity.class);
					I = new Intent(mContext,DirectorFragmentManageActivity.class);
				else if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MOBILE_ADMIN)){
					I = new Intent(mContext,DirectorFragmentManageActivity.class);
				}else if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_COACH)){
					I = new Intent(mContext,DirectorFragmentManageActivity.class);
				}
				else if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER)){
					//I = new Intent(mContext,MemberFragmenttManagerActivity.class);
					I = new Intent(mContext,DirectorFragmentManageActivity.class);
				}
				else {
					I = new Intent(mContext,DirectorFragmentManageActivity.class);
					//I = new Intent(mContext,DirectorFragmentManageActivity.class);
				}
				startActivity(I);
				finish(); */


                // Write your code here to execute after dialog closed
                //    Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });

        // Showing Alert Message
        alertDialog.show();



    }

    public void movetonext() {


        SessionManager.setPassword(getApplicationContext() , login_password.getText().toString());
        Intent intent = new Intent(getApplicationContext(), ProfileWizardActivity.class);
        intent.putExtra("action" ,1);

        SessionManager sessionManager = new SessionManager();
        sessionManager.setLoginStep(getApplicationContext(), 2);
        startActivity(intent);

        finish();



		/*clearEditText();
		*/
    }


    public static void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES); //Your            package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    private void getAppKeyHash() {
        try {

            String packageName = getPackageName() ;
            PackageInfo info = getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;

                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), Base64.DEFAULT));
                Log.d("Hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

    }

SessionManager sessionManager ;
    public class LoginListener {
        public void onSuccess(String message) {
            String password = login_password.getText().toString();

            sessionManager = new SessionManager();

           if(!Validation.isStringNullOrBlank(password))
           {
               sessionManager.setAppLoginType(getApplicationContext() , 1);
               onSuccessLogin();
           }
           else
            {
                sessionManager.setAppLoginType(getApplicationContext() , 2);
                SharedPreferences sharePref = mContext.getSharedPreferences("saveUser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharePref.edit();
                editor.putString("user_linkedin", "");
                editor.putString("user_instagram", "");
                editor.putString("user_facebook", face_book_link);
                editor.putString("user_twitteter", "");
                editor.commit();
                onSuccessLogin();

            }



        }

        public void onError(String message) {
            showDialgonActivity(message);
            //ShowUserMessage.showUserMessage(mContext, message);
        }
    }




    public void onSuccessLogin()
    {
        SessionManager sessionManager = new SessionManager();
        SessionManager.setPassword(getApplicationContext() , login_password.getText().toString());



        SqlListe sqlListe = new SqlListe(getApplicationContext());
        if(sqlListe.getAllClub().size() == 0)
        {
            return;
        }

        if(sqlListe.getAllClub().size() == 1)
        {






            LoginBean loginBean = new LoginBean();
            UserClub userClub = sqlListe.getAllClub().get(0);


            loginBean.setUser_profilepic(SessionManager.getUserProfilePic(getApplicationContext()));
            loginBean.setUser_sport_type(SessionManager.getSport_type(getApplicationContext()));
            loginBean.setRemaining_days(SessionManager.getReminderdays(getApplicationContext()));
            loginBean.setSixmonth_price(sessionManager.getSixMonthPrice(getApplicationContext()));
            loginBean.setOneyearprice(sessionManager.getOneYearPrice(getApplicationContext()));
            loginBean.setUser_password(SessionManager.getUser_Password(getApplicationContext()));
            loginBean.setUser_facebook(SessionManager.getUserFacebookLink1(getApplicationContext()));



            loginBean.setUser_email(userClub.getUser_email());
            //loginBean.setUser_password(mObj.optString("user_password"));
            loginBean.setUser_device_token(userClub.getUser_device_token());
            loginBean.setUser_device_type(userClub.getUser_type()+"");
            loginBean.setUserid(userClub.getUser_id()+"");

            loginBean.setUser_club_id(userClub.getUser_club_id()+"");
            loginBean.setClub_logo(userClub.getClub_logo());
            loginBean.setClub_status_message(userClub.getClub_status_message());
            loginBean.setUser_type(userClub.getUser_type()+"");
            loginBean.setUser_first_name(userClub.getUser_first_name());
            loginBean.setUser_last_name(userClub.getUser_last_name());
            loginBean.setClubname(userClub.getClub_name());
            loginBean.setUser_login_app(userClub.getUser_login_app()+"");
            loginBean.setClubtype(userClub.getClubtype()+"");
            loginBean.setUser_id(userClub.getUser_id()+"");
            loginBean.setClub_rating(userClub.getClub_rating());
            loginBean.setClub_ratting_show(userClub.getClub_ratting_show());

            loginBean.setUser_rating(userClub.getUser_rating());
            loginBean.setClub_rating_type(userClub.getClub_rating_type());
            loginBean.setMobileNumber(userClub.getUser_phone());
            loginBean.setUser_facebook(userClub.getUser_facebook());
            loginBean.setUser_twitteter(userClub.getUser_twitteter());
            loginBean.setUser_instagram(userClub.getUser_instagram());
            loginBean.setUser_linkedin(userClub.getUser_linkedin());
            loginBean.setUser_login_app(userClub.getUser_login_app()+"");
            loginBean.setClub_score_view(userClub.getClub_score_view());
            loginBean.setSport_name(userClub.getSport_name());
            loginBean.setSport_player(userClub.getSport_player());
            loginBean.setUser_profilepic(userClub.getUser_profilepic());
            loginBean.setSport_field_name(userClub.getSport_field_name());

            loginBean.setCurrencyCode(userClub.getCurrencyCode());








            //Toast.makeText(mContext, "sport_type === "+mObj.optString("sport_type"), 1).show();
            SessionManager.onSaveUser(getApplicationContext(), loginBean);

            getClubLogoDemoPeriode();




        }
        else
        {

            Intent intent = new Intent(getApplicationContext(), ClubListActivity.class);
            intent.putExtra("activityAction" ,1);

            sessionManager.setLoginStep(getApplicationContext(), 2);
            startActivity(intent);
            finish();

        }
    }







    public class ForgotListener {
        public void onSuccess(String message) {
            showDialgonActivity(message);
        }

        public void onError(String message) {
            showDialgonActivity(message);
        }
    }











    public void getClubLogoDemoPeriode()
    {
        try
        {



            Calendar calender = Calendar.getInstance();
            Calendar today = Calendar.getInstance();
            calender.set(Calendar.DATE, Integer.parseInt(SessionManager.getReminderdays(getApplicationContext()).split(" ")[1]));
            calender.set(Calendar.YEAR, Integer.parseInt(SessionManager.getReminderdays(getApplicationContext()).split(" ")[3]));
            calender.set(Calendar.MONTH, AppConstants.getMonthIndex(SessionManager.getReminderdays(getApplicationContext()).split(" ")[2])-1);
            Log.e("calender", calender.get(Calendar.DATE)+" "+calender.get(Calendar.MONTH)+" "+calender.get(Calendar.YEAR));

            //63625478399999
            long diff = calender.getTimeInMillis() - today.getTimeInMillis();

            AppConstants.setAutorenewdate(calender);
            long days = diff / (24 * 60 * 60 * 1000);


            if (sessionManager.getUserLoginAppInfo(getApplicationContext()) ==1 )
            {
                if(sessionManager.getClubTypeInfo(getApplicationContext()) != 3)
                {
                    movetonextlogin("Your trial period of this app expires on "+SessionManager.getReminderdays(getApplicationContext()));

                }
                else
                {
                    movetonext();
                }

            }
            else
            {
                if(sessionManager.getClubTypeInfo(getApplicationContext()) == 2)
                {
                    showDialogforfinish("Your demo for this app is over");
                }else {
                    movetonext();

                }
            }



        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }













    public void showDialogforfinish(String msg)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this ,AlertDialog.THEME_HOLO_LIGHT).create();

        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.app_name));
        alertDialog.setCancelable(false);
        // Setting Dialog Message
        alertDialog.setMessage(msg);

        // Setting Icon to Dialog


        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {





                alertDialog.cancel();







                // Write your code here to execute after dialog closed
                //    Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });

        // Showing Alert Message
        alertDialog.show();







    }

    ShowUserMessage showUserMessage ;


     public void sendRequestForLogin(HashMap<String , Object> param)
     {
         httpRequest.getResponse(LoginActivity.this, WebService.login, param, new OnServerRespondingListener(LoginActivity.this) {
             @Override
             public void onSuccess(final JSONObject jsonObject)
             {
                try
                {

                    if (jsonObject.getBoolean("status"))
                    {

                        double appVersion = Double.parseDouble(jsonObject.getString("a_version"));


                        JsonUtility jsonUtility = new JsonUtility();
                        jsonUtility.loginDataParseAndSaveData(jsonObject , LoginActivity.this);

                        String password = login_password.getText().toString();

                        sessionManager = new SessionManager();

                        if(!Validation.isStringNullOrBlank(password))
                        {
                            sessionManager.setAppLoginType(getApplicationContext() , 1);
                            onSuccessLogin();
                        }
                        else
                        {
                            sessionManager.setAppLoginType(getApplicationContext() , 2);
                            SharedPreferences sharePref = mContext.getSharedPreferences("saveUser", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharePref.edit();
                            editor.putString("user_linkedin", "");
                            editor.putString("user_instagram", "");
                            editor.putString("user_facebook", face_book_link);
                            editor.putString("user_twitteter", "");
                            editor.commit();
                            onSuccessLogin();

                        }
                       // Toast.makeText(getApplicationContext() , "Login successfully!" , Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        showDialgonActivity(jsonObject.getString("msg"));
                       //

                    }

                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext() ,e.getMessage() , Toast.LENGTH_LONG).show();
                }
             }
         });


     }

















}
