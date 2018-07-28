package com.clubscaddy.utility;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.TimePicker;

import com.clubscaddy.Interface.TimePickerListener;
import com.clubscaddy.R;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by administrator on 6/12/17.
 */

public class CusTomTimePickerDialog
{
    TextView cancel_tv;
    TimePicker time_picker;
    TextView ok_tv;
    int selectedHourOfDay , selectedMinute;
    Activity activity ;
    TimePickerListener timePickerListener ;

    public  CusTomTimePickerDialog(Activity activity ,TimePickerListener timePickerListener )
    {
        this.activity =activity ;
        this.timePickerListener =timePickerListener ;

    }

    public void showTimeDialog()
    {
        Calendar currentDate = Calendar.getInstance(Locale.ENGLISH);
        final Dialog timePicker = new Dialog(activity);
        timePicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
        timePicker.setContentView(R.layout.time_picker_dialog);
        cancel_tv = (TextView) timePicker.findViewById(R.id.cancel_tv);
        time_picker = (TimePicker) timePicker.findViewById(R.id.time_picker);

        selectedHourOfDay = currentDate.get(Calendar.HOUR_OF_DAY);
        selectedMinute = currentDate.get(Calendar.MINUTE);
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

                selectedHourOfDay = hourOfDay ;
                selectedMinute = minute ;

            }
        });


        ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.dismiss();;
                timePickerListener.onSelecteTime(selectedHourOfDay , selectedMinute);


            }
        });

        timePicker.show();
    }

}
