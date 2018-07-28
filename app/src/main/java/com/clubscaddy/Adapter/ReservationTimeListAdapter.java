package com.clubscaddy.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.clubscaddy.R;

import java.util.ArrayList;

/**
 * Created by administrator on 31/7/17.
 */

public class ReservationTimeListAdapter extends BaseAdapter implements SpinnerAdapter
{

    Activity activity ;
    String timeSloatList[]  ;
    boolean deveiderlineShowStatus;
 public ReservationTimeListAdapter(Activity activity ,String timeSloatList[] , boolean deveiderlineShowStatus  )
 {
     this.activity = activity ;
     this.timeSloatList = timeSloatList ;
     this.deveiderlineShowStatus = deveiderlineShowStatus ;

 }



    @Override
    public int getCount() {
        return timeSloatList.length;
    }

    @Override
    public Object getItem(int position) {
        return timeSloatList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
         ViewHolder viewHolder ;


        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.recursive_time_slot_layout , null);
            viewHolder.devider_line = (LinearLayout) convertView.findViewById(R.id.devider_line);
            viewHolder.time_solt_tv = (TextView) convertView.findViewById(R.id.time_solt_tv);
           convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.devider_line.setVisibility(View.GONE);

        viewHolder.time_solt_tv.setText(timeSloatList[position]);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.recursive_time_slot_layout , null);
            viewHolder.devider_line = (LinearLayout) convertView.findViewById(R.id.devider_line);
            viewHolder.time_solt_tv = (TextView) convertView.findViewById(R.id.time_solt_tv);
            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (deveiderlineShowStatus == true)
        {
            viewHolder.devider_line.setVisibility(View.VISIBLE);

        }
        else
        {
            viewHolder.devider_line.setVisibility(View.INVISIBLE);

        }

        if (position == 0)
        {
            viewHolder.time_solt_tv.setVisibility(View.GONE);

        }
        else
        {
            viewHolder.time_solt_tv.setVisibility(View.VISIBLE);
            viewHolder.time_solt_tv.setPadding(10 , 20 , 10 ,20);
            viewHolder.time_solt_tv.setText(timeSloatList[position]);
        }



        return convertView;
    }



    public class ViewHolder
    {
        LinearLayout devider_line;
        TextView time_solt_tv;

    }

}
