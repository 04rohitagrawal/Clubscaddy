package com.clubscaddy.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.clubscaddy.Adapter.MemberAutoCompleteAdapter;
import com.clubscaddy.Adapter.ReservationTimeListAdapter;
import com.clubscaddy.Bean.CoachSlot;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.Interface.MemberListListener;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.custumview.InstantAutoComplete;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.R;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.utility.SqlListe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by administrator on 27/4/17.
 */

public class CoachSloatBookFragment extends Fragment
{

    String coachName ;
    View convertView ;
    LinearLayout bottoms;
    HttpRequest httpRequest ;
    ArrayList< ArrayList<CoachSlot>>coachSlotArrayList ;
    boolean selectionStatus[][];
    boolean selectionStatuscopy[][];
    Calendar selected_calender;
    SqlListe sqlListe ;
    MemberAutoCompleteAdapter memberAutoCompleteAdapter ;

    boolean isCoachAsMember = false;
SessionManager sessionManager;

    ArrayList<Integer> daysArrayList ;



    public String getCoach_id() {
        return coach_id;
    }
    int priviousX = -1, priviousY = -1;
    public void setCoach_id(String coach_id , String coachName , boolean isCoachAsMember) {
        this.coach_id = coach_id;
        this.coachName = coachName;

    }
    Calendar currentTimeCal;
    String coach_id ;
    String deleteID = "";
    Context mContext;
    boolean isbulkbook = false;
    TableLayout tableLayout;
    int slotWidth;
    LinearLayout main_layout;
    ArrayList<Integer> totalSelectedCourtNoList;
    int slotHeight;
    ShowUserMessage showUserMessage ;
    LinearLayout bottomLayout;
    boolean longPressed = false;
    TextView coach_name_tv;
    public int max_book = 4;
    TextView date_duration_tv;
    Calendar selectedCalender ;
    ImageView privious_iv , next_iv;
    TextView recursive_book;


    ArrayList<MemberListBean>searchingMemberList ;

    TextView cancelBottomBtn, bookBtn, recursiveBookBtn;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState)
    {
        convertView  = LayoutInflater.from(getActivity()).inflate(R.layout.coach_sloat_fragment , null);
        bottoms = (LinearLayout) convertView.findViewById(R.id.bottoms);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        showUserMessage = new ShowUserMessage(getActivity());
        sessionManager = new SessionManager();
        daysArrayList =new ArrayList<Integer>();
        sqlListe = new SqlListe(getActivity());




        if (SessionManager.getUser_id(getActivity()).equals(coach_id))
        {
            this.isCoachAsMember = true;
        }
        else
        {
            this.isCoachAsMember = false;
        }


        daysArrayList.clear();
        for (int i = 0 ; i < 7 ;i++)
        {
            daysArrayList.add(0);
        }
        httpRequest = new HttpRequest(getActivity());
        mContext = getActivity() ;


        searchingMemberList = new ArrayList<>();

        if (DirectorFragmentManageActivity.actionbar_titletext != null) {
            DirectorFragmentManageActivity.updateTitle("Lesson reservation");
        }
        main_layout = (LinearLayout) convertView.findViewById(R.id.main_layout);
        bookBtn = (TextView) convertView.findViewById(R.id.okbottom);
        cancelBottomBtn = (TextView) convertView.findViewById(R.id.cancelbottom);
        coach_name_tv = (TextView) convertView.findViewById(R.id.coach_name_tv);
        recursive_book = (TextView) convertView.findViewById(R.id.recursive_book);

        cancelBottomBtn.setOnClickListener(clickOnBottomListener);
        bookBtn.setOnClickListener(clickOnBottomListener);
        recursive_book.setOnClickListener(clickOnBottomListener);


        coach_name_tv = (TextView) convertView.findViewById(R.id.coach_name_tv);
        date_duration_tv = (TextView) convertView.findViewById(R.id.date_duration_tv);

        privious_iv = (ImageView) convertView.findViewById(R.id.privious_iv);
        next_iv = (ImageView) convertView.findViewById(R.id.next_iv);

        privious_iv.setOnClickListener(nextpriviouesDataClickListner);
        next_iv.setOnClickListener(nextpriviouesDataClickListner);


        coach_name_tv.setText(coachName);

        coachSlotArrayList = new ArrayList< ArrayList<CoachSlot>>();
        selected_calender = Calendar.getInstance();

        selected_calender.set(Calendar.HOUR, 0);
        selected_calender.set(Calendar.HOUR_OF_DAY, 0);
        selected_calender.set(Calendar.MINUTE, 0);
        selected_calender.set(Calendar.SECOND, 0);
        selected_calender.set(Calendar.MILLISECOND, 0);
        selected_calender.set(Calendar.AM_PM, Calendar.AM);
        totalSelectedCourtNoList = new ArrayList<>();
        currentTimeCal = Calendar.getInstance(Locale.ENGLISH);
        bottomLayout = (LinearLayout) convertView.findViewById(R.id.bottoms);

        selectedCalender=Calendar.getInstance(Locale.ENGLISH);
        selectedCalender.add( Calendar.DAY_OF_WEEK, -(selectedCalender.get(Calendar.DAY_OF_WEEK)-1));

        Calendar afterSevenDays = (Calendar) selectedCalender.clone();
        afterSevenDays.add(Calendar.DATE , 6);

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("MMM dd yyyy");
        date_duration_tv.setText(dateFormat1.format(selectedCalender.getTime()) +"  -  "+dateFormat1.format(afterSevenDays.getTime()));
        getCoachTimeSoltReservation(selectedCalender);




        if (isCoachAsMember)
        {
            recursive_book.setVisibility(View.VISIBLE);
        }
        else {
            recursive_book.setVisibility(View.GONE);
        }

        return convertView;
    }
    ScrollView sv;
    HorizontalScrollView hsv;
    public void getCoachTimeSoltReservation(Calendar calendar)
    {
        showPorgress();
        HashMap<String , Object> param = new HashMap<String , Object>();




        param.put("court_club_id" , SessionManager.getUser_Club_id(getActivity()));
        param.put("coach_id" ,coach_id );
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        param.put("date" ,simpleDateFormat.format(calendar.getTime()));

        httpRequest.getResponseWihhoutPd(getActivity(), WebService.coachreserve, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                Log.e("jsonObject" , jsonObject+"");
                coachSlotArrayList.clear();


                try
                {
                    String servertime = jsonObject.getString("time");
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    currentTimeCal.setTime(simpleDateFormat1.parse(servertime));

                }
                catch (Exception e)
                {

                }


                try
                {
                    JSONArray responseArray = jsonObject.getJSONArray("Response");
                    for (int i = 0 ; i < responseArray.length() ;i++)
                    {
                        JSONArray timeArray = responseArray.getJSONObject(i).getJSONArray("time");
                        ArrayList<CoachSlot> coachSubSlotArrayList = new ArrayList<CoachSlot>();
                        for (int j =0 ; j < timeArray.length() ; j++)
                        {
                            JSONObject timeArrayItem = timeArray.getJSONObject(j) ;
                            CoachSlot coachSlot = new CoachSlot();
                            coachSlot.setCoach_id(timeArrayItem.getString("coach_id"));
                            coachSlot.setCoach_start_time(timeArrayItem.getString("coach_start_time"));
                            coachSlot.setUser_name(timeArrayItem.getString("user_name"));
                            coachSlot.setMemberbookedid(timeArrayItem.getString("memberbookedid"));
                            coachSlot.setCoach_user_id(timeArrayItem.getString("coach_user_id"));
                            coachSlot.setCoach_bulkbooking_id(timeArrayItem.getString("coach_bulkbooking_id"));
                            coachSlot.setCoach_recursiveid(timeArrayItem.getString("coach_recursiveid"));
                            coachSlot.setSlots(timeArrayItem.getString("slots"));
                            coachSlot.setDate(timeArrayItem.getString("date"));
                            coachSlot.setCourt_id(timeArrayItem.getString("court_id"));
                            coachSlot.setCoach_purpuse(timeArrayItem.getString("coach_purpuse"));
                            coachSlot.setCoach_end_time(timeArrayItem.getString("coach_end_time"));
                            coachSubSlotArrayList.add(coachSlot);

                        }
                        coachSlotArrayList.add(coachSubSlotArrayList);
                    }
                }
                catch (Exception e)
                {

                }
                totalSelectedCourtNoList.clear();
                for (int i =0 ;i <coachSlotArrayList.size() ;i++)
                {


                    totalSelectedCourtNoList.add(0);
                }







                sv = new ScrollView(mContext);
                TableLayout tableLayout = createTableLayout(coachSlotArrayList);
                hsv = new HorizontalScrollView(mContext);


                hsv.addView(tableLayout);
                sv.addView(hsv);


                main_layout.removeAllViews();
                main_layout.addView(sv);
                main_layout.setVisibility(View.VISIBLE);
                dismissDialog();

            }
        });
    }

    CoachSlot databean1;
    String coach_bulkbooking_id;
    CoachSlot databean;
    CoachSlot deleteCourtData;
    boolean delete = false;
    private TableLayout createTableLayout(final ArrayList<ArrayList<CoachSlot>> mainList) {


        ///Toast.makeText(getActivity(), "Calling", 1).show();

        //Utill.showProgress(getActivity());


        selectionStatus = new boolean[mainList.get(0).size()][mainList.size()];

        selectionStatuscopy = new boolean[mainList.get(0).size()][mainList.size()];
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        tableLayout = new TableLayout(mContext);


        tableLayout.setBackgroundColor(getResources().getColor(R.color.green_bg_color));

        // 2) create tableRow params
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();


        tableRowParams.setMargins(1, 1, 1, 1);

        int l = 0;
        //


        while (l < mainList.get(0).size()) {

            TableRow tableRow = new TableRow(mContext);
            tableRow.setBackgroundColor(mContext.getResources().getColor(R.color.blue_color));

            for (int j = 0; j < mainList.size(); j++) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final RelativeLayout relative = (RelativeLayout) inflater.inflate(R.layout.resurvation_item, null);


                ViewTreeObserver vto = relative.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        relative.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        slotWidth = relative.getMeasuredWidth();
                        slotHeight = relative.getMeasuredHeight();


                    }
                });

                ImageView crossImage = (ImageView) relative.findViewById(R.id.cross);
                crossImage.setVisibility(View.INVISIBLE);

                final TextView textView = (TextView) relative.findViewById(R.id.text);


                String slotValue = "";
                String inputFormate = "dd-MMM-yyyyy" ;
                String outputFormate = "EEE MM-dd-yyyy";
                String inputDate = mainList.get(j).get(l).getDate();

                String date  = Utill.formattedDateFromString(inputFormate , outputFormate , inputDate);


                if (!Utill.isStringNullOrBlank(mainList.get(j).get(l).getCoach_purpuse())) {
                    String purpos = mainList.get(j).get(l).getCoach_purpuse();
                    if (purpos.length() > 15) {
                        purpos = purpos.subSequence(0, 15) + "..";
                    }
                    slotValue = mainList.get(j).get(l).getDate() + "\n" + mainList.get(j).get(l).getSlots() + "\n" + purpos;

                } else {
                    if (Validation.isStringNullOrBlank(mainList.get(j).get(l).getUser_name()) == false) {
                        String userName = mainList.get(j).get(l).getUser_name();
                        if (userName.length() > 15) {
                            userName = userName.subSequence(0, 15) + "..";
                        }




                        if (isCoachAsMember == true || mainList.get(j).get(l).getCoach_user_id().equals(SessionManager.getUser_id(getActivity())))
                        {
                            slotValue = date + "\n" + mainList.get(j).get(l).getSlots() + "\n" + userName;
                        }
                        else
                        {




                            slotValue = date + "\n" + mainList.get(j).get(l).getSlots() + "\n" ;
                        }


                        Log.e("slotValue", slotValue);

                    } else {

                        // RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) textView.getLayoutParams();
                        //  layoutParams.setMargins(0 , getResources().getDimensionPixelSize(R.dimen.margin_5),0,0);
                        // textView.setPadding(0,25,0,0);
                        //textView.setLayoutParams(layoutParams);
                        slotValue = date + "\n" + mainList.get(j).get(l).getSlots() + "\n";

                    }
                }


                textView.setText(slotValue);
                textView.setTag(j + "-" + l);
                textView.setGravity(Gravity.CENTER);
                crossImage.setTag(j + "-" + l);


                Drawable background = textView.getBackground();
                CoachSlot bean = mainList.get(j).get(l);
                Calendar reservationSlotTime = (Calendar) selected_calender.clone();
                try {

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    reservationSlotTime.setTime(simpleDateFormat.parse(bean.getDate()));
                    reservationSlotTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(bean.getCoach_end_time().split(":")[0]));
                    reservationSlotTime.set(Calendar.MINUTE, Integer.parseInt(bean.getCoach_end_time().split(":")[1]));
                    reservationSlotTime.set(Calendar.SECOND, 00);

                } catch (Exception e) {
                    //  Toast.makeText(getActivity(), e.getMessage(), 1).show();
                }

                if (currentTimeCal.getTime().compareTo(reservationSlotTime.getTime()) > 0) {
                    textView.setEnabled(false);
                    relative.setEnabled(false);
                    bean.setCourtSelectable(false);
                } else {
                    textView.setEnabled(true);
                    relative.setEnabled(true);
                    bean.setCourtSelectable(true);
                }
                textView.setTextColor(mContext.getResources().getColor(R.color.white_color));

                textView.setTextSize(15);

                if (Utill.isStringNullOrBlank(bean.getCoach_bulkbooking_id())) {


                    textView.setTextSize(15);


                    if (currentTimeCal.getTime().compareTo(reservationSlotTime.
                            getTime()) > 0) {
                        textView.setBackgroundColor(mContext.getResources().getColor(R.color.gray_color));

                    } else {
                        textView.setBackgroundColor(mContext.getResources().getColor(R.color.free_slot_color));
                        ;
                    }
                    //textView.setGravity(Gravity.CENTER);

                } else {

                    if (!Utill.isStringNullOrBlank(bean.getCoach_user_id()) /*&& */) {
                        //relative.setBackgroundColor(mContext.getResources().getColor(R.color.my_booked_slot_color));
                        textView.setTextSize(15);


                        if (!bean.getCoach_user_id().equalsIgnoreCase(SessionManager.getUser_id(mContext))) {
                            textView.setBackgroundColor(mContext.getResources().getColor(R.color.booked_slot_color));
                            textView.setTextSize(15);
                        } else {
                            textView.setBackgroundColor(mContext.getResources().getColor(R.color.my_booked_slot_color));
                            textView.setTextSize(15);
                        }

                    } else {
                        textView.setBackgroundColor(mContext.getResources().getColor(R.color.black_color));
                        textView.setTextSize(15);
                    }
                }






                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Log.e("Simple Click", "Call");

                        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");




                        String index[] = view.getTag().toString().split("-");
                        databean1 = mainList.get(Integer.parseInt(index[0])).get(Integer.parseInt(index[1]));



                        SimpleDateFormat mySelecteDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                        Calendar mySelectedTimeCal = Calendar.getInstance(Locale.ENGLISH);
                        try {
                            mySelectedTimeCal.setTime(mySelecteDateFormat.parse(databean1.getDate()+" "+databean1.getCoach_start_time()));
                            ;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Calendar lastDateForMemberReservation = (Calendar) currentTimeCal.clone();
                        lastDateForMemberReservation.add(Calendar.DATE, 14);


                        if (isCoachAsMember == false && Utill.compareTwoDate(lastDateForMemberReservation , mySelectedTimeCal) == false)
                        {

                            Utill.showDialg("You are allowed to book only 2 weeks in advance ", getActivity());
                            return;
                        }





                        if (!Utill.isStringNullOrBlank(databean1.getCoach_bulkbooking_id()))
                        {
                            if (isCoachAsMember || databean1.getCoach_user_id().equalsIgnoreCase(SessionManager.getUser_id(mContext)))
                            {

                                String court_reservation_recursiveid = databean1.getCoach_recursiveid();
//
                                if (deleteID.equals( databean1.getMemberbookedid()))
                                {
                                    if (delete )
                                    {
                                        delete = false;
                                        //  resetReservationChart();
                                        //  resetReservationChartAll();

                                    }
                                    else
                                    {
                                        delete =true ;
                                    }
                                }
                                else
                                {
                                    delete =true ;
                                }



                                deleteID = databean1.getMemberbookedid();



                                Log.e("recursiveid" , court_reservation_recursiveid+"  ");

                                if (Utill.isStringNullOrBlank(deleteID) == false || Utill.isStringNullOrBlank(court_reservation_recursiveid) == false)
                                {

                                    try {

                                        if (databean != null)
                                        {
                                            hidedeleteReservationChart(databean , mainList);
                                        }

                                        databean = (CoachSlot) databean1.clone();
                                    } catch (CloneNotSupportedException e) {
                                        e.printStackTrace();
                                    }
                                }

////

                                coach_bulkbooking_id = databean1.getCoach_bulkbooking_id();
                                deleteCourtData = databean1;
                                resetReservationChart(mainList);
                                if (delete)
                                {
                                    deleteReservationChart(databean1 , mainList);
                                }

                            }
                            else {
                                //ShowUserMessage.showUserMessage(mContext, "Already Booked.");
                            }
                            return;
                        }
                        delete = false ;
                        int l = Integer.parseInt(index[0]);
                        int m = Integer.parseInt(index[1]);

                        setDataInTable(m, l ,mainList);
                        checkSlotSelection(mainList);








						/*if (longPressed)
						{
							sv.setOnTouchListener(touchListener);
							hsv.setOnTouchListener(touchListener);
						}
*/


                    }
                });

                crossImage.setTag(j+" "+l);
                crossImage.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(final View v) {
                        // TODO Auto-generated method stub

                        int row = Integer.parseInt(v.getTag().toString().split(" ")[0]);
                        int cloumn = Integer.parseInt(v.getTag().toString().split(" ")[1]);
                     final   String memberbookid = coachSlotArrayList.get(row).get(cloumn).getMemberbookedid();
                        ;
                       ;
                     final String userId =   coachSlotArrayList.get(row).get(cloumn).getCoach_user_id() ;
                        if (memberbookid.equals("0") || Validation.isStringNullOrBlank(memberbookid))
                        {




                            showDialogForRecusiveReservationDelete(coachSlotArrayList.get(row).get(cloumn).getCoach_user_id(),coachSlotArrayList.get(row).get(cloumn).getCoach_recursiveid() ,  AppConstants.getDateInYYYYMMDD(coachSlotArrayList.get(row).get(cloumn).getDate()));
                        }
                        else
                        {




                            final Dialog dialog = new Dialog(getActivity());
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.confirm_dialog_delete_coach_reser);
                            dialog.setCancelable(false);
                            dialog.getWindow().setLayout(AppConstants.getDeviceWidth(getActivity()), LinearLayout.LayoutParams.WRAP_CONTENT);
                            dialog.show();
                            TextView title_tv = (TextView) dialog.findViewById(R.id.title_tv);
                            title_tv.setText(SessionManager.getClubName(getActivity()));
                            final EditText cancel_reason_msg_edittv = (EditText) dialog.findViewById(R.id.cancel_reason_msg_edittv);


                            if (SessionManager.getUser_id(getActivity()).equals(userId) && isCoachAsMember)
                            {
                                cancel_reason_msg_edittv.setVisibility(View.GONE);
                            }


                            cancel_dialog_tv = (TextView) dialog.findViewById(R.id.cancel_dialog_tv);
                            cancel_dialog_tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    hideKeyBoard(cancel_reason_msg_edittv);
                                    dialog.cancel();

                                }
                            });
                            done_tv = (TextView) dialog.findViewById(R.id.done_tv);
                            done_tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if ((SessionManager.getUser_id(getActivity()).equals(userId) && isCoachAsMember) == false)
                                    {
                                        if (Validation.isStringNullOrBlank(cancel_reason_msg_edittv.getText().toString()))
                                        {
                                            ShowUserMessage.showUserMessage(getActivity() , "Please enter reason");
                                            return;
                                        }
                                    }


                                     hideKeyBoard(cancel_reason_msg_edittv);

                                    dialog.cancel();
                                    deleteReservation(coach_id, memberbookid , cancel_reason_msg_edittv.getText().toString() );
                                }
                            });

                          //  showUserMessage.showDialogBoxWithYesNoButton("Are you sure you want to delete this coach reservation?", new DialogBoxButtonListner() {
                              //  @Override
                              //  public void onYesButtonClick(DialogInterface dialogInterface) {

                                  //  dialogInterface.dismiss();
                                    //
                                  //


                                    //   Toast.makeText(getActivity() , "memberbookid "+memberbookid ,1).show();



                              //  }
                           //// });


                        }


                    }
                });















                tableRow.addView(relative, tableRowParams);
            }


            //tableRow.setBackgroundColor(mContext.getResources().getColor(R.color.black_color));


            tableLayout.addView(tableRow, tableLayoutParams);
            l++;
        }
        //Utill.hideProgress();
        return tableLayout;
    }

    void resetReservationChart(ArrayList<ArrayList<CoachSlot>>mainList) {
        if (mainList != null && selectionStatus != null) {
            for (int i = 0; i < selectionStatus.length; i++) {
                for (int j = 0; j < selectionStatus[i].length; j++) {
                    if (selectionStatus[i][j])
                        setDataInTable(i, j ,mainList);
                }
            }
        }
        hideBookCancel();
    }



    void hideDeleteDataInTable(int i, int j ,ArrayList<ArrayList<CoachSlot>>mainList) {
        if (tableLayout != null) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            RelativeLayout relative = (RelativeLayout) row.getChildAt(j);
            ImageView crossImage = (ImageView) relative.findViewById(R.id.cross);
            TextView textView = (TextView) relative.findViewById(R.id.text);
            crossImage.setVisibility(View.INVISIBLE);
            CoachSlot bean = mainList.get(j).get(i);


            if (isCoachAsMember)
            {
                if (bean.getCoach_user_id().equals(SessionManager.getUser_id(getActivity())))
                {
                    textView.setTextSize(15);
                    textView.setBackgroundColor(mContext.getResources().getColor(R.color.my_booked_slot_color));
                    textView.setTextSize(15);
                }
                else
                {
                    textView.setBackgroundColor(mContext.getResources().getColor(R.color.booked_slot_color));
                    textView.setTextSize(15);
                }

            }
            else
            {
                if (!Utill.isStringNullOrBlank(bean.getCoach_user_id()))
                {
                    textView.setTextSize(15);
                    textView.setBackgroundColor(mContext.getResources().getColor(R.color.my_booked_slot_color));
                    textView.setTextSize(15);
                }
                else
                {
                    textView.setBackgroundColor(mContext.getResources().getColor(R.color.booked_slot_color));
                    textView.setTextSize(15);


                }
            }




        }
    }

    void deleteReservationChart(CoachSlot bean ,ArrayList<ArrayList<CoachSlot>>mainList) {

        Log.e("memberbookedId" ,bean.getMemberbookedid()+"");


        if (mainList != null) {
            for (int i = 0; i < mainList.size(); i++)
            {
                for (int j = 0; j < mainList.get(i).size(); j++)
                {
                    if (!Utill.isStringNullOrBlank(bean.getMemberbookedid()) &&  bean.getMemberbookedid().equals("0") == false
                            && mainList.get(i).get(j).getMemberbookedid().equalsIgnoreCase(bean.getMemberbookedid())) {
                        {

                            showDeleteDataInTable(j, i);
                        }
                    } else if (!Utill.isStringNullOrBlank(bean.getCoach_recursiveid()) && bean.getCoach_recursiveid().equals("0") == false
                            && mainList.get(i).get(j).getCoach_recursiveid().equalsIgnoreCase(bean.getCoach_recursiveid())) {
                        showDeleteDataInTable(j, i);
                    }
                }
            }
        }
        hideBookCancel();
    }
    void showDeleteDataInTable(int i, int j) {
        if (tableLayout != null) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            RelativeLayout relative = (RelativeLayout) row.getChildAt(j);
            ImageView crossImage = (ImageView) relative.findViewById(R.id.cross);
            TextView tv = (TextView) relative.findViewById(R.id.text);
            crossImage.setVisibility(View.VISIBLE);
            tv.setBackgroundColor(mContext.getResources().getColor(R.color.selected_green_color));
        }
    }
    private void showBookCancel() {
        bottomLayout.setVisibility(View.VISIBLE);
    }

    private void hideBookCancel() {
        bottomLayout.setVisibility(View.GONE);
    }

    void hidedeleteReservationChart(CoachSlot bean ,ArrayList<ArrayList<CoachSlot>>mainList) {

        if (mainList != null) {
            for (int i = 0; i < mainList.size(); i++) {
                for (int j = 0; j < mainList.get(i).size(); j++) {
                    if (!Utill.isStringNullOrBlank(bean.getMemberbookedid())
                            && mainList.get(i).get(j).getMemberbookedid().equalsIgnoreCase(bean.getMemberbookedid())) {
                        hideDeleteDataInTable(j, i ,mainList);
                    } else if (!Utill.isStringNullOrBlank(bean.getCoach_recursiveid())
                            && mainList.get(i).get(j).getCoach_recursiveid().equalsIgnoreCase(bean.getCoach_recursiveid())) {
                        hideDeleteDataInTable(j, i ,mainList);
                    }
                }
            }
        }

    }

    void setDataInTable(int i, int j , ArrayList<ArrayList<CoachSlot>>mainList) {
        if (tableLayout != null) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            RelativeLayout relative = (RelativeLayout) row.getChildAt(j);

            TextView t = (TextView) relative.findViewById(R.id.text);
            ImageView cross = (ImageView) relative.findViewById(R.id.cross);


            cross.setVisibility(View.INVISIBLE);
            //Log.e("longPressed" ,selectionStatus[i][j]+"");

            if (selectionStatus[i][j]) {

                t.setBackgroundColor(mContext.getResources().getColor(R.color.free_slot_color));
                selectionStatus[i][j] = false;
                selectionStatuscopy[i][j] = false;
                //if (priviousX != i || priviousY != j )
                {
                    int count = totalSelectedCourtNoList.get(j);
                    if (count > 0)
                        totalSelectedCourtNoList.set(j, new Integer(--count));
                }


            } else {

                //

                //String selectedCourtId = mainList.get(j).get(i).getCourt_id();


                Log.e("longPressed", selectionStatuscopy[i][j] + "");

                if (longPressed == true) {
                    if ((priviousX != i || priviousY != j) && selectionStatuscopy[i][j] == false) {
                        int count = totalSelectedCourtNoList.get(j);
                        totalSelectedCourtNoList.set(j, new Integer(++count));
                    }
                } else {

                    if (selectionStatuscopy[i][j] == false) {
                        int count = totalSelectedCourtNoList.get(j);
                        totalSelectedCourtNoList.set(j, new Integer(++count));
                    }

                }


                t.setBackgroundColor(mContext.getResources().getColor(R.color.green_selection_color));

                selectionStatus[i][j] = true;
                selectionStatuscopy[i][j] = true;
                daysArrayList.set(j , 1);
            }

            isbulkbook = false;


            {
                int numofCourtSelect = 0;

                for (int count = 0; count < totalSelectedCourtNoList.size(); count++) {


                    if (totalSelectedCourtNoList.get(count) != 0) {
                        numofCourtSelect = numofCourtSelect + (int)totalSelectedCourtNoList.get(count);
                    }

                    //Log.e("Break ","True");

                }
                //Log.e("isbulkbook" , isbulkbook+" "+max_book);


                if (numofCourtSelect >1)
                {
                    isbulkbook = true ;
                }
                else {
                    isbulkbook = false ;
                }

                if (isbulkbook)
                {
                    bookBtn.setText("Book");
                } else {
                    bookBtn.setText("Book");
                }
            }


            //Toast.makeText(getActivity() , "J == "+totalSelectedCourtNoList.get(j) ,1).show();
            if (databean != null) {


                hidedeleteReservationChart(databean ,mainList);

                //databean = null ;
            }


        }


        priviousX = i;
        priviousY = j;


    }

    void checkSlotSelection(ArrayList<ArrayList<CoachSlot>>mainList) {
        // boolean flag = false;
        //Log.e("checkSlotSelection" ,"Call");
        int count = 0;
        if (mainList != null && selectionStatus != null) {
            for (int i = 0; i < selectionStatus.length; i++) {
                for (int j = 0; j < selectionStatus[i].length; j++) {
                    if (selectionStatus[i][j]) {

                        count++;
                    }
                }
            }
        }


        if (count > 0) {

            showBookCancel();
        } else {
            hideBookCancel();
        }

    }
    View.OnClickListener clickOnBottomListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {


                case R.id.recursive_book:


                    showRecurSiveResrvationDialog();
                    break;



                case R.id.cancelbottom:
                    priviousX = -1;
                    priviousY = -1;

                    for (int i = 0; i < totalSelectedCourtNoList.size(); i++) {
                        totalSelectedCourtNoList.set(i, 0);
                    }
                    resetReservationChart(coachSlotArrayList);

                    break;
                case R.id.okbottom:



                    //   if (SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.) == true || SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MOBILE_ADMIN) == true)
                {





                    bookCoachJsonObej();


                    if (isCoachAsMember == false) {
                        //  singelCourtBookingByDirector();
                    } else {
                        //bookMultipleCourtByDirector();
                    }


                }
                break;

                default:
                    break;
            }

        }
    };
    final String[] paths = {"Select Duration", "1 Week", "2 Week", "3 Week", "1 Month", "2 Months", "3 Months", "4 Months", "5 Months", "6 Months"};
   Spinner durationSpinner ;
    InstantAutoComplete recus_member_auto_completetv ;
    public void showRecurSiveResrvationDialog()
    {
        searchingMemberList.clear();
     final   Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.recursive_book_coach_sloat);
        dialog.getWindow().setLayout(AppConstants.getDeviceWidth(getActivity()), LinearLayout.LayoutParams.WRAP_CONTENT);
        durationSpinner = (Spinner) dialog.findViewById(R.id.spinner);
        done_tv = (TextView) dialog.findViewById(R.id.ok);
        final TextView error_msg_tv = (TextView) dialog.findViewById(R.id.error_msg_tv);
        cancel_dialog_tv = (TextView) dialog.findViewById(R.id.cancel);


        recus_member_auto_completetv = (InstantAutoComplete) dialog.findViewById(R.id.member_auto_completetv);

        recus_member_auto_completetv.setAdapter(new MemberAutoCompleteAdapter(mContext, R.id.textViewItem, searchingMemberList ,recus_member_auto_completetv));

        ReservationTimeListAdapter dataAdapter = new ReservationTimeListAdapter(getActivity() , paths , false);
        durationSpinner.setAdapter(dataAdapter);
        durationSpinner.setSelection(0);
           beforeName = "";



        recus_member_auto_completetv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
         beforeName = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {

                String keyWord = s.toString();

                if (searchingMemberList.size() == 0 && keyWord.length() == 1 && beforeName.length() == 0)
                {
                    memberAutoCompleteAdapter = null ;
                    getSearchingMemberList(keyWord , recus_member_auto_completetv);
                }
                else
                {
                    if (Validation.isStringNullOrBlank(keyWord))
                    {
                        searchingMemberList.clear();
                    }
                    else
                    {
                        recus_member_auto_completetv.setSelection(recus_member_auto_completetv.getText().length());
                    }
                }




                boolean isMemberExits =false;
                int totalMember = 0;

                if (searchingMemberList != null && s.toString().length() != 0)
                {
                    for (int i = 0 ; i < searchingMemberList.size() ;i++)
                    {
                        String memberName =    searchingMemberList.get(i).getUser_first_name()+" "+searchingMemberList.get(i).getUser_last_name() ;

                        if (memberName.equalsIgnoreCase(keyWord))
                        {
                            isMemberExits = true ;
                            memberBean = searchingMemberList.get(i);


                        }

                        if (searchingMemberList.get(i).getUser_first_name().toLowerCase().startsWith((keyWord.toLowerCase()))||searchingMemberList.get(i).getUser_last_name().toLowerCase().startsWith((keyWord.toLowerCase()))||memberName.toLowerCase().startsWith((keyWord.toLowerCase())))
                        {
                            totalMember++;
                        }

                    }
                }
                else
                {

                    if (s.toString().length() == 0)
                    {
                        isMemberExits = true ;


                        if (searchingMemberList != null)
                        {
                            searchingMemberList.clear();
                        }
                        if (memberAutoCompleteAdapter!= null)
                        {
                            try
                            {
                                //memberAutoCompleteAdapter.notifyDataSetChanged();
                            }
                            catch (Exception e)
                            {

                            }

                        }
                    }


                }




                if (isMemberExits)
                {
                    done_tv.setAlpha(1.0f);

                }
                else
                {
                    done_tv.setAlpha(0.8f);

                }








                if (totalMember == 0)
                {
                    if (Validation.isStringNullOrBlank(keyWord))
                    {
                        error_msg_tv.setVisibility(View.GONE);
                    }
                    else
                    {
                        if (memberAutoCompleteAdapter == null)
                        {

                            error_msg_tv.setVisibility(View.GONE);
                        }
                        else
                        {
                            error_msg_tv.setVisibility(View.VISIBLE);
                        }

                    }


                }
                else
                {
                    error_msg_tv.setVisibility(View.GONE);
                }


                done_tv.setEnabled(isMemberExits);




            }
        });


        recus_member_auto_completetv.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                //Toast.makeText(getActivity(), "KeyCode "+keyCode, 1).show();

                if (keyCode == 66) {
                    InputMethodManager inputManager =
                            (InputMethodManager) getActivity().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            recus_member_auto_completetv.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
//////////

        recus_member_auto_completetv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                recus_member_auto_completetv.setText(memberAutoCompleteAdapter.getResultList().get(position).getUser_first_name()+" "+memberAutoCompleteAdapter.getResultList().get(position).getUser_last_name());
                recus_member_auto_completetv.setSelection(recus_member_auto_completetv.getText().length());

                memberBean = searchingMemberList.get(position);
                hideKeyBoard(recus_member_auto_completetv);

            }
        });


        recus_member_auto_completetv.setAdapter(new MemberAutoCompleteAdapter(mContext, R.id.textViewItem, searchingMemberList ,recus_member_auto_completetv));



        cancel_dialog_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard(recus_member_auto_completetv);
                dialog.dismiss();
            }
        });


        done_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard(recus_member_auto_completetv);




                if (durationSpinner.getSelectedItemPosition() == 0)
                {

                    ShowUserMessage.showUserMessage(getActivity() , "Please select duration");
                    return;
                }
dialog.dismiss();

                //allDates = Utill.getAllBetweenDates(selectedCalender, getRecusiveDate(durationSpinner.getSelectedItemPosition() - 1), days);
                String data =  getRecusiveDate(durationSpinner.getSelectedItemPosition() - 1);
                Log.e("last date" , data);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar lastDateCalender = Calendar.getInstance(Locale.ENGLISH);

                try
                {
                  lastDateCalender.setTime(simpleDateFormat.parse(data));
                }
                catch (Exception e)
                {

                }









                if (Validation.isStringNullOrBlank(recus_member_auto_completetv.getText().toString()))
                {
                    bookRecusiveCoach(bookRecusiveCoachJsonObej(lastDateCalender) , SessionManager.getUserId(getActivity()));

                }
                else
                {

                    try
                    {

                        String selectedUserId =  memberBean.getUser_id();
                        bookRecusiveCoach(bookRecusiveCoachJsonObej(lastDateCalender) , selectedUserId);
                     //   bookCoach(jsonObject , selectedUserId);
                    }
                    catch (Exception e)
                    {

                    }

                }

            }
        });










        dialog.show();



    }


    public JSONObject bookRecusiveCoachJsonObej(Calendar lastDate)
    {
        JSONObject slotjsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();




        try
        {
            for (int i = 0 ; i <selectionStatus.length ;i ++)
            {

                for (int j = 0 ; j <selectionStatus[i].length ;j ++)
                {
                    Log.e("i  ,  j" , i +"  ,  "+j);


                    if (selectionStatus[i][j] == true)
                    {

                        SimpleDateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm");
                        Calendar startCalendar = Calendar.getInstance(Locale.ENGLISH);
                        Calendar endCalendar = Calendar.getInstance(Locale.ENGLISH);

                        endCalendar.setTime(simpleDateFormat.parse(coachSlotArrayList.get(j).get(i).getDate()+" "+coachSlotArrayList.get(j).get(i).getCoach_end_time()));
                        startCalendar.setTime(simpleDateFormat.parse(coachSlotArrayList.get(j).get(i).getDate()+" "+coachSlotArrayList.get(j).get(i).getCoach_start_time()));


                        while(startCalendar.before(lastDate))
                        {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("strart_time" ,serverDateFormat.format(startCalendar.getTime()));
                            jsonObject.put("end_time" ,serverDateFormat.format(endCalendar.getTime()));
                            jsonArray.put(jsonObject);
                            startCalendar.add(Calendar.DATE ,7);
                            endCalendar.add(Calendar.DATE ,7);

                        }





                        Log.e("main list " , startCalendar.getTime()+"");
                    }

                }

            }
            slotjsonObject.put("recursive",jsonArray);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return slotjsonObject ;
    }



    public void bookRecusiveCoach(JSONObject jsonObject , String userId)
    {
        HashMap<String , Object>param = new HashMap<>();
        param.put("coach_user_id" , SessionManager.getUser_id(getActivity()));
        param.put("coach_club_id" , SessionManager.getUser_Club_id(getActivity()));
        param.put("coach_coach_id" , coach_id);
        param.put("coach_mem_id" , userId);
        param.put("recursive" , jsonObject.toString());
        httpRequest.getResponse(getActivity(), WebService.coach_recursivereservation, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
            showMessage(jsonObject.optString("message"));
            }
        });


    }









    public void bookCoachJsonObej()
    {
        JSONObject slotjsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();




        try
        {
            for (int i = 0 ; i <selectionStatus.length ;i ++)
            {
                for (int j = 0 ; j <selectionStatus[i].length ;j ++)
                {
                    if (selectionStatus[i][j] == true)
                    {

                        JSONObject jsonObject = new JSONObject();
                        SimpleDateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                        Calendar startCalendar = Calendar.getInstance(Locale.ENGLISH);
                        Calendar endCalendar = Calendar.getInstance(Locale.ENGLISH);

                        endCalendar.setTime(simpleDateFormat.parse(coachSlotArrayList.get(j).get(i).getDate()+" "+coachSlotArrayList.get(j).get(i).getCoach_end_time()));
                        startCalendar.setTime(simpleDateFormat.parse(coachSlotArrayList.get(j).get(i).getDate()+" "+coachSlotArrayList.get(j).get(i).getCoach_start_time()));
                        jsonObject.put("strart_time" ,serverDateFormat.format(startCalendar.getTime()));
                        jsonObject.put("end_time" ,serverDateFormat.format(endCalendar.getTime()));
                        jsonArray.put(jsonObject);



                        Log.e("main list " , startCalendar.getTime()+"");
                    }

                }

            }
            slotjsonObject.put("time",jsonArray);
            if (isCoachAsMember)
            {
                showDialogForBookOtherMember(slotjsonObject);
            }
            else
            {
                bookCoach(slotjsonObject , SessionManager.getUserId(getActivity()));
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    TextView done_tv;


    MemberListBean memberBean;

    InstantAutoComplete member_auto_completetv;
    String beforeName = "" ;
    public void showDialogForBookOtherMember(final JSONObject jsonObject)
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.coach_res_other_memlayout);
        done_tv = (TextView) dialog.findViewById(R.id.done_tv);
        searchingMemberList.clear();
        cancel_dialog_tv = (TextView) dialog.findViewById(R.id.cancel_dialog_tv);
        final TextView error_msg_tv = (TextView) dialog.findViewById(R.id.error_msg_tv);

        beforeName = "" ;
        dialog.getWindow().setLayout(AppConstants.getDeviceWidth(getActivity()),LinearLayout.LayoutParams.WRAP_CONTENT);
        member_auto_completetv = (InstantAutoComplete) dialog.findViewById(R.id.member_auto_completetv);

        member_auto_completetv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeName = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {

                String keyWord = s.toString();

                if (searchingMemberList.size() == 0 && keyWord.length() == 1 && beforeName.length() == 0)
                {
                    memberAutoCompleteAdapter = null ;
                    getSearchingMemberList(keyWord , member_auto_completetv);
                }
                else
                {
                    if (Validation.isStringNullOrBlank(keyWord))
                    {
                        searchingMemberList.clear();
                    }
                    else
                    {
                        member_auto_completetv.setSelection(member_auto_completetv.getText().length());
                    }
                }



             //Toast.makeText(getActivity() , "Length  "+searchingMemberList.size() ,1).show();




                boolean isMemberExits =false;
                int totalMember = 0;

                if (searchingMemberList != null && s.toString().length() != 0)
                {
                    for (int i = 0 ; i < searchingMemberList.size() ;i++)
                    {
                        String memberName =    searchingMemberList.get(i).getUser_first_name()+" "+searchingMemberList.get(i).getUser_last_name() ;

                        if (memberName.equalsIgnoreCase(keyWord))
                        {
                            isMemberExits = true ;
                            memberBean = searchingMemberList.get(i);


                        }

                        if (searchingMemberList.get(i).getUser_first_name().toLowerCase().startsWith((keyWord.toLowerCase()))||searchingMemberList.get(i).getUser_last_name().toLowerCase().startsWith((keyWord.toLowerCase()))||memberName.toLowerCase().startsWith((keyWord.toLowerCase())))
                        {
                            totalMember++;
                        }

                    }
                }
                else
                {

                    if (s.toString().length() == 0)
                    {
                        isMemberExits = true ;


                        if (searchingMemberList != null)
                        {
                            searchingMemberList.clear();
                        }
                        if (memberAutoCompleteAdapter!= null)
                        {
                            try
                            {
                                //memberAutoCompleteAdapter.notifyDataSetChanged();
                            }
                            catch (Exception e)
                            {

                            }

                        }
                    }


                }




                if (isMemberExits)
                {
                    done_tv.setAlpha(1.0f);

                }
                else
                {
                    done_tv.setAlpha(0.8f);

                }
                /*if (totalMember != 0 || Validation.isStringNullOrBlank(keyWord)   )
                {
                    error_msg_tv.setVisibility(View.GONE);
                }
                else
                {
                    if (searchingMemberList.size() == 0)
                    {

                    }
                    else
                    {
                        error_msg_tv.setVisibility(View.GONE);
                    }




                }*/


                if (totalMember == 0)
                {
                    if (Validation.isStringNullOrBlank(keyWord))
                    {
                        error_msg_tv.setVisibility(View.GONE);
                    }
                    else
                    {
                        if (memberAutoCompleteAdapter == null)
                        {

                            error_msg_tv.setVisibility(View.GONE);
                        }
                        else
                        {
                            error_msg_tv.setVisibility(View.VISIBLE);
                        }

                    }


                }
                else
                {
                    error_msg_tv.setVisibility(View.GONE);
                }



                done_tv.setEnabled(isMemberExits);


            }
        });


        member_auto_completetv.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                //Toast.makeText(getActivity(), "KeyCode "+keyCode, 1).show();

                if (keyCode == 66) {
                    InputMethodManager inputManager =
                            (InputMethodManager) getActivity().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            member_auto_completetv.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

/////
        member_auto_completetv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Utill.hideKeybord(getActivity() , member_auto_completetv);
                memberBean = memberAutoCompleteAdapter.getResultList().get(position);
                member_auto_completetv.setText(memberAutoCompleteAdapter.getResultList().get(position).getUser_first_name()+" "+memberAutoCompleteAdapter.getResultList().get(position).getUser_last_name());
                member_auto_completetv.setSelection(member_auto_completetv.getText().length());


            }
        });


        member_auto_completetv.setAdapter(new MemberAutoCompleteAdapter(mContext, R.id.textViewItem, searchingMemberList ,member_auto_completetv));



        cancel_dialog_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyBoard(member_auto_completetv);
                dialog.dismiss();
            }
        });


        done_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

hideKeyBoard(member_auto_completetv);
                dialog.dismiss();

                if (Validation.isStringNullOrBlank(member_auto_completetv.getText().toString()))
                {
                    bookCoach(jsonObject , SessionManager.getUserId(getActivity()));
                }
                else
                {
                    hideKeyBoard(member_auto_completetv);

                    try
                    {

                        String selectedUserId =  memberBean.getUser_id();
                        bookCoach(jsonObject , selectedUserId);
                    }
                    catch (Exception e)
                    {

                    }

                }

            }
        });





        dialog.show();
    }



    public void bookCoach(JSONObject jsonObject ,final String bookedUserId)
    {
        HashMap<String , Object> param = new HashMap<String , Object>();
        param.put("coach_coach_id" ,coach_id);
        param.put("coach_mem_id" , bookedUserId);
        param.put("coach_club_id" ,SessionManager.getUser_Club_id(getActivity()));
        param.put("coach_user_id" ,SessionManager.getUserId(getActivity()));
        param.put("user_type" ,SessionManager.getUser_type(getActivity()));
        param.put("slots" ,jsonObject.toString());

        httpRequest.getResponse(getActivity(), WebService.coachreservations, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                Log.e("jsonObject" ,jsonObject+"");
                try
                {
                    if (jsonObject.getBoolean("status"))
                    {
                        if (bookedUserId.equals(SessionManager.getUser_id(getActivity())) && isCoachAsMember == false)
                        {
                            insertDataEventCalender(jsonObject);
                        }
                        else
                        {
                            showMessage(jsonObject.optString("message"));

                        }


                    }
                    else
                    {
                        Utill.showDialg(jsonObject.getString("message"),getActivity());
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }


    android.app.ProgressDialog pd ;


    public void showPorgress()
    {
        pd = new android.app.ProgressDialog(getActivity());
        pd.setCancelable(false);
        pd.setMessage("Loading...");
        pd.show();
    }
    public void dismissDialog()
    {
        pd.dismiss();
    }



    public void deleteReservation(String coach_mem_id , final String memberbookedid , String reason)
    {
        HashMap<String , Object>param = new HashMap<String , Object>();
        param.put("coach_user_id" ,SessionManager.getUserId(getActivity()));
        param.put("coach_club_id" ,SessionManager.getUser_Club_id(getActivity()));
        param.put("coach_mem_id" ,coach_mem_id);
        param.put("memberbookedid" ,memberbookedid);
        param.put("reason" ,reason);

        httpRequest.getResponse(getActivity(), WebService.deletecoachbooking, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                try
                {
                    Uri eventsUri = Uri.parse("content://com.android.calendar/" + "events");

                    ArrayList<Integer> eventIds = sqlListe.getCoachReservationEventIds(memberbookedid);


                    for (int i = 0; i < eventIds.size(); i++) {
                        int event_id = eventIds.get(i);
                        getActivity().getApplicationContext().getContentResolver().delete(ContentUris.withAppendedId(eventsUri, event_id), null, null);

                    }
                    sqlListe.deleteeventlistforcochreservationEvents(Integer.parseInt(memberbookedid) );
                    showMessage(jsonObject.getString("message"));
                }
                catch (Exception e)
                {
                Utill.showDialg(e.getMessage() , getActivity());
                }



                //  ShowUserMessage.showUserMessage(getActivity() ,jsonObject.toString());
            }
        });



    }
    public  void showMessage( String msg)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(
                getActivity()).create();


        // Setting Dialog Title
        alertDialog.setTitle(SessionManager.getClubName( getActivity()));
        alertDialog.setCancelable(false);
        // Setting Dialog Message
        alertDialog.setMessage(msg);

        // Setting Icon to Dialog


        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                priviousX = -1;
                priviousY = -1;

                for (int i = 0; i < totalSelectedCourtNoList.size(); i++) {
                    totalSelectedCourtNoList.set(i, 0);
                }
                resetReservationChart(coachSlotArrayList);


                getCoachTimeSoltReservation(selectedCalender);

                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    public View.OnClickListener nextpriviouesDataClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            if (v.getId() == R.id.privious_iv)
            {

                Calendar currentTimeCalClone = (Calendar) currentTimeCal.clone();
                currentTimeCalClone.add(Calendar.DATE , -31);

                if (selectedCalender.compareTo(currentTimeCalClone) <0 )
                {
                    Utill.showDialg("You can view only past 1 month reservation", getActivity());

                    return;
                }

                selectedCalender.add(Calendar.DATE , -7);

            }

            if (v.getId() == R.id.next_iv)
            {
                selectedCalender.add(Calendar.DATE , 7);

            }
            Calendar afterSevenDays = (Calendar) selectedCalender.clone();
            afterSevenDays.add(Calendar.DATE , 6);

            SimpleDateFormat dateFormat1 = new SimpleDateFormat("MMM dd yyyy");
            date_duration_tv.setText(dateFormat1.format(selectedCalender.getTime()) +"  -  "+dateFormat1.format(afterSevenDays.getTime()));

            getCoachTimeSoltReservation(selectedCalender);
        }
    };


    private void getMembersList() {
        sqlListe.getMemberList(getActivity(), new MemberListListener() {
            @Override
            public void onSuccess(ArrayList<MemberListBean> memberListBeanArrayList)
            {




            }
        },AppConstants.AllMEMBERlIST ,"");
    }


    public void getSearchingMemberList(final String keyWord , final  InstantAutoComplete member_auto_completetv) {
        searchingMemberList.clear();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_type", AppConstants.USER_TYPE_DIRECTOR);
        params.put("user_name", keyWord);
        params.put("club_id", SessionManager.getUser_Club_id(mContext));

        httpRequest.getResponse(getActivity(), WebService.coach_list, params, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject) {


                Log.e("jsonObject" , jsonObject+"");


                if(jsonObject.optBoolean("status"))
                {

                    try
                    {
                        JSONArray jsonArray = jsonObject.optJSONArray("member");

                        for (int i = 0 ; i < jsonArray.length() ; i++)
                        {
                            MemberListBean memberListBean = new MemberListBean();
                            JSONObject jsonArrayItem = jsonArray.optJSONObject(i);
                            memberListBean.setUser_id(jsonArrayItem.optString("user_id"));
                            memberListBean.setUser_first_name(jsonArrayItem.optString("user_first_name"));
                            memberListBean.setUser_last_name(jsonArrayItem.optString("user_last_name"));
                            memberListBean.setUser_email(jsonArrayItem.optString("user_email"));
                            memberListBean.setUser_profilepic(jsonArrayItem.optString("user_profilepic"));
                            memberListBean.setUser_phone(jsonArrayItem.optString("user_phone"));
                            memberListBean.setUser_status(jsonArrayItem.optString("user_status"));
                            memberListBean.setUser_type(jsonArrayItem.optString("user_type"));
                            searchingMemberList.add(memberListBean);

                        }
                    }
                    catch (Exception e)
                    {

                    }


                    memberAutoCompleteAdapter = new MemberAutoCompleteAdapter(getActivity() , R.id.textViewItem , searchingMemberList ,member_auto_completetv);
                    member_auto_completetv. setAdapter(memberAutoCompleteAdapter);
                    member_auto_completetv.setText(keyWord+"");


                }
                else
                {
                    ShowUserMessage.showUserMessage(getActivity() , jsonObject.optString("message"));
                }



            }
        });





    }
    public static final int ONE_WEEK = 0;
    public static final int TWO_WEEK = 1;
    public static final int THREE_WEEK = 2;
    public static final int ONE_MONTH = 3;
    public static final int TWO_MONTH = 4;
    public static final int THREE_MONTH = 5;
    public static final int FOUR_MONTH = 6;
    public static final int FIVE_MONTH = 7;
    public static final int SIX_MONTH = 8;

    String getRecusiveDate(int position) {
        int incrementIn = 0;
        int count = 0;
        switch (position) {
            case ONE_WEEK:
                incrementIn = Calendar.WEEK_OF_MONTH;
                count = 1;
                break;
            case TWO_WEEK:
                incrementIn = Calendar.WEEK_OF_MONTH;
                count = 2;
                break;
            case THREE_WEEK:
                incrementIn = Calendar.WEEK_OF_MONTH;
                count = 3;
                break;
            case ONE_MONTH:
                incrementIn = Calendar.MONTH;
                count = 1;
                break;
            case TWO_MONTH:
                incrementIn = Calendar.MONTH;
                count = 2;
                break;
            case THREE_MONTH:
                incrementIn = Calendar.MONTH;
                count = 3;
                break;
            case FOUR_MONTH:
                incrementIn = Calendar.MONTH;
                count = 4;
                break;
            case FIVE_MONTH:
                incrementIn = Calendar.MONTH;
                count = 5;
                break;
            case SIX_MONTH:
                incrementIn = Calendar.MONTH;
                count = 6;
                break;

            default:
                break;
        }
        String finalD = Utill.getRecurrentFinalDate(incrementIn, count,Utill.reverseDate( Utill.getCurrentDate()));
        // Utill.showToast(mContext,""+finalD);
        return finalD;
    }


    TextView single_booking_delete_tv;
    TextView all_booking_delete_tv;
    TextView cancel_dialog_tv;
Dialog recursiveReservationDeleteDialog ;
    EditText cancel_reason_edit_tv ;
    String reservationParticularDeletaDate = "";
    String memberBookedId = "";
    public void showDialogForRecusiveReservationDelete(String memberBookedId , String recusiveId , String date)
    {
        reservationParticularDeletaDate = date ;
        recursiveReservationDeleteDialog = new Dialog(getActivity());
        recursiveReservationDeleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        recursiveReservationDeleteDialog.setContentView(R.layout.recur_reser_delete_dialog);
        recursiveReservationDeleteDialog.getWindow().setLayout(AppConstants.getDeviceWidth(getActivity()) , LinearLayout.LayoutParams.WRAP_CONTENT);
        single_booking_delete_tv = (TextView) recursiveReservationDeleteDialog.findViewById(R.id.single_booking_delete_tv);
        all_booking_delete_tv = (TextView) recursiveReservationDeleteDialog.findViewById(R.id.all_booking_delete_tv);
        cancel_dialog_tv = (TextView) recursiveReservationDeleteDialog.findViewById(R.id.cancel_dialog_tv);
        single_booking_delete_tv.setOnClickListener(deleteRecdialogItemClickListener);
        all_booking_delete_tv.setOnClickListener(deleteRecdialogItemClickListener);
        single_booking_delete_tv.setTag(recusiveId);
        all_booking_delete_tv.setTag(recusiveId);
        this.memberBookedId = memberBookedId;
        cancel_reason_edit_tv = (EditText) recursiveReservationDeleteDialog.findViewById(R.id.cancel_reason_edit_tv);

        if ((SessionManager.getUser_id(getActivity()).equals(memberBookedId) && isCoachAsMember) )
        {
            cancel_reason_edit_tv.setVisibility(View.GONE);
        }

        TextView header = (TextView) recursiveReservationDeleteDialog.findViewById(R.id.header);
        header.setText(SessionManager.getClubName(getActivity()));

        cancel_dialog_tv.setOnClickListener(deleteRecdialogItemClickListener);


        recursiveReservationDeleteDialog.show();

    }



    public View.OnClickListener deleteRecdialogItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            hideKeyBoard(cancel_reason_edit_tv);

            if (v.getId() == R.id.single_booking_delete_tv )
            {

                if ((SessionManager.getUser_id(getActivity()).equals(memberBookedId) && isCoachAsMember) == false)
                {
                    if (Validation.isStringNullOrBlank(cancel_reason_edit_tv.getText().toString()))
                    {
                        ShowUserMessage.showUserMessage(getActivity() , "Please enter reason");
                        return;
                    }
                }






                hideKeyBoard(cancel_reason_edit_tv);
                String recusiveId = (String) v.getTag();
                deleteRecursiveBooking(memberBookedId ,recusiveId , cancel_reason_edit_tv.getText().toString() , reservationParticularDeletaDate);
                if (recursiveReservationDeleteDialog != null)
                {
                    recursiveReservationDeleteDialog.cancel();
                }
            }



            if ( v.getId() == R.id.all_booking_delete_tv)
            {

                if ((SessionManager.getUser_id(getActivity()).equals(memberBookedId) && isCoachAsMember) == false)
                {
                    if (Validation.isStringNullOrBlank(cancel_reason_edit_tv.getText().toString()))
                    {
                        ShowUserMessage.showUserMessage(getActivity() , "Please enter reason");
                        return;
                    }
                }



                hideKeyBoard(cancel_reason_edit_tv);
                String recusiveId = (String) v.getTag();








                deleteRecursiveBooking(memberBookedId ,recusiveId , cancel_reason_edit_tv.getText().toString(), "");
                if (recursiveReservationDeleteDialog != null)
                {
                    recursiveReservationDeleteDialog.cancel();
                }
            }





            if (v.getId() == R.id.cancel_dialog_tv)
            {
                hideKeyBoard(cancel_reason_edit_tv);

                if (recursiveReservationDeleteDialog != null)
                {
                    recursiveReservationDeleteDialog.cancel();
                }
            }

        }
    };


    public void deleteRecursiveBooking(String memberBookedId ,  String recursiveId , String reason , String date)
    {
       HashMap<String,Object>param = new HashMap<>();


        param.put("coach_mem_id", memberBookedId);
        param.put("coach_user_id", SessionManager.getUser_id(getActivity()));
        param.put("coach_club_id", SessionManager.getUser_Club_id(getActivity()));

        param.put("coach_date", date);

        param.put("coach_recursiveid", recursiveId);
        param.put("reason", reason);
        httpRequest.getResponse(getActivity(), WebService.coach_deleterecursive, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject) {

                showMessage(jsonObject.optString("message"));
            }
        });


    }


    public void hideKeyBoard(EditText editText)
    {
        InputMethodManager inputManager =
                (InputMethodManager) getActivity().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                editText.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public void insertDataEventCalender(final JSONObject jsonObj) {





        try {

            JSONArray courtInfoJsonArray = jsonObj.getJSONArray("CourtInfo");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Calendar startTimeCal = Calendar.getInstance();
            Calendar endTimeCal = Calendar.getInstance();


            int bookingId = 0 ;
            String timeSlot = sessionManager.getAlaramTimeDuration(getActivity());


            for (int i = 0; i < courtInfoJsonArray.length(); i++)
            {
                JSONObject courtInfoJsonArrayItem = courtInfoJsonArray.getJSONObject(i);
                bookingId = Integer.parseInt(courtInfoJsonArrayItem.getString("bookingid"));
                endTimeCal.setTime(simpleDateFormat.parse(courtInfoJsonArrayItem.getString("end_time")));
                if (i == 0)
                {
                    startTimeCal.setTime(simpleDateFormat.parse(courtInfoJsonArrayItem.getString("strart_time")));

                }


                if (courtInfoJsonArray.length() == (i+1))
                {
                    insertDataEventCalender(startTimeCal.getTimeInMillis(), endTimeCal.getTimeInMillis(), bookingId, coachName, timeSlot);

                }
                else
                {
                    JSONObject courtInfoJsonArrayItemnext = courtInfoJsonArray.getJSONObject(i+1);

                    int nextbookingId = Integer.parseInt(courtInfoJsonArrayItemnext.getString("bookingid")) ;


                    if (nextbookingId != bookingId)
                    {
                        insertDataEventCalender(startTimeCal.getTimeInMillis(), endTimeCal.getTimeInMillis(), bookingId, coachName, timeSlot);
                        startTimeCal.setTime(simpleDateFormat.parse(courtInfoJsonArrayItemnext.getString("strart_time")));

                    }

                }






            }

            showMessage(jsonObj.getString("message"));
        } catch (Exception e) {
            ShowUserMessage.showDialogOnActivity(getActivity(), e.getMessage());
        }


    }




  int reminderMinut;
    public void insertDataEventCalender(final long startTime, final long endTime, final int bookingid, String coachName, String timeMinut) {
        try {

            reminderMinut = Integer.parseInt(timeMinut.split(" ")[0]);



        } catch (Exception e) {
            reminderMinut = 0 ;
        }

        try {
            ContentResolver cr = getActivity().getContentResolver();
            ContentValues values = new ContentValues();

            values.put(CalendarContract.Events.DTSTART, startTime);
            values.put(CalendarContract.Events.DTEND, endTime);

            String titel = "Reminder : "+SessionManager.getClubName(getContext())+" "+"Lesson with  "+coachName;

            values.put(CalendarContract.Events.TITLE, titel);
            //values.put(CalendarContract.Events.DESCRIPTION, comment);

            TimeZone timeZone = TimeZone.getDefault();
            values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());

            // default calendar
            values.put(CalendarContract.Events.CALENDAR_ID, 1);

            //values.put(CalendarContract.Events.RRULE, "FREQ=DAILY;UNTIL=" + dtUntill);
            //for one hour

            //values.put(CalendarContract.Events.RRULE, "FREQ=YEARLY");
            //values.put(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
            if (reminderMinut == 0)
            {
                //   values.put(CalendarContract.Events.HAS_ALARM, 0);
            }
            else
            {
                values.put(CalendarContract.Events.HAS_ALARM, 1);
            }


            // insert event to calendar
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);


            //cr.delete(CalendarContract.Events.CONTENT_URI ,CalendarContract.Reminders.EVENT_ID +" = ?", new String[]{1});

            int id = Integer.parseInt(uri.getLastPathSegment());

            sqlListe.addEventForCoachReservation(id, bookingid);
            // String reminderUriString = "content://com.android.calendar/reminders";

            ContentValues reminders = new ContentValues();
            reminders.put(CalendarContract.Reminders.EVENT_ID, id);

            if(reminderMinut != 0)
            {
                reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                reminders.put(CalendarContract.Reminders.MINUTES, reminderMinut);
                Uri uri2 = cr.insert(CalendarContract.Reminders.CONTENT_URI, reminders);

            }








        } catch (Exception e) {

        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }
}
