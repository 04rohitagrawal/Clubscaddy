package com.clubscaddy.imageutility;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.clubscaddy.R;

import java.io.File;

/**
 * Created by administrator on 31/7/17.
 */

public class ShowImage
{
    Activity activity;
    Drawable defaultImage ;
    Drawable  errorImage ;

    public  ShowImage(Activity activity)
    {
        this.activity = activity ;
        defaultImage = activity.getResources().getDrawable(R.drawable.default_img_profile);
        errorImage = activity.getResources().getDrawable(R.drawable.default_img_profile);

    }

    public void setDefaultImage(Drawable defaultImage)
    {
      this.defaultImage = defaultImage ;
    }

    public void setErrorImage(Drawable errorImage)
    {
        this.errorImage = errorImage ;
    }


    public void showImageInCircleView(File file , ImageView imageView)
    {
        Glide.with(activity).load(file).error(defaultImage).placeholder(errorImage)
                .transform(new CircleBitmapTransformation(activity)).into(imageView);
    }

    public void showImageInCircleView(String imageUrl , ImageView imageView)
    {
        Glide.with(activity).load(imageUrl).error(defaultImage).placeholder(errorImage)
                .transform(new CircleBitmapTransformation(activity)).into(imageView);
    }



    public void showImageInSqureView(File file , ImageView imageView)
    {
        Glide.with(activity).load(file).error(defaultImage).placeholder(errorImage)
                .transform(new SqureBitmapTranslation(activity)).into(imageView);
    }

    public void showImageInSqureView(String imageUrl , ImageView imageView)
    {
        Glide.with(activity).load(imageUrl).error(defaultImage).placeholder(errorImage)
                .transform(new SqureBitmapTranslation(activity)).into(imageView);
    }

    public void showImageInFullView(File file , ImageView imageView)
    {
        Glide.with(activity).load(file).error(defaultImage).placeholder(errorImage).into(imageView);
    }
    public void showImageInFullView(String imageUrl , ImageView imageView)
    {
        Glide.with(activity).load(imageUrl).error(defaultImage).placeholder(errorImage).into(imageView);
    }




    public void showImageInRoundedCornerView(File file , ImageView imageView)
    {
        Glide.with(activity).load(file).error(defaultImage).placeholder(errorImage).transform(new RoundedCornerBitmapTranslation(activity , 10 ,10)).into(imageView);
    }
    public void showImageInRoundedCornerView(String imageUrl , ImageView imageView)
    {
        Glide.with(activity).load(imageUrl).error(defaultImage).placeholder(errorImage).transform(new RoundedCornerBitmapTranslation(activity , 10 ,10)).into(imageView);
    }


    public void showImageInResizeWidth(File file , ImageView imageView , int desireWidth)
    {
        Glide.with(activity).load(file).error(defaultImage).placeholder(errorImage).transform(new BitmapResizeTransformation(activity , desireWidth )).into(imageView);

    }

    public void showImageInResizeWidth(String imageUrl , ImageView imageView , int desireWidth)
    {
        Glide.with(activity).load(imageUrl).error(defaultImage).placeholder(errorImage).transform(new BitmapResizeTransformation(activity , desireWidth )).into(imageView);

    }

    public void showImageInResizeHeight(File file , ImageView imageView , int desireWidth)
    {
        Glide.with(activity).load(file).error(defaultImage).placeholder(errorImage).transform(new BitmapResizeTransformation(activity , desireWidth )).into(imageView);

    }

    public void showImageInResizeHeight(String imageUrl , ImageView imageView , int desireWidth)
    {
        Glide.with(activity).load(imageUrl).error(defaultImage).placeholder(errorImage).transform(new BitmapResizeTransformation(activity , desireWidth )).into(imageView);

    }
}
