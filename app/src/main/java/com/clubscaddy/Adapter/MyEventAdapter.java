package com.clubscaddy.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.clubscaddy.R;
import com.clubscaddy.Bean.EventBean;

public class MyEventAdapter extends BaseAdapter {
	ViewHolder mHolder;
	LayoutInflater inflator;
	View view;
	Context mContext;
	ListView listView;
	MyEventAdapter adapter;
	ArrayList<EventBean> list;


	int index;
	boolean expandStatus[];

	public MyEventAdapter(Context context, ArrayList<EventBean> l, OnClickListener click) {
		mContext = context;
		inflator = LayoutInflater.from(mContext);
		adapter = this;
		list = l;
		expandStatus = new boolean[list.size()];

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
			view = inflator.inflate(R.layout.my_event_item, parent, false);
			view.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return false;
				}
			});
			mHolder = new ViewHolder();
			mHolder.admin_list_name = (TextView) view.findViewById(R.id.event_title_name);
			mHolder.withdrawlTV = (TextView) view.findViewById(R.id.withdrawl);
			mHolder.withdrawlTV.setTag(position);
			view.setTag(mHolder);
			
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		final EventBean bean = list.get(position);
		mHolder.admin_list_name.setText("" + bean.getEvent_event_name());
		mHolder.withdrawlTV.setOnClickListener(adapterClicks);
		return view;
	}

	OnClickListener adapterClicks = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			int position = Integer.parseInt(v.getTag().toString());
			switch (id) {
			case R.id.withdrawl:
				//itemClickListener.onClickWithDrawl(position);
				break;
			}

		}
	};

	public class ViewHolder {
		TextView admin_list_name,withdrawlTV;
		
		
	}

}
