package com.clubscaddy.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.fragment.AddCoachesFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by administrator on 7/4/17.
 */

public class CoachMemberListAdapter extends BaseAdapter
{

    Activity activity ;
    ArrayList<MemberListBean> memberListBeanArrayList ;
    AddCoachesFragment addCoachesFragment;
    public CoachMemberListAdapter(Activity activity , ArrayList<MemberListBean> memberListBeanArrayList , AddCoachesFragment addCoachesFragment)
    {
    this.activity = activity ;
        this.addCoachesFragment = addCoachesFragment ;
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
        final ViewHolder viewHolder ;

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



        if (SessionManager.getUser_id(activity).equals(memberListBeanArrayList.get(position).getUser_id()))
        {
            viewHolder.cancel_btn.setVisibility(View.INVISIBLE);
        }
        else
        {
            viewHolder.cancel_btn.setVisibility(View.VISIBLE);
        }


final MemberListBean memberListBean = memberListBeanArrayList.get(position);

        viewHolder.cancel_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub




                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                // Setting Dialog Title
                alertDialog.setTitle(SessionManager.getClubName(activity));

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want to remove this coach?");

                // Setting Icon to Dialog


                // Setting Positive "Yes" Button
                alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {


                        addCoachesFragment.removeMemberAsCoach(memberListBean);
                        dialog.cancel();
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,	int which) {
                        // Write your code here to invoke NO event

                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();














            }
        });


        viewHolder.name_tv.setText(memberListBean.getUser_first_name()+" "+memberListBean.getUser_last_name());
        Drawable defaultImage = activity.getResources().getDrawable(R.drawable.default_img_profile);
        try
        {
            viewHolder.imageLoaderProgressbar.setVisibility(View.VISIBLE);

            Picasso.with(activity).load(memberListBean.getUser_profilepic()).transform(new CircleTransform()).error(defaultImage).placeholder(defaultImage).into(viewHolder
                    .profileImage, new Callback() {
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

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try
                {
                    DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) activity;
                    directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(memberListBean.getUser_id()+"");

                }
                catch (Exception e)
                {

                }

            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try
                {
                    DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) activity;
                    directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(memberListBean.getUser_id()+"");

                }
                catch (Exception e)
                {

                }

            }
        });

        return convertView;
    }
    public class ViewHolder {


        TextView name_tv;
        ImageView profileImage;
        ImageButton cancel_btn;
        ProgressBar imageLoaderProgressbar ;

//

    }
}
