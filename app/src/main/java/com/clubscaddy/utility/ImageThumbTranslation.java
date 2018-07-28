package com.clubscaddy.utility;
//

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by administrator on 12/9/17.
 */

public class ImageThumbTranslation extends BitmapTransformation
{
    int desireWidth;
    public ImageThumbTranslation(Context context , int desireWidth)
    {
        super(context);
        this.desireWidth = desireWidth;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap bitmap, int outWidth, int outHeight) {


        int  modifiedBitmapWidth = bitmap.getWidth();
        int modifiedBitmapHeight = bitmap.getHeight() ;

        if (desireWidth >= bitmap.getWidth() && desireWidth >= bitmap.getHeight())
        {
            modifiedBitmapWidth = bitmap.getWidth();
            modifiedBitmapHeight = bitmap.getHeight() ;
        }
        else
        {

            if (bitmap.getWidth() >= bitmap.getHeight())
            {
                modifiedBitmapWidth =  desireWidth;
                float scaleRatio = bitmap.getWidth() / (float)desireWidth ;
                modifiedBitmapHeight = (int)(bitmap.getHeight() /scaleRatio ) ;

            }
            else
            {
                modifiedBitmapHeight =  desireWidth;
                float scaleRatio = bitmap.getHeight() / (float)desireWidth ;
                modifiedBitmapWidth = (int)(bitmap.getWidth() /scaleRatio ) ;
            }





        }
        Bitmap bitmap1 = null ;

        try
        {
            bitmap1 = Bitmap.createScaledBitmap(bitmap, modifiedBitmapWidth, modifiedBitmapHeight, true);
            if (bitmap.isRecycled())
            bitmap.recycle();
        }
        catch (Exception e)
        {

        }





        return bitmap1;

    }

    @Override
    public String getId() {
        return "Width";
    }
}
