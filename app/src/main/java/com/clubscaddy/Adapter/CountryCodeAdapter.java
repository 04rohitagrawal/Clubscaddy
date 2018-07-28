package com.clubscaddy.Adapter;

import java.util.ArrayList;

import com.clubscaddy.utility.DeviceInfo;
import com.clubscaddy.R;
import com.clubscaddy.Bean.CountryDatabean;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class CountryCodeAdapter  extends BaseAdapter implements SpinnerAdapter
{

	
	ArrayList<CountryDatabean>country_list ;
	Activity activity ;
	public CountryCodeAdapter(ArrayList<CountryDatabean>country_list, Activity activity ,final Spinner spinner)
	{
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.country_list = country_list;


		spinner.post(new Runnable(){
			public void run(){
				int height = spinner.getHeight();
				spinner.setDropDownVerticalOffset(height +5);
			}
		});



	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return country_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return country_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder ;

		if (convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(R.layout.notification_item , null);
			viewHolder.text1 = (TextView) convertView.findViewById(R.id.text1);
			viewHolder.devider_line = (LinearLayout) convertView.findViewById(R.id.devider_line);

			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.devider_line.setVisibility(View.GONE);

		convertView.setPadding(10,20,10,20);
		viewHolder.text1.setText(country_list.get(position).getCountry_code_text());


		return convertView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder ;

		if (convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(R.layout.notification_item , null);
			viewHolder.text1 = (TextView) convertView.findViewById(R.id.text1);
			viewHolder.devider_line = (LinearLayout) convertView.findViewById(R.id.devider_line);

			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.devider_line.setVisibility(View.VISIBLE);

		viewHolder.text1.setPadding(10, 30,10,30);
		viewHolder.text1.setText(country_list.get(position).getCountry_code_text());


		return convertView;    }


	public class ViewHolder  {
		TextView text1 ;
		LinearLayout devider_line;
	}

}
