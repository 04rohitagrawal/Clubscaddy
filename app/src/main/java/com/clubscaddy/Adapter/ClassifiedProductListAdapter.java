package com.clubscaddy.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.Bean.ClassifiedItemOtherPicture;
import com.clubscaddy.imageutility.BitmapHeightResizeTransformation;
import com.clubscaddy.utility.ImageBitmapTransformationAccordingHeight;
import com.clubscaddy.utility.ImageThumbTranslation;
import com.clubscaddy.R;

import java.util.ArrayList;

/**
 * Created by administrator on 29/5/17.
 */

public class ClassifiedProductListAdapter extends BaseAdapter
{


    Activity activity ;

    ArrayList<ClassifiedItemOtherPicture> classifiedItemOtherPictureArrayList;


    //ArrayList<ProgressBar>pbarList = new ArrayList<ProgressBar>();

    public ClassifiedProductListAdapter(Activity activity , ArrayList<ClassifiedItemOtherPicture>classifiedItemOtherPictureArrayList)
    {
        this.activity = activity;

        this.classifiedItemOtherPictureArrayList = classifiedItemOtherPictureArrayList;


    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return classifiedItemOtherPictureArrayList.size();
    }

    @Override
    public ClassifiedItemOtherPicture getItem(int position) {
        // TODO Auto-generated method stub
        return classifiedItemOtherPictureArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    public class ViewHolder

    {
        ImageView image_view ;
        //ProgressBar file_downloading_bar;
        //LinearLayout spiiner_laout;
        ProgressBar imageLoaderProgressbar;
        RelativeLayout classifiedRelativeLayout;
        Thread thread;
    }
    private Handler handler = new Handler();
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
      final   ViewHolder holder ;
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.classifiend_pic_layout, null) ;
            //	holder.file_downloading_bar = (ProgressBar) convertView.findViewById(R.id.file_downloading_bar);
            holder.image_view = (ImageView) convertView.findViewById(R.id.image_view);
            holder.classifiedRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.classified_relative_layout);



            holder.imageLoaderProgressbar = (ProgressBar) convertView.findViewById(R.id.loader_progrss_bar);

            //holder.spiiner_laout = (LinearLayout) convertView.findViewById(R.id.spiiner_laout);



            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }


        RelativeLayout.LayoutParams imageparam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        holder.image_view.setLayoutParams(imageparam);




        try
        {

            holder.imageLoaderProgressbar.setVisibility(View.VISIBLE);
            Log.e("Thumb "  ,classifiedItemOtherPictureArrayList.get(position).getThumb());


            if(classifiedItemOtherPictureArrayList.size() ==1)
            {
                RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(width-40, width/2-40);
                param.setMargins(10, 10, 10, 10);
                holder.classifiedRelativeLayout.setLayoutParams(param);

            }
            if(classifiedItemOtherPictureArrayList.size() ==2)
            {
                RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(width/2-20, width/2-40);

                holder.classifiedRelativeLayout.setLayoutParams(param);

            }
            if(classifiedItemOtherPictureArrayList.size() >2)
            {
                RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(width/2-20, width/2-20);
                param.setMargins(0, 5, 0, 0);
                holder.classifiedRelativeLayout.setLayoutParams(param);

            }


            Glide.with(activity)
                    .load(classifiedItemOtherPictureArrayList.get(position).getThumb()).asBitmap()
                    .placeholder(activity. getResources().getDrawable( R.drawable.default_pic )) // optional
                    .error(R.drawable.default_pic)
                     .transform(new ImageThumbTranslation(activity , width /2))
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            holder.imageLoaderProgressbar.setVisibility(View.GONE);

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.imageLoaderProgressbar.setVisibility(View.GONE);

                            return false;
                        }
                    })
                    .into(holder.image_view) ;


           /* Glide.with(activity)
                    .load(classifiedItemOtherPictureArrayList.get(position).getThumb())
                    //.bitmapTransform(new ImageThumbTranslation(activity , width/2))
                    .placeholder(activity. getResources().getDrawable( R.drawable.default_pic )) // optional
                    .error(R.drawable.default_pic)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.imageLoaderProgressbar.setVisibility(View.GONE);

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.imageLoaderProgressbar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.image_view);*/
        }
        catch (Exception e)
        {
            holder.imageLoaderProgressbar.setVisibility(View.GONE);

        }


        return convertView;
    }

    public static String colorDecToHex(int p_red, int p_green, int p_blue)
    {
        String red = Integer.toHexString(p_red);
        String green = Integer.toHexString(p_green);
        String blue = Integer.toHexString(p_blue);

        if (red.length() == 1)
        {
            red = "0" + red;
        }
        if (green.length() == 1)
        {
            green = "0" + green;
        }
        if (blue.length() == 1)
        {
            blue = "0" + blue;
        }

        String colorHex = "#" + red + green + blue;
        return colorHex;
    }



}
