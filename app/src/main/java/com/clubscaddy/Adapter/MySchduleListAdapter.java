package com.clubscaddy.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clubscaddy.Bean.MySchdule;

import com.clubscaddy.R;

import java.util.ArrayList;

/**
 * Created by administrator on 23/5/17.
 */

public class MySchduleListAdapter extends RecyclerView.Adapter<MySchduleListAdapter.ViewHolder>
{

    Activity activity ;
    ArrayList<MySchdule> scheduleArrayList ;
    public MySchduleListAdapter(Activity activity , ArrayList<MySchdule>scheduleArrayList )
    {
        this.activity = activity ;
        this.scheduleArrayList = scheduleArrayList ;
    }

    @Override
    public MySchduleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(activity).inflate(R.layout.schdule_list_adapter , null);
        return new MySchduleListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MySchduleListAdapter.ViewHolder viewHolder, int position)
    {
        viewHolder.schdule_title_tv.setText(scheduleArrayList.get(position).getSchedule_title());
        viewHolder.owner_name_tv.setText("Owner Name : "+scheduleArrayList.get(position).getUser_name());
        viewHolder.schdule_date_tv.setText(scheduleArrayList.get(position).getSchedule_date());
        viewHolder.schdule_time_tv.setText(scheduleArrayList.get(position).getSchedule_time());
    }

    @Override
    public int getItemCount() {
        return scheduleArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView schdule_title_tv ;
        TextView owner_name_tv ;;
        TextView schdule_date_tv ;;
        TextView schdule_time_tv ;
        public ViewHolder(View itemView) {
            super(itemView);
            schdule_title_tv = (TextView) itemView.findViewById(R.id.schdule_title_tv);

        }
    }
}