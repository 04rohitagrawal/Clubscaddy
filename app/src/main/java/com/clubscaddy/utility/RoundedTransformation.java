package com.clubscaddy.utility;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.clubscaddy.utility.CropSquareTransformation;

// enables hardware accelerated rounded corners
// original idea here : http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/
public class RoundedTransformation implements com.squareup.picasso.Transformation {
    private final int radius;
    private final int margin;  // dp

    // radius is corner radii in dp
    // margin is the board in dp
    public RoundedTransformation(final int radius, final int margin) {
        this.radius = radius;
        this.margin = margin;

    }

    @Override
    public Bitmap transform(final Bitmap source) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);



      /*  int size = Math.min(sourcecopy.getWidth(), sourcecopy.getHeight());
        int x = (sourcecopy.getWidth() - size) / 2;
        int y = (sourcecopy.getHeight() - size) / 2;
        Bitmap source = Bitmap.createBitmap(sourcecopy, x, y, size, size);
        if (sourcecopy != source) {
            sourcecopy.recycle();
        }*/






        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        int width = source.getWidth();
        int height = source.getHeight();

        if (source.getWidth() < source.getHeight())
        {
            height = width ;
        }
        else
        {
            width = height ;
        }


        Bitmap output = Bitmap.createScaledBitmap(source ,width, height,true);

        Canvas canvas = new Canvas(output);

        int r = width/10 ;

        canvas.drawRoundRect(new RectF(margin, margin, width - margin, height - margin), r, r, paint);

        if (source != output) {
            source.recycle();
        }








        return output;
    }

    @Override
    public String key() {
        return "rounded()";
    }
}
