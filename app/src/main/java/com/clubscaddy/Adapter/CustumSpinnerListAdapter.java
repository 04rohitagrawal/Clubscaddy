package com.clubscaddy.Adapter;

//CustumSpinnerListAdapter

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.clubscaddy.Bean.Notification;
import com.clubscaddy.utility.DeviceInfo;
import com.clubscaddy.R;

import java.util.ArrayList;

/**
 * Created by administrator on 31/7/17.
 */

public class CustumSpinnerListAdapter extends BaseAdapter implements SpinnerAdapter
{


    ArrayList<String> notificationArrayList ;
    Activity activity ;


    public CustumSpinnerListAdapter(final Activity activity , ArrayList<String>notificationArrayList , final Spinner spinner)
    {
        this.activity = activity ;
        this.notificationArrayList = notificationArrayList ;


        spinner.post(new Runnable(){
            public void run(){
                int height = spinner.getHeight();
                double diff = DeviceInfo.getDensity(activity) /80.0;
                if (diff >3)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    {
                        spinner.setDropDownVerticalOffset(height +5);
                    }

                }
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    {
                        spinner.setDropDownVerticalOffset( height -12);
                    }

                }            }
        });

    }

    public CustumSpinnerListAdapter(final Activity activity ,String arr[] , final Spinner spinner)
    {
        this.activity = activity ;

        this.notificationArrayList = new ArrayList<>() ;

        for (int i = 0 ; i < arr.length ; i++)
        {
            notificationArrayList.add(arr[i]) ;
        }

        spinner.post(new Runnable(){
            public void run(){
                int height = spinner.getHeight();
                int diff = DeviceInfo.getDensity(activity) /80;

                if (diff >3)
                {
                    spinner.setDropDownVerticalOffset(height +5);

                }
                else {
                    spinner.setDropDownVerticalOffset( height -15);

                }            }
        });
    }


    @Override
    public int getCount() {
        return notificationArrayList.size();
    }

    @Override
    public String getItem(int position) {
        return notificationArrayList.get(position);
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.notification_item , null);
            viewHolder.text1 = (TextView) convertView.findViewById(R.id.text1);
            viewHolder.devider_line = (LinearLayout) convertView.findViewById(R.id.devider_line);



            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        convertView.setPadding(10,20,10,20);
        viewHolder.text1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_arraow, 0);




        viewHolder.text1.setText(notificationArrayList.get(position));


        return convertView;
    }
    public class ViewHolder  {
        TextView text1 ;
        LinearLayout devider_line;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.notification_item , null);
            viewHolder.text1 = (TextView) convertView.findViewById(R.id.text1);
            viewHolder.devider_line = (LinearLayout) convertView.findViewById(R.id.devider_line);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.devider_line.setVisibility(View.VISIBLE);

        viewHolder.text1.setPadding(10, 30,10,30);
        viewHolder.text1.setText(notificationArrayList.get(position));


        return convertView;    }
}

