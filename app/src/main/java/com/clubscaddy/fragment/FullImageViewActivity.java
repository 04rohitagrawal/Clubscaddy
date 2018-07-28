package com.clubscaddy.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.clubscaddy.custumview.ExtendedViewPager;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.GalaryPagerAdapter;
import com.clubscaddy.Bean.CommentBean;
import com.clubscaddy.Bean.NewsBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.Utill;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FullImageViewActivity extends AppCompatActivity {
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    ImageButton cancel_btn;
    AQuery aQuery;
    int position, image_pos;
    ImageView image_view_id;
    ExtendedViewPager viewPager;
    LinearLayout bottom_layout;
    TextView like_unlike_change_btn;
    TextView comment_btn;
    TextView download_btn;
    ArrayList<String> path_list;
    String likeStatus;
    ArrayList<NewsBean> newsList;
    EditText comment_edittxt;
    RelativeLayout comment_relative_layout;
    ImageButton comment_send_btn;
    boolean isLoadAdapter;
    int count;
    ProgressDialog pd;
    OnClickListener onclick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.like_unlike_change_btn:

                    Utill.showProgress(getApplicationContext());
                    count = Integer.parseInt(newsList.get(position).getLikeCount());
                    likeStatus = newsList.get(position).getMyLikeStatus();
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("news_feed_id", newsList.get(position).getNews_feed_id());
                    params.put("news_feed_like_user_id", SessionManager.getUser_id(getApplicationContext()));

                    if (newsList.get(position).getMyLikeStatus().equalsIgnoreCase("1")) {
                        likeStatus = "2";
                    } else {
                        likeStatus = "1";
                    }

                    params.put("likestatus", likeStatus);

                    if (Utill.isNetworkAvailable(FullImageViewActivity.this)) {
                        showProgress(FullImageViewActivity.this);

                        GlobalValues.getModelManagerObj(getApplicationContext()).likeNews(params, new ModelManagerListener() {

                            @Override
                            public void onSuccess(String json) {
                                hideProgress();
                                Log.e("like result ", json + "");

                                newsList.get(position).setMyLikeStatus(likeStatus);
                                if (likeStatus.equalsIgnoreCase("1")) {
                                    newsList.get(position).setLikeCount((++count) + "");
                                    like_unlike_change_btn.setTextColor(Color.parseColor("#536CB5"));
                                    like_unlike_change_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.active_like_icon, 0, 0, 0);

                                } else {
                                    newsList.get(position).setLikeCount((--count) + "");
                                    like_unlike_change_btn.setTextColor(Color.parseColor("#ffffff"));
                                    like_unlike_change_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_icon, 0, 0, 0);

                                }


                                newsList.get(position).setMyLikeStatus(likeStatus);
                                Utill.hideProgress();
                            }

                            @Override
                            public void onError(String msg) {


                                //	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                                hideProgress();
                            }
                        });
                    } else {

                    }


                    break;
                case R.id.download_btn:
                    try {

                        String filename = path_list.get(image_pos).substring(path_list.get(image_pos).lastIndexOf("/") + 1);


                        File imageFile = new File(Environment.getExternalStorageDirectory()
                                + File.separator + getResources().getString(R.string.app_name) + File.separator + filename);
                        if (imageFile.exists()) {
                            // Toast.makeText(getApplicationContext(), "This file already exists in your sd card", 1).show();

                        } else {
                            onDownloadUrl1(path_list.get(image_pos), filename);

                        }

                    } catch (Exception e) {
                        // Toast.makeText(getApplicationContext(), e.getMessage() , 1).show();
                    }

                    break;
                case R.id.comment_btn:
                    comment_relative_layout.setVisibility(View.VISIBLE);
                    break;
                case R.id.comment_send_btn:

                    //	Toast.makeText(getApplicationContext(), "Plsase enter comment in comment box", 1).show();
                    Utill.hideKeybord(FullImageViewActivity.this);

                    if (!comment_edittxt.getText().toString().equalsIgnoreCase("") || comment_edittxt.getText().toString() != "") {
                        comment_relative_layout.setVisibility(View.GONE);
                        commentNews(comment_edittxt.getText().toString(), newsList.get(position).getNews_feed_id());

                        comment_edittxt.setText("");
                    } else {
                        Utill.showDialg("Plsase enter comment in comment box", FullImageViewActivity.this);

                        //Toast.makeText(getApplicationContext(),, 1).show();
                    }

                    break;

            }

        }
    };




     ProgressDialog progress;

    // This method will be used for showing progress bar.
    public  void showProgress(Activity mContext) {
        try {
            if (progress == null)
                progress = new ProgressDialog(mContext);
            progress.setMessage("Please Wait..");
            progress.setCancelable(false);
            progress.show();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    // This method will show for hiding progressBar.
    public  void hideProgress() {
        if (progress != null) {
            progress.dismiss();
        }
    }











    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.galary_view_pager_layout);
        image_view_id = (ImageView) findViewById(R.id.image_view_id);
        cancel_btn = (ImageButton) findViewById(R.id.cancel_btn);
        viewPager = (ExtendedViewPager) findViewById(R.id.viewPager);
        bottom_layout = (LinearLayout) findViewById(R.id.bottom_layout);
        aQuery = new AQuery(FullImageViewActivity.this);
        newsList = AppConstants.newsList;
        try {
            position = Integer.parseInt(getIntent().getStringExtra("pos"));
        } catch (Exception e) {

        }

        comment_relative_layout = (RelativeLayout) findViewById(R.id.comment_relative_layout);


        comment_send_btn = (ImageButton) findViewById(R.id.comment_send_btn);


        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int pos) {
                // TODO Auto-generated method stub

                String filename = path_list.get(pos).substring(path_list.get(pos).lastIndexOf("/") + 1);

                image_pos = pos;

                File imageFile = new File(Environment.getExternalStorageDirectory()
                        + File.separator + getResources().getString(R.string.app_name) + File.separator + filename);
                //Toast.makeText(getApplicationContext(), "imageFile.exists()  "+imageFile.exists(), 1).show();
                if (imageFile.exists()) {
                    download_btn.setTextColor(Color.parseColor("#536CB5"));

                } else {
                    download_btn.setTextColor(Color.parseColor("#ffffff"));

                }
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int pos) {
                // TODO Auto-generated method stub


            }
        });


        like_unlike_change_btn = (TextView) bottom_layout.findViewById(R.id.like_unlike_change_btn);


        comment_edittxt = (EditText) bottom_layout.findViewById(R.id.comment_edittxt);


        download_btn = (TextView) bottom_layout.findViewById(R.id.download_btn);
        comment_btn = (TextView) bottom_layout.findViewById(R.id.comment_btn);


        like_unlike_change_btn.setOnClickListener(onclick);
        comment_btn.setOnClickListener(onclick);
        download_btn.setOnClickListener(onclick);

        comment_send_btn.setOnClickListener(onclick);

        path_list = (ArrayList<String>) getIntent().getSerializableExtra("path_list");
        //Toast.makeText(getApplicationContext(), position+" vedio url "+newsList.get(position).getVedioUrl(), 1).show();
        GalaryPagerAdapter pageAdapter = new GalaryPagerAdapter(FullImageViewActivity.this, path_list, newsList.get(position).getVedioUrl(), bottom_layout, this , new ImageZoomChangeListener());
        if (isLoadAdapter == false) {
            viewPager.setAdapter(pageAdapter);

            try {
                viewPager.setCurrentItem(Integer.parseInt(getIntent().getStringExtra("image_pos")));


                image_pos = Integer.parseInt(getIntent().getStringExtra("image_pos"));
            } catch (Exception e) {

            }
            isLoadAdapter = true;
        }

        try {
            if (getIntent().getStringExtra("show_bottom").equalsIgnoreCase("1")) {
                bottom_layout.setVisibility(View.VISIBLE);
                //Toast.makeText(getApplicationContext(), "status "+newsList.get(position).getMyLikeStatus(), 1).show();

                if (newsList.get(position).getMyLikeStatus().equals("1")) {
                    like_unlike_change_btn.setTextColor(Color.parseColor("#536CB5"));
                    like_unlike_change_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.active_like_icon, 0, 0, 0);
                }


            }
            String filename = path_list.get(image_pos).substring(path_list.get(image_pos).lastIndexOf("/") + 1);


            File imageFile = new File(Environment.getExternalStorageDirectory()
                    + File.separator + getResources().getString(R.string.app_name) + File.separator + filename);
            Log.e("pathh ", imageFile.getAbsolutePath());
            if (imageFile.exists()) {
                download_btn.setTextColor(Color.parseColor("#536CB5"));

            }

            if (!(newsList.get(position).getVedioUrl() == "" || newsList.get(position).getVedioUrl().equalsIgnoreCase(""))) {
                download_btn.setTextColor(Color.parseColor("#536CB5"));
            }


        } catch (Exception e) {

        }
        try {
            if (Integer.parseInt(newsList.get(position).getCommentCount()) != 0) {
                comment_btn.setTextColor(Color.parseColor("#536CB5"));
                comment_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.active_comment_icon, 0, 0, 0);

            }
        } catch (Exception e) {

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

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void onDownloadUrl1(final String downloadurl, final String filename) {

        AsyncTask<Void, String, String> task = new AsyncTask<Void, String, String>() {


            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub


                super.onPreExecute();
                pd = new ProgressDialog(FullImageViewActivity.this);
                pd.setCancelable(false);
                pd.show();

            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub
                DownloadManager mgr = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
                Uri downloadUri = Uri.parse(downloadurl);
                DownloadManager.Request request = new DownloadManager.Request(
                        downloadUri);
                request.setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI
                                | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false).setTitle(filename)
                        .setDescription("Image is downloading.")
                        .setDestinationInExternalPublicDir("/" + getResources().getString(R.string.app_name), filename);

                mgr.enqueue(request);
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                pd.dismiss();
                Utill.showDialg("DownLoad Successfully", FullImageViewActivity.this);

                //Toast.makeText(getApplicationContext(), "DownLoad Successfully", Toast.LENGTH_LONG).show();

                File imageFile = new File(Environment.getExternalStorageDirectory()
                        + File.separator + getResources().getString(R.string.app_name) + File.separator + filename);
                //if(imageFile.exists())
                {
                    download_btn.setTextColor(Color.parseColor("#536CB5"));

                }

	/*// /storage/sdcard0/image/profile.jpg

	File imageFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator + "image"+ File.separator + "profile.jpg");
	Log.e("paths", imageFile.getPath());
 Bitmap bitemp =	BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/image/profile.jpg");
 BitmapDrawable d = new BitmapDrawable(getResources(), imageFile.getAbsolutePath());

 //image_view_id.setImageBitmap(bitemp);
*/
            }

        };

        task.execute();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        hideSoftKeyboard();
    }


    public void commentNews(String comment, String newsId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("news_feed_id", newsId);
        params.put("news_feed_user_id", SessionManager.getUser_id(getApplicationContext()));
        params.put("news_feed_comment_text", comment);

        if (Utill.isNetworkAvailable(FullImageViewActivity.this)) {
            Utill.showProgress(getApplicationContext());
            GlobalValues.getModelManagerObj(getApplicationContext()).commentNews(params, new ModelManagerListener() {

                @Override
                public void onSuccess(String json) {
                    Utill.hideProgress();

                    try {
                        JSONObject jsonObj = new JSONObject(json);
                        Utill.showDialg(jsonObj.getString("message"), FullImageViewActivity.this);

                        ArrayList<CommentBean> commentList = newsList.get(position).getCommentBean();

                        JSONArray comment_json_array = new JSONArray(jsonObj.getString("comments"));


                        for (int j = 0; j < comment_json_array.length(); j++) {
                            CommentBean commentBean = new CommentBean();
                            commentBean.setNews_feed_comment_id(comment_json_array.getJSONObject(j).getString("news_feed_comment_id"));
                            commentBean.setNews_feed_comment_text(comment_json_array.getJSONObject(j).getString("news_feed_comment_text"));
                            commentBean.setUser_name(comment_json_array.getJSONObject(j).getString("user_name"));
                            commentBean.setUser_profilepic(comment_json_array.getJSONObject(j).getString("user_profilepic"));

                            commentBean.setNews_feed_user_id(SessionManager.getUser_id(FullImageViewActivity.this));
                            commentBean.setNews_feed_comment_date(comment_json_array.getJSONObject(j).getString("news_feed_comment_date"));

                            commentList.add(commentBean);
                        }


                        newsList.get(position).setCommentCount(commentList.size() + "");
                        newsList.get(position).setCommentBean(commentList);
                        try {
                            if (Integer.parseInt(newsList.get(position).getCommentCount()) != 0) {
                                comment_btn.setTextColor(Color.parseColor("#536CB5"));
                                comment_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.active_comment_icon, 0, 0, 0);

                            }
                        } catch (Exception e) {

                        }
            /*
				
				
				
				}
				bean.setCommentBean(commentList);
				*/


                    } catch (Exception e) {

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

    public class ImageZoomChangeListener
    {
        public void getZoomLevel(float zoomLevel)
        {
            //Log.e("zoom level" ,(zoomLevel ==  1.0)+" "+zoomLevel);
            if (zoomLevel == 1.0)
            {
                viewPager.setSwipeLocked(false);
            }
            else
            {
                viewPager.setSwipeLocked(true);
            }
        }
    }
}
