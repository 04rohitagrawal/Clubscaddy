package com.clubscaddy.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.StrikethroughSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.utility.CircleBitmapTranslation;

import com.clubscaddy.R;
import com.clubscaddy.Bean.NewsBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.custumview.InternetConnection;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.fragment.EditNewsFragment;
import com.clubscaddy.fragment.NewsFeedActivity;
import com.clubscaddy.fragment.NewsFeedActivity.onClickLike;
import com.clubscaddy.fragment.VedioWebView;


public class NewsMainAdapter extends BaseAdapter implements  OnTouchListener {

	Context mContext;
	ImageView cancel_icon;
	ArrayList<NewsBean> newsList;
	onClickLike onLikeClick;

	private int mPosition;
	private boolean isTouch = false;
	NewsFeedActivity fragment;
	AQuery aQuery;
	ProgressDialog pd;
	HttpRequest httpRequest ;
    FragmentManager fragmentManager;

	public NewsMainAdapter(Context mContext, ArrayList<NewsBean> alMemberList, onClickLike click, NewsFeedActivity fragment) {
		this.mContext = mContext;
		this.newsList = alMemberList;
		this.onLikeClick = click;

		this.fragment = fragment;
		aQuery = new AQuery(mContext);
		httpRequest = new HttpRequest(fragment.getActivity());
        fragmentManager = fragment.getChildFragmentManager();
	}

	@Override
	public int getCount() {
		return newsList.size();
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
		DisplayMetrics metrics = fragment.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;


		final ViewHolder Viewholder;


		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.news_main_view, parent, false);
			Viewholder = new ViewHolder();
			Viewholder.newsTitle = (TextView) convertView.findViewById(R.id.title);
			Viewholder.newsUrl = (TextView) convertView.findViewById(R.id.url);
			Viewholder.newslikeCount = (TextView) convertView.findViewById(R.id.like_count);

			Viewholder.news_detail_tv = (TextView) convertView.findViewById(R.id.news_detail);
			Viewholder.newscommentCount = (TextView) convertView.findViewById(R.id.comment_count);
			//Viewholder.likeBtn = (TextView) convertView.findViewById(R.id.like_btn);
			Viewholder.CommentBtn = (TextView) convertView.findViewById(R.id.comment_btn);
			Viewholder.singleCommentName = (TextView) convertView.findViewById(R.id.commentor_name);
			Viewholder.singleCommentMessage = (TextView) convertView.findViewById(R.id.comment_message);
			Viewholder.lastLIne = (View) convertView.findViewById(R.id.last_line);

			Viewholder.writeComment = (EditText) convertView.findViewById(R.id.comment);


			Viewholder.file_downloading_bar = (ProgressBar) convertView.findViewById(R.id.file_downloading_bar);
			;

			Viewholder.singlCommentView = (RelativeLayout) convertView.findViewById(R.id.single_comment_view);

			Viewholder.singleCommentorImageView = (ImageView) convertView.findViewById(R.id.commentor_image);

			Viewholder.member_info = (LinearLayout) convertView.findViewById(R.id.member_info);

			Viewholder.sendComment = (ImageView) convertView.findViewById(R.id.send_comment);

			Viewholder.delete_news_btn = (ImageButton) convertView.findViewById(R.id.delete_news_btn);

			Viewholder.edit_news_btn = (ImageButton) convertView.findViewById(R.id.edit_news_btn);
			//Viewholder.newsImage = (ImageView) convertView.findViewById(R.id.news_image);

			Viewholder.new_image_gridview = (GridView) convertView.findViewById(R.id.new_image_gridview);

			Viewholder.delete_member = (ImageView) convertView.findViewById(R.id.delete_member);




			Viewholder.news_creater_iv = (ImageView) convertView.findViewById(R.id.news_creater_iv);
			Viewholder.news_creater_name_tv = (TextView) convertView.findViewById(R.id.news_creater_name_tv);

          Viewholder.userImageLoaderProgressBar = (ProgressBar) convertView.findViewById(R.id.user_image_loader_progressbar);

			Viewholder.commenterImageLoaderProgressBar = (ProgressBar) convertView.findViewById(R.id.commenter_image_loader_progressbar);


			Viewholder.params = Viewholder.new_image_gridview.getLayoutParams();

			convertView.setTag(Viewholder);

		} else {
			Viewholder = (ViewHolder) convertView.getTag();
		}



		if (SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER)  || SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_COACH)) {
			Viewholder.delete_news_btn.setVisibility(View.INVISIBLE);
			Viewholder.edit_news_btn.setVisibility(View.INVISIBLE);
		} else {
			Viewholder.delete_news_btn.setVisibility(View.VISIBLE);
			Viewholder.edit_news_btn.setVisibility(View.VISIBLE);
		}





		try {
			//Viewholder.news_detail_tv.setText(newsList.get(position).getNews_details());

			Viewholder.writeComment.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence text, int start, int before, int count) {
					// TODO Auto-generated method stub
					newsList.get(position).setComment(text.toString());
					//Toast.makeText(fragment, "Position "+position, 1).show();
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub

				}
			});


			if (Integer.parseInt(newsList.get(position).getCommentCount()) != 0) {
				Viewholder.CommentBtn.setTextColor(Color.parseColor("#536CB5"));
				Viewholder.CommentBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.active_comment_icon, 0, 0, 0);

			}
		} catch (Exception e) {

		}

		Viewholder.edit_news_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditNewsFragment editNews = new EditNewsFragment();
				editNews.setInstanse(newsList.get(position), position, newsList, NewsMainAdapter.this);
				AppConstants.newsList = newsList;
				Intent intent = new Intent(mContext, EditNewsFragment.class);
				intent.putExtra("pos", position + "");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
			}
		});


		if (position == newsList.size() - 1) {

			JsonUtility.last_feed_id = Integer.parseInt(newsList.get(newsList.size() - 1).getNews_feed_id());


			if (JsonUtility.pre_feed_id != JsonUtility.last_feed_id)
				fragment.addNewsFeed();


		}
		final NewsBean bean = newsList.get(position);
		Viewholder.newsTitle.setText(bean.getNews_title());
		Viewholder.newsUrl.setText(bean.getNews_feed_attach_url());



		Viewholder.singleCommentorImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{




				try
				{

					DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) fragment.getActivity();
					directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(bean.getNews_user_id());
				}
				catch (Exception e)
				{

				}

			}
		});


		Viewholder.member_info.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{



				try
				{
					DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) fragment.getActivity();
					directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(bean.getNews_user_id());
				}
				catch (Exception e)
				{

				}
			}
		});




		Viewholder.news_creater_iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{



				try
				{
					DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) fragment.getActivity();
					directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(bean.getNews_user_id());
				}
				catch (Exception e)
				{

				}
			}
		});



        Viewholder.news_creater_name_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {


				try
				{
					DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) fragment.getActivity();
					directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(bean.getNews_user_id());
				}
				catch (Exception e)
				{

				}
            }
        });


	/*	Spanned text = Html.fromHtml("<HTML><p><a href=\"" + bean.getNews_details() + "\">" + bean.getNews_details() + "</a></p></HTML>");
		URLSpan[] currentSpans = text.getSpans(0, text.length(), URLSpan.class);
		SpannableString buffer = new SpannableString(text);


		//	<a href="www.fb.com">www.fb.com</a>

		String bufferarray[] = bean.getNews_details().toString().split(" ");

		String mtCommentString = "<HTML><p>";


		for (int i = 0; i < bufferarray.length; i++) {
			if (Patterns.WEB_URL.matcher(bufferarray[i]).matches()) {
				mtCommentString = mtCommentString +" "+ "<a href=\"" + bufferarray[i] + "\">" + bufferarray[i] + "</a>";

			} else {
				;
				mtCommentString = mtCommentString +" "+ bufferarray[i];
			}
		}

		mtCommentString = mtCommentString + "</p></HTML>";*/


//Html.fromHtml

		MyHtmlTagHandler myHtmlTagHandler = new MyHtmlTagHandler();
		Viewholder.news_detail_tv.setText(Html.fromHtml(bean.getNews_details_html_tag()));
		//Viewholder.news_detail_tv.setText(Html.fromHtml(mtCommentString));

		Viewholder.userImageLoaderProgressBar.setVisibility(View.VISIBLE);

		Glide.with(mContext)
				.load(bean.getUser_profilepic())
				.transform(new CircleBitmapTranslation(mContext))
				.error(R.drawable.default_img_profile)
				.placeholder(R.drawable.default_img_profile)
				.listener(new RequestListener<String, GlideDrawable>() {
					@Override
					public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource)
					{
						Viewholder.userImageLoaderProgressBar.setVisibility(View.GONE);

						return false;
					}

					@Override
					public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource)
					{
						Viewholder.userImageLoaderProgressBar.setVisibility(View.GONE);
						return false;
					}
				})
				.into(Viewholder.news_creater_iv);
		Viewholder.news_creater_name_tv.setText(bean.getUser_name());


		Viewholder.news_detail_tv.setMovementMethod(LinkMovementMethod.getInstance());


		Viewholder.newsUrl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				
				
				//01-04 03:02:29.441: E/Url(3431): https://m.youtube.com/watch?v=UI72Wu6o_EE

				
				//Toast.makeText(mContext, "URL "+ newsList.get(position).getNews_feed_attach_url(), 1).show();
				
				Log.e("URL",  newsList.get(position).getNews_feed_attach_url());
				
				
				if( newsList.get(position).getNews_feed_attach_url().toString().contains("youtube"))
				{
					Intent vedioIntent = new Intent(mContext, VedioWebView.class);
					vedioIntent.putExtra("vedio_url", newsList.get(position).getNews_feed_attach_url());
					vedioIntent.putExtra("pos", position+"");
			
					vedioIntent.putExtra("show_bottom", "1");
					AppConstants.newsList = newsList;
					fragment.startActivity(vedioIntent);
				}
				else
				{
					try
					{//: https://m.youtube.com/watch?v=UI72Wu6o_EE

						
						
						//VedioWebView


	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(newsList.get(position).getNews_feed_attach_url()));

						fragment.startActivity(browserIntent);	
					}
					catch(ActivityNotFoundException e)
					{
						Utill.showDialg("Your url not proper.Please check your url", fragment.getActivity());
					//Toast.makeText(mContext, , Toast.LENGTH_LONG).show();	
					}
					catch(Exception e)
					{
						Utill.showDialg("Your url not proper.Please check your url", fragment.getActivity());
						
						//Toast.makeText(mContext, "Your url not proper.Please check your url", Toast.LENGTH_LONG).show();	
					}	
				}
				
				
			
				
			}
		});


		try
		{
			if (Integer.parseInt(bean.getLikeCount())>1)
			{
				Viewholder.newslikeCount.setText(bean.getLikeCount() + " Likes");
			}
			else
			{
				Viewholder.newslikeCount.setText(bean.getLikeCount() + " Like");
			}


			if (Integer.parseInt(bean.getCommentCount())>1)
			{
				Viewholder.newscommentCount.setText(bean.getCommentCount() + " Comments");
			}
			else
			{
				Viewholder.newscommentCount.setText(bean.getCommentCount() + " Comment");
			}


		}
		catch (Exception e)
		{

		}
		



		isTouch = false;
		Viewholder.writeComment.setTag(position);
		Viewholder.CommentBtn.setTag(position);
		Viewholder.writeComment.setText(newsList.get(position).getEditValue());

		/*Viewholder.writeComment.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus)
				{
				final int position = v.getId();
				final EditText  caption = (EditText)v;
				Viewholder.writeComment = caption;
				}
			}
		});*/
		
		if(newsList.get(position).getNewImage().size() == 0)
		{
			Viewholder.new_image_gridview.setNumColumns(0);
			Viewholder.new_image_gridview.setNumColumns(1);

			Viewholder.params.height = 0 ;
			Viewholder.new_image_gridview.setLayoutParams(Viewholder.params);
		}
		if(newsList.get(position).getNewImage().size() == 1)
		{
			Viewholder.new_image_gridview.setNumColumns(1);

			Viewholder.params.height = width ;
			Viewholder.new_image_gridview.setLayoutParams(Viewholder.params);
			/*.height = Viewholder.new_image_gridview.getCount()*/;
		}
		if(newsList.get(position).getNewImage().size() == 2)
		{
			Viewholder.new_image_gridview.setNumColumns(2);
			//Viewholder.new_image_gridview.setNumColumns(1);

			Viewholder.params.height = width /2;
			Viewholder.params.width = width-30;
			Viewholder.new_image_gridview.setLayoutParams(Viewholder.params);
		}
		if(newsList.get(position).getNewImage().size() > 2)
		{
			width = metrics.widthPixels;
			height = metrics.heightPixels;
			Viewholder.new_image_gridview.setNumColumns(2);
			Viewholder. params = Viewholder.new_image_gridview.getLayoutParams();

			if(Viewholder.new_image_gridview.getCount()%2 == 0)
			{
				Viewholder.params.height = (width /2)*((newsList.get(position).getNews_thumb_ur().size()+1)/2);	
			}
			else
			{
				Viewholder.params.height = (width /2)*((newsList.get(position).getNews_thumb_ur().size()+1)/2);	
			}
			/*  Toast.makeText(mContext, bean.getNews_title()+" "+newsList.get(position).getNews_thumb_ur().size()+" "+width, 1).show();
			 */   	Viewholder.params.width = width-30;
			 //Toast.makeText(mContext, "Height "+(newsList.get(position).getNews_thumb_ur().size()), 1).show();
			 Viewholder.new_image_gridview.setLayoutParams(Viewholder.params);
		}
	Viewholder.adapter = 	 new ImageAdapters(fragment.getActivity(), newsList.get(position).getNews_thumb_ur(),newsList.get(position).getNewImage(),newsList,position,Viewholder.file_downloading_bar);
		Viewholder.new_image_gridview.setAdapter(Viewholder.adapter);




		Viewholder.newslikeCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			
				
			
				
				if(newsList.get(position).isSendLike())
				{
					newsList.get(position).setSendLike(false);
					notifyDataSetChanged();
					String likeStatus = "1";
					String status = bean.getMyLikeStatus();
					if (status.equalsIgnoreCase("1"))
						likeStatus = "2";
					onLikeClick.onClickLike(likeStatus, bean.getNews_feed_id(),position ,Integer.parseInt(bean.getLikeCount()),Viewholder.newslikeCount);
	
				}
				
						}
		});
		Viewholder.CommentBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				Viewholder.writeComment.requestFocus();
				InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
			}
		});

		Viewholder.sendComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String comment = newsList.get(position).getComment().toString();

				if (Utill.isStringNullOrBlank(comment)) 
        		{
					Utill.showDialg("Please Write Comment", fragment.getActivity());
					
				//	Toast.makeText(fragment, "Please Write Comment", 1).show();
        			//Utill.showToast(mContext, );
        			return;
        		}
				Viewholder.writeComment.setText("");
				//Toast.makeText(mContext,"abc "+ Viewholder.writeComment.getText().toString(), 1).show();
				onLikeClick.onClicComment(comment, newsList.get(position).getNews_feed_id() ,position,Viewholder.CommentBtn);
			}
		});




		Viewholder.writeComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				boolean handled = false;
				if (actionId == EditorInfo.IME_ACTION_SEND) {

					String comment = newsList.get(position).getComment().toString();

					if (Utill.isStringNullOrBlank(comment))
					{
						Utill.showDialg("Please Write Comment", fragment.getActivity());


					}
					else
					{
						Viewholder.writeComment.setText("");
						//Toast.makeText(mContext,"abc "+ Viewholder.writeComment.getText().toString(), 1).show();
						onLikeClick.onClicComment(comment, newsList.get(position).getNews_feed_id() ,position,Viewholder.CommentBtn);

					}


					handled = true;
				}
				return handled;
			}
		});



		Viewholder.newscommentCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onLikeClick.onCommentClick(position);
			}
		});

		if (bean.getMyLikeStatus().equalsIgnoreCase("1")) 
		{

			// set unlike Image Here
			Drawable img = mContext.getResources().getDrawable(R.drawable.active_like_icon);
			Viewholder.newslikeCount.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
			Viewholder.newslikeCount.setTextColor(Color.parseColor("#536CB5"));

		} else if (bean.getMyLikeStatus().equalsIgnoreCase("2") || bean.getMyLikeStatus().equalsIgnoreCase("0")) {
			Drawable img = mContext.getResources().getDrawable(R.drawable.like);
			Viewholder.newslikeCount.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);//a4a4a4
			Viewholder.newslikeCount.setTextColor(Color.parseColor("#a4a4a4"));
		}

		if (bean.getCommentBean().size() == 0) {
			Viewholder.singlCommentView.setVisibility(View.GONE);
			Viewholder.lastLIne.setVisibility(View.GONE);
		} else 
		{
			Viewholder.singlCommentView.setVisibility(View.VISIBLE);
			Viewholder.lastLIne.setVisibility(View.VISIBLE);
			Viewholder.singleCommentName.setText(bean.getCommentBean().get(bean.getCommentBean().size() - 1).getUser_name());
			Viewholder.singleCommentMessage.setText(bean.getCommentBean().get(bean.getCommentBean().size() - 1).getNews_feed_comment_text());

			Drawable d = mContext.getResources().getDrawable(R.drawable.default_img_profile);

			try
			{

				String imagepath = bean.getCommentBean().get(bean.getCommentBean().size() - 1 ).getUser_profilepic() ;
				Log.e("imagepath" , imagepath+"");
				Viewholder.commenterImageLoaderProgressBar.setVisibility(View.VISIBLE);
				Glide.with(fragment.getActivity())
						.load(imagepath)
						.error(R.drawable.default_img_profile)
						.placeholder(R.drawable.default_img_profile)
						.transform(new CircleBitmapTranslation(fragment.getActivity()))
						.listener(new RequestListener<String, GlideDrawable>() {
							@Override
							public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
								Viewholder.commenterImageLoaderProgressBar.setVisibility(View.GONE);

								return false;
							}

							@Override
							public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
								Viewholder.commenterImageLoaderProgressBar.setVisibility(View.GONE);
								return false;
							}
						})
						.into(Viewholder.singleCommentorImageView); ;
			}
			catch (Exception e)
			{
				Viewholder.singleCommentorImageView.setImageDrawable(d);
				Log.e("Excep" , e.getMessage()+"");
			}







			if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER) || SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_COACH))
			{

			//Toast.makeText(mContext, SessionManager.getUser_id(mContext)+" "+bean.getNews_feed_user_id(), 1).show();
			if(!SessionManager.getUser_id(mContext).equalsIgnoreCase(bean.getCommentBean().get(bean.getCommentBean().size() - 1).getNews_feed_user_id()))
			{
				Viewholder.delete_member.setVisibility(View.GONE);
			}
			else
			{
				Viewholder.delete_member.setVisibility(View.VISIBLE);
			}

			}
			else
			{
				Viewholder.delete_member.setVisibility(View.VISIBLE);
			}







			Viewholder.delete_member.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(InternetConnection.isInternetOn(mContext))
					{
					
						
								
						
						
						
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(fragment.getActivity());

						// Setting Dialog Title
						alertDialog.setTitle(SessionManager.getClubName(fragment.getActivity()));

						// Setting Dialog Message
						alertDialog.setMessage("Are you sure you want to remove this comment?");

						// Setting Icon to Dialog
						

						// Setting Positive "Yes" Button
						alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int which) {

						
								
	       pd = new ProgressDialog(fragment.getActivity());
	       pd.setMessage("Please Wait..");
	       pd.setCancelable(false);
	       pd.show();
								//Utill.showProgress(mContext);
								HashMap<String, String>params = new HashMap<String, String>();
								params.put("news_feed_comment_id", bean.getCommentBean().get(bean.getCommentBean().size() - 1).getNews_feed_comment_id());
							
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
									        newsList.get(position).getCommentBean().remove( newsList.get(position).getCommentBean().size()-1);
									        newsList.get(position).setCommentCount(String.valueOf(newsList.get(position).getCommentBean().size()));
									        notifyDataSetChanged();
							        	//	showToast(fragment, object.getString("message"), true, position);
											
										}
										else
										{
											showToast(fragment.getActivity(), object.getString("message") , false ,position);
										}
									}
									else
									{
										showToast(fragment.getActivity(), "Network error" , false ,position);
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
					Utill.showDialg(mContext.getResources().getString(R.string.check_internet_connection), fragment.getActivity());
					}
				}
			});
		
		}
		Drawable d = mContext.getResources().getDrawable(R.drawable.default_pic);
	
		
		Viewholder.delete_news_btn.setVisibility(View.GONE);
		
		

		return convertView;
	}

	static public class ViewHolder {
		TextView newsTitle, newsUrl, newslikeCount, newscommentCount, likeBtn1, CommentBtn, singleCommentName, singleCommentMessage;
		EditText writeComment;
	ImageView	delete_member ;
ImageAdapters adapter ;
		TextView news_detail_tv ;
		RelativeLayout singlCommentView;
		ImageView singleCommentorImageView, sendComment;
		View lastLIne;
		GridView new_image_gridview;
		ViewGroup.LayoutParams params ;
		ImageButton 	edit_news_btn  ;
		ImageButton 	delete_news_btn  ;
		ProgressBar file_downloading_bar;
		ImageView news_creater_iv ;
		TextView news_creater_name_tv ;
		LinearLayout member_info;

		ProgressBar userImageLoaderProgressBar ,commenterImageLoaderProgressBar;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.v("LOG_TAG", "onTouch-->");
		isTouch = true;
		mPosition = (Integer) v.getTag();
		return false;
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
        	
        	
        
        }
});

// Showing Alert Message
alertDialog.show();
	}
	public class MyHtmlTagHandler implements Html.TagHandler {

		public void handleTag(boolean opening, String tag, Editable output,
							  XMLReader xmlReader) {
			if(tag.equalsIgnoreCase("strike") || tag.equals("s")) {
				processStrike(opening, output);
			}
		}

		private void processStrike(boolean opening, Editable output) {
			int len = output.length();
			if(opening) {
				output.setSpan(new StrikethroughSpan(), len, len, Spannable.SPAN_MARK_MARK);
			} else {
				Object obj = getLast(output, StrikethroughSpan.class);
				int where = output.getSpanStart(obj);

				output.removeSpan(obj);

				if (where != len) {
					output.setSpan(new StrikethroughSpan(), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
		}

		private Object getLast(Editable text, Class kind) {
			Object[] objs = text.getSpans(0, text.length(), kind);

			if (objs.length == 0) {
				return null;
			} else {
				for(int i = objs.length;i>0;i--) {
					if(text.getSpanFlags(objs[i-1]) == Spannable.SPAN_MARK_MARK) {
						return objs[i-1];
					}
				}
				return null;
			}
		}


	}
	
	
	
	

}