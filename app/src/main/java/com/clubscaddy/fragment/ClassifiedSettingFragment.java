package com.clubscaddy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.clubscaddy.Adapter.TimeSloatAdapter;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.R;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by administrator on 30/5/17.
 */

public class ClassifiedSettingFragment extends Fragment
{

    View convertView ;

    Button okbutton;
    CheckBox yes_radio_cb;
    CheckBox no_radio_cb;
    SessionManager sessionManager;
    Spinner expiry_list_spinner;
    HttpRequest httpRequest ;
    //LinearLayout expiryDatelayout;

    ArrayList<String> expiryDateList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.classification_setting , null);

        httpRequest = new HttpRequest(getActivity());


        okbutton = (Button) convertView.findViewById(R.id.okbutton);
        yes_radio_cb = (CheckBox) convertView.findViewById(R.id.yes_radio_cb);
        no_radio_cb = (CheckBox) convertView.findViewById(R.id.no_radio_cb);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if (DirectorFragmentManageActivity.actionbar_titletext != null) {
            DirectorFragmentManageActivity.updateTitle("Classified Setting");
        }

         //  expiryDatelayout = (LinearLayout) convertView.findViewById(R.id.layout);
        sessionManager = new SessionManager();

        no_radio_cb.setOnClickListener(onClickListener);
        yes_radio_cb.setOnClickListener(onClickListener);
        okbutton.setOnClickListener(onClickListener);


        if (sessionManager.isSaveEventInCaleneder(getActivity()))
        {
            yes_radio_cb.setChecked(true);
       //     expiryDatelayout.setVisibility(View.VISIBLE);

        }
        else
        {
            yes_radio_cb.setChecked(true);
           // expiryDatelayout.setVisibility(View.GONE);

        }







        expiry_list_spinner = (Spinner) convertView.findViewById(R.id.expiry_list_spinner);
        expiryDateList = new ArrayList<>();

        expiryDateList.add("30 days");
        expiryDateList.add("60 days");
        expiryDateList.add("90 days");


        expiry_list_spinner.setAdapter(new TimeSloatAdapter(getActivity(), expiryDateList ,expiry_list_spinner));






        getClassificationSetting();





        return convertView;
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.okbutton)
            {
                updateClassifiedStatus();


            }
            if (v.getId() == R.id.yes_radio_cb)
            {
                yes_radio_cb.setChecked(true);
                no_radio_cb.setChecked(false);
  //              expiryDatelayout.setVisibility(View.VISIBLE);
//

            }
            if (v.getId() == R.id.no_radio_cb)
            {
                yes_radio_cb.setChecked(false);
                no_radio_cb.setChecked(true);
               // expiryDatelayout.setVisibility(View.GONE);

            }

        }
    };




    public void getClassificationSetting()
    {

        HashMap<String,Object>param = new HashMap<>();
        param.put("club_id", SessionManager.getUser_Club_id(getActivity()));
        httpRequest.getResponse(getActivity(), WebService.classified_status, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
//
                try
                {
                    if (jsonObject.getBoolean("status"))
                    {
                      int allow_status = jsonObject.getInt("allow_status");
                       int classified_expiry = jsonObject.getInt("classified_expiry") ;

                        if (allow_status == 1)
                        {
                            yes_radio_cb.setChecked(true);
                            no_radio_cb.setChecked(false);
                            ///expiryDatelayout.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            yes_radio_cb.setChecked(false);
                            no_radio_cb.setChecked(true);
                            //.setVisibility(View.GONE);
                        }


                        if (classified_expiry == 30)
                        {
                            expiry_list_spinner.setSelection(0);
                        }
                        if (classified_expiry == 60)
                        {
                            expiry_list_spinner.setSelection(1);
                        }
                        if (classified_expiry == 90)
                        {
                            expiry_list_spinner.setSelection(2);
                        }


                    }
                    else
                    {
                        ShowUserMessage.showUserMessage(getActivity() ,jsonObject.getString("message"));
                    }

                }
                catch (Exception e)
                {

                }


            }
        });


    }





    public void updateClassifiedStatus()
    {
        HashMap<String,Object>param = new HashMap<>();
        param.put("club_id", SessionManager.getUser_Club_id(getActivity()));

        if (yes_radio_cb.isChecked())
        {
            param.put("allow_status","1");
        }
        else
        {
            param.put("allow_status", "2");
        }

        if (expiry_list_spinner.getSelectedItemPosition() == 0)
        {
            param.put("classified_expiry", "30");
        }
        if (expiry_list_spinner.getSelectedItemPosition() == 1)
        {
            param.put("classified_expiry", "60");
        }
        if (expiry_list_spinner.getSelectedItemPosition() == 2)
        {
            param.put("classified_expiry", "90");
        }
        httpRequest.getResponse(getActivity(), WebService.update_classified_status, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {


                try
                {
                    if (jsonObject.getBoolean("status"))
                    {
                        int allow_status = jsonObject.getInt("allow_status");
                        int classified_expiry = jsonObject.getInt("classified_expiry") ;

                        if (allow_status == 1)
                        {
                            yes_radio_cb.setChecked(true);
                            no_radio_cb.setChecked(false);
                        }
                        else
                        {
                            yes_radio_cb.setChecked(false);
                            no_radio_cb.setChecked(true);
                        }


                        if (classified_expiry == 30)
                        {
                            expiry_list_spinner.setSelection(0);
                        }
                        if (classified_expiry == 60)
                        {
                            expiry_list_spinner.setSelection(1);
                        }
                        if (classified_expiry == 90)
                        {
                            expiry_list_spinner.setSelection(2);
                        }
                        ShowUserMessage.showUserMessage(getActivity() ,jsonObject.getString("message"));

                    }
                    else
                    {
                        ShowUserMessage.showUserMessage(getActivity() ,jsonObject.getString("message"));
                    }

                }
                catch (Exception e)
                {

                }



            }
        });






    }



}
