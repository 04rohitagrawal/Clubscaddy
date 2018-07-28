package com.clubscaddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.R;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by administrator on 22/11/16.
 */

public class AddSocialNetWorkActivity extends AppCompatActivity
{
    SessionManager sessionManager;
    TextView continue_tv;
    Intent I ;
    Context mContext;
    ImageView back_button;
    TextView skip_btn;
    EditText face_book_link_edit_tv;
    EditText linkedin_edit_tv;
    EditText instragram_edit_tv;
    EditText twitter_edit_tv;
    boolean isCallWebservices = false ;
HttpRequest httpRequest;

    String face_book_link ;
    String  twiitter_link ;
    String  instram_link ;
    String   linkedin_link ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_network_layout);
        sessionManager = new SessionManager();

        face_book_link = sessionManager.getUserFacebookLink(getApplicationContext());
        twiitter_link = sessionManager.getUserTwitterLink(getApplicationContext());
        instram_link = sessionManager.getUserInstragramLink(getApplicationContext());
        linkedin_link = sessionManager.getUserLinkedInLink(getApplicationContext());


        httpRequest = new HttpRequest(AddSocialNetWorkActivity.this);
        continue_tv = (TextView) findViewById(R.id.continue_tv);
        sessionManager = new SessionManager();
        mContext = getApplicationContext();



         face_book_link_edit_tv = (EditText) findViewById(R.id.face_book_link_edit_tv);
         linkedin_edit_tv  = (EditText) findViewById(R.id.linkedin_edit_tv);;
         instragram_edit_tv  = (EditText) findViewById(R.id.instragram_edit_tv);;
         twitter_edit_tv  = (EditText) findViewById(R.id.twitter_edit_tv);;


          if(!Validation.isStringNullOrBlank(face_book_link)||!Validation.isStringNullOrBlank(linkedin_link)||!Validation.isStringNullOrBlank(twiitter_link)||!Validation.isStringNullOrBlank(instram_link))
          {
             sessionManager.setLoginStep(getApplicationContext() ,5);
              Intent intent = new Intent(getApplicationContext() , AddReviewActivity.class);
              startActivity(intent);
              finish();
              return;
          }




        continue_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                isCallWebservices = false;
                if(!Validation.isStringNullOrBlank(face_book_link_edit_tv.getText().toString()) )
                {
                    if(Patterns.WEB_URL.matcher(face_book_link_edit_tv.getText().toString()).matches())
                    {
                        isCallWebservices = true;
                    }
                    else
                    {
                        ShowUserMessage.showDialogOnActivity(AddSocialNetWorkActivity.this ,"Please enter valid social media link.");
                        return;
                    }


                }
                if(!Validation.isStringNullOrBlank(linkedin_edit_tv.getText().toString()))
                {
                    if(Patterns.WEB_URL.matcher(linkedin_edit_tv.getText().toString()).matches())
                    {
                        isCallWebservices = true;
                    }
                    else
                    {
                        ShowUserMessage.showDialogOnActivity(AddSocialNetWorkActivity.this ,"Please enter valid social media link.");
                        return;
                    }
                }
                if(!Validation.isStringNullOrBlank(instragram_edit_tv.getText().toString()))
                {
                    if(Patterns.WEB_URL.matcher(instragram_edit_tv.getText().toString()).matches())
                    {
                        isCallWebservices = true;
                    }
                    else
                    {
                        ShowUserMessage.showDialogOnActivity(AddSocialNetWorkActivity.this ,"Please enter valid social media link.");
                        return;
                    }
                }
                if(!Validation.isStringNullOrBlank(twitter_edit_tv.getText().toString()))
                {
                    if(Patterns.WEB_URL.matcher(twitter_edit_tv.getText().toString()).matches())
                    {
                        isCallWebservices = true;
                    }
                    else
                    {
                        ShowUserMessage.showDialogOnActivity(AddSocialNetWorkActivity.this ,"Please enter valid social media link.");
                        return;
                    }
                }

                if(isCallWebservices == false)
                {
                  ShowUserMessage.showDialogOnActivity(AddSocialNetWorkActivity.this ,"Please enter atleast one social media link.");
                    return;
                }
                HashMap<String ,Object> params = new HashMap<String, Object>();
                params.put("user_email" ,SessionManager.getUser_email(getApplicationContext()));
                params.put("user_facebook" , face_book_link_edit_tv.getText().toString());
                params.put("user_insta" , instragram_edit_tv.getText().toString() );
                params.put("user_linkedin" ,  linkedin_edit_tv.getText().toString());
                params.put("user_twitter" ,  twitter_edit_tv.getText().toString());

                httpRequest.getResponse(AddSocialNetWorkActivity.this, WebService.update_profile ,params , new OnServerRespondingListener(AddSocialNetWorkActivity.this) {
                    @Override
                    public void onSuccess(JSONObject jsonObject)
                    {
                        try
                        {
                            //{"status":"true","message":"Profile Pic updated"}
                            Log.e("jsonObject" ,jsonObject+"");

                            if(jsonObject.getBoolean("status") == false)
                            {
                                ShowUserMessage.showDialogOnActivity(AddSocialNetWorkActivity.this , jsonObject.getString("message"));
                            }
                            else
                            {
                                SharedPreferences sharePref = mContext.getSharedPreferences("saveUser", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharePref.edit();
                                editor.putString("user_linkedin", linkedin_link);
                                editor.putString("user_instagram", instram_link);
                                editor.putString("user_facebook", face_book_link);
                                editor.putString("user_twitteter", twiitter_link);
                                editor.commit();



                                sessionManager.setLoginStep(getApplicationContext() ,5);
                                Intent intent = new Intent(getApplicationContext() , AddReviewActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            }


                        }
                        catch (Exception e)
                        {

                        }


                    }
                });

               /* */
            }
        });

        back_button = (ImageView) findViewById(R.id.back_button);
        back_button.setVisibility(View.INVISIBLE);
        skip_btn = (TextView) findViewById(R.id.skip_btn);
        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sessionManager.setLoginStep(getApplicationContext() ,5);
                Intent intent = new Intent(getApplicationContext() , AddReviewActivity.class);
                startActivity(intent);

                return;

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
