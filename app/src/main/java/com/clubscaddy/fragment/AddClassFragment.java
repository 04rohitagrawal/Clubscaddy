package com.clubscaddy.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.clubscaddy.Adapter.ClassDurationSpinner;
import com.clubscaddy.Adapter.SelectCategoryListAdapter;
import com.clubscaddy.Adapter.WeekListAdapter;
import com.clubscaddy.Bean.RecursiveBookingCourtColour;
import com.clubscaddy.Bean.Week;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.CalenderTabClickListener;
import com.clubscaddy.Interface.FragmentBackResponseListener;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.Interface.TimePickerListener;
import com.clubscaddy.R;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.utility.CalenderGridView;
import com.clubscaddy.utility.CusTomTimePickerDialog;
import com.clubscaddy.utility.DeviceInfo;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by administrator on 4/12/17.
 */

public class AddClassFragment extends Fragment implements View.OnClickListener
{

   View convertView ;

    EditText enterStartDateBtn ;
boolean isFullClassVailableIList ;

    FragmentBackResponseListener updateclassListListener ;
    Bundle bundle ;

    Activity activity ;
    Spinner selectDurationSpinner ;
    EditText startTimeEditText ;
    EditText endTimeEditText ;

    HttpRequest httpRequest ;
    EditText classTitleEditView ;


SessionManager sessionManager ;
    ArrayList<RecursiveBookingCourtColour> recursiveBookingCourtColourArrayList ;

    Calendar startTimeCalender , endTimeCalender ;

    Spinner colorCodeSpinner ;
    EditText costEditTv ;
    EditText participantEditTv ;

    ImageButton addClassBtn ;
    RecyclerView selectWeekSpinner ;
    ArrayList<Week> weekArrayList ;


    ClassDurationSpinner classDurationSpinner ;

    TextView cost_text_view ;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.add_class_layout , null);
        DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.INVISIBLE);
        DirectorFragmentManageActivity.actionbar_titletext.setText("Add Class");

        weekArrayList = new ArrayList<>();

        cost_text_view = (TextView) convertView.findViewById(R.id.cost_text_view);
        sessionManager = new SessionManager(getActivity());

        cost_text_view.setText("Cost("+sessionManager.getCurrencyCode(getActivity())+")");

        activity = getActivity() ;
        addClassBtn = (ImageButton) convertView.findViewById(R.id.add_class_btn);

        classTitleEditView = (EditText) convertView.findViewById(R.id.class_title_edit_view);

        selectWeekSpinner = (RecyclerView) convertView.findViewById(R.id.select_week_spinner);

        selectWeekSpinner.setLayoutManager(new LinearLayoutManager(getActivity() , LinearLayoutManager.HORIZONTAL , false));

        costEditTv = (EditText) convertView.findViewById(R.id.cost_edit_tv);

        httpRequest = new HttpRequest(activity);

        //priodeEditTv = (EditText) convertView.findViewById(R.id.priode_edit_tv);

        participantEditTv = (EditText) convertView.findViewById(R.id.participant_edit_tv);

        startTimeCalender = Calendar.getInstance(Locale.ENGLISH);
        endTimeCalender = Calendar.getInstance(Locale.ENGLISH);


        colorCodeSpinner = (Spinner) convertView.findViewById(R.id.color_code_spinner);


        selectDurationSpinner = (Spinner) convertView.findViewById(R.id.select_duration_spinner);

        enterStartDateBtn = (EditText) convertView.findViewById(R.id.enter_start_date_btn);
        enterStartDateBtn.setOnClickListener(this);


        addClassBtn.setOnClickListener(this);
         classDurationSpinner = new ClassDurationSpinner(getActivity() , selectDurationSpinner);
        selectDurationSpinner.setAdapter(classDurationSpinner);

        startTimeEditText = (EditText) convertView.findViewById(R.id.start_time_edit_text);
        endTimeEditText = (EditText) convertView.findViewById(R.id.end_time_edit_text);
        startTimeEditText.setOnClickListener(this);
        endTimeEditText.setOnClickListener(this);


        recursiveBookingCourtColourArrayList = new ArrayList<>();

        RecursiveBookingCourtColour recursiveBookingCourtColour = new RecursiveBookingCourtColour();
        recursiveBookingCourtColour.setCat_id(0);
        recursiveBookingCourtColour.setCat_color("#000000");
        recursiveBookingCourtColour.setCat_name("Black");
        recursiveBookingCourtColourArrayList.add(recursiveBookingCourtColour);


         recursiveBookingCourtColour = new RecursiveBookingCourtColour();
        recursiveBookingCourtColour.setCat_id(0);
        recursiveBookingCourtColour.setCat_color("#800000");
        recursiveBookingCourtColour.setCat_name("Maroon");
        recursiveBookingCourtColourArrayList.add(recursiveBookingCourtColour);



         recursiveBookingCourtColour = new RecursiveBookingCourtColour();
        recursiveBookingCourtColour.setCat_id(0);
        recursiveBookingCourtColour.setCat_color("#FFA500");
        recursiveBookingCourtColour.setCat_name("Orange");
        recursiveBookingCourtColourArrayList.add(recursiveBookingCourtColour);



         recursiveBookingCourtColour = new RecursiveBookingCourtColour();
        recursiveBookingCourtColour.setCat_id(0);
        recursiveBookingCourtColour.setCat_color("#bc8f8f");
        recursiveBookingCourtColour.setCat_name("Brown");
        recursiveBookingCourtColourArrayList.add(recursiveBookingCourtColour);



         recursiveBookingCourtColour = new RecursiveBookingCourtColour();
        recursiveBookingCourtColour.setCat_id(0);
        recursiveBookingCourtColour.setCat_color("#ff69b4");
        recursiveBookingCourtColour.setCat_name("Pink");
        recursiveBookingCourtColourArrayList.add(recursiveBookingCourtColour);


        SelectCategoryListAdapter selectCategoryListAdapter = new SelectCategoryListAdapter(getActivity() , recursiveBookingCourtColourArrayList);
        colorCodeSpinner.setAdapter(selectCategoryListAdapter);


     /*   The 7-day week is the international standard week (ISO 8601) used by the majority of the world.

            Illustration image
        Calendars help keep track of the days

©iStockphoto.com/nilsz

        Starts Monday or Sunday
        According to international standard ISO 8601,  is the first day of the week. It is followed by , , , , and .  i
*/        Week week = new Week();
        week.setWeekName("Sun");
        week.setIndex(7);
        weekArrayList.add(week);

        week = new Week();
        week.setWeekName("Mon");
        week.setIndex(1);

        weekArrayList.add(week);



        week = new Week();
        week.setIndex(2);

        week.setWeekName("Tue");
        weekArrayList.add(week);



        week = new Week();
        week.setIndex(3);

        week.setWeekName("Wed");
        weekArrayList.add(week);



        week = new Week();
        week.setIndex(4);

        week.setWeekName("Thu");
        weekArrayList.add(week);



        week = new Week();
        week.setIndex(5);

        week.setWeekName("Fri");
        weekArrayList.add(week);




        week = new Week();
        week.setIndex(6);

        week.setWeekName("Sat");
        weekArrayList.add(week);

        selectWeekSpinner.setAdapter(new WeekListAdapter(activity , weekArrayList));


        bundle = getArguments() ;
        updateclassListListener = (FragmentBackResponseListener) bundle.getSerializable("updateclassListListener");

        return convertView;
    }
    CalenderGridView calenderGridView;
    @Override
    public void onClick(View v)
    {

        if (v.getId() == R.id.enter_start_date_btn)
        {

            if (calenderGridView == null)
            {
                calenderGridView = new CalenderGridView(activity, new CalenderTabClickListener() {
                    @Override
                    public void onTabClick(AlertDialog calendarDialogue, Calendar seletedCal)
                    {
                        calendarDialogue.dismiss();

                        Calendar currentTimeCal = Calendar.getInstance(Locale.ENGLISH);



                        if (false == (Utill.isEqualDate(currentTimeCal ,seletedCal ) || Utill.compareTwoDate(seletedCal , currentTimeCal)) )
                        {
                            Utill.showDialg("Selected time should be greater than or equal to current time" , getActivity());
                        }
                        else
                        {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-MMM-yyyy");
                            enterStartDateBtn.setText(simpleDateFormat.format(seletedCal.getTime()));
                            calenderGridView.setMinimumDate(seletedCal);

                        }






                    }
                });
            }



            calenderGridView.showCalendar();
        }


        if (v.getId() == R.id.start_time_edit_text)
        {
           final EditText editText = (EditText) v;

            CusTomTimePickerDialog cusTomTimePickerDialog = new CusTomTimePickerDialog(getActivity(), new TimePickerListener() {
                @Override
                public void onSelecteTime(int hourOfDay, int minut) {

                    startTimeCalender = Calendar.getInstance(Locale.ENGLISH);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
                    startTimeCalender.set(Calendar.HOUR_OF_DAY , hourOfDay);
                    startTimeCalender.set(Calendar.MINUTE , minut);

                    endTimeEditText.setText("");
                    editText.setText(simpleDateFormat.format(startTimeCalender.getTime()));
                }
            });
            cusTomTimePickerDialog.showTimeDialog();

        }


        if (v.getId() == R.id.end_time_edit_text)
        {
            final EditText editText = (EditText) v;

            CusTomTimePickerDialog cusTomTimePickerDialog = new CusTomTimePickerDialog(getActivity(), new TimePickerListener() {
                @Override
                public void onSelecteTime(int hourOfDay, int minut) {

                    endTimeCalender = Calendar.getInstance(Locale.ENGLISH);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
                    endTimeCalender.set(Calendar.HOUR_OF_DAY , hourOfDay);
                    endTimeCalender.set(Calendar.MINUTE , minut);


                      if (Validation.isStringNullOrBlank(startTimeEditText.getText().toString()))
                      {
                          Utill.showDialg("Please enter start time" , getActivity());
                               return;
                      }

                    if ( endTimeCalender.compareTo(startTimeCalender) >0)
                    {
                        editText.setText(simpleDateFormat.format(endTimeCalender.getTime()));

                    }
                    else
                    {
                        Utill.showDialg("End time should be greater than start time" , getActivity());
                    }

                }
            });
            cusTomTimePickerDialog.showTimeDialog();

        }
        if (v.getId() == R.id.add_class_btn)
        {
            applyValidation();
        }



    }

  String weekStr = "";

    public void applyValidation()
    {

        Utill.hideKeybord(getActivity());

        if (Validation.isStringNullOrBlank(classTitleEditView.getText().toString()))
        {
            Utill.showDialg("Please enter class name" , getActivity());
            return;
        }


        if (Validation.isStringNullOrBlank(enterStartDateBtn.getText().toString()))
        {
            Utill.showDialg("Please enter class start date" , getActivity());
            return;
        }

        if (selectDurationSpinner.getSelectedItemPosition() == 0)
        {
            Utill.showDialg("Please select duration" , getActivity());


            return;
        }


        for (int i =0 ; i < weekArrayList.size() ;i++)
        {
            if (weekArrayList.get(i).isWeekSelected())
            {
                if (Validation.isStringNullOrBlank(weekStr))
                {
                    weekStr = weekArrayList.get(i).getIndex()+"" ;
                }
                else
                {
                    weekStr = weekStr+","+weekArrayList.get(i).getIndex()+"" ;

                }
            }
        }

        if (weekStr.length() == 0)
        {
            Utill.showDialg("Please select a week day" , getActivity());


            return;
        }



        if (Validation.isStringNullOrBlank(startTimeEditText.getText().toString()))
        {
            Utill.showDialg("Please enter start time" , getActivity());


            return;
        }




        if (Validation.isStringNullOrBlank(endTimeEditText.getText().toString()))
        {
            Utill.showDialg("Please enter end time" , getActivity());


            return;
        }



        if (Validation.isStringNullOrBlank(costEditTv.getText().toString()))
        {
            Utill.showDialg("Please enter cost" , getActivity());


            return;
        }




        if (Validation.isStringNullOrBlank(participantEditTv.getText().toString()))
        {
            Utill.showDialg("Please enter no of participant" , getActivity());


            return;
        }


        try
        {
            if (Integer.parseInt(participantEditTv.getText().toString()) == 0)
            {
                Utill.showDialg("Please enter no of participant" , getActivity());


                return;
            }
        }
        catch (Exception e)
        {

        }


        if (colorCodeSpinner.getSelectedItemPosition() == 0)
        {
            Utill.showDialg("Please select atleast one category" , getActivity());


            return;
        }

        sendDataOnServer();
    }


  public void sendDataOnServer()
  {
     /* :3499
      :269
      :Test
      :2017-12-12
      class_days:1,3,5
      :1 month
      :08:00:00
      :09:00:00
      :20
      :#abc123
      :20*/

     SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm");


      HashMap<String , Object> param = new HashMap<>();
      param.put("class_uid" , SessionManager.getUser_id(getActivity()));
      param.put("class_club_id" , SessionManager.getUser_Club_id(getActivity()));
      param.put("class_name" , classTitleEditView.getText().toString());
      param.put("class_startDate" , Utill.formattedDateFromString("d-MMM-yyyy" , "yyyy-MM-dd" , enterStartDateBtn.getText().toString()));



      param.put("class_days" , weekStr);

      param.put("class_duration" ,classDurationSpinner.durationItem.get(selectDurationSpinner.getSelectedItemPosition()) );
      param.put("class_startTime" , timeFormate.format(startTimeCalender.getTime()));
      param.put("class_endTime" , timeFormate.format(endTimeCalender.getTime()));
      param.put("class_cost" , costEditTv.getText().toString());
      param.put("class_color" , recursiveBookingCourtColourArrayList.get(colorCodeSpinner.getSelectedItemPosition()).getCat_color());
      param.put("class_noOfParticipents" , participantEditTv.getText().toString());

      httpRequest.getResponse(activity, WebService.createCclassLink, param, new OnServerRespondingListener(activity) {
          @Override
          public void onSuccess(JSONObject jsonObject) {

              Log.e("jsonObject",jsonObject+"");
              //{"status":"true","message":"Class created successfully","class_id":4}

              try
              {
                  Utill.hideKeybord(getActivity());
                  if (jsonObject.getBoolean("status"))
                  {

                      getActivity().getSupportFragmentManager().popBackStack();
                      getActivity().getSupportFragmentManager().popBackStack();
                      //updateclassListListener.onBackFragment();
                  }
                  else
                  {
                      Utill.showDialg(jsonObject.getString("message") , getActivity());
                  }

              }
              catch (Exception e)
              {
                  Utill.showToast(getActivity() , e.getMessage());
              }
          }
      });

  }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DirectorFragmentManageActivity.updateTitle("Class Reservation");

        DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.VISIBLE);
    }















    public class ClassDurationSpinner extends BaseAdapter implements SpinnerAdapter
    {

        Activity activity ;
        public   ArrayList<String> durationItem ;

        public ClassDurationSpinner(final Activity activity ,final Spinner spinner)
        {
            this.activity = activity ;
            this.durationItem = new ArrayList<>() ;

            durationItem.add("Duration");
            durationItem.add("2 Weeks");
            durationItem.add("3 Weeks");
            durationItem.add("1 Month");
            durationItem.add("2 Months");
            durationItem.add("3 Months");
            durationItem.add("4 Months");
            durationItem.add("6 Months");
            spinner.post(new Runnable(){
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                public void run(){
                    int height = spinner.getHeight();
                    int diff = DeviceInfo.getDensity(activity) /80;
                    spinner.setDropDownVerticalOffset(height +5);
                }
            });



        }



        @Override
        public int getCount() {
            return durationItem.size();
        }

        @Override
        public Object getItem(int position)
        {
            return durationItem.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent)
        {
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
                txt.setPadding(8, 8, 8, 8);
                txt.setTextSize(13);
                txt.setGravity(Gravity.CENTER_VERTICAL);
                txt.setText(durationItem.get(position));
                txt.setTextColor(Color.parseColor("#000000"));
                txt.setSingleLine(true);

            }

            return  txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(activity);
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(8, 10, 8, 10);
            txt.setTextSize(12);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_arraow, 0);
            txt.setText(durationItem.get(i));
            txt.setTextColor(Color.parseColor("#000000"));
            txt.setSingleLine(true);

            return  txt;
        }
    }
}
