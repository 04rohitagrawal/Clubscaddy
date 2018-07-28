package com.clubscaddy.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clubscaddy.R;
import com.clubscaddy.Bean.CourtBean;
import com.clubscaddy.Bean.MemberListBean;

public class MembersSelectionAdapter extends ArrayAdapter<MemberListBean>{

	ViewHolder Viewholder;
	Context mContext;
	ArrayList<MemberListBean> alMemberList;
	public MembersSelectionAdapter(Context mContext,ArrayList<MemberListBean> alMemberList) {
		super(mContext,R.id.tvName,alMemberList);
		this.mContext = mContext;
		this.alMemberList = alMemberList;
	}

	@Override
	public int getCount() {
		return alMemberList.size();
	}

	@Override
	public MemberListBean getItem(int position) {
		return this.alMemberList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.court_item, parent, false);
			Viewholder = new ViewHolder();
			Viewholder.courtName = (TextView) convertView.findViewById(R.id.court_name);
			convertView.setTag(Viewholder);

		}else{
			Viewholder = (ViewHolder) convertView.getTag();
		}
	//	Viewholder.courtName.setText(alMemberList.get(position).getCourt_number()+" "+alMemberList.get(position).getCourt_name());
		Viewholder.courtName.setText(alMemberList.get(position).getUser_first_name()+" "+alMemberList.get(position).getUser_last_name());
		return convertView;
	}

	static public class ViewHolder{
		TextView courtName;
	}
}
