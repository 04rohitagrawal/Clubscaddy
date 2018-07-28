package com.clubscaddy.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clubscaddy.R;
import com.clubscaddy.utility.AppConstants;

public class DirectorDashboardAdapter extends BaseAdapter {
public static TextView notificationCount;


	int dir_linearlayout_parentheight ;

	int columnHeight ;



	Context mContext;
	int[] dirResImage = { R.drawable.reserve, R.drawable.notification, R.drawable.drop_ins, R.drawable.event, R.drawable.group, R.drawable.news,
			R.drawable.profile, R.drawable.member_directory, R.drawable.setting ,R.drawable.communication ,R.drawable.coaches ,R.drawable.classified };
	String[] dirResName = new String[] { "Reserve", "Notification",AppConstants.drop_in, "Events / Classes", "Groups/schedules", "News", "Profile", "Member-Directory", "Settings" , "BroadCast", "Coaches", "Classifieds"};

	public DirectorDashboardAdapter(Context mContext ,int dir_linearlayout_parentheight) {
		this.mContext = mContext;
		this.dir_linearlayout_parentheight = dir_linearlayout_parentheight;
		columnHeight = (int) ((AppConstants.getDeviceHeight((Activity) mContext)-dir_linearlayout_parentheight)/5);
	}

	@Override
	public int getCount() {
		return dirResName.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.directordashboad_item_xml, parent, false);

		RelativeLayout relative_layout = (RelativeLayout) view.findViewById(R.id.relative_layout);



		RelativeLayout.LayoutParams relativeLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT , columnHeight );



		relative_layout.setLayoutParams(relativeLayout);


		ImageView dir_item_images = (ImageView) view.findViewById(R.id.dir_item_images);
		TextView dir_item_imagesName = (TextView) view.findViewById(R.id.dir_item_imagesName);
		notificationCount = (TextView) view.findViewById(R.id.notificationCount);

		// dir_item_images.setImageResource(dir_item_images[position]);
		dir_item_images.setBackgroundResource(dirResImage[position]);
		notificationCount.setVisibility(View.GONE);
		
		dir_item_imagesName.setText(dirResName[position]);
		switch (position) {
		case 0:
			dir_item_imagesName.setTextColor(mContext.getResources().getColor(R.color.reserver));
			break;
		case 1:
			dir_item_imagesName.setTextColor(mContext.getResources().getColor(R.color.notification));
			notificationCount.setVisibility(View.VISIBLE);
			try {
				int Count = Integer.parseInt(AppConstants.getNotificationCount());
				if(Count==0){
					notificationCount.setVisibility(View.GONE);
				}else{
					notificationCount.setText(""+AppConstants.getNotificationCount());	
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		case 2:
			dir_item_imagesName.setTextColor(mContext.getResources().getColor(R.color.drop_ins));
			break;
		case 3:
			dir_item_imagesName.setTextColor(mContext.getResources().getColor(R.color.event_text));
			break;
		case 4:
			dir_item_imagesName.setTextColor(mContext.getResources().getColor(R.color.lessions));
			break;
		case 5:
			dir_item_imagesName.setTextColor(mContext.getResources().getColor(R.color.news_text));



			notificationCount.setVisibility(View.VISIBLE);
			try {
				int Count = Integer.parseInt(AppConstants.getUser_news());
				if(Count==0){
					notificationCount.setVisibility(View.GONE);
				}else{
					notificationCount.setText(""+AppConstants.getUser_news());
				}
			} catch (Exception e) {
				// TODO: handle exception
			}


			break;
		case 6:
			dir_item_imagesName.setTextColor(mContext.getResources().getColor(R.color.profile_text_color));
			break;
		case 7:
			dir_item_imagesName.setTextColor(mContext.getResources().getColor(R.color.member_directory));
			break;
		case 8:
			dir_item_imagesName.setTextColor(mContext.getResources().getColor(R.color.setting));
			break;
		case 9:
			dir_item_imagesName.setTextColor(mContext.getResources().getColor(R.color.broadcast_text_color));
			String formateString = "<font color=#f4b407>"+"Communication"+"</font>";
			dir_item_imagesName.setText("Communications");
			break;
		case 10:
			dir_item_imagesName.setTextColor(mContext.getResources().getColor(R.color.score_text));
	//		Utill.showToast(mContext, "Profile");
			break;
		case 11:
			dir_item_imagesName.setTextColor(mContext.getResources().getColor(R.color.statistics_text_color));
		//	Utill.showToast(mContext, "Statistics");


			dir_item_imagesName.setTextColor(mContext.getResources().getColor(R.color.notification));
			notificationCount.setVisibility(View.VISIBLE);
			try {
				int Count = AppConstants.getUser_classified();
				if(Count==0){
					notificationCount.setVisibility(View.GONE);
				}else{
					notificationCount.setText(""+Count);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			break;
			
			
			

		default:
			break;
		}
	//	dir_item_imagesName.setText(dirResName[position]);
		return view;
	}
	
	
}
