package com.clubscaddy.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.clubscaddy.Adapter.CustomSpinnerAdapter;
import com.clubscaddy.Adapter.MatchListAdapter;
import com.clubscaddy.Bean.GroupBean;
import com.clubscaddy.Bean.Match;
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
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by administrator on 22/5/17.
 */

public class AddSchdulerFragment extends Fragment implements View.OnClickListener
{
    View convertView;
    RadioButton all_member_radio_btn;
    RadioButton group_member_radio_btn;
    RelativeLayout spinner_layout;
    Spinner group_item_spinner;
    Button add_group_tv;
    ArrayList<GroupBean> groupArrayList;
    ShowUserMessage showUserMessage ;
    HttpRequest httpRequest ;
    Calendar currentDate ;
    ArrayList<Match>matchArrayList ;
    SimpleDateFormat simpleTimeFormat ,serverTimeFormate ;
    Button add_match_btn;
    SimpleDateFormat simpleDateFormat , serverDateFormate ;

    Calendar selectedDate ;

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {




        }
    };
    CustomSpinnerAdapter customSpinnerAdapter;
    ArrayList<String> groupNameList;
    EditText scheduler_name;
    EditText scheduler_description;
    ImageView submit_btn_iv;
    EditText date_edit_txt;
    EditText time_edit_tv;
    Button add_schduler_button;
    TextView discription_textview_status;
    Spinner spinner;
    public ListView schdule_list_view;
    MatchListAdapter matchListAdapter ;
    EditText classfieb_title_tv;
    ManageGroupFragment.AddNewSchduleListner addNewSchduleListner ;
    Calendar currentTime;

    public void setInstanse(ArrayList<GroupBean> groupArrayList ,ManageGroupFragment.AddNewSchduleListner addNewSchduleListner , String currentTimeText) {
        this.groupArrayList = groupArrayList;
        this.addNewSchduleListner = addNewSchduleListner;
        currentTime = Calendar.getInstance(Locale.ENGLISH);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            currentTime.setTime(simpleDateFormat.parse(currentTimeText));


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.add_schdule_layout, null);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        schdule_list_view = (ListView) convertView.findViewById(R.id.schdule_list_view);
        submit_btn_iv = (ImageView) convertView.findViewById(R.id.submit_btn_iv);
        classfieb_title_tv = (EditText) convertView.findViewById(R.id.classfieb_title_tv);

        DirectorFragmentManageActivity.updateTitle("Add New Schedule");
        submit_btn_iv.setOnClickListener(this);
      //  inslizingView(convertView);

      //  setDataOnView();
        simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        serverDateFormate = new SimpleDateFormat("yyyy-MM-dd");
        simpleTimeFormat = new SimpleDateFormat("hh:mm aa");
        serverTimeFormate = new SimpleDateFormat("HH:mm:ss");
        currentDate = Calendar.getInstance(Locale.ENGLISH);
        add_match_btn = (Button) convertView.findViewById(R.id.add_match_btn);

        add_match_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogforAddSchdule();            }
        });

        matchArrayList = new ArrayList<>();
       // matchListAdapter = new MatchListAdapter(AddSchdulerFragment.this , matchArrayList);
       // schdule_list_view.setAdapter(matchListAdapter);








        return convertView;
    }



    TextView cancel_btn_tv;
    TextView done_btn_tv;
    Dialog dialog;
    EditText location_edit_tv;
    EditText opponent_name_tv;
    TextView delete_btn_tv;

    public void showDialogforAddSchdule()
    {
         dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_schdule_dialog);
        dialog.getWindow().setLayout(AppConstants.getDeviceWidth(getActivity()), LinearLayout.LayoutParams.WRAP_CONTENT);

        date_edit_txt = (EditText) dialog.findViewById(R.id.date_edit_txt);
        time_edit_tv = (EditText) dialog.findViewById(R.id.time_edit_tv);
        location_edit_tv = (EditText) dialog.findViewById(R.id.location_edit_tv);
        cancel_btn_tv = (TextView) dialog.findViewById(R.id.cancel_btn_tv);
        done_btn_tv = (TextView) dialog.findViewById(R.id.done_btn_tv);
        delete_btn_tv = (TextView) dialog.findViewById(R.id.delete_btn_tv);
        delete_btn_tv.setVisibility(View.GONE);
        opponent_name_tv = (EditText) dialog.findViewById(R.id.opponent_name_tv);
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

                if (Validation.isStringNullOrBlank(opponent_name_tv.getText().toString()))
                {
                    ShowUserMessage.showUserMessage(getActivity() , "Please enter opponent name");
                    return;
                }


                Match match = new Match();
                int listSize = matchArrayList.size();
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


                matchArrayList.add(match);
                addHeader();


                matchListAdapter.notifyDataSetChanged();



                ExpandableHeightListView.getListViewWithheaderSize(schdule_list_view, layoutHeight);

                dialog.cancel();
            }
        });


        dialog.show();
    }






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

        }


        calenderGridView.showCalendar();
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
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                AddSchdulerFragment.this.hourOfDay = hourOfDay ;
                AddSchdulerFragment.this.minute = minute ;
            }
        });


        ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.dismiss();;

                Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                calendar.set(Calendar.HOUR_OF_DAY , AddSchdulerFragment.this.hourOfDay);
                calendar.set(Calendar.MINUTE , AddSchdulerFragment.this.minute);


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
        final TextView dialog_header_tv = (TextView) dialog.findViewById(R.id.dialog_header_tv);
        dialog_header_tv.setText("Edit Match");
        opponent_name_tv = (EditText) dialog.findViewById(R.id.opponent_name_tv);
        delete_btn_tv = (TextView) dialog.findViewById(R.id.delete_btn_tv);
        selectedDate = null ;

        delete_btn_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                ShowUserMessage showUserMessage = new ShowUserMessage(getActivity());
                showUserMessage.showDialogBoxWithYesNoButton("Are you sure you want to delete this match?", new DialogBoxButtonListner() {
                    @Override
                    public void onYesButtonClick(DialogInterface dialogs) {
                        matchArrayList.remove(position);
                        matchListAdapter.notifyDataSetChanged();
                        ExpandableHeightListView.getListViewWithheaderSize(schdule_list_view, layoutHeight);
                        removeHeader();
                        dialogs.cancel();
                        dialog.cancel();
                    }
                });


            }
        });


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


                    Utill.showDialg("Match time been passed" , getActivity());
                    return;


                }



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
    LinearLayout headerLayout ;
    int layoutHeight ;

    public void addHeader()
    {
        if (matchArrayList.size() == 1)
        {

             headerLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.schdule_list_headed , null);
            schdule_list_view.addHeaderView(headerLayout);
            matchListAdapter = new MatchListAdapter(AddSchdulerFragment.this , matchArrayList);
            schdule_list_view.setAdapter(matchListAdapter);
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

                }
            });


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


    @Override
    public void onClick(View v)
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

        HashMap<String , Object>param = new HashMap<>();
        param.put("league_uid" , SessionManager.getUser_id(getActivity()));
        param.put("league_name" ,classfieb_title_tv.getText().toString());
        param.put("club_id" ,SessionManager.getUser_Club_id(getActivity()));



        JSONArray matchListJsonArray = new JSONArray();



        try
        {
            for (int i = 0 ; i < matchArrayList.size() ; i++)
            {
                JSONObject matchListJsonArrayJsonObject = new JSONObject();
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
        httpRequest = new HttpRequest(getActivity());

        httpRequest.getResponse(getActivity(), WebService.create_schedule, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {

                try
                {

                    if (jsonObject.getBoolean("status"))
                    {

                        showMessageForFragmeneWithBack(getActivity() , jsonObject.getString("message"));
                    }
                    else
                    {
                        ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("message"));

                    }

                }
                catch (Exception e)
                {
                    ShowUserMessage.showUserMessage(getActivity() , e.getMessage());
                }


            }
        });



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

    public  void showMessageForFragmeneWithBack(final FragmentActivity fragmentActivity , String msg)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(
                fragmentActivity).create();


        // Setting Dialog Title
        alertDialog.setTitle(SessionManager.getClubName(fragmentActivity));
        alertDialog.setCancelable(false);
        // Setting Dialog Message
        alertDialog.setMessage(msg);


        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                fragmentActivity.getSupportFragmentManager().popBackStack();
                addNewSchduleListner.onSuccess();
                dialog.cancel();
            }
        });


        alertDialog.show();
    }
}



