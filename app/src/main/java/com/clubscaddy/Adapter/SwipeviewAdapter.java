package com.clubscaddy.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.clubscaddy.utility.CircleBitmapTranslation;
import com.clubscaddy.R;
import com.clubscaddy.Bean.MemberListBean;


public class SwipeviewAdapter extends BaseAdapter {
	ViewHolder mHolder;
	LayoutInflater inflator;
	View view;
	Context mContext;
	ListView listView;
	SwipeviewAdapter adapter;
	ArrayList<MemberListBean> list;
	SwipeEventItemClickListener eventItemClickListener;


	public SwipeviewAdapter(Context context, ArrayList<MemberListBean> l) {
		mContext = context;
		inflator = LayoutInflater.from(mContext);
		adapter = this;
		list = l;
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
		view = convertView;
		if (view == null) {
			view = inflator.inflate(R.layout.new_list_item, parent, false);
			view.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return false;
				}
			});

			mHolder = new ViewHolder();
			mHolder.admin_list_email = (TextView) view.findViewById(R.id.admin_list_email);
			mHolder.admin_list_name = (TextView) view.findViewById(R.id.admin_list_name);
			mHolder.admin_list_phone = (TextView) view.findViewById(R.id.admin_list_phone);
			mHolder.admin_list_nextArrow = (ImageView) view.findViewById(R.id.admin_list_nextArrow);
			mHolder.admin_list_relativeLayout = (RelativeLayout) view.findViewById(R.id.admin_list_relativeLayout);
			mHolder.user_select_checkbox = (CheckBox) view.findViewById(R.id.user_select_checkbox);
			mHolder.profileImage = (ImageView) view.findViewById(R.id.image);
			mHolder.prog = (ProgressBar) view.findViewById(R.id.prog);

			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		final MemberListBean bean = list.get(position);
		mHolder.user_select_checkbox.setChecked(bean.getMemberSelection());
		mHolder.user_select_checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				list.get(position).setMemberSelection(isChecked);
			}
		});
		String status = bean.getUser_status();
		

		if (bean.getUser_status().equals("1")) {
			mHolder.admin_list_name.setText(bean.getUser_first_name() + " " + bean.getUser_last_name());
			mHolder.admin_list_email.setText(bean.getUser_email());
			mHolder.admin_list_phone.setText(bean.getUser_phone());
		} else {
			mHolder.admin_list_name.setText(bean.getUser_first_name() + " " + bean.getUser_last_name());
			mHolder.admin_list_email.setText(bean.getUser_email());
			mHolder.admin_list_phone.setText(bean.getUser_phone());
		}
		/*mHolder.button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (SwipeviewAdapter.this.eventItemClickListener != null)
					SwipeviewAdapter.this.eventItemClickListener.OnEditClick(position);
			}
		});

		mHolder.button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(mContext,
				// "Button 2 Clicked",Toast.LENGTH_SHORT).show();

				if (SwipeviewAdapter.this.eventItemClickListener != null) {
					int bloackStatus = -1;
					if (bean.getUser_status().equalsIgnoreCase("1")) {
						bloackStatus = 1;
					} else {
						bloackStatus = 2;
					}
					SwipeviewAdapter.this.eventItemClickListener.OnBlockClick(position, bloackStatus);
				}
			}

		});*/
		

		Drawable d = mContext.getResources().getDrawable(R.drawable.logo_profile);

		Glide.with(mContext).load(bean.getUser_profilepic()).transform(new CircleBitmapTranslation(mContext)).placeholder(d).error(d).into(mHolder.profileImage);


		return view;
	}

	public class ViewHolder {
		ImageView admin_list_nextArrow;
		TextView admin_list_email;
		TextView admin_list_phone;
		TextView admin_list_name;
		RelativeLayout admin_list_relativeLayout;
		
		public ImageView profileImage;
		public ProgressBar prog;
		CheckBox user_select_checkbox;
		// public TextView editEvent;

	}

	public interface SwipeEventItemClickListener {
		// public void onClickProfilePic(String user_id);

		public void OnEditClick(int position);

		public void OnBlockClick(int position, int blockStatus);

	}

}
