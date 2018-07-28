package com.clubscaddy.Adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.utility.CircleTransform;
import com.clubscaddy.R;
import com.clubscaddy.fragment.AddCoachesFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by administrator on 26/4/17.
 */

public class CoachMemberListDialogAdapter extends BaseAdapter
    {

        Activity activity ;
        ArrayList<MemberListBean> memberListBeanArrayList ;
        AddCoachesFragment addCoachesFragment;
    public CoachMemberListDialogAdapter(Activity activity , ArrayList<MemberListBean> memberListBeanArrayList )
        {
            this.activity = activity ;

            this.memberListBeanArrayList = memberListBeanArrayList ;
        }


        @Override
        public int getCount() {
        return memberListBeanArrayList.size();
    }

        @Override
        public Object getItem(int position) {
        return memberListBeanArrayList.get(position);
    }

        @Override
        public long getItemId(int position) {
        return position;
    }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            final   ViewHolder viewHolder ;

            if (convertView == null)
            {
                convertView = LayoutInflater.from(activity).inflate(R.layout.manage_coach_adapter_layout , null);
                viewHolder = new ViewHolder();

                viewHolder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);

                viewHolder.imageLoaderProgressbar = (ProgressBar) convertView.findViewById(R.id.image_loader_progressbar);



                viewHolder.cancel_btn = (ImageButton) convertView.findViewById(R.id.cancel_btn);
                //mHolder.admin_list_relativeLayout = (RelativeLayout) view.findViewById(R.id.admin_list_relativeLayout);

                //mHolder.button2.setBackground(activity.getResources().getDrawable(R.drawable.delete_icon));
                viewHolder.profileImage = (ImageView) convertView.findViewById(R.id.image);

                //mHolder.button2.setBackground(activity.getResources().getDrawable(R.drawable.delete_icon));
                convertView.setTag(viewHolder);

            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final MemberListBean memberListBean = memberListBeanArrayList.get(position);

            viewHolder.cancel_btn.setVisibility(View.GONE);


            viewHolder.name_tv.setText(memberListBean.getUser_first_name()+" "+memberListBean.getUser_last_name());
            Drawable defaultImage = activity.getResources().getDrawable(R.drawable.default_img_profile);
            try
            {
                viewHolder.imageLoaderProgressbar.setVisibility(View.VISIBLE);
                Picasso.with(activity).load(memberListBean.getUser_profilepic()).transform(new CircleTransform()).error(defaultImage).placeholder(defaultImage)
                        .into(viewHolder.profileImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        viewHolder.imageLoaderProgressbar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onError() {
                        viewHolder.imageLoaderProgressbar.setVisibility(View.GONE);

                    }
                });

            }
            catch (Exception e)
            {
                viewHolder.imageLoaderProgressbar.setVisibility(View.GONE);

                viewHolder.profileImage.setImageDrawable(defaultImage);

            }



            return convertView;
        }
        public class ViewHolder {


            TextView name_tv;
            ImageView profileImage;
            ImageButton cancel_btn;

            ProgressBar imageLoaderProgressbar  ;


//image_loader_progressbar
        }
    }


