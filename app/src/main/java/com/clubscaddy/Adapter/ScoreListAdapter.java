package com.clubscaddy.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.clubscaddy.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ScoreListAdapter extends BaseAdapter
{
	ArrayList<HashMap<String, String>>score_list ;
	Activity activity ;
	
	public ScoreListAdapter(ArrayList<HashMap<String, String>>score_list , Activity activity)
	{
	this.activity = activity;
	this.score_list = score_list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return score_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return score_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	public class ViewHolder
	{
	TextView score_result_tv;	
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holder ;
		
		if(convertView == null)
		{
			convertView = LayoutInflater.from(activity).inflate(R.layout.score_list_adapter, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.score_result_tv = (TextView) convertView.findViewById(R.id.score_result_tv);
		holder.score_result_tv .setText(score_list.get(position).get("score_result"));
		return convertView;
	}

}
