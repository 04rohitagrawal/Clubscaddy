package com.clubscaddy.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.clubscaddy.Adapter.NewsListAdapter;
import com.clubscaddy.Bean.Likes;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.CommentAdapter;
import com.clubscaddy.Adapter.NewsMainAdapter;
import com.clubscaddy.Bean.CommentBean;
import com.clubscaddy.Bean.NewsBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;

import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.AbsListView.OnScrollListener;

public class NewsFeedActivity extends Fragment
{
	public  ImageButton  backButton, uploadNewsOrEditProfile;
public 	ImageButton logoutButton ;
public 	TextView delete_all_btn;
public  TextView actionbar_titletext;
	ListView newsListView;
	String Tag = getClass().getName();
	public static FragmentManager mFragmentManager;
	public static Fragment mFragment;
	Context mContext;
	LinearLayout linearMain;
	String picturePath = Environment.getExternalStorageDirectory().getPath();
	ArrayList<NewsBean> dropInList = new ArrayList<NewsBean>();
	public static boolean add_new_cond = false ;
	public static boolean edit_new_cond = false ;
	public  ActionBar mActionBar;
	ProgressBar load_new_progrss_bar;
    View view;
	boolean isWebservicesCalling = false ;
    private DirectorFragmentManageActivity directorFragmentManageActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news_feed,null,false);
		DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.news_feed));
		DirectorFragmentManageActivity.showBackButton();


		DirectorFragmentManageActivity.logoutButton.setImageDrawable(getResources().getDrawable(R.drawable.upload));
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		DirectorFragmentManageActivity.logoutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppConstants.newsList = newsList;
				DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.AddNewsFeedFragment_id);

			}
		});
		DirectorFragmentManageActivity.backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});

		init(view);
        initializeView(view);
        return view;
    }

	private void init(View view) {
		// TODO Auto-generated method stub

		mContext = getActivity();
		load_new_progrss_bar = (ProgressBar) view.findViewById(R.id.load_new_progrss_bar);
		/*if (DirectorFragmentManageActivity.actionbar_titletext != null) {
			DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.news_feed));
		}*/




		if (SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_DIRECTOR)== true || SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MOBILE_ADMIN)== true)
		{
			if (uploadNewsOrEditProfile != null) {
				uploadNewsOrEditProfile.setVisibility(View.VISIBLE);
				showBackButton();

				uploadNewsOrEditProfile.setVisibility(View.VISIBLE);
				uploadNewsOrEditProfile.setImageDrawable(mContext.getResources().getDrawable(R.drawable.upload));
				uploadNewsOrEditProfile.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						AppConstants.newsList = newsList;
						DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.AddNewsFeedFragment_id);
					}
				});
			}
		}
		else
		{
			/*if (DirectorFragmentManageActivity.backButton != null)
			{
				DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
				DirectorFragmentManageActivity.showBackButton();
			}*/
		}
		initializeView(view);
		setOnClicks();
		getNewsFeed();
	}
	ArrayList<NewsBean> newsList;
	NewsMainAdapter adapter;

	public void getNewsFeed()
	{
		if (Utill.isNetworkAvailable(getActivity()))
		{
			final ProgressDialog pd = new ProgressDialog(getActivity());
			pd.setMessage("Please wait....");
			pd.setCancelable(false);
			pd.show();
			JsonUtility.last_feed_id = 0 ;
			GlobalValues.getModelManagerObj(mContext).getNews(new ModelManagerListener() {

				@Override

				public void onSuccess(String json) {

					Log.e("json",json+"");

					newsList = JsonUtility.parseNews(json , dropInList);
					pd.dismiss();
					onClickLike	likeclike = new onClickLike();
					/*for(int i =0 ;i< newsList.size();i++)
					{


					}*/

					adapter = new NewsMainAdapter(mContext, newsList,likeclike ,NewsFeedActivity.this);
					newsListView.setAdapter(adapter);
					newsListView.setItemsCanFocus(true);
					//newsListView.setSelection(currentPosition);
					int x = (int) mCurrentX;
					int y = (int) mCurrentY;
					// newsListView.scrollTo(x,y);
					//createNews();
					Utill.hideProgress();
				}

				@Override
				public void onError(String msg) {
					// TODO Auto-generated method stub
					pd.dismiss();
					//Toast.makeText(getApplicationContext(), msg, 1).show();
					Utill.hideProgress();
				}
			},JsonUtility.last_feed_id);
		}
		else
		{
			ShowUserMessage.showUserMessage(getActivity() , getResources().getString(R.string.check_internet_connection));
		}
	}
	public void addNewsFeed()
	{
		if (Utill.isNetworkAvailable(getActivity())) {
			final ProgressDialog pd = new ProgressDialog(getActivity());
			pd.setMessage("Please wait....");
			pd.setCancelable(false);
			//pd.show();

			if (isWebservicesCalling == false)
			{
				load_new_progrss_bar.setVisibility(View.VISIBLE);
				isWebservicesCalling = true ;

			}
			else
			{
				return;
			}

			GlobalValues.getModelManagerObj(mContext).getNews(new ModelManagerListener() {

				@Override

				public void onSuccess(String json) {
					load_new_progrss_bar.setVisibility(View.GONE);
					newsList = JsonUtility.parseNews(json , dropInList);
					Log.e("result json", json);
					pd.dismiss();
					adapter.notifyDataSetChanged();
					isWebservicesCalling = false ;
					Utill.hideProgress();
				}

				@Override
				public void onError(String msg) {
					// TODO Auto-generated method stub
					pd.dismiss();
					isWebservicesCalling = false ;
					ShowUserMessage.showUserMessage(getActivity() , msg);
					Utill.hideProgress();
				}
			},JsonUtility.last_feed_id);
		} else {
			Utill.showNetworkError(mContext);
		}
	}

	public class onClickLike {
		public void onClickLike(String status, String id ,int pos,int count ,TextView textview) {
			likeNews(status, id,pos , count,textview);
		}

		public void onClicComment(String comment, String id ,int pos ,TextView btn) {
			commentNews(comment, id ,pos,btn);
		}

		public void onCommentClick(int position) {
			showComments(position);
		}
	}
	int count1 ;
	ProgressDialog progress;
	void likeNews(final String likeStatus, String newsId,final int pos ,  int count ,final TextView textvie1w) {
		count1 = count;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("news_feed_id", newsId);
		params.put("news_feed_like_user_id", SessionManager.getUser_id(mContext));
		params.put("likestatus", likeStatus);

		if (Utill.isNetworkAvailable(getActivity())) {
			progress = new ProgressDialog(getActivity());
			progress.setMessage("Please Wait..");
			progress.setCancelable(false);
			progress.show();

			GlobalValues.getModelManagerObj(mContext).likeNews(params, new ModelManagerListener() {

				@Override
				public void onSuccess(String json) {
					/*// Utill.hideProgress();
					//Log.e("like result ", json+"");
					Toast.makeText(getActivity(), json+" "+likeStatus, Toast.LENGTH_LONG).show();
				//	getNewsFeed();
					 */
					progress.dismiss();
					//textview.setEnabled(true);
					if(likeStatus.equalsIgnoreCase("1"))
					{
						count1 = count1 +1 ;
						newsList.get(pos).setLikeCount(count1+"");
					}

					else
					{
						count1 = count1 - 1 ;
						newsList.get(pos).setLikeCount(count1+"");
					}

					newsList.get(pos).setMyLikeStatus(likeStatus);
					//setLikeCount
					//Toast.makeText(getApplicationContext(), "Like count "+count1, Toast.LENGTH_LONG).show();
					adapter.notifyDataSetChanged();
					newsList.get(pos).setSendLike(true);
					progress.dismiss();
				}

				@Override
				public void onError(String msg) {

				//	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
					//textview.setEnabled(true);

					progress.dismiss();
				}
			});
		} else {
			newsList.get(pos).setSendLike(true);
			Utill.showNetworkError(mContext);
		}
	}

	public       void commentNews(String comment, String newsId ,final int position ,final TextView comment_btn) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("news_feed_id", newsId);
		params.put("news_feed_user_id", SessionManager.getUser_id(getActivity()));
		params.put("news_feed_comment_text", comment);
		hideSoftKeyboard();
		if (Utill.isNetworkAvailable(getActivity()))
		{
			final ProgressDialog pd = new ProgressDialog(getActivity());
			pd.setMessage("Please wait....");
			pd.setCancelable(false);
			pd.show();
			GlobalValues.getModelManagerObj(getActivity()).commentNews(params, new ModelManagerListener() {

				@Override
				public void onSuccess(String json) {
					pd.dismiss();
					Log.e("result ", json+"");
					try
					{
					JSONObject jsonObj = new JSONObject(json);
					//Utill.showDialg( jsonObj.getString("message"), NewsFeedActivity.this);
					//Toast.makeText(getApplicationContext(), jsonObj.getString("message"), Toast.LENGTH_LONG).show();

					ArrayList<CommentBean> commentList = newsList.get(position).getCommentBean();

					JSONArray comment_json_array = new JSONArray(jsonObj.getString("comments"));


					for (int j = 0; j < comment_json_array.length(); j++)
					{
						CommentBean commentBean = new CommentBean();
						commentBean.setNews_feed_comment_id(comment_json_array.getJSONObject(j).getString("news_feed_comment_id"));
						commentBean.setNews_feed_comment_text(comment_json_array.getJSONObject(j).getString("news_feed_comment_text"));
						commentBean.setUser_name(comment_json_array.getJSONObject(j).getString("user_name"));
						commentBean.setUser_profilepic(comment_json_array.getJSONObject(j).getString("user_profilepic"));
						commentBean.setNews_feed_user_id(SessionManager.getUser_id(getActivity()));
						commentBean.setNews_feed_comment_date(comment_json_array.getJSONObject(j).getString("news_feed_comment_date"));

						//
						commentList.add(commentBean);
					}


					newsList.get(position).setCommentCount(commentList.size()+"");
					newsList.get(position).setCommentBean(commentList);
					adapter.notifyDataSetChanged();

					try
					{
					if(Integer.parseInt(newsList.get(position).getCommentCount())!=0)
					{
						comment_btn.setTextColor(Color.parseColor("#536CB5"));
						comment_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.active_comment_icon, 0, 0, 0);

					}
					}
					catch(Exception e)
					{

					}
				/*



					}
					bean.setCommentBean(commentList);
					*/



					}
					catch(Exception e)
					{

					}
					/*if (writeComment != null) {
						writeComment.setText("");
						InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(writeComment.getWindowToken(), 0);
					}*/
					//getNewsFeed();
				}

				@Override
				public void onError(String msg) {
					Utill.showDialg(msg, mContext);
					pd.dismiss();
					//Utill.hideProgress();
				}
			});
		} else {
			Utill.showNetworkError(getActivity());
		}
	}

	void initializeView(View view) {
		linearMain = (LinearLayout) view.findViewById(R.id.mainView);
		newsListView = (ListView) view.findViewById(R.id.list);
	}

	void setOnClicks() {
		newsListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				mCurrentX = view.getScrollX();
				mCurrentY = view.getScrollY();
				View v = view.getChildAt(0);
				int top = (v == null) ? 0 : (v.getTop() - view.getPaddingTop());
				mCurrentY = top;
				/*				Log.e("scroll", mCurrentX + " " + mCurrentY + " " + view.getFirstVisiblePosition() + " " + view.getY() + " top " + top);*/

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void onStart() {
		super.onStart();
		Log.e(Tag, "onStart");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(Tag, "onResume");
		//Toast.makeText(getActivity(), "resume", Toast.LENGTH_LONG).show();

		if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_DIRECTOR) || SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MOBILE_ADMIN))
		{
			DirectorFragmentManageActivity.logoutButton.setVisibility(View.VISIBLE);

		}
		else
		{
			DirectorFragmentManageActivity.logoutButton.setVisibility(View.GONE);

		}

		if(add_new_cond)
		{
			JsonUtility.last_feed_id = 0 ;
			try
			{
				newsList.clear();
			}
			catch (Exception e)
			{

			}

			getNewsFeed();
			add_new_cond = false ;
		}
		if(edit_new_cond)
		{
			newsList.clear();
			JsonUtility.last_feed_id = 0 ;
			getNewsFeed();
			edit_new_cond = false ;
		}
		if(adapter != null)
			adapter.notifyDataSetChanged();
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.e(Tag, "onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.e(Tag, "onStop");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e(Tag, "onDestroy");
	}

	/*public void onBackPressed() {
		//super.onBackPressed();
		DirectorFragmentManageActivity.visibleLogout();
		DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.GONE);
	}*/
	/*@Override
	public void onDetach()
	{
		super.onDetach();
		
		Log.e(Tag, "onDetach");
	}*/

	OnClickListener addToBack = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				DirectorFragmentManageActivity.popBackStackFragment();
			} catch (Exception e) {
				ShowUserMessage.showUserMessage(mContext, e.toString());
			}
		}
	};

	float mCurrentX, mCurrentY;


	public AlertDialog reservationSelectionPopup;
	int[] colors = { 0, 0xFFFF0000, 0 }; // red for the example

	void showComments(final int index) {
		/*if (newsList == null || newsList.get(index).getCommentBean() == null || newsList.get(index).getCommentBean().size() == 1
				|| newsList.get(index).getCommentBean().size() == 0) {
			ShowUserMessage.showUserMessage(NewsFeedActivity.this, "No more comments");
			return;
		}*/
		reservationSelectionPopup = new AlertDialog.Builder(getActivity()).create();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.news_like_cmnt_diallog_layout, null);
		v.setBackgroundColor(mContext.getResources().getColor(R.color.white_color));
		final ListView like_cmnt_list_view = (ListView) v.findViewById(R.id.like_cmnt_list_view);
		like_cmnt_list_view.setVisibility(View.VISIBLE);




		TextView total_like_tv = (TextView) v.findViewById(R.id.total_like_tv);
		final	TextView header_tv = (TextView) v.findViewById(R.id.header_tv);






		try
		{
			if (Integer.parseInt(newsList.get(index).getLikeCount())>1)
			{
				total_like_tv.setText(newsList.get(index).getLikeCount() + " Likes");
			}
			else
			{
				total_like_tv.setText(newsList.get(index).getLikeCount() + " Like");
			}
		}
		catch (Exception e)
		{

		}



		final LinearLayout dialog_botton_view = (LinearLayout) v.findViewById(R.id.dialog_botton_view);


		//////////



		dialog_botton_view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getLikeList(newsList.get(index).getNews_feed_id() ,like_cmnt_list_view ) ;

				dialog_botton_view.setVisibility(View.INVISIBLE);
				header_tv.setText("Likes");

			}
		});

		ImageView cross_btn_iv = (ImageView) v.findViewById(R.id.cross_btn_iv);

		cross_btn_iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reservationSelectionPopup.cancel();
			}
		});

		////total_like_tv
		CommentAdapter cmdadapter = new CommentAdapter(mContext, newsList.get(index).getCommentBean(),NewsFeedActivity.this ,index);
		like_cmnt_list_view.setAdapter(cmdadapter);
		reservationSelectionPopup.setView(v);




		/*reservationSelectionPopup.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                    KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK)
               {

                	reservationSelectionPopup.dismiss();
                }
                return true;
            }
        });*/


		reservationSelectionPopup.show();
	}

	public void refreshAdapter(int pos , int total_comment)
	{
		 if(adapter != null)
         {
			 newsList.get(pos).setCommentCount(String.valueOf(total_comment));


			 //Toast.makeText(getApplicationContext(), "position "+pos+"  total_comment "+total_comment, 1).show();

			 if(reservationSelectionPopup != null)
			 {
				 if(total_comment ==1)
				 {
					 //reservationSelectionPopup.dismiss();
				 }
			 }
      	   adapter.notifyDataSetChanged();
         }

	}


	@SuppressLint("NewApi")
	// AlertDialog youTubeDialogue;
	// Dialog d;

	void showYouTubeVedio(NewsBean bean, final int position) {
		if (newsList == null && Utill.isStringNullOrBlank(newsList.get(position).getNews_feed_attach_url())) {
			ShowUserMessage.showUserMessage(mContext, "No Url Found");
			return;
		}
		final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar);
		LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		View vi = li.inflate(R.layout.youtube_dialogue_view, null, false);
		ImageView cancel = (ImageView) vi.findViewById(R.id.Cancel);
		WebView webview = (WebView) vi.findViewById(R.id.web);
		String mainUrl = newsList.get(position).getNews_feed_attach_url();
		if (Utill.isStringNullOrBlank(mainUrl)) {
			Utill.showToast(mContext, "No url found.");
			return;
		}
		if (mainUrl.startsWith("http://") || mainUrl.startsWith("https://")) {

		} else {
			mainUrl = "https://" + mainUrl;
		}

		// mainUrl = "http://www.youtube.com/embed/9jeFD4I6Zuk";
		// mainUrl = "https://www.youtube.com/watch?v=nOu1v6W8hLg";

		String main = mainUrl.toLowerCase();
		String watch = "watch?v=";
		String youtube = "www.youtube.com".toLowerCase();
		if (main.contains(watch)) {
			mainUrl = mainUrl.replace("watch?v=", "embed/");
			// mainUrl = mainUrl+"/";
		}
		String html = "<iframe class=\"youtube-player\" style=\"width: 100%; height: 95%;\" id=\"ytplayer\" type=\"text/html\" " + "src=\"" + mainUrl
				+ "\"framespacing=\"0\">\n" + "</iframe>\n";

		// perfect working
		/*
		 * String html =
		 * "<iframe class=\"youtube-player\" style=\"border: 0; width: 100%; height: 95%; padding:0px; margin:0px\" id=\"ytplayer\" type=\"text/html\" "
		 * + "src=\""+mainUrl + "\" frameborder=\"5\">\n" + "</iframe>\n";
		 */
		webview.setEnabled(true);
		webview.getSettings().setPluginState(PluginState.ON);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setMediaPlaybackRequiresUserGesture(true);
		webview.setWebChromeClient(new WebChromeClient());
		webview.getSettings().setPluginState(WebSettings.PluginState.ON);
		webview.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
		webview.setWebViewClient(new WebViewClient());
		webview.getSettings().setJavaScriptEnabled(true);
		dialog.setContentView(vi);
		dialog.show();
		if (main.contains(youtube)) {
			webview.loadData(html, "text/html", null);
		} else {
			webview.loadUrl(mainUrl);
		}

		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		TextView likeBtn, commentBtn;
		likeBtn = (TextView) vi.findViewById(R.id.like_btn);
		commentBtn = (TextView) vi.findViewById(R.id.comment_btn);
		likeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				View vi = viewList.get(position);
				vi.findViewById(R.id.like_btn).performClick();
			}
		});
		commentBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				View vi = viewList.get(position);
				vi.findViewById(R.id.comment_btn).performClick();

			}
		});
		if (bean.getMyLikeStatus().equalsIgnoreCase("1")) {

			// set unlike Image Here
			Drawable img = mContext.getResources().getDrawable(R.drawable.active_like_icon);
			likeBtn.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
		} else if (bean.getMyLikeStatus().equalsIgnoreCase("2") || bean.getMyLikeStatus().equalsIgnoreCase("0")) {
			Drawable img = mContext.getResources().getDrawable(R.drawable.like_icon);
			likeBtn.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
		}

	}



	/*void createNews() {
		viewList = new ArrayList<View>();
		this.opt = UniversalImageLoaderHelper.setImageOptions();
		this.image_load = ImageLoader.getInstance();
		this.image_load.init(ImageLoaderConfiguration.createDefault(mContext));
		linearMain.removeAllViews();
		for (int i = 0; i < newsList.size(); i++) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View convertView = inflater.inflate(R.layout.news_main_view, null);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 20, 0, 0);
			final ViewHolder Viewholder = new ViewHolder();
			Viewholder.newsTitle = (TextView) convertView.findViewById(R.id.title);
			Viewholder.newsDetail = (TextView) convertView.findViewById(R.id.news_detail);
			Viewholder.prog = (ProgressBar) convertView.findViewById(R.id.prog);
			Viewholder.newsUrl = (TextView) convertView.findViewById(R.id.url);
			//Viewholder.imageFrame = (FrameLayout) convertView.findViewById(R.id.imageframe);
			Viewholder.newslikeCount = (TextView) convertView.findViewById(R.id.like_count);
			Viewholder.newscommentCount = (TextView) convertView.findViewById(R.id.comment_count);
			Viewholder.likeBtn = (TextView) convertView.findViewById(R.id.like_btn);
			Viewholder.CommentBtn = (TextView) convertView.findViewById(R.id.comment_btn);
			Viewholder.singleCommentName = (TextView) convertView.findViewById(R.id.commentor_name);
			Viewholder.singleCommentMessage = (TextView) convertView.findViewById(R.id.comment_message);
			Viewholder.lastLIne = (View) convertView.findViewById(R.id.last_line);

			Viewholder.singlCommentView = (LinearLayout) convertView.findViewById(R.id.single_comment_view);
			Viewholder.singleCommentorImageView = (ImageView) convertView.findViewById(R.id.commentor_image);
			Viewholder.writeComment = (EditText) convertView.findViewById(R.id.comment);
			Viewholder.sendComment = (ImageView) convertView.findViewById(R.id.send_comment);

			convertView.setLayoutParams(params);
			final NewsBean bean = newsList.get(i);
			Viewholder.newsTitle.setText(bean.getNews_title());
			Viewholder.newsUrl.setText(bean.getNews_feed_attach_url());
			Viewholder.newslikeCount.setText(bean.getLikeCount() + " Likes");
			Viewholder.newscommentCount.setText(bean.getCommentCount() + " Comments");
			if (Utill.isStringNullOrBlank(bean.getNews_details())) {
				Viewholder.newsDetail.setVisibility(View.GONE);
			} else {
				Viewholder.newsDetail.setText(bean.getNews_details());
				Viewholder.newsDetail.setVisibility(View.VISIBLE);
			}
			Viewholder.writeComment.setText(newsList.get(i).getEditValue());

			Viewholder.likeBtn.setTag(i);
			Viewholder.CommentBtn.setTag(i);
			Viewholder.sendComment.setTag(i);
			Viewholder.newscommentCount.setTag(i);
			Viewholder.writeComment.setTag(i);
			Viewholder.newscommentCount.setTag(i);
			Viewholder.newsUrl.setTag(i);
			Viewholder.newsImage.setTag(i);
			Viewholder.newsImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int id = Integer.parseInt(v.getTag().toString());
					NewsBean bean = newsList.get(id);
					if (!Utill.isStringNullOrBlank(bean.getVedioUrl())) {


						Utill.showToast(mContext, "DownLoading..."+getFileName(bean.getVedioUrl()));

						String destinationFilename = android.os.Environment.getExternalStorageDirectory().getPath() + File.separatorChar + "abc.3gp";
						new DownloadImageAsyTask(mContext, bean,getFileName(bean.getVedioUrl()),new VideoPlayerListener(),Viewholder.prog,id).execute();
						//new DownLoadManager().startDownLoad(mContext, bean.getVedioUrl(), destinationFilename);
						// downloadFile(bean.getVedioUrl());
					} else {
						showImage(bean, id);
					}

				}
			});

			Viewholder.newsUrl.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int id = Integer.parseInt(v.getTag().toString());
					String url = newsList.get(id).getNews_feed_attach_url();
					showYouTubeVedio(bean, id);
					// Utill.openYouTube(mContext, ""+url);

				}
			});
			Viewholder.likeBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int id = Integer.parseInt(v.getTag().toString());
					String likeStatus = "1";
					String status = newsList.get(id).getMyLikeStatus();
					if (status.equalsIgnoreCase("1"))
						likeStatus = "2";
					likeNews(likeStatus, bean.getNews_feed_id(),0,0);
				}
			});
			Viewholder.CommentBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int id = Integer.parseInt(v.getTag().toString());
					Viewholder.writeComment.requestFocus();
					InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

				}
			});

			Viewholder.sendComment.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int id = Integer.parseInt(v.getTag().toString());
					LinearLayout linear = (LinearLayout) v.getParent();
					EditText et = (EditText) linear.getChildAt(0);
					String comment = et.getText().toString().trim();
					if (Utill.isStringNullOrBlank(comment)) {
						Utill.showToast(mContext, "Please Write Comment");
						return;
					}
					commentNews(comment, newsList.get(id).getNews_feed_id());
				}
			});

			Viewholder.newscommentCount.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int id = Integer.parseInt(v.getTag().toString());
					showComments(id);
				}
			});

			if (bean.getMyLikeStatus().equalsIgnoreCase("1")) {

				// set unlike Image Here
				Drawable img = mContext.getResources().getDrawable(R.drawable.active_like_icon);
				Viewholder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
			} else if (bean.getMyLikeStatus().equalsIgnoreCase("2") || bean.getMyLikeStatus().equalsIgnoreCase("0")) {
				Drawable img = mContext.getResources().getDrawable(R.drawable.like_icon);
				Viewholder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
			}

			if (bean.getCommentBean().size() == 0) {
				Viewholder.singlCommentView.setVisibility(View.GONE);
				Viewholder.lastLIne.setVisibility(View.GONE);
			} else {
				Viewholder.singlCommentView.setVisibility(View.VISIBLE);
				Viewholder.lastLIne.setVisibility(View.VISIBLE);
				Viewholder.singleCommentName.setText(bean.getCommentBean().get(bean.getCommentBean().size() - 1).getUser_name());
				Viewholder.singleCommentMessage.setText(bean.getCommentBean().get(bean.getCommentBean().size() - 1).getNews_feed_comment_text());

				Drawable d = mContext.getResources().getDrawable(R.drawable.logo_profile);
				image_load.displayImage(bean.getCommentBean().get(bean.getCommentBean().size() - 1).getUser_profilepic(),
						Viewholder.singleCommentorImageView, opt, new CustomImageLoader(null, mContext, d, false));
			}
			if (bean.getNewImage() != null && bean.getNewImage().size() > 0) {
				String firstImageUrl = bean.getNewImage().get(0);
				if (Utill.isStringNullOrBlank(firstImageUrl)) {
					Viewholder.newsImage.setVisibility(View.GONE);
					//Viewholder.imageFrame.setVisibility(View.GONE);

				} else {
					Viewholder.newsImage.setVisibility(View.VISIBLE);
					//	Viewholder.imageFrame.setVisibility(View.VISIBLE);
					Drawable d = mContext.getResources().getDrawable(R.drawable.default_pic);
					if (bean.getNewImage() != null && bean.getNewImage().size() > 0)
						image_load.displayImage(bean.getNewImage().get(0), Viewholder.newsImage, opt, new CustomImageLoader(Viewholder.prog,
								mContext, d, false));
					else {
						Viewholder.newsImage.setVisibility(View.GONE);
						//	Viewholder.imageFrame.setVisibility(View.GONE);
					}
				}
			} else {
				Viewholder.newsImage.setVisibility(View.GONE);
				//Viewholder.imageFrame.setVisibility(View.GONE);
			}

			linearMain.addView(convertView);
			viewList.add(convertView);

		}
		setDataToNews();
	}
*/
	int totalSize = 0;
	int downloadedSize = 0;

	void downloadFile(String dwnload_file_path) {

		try {
			URL url = new URL(dwnload_file_path);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);

			// connect
			urlConnection.connect();

			// set the path where we want to save the file
			File SDCardRoot = Environment.getExternalStorageDirectory();
			// create a new file, to save the <span id="IL_AD2"
			// class="IL_AD">downloaded</span> file
			File file = new File(SDCardRoot, "downloaded_file.png");

			FileOutputStream fileOutput = new FileOutputStream(file);

			// Stream used for reading the data from the internet
			InputStream inputStream = urlConnection.getInputStream();

			// this is the total size of the file which we are downloading
			totalSize = urlConnection.getContentLength();

			((Activity) mContext).runOnUiThread(new Runnable() {
				public void run() {
					// pb.setMax(totalSize);
				}
			});

			// create a buffer...
			byte[] buffer = new byte[1024];
			int bufferLength = 0;

			while ((bufferLength = inputStream.read(buffer)) > 0) {
				fileOutput.write(buffer, 0, bufferLength);
				downloadedSize += bufferLength;
				// update the progressbar //
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						// pb.setProgress(downloadedSize);
						float per = ((float) downloadedSize / totalSize) * 100;
						// cur_val.setText("Downloaded " + downloadedSize +
						// "KB / " + totalSize + "KB (" + (int)per + "%)" );
					}
				});
			}
			// close the output stream when complete //
			fileOutput.close();
			((Activity) mContext).runOnUiThread(new Runnable() {
				public void run() {
					// pb.dismiss(); // if you want close it..
				}
			});

		} catch (final MalformedURLException e) {
			// showError("Error : MalformedURLException " + e);
			e.printStackTrace();
		} catch (final IOException e) {
			// showError("Error : IOException " + e);
			e.printStackTrace();
		} catch (final Exception e) {
			// showError("Error : Please <span id="IL_AD11" class="IL_AD">check your internet connection</span> "
			// + e);
		}
	}





	EditText writeComment;




	ArrayList<View> viewList;

	public class ShopkeeperShoppingTimeAttachment {
		public void onSucess(String msg) {

		}
	}

	public class CustomerShoppingTimeAttachment {
		public void onSucess(String msg) {

		}
	}

	public class ShopAdptDownloadImageListener {
		public void onSucess() {

		}
	}

	public class CustomerDownloadImageListener {
		public void onSucess() {

		}
	}

	public class PlayAudioListener {
		public void onSucess(String msg) {

		}
	}

	public class VideoPlayerListener {
		public void onSucess(String vedioUrl,NewsBean bean) {
			Utill.showToast(mContext, "Play Vedio"+vedioUrl);
			showVedio(bean, 0,vedioUrl);
		}
	}



	void showVedio(NewsBean bean, final int position,String filePath) {
		try {
			final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar);
			LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			View vi = li.inflate(R.layout.news_vedio_popup, null, false);
			dialog.setContentView(vi);
			// ImageView full_imgView = (ImageView)
			// vi.findViewById(R.id.full_imgView);
			// full_imgView.setVisibility(View.GONE);
			TextView backButton = (TextView) vi.findViewById(R.id.back_button);
			LinearLayout backLinear = (LinearLayout) vi.findViewById(R.id.backLinear);

			writeComment = (EditText) vi.findViewById(R.id.comment);
			final ImageView sendComment = (ImageView) vi.findViewById(R.id.send_comment);
			final LinearLayout commentLayout = (LinearLayout) vi.findViewById(R.id.comment_view);
			VideoView videoView = (VideoView) vi.findViewById(R.id.news_vedio);
			videoView.setVideoPath(filePath);
			MediaController ctrl = new MediaController(mContext);
			ctrl.requestFocus();
			ctrl.setVisibility(View.VISIBLE);
			videoView.setMediaController(ctrl);
			ctrl.show(0);
			videoView.start();

			sendComment.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String comment = writeComment.getText().toString().trim();
					if (Utill.isStringNullOrBlank(comment)) {
						Utill.showToast(mContext, "Please Write Comment");
						return;
					}
					//.NewsFeedActivity.commentNews(comment, newsList.get(position).getNews_feed_id());
				}
			});


			backButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			backLinear.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			final TextView likeBtn, commentBtn;
			likeBtn = (TextView) vi.findViewById(R.id.like_btn);
			commentBtn = (TextView) vi.findViewById(R.id.comment_btn);
			likeBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					int id = position;
					String likeStatus = "1";
					String status = newsList.get(id).getMyLikeStatus();
					if (status.equalsIgnoreCase("1")) {
						likeStatus = "2";
						Drawable img = mContext.getResources().getDrawable(R.drawable.like_icon);
						likeBtn.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
					} else {
						Drawable img = mContext.getResources().getDrawable(R.drawable.active_like_icon);
						likeBtn.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
					}
					likeNews(likeStatus, newsList.get(id).getNews_feed_id(),0,0,likeBtn);

					/*
					 * dialog.dismiss(); View vi = viewList.get(position);
					 * vi.findViewById(R.id.like_btn).performClick();
					 */}
			});
			commentBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (commentLayout.getVisibility() == View.VISIBLE) {
						commentLayout.setVisibility(View.GONE);
						InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(writeComment.getWindowToken(), 0);
						return;
					}

					commentLayout.setVisibility(View.VISIBLE);
					writeComment.requestFocus();
					InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

					/*
					 * View vi = viewList.get(position);
					 * vi.findViewById(R.id.comment).requestFocus();
					 * vi.findViewById(R.id.comment).performClick();
					 * dialog.dismiss();
					 */

				}
			});
			if (bean.getMyLikeStatus().equalsIgnoreCase("1")) {

				// set unlike Image Here
				Drawable img = mContext.getResources().getDrawable(R.drawable.active_like_icon);
				likeBtn.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
			} else if (bean.getMyLikeStatus().equalsIgnoreCase("2") || bean.getMyLikeStatus().equalsIgnoreCase("0")) {
				Drawable img = mContext.getResources().getDrawable(R.drawable.like_icon);
				likeBtn.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
			}

			// full_imgView.setOnTouchListener(new com..Data.Touch());

			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}


	}



	public  void showBackButton() {
		if (backButton != null) {
			backButton.setVisibility(View.VISIBLE);
			uploadNewsOrEditProfile.setVisibility(View.GONE);
		}
	}


	public void hideSoftKeyboard() {
	    if(view!=null) {
	        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
	        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	    }
	}
      ArrayList<Likes>likesArrayList ;
	HttpRequest httpRequest ;

	public void getLikeList(String news_feed_id ,final ListView  like_cmnt_list_view)
	{

		HashMap<String ,Object> params = new HashMap<String ,Object>();
		params.put("news_feed_id",news_feed_id);
		likesArrayList = new ArrayList<>();
		httpRequest = new HttpRequest(getActivity());
		httpRequest.getResponse(getActivity(), WebService.news_like_list, params, new OnServerRespondingListener(getActivity()) {
			@Override
			public void onSuccess(JSONObject jsonObject)
			{

				try
				{
                 if (jsonObject.getBoolean("status"))
				 {
					 JSONArray newsListJsonArray = jsonObject.getJSONArray("Response");
					 for (int i =0 ;i < newsListJsonArray.length() ;i++)
					 {
					    JSONObject    newsListJsonArrayItem = newsListJsonArray.getJSONObject(i);
						 Likes likes = new Likes();
						 likes.setNews_feed_id(newsListJsonArrayItem.getInt("news_feed_id"));
						 likes.setNews_feed_like_id(newsListJsonArrayItem.getInt("news_feed_like_id"));
						 likes.setNews_feed_like_user_id(newsListJsonArrayItem.getInt("news_feed_like_user_id"));
						 likes.setUser_name(newsListJsonArrayItem.getString("user_name"));
						 likes.setUser_profilepic(newsListJsonArrayItem.getString("user_profilepic"));
						 likesArrayList.add(likes);
					 }

					 NewsListAdapter newsListAdapter =	 new NewsListAdapter(NewsFeedActivity.this,likesArrayList);
					 like_cmnt_list_view.setAdapter(newsListAdapter);

				 }
					else
				 {
					 ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("message"));
				 }
				}
				catch (Exception e)
				{

				}

			}
		});


	}



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        directorFragmentManageActivity = (DirectorFragmentManageActivity) activity;
    }
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.e(Tag, "onDestroyView");
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		DirectorFragmentManageActivity.isbackPress = false;
	}
}
