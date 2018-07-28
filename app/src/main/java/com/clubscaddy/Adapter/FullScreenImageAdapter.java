package com.clubscaddy.Adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.custumview.Touch;

import com.clubscaddy.R;



/**
 * @author administrator
 *  for Home screen(splash) screen
 */
public class FullScreenImageAdapter extends PagerAdapter {
//public static final int Imagess[]={R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher};

Context mContext;
ArrayList<String> newsPath;
	private Activity _activity;
	//private ArrayList<String> _imagePaths;
	private LayoutInflater inflater;

	// constructor
	public FullScreenImageAdapter(Activity activity,
		Context context,ArrayList<String> path) {
		this.mContext = context;
		this._activity = activity;

	//	this.image_load.init(ImageLoaderConfiguration.createDefault(mContext));
		this.newsPath = path;
	//	this._imagePaths = imagePaths;
	}

	@Override
	public int getCount() {
		return newsPath.size();
		
		//return this._imagePaths.size();
	}

	@Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
	
	@Override
    public Object instantiateItem(ViewGroup container, int position) {
        //TouchImageView imgDisplay;
		;
   //     Button btnClose;
 
        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,
                false);

		ImageView imgDisplay;
		imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
		final ProgressBar progress   = (ProgressBar) viewLayout.findViewById(R.id.prog);
        imgDisplay.setOnTouchListener(new Touch());
        Drawable d = mContext.getResources().getDrawable(R.drawable.default_pic);

		progress.setVisibility(View.VISIBLE);

		Glide.with(mContext)
				.load(newsPath.get(position))
				.placeholder(d).error(d)
				.listener(new RequestListener<String, GlideDrawable>() {
					@Override
					public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
						progress.setVisibility(View.GONE);

						return false;
					}

					@Override
					public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
						progress.setVisibility(View.GONE);
						return false;
					}
				})
				.into(imgDisplay);

        ((ViewPager) container).addView(viewLayout);
        return viewLayout;
	}
	
	@Override
    public void destroyItem(ViewGroup container, int position, Object object) { 
		
	}
	
	

}