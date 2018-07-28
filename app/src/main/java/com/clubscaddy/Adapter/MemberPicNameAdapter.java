package com.clubscaddy.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.fragment.BroadCastDetailFrageMent;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.clubscaddy.R;
import com.clubscaddy.Bean.BroadcastMemberBean;
import com.clubscaddy.utility.CircleTransform;

public class MemberPicNameAdapter extends ArrayAdapter<BroadcastMemberBean>{

	Context mContext;
	ArrayList<BroadcastMemberBean> broadCastList;

	BroadCastDetailFrageMent frageMent ;
	public MemberPicNameAdapter(BroadCastDetailFrageMent frageMent, ArrayList<BroadcastMemberBean> alMemberList) {
		super(frageMent.getActivity(),R.id.tvName,alMemberList);
		this.frageMent = frageMent;

		this.mContext = frageMent.getActivity();
		this.broadCastList = alMemberList;

	}

	@Override
	public int getCount() {
		return broadCastList.size();
	}

	@Override
	public BroadcastMemberBean getItem(int position) {
		return this.broadCastList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {


		final ViewHolder Viewholder;

		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.member_pic_name_view, parent, false);
			Viewholder = new ViewHolder();
			Viewholder.brodCastMessage = (TextView) convertView.findViewById(R.id.memberName);
			Viewholder.profilePicIV = (ImageView) convertView.findViewById(R.id.image);
			Viewholder.proBar = (ProgressBar) convertView.findViewById(R.id.prog);
			convertView.setTag(Viewholder);

		}else{
			Viewholder = (ViewHolder) convertView.getTag();
		}
		
		final BroadcastMemberBean bean = broadCastList.get(position);
		
		Viewholder.brodCastMessage.setText(bean.getMember_first_name()+ " "+bean.getMember_last_name());
		Drawable d = mContext.getResources().getDrawable(R.drawable.default_img_profile);

		try
		{
			Viewholder.proBar.setVisibility(View.VISIBLE);
			if (bean.getUser_profilepic() != null){
				Picasso.with(mContext)
						.load(bean.getUser_profilepic())
						.placeholder(mContext. getResources().getDrawable( R.drawable.default_img_profile )) // optional
						.error(R.drawable.default_img_profile)         // optional
						.transform(new CircleTransform()) .into(Viewholder.profilePicIV, new Callback() {
					@Override
					public void onSuccess() {
						Viewholder.proBar.setVisibility(View.GONE);
					}

					@Override
					public void onError() {
						Viewholder.proBar.setVisibility(View.GONE);

					}
				});
				//image_load.displayImage(bean.getUser_profilepic(),Viewholder.profilePicIV, opt, new CustomImageLoader(Viewholder.proBar, mContext,d,false));
			}
		}
		catch (Exception e)
		{
			Viewholder.proBar.setVisibility(View.GONE);

			Viewholder.profilePicIV.setImageDrawable(d);
		}



		Viewholder.brodCastMessage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) frageMent.getActivity();
				directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(bean.getMember_id());
			}
		});




		Viewholder.profilePicIV .setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) frageMent.getActivity();
				directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(bean.getMember_id());
			}
		});




		return convertView;
	}

	static public class ViewHolder{
		TextView brodCastMessage;
		ImageView profilePicIV;
		ProgressBar proBar;
	}
}
