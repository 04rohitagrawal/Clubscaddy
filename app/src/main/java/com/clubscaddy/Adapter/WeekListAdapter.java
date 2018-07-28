package com.clubscaddy.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.clubscaddy.Bean.Week;
import com.clubscaddy.R;

import java.util.ArrayList;

/**
 * Created by administrator on 6/12/17.
 */

public class WeekListAdapter extends RecyclerView.Adapter<WeekListAdapter.WeekViewHolder>
{




    Activity activity ;
    ArrayList<Week> weekArrayList ;


    public WeekListAdapter(Activity activity , ArrayList<Week> weekArrayList)
    {
        this.activity = activity ;
        this.weekArrayList = weekArrayList ;




    }



    @Override
    public WeekViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
      View  convertView = LayoutInflater.from(activity).inflate(R.layout.weak_adapter_layout , null);


        return new WeekViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final WeekViewHolder holder,final int position)
    {

        holder.weekNameTv.setText(weekArrayList.get(position).getWeekName());
        holder.weekSelectedCheckBox.setText("");


        if ( weekArrayList.get(position).isWeekSelected())
        {
            holder.weekNameTv.setTextColor(activity.getResources().getColor(R.color.black_color));
            holder.weekSelectedCheckBox.setChecked(true);
        }
        else
        {
            holder.weekNameTv.setTextColor(activity.getResources().getColor(R.color.gray_color));
            holder.weekSelectedCheckBox.setChecked(false);

        }


        holder.weekSelectedCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                weekArrayList.get(position).setWeekSelected( holder.weekSelectedCheckBox.isChecked());

                ;
                notifyDataSetChanged();


            }
        });



        holder.weekNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.weekSelectedCheckBox.performClick();
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return weekArrayList.size();
    }
   /*
    @Override
    public int getCount() {
        return weekArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return weekArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
       final ViewHolder viewHolder ;

        if (convertView == null)
        {

            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.weekSelectedCheckBox.setVisibility(View.GONE);

        viewHolder.weekNameTv.setTextSize(12);

        *//* if (position == 0)
         {
             viewHolder.weekNameTv.setVisibility(View.GONE);
         }
         else
         {
             viewHolder.weekNameTv.setVisibility(View.VISIBLE);

         }*//*

        viewHolder.weekNameTv.setPadding(0,10,0,10);


        viewHolder.weekNameTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_arraow, 0);
        viewHolder.weekNameTv.setText(weekArrayList.get(position).getWeekName());




        return convertView;
    }

    @Override
    public View getDropDownView(final int position, View convertView, ViewGroup parent)
    {
        final ViewHolder viewHolder ;

        if (convertView == null)
        {

            convertView = LayoutInflater.from(activity).inflate(R.layout.weak_adapter_layout , null);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == 0)
        {
            viewHolder.weekSelectedCheckBox.setVisibility(View.GONE);
            viewHolder.weekNameTv.setVisibility(View.GONE);
            viewHolder.weekNameTv.setPadding(0,0,0,0);

        }
        else
        {
            viewHolder.weekSelectedCheckBox.setVisibility(View.VISIBLE);
            viewHolder.weekNameTv.setVisibility(View.VISIBLE);
            viewHolder.weekNameTv.setPadding(0,5,0,5);

        }


      *//*  viewHolder.weekNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.weekSelectedCheckBox.setChecked(!viewHolder.weekSelectedCheckBox.isChecked());

            }
        });*//*


        return convertView;

    }


   */


    public class WeekViewHolder extends RecyclerView.ViewHolder
    {
        CheckBox weekSelectedCheckBox;
        TextView weekNameTv;
        public  WeekViewHolder(View convertView)
        {
            super(convertView);
            weekSelectedCheckBox = (CheckBox) convertView.findViewById(R.id.week_selected_check_box);

            weekNameTv = (TextView) convertView.findViewById(R.id.week_name_tv);

        }

    }
}
