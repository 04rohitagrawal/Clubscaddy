package com.clubscaddy.Adapter;

import java.util.ArrayList;

import com.clubscaddy.R;
import com.clubscaddy.Bean.CourtBookBean;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyCourtListAdapter extends BaseAdapter
{

	ArrayList<CourtBookBean>CourtBookList ;
	Activity activity;
	public  MyCourtListAdapter(ArrayList<CourtBookBean>CourtBookList ,Activity activity) {
		// TODO Auto-generated constructor stub
		this.CourtBookList = CourtBookList;
		this.activity = activity;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return CourtBookList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return CourtBookList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		convertView = LayoutInflater.from(activity).inflate(R.layout.my_courtlist_adapter_layout, null);
		
		TextView court_name_tv = (TextView) convertView.findViewById(R.id.court_name_tv);
		court_name_tv.setText(CourtBookList.get(position).getCourt_name());
		
		
		TextView court_time_tv = (TextView) convertView.findViewById(R.id.court_time_tv);
		court_time_tv.setText(CourtBookList.get(position).getCourt_reservation_start_time());
		
		return convertView;
	}

}
