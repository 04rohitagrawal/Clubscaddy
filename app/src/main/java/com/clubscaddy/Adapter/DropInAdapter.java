package com.clubscaddy.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.clubscaddy.R;
import com.clubscaddy.Bean.DropInBean;
import com.clubscaddy.utility.Utill;

public class DropInAdapter extends BaseAdapter {
	ViewHolder mHolder;
	LayoutInflater inflator;
	View view;
	Context mContext;
	ListView listView;
	DropInAdapter adapter;
	ArrayList<DropInBean> list;
	SwipeEventItemClickListener eventItemClickListener;


	public DropInAdapter(Context context, ArrayList<DropInBean> l) {
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
			mHolder.button1 = (Button) view.findViewById(R.id.swipe_button1);
			mHolder.button2 = (Button) view.findViewById(R.id.swipe_button2);
			mHolder.button2.setBackground(mContext.getResources().getDrawable(R.drawable.delete_icon));
			mHolder.profileImage = (ImageView) view.findViewById(R.id.image);
			mHolder.profileImage.setVisibility(View.GONE);
			mHolder.prog = (ProgressBar) view.findViewById(R.id.prog);
			mHolder.prog.setVisibility(View.GONE);
			mHolder.relative= (RelativeLayout) view.findViewById(R.id.relative);
			mHolder.relative.setVisibility(View.GONE);

			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		final DropInBean bean = list.get(position);
		

		
		mHolder.button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (DropInAdapter.this.eventItemClickListener != null)
					DropInAdapter.this.eventItemClickListener.OnEditClick(position);
			}
		});

		mHolder.button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(mContext,
				// "Button 2 Clicked",Toast.LENGTH_SHORT).show();

				if (DropInAdapter.this.eventItemClickListener != null) {
					DropInAdapter.this.eventItemClickListener.OnBlockClick(position, 1);
				}
			}

		});
		mHolder.admin_list_name.setText(""+Utill.formattedDateFromString(bean.getDropin_date())+""+Utill.get12TimeFormate(bean.getDropin_time()));
		return view;
	}

	public class ViewHolder {
		ImageView admin_list_nextArrow;
		TextView admin_list_email;
		TextView admin_list_phone;
		TextView admin_list_name;
		RelativeLayout admin_list_relativeLayout;
		public Button button1;
		public Button button2;
		public ImageView profileImage;
		public ProgressBar prog;
		public RelativeLayout relative;
		// public TextView editEvent;

	}

	public interface SwipeEventItemClickListener {
		// public void onClickProfilePic(String user_id);

		public void OnEditClick(int position);

		public void OnBlockClick(int position, int blockStatus);

	}

}
