package com.clubscaddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.clubscaddy.Adapter.AppRatingAdapter;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.CircleTransform;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by administrator on 22/11/16.
 */

public class AddReviewActivity extends AppCompatActivity
{
    Spinner rate_list_spinner;

    ArrayList<String> ratingList ;
    TextView  continue_tv;

    ImageView back_button;
    ImageView profile_pic_imageview;
    TextView skip_btn;
    SessionManager sessionManager ;
    HttpRequest httpRequest ;



    Intent I;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_review_layout);

        rate_list_spinner = (Spinner) findViewById(R.id.rate_list_spinner);

        profile_pic_imageview = (ImageView) findViewById(R.id.profile_pic_imageview);




        httpRequest = new HttpRequest(this);


        sessionManager = new SessionManager();




        try
        {
            Picasso.with(this).load(SessionManager.getClub_Logo(getApplicationContext())).placeholder(R.drawable.default_club_profile).error(R.drawable.default_club_profile).transform(new CircleTransform()).into(profile_pic_imageview);

        }
        catch (Exception e)
        {

        }




        ratingList = new ArrayList<>();


        try
        {

            JSONArray club_rating_json_array = new JSONArray(sessionManager.getClubRatting(getApplicationContext()));



            ratingList.add("None");

            for(int i = 0 ; i < club_rating_json_array.length() ;i++)
            {
                ratingList.add(club_rating_json_array.getString(i));
            }


        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext() ,e.getMessage() ,Toast.LENGTH_LONG).show();
        }








        if(sessionManager.getClubRatingType(getApplicationContext()) == 1 || !Validation.isStringNullOrBlank(sessionManager.getUserRating(getApplicationContext())))

        {
            sessionManager.setLoginStep(getApplicationContext() ,6);

            if(SessionManager.getUser_type(getApplicationContext()).equalsIgnoreCase(AppConstants.USER_TYPE_DIRECTOR))
                //		I = new Intent(mContext,DirectorFragmentManageActivity.class);
                I = new Intent(getApplicationContext(),DirectorFragmentManageActivity.class);
            else if(SessionManager.getUser_type(getApplicationContext()).equalsIgnoreCase(AppConstants.USER_TYPE_MOBILE_ADMIN)){
                I = new Intent(getApplicationContext(),DirectorFragmentManageActivity.class);
            }else if(SessionManager.getUser_type(getApplicationContext()).equalsIgnoreCase(AppConstants.USER_TYPE_COACH)){
                I = new Intent(getApplicationContext(),DirectorFragmentManageActivity.class);
            }
            else if(SessionManager.getUser_type(getApplicationContext()).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER)){
                //I = new Intent(mContext,MemberFragmenttManagerActivity.class);
                I = new Intent(getApplicationContext(),DirectorFragmentManageActivity.class);
            }
            else {
                I = new Intent(getApplicationContext(),DirectorFragmentManageActivity.class);
                //I = new Intent(mContext,DirectorFragmentManageActivity.class);
            }
            I.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(I);
            finish();
            //  return;
        }





















        rate_list_spinner.setAdapter(new AppRatingAdapter(this , ratingList,rate_list_spinner));

        rate_list_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        rate_list_spinner.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return false;
            }
        });

        continue_tv = (TextView) findViewById(R.id.continue_tv);

        continue_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                HashMap<String ,Object>params = new HashMap<String, Object>();
                params.put("user_id" ,SessionManager.getUser_id(getApplicationContext()));

                if (rate_list_spinner.getSelectedItemPosition() != 0)
                {
                    params.put("user_rating" ,ratingList.get(rate_list_spinner.getSelectedItemPosition())+"");

                }
                else
                {
                    params.put("user_rating" ,"0");

                }



                httpRequest.getResponse(AddReviewActivity.this,WebService.update_profile ,params  , new OnServerRespondingListener(AddReviewActivity.this) {
                    @Override
                    public void onSuccess(JSONObject jsonObject)
                    {
                        Log.e("jsonObject" ,jsonObject.toString());
                        //{"":"Rating updated","":"true"}


                        try
                        {
                            if(jsonObject.getBoolean("status"))
                            {

                                SharedPreferences sharePref = getSharedPreferences("saveUser", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharePref.edit();
                                editor.putInt("user_rating" ,(int)rate_list_spinner.getSelectedItemId() );
                                editor.commit();

                                sessionManager.setLoginStep(getApplicationContext() ,6);
                                if(SessionManager.getUser_type(getApplicationContext()).equalsIgnoreCase(AppConstants.USER_TYPE_DIRECTOR))
                                    //		I = new Intent(mContext,DirectorFragmentManageActivity.class);
                                    I = new Intent(getApplicationContext(),DirectorFragmentManageActivity.class);
                                else if(SessionManager.getUser_type(getApplicationContext()).equalsIgnoreCase(AppConstants.USER_TYPE_MOBILE_ADMIN)){
                                    I = new Intent(getApplicationContext(),DirectorFragmentManageActivity.class);
                                }else if(SessionManager.getUser_type(getApplicationContext()).equalsIgnoreCase(AppConstants.USER_TYPE_COACH)){
                                    I = new Intent(getApplicationContext(),DirectorFragmentManageActivity.class);
                                }
                                else if(SessionManager.getUser_type(getApplicationContext()).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER)){
                                    //I = new Intent(mContext,MemberFragmenttManagerActivity.class);
                                    I = new Intent(getApplicationContext(),DirectorFragmentManageActivity.class);
                                }
                                else {
                                    I = new Intent(getApplicationContext(),DirectorFragmentManageActivity.class);
                                    //I = new Intent(mContext,DirectorFragmentManageActivity.class);
                                }
                                I.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(I);
                                finish();
                            }
                            else
                            {
                                ShowUserMessage.showDialogOnActivity(AddReviewActivity.this , jsonObject.getString("message"));
                            }

                        }
                        catch (Exception e)
                        {

                        }

                    }
                });








              /*
*/
            }
        });



        back_button = (ImageView) findViewById(R.id.back_button);
        back_button.setVisibility(View.INVISIBLE);
        skip_btn = (TextView) findViewById(R.id.skip_btn);
        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




               /* I = new Intent(getApplicationContext(),BackgroundService.class);
                startService(I);*/


                sessionManager.setLoginStep(getApplicationContext() ,6);
                if(SessionManager.getUser_type(getApplicationContext()).equalsIgnoreCase(AppConstants.USER_TYPE_DIRECTOR))
                    //		I = new Intent(mContext,DirectorFragmentManageActivity.class);
                    I = new Intent(getApplicationContext(),DirectorFragmentManageActivity.class);
                else if(SessionManager.getUser_type(getApplicationContext()).equalsIgnoreCase(AppConstants.USER_TYPE_MOBILE_ADMIN)){
                    I = new Intent(getApplicationContext(),DirectorFragmentManageActivity.class);
                }else if(SessionManager.getUser_type(getApplicationContext()).equalsIgnoreCase(AppConstants.USER_TYPE_COACH)){
                    I = new Intent(getApplicationContext(),DirectorFragmentManageActivity.class);
                }
                else if(SessionManager.getUser_type(getApplicationContext()).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER)){
                    //I = new Intent(mContext,MemberFragmenttManagerActivity.class);
                    I = new Intent(getApplicationContext(),DirectorFragmentManageActivity.class);
                }
                else {
                    I = new Intent(getApplicationContext(),DirectorFragmentManageActivity.class);
                    //I = new Intent(mContext,DirectorFragmentManageActivity.class);
                }
                I.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(I);
                finish();
                // finish();
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
