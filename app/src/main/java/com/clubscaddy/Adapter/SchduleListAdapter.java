package com.clubscaddy.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clubscaddy.Bean.League;

import com.clubscaddy.R;
import com.clubscaddy.fragment.ManageGroupFragment;

import java.util.ArrayList;

/**
 * Created by administrator on 23/5/17.
 */

public class SchduleListAdapter extends RecyclerView.Adapter<SchduleListAdapter.ViewHolder>
{

    Activity activity ;
    ArrayList<League>scheduleArrayList ;
    ManageGroupFragment.OnItemClickListener onItemClickListener ;
    public SchduleListAdapter(Activity activity , ArrayList<League>scheduleArrayList ,    ManageGroupFragment.OnItemClickListener onItemClickListener  )
    {
        this.activity = activity ;
        this.scheduleArrayList = scheduleArrayList ;
        this.onItemClickListener = onItemClickListener ;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(activity).inflate(R.layout.schdule_list_adapter , null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position)
    {
       viewHolder.schdule_title_tv.setText(scheduleArrayList.get(position).getLeague_name());

        viewHolder.relative_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return scheduleArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView schdule_title_tv ;
RelativeLayout relative_layout;
        public ViewHolder(View itemView) {
            super(itemView);
             schdule_title_tv = (TextView) itemView.findViewById(R.id.schdule_title_tv);
            relative_layout = (RelativeLayout) itemView.findViewById(R.id.relative_layout);

        }
    }
}
