package com.clubscaddy.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.clubscaddy.R;
import com.clubscaddy.utility.DeviceInfo;

import java.util.ArrayList;

/**
 * Created by administrator on 21/11/16.
 */

public class AppRatingAdapter extends BaseAdapter implements SpinnerAdapter
{

    Activity activity ; ArrayList<String> ratingList ;
    public AppRatingAdapter(final Activity activity , ArrayList<String> ratingList ,final Spinner spinner)
    {
     this.activity = activity ;
        this.ratingList = ratingList ;

        spinner.post(new Runnable(){
            public void run(){
                int height = spinner.getHeight();
                int diff = DeviceInfo.getDensity(activity) /80;
                spinner.setDropDownVerticalOffset(height +5);
            }
        });

    }

    @Override
    public int getCount() {
        return ratingList.size();
    }

    @Override
    public Object getItem(int position) {
        return ratingList.get(0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        ViewHolder viewHolder ;
        if(convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.rating_spinner_item ,null);
            viewHolder.rating_tv = (TextView) convertView.findViewById(R.id.rating_tv);
            viewHolder.action_divider = (LinearLayout) convertView.findViewById(R.id.action_divider);
            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.rating_tv.setPadding(5,10,5,10);
        viewHolder.rating_tv.setText(ratingList.get(position));
        viewHolder.rating_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_arraow, 0);
        viewHolder.action_divider.setVisibility(View.INVISIBLE);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder ;
        if(convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.rating_spinner_item ,null);
            viewHolder.action_divider = (LinearLayout) convertView.findViewById(R.id.action_divider);

            viewHolder.rating_tv = (TextView) convertView.findViewById(R.id.rating_tv);
            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.rating_tv.setPadding(5,20,5,20);
        viewHolder.action_divider.setVisibility(View.VISIBLE);

        viewHolder.rating_tv.setText(ratingList.get(position));

        return convertView;
    }

    public class ViewHolder
    {
        TextView rating_tv;
        LinearLayout action_divider ;
    }


}
