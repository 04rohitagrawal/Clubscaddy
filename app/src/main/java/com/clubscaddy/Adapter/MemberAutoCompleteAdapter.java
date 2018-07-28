package com.clubscaddy.Adapter;

import java.util.ArrayList;

import com.clubscaddy.utility.Utill;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.clubscaddy.R;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.utility.CircleTransform;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MemberAutoCompleteAdapter extends ArrayAdapter<MemberListBean> implements Filterable {
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
	LayoutInflater inflator;
	Context mContext;
	AutoCompleteTextView autoCompleteTextView;
	public MemberAutoCompleteAdapter(Context context, int textViewResourceId ,ArrayList<MemberListBean> mlist , AutoCompleteTextView autoCompleteTextView) {

		super(context, textViewResourceId);
		mContext = context;
		this.autoCompleteTextView = autoCompleteTextView ;
		inflator = LayoutInflater.from(context);
		resultList = mlist;
		mainList = mlist;

	}




	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder mHolder;


		if (convertView == null)
		{
			convertView = inflator.inflate(R.layout.participent_member_view, parent, false);
			mHolder = new ViewHolder();
			mHolder.name = (TextView) convertView.findViewById(R.id.commentor_name);

			mHolder.delete_member = (ImageView) convertView.findViewById(R.id.delete_member);
			mHolder.imageLoaderProgressBar = (ProgressBar) convertView.findViewById(R.id.commentor_image_progress_bar);
			//delete_member

			mHolder.member_image = (ImageView) convertView.findViewById(R.id.commentor_image);

			mHolder.delete_member = (ImageView) convertView.findViewById(R.id.delete_member);

			convertView.setTag(mHolder);
		} else
		{
			mHolder = (ViewHolder) convertView.getTag();

		}


		convertView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				try
				{
					Utill.hideKeybord((Activity) mContext , autoCompleteTextView);

				}
				catch (Exception e)
				{

				}



				return false;
			}
		});


		mHolder.delete_member.setVisibility(View.GONE);



		try
		{
			if(mHolder.name!=null)
			{
				//Toast.makeText(getContext(), resultList.get(position).getUser_first_name()+" "+resultList.get(position).getUser_last_name(), 1).show();
				mHolder.name.setText(resultList.get(position).getUser_first_name()+" "+resultList.get(position).getUser_last_name());

				try
				{
					mHolder.imageLoaderProgressBar.setVisibility(View.VISIBLE);

					Picasso.with(mContext)
							.load(resultList.get(position).getUser_profilepic())
							.placeholder(mContext. getResources().getDrawable( R.drawable.default_img_profile )) // optional
							.error(R.drawable.default_img_profile)
							.transform(new CircleTransform()).into(mHolder.member_image, new Callback() {
						@Override
						public void onSuccess() {
							mHolder.imageLoaderProgressBar.setVisibility(View.GONE);

						}

						@Override
						public void onError() {
							mHolder.imageLoaderProgressBar.setVisibility(View.GONE);

						}
					});
				}
				catch (Exception e)
				{
					mHolder.imageLoaderProgressBar.setVisibility(View.GONE);

					mHolder.member_image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_img_profile));
				}


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
		}
		catch (Exception e)
		{

		}


		return convertView;
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
				if (constraint != null)
				{
					// Retrieve the autocomplete results.
					resultList =  autocomplete(constraint.toString());

					// Assign the data to the FilterResults
					filterResults.values = resultList;
					filterResults.count = resultList.size();
				}
				else
				{
					resultList =  autocomplete("");

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

		ArrayList<MemberListBean> result = new ArrayList<MemberListBean>();;

		if(input.equalsIgnoreCase("")||input=="")
		{
			//Toast.makeText(getContext(), "Search txt "+input, 1).show();
			//Log.e("Search txt ", input)
			result.addAll(mainList)	;
			//
		}
		else
		{
				for(int i = 0 ;i<mainList.size();i++)
			{
				String name = mainList.get(i).getUser_first_name()+" "+mainList.get(i).getUser_last_name();
				if (mainList.get(i).getUser_first_name().toLowerCase().startsWith((input.toLowerCase()))||mainList.get(i).getUser_last_name().toLowerCase().startsWith((input.toLowerCase()))||name.toLowerCase().startsWith((input.toLowerCase())))
				{
					result.add(mainList.get(i));
				}
				else
				{

				}

			}
		}

		return result;
	}

	public class ViewHolder
	{
		public ImageView member_image,delete_member;

		ProgressBar imageLoaderProgressBar ;
		public TextView name;

	}

	public ArrayList<MemberListBean>getFilterList()
	{
		return resultList ;
	}


}
