package com.clubscaddy.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.Bean.LeagueUser;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.fragment.SchduleDetailFragment;
import com.clubscaddy.utility.CircleBitmapTranslation;

import java.util.ArrayList;

/**
 * Created by administrator on 20/6/17.
 */

public class LeagueUserListAdapter extends RecyclerView.Adapter<LeagueUserListAdapter.ViewHolder>
{

   Activity activity ;
    ArrayList<LeagueUser> leagueUsers ;
    SchduleDetailFragment schduleDetailFragment ;

    public LeagueUserListAdapter(SchduleDetailFragment schduleDetailFragment , ArrayList<LeagueUser> leagueUsers)
    {
    this.activity = schduleDetailFragment.getActivity() ;
        this.leagueUsers = leagueUsers ;
        this.schduleDetailFragment = schduleDetailFragment ;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(activity).inflate(R.layout.league_partivipent_list , null) ;


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {




        if (schduleDetailFragment.leagueUserId.equals(SessionManager.getUser_id(activity)) )
        {
            if (Integer.parseInt(SessionManager.getUserId(activity)) == leagueUsers.get(position).getLeague_user_id())
            {
                 holder.cross_iv.setVisibility(View.INVISIBLE);
            }
            else
            {
                holder.cross_iv.setVisibility(View.VISIBLE);

            }


        }
        else
        {
            holder.cross_iv.setVisibility(View.INVISIBLE);
        }

        holder.user_name_tv.setText(leagueUsers.get(position).getLeague_user_name());

        holder.cross_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                schduleDetailFragment.leagueWithdraw(leagueUsers.get(position).getLeague_user_id()+"" , position);
            }
        });


        holder.user_profile_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) schduleDetailFragment.getActivity();
                directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(""+leagueUsers.get(position).getLeague_user_id());
            }
        });



        holder.user_name_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) schduleDetailFragment.getActivity();
                directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(""+leagueUsers.get(position).getLeague_user_id());
            }
        });

        try
        {

            holder.imageLoaderProgressbar.setVisibility(View.VISIBLE);
                     Glide.with(activity)
                    .load(leagueUsers.get(position)
                    .getLeague_user_profile())
                    .placeholder(R.drawable.default_img_profile)
                    .error(R.drawable.default_img_profile)
                    .transform(new CircleBitmapTranslation(activity))
                             .listener(new RequestListener<String, GlideDrawable>() {
                                 @Override
                                 public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                     holder.imageLoaderProgressbar.setVisibility(View.GONE);

                                     return false;
                                 }

                                 @Override
                                 public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                     holder.imageLoaderProgressbar.setVisibility(View.GONE);
                                     return false;
                                 }
                             })
                    .into(holder.user_profile_iv);

        }
        catch (Exception e)
        {
            holder.imageLoaderProgressbar.setVisibility(View.GONE);

            holder.user_profile_iv.setImageDrawable(activity.getResources().getDrawable(R.drawable.default_img_profile));
        }

    }

    @Override
    public int getItemCount() {
        return leagueUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public  ImageView user_profile_iv;
        public ImageView cross_iv ;
        public TextView user_name_tv;
        public ProgressBar imageLoaderProgressbar ;//image_loader_progressbar

        public ViewHolder(View itemView)
        {
            super(itemView);
            user_profile_iv = (ImageView) itemView.findViewById(R.id.commentor_image);
            cross_iv = (ImageView) itemView.findViewById(R.id.delete_member);
            user_name_tv = (TextView) itemView.findViewById(R.id.commentor_name);
            imageLoaderProgressbar = (ProgressBar) itemView.findViewById(R.id.image_loader_progressbar);
        }
    }

}
