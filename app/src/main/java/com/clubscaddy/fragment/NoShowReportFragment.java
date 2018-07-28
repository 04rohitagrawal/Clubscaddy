package com.clubscaddy.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.clubscaddy.Adapter.CustomSpinnerAdapter;
import com.clubscaddy.Adapter.CustumSpinnerListAdapter;
import com.clubscaddy.Adapter.NoShowMemberListAdapter;
import com.clubscaddy.Adapter.NoShowReportListAdapter;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.R;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by administrator on 14/4/17.
 */

public class NoShowReportFragment extends Fragment
{

   /* CheckBox three_month_check_box;
    CheckBox six_month_check_box;
    CheckBox one_year_check_box;*/
    View convertView ;
    ListView noShowReportMemberList;
    HttpRequest httpRequest ;
    ArrayList<MemberListBean>noShowMember ;
    NoShowMemberListAdapter noShowMemberListAdapter ;
    Spinner select_format_spinner ;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState)
    {
        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.no_show_report , null);
        //three_month_check_box = (CheckBox) convertView.findViewById(R.id.three_month_check_box);
        //six_month_check_box = (CheckBox) convertView.findViewById(R.id.six_month_check_box);
       // one_year_check_box = (CheckBox) convertView.findViewById(R.id.one_year_check_box);
       // three_month_check_box.setOnClickListener(onClickListener);
       // six_month_check_box.setOnClickListener(onClickListener);
       // one_year_check_box.setOnClickListener(onClickListener);
      //  three_month_check_box.setChecked(true);
        select_format_spinner = (Spinner) convertView.findViewById(R.id.select_format_spinner);
        ArrayList<String> optionList = new ArrayList<String>();
        optionList.add("3 Months");
        optionList.add("6 Months");
        optionList.add("1 Year");

      //  ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.no_show_spinner_item, optionList);

        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        NoShowReportListAdapter adapter = new NoShowReportListAdapter(getActivity() , optionList ,select_format_spinner);
        select_format_spinner.setAdapter(adapter);

        select_format_spinner.setOnItemSelectedListener(onItemClickListener);
        noShowReportMemberList = (ListView) convertView.findViewById(R.id.no_show_report_member_list);
        httpRequest = new HttpRequest(getActivity());
        noShowMember = new ArrayList<>();

        DirectorFragmentManageActivity.updateTitle("No Show Report");

        select_format_spinner.setSelection(0);

        /*spinner_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_btn.showContextMenu();
            }
        });

        spinner_btn.showContextMenu();*/
        return convertView;
    }


    public AdapterView.OnItemSelectedListener onItemClickListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position == 0)
            {
                getNoShowReportMemberList("3");
            }

            if(position == 1)
            {
                getNoShowReportMemberList("6");
            }

            if(position == 2)
            {
                getNoShowReportMemberList("12");
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {

        }
    };





    public void getNoShowReportMemberList(String reportmonth)
    {
        noShowMember.clear();
        HashMap<String ,Object>params = new HashMap<String ,Object>();
        params.put("user_club_id", SessionManager.getUser_Club_id(getActivity()));
        params.put("filter", reportmonth);
        httpRequest.getResponse(getActivity(), WebService.usernoshowlist, params, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                try
                {
                    Log.e("jsonObject" , jsonObject+"");

                    if (jsonObject.getBoolean("status"))
                    {
                        JSONArray member_list_json_array = jsonObject.optJSONArray("response");
                        if(member_list_json_array != null)
                        {
                            for (int i = 0 ; i< member_list_json_array.length() ;i++)
                            {
                                MemberListBean memberListBean = new MemberListBean();
                                JSONObject member_list_json_array_item = member_list_json_array.getJSONObject(i);
                                memberListBean.setUser_id(member_list_json_array_item.getString("user_id"));
                                memberListBean.setUser_first_name(member_list_json_array_item.getString("user_first_name"));
                                memberListBean.setUser_last_name(member_list_json_array_item.getString("user_last_name"));
                                memberListBean.setUser_email(member_list_json_array_item.getString("user_email"));
                                memberListBean.setUser_profilepic(member_list_json_array_item.getString("user_profilepic"));
                                memberListBean.setUser_no_show(member_list_json_array_item.getString("user_no_show"));
                                memberListBean.setUser_phone(member_list_json_array_item.getString("user_phone"));
                                noShowMember.add(memberListBean);
                            }
                        }
                        else
                        {
                            ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("message")+"");
                        }

                        noShowMemberListAdapter = new NoShowMemberListAdapter((DirectorFragmentManageActivity) getActivity() , noShowMember);
                        noShowReportMemberList.setAdapter(noShowMemberListAdapter);

                    }
                    else
                    {
                        noShowMemberListAdapter = new NoShowMemberListAdapter((DirectorFragmentManageActivity)getActivity() , noShowMember);
                        noShowReportMemberList.setAdapter(noShowMemberListAdapter);
                        ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("message")+"");
                    }





                }
                catch (Exception e)
                {
                    ShowUserMessage.showUserMessage(getActivity() , e.getMessage()+"");
                }
            }
        });



    }


}
