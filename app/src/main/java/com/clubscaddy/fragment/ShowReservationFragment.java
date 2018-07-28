package com.clubscaddy.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;

import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.bumptech.glide.util.Util;
import com.clubscaddy.Adapter.ReservationTimeListAdapter;
import com.clubscaddy.Adapter.SelectCategoryListAdapter;
import com.clubscaddy.Bean.RecursiveBookingCourtColour;
import com.clubscaddy.Interface.FragmentBackListener;
import com.clubscaddy.Interface.FragmentBackResponseListener;
import com.clubscaddy.Interface.MemberListListener;
import com.clubscaddy.Interface.ModelManager;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.MemberAutoCompleteAdapter;
import com.clubscaddy.Adapter.MembersSelectionAdapter;
import com.clubscaddy.Adapter.MyReservationListAdapter;
import com.clubscaddy.Bean.CourtBookBean;
import com.clubscaddy.Bean.CourtData;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.Bean.MyReservationBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.custumview.InstantAutoComplete;
import com.clubscaddy.custumview.InternetConnection;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.utility.SqlListe;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ShowReservationFragment extends Fragment implements OnClickListener, OnTouchListener {
    public static final String deleteRecursiveArray[] = {"Just delete for selected date", "Delete all recursive reservations", "Cancel"};
    public static final int ONE_WEEK = 0;
    public static final int TWO_WEEK = 1;
    public static final int THREE_WEEK = 2;
    public static final int ONE_MONTH = 3;
    public static final int TWO_MONTH = 4;
    public static final int THREE_MONTH = 5;
    public static final int FOUR_MONTH = 6;
    public static final int FIVE_MONTH = 7;
    public static final int SIX_MONTH = 8;
    public static final String SUNDAY = "1";
    public static final String MONDAY = "2";
    public static final String TUESDAY = "3";
    public static final String WEDNESDAY = "4";
    public static final String THURSDAY = "5";
    public static final String FRIDAY = "6";
    public static final String SATUARDAY = "7";
    public static final int START_DATE = 1;
    public static final int END_DATE = 2;
    private static final String tag = "MyCalendarActivity";
    private static final String dateTemplate = "MMMM yyyy";
    private final static int START_DRAGGING = 0;
    private final static int STOP_DRAGGING = 1;
    public static FragmentManager mFragmentManager;
    public static Fragment mFragment;
    static String calendarDate = Utill.reverseDate(Utill.getCurrentDate());
    static String userId = "";
    final String[] paths = {"Select Duration", "1 Week", "2 Weeks", "3 Weeks", "1 Month", "2 Months", "3 Months", "4 Months", "5 Months", "6 Months"};
    private final DateFormat dateFormatter = new DateFormat();
    //
    public int max_book = 0;
    public boolean isdialogshow = true;
    ArrayList<ArrayList<CourtData>> mainList;
    String Tag = getClass().getName();
    Context mContext;
    RelativeLayout bottoms;
    LinearLayout mainLayout;
    LinearLayout all_reservation_linear_layout;
    View all_reservation_view;
    LinearLayout my_reservation_linear_layout;


    ListView my_reservation_list_view;
    ArrayList<MyReservationBean> myReservationList = new ArrayList<MyReservationBean>();
    RelativeLayout right_layout;
    // ImageButton actionbar_backbutton1;
    LinearLayout left_layout;
    Calendar selected_calender;
    Calendar currentTimeCal;
    Calendar endTimeClub;
    View my_reservation_view;
    TextView datePicker, oldDateMSG;
    int advacsedbookingdays = 0;
    String CourtName = "";
    boolean isbulkbook = false;
    int scrollY;
    int scrollX;
    Calendar max_cal;
    int slotWidth;
    int slotHeight;
    boolean longPressed = false;
    boolean firsttimelongPressed = false;
    boolean mainlayoutScroll = false;
    int selectedLongPressedItem;
    int selectedLongPressedItemX;
    CourtData databean1;
    CourtData databean;
    String selectedCourtReservationId = "";

    String selectedReservationIndex = "" ;

    SessionManager sessionManager;
    MyReservationListAdapter myReservationListAdapter ;
    ArrayList<Integer> totalSelectedCourtNoList;
    boolean isScrolled ;
    //end
    int priviousX = -1, priviousY = -1;
    AQuery aqQuery;
    /*LinearLayout userDateLL;*/
    /*ImageView previousDate, NextDate;*/
    //TextView memberDate;
    boolean isDataPassed;
    ImageButton add_reservation_btn;
    OnClickListener addToBack = new OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                DirectorFragmentManageActivity.popBackStackFragment();
            } catch (Exception e) {
                ShowUserMessage.showUserMessage(getActivity(), e.toString());
            }
        }
    };
    ScrollView sv;
    HorizontalScrollView hsv;
    TableLayout tableLayout;
    boolean selectionStatus[][];
    boolean selectionStatuscopy[][];
    boolean delete = false;
    String deleteID = "";
    String court_reservation_id = "";
    CourtData deleteCourtData;
    SqlListe sqlListe;
    ListView listView;
    AlertDialog reservationSelectionPopup;
    AlertDialog calendarDialogue;
    // ArrayList<String>
    HashSet<String> selectedDateList;
    CourtData moveSlotBean;
    String beforname;
    AlertDialog memeberPopUp;
    ArrayList<MemberListBean> membersList;
    ArrayList<MemberListBean> filteredList;
    ListView memberListView;
    MemberListBean memberBean;
    HttpRequest httpRequest;

    //boolean ismoving = false;
    ArrayList<RecursiveBookingCourtColour> recursiveBookingCourtColours = new ArrayList<RecursiveBookingCourtColour>();
    HashSet<String> days = new HashSet<String>();
    LinearLayout sunLL, monLL, tueLL, wedLL, thuLL, friLL, satLL;
    TextView sunTV, monTV, tueTV, wedTV, thuTV, friTV, satTV;
    ImageView sunTick, monTick, tueTick, wedTick, thuTick, friTick, satTick;





    OnClickListener daysClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.sun:
                    updateDateUi(SUNDAY);
                    break;
                case R.id.mon:
                    updateDateUi(MONDAY);
                    break;
                case R.id.tue:
                    updateDateUi(TUESDAY);
                    break;
                case R.id.wed:
                    updateDateUi(WEDNESDAY);
                    break;
                case R.id.thu:
                    updateDateUi(THURSDAY);
                    break;
                case R.id.fri:
                    updateDateUi(FRIDAY);
                    break;
                case R.id.sat:
                    updateDateUi(SATUARDAY);
                    break;
            }

        }
    };
    int flag = 0;
    float xAxis = 0f;
    float yAxis = 0f;
    float lastXAxis = 0f;
    float lastYAxis = 0f;
    LinearLayout bottomLayout;
    public OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {


            //Toast.makeText(getActivity() , "action "+ event.getAction(),1).show();


            if (event.getAction() == 2)
            {
                longPressed = true;
            }
            try {
                // For ScrollView
                scrollX = hsv.getScrollX();
                scrollX = scrollX + (int) event.getX();

                //Log.e("firsttimelongPressed   " , firsttimelongPressed+"    ");

                if (sv.getScrollY() == 0) {
                    scrollY = sv.getScrollY();
                } else {
                    scrollY = mainLayout.getScrollY();


                    int itemPos = (scrollX) / slotWidth;


                    int selectedLongPressedPrivItem = selectedLongPressedItem - 1;
                    int selectedLongPressedNextItem = selectedLongPressedItem + 1;


                    //Log.e("selectedLongPressedItem" , selectedLongPressedItem+"");

//


                    if (firsttimelongPressed) {
                        //Log.e("selectedLongPressedItem" ,selectedLongPressedPrivItem+"  "+ itemPos+" "+selectedLongPressedNextItem);

                        if (selectedLongPressedPrivItem == itemPos || selectedLongPressedNextItem == itemPos) {
                            if (selectedLongPressedItem == itemPos) {
                                return true;
                            }
                            scrollY = mainLayout.getScrollY();
                            //Log.e("selectedLongPressedItem" , itemPos+" "+selectedLongPressedItem);
                            mainlayoutScroll = true;
                            firsttimelongPressed = false;

                        } else {
                            if (sv.getScaleY() == 0)
                            {
                                scrollY = mainLayout.getScrollY();

                            }
                            else {
                                scrollY = sv.getScrollY();

                            }
                            mainlayoutScroll = false;
                        }
                    } else {
                        if (mainlayoutScroll == true) {
                            scrollY = mainLayout.getScrollY();
                        } else {
                            if (sv.getScaleY() == 0)
                            {
                                scrollY = mainLayout.getScrollY();

                            }
                            else {
                                scrollY = sv.getScrollY();

                            }                        }


                    }


                }

                Log.e("mainlayoutScroll",scrollY+"");

                scrollY = scrollY + (int) event.getY();


                int scrollY1 = mainLayout.getScrollY() + (int) event.getY();


                if (longPressed) {


                    int i = (scrollX / slotWidth);
                    int j = (scrollY / slotHeight);
                    int j1 = (scrollY1 / slotHeight);


                    if (j1 != selectedLongPressedItemX) {
                        firsttimelongPressed = false;
                        //Log.e("selectedLongPressedItemX" , selectedLongPressedItemX+" "+j1);
                    }


                    CourtData databean1;


                    databean1 = mainList.get(i).get(j);
///////
                    //  uniqueId
                    if ( databean1.isCourtSelectable())
                    {
                        selectionStatus[j][i] = false;

                        //selectionStatuscopy[j][i] = true ;
                        //setDataInTable(j, i);
                        //checkSlotSelection();


                        String localUniqueId = "" ;


                        if (Validation.isStringNullOrBlank(databean1.getMemberbookedid()) == false)
                        {
                            localUniqueId = databean1.getMemberbookedid() ;
                        }
                        else
                        {
                            localUniqueId = databean1.getRecursive_id() ;
                        }

                        Log.e("delete status" , " "+delete);

                        if ((delete == false &&Validation.isStringNullOrBlank(databean1.getCourt_reservation_id()))||(delete && Validation.isStringNullOrBlank(uniqueId) == false && uniqueId.equals(localUniqueId)))
                        {




                            selectionStatus[j][i] = false;

                            //selectionStatuscopy[j][i] = true ;
                            setDataInTable(j, i);
                            checkSlotSelection();
                        }
                    }
                    else
                    {

                        String localUniqueId = "" ;


                        if (Validation.isStringNullOrBlank(databean1.getMemberbookedid()) == false)
                        {
                            localUniqueId = databean1.getMemberbookedid() ;
                        }
                        else
                        {
                            localUniqueId = databean1.getRecursive_id() ;
                        }

                        Log.e("delete status" , " "+delete);

                        if (delete && Validation.isStringNullOrBlank(uniqueId) == false && uniqueId.equals(localUniqueId))
                        {
                            selectionStatus[j][i] = false;

                            //selectionStatuscopy[j][i] = true ;
                            setDataInTable(j, i);
                            checkSlotSelection();
                        }
                    }


                }

            } catch (Exception e) {

            }


            if (event.getAction() == 2) {
                return true;
            } else {
                sv.setOnTouchListener(null);
                hsv.setOnTouchListener(null);
                longPressed = false;
//
                priviousX = -1;
                priviousY = -1;
                return false;
            }


        }
    };
    TextView cancelBottomBtn, bookBtn, recursiveBookBtn;
    String date = "";
    boolean isMemberInSameSlot;
    OnClickListener clickOnBottomListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (membersList != null)
                membersList.clear();
            switch (id) {
                case R.id.cancelbottom:
                    priviousX = -1;
                    priviousY = -1;

                    for (int i = 0; i < totalSelectedCourtNoList.size(); i++) {
                        totalSelectedCourtNoList.set(i, 0);
                    }
                    resetReservationChart();

                    break;
                case R.id.okbottom:

                    if (memeberPopUp != null) {
                        try {
                            memeberPopUp.cancel();
                        } catch (Exception e) {

                        }
                    }





               /*  int totalBookingCourt = 0;

					for (int i = 0; i < selectionStatus.length; i++)
					{
						for (int j = 0; j < selectionStatus[i].length; j++)
						{
							if (selectionStatus[i][j])
							{
								totalBookingCourt++ ;
							}
						}

					}*/




                    isbulkbook = false ;


                    SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm");
                    long standredSoltDiffrence  = 0;
                    try {
                        standredSoltDiffrence =  timeFormate.parse( mainList.get(0).get(0).getCourt_end_time()).getTime() - timeFormate.parse( mainList.get(0).get(0).getCourt_start_time()).getTime() ;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String startDate = "" ,courtName = "" ,reserveStartDate = "" ,reserveCourtName = "";
                    int totalrowSelected = 0 ;


                    int totalSelectedCourt = 0 ;


                    isMemberInSameSlot = false ;



                    ArrayList<ArrayList<CourtData>> copyMainList = transpose(mainList) ;






                    for (int i = 0 ; i < selectionStatus.length ;i ++)
                    {
                        totalrowSelected = 0 ;
                        //courtName = "" ;
                        for (int j = 0 ; j < selectionStatus[i].length ;j ++)
                        {

                            if (selectionStatus[i][j])
                            {
                                totalrowSelected++ ;

                                totalSelectedCourt++;


                                courtName = mainList.get(j).get(i).getCourt_name() ;

                                if (Validation.isStringNullOrBlank(startDate))
                                {
                                    startDate = mainList.get(j).get(i).getCourt_start_time() ;
                                }
                                else
                                {
                                    String myLocalStartDate = mainList.get(j).get(i).getCourt_start_time() ;


                                    long SoltDiffrence = 0 , reservSoltDiffrence = 0 ;
                                    try {
                                        SoltDiffrence =  timeFormate.parse(myLocalStartDate).getTime() - timeFormate.parse(startDate).getTime() ;


                                        if (Validation.isStringNullOrBlank(reserveStartDate) == false)
                                        {
                                            reservSoltDiffrence =  timeFormate.parse(myLocalStartDate).getTime() - timeFormate.parse(reserveStartDate).getTime() ;

                                        }





                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }


                                    if (SoltDiffrence == 0)
                                    {
                                        isMemberInSameSlot = true ;
                                    }

                                    if (SoltDiffrence == standredSoltDiffrence)
                                    {
                                        startDate = new String(myLocalStartDate);
                                    }
                                    else
                                    {
                                        if (reservSoltDiffrence == standredSoltDiffrence && courtName.equals(reserveCourtName))
                                        {
                                            reserveStartDate = new String(myLocalStartDate);

                                        }
                                        else
                                        {
                                            isbulkbook = true ;
                                            break;
                                        }


                                    }


                                }







                            }

                            else
                            {
                                CourtData courtData = copyMainList.get(i).get(j) ;


                                if ( Validation.isStringNullOrBlank(courtData.getCourt_reservation_id()) == false && Validation.isStringNullOrBlank(courtName) == false)
                                {
                                    Log.e("reserve Court name" , courtData.getCourt_name()+"  "+courtData.getCourt_start_time()+" "+courtName);


                                    if (courtName.equals(courtData.getCourt_name()))
                                    {
                                        String myLocalStartDate = courtData.getCourt_start_time() ;


                                        long SoltDiffrence = 0;
                                        try {
                                            if (Validation.isStringNullOrBlank(reserveStartDate))
                                            {
                                                SoltDiffrence =  timeFormate.parse(myLocalStartDate).getTime() - timeFormate.parse(startDate).getTime() ;

                                            }
                                            else
                                            {
                                                SoltDiffrence =  timeFormate.parse(myLocalStartDate).getTime() - timeFormate.parse(reserveStartDate).getTime() ;

                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        if (SoltDiffrence == standredSoltDiffrence)
                                        {
                                            reserveStartDate = new String(myLocalStartDate);
                                            reserveCourtName = courtData.getCourt_name();
                                        }
                                    }






                                }

                            }




                        }
                        if (totalrowSelected >1 || totalSelectedCourt >max_book)
                        {
                            isbulkbook = true ;
                        }

                    }



                    if (SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_DIRECTOR) == true || SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MOBILE_ADMIN) == true)
                    {



                        if (isbulkbook)
                            bookMultipleCourtByDirector();
                        else
                            singelCourtBookingByDirector();






                    }
                    else
                    {

                        if ( isMemberInSameSlot)
                        {


                            Utill.showDialg("You cannot reserve the same time slot for more than one "+sessionManager.getSportFiledName(getActivity()).toLowerCase() , getActivity());

                        }
                        else
                        {
                            if (isbulkbook)
                            {

                                if (totalSelectedCourt >max_book)
                                {

                                    double hourInDouble = standredSoltDiffrence*max_book/1000/60/60 ;

                                    int hour = (int) (standredSoltDiffrence*max_book/1000/60/60);
                                    long minut = standredSoltDiffrence*max_book/1000/60 %60 ;

                                    String timeText ;

                                    if (hour != 0)
                                    {
                                        if (minut != 0)
                                        {
                                            timeText = hour+":"+minut ;
                                        }
                                        else
                                        {
                                            timeText = hour+"" ;

                                        }
                                    }
                                    else
                                    {
                                        timeText = "00:"+minut ;
                                    }



                                    if (hour>1)
                                    {
                                        String msg ="You can only book "+timeText+" hours for a reservation";
                                        Utill.showDialg(msg ,getActivity());

                                    }
                                    else
                                    {
                                        String msg ="You can only book "+timeText+" hour for a reservation";
                                        Utill.showDialg(msg ,getActivity());

                                    }



                                }
                                else

                                    Utill.showDialg("You cannot have time gap in your reservation slot" , getActivity());
                            }
                            else
                            {



                                sendReservationNew(SessionManager.getUser_id(mContext), "", "");

                            }
                        }



                    }
                    break;
                case R.id.recursive_book:


                    if (memeberPopUp != null) {
                        try {
                            memeberPopUp.cancel();
                        } catch (Exception e) {

                        }
                    }
                    if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_DIRECTOR) == true || SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MOBILE_ADMIN) == true) {

                        membersList = new ArrayList<>();
                        filteredList = membersList;
                        showRecursiveView();
                    }
                    break;
                default:
                    break;
            }

        }
    };
    private TextView currentMonth;
    private Button selectedDayMonthYearButton;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private GridView calendarView;
    private GridCellAdapter adapter;
    private Calendar _calendar;
    private int month, year;
    private int DATE_TYPE = 0;
    private int status;

    ProgressBar  loaderProgrssBar ;

    public static Fragment getInstance(FragmentManager mFragManager) {
        if (mFragment == null) {
            mFragment = new ShowReservationFragment();
        }
        return mFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.e(Tag, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(Tag, "onCreate");
    }

    /*
        Tested By Android Developer
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(Tag, "onCreateView");

        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.show_reservation, container, false);
        totalSelectedCourtNoList = new ArrayList<>();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        selected_calender = Calendar.getInstance();
        sqlListe = new SqlListe(getActivity());
        selected_calender.set(Calendar.HOUR, 0);
        selected_calender.set(Calendar.HOUR_OF_DAY, 0);
        selected_calender.set(Calendar.MINUTE, 0);
        selected_calender.set(Calendar.SECOND, 0);
        selected_calender.set(Calendar.MILLISECOND, 0);
        selected_calender.set(Calendar.AM_PM, Calendar.AM);
        lastCallItemCount = 10 ;
        counter = 10 ;
        aqQuery = new AQuery(getActivity());
        isScrolled = false ;
        loaderProgrssBar = (ProgressBar) rootView.findViewById(R.id.loader_progrss_bar);
// = Calendar.getInstance();
        loaderProgrssBar.setVisibility(View.GONE);

        sessionManager = new SessionManager(getActivity());


        mainLayout = (LinearLayout) rootView.findViewById(R.id.main_view);
        datePicker = (TextView) rootView.findViewById(R.id.date);



        // datePicker.setOnTouchListener(this);
        add_reservation_btn = (ImageButton) rootView.findViewById(R.id.add_reservation_btn);


        bottoms = (RelativeLayout) rootView.findViewById(R.id.bottoms);

        right_layout = (RelativeLayout) rootView.findViewById(R.id.right_layout);
        left_layout = (LinearLayout) rootView.findViewById(R.id.left_layout);

        my_reservation_list_view = (ListView) rootView.findViewById(R.id.my_reservation_list_view);


        my_reservation_list_view.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {

                MyreservationDeatilFragment fragment = new MyreservationDeatilFragment();

                fragment.inslizedView(myReservationList.get(pos) , datepickerFromMyReservationListener);

                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_frame, fragment, "fragment").addToBackStack(null).commit();

            }
        });






        all_reservation_linear_layout = (LinearLayout) rootView.findViewById(R.id.all_reservation_linear_layout);
        my_reservation_linear_layout = (LinearLayout) rootView.findViewById(R.id.my_reservation_linear_layout);

        all_reservation_view = (View) rootView.findViewById(R.id.all_reservation_view);
        my_reservation_view = (View) rootView.findViewById(R.id.my_reservation_view);
        left_layout.setVisibility(View.GONE);
        right_layout.setVisibility(View.VISIBLE);


        if (myReservationList != null) {
            if (myReservationList.size() == 0) {

            }
        }
        myReservationList.clear();
        setReservation();
        add_reservation_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                left_layout.setVisibility(View.VISIBLE);
                right_layout.setVisibility(View.GONE);
                getReservation(calendarDate);

            }
        });


        bottomLayout = (LinearLayout) rootView.findViewById(R.id.bottom_button);
        cancelBottomBtn = (TextView) rootView.findViewById(R.id.cancelbottom);
        bookBtn = (TextView) rootView.findViewById(R.id.okbottom);
        recursiveBookBtn = (TextView) rootView.findViewById(R.id.recursive_book);

        //userDateLL = (LinearLayout) rootView.findViewById(R.id.select_all_linear);
		/*	previousDate = (ImageView) rootView.findViewById(R.id.previous);
		NextDate = (ImageView) rootView.findViewById(R.id.next);
		 */    //memberDate = (TextView) rootView.findViewById(R.id.member_date);
        oldDateMSG = (TextView) rootView.findViewById(R.id.oldDate);

		/*NextDate.setOnClickListener(nextPreviousDateClickListener);
		previousDate.setOnClickListener(nextPreviousDateClickListener);*/


        mContext = getActivity();
        if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_DIRECTOR) == true || SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MOBILE_ADMIN) == true) {
            datePicker.setVisibility(View.VISIBLE);
            //userDateLL.setVisibility(View.GONE);
            recursiveBookBtn.setVisibility(View.VISIBLE);
        } else {
            //userDateLL.setVisibility(View.GONE);
            //datePicker.setVisibility(View.GONE);
            //userDateLL.setVisibility(View.VISIBLE);
            recursiveBookBtn.setVisibility(View.GONE);
        }

        cancelBottomBtn.setOnClickListener(clickOnBottomListener);
        bookBtn.setOnClickListener(clickOnBottomListener);
        recursiveBookBtn.setOnClickListener(clickOnBottomListener);


        if (DirectorFragmentManageActivity.actionbar_titletext != null) {
            DirectorFragmentManageActivity.updateTitle(sessionManager.getSportFiledName(getActivity()) + " Reservation");
        }

        if (DirectorFragmentManageActivity.backButton != null) {
            DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
            DirectorFragmentManageActivity.showBackButton();
        }
        datePicker.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // hideClickInfo();
                resetReservationChart();
                showCalendar();
                // showDatePopup(0);
            }
        });
        datePicker.setText(Utill.formattedDateFromString("", "", Utill.getCurrentDate()));
        String da = Utill.reverseDate(Utill.getCurrentDate());
        calendarDate = da;
        //memberDate.setText(Utill.formattedDateFromString("", "", Utill.getCurrentDate()));


        // new TimePickerDialog(getActivity(), timeListener, hour, minutes,
        // false);




        my_reservation_list_view.setOnScrollListener(new AbsListView.OnScrollListener() {





            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                isScrolled = true ;


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;

                Log.e("visibleItemhhhh" ,lastItem+" "+(myReservationList.size()-1));

                if (isScrolled &&lastItem == myReservationList.size() && isWebServiceCalling == false && lastCallItemCount != 0)
                {
                    //
                    getNextReservationList();
                }


            }
        });




        DirectorFragmentManageActivity.showLogoutButton();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(Tag, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(Tag, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(Tag, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(Tag, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(Tag, "onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(Tag, "onDestroyView");
        selectedCourtReservationId = "";
        selectedReservationIndex = "";
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        DirectorFragmentManageActivity.isbackPress = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(Tag, "onDetach");
    }

/*
    Tested By Rohit Agrawal
*/

    String club_todayView = "";

    private void getReservation(String date) {

        delete = false ;
        mainLayout.setVisibility(View.INVISIBLE);
        if (Utill.isNetworkAvailable(getActivity())) {
            Utill.showProgress(mContext);
            GlobalValues.getModelManagerObj(mContext).getReservation(date, SessionManager.getUser_Club_id(mContext), new ModelManagerListener() {
                @Override
                public void onSuccess(String json) {

                    datePicker.setText(Utill.formattedDateFromString("", "", Utill.getDateDDMMYYYY(selected_calender)));
                    mainList = JsonUtility.parserReservation(json);

                    try {
                        Log.e("json", json + "");
                        JSONObject jsonObject = new JSONObject(json);
                        advacsedbookingdays = jsonObject.getInt("adv_book");
                        max_cal = (Calendar) currentTimeCal.clone();
                        ;
                        max_cal.add(Calendar.DATE, advacsedbookingdays);


                        club_todayView = jsonObject.getString("club_todayView");



                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");


                     Calendar soltsTime =   Calendar.getInstance(Locale.ENGLISH);
                        soltsTime.setTime(simpleDateFormat.parse(mainList.get(0).get(0).getSlots()));

                        max_cal.set(Calendar.HOUR_OF_DAY , soltsTime.get(Calendar.HOUR_OF_DAY));

                        max_cal.set(Calendar.MINUTE , soltsTime.get(Calendar.MINUTE));


                    } catch (Exception e) {

                    }

//
                    totalSelectedCourtNoList.clear();

                    for (int i = 0; i < mainList.size(); i++) {
                        totalSelectedCourtNoList.add(0);
                    }

                    mainLayout.setVisibility(View.VISIBLE);

                    sv = new ScrollView(mContext);
                    TableLayout tableLayout = createTableLayout();
                    hsv = new HorizontalScrollView(mContext);
                    hsv.addView(tableLayout);
                    sv.addView(hsv);
                    mainLayout.removeAllViews();
                    mainLayout.addView(sv);
                    mainLayout.setVisibility(View.VISIBLE);



                    if(Validation.isStringNullOrBlank(selectedReservationIndex) == false)
                    {
                        View view = new View(getActivity());
                        view.setTag(selectedReservationIndex);

                        onclickListener(view);


                        if (Validation.isStringNullOrBlank(selectedReservationIndex) == false)
                        {
                            hsv.post(new Runnable() {
                                @Override
                                public void run() {
                                    // Get the button.

                                    // Locate the button.

                                    // Scroll to the button.

                                    String index[] =     selectedReservationIndex.split("-");

                                    hsv.scrollTo(Integer.parseInt(index[0])*slotWidth, 0);
                                    sv.scrollTo(0 , Integer.parseInt(index[1])*slotHeight);
                                    selectedCourtReservationId = "";
                                    selectedReservationIndex = "";

                                }
                            });
                        }





                    }



                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            Utill.hideProgress();
                        }
                    }, 200);


                }

                @Override
                public void onError(String msg) {
                    ShowUserMessage.showUserMessage(getActivity(), "" + msg);
                    Utill.hideProgress();
                }
            });

        } else {
            Utill.showNetworkError(mContext);
        }


    }
    String uniqueId = "" ;
    boolean mmmm ;




    /*
        Tested by Android developer
    */
    private TableLayout createTableLayout() {




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


                String slotValue ="";






                if (!Utill.isStringNullOrBlank(mainList.get(j).get(l).getCourt_reservation_purpuse()))
                {
                    String purpos = mainList.get(j).get(l).getCourt_reservation_purpuse();
                    if (purpos.length() > 15) {
                        purpos = purpos.subSequence(0, 15) + "..";
                    }
                    slotValue = mainList.get(j).get(l).getCourt_name() + "\n" + mainList.get(j).get(l).getSlots() + "\n" + purpos;

                }
                else
                {
                    if (Validation.isStringNullOrBlank(mainList.get(j).get(l).getUser_name()) == false)
                    {
                        String userName = mainList.get(j).get(l).getUser_name();
                        if (userName.length() > 15) {
                            userName = userName.subSequence(0, 15) + "..";
                        }
                        slotValue =  mainList.get(j).get(l).getCourt_name() + "\n" + mainList.get(j).get(l).getSlots() + "\n" + userName;
                        Log.e("slotValue", slotValue);

                    }
                    else
                    {

                        // RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) textView.getLayoutParams();
                        //  layoutParams.setMargins(0 , getResources().getDimensionPixelSize(R.dimen.margin_5),0,0);
                        // textView.setPadding(0,25,0,0);
                        //textView.setLayoutParams(layoutParams);
                        slotValue = mainList.get(j).get(l).getCourt_name() + "\n" + mainList.get(j).get(l).getSlots()+"\n" ;

                    }
                }





                textView.setText(slotValue);
                textView.setTag(j + "-" + l);
                textView.setGravity(Gravity.CENTER);
                crossImage.setTag(j + "-" + l);


                Drawable background = textView.getBackground();
                //importand
                CourtData bean = mainList.get(j).get(l);

                //


                Calendar reservationSlotTime = (Calendar) selected_calender.clone();

                //
                try {
                    reservationSlotTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(bean.getCourt_end_time().split(":")[0]));
                    reservationSlotTime.set(Calendar.MINUTE, Integer.parseInt(bean.getCourt_end_time().split(":")[1]));
                    reservationSlotTime.set(Calendar.SECOND, 00);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
                // Log.e("date ", currentTimeCal.getTime() + "   " + reservationSlotTime.getTime() + " " + currentTimeCal.compareTo(reservationSlotTime));
                boolean isCurrentDate = Utill.isEqualDate(currentTimeCal , reservationSlotTime);

                if ((isCurrentDate && club_todayView.equals("2")  && SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER)) ||currentTimeCal.getTime().compareTo(reservationSlotTime.getTime()) > 0) {
                    textView.setEnabled(false);
                    relative.setEnabled(false);
                    bean.setCourtSelectable(false);
                } else {
                    textView.setEnabled(true);
                    relative.setEnabled(true);
                    bean.setCourtSelectable(true);
                }
                textView.setTextColor(mContext.getResources().getColor(R.color.white_color));

                //	textView.setBackgroundColor(mContext.getResources().getColor(R.color.free_slot_color));
                if (Utill.isStringNullOrBlank(bean.getCourt_reservation_id())) {


                    textView.setTextSize(15);

                    if ((isCurrentDate && club_todayView.equals("2") && SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER))  ||currentTimeCal.getTime().compareTo(reservationSlotTime.getTime()) > 0) {
                        textView.setBackgroundColor(mContext.getResources().getColor(R.color.gray_color));
                    } else {
                        textView.setBackgroundColor(mContext.getResources().getColor(R.color.free_slot_color));
                    }


                    //textView.setGravity(Gravity.CENTER);

                } else {

                    if (!Utill.isStringNullOrBlank(bean.getCourt_reservation_bulkbooking_id())
                            && !bean.getCourt_reservation_bulkbooking_id().equalsIgnoreCase("0")) {
                        textView.setTextSize(15);
                        textView.setBackgroundColor(mContext.getResources().getColor(R.color.bulk_booked_slot_color));
                        textView.setTextColor(mContext.getResources().getColor(R.color.black_color));
                    } else if (!Utill.isStringNullOrBlank(bean.getCourt_reservation_user_id()) /*&& */) {
                        //relative.setBackgroundColor(mContext.getResources().getColor(R.color.my_booked_slot_color));
                        textView.setTextSize(15);


                        if (Validation.isStringNullOrBlank(bean.getCat_color())) {
                            if (!bean.getCourt_reservation_user_id().equalsIgnoreCase(SessionManager.getUser_id(mContext))) {
                                textView.setBackgroundColor(mContext.getResources().getColor(R.color.booked_slot_color));
                                textView.setTextSize(15);
                            } else {
                                textView.setBackgroundColor(mContext.getResources().getColor(R.color.my_booked_slot_color));
                                textView.setTextSize(15);
                            }


                        } else {
                            try {
                                textView.setBackgroundColor(Color.parseColor(bean.getCat_color()));
                                textView.setTextSize(15);
                            } catch (Exception e) {

                            }
                        }


                    } else {
                        textView.setBackgroundColor(mContext.getResources().getColor(R.color.booked_slot_color));
                        textView.setTextSize(15);
                    }
                }


                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {




                        onclickListener(view);






						/*if (longPressed)
						{
							sv.setOnTouchListener(touchListener);
							hsv.setOnTouchListener(touchListener);
						}
*/


                    }
                });

                if (Validation.isStringNullOrBlank(bean.getCourt_reservation_id()) == false &&
                        (selectedCourtReservationId.equals(bean.getMemberbookedid()) && Validation.isStringNullOrBlank(bean.getMemberbookedid()) == false)
                        || selectedCourtReservationId.equals(bean.getRecursive_id())&& Validation.isStringNullOrBlank(bean.getRecursive_id()) == false )
                {

                    try
                    {
                        if (Validation.isStringNullOrBlank(selectedReservationIndex))
                        {
                            selectedReservationIndex= j + "-" + l ;

                        }

                    }
                    catch (Exception e)
                    {

                    }


                    //
                }
// Main point


                textView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {


                        firsttimelongPressed = true;

                        priviousX = -1;
                        priviousY = -1;

                        Log.e("Long Click", "Call");

                        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                        String max_cal_format = sdf1.format(max_cal.getTime());
                        String slectedDate = sdf1.format(selected_calender.getTime());




                        boolean bookingDialog = false ;

                        if (Utill.isEqualDate(max_cal ,selected_calender))
                        {
                            if (Utill.compareTwoTime(max_cal , currentTimeCal))
                            {
                                bookingDialog =true ;
                            }
                        }




                        try {
                            if (bookingDialog || sdf1.parse(slectedDate).after(sdf1.parse(max_cal_format)) && SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER))
                            {
                                if (advacsedbookingdays == 1)
                                {
                                    // Utill.showDialg("You are allowed to book " + CourtName.toLowerCase() + " only " + advacsedbookingdays + " day in advance ", getActivity());

                                }
                                else
                                {
                                    // Utill.showDialg("You are allowed to book " + CourtName.toLowerCase() + " only " + advacsedbookingdays + " days in advance ", getActivity());

                                }


                                Calendar selected_calender_clone = (Calendar) selected_calender.clone();

                                selected_calender_clone.add(Calendar.DATE , -advacsedbookingdays);

                                Utill.showDialg("You can book this court starting from  " +sdf1.format(selected_calender_clone.getTime()) +" "+Utill.formattedDateFromString("HH:mm" , "hh:mm a" ,mainList.get(0).get(0).getCourt_start_time()), getActivity());


                                return true;
                            }
                        } catch (Exception e) {

                        }

                        //firsttimelongPressed

                        String index[] = v.getTag().toString().split("-");
                        CourtData bean = mainList.get(Integer.parseInt(index[0])).get(Integer.parseInt(index[1]));


                        if (delete == true && Validation.isStringNullOrBlank(bean.getCourt_reservation_id()) )
                        {
                            delete = false ;

                            hidedeleteReservationChartAll(databean , true);

                            return true;
                        }


                        selectedLongPressedItem = Integer.parseInt(index[0]);

                        selectedLongPressedItemX = Integer.parseInt(index[1]);


                        Log.e("getCourt_reservation_id" , bean.getCourt_reservation_id());


                        if (delete && Validation.isStringNullOrBlank(deleteID) )
                        {
                        }
                        // deleteReservationChart(deleteCourtData);


                        if (Validation.isStringNullOrBlank(bean.getMemberbookedid()) == false)
                        {
                            uniqueId  = bean.getMemberbookedid() ;
                        }
                        else
                        {
                            uniqueId = bean.getRecursive_id() ;
                        }


                        if ((delete && Validation.isStringNullOrBlank(uniqueId) == false) || Validation.isStringNullOrBlank(bean.getCourt_reservation_id()) == true)
                        {
                            selectionStatus[Integer.parseInt(index[1])][Integer.parseInt(index[0])] = false;



                            setDataInTable(Integer.parseInt(index[1]), Integer.parseInt(index[0]));
                            checkSlotSelection();
                        }


                        sv.setOnTouchListener(touchListener);
                        hsv.setOnTouchListener(touchListener);


                        return true;
                    }
                });


                crossImage.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (deleteCourtData != null && !Utill.isStringNullOrBlank(deleteCourtData.getRecursive_id())) {
                            showRecursiveSelector(deleteCourtData);
                        } else {
                            String index[] = v.getTag().toString().split("-");
                            CourtData databean1 = mainList.get(Integer.parseInt(index[0])).get(Integer.parseInt(index[1]));

                            showDeleteConfirmationDialogu(deleteCourtData, databean1);

                        }
                    }
                });

                // relative.addView(textView, 0);
                // relative.addView(crossImage,1);
                tableRow.addView(relative, tableRowParams);
            }


            //tableRow.setBackgroundColor(mContext.getResources().getColor(R.color.black_color));


            tableLayout.addView(tableRow, tableLayoutParams);
            l++;
        }
        //Utill.hideProgress();
        return tableLayout;
    }
    String renaingingCourtReservationId = "" ;


    /*
    Tested by Android developer
*/

    Calendar firstdeleteIndexStartTime ,firstdeleteIndexEndTime;

    boolean isfirstdeleteIndex;

    void showDeleteConfirmationDialogu(CourtData deleteCourtData,final CourtData courtData) {
        renaingingCourtReservationId = "" ;

        firstdeleteIndexStartTime = Calendar.getInstance(Locale.ENGLISH);

        firstdeleteIndexEndTime = Calendar.getInstance(Locale.ENGLISH);


        isfirstdeleteIndex = false ;

        firstdeleteIndexStartTime = (Calendar) selected_calender.clone();

        final Dialog alertDialog = new Dialog(mContext);
        //LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View v = inflater.inflate(R.layout.delete_confirmation, null);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.delete_confirmation);
        TextView doneTV, cancelTV, headerTV;
        final CheckBox noshowCheck = (CheckBox) alertDialog.findViewById(R.id.noshow);



        doneTV = (TextView) alertDialog.findViewById(R.id.done);
        cancelTV = (TextView) alertDialog.findViewById(R.id.cancel);
        headerTV = (TextView) alertDialog.findViewById(R.id.title);








        for (int i = 0; i < mainList.size(); i++)
        {
            for (int j = 0; j < mainList.get(i).size(); j++)
            {


                if(mainList.get(i).get(j).isCourtSelectable() && courtData.getMemberbookedid().equals(mainList.get(i).get(j).getMemberbookedid()))
                {

                    SimpleDateFormat simpleDateFormsat = new SimpleDateFormat("HH:mm");




                    try {
                        Calendar temCal = Calendar.getInstance(Locale.ENGLISH);
                        temCal.setTime(simpleDateFormsat.parse(mainList.get(i).get(j).getCourt_start_time()));

                        if (isfirstdeleteIndex == false || Utill.compareTwoHourAndMonute(firstdeleteIndexStartTime ,temCal )) {

                            firstdeleteIndexStartTime.setTime(simpleDateFormsat.parse(mainList.get(i).get(j).getCourt_start_time()));

                            firstdeleteIndexEndTime.setTime(simpleDateFormsat.parse(mainList.get(i).get(j).getCourt_end_time()));
                            isfirstdeleteIndex = true;
                            ;
                        }
                    }
                    catch (Exception e)
                    {
                    }
                }


                if (mainList.get(i).get(j).isItemRemoved() == false)
                {
                       /* Log.e("remaining court" , mainList.get(i).get(j)

                                .getCourt_name()
                                +" "+mainList.get(i).get(j).getCourt_start_time());
*/




                    if (Validation.isStringNullOrBlank(renaingingCourtReservationId))
                    {
                        renaingingCourtReservationId = mainList.get(i).get(j).getCourt_reservation_id();
                    }
                    else
                    {
                        renaingingCourtReservationId =renaingingCourtReservationId+","+ mainList.get(i).get(j).getCourt_reservation_id();

                    }





                }

                if (deleteCourtData.getCourt_id().equalsIgnoreCase((mainList.get(i).get(j).getCourt_id())))
                {
                    if (deleteCourtData.getSlots().equalsIgnoreCase((mainList.get(i).get(j).getSlots())))
                    {

                        if (mainList.get(i).get(j).getCourt_reservation_user_id().equalsIgnoreCase(SessionManager.getUserId(mContext)))
                        {

                            noshowCheck.setChecked(false);
                            noshowCheck.setVisibility(View.GONE);
                        }
                    }
                }


            }

        }


        if (SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER))
        {
            noshowCheck.setChecked(false);
            noshowCheck.setVisibility(View.GONE);
        }
        else
        {


            if (courtData.getCourt_reservation_user_id().equalsIgnoreCase(SessionManager.getUser_id(mContext)))
            {
                noshowCheck.setChecked(false);
                noshowCheck.setVisibility(View.GONE);
            }
            else
            {
                if(Utill.isEqualDate(currentTimeCal,selected_calender)== false)
                {
                    noshowCheck.setChecked(false);

                    noshowCheck.setVisibility(View.GONE);

                }
                else
                {

                    firstdeleteIndexStartTime.set(Calendar.DATE ,currentTimeCal.get(Calendar.DATE) );
                    firstdeleteIndexStartTime.set(Calendar.MONTH ,currentTimeCal.get(Calendar.MONTH) );
                    firstdeleteIndexStartTime.set(Calendar.YEAR ,currentTimeCal.get(Calendar.YEAR) );
                    firstdeleteIndexStartTime.set(Calendar.SECOND ,00 );


                    firstdeleteIndexEndTime.set(Calendar.DATE ,currentTimeCal.get(Calendar.DATE) );
                    firstdeleteIndexEndTime.set(Calendar.MONTH ,currentTimeCal.get(Calendar.MONTH) );
                    firstdeleteIndexEndTime.set(Calendar.YEAR ,currentTimeCal.get(Calendar.YEAR) );
                    firstdeleteIndexEndTime.set(Calendar.SECOND ,00 );


                    if (Utill.compareTwoDateAndTime(  currentTimeCal,firstdeleteIndexStartTime)  && Utill.compareTwoDateAndTime(firstdeleteIndexEndTime,  currentTimeCal))
                    {
                        noshowCheck.setVisibility(View.VISIBLE);

                    }
                    else
                    {
                        noshowCheck.setVisibility(View.GONE);

                    }

                }

            }
        }


        doneTV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub



                deleteMultiple(noshowCheck.isChecked() , courtData.getCourt_reservation_user_id() ,  renaingingCourtReservationId );
                alertDialog.cancel();
            }
        });
        cancelTV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.cancel();

            }
        });
        headerTV.setText(SessionManager.getClubName(mContext));
        alertDialog.getWindow().setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.white_circle_back));     /*(Color.parseColor("#FFFFFFFF"))*/
        ;

        alertDialog.show();

    }

    void deleteMultiple(boolean noshow , String Court_reservation_user_id ,String slotJson) {


        if (Utill.isNetworkAvailable(getActivity())) {
            Utill.showProgress(mContext);
            Map<String, Object> params = new HashMap<String, Object>();
            String clubId = SessionManager.getUser_Club_id(mContext);
            String userid = SessionManager.getUser_id(mContext);
            params.put("memberbookid", deleteID);
            params.put("court_reservation_club_id", clubId);
            params.put("court_reservation_user_id", Court_reservation_user_id);
            params.put("user_id", userid);
            params.put("user_type" , SessionManager.getUser_type(getActivity()));
            params.put("slots", slotJson);

            Log.e("slotJson" ,slotJson+" blank");
            //
            String noShowStr = "";
            params.put("no_show", "");
            if (noshow)
            {
                params.put("no_show", "1");
            }
            else
            {
                params.put("no_show", "");
            }


            //Toast.makeText(getActivity() , "no_show "+noShowStr,1).show();

//

            Log.e("dada" , params.toString());
            GlobalValues.getModelManagerObj(mContext).CancelMultipleBooking(params, new ModelManagerListener() {
                @Override
                public void onSuccess(String json) {
                    Utill.hideProgress();
					/*getReservation(calendarDate);*/
                    //
                    try {

                        try {
                            Uri eventsUri = Uri.parse("content://com.android.calendar/" + "events");

                            ArrayList<Integer> eventIds = sqlListe.getEventIds(deleteID);


                            for (int i = 0; i < eventIds.size(); i++) {
                                int event_id = eventIds.get(i);
                                getActivity().getApplicationContext().getContentResolver().delete(ContentUris.withAppendedId(eventsUri, event_id), null, null);

                            }


                            sqlListe.deleteEvents(Integer.parseInt(deleteID));

                            //	getActivity().getApplicationContext().getContentResolver().delete(CalendarContract.Events.CONTENT_URI ,CalendarContract.Reminders.EVENT_ID +" = ?", new String[]{sqlListe.getEventId(court_reservation_id)+""});

                        } catch (Exception e) {
                            ShowUserMessage.showDialogOnActivity(getActivity(), e.getMessage());
                        }

                        //	sqlListe.deleteEvent(Integer.parseInt(court_reservation_id));
                        JSONObject jsonObj = new JSONObject(json);
                        showDialgfordialgforbooking(jsonObj.getString("message"), getActivity());

                        //Toast.makeText(mContext, jsonObj.getString("message"), 1).show();
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onError(String msg) {
                    ShowUserMessage.showUserMessage(getActivity(), "" + msg);
                    Utill.hideProgress();
                }
            });

        } else {
            Utill.showNetworkError(mContext);
        }

    }


    public void showCalendar() {
        selectedDateList = new HashSet<String>();

        calendarDialogue = new AlertDialog.Builder(mContext).create();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.my_calendar_view, null);
        initializeCalendar(v);
        calendarDialogue.setView(v);
        calendarDialogue.show();
    }

    void initializeCalendar(View v) {
        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);
        Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: " + year);

        selectedDayMonthYearButton = (Button) v.findViewById(R.id.selectedDayMonthYear);
        selectedDayMonthYearButton.setText("Selected: ");
        TextView cancelTV = (TextView) v.findViewById(R.id.cancel);
        cancelTV.setOnClickListener(new OnClickListener() {

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
        adapter = new GridCellAdapter(mContext, R.id.calendar_day_gridcell, month, year);
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

    private void setGridCellAdapterToDate(int month, int year) {
        adapter = new GridCellAdapter(mContext, R.id.calendar_day_gridcell, month, year);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(DateFormat.format(dateTemplate, _calendar.getTime()));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
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



    public void showRecursiveSelector(final CourtData courtBeab) {

        moveSlotBean = null;
        final Dialog alertDialog = new Dialog(getActivity());
        //LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.listview);

        //	final AlertDialog alertDialog = new AlertDialog.Builder(mContext,R.style.CustomDialog).create();

        ListView listView = (ListView) alertDialog.findViewById(R.id.list);

        TextView header = (TextView) alertDialog.findViewById(R.id.header);
        header.setText(SessionManager.getClubName(mContext));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.simpletextview, deleteRecursiveArray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    deleteRecursive(courtBeab, 1);
                } else if (position == 1) {
                    deleteRecursive(courtBeab, 0);
                }

                alertDialog.cancel();
                ;
            }
        });
        alertDialog.getWindow().setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.white_circle_back));     /*(Color.parseColor("#FFFFFFFF"))*/
        ;

        ///
        //alertDialog.getWindow().
        alertDialog.show();
    }


    void deleteRecursive(CourtData bean, int id) {
        // delete all
        Map<String, Object> params = new HashMap<String, Object>();
        if (id == 0) {

        }// delete for a perticular date
        else if (id == 1) {
            params.put("court_reservation_date", calendarDate);
        }
        params.put("user_type", SessionManager.getUser_type(mContext));
        params.put("court_reservation_recursiveid", bean.getRecursive_id());


        HashSet<String> selectedCourtIdHashSet = new HashSet<>();


        renaingingCourtReservationId = "" ;


        selectedCourtReservationId = "";


        JSONArray jArray = new JSONArray();
        JSONObject slotjson = new JSONObject();

        try {
            for (int i = 0; i < mainList.size(); i++) {
                for (int j = 0; j < mainList.get(i).size(); j++) {


                    if (mainList.get(i).get(j).isItemRemoved() == false) {
                        Log.e("remaining court", mainList.get(i).get(j).getCourt_name());

                        JSONObject myJson = new JSONObject();




                        if (Validation.isStringNullOrBlank(renaingingCourtReservationId))
                        {
                            renaingingCourtReservationId = mainList.get(i).get(j).getCourt_reservation_id();
                        }
                        else
                        {
                            renaingingCourtReservationId =renaingingCourtReservationId+","+ mainList.get(i).get(j).getCourt_reservation_id();

                        }



                        myJson.put("strart_time", mainList.get(i).get(j).getCourt_start_time());
                        myJson.put("end_time", mainList.get(i).get(j).getCourt_end_time());
                        myJson.put("court_reservation_court_id", mainList.get(i).get(j).getCourt_id());
                        myJson.put("court_name", mainList.get(i).get(j).getCourt_name());

                        jArray.put(myJson);


                    }

                    else
                    {
                        if (bean.getRecursive_id().equals(mainList.get(i).get(j).getRecursive_id()) &&   Validation.isStringNullOrBlank(mainList.get(i).get(j).getCourt_reservation_id()) == false)
                        {
                            String selectedCourtId = mainList.get(i).get(j).getCourt_id();

                            selectedCourtIdHashSet.add(selectedCourtId);


                        }
                    }


                }
                slotjson.put("time" ,jArray);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        params.put("slots", renaingingCourtReservationId);



        String selectedCourtId ="";


        Iterator itr = selectedCourtIdHashSet.iterator();
        while(itr.hasNext())
        {



            if (selectedCourtId.length() == 0)
            {
                selectedCourtId = itr.next()+"";
            }
            else
            {
                selectedCourtId =selectedCourtId+","+ itr.next();

            }


        }


        params.put("court_reservation_court_id", selectedCourtId);


        if (Utill.isNetworkAvailable(getActivity())) {
            Utill.showProgress(mContext);
            GlobalValues.getModelManagerObj(mContext).CancelRecursiveBooking(params, new ModelManagerListener() {
                @Override
                public void onSuccess(String json) {
                    Utill.hideProgress();
                    //getReservation(calendarDate);
                    try {
                        JSONObject jsonObj = new JSONObject(json);
                        showDialgfordialgforbooking(jsonObj.getString("message"), getActivity());

                        //Toast.makeText(mContext, jsonObj.getString("message"), 1).show();
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onError(String msg) {
                    ShowUserMessage.showUserMessage(getActivity(), "" + msg);
                    Utill.hideProgress();
                }
            });

        } else {
            Utill.showNetworkError(mContext);
        }
    }


    MemberListBean selectedDirectorOrAdmin ;

    void bookMultipleCourtByDirector()
    {
        selectedDirectorOrAdmin = new MemberListBean();

        recursiveBookingCourtColours.clear();
        httpRequest = new HttpRequest(getActivity());
        final String name = SessionManager.getFirstName(getActivity()) + " " + SessionManager.getLastName(getActivity());

        HashMap<String, Object> paras = new HashMap<String, Object>();
        paras.put("club_id", SessionManager.getUser_Club_id(getActivity()));
        httpRequest.getResponse(getActivity(), WebService.cat_list, paras, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.getBoolean("status") == true)
                    {
                        JSONArray cat_list_json_array = jsonObject.getJSONArray("cat_list");

                        for (int i = 0; i < cat_list_json_array.length(); i++) {
                            JSONObject cat_list_json_array_item = cat_list_json_array.getJSONObject(i);
                            RecursiveBookingCourtColour recursiveBookingCourtColour = new RecursiveBookingCourtColour();
                            recursiveBookingCourtColour.setCat_id(cat_list_json_array_item.getInt("cat_id"));
                            recursiveBookingCourtColour.setCat_name(cat_list_json_array_item.getString("cat_name"));
                            recursiveBookingCourtColour.setCat_color(cat_list_json_array_item.getString("cat_color"));

                            recursiveBookingCourtColours.add(recursiveBookingCourtColour);

                        }


                        memeberPopUp = new AlertDialog.Builder(mContext).create();
                        memeberPopUp.setCancelable(false);
                        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v = inflater.inflate(R.layout.director_court_booking_dialog, null);
                        TextView cancel = (TextView) v.findViewById(R.id.cancel);
                        final TextView ok = (TextView) v.findViewById(R.id.ok);
                        LinearLayout reserve_user_layout = (LinearLayout) v.findViewById(R.id.reserve_user_layout);
                        final EditText booking_reason = (EditText) v.findViewById(R.id.booking_reason);
                        final   InstantAutoComplete reservation_user_autocomple_tv = (InstantAutoComplete) v.findViewById(R.id.reservation_user_autocomple_tv);
                        TextView enter_member_name_lable_tv = (TextView) v.findViewById(R.id.enter_member_name_lable_tv);
                        final  TextView error_msg_tv = (TextView) v.findViewById(R.id.error_msg_tv);

                        reservation_user_autocomple_tv.setAdapter(new MemberAutoCompleteAdapter(mContext, R.id.textViewItem, membersList ,reservation_user_autocomple_tv));





                        reservation_user_autocomple_tv.setAdapter(new MemberAutoCompleteAdapter(mContext, R.id.textViewItem, membersList ,reservation_user_autocomple_tv));


                        reservation_user_autocomple_tv.addTextChangedListener(new TextWatcher() {

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                // TODO Auto-generated method stub


                            }

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                // TODO Auto-generated method stub

                                beforname = s.toString();
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                // TODO Auto-generated method stub


                                String bookingForStr = s.toString();
                                boolean isNameValid = false;

                                if (s.toString().length() != 0)
                                {
                                    if (s.toString().length() == 1 && beforname.length() == 0)
                                    {
                                        memberAutoCompleteAdapter = null ;
                                        selectedDirectorOrAdmin = new MemberListBean();

                                        getMembersList(s.toString(), reservation_user_autocomple_tv ,AppConstants.DIRECTORADMINLIST);
                                    }

                                } else {

                                }



                                boolean isMemberExits =false;
                                int totalMember = 0;

                                if (membersList != null && s.toString().length() != 0)
                                {
                                    for (int i = 0 ; i < membersList.size() ;i++)
                                    {
                                        String memberName =    membersList.get(i).getUser_first_name()+" "+membersList.get(i).getUser_last_name() ;

                                        if (memberName.equalsIgnoreCase(bookingForStr))
                                        {
                                            isMemberExits = true ;
                                            selectedDirectorOrAdmin = membersList.get(i);


                                        }

                                        if (membersList.get(i).getUser_first_name().toLowerCase().startsWith((bookingForStr.toLowerCase()))||membersList.get(i).getUser_last_name().toLowerCase().startsWith((bookingForStr.toLowerCase()))||memberName.toLowerCase().startsWith((bookingForStr.toLowerCase())))
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


                                        if (membersList != null)
                                        {
                                            membersList.clear();
                                        }
                                        if (memberAutoCompleteAdapter!= null)
                                        {
                                            try
                                            {
                                                //  memberAutoCompleteAdapter.notifyDataSetChanged();
                                            }
                                            catch (Exception e)
                                            {

                                            }

                                        }
                                    }


                                }




                                if (isMemberExits)
                                {
                                    ok.setAlpha(1.0f);

                                }
                                else
                                {
                                    ok.setAlpha(0.8f);

                                }
                                if (totalMember != 0 || Validation.isStringNullOrBlank(bookingForStr) || memberAutoCompleteAdapter == null ||(membersList == null) )
                                {
                                    error_msg_tv.setVisibility(View.GONE);
                                }
                                else
                                {
                                    if (membersList.size() == 0)
                                    {
                                        error_msg_tv.setVisibility(View.GONE);

                                    }
                                    else
                                    {

                                    }
                                    error_msg_tv.setVisibility(View.VISIBLE);


                                }


                                ok.setEnabled(isMemberExits);

                                if (name.equalsIgnoreCase(beforname) /*&& !(bookingForAuto.getText().toString() =="" ||bookingForAuto.getText().toString().equalsIgnoreCase("")*/) {
                                    reservation_user_autocomple_tv.setText("");
                                }
                                //
                            }
                        });



                        reservation_user_autocomple_tv.setOnKeyListener(new OnKeyListener() {

                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {
                                // TODO Auto-generated method stub
                                //Toast.makeText(getActivity(), "KeyCode "+keyCode, 1).show();

                                if (keyCode == 66) {
                                    InputMethodManager inputManager =
                                            (InputMethodManager) getActivity().
                                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputManager.hideSoftInputFromWindow(
                                            reservation_user_autocomple_tv.getWindowToken(),
                                            InputMethodManager.HIDE_NOT_ALWAYS);
                                }
                                return false;
                            }
                        });


                        //Toast.makeText(getActivity(), "name  "+name, 1).show();
                        //	Editable etext = bookingForAuto.getText();
                        //Selection.setSelection(etext, position);
                        reservation_user_autocomple_tv.setSelection(reservation_user_autocomple_tv.getText().length());


                        reservation_user_autocomple_tv.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                MemberListBean place = (MemberListBean) arg0.getItemAtPosition(arg2);
                                selectedDirectorOrAdmin = place;

                                Utill.hideKeybord(getActivity() , reservation_user_autocomple_tv);
                                reservation_user_autocomple_tv.setText(place.getUser_first_name() + " " + place.getUser_last_name());
                                reservation_user_autocomple_tv.setSelection(reservation_user_autocomple_tv.getText().length());
                            }
                        });































                        final Spinner slecet_catogory_spinner = (Spinner) v.findViewById(R.id.slecet_catogory_spinner);
                        slecet_catogory_spinner.setAdapter(new SelectCategoryListAdapter(getActivity(), recursiveBookingCourtColours));


                        cancel.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                memeberPopUp.cancel();
                            }
                        });
                        ok.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                String bookingName = booking_reason.getText().toString();
                                String bookingForStr = "";

                                InputMethodManager inputManager =
                                        (InputMethodManager) getActivity().
                                                getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputManager.hideSoftInputFromWindow(
                                        booking_reason.getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);


                                if (Utill.isNetworkAvailable(getActivity()))
                                {



                                    if (slecet_catogory_spinner.getSelectedItemPosition() == 0)
                                    {
                                        Utill.showDialg("Please select category.", getActivity());

                                        return;
                                    }


                                    String reservationUserId = "";

                                    if (Validation.isStringNullOrBlank(selectedDirectorOrAdmin.getUser_id()))
                                    {
                                        reservationUserId =SessionManager.getUser_id(getActivity());
                                    }
                                    else
                                    {
                                        reservationUserId = selectedDirectorOrAdmin.getUser_id() ;
                                    }


                                    sendReservationNew(reservationUserId, bookingName, recursiveBookingCourtColours.get(slecet_catogory_spinner.getSelectedItemPosition()).getCat_id() + "");
                                } else {
                                    Utill.showNetworkError(mContext);
                                }


                            }
                        });
                        //memeberPopUp.	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                        memeberPopUp.setView(v);
                        memeberPopUp.show();


                    }

                } catch (Exception e) {

                }

            }
        });

        //

    }

    void showRecursiveView() {


//

        selectedDirectorOrAdmin = new MemberListBean();
        recursiveBookingCourtColours.clear();
        httpRequest = new HttpRequest(getActivity());

        HashMap<String, Object> paras = new HashMap<String, Object>();
        paras.put("club_id", SessionManager.getUser_Club_id(getActivity()));
        httpRequest.getResponse(getActivity(), WebService.cat_list, paras, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if (jsonObject.getBoolean("status") == true) {
                        JSONArray cat_list_json_array = jsonObject.getJSONArray("cat_list");

                        for (int i = 0; i < cat_list_json_array.length(); i++) {
                            JSONObject cat_list_json_array_item = cat_list_json_array.getJSONObject(i);
                            RecursiveBookingCourtColour recursiveBookingCourtColour = new RecursiveBookingCourtColour();
                            recursiveBookingCourtColour.setCat_id(cat_list_json_array_item.getInt("cat_id"));
                            recursiveBookingCourtColour.setCat_name(cat_list_json_array_item.getString("cat_name"));
                            recursiveBookingCourtColour.setCat_color(cat_list_json_array_item.getString("cat_color"));

                            recursiveBookingCourtColours.add(recursiveBookingCourtColour);

                        }


                        for (int i = 0; i < 7; i++) {
                            days.add(i + 1 + "");
                        }
                        memberBean = new MemberListBean();
                        memeberPopUp = new AlertDialog.Builder(mContext).create();
                        final String name = SessionManager.getFirstName(getActivity()) + " " + SessionManager.getLastName(getActivity());

                        memeberPopUp.setCancelable(false);
                        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v = inflater.inflate(R.layout.recurrent_popup, null);
                        final EditText booking_reason = (EditText) v.findViewById(R.id.booking_reason);


                        //LinearLayout	booking_name_layouit =(LinearLayout) v.findViewById(R.id.booking_name_layouit);
                        //booking_name_layouit.setVisibility(View.GONE);
                        TextView cancel = (TextView) v.findViewById(R.id.cancel);
                        final  TextView ok = (TextView) v.findViewById(R.id.ok);

                        //final InstantAutoComplete bookingForAuto = (InstantAutoComplete) v.findViewById(R.id.booking_for);
                        //bookingForAuto.setText(SessionManager.getFirstName(mContext)+" "+SessionManager.getLastName(mContext));
                        final Spinner durationSpinner = (Spinner) v.findViewById(R.id.spinner);


                        final  InstantAutoComplete reservation_user_autocomple_tv = (InstantAutoComplete) v.findViewById(R.id.member_auto_completetv);

                        final TextView error_msg_tv = (TextView) v.findViewById(R.id.error_msg_tv);




                        reservation_user_autocomple_tv.setAdapter(new MemberAutoCompleteAdapter(mContext, R.id.textViewItem, membersList ,reservation_user_autocomple_tv));


                        reservation_user_autocomple_tv.setOnKeyListener(new OnKeyListener() {

                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {
                                // TODO Auto-generated method stub
                                //Toast.makeText(getActivity(), "KeyCode "+keyCode, 1).show();

                                if (keyCode == 66) {
                                    InputMethodManager inputManager =
                                            (InputMethodManager) getActivity().
                                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputManager.hideSoftInputFromWindow(
                                            reservation_user_autocomple_tv.getWindowToken(),
                                            InputMethodManager.HIDE_NOT_ALWAYS);
                                }
                                return false;
                            }
                        });


                        reservation_user_autocomple_tv.setAdapter(new MemberAutoCompleteAdapter(mContext, R.id.textViewItem, membersList ,reservation_user_autocomple_tv));


                        reservation_user_autocomple_tv.addTextChangedListener(new TextWatcher() {

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                // TODO Auto-generated method stub


                            }

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                // TODO Auto-generated method stub

                                beforname = s.toString();
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                // TODO Auto-generated method stub


                                String bookingForStr = s.toString();
                                boolean isNameValid = false;

                                if (s.toString().length() != 0)
                                {
                                    if (s.toString().length() == 1 && beforname.length() == 0)
                                    {
                                        memberAutoCompleteAdapter = null ;
                                        selectedDirectorOrAdmin = new MemberListBean();


                                        getMembersList(s.toString(), reservation_user_autocomple_tv ,AppConstants.DIRECTORADMINLIST);
                                    }

                                } else {

                                }



                                boolean isMemberExits =false;
                                int totalMember = 0;

                                if (membersList != null && s.toString().length() != 0)
                                {
                                    for (int i = 0 ; i < membersList.size() ;i++)
                                    {
                                        String memberName =    membersList.get(i).getUser_first_name()+" "+membersList.get(i).getUser_last_name() ;

                                        if (memberName.equalsIgnoreCase(bookingForStr))
                                        {
                                            isMemberExits = true ;
                                            selectedDirectorOrAdmin = membersList.get(i);


                                        }

                                        if (membersList.get(i).getUser_first_name().toLowerCase().startsWith((bookingForStr.toLowerCase()))||membersList.get(i).getUser_last_name().toLowerCase().startsWith((bookingForStr.toLowerCase()))||memberName.toLowerCase().startsWith((bookingForStr.toLowerCase())))
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


                                        if (membersList != null)
                                        {
                                            membersList.clear();
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
                                    ok.setAlpha(1.0f);

                                }
                                else
                                {
                                    ok.setAlpha(0.8f);

                                }
                                if (totalMember != 0 || Validation.isStringNullOrBlank(bookingForStr) || memberAutoCompleteAdapter == null ||(membersList == null) )
                                {
                                    error_msg_tv.setVisibility(View.GONE);
                                }
                                else
                                {
                                    if (membersList.size() == 0)
                                    {
                                        error_msg_tv.setVisibility(View.GONE);

                                    }
                                    else
                                    {

                                    }
                                    error_msg_tv.setVisibility(View.VISIBLE);


                                }


                                ok.setEnabled(isMemberExits);

                                if (name.equalsIgnoreCase(beforname) /*&& !(bookingForAuto.getText().toString() =="" ||bookingForAuto.getText().toString().equalsIgnoreCase("")*/) {
                                    reservation_user_autocomple_tv.setText("");
                                }
                                //
                            }
                        });

                        //Toast.makeText(getActivity(), "name  "+name, 1).show();
                        //	Editable etext = bookingForAuto.getText();
                        //Selection.setSelection(etext, position);
                        reservation_user_autocomple_tv.setSelection(reservation_user_autocomple_tv.getText().length());


                        reservation_user_autocomple_tv.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                MemberListBean place = (MemberListBean) arg0.getItemAtPosition(arg2);
                                selectedDirectorOrAdmin = place;
                                Utill.hideKeybord(getActivity() , reservation_user_autocomple_tv);
                                reservation_user_autocomple_tv.setText(place.getUser_first_name() + " " + place.getUser_last_name());
                                reservation_user_autocomple_tv.setSelection(reservation_user_autocomple_tv.getText().length());
                            }
                        });



                        //media player , me











































                        sunLL = (LinearLayout) v.findViewById(R.id.sun);
                        monLL = (LinearLayout) v.findViewById(R.id.mon);
                        tueLL = (LinearLayout) v.findViewById(R.id.tue);
                        wedLL = (LinearLayout) v.findViewById(R.id.wed);
                        thuLL = (LinearLayout) v.findViewById(R.id.thu);
                        friLL = (LinearLayout) v.findViewById(R.id.fri);
                        satLL = (LinearLayout) v.findViewById(R.id.sat);
                        sunTV = (TextView) v.findViewById(R.id.sun_text);
                        monTV = (TextView) v.findViewById(R.id.mon_text);
                        tueTV = (TextView) v.findViewById(R.id.tue_text);
                        wedTV = (TextView) v.findViewById(R.id.wed_text);
                        thuTV = (TextView) v.findViewById(R.id.thu_text);
                        friTV = (TextView) v.findViewById(R.id.fri_text);
                        satTV = (TextView) v.findViewById(R.id.sat_text);
                        sunTick = (ImageView) v.findViewById(R.id.sun_tick);
                        monTick = (ImageView) v.findViewById(R.id.mon_tick);
                        tueTick = (ImageView) v.findViewById(R.id.tue_tick);
                        wedTick = (ImageView) v.findViewById(R.id.wed_tick);
                        thuTick = (ImageView) v.findViewById(R.id.thu_tick);
                        friTick = (ImageView) v.findViewById(R.id.fri_tick);
                        satTick = (ImageView) v.findViewById(R.id.sat_tick);

                        sunLL.setOnClickListener(daysClick);
                        monLL.setOnClickListener(daysClick);
                        tueLL.setOnClickListener(daysClick);
                        wedLL.setOnClickListener(daysClick);
                        thuLL.setOnClickListener(daysClick);
                        friLL.setOnClickListener(daysClick);
                        satLL.setOnClickListener(daysClick);

                        ReservationTimeListAdapter dataAdapter = new ReservationTimeListAdapter(getActivity() , paths , true);
                        durationSpinner.setAdapter(dataAdapter);
                        durationSpinner.setSelection(0);


                        final Spinner slecet_catogory_spinner = (Spinner) v.findViewById(R.id.slecet_catogory_spinner);


                        slecet_catogory_spinner.setAdapter(new SelectCategoryListAdapter(getActivity(), recursiveBookingCourtColours));


                        cancel.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                memeberPopUp.cancel();
                            }
                        });

                        ok.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                boolean isNameValid = false;
                                ArrayList<String> allDates = new ArrayList<String>();
                                String bookingName = booking_reason.getText().toString();


                                InputMethodManager inputManager =
                                        (InputMethodManager) getActivity().
                                                getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputManager.hideSoftInputFromWindow(
                                        booking_reason.getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);


                                try {

                                    if (slecet_catogory_spinner.getSelectedItemPosition() == 0)
                                    {
                                        Utill.showDialg("Please select category.", getActivity());

                                        return;
                                    }

                                    allDates = Utill.getAllBetweenDatess(calendarDate, getRecusiveDate(durationSpinner.getSelectedItemPosition() - 1), days);

                                    if (durationSpinner.getSelectedItemPosition() == 0) {
                                        Utill.showDialg("Please select duration.", getActivity());

                                        return;
                                    }
                                    if (allDates.size() == 0) {
                                        Utill.showDialg("Please select at least one day.", getActivity());

                                        return;
                                    }

                                    memeberPopUp.cancel();


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (Utill.isNetworkAvailable(getActivity())) {
                                    sendRecursiveBooking(selectedDirectorOrAdmin.getUser_id(), allDates, bookingName, recursiveBookingCourtColours.get(slecet_catogory_spinner.getSelectedItemPosition()).getCat_id() + "");
                                } else {
                                    Utill.showNetworkError(mContext);
                                }
                            }
                        });
                        memeberPopUp.setView(v);
                        memeberPopUp.show();


                    }

                } catch (Exception e) {

                }

            }
        });


    }

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
        String finalD = Utill.getRecurrentFinalDate(incrementIn, count, calendarDate);
        // Utill.showToast(mContext,""+finalD);
        return finalD;
    }

    void updateDateUi(String day) {
        if (days.contains(day)) {
            days.remove(day);
        } else {
            days.add(day);
        }
        if (day.equalsIgnoreCase(SUNDAY)) {
            if (days.contains(SUNDAY)) {
                sunTV.setTextColor(getResources().getColor(R.color.blue_header));
                sunTick.setVisibility(View.VISIBLE);
            } else {
                sunTV.setTextColor(getResources().getColor(R.color.black_color));
                sunTick.setVisibility(View.INVISIBLE);
            }
        } else if (day.equalsIgnoreCase(MONDAY)) {
            if (days.contains(MONDAY)) {
                monTV.setTextColor(getResources().getColor(R.color.blue_header));
                monTick.setVisibility(View.VISIBLE);
            } else {
                monTV.setTextColor(getResources().getColor(R.color.black_color));
                monTick.setVisibility(View.INVISIBLE);
            }
        } else if (day.equalsIgnoreCase(TUESDAY)) {
            if (days.contains(TUESDAY)) {
                tueTV.setTextColor(getResources().getColor(R.color.blue_header));
                tueTick.setVisibility(View.VISIBLE);
            } else {
                tueTV.setTextColor(getResources().getColor(R.color.black_color));
                tueTick.setVisibility(View.INVISIBLE);
            }
        } else if (day.equalsIgnoreCase(WEDNESDAY)) {
            if (days.contains(WEDNESDAY)) {
                wedTV.setTextColor(getResources().getColor(R.color.blue_header));
                wedTick.setVisibility(View.VISIBLE);
            } else {
                wedTV.setTextColor(getResources().getColor(R.color.black_color));
                wedTick.setVisibility(View.INVISIBLE);
            }
        } else if (day.equalsIgnoreCase(THURSDAY)) {
            if (days.contains(THURSDAY)) {
                thuTV.setTextColor(getResources().getColor(R.color.blue_header));
                thuTick.setVisibility(View.VISIBLE);
            } else {
                thuTV.setTextColor(getResources().getColor(R.color.black_color));
                thuTick.setVisibility(View.INVISIBLE);
            }
        } else if (day.equalsIgnoreCase(FRIDAY)) {
            if (days.contains(FRIDAY)) {
                friTV.setTextColor(getResources().getColor(R.color.blue_header));
                friTick.setVisibility(View.VISIBLE);
            } else {
                friTV.setTextColor(getResources().getColor(R.color.black_color));
                friTick.setVisibility(View.INVISIBLE);
            }
        } else if (day.equalsIgnoreCase(SATUARDAY)) {
            if (days.contains(SATUARDAY)) {
                satTV.setTextColor(getResources().getColor(R.color.blue_header));
                satTick.setVisibility(View.VISIBLE);
            } else {
                satTV.setTextColor(getResources().getColor(R.color.black_color));
                satTick.setVisibility(View.INVISIBLE);
            }
        }

    }







    MemberAutoCompleteAdapter memberAutoCompleteAdapter ;
    private void getMembersList(String keyWord, final InstantAutoComplete autocompletetv , int userType) {
        sqlListe.getMemberList(getActivity(), new MemberListListener() {
            @Override
            public void onSuccess(ArrayList<MemberListBean> memberListBeanArrayList)
            {

                membersList = new ArrayList<MemberListBean>();
                membersList.addAll(memberListBeanArrayList);

                filteredList = membersList;
                memberAutoCompleteAdapter = new MemberAutoCompleteAdapter(mContext, R.id.textViewItem, membersList ,autocompletetv);

                autocompletetv.setAdapter(memberAutoCompleteAdapter);
                autocompletetv.setText(autocompletetv.getText().toString() + "");


                  autocompletetv.setSelection(1);

                Utill.showKeybord(mContext , autocompletetv);







            }
        },userType ,keyWord);
    }

    @Override
    public boolean onTouch(View v, MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {
            status = START_DRAGGING;
            final float x = me.getX();
            final float y = me.getY();
            lastXAxis = x;
            lastYAxis = y;
            v.setVisibility(View.INVISIBLE);
        } else if (me.getAction() == MotionEvent.ACTION_UP) {
            status = STOP_DRAGGING;
            flag = 0;
            v.setVisibility(View.VISIBLE);
        } else if (me.getAction() == MotionEvent.ACTION_MOVE) {
            if (status == START_DRAGGING) {
                flag = 1;
                v.setVisibility(View.VISIBLE);
                final float x = me.getX();
                final float y = me.getY();
                final float dx = x - lastXAxis;
                final float dy = y - lastYAxis;
                xAxis += dx;
                yAxis += dy;
                v.setX((int) xAxis);
                v.setY((int) yAxis);
                v.invalidate();
            }
        }
        return false;
    }

    void setDataInTable(int i, int j) {
        if (tableLayout != null) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            RelativeLayout relative = (RelativeLayout) row.getChildAt(j);

            TextView t = (TextView) relative.findViewById(R.id.text);
            ImageView cross = (ImageView) relative.findViewById(R.id.cross);


            cross.setVisibility(View.INVISIBLE);
            Log.e("longPressed" ,selectionStatus[i][j]+"");

            if (selectionStatus[i][j])
            {

                t.setBackgroundColor(mContext.getResources().getColor(R.color.free_slot_color));
                selectionStatus[i][j] = false;
                selectionStatuscopy[i][j] = false;
                //if (priviousX != i || priviousY != j )
                {
                    int count = totalSelectedCourtNoList.get(j);
                    if (count > 0)
                        totalSelectedCourtNoList.set(j, new Integer(--count));
                }


            } else
            {




                Log.e("longPressed", longPressed + "");

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

                Log.e("rrrrrrrrr" ,mainList.get(j).get(i).getCourt_reservation_id());

                if (delete == true && false == Validation.isStringNullOrBlank(mainList.get(j).get(i).getCourt_reservation_id()) /*&&(true||uniqueId.equals(mainList.get(j).get(i).getMemberbookedid())|| uniqueId.equals(mainList.get(j).get(i).getRecursive_id() )*/)
                    mainList.get(j).get(i).setItemRemoved(false) ;


                if (delete)
                {
                    if (Validation.isStringNullOrBlank(mainList.get(j).get(i).getCat_color()))
                    {
                        if (!mainList.get(j).get(i).getCourt_reservation_user_id().equalsIgnoreCase(SessionManager.getUser_id(mContext))) {
                            t.setBackgroundColor(mContext.getResources().getColor(R.color.booked_slot_color));
                            t.setTextSize(15);
                        } else {
                            t.setBackgroundColor(mContext.getResources().getColor(R.color.my_booked_slot_color));
                            t.setTextSize(15);
                        }


                    } else
                    {
                        try {
                            t.setBackgroundColor(Color.parseColor(mainList.get(j).get(i).getCat_color()));
                            t.setTextSize(15);
                        } catch (Exception e) {

                        }
                    }
                }
                else
                {
                    t.setBackgroundColor(mContext.getResources().getColor(R.color.green_selection_color));

                }


                selectionStatus[i][j] = true;
                selectionStatuscopy[i][j] = true;
            }

            isbulkbook = false;


            {
                int numofCourtSelect = 0;

                for (int count = 0; count < totalSelectedCourtNoList.size(); count++) {


                    if (totalSelectedCourtNoList.get(count) > max_book) {

                        isbulkbook = true;
                        break;
                    } else {
                        if (totalSelectedCourtNoList.get(count) != 0) {
                            numofCourtSelect++;
                        }
                    }


                }
                //Log.e("Break ","True");
                if (numofCourtSelect > 1) {
                    isbulkbook = true;
                }
            }
            //Log.e("isbulkbook" , isbulkbook+" "+max_book);


            if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_DIRECTOR) == true || SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MOBILE_ADMIN) == true) {
                if (isbulkbook) {
                    bookBtn.setText("Book");/*Block */
                } else {
                    bookBtn.setText("Book");
                }
            } else {
                bookBtn.setText("Book");
            }



            //Toast.makeText(getActivity() , "J == "+totalSelectedCourtNoList.get(j) ,1).show();
            if (databean != null ) {


                if (Validation.isStringNullOrBlank(databean.getCourt_reservation_id()) == true)

                {
                    hidedeleteReservationChart(databean ,true);

                }

                //databean = null ;
            }


        }


        priviousX = i;
        priviousY = j;


    }

    void invisileCancel(int i, int j) {
        if (tableLayout != null) {
            TableRow row = (TableRow) tableLayout.getChildAt(j);
            RelativeLayout relative = (RelativeLayout) row.getChildAt(i);

            TextView t = (TextView) relative.findViewById(R.id.text);
            ColorDrawable cd = (ColorDrawable) relative.getBackground();

            ImageView cross = (ImageView) relative.findViewById(R.id.cross);

            if (selectionStatus[j][i]) {
                selectionStatus[j][i] = false;
                selectionStatuscopy[j][i] = false;
                t.setBackgroundColor(mContext.getResources().getColor(R.color.free_slot_color));
            }
            if (cross.getVisibility() == View.VISIBLE) {
                cross.setVisibility(View.INVISIBLE);
                if ((SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_DIRECTOR) == true || SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MOBILE_ADMIN) == true) && deleteCourtData != null
                        && !deleteCourtData.getCourt_reservation_user_id().equalsIgnoreCase(SessionManager.getUser_id(mContext)))
                    t.setBackgroundColor(mContext.getResources().getColor(R.color.booked_slot_color));
                else
                    t.setBackgroundColor(mContext.getResources().getColor(R.color.my_booked_slot_color));
            }

        }
    }

    void checkSlotSelection() {
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

    void resetReservationChart() {
        if (mainList != null && selectionStatus != null) {
            for (int i = 0; i < selectionStatus.length; i++) {
                for (int j = 0; j < selectionStatus[i].length; j++) {

                    mainList.get(j).get(i).setItemRemoved(true);

                    if (selectionStatus[i][j])
                    {
                        setDataInTable(i, j);

                    }
                }
            }
        }
        hideBookCancel();
    }

    void resetReservationChartAll() {
        if (mainList != null) {
            for (int i = 0; i < mainList.size(); i++) {
                for (int j = 0; j < mainList.get(i).size(); j++) {
                    invisileCancel(i, j);
                }
            }
        }
        hideBookCancel();
    }

    void deleteReservationChart(CourtData bean) {

        if (mainList != null) {
            for (int i = 0; i < mainList.size(); i++) {
                for (int j = 0; j < mainList.get(i).size(); j++) {
                    if (!Utill.isStringNullOrBlank(bean.getMemberbookedid())
                            && mainList.get(i).get(j).getMemberbookedid().equalsIgnoreCase(bean.getMemberbookedid())) {
                        showDeleteDataInTable(j, i);
                    } else if (!Utill.isStringNullOrBlank(bean.getRecursive_id())
                            && mainList.get(i).get(j).getRecursive_id().equalsIgnoreCase(bean.getRecursive_id())) {
                        showDeleteDataInTable(j, i);
                    }
                }
            }
        }
        hideBookCancel();
    }

    void hidedeleteReservationChart(CourtData bean , boolean myStatus) {

        if (mainList != null) {
            for (int i = 0; i < mainList.size(); i++) {
                for (int j = 0; j < mainList.get(i).size(); j++) {
                    if (!Utill.isStringNullOrBlank(bean.getMemberbookedid())
                            && mainList.get(i).get(j).getMemberbookedid().equalsIgnoreCase(bean.getMemberbookedid())) {
                        hideDeleteDataInTable(j, i ,myStatus);
                    } else if (!Utill.isStringNullOrBlank(bean.getRecursive_id())
                            && mainList.get(i).get(j).getRecursive_id().equalsIgnoreCase(bean.getRecursive_id())) {
                        hideDeleteDataInTable(j, i ,myStatus);
                    }
                }
            }
        }

    }



    void hidedeleteReservationChartAll(CourtData bean , boolean myStatus) {

        if (mainList != null) {
            for (int i = 0; i < mainList.size(); i++) {
                for (int j = 0; j < mainList.get(i).size(); j++) {

                    hideDeleteDataInTable(j, i ,myStatus);

                }
            }
        }

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

    void hideDeleteDataInTable(int i, int j , boolean myStatus)
    {
        if (tableLayout != null) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            RelativeLayout relative = (RelativeLayout) row.getChildAt(j);
            ImageView crossImage = (ImageView) relative.findViewById(R.id.cross);
            TextView textView = (TextView) relative.findViewById(R.id.text);
            CourtData bean = mainList.get(j).get(i);



            if(!myStatus)
            {

            }
            else
            {

            }


            if (!Utill.isStringNullOrBlank(bean.getCourt_reservation_user_id())/* && bean.getCourt_reservation_user_id().equalsIgnoreCase(SessionManager.getUser_id(mContext))*/)
            {
                //relative.setBackgroundColor(mContext.getResources().getColor(R.color.my_booked_slot_color));
                textView.setTextSize(15);
                ///

                Log.e("status" ,bean.isItemRemoved()+" ");


                if (delete )
                {

                    if (databean.getCourt_reservation_id().equals(databean1.getCourt_reservation_id()))
                    {
                        crossImage.setVisibility(View.VISIBLE);

                    }
                    else
                    {
                        crossImage.setVisibility(View.INVISIBLE);

                    }


                }
                else
                {


                    crossImage.setVisibility(View.INVISIBLE);

                }

                if (bean.isItemRemoved() == false || myStatus)
                {

                    if (Validation.isStringNullOrBlank(bean.getCat_color()))
                    {


                        if (!bean.getCourt_reservation_user_id().equalsIgnoreCase(SessionManager.getUser_id(mContext)))
                        {
                            textView.setBackgroundColor(mContext.getResources().getColor(R.color.booked_slot_color));
                            textView.setTextSize(15);
                        }
                        else
                        {
                            textView.setBackgroundColor(mContext.getResources().getColor(R.color.my_booked_slot_color));
                            textView.setTextSize(15);
                        }


                    } else {
                        try {
                            textView.setBackgroundColor(Color.parseColor(bean.getCat_color()));
                            textView.setTextSize(15);
                        }
                        catch (Exception e)
                        {

                        }
                    }

                }
                else
                {

                }




            } else {
                crossImage.setVisibility(View.INVISIBLE);


            }


        }
    }



    void sendReservationNew(String userId, String bookingName, String cat_id) {
        // String user_id = "";

        date = "";
        String club_id = "";
        if (Utill.isStringNullOrBlank(userId))
            userId = SessionManager.getUser_id(mContext);

        // if (bean != null && bean.size() > 0)


        this.userId = userId;

        {
            // user_id = SessionManager.getUser_id(mContext);
            // if
            // (SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_DIRECTOR))
            // user_id = SessionManager.getUser_id(mContext);

            club_id = SessionManager.getUser_Club_id(mContext);
            date = calendarDate;
            JSONObject json = new JSONObject();
            final JSONArray jArray = new JSONArray();
            for (int i = 0; i < selectionStatus.length; i++) {
                for (int j = 0; j < selectionStatus[i].length; j++)
                    if (selectionStatus[i][j]) {
                        JSONObject myJson = new JSONObject();
                        try {
                            myJson.put("strart_time", mainList.get(j).get(i).getCourt_start_time());
                            myJson.put("end_time", mainList.get(j).get(i).getCourt_end_time());
                            myJson.put("court_reservation_court_id", mainList.get(j).get(i).getCourt_id());
                            myJson.put("court_name", mainList.get(j).get(i).getCourt_name());

                            jArray.put(myJson);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
            }
            try {
                //Toast.makeText(getActivity(), "xcasdfasdsada "+jArray, 1).show();
                json.put("time", jArray);
                // ShowUserMessage.showUserMessage(mContext, json.toString());
                Utill.showProgress(mContext);
                Map<String, Object> params = new HashMap<String, Object>();
//2017-01-03


                params.put("court_reservation_user_id", userId);
                params.put("court_reservation_date", date);
                params.put("slots", json.toString());
                params.put("court_reservation_club_id", club_id);
                params.put("user_type", SessionManager.getUser_type(mContext));
                params.put("court_reservation_purpuse", "" + bookingName);
                params.put("user_id", SessionManager.getUserId(getActivity()));
                params.put("cat_id", cat_id);
                ///


                ModelManager modelManager = new ModelManager(getActivity());

                modelManager.sendReservation(params, new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {

                        Utill.hideProgress();
                        membersList = null;

                        String rsss = json;

                        try {
                            JSONObject jsonObj = new JSONObject(json);
                            bottomLayout.setVisibility(View.GONE);


                            if (jsonObj.getBoolean("status")) {


                                if (ShowReservationFragment.userId.equals(SessionManager.getUser_id(getActivity()))) {
                                    insertDataEventCalender(jsonObj);
                                } else {
                                    showDialgfordialgforbooking(jsonObj.getString("message"), getActivity());
                                }


                            } else {
                                ShowUserMessage.showUserMessage(getActivity(), jsonObj.getString("message"));
                            }


                        } catch (Exception e) {
                            ShowUserMessage.showDialogOnActivity(getActivity(), e.getMessage());
                            e.printStackTrace();
                        }


                        if (reservationSelectionPopup != null)
                            reservationSelectionPopup.cancel();
                        if (memeberPopUp != null)
                            memeberPopUp.cancel();
                    }

                    @Override
                    public void onError(String msg) {
                        ShowUserMessage.showUserMessage(getActivity(), "" + msg);
                        resetReservationChart();

                        Utill.hideProgress();
                    }
                });
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
		/*
		 * else { ShowUserMessage.showUserMessage(mContext, "Error."); }
		 */
    }

    void sendRecursiveBooking(String userId, ArrayList<String> dates, String purpose, String cat_id) {
        // String user_id = "";
        String club_id = "";
        if (Utill.isStringNullOrBlank(userId))
            userId = SessionManager.getUser_id(mContext);
        {
            club_id = SessionManager.getUser_Club_id(mContext);
            JSONObject json = new JSONObject();
            JSONArray jArray = new JSONArray();
            for (int l = 0; l < dates.size(); l++)
                for (int i = 0; i < selectionStatus.length; i++) {
                    for (int j = 0; j < selectionStatus[i].length; j++)
                        if (selectionStatus[i][j]) {
                            JSONObject myJson = new JSONObject();
                            try {
                                myJson.put("strart_time", mainList.get(j).get(i).getCourt_start_time());
                                myJson.put("end_time", mainList.get(j).get(i).getCourt_end_time());
                                myJson.put("court_reservation_court_id", mainList.get(j).get(i).getCourt_id());
                                myJson.put("court_reservation_date", dates.get(l));
                                myJson.put("court_reservation_club_id", SessionManager.getUser_Club_id(mContext));
                                myJson.put("court_name", mainList.get(j).get(i).getCourt_name());
                                myJson.put("cat_id", cat_id);


                                jArray.put(myJson);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                }
            try {


                json.put("recursive", jArray);
                // ShowUserMessage.showUserMessage(mContext, json.toString());
                Utill.showProgress(mContext);
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("court_reservation_user_id", userId);
                params.put("recursivebooking_purpuse", "" + purpose);
                // params.put("court_reservation_club_id", club_id);
                params.put("user_type", SessionManager.getUser_type(mContext));
                params.put("cat_id", cat_id);
                params.put("recursive", json);

                GlobalValues.getModelManagerObj(mContext).sendRecursiveReuest(params, new ModelManagerListener() {
                    @Override
                    public void onSuccess(String json) {
                        Utill.hideProgress();
						/*resetReservationChart();
						getReservation(calendarDate);

						*/

                        if (reservationSelectionPopup != null)
                            reservationSelectionPopup.cancel();
                        bottomLayout.setVisibility(View.GONE);
                        if (memeberPopUp != null)
                            memeberPopUp.cancel();
                        try {
                            JSONObject jsonObj = new JSONObject(json);


                            showDialgfordialgforbooking(jsonObj.getString("message"), getActivity());


                            //showDialgfordialg(jsonObj.getString("message"), getActivity());
                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onError(String msg) {
                        ShowUserMessage.showUserMessage(getActivity(), "" + msg);
                        Utill.hideProgress();
                    }
                });
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
		/*
		 * else { ShowUserMessage.showUserMessage(mContext, "Error."); }
		 */
    }

    private void showBookCancel() {
        if (delete)
        {
            bottomLayout.setVisibility(View.GONE);

        }
        else
        {
            bottomLayout.setVisibility(View.VISIBLE);

        }
    }

    private void hideBookCancel() {
        bottomLayout.setVisibility(View.GONE);
    }

    /*void loadTableAgain1111() {
		ScrollView sv = new ScrollView(mContext);
		TableLayout tableLayout = createTableLayout();
		HorizontalScrollView hsv = new HorizontalScrollView(mContext);
		hsv.addView(tableLayout);
		sv.addView(hsv);
		mainLayout.removeAllViews();
		mainLayout.addView(sv);
	}*/
    private void showOldDate() {

        bottomLayout.setVisibility(View.GONE);
        oldDateMSG.setVisibility(View.VISIBLE);
    }

    void hideOldDate() {
        //bottomLayout.setVisibility(View.VISIBLE);
        oldDateMSG.setVisibility(View.GONE);
    }

    void cheackDate() {
        boolean oldDate = false;


        if(Utill.compareTwoDate(currentTimeCal , selected_calender))//6:30 5:00
        {
            showOldDate();

        }
        else
        {
            hideOldDate();

        }


    }

    @SuppressWarnings("deprecation")
    public void showDialg(String msg, Context mContext, final AutoCompleteTextView tv) {


        isdialogshow = false;

        final AlertDialog alertDialog = new AlertDialog.Builder(
                mContext).create();

// Setting Dialog Title
        alertDialog.setTitle(SessionManager.getClubName(mContext));

// Setting Dialog Message
        alertDialog.setMessage(msg);

// Setting Icon to Dialog


// Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                if (tv.getText().toString().length() >= 2) {
                    tv.setText(tv.getText().toString().substring(0, tv.getText().toString().length() - 2));

                    tv.setSelection(tv.getText().toString().length());
                } else
                    tv.setText("");
                isdialogshow = true;
                alertDialog.dismiss();
                tv.showDropDown();
                tv.requestFocus();


            }
        });

// Showing Alert Message
        alertDialog.show();
    }



    @SuppressWarnings("deprecation")
    public void showDialgfordialgforbooking(String msg, Context mContext) {


        final AlertDialog alertDialog = new AlertDialog.Builder(
                mContext).create();

// Setting Dialog Title
        alertDialog.setTitle(SessionManager.getClubName(mContext));

// Setting Dialog Message
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);

// Setting Icon to Dialog


// Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed

                //Toast.makeText(getActivity(), "ssssssss", 1).sh

                getReservation(calendarDate);

                dialog.dismiss();
                //Utill.showDialg(msg, mContext);


            }
        });

// Showing Alert Message
        alertDialog.show();
    }

    public void setReservation() {
        if (InternetConnection.isInternetOn(getActivity())) {
            Utill.showProgress(getActivity());
            HashMap<String, String> params = new HashMap<String, String>();

            params.put("court_club_id", SessionManager.getUser_Club_id(getActivity()));

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String formatted = format1.format(cal.getTime());

            params.put("court_reservation_date", formatted);
            params.put("court_reservation_user_id", SessionManager.getUser_id(getActivity()));
            ;

            aqQuery.ajax(WebService.myreservation, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                public void callback(String url, JSONObject object, com.androidquery.callback.AjaxStatus status) {
                    Utill.hideProgress();
                    Log.e("aaaaaaa", object + "");

                    try {


                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        currentTimeCal = Calendar.getInstance(Locale.ENGLISH);
                        currentTimeCal.set(Calendar.AM_PM, Calendar.AM);
                        currentTimeCal.setTime(simpleDateFormat.parse(object.getString("time")));




                        calendarDate =   Utill.getDateYYMMDDFromCal(currentTimeCal) ;

                        CourtName = object.optString("CourtName");
                        max_book = object.getInt("max_book");

                        endTimeClub = (Calendar) currentTimeCal.clone();
                        selected_calender = (Calendar) currentTimeCal.clone();

                        endTimeClub.set(Calendar.HOUR_OF_DAY, Integer.parseInt(object.getString("endtime").split(":")[0]));
                        endTimeClub.set(Calendar.MINUTE, Integer.parseInt(object.getString("endtime").split(":")[1]));


                        Log.e("time ", object.getString("time") + " " + currentTimeCal.getTime() + " " + endTimeClub.getTime() + " " + currentTimeCal.compareTo(endTimeClub));
//


                        if (currentTimeCal.compareTo(endTimeClub) > 0)
                        {
                            Calendar currentCalCopy = (Calendar) currentTimeCal.clone();

                            String nextDate = Utill.getNextDate(currentCalCopy);
                            datePicker.setText(Utill.formattedDateFromString("", "", nextDate));
                            left_layout.setVisibility(View.GONE);
                            right_layout.setVisibility(View.VISIBLE);


                            calendarDate = Utill.reverseDate(nextDate);

                            setNextReservation(currentCalCopy);
                            return;
                        } else {

                        }

                        JSONArray response_array = object.getJSONArray("response");


                        for (int i = 0; i < response_array.length(); i++) {


                            JSONArray info_array = response_array.getJSONObject(i).getJSONArray("info");
                            for (int j = 0; j < info_array.length(); j++) {
                                JSONObject info_array_item = info_array.getJSONObject(j);

                                JSONArray booking_array = info_array_item.getJSONArray("book");


                                ArrayList<CourtBookBean> CourtBookList = new ArrayList<CourtBookBean>();

                                MyReservationBean my_reservation_nean = new MyReservationBean();
                                for (int k = 0; k < booking_array.length(); k++)
                                {
                                    JSONObject booking_array_item = booking_array.getJSONObject(k);
                                    CourtBookBean court_book_bean = new CourtBookBean();
                                    court_book_bean.setCourt_id(booking_array_item.getString("court_id"));
                                    court_book_bean.setCourt_name(booking_array_item.getString("court_name"));
                                    court_book_bean.setCourt_reservation_start_time(booking_array_item.getString("court_reservation_start_time"));
                                    CourtBookList.add(court_book_bean);
                                }
                                my_reservation_nean.setBookingname(info_array_item.getString("bookingname"));
                                my_reservation_nean.setCourt_reservation_recursiveid(info_array_item.getString("court_reservation_recursiveid"));
                                my_reservation_nean.setReservedate(info_array_item.getString("reservedate"));
                                my_reservation_nean.setReservationId(info_array_item.getString("court_reservation_id"));

                                my_reservation_nean.setRecursivedates(info_array_item.getString("recursivedates"));
                                my_reservation_nean.setMemberbookedid(info_array_item.getString("memberbookedid"));
                                //.setReservedate(info_array_item.getString("reservedate"));

                                my_reservation_nean.setCourt_list(CourtBookList);
                                myReservationList.add(my_reservation_nean);
                            }
                        }


                        if (myReservationList.size() != 0) {
                            myReservationListAdapter = new MyReservationListAdapter(myReservationList, getActivity() , ShowReservationFragment.this);
                            my_reservation_list_view.setAdapter(myReservationListAdapter);

                        } else {
                            left_layout.setVisibility(View.VISIBLE);
                            right_layout.setVisibility(View.GONE);
                            getReservation(calendarDate);
                        }


                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }

                    //Toast.makeText(getActivity(), "Size  "+myReservationList.size(), Toast.LENGTH_SHORT).show();

                }

                ;
            });

        } else {
            Utill.showDialg(getString(R.string.no_internet_connection), getActivity());
        }
    }

    public void setNextReservation(Calendar cal) {
        if (InternetConnection.isInternetOn(getActivity())) {
            Utill.showProgress(getActivity());
            HashMap<String, String> params = new HashMap<String, String>();

            params.put("court_club_id", SessionManager.getUser_Club_id(getActivity()));


            //cal.add(Calendar.DATE ,1);

            //max_cal = (Calendar) cal.clone();
            //max_cal.add(Calendar.DATE ,advacsedbookingdays);

            //	Log.e("max_cal" , max_cal.getTime()+"");

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String formatted = format1.format(cal.getTime());

            selected_calender = (Calendar) cal.clone();

            params.put("court_reservation_date", formatted);
            params.put("court_reservation_user_id", SessionManager.getUser_id(getActivity()));
            ;

            aqQuery.ajax(WebService.myreservation, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                public void callback(String url, JSONObject object, com.androidquery.callback.AjaxStatus status) {
                    Utill.hideProgress();
                    Log.e("aaaaaaa", object + "");

                    try {


                        JSONArray response_array = object.getJSONArray("response");


                        for (int i = 0; i < response_array.length(); i++) {


                            JSONArray info_array = response_array.getJSONObject(i).getJSONArray("info");
                            for (int j = 0; j < info_array.length(); j++) {
                                JSONObject info_array_item = info_array.getJSONObject(j);

                                JSONArray booking_array = info_array_item.getJSONArray("book");


                                ArrayList<CourtBookBean> CourtBookList = new ArrayList<CourtBookBean>();

                                MyReservationBean my_reservation_nean = new MyReservationBean();
                                for (int k = 0; k < booking_array.length(); k++) {
                                    JSONObject booking_array_item = booking_array.getJSONObject(k);
                                    CourtBookBean court_book_bean = new CourtBookBean();
                                    court_book_bean.setCourt_id(booking_array_item.getString("court_id"));
                                    court_book_bean.setCourt_name(booking_array_item.getString("court_name"));
                                    court_book_bean.setCourt_reservation_start_time(booking_array_item.getString("court_reservation_start_time"));
                                    CourtBookList.add(court_book_bean);
                                }
                                my_reservation_nean.setBookingname(info_array_item.getString("bookingname"));
                                my_reservation_nean.setCourt_reservation_recursiveid(info_array_item.getString("court_reservation_recursiveid"));
                                my_reservation_nean.setReservedate(info_array_item.getString("reservedate"));
                                my_reservation_nean.setReservationId(info_array_item.getString("court_reservation_id"));
                                my_reservation_nean.setRecursivedates(info_array_item.getString("recursivedates"));
                                my_reservation_nean.setMemberbookedid(info_array_item.getString("memberbookedid"));
                                //.setReservedate(info_array_item.getString("reservedate"));

                                my_reservation_nean.setCourt_list(CourtBookList);
                                myReservationList.add(my_reservation_nean);
                            }
                        }


                        if (myReservationList.size() != 0) {
                            myReservationListAdapter = new MyReservationListAdapter(myReservationList, getActivity() , ShowReservationFragment.this);
                            my_reservation_list_view.setAdapter(myReservationListAdapter);

                        } else {
                            left_layout.setVisibility(View.VISIBLE);
                            right_layout.setVisibility(View.GONE);
                            getReservation(calendarDate);
                        }


                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }

                    //Toast.makeText(getActivity(), "Size  "+myReservationList.size(), Toast.LENGTH_SHORT).show();

                }

                ;
            });

        } else {
            Utill.showDialg(getString(R.string.no_internet_connection), getActivity());
        }
    }

    void singelCourtBookingByDirector() {
        memberBean = new MemberListBean();


        memeberPopUp = new AlertDialog.Builder(mContext).create();
        memeberPopUp.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.memberlistpopup, null);
        TextView cancel = (TextView) v.findViewById(R.id.cancel);
        final TextView ok = (TextView) v.findViewById(R.id.ok);
        final TextView error_msg_tv = (TextView) v.findViewById(R.id.error_msg_tv);


        final InstantAutoComplete bookingForAuto = (InstantAutoComplete) v.findViewById(R.id.booking_for);

        TextView select_category_lable_tv = (TextView) v.findViewById(R.id.select_category_lable_tv);
        RelativeLayout select_category_spinner_layout = (RelativeLayout) v.findViewById(R.id.select_category_spinner_layout);
        final EditText booking_reason = (EditText) v.findViewById(R.id.booking_reason);

        select_category_lable_tv.setTextColor(getActivity().getResources().getColor(R.color.gray_color));
        select_category_spinner_layout.setAlpha(0.6f);
        select_category_spinner_layout.setEnabled(false);

        //InputMethodManager imm = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(bookingForAuto, InputMethodManager.SHOW_IMPLICIT);

        //bookingForAuto.setText(SessionManager.getFirstName(mContext)+" "+SessionManager.getLastName(mContext));
        final String name = SessionManager.getFirstName(getActivity()) + " " + SessionManager.getLastName(getActivity());


        bookingForAuto.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                //Toast.makeText(getActivity(), "KeyCode "+keyCode, 1).show();

                if (keyCode == 66) {
                    InputMethodManager inputManager =
                            (InputMethodManager) getActivity().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            bookingForAuto.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });


        bookingForAuto.setAdapter(new MemberAutoCompleteAdapter(mContext, R.id.textViewItem, membersList ,bookingForAuto));


        bookingForAuto.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

                beforname = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub


                String bookingForStr = s.toString();
                boolean isNameValid = false;

                if (s.toString().length() != 0)
                {
                    if (s.toString().length() == 1 && beforname.length() == 0)
                    {
                        memberAutoCompleteAdapter = null ;
                        memberBean = new MemberListBean();

                        getMembersList(s.toString(), bookingForAuto ,AppConstants.AllMEMBERlIST);
                    }

                } else {

                }



                boolean isMemberExits =false;
                int totalMember = 0;

                if (membersList != null && s.toString().length() != 0)
                {
                    for (int i = 0 ; i < membersList.size() ;i++)
                    {
                        String memberName =    membersList.get(i).getUser_first_name()+" "+membersList.get(i).getUser_last_name() ;

                        if (memberName.equalsIgnoreCase(bookingForStr))
                        {
                            isMemberExits = true ;
                            memberBean = membersList.get(i);


                        }

                        if (membersList.get(i).getUser_first_name().toLowerCase().startsWith((bookingForStr.toLowerCase()))||membersList.get(i).getUser_last_name().toLowerCase().startsWith((bookingForStr.toLowerCase()))||memberName.toLowerCase().startsWith((bookingForStr.toLowerCase())))
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


                        if (membersList != null)
                        {
                            membersList.clear();
                        }
                        if (memberAutoCompleteAdapter!= null)
                        {
                            try
                            {
                                // memberAutoCompleteAdapter.notifyDataSetChanged();
                            }
                            catch (Exception e)
                            {

                            }

                        }
                    }


                }




                if (isMemberExits)
                {
                    ok.setAlpha(1.0f);

                }
                else
                {
                    ok.setAlpha(0.8f);

                }
                if (totalMember != 0 || Validation.isStringNullOrBlank(bookingForStr) || memberAutoCompleteAdapter == null ||(membersList == null) )
                {
                    error_msg_tv.setVisibility(View.GONE);
                }
                else
                {
                    if (membersList.size() == 0)
                    {
                        error_msg_tv.setVisibility(View.GONE);

                    }
                    else
                    {

                    }
                    error_msg_tv.setVisibility(View.VISIBLE);


                }


                ok.setEnabled(isMemberExits);

                if (name.equalsIgnoreCase(beforname) /*&& !(bookingForAuto.getText().toString() =="" ||bookingForAuto.getText().toString().equalsIgnoreCase("")*/) {
                    bookingForAuto.setText("");
                }
                //
            }
        });

        //Toast.makeText(getActivity(), "name  "+name, 1).show();
        //	Editable etext = bookingForAuto.getText();
        //Selection.setSelection(etext, position);
        bookingForAuto.setSelection(bookingForAuto.getText().length());


        bookingForAuto.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                MemberListBean place = (MemberListBean) arg0.getItemAtPosition(arg2);
                memberBean = place;
                Utill.hideKeybord(getActivity() , bookingForAuto);
                bookingForAuto.setText(place.getUser_first_name() + " " + place.getUser_last_name());
                bookingForAuto.setSelection(bookingForAuto.getText().length());
            }
        });


        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                memeberPopUp.cancel();
            }
        });
        ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String bookingName = booking_reason.getText().toString();


                Utill.hideKeybord(getActivity() , bookingForAuto);

                if (Utill.isNetworkAvailable(getActivity())) {
                    sendReservationNew(memberBean.getUser_id(), bookingName, "");
                } else {
                    Utill.showNetworkError(mContext);
                }




            }
        });
        memeberPopUp.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        memeberPopUp.setView(v);
        memeberPopUp.show();
    }
    int reminderMinut ;

    public void insertDataEventCalender(final long startTime, final long endTime, final int court_reservation_court_id, final int court_reservation_club_id, final int court_reservation_id, final int bookingid, String reservationName, String timeMinut , String courtName) {
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

            if (Validation.isStringNullOrBlank(reservationName)) {

            } else {
                values.put(CalendarContract.Events.TITLE, reservationName);

            }
            String titel = "Reminder : "+SessionManager.getClubName(getContext())+" "+sessionManager.getSportFiledName(getActivity())+" reservation";

            values.put(CalendarContract.Events.TITLE,titel);

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

            sqlListe.addEvent(id, court_reservation_court_id, court_reservation_club_id, court_reservation_id, bookingid);
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

    public void insertDataEventCalender(final JSONObject jsonObj) {





        try {

            JSONArray courtInfoJsonArray = jsonObj.getJSONArray("CourtInfo");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Calendar startTimeCal = Calendar.getInstance();
            Calendar endTimeCal = Calendar.getInstance();
            for (int i = 0; i < courtInfoJsonArray.length(); i++) {

                JSONObject courtInfoJsonArrayItem = courtInfoJsonArray.getJSONObject(i);
                if (i == 0) {
                    startTimeCal.setTime(simpleDateFormat.parse(date + " " + courtInfoJsonArrayItem.getString("strart_time")));
                    endTimeCal.setTime(simpleDateFormat.parse(date + " " + courtInfoJsonArrayItem.getString("end_time")));

                }

                int nextindex = (i + 1);

                if (nextindex == courtInfoJsonArray.length()) {
                    //timeSloatList.get(time_list_spinner.getSelectedItemPosition())

                    String timeSlot = sessionManager.getAlaramTimeDuration(getActivity());


                    insertDataEventCalender(startTimeCal.getTimeInMillis(), endTimeCal.getTimeInMillis(), courtInfoJsonArrayItem.getInt("court_reservation_court_id"), courtInfoJsonArrayItem.getInt("court_reservation_club_id"), courtInfoJsonArrayItem.getInt("court_reservation_id"), courtInfoJsonArrayItem.getInt("bookingid"), sessionManager.getCalenderEntryTitle(getActivity()), timeSlot,"");

                } else {
                    JSONObject courtInfoJsonArrayNextItem = courtInfoJsonArray.getJSONObject(nextindex);
                    if (courtInfoJsonArrayItem.getString("end_time").equals(courtInfoJsonArrayNextItem.getString("strart_time")) || courtInfoJsonArrayItem.getString("strart_time").equals(courtInfoJsonArrayNextItem.getString("strart_time"))) {
                        endTimeCal.setTime(simpleDateFormat.parse(date + " " + courtInfoJsonArrayNextItem.getString("end_time")));

                    } else {
                        String timeSlot = sessionManager.getAlaramTimeDuration(getActivity());

                        insertDataEventCalender(startTimeCal.getTimeInMillis(), endTimeCal.getTimeInMillis(), courtInfoJsonArrayItem.getInt("court_reservation_court_id"), courtInfoJsonArrayItem.getInt("court_reservation_club_id"), courtInfoJsonArrayItem.getInt("court_reservation_id"), courtInfoJsonArrayItem.getInt("bookingid"), sessionManager.getCalenderEntryTitle(getActivity()), timeSlot,"");
                        startTimeCal.setTime(simpleDateFormat.parse(date + " " + courtInfoJsonArrayNextItem.getString("strart_time")));
                        endTimeCal.setTime(simpleDateFormat.parse(date + " " + courtInfoJsonArrayNextItem.getString("end_time")));

                    }
                }


            }

            showDialgfordialgforbooking(jsonObj.getString("message"), getActivity());
        } catch (Exception e) {
            ShowUserMessage.showDialogOnActivity(getActivity(), e.getMessage());
        }


    }

    // Inner Class
    public class GridCellAdapter extends BaseAdapter implements OnClickListener {
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
            Calendar calendar = getInstanse();
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
            gridcell.setBackground(getResources().getDrawable(R.drawable.calendar_tile_small));
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);
            // Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear);

            if (day_color[1].equals("GREY")) {
                gridcell.setTextColor(getResources().getColor(R.color.gray_color));

            }
            if (day_color[1].equals("WHITE")) {
                gridcell.setTextColor(getResources().getColor(R.color.black_color));
            }
            String currentDate = day_color[0]+"-"+day_color[2]+"-"+day_color[3];
            SimpleDateFormat currentDateformat = new SimpleDateFormat("d-MMMM-yyyy");
            String systemCurrentDate = currentDateformat.format(getInstanse().getTime());
            String appDate = theday + "-" + themonth + "-" + theyear;

            if (day_color[1].equals("WHITE"))
            {


                if(selected_calender != null)
                {
                    mySelectedDate = currentDateformat.format(selected_calender.getTime());

                    //String dateFormate = simpleDateFormat.format(date);
                    Log.e("current date" ,mySelectedDate+"  "+ appDate);
                    if (mySelectedDate.equals(appDate))
                    {

                        gridcell.setBackground(getResources().getDrawable(R.color.blue_header));
                    }
                    else
                    {
                        gridcell.setTextColor(getResources().getColor(R.color.black_color));
                    }
                }
                else
                {
                    gridcell.setTextColor(getResources().getColor(R.color.black_color));
                }


            }

            if (day_color[1].equals("BLUE") )
            {
                gridcell.setTextColor(getResources().getColor(R.color.white_color));


                if (Validation.isStringNullOrBlank(mySelectedDate) == false && mySelectedDate.equals(systemCurrentDate) == false)
                {
                    gridcell.setBackground(getResources().getDrawable(R.drawable.calendar_bg_orange));
                }
                else
                {
                    gridcell.setBackground(getResources().getDrawable(R.color.blue_header));
                    //  gridcell.setBackground(getResources().getDrawable(R.drawable.calendar_bg_orange));
                }

            }






            return row;
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        public void onClick(View view) {

            selectDateFromCalender(view);
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


    String mySelectedDate = "";


    public void selectDateFromCalender(View view)
    {
        String date_month_year = (String) view.getTag();

        if (selectedDayMonthYearButton != null)
            selectedDayMonthYearButton.setText("Selected: " + date_month_year);
        mySelectedDate = date_month_year;



        try {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

            Date parsedDate = dateFormatter.parse(date_month_year);


            selected_calender.set(Calendar.DATE, parsedDate.getDate());
            selected_calender.set(Calendar.MONTH, parsedDate.getMonth());
            try {
                selected_calender.set(Calendar.YEAR, Integer.parseInt(date_month_year.split("-")[2]));

            } catch (Exception e) {

            }
            Log.e("Selected date", date_month_year.split("-")[2]);
            // parsedDate.
            Calendar c = getInstanse();

            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = df.format(c.getTime());

            c.setTime(parsedDate);

            int date = c.get(Calendar.DATE);
            int month = c.get(Calendar.MONTH) + 1;
            int year = c.get(Calendar.YEAR);
            String dateStr = "" + date;
            String monthStr = "" + month;
            if (dateStr.length() == 1) {
                dateStr = "0" + dateStr;
            }
            if (monthStr.length() == 1) {
                monthStr = "0" + monthStr;
            }
            String finalDate1 = year + "-" + monthStr + "-" + dateStr;
            String finalDate = dateStr + "-" + monthStr + "-" + year;
            String selectedDate = dateStr + "/" + monthStr + "/" + year;


            if (SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER))
            {

                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");


				/*	if(sdf1.parse(selectedDate).after(sdf1.parse(max_cal_format)))
					{
					Utill.showDialg("You are allowed to book "+CourtName+"s only "+advacsedbookingdays+" days in advance ", getActivity());
					return;
					}*/

                if (getDateFromString(selectedDate).before(getDateFromString(formattedDate))) {


                    Calendar cal1 = (Calendar) currentTimeCal.clone();
                    cal1.add(Calendar.DAY_OF_MONTH, -7);

                    String formattedDate111 = sdf1.format(cal1.getTime());

                    //importent
                    if (sdf1.parse(selectedDate).after(sdf1.parse(formattedDate111))) {
                        datePicker.setText(Utill.formattedDateFromString("", "", finalDate));
                        finalDate = year + "-" + monthStr + "-" + dateStr;
                        calendarDate = finalDate;
                        cheackDate();
                        getReservation(finalDate1);
                        if (calendarDialogue != null) {
                            calendarDialogue.dismiss();
                        }
                    } else {
                        Utill.showDialg("You can view only past 7 days booking", getActivity());


                    }
                } else { //
                    Calendar selected_cal = Calendar.getInstance();
                    selected_cal.set(Integer.parseInt(selectedDate.split("/")[2]), Integer.parseInt(selectedDate.split("/")[1]) - 1, Integer.parseInt(selectedDate.split("/")[0]));
                    //selected_cal.set(Integer.parseInt(selectedDate.split("/")[0],Integer.parseInt(selectedDate.split("/")[1], Integer.parseInt(selectedDate.split("/")[2]))));
                    SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
                    //String formattedDate = df1.format(selected_cal.getTime());
                    datePicker.setText(Utill.formattedDateFromString("", "", df1.format(selected_cal.getTime())));
                    calendarDate = finalDate1;
                    getReservation(finalDate1);
                    hideOldDate();
                    calendarDialogue.dismiss();


                }
                return;
            }
            else
            {

                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");

                Calendar compareCal = (Calendar) currentTimeCal.clone();
                compareCal.add(Calendar.DAY_OF_MONTH, -31);
                String formattedDate111 = sdf1.format(compareCal.getTime());
                if (sdf1.parse(selectedDate).after(sdf1.parse(formattedDate111)))
                {
                    datePicker.setText(Utill.formattedDateFromString("", "", finalDate));
                    finalDate = year + "-" + monthStr + "-" + dateStr;
                    calendarDate = finalDate;
                    //currentTimeCal = (Calendar) selected_calender.clone();
                    cheackDate();
                    getReservation(finalDate);
                    if (calendarDialogue != null) {
                        calendarDialogue.dismiss();
                    }
                }
                else
                {
                    Utill.showDialg("You can view only past 1 month reservation", getActivity());

                }
            }




        } catch (ParseException e) {
            e.printStackTrace();
        }
    }




    int counter = 10 ;
    public boolean isWebServiceCalling = false ;
    public int lastCallItemCount = 10;

    public void getNextReservationList() {
        if (InternetConnection.isInternetOn(getActivity())) {
            //Utill.showProgress(getActivity());

            loaderProgrssBar.setVisibility(View.VISIBLE);
            isWebServiceCalling = true ;

            HashMap<String, String> params = new HashMap<String, String>();

            params.put("court_club_id", SessionManager.getUser_Club_id(getActivity()));

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String formatted = format1.format(selected_calender.getTime());

            params.put("court_reservation_date", formatted);
            params.put("court_reservation_user_id", SessionManager.getUser_id(getActivity()));
            params.put("counter", counter+"");

            ;
////////////////////
            aqQuery.ajax(WebService.myreservation, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                public void callback(String url, JSONObject object, com.androidquery.callback.AjaxStatus status) {
                    Utill.hideProgress();
                    Log.e("aaaaaaa", object + "");
                    loaderProgrssBar.setVisibility(View.GONE);
                    isWebServiceCalling = false ;

                    try {



                        JSONArray response_array = object.getJSONArray("response");


                        counter = object.getInt("counter");
                        for (int i = 0; i < response_array.length(); i++) {


                            JSONArray info_array = response_array.getJSONObject(i).getJSONArray("info");
                            lastCallItemCount = info_array.length();

                            for (int j = 0; j < info_array.length(); j++) {
                                JSONObject info_array_item = info_array.getJSONObject(j);

                                JSONArray booking_array = info_array_item.getJSONArray("book");


                                ArrayList<CourtBookBean> CourtBookList = new ArrayList<CourtBookBean>();

                                MyReservationBean my_reservation_nean = new MyReservationBean();
                                for (int k = 0; k < booking_array.length(); k++) {
                                    JSONObject booking_array_item = booking_array.getJSONObject(k);
                                    CourtBookBean court_book_bean = new CourtBookBean();
                                    court_book_bean.setCourt_id(booking_array_item.getString("court_id"));
                                    court_book_bean.setCourt_name(booking_array_item.getString("court_name"));
                                    court_book_bean.setCourt_reservation_start_time(booking_array_item.getString("court_reservation_start_time"));
                                    CourtBookList.add(court_book_bean);
                                }
                                my_reservation_nean.setBookingname(info_array_item.getString("bookingname"));
                                my_reservation_nean.setCourt_reservation_recursiveid(info_array_item.getString("court_reservation_recursiveid"));
                                my_reservation_nean.setReservedate(info_array_item.getString("reservedate"));

                                my_reservation_nean.setRecursivedates(info_array_item.getString("recursivedates"));
                                my_reservation_nean.setMemberbookedid(info_array_item.getString("memberbookedid"));
                                //.setReservedate(info_array_item.getString("reservedate"));

                                my_reservation_nean.setCourt_list(CourtBookList);
                                myReservationList.add(my_reservation_nean);
                            }
                        }


                        if (myReservationList.size() != 0) {
                            myReservationListAdapter.notifyDataSetChanged();

                        }


                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }

                    //Toast.makeText(getActivity(), "Size  "+myReservationList.size(), Toast.LENGTH_SHORT).show();

                }

                ;
            });

        } else {
            Utill.showDialg(getString(R.string.no_internet_connection), getActivity());
        }
    }



    public  void onclickListener(final View view)
    {
        Log.e("Simple Click", "Call");
        String index[] = view.getTag().toString().split("-");

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        String max_cal_format = sdf1.format(max_cal.getTime());
        String slectedDate = sdf1.format(selected_calender.getTime());

        try
        {

            CourtData  databean1 = mainList.get(Integer.parseInt(index[0])).get(Integer.parseInt(index[1]));

            String startTime = databean1.getCourt_start_time();

            int hour = Integer.parseInt(startTime.split(":")[0]);
            int minut = Integer.parseInt(startTime.split(":")[1]);
            // selected_calender.set(Calendar.HOUR_OF_DAY , hour);
            //  selected_calender.set(Calendar.MINUTE , minut);


        }
        catch (Exception e)
        {

        }
//Utill.compareTwoDateAndTime(selected_calender , max_cal)

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        Log.e("my time " , simpleDateFormat.format(max_cal.getTime())+" "+simpleDateFormat.format(selected_calender.getTime()));


        boolean bookingDialog = false ;

        if (Utill.isEqualDate(max_cal ,selected_calender))
        {
            if (Utill.compareTwoTime(max_cal , currentTimeCal))
            {
                bookingDialog =true ;
            }
        }



        try {
            if (bookingDialog || sdf1.parse(slectedDate).after(sdf1.parse(max_cal_format)) && SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER))
            {
                if (advacsedbookingdays == 1)/////a
                {
                    //Utill.showDialg("kYou are allowed to boo " + CourtName.toLowerCase() + " only " + advacsedbookingdays + " day in advance ", getActivity());

                }
                else
                {
                    // Utill.showDialg("You are allowed to book " + CourtName.toLowerCase() + " only " + advacsedbookingdays + " days in advance ", getActivity());

                }

                Calendar selected_calender_clone = (Calendar) selected_calender.clone();

                selected_calender_clone.add(Calendar.DATE , -advacsedbookingdays);

                SimpleDateFormat sdf2 = new SimpleDateFormat("MMMM dd, yyyy");

                Utill.showDialg("You can book this court starting from  " +sdf2.format(selected_calender_clone.getTime()) +" "+Utill.formattedDateFromString("HH:mm" , "hh:mm a" ,mainList.get(0).get(0).getCourt_start_time()), getActivity());


                return;
            }
        } catch (Exception e) {

        }


        if (oldDateMSG.getVisibility() == View.VISIBLE) {
            return;
        }


        databean1 = mainList.get(Integer.parseInt(index[0])).get(Integer.parseInt(index[1]));

        // Log.e("court id" , databean1.get+"   ");
        if (!Utill.isStringNullOrBlank(databean1.getMemberbookedid())||!Utill.isStringNullOrBlank(databean1.getCourt_reservation_id()) || delete)
        {





            if ((SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_DIRECTOR) == true || SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MOBILE_ADMIN) == true)
                    || databean1.getCourt_reservation_user_id().equalsIgnoreCase(SessionManager.getUser_id(mContext)))
            {

                String court_reservation_recursiveid = databean1.getRecursive_id();
//
                if (court_reservation_recursiveid.equals(databean1.getRecursive_id())|| deleteID.equals( databean1.getMemberbookedid()))
                {
                    if (delete )
                    {
                        delete = false;

                        resetReservationChart();
                        uniqueId = "";
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


                // resetReservationChart();

                Log.e("recursiveid" , court_reservation_recursiveid+"  ");

                if (Utill.isStringNullOrBlank(deleteID) == false || Utill.isStringNullOrBlank(court_reservation_recursiveid) == false)
                {

                    try {

                        if (databean != null)
                        {
                            hidedeleteReservationChart(databean , true);
                        }

                        databean = (CourtData) databean1.clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    try {

                        if (databean != null)
                        {
                            hidedeleteReservationChart(databean , true);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

////

                court_reservation_id = databean1.getCourt_reservation_id();
                deleteCourtData = databean1;
                if (delete )
                {
                    if (mmmm == false)
                    {

                        cancelBottomBtn.performClick();
                        deleteReservationChart(databean1);

                    }
                    mmmm = false ;
                }
            }
            else {
                //ShowUserMessage.showUserMessage(mContext, "Already Booked.");
            }
            //

            return;
        }
        uniqueId = "";

        //resetReservationChart();

        delete = false ;
        int l = Integer.parseInt(index[0]);
        int m = Integer.parseInt(index[1]);

        setDataInTable(m, l);
        checkSlotSelection();
    }

    FragmentBackResponseListener datepickerFromMyReservationListener = new FragmentBackResponseListener() {
        @Override
        public void onResponse(String date ,String reservationId)
        {
            View view = new View(getActivity());
            view.setTag(date);
            selectedCourtReservationId = reservationId ;
            left_layout.setVisibility(View.VISIBLE);
            right_layout.setVisibility(View.GONE);
            selectDateFromCalender(view);

        }

        @Override
        public void deleteSuccessFullyListener() {
            super.deleteSuccessFullyListener();
            myReservationList.clear();

            setReservation();

        }
    };

    public    ArrayList<ArrayList<CourtData>> transpose(ArrayList<ArrayList<CourtData>> table) {
        ArrayList<ArrayList<CourtData>> ret = new ArrayList<ArrayList<CourtData>>();
        final int N = table.get(0).size();
        for (int i = 0; i < N; i++) {
            ArrayList<CourtData> col = new ArrayList<CourtData>();
            for (List<CourtData> row : table) {
                col.add(row.get(i));
            }
            ret.add(col);
        }
        return ret;
    }

    public  Calendar getInstanse()
    {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);

        try
        {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            calendar.setTime(simpleDateFormat.parse(sessionManager.getCurrentTime()) );


        }
        catch (Exception e)
        {

        }


        return calendar ;

    }
}
