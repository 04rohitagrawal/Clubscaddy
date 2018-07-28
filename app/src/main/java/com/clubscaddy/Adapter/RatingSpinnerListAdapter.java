package com.clubscaddy.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.clubscaddy.Bean.FilterRating;
import com.clubscaddy.R;
import com.clubscaddy.fragment.MembersDirectoryFragment;

import java.util.ArrayList;

/**
 * Created by administrator on 30/12/16.
 */

public class RatingSpinnerListAdapter extends BaseAdapter
{

    Activity activity ;
    ArrayList<FilterRating>rattingList ;
    MembersDirectoryFragment membersDirectoryFragment;
    public RatingSpinnerListAdapter(Activity activity , ArrayList<FilterRating>rattingList , MembersDirectoryFragment membersDirectoryFragment)
    {
        this.activity = activity ;
        this.rattingList = rattingList ;
        this.membersDirectoryFragment = membersDirectoryFragment ;
    }

    @Override
    public int getCount() {
        return rattingList.size();
    }

    @Override
    public Object getItem(int position) {
        return rattingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    ViewHolder viewHolder ;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {


        if (convertView == null)
        {
            convertView = LayoutInflater.from(activity).inflate(R.layout.ratting_list_item ,null);
            viewHolder = new ViewHolder();
            viewHolder.rating_cb = (CheckBox) convertView.findViewById(R.id.rating_cb);
            viewHolder.rating_tv = (TextView) convertView.findViewById(R.id.rating_tv);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.rating_tv.setText(rattingList.get(position).getRating());

        if (position == 0)
        {
            viewHolder.rating_cb.setVisibility(View.GONE);
            convertView.setBackgroundColor(activity.getResources().getColor(R.color.transparent));
            viewHolder.rating_tv.setClickable(false);
            viewHolder.rating_tv.setTextSize(14);

        }
        else
        {
            convertView.setBackgroundColor(activity.getResources().getColor(R.color.white_color));
            viewHolder.rating_cb.setVisibility(View.VISIBLE);
            viewHolder.rating_tv.setEnabled(true);
            viewHolder.rating_tv.setTextSize(18);

        }

        if (position != 0)
        {
            viewHolder.rating_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (position != 0)
                    {
                        clickOnItemPos(position ,viewHolder.rating_cb ,viewHolder.rating_tv);
                    }

                }
            });
        }



       Log.e("ischeck ",rattingList.get(position).isItemSelected()+"" );
        viewHolder.rating_cb.setChecked(rattingList.get(position).isItemSelected());
        viewHolder.rating_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickOnItemPos(position ,viewHolder.rating_cb ,viewHolder.rating_tv);
            }
        });



        return convertView;
    }


    public class ViewHolder
    {
        CheckBox rating_cb;
        TextView rating_tv;
    }

    public void clickOnItemPos(int pos ,CheckBox checkBox , TextView textView)
    {
        Log.e("pos ",pos+"");
        if(pos == 0)
        {
            membersDirectoryFragment.ratting_list_spinner.showContextMenu();
           // membersDirectoryFragment.ratting_list_spinner.performLongClick();
            return;
        }


        if (pos == 1) {


            if (rattingList.get(pos).isItemSelected() == false) {

                for (int i = 0; i < rattingList.size(); i++) {
                    rattingList.get(i).setItemSelected(false);
                }

                rattingList.get(pos).setItemSelected(true);
                notifyDataSetChanged();
                membersDirectoryFragment.filterList();
            }
        }
            else
            {
                rattingList.get(1).setItemSelected(false);

                if (rattingList.get(pos).isItemSelected())
                {

                    rattingList.get(pos).setItemSelected(false);

                    if (getSelectedItemCount() == 0)
                    {
                        rattingList.get(1).setItemSelected(true);
                    }
                }
                else
                {
                    rattingList.get(pos).setItemSelected(true);
                }



             notifyDataSetChanged();


                membersDirectoryFragment.filterList();

        }


    }

    public int getSelectedItemCount()
    {
        int count  =0;
        for (int i = 0 ; i < rattingList.size() ;i++)
        {

            if (rattingList.get(i).isItemSelected())
            {
                count++;
            }
        }





        return count ;
    }

}
