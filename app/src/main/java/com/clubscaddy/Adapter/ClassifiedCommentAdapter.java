package com.clubscaddy.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.Bean.ClassifiedCommentBean;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.DialogBoxButtonListner;
import com.clubscaddy.fragment.ClassifiedFragment;
import com.clubscaddy.utility.CircleBitmapTranslation;
import com.clubscaddy.R;

import java.util.ArrayList;

/**
 * Created by administrator on 21/7/17.
 */

public class ClassifiedCommentAdapter extends RecyclerView.Adapter<ClassifiedCommentAdapter.ViewHolder>
{


    ArrayList<ClassifiedCommentBean> classifiedCommentArrayList;
    Activity activity ;
    ClassifiedFragment fragment;
    boolean isUserAsProductOwner ;
    int itempos;
    public ClassifiedCommentAdapter(ClassifiedFragment fragment , ArrayList<ClassifiedCommentBean> classifiedCommentArrayList ,boolean isUserAsProductOwner , int itempos)
    {
        this.activity =fragment.getActivity() ;
        this.classifiedCommentArrayList =classifiedCommentArrayList ;
        this.fragment =fragment ;
        this.isUserAsProductOwner =isUserAsProductOwner ;
        this.itempos =itempos ;





    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = LayoutInflater.from(activity).inflate(R.layout.classified_cmt_adapter_item , null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ClassifiedCommentAdapter.ViewHolder holder, final int position)
    {
        holder.comment_tv.setText(classifiedCommentArrayList.get(position).getClassifieds_comment_text());
        holder.commenter_name_tv.setText(classifiedCommentArrayList.get(position).getUser_name());

        holder. imageLoaderProgressbar.setVisibility(View.VISIBLE);

        Glide.with(activity)
                .load(classifiedCommentArrayList.get(position).getUser_profilepic())
                .transform(new CircleBitmapTranslation(activity))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder. imageLoaderProgressbar.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder. imageLoaderProgressbar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.commenter_image_view) ;


        holder.comment_date_tv.setText(classifiedCommentArrayList.get(position).getClassifieds_comment_date());

        holder.delete_cmt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ShowUserMessage showUserMessage = new ShowUserMessage(fragment.getActivity());

                showUserMessage.showDialogBoxWithYesNoButton("Are you sure you want to delete this comment?", new DialogBoxButtonListner() {
                    @Override
                    public void onYesButtonClick(DialogInterface dialog) {
                        fragment.deleteComment(classifiedCommentArrayList.get(position).getClassifieds_comment_id() ,""+ classifiedCommentArrayList.get(position).getClassifieds_user_id() , itempos , position);

                    }
                });

            }
        });

        holder.comment_box_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment.commentDialog != null)
                {
                    fragment.commentDialog.cancel();
                }

                DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) fragment.getActivity();
                directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(classifiedCommentArrayList.get(position).getClassifieds_user_id()+"");
            }
        });
        holder.commenter_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment.commentDialog != null)
                {
                    fragment.commentDialog.cancel();
                }

                DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) fragment.getActivity();
                directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(classifiedCommentArrayList.get(position).getClassifieds_user_id()+"");
            }
        });

        if (isUserAsProductOwner)
        {
            holder.delete_cmt_btn.setVisibility(View.VISIBLE);
        }
        else
        {
            if (Integer.parseInt(SessionManager.getUserId(activity)) == classifiedCommentArrayList.get(position).getClassifieds_user_id())
            {
                holder.delete_cmt_btn.setVisibility(View.VISIBLE);

            }
            else
            {
                holder.delete_cmt_btn.setVisibility(View.INVISIBLE);

            }
        }


    }

    @Override
    public int getItemCount()
    {
        return classifiedCommentArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView commenter_name_tv;
        TextView comment_tv;
        ImageView commenter_image_view ;
        ImageView delete_cmt_btn ;
        LinearLayout comment_box_layout;
        TextView comment_date_tv;
        ProgressBar imageLoaderProgressbar ;


        public ViewHolder(View itemView) {
            super(itemView);
            comment_tv = (TextView) itemView.findViewById(R.id.comment_tv);
            commenter_name_tv = (TextView) itemView.findViewById(R.id.commenter_name_tv);
            commenter_image_view = (ImageView) itemView.findViewById(R.id.commenter_image_view);
            delete_cmt_btn = (ImageView) itemView.findViewById(R.id.delete_cmt_btn);
            comment_box_layout = (LinearLayout) itemView.findViewById(R.id.comment_box_layout);
            comment_date_tv = (TextView) itemView.findViewById(R.id.comment_date_tv);

            imageLoaderProgressbar = (ProgressBar) itemView.findViewById(R.id.image_loader_progressbar);



        }
    }


}
