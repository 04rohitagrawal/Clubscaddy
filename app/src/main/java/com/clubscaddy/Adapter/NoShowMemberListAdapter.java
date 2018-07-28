package com.clubscaddy.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.utility.CircleBitmapTranslation;

import java.util.ArrayList;

/**
 * Created by administrator on 14/4/17.
 */

public class NoShowMemberListAdapter extends BaseAdapter
{

    DirectorFragmentManageActivity activity ;
    ArrayList<MemberListBean>noShowMemberList ;
    public NoShowMemberListAdapter(DirectorFragmentManageActivity activity , ArrayList<MemberListBean>noShowMemberList)
    {
    this.activity = activity ;
        this.noShowMemberList = noShowMemberList ;
    }



    @Override
    public int getCount() {
        return noShowMemberList.size();
    }

    @Override
    public Object getItem(int position) {
        return noShowMemberList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
     final   ViewHolder viewHolder ;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.no_show_report_member_item , null);
            viewHolder.user_name_tv = (TextView) convertView.findViewById(R.id.user_name_tv);
           // viewHolder.user_phone_tv = (TextView) convertView.findViewById(R.id.user_phone_tv);
           // viewHolder.user_email_tv = (TextView) convertView.findViewById(R.id.user_email_tv);
            viewHolder.no_show_user_counter_tv = (TextView) convertView.findViewById(R.id.no_show_user_counter_tv);
            viewHolder.user_profile_iv = (ImageView) convertView.findViewById(R.id.user_profile_iv);
            viewHolder.imageLoaderProgressBar = (ProgressBar) convertView.findViewById(R.id.image_loader_progressbar);
            viewHolder.admin_list_relativeLayout = (LinearLayout) convertView.findViewById(R.id.admin_list_relativeLayout);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.user_name_tv.setText(noShowMemberList.get(position).getUser_first_name()+" "+noShowMemberList.get(position).getUser_last_name());
        //viewHolder.user_email_tv.setText(noShowMemberList.get(position).getUser_email());
        //viewHolder.user_phone_tv.setText(noShowMemberList.get(position).getUser_phone());
        viewHolder.no_show_user_counter_tv.setText(noShowMemberList.get(position).getUser_no_show());

        try
        {
            Glide.with(activity).load(noShowMemberList.get(position).getUser_profilepic())
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
                    .placeholder(R.drawable.default_img_profile).into(viewHolder.user_profile_iv);
        }
        catch (Exception e)
        {
            viewHolder.imageLoaderProgressBar.setVisibility(View.GONE);

            viewHolder.user_profile_iv.setImageResource(R.drawable.default_img_profile);
        }

        viewHolder.admin_list_relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.SwitcFragmentToUserInfoActivity(noShowMemberList.get(position).getUser_id());
            }
        });


        viewHolder.user_profile_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.SwitcFragmentToUserInfoActivity(noShowMemberList.get(position).getUser_id());
            }
        });
        return convertView;
    }

    public class ViewHolder
    {
        TextView user_name_tv;
       // TextView user_phone_tv ;
        //TextView user_email_tv;
        ImageView user_profile_iv;
        TextView no_show_user_counter_tv;
        ProgressBar imageLoaderProgressBar ;
        LinearLayout admin_list_relativeLayout;
    }
}
