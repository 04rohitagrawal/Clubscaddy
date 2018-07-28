package com.clubscaddy.Adapter;

import java.util.ArrayList;
import java.util.List;

import com.clubscaddy.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class SportTypeSpinnerAdapter extends ArrayAdapter<String>
{
	
	public SportTypeSpinnerAdapter(Activity activity, int resource, int textViewResourceId, ArrayList<String> sport_type_item_list) {
		super(activity, resource, textViewResourceId, sport_type_item_list);
		this.sport_type_item_list = sport_type_item_list;
		this.activity = activity;
		// TODO Auto-generated constructor stub
	}

	ArrayList<String>sport_type_item_list ;
	Activity activity;
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return sport_type_item_list.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return sport_type_item_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class ViewHolder
	{
		CheckedTextView spinner_list_item_tv ;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holder ;
		
		if(convertView == null)
		{
			holder = new ViewHolder();
		convertView = LayoutInflater.from(activity).inflate(R.layout.spinner_item_layout, null);	
		holder.	spinner_list_item_tv = (CheckedTextView) convertView.findViewById(R.id.text1);
		convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.	spinner_list_item_tv.setText(sport_type_item_list.get(position));
		//spinner_list_item_tv
		return convertView;
	}

}
