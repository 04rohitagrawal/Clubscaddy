package com.clubscaddy.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.clubscaddy.Bean.UserClub;
import com.clubscaddy.utility.CircleTransform;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by administrator on 30/11/16.
 */

public class ClubListAdapter extends BaseAdapter
{
    ArrayList<UserClub>userClubArrayList ;
    Activity  activity ;

    public ClubListAdapter( Activity  activity , ArrayList<UserClub>userClubArrayList)
    {
      this.activity = activity ;
        this.userClubArrayList = userClubArrayList ;
    }

    @Override
    public int getCount() {
        return userClubArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return userClubArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
       final ClubListAdapter.ViewHolder viewHolder;
        if(convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.club_list_adapter_layout ,null);
            viewHolder.club_name_tv = (TextView) convertView.findViewById(R.id.club_name_tv);
            viewHolder.club_status_msg_tv = (TextView) convertView.findViewById(R.id.club_status_msg_tv);
            viewHolder.user_logo_iv = (ImageView) convertView.findViewById(R.id.user_logo_iv);
            viewHolder.loader_progrss_bar = (ProgressBar) convertView.findViewById(R.id.loader_progrss_bar);

            //
            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.club_name_tv.setText(userClubArrayList.get(position).getClub_name()+"");
        viewHolder.club_status_msg_tv.setText(userClubArrayList.get(position).getSport_name()+"");


       try
       {
           viewHolder.loader_progrss_bar.setVisibility(View.VISIBLE);
           Picasso.with(activity)
                   .load(userClubArrayList.get(position).getClub_logo())
                   .placeholder(R.drawable.default_club_profile)
                   .error(R.drawable.default_club_profile)
                   .transform(new CircleTransform())
                   .into(viewHolder.user_logo_iv, new Callback() {
                       @Override
                       public void onSuccess() {

                           viewHolder.loader_progrss_bar.setVisibility(View.GONE);
                       }

                       @Override
                       public void onError() {
                           viewHolder.loader_progrss_bar.setVisibility(View.GONE);
                       }
                   });
       }
       catch (Exception e)
       {
           viewHolder.loader_progrss_bar.setVisibility(View.GONE);

           viewHolder.user_logo_iv.setImageDrawable(activity.getResources().getDrawable(R.drawable.default_club_profile));
       }



        return convertView;
    }


    public  class ViewHolder
    {
        TextView club_name_tv;
        TextView club_status_msg_tv;
        ImageView user_logo_iv;
        ProgressBar loader_progrss_bar;
    }

}
