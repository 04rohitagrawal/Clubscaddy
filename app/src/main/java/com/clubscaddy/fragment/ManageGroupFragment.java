package com.clubscaddy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.clubscaddy.Adapter.GroupAdapter;
import com.clubscaddy.Adapter.MySchduleListAdapter;
import com.clubscaddy.Adapter.SchduleListAdapter;
import com.clubscaddy.Bean.GroupBean;
import com.clubscaddy.Bean.League;
import com.clubscaddy.Bean.MySchdule;

import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.R;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.utility.DividerItemDecoration;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by administrator on 22/5/17.
 */

public class ManageGroupFragment extends Fragment
{
    RecyclerView scheduling_recyle_view;
    View convertView ;
    ArrayList<GroupBean> groupList;
    GroupAdapter groupAdapter;
    public static int tabIndex =1;
    ListView group_list_view;
    ImageButton add_group_btn;
    LinearLayout group_tab_layout , scheduling_tab_layout ;

    RelativeLayout group_list_layout;

    ImageButton add_scheduling_btn;
    HttpRequest httpRequest ;
    SessionManager sessionManager ;
    ArrayList<League>scheduleArrayList ;
    ArrayList<MySchdule>muScheduleArrayList ;
    SchduleListAdapter schduleListAdapter ;

    MySchduleListAdapter mySchduleListAdapter ;

    String currentTimeText ="";
    Calendar currentTime  ;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.manage_group_layout , null);
        scheduling_recyle_view = (RecyclerView) convertView.findViewById(R.id.scheduling_recyle_view);

        DirectorFragmentManageActivity.showBackButton();
        DirectorFragmentManageActivity.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        group_list_view = (ListView) convertView.findViewById(R.id.group_list_view);
        currentTime = Calendar.getInstance(Locale.ENGLISH);

        add_group_btn = (ImageButton) convertView.findViewById(R.id.add_group_btn);
        add_group_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddGroupFragment.groupEdit = null;
                DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.AddGroupFragment_id);

            }
        });
        muScheduleArrayList = new ArrayList<>();
        add_scheduling_btn = (ImageButton) convertView.findViewById(R.id.add_scheduling_btn);

        add_scheduling_btn.setOnClickListener(onClickListener);

        groupList = new ArrayList<GroupBean>();
        // ,  ,
        group_tab_layout = (LinearLayout) convertView.findViewById(R.id.group_tab);
        scheduling_tab_layout = (LinearLayout) convertView.findViewById(R.id.scheduling_tab);

        group_tab_layout.setOnClickListener(tabClickListener);
        scheduling_tab_layout.setOnClickListener(tabClickListener);




        group_list_layout = (RelativeLayout) convertView.findViewById(R.id.group_list_layout);

        httpRequest = new HttpRequest(getActivity());
        DirectorFragmentManageActivity.updateTitle("Groups/schedules");


        if (tabIndex == 1)
        {
            add_group_btn.setVisibility(View.VISIBLE);
            add_scheduling_btn.setVisibility(View.GONE);

            group_tab_layout.performClick();
        }
        else
        {
            add_group_btn.setVisibility(View.GONE);
            add_scheduling_btn.setVisibility(View.VISIBLE);
            scheduling_tab_layout.performClick();
        }

        sessionManager = new SessionManager();
        scheduleArrayList = new ArrayList<>();

        return convertView;
    }


    private void getGroupList()
    {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("group_club_id",SessionManager.getUser_Club_id(getActivity()));
        params.put("group_owner_user_id",SessionManager.getUser_id(getActivity()));
        httpRequest.getResponse(getActivity(), WebService.get_group_list, params, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {

                groupList = JsonUtility.parseGroupList(jsonObject+"");
                groupAdapter = new GroupAdapter(getActivity(), groupList);
                groupAdapter.setEventItemClickListener(swipeEventItemClickListener);
                group_list_view.setAdapter(groupAdapter);

                Utill.hideProgress();

            }
        });



    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            if (v.getId() == R.id.add_group_btn)
            {

            }

            if (v.getId() == R.id.add_scheduling_btn)
            {
               AddSchdulerFragment addSchdulerFragment = new AddSchdulerFragment();
                addSchdulerFragment.setInstanse(groupList , new AddNewSchduleListner() ,currentTimeText);
               getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_frame , addSchdulerFragment , "addSchdulerFragment").addToBackStack("addSchdulerFragment").commit();

            }




        }
    };


    private void getGroupListItem(String group_id) {
        if (Utill.isNetworkAvailable(getActivity())) {

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("group_id", group_id);
            Utill.showProgress(getActivity());
            GlobalValues.getModelManagerObj(getActivity()).getGroupListItem(new ModelManagerListener() {
                @Override
                public void onSuccess(String json) {
                    Utill.hideProgress();
                    groupList = JsonUtility.parseGroupList(json);
                    AddGroupFragment.groupEdit = groupList.get(0);
                    //DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.AddGroupFragment_id);

                    AddGroupFragment fragment = new AddGroupFragment(AddGroupFragment.groupEdit);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "fragment").addToBackStack(null).commit();


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
    GroupAdapter.SwipeEventItemClickListener swipeEventItemClickListener = new GroupAdapter.SwipeEventItemClickListener() {
        @Override
        public void OnEditClick(int position) {
            getGroupListItem(groupList.get(position).getGroup_id());
        }

        @Override
        public void OnBlockClick(int position, int blockStatus) {

        }
    };
//group_tab_line , scheduling_tab_line , my_scheduling_tab_line
    public View.OnClickListener tabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {


            group_list_layout.setVisibility(View.GONE);

            scheduling_recyle_view.setVisibility(View.GONE);











            if (v.getId() == R.id.group_tab)
            {

                scheduling_tab_layout.setBackgroundColor(getResources().getColor(R.color.unselected_tab));
                group_tab_layout.setBackground(getResources().getDrawable(R.drawable.active_tb_bg));

                add_group_btn.setVisibility(View.VISIBLE);
                add_scheduling_btn.setVisibility(View.GONE);

                group_list_layout.setVisibility(View.VISIBLE);
                tabIndex = 1;
                getGroupList();
                // DirectorFragmentManageActivity.updateTitle("Groups");
            }
            if (v.getId() == R.id.scheduling_tab)
            {

                group_tab_layout.setBackgroundColor(getResources().getColor(R.color.unselected_tab));
                scheduling_tab_layout.setBackground(getResources().getDrawable(R.drawable.active_tb_bg));
                add_group_btn.setVisibility(View.GONE);
                add_scheduling_btn.setVisibility(View.VISIBLE);

                scheduling_recyle_view.setVisibility(View.VISIBLE);

                scheduleArrayList.clear();
                tabIndex = 2;
                if (scheduleArrayList.size() == 0)
                {
                    getSchdlingList();
                }
                else
                {

                }

                //   DirectorFragmentManageActivity.updateTitle("Scheduling");
            }


        }
    };





    public void getSchdlingList()
    {
        HashMap<String,Object>param = new HashMap<>();
        param.put("league_uid" , SessionManager.getUserId(getActivity()));
        param.put("league_club_id" , SessionManager.getUser_Club_id(getActivity()));

        httpRequest.getResponse(getActivity(), WebService.schedule_list, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                if (jsonObject.optBoolean("status"))
                {

                    currentTimeText = jsonObject.optString("time");



                   for (int i = 0 ; i < jsonObject.optJSONArray("Response").length() ;i++)
                   {
                       JSONObject jsonObject1 = jsonObject.optJSONArray("Response").optJSONObject(i);
                       League schedule = new League();
                       schedule.setLeague_uid(jsonObject1.optInt("league_uid"));
                       schedule.setLeague_id(jsonObject1.optInt("league_id"));
                       schedule.setLeague_name(jsonObject1.optString("league_name"));

                       scheduleArrayList.add(schedule);
                   }
                   RecyclerView .LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    scheduling_recyle_view.setLayoutManager(layoutManager);
                    RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(getResources().getDrawable(R.drawable.divider_line));
                    scheduling_recyle_view.addItemDecoration(dividerItemDecoration);
                    ;
                    schduleListAdapter = new SchduleListAdapter(getActivity(), scheduleArrayList, new OnItemClickListener() {
                        @Override
                        public void OnItemClick(int pos)
                        {
                            SchduleDetailFragment schduleDetailFragment = new SchduleDetailFragment();
                            schduleDetailFragment.setInstanse(new AddNewSchduleListner());
                            Bundle bundle = new Bundle();
                            bundle.putString("league_uid", scheduleArrayList.get(pos).getLeague_uid()+"");
                            bundle.putString("league_id", scheduleArrayList.get(pos).getLeague_id()+"");
                            bundle.putString("league_name", scheduleArrayList.get(pos).getLeague_name()+"");
                            //
                            schduleDetailFragment.setArguments(bundle);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame ,schduleDetailFragment ,"schduleDetailFragment").addToBackStack("schduleDetailFragment").commit();
                        }
                    });
                    scheduling_recyle_view.setAdapter(schduleListAdapter);

                }
                else
                {
                    Utill.showDialg(jsonObject.optString("message") , getActivity());
                }

            }
        });

    }

    public interface OnItemClickListener
    {
        public void OnItemClick(int pos);

    }



    public class AddNewSchduleListner
    {
        public void onSuccess()
        {
            scheduleArrayList.clear();
            getSchdlingList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

      //  Toast.makeText(getActivity() , "dbdbb",1).show();
    }
}
