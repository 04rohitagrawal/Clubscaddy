package com.clubscaddy.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.clubscaddy.R;
import com.clubscaddy.Bean.NotificationsBean;

public class NotificationAdapter extends BaseAdapter {
	ViewHolder mHolder;
	LayoutInflater inflator;
	View view;
	Context mContext;
	ListView listView;
	NotificationAdapter adapter;
	ArrayList<NotificationsBean> list;

String colour_array[]={"#76aa43"};
	public NotificationAdapter(Context context, ArrayList<NotificationsBean> l) {
		mContext = context;
		inflator = LayoutInflater.from(mContext);
		adapter = this;
		list = l;
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
			view = inflator.inflate(R.layout.notification, parent, false);
			

			mHolder = new ViewHolder();
			//mHolder.admin_list_email = (TextView) view.findViewById(R.id.admin_list_email);
			mHolder.admin_list_name = (TextView) view.findViewById(R.id.admin_list_name);


			view.setTag(mHolder);
		} else 
		{
			mHolder = (ViewHolder) view.getTag();
		}
		 NotificationsBean bean = list.get(position);
		

		
		
		
		try
		{
			
			if(Integer.parseInt( bean.getNotification_status()) ==2)	
			{
				mHolder.admin_list_name.setTypeface(null, Typeface.BOLD);
				
							
			}
			if(Integer.parseInt( bean.getNotification_status()) ==1)	
			{
			
				mHolder.admin_list_name.setTypeface(null, Typeface.NORMAL);
							
			}
		}
		catch(Exception e)
		{
			
		}
		

		

		mHolder.admin_list_name.setText(""+bean.getNotification_message());
		
		
		
	
		
		
		if(bean.getNotification_type().equalsIgnoreCase("1") )
		{
			mHolder.admin_list_name.setTextColor(Color.parseColor("#76aa43"));	
		}
		if(bean.getNotification_type().equalsIgnoreCase("2") )
		{
			mHolder.admin_list_name.setTextColor(Color.parseColor("#bb653f"));	
		}
		if(bean.getNotification_type().equalsIgnoreCase("3") )
		{
			mHolder.admin_list_name.setTextColor(Color.parseColor("#435da"));	
		}
		if(bean.getNotification_type().equalsIgnoreCase("4") )
		{
			mHolder.admin_list_name.setTextColor(Color.parseColor("#ad4e9c"));	
		}
		if(bean.getNotification_type().equalsIgnoreCase("5") )
		{
			mHolder.admin_list_name.setTextColor(Color.parseColor("#769EB4"));	
		}
		if(bean.getNotification_type().equalsIgnoreCase("6") )
		{
			mHolder.admin_list_name.setTextColor(Color.parseColor("#27959c"));	
		}
		if(bean.getNotification_type().equalsIgnoreCase("7") )
		{
			mHolder.admin_list_name.setTextColor(Color.parseColor("#5A5A5A"));	
		}
		if(bean.getNotification_type().equalsIgnoreCase("8") )
		{
			mHolder.admin_list_name.setTextColor(mContext.getResources().getColor(R.color.score_text));
		}


		if(bean.getNotification_type().equalsIgnoreCase("9") )
		{
			mHolder.admin_list_name.setTextColor(mContext.getResources().getColor(R.color.leaguew_notification));
		}




		return view;
	}

	public class ViewHolder {

		TextView admin_list_name;



	}



}
