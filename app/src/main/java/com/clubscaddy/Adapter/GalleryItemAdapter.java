package com.clubscaddy.Adapter;

import java.io.File;
import java.util.ArrayList;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


import com.clubscaddy.utility.ImageThumbTranslation;
import com.clubscaddy.R;
import com.clubscaddy.utility.SessionManager;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class GalleryItemAdapter extends BaseAdapter {
	LayoutInflater inflator;
	Activity mContext;
	ArrayList<String> list;

	int index;
	AQuery aq;
	boolean expandStatus[];

	public GalleryItemAdapter(Activity context, ArrayList<String> l ) {
		mContext = context;
		inflator = LayoutInflater.from(mContext);
		list = l;

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
		final ViewHolder mHolder;

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
				alertDialog.setMessage("Are you sure you want to remove this image?");

				alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {

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

  int scaleWidth = (int) mContext.getResources().getDimension(R.dimen.galary_thump_image_width);
		if(imageUrl.contains("http")) {
			mHolder.progress.setVisibility(View.VISIBLE);


			Glide.with(mContext)
					.load(imageUrl).asBitmap()
					.transform(new ImageThumbTranslation(mContext ,scaleWidth))
					.placeholder(R.drawable.default_img).error(R.drawable.default_img)
					.error(R.drawable.default_img).error(R.drawable.default_img)
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
					.into(mHolder.image);
		}else{
			mHolder.progress.setVisibility(View.GONE);
			/*
			Bitmap bitmap = imageRoated(file);
			if(bitmap!=null){
				bitmap = resize(bitmap,160,160);
				mHolder.image.setImageBitmap(bitmap);*/
			
			
			
			final File file = new File(list.get(position));
			Uri uri = Uri.fromFile(new File(list.get(position)));
			//aq.id(mHolder.image).image(n, true, true, 0, R.drawable.default_pic);
			//aq.id(mHolder.image).image(file, true, 110, null);

			//Glide.with(mContext).load(file).placeholder(R.drawable.default_img).error(R.drawable.default_img).transform(new ImageBitmapTranslation(mContext ,(int) mContext.getResources().getDimension(R.dimen.galary_thump_image_width))).into(mHolder.image);

			mHolder.progress.setVisibility(View.VISIBLE);
             //file:///storage/emulated/0/Pictures/Screenshots/Screenshot_20170726-183318.png
			//file:///storage/emulated/0/Pictures/Screenshots/Screenshot_20170726-183318.png
			try
			{
				Glide.with(mContext)
						.load(file).asBitmap()
						.transform(new ImageThumbTranslation(mContext , scaleWidth))
						.placeholder(R.drawable.default_img).error(R.drawable.default_img)
						.error(R.drawable.default_img).error(R.drawable.default_img)
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
						.into(mHolder.image);

			}
			catch (Exception e)
			{
				mHolder.progress.setVisibility(View.GONE);

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
