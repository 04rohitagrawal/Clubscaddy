package com.clubscaddy.Adapter;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.clubscaddy.Bean.Likes;
import com.clubscaddy.utility.CircleTransform;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.fragment.NewsFeedActivity;
import com.clubscaddy.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by administrator on 23/12/16.
 */

public class NewsListAdapter extends BaseAdapter
{

    ArrayList<Likes>likesArrayList ;
    FragmentActivity activity ;
    NewsFeedActivity newsFeedActivity ;


    public NewsListAdapter(NewsFeedActivity activity , ArrayList<Likes> likesArrayList)
    {
          this.newsFeedActivity =activity ;
        this.activity =activity.getActivity() ;

        this.likesArrayList =likesArrayList ;
    }

    @Override
    public int getCount() {
        return likesArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return likesArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
     final   ViewHolder  viewHolder ;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.like_list_adapter_layout ,null);
            viewHolder.like_user_name = (TextView) convertView.findViewById(R.id.like_user_name);
            viewHolder.like_image_view = (ImageView) convertView.findViewById(R.id.like_image_view);

            viewHolder.image_loader_progressbar = (ProgressBar) convertView.findViewById(R.id.image_loader_progressbar);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.like_user_name.setText(likesArrayList.get(position).getUser_name());

        try
        {
            viewHolder.image_loader_progressbar.setVisibility(View.VISIBLE);

                     Picasso.with(activity)
                    .load(likesArrayList.get(position).getUser_profilepic())
                    .error(R.drawable.default_img_profile)
                    .error(R.drawable.default_img_profile)
                    .transform(new CircleTransform())
                    .into(viewHolder.like_image_view, new Callback() {
                        @Override
                        public void onSuccess() {
                            viewHolder.image_loader_progressbar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {
                            viewHolder.image_loader_progressbar.setVisibility(View.GONE);

                        }
                    });
        }
        catch (Exception e)
        {
            viewHolder.image_loader_progressbar.setVisibility(View.GONE);

        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try
                {
                    newsFeedActivity.reservationSelectionPopup.cancel();
                    DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) activity;
                    directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(likesArrayList.get(position).getNews_feed_like_user_id()+"");

                }
                catch (Exception e)
                {

                }

                   }
        });


        return convertView;
    }

    public  class ViewHolder
    {
        TextView like_user_name;
        ImageView like_image_view;
        ProgressBar image_loader_progressbar;
    }
}
