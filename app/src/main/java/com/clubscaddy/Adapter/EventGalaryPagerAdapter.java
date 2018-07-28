package com.clubscaddy.Adapter;


import java.io.File;
import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.custumview.TouchImageView;
import com.clubscaddy.fragment.EventFullImageViewActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.clubscaddy.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class EventGalaryPagerAdapter extends PagerAdapter {
 
    Activity mContext;
    LayoutInflater mLayoutInflater;
   ArrayList<String>path_list ;
	 ;
	ViewPager viewPager;
	EventFullImageViewActivity.ImageZoomChangeListener imageZoomChangeListener;
    public EventGalaryPagerAdapter(Activity context , ArrayList<String>path_list , EventFullImageViewActivity.ImageZoomChangeListener imageZoomChangeListener , ViewPager viewPager) {
    	this.path_list = path_list;
		this.viewPager = viewPager;

		this.imageZoomChangeListener = imageZoomChangeListener;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



	@Override
    public int getCount() {
        return path_list.size();
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
 
    @SuppressWarnings("static-access")
	@Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.fullimage_view_layout, container, false);
 
        final TouchImageView imageView = (TouchImageView) itemView.findViewById(R.id.image_view_id);

		final	ProgressBar image_loader_progressbar = (ProgressBar) itemView.findViewById(R.id.image_loader_progressbar);

        if(Patterns.WEB_URL.matcher(path_list.get(position)).matches() )
		{

			image_loader_progressbar.setVisibility(View.VISIBLE);


			Glide.with(mContext)
		    .load( path_list.get(position))
		    .placeholder(mContext. getResources().getDrawable( R.drawable.default_pic )) // optional
					.listener(new RequestListener<String, GlideDrawable>() {
						@Override
						public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
							image_loader_progressbar.setVisibility(View.GONE);

							return false;
						}

						@Override
						public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
							image_loader_progressbar.setVisibility(View.GONE);
							return false;
						}
					})
		    .error(R.drawable.default_pic)         // optional
		    .into(imageView);	
		}
		else
		{

			image_loader_progressbar.setVisibility(View.VISIBLE);

	
			Uri uri = Uri.fromFile(new File(path_list.get(position)));


			Glide.with(mContext).load((path_list.get(position))).placeholder(mContext. getResources().getDrawable( R.drawable.default_pic ))
			.error(mContext. getResources().getDrawable( R.drawable.default_pic ))
					.listener(new RequestListener<String, GlideDrawable>() {
						@Override
						public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
							image_loader_progressbar.setVisibility(View.GONE);

							return false;
						}

						@Override
						public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
							image_loader_progressbar.setVisibility(View.GONE);
							return false;
						}
					})
					.into(imageView);












            	



				
		}

		imageView.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
			@Override
			public void onMove() {


				imageZoomChangeListener.getZoomLevel(imageView.getCurrentZoom());
			}
		});
		
	
        container.addView(itemView);
 
        return itemView;
    }
 
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    
    
    
    
    
    

    
    
    
    
    
}
