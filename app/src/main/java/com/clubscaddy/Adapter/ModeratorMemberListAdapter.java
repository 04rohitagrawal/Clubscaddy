package com.clubscaddy.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.Interface.FragmentBackResponseListener;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.CircleTransform;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.fragment.AddModeratorFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



/**
 * Created by administrator on 21/12/16.
 */

public class ModeratorMemberListAdapter extends BaseAdapter
{

   FragmentActivity activity ;
    ArrayList<MemberListBean>memberListBeanArrayList ;
    int type ;
    AddModeratorFragment fragment;

    FragmentBackResponseListener fragmentBackResponseListener ;
    public ModeratorMemberListAdapter(FragmentActivity activity , ArrayList<MemberListBean>memberListBeanArrayList , int type , AddModeratorFragment fragment ,FragmentBackResponseListener fragmentBackResponseListener)
    {
    this.activity = activity ;
        this.memberListBeanArrayList = memberListBeanArrayList ;
        this.type = type ;
        this.fragment = fragment ;


        this.fragmentBackResponseListener = fragmentBackResponseListener ;

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
    public View getView(final int position, View convertView, ViewGroup parent)
    {
       final ViewHolder viewHolder ;

        if(convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.event_modetor_list_item ,null);
            viewHolder.user_name_tv = (TextView) convertView.findViewById(R.id.admin_list_name);
            viewHolder.imageLoardProgrssBar = (ProgressBar) convertView.findViewById(R.id.load_new_progrss_bar);
            //viewHolder.user_email_tv = (TextView) convertView.findViewById(R.id.admin_list_email);
            //viewHolder.user_phone_tv = (TextView) convertView.findViewById(R.id.admin_list_phone);
            viewHolder.user_profile_pic_iv = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.cancel_btn = (ImageView) convertView.findViewById(R.id.cancel_btn);

            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.cancel_btn.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.cancel_image));



        if (SessionManager.getUser_type(activity).equals(AppConstants.USER_TYPE_MOBILE_ADMIN) || SessionManager.getUser_type(activity).equals(AppConstants.USER_TYPE_DIRECTOR))
        {
            viewHolder.cancel_btn.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.cancel_btn.setVisibility(View.INVISIBLE);

        }




        viewHolder.user_name_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) fragment.getActivity();
                directorFragmentManageActivity.SwitcFragmentToUserInfoActivityWithAdd(memberListBeanArrayList.get(position).getUser_id() ,fragmentBackResponseListener);
            }
        });

        viewHolder.user_profile_pic_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) fragment.getActivity();
                directorFragmentManageActivity.SwitcFragmentToUserInfoActivityWithAdd(memberListBeanArrayList.get(position).getUser_id(),fragmentBackResponseListener);

            }
        });



        viewHolder.cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {















                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                // Setting Dialog Title
                alertDialog.setTitle(SessionManager.getClubName(activity));

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want delete this moderator?");

                // Setting Icon to Dialog


                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        // Write your code here to invoke YES event
                        fragment.removeModeator(position ,memberListBeanArrayList.get(position).getUser_id() );
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event

                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();




            }
        });


        viewHolder.user_name_tv.setText(memberListBeanArrayList.get(position).getUser_first_name()+" "+memberListBeanArrayList.get(position).getUser_last_name());
        //viewHolder.user_email_tv.setText(memberListBeanArrayList.get(position).getUser_email()+" ");
        //viewHolder.user_phone_tv.setText(memberListBeanArrayList.get(position).getUser_phone()+" ");

        if (type ==1)
        {
           // viewHolder.cancel_btn.setVisibility(View.VISIBLE);
        }
        else
        {
          //  viewHolder.cancel_btn.setVisibility(View.INVISIBLE);
        }
        try
        {
            viewHolder.imageLoardProgrssBar.setVisibility(View.VISIBLE);
            Picasso.with(activity)
                    .load(memberListBeanArrayList.get(position).getUser_profilepic())
                    .transform(new CircleTransform())
                    .error(R.drawable.default_img_profile)
                    .placeholder(R.drawable.default_img_profile)
                    .into(viewHolder.user_profile_pic_iv, new Callback() {
                        @Override
                        public void onSuccess() {
                            viewHolder.imageLoardProgrssBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {
                            viewHolder.imageLoardProgrssBar.setVisibility(View.GONE);

                        }
                    });

        }
        catch (Exception e)
        {
            viewHolder.imageLoardProgrssBar.setVisibility(View.GONE);

        }


        return convertView;
    }

    public class ViewHolder
    {
        TextView user_name_tv ;
        //TextView user_email_tv ;
        //TextView user_phone_tv ;
        ProgressBar imageLoardProgrssBar ;
        ImageView user_profile_pic_iv;
        ImageView cancel_btn ;
    }
}
