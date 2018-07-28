package com.clubscaddy.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by administrator on 28/7/17.
 */

public class ImageBitmapTransformationAccordingHeight extends BitmapTransformation
{

    int desireHeight;
    Bitmap desireBitmap ;
    int scaleWidth ;
    int scaleHeight ;

    public ImageBitmapTransformationAccordingHeight(Activity activity , int desireHeight)
    {
        super(activity);
        this.desireHeight = desireHeight ;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap bitmap, int outWidth, int outHeight) {
        if (bitmap != null) {
            int bitmapHeight = bitmap.getHeight(); // 640
            int bitmapWidth = bitmap.getWidth();

            scaleHeight = desireHeight;

            float scalinfratio = (float) desireHeight / bitmapHeight;

            int desireWidth = (int) (bitmapWidth * scalinfratio);

            if (desireWidth <= bitmapWidth)
            {
                scaleWidth = desireWidth;


                desireBitmap = Bitmap.createScaledBitmap(bitmap, scaleWidth, scaleHeight, true);
                if (bitmap.isRecycled() == false) {
                    bitmap.recycle();
                }

                //  return desireBitmap;
            } else {
                return bitmap;
            }

        }
        return desireBitmap;

    }

    @Override
    public String getId() {
        return "Heigth";
    }
}
