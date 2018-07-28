package com.clubscaddy.fragment;

import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.clubscaddy.Adapter.MemberAutoCompleteAdapter;
import com.clubscaddy.Adapter.ModeratorMemberListAdapter;
import com.clubscaddy.Bean.EventBean;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.FragmentBackResponseListener;
import com.clubscaddy.Interface.MemberListListener;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.R;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.utility.SqlListe;
import com.clubscaddy.utility.Utill;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by administrator on 21/12/16.
 */

public class AddModeratorFragment
{
    View convertView ;
    String beforname;
    AutoCompleteTextView member_name_edit_tv;
    HttpRequest httpRequest ;
    ArrayList<MemberListBean>memberBeanArrayList;
    ArrayList<MemberListBean>modeatormemberBeanArrayList;
    ShowUserMessage showUserMessage ;
    String event_id ;String event_club_id ;
    MemberAutoCompleteAdapter memberAutoCompleteAdapter ;
    ListView event_moderotor_list_view;
    ModeratorMemberListAdapter moderatorMemberListAdapter ;
    Button change_event_status_btn;
    int event_status ;
    SqlListe sqlListe;
    View edit_event_tab_line;

    View manage_event_tab_line;
    TextView add_mod_tv;
    EventBean eventBean ;
boolean  isMemberasDirector = false ;;
    FragmentActivity activity ;

    public void setArgument(String event_id , String event_club_id , ArrayList<MemberListBean> modeatormemberBeanArrayList , EventBean eventBean ,FragmentActivity activity)
    {
         this.event_id = event_id ;
        this.eventBean = eventBean ;
        this.event_club_id = event_club_id ;
        this.modeatormemberBeanArrayList = modeatormemberBeanArrayList ;
        event_status = EventDetailFrageMent.eventBean.getEvent_state();


        this.activity = activity ;
    }




    public View onCreateView(LinearLayout convertView)
    {




        httpRequest = new HttpRequest(getActivity());
        sqlListe = new SqlListe(getActivity());

        member_name_edit_tv = (AutoCompleteTextView) convertView.findViewById(R.id.member_name_edit_tv);
        showUserMessage = new ShowUserMessage();
        memberBeanArrayList = new ArrayList<>();
        member_name_edit_tv.addTextChangedListener(searchWatcher);
        event_moderotor_list_view = (ListView) convertView.findViewById(R.id.event_moderotor_list_view);
        moderatorMemberListAdapter =  new ModeratorMemberListAdapter(getActivity() ,modeatormemberBeanArrayList ,1 ,this ,fragmentBackResponseListener) ;
        event_moderotor_list_view.setAdapter(moderatorMemberListAdapter);


        try
        {
            isMemberasDirector = false ;
            for (int i =0 ; i< modeatormemberBeanArrayList.size() ;i++)
            {
                if(SessionManager.getUser_id(getActivity()).equals(modeatormemberBeanArrayList.get(i).getUser_id()))
                {


                    isMemberasDirector = true ;
                    break;
                }


            }

        }
        catch (Exception e)
        {

        }


        member_name_edit_tv.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== 5){
                    //do something
                    Utill.hideKeybord(getActivity());
                }
                return false;
            }
        });

        add_mod_tv = (TextView) convertView.findViewById(R.id.add_mod_tv);
        if ( SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER) && isMemberasDirector == false)
        {
            member_name_edit_tv.setVisibility(View.INVISIBLE);
            event_moderotor_list_view.setVisibility(View.INVISIBLE);
            add_mod_tv.setVisibility(View.INVISIBLE);
        }

        if ( SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER) )
        {
            member_name_edit_tv.setEnabled(false);

        }


        edit_event_tab_line = (View) convertView.findViewById(R.id.edit_event_tab_line);

        manage_event_tab_line = (View) convertView.findViewById(R.id.manage_event_tab_line);












        change_event_status_btn = (Button) convertView.findViewById(R.id.change_event_status_btn);



        if (event_status == 1)
        {
            change_event_status_btn.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.dark_green_circle_back));
            change_event_status_btn.setText("Start Event");
        }
        else
        {
            if (event_status == 2)
            {
                change_event_status_btn.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.red_circle_back));
                change_event_status_btn.setText("End Event");
            }
            else
            {
                change_event_status_btn.setVisibility(View.GONE);
            }
        }



       change_event_status_btn.setOnClickListener(new ChangeEventStatusListener());



        member_name_edit_tv.setOnItemClickListener(new SelectAutoCompleTextViewItemListener());

        return convertView;
    }


    public class ChangeEventStatusListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {





//


            // Showing Alert Message

            if (event_status == 1)
            {
                if (eventBean.getEvent_type() == 2)
                {
                    showDialogChangeEvenetStatus("Are you sure you want to start this event?");

                }
                else
                {
                    //Friday 29 December 2017
                    Calendar currentDate = Calendar.getInstance(Locale.ENGLISH);
                    Calendar deadLineDate = Calendar.getInstance(Locale.ENGLISH);

                    ;


                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE d MMMM yyyy");

                    try
                    {
                        deadLineDate.setTime(simpleDateFormat.parse(eventBean.getEvent_signup_deadline_date()));
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getActivity() , e.getMessage() ,1).show();
                    }

                    if (Utill.compareTwoDate(currentDate ,deadLineDate ))
                    {
                        showDialogChangeEvenetStatus("Are you sure you want to start this event?");

                    }
                    else
                    {
                      Utill.showDialg("You can only start event after deadline" , getActivity());
                    }


                    //if (Utill.compareTwoDate())
                }

            }
             else
            {
                showDialogChangeEvenetStatus("Are you sure you want to end this event?");
            }


















        }
    }




    TextWatcher searchWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
            beforname = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub



            if(s.toString().length() != 0)
            {
                //Toast.makeText(getActivity(), "" +(s.toString().length() == 1 && beforname.length() == 0), 1).show();
                if(s.toString().length() == 1 && beforname.length() == 0)
                {























                    sqlListe.getMemberList(getActivity(), new MemberListListener() {
                        @Override
                        public void onSuccess(ArrayList<MemberListBean> memberArrayList)
                        {

                            memberBeanArrayList.clear();
                            memberBeanArrayList.addAll(memberArrayList);

                            memberAutoCompleteAdapter = new MemberAutoCompleteAdapter(getActivity() ,R.id.textViewItem, memberBeanArrayList ,member_name_edit_tv );
                            member_name_edit_tv.setAdapter(memberAutoCompleteAdapter);

                            member_name_edit_tv.setText(member_name_edit_tv.getText().toString()+"");
                            member_name_edit_tv.setSelection(member_name_edit_tv.getText().length());


                        }
                    },AppConstants.AllMEMBERlIST ,s.toString());


                }


            }



        }
    };




    public class SelectAutoCompleTextViewItemListener implements AdapterView.OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            member_name_edit_tv.setText(memberAutoCompleteAdapter.getResultList().get(position).getUser_first_name()+" "+memberAutoCompleteAdapter.getResultList().get(position).getUser_last_name());

            addNewModeator(memberAutoCompleteAdapter.getResultList().get(position).getUser_id());
            member_name_edit_tv.setText("");
            memberBeanArrayList.remove(position);
            //  joinEvntByDirector(adpter.getResultList().get(arg2).getUser_id());
           // memeberPopUp.dismiss();
        }
    }


    public void addNewModeator(String event_user_id)
    {
        HashMap<String ,Object>parasms = new HashMap<>();
        parasms.put("event_id" , event_id);
        parasms.put("event_club_id" ,event_club_id);

        parasms.put("event_user_id" ,event_user_id);


        httpRequest.getResponse(getActivity(), WebService.ManageEvent, parasms, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
             Log.e("jsonObject" ,jsonObject+"");






              try
              {

                  if (jsonObject.getBoolean("status"))
                  {
                      JSONObject user_list_json_array_item = jsonObject.getJSONObject("response");
                      MemberListBean memberListBean = new MemberListBean();
                      memberListBean.setUser_id(user_list_json_array_item.getString("user_id"));

                      memberListBean.setUser_email(user_list_json_array_item.getString("user_email"));

                      memberListBean.setUser_first_name(user_list_json_array_item.getString("user_name").split(" ")[0]);
                      memberListBean.setUser_last_name(user_list_json_array_item.getString("user_name").split(" ")[1]);

                      memberListBean.setUser_phone(user_list_json_array_item.getString("user_phone"));
                      memberListBean.setUser_profilepic(user_list_json_array_item.getString("user_profilepic"));
                      modeatormemberBeanArrayList.add(0 ,memberListBean);
                      moderatorMemberListAdapter.notifyDataSetChanged();
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








    public void removeModeator(final int pos , String event_user_id)
    {
        final HashMap<String ,Object>parasms = new HashMap<>();
        parasms.put("event_id" , event_id);
        parasms.put("event_club_id" ,event_club_id);

        parasms.put("event_user_id" ,event_user_id);


        httpRequest.getResponse(getActivity(), WebService.ManageEvent, parasms, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                Log.e("jsonObject" ,jsonObject+"");






                try
                {

                    if (jsonObject.getBoolean("status"))
                    {
                      modeatormemberBeanArrayList.remove(pos);
                        moderatorMemberListAdapter.notifyDataSetChanged();




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







    public void changeEventStatus(String event_state)
    {
        HashMap<String ,Object>parasms = new HashMap<>();
        parasms.put("event_id" , event_id);
        parasms.put("event_club_id" ,event_club_id);


        parasms.put("event_state" ,event_state);

        httpRequest.getResponse(getActivity(), WebService.ManageEvent, parasms, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                Log.e("jsonObject" ,jsonObject+"");






                try
                {

                    if (jsonObject.getBoolean("status"))
                    {
                        if (event_status == 1)
                        {
                            change_event_status_btn.setBackground(activity.getResources().getDrawable(R.drawable.dark_green_circle_back));
                            change_event_status_btn.setText("Start Event");

                        }
                        else
                        {
                            if (event_status == 2)
                            {
                                change_event_status_btn.setBackground(activity.getResources().getDrawable(R.drawable.red_circle_back));
                                change_event_status_btn.setText("End Event");
                                EventDetailFrageMent.eventBean.setEvent_state(2);
                            }
                            else
                            {
                                change_event_status_btn.setVisibility(View.GONE);
                                EventDetailFrageMent.eventBean.setEvent_state(3);
                                getActivity().getSupportFragmentManager().popBackStack();

                            }
                        }

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


    public boolean isUserExitsInMedotrorList(String userId)
    {
        for (int i = 0 ; i < modeatormemberBeanArrayList.size() ;i++)
        {
            if (modeatormemberBeanArrayList.get(i).getUser_id().equals(userId))
            {
                return true;
            }
        }

        return false ;
    }
    public  class ModifyEventTextClickListener implements View.OnClickListener
    {

        @Override
        public void onClick(View v)
        {


            if (R.id.manage_event_tab == v.getId())
            {
                /*AddModeratorFragment fragment = new AddModeratorFragment();
                fragment.setArgument(e.getEvent_id() ,bean.getEvent_club_id() ,modeatormemberBeanArrayList);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "fragment").commit();
*/
            }
            if (R.id.edit_event_tab == v.getId())
            {
                EditEventFragment fragment = new EditEventFragment();
                fragment.setInstanse(eventBean ,modeatormemberBeanArrayList );
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "fragment").commit();
                AddEventFragment.edit = false;
                //getActivity().getSupportFragmentManager().popBackStack();
            }



        }
    }


    public void showDialogChangeEvenetStatus(String msg)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle(SessionManager.getClubName(getActivity()));

        // Setting Dialog Message
        //alertDialog.setMessage("Are you sure you want to start this event?");

        // Setting Icon to Dialog

        alertDialog.setMessage(msg);
        // Setting Positive "Yes" Button
        alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // User pressed YES button. Write Logic Here
                dialog.cancel();
                changeEventStatus(++event_status+"");
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // User pressed No button. Write Logic Here

                dialog.cancel();
            }
        });

        alertDialog.show();
    }



    public FragmentActivity getActivity()
    {
        return activity;
    }


    FragmentBackResponseListener fragmentBackResponseListener = new FragmentBackResponseListener() {
        @Override
        public void UpdateView() {
            super.UpdateView();

            DirectorFragmentManageActivity.updateTitle("Manage Event");
        }
    };




}
