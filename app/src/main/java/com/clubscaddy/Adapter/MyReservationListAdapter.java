package com.clubscaddy.Adapter;

import java.util.ArrayList;

import com.clubscaddy.fragment.ShowReservationFragment;
import com.clubscaddy.R;
import com.clubscaddy.Bean.MyReservationBean;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;



public class MyReservationListAdapter extends BaseAdapter
{
	
	
	ArrayList<MyReservationBean>my_reservation_list ;
	Context context ;
	ShowReservationFragment showReservationFragment ;
	public MyReservationListAdapter(ArrayList<MyReservationBean>my_reservation_list , Context context , ShowReservationFragment showReservationFragment)
	{
	this.context = context;
		this.showReservationFragment = showReservationFragment;

		this.my_reservation_list = my_reservation_list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return my_reservation_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return my_reservation_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	 public   class   ViewHolder 
	{
		TextView booking_name_tv ;
		TextView reservation_type_tv ;
		TextView reservation_date_tv ;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holeder ;
		if(convertView == null)
		{
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.my_reservation_adapter, null);
			holeder = new ViewHolder();
			holeder.booking_name_tv = (TextView) convertView.findViewById(R.id.booking_name_tv);
			holeder.reservation_type_tv = (TextView) convertView.findViewById(R.id.reservation_type_tv);
			holeder.reservation_date_tv = (TextView) convertView.findViewById(R.id.booking_date_tv);
			convertView.setTag(holeder);
		}
		else
		{
			holeder = (ViewHolder) convertView.getTag();	
		}
		if(!my_reservation_list.get(position).getBookingname().equals(""))
		{
			holeder.booking_name_tv.setText(my_reservation_list.get(position).getBookingname());	
			holeder.booking_name_tv.setVisibility(View.VISIBLE);	

		}
		else
		{
				
			holeder.booking_name_tv.setVisibility(View.GONE);

		}
		
		if(my_reservation_list.get(position).getCourt_reservation_recursiveid().equals("0"))
		{
			holeder.reservation_type_tv.setText(my_reservation_list.get(position).getReservedate().split(" ")[0]);
			holeder.reservation_date_tv.setText(my_reservation_list.get(position).getReservedate().split(" ")[1]+" "+my_reservation_list.get(position).getReservedate().split(" ")[2]+" "+my_reservation_list.get(position).getReservedate().split(" ")[3]);
		}
		else
		{
			holeder.reservation_type_tv.setText("Recursive reservation");
			holeder.reservation_date_tv.setText(my_reservation_list.get(position).getReservedate());
		}
		Log.e("position" ,position+" "+(my_reservation_list.size()-1)+" "+showReservationFragment.lastCallItemCount);


		/*if (position == my_reservation_list.size()-1 && showReservationFragment.isWebServiceCalling == false && showReservationFragment.lastCallItemCount != 0)
		{
			Log.e("position" ,position+"");

			showReservationFragment.getNextReservationList();
		}
		*/
		return convertView;
	}

}
