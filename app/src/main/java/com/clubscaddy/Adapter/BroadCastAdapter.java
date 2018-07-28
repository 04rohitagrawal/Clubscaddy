package com.clubscaddy.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.clubscaddy.R;
import com.clubscaddy.Bean.BroadcastPollingBean;

public class BroadCastAdapter extends ArrayAdapter<BroadcastPollingBean>{

	ViewHolder Viewholder;
	Context mContext;
	ArrayList<BroadcastPollingBean> broadCastList;
	public BroadCastAdapter(Context mContext,ArrayList<BroadcastPollingBean> alMemberList) {
		super(mContext,R.id.tvName,alMemberList);
		this.mContext = mContext;
		this.broadCastList = alMemberList;
	}

	@Override
	public int getCount() {
		return broadCastList.size();
	}

	@Override
	public BroadcastPollingBean getItem(int position) {
		return this.broadCastList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.broadcastite, parent, false);
			Viewholder = new ViewHolder();
			Viewholder.brodCastMessage = (TextView) convertView.findViewById(R.id.broadcastmessage);
			convertView.setTag(Viewholder);

		}else{
			Viewholder = (ViewHolder) convertView.getTag();
		}
		//if item is type of broadcast 
		Drawable right = getContext().getResources().getDrawable( R.drawable.next_arrow);
		if(broadCastList.get(position).getBroadcast_type().equalsIgnoreCase("1")){
			Drawable img = getContext().getResources().getDrawable( R.drawable.small_broadcast);
			Viewholder.brodCastMessage.setCompoundDrawablesWithIntrinsicBounds( img, null, right, null);
		}else{
			//if item is type of polling 
			Drawable img = getContext().getResources().getDrawable( R.drawable.polling_new);
			Viewholder.brodCastMessage.setCompoundDrawablesWithIntrinsicBounds(img, null, right, null);
		}
		Viewholder.brodCastMessage.setText(broadCastList.get(position).getBroadcast_msg());
		return convertView;
	}

	static public class ViewHolder{
		TextView brodCastMessage;
	}
}
