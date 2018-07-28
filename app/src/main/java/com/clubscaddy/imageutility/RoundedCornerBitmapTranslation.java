package com.clubscaddy.imageutility;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by administrator on 31/7/17.
 */

public class RoundedCornerBitmapTranslation extends BitmapTransformation {
    private final int radius;
    private final int margin;  // dp

    // radius is corner radii in dp
    // margin is the board in dp
    public RoundedCornerBitmapTranslation(Activity activity , final int radius, final int margin) {
        super(activity);
        this.radius = radius;
        this.margin = margin;
    }



    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight)
    {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);




        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        int width = source.getWidth();
        int height = source.getHeight();

        if (source.getWidth() < source.getHeight()) {
            height = width;
        } else {
            width = height;
        }


        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        int r = width / 10;

        canvas.drawRoundRect(new RectF(margin, margin, width - margin, height - margin), r, r, paint);

        if (source != output) {
            source.recycle();
        }


        return output;
    }

    @Override
    public String getId() {
        return "rounded()";
    }
}
