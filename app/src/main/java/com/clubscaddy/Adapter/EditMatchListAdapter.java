package com.clubscaddy.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.clubscaddy.Bean.Match;
import com.clubscaddy.custumview.ExpandableHeightListView;
import com.clubscaddy.utility.DeviceInfo;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.R;
import com.clubscaddy.fragment.EditSchduleFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by administrator on 12/6/17.
 */

public class EditMatchListAdapter  extends BaseAdapter
{

    Activity activity ; ArrayList<Match> matchArrayList ;
    ArrayList<String>avilableList ;
    SimpleDateFormat simpleTimeFormat ;
    public   EditSchduleFragment  addSchdulerFragment  ;
    SimpleDateFormat  datetimeformate ;

    Calendar currentcalendar ;

    public EditMatchListAdapter(EditSchduleFragment addSchdulerFragment , ArrayList<Match> matchArrayList , String currentTime)
    {
        this.activity = addSchdulerFragment.getActivity() ;
        this.addSchdulerFragment = addSchdulerFragment ;
        this.matchArrayList = matchArrayList ;
        avilableList = new ArrayList<>();
        avilableList.add("No Response");
        avilableList.add("Yes");
        avilableList.add("No");
        avilableList.add("Maybe");
        avilableList.add("Call last");

        datetimeformate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            currentcalendar = Calendar.getInstance(Locale.ENGLISH);
            currentcalendar.setTime(datetimeformate.parse(currentTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }





    @Override
    public int getCount() {
        return matchArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return matchArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder ;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.match_list_item_layout , null);

            viewHolder.date_tv = (TextView) convertView.findViewById(R.id.date_tv);
            viewHolder.location_tv = (TextView) convertView.findViewById(R.id.location_tv);
            viewHolder.avail_list_spiiner = (Spinner) convertView.findViewById(R.id.avail_list_spiiner);

            viewHolder.customSpinnerAdapter = new CustomSpinnerAdapter(activity , avilableList ,viewHolder.avail_list_spiiner);

            viewHolder.edit_btn = (ImageView) convertView.findViewById(R.id.edit_btn);


            viewHolder.table_down_arrow = (ImageView) convertView.findViewById(R.id.table_down_arrow);
            viewHolder.opponent_name_tv = (TextView) convertView.findViewById(R.id.opponent_name_tv);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        int pos = position ;
        viewHolder.opponent_name_tv.setText(matchArrayList.get(position).getMatch_opponet());

        viewHolder.date_tv.setText(matchArrayList.get(position).getDate()+"\n" +matchArrayList.get(position).getTime() );
        viewHolder.location_tv.setText(matchArrayList.get(position).getLocation());
        viewHolder.avail_list_spiiner.setAdapter(viewHolder.customSpinnerAdapter);

        viewHolder.avail_list_spiiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                matchArrayList.get(position).setListItemPos(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                matchArrayList.get(position).setListItemPos(0);
            }
        });



        viewHolder.location_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    String url = "http://maps.google.com/maps?daddr="+ matchArrayList.get(position).getLocation();
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(url));
                    activity. startActivity(intent);
                }
                catch (Exception e)
                {
                    ShowUserMessage.showUserMessage(activity , e.getMessage());
                }



            }
        });

        viewHolder.avail_list_spiiner.setSelection(matchArrayList.get(position).getListItemPos() , true);





        viewHolder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                addSchdulerFragment.showDialogforEditSchdule(position);


            }
        });






        SimpleDateFormat dateTimeWithtwelHour = new SimpleDateFormat("MM-dd-yyyy hh:mm aa");
        Calendar schduleTimeCal = Calendar.getInstance(Locale.ENGLISH);
        try {
            schduleTimeCal.setTime(dateTimeWithtwelHour.parse(matchArrayList.get(position).getDate()+" "+matchArrayList.get(position).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.edit_btn.setVisibility(View.VISIBLE);

        if (schduleTimeCal.compareTo(currentcalendar) >= 0)
        {
            viewHolder.avail_list_spiiner.setEnabled(true);
            viewHolder.avail_list_spiiner.setBackground(activity.getResources().getDrawable(R.drawable.gray_border_back));
            viewHolder.avail_list_spiiner.setAlpha(1.0f);
            viewHolder.table_down_arrow.setVisibility(View.VISIBLE);


        }
        else
        {
            viewHolder.avail_list_spiiner.setBackground(activity.getResources().getDrawable(R.drawable.dark_gray_back));

            viewHolder.avail_list_spiiner.setEnabled(false);
            viewHolder.avail_list_spiiner.setAlpha(0.6f);
            viewHolder.table_down_arrow.setVisibility(View.GONE);



        }



        return convertView;
    }

    public class ViewHolder
    {

        TextView date_tv;
        TextView location_tv;
        CustomSpinnerAdapter customSpinnerAdapter ;
        Spinner avail_list_spiiner ;
        ImageView edit_btn;
        ImageView table_down_arrow;

        TextView opponent_name_tv;

    }

    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Activity activity;
        private ArrayList<String> asr;

        public CustomSpinnerAdapter(Activity context, ArrayList<String> asr ,final Spinner spinner) {
            this.asr=asr;
            activity = context;

            spinner.post(new Runnable(){
                public void run(){
                    int height = spinner.getHeight();
                    int diff = DeviceInfo.getDensity(activity) /80;
                    spinner.setDropDownVerticalOffset(height +5);
                }
            });
        }



        public int getCount()
        {
            return asr.size();
        }

        public Object getItem(int i)
        {
            return asr.get(i);
        }

        public long getItemId(int i)
        {
            return (long)i;
        }



        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(activity);

            if (position == 0)
            {
                txt.setVisibility(View.GONE);
                txt.setPadding(0, 0, 0, 0);
                txt.setTextSize(0);
            }
            else
            {
                txt.setVisibility(View.VISIBLE);
                txt.setPadding(10, 12, 12, 8);
                txt.setTextSize(11);
            }



            txt.setSingleLine();
            txt.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(activity);
            txt.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);





            txt.setPadding(10, 12, 5, 12);

            if (i == 0)
            txt.setTextSize(11);
            else
                txt.setTextSize(11);
            txt.setSingleLine();
            try
            {
                txt.setText(asr.get(i));
            }
            catch (Exception e)
            {
                txt.setText(asr.get(0));
            }

            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }

    }






}
