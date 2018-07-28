package com.clubscaddy.imageutility;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by administrator on 31/7/17.
 */

public class BitmapResizeTransformation extends BitmapTransformation
{

    int desireWidth;

    int desireHeight ;


    public BitmapResizeTransformation(Activity activity , int desireWidth)
    {
        super(activity);
        this.desireWidth = desireWidth ;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap bitmap, int outWidth, int outHeight) {


        Bitmap resizeBitmap = null;

        Log.e("Image resolution ", bitmap.getWidth()+"   "+bitmap.getHeight());

        if (bitmap != null)
        {
            int bitmapHeight = bitmap.getHeight(); // 640
            int bitmapWidth = bitmap.getWidth(); // 480




            if (bitmapWidth > desireWidth)
            {
                desireHeight =  (int) (bitmapHeight/(bitmapWidth/desireWidth));

            }
            else
            {
                desireWidth = bitmapWidth-10;
                desireHeight =  (int) (bitmapHeight/(bitmapWidth/desireWidth));

            }



            }

        resizeBitmap = Bitmap.createScaledBitmap(bitmap, desireWidth, desireHeight, true); //reference the scaled bitmap to local bitmap
        resizeBitmap.recycle();


        return resizeBitmap;
    }

    @Override
    public String getId() {
        return "Heigth";
    }
}
/*
try
        {
        //recycle the original bitmap

        } catch (Exception e){
        e.printStackTrace();

        }*/
