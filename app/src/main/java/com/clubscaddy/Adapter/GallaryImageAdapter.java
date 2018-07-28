package com.clubscaddy.Adapter;

import java.io.File;
import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.utility.ImageBitmapTranslation;
import com.clubscaddy.utility.ImageThumbTranslation;
import com.clubscaddy.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class GallaryImageAdapter extends BaseAdapter {

    private Activity ctx;
    int imageBackground;
    ArrayList<String>galary_list ;
    public GallaryImageAdapter(Activity c ,ArrayList<String>galary_list) {
        ctx = c;
        this.galary_list = galary_list;
        /*TypedArray ta = obtainStyledAttributes(R.styleable.Gallery1);
        imageBackground = ta.getResourceId(R.styleable.Gallery1_android_galleryItemBackground, 1);
        ta.recycle();*/
    }

    @Override
    public int getCount() {
       
        return galary_list.size();
    }

    @Override
    public Object getItem(int arg0) {
       
        return galary_list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
       
        return arg0;
    }

    public class ViewHolder
    {
    ImageView 	gallry_image_view_id;
		ProgressBar imageIoaderProgressbar ;
    }
    
	@Override
    public View getView(final int pos, View convertView, ViewGroup arg2) {
		
		
		final ViewHolder holder ;
		
		if(convertView == null)
		{
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(ctx);
	    	convertView = inflater.inflate(R.layout.glary_image_view_adapter_view, null);
	    	holder.gallry_image_view_id = (ImageView) convertView.findViewById(R.id.gallry_image_view_id);
	       holder.imageIoaderProgressbar = (ProgressBar) convertView.findViewById(R.id.image_loader_progressbar);
	    	convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();	
		}
		String   temp = galary_list.get(pos);
		int  desireHeight = (int) ctx.getResources().getDimension(R.dimen.galary_thump_image_width);

		if(temp.startsWith("http"))
		{
			//Toast.makeText(ctx, "I  "+galary_list.get(pos), 1).show();


			try
			{


				holder.imageIoaderProgressbar.setVisibility(View.VISIBLE);

				Glide.with(ctx)
						.load(temp).asBitmap()
			          	.placeholder(ctx. getResources().getDrawable( R.drawable.default_img )) // optional
						.error(R.drawable.default_img)         // optional
						.transform(new ImageThumbTranslation(ctx ,desireHeight))
                         .listener(new RequestListener<String, Bitmap>() {
							 @Override
							 public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
								 holder.imageIoaderProgressbar.setVisibility(View.GONE);

								 return false;
							 }

							 @Override
							 public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
								 holder.imageIoaderProgressbar.setVisibility(View.GONE);

								 return false;
							 }
						 })
						.into(holder.gallry_image_view_id);


			}
			catch (Exception e)
			{
				Toast.makeText(ctx , e.getMessage() ,Toast.LENGTH_SHORT).show();
			}
			

		}
		else
		{
			Uri uri = Uri.fromFile(new File(galary_list.get(pos)));
			holder.imageIoaderProgressbar.setVisibility(View.VISIBLE);




			Glide.with(ctx)
					.load(uri)

					.placeholder(ctx. getResources().getDrawable( R.drawable.default_img )) // optional
					.error(R.drawable.default_img)         // optional
					.transform(new ImageThumbTranslation(ctx ,desireHeight))
					.listener(new RequestListener<Uri, GlideDrawable>() {
						@Override
						public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
							holder.imageIoaderProgressbar.setVisibility(View.GONE);

							return false;
						}

						@Override
						public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
							holder.imageIoaderProgressbar.setVisibility(View.GONE);

							return false;
						}
					})
					.into(holder.gallry_image_view_id);




		}
		
	

		
		
        return convertView;
    }


}
