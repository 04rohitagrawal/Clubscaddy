package com.clubscaddy.fragment;

import java.io.File;
import java.util.ArrayList;

import com.clubscaddy.custumview.ExtendedViewPager;
import com.squareup.picasso.Picasso;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.EventGalaryPagerAdapter;
import com.clubscaddy.Adapter.GalaryPagerAdapter;
import com.clubscaddy.Adapter.GallaryImageAdapter;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

public class EventFullImageViewActivity extends AppCompatActivity
{
	ImageButton cancel_btn ;
	ImageView image_view_id;
	ExtendedViewPager viewPager;


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
	ArrayList<String> path_list = (ArrayList<String>) getIntent().getSerializableExtra("path_list");
	EventGalaryPagerAdapter pageAdapter = new EventGalaryPagerAdapter(EventFullImageViewActivity.this, path_list , new ImageZoomChangeListener() ,viewPager);
	viewPager.setAdapter(pageAdapter);
	
	try
	{

		viewPager.setCurrentItem(Integer.parseInt(getIntent().getStringExtra("pos")));
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
ProgressDialog pd ;


@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
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
