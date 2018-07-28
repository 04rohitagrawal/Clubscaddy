package com.clubscaddy.Adapter;

import java.util.ArrayList;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.clubscaddy.R;
import com.clubscaddy.Bean.EventBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.SessionManager;

public class NewEventAdapter extends BaseAdapter {
	ViewHolder mHolder;
	LayoutInflater inflator;
	View view;
	Context mContext;
	ListView listView;
	NewEventAdapter adapter;
	ArrayList<EventBean> list;
	SwipeEventItemClickListener eventItemClickListener;


	public NewEventAdapter(Context context, ArrayList<EventBean> l) {
		mContext = context;
		inflator = LayoutInflater.from(mContext);
		adapter = this;
		list = l;
	}

	public void setEventItemClickListener(SwipeEventItemClickListener evtclck) {
		this.eventItemClickListener = evtclck;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		view = convertView;
		if (view == null) {
			view = inflator.inflate(R.layout.group_adapter_item_view, parent, false);
			

			mHolder = new ViewHolder();
			//mHolder.admin_list_email = (TextView) view.findViewById(R.id.admin_list_email);
			mHolder.admin_list_name = (TextView) view.findViewById(R.id.admin_list_name);
			//mHolder.admin_list_phone = (TextView) view.findViewById(R.id.admin_list_phone);
			mHolder.admin_list_nextArrow = (ImageView) view.findViewById(R.id.admin_list_nextArrow);
			
			
			

			
			mHolder.admin_list_relativeLayout = (RelativeLayout) view.findViewById(R.id.admin_list_relativeLayout);
			

			//mHolder.button1 = (Button) view.findViewById(R.id.swipe_button1);
			//mHolder.button2 = (Button) view.findViewById(R.id.swipe_button2);
			/*try
			{
				mHolder.button2.setBackground(mContext.getResources().getDrawable(R.drawable.delete_icon));	
			}catch(NoSuchMethodError e)
			{
				
			}*/
			
			/*
			*/
			/*mHolder.profileImage = (ImageView) view.findViewById(R.id.image);
			mHolder.profileImage.setVisibility(View.GONE);
			mHolder.prog = (ProgressBar) view.findViewById(R.id.prog);
			mHolder.prog.setVisibility(View.GONE);
			mHolder.relative= (RelativeLayout) view.findViewById(R.id.relative);
			mHolder.relative.setVisibility(View.GONE);
			mHolder.button1.setVisibility(View.GONE);
*/
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		final EventBean bean = list.get(position);
		
		mHolder.admin_list_relativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				eventItemClickListener.OnEditClick(position);
			}
		});
		/*
		mHolder.button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NewEventAdapter.this.eventItemClickListener != null)
					NewEventAdapter.this.eventItemClickListener.OnEditClick(position);
			}
		});

		mHolder.button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(mContext,
				// "Button 2 Clicked",Toast.LENGTH_SHORT).show();

				if (NewEventAdapter.this.eventItemClickListener != null) {
					
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
					 
			        // Setting Dialog Title
			        alertDialog.setTitle("Tanis club");
			 
			        // Setting Dialog Message
			        alertDialog.setMessage("Are you sure you want delete this?");
			 
			        // Setting Icon to Dialog
			     
			 
			        // Setting Positive "Yes" Button
			        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog,int which) {
			            	NewEventAdapter.this.eventItemClickListener.OnBlockClick(position, 1);
			            	  dialog.cancel();
			           		            }
			        });
			 
			        // Setting Negative "NO" Button
			        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			            // Write your code here to invoke NO event
			               
			            }
			        });
			 
			        // Showing Alert Message
			        alertDialog.show();
					//
				}
			}

		});*/
		if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MOBILE_ADMIN) || SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_DIRECTOR)){
			}else{
			//mHolder.admin_list_nextArrow.setVisibility(View.GONE);
		}
		mHolder.admin_list_nextArrow.setVisibility(View.VISIBLE);
		
		mHolder.admin_list_name.setText(""+bean.getEvent_event_name());
		return view;
	}

	public class ViewHolder {
		public ImageView admin_list_nextArrow;
		//TextView admin_list_email;
		//TextView admin_list_phone;
		TextView admin_list_name;
		RelativeLayout admin_list_relativeLayout;
		//public Button button1;
		//public Button button2;
		//public ImageView profileImage;
		//public ProgressBar prog;
		//public RelativeLayout relative;
		
		// public TextView editEvent;

	}

	public interface SwipeEventItemClickListener {
		// public void onClickProfilePic(String user_id);

		public void OnEditClick(int position);

		public void OnBlockClick(int position, int blockStatus);

	}

}
