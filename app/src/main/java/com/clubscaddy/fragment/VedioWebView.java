package com.clubscaddy.fragment;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.clubscaddy.R;
import com.clubscaddy.Bean.CommentBean;
import com.clubscaddy.Bean.NewsBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.Utill;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VedioWebView extends AppCompatActivity
{
	ImageButton cancel_btn ;
	
	int position ,image_pos ;
	
	
	LinearLayout bottom_layout;
	TextView like_unlike_change_btn;
	TextView comment_btn ;
	
	ArrayList<String> path_list;
	String likeStatus;
	ArrayList<NewsBean> newsList;
	EditText comment_edittxt;
	RelativeLayout comment_relative_layout ;
	ImageButton comment_send_btn;
	Matrix matrix = new Matrix(); 
	Matrix savedMatrix = new Matrix(); 
	PointF startPoint = new PointF();
	PointF midPoint = new PointF(); 
	float oldDist = 1f; 
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
    boolean isLoadAdapter ;
    WebView vedioView ;

ProgressDialog pd ;

@SuppressLint("JavascriptInterface")
@SuppressWarnings("unchecked")
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.vedio_web_view_layout);
	getSupportActionBar().hide();
	cancel_btn = (ImageButton) findViewById(R.id.cancel_btn);
	vedioView = (WebView) findViewById(R.id.vedio_view_id);
	//Uri uri=Uri.parse(getIntent().getStringExtra("vedio_url"));
	pd = new ProgressDialog(VedioWebView.this);
	//pd.setCancelable(false);
	pd.setMessage("Loading");
	Log.e("Url ", getIntent().getStringExtra("vedio_url"));//01-04 03:07:49.042: E/Url(4158): 
	WebSettings webSettings = vedioView.getSettings();

	vedioView.addJavascriptInterface(new WebAppInterface(getApplicationContext()), "Android");
	 webSettings.setJavaScriptEnabled(true);
	 vedioView.requestFocusFromTouch();
	vedioView.setWebViewClient(new WebViewClient()
			{

				@Override
				public void onPageFinished(WebView view, String url) {
					// TODO Auto-generated method stub
					super.onPageFinished(view, url);
					pd.show();
				}

				@Override
				public void onPageStarted(WebView view, String url, Bitmap favicon) {
					// TODO Auto-generated method stub
					super.onPageStarted(view, url, favicon);
					pd.dismiss();
				}
		
			});

	//01-04 03:02:29.441: E/Url(3431): https://m.youtube.com/watch?v=UI72Wu6o_EE

	
	//10-30 18:18:33.408: E/Base  URL(1779): https://m.youtube.com/embed/UI72Wu6o_EE
//https://www.youtube.com/embed/xfLhdkkGUKo
	
	//10-30 16:59:33.510: E/Base  URL(6790): https://www.youtube.com/watch/?v=q5-rZRKFQJA

	
	
	
	//Toast.makeText(mContext, "URL "+ newsList.get(position).getNews_feed_attach_url(), 1).show();
	
	Log.e("Base  URL",  getIntent().getStringExtra("vedio_url").replace("watch?v=", "embed/").replace("//m.", "//www."));

	vedioView.loadUrl(getIntent().getStringExtra("vedio_url").replace("watch?v=", "embed/").replace("//m.", "//www."));
	
	
	
	bottom_layout = (LinearLayout) findViewById(R.id.bottom_layout);
	
	newsList = AppConstants.newsList;
	try
	{
		position =Integer.parseInt(getIntent().getStringExtra("pos"));	
	}
	catch(Exception e)
	{
		
	}
	
	comment_relative_layout = (RelativeLayout) findViewById(R.id.comment_relative_layout);
	
	
	
	comment_send_btn = (ImageButton) findViewById(R.id.comment_send_btn);
	
	
	
	like_unlike_change_btn = (TextView) bottom_layout.findViewById(R.id.like_unlike_change_btn);
	
	
	comment_edittxt = (EditText) bottom_layout.findViewById(R.id.comment_edittxt);
	
	
	
	
	comment_btn = (TextView) bottom_layout.findViewById(R.id.comment_btn);
	
	
	
	like_unlike_change_btn.setOnClickListener(onclick);
	comment_btn.setOnClickListener(onclick);
	
	
	comment_send_btn.setOnClickListener(onclick);
	
	 path_list = (ArrayList<String>) getIntent().getSerializableExtra("path_list");
	 //Toast.makeText(getApplicationContext(), position+" vedio url "+newsList.get(position).getVedioUrl(), 1).show();
	if(isLoadAdapter == false)
	{
		
		
		
		isLoadAdapter = true;
	}
		
	try
	{
		if(getIntent().getStringExtra("show_bottom").equalsIgnoreCase("1"))
		{
			bottom_layout.setVisibility(View.VISIBLE);	
			//Toast.makeText(getApplicationContext(), "status "+newsList.get(position).getMyLikeStatus(), 1).show();
			
			if(newsList.get(position).getMyLikeStatus().equals("1"))
			{
				like_unlike_change_btn.setTextColor(Color.parseColor("#536CB5"));
				like_unlike_change_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.active_like_icon, 0, 0, 0);
			}
			
			
		}
		String filename=path_list.get(image_pos)  .substring(path_list.get(image_pos).lastIndexOf("/")+1);
		
		
		

		 
		 
		 
			
	}
	catch(Exception e)
	{
		
	}
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
	cancel_btn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	});
	
}
@Override
protected void onStart() {
	// TODO Auto-generated method stub
	super.onStart();
	//Toast.makeText(getApplicationContext(), "Start", 1).show();
}
@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	//Toast.makeText(getApplicationContext(), "onResume", 1).show();
}
int count;
OnClickListener onclick = new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.like_unlike_change_btn:
			
			Utill.showProgress(getApplicationContext());
			  count = Integer.parseInt(newsList.get(position).getLikeCount()) ;
			  likeStatus = newsList.get(position).getMyLikeStatus();
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("news_feed_id", newsList.get(position).getNews_feed_id());
				params.put("news_feed_like_user_id", SessionManager.getUser_id(getApplicationContext()));
				
			    if(newsList.get(position).getMyLikeStatus().equalsIgnoreCase("1"))
				{
					likeStatus = "2";	
				}
				else
				{
					likeStatus = "1";	
				}
				
				params.put("likestatus", likeStatus);

				if (Utill.isNetworkAvailable(VedioWebView.this)) {
					Utill.showProgress(getApplicationContext());

					GlobalValues.getModelManagerObj(getApplicationContext()).likeNews(params, new ModelManagerListener() {

						@Override
						public void onSuccess(String json) {
							// Utill.hideProgress();
							Log.e("like result ", json+"");
						//	getNewsFeed();
							try
							{
								JSONObject jsonObj = new JSONObject(json);
								
							//	10-23 20:25:32.215: E/result:(4090): {"status":"true","message":"Liked Successfully","liked":"1"}

								Toast.makeText(getApplicationContext(),jsonObj.getString("message"), Toast.LENGTH_LONG).show();
						
							 // if()
								//likeStatus = jsonObj.getString("liked");
							}
							catch (Exception e) {
								// TODO: handle exception
							}
							newsList.get(position).setMyLikeStatus(likeStatus);
							if(likeStatus.equalsIgnoreCase("1"))
							{
								newsList.get(position).setLikeCount((++count)+"");
								like_unlike_change_btn.setTextColor(Color.parseColor("#536CB5") );
								like_unlike_change_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.active_like_icon, 0, 0, 0);
								
							}
							else
							{
								newsList.get(position).setLikeCount((--count)+"");
								like_unlike_change_btn.setTextColor(Color.parseColor("#ffffff") );
								like_unlike_change_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_icon, 0, 0, 0);
								
							}
								
							
							newsList.get(position).setMyLikeStatus(likeStatus);
							Utill.hideProgress();
						}

						@Override
						public void onError(String msg) {

							Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
							
							Utill.hideProgress();
						}
					});
				} else {
					
				}
			
			
			
			break;

			case R.id.comment_btn:
				comment_relative_layout.setVisibility(View.VISIBLE);
					break;
			case	R.id.comment_send_btn :
				
			//	Toast.makeText(getApplicationContext(), "Plsase enter comment in comment box", 1).show();
				
				
				if(!comment_edittxt.getText().toString().equalsIgnoreCase("")||comment_edittxt.getText().toString()!="")
				{
					comment_relative_layout.setVisibility(View.GONE);
					commentNews(comment_edittxt.getText().toString(), newsList.get(position).getNews_feed_id());
					hideSoftKeyboard();
					comment_edittxt.setText("");	
				}
				else
				{
					//Toast.makeText(getApplicationContext(), "Plsase enter comment in comment box", 1).show();
				}
				
					break;
		
		}
		
	}
};
  
public void hideSoftKeyboard() {
    if(getCurrentFocus()!=null) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
}



@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		hideSoftKeyboard();
	}



public       void commentNews(String comment, String newsId) {
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("news_feed_id", newsId);
	params.put("news_feed_user_id", SessionManager.getUser_id(getApplicationContext()));
	params.put("news_feed_comment_text", comment);

	if (Utill.isNetworkAvailable(VedioWebView.this))
	{
		Utill.showProgress(getApplicationContext());
		GlobalValues.getModelManagerObj(getApplicationContext()).commentNews(params, new ModelManagerListener() {

			@Override
			public void onSuccess(String json) {
				Utill.hideProgress();
				Log.e("result ", json+"");
				try
				{
				JSONObject jsonObj = new JSONObject(json);
				Toast.makeText(getApplicationContext(), jsonObj.getString("message"), Toast.LENGTH_LONG).show();
				
				ArrayList<CommentBean> commentList = newsList.get(position).getCommentBean();
				
				JSONArray comment_json_array = new JSONArray(jsonObj.getString("comments"));
				
				
				for (int j = 0; j < comment_json_array.length(); j++) 
				{
					CommentBean commentBean = new CommentBean();	
					commentBean.setNews_feed_comment_id(comment_json_array.getJSONObject(j).getString("news_feed_comment_id"));
					commentBean.setNews_feed_comment_text(comment_json_array.getJSONObject(j).getString("news_feed_comment_text"));
					commentBean.setUser_name(comment_json_array.getJSONObject(j).getString("user_name"));
					commentBean.setUser_profilepic(comment_json_array.getJSONObject(j).getString("user_profilepic"));
					commentList.add(commentBean);
				}	
				
				
				newsList.get(position).setCommentCount(commentList.size()+"");
				newsList.get(position).setCommentBean(commentList);
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

				Utill.hideProgress();
			}
		});
	} else {
		Utill.showNetworkError(getApplicationContext());
	}
}
public class WebAppInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }
}

	@Override
	public void onPause() {
		super.onPause();
		vedioView.onPause();
		try {
			Class.forName("android.webkit.WebView")
					.getMethod("onPause", (Class[]) null)
					.invoke(vedioView, (Object[]) null);

		} catch(ClassNotFoundException cnfe) {
		} catch(NoSuchMethodException nsme) {

		} catch(InvocationTargetException ite) {

		} catch (IllegalAccessException iae) {

		}
	}

}
