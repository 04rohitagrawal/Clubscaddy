package com.clubscaddy.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.FragmentBackResponseListener;
import com.clubscaddy.Interface.RecycleViewItemClickListner;
import com.clubscaddy.R;
import com.clubscaddy.imageutility.CircleBitmapTransformation;

import java.util.ArrayList;

/**
 * Created by administrator on 14/12/17.
 */

public class ClassExitingMemberListAdapter extends RecyclerView.Adapter<ClassExitingMemberListAdapter.MemberBeanViewHolder>
{

    ArrayList<MemberListBean> memberListBeanArrayList ;
    RecycleViewItemClickListner recycleViewItemClickListner;
    FragmentBackResponseListener fragmentBackResponseListener ;
    FragmentActivity fragmentActivity;
    public ClassExitingMemberListAdapter(FragmentActivity fragmentActivity , ArrayList<MemberListBean> memberListBeanArrayList , RecycleViewItemClickListner recycleViewItemClickListner , FragmentBackResponseListener fragmentBackResponseListener)
    {
        this.fragmentActivity = fragmentActivity ;
        this.recycleViewItemClickListner = recycleViewItemClickListner ;

        this.memberListBeanArrayList = memberListBeanArrayList ;
        this.fragmentBackResponseListener = fragmentBackResponseListener ;

    }


    @Override
    public MemberBeanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(fragmentActivity).inflate(R.layout.class_member_item , null);

        return new MemberBeanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MemberBeanViewHolder holder, final int position) {

        try
        {
            holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recycleViewItemClickListner.onItemClick(position ,0);

                }
            });

            holder.userNameTv.setText(memberListBeanArrayList.get(position).getUser_first_name()+" "+memberListBeanArrayList.get(position).getUser_last_name());
            Glide.with(fragmentActivity).load(memberListBeanArrayList.get(position).getUser_profilepic())
                    .asBitmap()
                    .placeholder(R.drawable.default_img_profile)
                    .error(R.drawable.default_img_profile)
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    }).transform(new CircleBitmapTransformation(fragmentActivity)).into(holder.userImageView);


           holder.userNameTv.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {


                   DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) fragmentActivity;
                   directorFragmentManageActivity.SwitcFragmentToUserInfoActivityWithAdd(memberListBeanArrayList.get(position).getUser_id() ,fragmentBackResponseListener);
               }
           });

            holder.userImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) fragmentActivity;
                    directorFragmentManageActivity.SwitcFragmentToUserInfoActivityWithAdd(memberListBeanArrayList.get(position).getUser_id() ,fragmentBackResponseListener);
                }
            });


        }
        catch (Exception e)
        {

        }

    }

    @Override
    public int getItemCount() {
        return memberListBeanArrayList.size();
    }

    public class MemberBeanViewHolder  extends RecyclerView.ViewHolder
    {

        ImageView userImageView ;
        ImageView cancelBtn ;
        TextView userNameTv ;

        public MemberBeanViewHolder(View itemView) {
            super(itemView);
            userImageView = (ImageView) itemView.findViewById(R.id.user_image_view);

            cancelBtn = (ImageView) itemView.findViewById(R.id.cancel_btn);
            userNameTv = (TextView) itemView.findViewById(R.id.user_name_tv);

        }
    }
}
