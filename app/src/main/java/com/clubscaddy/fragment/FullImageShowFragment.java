package com.clubscaddy.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.R;
import com.clubscaddy.custumview.TouchImageView;

/**
 * Created by administrator on 18/4/17.
 */

public class FullImageShowFragment extends AppCompatActivity
{

    TouchImageView imageview ;
    ImageButton cancel_btn;
    Drawable defaultImage ; String imagePath ;

    ProgressBar image_loader_progressbar ;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.full_image_view);
        imageview = (TouchImageView) findViewById(R.id.imageview);
        cancel_btn = (ImageButton) findViewById(R.id.cancel_btn);
        image_loader_progressbar = (ProgressBar) findViewById(R.id.image_loader_progressbar);
        image_loader_progressbar.setVisibility(View.VISIBLE);
        try
        {
            final int imageType = Integer.parseInt(getIntent().getStringExtra("image_type")) ;
            imagePath = getIntent().getStringExtra("image_path");
            defaultImage = getResources().getDrawable(R.drawable.default_club_profile);
            if (imageType == 1)
            {
                defaultImage = getResources().getDrawable(R.drawable.default_club_profile);
            }

            if (imageType == 2)
            {
                defaultImage = getResources().getDrawable(R.drawable.default_img_profile);
            }




            Glide.with(FullImageShowFragment.this)
                    .load(imagePath).placeholder(defaultImage).error(defaultImage)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            image_loader_progressbar.setVisibility(View.GONE);

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            image_loader_progressbar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageview);
        }
        catch (Exception e)
        {
        imageview.setImageDrawable(defaultImage);
        }

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
