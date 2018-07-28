package com.clubscaddy.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.clubscaddy.Adapter.GroupAdapter;
import com.clubscaddy.Adapter.MemberAutoCompleteAdapter;
import com.clubscaddy.Bean.EventMemberBean;
import com.clubscaddy.Bean.GroupBean;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.Interface.MemberListListener;
import com.clubscaddy.custumview.ExpandableHeightListView;
import com.clubscaddy.custumview.InstantAutoComplete;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.SqlListe;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by administrator on 25/1/17.
 */

public class AddParticipantInEvent extends DialogFragment implements View.OnClickListener
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
    EventDetailFrageMent eventDetailFrageMent ;

    ArrayList<GroupBean> groupList ;

    ArrayList<GroupBean> copygroupList ;

    ListView group_list_view;


    SqlListe sqlListe ;


    public void setInstanse( EventDetailFrageMent eventDetailFrageMent )
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

        sqlListe = new SqlListe(getActivity());


        group_name_ev = (EditText) convertView.findViewById(R.id.group_name_ev);

        group_name_ev.addTextChangedListener(searchWatcherforGroup);

        search_member = (AutoCompleteTextView) convertView.findViewById(R.id.search_member);

        search_member.addTextChangedListener(searchWatcher);

        groupList = new ArrayList<>();

        copygroupList = new ArrayList<>();

        individual_tab_line_view =  convertView.findViewById(R.id.individual_tab_line_view);
        by_group_line_view =  convertView.findViewById(R.id.by_group_line_view);

        group_list_view = (ListView) convertView.findViewById(R.id.group_list_view);

        group_list_view.setOnItemClickListener(onItemClickListener);

        individual_tab_layout = (LinearLayout) convertView.findViewById(R.id.individual_tab_layout);
        group_tab_layout = (LinearLayout) convertView.findViewById(R.id.group_tab_layout);


        group_tab.setOnClickListener(this);
        individual_tab.setOnClickListener(this);
        getGroupList();
        return convertView;
    }


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

                    getMembersList(s.toString() ,search_member);





                    search_member.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
                        {
							/**/
                            search_member.setText(adpter.getResultList().get(arg2).getUser_first_name()+" "+adpter.getResultList().get(arg2).getUser_last_name());
                            joinEvntByDirector(adpter.getResultList().get(arg2).getUser_id() ,null);

                            //Utill.showDialg(adpter.getResultList().get(arg2).getUser_id(), mContext);
                        }
                    });
                }


            }


			/*filterList(str);
			getMembersList();*/
        }
    };






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









    MemberAutoCompleteAdapter adpter;
    private void getMembersList(String keyWord,
                                final AutoCompleteTextView autocompletetv ) {
        sqlListe.getMemberList(getActivity(), new MemberListListener() {
            @Override
            public void onSuccess(ArrayList<MemberListBean> alMemberList)
            {

                ArrayList<MemberListBean> memberListBeanArrayList = new ArrayList<MemberListBean>();

                ArrayList<EventMemberBean> existMember = eventDetailFrageMent.eventBean.getParticipantsList();

                for (int i = 0 ; i < alMemberList.size() ;i++)
                {

                    boolean isExits = false;
                    for (int j = 0 ; j < existMember.size() ;j++)
                    {
                        if (existMember.get(j).getUser_id().equals(alMemberList.get(i).getUser_id()) )
                        {
                            isExits = true ;

                        }

                    }
                    if (isExits == false)
                    {
                        memberListBeanArrayList.add(alMemberList.get(i));
                    }
                }



                if (memberListBeanArrayList.size() > 0) {

                    adpter = new MemberAutoCompleteAdapter(getActivity(),
                            R.id.textViewItem,memberListBeanArrayList ,autocompletetv);
                    autocompletetv.setAdapter(adpter);
                    autocompletetv.setText(autocompletetv.getText().toString()+"");
                    autocompletetv.setSelection(1);
                    //showMemberSelector();
                    // setEventAdapter(alMemberList);
                } else {
                    ShowUserMessage.showUserMessage(getActivity(), getResources().getString(R.string.no_record_found));
                }

            }
        },AppConstants.AllMEMBERlIST ,keyWord);
    }




    void joinEvntByDirector(final String userId ,final String groupId) {

        if (Utill.isNetworkAvailable(getActivity())) {
            Utill.showProgress(getActivity());
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("event_club_id", SessionManager.getUser_Club_id(getActivity()));
            if (Validation.isStringNullOrBlank(userId) == false)
            {
                params.put("event_user_id", userId);
            }
            else
            {
                params.put("group_id", groupId);
            }


            params.put("event_id", eventDetailFrageMent.eventBean.getEvent_id());
            Utill.hideKeybord(getActivity() , search_member);

            //	10-03 12:35:12.065: E/update participent user(9206): {"Response":{"user_profilepic":"http:\/\/72.167.41.165\/\/appwebservices\/\/user_images\/1441634730.jpeg","user_name":"yp hd","user_id":"326"},"message":"Join Successfully","status":"true"}

            GlobalValues.getModelManagerObj(getActivity()).joinEventReply(params, new ModelManagerListener() {
                @Override
                public void onSuccess(String json) {
                    //	ShowUserMessage.showUserMessage(mContext, json);
                    Utill.hideProgress();

                    try
                    {
                        JSONObject jsonObj = new JSONObject(json);

                        if (jsonObj.getBoolean("status"))
                        {
                            if (Validation.isStringNullOrBlank(userId) == false)
                            {
                                EventMemberBean eventBean1 = new EventMemberBean();
                                JSONObject ResponseObj = new JSONObject(jsonObj.getString("response"));
                                eventBean1.setUser_id(ResponseObj.getString("user_id"));
                                eventBean1.setUser_name(ResponseObj.getString("user_name"));
                                eventBean1.setUser_profilepic(ResponseObj.getString("user_profilepic"));
                                eventBean1.setUser_email(ResponseObj.getString("user_email"));
                                eventBean1.setUser_no(ResponseObj.getString("user_phone"));
                                eventDetailFrageMent.eventBean.getParticipantsList().add(eventBean1)	;
                                eventDetailFrageMent.eventParticipantsAdapter.notifyDataSetChanged();
                                ExpandableHeightListView.getListViewSize(eventDetailFrageMent.participantsListView);

                                search_member.setText("");

                                dialog.cancel();
                                Utill.showDialg(jsonObj.getString("message"), getActivity());
                            }
                            else
                            {
                                getGroupListItem(groupId);
                            }

                        }
                        else
                        {
                            Utill.showDialg(jsonObj.getString("message"), getActivity());
                        }





                        //Toast.makeText(getActivity(), jsonObj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    catch(Exception e)
                    {
                        //Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();	;
                    }


                }

                @Override
                public void onError(String msg) {
                    ShowUserMessage.showUserMessage(getActivity(), msg);
                    Utill.hideProgress();
                }
            });
        } else {
            Utill.showNetworkError(getActivity());
        }

    }

    private void getGroupList() {
        if (Utill.isNetworkAvailable(getActivity())) {
            Utill.showProgress(getActivity());
            GlobalValues.getModelManagerObj(getActivity()).getGroupList(new ModelManagerListener() {
                @Override
                public void onSuccess(String json) {

                    dialog.setCancelable(true);
                    groupList.addAll(JsonUtility.parseGroupList(json));

                    copygroupList.addAll(JsonUtility.parseGroupList(json));
                    setGroupAdapter(groupList);

                    Utill.hideProgress();
                }

                @Override
                public void onError(String msg) {
                    ShowUserMessage.showUserMessage(getActivity(), msg);
                    Utill.hideProgress();
                }
            });
        } else {
            dialog.setCancelable(true);
            Utill.showNetworkError(getActivity());
        }
    }
    GroupAdapter groupAdapter;
    public void setGroupAdapter(final ArrayList<GroupBean> list) {


        groupAdapter = new GroupAdapter(getActivity(), groupList);





        GroupAdapter.SwipeEventItemClickListener swipeEventItemClickListener = new GroupAdapter.SwipeEventItemClickListener() {
            @Override
            public void OnEditClick(int position) {
                joinEvntByDirector( null ,groupList.get(position).getGroup_id());
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




    private void getGroupListItem(String group_id) {
        if (Utill.isNetworkAvailable(getActivity())) {

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("group_id", group_id);
            Utill.showProgress(getActivity());
            GlobalValues.getModelManagerObj(getActivity()).getGroupListItem(new ModelManagerListener() {
                @Override
                public void onSuccess(String json) {
                    Utill.hideProgress();
                   //

                    try
                    {

                    ArrayList<GroupBean>    groupList = JsonUtility.parseGroupList(json);

                      ArrayList<MemberListBean> groupmemebers = groupList.get(0).getMembersList();
                        ArrayList<EventMemberBean> existMember = eventDetailFrageMent.eventBean.getParticipantsList();
                        for (int i = 0 ; i < groupmemebers.size() ; i++)
                        {
                            boolean isExits = false;
                            for (int j = 0 ; j < existMember.size() ;j++)
                            {
                                if (existMember.get(j).getUser_id().equals(groupmemebers.get(i).getUser_id()) )
                                {
                                    isExits = true ;

                                }

                            }
                            if (isExits == false)
                            {
                                EventMemberBean eventMemberBean = new EventMemberBean();
                                eventMemberBean.setUser_id(groupmemebers.get(i).getUser_id());
                                eventMemberBean.setUser_name(groupmemebers.get(i).getUser_first_name()+" "+groupmemebers.get(i).getUser_last_name());
                                eventMemberBean.setUser_email(groupmemebers.get(i).getUser_email());
                                eventMemberBean.setUser_profilepic(groupmemebers.get(i).getUser_profilepic());
                                eventMemberBean.setUser_no(groupmemebers.get(i).getUser_phone());
                                eventDetailFrageMent.eventBean.getParticipantsList().add(eventMemberBean);
                                ExpandableHeightListView.getListViewSize(eventDetailFrageMent.participantsListView);

                            }
                        }

                        eventDetailFrageMent.eventParticipantsAdapter.notifyDataSetChanged();
                        dialog.cancel();

                    }
                    catch (Exception e)
                    {

                    }


                }

                @Override
                public void onError(String msg) {
                    ShowUserMessage.showUserMessage(getActivity(), msg);
                    Utill.hideProgress();
                }
            },params);
        } else {
            Utill.showNetworkError(getActivity());
        }
    }

   public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
       @Override
       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

           Utill.hideKeybord(getActivity() , group_name_ev);

           joinEvntByDirector( null ,groupList.get(position).getGroup_id());

       }
   };



}
