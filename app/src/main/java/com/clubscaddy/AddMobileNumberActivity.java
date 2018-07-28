package com.clubscaddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.clubscaddy.Bean.CountryDatabean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by administrator on 21/11/16.
 */

public class AddMobileNumberActivity extends AppCompatActivity {
    HashMap<String, Object> params;
    ImageView back_button;
    TextView skip_btn;
    HttpRequest httpRequest;
    ArrayList<CountryDatabean> phoneCodeList;
    EditText contact_number;
    TextView continue_tv;
    SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_no_layout);

        sessionManager = new SessionManager();
        contact_number = (EditText) findViewById(R.id.contact_number);

        // mobile_no_spinner = (Spinner) findViewById(R.id.mobile_no_spinner);
        phoneCodeList = new ArrayList<CountryDatabean>();

        CountryDatabean countryDatabean = new CountryDatabean();
        countryDatabean.setPhonecode_country("India");
        countryDatabean.setPhonecode_val("91");
        phoneCodeList.add(countryDatabean);


        if (!Validation.isStringNullOrBlank(SessionManager.getPhoneNumber(getApplicationContext()))) {
            sessionManager.setLoginStep(getApplicationContext(), 4);
            Intent intent = new Intent(getApplicationContext(), AddSocialNetWorkActivity.class);
            startActivity(intent);
            finish();
            return;
            // Picasso.with(this).load(SessionManager.getClub_Logo(getApplicationContext())).placeholder(R.drawable.logo_profile).error(R.drawable.logo_profile).transform(new CircleTransform()).into(profile_pic_imageview);

        }


        httpRequest = new HttpRequest(this);

        params = new HashMap<String, Object>();


        continue_tv = (TextView) findViewById(R.id.continue_tv);

        continue_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* sessionManager.setLoginStep(getApplicationContext() ,4);
                Intent intent = new Intent(getApplicationContext() , AddReviewActivity.class);
                startActivity(intent);
                //finish();*/


                AppConstants.hideSoftKeyboard(AddMobileNumberActivity.this);

                if (contact_number.getText().toString().length() == 0) {
                    ShowUserMessage.showDialogOnActivity(AddMobileNumberActivity.this, "Please enter  mobile number.");
                    return;
                }

                if (contact_number.getText().toString().length() < 8) {
                    ShowUserMessage.showDialogOnActivity(AddMobileNumberActivity.this, "Please enter valid mobile number.");
                    return;
                }

                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("user_email", SessionManager.getUser_email(getApplicationContext()));
                params.put("user_phone", contact_number.getText().toString());

                httpRequest.getResponse(AddMobileNumberActivity.this, WebService.update_profile, params, new OnServerRespondingListener(AddMobileNumberActivity.this) {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        try {
                            //{"status":"true","message":"Profile Pic updated"}
                            Log.e("jsonObject", jsonObject + "");

                            if (jsonObject.getBoolean("status") == false) {
                                ShowUserMessage.showDialogOnActivity(AddMobileNumberActivity.this, jsonObject.getString("message"));
                            } else {
                                SessionManager.setPhoneNumber(getApplicationContext(), contact_number.getText().toString());

                                sessionManager.setLoginStep(getApplicationContext(), 4);
                                Intent intent = new Intent(getApplicationContext(), AddSocialNetWorkActivity.class);
                                startActivity(intent);
                                finish();
                            }


                        } catch (Exception e) {

                        }


                    }
                });


            }
        });

        back_button = (ImageView) findViewById(R.id.back_button);
        back_button.setVisibility(View.INVISIBLE);
        skip_btn = (TextView) findViewById(R.id.skip_btn);
        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setLoginStep(getApplicationContext(), 4);
                Intent intent = new Intent(getApplicationContext(), AddSocialNetWorkActivity.class);
                startActivity(intent);

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
