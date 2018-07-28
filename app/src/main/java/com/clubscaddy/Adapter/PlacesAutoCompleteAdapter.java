package com.clubscaddy.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.clubscaddy.R;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.utility.SessionManager;

public class PlacesAutoCompleteAdapter extends ArrayAdapter<MemberListBean> implements Filterable {
	public ArrayList<MemberListBean> getResultList() {
		return resultList;
	}
	public void setResultList(ArrayList<MemberListBean> resultList) {
		this.resultList = resultList;
	}

	private static final String LOG_TAG = "ExampleApp";
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";
	//private static final String API_KEY = "AIzaSyDx7FsIHyizKEghTWrCoh10AFheZ38NjUk";
	

	private ArrayList<MemberListBean> resultList;
	private ArrayList<MemberListBean> mainList;
	ViewHolder mHolder;
	LayoutInflater inflator;
	View view;
	Context mContext;
	public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
		
		super(context, textViewResourceId);
		mContext = context;
		inflator = LayoutInflater.from(context);
		
	}
	/*public PlacesAutoCompleteAdapter(Context context) {
		// TODO Auto-generated constructor stub
	}
*/
	public PlacesAutoCompleteAdapter(Context context, int textViewResourceId, ArrayList<MemberListBean> mlist) {
		super(context, textViewResourceId);
		mContext = context;
		inflator = LayoutInflater.from(context);
		resultList = mlist; 
		mainList = mlist;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		view = convertView;
		if (view == null) {
			view = inflator.inflate(R.layout.place_item, parent, false);
			mHolder = new ViewHolder();
			mHolder.name = (TextView) view.findViewById(R.id.place_item_tv);
			view.setTag(mHolder);
		//	mHolder.name.setTypeface(CustomTypeFace.GetTypeFace(mContext));
		} else {
			mHolder = (ViewHolder) view.getTag();
			
		}
		if(mHolder.name!=null)
		{
			mHolder.name.setText(resultList.get(position).getUser_first_name()+" "+resultList.get(position).getUser_last_name());
			
		}
	mHolder.name.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				  // TODO Auto-generated method stub
			    InputMethodManager keyboard = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			    keyboard.showSoftInput(mHolder.name, 0);
				return false;
			}
		});
		
		return view;
	}

	@Override
	public int getCount() {
		return resultList.size();
	}

	@Override
	public MemberListBean getItem(int index) {
		return resultList.get(index);
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				if (constraint != null) {
					// Retrieve the autocomplete results.
					resultList =  autocomplete(constraint.toString());

					// Assign the data to the FilterResults
					filterResults.values = resultList;
					filterResults.count = resultList.size();
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}
		};
		return filter;
	}

	private ArrayList<MemberListBean> autocomplete(String input) {
		//Toast.makeText(getContext(), SessionManager.getFirstName(mContext)+" "+SessionManager.getLastName(mContext), 1).show();
		ArrayList<MemberListBean> result = new ArrayList<MemberListBean>();;
		for(int i = 0 ;i<mainList.size();i++){
			String name = mainList.get(i).getUser_first_name()+" "+mainList.get(i).getUser_last_name();
			if(name.toLowerCase().contains(input.toLowerCase())&&!name.toLowerCase().equalsIgnoreCase(SessionManager.getFirstName(mContext)+" "+SessionManager.getLastName(mContext)))
			{
				result.add(mainList.get(i));
			}
			else
			{
				
			}
		}
		return result;
	}
	
	public class ViewHolder {
		public TextView name;
	}

}