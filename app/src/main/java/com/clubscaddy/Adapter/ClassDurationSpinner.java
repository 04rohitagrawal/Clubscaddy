package com.clubscaddy.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.clubscaddy.R;
import com.clubscaddy.utility.DeviceInfo;

import java.util.ArrayList;

/**
 * Created by administrator on 5/12/17.
 */

public class ClassDurationSpinner extends BaseAdapter  implements SpinnerAdapter
{

    Activity activity ;
   public   ArrayList<String> durationItem ;

    public ClassDurationSpinner(final Activity activity ,final Spinner spinner)
    {
     this.activity = activity ;
        this.durationItem = new ArrayList<>() ;

        durationItem.add("Select duration");
        durationItem.add("2 Week");
        durationItem.add("3 Week");
        durationItem.add("1 Month");
        durationItem.add("2 Month");
        durationItem.add("3 Month");
        durationItem.add("4 Month");
        durationItem.add("6 Month");
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
        return durationItem.size();
    }

    @Override
    public Object getItem(int position)
    {
        return durationItem.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        TextView txt = new TextView(activity);

        if (position == 0)
        {
            txt.setVisibility(View.GONE);
            txt.setPadding(0, 0, 0, 0);
            txt.setTextSize(0);
        }
        else
        {
            txt.setVisibility(View.VISIBLE);
            txt.setPadding(8, 8, 8, 8);
            txt.setTextSize(13);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(durationItem.get(position));
            txt.setTextColor(Color.parseColor("#000000"));
            txt.setSingleLine(true);

        }

        return  txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(activity);
        txt.setGravity(Gravity.CENTER);
        txt.setPadding(8, 16, 8, 16);
        txt.setTextSize(12);
        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_arraow, 0);
        txt.setText(durationItem.get(i));
        txt.setTextColor(Color.parseColor("#000000"));
        txt.setSingleLine(true);

        return  txt;
    }
}
