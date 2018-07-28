package com.clubscaddy.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.clubscaddy.Interface.ImageResizingListener;
import com.clubscaddy.Interface.OnServerRespondingListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class ImageDecoder
{
	Activity activity;

	public ImageDecoder()
	{

	}
	public ImageDecoder(Activity activity)
	{
  this.activity = activity ;
	}


	public static  Bitmap decodeFile(String path) {
		//you can provide file path here 
		int orientation;
		try {
			if (path == null) {
				return null;
			}
			// decode image size 
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 0;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale++;
			}
			// decode with inSampleSize





			Bitmap bm ;

			BitmapFactory.Options options = new BitmapFactory.Options();
			try
			{
				bm = BitmapFactory.decodeFile(path );

			}
			catch (OutOfMemoryError e)
			{
				options.inSampleSize = 8;
				bm = BitmapFactory.decodeFile(path ,options);

			}
			catch (Exception e)
			{
				options.inSampleSize = 8;
				bm = BitmapFactory.decodeFile(path ,options);


			}








			return bm;
		} catch (Exception e) {
			return null;
		}
	}



	File f;
	ProgressDialog progressDialog ;

	public void imageResize(final String   selectedImagePath ,final int REQUIRED_SIZE , final ImageResizingListener imageResizingListener)
	{


		AsyncTask<Void , String ,Void> task = new AsyncTask<Void, String, Void>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog = new ProgressDialog(activity);
				progressDialog.setMessage("Image is resizing...");
				progressDialog.setCancelable(false);
				progressDialog.show();
			}

			@Override
			protected Void doInBackground(Void... params)
			{

				Random random = new Random();

				String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()
						+ "/MygalleryProfile";
				File dir = new File(file_path);

				if (!dir.exists())
					dir.mkdirs();

				f = new File(dir, "yourImageName" + random.nextInt(1000) + ".png");



				Bitmap bm ;

				BitmapFactory.Options options = new BitmapFactory.Options();
				try
				{
					bm = BitmapFactory.decodeFile(selectedImagePath );

				}
				catch (OutOfMemoryError e)
				{
					options.inSampleSize = 8;
					bm = BitmapFactory.decodeFile(selectedImagePath ,options);

				}
				catch (Exception e)
				{
					options.inSampleSize = 8;
					bm = BitmapFactory.decodeFile(selectedImagePath ,options);


				}




				int scaleWidth = bm.getWidth();
				int scaleheight = bm.getHeight() ;

				if (scaleWidth > REQUIRED_SIZE && scaleheight > REQUIRED_SIZE)
				{
					if (bm.getWidth() < bm.getHeight())
					{
						scaleWidth = REQUIRED_SIZE ;
						float ratio =  (bm.getWidth() / (float)REQUIRED_SIZE);
						scaleheight = (int) (bm.getHeight() / ratio);
					}
					else
					{
						scaleheight = REQUIRED_SIZE ;
						float ratio =  (bm.getHeight() / (float)REQUIRED_SIZE);
						scaleWidth = (int) (bm.getWidth() / ratio);
					}
				}



				Bitmap resizeBitmap = Bitmap.createScaledBitmap(bm , scaleWidth ,scaleheight , true);


					try {
						f.createNewFile();

						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						resizeBitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
						byte[] bitmapdata = bos.toByteArray();

						FileOutputStream fos = new FileOutputStream(f);
						fos.write(bitmapdata);
						fos.flush();
						fos.close();




					} catch (Exception e) {
						Log.e("image bitmap", e.toString());
					}




				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				super.onPostExecute(aVoid);
				progressDialog.dismiss();
				Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
				Log.e("Width == " , bitmap.getWidth()+"");
				Log.e("Height == " , bitmap.getHeight()+"");


				imageResizingListener.onImageResize(f.getAbsolutePath());
			}
		};
		task.execute();



	}



	public void imageResize(final ArrayList<String> selectedImagePathList , final int REQUIRED_SIZE , final ImageResizingListener imageResizingListener)
	{


		AsyncTask<Void ,  Void ,ArrayList<String>> task = new AsyncTask<Void, Void, ArrayList<String>>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog = new ProgressDialog(activity);
				progressDialog.setMessage("Image is resizing...");
				progressDialog.setCancelable(false);
				progressDialog.show();
			}

			@Override
			protected ArrayList<String> doInBackground(Void... params)
			{
             ArrayList<String>convertFilePath = new ArrayList<>();


				for (int i= 0 ; i < selectedImagePathList.size() ;i ++)
                {
					Random random = new Random();
					String selectedImagePath = selectedImagePathList.get(i);
					String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()
							+ "/MygalleryProfile";
					File dir = new File(file_path);

					if (!dir.exists())
						dir.mkdirs();

					f = new File(dir, "yourImageName" + random.nextInt(1000) + ".png");












					Bitmap bitmap ;

					BitmapFactory.Options options = new BitmapFactory.Options();
					try
					{
						bitmap = BitmapFactory.decodeFile(selectedImagePath );

					}
					catch (OutOfMemoryError e)
					{
						options.inSampleSize = 8;
						bitmap = BitmapFactory.decodeFile(selectedImagePath ,options);

					}
					catch (Exception e)
					{
						options.inSampleSize = 8;
						bitmap = BitmapFactory.decodeFile(selectedImagePath ,options);


					}







					int scaleWidth = bitmap.getWidth();
					int scaleheight = bitmap.getHeight() ;

					if (scaleWidth > REQUIRED_SIZE && scaleheight > REQUIRED_SIZE)
					{
						if (bitmap.getWidth() < bitmap.getHeight())
						{
							scaleWidth = REQUIRED_SIZE ;
							float ratio =  (bitmap.getWidth() / (float)REQUIRED_SIZE);
							scaleheight = (int) (bitmap.getHeight() / ratio);
						}
						else
						{
							scaleheight = REQUIRED_SIZE ;
							float ratio =  (bitmap.getHeight() / (float)REQUIRED_SIZE);
							scaleWidth = (int) (bitmap.getWidth() / ratio);
						}
					}



					Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap , scaleWidth ,scaleheight , true);


					try {
						f.createNewFile();

						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						resizeBitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
						byte[] bitmapdata = bos.toByteArray();

						FileOutputStream fos = new FileOutputStream(f);
						fos.write(bitmapdata);
						fos.flush();
						fos.close();

						convertFilePath.add(f.getAbsolutePath());


					} catch (Exception e) {
						Log.e("image bitmap", e.toString());
					}

                }




				return convertFilePath;
			}

			@Override
			protected void onPostExecute(ArrayList<String> convertFilePath) {
				super.onPostExecute(convertFilePath);
				progressDialog.dismiss();
				imageResizingListener.onImageResize(convertFilePath);
			}
		};
		task.execute();



	}





}
