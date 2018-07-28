package com.clubscaddy.Adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clubscaddy.Bean.CoachReserve;
import com.clubscaddy.Bean.CoachSlot;
import com.clubscaddy.Bean.CourtBookBean;
import com.clubscaddy.R;

import java.util.ArrayList;

/**
 * Created by administrator on 1/5/17.
 */

public class CoachReservationListAdapter extends BaseAdapter
{

    ArrayList<CoachReserve> coachReservationList ;
    Activity activity;
    public  CoachReservationListAdapter(ArrayList<CoachReserve>CourtBookList , Activity activity) {
        // TODO Auto-generated constructor stub
        this.coachReservationList = CourtBookList;
        this.activity = activity;
    }



    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return coachReservationList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return coachReservationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub




        ViewHolder viewHolder ;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.coach_res_detail_page_list_adapter_layout, null);
            viewHolder. coach_reservation_time = (TextView) convertView.findViewById(R.id.court_time_tv);
            viewHolder.coach_reservation_date = (TextView) convertView.findViewById(R.id.court_name_tv);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }




        String date[] =coachReservationList.get(position).getCoach_reservation_date().split(" ") ;
        viewHolder.coach_reservation_date.setText(coachReservationList.get(position).getCoach_reservation_date());

        viewHolder.coach_reservation_time.setText(coachReservationList.get(position).getCoach_reservation_start_datetime());

        return convertView;
    }


    public class ViewHolder
    {
        TextView coach_reservation_date ;
        TextView coach_reservation_time ;
    }



}

