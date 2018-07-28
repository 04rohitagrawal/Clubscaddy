package com.clubscaddy.Adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.Bean.LeagueUser;
import com.clubscaddy.R;
import com.clubscaddy.utility.CircleBitmapTranslation;

import java.util.ArrayList;

/**
 * Created by administrator on 23/6/17.
 */

public class UserStatusListAdapter extends BaseAdapter
{

    Activity activity ;
    ArrayList<LeagueUser>leagueUserArrayList ;

    ArrayList<String>userStatusList = new ArrayList<>();
    String leagueUserId;

    public UserStatusListAdapter(Activity activity ,  ArrayList<LeagueUser>leagueUserArrayList , String leagueUserId )
    {
        this.activity = activity ;
        this.leagueUserArrayList = leagueUserArrayList ;
        userStatusList.add("Not Responded");
        userStatusList.add("Yes");
        userStatusList.add("No");
        userStatusList.add("Maybe");
        userStatusList.add("Call last");
        this.leagueUserId = leagueUserId ;
    }

    @Override
    public int getCount() {
        return leagueUserArrayList.size();
    }

    @Override
    public LeagueUser getItem(int position) {
        return leagueUserArrayList.get(position);
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.user_status_list_adapter , null);
            viewHolder = new ViewHolder();
            viewHolder.user_image_view = (ImageView) convertView.findViewById(R.id.user_image_view);
            viewHolder.user_name_tv = (TextView) convertView.findViewById(R.id.user_name_tv);
            viewHolder.user_status_tv = (TextView) convertView.findViewById(R.id.user_status_tv);
            viewHolder.imageLoaderProgressBar = (ProgressBar) convertView.findViewById(R.id.image_loader_progressbar);

            convertView.setTag(viewHolder);

        }
        else
        {
         viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == 0)
        {
            convertView.setBackgroundColor(activity.getResources().getColor(R.color.gray_color));
            viewHolder.user_image_view.setVisibility(View.INVISIBLE);
            viewHolder.user_name_tv.setTextColor(activity.getResources().getColor(R.color.black_color));
            viewHolder.user_status_tv.setTextColor(activity.getResources().getColor(R.color.black_color));
            viewHolder.user_name_tv.setTypeface(null , Typeface.BOLD);
            viewHolder.user_status_tv.setTypeface(null , Typeface.BOLD);
            viewHolder.user_name_tv.setTextSize(15);
            viewHolder.user_status_tv.setTextSize(15);

        }
        else
        {
            convertView.setBackgroundColor(activity.getResources().getColor(R.color.white_color));
            viewHolder.user_image_view.setVisibility(View.VISIBLE);



            viewHolder.user_status_tv.setTextColor(activity.getResources().getColor(R.color.blue_header));
            viewHolder.user_name_tv.setTypeface(null , Typeface.NORMAL);
            viewHolder.user_status_tv.setTypeface(null , Typeface.NORMAL);


            viewHolder.user_name_tv.setTextSize(15);
            viewHolder.user_status_tv.setTextSize(15);
        }

        if (position  == 0)
        {
            viewHolder.user_name_tv.setText("Captain");
            viewHolder.user_status_tv.setText((leagueUserArrayList.get(position).getLeague_user_name()));

        }
        else
        {
            try
            {
                viewHolder.user_name_tv.setText(leagueUserArrayList.get(position).getLeague_user_name());
                viewHolder.user_status_tv.setText("");
            }
            catch (Exception e)
            {

            }


        }



        try
        {
            viewHolder.imageLoaderProgressBar.setVisibility(View.VISIBLE);

            Glide.with(activity)
                    .load(leagueUserArrayList.get(position).getLeague_user_profile())
                    .placeholder(R.drawable.default_img_profile)
                    .transform(new CircleBitmapTranslation(activity))
                    .error(R.drawable.default_img_profile)
                             .listener(new RequestListener<String, GlideDrawable>() {
                                 @Override
                                 public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                     viewHolder.imageLoaderProgressBar.setVisibility(View.GONE);

                                     return false;
                                 }

                                 @Override
                                 public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                     viewHolder.imageLoaderProgressBar.setVisibility(View.GONE);
                                     return false;
                                 }
                             })
                    .into(viewHolder.user_image_view );

        }
        catch (Exception e)
        {
            viewHolder.imageLoaderProgressBar.setVisibility(View.GONE);

            viewHolder.user_image_view.setImageDrawable(activity.getResources().getDrawable(R.drawable.default_img_profile));
        }


        return convertView;
    }
    public class ViewHolder
    {
        ImageView user_image_view;
        TextView user_name_tv;
        TextView user_status_tv;
        ProgressBar imageLoaderProgressBar;
    }
}
