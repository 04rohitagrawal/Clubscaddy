package com.clubscaddy.utility;
import com.squareup.picasso.Transformation;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;
public class ImageTransformation implements Transformation

{
	int desireWidth;

	public ImageTransformation( int desireWidth)
	{
		this.desireWidth = desireWidth;
	}


	@Override
	public Bitmap transform(Bitmap bitmap) {
		// TODO Auto-generated method stub
		Bitmap bitmap2 = null;
		Bitmap bitmap3;

		Log.e("Image resolution ", bitmap.getWidth()+"   "+bitmap.getHeight());

		if (bitmap != null)
		{
			boolean flag = true;
			int bitmapHeight = bitmap.getHeight(); // 640
			int bitmapWidth = bitmap.getWidth(); // 480
			Log.e("desireWidth", desireWidth+"");



			int desireHeight ;
			if(desireWidth <bitmapWidth)
			{
				desireHeight =  (int) (bitmapHeight/(bitmapWidth/desireWidth));
			}
			else
			{
				desireHeight =   bitmapHeight;
			}
			// aSCPECT rATIO IS Always WIDTH x HEIGHT rEMEMMBER 1024 x 768
			if (bitmapWidth > desireWidth)
			{
				flag = false;

				// scale According to WIDTH
				int scaledWidth = desireWidth;//400
				int scaledHeight = (scaledWidth * bitmapHeight) / bitmapWidth;//533

				try
				{
					bitmap2 = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true); //reference the scaled bitmap to local bitmap
					bitmap.recycle(); //recycle the original bitmap

				} catch (Exception e){
					e.printStackTrace();

				}
			}

			if (flag)
			{
				if (bitmapHeight > desireHeight)
				{
					// scale According to HEIGHT
					int scaledHeight = desireHeight;
					int scaledWidth = (scaledHeight * bitmapWidth) / bitmapHeight;

					try
					{
						if (scaledWidth > desireWidth)
							scaledWidth = desireWidth;

						bitmap3 = Bitmap.createScaledBitmap(bitmap2, scaledWidth, scaledHeight, true);
						bitmap2.recycle();
						return bitmap3;

					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		if(bitmap2 == null)
		{
			return bitmap;
		}

		return bitmap2;

	}

	@Override
	public String key() {
		// TODO Auto-generated method stub
		return "transformation" + " Width";
	}
}