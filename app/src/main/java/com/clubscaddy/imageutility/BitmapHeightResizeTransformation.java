package com.clubscaddy.imageutility;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by administrator on 31/7/17.
 */

public class BitmapHeightResizeTransformation extends BitmapTransformation
{

    int desireWidth;

    int desireHeight ;


    public BitmapHeightResizeTransformation(Activity activity , int desireHeight)
    {
        super(activity);
        this.desireHeight = desireHeight ;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap bitmap, int outWidth, int outHeight) {


        Bitmap resizeBitmap = null;

        Log.e("Image resolution ", bitmap.getWidth()+"   "+bitmap.getHeight());

        if (bitmap != null)
        {
            int bitmapHeight = bitmap.getHeight(); // 640
            int bitmapWidth = bitmap.getWidth(); // 480

            desireWidth = (desireHeight * bitmapWidth) / bitmapHeight;







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
