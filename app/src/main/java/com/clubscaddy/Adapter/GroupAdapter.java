package com.clubscaddy.Adapter;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.clubscaddy.R;
import com.clubscaddy.Bean.GroupBean;

public class GroupAdapter extends BaseAdapter {
	ViewHolder mHolder;
	LayoutInflater inflator;
	View view;
	Context mContext;
	ListView listView;
	GroupAdapter adapter;
	ArrayList<GroupBean> list;
	SwipeEventItemClickListener eventItemClickListener;


	public GroupAdapter(Context context, ArrayList<GroupBean> l) {
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		view = convertView;
		if (view == null) {
			view = inflator.inflate(R.layout.group_adapter_item_view, parent, false);
			view.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return false;
				}
			});

			mHolder = new ViewHolder();
			//mHolder.admin_list_email = (TextView) view.findViewById(R.id.admin_list_email);
			mHolder.admin_list_name = (TextView) view.findViewById(R.id.admin_list_name);
			//mHolder.admin_list_phone = (TextView) view.findViewById(R.id.admin_list_phone);
			mHolder.admin_list_nextArrow = (ImageView) view.findViewById(R.id.admin_list_nextArrow);
			mHolder.admin_list_relativeLayout = (RelativeLayout) view.findViewById(R.id.admin_list_relativeLayout);
			//mHolder.button1 = (Button) view.findViewById(R.id.swipe_button1);
			/*mHolder.button2 = (Button) view.findViewById(R.id.swipe_button2);
			mHolder.button2.setBackground(mContext.getResources().getDrawable(R.drawable.delete_icon));
			mHolder.profileImage = (ImageView) view.findViewById(R.id.image);
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
		final GroupBean bean = list.get(position);
		

		
		mHolder.admin_list_relativeLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (GroupAdapter.this.eventItemClickListener != null)
					GroupAdapter.this.eventItemClickListener.OnEditClick(position);
			}
		});

		/*mHolder.button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(mContext,
				// "Button 2 Clicked",Toast.LENGTH_SHORT).show();
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
				 
		        // Setting Dialog Title
		        alertDialog.setTitle("Tannis club");
		 
		        // Setting Dialog Message
		        alertDialog.setMessage("Are you sure you want delete this?");
		 
		       
		        // Setting Positive "Yes" Button
		        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,int which) {
		 
		            	
		            	if (GroupAdapter.this.eventItemClickListener != null) {
							GroupAdapter.this.eventItemClickListener.OnBlockClick(position, 1);
						}
		            	
		            	  dialog.cancel();
		                    }
		        });
		 
		        // Setting Negative "NO" Button
		        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            // Write your code here to invoke NO event
		                dialog.cancel();
		            }
		        });
		 
		        // Showing Alert Message
		        alertDialog.show();
				
			}

		});*/
		mHolder.admin_list_name.setText(""+bean.getGroup_name());
		return view;
	}

	public class ViewHolder {
		ImageView admin_list_nextArrow;
		//TextView admin_list_email;
		//TextView admin_list_phone;
		TextView admin_list_name;
		RelativeLayout admin_list_relativeLayout;
		//public Button button1;
		//public Button button2;
		//public ImageView profileImage;
		//public ProgressBar prog;
	//	public RelativeLayout relative;
		// public TextView editEvent;

	}

	public interface SwipeEventItemClickListener {
		// public void onClickProfilePic(String user_id);

		public void OnEditClick(int position);

		public void OnBlockClick(int position, int blockStatus);

	}

}
