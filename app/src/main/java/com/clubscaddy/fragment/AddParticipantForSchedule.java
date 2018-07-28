package com.clubscaddy.fragment;

/**
 * Created by administrator on 21/6/17.
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.clubscaddy.Adapter.GroupAdapter;
import com.clubscaddy.Adapter.MemberAutoCompleteAdapter;
import com.clubscaddy.Bean.GroupBean;
import com.clubscaddy.Bean.LeagueUser;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.Interface.MemberListListener;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.SqlListe;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.R;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by administrator on 25/1/17.
 */

public class AddParticipantForSchedule extends DialogFragment implements View.OnClickListener , AdapterView.OnItemClickListener
{

    EditText group_name_ev;
    View convertView ;
    LinearLayout individual_tab;
    LinearLayout group_tab;
    View individual_tab_line_view;
    View by_group_line_view;
    LinearLayout individual_tab_layout;
    LinearLayout group_tab_layout;
    String beforname;
    AutoCompleteTextView search_member;
    SchduleDetailFragment eventDetailFrageMent ;

    ArrayList<GroupBean> groupList ;

    ArrayList<GroupBean> copygroupList ;

    MemberAutoCompleteAdapter memberAutoCompleteAdapter ;


    ArrayList<LeagueUser>leagueUserArrayList ;


    ListView group_list_view;
    HttpRequest httpRequest ;
    ArrayList<MemberListBean>memberList = new ArrayList<>();
    SqlListe sqlListe ;

    public void setInstanse( SchduleDetailFragment eventDetailFrageMent )
    {


        this.eventDetailFrageMent = eventDetailFrageMent ;
    }
    Dialog dialog;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(AppConstants.getDeviceWidth(getActivity()), AppConstants.getDeviceHeight(getActivity())));
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        root.setLayoutParams(new ViewGroup.LayoutParams(AppConstants.getDeviceWidth(getActivity()), AppConstants.getDeviceHeight(getActivity())));

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(AppConstants.getDeviceWidth(getActivity())-50,  AppConstants.getDeviceHeight(getActivity())-150);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState)
    {

        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.add_participent_layout ,null);
        individual_tab = (LinearLayout) convertView.findViewById(R.id.individual_tab);
        group_tab = (LinearLayout) convertView.findViewById(R.id.group_tab);
        httpRequest = new HttpRequest(eventDetailFrageMent.getActivity());
        group_name_ev = (EditText) convertView.findViewById(R.id.group_name_ev);
        search_member = (AutoCompleteTextView) convertView.findViewById(R.id.search_member);
        search_member.setOnItemClickListener(this);
        search_member.addTextChangedListener(textWatcher);

        memberList.clear();
        groupList = new ArrayList<>();

        copygroupList = new ArrayList<>();

        leagueUserArrayList = new ArrayList<>();

        individual_tab_line_view =  convertView.findViewById(R.id.individual_tab_line_view);
        by_group_line_view =  convertView.findViewById(R.id.by_group_line_view);

        group_list_view = (ListView) convertView.findViewById(R.id.group_list_view);

        group_name_ev.addTextChangedListener(searchWatcherforGroup);

        sqlListe = new SqlListe(getActivity());

        individual_tab_layout = (LinearLayout) convertView.findViewById(R.id.individual_tab_layout);
        group_tab_layout = (LinearLayout) convertView.findViewById(R.id.group_tab_layout);


        group_tab.setOnClickListener(this);
        individual_tab.setOnClickListener(this);


        return convertView;
    }


    String priviousText = "";

    public TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
            priviousText = s.toString() ;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s)
        {
            if (priviousText.length() == 0)
            {
                getMemberList(s.toString());
            }


        }
    } ;


    @Override
    public void onClick(View v)
    {

        if (v.getId() == R.id.individual_tab)
        {
            individual_tab_line_view.setVisibility(View.VISIBLE);
            by_group_line_view.setVisibility(View.INVISIBLE);


            individual_tab_layout.setVisibility(View.VISIBLE);
            group_tab_layout.setVisibility(View.INVISIBLE);
        }

        if (v.getId() == R.id.group_tab)
        {
            individual_tab_line_view.setVisibility(View.INVISIBLE);
            by_group_line_view.setVisibility(View.VISIBLE);


            group_tab_layout.setVisibility(View.VISIBLE);
            individual_tab_layout.setVisibility(View.INVISIBLE);

            if (copygroupList != null && copygroupList.size() == 0)
            {
                getGroupList();

            }
        }

    }




    private void getMemberList(String keyWord) {
        sqlListe.getMemberList(getActivity(), new MemberListListener() {
            @Override
            public void onSuccess(ArrayList<MemberListBean> memberListBeanArrayList)
            {



                try
                {


                        ArrayList<LeagueUser> existMember = eventDetailFrageMent.getLeagueMemberList();





                        for (int i =0 ; i < memberListBeanArrayList.size() ;i++)
                        {
                            MemberListBean userObj = memberListBeanArrayList.get(i);

                            boolean isMemberExits = false ;


                            for (int j = 0 ; j < existMember.size() ; j++)
                            {
                                if (existMember.get(j).getLeague_user_id() == Integer.parseInt(userObj.getUser_id()))
                                {
                                    isMemberExits = true ;
                                }

                            }


                            if (isMemberExits == false)
                            {
                                MemberListBean leagueUser = new MemberListBean();
                                leagueUser.setUser_id(userObj.getUser_id());
                                leagueUser.setUser_first_name(userObj.getUser_first_name());
                                leagueUser.setUser_last_name(userObj.getUser_last_name());
                                leagueUser.setUser_profilepic(userObj.getUser_profilepic());
                                memberList.add(leagueUser);
                            }



                        }

                        if (memberList.size() > 0) {

                            memberAutoCompleteAdapter = new MemberAutoCompleteAdapter(getActivity(), R.id.textViewItem,memberList,search_member);
                            search_member.setAdapter(memberAutoCompleteAdapter);
                            search_member.setText(search_member.getText().toString()+"");
                            search_member.setSelection(search_member.getText().toString().length());
                            //showMemberSelector();
                            // setEventAdapter(alMemberList);
                        } else {
                            ShowUserMessage.showUserMessage(getActivity(), getResources().getString(R.string.no_record_found));
                        }
                    }



                catch (Exception e)
                {
                    ShowUserMessage.showUserMessage(eventDetailFrageMent.getActivity() , e.getMessage());
                }


            }
        },AppConstants.AllMEMBERlIST ,keyWord);
    }






    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        String userName = memberAutoCompleteAdapter.getResultList().get(position).getUser_first_name() +" "+memberAutoCompleteAdapter.getResultList().get(position).getUser_last_name();

        Utill.hideKeybord(eventDetailFrageMent.getActivity());
        MemberListBean memberListBean = memberAutoCompleteAdapter.getResultList().get(position) ;
        LeagueUser leagueUser = new LeagueUser();
        leagueUser.setLeague_user_id(Integer.parseInt(memberListBean.getUser_id()));
        leagueUser.setLeague_user_name(userName);
        leagueUser.setLeague_user_profile(memberListBean.getUser_profilepic());
        search_member.setText(userName);
        search_member.setSelection(userName.length());


        InputMethodManager imm = (InputMethodManager) eventDetailFrageMent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search_member.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


                  dialog.dismiss();

               eventDetailFrageMent.joinLeagueByIndivisoal(leagueUser);
    }


    public void getGroupList()
    {


        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("group_club_id",SessionManager.getUser_Club_id(eventDetailFrageMent.getActivity()));
        params.put("group_owner_user_id",SessionManager.getUser_id(eventDetailFrageMent.getActivity()));
        httpRequest.getResponse(getActivity(), WebService.get_group_list, params, new OnServerRespondingListener(eventDetailFrageMent.getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                Log.e("jsonObject" , jsonObject+"");

                try
                {
                    if (jsonObject.getBoolean("status"))
                    {
                        JSONArray jsonArray = jsonObject.getJSONObject("group").getJSONArray("list");

                        for (int i = 0 ; i < jsonArray.length() ;i++)
                        {
                            GroupBean groupBean = new GroupBean();
                            groupBean.setGroup_id(jsonArray.getJSONObject(i).getString("group_id"));
                            groupBean.setGroup_name(jsonArray.getJSONObject(i).getString("group_name"));
                            groupList.add(groupBean);
                        }
                        copygroupList.addAll(groupList);
                       setGroupAdapter(groupList);
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


    GroupAdapter groupAdapter;
    public void setGroupAdapter(final ArrayList<GroupBean> list) {


        groupAdapter = new GroupAdapter(getActivity(), groupList);





        GroupAdapter.SwipeEventItemClickListener swipeEventItemClickListener = new GroupAdapter.SwipeEventItemClickListener() {
            @Override
            public void OnEditClick(int position)
            {
                InputMethodManager imm = (InputMethodManager) eventDetailFrageMent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(search_member.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                joinLeagueByGroupId(groupList.get(position).getGroup_id());
            }

            @Override
            public void OnBlockClick(int position, int blockStatus) {

            }
        };
        groupAdapter.setEventItemClickListener(swipeEventItemClickListener);
        group_list_view.setAdapter(groupAdapter);
        //group_list_view.setOnItemClickListener(onItemClickListener);

        //ShowUserMessage.showUserMessage(mContext, "" + groupAdapter.getCount());
    }

    TextWatcher searchWatcherforGroup = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub



            if(s.toString().length() != 0)
            {
                //Toast.makeText(getActivity(), "" +(s.toString().length() == 1 && beforname.length() == 0), 1).show();
                groupList.clear();

                for (int i =0 ; i < copygroupList.size() ;i++)
                {
                    if (copygroupList.get(i).getGroup_name().toLowerCase().startsWith(s.toString().toLowerCase()))
                    {
                        groupList.add(copygroupList.get(i));
                    }
                }


                setGroupAdapter(groupList);
            }
            else
            {
                groupList.clear();
                groupList.addAll(copygroupList);
                setGroupAdapter(groupList);
            }


			/*filterList(str);
			getMembersList();*/
        }
    };





    public void joinLeagueByGroupId(final String groupId)
    {
        Utill.hideKeybord(getActivity());
        HashMap<String , Object>param = new HashMap<>();
        param.put("league_id" ,eventDetailFrageMent. bundle.getString("league_id"));
        param.put("user_id" , SessionManager.getUser_id(getActivity()));

        param.put("group_id" ,groupId);
        param.put("league_club_id" ,SessionManager.getUser_Club_id(getActivity()));
        httpRequest.getResponse(getActivity(), WebService.join_league, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                try
                {
                    if (jsonObject.getBoolean("status"))
                    {
                        dialog.dismiss();
                        getGroupMemberByGroupId(groupId);
                    }

                    ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("message")+"");

                }
                catch (Exception e)
                {

                }


            }
        });
    }
    ArrayList<LeagueUser> groupMemberList ;
    public void getGroupMemberByGroupId(String groupId)
    {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("group_id", groupId);
        params.put("group_club_id",SessionManager.getUser_Club_id(eventDetailFrageMent.getActivity()));
        params.put("group_owner_user_id",SessionManager.getUser_id(eventDetailFrageMent.getActivity()));
        httpRequest.getResponse(getActivity(), WebService.get_group_list, params, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {

                groupMemberList = new ArrayList<LeagueUser>();

                Log.e("jsonObject" ,jsonObject+"");

                try
                {
                    if (jsonObject.getBoolean("status"))
                    {
                        JSONArray jsonArray = jsonObject.getJSONObject("group").getJSONArray("list");

                        for (int i = 0 ; i < jsonArray.length() ;i++)
                        {
                            JSONArray memberjsonArrayItem = jsonArray.getJSONObject(i).getJSONArray("members");

                            for (int j = 0 ; j < memberjsonArrayItem.length() ; j++)
                            {
                                JSONObject userObj = memberjsonArrayItem.getJSONObject(j) ;


                                ArrayList<LeagueUser> existMember = eventDetailFrageMent.getLeagueMemberList();

                                boolean isMemberExits = false ;


                                for (int k = 0 ; k< existMember.size() ; k++)
                                {
                                    if (existMember.get(k).getLeague_user_id() == userObj.getInt("user_id"))
                                    {
                                        isMemberExits = true ;
                                    }

                                }


                                if (isMemberExits == false)
                                {
                                    LeagueUser leagueUser = new LeagueUser();
                                    leagueUser.setLeague_user_id(userObj.getInt("user_id"));
                                    leagueUser.setLeague_user_name(userObj.getString("user_first_name")+" "+userObj.getString("user_last_name"));
                                    leagueUser.setLeague_user_profile(userObj.getString("user_profilepic"));
                                    groupMemberList.add(leagueUser);
                                }
                            }



                        }


                  eventDetailFrageMent.joinLeagueByInGroupId(groupMemberList);

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

