package com.clubscaddy.Adapter;


import java.io.File;
import java.util.ArrayList;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.custumview.TouchImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.clubscaddy.R;
import com.clubscaddy.custumview.FileDir;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.fragment.FullImageViewActivity;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import android.widget.VideoView;


public class GalaryPagerAdapter extends PagerAdapter {

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    Activity mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<String> path_list;
    ProgressBar progressbar;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    PointF startPoint = new PointF();
    PointF midPoint = new PointF();
    float oldDist = 1f;
    int mode = NONE;
    String vedio_url;
    LinearLayout bottom_layout;
    FullImageViewActivity atActivity;
    String filePath;
    Bitmap photo;
    ProgressDialog pd;

    FullImageViewActivity.ImageZoomChangeListener  imageZoomChangeListener ;
    public GalaryPagerAdapter(Activity context, ArrayList<String> path_list, String vedio_url, LinearLayout bottom_layout, FullImageViewActivity atActivity , FullImageViewActivity.ImageZoomChangeListener  imageZoomChangeListener ) {
        this.path_list = path_list;
        this.vedio_url = vedio_url;
        this.imageZoomChangeListener = imageZoomChangeListener;
        mContext = context;
        this.bottom_layout = bottom_layout;
        this.atActivity = atActivity;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public static void setAutoOrientationEnabled(Context context, boolean enabled) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
    }



    @Override
    public int getCount() {
        return path_list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }




    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.fullimage_view_layout, container, false);

        final TouchImageView imageView = (TouchImageView) itemView.findViewById(R.id.image_view_id);

       final ProgressBar image_loader_progressbar = (ProgressBar) itemView.findViewById(R.id.image_loader_progressbar);



        image_loader_progressbar.setVisibility(View.VISIBLE);

        if (path_list.get(position).contains("http"))
        {
            Glide.with(mContext)
                    .load(path_list.get(position))
                    .placeholder(mContext.getResources().getDrawable(R.drawable.default_pic)) // optional
                    .error(R.drawable.default_pic)        // optional
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






        } else {
            File file = new File(path_list.get(position));
            try
            {
                Picasso.with(mContext)
                        .load(file)
                        .placeholder(mContext.getResources().getDrawable(R.drawable.default_pic)) // optional
                        .error(R.drawable.default_pic)
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                image_loader_progressbar.setVisibility(View.GONE);

                            }

                            @Override
                            public void onError() {
                                image_loader_progressbar.setVisibility(View.GONE);

                            }
                        });
            }
            catch (Exception e)
            {
                image_loader_progressbar.setVisibility(View.GONE);

            }




        }

        container.addView(itemView);

        return itemView;
    }




    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }




}
