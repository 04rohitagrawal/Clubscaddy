package com.clubscaddy.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clubscaddy.R;
import com.clubscaddy.Bean.MemberListBean;

public class AdminListAdapter extends BaseAdapter{

	ViewHolder Viewholder;
	Context mContext;
	ArrayList<MemberListBean> alMemberList;
	public AdminListAdapter(Context mContext,ArrayList<MemberListBean> alMemberList) {
		this.mContext = mContext;
		this.alMemberList = alMemberList;
	}

	@Override
	public int getCount() {
		return alMemberList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.adminlist_item_xml, parent, false);
			Viewholder = new ViewHolder();

			Viewholder.admin_list_email = (TextView) convertView.findViewById(R.id.admin_list_email);
			Viewholder.admin_list_name = (TextView) convertView.findViewById(R.id.admin_list_name);
			Viewholder.admin_list_phone = (TextView) convertView.findViewById(R.id.admin_list_phone);
			Viewholder.admin_list_nextArrow = (ImageView) convertView.findViewById(R.id.admin_list_nextArrow);
			Viewholder.admin_list_relativeLayout = (RelativeLayout) convertView.findViewById(R.id.admin_list_relativeLayout);

			convertView.setTag(Viewholder);

		}else{
			Viewholder = (ViewHolder) convertView.getTag();
		}


		if(alMemberList.get(position).getUser_status().equals("1")){
			Viewholder.admin_list_relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.light_gray_color));
			Viewholder.admin_list_name.setText(alMemberList.get(position).getUser_first_name()+" "+alMemberList.get(position).getUser_last_name());
			Viewholder.admin_list_email.setText(alMemberList.get(position).getUser_email());
			Viewholder.admin_list_phone.setText(alMemberList.get(position).getUser_phone()); 
		}else{
			Viewholder.admin_list_relativeLayout.setBackgroundColor(mContext.getResources().getColor(R.color.gray_color));
			Viewholder.admin_list_name.setText(alMemberList.get(position).getUser_first_name()+" "+alMemberList.get(position).getUser_last_name());
			Viewholder.admin_list_email.setText(alMemberList.get(position).getUser_email());
			Viewholder.admin_list_phone.setText(alMemberList.get(position).getUser_phone()); 
		}

		return convertView;
	}

	static public class ViewHolder{
		ImageView admin_list_nextArrow;
		TextView admin_list_email;
		TextView admin_list_phone;
		TextView admin_list_name;
		RelativeLayout admin_list_relativeLayout;
	}
}
