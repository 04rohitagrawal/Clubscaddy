package com.clubscaddy.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.cleveroad.adaptivetablelayout.OnItemClickListener;
import com.clubscaddy.Adapter.ClassExitingMemberListAdapter;
import com.clubscaddy.Adapter.MemberAutoCompleteAdapter;
import com.clubscaddy.Bean.ClassReservation;
import com.clubscaddy.Bean.EventMemberBean;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.DialogBoxButtonListner;
import com.clubscaddy.Interface.FragmentBackResponseListener;
import com.clubscaddy.Interface.MemberListListener;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.Interface.RecycleViewItemClickListner;
import com.clubscaddy.R;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.custumview.InstantAutoComplete;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.SqlListe;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by administrator on 14/12/17.
 */

public class AddPartivipentClass extends Fragment implements AdapterView.OnItemClickListener
{


   View convertView ;
    HttpRequest httpRequest ;
    Bundle bundle;

    SqlListe sqlListe ;

    String classDetailIds ;
boolean isFullClassVailableIList ;
    ShowUserMessage showUserMessage ;
    FragmentBackResponseListener updateclassListListener ;

    MemberAutoCompleteAdapter adpter;
      AutoCompleteTextView  searchNewAutoCompleteTextview ;
    ClassExitingMemberListAdapter classExitingMemberListAdapter ;
    RecyclerView participentListView ;

    ArrayList<ClassReservation> classDetailReservationList ;


    ArrayList<MemberListBean> existMember ;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        DirectorFragmentManageActivity.uploadNewsOrEditProfile.setImageResource(R.drawable.message_icon);
        DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.INVISIBLE);
        DirectorFragmentManageActivity.updateTitle("Participant");

        DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.VISIBLE);

        DirectorFragmentManageActivity.uploadNewsOrEditProfile.setOnClickListener(sendNotificationBtnClickListener);


        convertView = inflater.inflate(R.layout.add_participent_class , null);
        searchNewAutoCompleteTextview = (AutoCompleteTextView) convertView.findViewById(R.id.search_new_auto_complete_textview);
        searchNewAutoCompleteTextview.setOnItemClickListener(this);
        searchNewAutoCompleteTextview.addTextChangedListener(searchNewMemberWatcher);
        showUserMessage = new ShowUserMessage(getActivity());
        classDetailIds = getArguments().getString("class_detail_ids");


        classDetailReservationList = getArguments().getParcelableArrayList("class_list");


        isFullClassVailableIList = getArguments().getBoolean("isFullClassVailableIList");



        /*if (isFullClassVailableIList)
        {
            searchNewAutoCompleteTextview.setEnabled(false);
            ShowUserMessage.showUserMessage(getActivity() , "Class is full");
        }*/

        sqlListe = new SqlListe(getActivity());
        httpRequest = new HttpRequest(getActivity());
        existMember = new ArrayList<>();
        participentListView = (RecyclerView) convertView.findViewById(R.id.participent_list_view);
        classExitingMemberListAdapter = new ClassExitingMemberListAdapter(getActivity() , existMember , recycleViewItemClickListner ,fragmentBackResponseListener);
        participentListView.setAdapter(classExitingMemberListAdapter);
        participentListView.setLayoutManager(new LinearLayoutManager(getActivity()));


        if(existMember.size() == 0)
        {
            DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.INVISIBLE);
        }
        else

        {
            DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.VISIBLE);

        }


        bundle = getArguments() ;
        updateclassListListener = (FragmentBackResponseListener) bundle.getSerializable("updateclassListListener");



        getExtingMemberMemberList();

        return convertView;
    }

    public View.OnClickListener sendNotificationBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
             createDilog();

        }
    };


    TextView cancel_dialog_btn;
    TextView email_send_btn;
    TextView notification_send_btn;
    EditText email_notification_msg;
    TextView discription_textview_status;
    public void createDilog() {


        final Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.email_notification_layout);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
        discription_textview_status = (TextView) dialog.findViewById(R.id.discription_textview_status);

        email_notification_msg = (EditText) dialog.findViewById(R.id.email_notification_msg);


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
        cancel_dialog_btn = (TextView) dialog.findViewById(R.id.cancel_dialog_btn);
        email_send_btn = (TextView) dialog.findViewById(R.id.email_send_btn);
        notification_send_btn = (TextView) dialog.findViewById(R.id.notification_send_btn);
        email_notification_msg = (EditText) dialog.findViewById(R.id.email_notification_msg);
        cancel_dialog_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Utill.hideKeybord(getActivity() ,email_notification_msg);

                dialog.cancel();
            }
        });


        email_send_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (email_notification_msg.getText().toString() != "" && !email_notification_msg.getText().toString().equalsIgnoreCase("")) {


                    Utill.hideKeybord(getActivity() ,email_notification_msg);
                    dialog.cancel();
                    sendEmailAndNotification("1" ,email_notification_msg.getText().toString() );

                } else {
                    Utill.showDialg("Enter Message", getActivity());
                    //Toast.makeText(getActivity(), "Enter Message", Toast.LENGTH_LONG).show();
                }

            }
        });


        notification_send_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (email_notification_msg.getText().toString() != "" && !email_notification_msg.getText().toString().equalsIgnoreCase("")) {
                    Utill.hideKeybord(getActivity() ,email_notification_msg);
                    dialog.cancel();

                    sendEmailAndNotification("2" ,email_notification_msg.getText().toString() );


                } else {
                    Utill.showDialg("Enter Message", getActivity());
                    //Toast.makeText(getActivity(), "Enter Message", Toast.LENGTH_LONG).show();
                }

            }
        });

	/*

*/
    }

    String previousText = "";

    TextWatcher searchNewMemberWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            previousText = s.toString() ;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s)
        {
            if (previousText.length() == 0 && searchNewAutoCompleteTextview.getText().toString().length() ==1)
            {
                getMemberList(searchNewAutoCompleteTextview.getText().toString() );
            }

        }
    };

    private void getMemberList(String  key_name)
    {
        sqlListe.getMemberList(getActivity(), new MemberListListener() {
            @Override
            public void onSuccess(ArrayList<MemberListBean> alMemberList)
            {

                ArrayList<MemberListBean> memberListBeanArrayList = new ArrayList<MemberListBean>();

                for (int i = 0 ; i < alMemberList.size() ;i++)
                {

                    boolean isExits = false;
                    for (int j = 0 ; j < existMember.size() ;j++)
                    {
                        if (existMember.get(j).getUser_id().equals(alMemberList.get(i).getUser_id())  )
                        {
                            isExits = true ;

                        }

                    }
                    if (isExits == false &&  alMemberList.get(i).getUser_type().equals(AppConstants.USER_TYPE_DIRECTOR)==false)
                    {
                        memberListBeanArrayList.add(alMemberList.get(i));
                    }
                }



                if (memberListBeanArrayList.size() > 0)
                {

                    adpter = new MemberAutoCompleteAdapter(getActivity(), R.id.textViewItem,memberListBeanArrayList,searchNewAutoCompleteTextview);
                    searchNewAutoCompleteTextview.setAdapter(adpter);
                    searchNewAutoCompleteTextview.setText(searchNewAutoCompleteTextview.getText().toString()+"");
                    searchNewAutoCompleteTextview.setSelection(1);

                    //showMemberSelector();
                    // setEventAdapter(alMemberList);


                    // AddPartivipentAdapter addPartivipentAdapter = new AddPartivipentAdapter(getActivity(),memberListBeanArrayList);
                    // searchNewAutoCompleteTextview.setAdapter(addPartivipentAdapter);

                } else {
                    ShowUserMessage.showUserMessage(getActivity(), getResources().getString(R.string.no_record_found));
                }


            }
        },AppConstants.AllMEMBERlIST ,key_name);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

        Utill.hideKeybord(getActivity());
        MemberListBean memberListBean = (MemberListBean) parent.getItemAtPosition(position);
        searchNewAutoCompleteTextview.setText(adpter.getFilterList().get(position).getUser_first_name()+" "+adpter.getFilterList().get(position).getUser_last_name());

        addMemberOnClass(memberListBean.getUser_id() ,memberListBean);
    }



  /*  public class AddPartivipentAdapter extends ArrayAdapter<MemberListBean> {

        ArrayList<MemberListBean> memberListBeanArrayList,tempCustomer, suggestions;

        public AddPartivipentAdapter(Context context, ArrayList<MemberListBean> memberListBeanArrayList) {
            super(context, android.R.layout.simple_list_item_1, memberListBeanArrayList);
            this.memberListBeanArrayList = memberListBeanArrayList;
            this.tempCustomer = new ArrayList<MemberListBean>(memberListBeanArrayList);
            this.suggestions = new ArrayList<MemberListBean>(memberListBeanArrayList);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MemberListBean customer = getItem(position);

            TextView tv = new TextView(getActivity());

            tv.setText(customer.getUser_first_name());

            return tv;
        }

        @NonNull
        @Override
        public Filter getFilter() {
            return myFilter;
        }

        Filter myFilter = new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                MemberListBean customer = (MemberListBean) resultValue;
                return customer.getUser_first_name();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    suggestions.clear();
                    for (MemberListBean people : tempCustomer) {
                        if (people.getUser_first_name().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            suggestions.add(people);
                        }
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ArrayList<MemberListBean> c = (ArrayList<MemberListBean>) results.values;
                if (results != null && results.count > 0) {
                    clear();
                    for (MemberListBean cust : c) {
                        add(cust);
                        notifyDataSetChanged();
                    }
                }
            }
        };
    }*/


    public void addMemberOnClass(String classMemberUid,final MemberListBean memberListBean)
    {
        HashMap<String , Object> param = new HashMap<>();
        param.put("class_member_uid" ,classMemberUid );
        param.put("user_type" ,SessionManager.getUser_type(getActivity()) );
        param.put("class_detail_ids" ,classDetailIds );


        httpRequest.getResponse(getActivity(), WebService.classSignupLink, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {

                Log.e("jsonObject" ,jsonObject+"");
                try
                {
                    if (jsonObject.getBoolean("status"))
                    {
                        existMember.add(memberListBean);

                        if(existMember.size() == 0)
                        {
                            DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.INVISIBLE);
                        }
                        else

                        {
                            DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.VISIBLE);

                        }

                        classExitingMemberListAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("message"));
                        getExtingMemberMemberList();
                    }

                }
                catch (Exception e)
                {

                }


                searchNewAutoCompleteTextview.setText("");
            }
        });
    }


    public void getExtingMemberMemberList()
    {
        HashMap<String , Object> param = new HashMap<>();
        param.put("class_uid" ,SessionManager.getUser_id(getActivity()) );
        param.put("user_type" ,SessionManager.getUser_type(getActivity()) );
        param.put("class_detail_ids" ,classDetailIds );


        httpRequest.getResponse(getActivity(), WebService.classUserList, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                existMember.clear();
                Log.e("jsonObject" ,jsonObject+"");
                try
                {
                    //ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("message"));

                    if (jsonObject.getBoolean("status"))
                    {
                        JSONArray responseJsonArray = jsonObject.getJSONArray("Response");
                        for (int i = 0 ; i < responseJsonArray.length() ;i++)
                        {
                            JSONObject responseJsonArrayItem = responseJsonArray.getJSONObject(i);
                            MemberListBean memberListBean = new MemberListBean();
                            memberListBean.setUser_id(responseJsonArrayItem.optString("class_member_uid"));

                            String userName = responseJsonArrayItem.getString("user_name") ;

                            memberListBean.setUser_first_name(userName.split(" ")[0]);

                            if (userName.split(" ").length >= 2)
                            {
                                memberListBean.setUser_last_name(userName.split(" ")[1]);

                            }

                            memberListBean.setUser_profilepic(responseJsonArrayItem.getString("user_profilepic"));
                            existMember.add(memberListBean);

                        }

                        if(existMember.size() == 0)
                        {
                            DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.INVISIBLE);
                        }
                        else

                        {
                            DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.VISIBLE);

                        }

                        classExitingMemberListAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                      ShowUserMessage.showUserMessage( getActivity() , jsonObject.getString("message") );
                    }



                }
                catch (Exception e)
                {

                }


                searchNewAutoCompleteTextview.setText("");
            }
        });
    }
    RecycleViewItemClickListner recycleViewItemClickListner = new RecycleViewItemClickListner() {
        @Override
        public void onItemClick(final int pos, int status)
        {

            showUserMessage.showDialogBoxWithYesNoButton("Are you sure you want to delete member?", new DialogBoxButtonListner() {
                @Override
                public void onYesButtonClick(DialogInterface dialog) {
                    deleteMemberOnClass(existMember.get(pos).getUser_id() , pos);
                }
            });
        }
    };




    public String getExitMemberIds()
    {
        String ids = "";

        for (int i = 0 ;i < existMember.size() ;i++)
        {
            if (Validation.isStringNullOrBlank(ids))
            {
            ids = existMember.get(i).getUser_id();
            }
            else
            {
                ids =ids+","+ existMember.get(i).getUser_id();

            }
        }



        return ids ;

    }


    public void deleteMemberOnClass(String classMemberUid , final int pos)
    {
        HashMap<String , Object> param = new HashMap<>();
        param.put("class_member_uid" ,classMemberUid );
        param.put("user_type" ,SessionManager.getUser_type(getActivity()) );
        param.put("class_detail_ids" ,classDetailIds );


        httpRequest.getResponse(getActivity(), WebService.class_member_delete, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {

                Log.e("jsonObject" ,jsonObject+"");
                try
                {
                    if(jsonObject.getBoolean("status"))
                    {
                       existMember.remove(pos);
                        classExitingMemberListAdapter.notifyDataSetChanged();

                        if(existMember.size() == 0)
                        {
                            DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.INVISIBLE);
                        }
                        else

                        {
                            DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.VISIBLE);

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


                searchNewAutoCompleteTextview.setText("");
            }
        });
    }

    public  void sendEmailAndNotification(String notify_via ,String msg)
    {

        HashMap<String,Object> param = new HashMap<>();
        param.put("notify_via" ,notify_via);
        param.put("reciver_id" ,getExitMemberIds());
        param.put("sender_id" ,SessionManager.getUser_id(getActivity()));
        param.put("msg" ,msg);

        httpRequest.getResponse(getActivity(), WebService.sendnotification_class, param, new OnServerRespondingListener(getActivity()) {
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
    @Override
    public void onDestroy() {

        super.onDestroy();
        if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_DIRECTOR)) {
            DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.VISIBLE);
        }
        DirectorFragmentManageActivity.updateTitle("Class Reservation");
        DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.GONE);

        updateclassListListener.onBackFragment();
    }






    public String getUnfullClassIds()
    {
        String ids = "";



        isFullClassVailableIList = false ;


        for (int i = 0 ; i < classDetailReservationList.size() ;i++)
        {

                ClassReservation classReservation = classDetailReservationList.get(i) ;

                if (classReservation.getClassNoOfParticipents() <= classReservation.getClassParticipents())
                {
                    if (Validation.isStringNullOrBlank(ids))
                    {
                        ids =  classReservation.getClassDetailId() ;
                    }
                    else
                    {
                        ids = ids+","+ classReservation.getClassDetailId() ;

                    }



                }


            }







        return ids ;

    }



   FragmentBackResponseListener fragmentBackResponseListener = new FragmentBackResponseListener() {
       @Override
       public void onBackFragment() {
           super.onBackFragment();



       }

       @Override
       public void UpdateView() {
           super.UpdateView();
           DirectorFragmentManageActivity.uploadNewsOrEditProfile.setImageResource(R.drawable.msg_icon_participeting);
           DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.INVISIBLE);
           DirectorFragmentManageActivity.updateTitle("Participant");

           DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.VISIBLE);

           DirectorFragmentManageActivity.uploadNewsOrEditProfile.setOnClickListener(sendNotificationBtnClickListener);

       }
   };


















}





































/*
package com.clubscaddy.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.cleveroad.adaptivetablelayout.OnItemClickListener;
import com.clubscaddy.Adapter.ClassExitingMemberListAdapter;
import com.clubscaddy.Adapter.MemberAutoCompleteAdapter;
import com.clubscaddy.Bean.ClassReservation;
import com.clubscaddy.Bean.EventMemberBean;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.DialogBoxButtonListner;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.Interface.RecycleViewItemClickListner;
import com.clubscaddy.R;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

*/
/**
 * Created by administrator on 14/12/17.
 *//*


public class AddPartivipentClass extends Fragment implements AdapterView.OnItemClickListener
{


   View convertView ;
    HttpRequest httpRequest ;

    String getClassDetailIds ;
    String addClassclassDetailIds ;
    String removeClassDetailIds ;

    boolean isFullClassVailableIList ;
    ShowUserMessage showUserMessage ;

    MemberAutoCompleteAdapter adpter;
      AutoCompleteTextView  searchNewAutoCompleteTextview ;
    ClassExitingMemberListAdapter classExitingMemberListAdapter ;
    RecyclerView participentListView ;

    ArrayList<ClassReservation> classDetailReservationList ;


    ArrayList<MemberListBean> existMember ;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.INVISIBLE);
        DirectorFragmentManageActivity.updateTitle("Participant");

        DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.VISIBLE);

        DirectorFragmentManageActivity.uploadNewsOrEditProfile.setOnClickListener(sendNotificationBtnClickListener);

        DirectorFragmentManageActivity.uploadNewsOrEditProfile.setImageResource(R.drawable.msg_icon_participeting);

        convertView = inflater.inflate(R.layout.add_participent_class , null);
        searchNewAutoCompleteTextview = (AutoCompleteTextView) convertView.findViewById(R.id.search_new_auto_complete_textview);
        searchNewAutoCompleteTextview.setOnItemClickListener(this);
        searchNewAutoCompleteTextview.addTextChangedListener(searchNewMemberWatcher);
        showUserMessage = new ShowUserMessage(getActivity());



        classDetailReservationList = getArguments().getParcelableArrayList("class_list");


        isFullClassVailableIList = getArguments().getBoolean("isFullClassVailableIList");

        addClassclassDetailIds = getUnfullClassIds();
        getClassDetailIds = getSelectedIds();


        if (Validation.isStringNullOrBlank(addClassclassDetailIds) )
        {
            searchNewAutoCompleteTextview.setEnabled(false);
            ShowUserMessage.showUserMessage(getActivity() , "Class is full");
        }


        httpRequest = new HttpRequest(getActivity());
        existMember = new ArrayList<>();
        participentListView = (RecyclerView) convertView.findViewById(R.id.participent_list_view);
        classExitingMemberListAdapter = new ClassExitingMemberListAdapter(getActivity() , existMember , recycleViewItemClickListner);
        participentListView.setAdapter(classExitingMemberListAdapter);
        participentListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getClassExitingMemberList();

        return convertView;
    }

    public View.OnClickListener sendNotificationBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {


        }
    };




	*/
/*

*//*

    }

    String previousText = "";

    TextWatcher searchNewMemberWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            previousText = s.toString() ;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s)
        {
            if (previousText.length() == 0 && searchNewAutoCompleteTextview.getText().toString().length() ==1)
            {
                getMembersList(searchNewAutoCompleteTextview.getText().toString() );
            }

        }
    };



    private void getMembersList(String key_name )
    {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("user_type", AppConstants.USER_TYPE_MEMBER);
            params.put("user_club_id", SessionManager.getUser_Club_id(getActivity()));
            params.put("user_name", key_name);

            httpRequest.getResponse(getActivity(), WebService.get_all_member_list, params, new OnServerRespondingListener(getActivity()) {
                @Override
                public void onSuccess(JSONObject jsonObject)
                {

                    ArrayList<MemberListBean> memberListBeanArrayList = new ArrayList<MemberListBean>();

                    ArrayList<MemberListBean> alMemberList = JsonUtility.parserMembersList(jsonObject+"", getActivity());
                    //ArrayList<EventMemberBean> existMember = eventDetailFrageMent.eventBean.getParticipantsList();
                    for (int i = 0 ; i < alMemberList.size() ;i++)
                    {

                        boolean isExits = false;
                        for (int j = 0 ; j < existMember.size() ;j++)
                        {
                            if (existMember.get(j).getUser_id().equals(alMemberList.get(i).getUser_id())  )
                            {
                                isExits = true ;

                            }

                        }
                        if (isExits == false &&  alMemberList.get(i).getUser_type().equals(AppConstants.USER_TYPE_DIRECTOR)==false)
                        {
                            memberListBeanArrayList.add(alMemberList.get(i));
                        }
                    }



                    if (memberListBeanArrayList.size() > 0)
                    {

                        adpter = new MemberAutoCompleteAdapter(getActivity(), R.id.textViewItem,memberListBeanArrayList);
                        searchNewAutoCompleteTextview.setAdapter(adpter);
                        searchNewAutoCompleteTextview.setText(searchNewAutoCompleteTextview.getText().toString()+"");
                        searchNewAutoCompleteTextview.setSelection(1);

                        //showMemberSelector();
                        // setEventAdapter(alMemberList);


                       // AddPartivipentAdapter addPartivipentAdapter = new AddPartivipentAdapter(getActivity(),memberListBeanArrayList);
                       // searchNewAutoCompleteTextview.setAdapter(addPartivipentAdapter);

                    } else {
                        ShowUserMessage.showUserMessage(getActivity(), getResources().getString(R.string.no_record_found));
                    }

                }
            });



        } catch (Exception e) {
            ShowUserMessage.showUserMessage(getActivity(), e.toString());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

        Utill.hideKeybord(getActivity());
        MemberListBean memberListBean = (MemberListBean) parent.getItemAtPosition(position);
        searchNewAutoCompleteTextview.setText(adpter.getFilterList().get(position).getUser_first_name()+" "+adpter.getFilterList().get(position).getUser_last_name());

        addMemberOnClass(memberListBean.getUser_id() ,memberListBean);
    }



  */
/*  public class AddPartivipentAdapter extends ArrayAdapter<MemberListBean> {

        ArrayList<MemberListBean> memberListBeanArrayList,tempCustomer, suggestions;

        public AddPartivipentAdapter(Context context, ArrayList<MemberListBean> memberListBeanArrayList) {
            super(context, android.R.layout.simple_list_item_1, memberListBeanArrayList);
            this.memberListBeanArrayList = memberListBeanArrayList;
            this.tempCustomer = new ArrayList<MemberListBean>(memberListBeanArrayList);
            this.suggestions = new ArrayList<MemberListBean>(memberListBeanArrayList);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MemberListBean customer = getItem(position);

            TextView tv = new TextView(getActivity());

            tv.setText(customer.getUser_first_name());

            return tv;
        }

        @NonNull
        @Override
        public Filter getFilter() {
            return myFilter;
        }

        Filter myFilter = new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                MemberListBean customer = (MemberListBean) resultValue;
                return customer.getUser_first_name();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    suggestions.clear();
                    for (MemberListBean people : tempCustomer) {
                        if (people.getUser_first_name().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            suggestions.add(people);
                        }
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ArrayList<MemberListBean> c = (ArrayList<MemberListBean>) results.values;
                if (results != null && results.count > 0) {
                    clear();
                    for (MemberListBean cust : c) {
                        add(cust);
                        notifyDataSetChanged();
                    }
                }
            }
        };
    }*//*



    public void addMemberOnClass(String classMemberUid,final MemberListBean memberListBean)
    {
        addClassclassDetailIds = getUnfullClassIds() ;
        HashMap<String , Object> param = new HashMap<>();
        param.put("class_member_uid" ,classMemberUid );
        param.put("user_type" ,SessionManager.getUser_type(getActivity()) );
        param.put("class_detail_ids" ,addClassclassDetailIds );


        httpRequest.getResponse(getActivity(), WebService.classSignupLink, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {

                Log.e("jsonObject" ,jsonObject+"");
                try
                {
                    if (jsonObject.getBoolean("status"))
                    {
                        existMember.add(memberListBean);


                        classExitingMemberListAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("message"));

                    }

                }
                catch (Exception e)
                {

                }


                searchNewAutoCompleteTextview.setText("");

                addParticipentCountInList();


                addClassclassDetailIds = getUnfullClassIds();



                if (Validation.isStringNullOrBlank(addClassclassDetailIds) )
                {
                    searchNewAutoCompleteTextview.setEnabled(false);
                    ShowUserMessage.showUserMessage(getActivity() , "Class is full");
                }
            }
        });
    }


    public void getClassExitingMemberList()
    {
        HashMap<String , Object> param = new HashMap<>();
        param.put("class_uid" ,SessionManager.getUser_id(getActivity()) );
        param.put("user_type" ,SessionManager.getUser_type(getActivity()) );
        param.put("class_detail_ids" ,getSelectedIds() );


        httpRequest.getResponse(getActivity(), WebService.classUserList, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {

                Log.e("jsonObject" ,jsonObject+"");
                try
                {
                    //ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("message"));

                    if (jsonObject.getBoolean("status"))
                    {
                        JSONArray responseJsonArray = jsonObject.getJSONArray("Response");
                        for (int i = 0 ; i < responseJsonArray.length() ;i++)
                        {
                            JSONObject responseJsonArrayItem = responseJsonArray.getJSONObject(i);
                            MemberListBean memberListBean = new MemberListBean();
                            memberListBean.setUser_id(responseJsonArrayItem.optString("class_member_uid"));
                            memberListBean.setUser_first_name(responseJsonArrayItem.getString("user_name"));
                            memberListBean.setUser_profilepic(responseJsonArrayItem.getString("user_profilepic"));
                            existMember.add(memberListBean);

                        }
                        classExitingMemberListAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                      ShowUserMessage.showUserMessage( getActivity() , jsonObject.getString("message") );
                    }



                }
                catch (Exception e)
                {

                }


                searchNewAutoCompleteTextview.setText("");
            }
        });
    }
    RecycleViewItemClickListner recycleViewItemClickListner = new RecycleViewItemClickListner() {
        @Override
        public void onItemClick(final int pos, int status)
        {

            showUserMessage.showDialogBoxWithYesNoButton("Are you sure you want to delete member?", new DialogBoxButtonListner() {
                @Override
                public void onYesButtonClick(DialogInterface dialog) {
                    deleteMemberOnClass(existMember.get(pos).getUser_id() , pos);
                }
            });
        }
    };


    public void deleteMemberOnClass(String classMemberUid , final int pos)
    {
        HashMap<String , Object> param = new HashMap<>();
        param.put("class_member_uid" ,classMemberUid );
        param.put("user_type" ,SessionManager.getUser_type(getActivity()) );
        param.put("class_detail_ids" ,getSelectedIds() );


        httpRequest.getResponse(getActivity(), WebService.class_member_delete, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {

                Log.e("jsonObject" ,jsonObject+"");
                try
                {
                    if(jsonObject.getBoolean("status"))
                    {
                       existMember.remove(pos);
                        classExitingMemberListAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("message"));

                    }

                }
                catch (Exception e)
                {

                }


                 removeParticipentCountInList();




                addClassclassDetailIds = getUnfullClassIds();



                if (Validation.isStringNullOrBlank(addClassclassDetailIds) )
                {
                    searchNewAutoCompleteTextview.setEnabled(false);
                    ShowUserMessage.showUserMessage(getActivity() , "Class is full");
                }
                searchNewAutoCompleteTextview.setText("");
            }
        });
    }

    public  void sendEmailAndNotification(String notify_via ,String msg)
    {

        HashMap<String,Object> param = new HashMap<>();
        param.put("notify_via" ,notify_via);
        param.put("class_detail_ids" ,getClassDetailIds);
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_DIRECTOR)) {
            DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.VISIBLE);
        }
        DirectorFragmentManageActivity.updateTitle("Class Reservation");
        DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.GONE);


    }






    public String getUnfullClassIds()
    {
        String ids = "";



        isFullClassVailableIList = false ;


        for (int i = 0 ; i < classDetailReservationList.size() ;i++)
        {

                ClassReservation classReservation = classDetailReservationList.get(i) ;

                if (classReservation.getClassNoOfParticipents() != classReservation.getClassParticipents())
                {
                    if (Validation.isStringNullOrBlank(ids))
                    {
                        ids =  classReservation.getClassDetailId() ;
                    }
                    else
                    {
                        ids = ids+","+ classReservation.getClassDetailId() ;

                    }



                }


            }







        return ids ;

    }





    public void addParticipentCountInList()
    {



        isFullClassVailableIList = false ;

        String ids[]= addClassclassDetailIds.split(",");

        for (int i = 0 ; i < classDetailReservationList.size() ;i++)
        {

            for (int j = 0 ; j < ids.length ;j++)
            {
                ClassReservation classReservation = classDetailReservationList.get(i) ;

                if (classReservation.getClassDetailId().equals(ids[j]))
                {
                    classReservation.setClassParticipents(classReservation.getClassParticipents()+1);
                }
            }





        }








    }


    public void removeParticipentCountInList()
    {



        isFullClassVailableIList = false ;

        String ids[]= removeClassDetailIds.split(",");

        for (int i = 0 ; i < classDetailReservationList.size() ;i++)
        {

            for (int j = 0 ; j < ids.length ;j++)
            {
                ClassReservation classReservation = classDetailReservationList.get(i) ;

                if (classReservation.getClassDetailId().equals(ids[j]))
                {
                    classReservation.setClassParticipents(classReservation.getClassParticipents()-1);
                }
            }





        }








    }

    public String getSelectedIds()
    {
        String ids = "";


//////////////////////

        isFullClassVailableIList = false ;


        for (int i = 0 ; i < classDetailReservationList.size() ;i++)
        {
            {
                ClassReservation classReservation = classDetailReservationList.get(i) ;

                if (classReservation.isItemSelected())
                {
                    if (Validation.isStringNullOrBlank(ids))
                    {
                        ids =  classReservation.getClassDetailId() ;
                    }
                    else
                    {
                        ids = ids+","+ classReservation.getClassDetailId() ;

                    }



                }


            }


        }




        return ids ;

    }




}
*/
