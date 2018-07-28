package com.clubscaddy.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.utility.ImageThumbTranslation;
import com.clubscaddy.R;
import com.clubscaddy.utility.ImageBitmapTranslation;
import com.clubscaddy.utility.ImageTransformation;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by administrator on 15/6/17.
 */

public class InstragramImageListAdapter extends BaseAdapter
{

   Activity activity ;
    ArrayList<String>imageList ;

    public InstragramImageListAdapter(Activity activity , ArrayList<String>imageList )
    {
       this.activity = activity ;
        this.imageList = imageList ;
    }


    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public String getItem(int position)
    {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

       final ViewHolder viewHolder ;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.instragram_image_adapter_layout , null);
            viewHolder.instrgram_image_view = (ImageView) convertView.findViewById(R.id.instragram_image_view);
            viewHolder.imageLoaderProgressbar = (ProgressBar) convertView.findViewById(R.id.image_loader_progressbar);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        try
        {
            int  desireHeight = (int) activity.getResources().getDimension(R.dimen.galary_thump_image_width);

            viewHolder.imageLoaderProgressbar.setVisibility(View.VISIBLE);
            Picasso.with(activity)
                    .load(imageList.get(position))
                    .placeholder(R.drawable.default_img)
                    .error(R.drawable.default_img)
                    .transform(new ImageTransformation(desireHeight))

                    /*.transform(new ImageThumbTranslation(activity , desireHeight))
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            viewHolder.imageLoaderProgressbar.setVisibility(View.GONE);

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            viewHolder.imageLoaderProgressbar.setVisibility(View.GONE);

                            return false;
                        }
                    })*/

                    .into(viewHolder.instrgram_image_view, new Callback() {
                        @Override
                        public void onSuccess()
                        {
                            viewHolder.imageLoaderProgressbar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError()
                        {
                            viewHolder.imageLoaderProgressbar.setVisibility(View.GONE);

                        }
                    });
        }
        catch (Exception e)
        {
            viewHolder.imageLoaderProgressbar.setVisibility(View.GONE);

            viewHolder.instrgram_image_view.setImageResource(R.drawable.default_img_profile);
        }

        return convertView;
    }

    public class ViewHolder
    {
        ImageView instrgram_image_view ;
        ProgressBar imageLoaderProgressbar;

        //
    }

}
