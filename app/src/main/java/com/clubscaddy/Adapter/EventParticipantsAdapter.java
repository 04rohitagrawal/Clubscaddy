package com.clubscaddy.Adapter;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.fragment.EventDetailFrageMent;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.clubscaddy.R;
import com.clubscaddy.Bean.EventMemberBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.CircleTransform;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.fragment.EventDetailFrageMent.DeleteMemberListener;

public class EventParticipantsAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<EventMemberBean> commentList;

	DeleteMemberListener onClickDelete;
	boolean iscrossVisivle;
	EventDetailFrageMent eventDetailFrageMent ;
	int eventState ;
	public EventParticipantsAdapter(EventDetailFrageMent eventDetailFrageMent, ArrayList<EventMemberBean> alMemberList, DeleteMemberListener deleteL, boolean iscrossVisivle , int eventState) {

		this.eventDetailFrageMent = eventDetailFrageMent;
		this.mContext = eventDetailFrageMent.getActivity();
		this.commentList = alMemberList;

		this.onClickDelete = deleteL;

		this.eventState = eventState ;

		this.iscrossVisivle = iscrossVisivle;
	}

	@Override
	public int getCount() {
		return commentList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder Viewholder;

		if (convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.participent_member_view, parent, false);
			Viewholder = new ViewHolder();
			Viewholder.participantName = (TextView) convertView.findViewById(R.id.commentor_name);
			Viewholder.participantName.setTextColor(mContext.getResources().getColor(R.color.black_color));
			//commentor_image
			Viewholder.email_tv = (TextView) convertView.findViewById(R.id.email_tv);
			//Viewholder.phone_tv = (TextView) convertView.findViewById(R.id.phone_tv);
			Viewholder.participantImage = (ImageView) convertView.findViewById(R.id.commentor_image);
			Viewholder.deleteMember = (ImageView) convertView.findViewById(R.id.delete_member);
			Viewholder.commentorImageProgressBar = (ProgressBar) convertView.findViewById(R.id.commentor_image_progress_bar);
			convertView.setTag(Viewholder);

		} else {
			Viewholder = (ViewHolder) convertView.getTag();
		}
		
	
		final EventMemberBean bean = commentList.get(position);
		//Viewholder.email_tv.setText(bean.getUser_email());
		//Viewholder.phone_tv.setText(bean.getUser_no());
		Viewholder.participantName.setText(bean.getUser_name());
		Drawable d = mContext.getResources().getDrawable(R.drawable.logo_profile);


		Viewholder.participantName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) eventDetailFrageMent.getActivity();
				directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(bean.getUser_id());

			}
		});


		Viewholder.participantImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) eventDetailFrageMent.getActivity();
				directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(bean.getUser_id());

			}
		});
		try
		{
			Viewholder.commentorImageProgressBar.setVisibility(View.VISIBLE);
			Picasso.with(mContext)
					.load(bean.getUser_profilepic())
					.transform(new CircleTransform())
					.placeholder(mContext. getResources().getDrawable( R.drawable.default_img_profile )) // optional
					.error(R.drawable.default_img_profile)
					.into(Viewholder.participantImage, new Callback() {
						@Override
						public void onSuccess() {
							Viewholder.commentorImageProgressBar.setVisibility(View.GONE);

						}

						@Override
						public void onError() {
							Viewholder.commentorImageProgressBar.setVisibility(View.GONE);

						}
					});

		}
		catch (Exception e)
		{
			Viewholder.commentorImageProgressBar.setVisibility(View.GONE);

			Viewholder.participantImage.setBackgroundResource(R.drawable.default_img_profile);
		}
		

		//image_load.displayImage(bean.getUser_profilepic(),Viewholder.participantImage, opt, new CustomImageLoader(null, mContext,d,false));
		
		if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER)){
			
		//	if(SessionManager.getUser_id(mContext).equalsIgnoreCase(bean.getUser_id()))
			
			Viewholder.deleteMember.setVisibility(View.GONE);	
			}
		
		
		if(iscrossVisivle == false || eventState !=1)
		{


			Viewholder.deleteMember.setVisibility(View.INVISIBLE);
		}
		else
		{
			Viewholder.deleteMember.setVisibility(View.VISIBLE);
		}
		Viewholder.deleteMember.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
				 
		        // Setting Dialog Title
		        alertDialog.setTitle(SessionManager.getClubName(mContext));
		 
		        // Setting Dialog Message
		        alertDialog.setMessage("Are you sure you want to remove this participant from the event? ");
		 
		      
		 
		        // Setting Positive "Yes" Button
		        alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,int which) 
		             {
		            	onClickDelete.onClickDeleteMember(position);
		            	dialog.cancel();
		              }
		        });
		 
		        // Setting Negative "NO" Button
		        alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which)
		            {
		            
		               dialog.cancel();
		            }
		        });
		 
		        // Showing Alert Message
		        alertDialog.show();
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
			}
		});
		
		
		return convertView;
	}

	static public class ViewHolder {
		TextView participantName,email_tv ,phone_tv;
		ImageView participantImage,deleteMember;

		ProgressBar commentorImageProgressBar;

		//commentor_image_progress_bar

	}
}
