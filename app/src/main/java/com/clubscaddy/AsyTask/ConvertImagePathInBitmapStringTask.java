package com.clubscaddy.AsyTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.clubscaddy.Interface.ImageToStringConveterListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;



/**
 * Created by administrator on 21/1/17.
 */

public class ConvertImagePathInBitmapStringTask extends AsyncTask<Void,Integer ,String>
{

    Activity activity ;
    ArrayList<String>imageListPath ;
    ImageToStringConveterListener listener ;
    ArrayList<EncondingStringBean>encodedStringList ;


    public ConvertImagePathInBitmapStringTask( Activity activity ,ArrayList<String>imageListPath , ImageToStringConveterListener listener)
    {
        this.activity = activity ;
        this.imageListPath = imageListPath ;
        this.listener = listener ;
        encodedStringList = new ArrayList<EncondingStringBean>();
    }


    @Override
    protected String doInBackground(Void... params) {




        for (int i= 0 ; i < imageListPath.size() ;i++)
        {

            String encodedString;

            Log.e("increment", i + "");

            float RWQUIREDSIZE = 1200 ;


           String string = imageListPath.get(i);
            if (string != null) {

                double progress =  (int)((double)i+1)*(100.0/imageListPath.size());

                publishProgress((int)progress);
                EncondingStringBean encondingStringBean = new EncondingStringBean();

                try {
                    File imagefile = new File(string);
                    encondingStringBean.setImageName(imagefile.getName().replace(" ",""));

                    Bitmap bm = BitmapFactory.decodeFile(imagefile.getAbsolutePath());
                    int width = bm.getWidth();
                    int height = bm.getHeight();
                    Log.e("width ", width + "");
                    Log.e("height ", height + "");
                    int nw = width;
                    int nh = height;

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();


                    if (width < RWQUIREDSIZE || height < RWQUIREDSIZE)
                    {
                        nw = width;
                        nh = height;
                    }
                    else
                    {
                        if (bm.getWidth() < bm.getHeight())
                        {
                            nw = (int)RWQUIREDSIZE;
                            nh = (int) (RWQUIREDSIZE / width * height);
                        }
                        else
                        {
                            nh = (int)RWQUIREDSIZE;
                            double heightReso = (float) height / RWQUIREDSIZE;
                            nw = (int) ((float) width / heightReso);
                        }
                    }



                    bm = Bitmap.createScaledBitmap(bm, nw, nh , true);




                    bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);


                    byte[] b = baos.toByteArray();


                    encodedString = Base64.encodeToString(b, Base64.DEFAULT);
                    encondingStringBean.setEncdingImage(encodedString);
                    encodedStringList.add(encondingStringBean);





                    //new Compressor.Builder(activity);


                   /* File imagefile = new File(string);
                    Bitmap bm = BitmapFactory.decodeFile(imagefile.getAbsolutePath());
                    int width = bm.getWidth();
                    int height = bm.getHeight();
                    File   compressedImage = new Compressor.Builder(activity)
                            .setMaxWidth(width)
                            .setMaxHeight(height)
                            .setQuality(65)
                            .setCompressFormat(Bitmap.CompressFormat.WEBP)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .build()
                            .compressToFile(imagefile);
                    encondingStringBean.setImageName(imagefile.getName().replace(" ",""));

                    Bitmap encodingBitmap = BitmapFactory.decodeFile(compressedImage.getAbsolutePath());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] b = baos.toByteArray();
                    encodedString = Base64.encodeToString(b, Base64.DEFAULT);
                    encondingStringBean.setEncdingImage(encodedString);
                    encodedStringList.add(encondingStringBean);*/




                } catch (Exception e) {
                  e.printStackTrace();
                }

            }

        }


        return null;
    }

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);


        JSONArray jsonArray = new JSONArray();

        for (int i= 0 ; i < encodedStringList.size() ;i++)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("image_data", encodedStringList.get(i).getEncdingImage());
                jsonObject.put("image_name", encodedStringList.get(i).getImageName());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        listener.onSuccess(true , jsonArray);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        listener.onUpdate(values[0]);

    }

    private int imageRoated(File file){
        //Bitmap bitmap = null;
        int rotate =0;
        try {
            ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    //bitmap = rotateImage(file,rotate);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    //bitmap = rotateImage(file,rotate);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    //bitmap = rotateImage(file,rotate);
                    break;
                case 1:
                    rotate = 0;
                    //	bitmap = rotateImage(file,rotate);
                    break;

                case 2:
                    rotate = 0;
                    //bitmap = rotateImage(file,rotate);
                    break;
                case 4:
                    rotate = 180;
                    //bitmap = rotateImage(file,rotate);
                    break;

                case 0:
                    rotate = 0;
                    //bitmap = rotateImage(file,rotate);
                    break;
            }
        } catch (Exception e) {
            Log.e("Exception", ""+e.toString());
        }

        return rotate;
    }

    public class EncondingStringBean
    {
        String encdingImage ;

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        public String getEncdingImage() {
            return encdingImage;
        }

        public void setEncdingImage(String encdingImage) {
            this.encdingImage = encdingImage;
        }

        String imageName ;
    }


}
