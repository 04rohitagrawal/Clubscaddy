package com.clubscaddy.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.clubscaddy.Adapter.InstragramGalaryPagerAdapter;
import com.clubscaddy.R;
import com.clubscaddy.custumview.ExtendedViewPager;

import java.util.ArrayList;

/**
 * Created by administrator on 17/6/17.
 */

public class IstragramFullImageView  extends AppCompatActivity
{
    ImageButton cancel_btn ;
    ImageView image_view_id;
    ExtendedViewPager viewPager;


    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.instragram_full_imageview);
        image_view_id = (ImageView) findViewById(R.id.image_view_id);
        cancel_btn = (ImageButton) findViewById(R.id.cancel_btn);
        viewPager = (ExtendedViewPager) findViewById(R.id.viewPager);
        ArrayList<String> path_list = (ArrayList<String>) getIntent().getSerializableExtra("path_list");
        InstragramGalaryPagerAdapter pageAdapter = new InstragramGalaryPagerAdapter(IstragramFullImageView.this, path_list , new ImageZoomChangeListener());
        viewPager.setAdapter(pageAdapter);

        try
        {
            viewPager.setCurrentItem(Integer.parseInt(getIntent().getStringExtra("pos")));
        }
        catch(Exception e)
        {

        }


        cancel_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });


    }
    ProgressDialog pd ;


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }


    public class ImageZoomChangeListener
    {
        public void getZoomLevel(float zoomLevel)
        {
            //Log.e("zoom level" ,(zoomLevel ==  1.0)+" "+zoomLevel);
            if (zoomLevel == 1.0)
            {
                viewPager.setSwipeLocked(false);
            }
            else
            {
                viewPager.setSwipeLocked(true);
            }
        }
    }



}

