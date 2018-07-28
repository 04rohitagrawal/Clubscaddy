package com.clubscaddy.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.clubscaddy.Interface.CalenderTabClickListener;
import com.clubscaddy.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by administrator on 23/5/17.
 */

public class CalenderGridView implements View.OnClickListener {

    private static final String dateTemplate = "MMMM yyyy";
    Activity activity;

    Calendar _calendar;
    int month;
    int year;
    String tag = "Tag";
    Button selectedDayMonthYearButton;
    ImageView prevMonth;
    ImageView nextMonth;
    TextView currentMonth;
    GridView calendarView;
    GridCellAdapter adapter;
    Calendar selected_calender;
    AlertDialog calendarDialogue ;
    CalenderTabClickListener calenderTabClickListener ;
    public CalenderGridView(Activity activity , CalenderTabClickListener calenderTabClickListener  ) {
        this.activity = activity;
        selected_calender = Calendar.getInstance(Locale.ENGLISH);
        selected_calender.set(Calendar.HOUR, 0);
        selected_calender.set(Calendar.HOUR_OF_DAY, 0);
        selected_calender.set(Calendar.MINUTE, 0);
        selected_calender.set(Calendar.SECOND, 0);
        selected_calender.set(Calendar.MILLISECOND, 0);
        selected_calender.set(Calendar.AM_PM, Calendar.AM);
        this.calenderTabClickListener = calenderTabClickListener ;

    }

    public void setMinimumDate(Calendar minimumDate)
    {
        selected_calender = (Calendar) minimumDate.clone();
        selected_calender.set(Calendar.HOUR, 0);
        selected_calender.set(Calendar.HOUR_OF_DAY, 0);
        selected_calender.set(Calendar.MINUTE, 0);
        selected_calender.set(Calendar.SECOND, 0);
        selected_calender.set(Calendar.MILLISECOND, 0);
        selected_calender.set(Calendar.AM_PM, Calendar.AM);
    }



    public void showCalendar() {


        calendarDialogue = new AlertDialog.Builder(activity).create();
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.my_calendar_view, null);
        initializeCalendar(v);
        calendarDialogue.setView(v);
        calendarDialogue.show();
    }

    void initializeCalendar(View v) {
        _calendar = Calendar.getInstance(Locale.ENGLISH);
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);
        Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: " + year);

        selectedDayMonthYearButton = (Button) v.findViewById(R.id.selectedDayMonthYear);
        selectedDayMonthYearButton.setText("Selected: ");
        TextView cancelTV = (TextView) v.findViewById(R.id.cancel);
        cancelTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                calendarDialogue.dismiss();
            }
        });
        prevMonth = (ImageView) v.findViewById(R.id.prevMonth);

        prevMonth.setOnClickListener(this);

        currentMonth = (TextView) v.findViewById(R.id.currentMonth);
        currentMonth.setText(DateFormat.format(dateTemplate, _calendar.getTime()));

        nextMonth = (ImageView) v.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(this);

        calendarView = (GridView) v.findViewById(R.id.calendar);

        // Initialised
        adapter = new GridCellAdapter(activity, R.id.calendar_day_gridcell, month, year);
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        if (v == prevMonth) {
            if (month <= 1) {
                month = 12;
                year--;
            } else {
                month--;
            }
            Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
            setGridCellAdapterToDate(month, year);
        }
        if (v == nextMonth) {
            if (month > 11) {
                month = 1;
                year++;
            } else {
                month++;
            }
            Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
            setGridCellAdapterToDate(month, year);
        }
    }


    public class GridCellAdapter extends BaseAdapter implements View.OnClickListener {
        private static final String tag = "GridCellAdapter";
        private static final int DAY_OFFSET = 1;
        private final Context _context;
        private final List<String> list;
        private final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
                "November", "December"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        private final HashMap<String, Integer> eventsPerMonthMap;
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private Button gridcell;
        private TextView num_events_per_day;

        // Days in Current Month
        public GridCellAdapter(Context context, int textViewResourceId, int month, int year) {
            super();
            this._context = context;
            this.list = new ArrayList<String>();
            Log.d(tag, "==> Passed in Date FOR Month: " + month + " " + "Year: " + year);
            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
            Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
            Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

            // Print Month
            printMonth(month, year);

            // Find Number of Events
            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }

        private String getMonthAsString(int i) {
            return months[i];
        }

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
        }

        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        /**
         * Prints Month
         *
         * @param mm
         * @param yy
         */
        private void printMonth(int mm, int yy) {
            Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
            int trailingSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            int currentMonth = mm - 1;
            String currentMonthName = getMonthAsString(currentMonth);
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);

            Log.d(tag, "Current Month: " + " " + currentMonthName + " having " + daysInMonth + " days.");

            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
            Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

            if (currentMonth == 11) {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
                Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
            } else if (currentMonth == 0) {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
                Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
            } else {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
            }

            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;

            Log.d(tag, "Week Day:" + currentWeekDay + " is " + getWeekDayAsString(currentWeekDay));
            Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
            Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

            if (cal.isLeapYear(cal.get(Calendar.YEAR)))
                if (mm == 2)
                    ++daysInMonth;
                else if (mm == 3)
                    ++daysInPrevMonth;

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++) {
                Log.d(tag,
                        "PREV MONTH:= " + prevMonth + " => " + getMonthAsString(prevMonth) + " "
                                + String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
                list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-"
                        + prevYear);
            }

            // Current Month Days
            for (int i = 1; i <= daysInMonth; i++) {
                Log.e(currentMonthName, String.valueOf(i) + " " + getMonthAsString(currentMonth) + " " + yy);
                if (i == getCurrentDayOfMonth()) {
                    list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                } else {
                    list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                }
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++) {
                Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
                list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
            }
        }


        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year, int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();

            return map;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.screen_gridcell, parent, false);
            }

            // Get a reference to the Day gridcell
            gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
            gridcell.setOnClickListener(this);

            // ACCOUNT FOR SPACING

            // Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
            String[] day_color = list.get(position).split("-");
            //    Log.e("day colour" , day_color[0]+" "+day_color[1]+" "+day_color[2]+" "+day_color[3]);
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];
            String date = theday + "-" + Utill.getMonthNumber(themonth) + "-" + theyear;


            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
                if (eventsPerMonthMap.containsKey(theday)) {
                    num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
                    Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
                    num_events_per_day.setText(numEvents.toString());
                }
            }

            // Set the Day GridCell
            gridcell.setText(theday);
            gridcell.setBackground(activity.getResources().getDrawable(R.drawable.calendar_tile_small));
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);
            // Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear);

            if (day_color[1].equals("GREY")) {
                gridcell.setTextColor(activity.getResources().getColor(R.color.gray_color));

            }
            if (day_color[1].equals("WHITE")) {
                gridcell.setTextColor(activity.getResources().getColor(R.color.black_color));
            }
            String currentDate = day_color[0] + "-" + day_color[2] + "-" + day_color[3];
            SimpleDateFormat currentDateformat = new SimpleDateFormat("d-MMMM-yyyy");
            String systemCurrentDate = currentDateformat.format(Calendar.getInstance().getTime());

            if (day_color[1].equals("BLUE")) {
                Log.e("current date", systemCurrentDate + " " + currentDate);
                if (systemCurrentDate.equals(currentDate)) {
                    gridcell.setTextColor(activity.getResources().getColor(R.color.white_color));
                    gridcell.setBackground(activity.getResources().getDrawable(R.drawable.calendar_bg_orange));
                } else {
                    gridcell.setTextColor(activity.getResources().getColor(R.color.black_color));
                    //  gridcell.setBackground(getResources().getDrawable(R.drawable.calendar_bg_orange));
                }

            } else {
                if (selected_calender != null) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-M-yyyy");
                    mySelectedDate = simpleDateFormat.format(selected_calender.getTime());
                    if (mySelectedDate.equals(date)) {
                        gridcell.setBackground(activity.getResources().getDrawable(R.color.blue_header));
                    }
                }
            }


            return row;
        }

        String mySelectedDate = "";

        @SuppressLint("SimpleDateFormat")
        @Override
        public void onClick(View view) {
            String date_month_year = (String) view.getTag();


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            Calendar selectedCalender = Calendar.getInstance(Locale.ENGLISH) ;
            selectedCalender = Calendar.getInstance();
            try {
                selectedCalender.setTime(simpleDateFormat.parse(date_month_year));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            selectedCalender.set(Calendar.HOUR, 0);
            selectedCalender.set(Calendar.HOUR_OF_DAY, 0);
            selectedCalender.set(Calendar.MINUTE, 0);
            selectedCalender.set(Calendar.SECOND, 0);
            selectedCalender.set(Calendar.MILLISECOND, 0);
            selectedCalender.set(Calendar.AM_PM, Calendar.AM);



            calenderTabClickListener.onTabClick(calendarDialogue , selectedCalender);





        }
        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }
    }
    Date getDateFromString(String dateStr) {
        // 2015-04-03 03:11 PM
        Calendar cal = Calendar.getInstance();
        int year = 0, monthOfYear = 0, dayOfMonth = 0;
        String d[] = dateStr.split("/");

        year = Integer.parseInt(d[2]);
        monthOfYear = Integer.parseInt(d[1]) - 1;
        dayOfMonth = Integer.parseInt(d[0]);

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DATE, dayOfMonth);
        Date dati = cal.getTime();
        Log.e("Date", dati.toString());
        return cal.getTime();
    }
    private void setGridCellAdapterToDate(int month, int year) {
        adapter = new GridCellAdapter(activity, R.id.calendar_day_gridcell, month, year);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(DateFormat.format(dateTemplate, _calendar.getTime()));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }
}