package com.clubscaddy.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clubscaddy.Bean.ClassBean;
import com.clubscaddy.Interface.RecycleViewItemClickListner;
import com.clubscaddy.R;

import java.util.ArrayList;

/**
 * Created by administrator on 6/12/17.
 */

public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.ClassListViewHolder>
{
    Activity activity ;
    ArrayList<ClassBean> classBeanArrayList ;

    RecycleViewItemClickListner itemClickListner ;

    public ClassListAdapter(Activity activity ,ArrayList<ClassBean> classBeanArrayList , RecycleViewItemClickListner itemClickListner)
    {
        this.activity = activity ;
        this.classBeanArrayList = classBeanArrayList ;

        this.itemClickListner = itemClickListner ;


    }

    @Override
    public ClassListViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(activity).inflate(R.layout.class_list_layout , null);

        return new ClassListViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return classBeanArrayList.size();
    }

    @Override
    public void onBindViewHolder(ClassListViewHolder holder, final int position)
    {
     holder.classNameTextView.setText(classBeanArrayList.get(position).getClassName());

        holder.admin_list_relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemClickListner.onItemClick(position ,0);

            }
        });

    }


    public class ClassListViewHolder extends RecyclerView.ViewHolder
    {

        TextView classNameTextView ;
        RelativeLayout admin_list_relativeLayout;
        public ClassListViewHolder(View itemView) {
            super(itemView);
            classNameTextView = (TextView) itemView.findViewById(R.id.admin_list_name);

            admin_list_relativeLayout = (RelativeLayout) itemView.findViewById(R.id.admin_list_relativeLayout);

        }
    }

}
