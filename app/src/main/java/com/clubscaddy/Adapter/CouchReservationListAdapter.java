package com.clubscaddy.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clubscaddy.Bean.CoachReserve;
import com.clubscaddy.R;

import java.util.ArrayList;

/**
 * Created by administrator on 29/4/17.
 */

public class CouchReservationListAdapter extends BaseAdapter
{

    ArrayList<CoachReserve>coachReserveArrayList ;
    Activity activity ;
    public CouchReservationListAdapter( Activity activity ,ArrayList<CoachReserve>coachReserveArrayList )
    {
    this.activity = activity ;
        this.coachReserveArrayList = coachReserveArrayList ;
    }

    @Override
    public int getCount() {
        return coachReserveArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return coachReserveArrayList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        ViewHolder viewHolder ;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.coach_res_layout , null);
            viewHolder.coach_name_tv = (TextView) convertView.findViewById(R.id.coach_name_tv);
            viewHolder.coach_reservation_date_tv = (TextView) convertView.findViewById(R.id.coach_reservation_date_tv);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.coach_name_tv.setText(coachReserveArrayList.get(position).getCoach_name());
        viewHolder.coach_reservation_date_tv.setText(coachReserveArrayList.get(position).getCoach_reservation_date());

        return convertView;
    }

    public class ViewHolder
    {
        public TextView coach_name_tv ;
        public  TextView coach_reservation_date_tv ;
    }

}
