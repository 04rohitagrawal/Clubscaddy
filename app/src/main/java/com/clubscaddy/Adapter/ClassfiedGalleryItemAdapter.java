package com.clubscaddy.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.Bean.ClassifiedItemOtherPicture;
import com.clubscaddy.utility.ImageBitmapTransformationAccordingHeight;
import com.clubscaddy.utility.ImageBitmapTranslation;
import com.clubscaddy.utility.ImageThumbTranslation;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.R;

import com.clubscaddy.utility.CropSquareTransformation;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by administrator on 15/5/17.
 */

public class ClassfiedGalleryItemAdapter extends BaseAdapter {
    LayoutInflater inflator;
    Activity mContext;
    ArrayList<ClassifiedItemOtherPicture> list;

    ArrayList<String> deleteImageList;


    int index;
    AQuery aq;
    boolean expandStatus[];

    public ClassfiedGalleryItemAdapter(Activity context, ArrayList<ClassifiedItemOtherPicture> l , ArrayList<String> deleteImageList ) {
         mContext = context;
         inflator = LayoutInflater.from(mContext);
         list = l;
        this.deleteImageList = deleteImageList;

        aq = new AQuery(mContext);
        // this.image_load.init(ImageLoaderConfiguration.createDefault(mContext));

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
      final  ViewHolder mHolder;

        if (convertView == null) {

            mHolder = new ViewHolder();
            convertView = inflator.inflate(R.layout.gallery_item_view, parent, false);
            mHolder.image = (ImageView) convertView.findViewById(R.id.image);
            mHolder.delete_img_btn =(ImageButton) convertView.findViewById(R.id.delete_img_btn);
            mHolder.progress = (ProgressBar) convertView.findViewById(R.id.prog);
            convertView.setTag(mHolder);

        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        Drawable d = mContext.getResources().getDrawable(R.drawable.default_img);
        String imageUrl = list.get(position).getThumb();

        mHolder.delete_img_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

                // Setting Dialog Title
                alertDialog.setTitle(SessionManager.getClubName(mContext));

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want to remove this image?");

                alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        deleteImageList.add(list.get(position).getId()+"");
                        list.remove(position);

                        notifyDataSetChanged();
                        // Write your code here to invoke NO event
                        //  Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();			}
                });

                // Setting Negative "NO" Button//Confirm
                alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.cancel();


                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
        });

        if (!imageUrl.contains("http")) {
            imageUrl = "file://" + imageUrl;
        }
        int desireHeight =(int) mContext.getResources().getDimension(R.dimen.galary_thump_image_width);


        if(imageUrl.contains("http")) {
            mHolder.progress.setVisibility(View.VISIBLE);

            Glide.with(mContext)
                    .load(imageUrl).asBitmap()
                    .error(d).placeholder(d)
                    .transform(new ImageThumbTranslation(mContext , desireHeight))
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {

                            mHolder.progress.setVisibility(View.GONE);

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {

                            mHolder.progress.setVisibility(View.GONE);

                            return false;
                        }
                    })
                    .into(mHolder.image );



        }else{
            mHolder.progress.setVisibility(View.VISIBLE);
			/*
			Bitmap bitmap = imageRoated(file);
			if(bitmap!=null){
				bitmap = resize(bitmap,160,160);
				mHolder.image.setImageBitmap(bitmap);*/



            final File file = new File(list.get(position).getThumb());
            Uri uri = Uri.fromFile(file);
            //aq.id(mHolder.image).image(n, true, true, 0, R.drawable.default_pic);
            //aq.id(mHolder.image).image(file, true, 110, null);

            //Glide.with(mContext).load(file).placeholder(R.drawable.default_img).error(R.drawable.default_img).transform(new ImageBitmapTranslation(mContext ,(int) mContext.getResources().getDimension(R.dimen.galary_thump_image_width))).into(mHolder.image);

///////////////////////////////////////
            try
            {
//file:///storage/emulated/0/Pictures/Screenshots/Screenshot_20170726-183318.png
                Glide.with(mContext)
                        .load(file).asBitmap()
                        .error(d).placeholder(d)
                        .transform(new ImageThumbTranslation(mContext , desireHeight))
                        .listener(new RequestListener<File, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, File model, Target<Bitmap> target, boolean isFirstResource) {
                                mHolder.progress.setVisibility(View.GONE);

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, File model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                mHolder.progress.setVisibility(View.GONE);

                                return false;
                            }
                        })
                        .into(mHolder.image );




            }
            catch (Exception e)
            {
                Toast.makeText(mContext , e.getMessage() ,Toast.LENGTH_LONG).show();
            }

            //}
        }
        return convertView;
    }






    public class ViewHolder {
        ImageView image;
        ProgressBar progress;
        ImageButton	delete_img_btn;
    }


}

