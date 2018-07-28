package com.clubscaddy.Adapter;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.clubscaddy.Bean.ClassDetail;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.RecycleViewItemClickListner;
import com.clubscaddy.R;
import com.clubscaddy.utility.Utill;

import java.util.ArrayList;

/**
 * Created by administrator on 8/12/17.
 */

public class ClassDetailListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    Activity activity ;
    ArrayList<ClassDetail> classDetailArrayList ;


    RecycleViewItemClickListner itemClickListner ;

    public ClassDetailListAdapter(Activity activity ,ArrayList<ClassDetail> classDetailArrayList ,RecycleViewItemClickListner itemClickListner )
    {
        this.activity = activity ;

        ClassDetail classDetail = new ClassDetail();




        this.classDetailArrayList = classDetailArrayList ;

        this.classDetailArrayList.add(0 , classDetail);


        this.itemClickListner = itemClickListner ;

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {


        if (viewType == 0)
        {
            View view = LayoutInflater.from(activity).inflate(R.layout.class_detail_list_header_layout , null);

          return new ClassDetailHeaderHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(activity).inflate(R.layout.class_detail_list_item_layout , null);

            return  new ClassDetailViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        if (holder instanceof ClassDetailViewHolder )
        {
            ((ClassDetailViewHolder) holder).dateTv.setText(/*Utill.formattedDateFromString("EEEE DD MMMM yyyy" ,"EEE DD MMM" ,*/classDetailArrayList.get(position).getClassDate() /*)*/);

            ((ClassDetailViewHolder) holder).timeDurationTv.setText(classDetailArrayList.get(position).getClassStartTime() +" to "+classDetailArrayList.get(position).getClassEndTime());

            ((ClassDetailViewHolder) holder).deletBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    itemClickListner.onItemClick(position , 0);
                }
            });

        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        else
            return 1;
    }
    @Override
    public int getItemCount() {
        return classDetailArrayList.size();
    }

    public class ClassDetailViewHolder extends RecyclerView.ViewHolder
    {

        TextView dateTv ;
        TextView timeDurationTv ;
        ImageButton deletBtn ;

        public ClassDetailViewHolder(View itemView) {
            super(itemView);

            dateTv = (TextView) itemView.findViewById(R.id.date_tv);
             timeDurationTv = (TextView) itemView.findViewById(R.id.time_duration_tv);
             deletBtn = (ImageButton) itemView.findViewById(R.id.delet_btn);

        }
    }

    public class ClassDetailHeaderHolder extends RecyclerView.ViewHolder
    {

        public ClassDetailHeaderHolder(View itemView) {
            super(itemView);
        }
    }
}
