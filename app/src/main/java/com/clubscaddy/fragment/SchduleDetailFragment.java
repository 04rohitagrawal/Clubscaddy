package com.clubscaddy.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.clubscaddy.Adapter.LeagueListAdapter;
import com.clubscaddy.Adapter.LeagueUserListAdapter;
import com.clubscaddy.Adapter.UserStatusListAdapter;
import com.clubscaddy.Bean.League;
import com.clubscaddy.Bean.LeagueUser;
import com.clubscaddy.Bean.Match;
import com.clubscaddy.Bean.MatchDetail;
import com.clubscaddy.custumview.ExpandableHeightListView;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.DialogBoxButtonListner;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.R;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by administrator on 13/6/17.
 */

public class SchduleDetailFragment extends Fragment
{

    View convertView ;
    League league ;
    Bundle bundle ;
    HttpRequest httpRequest ;
    String serverDataTime = "";

    public ArrayList<LeagueUser> getLeagueMemberList() {
        return leagueMemberList;
    }

    ArrayList<LeagueUser>leagueMemberList ;
    ArrayList<MatchDetail>matchDetailArrayList ;
    ListView league_list_view ;
    TextView league_title_tv;
    LinearLayout headerLayout ;
    LeagueListAdapter leagueListAdapter ;
    RecyclerView league_participant_list_view;

    TextView league_edit_tab;
    TextView league_delete_tab;
    TextView league_message_tab;
    TextView add_new_member_tab;
    ShowUserMessage showUserMessage ;
    LeagueUserListAdapter leagueUserListAdapter ;
    LinearLayout edit_mode_linear_layout;
   public String leagueUserId = "" ;
    public String leagueId = "" ;

ManageGroupFragment.AddNewSchduleListner addNewSchduleListner ;
    public void setInstanse(ManageGroupFragment.AddNewSchduleListner addNewSchduleListner )
    {
        this.addNewSchduleListner  =addNewSchduleListner ;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.schdule_detail_fragment , null);
        league = new League();
        bundle = getArguments();
        httpRequest = new HttpRequest(getActivity());
        leagueMemberList = new ArrayList<>();
        DirectorFragmentManageActivity.updateTitle("My Schedule");
        matchDetailArrayList = new ArrayList<>();
        league_list_view  = (ListView) convertView.findViewById(R.id.league_list_view);
        league_title_tv = (TextView) convertView.findViewById(R.id.league_title_tv);
        headerLayout = (LinearLayout) inflater.inflate(R.layout.league_list_header , null);
        league_list_view.addHeaderView(headerLayout);
        leagueListAdapter = new LeagueListAdapter(this , matchDetailArrayList , serverDataTime);
        league_list_view.setAdapter(leagueListAdapter);
        league_participant_list_view = (RecyclerView) convertView.findViewById(R.id.league_participant_list_view);
        RecyclerView.LayoutManager layoutManager=new  LinearLayoutManager(getActivity());
        league_participant_list_view.setLayoutManager(layoutManager);


        league_edit_tab = (TextView) convertView.findViewById(R.id.league_edit_tab);
        league_edit_tab.setOnClickListener(ontabClickListener);


        if (DirectorFragmentManageActivity.backButton != null) {
            DirectorFragmentManageActivity.showBackButton();
            DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
        }


        league_delete_tab = (TextView) convertView.findViewById(R.id.league_delete_tab);
        league_delete_tab.setOnClickListener(ontabClickListener);

        league_message_tab = (TextView) convertView.findViewById(R.id.league_message_tab);
        league_message_tab.setOnClickListener(ontabClickListener);


        add_new_member_tab = (TextView) convertView.findViewById(R.id.add_new_member_tab);
        add_new_member_tab.setOnClickListener(ontabClickListener);

        showUserMessage= new ShowUserMessage(getActivity());
        leagueUserId =  bundle.getString("league_uid") ;
        leagueId = bundle.getString("league_id");
        edit_mode_linear_layout = (LinearLayout) convertView.findViewById(R.id.edit_mode_linear_layout);
        edit_mode_linear_layout.setVisibility(View.VISIBLE);


        if (leagueUserId.equals(SessionManager.getUser_id(getActivity())) == false )
        {
            league_delete_tab.setEnabled(false);
            league_delete_tab.setAlpha(0.4f);

            league_edit_tab.setEnabled(false);
            league_edit_tab.setAlpha(0.4f);

            add_new_member_tab.setEnabled(false);
            add_new_member_tab.setAlpha(0.4f);

        }

        //

        getSchduleDetail();


        //Bundle[{league_uid=3499, league_id=1}]


        return convertView;
    }

    public League getSchduleDetail()
    {
        HashMap<String , Object>param = new HashMap<>();
        param.put("league_id" , bundle.getString("league_id"));
        param.put("league_uid" , bundle.getString("league_uid"));
        param.put("league_name" , bundle.getString("league_name"));





        httpRequest.getResponse(getActivity(), WebService.scoredetails, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject) {


              //  ShowUserMessage.showUserMessage(getActivity() , jsonObject+"");

                setDataOnView(jsonObject);


            }
        });



        return league ;

    }



    public void setDataOnView(JSONObject jsonObject)
    {
        try
        {
            if (jsonObject.getBoolean("status"))
            {

                leagueMemberList.clear();
                matchDetailArrayList.clear();

                serverDataTime = jsonObject.getString("time");

                leagueListAdapter.setCurrentTime(serverDataTime);



                league_title_tv.setText(jsonObject.getString("league_name"));
                JSONArray leagueMemberJsonArray = jsonObject.getJSONArray("league_member");

                for (int i = 0 ; i < leagueMemberJsonArray.length();i++)
                {
                    JSONObject leagueMemberJsonArrayItem = leagueMemberJsonArray.getJSONObject(i);
                    LeagueUser leagueUser = new LeagueUser();
                    leagueUser.setLeague_user_id(leagueMemberJsonArrayItem.getInt("league_user_id"));
                    leagueUser.setLeague_user_name(leagueMemberJsonArrayItem.getString("league_user_name"));
                    leagueUser.setLeague_user_profile(leagueMemberJsonArrayItem.getString("league_user_profile"));
                    leagueMemberList.add(leagueUser);
                }


                JSONArray matchDetailJsonArray = jsonObject.getJSONArray("match_detail");
                for (int j = 0 ;j < matchDetailJsonArray.length() ; j++)
                {
                    JSONObject matchDetailJsonArrayItem = matchDetailJsonArray.getJSONObject(j);
                    MatchDetail matchDetail = new MatchDetail();
                    matchDetail.setMatch_id(matchDetailJsonArrayItem.getInt("match_id"));
                    matchDetail.setMatch_date(matchDetailJsonArrayItem.getString("match_date"));
                    matchDetail.setMatch_time(matchDetailJsonArrayItem.getString("match_time"));
                    matchDetail.setMatch_location(matchDetailJsonArrayItem.getString("match_location"));
                    matchDetail.setMatch_opponet(matchDetailJsonArrayItem.getString("match_opponet"));

                    matchDetail.setMatch_createAt(matchDetailJsonArrayItem.getString("match_createAt"));
                    HashMap<String , Integer>param = new HashMap<String, Integer>();
                    JSONObject matchAllUserStatus = matchDetailJsonArrayItem.getJSONObject("match_all_user_status");
                    param.put("total_yes" , matchAllUserStatus.getInt("total_yes"));
                    param.put("total_no" , matchAllUserStatus.getInt("total_no"));
                    param.put("total_later" , matchAllUserStatus.getInt("total_later"));
                    param.put("total_last_call" , matchAllUserStatus.getInt("total_last_call"));
                    param.put("total_member" , matchAllUserStatus.getInt("total_member"));
                    param.put("not_responding" , matchAllUserStatus.getInt("not_responding"));
                    matchDetail.setMatchAllUserStatus(param);
                    ArrayList<LeagueUser> matchAllMemberList = new ArrayList<LeagueUser>();

                    JSONArray matchAllMemberListJsonArray = matchDetailJsonArrayItem.getJSONArray("match_all_member_list");





                    for (int i = 0 ; i < matchAllMemberListJsonArray.length();i++)
                    {
                        JSONObject matchAllMemberListJsonArrayItem = matchAllMemberListJsonArray.getJSONObject(i);
                        LeagueUser leagueUser = new LeagueUser();
                        leagueUser.setLeague_user_id(matchAllMemberListJsonArrayItem.getInt("league_user_id"));
                        leagueUser.setLeague_user_name(matchAllMemberListJsonArrayItem.getString("league_user_name"));
                        leagueUser.setLeague_user_profile(matchAllMemberListJsonArrayItem.getString("league_user_profile"));
                        leagueUser.setMatch_user_status(matchAllMemberListJsonArrayItem.getInt("match_user_status"));

                        if (leagueUser.getLeague_user_id() == Integer.parseInt(SessionManager.getUser_id(getActivity())) )
                        {
                            matchDetail.setMatch_user_status(matchAllMemberListJsonArrayItem.getInt("match_user_status"));
                            matchDetail.setListItemPos(matchAllMemberListJsonArrayItem.getInt("match_user_status"));
                        }


                        if (leagueUser.getLeague_user_id() == Integer.parseInt(leagueUserId))
                        {
                            matchAllMemberList.add(0 ,leagueUser);
                        }
                        matchAllMemberList.add(leagueUser);





                    }


                    matchDetail.setMatchAllMemberList(matchAllMemberList);
                    matchDetailArrayList.add(matchDetail);

                }

                leagueListAdapter.notifyDataSetChanged();
                ExpandableHeightListView.getListViewSize(league_list_view);
                leagueUserListAdapter = new LeagueUserListAdapter(SchduleDetailFragment.this , leagueMemberList) ;
                league_participant_list_view.setAdapter(leagueUserListAdapter);

            }
            else
            {
                ShowUserMessage.showUserMessage(getActivity() , jsonObject.optString("message"));
            }

        }
        catch (Exception e)
        {
            ShowUserMessage.showUserMessage(getActivity() , e.getMessage());
        }

    }



    public View.OnClickListener ontabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {

            if (v.getId() == R.id.league_edit_tab)
            {
                EditSchduleFragment editSchduleFragment = new EditSchduleFragment();
                editSchduleFragment.registerEditSchduleListner(new EditSchudlerListner());
                Bundle mybundle = new Bundle();
                mybundle.putString("league_name" , bundle.getString("league_name"));
                mybundle.putString("league_id" , bundle.getString("league_id"));
                mybundle.putString("league_uid" , bundle.getString("league_uid"));
                mybundle.putSerializable("matchDetailList" , matchDetailArrayList);
                mybundle.putString("serverDataTime" ,serverDataTime);
                editSchduleFragment.setArguments(mybundle);

                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_frame , editSchduleFragment , "editSchduleFragment").addToBackStack(null). commit();

            }



            if (v.getId() == R.id.league_delete_tab)
            {
                deleteLeague();
            }



            if (v.getId() == R.id.league_message_tab)
            {
                showDialogForSendNotification();
            }



            if (v.getId() == R.id.add_new_member_tab)
            {
                AddParticipantForSchedule addParticipantInEvent = new AddParticipantForSchedule();
                addParticipantInEvent.setInstanse(SchduleDetailFragment.this);

                addParticipantInEvent.show(getFragmentManager(),"");

            }

        }
    };



    public void editLeague()
    {

    }

    public void deleteLeague()
    {

        showUserMessage.showDialogBoxWithYesNoButton("Are you sure you want to delete this schedule", new DialogBoxButtonListner() {
            @Override
            public void onYesButtonClick(DialogInterface dialog)
            {

                HashMap<String , Object> param = new HashMap<String, Object>();
                param.put("league_id" , bundle.getString("league_id"));
                httpRequest.getResponse(getActivity(), WebService.delete_schedule, param, new OnServerRespondingListener(getActivity()) {
                    @Override
                    public void onSuccess(JSONObject jsonObject)
                    {
                        if (jsonObject.optBoolean("status"))
                        {
                            showMessageForFragmeneWithBack(getActivity() , jsonObject.optString("message"));
                        }
                        else
                        {
ShowUserMessage.showUserMessage(getActivity() , jsonObject.optString("message"));
                        }

                    }
                });
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();

    }

    public  void showMessageForFragmeneWithBack(final FragmentActivity fragmentActivity , String msg)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(
                fragmentActivity).create();


        // Setting Dialog Title
        alertDialog.setTitle(SessionManager.getClubName(fragmentActivity));
        alertDialog.setCancelable(false);
        // Setting Dialog Message
        alertDialog.setMessage(msg);


        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                fragmentActivity.getSupportFragmentManager().popBackStack();
                //addNewSchduleListner.onSuccess();
                dialog.cancel();
            }
        });


        alertDialog.show();
    }
    public class FragmentCommunicate
    {
        public void onSuccess(String title , Match match)
        {

        }
        public void onError(String msg)
        {

        }
    }

    TextView cancel_dialog_btn ;
    TextView email_send_btn ;
    TextView notification_send_btn ;
    TextView discription_textview_status ;
    EditText email_notification_msg ;


     public void showDialogForSendNotification()
     {
         final Dialog dialog = new Dialog(getActivity());
         dialog.setCancelable(false);
         dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
         dialog.setContentView(R.layout.email_notification_layout);
         dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
         cancel_dialog_btn = (TextView) dialog.findViewById(R.id.cancel_dialog_btn);
         dialog.setCancelable(false);
         email_send_btn = (TextView) dialog.findViewById(R.id.email_send_btn);
         notification_send_btn = (TextView) dialog.findViewById(R.id.notification_send_btn);
         discription_textview_status = (TextView) dialog.findViewById(R.id.discription_textview_status);
         email_notification_msg = (EditText) dialog.findViewById(R.id.email_notification_msg);

         dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN );
         dialog.show();
        notification_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Validation.isStringNullOrBlank(email_notification_msg.getText().toString()))
                {
                    ShowUserMessage.showUserMessage(getActivity() , "Please enter message.");
                    return;
                }

                email_notification_msg.requestFocus();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(email_notification_msg.getWindowToken(), InputMethodManager.SHOW_FORCED);
                dialog.cancel();
                sendEmailAndNotification("1" ,email_notification_msg.getText().toString() );
            }
        });



         email_send_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 if (Validation.isStringNullOrBlank(email_notification_msg.getText().toString()))
                 {
                     ShowUserMessage.showUserMessage(getActivity() , "Please enter message.");
                     return;
                 }
                 email_notification_msg.requestFocus();
                 InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                 imm.hideSoftInputFromWindow(email_notification_msg.getWindowToken(), InputMethodManager.SHOW_FORCED);

                 dialog.cancel();
                 sendEmailAndNotification("2" ,email_notification_msg.getText().toString() );
             }
         });




         email_notification_msg.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {

             }

             @Override
             public void afterTextChanged(Editable s)
             {
                 discription_textview_status.setText(s.toString().length()+"/1000");
             }
         });

         cancel_dialog_btn.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 // TODO Auto-generated method stub

                 email_notification_msg.requestFocus();
                 InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                 imm.hideSoftInputFromWindow(email_notification_msg.getWindowToken(), InputMethodManager.SHOW_FORCED);
                 dialog.cancel();
             }
         });


     }



    public void leagueWithdraw(final String league_user_id ,final int pos)
    {


        showUserMessage.showDialogBoxWithYesNoButton("Are you sure you want to withdraw this participant", new DialogBoxButtonListner() {
            @Override
            public void onYesButtonClick(DialogInterface dialog) {
                HashMap<String , Object>param = new HashMap<>();
                param.put("league_id" ,bundle.getString("league_id"));
                param.put("user_id" , SessionManager.getUser_id(getActivity()));
                param.put("league_user_id" ,league_user_id);




                httpRequest.getResponse(getActivity(), WebService.league_withdraw, param, new OnServerRespondingListener(getActivity()) {
                    @Override
                    public void onSuccess(JSONObject jsonObject)
                    {
                        try
                        {
                            if (jsonObject.getBoolean("status"))
                            {
                                leagueMemberList.remove(pos);
                                leagueUserListAdapter.notifyDataSetChanged();
                                ExpandableHeightListView.getListViewSize(league_list_view);

                                getSchduleDetail();
                            }

                            ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("message")+"");

                        }
                        catch (Exception e)
                        {

                        }


                    }
                });
            }
        });



    }


    public void joinLeagueByIndivisoal(final LeagueUser leagueUser)
    {
        Utill.hideKeybord(getActivity());
        HashMap<String , Object>param = new HashMap<>();
        param.put("league_id" ,bundle.getString("league_id"));
        param.put("user_id" , SessionManager.getUser_id(getActivity()));

        param.put("league_user_id" ,leagueUser.getLeague_user_id());
        param.put("league_club_id" ,SessionManager.getUser_Club_id(getActivity()));
        httpRequest.getResponse(getActivity(), WebService.join_league, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                try
                {
                    if (jsonObject.getBoolean("status"))
                    {
                        leagueMemberList.add(leagueUser);
                        leagueUserListAdapter.notifyDataSetChanged();




                        for (int i = 0 ;i < matchDetailArrayList.size() ;i++)
                        {
                            matchDetailArrayList.get(i).addMatchAllMemberList(leagueUser);

                            HashMap<String , Integer> param =  matchDetailArrayList.get(i).getMatchAllUserStatus() ;
                            int not_responding = param.get("not_responding") ;
                            not_responding =  ++not_responding ;
                            param.put("not_responding" ,not_responding);
                            matchDetailArrayList.get(i).setMatchAllUserStatus(param);


                        }
                        leagueListAdapter.notifyDataSetChanged();



                        ExpandableHeightListView.getListViewSize(league_list_view);
                    }

                    ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("message")+"");

                }
                catch (Exception e)
                {

                }


            }
        });
    }



    public void joinLeagueByInGroupId(final ArrayList<LeagueUser> leagueUserArrayList)
    {
        leagueMemberList.addAll(leagueUserArrayList);
        leagueUserListAdapter.notifyDataSetChanged();

        for (int i = 0 ;i < matchDetailArrayList.size() ;i++)
        {
            matchDetailArrayList.get(i).getMatchAllMemberList().addAll(leagueUserArrayList);
          HashMap<String , Integer> param =  matchDetailArrayList.get(i).getMatchAllUserStatus() ;
           int not_responding = param.get("not_responding") ;
             not_responding =  not_responding + leagueUserArrayList.size() ;
            param.put("not_responding" ,not_responding);
            matchDetailArrayList.get(i).setMatchAllUserStatus(param);


        }
        leagueListAdapter.notifyDataSetChanged();

        ExpandableHeightListView.getListViewSize(league_list_view);
    }



public  void sendEmailAndNotification(String notify_via ,String msg)
{

    HashMap<String,Object> param = new HashMap<>();
    param.put("notify_via" ,notify_via);
    param.put("league_id" ,leagueId);
    param.put("user_id" ,SessionManager.getUser_id(getActivity()));
    param.put("league_message" ,msg);

    httpRequest.getResponse(getActivity(), WebService.leaguenotification, param, new OnServerRespondingListener(getActivity()) {
        @Override
        public void onSuccess(JSONObject jsonObject)
        {

            try
            {
                ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("message"));
            }
            catch (Exception e)
            {

            }

        }
    });


}


 public void changeMatchStatus(String league_status , String match_id)
 {
     HashMap<String , Object>param = new HashMap<>();
     param.put("user_id" , SessionManager.getUser_id(getActivity()));
     param.put("league_id" , leagueId);
     param.put("league_status" , league_status);
     param.put("match_id" , match_id);
     httpRequest.getResponse(getActivity(), WebService.schedule_change, param, new OnServerRespondingListener(getActivity()) {
         @Override
         public void onSuccess(JSONObject jsonObject)
         {
             try
             {
                ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("msg"));
                 getSchduleDetail();
             }
             catch (Exception e)
             {

             }

         }
     });

 }


  public  class EditSchudlerListner
  {
      public void onEditSchdule(JSONObject jsonObject)
      {
          getSchduleDetail();

      }

  }



    Dialog userStatusDialog ;
 ListView userstatus_list_view;
    UserStatusListAdapter userStatusListAdapter ;
    TextView total_participent_tv;

    public void showDialogforUserStatus(ArrayList<LeagueUser> leagueUserArrayList , int status , String statusText )
    {

      final  ArrayList<LeagueUser>filterLeagueUserList = new ArrayList<>();



        for (int i =0 ; i < leagueUserArrayList.size() ;i++)
        {

            if (leagueUserArrayList.get(i).getMatch_user_status() == status || i == 0)
            {
                filterLeagueUserList.add(leagueUserArrayList.get(i));
            }

        }



        userStatusDialog = new Dialog(getActivity());
        userStatusDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       // userStatusDialog.setCancelable(false);
        userStatusDialog.setContentView(R.layout.league_match_user_status_dialog);
        userStatusDialog.getWindow().setLayout(AppConstants.getDeviceWidth(getActivity())-30 , LinearLayout.LayoutParams.WRAP_CONTENT);
        userstatus_list_view = (ListView) userStatusDialog.findViewById(R.id.userstatus_list_view);

        total_participent_tv = (TextView) userStatusDialog.findViewById(R.id.total_participent_tv);

        total_participent_tv.setText(statusText +" " +(filterLeagueUserList.size()-1)+"  out of "  +(leagueUserArrayList.size()-1));

        userStatusListAdapter = new UserStatusListAdapter(getActivity() , filterLeagueUserList ,leagueUserId);
        userstatus_list_view.setAdapter(userStatusListAdapter);


        userstatus_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (position != 0)
                {
                    userStatusDialog.dismiss();
                    DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) getActivity();
                    directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(filterLeagueUserList.get(position).getLeague_user_id()+"");
                }

            }
        });


        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.cancel_dialog_layout , null);
        userstatus_list_view.addFooterView(linearLayout);

        TextView calcel_dialog_btn = (TextView) linearLayout.findViewById(R.id.calcel_dialog_btn);
        calcel_dialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userStatusDialog.cancel();

            }
        });


        if (filterLeagueUserList.size() > 1)
        userStatusDialog.show();
    }

View.OnClickListener addToBack = new View.OnClickListener() {

    @Override
    public void onClick(View v) {
        try {
            DirectorFragmentManageActivity.popBackStackFragment();
        } catch (Exception e) {
            ShowUserMessage.showUserMessage(getActivity(), e.toString());
        }
    }
};




}
