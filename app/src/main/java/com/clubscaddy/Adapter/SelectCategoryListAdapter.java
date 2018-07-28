package com.clubscaddy.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.clubscaddy.Bean.RecursiveBookingCourtColour;
import com.clubscaddy.R;

import java.util.ArrayList;

/**
 * Created by administrator on 5/12/16.
 */

public class SelectCategoryListAdapter extends BaseAdapter implements SpinnerAdapter
{

  Activity activity ;
    ArrayList<RecursiveBookingCourtColour> recursiveBookingCourtColourArrayList ;

    public SelectCategoryListAdapter(Activity activity ,ArrayList<RecursiveBookingCourtColour> recursiveBookingCourtColourArrayList)
    {
     this.activity = activity ;
        this.recursiveBookingCourtColourArrayList = recursiveBookingCourtColourArrayList ;

        RecursiveBookingCourtColour recursiveBookingCourtColour = new RecursiveBookingCourtColour();
        recursiveBookingCourtColour.setCat_color("#00000000");
        recursiveBookingCourtColour.setCat_name("Select category");
        recursiveBookingCourtColourArrayList.add(0,recursiveBookingCourtColour);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.selece_colour_catogery_layout ,null);
            viewHolder.clourbox = (ImageView) convertView.findViewById(R.id.clourbox);
            viewHolder.devider_line = (LinearLayout) convertView.findViewById(R.id.devider_line);
viewHolder.topPanel = (RelativeLayout) convertView.findViewById(R.id.topPanel);

            viewHolder.colour_name_tv = (TextView) convertView.findViewById(R.id.colour_name_tv);
            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.devider_line.setVisibility(
                View.VISIBLE
        );
        convertView.setBackgroundColor(activity.getResources().getColor(R.color.white_color));

        if (position == 0)
        {
            viewHolder.topPanel.setVisibility(View.GONE);
        }
        else
        {
            viewHolder.topPanel.setVisibility(View.VISIBLE);

            viewHolder.colour_name_tv.setPadding(5,10,5,10);
            viewHolder.colour_name_tv.setText(recursiveBookingCourtColourArrayList.get(position).getCat_name());

        }


        GradientDrawable bgShape = (GradientDrawable)viewHolder.clourbox.getBackground();
        bgShape.setColor(Color.parseColor(recursiveBookingCourtColourArrayList.get(position).getCat_color()));





        return convertView;
    }

    @Override
    public int getCount() {
        return recursiveBookingCourtColourArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return recursiveBookingCourtColourArrayList.get(position);
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.selece_colour_catogery_layout ,null);
            viewHolder.clourbox = (ImageView) convertView.findViewById(R.id.clourbox);
            viewHolder.devider_line = (LinearLayout) convertView.findViewById(R.id.devider_line);


            viewHolder.colour_name_tv = (TextView) convertView.findViewById(R.id.colour_name_tv);
            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.devider_line.setVisibility(
                View.GONE
        );

        GradientDrawable bgShape = (GradientDrawable)viewHolder.clourbox.getBackground();
        bgShape.setColor(Color.parseColor(recursiveBookingCourtColourArrayList.get(position).getCat_color()));
        viewHolder.colour_name_tv.setText(recursiveBookingCourtColourArrayList.get(position).getCat_name());
        return convertView;
    }


    public class ViewHolder
    {
        TextView colour_name_tv ;
        ImageView clourbox;
        LinearLayout devider_line;
        RelativeLayout topPanel;
    }


}
