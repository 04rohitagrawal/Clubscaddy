package com.clubscaddy.Adapter;

import java.io.File;
import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.utility.CropSquareTransformation;
import com.clubscaddy.utility.ImageThumbTranslation;
import com.clubscaddy.utility.SessionManager;

import com.clubscaddy.R;
import com.clubscaddy.Bean.NewsBean;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class EditGalleryItemAdapter extends BaseAdapter {
	LayoutInflater inflator;
	Activity mContext;
	ArrayList<String> list;

	int index;
	boolean expandStatus[];
	NewsBean newsBean ;
	ArrayList<String>mainImageList ;
	public EditGalleryItemAdapter(Activity context, ArrayList<String> l ,ArrayList<String>mainImageList,NewsBean newsBean) {
		mContext = context;
		inflator = LayoutInflater.from(mContext);
		list = l;
		this.mainImageList = mainImageList;
		this.newsBean = newsBean;
		// this.image_load.init(ImageLoaderConfiguration.createDefault(mContext));

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

	final	ViewHolder mHolder;

		if (convertView == null)
		{
			mHolder = new ViewHolder();
			convertView = inflator.inflate(R.layout.gallery_item_view, parent, false);
			mHolder.image = (ImageView) convertView.findViewById(R.id.image);
			mHolder.delete_img_btn =(ImageButton) convertView.findViewById(R.id.delete_img_btn);
			mHolder.progress = (ProgressBar) convertView.findViewById(R.id.prog);
			convertView.setTag(mHolder);

		}
		 else
		 {
			mHolder = (ViewHolder) convertView.getTag();
		 }
		Drawable d = mContext.getResources().getDrawable(R.drawable.logo_profile);
		String imageUrl = list.get(position);
		
		mHolder.delete_img_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog.setTitle(SessionManager.getClubName(mContext));

		// Setting Dialog Message
		alertDialog.setMessage("Are you sure you want to remove this image");

		alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	  dialog.cancel();
            // Write your code here to invoke YES event
            //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
            }
        });
 
        // 10-27 20:10:46.965: E/old path(18833): 0old_url[{"id":"70"},{"id":"71"}]

        alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

				mainImageList.remove(position);
            	
            	list.remove(position);
            	
            	notifyDataSetChanged();
            // Write your code here to invoke NO event
          //  Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
            dialog.cancel();
            }
        });

		// Showing Alert Message
		alertDialog.show();
			}
		});


		try
		{
            int  width = (int) mContext.getResources().getDimension(R.dimen.galary_thump_image_width);

			if(list.get(position).contains("http"))
			{
				mHolder.progress.setVisibility(View.VISIBLE);

				Glide.with(mContext)
						.load(list.get(position)).asBitmap()
						.transform(new ImageThumbTranslation(mContext ,width ))
						.placeholder(R.drawable.default_img)
						.error(R.drawable.default_img)
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
						.into(mHolder.image) ;





			}
			else
			{
				Uri uri = Uri.fromFile(new File(list.get(position)));
				mHolder.progress.setVisibility(View.VISIBLE);

				Glide.with(mContext)
						.load(new File(list.get(position))).asBitmap()
						.transform(new ImageThumbTranslation(mContext ,width ))
						.placeholder(R.drawable.default_img)
						.error(R.drawable.default_img)
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
						.into(mHolder.image) ;

			}
		}
		catch (Exception e)
		{
			mHolder.progress.setVisibility(View.GONE);

		}









			return convertView;
	}

	public class ViewHolder {
		ImageView image;
		ProgressBar progress;
		ImageButton	delete_img_btn;
	}

}
