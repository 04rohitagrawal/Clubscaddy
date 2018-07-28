package com.clubscaddy.Adapter;

import android.app.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clubscaddy.utility.Utill;
import com.clubscaddy.R;
import com.clubscaddy.custumview.TouchImageView;
import com.clubscaddy.fragment.IstragramFullImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by administrator on 17/6/17.
 */

public class InstragramGalaryPagerAdapter extends PagerAdapter {

    Activity mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<String> path_list ;

    Drawable wrappedDrawable;

    IstragramFullImageView.ImageZoomChangeListener imageZoomChangeListener;
    public InstragramGalaryPagerAdapter(Activity context , ArrayList<String>path_list ,IstragramFullImageView.ImageZoomChangeListener imageZoomChangeListener) {
        this.path_list = path_list;
        this.imageZoomChangeListener = imageZoomChangeListener;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return path_list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @SuppressWarnings("static-access")
    @Override
    public Object instantiateItem(ViewGroup container,final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.fullimage_view_layout, container, false);

        final TouchImageView imageView = (TouchImageView) itemView.findViewById(R.id.image_view_id);

        final ProgressBar image_loader_progressbar = (ProgressBar) itemView.findViewById(R.id.image_loader_progressbar);

     final TextView   download_btn = (TextView) itemView.findViewById(R.id.download_btn);
     final   ImageView download_icon_iv = (ImageView) itemView.findViewById(R.id.download_icon_iv);

        download_btn.setVisibility(View.VISIBLE);

        download_icon_iv.setVisibility(View.VISIBLE);


        Drawable drawable = mContext.getResources().getDrawable(R.drawable.download);


        wrappedDrawable = DrawableCompat.wrap(drawable);

        download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                String filename = path_list.get(position).substring(path_list.get(position).lastIndexOf("/") + 1);


                File imageFile = new File(Environment.getExternalStorageDirectory()
                        + File.separator + mContext.getResources().getString(R.string.app_name) + File.separator + filename);
                if (imageFile.exists() == false)
                {

                    onDownloadUrl1(path_list.get(position), filename ,download_icon_iv ,download_btn);
                }


            }
        });




        String filename = path_list.get(position).substring(path_list.get(position).lastIndexOf("/") + 1);


        File imageFile = new File(Environment.getExternalStorageDirectory()
                + File.separator + mContext.getResources().getString(R.string.app_name) + File.separator + filename);
        if (imageFile.exists() == false)
        {
            download_btn.setTextColor(Color.parseColor("#ffffff"));
            DrawableCompat.setTint(wrappedDrawable, mContext.getResources().getColor(R.color.white_color));
            download_icon_iv.setImageDrawable(wrappedDrawable);
        }
        else
            {
                download_btn.setTextColor(Color.parseColor("#536CB5"));
                DrawableCompat.setTint(wrappedDrawable, mContext.getResources().getColor(R.color.lessions));
                download_icon_iv.setImageDrawable(wrappedDrawable);
           //

        }


        if(Patterns.WEB_URL.matcher(path_list.get(position)).matches() )
        {
            image_loader_progressbar.setVisibility(View.VISIBLE);
            Picasso.with(mContext)
                    .load(path_list.get(position))
                    .placeholder(mContext. getResources().getDrawable( R.drawable.default_pic )) // optional
                    .error(R.drawable.default_pic)         // optional
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            image_loader_progressbar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {
                            image_loader_progressbar.setVisibility(View.GONE);

                        }
                    });
        }
        else
        {



            Uri uri = Uri.fromFile(new File(path_list.get(position)));
            try
            {
                final File imgFile = new  File(path_list.get(position));

                if(imgFile.exists())
                {
                    uri = Uri.fromFile(new File(path_list.get(position)));

                    image_loader_progressbar.setVisibility(View.VISIBLE);
                    Picasso.with(mContext)
                            .load(uri)
                            .placeholder(mContext. getResources().getDrawable( R.drawable.default_pic )) // optional
                            .error(R.drawable.default_pic)         // optional
                            .into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    image_loader_progressbar.setVisibility(View.GONE);

                                }

                                @Override
                                public void onError() {
                                    image_loader_progressbar.setVisibility(View.GONE);

                                }
                            });


                }
            }
            catch(Exception e)
            {
                image_loader_progressbar.setVisibility(View.GONE);

                Toast.makeText(mContext, "Path "+e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }

        imageView.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
            @Override
            public void onMove() {


                imageZoomChangeListener.getZoomLevel(imageView.getCurrentZoom());
            }
        });


        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }













ProgressDialog pd;
    public void onDownloadUrl1(final String downloadurl, final String filename ,final ImageView download_icon_iv , final  TextView download_btn) {

        AsyncTask<Void, String, String> task = new AsyncTask<Void, String, String>() {


            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub


                super.onPreExecute();
                pd = new android.app.ProgressDialog(mContext);
                pd.setCancelable(false);
                pd.show();

            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub
                DownloadManager mgr = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri downloadUri = Uri.parse(downloadurl);
                DownloadManager.Request request = new DownloadManager.Request(
                        downloadUri);
                request.setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI
                                | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false).setTitle(filename)
                        .setDescription("Image is downloading.")
                        .setDestinationInExternalPublicDir("/" +mContext.getResources().getString(R.string.app_name), filename);

                mgr.enqueue(request);
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                pd.dismiss();
                Utill.showDialg("DownLoad Successfully", mContext);

                //Toast.makeText(getApplicationContext(), "DownLoad Successfully", Toast.LENGTH_LONG).show();

                File imageFile = new File(Environment.getExternalStorageDirectory()
                        + File.separator +mContext. getResources().getString(R.string.app_name) + File.separator + filename);
                //if(imageFile.exists())
                {
                    Drawable drawable = mContext.getResources().getDrawable(R.drawable.download);


                    wrappedDrawable = DrawableCompat.wrap(drawable);
                    download_btn.setTextColor(Color.parseColor("#536CB5"));
                    DrawableCompat.setTint(wrappedDrawable, mContext.getResources().getColor(R.color.lessions));
                    download_icon_iv.setImageDrawable(wrappedDrawable);

                }



            }

        };

        task.execute();
    }



}
