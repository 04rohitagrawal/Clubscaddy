package com.clubscaddy.Adapter;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.Bean.Dropin_memberBean;
import com.clubscaddy.utility.CircleBitmapTranslation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MemberListAdapter extends BaseAdapter
{
	ArrayList<Dropin_memberBean> memeberList;
	FragmentActivity activity;
	public MemberListAdapter(FragmentActivity activity , ArrayList<Dropin_memberBean> memeberList)
	{
	this.memeberList = memeberList;	
	this.activity = activity;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return memeberList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return memeberList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub


		final Dropin_memberBean memberBean = (Dropin_memberBean) memeberList.get(position);

	 final	ViewHolder viewHolder ;

		if (convertView == null)
		{
			viewHolder = new ViewHolder();
			LayoutInflater infalInflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.dropiin_child_item, null);
			viewHolder.txtListChild = (TextView) convertView.findViewById(R.id.name);
			viewHolder.status = (TextView) convertView.findViewById(R.id.status);
			viewHolder.deleteIV = (ImageView) convertView.findViewById(R.id.delete);
			viewHolder.user_image_view = (ImageView) convertView.findViewById(R.id.user_image_view);
			viewHolder.prog = (ProgressBar) convertView.findViewById(R.id.prog);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Drawable d = activity.getResources().getDrawable(R.drawable.default_img_profile);

		String userProfileImageUrl = memberBean.getUser_profile_pic();


		viewHolder.user_image_view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) activity;

				directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(memberBean.getMember_id());
			}
		});

		viewHolder.txtListChild.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) activity;

				directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(memberBean.getMember_id());
			}
		});



		try
		{
			if (Validation.isStringNullOrBlank(userProfileImageUrl) == false)
			{
				viewHolder.prog.setVisibility(View.VISIBLE);

				Glide.with(activity)
						.load(userProfileImageUrl)
						.error(d).placeholder(d)
						.transform(new CircleBitmapTranslation(activity))
						.listener(new RequestListener<String, GlideDrawable>() {
							@Override
							public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
								viewHolder.prog.setVisibility(View.GONE);
								return false;
							}

							@Override
							public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
								viewHolder.prog.setVisibility(View.GONE);								return false;
							}
						})
						.into(viewHolder.user_image_view );
				//image_load.displayImage(userProfileImageUrl,mHolder.profileImage, opt, new CustomImageLoader(mHolder.prog, mContext,d,true));

			}
			else
			{viewHolder.prog.setVisibility(View.GONE);
				viewHolder.user_image_view.setImageDrawable(activity.getResources().getDrawable(R.drawable.default_img_profile));
			}
		}
		catch (Exception e)
		{

		}





		/*deleteIV.setTag(groupPosition+"-"+childPosition);*/

		viewHolder.txtListChild.setText(memberBean.getMember_first_name() + " " + memberBean.getMember_last_name());
		if (memberBean.getParticipants_status().equalsIgnoreCase("1")) {
			viewHolder.status.setText("Invited");
			viewHolder.deleteIV.setVisibility(View.GONE);
		} else if (memberBean.getParticipants_status().equalsIgnoreCase("2")) {
			viewHolder.status.setText("Joined");
			viewHolder.deleteIV.setVisibility(View.GONE);
		} else
			if (memberBean.getParticipants_status().equalsIgnoreCase("4")) 
			{
				viewHolder.status.setText("Accepted");
				viewHolder.deleteIV.setVisibility(View.GONE);
		    }
			else
			{
				if (memberBean.getParticipants_status().equalsIgnoreCase("3")) 
				{
					viewHolder.status.setText("Not accepted");
					viewHolder.deleteIV.setVisibility(View.GONE);
			    }	
				else
				{
					if (memberBean.getParticipants_status().equalsIgnoreCase("5")) 
					{
						viewHolder.status.setText("Not accepted");
						viewHolder.deleteIV.setVisibility(View.GONE);
				    }
					else
					{
						viewHolder.status.setText("Not accepted");
						viewHolder.deleteIV.setVisibility(View.GONE);
					}
				}
			}
		
		return convertView;
	}


	public class ViewHolder
	{
		public ImageView user_image_view;
		public TextView txtListChild ,status;
		ImageView deleteIV;
		ProgressBar prog;
	}




}
