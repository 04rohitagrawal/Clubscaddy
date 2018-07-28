package com.clubscaddy.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.clubscaddy.Adapter.EditMatchListAdapter;
import com.clubscaddy.Bean.Match;
import com.clubscaddy.Bean.MatchDetail;
import com.clubscaddy.Interface.DialogBoxButtonListner;
import com.clubscaddy.custumview.ExpandableHeightListView;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.CalenderTabClickListener;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.R;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.utility.CalenderGridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by administrator on 21/6/17.
 */

public class EditSchduleFragment  extends Fragment
{

    View convertView ;
    Bundle bundle ;
    EditText classfieb_title_tv;
    Button add_match_btn;
    SchduleDetailFragment.FragmentCommunicate fragmentCommunicate ;
    ArrayList<MatchDetail>matchDetailArrayList ;
    ArrayList<MatchDetail>copymatchDetailArrayList ;

    ArrayList<Match>matchArrayList ;
    public ListView schdule_list_view;
    EditMatchListAdapter matchListAdapter ;



    ImageView submit_btn_iv;
    EditText date_edit_txt;
    EditText time_edit_tv;
    Calendar currentDate ;
    SimpleDateFormat simpleTimeFormat ,serverTimeFormate ;
    SimpleDateFormat simpleDateFormat , serverDateFormate ;
    SimpleDateFormat dateFormatWithDays ;
HttpRequest httpRequest ;

    String leagueId ;
    String leagueName ;
    String leagueUserId ;

    JSONArray editMatchIdJsonArray ;

    SchduleDetailFragment.EditSchudlerListner editSchudlerListner ;

    public boolean isAnyMatchEdited = false ;


    int layoutHeight ;

    ListView userstatus_list_view;
   public String delete_match_id = "";


    public void registerEditSchduleListner( SchduleDetailFragment.EditSchudlerListner editSchudlerListner )
    {
       this.editSchudlerListner = editSchudlerListner ;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.add_schdule_layout , null);
        bundle = getArguments();
        DirectorFragmentManageActivity.updateTitle("Edit Schedule");
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        delete_match_id = "";
        isAnyMatchEdited = false ;
        inislizedViewAndData(convertView);


        return convertView;
    }



    String serverDataTime ;

    public void inislizedViewAndData(View convertView)
    {
        simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        serverDateFormate = new SimpleDateFormat("yyyy-MM-dd");
        simpleTimeFormat = new SimpleDateFormat("hh:mm aa");
        serverTimeFormate = new SimpleDateFormat("HH:mm:ss");
        dateFormatWithDays = new SimpleDateFormat("EEEE dd MMMM yyyy");

        leagueId = bundle.getString("league_id");
        leagueName = bundle.getString("league_name");
        leagueUserId = bundle.getString("league_uid");
        serverDataTime = bundle.getString("serverDataTime");


        submit_btn_iv = (ImageView) convertView.findViewById(R.id.submit_btn_iv);

        submit_btn_iv.setOnClickListener(submitClickListener);

        classfieb_title_tv = (EditText) convertView.findViewById(R.id.classfieb_title_tv);
        add_match_btn = (Button) convertView.findViewById(R.id.add_match_btn);
        schdule_list_view = (ListView) convertView.findViewById(R.id.schdule_list_view);




        classfieb_title_tv.setText(bundle.getString("league_name"));
       matchDetailArrayList = (ArrayList<MatchDetail>) bundle.getSerializable("matchDetailList");
        copymatchDetailArrayList = (ArrayList<MatchDetail>) matchDetailArrayList.clone();


        matchArrayList = new ArrayList<>();
        httpRequest = new HttpRequest(getActivity());


        for (int i = 0 ; i < matchDetailArrayList.size() ;i++)
        {
            Match match = new Match();
            match.setMatch_id(matchDetailArrayList.get(i).getMatch_id());
            match.setMatch_opponet(matchDetailArrayList.get(i).getMatch_opponet());
            Date myDateCal ,myTimeCal;
            try {
                 myDateCal = dateFormatWithDays.parse(matchDetailArrayList.get(i).getMatch_date()) ;
                myTimeCal = simpleTimeFormat.parse(matchDetailArrayList.get(i).getMatch_time()) ;
                match.setDate(simpleDateFormat.format(myDateCal));
                match.setServerDate(serverDateFormate.format(myDateCal));
                match.setServerTime(serverTimeFormate.format(myTimeCal));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            match.setTime(matchDetailArrayList.get(i).getMatch_time());
            match.setLocation(matchDetailArrayList.get(i).getMatch_location());
            match.setListItemPos(matchDetailArrayList.get(i).getListItemPos());

            matchArrayList.add(match);

        }


        headerLayout = (LinearLayout) LayoutInflater.from(getActivity()).
                inflate(R.layout.schdule_list_headed , null);
        schdule_list_view.addHeaderView(headerLayout);


        ViewTreeObserver vto = headerLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    headerLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    headerLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width  = headerLayout.getMeasuredWidth();
                int height = headerLayout.getHeight();
                layoutHeight = height ;
                ExpandableHeightListView.getListViewWithheaderSize(schdule_list_view,layoutHeight);


            }
        });

        matchListAdapter = new EditMatchListAdapter(EditSchduleFragment.this , matchArrayList , serverDataTime);
        schdule_list_view.setAdapter(matchListAdapter);

        add_match_btn.setOnClickListener(addMatchButtonClickLisntener);



        currentDate = Calendar.getInstance(Locale.ENGLISH);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            currentDate.setTime(simpleDateFormat.parse(serverDataTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public View.OnClickListener addMatchButtonClickLisntener = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {

            showDialogforAddSchdule();

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    LinearLayout headerLayout ;
    public void addHeader()
    {
        if (matchArrayList.size() == 1)
        {

            schdule_list_view = (ListView) convertView.findViewById(R.id.schdule_list_view);


            headerLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.schdule_list_headed , null);
            schdule_list_view.addHeaderView(headerLayout);

            matchListAdapter = new EditMatchListAdapter(EditSchduleFragment.this , matchArrayList , serverDataTime);
            schdule_list_view.setAdapter(matchListAdapter);

        }

    }
    public void removeHeader()
    {
        if (matchArrayList.size() == 0)
        {
            // headerLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.schdule_list_headed , null);
            schdule_list_view.removeHeaderView(headerLayout);
            ExpandableHeightListView.getListViewWithheaderSize(schdule_list_view,layoutHeight);
        }

    }
    TextView cancel_btn_tv;
    TextView done_btn_tv;
    Dialog dialog;
    EditText location_edit_tv;
    EditText opponent_name_tv;
    TextView delete_btn_tv ;

    public void showDialogforAddSchdule()
    {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_schdule_dialog);
        dialog.getWindow().setLayout(AppConstants.getDeviceWidth(getActivity()), LinearLayout.LayoutParams.WRAP_CONTENT);

        date_edit_txt = (EditText) dialog.findViewById(R.id.date_edit_txt);
        //date_edit_txt.setText(simpleDateFormat.format(currentDate.getTime()));
        time_edit_tv = (EditText) dialog.findViewById(R.id.time_edit_tv);
       // time_edit_tv.setText(simpleTimeFormat.format(currentDate.getTime()));

        location_edit_tv = (EditText) dialog.findViewById(R.id.location_edit_tv);
        cancel_btn_tv = (TextView) dialog.findViewById(R.id.cancel_btn_tv);
        done_btn_tv = (TextView) dialog.findViewById(R.id.done_btn_tv);
        opponent_name_tv = (EditText) dialog.findViewById(R.id.opponent_name_tv);
        delete_btn_tv = (TextView) dialog.findViewById(R.id.delete_btn_tv);
        delete_btn_tv.setVisibility(View.GONE);
        selectedDate = null ;

        date_edit_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDateDialog();
            }
        });



        time_edit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showTimeDialog();
            }
        });

        cancel_btn_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
            }
        });



        done_btn_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Validation.isStringNullOrBlank(date_edit_txt.getText().toString()))
                {

                    ShowUserMessage.showUserMessage(getActivity() , "Please select date");
                    return;
                }


                if (Validation.isStringNullOrBlank(time_edit_tv.getText().toString()))
                {

                    ShowUserMessage.showUserMessage(getActivity() , "Please select time");
                    return;
                }



                if (Validation.isStringNullOrBlank(location_edit_tv.getText().toString()))
                {

                    ShowUserMessage.showUserMessage(getActivity() , "Please enter location");
                    return;
                }

                SimpleDateFormat compareDateTimeFormate = new SimpleDateFormat("MM-dd-yyyy HH:mm");
                Calendar compareDate = Calendar.getInstance(Locale.ENGLISH);

                try {
                    compareDate.setTime(compareDateTimeFormate.parse(date_edit_txt.getText().toString() +" "+EditSchduleFragment.this.hourOfDay +":"+EditSchduleFragment.this.minute));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar minimumDate = (Calendar) currentDate.clone();
                minimumDate.add(Calendar.DATE ,1);

                if (Utill.compareTwoDate(minimumDate ,compareDate) )
                {


                    Utill.showDialg("Match time been passed" , getActivity());
                    return;


                }


                Match match = new Match();
                int listSize = matchArrayList.size();
                match.setMatch_id(0);
                match.setSno((listSize)+"");
                match.setDate(date_edit_txt.getText().toString());
                match.setTime(time_edit_tv.getText().toString());
                match.setLocation(location_edit_tv.getText().toString());
                match.setMatch_opponet(opponent_name_tv.getText().toString());


                try {
                    match.setServerDate(serverDateFormate.format(simpleDateFormat.parse(date_edit_txt.getText().toString())));
                    match.setServerTime(serverTimeFormate.format(simpleTimeFormat.parse(time_edit_tv.getText().toString())));


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                match.setListItemPos(0);

                matchArrayList.add(match);

                matchListAdapter.notifyDataSetChanged();


                addHeader();

                ExpandableHeightListView.getListViewWithheaderSize(schdule_list_view,layoutHeight);

                dialog.cancel();
            }
        });


        dialog.show();
    }




   Calendar selectedDate;

    public void showDateDialog()
    {
        final Calendar minimumCal = (Calendar) currentDate.clone();
        minimumCal.add(Calendar.DATE ,1);

        CalenderGridView calenderGridView = new CalenderGridView(getActivity(), new CalenderTabClickListener() {
            @Override
            public void onTabClick(AlertDialog calendarDialogue, Calendar seletedCal)
            {
                if(Utill.compareTwoDate(minimumCal ,seletedCal))
                {
                    Utill.showDialg("Slected date greater than current date." ,getActivity());
                    return;
                }

                date_edit_txt.setText(simpleDateFormat.format(seletedCal.getTime()));
                selectedDate = (Calendar) seletedCal.clone();

                //selectedDate = myserverFormate.format(seletedCal.getTime());
                calendarDialogue.dismiss();
            }
        });


        if (selectedDate != null)
        {
            calenderGridView.setMinimumDate(selectedDate);

        }
        else
        {
            calenderGridView.setMinimumDate(minimumCal);

        }        calenderGridView.showCalendar();
    }
    TextView cancel_tv;
    TimePicker time_picker;
    TextView ok_tv;
    int hourOfDay , minute;
    public void showTimeDialog()
    {
        final Dialog timePicker = new Dialog(getActivity());
        timePicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
        timePicker.setContentView(R.layout.time_picker_dialog);
        cancel_tv = (TextView) timePicker.findViewById(R.id.cancel_tv);
        time_picker = (TimePicker) timePicker.findViewById(R.id.time_picker);

        hourOfDay = currentDate.get(Calendar.HOUR_OF_DAY);
        minute = currentDate.get(Calendar.MINUTE);



        ok_tv = (TextView) timePicker.findViewById(R.id.ok_tv);

        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.dismiss();;
            }
        });



        time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
            {

                EditSchduleFragment.this.hourOfDay = hourOfDay ;
                EditSchduleFragment.this.minute = minute ;
            }
        });


        ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                calendar.set(Calendar.HOUR_OF_DAY , EditSchduleFragment.this.hourOfDay);
                calendar.set(Calendar.MINUTE , EditSchduleFragment.this.minute);

                SimpleDateFormat compareDateTimeFormate = new SimpleDateFormat("MM-dd-yyyy HH:mm");
                Calendar compareDate = Calendar.getInstance(Locale.ENGLISH);




                try {
                    compareDate.setTime(compareDateTimeFormate.parse(date_edit_txt.getText().toString() +" "+EditSchduleFragment.this.hourOfDay +":"+EditSchduleFragment.this.minute));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                timePicker.dismiss();;
                time_edit_tv.setText(simpleTimeFormat.format(calendar.getTime()));









            }
        });

        timePicker.show();
    }




    public void showDialogforEditSchdule(final int position)
    {
        final Match match = matchArrayList.get(position);
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_schdule_dialog);
        dialog.getWindow().setLayout(AppConstants.getDeviceWidth(getActivity()), LinearLayout.LayoutParams.WRAP_CONTENT);

        date_edit_txt = (EditText) dialog.findViewById(R.id.date_edit_txt);
        time_edit_tv = (EditText) dialog.findViewById(R.id.time_edit_tv);
        location_edit_tv = (EditText) dialog.findViewById(R.id.location_edit_tv);
        cancel_btn_tv = (TextView) dialog.findViewById(R.id.cancel_btn_tv);
        done_btn_tv = (TextView) dialog.findViewById(R.id.done_btn_tv);
        TextView dialog_header_tv = (TextView) dialog.findViewById(R.id.dialog_header_tv);
        opponent_name_tv = (EditText) dialog.findViewById(R.id.opponent_name_tv);
        delete_btn_tv = (TextView) dialog.findViewById(R.id.delete_btn_tv);

        selectedDate = null ;


        dialog_header_tv.setText("Edit Match");

        date_edit_txt.setText(match.getDate());
        time_edit_tv.setText(match.getTime());
        location_edit_tv.setText(match.getLocation());
        opponent_name_tv.setText(match.getMatch_opponet());


        date_edit_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDateDialog();
            }
        });



        time_edit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showTimeDialog();
            }
        });

        cancel_btn_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
            }
        });


        delete_btn_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                ShowUserMessage showUserMessage = new ShowUserMessage(getActivity());
                showUserMessage.showDialogBoxWithYesNoButton("Are you sure you want to delete this match?", new DialogBoxButtonListner() {
                    @Override
                    public void onYesButtonClick(DialogInterface alertDialog) {

                        if (Validation.isStringNullOrBlank(delete_match_id) )
                        {
                            delete_match_id = matchArrayList.get(position).getMatch_id()+"";
                        }
                        else
                        {
                            delete_match_id =      delete_match_id+","+ matchArrayList.get(position).getMatch_id()+"";
                        }


                        matchArrayList.remove(position);
                        matchListAdapter.notifyDataSetChanged();
                        ExpandableHeightListView.getListViewWithheaderSize(schdule_list_view,layoutHeight);
                        removeHeader();
                        alertDialog.cancel();
                        dialog.cancel();
                    }
                });




            }
        });



        done_btn_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Validation.isStringNullOrBlank(date_edit_txt.getText().toString()))
                {

                    ShowUserMessage.showUserMessage(getActivity() , "Please select date");
                    return;
                }


                if (Validation.isStringNullOrBlank(time_edit_tv.getText().toString()))
                {

                    ShowUserMessage.showUserMessage(getActivity() , "Please select time");
                    return;
                }



                if (Validation.isStringNullOrBlank(location_edit_tv.getText().toString()))
                {

                    ShowUserMessage.showUserMessage(getActivity() , "Please enter location");
                    return;
                }

                if (Validation.isStringNullOrBlank(opponent_name_tv.getText().toString()))
                {
                    ShowUserMessage.showUserMessage(getActivity() , "Please enter opponent name");
                    return;
                }



                SimpleDateFormat compareDateTimeFormate = new SimpleDateFormat("MM-dd-yyyy hh:mm aa");
                Calendar compareDate = Calendar.getInstance(Locale.ENGLISH);

                try {
                    compareDate.setTime(compareDateTimeFormate.parse(date_edit_txt.getText().toString() +" "+time_edit_tv.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar minimumDate = (Calendar) currentDate.clone();
                minimumDate.add(Calendar.DATE ,1);

                if (Utill.compareTwoDate(minimumDate ,compareDate) )
                {

                    //
                    Utill.showDialg("Match time been passed" , getActivity());
                    return;


                }


                if (isAnyMatchEdited == false)
                {
                    if (match.getDate().equals(date_edit_txt.getText().toString()) == false)
                    {
                        isAnyMatchEdited = true ;
                    }
                    /*else
                    {
                        if (match.getTime().equals(time_edit_tv.getText().toString()) == false)
                        {
                            isAnyMatchEdited = true ;
                        }
                        else
                        {
                            if (match.getLocation().equals(location_edit_tv.getText().toString()) == false)
                            {
                                isAnyMatchEdited = true ;
                            }
                            else
                            {
                                if (match.getMatch_opponet().equals(opponent_name_tv.getText().toString()) == false)
                                {
                                    isAnyMatchEdited = true ;
                                }

                            }
                        }

                    }
             */   }





                ////////////

                match.setMatch_opponet(opponent_name_tv.getText().toString());
                match.setDate(date_edit_txt.getText().toString());
                match.setTime(time_edit_tv.getText().toString());
                match.setLocation(location_edit_tv.getText().toString());
                try {
                    match.setServerDate(serverDateFormate.format(simpleDateFormat.parse(date_edit_txt.getText().toString())));
                    match.setServerTime(serverTimeFormate.format(simpleTimeFormat.parse(time_edit_tv.getText().toString())));


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                matchArrayList.set(position ,  match);

                matchListAdapter.notifyDataSetChanged();



                dialog.cancel();
            }
        });


        dialog.show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

    public AdapterView.OnClickListener submitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            submitDataOnServer();
        }
    } ;

    public void submitDataOnServer()
    {
        if (Validation.isStringNullOrBlank(classfieb_title_tv.getText().toString()))
        {
            ShowUserMessage.showUserMessage( getActivity() , " Please enter league title");
            return;
        }

        if (matchArrayList.size() == 0)
        {
            ShowUserMessage.showUserMessage( getActivity() , "Please add atleast one match");
            return;
        }

        final HashMap<String , Object> param = new HashMap<>();
        param.put("league_uid" , leagueUserId);
        param.put("league_name" ,classfieb_title_tv.getText().toString());
        param.put("league_id" , leagueId);


        final JSONArray matchListJsonArray = new JSONArray();



        try
        {
            for (int i = 0 ; i < matchArrayList.size() ; i++)
            {
                JSONObject matchListJsonArrayJsonObject = new JSONObject();
                matchListJsonArrayJsonObject.put("match_id" , matchArrayList.get(i).getMatch_id()) ;
                matchListJsonArrayJsonObject.put("match_date" , matchArrayList.get(i).getServerDate()) ;
                matchListJsonArrayJsonObject.put("match_time" , matchArrayList.get(i).getServerTime()) ;
                matchListJsonArrayJsonObject.put("match_location" , matchArrayList.get(i).getLocation()) ;
                matchListJsonArrayJsonObject.put("match_user_status" , (matchArrayList.get(i).getListItemPos())+"") ;
                matchListJsonArrayJsonObject.put("match_opponet" , matchArrayList.get(i).getMatch_opponet()) ;

                matchListJsonArray.put(matchListJsonArrayJsonObject);


            }        }
        catch (Exception e)
        {

        }
        param.put("match_desc" ,matchListJsonArray.toString());
        param.put("club_id" ,SessionManager.getUser_Club_id(getActivity()));
        param.put("delete_match_id" ,delete_match_id);


        if (isAnyMatchEdited == true)
        {
            ShowUserMessage showUserMessage = new ShowUserMessage(getActivity());
            showUserMessage.showDialogBoxWithYesNoButton("Do you want to send notification to participants about the changes?", new DialogBoxButtonListner() {

                        @Override
                        public void onNoButtonClick(DialogInterface dialog) {
                            super.onNoButtonClick(dialog);
                            sendRequestOnServer(param);
                        }

                        @Override
                public void onYesButtonClick(DialogInterface dialog)
                {


                    /*editMatchIdJsonArray = new JSONArray();
                    for (int i = 0 ; i < matchArrayList.size() ;i++)
                    {

                        if ((matchArrayList.get(i).getMatch_id()) != 0)
                        {
                            for (int j = 0 ; j < copymatchDetailArrayList.size() ; j++)
                            {
                                if (matchArrayList.get(i).getMatch_id() == copymatchDetailArrayList.get(j).getMatch_id())
                                {//Friday 18 August 2017

                                    String convertDate = Utill.formattedDateFromString("MM-dd-yyyy" , "EEEE d MMMM yyyy" , matchArrayList.get(i).getDate());

                                    if (false == convertDate.equals(copymatchDetailArrayList.get(j).getMatch_date()))
                                    {
                                        editMatchIdJsonArray.put(matchArrayList.get(i).getMatch_id());


                                    }
                                }

                            }
                        }


                    }*/



                    param.put("noti_status" ,leagueId);
                    sendRequestOnServer(param);

                }
            }


            );



      }
      else
        {
            sendRequestOnServer(param);
        }


    }

    public void sendRequestOnServer(HashMap<String , Object> param)
    {
        httpRequest.getResponse(getActivity(), WebService.edit_schedule, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(final JSONObject jsonObject)
            {
                Log.e("json obj" , jsonObject+"");
                ShowUserMessage.showUserMessage(getActivity() , jsonObject.optString("message"));


                if (jsonObject.optBoolean("status"))
                {

                    getActivity().getSupportFragmentManager().popBackStack();
                    editSchudlerListner.onEditSchdule(jsonObject);
                }




            }




        });

    }


}
