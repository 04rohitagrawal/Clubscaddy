package com.clubscaddy.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.clubscaddy.Bean.CourtBean;
import com.clubscaddy.R;
import com.clubscaddy.utility.DeviceInfo;

import java.util.ArrayList;

/**
 * Created by administrator on 20/3/17.
 */

public class TimeSloatAdapter extends BaseAdapter {

    ViewHolder Viewholder;
    Activity mContext;
    ArrayList<String> timeSlotList;
    public TimeSloatAdapter(final Activity mContext, ArrayList<String> timeSlotList ,final Spinner spinner) {
        this.mContext = mContext;
        this.timeSlotList = timeSlotList;

        spinner.post(new Runnable(){
            public void run(){
                int height = spinner.getHeight();



                double diff = DeviceInfo.getDensity(mContext) /80.0;


                if (diff >3)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        spinner.setDropDownVerticalOffset(height +5);
                    }

                }
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        spinner.setDropDownVerticalOffset(height - 12);
                    }


                }
                }
        });
    }

    @Override
    public int getCount() {
        return timeSlotList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.time_sloat_adapter_layout, parent, false);
            Viewholder = new ViewHolder();
            Viewholder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            convertView.setTag(Viewholder);

        }else{
            Viewholder = (ViewHolder) convertView.getTag();
        }
        //	Viewholder.courtName.setText(alMemberList.get(position).getCourt_number()+" "+alMemberList.get(position).getCourt_name());
        Viewholder.time_tv.setText(timeSlotList.get(position));
        return convertView;
    }

     public class ViewHolder{
        TextView time_tv;
    }
}

