package com.clubscaddy.Adapter;

import java.io.File;
import java.util.ArrayList;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.utility.ImageBitmapTranslation;
import com.clubscaddy.utility.ImageThumbTranslation;
import com.clubscaddy.utility.SessionManager;

import com.clubscaddy.R;
import com.clubscaddy.utility.CropSquareTransformation;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class EditEventGalaryAdapter extends BaseAdapter {
	LayoutInflater inflator;
	Activity mContext;
	ArrayList<String> list;

	int index;
	SessionManager sessionManager ;
	boolean expandStatus[];
	ArrayList<String>delete_image_id_list;

	public EditEventGalaryAdapter(Activity context,ArrayList<String>delete_image_id_list,ArrayList<String> list ) {
		mContext = context;
		inflator = LayoutInflater.from(mContext);
		sessionManager = new SessionManager();
		
		this.list = list;

		this.delete_image_id_list = delete_image_id_list;

		//Toast.makeText(mContext, "List size "+delete_image_id_list.size(), 1).show();
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

				try
				{
					delete_image_id_list.remove(position);
				}
				catch(Exception e)
				{

				}


				//Toast.makeText(mContext, "ids   "+delete_image_id_list.size(), 1).show();;

				list.remove(position);

				notifyDataSetChanged();
				// Write your code here to invoke NO event
				//  Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
				dialog.cancel();         }
        });
 //
        // Setting Negative "NO" Button
        alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	



				dialog.cancel();
            }
        });

		// Showing Alert Message
		alertDialog.show();
			}
		});
		int  desireHeight = (int) mContext.getResources().getDimension(R.dimen.galary_thump_image_width);

		if (!imageUrl.contains("http"))
		{






			mHolder.progress.setVisibility(View.VISIBLE);
			
			 Uri uri = Uri.fromFile(new File(list.get(position)));






			Glide.with(mContext)
					.load(new File(list.get(position))).asBitmap()

					.placeholder(mContext. getResources().getDrawable( R.drawable.default_img )) // optional
					.error(R.drawable.default_img)         // optional
					.transform(new ImageThumbTranslation(mContext ,desireHeight))
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
		else
		{
			try
			{
				mHolder.progress.setVisibility(View.VISIBLE);




				Glide.with(mContext)
						.load(list.get(position)).asBitmap()
						.placeholder(mContext. getResources().getDrawable( R.drawable.default_img )) // optional
						.error(R.drawable.default_img)         // optional
						.transform(new ImageThumbTranslation(mContext ,desireHeight))
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







			}
			catch (Exception e)
			{
				mHolder.progress.setVisibility(View.GONE);

				Toast.makeText(mContext , e.getMessage() ,Toast.LENGTH_LONG).show();
			}
			//image_load.displayImage(imageUrl, mHolder.image, opt, new CustomImageLoader(mHolder.progress, mContext, d, false));
	
		}
    
			return convertView;
	}

	public class ViewHolder {
		ImageView image;
		ProgressBar progress;
		ImageButton	delete_img_btn;
	}

}
