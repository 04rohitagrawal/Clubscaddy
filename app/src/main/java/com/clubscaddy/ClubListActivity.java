package com.clubscaddy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clubscaddy.Adapter.ClubListAdapter;
import com.clubscaddy.Bean.LoginBean;
import com.clubscaddy.Bean.UserClub;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.SessionManager;

import com.clubscaddy.utility.SqlListe;
import com.clubscaddy.R;

import java.util.Calendar;

/**
 * Created by administrator on 30/11/16.
 */

public class ClubListActivity extends AppCompatActivity
{

    ListView club_list_view;
    SqlListe sqlListe ;
    static final String TAG = "LoginActivity";
    static final int RC_REQUEST = 10001;
    public static  String SKU_GAS = "sixmonthproduct";
    static final int TANK_MAX = 4;

    SessionManager sessionManager ;


    int mTank;
    boolean isFaceBookbtn =false;
    public static String  purchesitem1 = "sixmonthproduct";
    public static String  purchesitem2 = "oneyearproduct";

    TextView title;
ImageView back_button;
    TextView skip_btn;


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_list_layout);

        title = (TextView) findViewById(R.id.title);
        title.setText("Clubs");

        skip_btn = (TextView) findViewById(R.id.skip_btn);

        back_button = (ImageView) findViewById(R.id.back_button);
        sessionManager = new SessionManager();


        if(getIntent().getIntExtra("activityAction",1)  ==1)
        {
            sessionManager.setLoginStep(getApplicationContext() , 1);
        }

        back_button.setVisibility(View.GONE);
        skip_btn.setVisibility(View.GONE);
        club_list_view = (ListView) findViewById(R.id.club_list_view);

        sqlListe = new SqlListe(getApplicationContext());

        club_list_view.setAdapter(new ClubListAdapter(this , sqlListe.getAllClub()));


        if(getIntent().getIntExtra("activityAction",1)  ==2)
        {
            back_button.setVisibility(View.VISIBLE);
        }
        else
        {
            back_button.setVisibility(View.GONE);
        }


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

            String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1TZSsixaeFQyIRKDsbmZZj32O2ebTIGAmcA9dd9AABTNUPO11ADdxf6RAV7tKDfb0NRI2V6OavbrUPSAreHVdwOsJ5uJd736bJR4+x+ukR6zC8uHO2j/xLHy01sX7tfyMyX1/RK5LnjYnA71Yle64RkHek/lbwzpUv/yfrHGmEhRHOIQZ9Xek9pnUdet4yoArQmu136yqkl1juMDIEAaHR3WG5L24qwAbtDUS4hcIlE6rSaKQdO9mt3o7jVSffh32LK4Bo5hML3/r3lRkJ5zy+URDYN3y+1qWyveXWbntnF8YNfQub8ae8aZjjtylzHOtkDVxopAFDmFcJlUHOT4bwIDAQAB";

        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
        if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR"))
        {
            throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
        }
        if (getPackageName().startsWith("com.example"))
        {
            throw new RuntimeException("Please change the sample's package name! See README.");
        }




        club_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {


                //intent.putExtra("activityAction" ,"2");



                if(getIntent().getIntExtra("activityAction",1)  ==1)
                {

                    LoginBean loginBean = new LoginBean();
                    UserClub userClub = sqlListe.getAllClub().get(position);

                    loginBean.setUser_profilepic(SessionManager.getUserProfilePic(getApplicationContext()));

                    loginBean.setRemaining_days(SessionManager.getReminderdays(getApplicationContext()));
                    loginBean.setSixmonth_price(sessionManager.getSixMonthPrice(getApplicationContext()));
                    loginBean.setOneyearprice(sessionManager.getOneYearPrice(getApplicationContext()));
                    loginBean.setUser_password(SessionManager.getUser_Password(getApplicationContext()));
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

                    loginBean.setMobileNumber(userClub.getUser_phone());
                    loginBean.setUser_facebook(userClub.getUser_facebook());
                    loginBean.setUser_twitteter(userClub.getUser_twitteter());
                    loginBean.setUser_instagram(userClub.getUser_instagram());
                    loginBean.setUser_linkedin(userClub.getUser_linkedin());
                    loginBean.setUser_login_app(userClub.getUser_login_app()+"");

                    loginBean.setClub_rating_type(userClub.getClub_rating_type());
                    loginBean.setUser_rating(userClub.getUser_rating());

                    loginBean.setClub_score_view(userClub.getClub_score_view());
                    loginBean.setClub_rating(userClub.getClub_rating());

                    loginBean.setSport_name(userClub.getSport_name());
                    loginBean.setUser_profilepic(userClub.getUser_profilepic());
                    loginBean.setUser_sport_type(userClub.getSport_type());

                    loginBean.setSport_player(userClub.getSport_player());
                    loginBean.setSport_field_name(userClub.getSport_field_name());
                    loginBean.setClub_status_change_date(userClub.getClub_status_change_date());
                    loginBean.setCurrencyCode(userClub.getCurrencyCode());

                    sessionManager.setLoginStep(getApplicationContext() ,2);

                    //Toast.makeText(mContext, "sport_type === "+mObj.optString("sport_type"), 1).show();
                    SessionManager.onSaveUser(getApplicationContext(), loginBean);

                    getClubLogoDemoPeriode();

                }
                else
                {

                    UserClub userClub = sqlListe.getAllClub().get(position);

                    String user_club_id = SessionManager.getUser_Club_id(ClubListActivity.this);

                    if (Integer.parseInt(user_club_id) ==(userClub.getClub_id()))
                    {




                        AlertDialog alertDialog = new AlertDialog.Builder(
                         ClubListActivity.this ,  AlertDialog.THEME_HOLO_LIGHT).create();

                        // Setting Dialog Title
                        alertDialog.setTitle(SessionManager.getClubName(getApplicationContext()));

                        // Setting Dialog Message
                        alertDialog.setMessage("You have already login with this club.");

                        // Setting Icon to Dialog


                        // Setting OK Button
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed
                              //  Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();

































                        return;
                    }






                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ClubListActivity.this ,AlertDialog.THEME_HOLO_LIGHT);

                    // Setting Dialog Title
                    alertDialog.setTitle(SessionManager.getClubName(getApplicationContext()));

                    // Setting Dialog Message
                    alertDialog.setMessage("Are you sure you want to change club?");

                    // Setting Icon to Dialog


                    // Setting Positive "Yes" Button
                    alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {















                            LoginBean loginBean = new LoginBean();
                            UserClub userClub = sqlListe.getAllClub().get(position);

                            loginBean.setUser_profilepic(SessionManager.getUserProfilePic(getApplicationContext()));

                            loginBean.setRemaining_days(SessionManager.getReminderdays(getApplicationContext()));
                            loginBean.setSixmonth_price(sessionManager.getSixMonthPrice(getApplicationContext()));
                            loginBean.setOneyearprice(sessionManager.getOneYearPrice(getApplicationContext()));
                            loginBean.setUser_password(SessionManager.getUser_Password(getApplicationContext()));
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
                            loginBean.setMobileNumber(userClub.getUser_phone());
                            loginBean.setUser_facebook(userClub.getUser_facebook());
                            loginBean.setUser_twitteter(userClub.getUser_twitteter());
                            loginBean.setUser_instagram(userClub.getUser_instagram());
                            loginBean.setUser_linkedin(userClub.getUser_linkedin());
                            loginBean.setUser_login_app(userClub.getUser_login_app()+"");
                            loginBean.setUser_profilepic(userClub.getUser_profilepic());
                            loginBean.setClub_rating(userClub.getClub_rating());
                            loginBean.setClub_score_view(userClub.getClub_score_view());
                            loginBean.setClub_rating_type(userClub.getClub_rating_type());
                            loginBean.setUser_rating(userClub.getUser_rating());
                            loginBean.setSport_name(userClub.getSport_name());
                            loginBean.setUser_sport_type(userClub.getSport_type());
                            loginBean.setSport_player(userClub.getSport_player());
                            loginBean.setSport_field_name(userClub.getSport_field_name());
                            loginBean.setClub_status_change_date(userClub.getClub_status_change_date());
                            loginBean.setCurrencyCode(userClub.getCurrencyCode());


                            //
                            //Toast.makeText(mContext, "sport_type === "+mObj.optString("sport_type"), 1).show();
                            SessionManager.onSaveUser(getApplicationContext(), loginBean);











                            Intent I ;
                            if(SessionManager.getUser_type(getApplicationContext())!=null) {
                                if (SessionManager.getUser_type(getApplicationContext()).equals(AppConstants.USER_TYPE_DIRECTOR)) {
                                    I = new Intent(getApplicationContext(), DirectorFragmentManageActivity.class);
                                    I.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(I);
                                    finish();
                                } else if (SessionManager.getUser_type(getApplicationContext()).equals(AppConstants.USER_TYPE_MOBILE_ADMIN)) {
                                    I = new Intent(getApplicationContext(), DirectorFragmentManageActivity.class);
                                    I.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(I);
                                    finish();
                                } else if (SessionManager.getUser_type(getApplicationContext()).equals(AppConstants.USER_TYPE_MEMBER)) {
                                    I = new Intent(getApplicationContext(), DirectorFragmentManageActivity.class);
                                    //I = new Intent(mContext,MemberFragmenttManagerActivity.class);
                                    I.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                    startActivity(I);
                                    finish();
                                }
                            }


















                            dialog.cancel();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event

                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();



                }





            }
        });



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
                } else {
                   Intent I = new Intent(getApplicationContext(), ProfileWizardActivity.class);
                    startActivity(I);
                    finish();
                }
            }





        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), 1).show();
        }
    }



    ProgressDialog pd ;
    boolean isshowdialog ;





    public void movetonextlogin(String msg)
    {

        AlertDialog alertDialog = new AlertDialog.Builder(ClubListActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.app_name));
        alertDialog.setCancelable(false);
        // Setting Dialog Message
        alertDialog.setMessage(msg);

        // Setting Icon to Dialog


        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {



                Intent intent = new Intent(getApplicationContext() , ProfileWizardActivity.class);
                sessionManager.setLoginStep(getApplicationContext() , 2);
                startActivity(intent);

                finish();


            }
        });

        // Showing Alert Message
        alertDialog.show();












    }
    public void movetonext()
    {

        Intent intent = new Intent(getApplicationContext() , ProfileWizardActivity.class);
        startActivity(intent);
        finish();

    }


    public void showDialogforfinish(String msg)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(ClubListActivity.this).create();

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











    public void showDialogforpurchesproduct(String msg)
    {

        final Dialog dialog = new Dialog(ClubListActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.purchresdialog);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText(SessionManager.getClubName(getApplicationContext()));

        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText(msg);


        TextView sixmonth_tv = (TextView) dialog.findViewById(R.id.sixmonth_tv);
        SessionManager sessionManager = new SessionManager();
        sixmonth_tv.setText(sixmonth_tv.getText().toString()+"("+sessionManager.getSixMonthPrice(getApplicationContext())+")");

        TextView twalemonth_tv = (TextView) dialog.findViewById(R.id.twalemonth_tv);
        twalemonth_tv.setText(twalemonth_tv.getText().toString()+"("+sessionManager.getOneYearPrice(getApplicationContext())+")");

        TextView cancel_tv = (TextView) dialog.findViewById(R.id.cancel_tv);

        twalemonth_tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SKU_GAS = purchesitem2;
                dialog.cancel();
                //	purchesdate.set(year, month, day,dt.getHours(),dt.getMinutes(),dt.getSeconds());


                SessionManager sessionManager = new SessionManager();

                sessionManager.setAppPurchaseDate(getApplicationContext(),AppConstants.getAppDate(Calendar.getInstance()));
                sessionManager.setTimePeriode(getApplicationContext(), 6);

                onBuyGasButtonClicked(null);
            }
        });


        sixmonth_tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SessionManager sessionManager = new SessionManager();

                sessionManager.setAppPurchaseDate(getApplicationContext(),AppConstants.getAppDate(Calendar.getInstance()));
                sessionManager.setTimePeriode(getApplicationContext(), 12);
                SKU_GAS = purchesitem1;
                dialog.cancel();
                onBuyGasButtonClicked(null);
            }
        });


        cancel_tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });

        dialog.show();


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
    void setWaitScreen(boolean set) {

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
}
