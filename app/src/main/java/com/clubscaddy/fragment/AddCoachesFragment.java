package com.clubscaddy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.clubscaddy.Adapter.CoachMemberListAdapter;
import com.clubscaddy.Adapter.MemberAutoCompleteAdapter;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.R;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.utility.Utill;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by administrator on 7/4/17.
 */

public class AddCoachesFragment extends Fragment
{
   LayoutInflater inflater ;
    View convertView ;
    ListView club_coach_list_view;
    HttpRequest httpRequest ;

    ArrayList<MemberListBean> clubsmembersList;
    AutoCompleteTextView search_coach_auto_complate_textview;
    MemberAutoCompleteAdapter memberAutoCompleteAdapter ;
    ArrayList<MemberListBean> clubCoachList;
    CoachMemberListAdapter coachMemberListAdapter ;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState)
    {
        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.manage_coach_layout,null);
        search_coach_auto_complate_textview = (AutoCompleteTextView) convertView.findViewById(R.id.search_coach_auto_complate_textview);
        httpRequest = new HttpRequest(getActivity());
        club_coach_list_view = (ListView) convertView.findViewById(R.id.club_coach_list_view);
        clubCoachList = new ArrayList<MemberListBean>();
        DirectorFragmentManageActivity.updateTitle("Manage Coaches");


        search_coach_auto_complate_textview.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                //Toast.makeText(getActivity(), "KeyCode "+keyCode, 1).show();

                if(keyCode == 66)
                {
                    InputMethodManager inputManager =
                            (InputMethodManager)getActivity().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            search_coach_auto_complate_textview.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
        search_coach_auto_complate_textview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                // TODO Auto-generated method stub

                ArrayList<MemberListBean>  filter_List =	 memberAutoCompleteAdapter.getResultList();
                search_coach_auto_complate_textview.setText(filter_List.get(pos).getUser_first_name()+" "+filter_List.get(pos).getUser_last_name());
                search_coach_auto_complate_textview.setSelection((filter_List.get(pos).getUser_first_name()+" "+filter_List.get(pos).getUser_last_name()).length());
                addMemberAsCoach(filter_List.get(pos));
            }
        });

        setClubCoachList();

        return convertView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Utill.hideKeybord(getActivity());

    }

    public void setClubCoachList()
    {

        HashMap<String , Object>params = new   HashMap<String , Object>();
        params.put("club_id" , SessionManager.getUser_Club_id(getActivity()));
        //params.put("user_type" , "1");

   httpRequest.getResponse(getActivity(), WebService.coach_list, params, new OnServerRespondingListener(getActivity()) {
       @Override
       public void onSuccess(JSONObject jsonObject)
       {
        try
        {
            Log.e("jsonObject",jsonObject+"");

            clubsmembersList = new ArrayList<MemberListBean>();
            clubCoachList = new ArrayList<MemberListBean>();



            if (jsonObject.getBoolean("status"))
            {
                JSONArray coachJsonArray = jsonObject.getJSONArray("coach");
                for (int i = 0  ;i < coachJsonArray.length() ;i++)
                {
                    JSONObject coachJsonArrayItem = coachJsonArray.getJSONObject(i);
                    MemberListBean memberListBean = new MemberListBean();
                    memberListBean.setUser_first_name(coachJsonArrayItem.getString("user_first_name"));
                    memberListBean.setUser_last_name(coachJsonArrayItem.getString("user_last_name"));
                    memberListBean.setUser_id(coachJsonArrayItem.getString("user_id"));
                    memberListBean.setUser_profilepic(coachJsonArrayItem.getString("user_profilepic"));
                    clubCoachList.add(memberListBean);
                }



                JSONArray memberJsonArray = jsonObject.getJSONArray("member");
                for (int i = 0  ;i < memberJsonArray.length() ;i++)
                {
                    JSONObject memberJsonArrayItem = memberJsonArray.getJSONObject(i);
                    MemberListBean memberListBean = new MemberListBean();
                    memberListBean.setUser_first_name(memberJsonArrayItem.getString("user_first_name"));
                    memberListBean.setUser_last_name(memberJsonArrayItem.getString("user_last_name"));
                    memberListBean.setUser_id(memberJsonArrayItem.getString("user_id"));
                    memberListBean.setUser_profilepic(memberJsonArrayItem.getString("user_profilepic"));
                    clubsmembersList.add(memberListBean);
                }



                memberAutoCompleteAdapter = new MemberAutoCompleteAdapter(getActivity(), R.id.textViewItem,clubsmembersList ,search_coach_auto_complate_textview);
                search_coach_auto_complate_textview.setAdapter(memberAutoCompleteAdapter);

                coachMemberListAdapter = new CoachMemberListAdapter(getActivity() , clubCoachList, AddCoachesFragment.this);
                club_coach_list_view.setAdapter(coachMemberListAdapter);

            }
            else
            {
                ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("msg"));
            }












        }
        catch (Exception e)
        {

        }
       }
   });
    }


    public void  removeMemberAsCoach(final MemberListBean memberListBean)
    {
        HashMap<String , Object>params = new  HashMap<String , Object>();
        params.put("user_type", "2");
        params.put("user_id", memberListBean.getUser_id());
        httpRequest.getResponse(getActivity(), WebService.createcoach, params, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {

                try
                {
                    if (jsonObject.getBoolean("status")== true)
                    {
                        clubsmembersList.add(memberListBean);
                        clubCoachList.remove(memberListBean);
                        coachMemberListAdapter.notifyDataSetChanged();
                        memberAutoCompleteAdapter.notifyDataSetChanged();
                        ShowUserMessage.showUserMessage(getActivity() , jsonObject.optString("msg"));
                    }
                    else
                    {
                        ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("msg"));
                    }
                }
                catch (Exception e)
                {

                }



            }
        });

    }

    public void  addMemberAsCoach(final MemberListBean memberListBean)
    {
        HashMap<String , Object>params = new  HashMap<String , Object>();
        params.put("user_type", "1");
        params.put("user_id", memberListBean.getUser_id());
        httpRequest.getResponse(getActivity(), WebService.createcoach, params, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {


                search_coach_auto_complate_textview.setText("");
                try
                {
                 if (jsonObject.getBoolean("status")== true)
                 {
                     clubsmembersList.remove(memberListBean);
                     clubCoachList.add(memberListBean);
                     coachMemberListAdapter.notifyDataSetChanged();
                     memberAutoCompleteAdapter.notifyDataSetChanged();
                     ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("msg"));
                 }
                 else
                 {
                     ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("message"));
                 }
                }
                catch (Exception e)
                {

                }



            }
        });

    }


}
