package com.clubscaddy.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clubscaddy.R;
import com.clubscaddy.Bean.DropInBean;
import com.clubscaddy.fragment.DropInGroupHomeFragment.onClickDeleteDropIn;

public class ExpandableListAdapter extends BaseAdapter {
	private Context _context;
	

	// child data in format of header title, child title
	private ArrayList<DropInBean> dropinList;
	onClickDeleteDropIn onClicks;

	public ExpandableListAdapter(Context context, ArrayList<DropInBean> list, onClickDeleteDropIn click) {
		this._context = context;
		this.dropinList = list;
		this.onClicks = click;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dropinList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dropinList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		DropInBean headerTitle = (DropInBean) dropinList.get(position);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.dropiin_main_item, null);
		}

		TextView lblListHeader = (TextView) convertView.findViewById(R.id.date);
		
		lblListHeader.setTypeface(null, Typeface.BOLD);
		//lblListHeader.setText(headerTitle.getDropin_date() + headerTitle.getDropin_time());
		String formateString  = "";
		if(headerTitle.getDropin_formate().equalsIgnoreCase("1")){
			formateString = "Single";
		}else if(headerTitle.getDropin_formate().equalsIgnoreCase("2")){
			formateString = "Double";
		}else if(headerTitle.getDropin_formate().equalsIgnoreCase("3")){
			formateString = "Mixed";
		}
		formateString = "<font color=#000000>("+formateString+")</font>";
		lblListHeader.setText(Html.fromHtml("" +headerTitle.getDropin_date() + " "
				+ headerTitle.getDropin_time()));
		
		
		 
		
		return convertView;
	}

	/*@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this.dropinList.get(groupPosition).getMemeberList().get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		final Dropin_memberBean memberBean = (Dropin_memberBean) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.dropiin_child_item, null);
		}

		TextView txtListChild = (TextView) convertView.findViewById(R.id.name);
		TextView status = (TextView) convertView.findViewById(R.id.status);
		ImageView deleteIV = (ImageView) convertView.findViewById(R.id.delete);
		deleteIV.setTag(groupPosition+"-"+childPosition);

		txtListChild.setText(memberBean.getMember_first_name() + " " + memberBean.getMember_last_name());
		if (memberBean.getParticipants_status().equalsIgnoreCase("1")) {
			status.setText("Invited");
			deleteIV.setVisibility(View.GONE);
		} else if (memberBean.getParticipants_status().equalsIgnoreCase("2")) {
			status.setText("Joined");
			deleteIV.setVisibility(View.GONE);
		} else
			if (memberBean.getParticipants_status().equalsIgnoreCase("4"))
			{
			status.setText("Accepted");
			deleteIV.setVisibility(View.GONE);
		    }
			else
			{
				if (memberBean.getParticipants_status().equalsIgnoreCase("3"))
				{
				status.setText("Decline");
				deleteIV.setVisibility(View.GONE);
			    }
				else
				{
					if (memberBean.getParticipants_status().equalsIgnoreCase("5"))
					{
					status.setText("Rejected");
					deleteIV.setVisibility(View.GONE);
				    }
					else
					{
						status.setText("Deleted");
						deleteIV.setVisibility(View.GONE);
					}
				}
			}
		deleteIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String tag = (String) v.getTag();
				String index [] = tag.split("-");
				int groupPostion = Integer.parseInt(index[0]);
				int childPostion = Integer.parseInt(index[1]);
				if(onClicks!=null)
				onClicks.onDeleteMember(groupPosition, childPosition);

			}
		});

		Animation anim = AnimationUtils.loadAnimation(_context, R.anim.bottom_down);
		convertView.startAnimation(anim);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.dropinList.get(groupPosition).getMemeberList().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.dropinList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this.dropinList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		DropInBean headerTitle = (DropInBean) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.dropiin_main_item, null);
		}

		TextView lblListHeader = (TextView) convertView.findViewById(R.id.date);

		lblListHeader.setTypeface(null, Typeface.BOLD);
		//lblListHeader.setText(headerTitle.getDropin_date() + headerTitle.getDropin_time());
		String formateString  = "";
		if(headerTitle.getDropin_formate().equalsIgnoreCase("1")){
			formateString = "Single";
		}else if(headerTitle.getDropin_formate().equalsIgnoreCase("2")){
			formateString = "Double";
		}else if(headerTitle.getDropin_formate().equalsIgnoreCase("3")){
			formateString = "Mixed";
		}
		formateString = "<font color=#000000>("+formateString+")</font>";
		lblListHeader.setText(Html.fromHtml("" +headerTitle.getDropin_date() + " At "
				+ headerTitle.getDropin_time()+" "+formateString));



		ImageView deleteIcon = (ImageView) convertView.findViewById(R.id.delete);
		TextView status_line_tv = (TextView) convertView.findViewById(R.id.status_line_tv);
		status_line_tv.setText(" Invited : "+ headerTitle.getTotal_invited_user()+", Joined : "+headerTitle.getTotal_joined_user()+", Accepted : "+headerTitle.getTotal_acceptedback_user()+", Rejected : "+(headerTitle.getDeclined_user())+", Status : "+headerTitle.getOpenstatus());

		deleteIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {




				AlertDialog.Builder alertDialog = new AlertDialog.Builder(_context);

				// Setting Dialog Title
				alertDialog.setTitle(SessionManager.getClubName(_context));

				// Setting Dialog Message
				alertDialog.setMessage("Are you sure want to delete this matchmaker?");

				// Setting Icon to Dialog


				// Setting Positive "Yes" Button
				alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {
						dialog.cancel();
						if (onClicks != null) {
							onClicks.onDelete(groupPosition);
						}
					// Write your code here to invoke YES event
					}
				});

				// Setting Negative "NO" Button
				alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,	int which) {
					// Write your code here to invoke NO event
							dialog.cancel();
					}
				});

				// Showing Alert Message
				alertDialog.show();












			}
		});

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	int lastExpandedGroupPosition = -1;

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		// collapse the old expanded group, if not the same
		// as new group to expand
		if (groupPosition != lastExpandedGroupPosition) {
			listView.collapseGroup(lastExpandedGroupPosition);
		}

		super.onGroupExpanded(groupPosition);
		lastExpandedGroupPosition = groupPosition;
	}*/
}
