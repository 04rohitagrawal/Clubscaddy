package com.clubscaddy.fragment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.androidquery.AQuery;
import com.clubscaddy.Interface.FragmentBackResponseListener;
import com.clubscaddy.Interface.MyPermissionGrrantedListner;
import com.clubscaddy.custumview.CustomScrollView;

import com.clubscaddy.AsyTask.ConvertImagePathInBitmapStringTask;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.CustomPhotoGalleryActivity;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.ImageToStringConveterListener;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.EditEventGalaryAdapter;
import com.clubscaddy.Bean.EventBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.ImageDecoder;
import com.clubscaddy.custumview.SDCardMemory;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.UserPermision;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.custumview.CircularProgressBar;

@SuppressLint("ValidFragment")
public class EditEventFragment extends Fragment implements OnClickListener, OnItemClickListener {

    public static final int START_DATE = 1;
    public static final int END_DATE = 2;
    public static final int DEADLINE_DATE = 3;
    public static final int VEDIO_SELECT = 109;
    private static final int CAMERA_REQUEST = 11;
    private static final int SELECT_FILE = 12;
    private static final String tag = "MyCalendarActivity";
    private static final String dateTemplate = "MMMM yyyy";
    public static FragmentManager mFragmentManager;
    public static Fragment mFragment;
    public static boolean edit = false;
    public static boolean referesh = true;
    public static boolean localImage = false;
    public int SELECTED_VIEW = 0;
    String startDateStr = "";
    String endDateStr = "";
    String deadLineDateStr = "";
    String Tag = getClass().getName();
    Activity mContext;
    Calendar selected_calender;
    boolean imageselectedflag = false;
    // ImageView addadmin_profileImage;
    // ImageButton addadmin_pickfile;
    EditText eventNameET, eventCost, startDate, endDate, deadLineDate, descriptionET;
    Button adminList_addAdmin, addAttachMent;
    ArrayList<String> imageList;
    EditEventGalaryAdapter GalleryAdapter;
    ArrayList<String> url_list = new ArrayList<String>();
    ProgressDialog pDialog;
    // Camera Code
    Bitmap imageBitmap;
    int flag1;
    Bitmap photo, image;
    String absPath;
    int rotate;
    String filePath = "null";
    Uri cameraImagePath;
    int currentVersion = Build.VERSION.SDK_INT;
    String picturePath = Environment.getExternalStorageDirectory().getPath();
    String vedioPathPath = Environment.getExternalStorageDirectory().getPath();
    AQuery aq;
    SessionManager sessionManager;
    CheckBox enable_scoring;

    Gallery imageGallery;
    //View manage_event_tab_line;
    EventBean bean;
    LinearLayout edit_event_tab;

    LinearLayout manage_event_tab;
    //View edit_event_tab_line;
    TextView discription_textview_status;
    ArrayList<MemberListBean> eventModeratorList;


    LinearLayout add_modetoror_layout;

          CustomScrollView edit_tab_layout;

       CircularProgressBar circularProgressbar;


    CheckBox private_check_box;
    CheckBox public_check_box;


    ConvertImagePathInBitmapStringTask task ;


        LinearLayout deadline_date_layout;

    String description;
    OnClickListener addToBack = new OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                DirectorFragmentManageActivity.popBackStackFragment();
            } catch (Exception e) {
                ShowUserMessage.showUserMessage(mContext, e.toString());
            }

        }
    };
    OnClickListener addToAdmin = new OnClickListener() {

        @Override
        public void onClick(View v) {





            String startDateETStr = startDate.getText().toString().trim();
            String endDateETStr = endDate.getText().toString().trim();
            String deadLineDateETStr = deadLineDate.toString().trim();
            String coastStr = eventCost.getText().toString().trim();
            description = descriptionET.getText().toString().trim();
            if (eventNameET.getText().toString().equals("")) {
                ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.empty_first_name));
            } else if (Utill.isStringNullOrBlank(startDateETStr)) {
                Utill.showToast(mContext, "Please Select Start Date.");
            } else if (Utill.isStringNullOrBlank(endDateETStr)) {
                Utill.showToast(mContext, "Please Select End Date.");
            } else if (Utill.isStringNullOrBlank(deadLineDateETStr)) {
                Utill.showToast(mContext, "Please Select DeadLine Date.");
            } else
            if (Utill.isStringNullOrBlank(coastStr)) {
                Utill.showToast(mContext, "Please Enter Event Coast.");
            }
            else
            {


                ArrayList<String>fileimageList = new ArrayList<>();


                for (int i = 0 ;i < imageList.size() ;i++)
                {
                    if (!imageList.get(i).contains("http"))
                    {
                        fileimageList.add(imageList.get(i));
                    }
                }



                task = new ConvertImagePathInBitmapStringTask(getActivity(), fileimageList, new ImageToStringConveterListener() {
                    @Override
                    public void onSuccess(boolean status, ArrayList<String> bitmapString) {
                        circularProgressbar.setVisibility(View.GONE);
                        edit_tab_layout.setEnabled(true);
                    }

                    @Override
                    public void onUpdate(int update) {
                        circularProgressbar.setProgress(update);
                    }

                    @Override
                    public void onError() {
                        circularProgressbar.setVisibility(View.GONE);
                        edit_tab_layout.setEnabled(true);
                    }

                    @Override
                    public void onSuccess(boolean status, JSONArray imageOutPutJsonArray) {
                        circularProgressbar.setVisibility(View.GONE);
                        edit_tab_layout.setEnabled(true);
                        invalidateEvent(imageOutPutJsonArray);

                    }
                });

                task.execute();

                circularProgressbar.setVisibility(View.VISIBLE);
                 edit_tab_layout.setEnabled(false);


            }



        }
    };
    OnClickListener addToPickFile = new OnClickListener() {

        @Override
        public void onClick(View v) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pickImage();
                }
            });
        }
    };
    String startTimeText = "";
    AlertDialog calendarDialogue;
    // ArrayList<String>
    HashSet<String> selectedDateList;
    private TextView currentMonth;
    private Button selectedDayMonthYearButton;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private GridView calendarView;
    private GridCellAdapter adapter;
    private Calendar _calendar;
    private int month, year;
    OnClickListener dateClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.start_date:
                    SELECTED_VIEW = START_DATE;

                    break;
                case R.id.end_date:
                    SELECTED_VIEW = END_DATE;
                    break;
                case R.id.deadlineDate:
                    SELECTED_VIEW = DEADLINE_DATE;
                    break;

                default:
                    break;
            }
            showCalendar();

        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.e(Tag, "onAttach");
    }

    public void setInstanse(EventBean bean, ArrayList<MemberListBean> eventModeratorList) {
        this.bean = bean;
        this.eventModeratorList = eventModeratorList;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    TextView cost_tv ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.edit_event, container, false);
        Log.e(Tag, "onCreateView");
        selected_calender = Calendar.getInstance(Locale.ENGLISH);

        edit_event_tab = (LinearLayout) rootView.findViewById(R.id.edit_event_tab);
        //edit_event_tab_line = (View) rootView.findViewById(R.id.edit_event_tab_line);
        manage_event_tab = (LinearLayout) rootView.findViewById(R.id.manage_event_tab);
        //manage_event_tab_line = (View) rootView.findViewById(R.id.manage_event_tab_line);

        deadline_date_layout = (LinearLayout) rootView.findViewById(R.id.deadline_date_layout);
        //
        private_check_box = (CheckBox) rootView.findViewById(R.id.private_check_box);

        public_check_box = (CheckBox) rootView.findViewById(R.id.public_check_box);

        circularProgressbar = (CircularProgressBar) rootView.findViewById(R.id.circularProgressbar);
        edit_tab_layout = (CustomScrollView) rootView.findViewById(R.id.edit_tab_layout);

        discription_textview_status = (TextView) rootView.findViewById(R.id.discription_textview_status);
        private_check_box.setEnabled(false);
        public_check_box.setEnabled(false);


        if (bean.getEvent_type() == 1)
        {
            private_check_box.setChecked(false);
            public_check_box.setChecked(true);
            deadline_date_layout.setVisibility(View.VISIBLE);
        }
        else
        {
            private_check_box.setChecked(true);
            public_check_box.setChecked(false);
            deadline_date_layout.setVisibility(View.GONE);
        }



        add_modetoror_layout = (LinearLayout) rootView.findViewById(R.id.add_modetoror_layout);



        edit_event_tab.setOnClickListener(new ModifyEventTextClickListener());
        manage_event_tab.setOnClickListener(new ModifyEventTextClickListener());


        sessionManager = new SessionManager();
        mContext = getActivity();

        cost_tv = (TextView) rootView.findViewById(R.id.cost_tv);

        cost_tv.setText("Cost ("+sessionManager.getCurrencyCode(getActivity())+")");



        aq = new AQuery(getActivity());
        try {
            DirectorFragmentManageActivity.hideLogout();
            if (DirectorFragmentManageActivity.actionbar_titletext != null) {
                DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.edit_event));
            }
            if (DirectorFragmentManageActivity.backButton != null) {
                DirectorFragmentManageActivity.showBackButton();
                DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
            }

            eventNameET = (EditText) rootView.findViewById(R.id.event_name);
            eventNameET.setText(bean.getEvent_event_name());


            eventCost = (EditText) rootView.findViewById(R.id.event_cost);
            eventCost.setText(bean.getEvent_cost());


            adminList_addAdmin = (Button) rootView.findViewById(R.id.adminList_addAdmin);

            startDate = (EditText) rootView.findViewById(R.id.start_date);
            startDate.setText(/*AppConstants.getAppDate*/(bean.getEvent_startdate()));


            endDate = (EditText) rootView.findViewById(R.id.end_date);
            endDate.setText(/*AppConstants.getAppDate*/(bean.getEvent_finishdate()));

            deadLineDate = (EditText) rootView.findViewById(R.id.deadlineDate);
            deadLineDate.setText(/*AppConstants.getAppDate*/(bean.getEvent_signup_deadline_date()));


            deadLineDateStr = AppConstants.getDateInDDMMYYYYYWithServerSending(bean.getEvent_signup_deadline_date());




            startDate.setOnClickListener(dateClick);
            endDate.setOnClickListener(dateClick);
            deadLineDate.setOnClickListener(dateClick);

            addAttachMent = (Button) rootView.findViewById(R.id.add_attachment);
            imageGallery = (Gallery) rootView.findViewById(R.id.image_gallery);

            descriptionET = (EditText) rootView.findViewById(R.id.event_description);

            adminList_addAdmin.setOnClickListener(addToAdmin);

            addAttachMent.setOnClickListener(addToPickFile);
            imageList = new ArrayList<String>();
            url_list = bean.getEvent_attach_url();
            GalleryAdapter = new EditEventGalaryAdapter(getActivity(), url_list, imageList);

            //imageGallery.setAdapter(GalleryAdapter);
            //imageGallery.setOnItemClickListener(this);
            // addadmin_ratingBar.on

            setDataToView(bean);


            enable_scoring = (CheckBox) rootView.findViewById(R.id.enable_scoring);

            if (sessionManager.getClub_score_show(getActivity()) == 2) {
                enable_scoring.setVisibility(View.INVISIBLE);
            } else {
                enable_scoring.setVisibility(View.VISIBLE);
                if (bean.getEvent_score() == 1)
                {
                    enable_scoring.setChecked(true);
                }
                else
                {
                    enable_scoring.setChecked(false);
                }


            }

        } catch (Exception e) {
            ShowUserMessage.showUserMessage(mContext, e.toString());
        }
        DirectorFragmentManageActivity.showLogoutButton();
        return rootView;
    }

    String getFormattendDate(String date) {
        String s[];
        try {
            s = date.split("-");
            date = s[2] + "/" + s[1] + "/" + s[0];
        } catch (Exception e) {

        }
        return date;
    }

    void setDataToView(EventBean bean) {
        eventNameET.setText(bean.getEvent_event_name());
        eventCost.setText(bean.getEvent_cost());
        //	Toast.makeText(getActivity(), bean.getEvent_event_name(), 1).show();
        startDate.setText("" + getFormattendDate(bean.getEvent_startdate()));
        deadLineDate.setText("" + getFormattendDate(bean.getEvent_signup_deadline_date()));
        endDate.setText("" + getFormattendDate(bean.getEvent_finishdate()));


        startDateStr = bean.getEvent_startdate();
        endDateStr = bean.getEvent_finishdate();


        imageList.addAll(bean.getEvent_attach_url());
        GalleryAdapter = new EditEventGalaryAdapter(getActivity(), url_list, imageList);
        imageGallery.setAdapter(GalleryAdapter);


        imageGallery.setOnItemClickListener(this);

        descriptionET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.e("action", event.getAction() + "");


                //	Toast.makeText(getActivity() ,"action  " + event.getAction(),Toast.LENGTH_SHORT ).show();


                if (1 == event.getAction()) {
                    edit_tab_layout.setEnableScrolling(true);

                } else {

                    edit_tab_layout.setEnableScrolling(false);


                }

                return false;
            }
        });
        descriptionET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                discription_textview_status.setText(s.toString().length()+"/1000");
            }
        });
        descriptionET.setText("" + bean.getEventDescription());
        // opt = UniversalImageLoaderHelper.setImageOptions();
        // image_load = ImageLoader.getInstance();
        // image_load.init(ImageLoaderConfiguration.createDefault(mContext));

		/*
         * Drawable d = getResources().getDrawable(R.drawable.default_pic); if
		 * (!localImage) { if(bean.getEvent_attach_url()!=null &&
		 * bean.getEvent_attach_url().size()>0)
		 * image_load.displayImage(bean.getEvent_attach_url().get(0),
		 * addadmin_profileImage, opt, new CustomImageLoader(progress, mContext,
		 * d, false)); } else { localImage = false; }
		 */

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(Tag, "onStart");
        if (!edit && referesh) {
            referesh = false;

        }
    }

    void clearFields1() {
        eventNameET.setText("");
        eventCost.setText("");
        startDate.setText("");
        endDate.setText("");
        deadLineDate.setText("");
        // addadmin_profileImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_pic));

        descriptionET.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (edit && bean != null) {
            setDataToView(bean);
            if (DirectorFragmentManageActivity.actionbar_titletext != null) {
                DirectorFragmentManageActivity.updateTitle("Edit Event");
                // adminList_addAdmin.setText("Update Admin");

            }
        }
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
        setSoftInputMode();
    }

    ;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(Tag, "onDestroyView");
        try {
            AppConstants.hideSoftKeyboard(getActivity());
        } catch (Exception e) {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(Tag, "onDetach");
    }

    private void invalidateEvent(JSONArray all_path_json) {
          {
            if (Utill.isNetworkAvailable(getActivity())) {
                HashMap<String, Object> map = new HashMap<String, Object>();



                pDialog = new ProgressDialog(mContext);
                pDialog.setCancelable(false);
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.setIndeterminate(true);
                pDialog.setMessage("Please Wait...");
                pDialog.show();


                try {

                    map.put("user_type", ("5"));
                   // map.put("user_club_id", (SessionManager.getUser_Club_id(mContext)));


                    //JSONObject old_path_json_array = new JSONObject();
                    JSONArray oldimage_path_json_array = new JSONArray();



                    for (int i = 0; i < url_list.size(); i++) {
                        JSONObject imageJson = new JSONObject();
                        try {
                            imageJson.put("image", url_list.get(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        oldimage_path_json_array.put(imageJson);
                    }
                     String allPath = all_path_json.toString();
                    map.put("event_attach_url", (allPath));

                    String web = WebService.addEvent;
                    if (edit && bean != null) {
                        map.put("event_id", (bean.getEvent_id()));
                        web = WebService.editEvent;
                    }
                    {


                        String user_id = bean.getEvent_user_id();

                        map.put("event_id", (bean.getEvent_id()));


                        map.put("oldurl", oldimage_path_json_array);
                        //	Toast.makeText(getActivity(), "User id "+user_id, 1).show();
                        map.put("event_club_id", ("" + SessionManager.getUser_Club_id(mContext)));
                        map.put("event_user_id", (user_id));
                        map.put("event_event_name", (eventNameET.getText().toString()));

                        //map.put("event_startdate", (startDateStr));
                        //map.put("event_finishdate", (endDateStr));
                        map.put("event_cost", (eventCost.getText().toString()));
                        map.put("user_type", (SessionManager.getUser_type(mContext)));
                        map.put("description", ("" + description));


                        if (bean.getEvent_type() == 1)
                        {
                            map.put("event_signup_deadline_date", (deadLineDateStr));
                        }

                        map.put("event_type",bean.getEvent_type()+"");


                        if (sessionManager.getClub_score_show(getActivity()) == 2) {
                            map.put("score", "2");
                        } else {
                            if (enable_scoring.isChecked()) {
                                map.put("score", ("1"));
                            } else {
                                map.put("score", ("2"));
                            }

                        }


                        HttpRequest httpRequest = new HttpRequest(getActivity());
                        httpRequest.getResponseWihhoutPd(getActivity(), WebService.editevent, map, new OnServerRespondingListener(getActivity()) {
                            @Override
                            public void onSuccess(JSONObject jsonObj) {

                                pDialog.dismiss();





                                try
                                {

                                    if (jsonObj.getBoolean("status"))
                                    {
                                        AlertDialog alertDialog = new AlertDialog.Builder(
                                                getActivity()).create();

                                        // Setting Dialog Title
                                        alertDialog.setTitle(SessionManager.getClubName(getActivity()));

                                        // Setting Dialog Message
                                        alertDialog.setMessage(jsonObj.getString("message"));

                                        // Setting Icon to Dialog

                                        alertDialog.setCancelable(false);
                                        // Setting OK Button
                                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Write your code here to execute after dialog closed




                                                Calendar calendar = Calendar.getInstance();
                                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/M/dd");
                                                try {
                                                    calendar.setTime(simpleDateFormat.parse(deadLineDateStr));
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }


                                                bean.setEvent_signup_deadline_date(AppConstants.getAppDate(calendar));
                                                bean.setEvent_event_name(eventNameET.getText().toString());
                                                bean.setEvent_cost(eventCost.getText().toString());

                                                bean.setEvent_attach_url(imageList);
                                                bean.setThumnailList(imageList);
                                                bean.setEventDescription(description);


                                                dialog.cancel();
                                                getActivity().getSupportFragmentManager().popBackStack();

                                            }
                                        });

                                        // Showing Alert Message
                                        alertDialog.show();
                                    }
                                    else
                                    {
                                        AlertDialog alertDialog = new AlertDialog.Builder(
                                                getActivity()).create();

                                        // Setting Dialog Title
                                        alertDialog.setTitle(SessionManager.getClubName(getActivity()));

                                        // Setting Dialog Message
                                        alertDialog.setMessage(jsonObj.getString("message"));
                                        alertDialog.setCancelable(false);
                                        // Setting Icon to Dialog


                                        // Setting OK Button
                                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Write your code here to execute after dialog closed

                                                dialog.cancel();


                                            }
                                        });

                                        // Showing Alert Message
                                        alertDialog.show();
                                    }

                                }
                                catch (Exception e)
                                {

                                }


















                            }

                            @Override
                            public void onNetWorkError() {
                                super.onNetWorkError();
                                pDialog.dismiss();
                            }
                        });




					/*	AQuery aQuery = new AQuery(getActivity());
						aQuery.ajax(WebService.editEvent, map, JSONObject.class, new AjaxCallback<JSONObject>()
								{
							@Override
							public void callback(String url, JSONObject jsonObj, AjaxStatus status) {
								// TODO Auto-generated method stub
								super.callback(url, jsonObj, status);
								pDialog.dismiss();

							}
								});*/




                    }
                    // Utill.showProgress(mContext);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Utill.showNetworkError(mContext);
            }
        }
    }

    public void openGallery() {
        try {

            Intent intent = new Intent(getActivity(), CustomPhotoGalleryActivity.class);//item
            intent.putExtra("item", imageList.size() + "");
            getActivity().startActivityForResult(intent, SELECT_FILE);
        } catch (Exception e) {
            // new SendErrorAsync(mContext, e).execute();
        }
    }

    ArrayList<String> cameraPermissionArrayList ;

    private void pickImage() {
        cameraPermissionArrayList = new ArrayList<>();
        cameraPermissionArrayList.add(Manifest.permission.CAMERA);
        cameraPermissionArrayList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        cameraPermissionArrayList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        final CharSequence[] items = {"Take from camera", "Upload from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select pictures");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int position) {
                try {
                    if (items[position].equals("Take from camera"))
                    {


                        if (imageList.size() < 10)
                        {
                            openCameraByIntent();

                        } else {
                            Utill.showDialg("No more image can be select", getActivity());
                        }

                    } else if (items[position].equals("Upload from gallery")) {
                        //	openVedio();
                        openGallery();
                    } else {
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    ShowUserMessage.showUserMessage(mContext, e.toString());
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public void openCameraByIntent()
    {
        UserPermision.updatePermission(mContext, cameraPermissionArrayList, new MyPermissionGrrantedListner() {
            @Override
            public void isAllPermissionGranted(boolean isPermissionGranted, int code) {

                if (isPermissionGranted)
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File sdCardDir = SDCardMemory.createSDCardDir(mContext);
                    File mFile = SDCardMemory.createImageSubDir(mContext, sdCardDir);
                    File file = new File(mFile, "tennis-img" + System.currentTimeMillis() + ".jpg");
                    absPath = file.getAbsolutePath();
                    Uri photoURI = FileProvider.getUriForFile(mContext,
                            getString(R.string.file_provider_authority),
                            file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    savePref(absPath);
                    ((Activity) mContext).startActivityForResult(intent,
                            CAMERA_REQUEST);

                }
            }
        });
    }

    private void savePref(String str) {
        SharedPreferences sf = mContext.getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString("imageURI", str);
        editor.commit();
    }

    private String getImageURI() {
        SharedPreferences sf = mContext.getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
        String uri = sf.getString("imageURI", null);
        return uri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == VEDIO_SELECT && resultCode == getActivity().RESULT_OK) {

                if (data != null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = mContext.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    Log.e("PicturePath 1 = ", picturePath);
                    image = ImageDecoder.decodeFile(picturePath);
                    if (image != null) {/*
						image = Bitmap.createBitmap(image);
						// image = Bitmap.createScaledBitmap(image, 100,
						// 100, false);
						ByteArrayOutputStream bytes = new ByteArrayOutputStream();
						image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
						Bitmap bm = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
						// profilePic.setImageBitmap(bm);
						imageBitmap = bm;
						// profileImageView.setImageBitmap(imageBitmap);
						newsImage.setImageBitmap(imageBitmap);
						localImage = true;
						// addadmin_profileImage.setImageBitmap(imageBitmap);
						// uploadBT.setBackgroundColor(Color.GREEN);
						// event_invity_eventpic.setImageBitmap(imageBitmap);
						Log.e("PicturePath 2 = ", picturePath);
						// extention=picturePath.split("\\.");
						// imageselectedflag=true;
						filePath = picturePath;
						// addFeedView();
					*/
                    } else {
                        ShowUserMessage.showUserMessage(mContext, "Wrong Path");
                        // imageselectedflag = false;
                    }
                    cursor.close();
                }

            } else if (requestCode == SELECT_FILE && resultCode == getActivity().RESULT_OK) {

                if (flag1 == 0) {
                    if (data != null) {
                        String uri = data.getStringExtra("uri");
                        String absUri[] = uri.split("\\|");
                        String s = data.getStringExtra("data");
                        String abs[] = s.split("\\|");
                        int totalUnselectedItem = 0 ;

                        for (int i = 0; i < abs.length; i++) {
                            String str = "";
                            str = abs[i];
                            String imageUri = abs[i];
                            imageUri = imageUri.replaceAll("%20", " ");




                            Bitmap bitmap ;

                            BitmapFactory.Options options = new BitmapFactory.Options();
                            try
                            {
                                bitmap = BitmapFactory.decodeFile(imageUri );

                            }
                            catch (OutOfMemoryError e)
                            {
                                options.inSampleSize = 8;
                                bitmap = BitmapFactory.decodeFile(imageUri ,options);

                            }
                            catch (Exception e)
                            {
                                options.inSampleSize = 8;
                                bitmap = BitmapFactory.decodeFile(imageUri ,options);


                            }


                            if (bitmap.getWidth()<512 || bitmap.getHeight() < 512)
                            {
                                totalUnselectedItem++ ;
                            }
                            else
                            {
                                imageList.add(imageUri);

                            }




                           // bean.getEvent_attach_url().add(imageUri);

                        }


                        if (totalUnselectedItem != 0)
                        {
                            Utill.showDialg(totalUnselectedItem+" images are in wrong format Please upload correct format (Greater than 512*512)" , getActivity());
                        }
                        //	aaaa
                        GalleryAdapter.notifyDataSetChanged();
                    }
                }

            } else if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK) {
                try {
                    String URI = getImageURI();
                    try {
                        File file = new File(URI);
                        Bitmap bitmap = BitmapFactory.decodeFile(URI);


                        if (bitmap.getWidth()<512 || bitmap.getHeight() < 512)
                        {
                            Utill.showDialg("Images are in wrong format Please upload correct format (Greater than 512*512)" , getActivity());
                        }
                        else
                        {
                            imageList.add(file.getAbsolutePath());
                            GalleryAdapter.notifyDataSetChanged();

                        }


                    } catch (Exception e) {
                        ShowUserMessage.showUserMessage(mContext, e.toString());
                    }
                } catch (Exception e) {
                    ShowUserMessage.showUserMessage(mContext, e.toString());
                }

            }


        } catch (Exception e) {
            ShowUserMessage.showUserMessage(mContext, e.toString());
        }
    }



    public void onSaveInstanceState(Bundle outState) {
        // outState.put
        // ShowUserMessage.showUserMessage(mContext, "onSaveInstance");
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        // ShowUserMessage.showUserMessage(mContext, "onSaveInstance restore");
        super.onViewStateRestored(savedInstanceState);
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
		/*
		 * if(CalendarType == RECURRENT_DATE){ TextView cancel = (TextView)
		 * v.findViewById(R.id.cancel); TextView done = (TextView)
		 * v.findViewById(R.id.done); done.setVisibility(View.VISIBLE);
		 * cancel.setOnClickListener(new OnClickListener() {
		 *
		 * @Override public void onClick(View v) { selectedDateList = new
		 * HashSet<String>(); calendarDialogue.dismiss(); } });
		 *
		 * done.setOnClickListener(new OnClickListener() {
		 *
		 * @Override public void onClick(View v) { String msg = ""; for(int
		 * i=0;i<selectedDateList.size();i++){ msg = msg +
		 * selectedDateList.toString(); } Utill.showToast(mContext,
		 * ""+selectedDateList.toString()); calendarDialogue.dismiss();
		 * dateCountTV.setText("You have selected "+selectedDateList.size()+
		 * " days to repeat the trip."); } }); }
		 */
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

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
        Intent intent = new Intent(getActivity(), EventFullImageViewActivity.class);
        intent.putExtra("path_list", imageList);
        intent.putExtra("pos", String.valueOf(pos));
        startActivity(intent);
    }

    void showDeleteConfirmationDialogu(final int position) {

        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.delete_confirmation, null);
        TextView doneTV, cancelTV, headerTV;
        doneTV = (TextView) v.findViewById(R.id.done);
        cancelTV = (TextView) v.findViewById(R.id.cancel);
        headerTV = (TextView) v.findViewById(R.id.title);
        headerTV.setText("Do you want to remove this image?");
        doneTV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                imageList.remove(position);
                GalleryAdapter.notifyDataSetChanged();
                alertDialog.cancel();
            }
        });
        cancelTV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.cancel();

            }
        });

        alertDialog.setView(v);
        alertDialog.show();

    }

    private int imageRoated(File file) {
        //Bitmap bitmap = null;
        int rotate = 0;
        try {
            ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    //bitmap = rotateImage(file,rotate);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    //bitmap = rotateImage(file,rotate);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    //bitmap = rotateImage(file,rotate);
                    break;
                case 1:
                    rotate = 0;
                    //	bitmap = rotateImage(file,rotate);
                    break;

                case 2:
                    rotate = 0;
                    //bitmap = rotateImage(file,rotate);
                    break;
                case 4:
                    rotate = 180;
                    //bitmap = rotateImage(file,rotate);
                    break;

                case 0:
                    rotate = 0;
                    //bitmap = rotateImage(file,rotate);
                    break;
            }
        } catch (Exception e) {
            Log.e("Exception", "" + e.toString());
        }

        return rotate;
    }

    public class AddAdminListener {
        public void onSuccess(String msg) {
            ShowUserMessage.showUserMessage(mContext, msg);
            DirectorFragmentManageActivity.popBackStackFragment();
            Utill.hideProgress();
        }

        public void onError(String message) {
            ShowUserMessage.showUserMessage(mContext, message);
            Utill.hideProgress();
        }
    }

    // Inner Class
    @SuppressLint("SimpleDateFormat")
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
        boolean isCalederdissmiss;
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
                Log.d(currentMonthName, String.valueOf(i) + " " + getMonthAsString(currentMonth) + " " + yy);
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

        /**
         * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
         * ALL entries from a SQLite database for that month. Iterate over the
         * List of All entries, and get the dateCreated, which is converted into
         * day.
         *
         * @param year
         * @param month
         * @return
         */
        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year, int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();

            return map;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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

            Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
            String[] day_color = list.get(position).split("-");
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
            try {
                gridcell.setBackground(getResources().getDrawable(R.drawable.calendar_tile_small));
            } catch (NoSuchMethodError e) {

            }

            gridcell.setTag(theday + "-" + themonth + "-" + theyear);
            Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear);






            if (day_color[1].equals("GREY")) {
                gridcell.setTextColor(getResources().getColor(R.color.gray_color));

            }
            if (day_color[1].equals("WHITE")) {
                gridcell.setTextColor(getResources().getColor(R.color.black_color));
            }
            String currentDate = day_color[0]+"-"+day_color[2]+"-"+day_color[3];
            SimpleDateFormat currentDateformat = new SimpleDateFormat("d-MMMM-yyyy");
            String systemCurrentDate = currentDateformat.format(Calendar.getInstance().getTime());

            if (day_color[1].equals("BLUE") )
            {
                Log.e("current date" , systemCurrentDate+" "+currentDate);
                if (systemCurrentDate.equals(currentDate))
                {
                    gridcell.setTextColor(getResources().getColor(R.color.white_color));
                    gridcell.setBackground(getResources().getDrawable(R.drawable.calendar_bg_orange));
                }
                else
                {
                    gridcell.setTextColor(getResources().getColor(R.color.black_color));
                    //  gridcell.setBackground(getResources().getDrawable(R.drawable.calendar_bg_orange));
                }

            }
            else
            {

                if(selected_calender != null)
                {
                    Log.e("Selected date", "with");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-M-yyyy");
                    String  mySelectedDate = simpleDateFormat.format(selected_calender.getTime());
                    if (mySelectedDate.equals(date))
                    {
                        gridcell.setBackground(getResources().getDrawable(R.color.blue_header));
                    }
                }
            }




            return row;
        }

        @Override
        public void onClick(View view) {
            String date_month_year = (String) view.getTag();
            isCalederdissmiss = true;
            selectedDayMonthYearButton.setText("Selected: " + date_month_year);
            Log.e("Selected date", date_month_year);

            try {
                Date parsedDate = dateFormatter.parse(date_month_year);

                // parsedDate.
                Calendar c = Calendar.getInstance();

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

                String finalDate = dateStr + "-" + monthStr + "-" + year;
                String selectedDate = dateStr + "/" + monthStr + "/" + year;
                // ShowUserMessage.showUserMessage(mContext,selectedDate +
                // formattedDate);

				/*
				 * if(getDateFromString(formattedDate).compareTo(getDateFromString
				 * (selectedDate))<0){
				 * ShowUserMessage.showUserMessage(mContext,"Date passed");
				 * return; }
				 */
                finalDate = year + "-" + monthStr + "-" + dateStr;
                switch (SELECTED_VIEW) {
                    case START_DATE:

                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            Calendar c1 = Calendar.getInstance();
                            //c1.add(Calendar.DATE, 1);
                            Date date1 = sdf.parse(c1.get(Calendar.DAY_OF_MONTH) + "-" + (c1.get(Calendar.MONTH) + 1) + "-" + c1.get(Calendar.YEAR));
                            Date date2 = sdf.parse(selectedDate.toString().replace("/", "-"));
                            if (date1.compareTo(date2) >= 0) {
                                isCalederdissmiss = false;
                                //startDate.setText("");
                                //endDate.setText("");
                                //Toast.makeText(getActivity(), "Start date must be grat", 1).show();
                                //System.out.println("Date1 is after Date2");
                            } else {
                                startDate.setText(AppConstants.getAppDate(c));
                                endDate.setText("");
                                deadLineDate.setText("");
                                startDateStr = finalDate;
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                            //	Toast.makeText(mContext, e.getMessage(),1).show();
                        }

					/*try
					{
					 //	Toast.makeText(getActivity(), "Start date must be grat "+endDate.getText().toString().replace("/", "-"), 1).show();

				    	*/


                        break;
                    case END_DATE:
                        try {
                            //	Toast.makeText(getActivity(), "Start date must be grat "+endDate.getText().toString().replace("/", "-"), 1).show();

                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            Date date1 = sdf.parse(AppConstants.getDateInDDMMYYYYY(startDate.getText().toString()));
                            Date date2 = sdf.parse(selectedDate.toString().replace("/", "-"));
                            if (date1.compareTo(date2) > 0) {
                                isCalederdissmiss = false;
                            } else {
                                endDate.setText(AppConstants.getAppDate(c));
                                //deadLineDate.setText("");
                                endDateStr = finalDate;
                            }
			        	/*
			        	Toast.makeText(getActivity(), "Start date must be grat "+date1.compareTo(date2), 1).show();

						*/
                        } catch (Exception e) {
                            // TODO: handle exception
                            //	Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }


                        break;
                    case DEADLINE_DATE:

                        try {
                            //	Toast.makeText(getActivity(), "Start date must be grat "+endDate.getText().toString().replace("/", "-"), 1).show();

                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            //Date date1 = sdf.parse((AppConstants.getDateInDDMMYYYYY(startDate.getText().toString())));
                            Date date2 = sdf.parse(selectedDate.toString().replace("/", "-"));
                            Calendar c1 = Calendar.getInstance();
                            Date date3 = sdf.parse(c1.get(Calendar.DAY_OF_MONTH) + "-" + (c1.get(Calendar.MONTH) + 1) + "-" + c1.get(Calendar.YEAR));


                            try {

                                Calendar mydate = Calendar.getInstance();

                                Calendar selectedTime = (Calendar) mydate.clone();
                                Calendar currentTime = (Calendar) mydate.clone();

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                selectedTime.setTime(simpleDateFormat.parse(selectedDate));
                                currentTime.setTime(simpleDateFormat.parse(currentTime.get(Calendar.DATE) + "/" + (currentTime.get(Calendar.MONTH) + 1) + "/" + currentTime.get(Calendar.YEAR)));



                                if (Utill.compareDates(selectedTime , currentTime)) {

                                    String dateArray[] = selectedDate.split("/");


                                    deadLineDateStr = dateArray[2] + "/" + (Integer.parseInt(dateArray[1])) + "/" + dateArray[0];
                                    isCalederdissmiss = true;
                                    deadLineDate.setText(AppConstants.getAppDateFromCal(selectedTime));
                                } else {
                                    deadLineDate.setText("");
                                    isCalederdissmiss = false;
                                }
                                selected_calender.set(selectedTime.get(Calendar.YEAR) , selectedTime.get(Calendar.MONTH),selectedTime.get(Calendar.DATE));  ;


                            } catch (Exception e) {
                                Log.e("Exceptiop", e.getMessage());
                                // TODO: handle exception
                                //Toast.makeText(getActivity(), e.getMessage(), 1).show();
                            }


                        } catch (Exception e) {
                            // TODO: handle exception
                            //	Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        break;

                    default:
                        break;
                }

                if (calendarDialogue != null && isCalederdissmiss) {
                    calendarDialogue.dismiss();
                }
                isCalederdissmiss = true;

            } catch (ParseException e) {
                e.printStackTrace();
            }
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

    public class EditEventTaskListener {
        public void onSuccess(String msg) {
            bean.setEvent_attach_url(imageList);
            bean.setEventDescription(description);
            //description
            getActivity().getSupportFragmentManager().popBackStack();
        }

        public void onEarror(String msg) {

        }
    }

    /* RelativeLayout edit_tab_layout;
        ScrollView add_modetoror_layout;*/

    public class ModifyEventTextClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {


            if (R.id.manage_event_tab == v.getId()) {


                edit_tab_layout.setVisibility(View.GONE);

                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


                add_modetoror_layout.setVisibility(View.VISIBLE);

                //edit_event_tab_line.setVisibility(View.GONE);
               // manage_event_tab_line.setVisibility(View.VISIBLE);
                 ;
                AddModeratorFragment fragment = new AddModeratorFragment();
                fragment.setArgument(bean.getEvent_id(), bean.getEvent_club_id(), eventModeratorList, bean ,getActivity());

                fragment.onCreateView(add_modetoror_layout);




                if (DirectorFragmentManageActivity.actionbar_titletext != null) {
                    DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.change_moderator));
                }
                edit_event_tab.setBackgroundColor(getResources().getColor(R.color.unselected_tab));
                manage_event_tab.setBackgroundDrawable(getResources().getDrawable(R.drawable.active_tb_bg));
                Utill.hideKeybord(getActivity());


            }
            if (R.id.edit_event_tab == v.getId())
            {


                edit_tab_layout.setVisibility(View.VISIBLE);
                add_modetoror_layout.setVisibility(View.GONE);
               setSoftInputMode();

                if (DirectorFragmentManageActivity.actionbar_titletext != null) {
                    DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.edit_event));
                }
                Utill.hideKeybord(getActivity());

                manage_event_tab.setBackgroundColor(getResources().getColor(R.color.unselected_tab));
                edit_event_tab.setBackgroundDrawable(getResources().getDrawable(R.drawable.active_tb_bg));

               /* EditEventFragment fragment = new EditEventFragment();
                fragment.setInstanse(bean, eventModeratorList);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "fragment").commit();
                AddEventFragment.edit = false;*/
            }


        }
    }














    public void setSoftInputMode() {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }





}