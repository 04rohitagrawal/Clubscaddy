package com.clubscaddy.Adapter;

import java.io.File;
import java.util.ArrayList;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.utility.ImageThumbTranslation;
import com.clubscaddy.R;
import com.clubscaddy.Bean.NewsBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.custumview.FileDir;
import com.clubscaddy.fragment.FullImageViewActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class ImageAdapters extends BaseAdapter
{


	Activity activity ;
	ArrayList<String>image_list ;
	ArrayList<String>full_image_list;
	ArrayList<NewsBean> newsList;
	int pos;
	ProgressBar progrssbar;
	//ArrayList<ProgressBar>pbarList = new ArrayList<ProgressBar>();

	public ImageAdapters(Activity activity , ArrayList<String>image_list,ArrayList<String>full_image_list ,ArrayList<NewsBean> newsList , int pos,ProgressBar progrssbar)
	{
		this.activity = activity;
		this.image_list = image_list;
		this.full_image_list = full_image_list;
		this.newsList = newsList;
		this.pos = pos;
		this.progrssbar = progrssbar;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return image_list.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return image_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}


	public class ViewHolder

	{
		ImageView	image_view ;
		ProgressBar loader_progrss_bar;
		//LinearLayout spiiner_laout;
		AQuery aq;
		Thread thread;
	}
	private Handler handler = new Handler();
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder ;
		DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
	if(convertView == null)
		{
			holder = new ViewHolder();
			convertView =LayoutInflater.from(activity).inflate(R.layout.image_adapter_layout, null) ;
		//	holder.file_downloading_bar = (ProgressBar) convertView.findViewById(R.id.file_downloading_bar);

			holder.image_view = (ImageView) convertView.findViewById(R.id.image_view);
			holder.loader_progrss_bar = (ProgressBar) convertView.findViewById(R.id.loader_progrss_bar);
			holder.image_view.setBackgroundColor(activity.getResources().getColor(R.color.transparent));

			//holder.spiiner_laout = (LinearLayout) convertView.findViewById(R.id.spiiner_laout);

			int height = metrics.heightPixels;
			holder.aq = new AQuery(convertView);

			if(image_list.size() ==1)
			{
				RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(width-60, width-60);
				param.setMargins(10, 10, 10, 10);
				holder.image_view.setLayoutParams(param);

			}
			if(image_list.size() ==2)
			{
				RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(width/2-20, width/2-40);

				holder.image_view.setLayoutParams(param);

			}
			if(image_list.size() >2)
			{
				RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(width/2-20, width/2-20);
				param.setMargins(0, 5, 0, 0);
				holder.image_view.setLayoutParams(param);

			}
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}


	/*try
	{
		pbarList.set(position, holder.file_downloading_bar)	;
	}
	catch(Exception e)
	{
		pbarList.add(holder.file_downloading_bar);
	}	*/





		//Toast.makeText(activity, newsList.get(pos).isIsfiledownliad()+"  Pos "+pos, Toast.LENGTH_LONG).show();


		if(newsList.get(pos).isIsfiledownliad())
		{
			this.progrssbar .setVisibility(View.VISIBLE);
			 // holder.aq.progress(holder.file_downloading_bar);
			//holder.file_downloading_bar.setProgress(30);
		}
		else
		{
			this.progrssbar.setVisibility(View.GONE);
		}

		//holder.file_downloading_bar.getProgress();
		holder.image_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//FullImageViewActivity

				Intent intent = new Intent(activity ,FullImageViewActivity.class );//path_list
				AppConstants.newsList = newsList;
				intent.putExtra("path_list", full_image_list);


				//intent.putExtra("news_list", newsList);
				intent.putExtra("pos", pos+"");
				intent.putExtra("image_pos", position+"");
				intent.putExtra("show_bottom", "1");
				//activity.startActivity(intent);
				String filename=newsList.get(pos).getVedioUrl()  .substring(newsList.get(pos).getVedioUrl() .lastIndexOf("/")+1);

				String url = newsList.get(pos).getVedioUrl();
				String filename1 =url.substring(url.lastIndexOf("/")+1);



					File fileDir = FileDir.createSDCardDir(activity);
					File videoFolder = FileDir.createVidoeSubDir(activity, fileDir);
					File downloadVideo = new File(videoFolder, filename1);



				if(!newsList.get(pos).isIsfiledownliad())
				{
					if(downloadVideo.exists())
					{
						intent = new Intent(activity ,FullImageViewActivity.class );//path_list
						AppConstants.newsList = newsList;
						intent.putExtra("path_list", full_image_list);


						//intent.putExtra("news_list", newsList);
						intent.putExtra("pos", pos+"");
						intent.putExtra("image_pos", position+"");
						intent.putExtra("show_bottom", "1");
						activity.startActivity(intent);
					}
					else
					{

						try
						{

							   newsList.get(pos).setIsfiledownliad(true);
							   progrssbar.setVisibility(View.VISIBLE);
							   // pbarList.get(pos).setVisibility(View.VISIBLE);
							  // Toast.makeText(activity, "Start  = "+pos, Toast.LENGTH_LONG).show();




							   holder.thread =	new Thread(new Runnable() {



								@Override
								public void run() {
									// TODO Auto-generated method stub
										while (true) {
											handler.post(new Runnable() {
											@Override
											public void run() {
												// TODO Auto-generated method stub
								//
											}
										});
										try {
												Thread.sleep(200);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}

										}
									}
								});//

				//	holder.thread.start();





















							    holder.aq.progress(progrssbar).download(url, downloadVideo, new AjaxCallback<File>(){






							    	@Override
								public void callback(String url, File object, AjaxStatus status) {
									super.callback(url, object, status);
									//  Toast.makeText(activity, "End  = "+pos, Toast.LENGTH_LONG).show();


									if(object!=null)
									{

									     Intent	intent = new Intent(activity ,FullImageViewActivity.class );//path_list
										AppConstants.newsList = newsList;
										intent.putExtra("path_list", full_image_list);
										progrssbar.setVisibility(View.GONE);
										 //pbarList.get(position).setVisibility(View.GONE);
										//intent.putExtra("news_list", newsList);
										intent.putExtra("pos", pos+"");
										intent.putExtra("image_pos", position+"");
										intent.putExtra("show_bottom", "1");
										 newsList.get(pos).setIsfiledownliad(false);
										//activity.startActivity(intent);
										//pbarList.get(pos).setVisibility(View.GONE);
										notifyDataSetChanged();
									}
								}



							});

						}
						catch (Exception e)
						{
							Log.e("Exception", ""+e.toString());
						}

					}
				}

			}
		});

		try
		{
			holder.loader_progrss_bar.setVisibility(View.VISIBLE);
			Glide.with(activity)
					.load(image_list.get(position)).asBitmap()

					.placeholder(activity. getResources().getDrawable( R.drawable.default_img )) // optional
					.error(R.drawable.default_pic)         // optional
					.transform(new ImageThumbTranslation(activity ,width/2))
					.listener(new RequestListener<String, Bitmap>() {
						@Override
						public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
							holder.loader_progrss_bar.setVisibility(View.GONE);

							return false;
						}

						@Override
						public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
							holder.loader_progrss_bar.setVisibility(View.GONE);

							return false;
						}
					})
					.into(holder.image_view);
		}
		catch (Exception e)
		{
			holder.loader_progrss_bar.setVisibility(View.GONE);

		}
/*.listener(new RequestListener<String, GlideDrawable>() {
			@Override
			public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
				holder.loader_progrss_bar.setVisibility(View.GONE);

				return false;
			}

			@Override
			public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
				holder.loader_progrss_bar.setVisibility(View.GONE);
				return false;
			}
		})*/


		return convertView;
	}

	public static String colorDecToHex(int p_red, int p_green, int p_blue)
	{
	    String red = Integer.toHexString(p_red);
	    String green = Integer.toHexString(p_green);
	    String blue = Integer.toHexString(p_blue);

	    if (red.length() == 1)
	    {
	        red = "0" + red;
	    }
	    if (green.length() == 1)
	    {
	        green = "0" + green;
	    }
	    if (blue.length() == 1)
	    {
	        blue = "0" + blue;
	    }

	    String colorHex = "#" + red + green + blue;
	    return colorHex;
	}



}