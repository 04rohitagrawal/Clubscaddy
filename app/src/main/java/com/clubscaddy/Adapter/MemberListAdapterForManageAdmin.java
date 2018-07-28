package com.clubscaddy.Adapter;

import java.util.ArrayList;

import com.clubscaddy.utility.CircleTransform;
import com.clubscaddy.DirectorFragmentManageActivity;

import com.clubscaddy.R;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.fragment.ManageADminFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MemberListAdapterForManageAdmin extends BaseAdapter
{

	ArrayList<MemberListBean>mamberList;
	ManageADminFragment activity;

    boolean show_cross = false;
	public MemberListAdapterForManageAdmin(ArrayList<MemberListBean>mamberList , ManageADminFragment activity ,boolean show_cross)
	{
	this.mamberList = mamberList;	
	this.activity = activity;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mamberList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mamberList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder mHolder;


		if (view == null) 
		{
			view = LayoutInflater.from(activity.getActivity()).inflate(R.layout.manage_admin_member_list_item, parent, false);
			

			mHolder = new ViewHolder();
			//mHolder.admin_list_email = (TextView) view.findViewById(R.id.admin_list_email);
			mHolder.admin_list_name = (TextView) view.findViewById(R.id.admin_list_name);
			//mHolder.admin_list_phone = (TextView) view.findViewById(R.id.admin_list_phone);
			mHolder.admin_list_nextArrow = (ImageView) view.findViewById(R.id.admin_list_nextArrow);
			
			
			mHolder.cancel_btn = (ImageButton) view.findViewById(R.id.cancel_btn);

			//mHolder.button2.setBackground(activity.getResources().getDrawable(R.drawable.delete_icon));
			mHolder.profileImage = (ImageView) view.findViewById(R.id.image);
			mHolder.prog = (ProgressBar) view.findViewById(R.id.prog);
			//mHolder.button2.setBackground(activity.getResources().getDrawable(R.drawable.delete_icon));
			view.setTag(mHolder);
		} 
		else 
		{
			mHolder = (ViewHolder) view.getTag();
		}
		final MemberListBean bean = mamberList.get(position);
		
		mHolder.admin_list_name.setText(bean.getUser_first_name() + " " + bean.getUser_last_name());
		//mHolder.admin_list_email.setText(bean.getUser_email());
		//mHolder.admin_list_phone.setText(bean.getUser_phone());
		
		
    if(show_cross)
    {
    	mHolder.cancel_btn.setVisibility(View.VISIBLE);
    }
    
    
    mHolder.cancel_btn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			
			
			
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity.getActivity());

			// Setting Dialog Title
			alertDialog.setTitle(SessionManager.getClubName(activity.getActivity()));

			// Setting Dialog Message
			alertDialog.setMessage("Are you sure you want remove this Admin?");

			// Setting Icon to Dialog
		

			// Setting Positive "Yes" Button
			alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int which) {

			
					activity.cancelAdmin(mamberList.get(position).getUser_id(),mamberList.get(position));
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
		

		Drawable defaultImage = activity.getResources().getDrawable(R.drawable.default_img_profile);



		try
		{
			mHolder.prog.setVisibility(View.VISIBLE);
			Picasso.with(activity.getActivity()).load(bean.getUser_profilepic()).transform(new CircleTransform()).error(defaultImage).placeholder(defaultImage).into(mHolder.profileImage, new Callback() {
				@Override
				public void onSuccess() {
					mHolder.prog.setVisibility(View.GONE);
				}

				@Override
				public void onError() {
					mHolder.prog.setVisibility(View.GONE);
				}
			});

		}
		catch (Exception e)
		{
			mHolder.profileImage.setImageDrawable(defaultImage);
			mHolder.prog.setVisibility(View.GONE);
		}

		mHolder.admin_list_name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) activity.getActivity();
				directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(bean.getUser_id());
			}
		});

		mHolder.profileImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) activity.getActivity();
				directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(bean.getUser_id());
			}
		});


		//image_load.displayImage(bean.getUser_profilepic(),mHolder.profileImage, opt, new CustomImageLoader(mHolder.prog, activity.getActivity(),d,true));


		return view;
	}

	public class ViewHolder {
		ImageView admin_list_nextArrow;
		//TextView admin_list_email;
		//TextView admin_list_phone;
		TextView admin_list_name;
		
		
		public ImageView profileImage;
		ImageButton cancel_btn;
		public ProgressBar prog;
		

	}
	

}
