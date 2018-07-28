package com.clubscaddy.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.Interface.FragmentBackListener;
import com.clubscaddy.Interface.FragmentBackResponseListener;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.utility.CircleBitmapTranslation;

import com.clubscaddy.R;
import com.clubscaddy.Bean.MemberListBean;

public class GroupMemberAdapter extends BaseAdapter {
	LayoutInflater inflator;
	Context mContext;
	GroupMemberAdapter adapter;
	ArrayList<MemberListBean> list;


	private boolean swipe = true;
	
	public void showSwipe(){
		swipe = true;
	}
	public void hideSwipe(){
		swipe = false;
	}
	FragmentActivity fragmentActivity;
     FragmentBackResponseListener fragmentBackResponseListener ;
	public GroupMemberAdapter(FragmentActivity fragmentActivity, ArrayList<MemberListBean> l ,FragmentBackResponseListener fragmentBackResponseListener) {
		mContext = fragmentActivity;
		inflator = LayoutInflater.from(mContext);
		adapter = this;
		this.fragmentBackResponseListener =fragmentBackResponseListener ;
		list = l;
		swipe = false;
		this.fragmentActivity = fragmentActivity ;
	}
	SwipeEventItemClickListener eventItemClickListener ;
	public void setEventItemClickListener(SwipeEventItemClickListener eventItemClickListener ) {
		this.eventItemClickListener = eventItemClickListener;
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


		if (convertView == null) {
			convertView = inflator.inflate(R.layout.swipe_list_item1, parent, false);
			convertView.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return false;
				}
			});

			mHolder = new ViewHolder();
			//mHolder.admin_list_email = (TextView) view.findViewById(R.id.admin_list_email);
			mHolder.admin_list_name = (TextView) convertView.findViewById(R.id.admin_list_name);
			//mHolder.admin_list_phone = (TextView) view.findViewById(R.id.admin_list_phone);
			mHolder.admin_list_nextArrow = (ImageButton) convertView.findViewById(R.id.admin_list_nextArrow);
			mHolder.profileImage = (ImageView) convertView.findViewById(R.id.image);
			mHolder.prog = (ProgressBar) convertView.findViewById(R.id.prog);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		final MemberListBean bean = list.get(position);
		
		mHolder.admin_list_name.setText(bean.getUser_first_name() + " " + bean.getUser_last_name());
	//	mHolder.admin_list_email.setText(bean.getUser_email());
		//mHolder.admin_list_phone.setText(bean.getUser_phone());

		mHolder.admin_list_name.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.INVISIBLE);

				DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) fragmentActivity;
				directorFragmentManageActivity.SwitcFragmentToUserInfoActivityWithAdd(bean.getUser_id(),fragmentBackResponseListener);
				}
		});

		mHolder.profileImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.INVISIBLE);

				DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) fragmentActivity;
				directorFragmentManageActivity.SwitcFragmentToUserInfoActivityWithAdd(bean.getUser_id() ,fragmentBackResponseListener);
			}
		});



		

		Drawable d = mContext.getResources().getDrawable(R.drawable.default_img_profile);

		String userProfileImageUrl = bean.getUser_profilepic();

		if (Validation.isStringNullOrBlank(userProfileImageUrl) == false)
		{
			mHolder.prog.setVisibility(View.VISIBLE);

			         Glide.with(mContext)
					.load(userProfileImageUrl)
					.error(d).placeholder(d)
					.transform(new CircleBitmapTranslation(mContext))
							 .listener(new RequestListener<String, GlideDrawable>() {
								 @Override
								 public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

									 mHolder.prog.setVisibility(View.GONE);

									 return false;
								 }

								 @Override
								 public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
									 mHolder.prog.setVisibility(View.GONE);

									 return false;
								 }
							 })
					.into(mHolder.profileImage );
			//image_load.displayImage(userProfileImageUrl,mHolder.profileImage, opt, new CustomImageLoader(mHolder.prog, mContext,d,true));

		}
		else
		{
			mHolder.prog.setVisibility(View.GONE);
			mHolder.profileImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_img_profile));
		}


		mHolder.admin_list_nextArrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				eventItemClickListener.OnBlockClick(position ,0);
			}
		});


			return convertView;
	}

	public class ViewHolder {
		ImageButton admin_list_nextArrow;
		/*TextView admin_list_email;
		TextView admin_list_phone;*/
		TextView admin_list_name;

		public ImageView profileImage;
		public ProgressBar prog;

		// public TextView editEvent;

	}




	public interface SwipeEventItemClickListener {
		// public void onClickProfilePic(String user_id);

		public void OnEditClick(int position);

		public void OnBlockClick(int position, int blockStatus);

	}

}
