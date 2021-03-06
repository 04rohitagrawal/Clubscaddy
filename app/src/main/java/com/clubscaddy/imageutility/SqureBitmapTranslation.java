package com.clubscaddy.imageutility;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by administrator on 31/7/17.
 */

public class SqureBitmapTranslation extends BitmapTransformation
{
    public SqureBitmapTranslation(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
        if (result != source) {
            source.recycle();
        }
        return result;

    }

    @Override
    public String getId() {
        return "square()";
    }
}
