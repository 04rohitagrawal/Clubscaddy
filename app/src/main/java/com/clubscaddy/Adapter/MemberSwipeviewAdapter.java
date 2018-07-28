package com.clubscaddy.Adapter;

import java.util.ArrayList;

import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.CircleTransform;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.fragment.MembersDirectoryFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MemberSwipeviewAdapter extends BaseAdapter {
	LayoutInflater inflator;
	Context mContext;
	ListView listView;
	MemberSwipeviewAdapter adapter;
	ArrayList<MemberListBean> list;
	SwipeEventItemClickListener eventItemClickListener;

	MembersDirectoryFragment activity;
	public MemberSwipeviewAdapter(Context context, ArrayList<MemberListBean> l,MembersDirectoryFragment activity) 
	{
		mContext = context;
		inflator = LayoutInflater.from(mContext);
		adapter = this;
		this.activity = activity;
		list = l;
	/*	ArrayList<MemberListBean> result = new ArrayList<MemberListBean>();;
		for(int i = 0 ;i<list.size();i++){
			String name = list.get(i).getUser_first_name()+" "+list.get(i).getUser_last_name();
			if(!name.toLowerCase().equalsIgnoreCase(SessionManager.getFirstName(mContext)+" "+SessionManager.getLastName(mContext)))
			{
				result.add(list.get(i));
			}
			else
			{
				
			}
			
			
		}
		list.clear();
		list.addAll(result);*/
	}

	public void setEventItemClickListener(SwipeEventItemClickListener evtclck) {
		this.eventItemClickListener = evtclck;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder mHolder;


		if (convertView == null)
		{
			convertView = inflator.inflate(R.layout.member_item_adapter, parent, false);
			
			mHolder = new ViewHolder();
			//mHolder.admin_list_email = (TextView) view.findViewById(R.id.admin_list_email);
			mHolder.admin_list_name = (TextView) convertView.findViewById(R.id.admin_list_name);
			//mHolder.admin_list_phone = (TextView) view.findViewById(R.id.admin_list_phone);
			mHolder.admin_list_nextArrow = (ImageView) convertView.findViewById(R.id.admin_list_nextArrow);
			mHolder.admin_list_relativeLayout = (RelativeLayout) convertView.findViewById(R.id.admin_list_relativeLayout);
			mHolder.user_select_checkbox = (CheckBox) convertView.findViewById(R.id.user_select_checkbox);
			mHolder.profileImage = (ImageView) convertView.findViewById(R.id.image);
			mHolder.prog = (ProgressBar) convertView.findViewById(R.id.prog);

			mHolder.right_icon = (ImageButton) convertView.findViewById(R.id.right_icon);



			convertView.setTag(mHolder);
		} 
		else 
		{
			mHolder = (ViewHolder) convertView.getTag();
		}
		final  MemberListBean bean = list.get(position);
		  mHolder.admin_list_relativeLayout.setOnClickListener(new OnClickListener()
		  {
				
				@Override
				public void onClick(View v) 
				{

              DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) activity.getActivity();
					directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(list.get(position).getUser_id());


								}
			});



		mHolder.profileImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) activity.getActivity();
				directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(list.get(position).getUser_id());

			}
		});


		if(bean.getUser_email().equalsIgnoreCase(SessionManager.getUser_email(mContext)) )
		{
			list.get(position).setMemberSelection(false);
			mHolder.user_select_checkbox.setEnabled(false);
			mHolder.user_select_checkbox.setVisibility(View.INVISIBLE);

			mHolder.right_icon.setVisibility(View.VISIBLE);


		}
		else
		{
			mHolder.user_select_checkbox.setVisibility(View.VISIBLE);

			if(Integer.parseInt(bean.getUser_status()) == 2)
			{
				list.get(position).setMemberSelection(false);
				mHolder.user_select_checkbox.setVisibility(View.INVISIBLE);

				mHolder.right_icon.setVisibility(View.VISIBLE);

				mHolder.right_icon.setVisibility(View.VISIBLE);


			}
			else
			{
				mHolder.right_icon.setVisibility(View.VISIBLE);
				mHolder.user_select_checkbox.setEnabled(true);

				mHolder.user_select_checkbox.setVisibility(View.VISIBLE);

				mHolder.right_icon.setVisibility(View.VISIBLE);


			}



		}







		mHolder.user_select_checkbox.setChecked(list.get(position).getMemberSelection());
		 mHolder.user_select_checkbox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(mContext,mHolder.user_select_checkbox.isChecked()+ " check "+list.get(position).getMemberSelection(), 1).show();
					
			boolean isChecked =  !list.get(position).getMemberSelection();	
				 list.get(position).setMemberSelection(isChecked);
						
				if(isChecked)
				{
			    activity.numofselecteditem++;
				}
				else
				{
					activity.numofselecteditem--;
				}
				try
				{
				if(activity.numofselecteditem == 0)	
				{
					activity.send_ralative_id.setVisibility(View.INVISIBLE);
					activity.create_ralative_id.setVisibility(View.INVISIBLE);	
				}
				else
				{
					if(SessionManager.getUser_type(activity.getActivity()).equals(AppConstants.USER_TYPE_MEMBER) == false)
					activity.send_ralative_id.setVisibility(View.VISIBLE);

					activity.create_ralative_id.setVisibility(View.VISIBLE);	
				}
				activity.countr.setText(String.valueOf(activity.numofselecteditem));
				activity.crete_counter.setText(String.valueOf(activity.numofselecteditem));
				}
				catch(Exception e)
				{
					
				}	
				
			}
		});
	
		String status = bean.getUser_status();
		

		if (bean.getUser_status().equals("1")) {
			mHolder.admin_list_name.setText(bean.getUser_first_name() + " " + bean.getUser_last_name());
			/*mHolder.admin_list_email.setText(bean.getUser_email());
			mHolder.admin_list_phone.setText(bean.getUser_phone());*/
		} else {
			mHolder.admin_list_name.setText(bean.getUser_first_name() + " " + bean.getUser_last_name());
			/*mHolder.admin_list_email.setText(bean.getUser_email());
			mHolder.admin_list_phone.setText(bean.getUser_phone());*/
		}

		mHolder.prog.setVisibility(View.VISIBLE);
		try
		{
			Picasso.with(mContext)
					.load(bean.getUser_profilepic())
					.placeholder(mContext. getResources().getDrawable( R.drawable.default_img_profile )) // optional
					.error(R.drawable.default_img_profile)         // optional
					.transform(new CircleTransform()) .into(mHolder.profileImage, new Callback() {
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
			mHolder.prog.setVisibility(View.GONE);
		}


		
/*		Drawable d = mContext.getResources().getDrawable(R.drawable.logo_profile);
		image_load.displayImage(bean.getUser_profilepic(),mHolder.profileImage, opt, new CustomImageLoader(mHolder.prog, mContext,d,true));
	*/	return convertView;
	}

	public class ViewHolder {
		ImageView admin_list_nextArrow;
		/*TextView admin_list_email;
		TextView admin_list_phone;*/
		TextView admin_list_name;
		RelativeLayout admin_list_relativeLayout;
		
		public ImageView profileImage;
		public ProgressBar prog;
		CheckBox user_select_checkbox;
		ImageButton right_icon;
		// public TextView editEvent;

	}

	public interface SwipeEventItemClickListener {
		// public void onClickProfilePic(String user_id);

		public void OnEditClick(int position);

		public void OnBlockClick(int position, int blockStatus);

	}



}

