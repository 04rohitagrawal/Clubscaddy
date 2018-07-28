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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.clubscaddy.Bean.MatchDetail;
import com.clubscaddy.utility.DeviceInfo;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.R;
import com.clubscaddy.custumview.CustomSpinner;
import com.clubscaddy.fragment.SchduleDetailFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by administrator on 20/6/17.
 */

public class LeagueListAdapter extends BaseAdapter
{
 Activity activity ;
    ArrayList<MatchDetail>matchDetailArrayList ;
    ArrayList<String>avilableList ;


    SimpleDateFormat simpleTimeFormat ,serverTimeFormate ;
    SimpleDateFormat simpleDateFormat , serverDateFormate , datetimeformate ;
    SimpleDateFormat dateFormatWithDays ;
    SchduleDetailFragment fragment;
    boolean isCallWebServices = false;

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
        //2017-08-02 19:03:00
        datetimeformate = new SimpleDateFormat("MM-dd-yyyy");
        try {
        SimpleDateFormat    currenttimeformate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            currentcalendar = Calendar.getInstance(Locale.ENGLISH);
            currentcalendar.setTime(currenttimeformate.parse(currentTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    String currentTime;
    Calendar currentcalendar ;



    public   LeagueListAdapter(SchduleDetailFragment fragment , ArrayList<MatchDetail>matchDetailArrayList, String currentTime)
    {
      this.fragment = fragment ;
        this.activity = fragment.getActivity() ;

        this.matchDetailArrayList = matchDetailArrayList ;
        avilableList = new ArrayList<>();
        avilableList.add("No Response");
        avilableList.add("Yes");
        avilableList.add("No");
        avilableList.add("Maybe");
        avilableList.add("Call last");

        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        serverDateFormate = new SimpleDateFormat("yyyy-MM-dd");
        simpleTimeFormat = new SimpleDateFormat("hh:mm aa");
        serverTimeFormate = new SimpleDateFormat("HH:mm:ss");
        dateFormatWithDays = new SimpleDateFormat("EEEE dd MMMM yyyy");




    }
    @Override
    public int getCount() {
        return matchDetailArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return matchDetailArrayList.get(position);
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.league_list_adapter_layout , null);
            viewHolder = new ViewHolder();

            viewHolder.league_date_tv = (TextView) convertView.findViewById(R.id.league_date_tv);
            viewHolder.league_location_tv = (TextView) convertView.findViewById(R.id.league_location_tv);
            viewHolder.avail_list_spiiner = (CustomSpinner) convertView.findViewById(R.id.avail_list_spiiner);
            viewHolder.customSpinnerAdapter = new CustomSpinnerAdapter(activity , avilableList , viewHolder.avail_list_spiiner);



            viewHolder.total_yes_member_tv = (TextView) convertView.findViewById(R.id.total_yes_member_tv);
            viewHolder.total_no_member_tv = (TextView) convertView.findViewById(R.id.total_no_member_tv);
            viewHolder.total_maybe_member_tv = (TextView) convertView.findViewById(R.id.total_maybe_member_tv);
            viewHolder.total_lastcall_member_tv = (TextView) convertView.findViewById(R.id.total_lastcall_member_tv);
            viewHolder.total_not_responded_member_tv = (TextView) convertView.findViewById(R.id.total_not_responded_member_tv);




            viewHolder.yes_btn_linear_layout = (LinearLayout) convertView.findViewById(R.id.yes_btn_linear_layout);
            viewHolder.total_maybe_btn_linear_layout = (LinearLayout) convertView.findViewById(R.id.total_maybe_btn_linear_layout);
            viewHolder.no_btn_linear_layout = (LinearLayout) convertView.findViewById(R.id.no_btn_linear_layout);
            viewHolder.lastcall_btn_linear_layout = (LinearLayout) convertView.findViewById(R.id.lastcall_btn_linear_layout);
            viewHolder.norecoed_btn_linear_layout = (LinearLayout) convertView.findViewById(R.id.norecoed_btn_linear_layout);

            viewHolder.opponent_name_tv = (TextView) convertView.findViewById(R.id.opponent_name_tv);

           viewHolder.table_down_arrow = (ImageView) convertView.findViewById(R.id.table_down_arrow);




            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        HashMap<String , Integer>param = matchDetailArrayList.get(position).getMatchAllUserStatus();

        MatchDetail match = matchDetailArrayList.get(position);

        int pos = position;

        SimpleDateFormat myServerDateFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar =Calendar.getInstance(Locale.ENGLISH);
        try {
            Date date = myServerDateFormate.parse(matchDetailArrayList.get(position).getMatch_createAt());
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        try
        {
            String dateStr = matchDetailArrayList.get(position).getMatch_date();

            Date myDate = dateFormatWithDays.parse(dateStr);


            viewHolder.league_date_tv.setText(datetimeformate.format(myDate)+"\n"+matchDetailArrayList.get(position).getMatch_time());
        }
        catch (Exception e)
        {

        }













        viewHolder.opponent_name_tv.setText(matchDetailArrayList.get(position).getMatch_opponet());

        viewHolder.league_location_tv.setText(matchDetailArrayList.get(position).getMatch_location());
        viewHolder.avail_list_spiiner.setAdapter(viewHolder.customSpinnerAdapter);

        viewHolder.total_yes_member_tv.setText(param.get("total_yes")+"");
        viewHolder.total_no_member_tv.setText(param.get("total_no")+"");
        viewHolder.total_maybe_member_tv.setText(param.get("total_later")+"");
        viewHolder.total_lastcall_member_tv.setText(param.get("total_last_call")+"");
        viewHolder.total_not_responded_member_tv.setText(param.get("not_responding")+"");

        viewHolder.yes_btn_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragment.showDialogforUserStatus(matchDetailArrayList.get(position).getMatchAllMemberList() , 1 ,"Yes  respond");
            }
        });

        viewHolder.no_btn_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragment.showDialogforUserStatus(matchDetailArrayList.get(position).getMatchAllMemberList() ,2 ,"No  respond");
            }
        });

        viewHolder.total_maybe_btn_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragment.showDialogforUserStatus(matchDetailArrayList.get(position).getMatchAllMemberList() , 3 ,"Maybe  respond");
            }
        });

        viewHolder.lastcall_btn_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragment.showDialogforUserStatus(matchDetailArrayList.get(position).getMatchAllMemberList() , 4 ,"Last Call  respond");
            }
        });


        viewHolder.norecoed_btn_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragment.showDialogforUserStatus(matchDetailArrayList.get(position).getMatchAllMemberList() ,0 ,"No  response");
            }
        });


        SimpleDateFormat dateTimeWithtwelHour = new SimpleDateFormat("EEEE dd MMMM yyyy hh:mm aa");
        Calendar schduleTimeCal = Calendar.getInstance(Locale.ENGLISH);
        try {
            schduleTimeCal.setTime(dateTimeWithtwelHour.parse(match.getMatch_date()+" "+match.getMatch_time()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

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



        viewHolder.avail_list_spiiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                matchDetailArrayList.get(position).setListItemPos(pos);



                if (isCallWebServices == true)
                {
                    isCallWebServices = false ;
                    fragment.changeMatchStatus((pos)+"" ,""+ matchDetailArrayList.get(position).getMatch_id());

                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                matchDetailArrayList.get(position).setListItemPos(0);
            }
        });
        /*viewHolder.avail_list_spiiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int pos, long id) {


            }
        });*/

        if (matchDetailArrayList.get(position).getListItemPos() != 0)
        {

            viewHolder.avail_list_spiiner.setSelection(matchDetailArrayList.get(position).getListItemPos() , true);
        }


        viewHolder.league_location_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    String url = "http://maps.google.com/maps?daddr="+ matchDetailArrayList.get(position).getMatch_location();
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(url));
                    activity. startActivity(intent);
                }
                catch (Exception e)
                {
                    ShowUserMessage.showUserMessage(activity , e.getMessage());
                }



            }
        });


        return convertView;
    }

    public class ViewHolder
    {
        TextView league_date_tv;
        TextView league_location_tv;
        CustomSpinner avail_list_spiiner;
        CustomSpinnerAdapter customSpinnerAdapter ;
        TextView total_yes_member_tv ;
        TextView total_no_member_tv;
        TextView total_maybe_member_tv;
        TextView total_lastcall_member_tv;
        TextView total_not_responded_member_tv ;


        LinearLayout yes_btn_linear_layout;
        LinearLayout total_maybe_btn_linear_layout ;
        LinearLayout no_btn_linear_layout;
        LinearLayout lastcall_btn_linear_layout;
        LinearLayout norecoed_btn_linear_layout;
        ImageView table_down_arrow;

        TextView opponent_name_tv;

    }

    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Activity activity;
        private ArrayList<String> asr;
        CustomSpinner customSpinner ;
        public CustomSpinnerAdapter(Activity context, ArrayList<String> asr ,final CustomSpinner customSpinner) {
            this.asr=asr;
            activity = context;
            this.customSpinner = customSpinner;

            customSpinner.post(new Runnable(){
                public void run(){
                    int height = customSpinner.getHeight();
                    customSpinner.setDropDownVerticalOffset(height +5);
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
        public View getDropDownView(final int position, View convertView, ViewGroup parent) {
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
            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                    customSpinner.onDetachedFromWindow();
                    isCallWebServices = true;
                    customSpinner.setSelection(position , true);
                }
            });

            return  txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(activity);
            txt.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);


            txt.setSingleLine();
            try
            {
                if(i == 0)
                {
                    txt.setTextSize(8);
                    txt.setPadding(5, 12, 5, 12);
                    txt.setText(asr.get(0));
                }
                else
                {
                    txt.setPadding(10, 12, 5, 12);
                    txt.setTextSize(11);
                    txt.setText(asr.get(i));                }

            }
            catch (Exception e)
            {
                txt.setTextSize(8);
                txt.setPadding(0, 12, 0, 12);
                txt.setText(asr.get(0));
            }

            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }

    }


}
