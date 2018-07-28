package com.clubscaddy.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.clubscaddy.DirectorFragmentManageActivity;

import com.clubscaddy.R;
import com.clubscaddy.Bean.CommentBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.CircleTransform;
import com.clubscaddy.custumview.InternetConnection;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.fragment.NewsFeedActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class CommentAdapter extends BaseAdapter {

	
	Context mContext;
	ArrayList<CommentBean> commentList;

	AQuery aQuery ;
	int clickPos ;
	ProgressDialog pd ;
	 NewsFeedActivity activity ;
	public CommentAdapter(Context mContext, ArrayList<CommentBean> alMemberList , NewsFeedActivity activity ,int clickPos) {
		this.mContext = mContext;
		this.commentList = alMemberList;

		this.activity = activity;
		aQuery = new AQuery(mContext);
		this.clickPos = clickPos;
	}

	@Override
	public int getCount() {
		return commentList.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		final ViewHolder Viewholder;
		if (convertView == null) 
		{
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.comment_item_view, parent, false);
			Viewholder = new ViewHolder();
			Viewholder.commentorName = (TextView) convertView.findViewById(R.id.commentor_name);
			Viewholder.message = (TextView) convertView.findViewById(R.id.comment_message);
			Viewholder.userImage = (ImageView) convertView.findViewById(R.id.commentor_image);
			Viewholder.delete_member = (ImageView) convertView.findViewById(R.id.delete_member);
			Viewholder.commant_time_tv = (TextView) convertView.findViewById(R.id.commant_time_tv);
			Viewholder.user_name_view = (LinearLayout) convertView.findViewById(R.id.user_name_view);
			Viewholder.imageLoaderProgressbar = (ProgressBar) convertView.findViewById(R.id.image_loader_progressbar);
			//
			convertView.setTag(Viewholder);

		} else {
			Viewholder = (ViewHolder) convertView.getTag();
		}

	final	CommentBean bean = commentList.get(position);
		Viewholder.commentorName.setText(bean.getUser_name());
		Viewholder.message.setText(bean.getNews_feed_comment_text());
		Drawable d = mContext.getResources().getDrawable(R.drawable.logo_profile);

		Viewholder.commant_time_tv.setText(bean.getNews_feed_comment_date()+"");


		Viewholder.user_name_view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {



				try
				{
					activity.reservationSelectionPopup.cancel();
				}
				catch (Exception e)
				{

				}
				DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) activity.getActivity();
				directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(bean.getNews_feed_user_id());
			}
		});


		Viewholder.userImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try
				{
					activity.reservationSelectionPopup.cancel();
				}
				catch (Exception e)
				{

				}
				DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) activity.getActivity() ;
				directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(bean.getNews_feed_user_id());
			}
		});

		if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER))
		{
			

		if(SessionManager.getUser_id(mContext).equalsIgnoreCase(bean.getNews_feed_user_id()))
		{
			Viewholder.delete_member.setVisibility(View.VISIBLE);
		}
			else
		{
			Viewholder.delete_member.setVisibility(View.GONE);
		}
			
		}
		else
		{
			Viewholder.delete_member.setVisibility(View.VISIBLE);
		}
		//
		Viewholder.delete_member.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(InternetConnection.isInternetOn(mContext))
				{
				
					
							
					
					
					
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity.getActivity());

					// Setting Dialog Title
					alertDialog.setTitle(SessionManager.getClubName(activity.getActivity()));

					// Setting Dialog Message
					alertDialog.setMessage("Are you sure you want to remove this comment?");

					// Setting Icon to Dialog
					

					// Setting Positive "Yes" Button
					alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int which) {

					
							
       pd = new ProgressDialog(activity.getActivity());
       pd.setMessage("Please Wait..");
       pd.setCancelable(false);
       pd.show();
							//Utill.showProgress(mContext);
							HashMap<String, String>params = new HashMap<String, String>();
							params.put("news_feed_comment_id", bean.getNews_feed_comment_id());
						
						      aQuery.ajax(WebService.deletecomment, params, JSONObject.class, new AjaxCallback<JSONObject>()
								{
							public void callback(String url, JSONObject object, com.androidquery.callback.AjaxStatus status)
							{
								pd.dismiss();
								
								try
								{
								if(object != null)
								{
									if(object.getString("status").equalsIgnoreCase("true"))
									{
										commentList.remove(position);
										//showToast(activity, object.getString("message"), true, position);
										notifyDataSetChanged();
										activity.refreshAdapter(clickPos ,commentList.size() );
									}

									else
									{
										showToast(activity.getActivity(), object.getString("message") , false ,position);
									}
								}
								else
								{
									showToast(activity.getActivity(), "Network error" , false ,position);
								}
								}
								catch(Exception e)
								{
									
								}
								
							//	01-04 18:06:56.941: E/object(25080): {"message":"Deleted successfully!","status":"true"}
	
								
								Log.e("object", object+"");
							};
								});
						
							
							
							
	                               }
					});

					// Setting Negative "NO" Button
					alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,	int which) {
						// Write your code here to invoke NO event
						//Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
						dialog.cancel();
						}
					});

					// Showing Alert Message
					alertDialog.show();
					
					
				}
				else
				{
				Utill.showDialg(mContext.getResources().getString(R.string.check_internet_connection), activity.getActivity());
				}
				
			}
		});


		try
		{
			Viewholder.imageLoaderProgressbar.setVisibility(View.VISIBLE);
			         Picasso.with(mContext)
					.load(bean.getUser_profilepic())
					.transform(new CircleTransform())
					.placeholder(mContext. getResources().getDrawable( R.drawable.default_img_profile )) // optional
					.error(R.drawable.default_img_profile)
					.into(Viewholder.userImage, new Callback() {
						@Override
						public void onSuccess() {
							Viewholder.imageLoaderProgressbar.setVisibility(View.GONE);

						}

						@Override
						public void onError() {
							Viewholder.imageLoaderProgressbar.setVisibility(View.GONE);

						}
					});

		}
		catch (Exception e)
		{
			Viewholder.imageLoaderProgressbar.setVisibility(View.GONE);

		}

		//image_load.displayImage(bean.getUser_profilepic(),Viewholder.userImage, opt, new CustomImageLoader(null, mContext,d,false));
		return convertView;
	}

	 public class ViewHolder {
		TextView commentorName, message ,commant_time_tv;
		ImageView userImage,delete_member;
		 LinearLayout user_name_view;

		 ProgressBar imageLoaderProgressbar ;

	}
	@SuppressWarnings("deprecation")
	public  final void showToast(Activity mContext, String msg  ,final boolean isdialogdismess , final int pos) {
		// Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
		final	AlertDialog alertDialog = new AlertDialog.Builder(
				mContext).create();

// Setting Dialog Title
alertDialog.setTitle(SessionManager.getClubName(mContext));

// Setting Dialog Message
alertDialog.setMessage(msg);

// Setting Icon to Dialog


// Setting OK Button
alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
        // Write your code here to execute after dialog closed
        	alertDialog.dismiss();
        	
        	if(isdialogdismess)
        	{


        		
        	}
        
        }
});

// Showing Alert Message
alertDialog.show();
	}
}
