package com.clubscaddy.fragment;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cleveroad.adaptivetablelayout.AdaptiveTableLayout;
import com.cleveroad.adaptivetablelayout.OnItemClickListener;
import com.clubscaddy.Adapter.ClassReservationGridAdapter;
import com.clubscaddy.Bean.ClassReservation;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.DialogBoxButtonListner;
import com.clubscaddy.Interface.FragmentBackResponseListener;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.Interface.TableItemClickListener;
import com.clubscaddy.R;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.SqlListe;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by administrator on 4/12/17.
 */

public class ClassReservationGridFragment extends Fragment implements View.OnClickListener , OnItemClickListener
{
    @Override
    public void onRowHeaderClick(int row) {

    }

    @Override
    public void onColumnHeaderClick(int column) {

    }

    @Override
    public void onLeftTopHeaderClick() {

    }

    View convertView ;
    HttpRequest httpRequest ;
    FragmentBackResponseListener updateclassListListener ;
    Bundle bundle ;

    AdaptiveTableLayout tableLayout;

    Calendar currentTime ;



    TextView dateDurationtv ;

    Calendar startDate ;
    Calendar endDate ;
    int totalSelectedItem = 0;
    LinearLayout bottomsLayout;


    int rowHeight = 150;
    int HeaderColumnHeigh =100;
    int ColumnWidth =150;

    TextView cancelBtn ;
    TextView classDeleteBtn ;
    TextView addParticipantBtn ;

    boolean isFullClassVailableIList ;


    SessionManager sessionManager ;

    ImageView priviousIv ;

    SqlListe sqlListe ;

    ImageView nextIv ;

    ArrayList<ArrayList<ClassReservation>> classReservationArrayList ;


    ArrayList<ClassReservation> selectedclassReservation ;



    ShowUserMessage showUserMessage ;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {

        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.class_grid_layout , null);
        bundle = getArguments() ;


        sessionManager = new SessionManager(getActivity());

        currentTime = Calendar.getInstance(Locale.ENGLISH);

        sqlListe = new SqlListe(getContext());


        totalSelectedItem = 0;


        showUserMessage = new ShowUserMessage(getActivity());


        startDate = Calendar.getInstance(Locale.ENGLISH);
        startDate.add( Calendar.DAY_OF_WEEK, -(startDate.get(Calendar.DAY_OF_WEEK)-1));

        priviousIv = (ImageView) convertView.findViewById(R.id.privious_iv);
        nextIv = (ImageView) convertView.findViewById(R.id.next_iv);
        priviousIv.setOnClickListener(leftRightBtnClickListener);
        nextIv.setOnClickListener(leftRightBtnClickListener);



        showUserMessage = new ShowUserMessage(getActivity());

         bottomsLayout = (LinearLayout) convertView.findViewById(R.id.bottoms);
         cancelBtn = (TextView) convertView.findViewById(R.id.cancel_btn);
         classDeleteBtn = (TextView) convertView.findViewById(R.id.class_delete_btn);
         addParticipantBtn = (TextView) convertView.findViewById(R.id.add_participant_btn);
         cancelBtn.setOnClickListener(bottomItemClickListener);
        classDeleteBtn.setOnClickListener(bottomItemClickListener);
        addParticipantBtn.setOnClickListener(bottomItemClickListener);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM-dd-yyyy");



        tableLayout = (AdaptiveTableLayout) convertView.findViewById(R.id.tableLayout);



        rowHeight = AppConstants.getDeviceHeight(getActivity()) /8 ;
        HeaderColumnHeigh  = AppConstants.getDeviceHeight(getActivity()) /10 ;
        ColumnWidth = (int)(AppConstants.getDeviceWidth(getActivity()) /2.5) ;






        classReservationArrayList = new ArrayList<>();

       dateDurationtv = (TextView) convertView.findViewById(R.id.date_duration_tv);









        httpRequest = new HttpRequest(getActivity());

        DirectorFragmentManageActivity.updateTitle("Class Reservation");

        updateclassListListener = (FragmentBackResponseListener)
                bundle.getSerializable("updateclassListListener");

        getDataFromServer();

        if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_DIRECTOR)||SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MOBILE_ADMIN))
        {
            DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.VISIBLE);
            DirectorFragmentManageActivity.delete_all_btn.setText("Add Class");
            DirectorFragmentManageActivity.delete_all_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddClassFragment addClassifiedFragment = new AddClassFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("updateclassListListener" , updateclassListListener);


                    addClassifiedFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_frame , addClassifiedFragment , "").addToBackStack("").commit();

                }
            });

        }
        return convertView;
    }


    HashSet<String> param ;

    public View.OnClickListener bottomItemClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            if (v.getId() == R.id.cancel_btn)
            {
                classReservationGridAdapter.firstTime = true ;
                classReservationGridAdapter.isUserSignUp = true ;

               /* classReservationArrayList.remove(1);
                classReservationGridAdapter.notifyDataSetChanged();
                classReservationGridAdapter.setOnItemClickListener(ClassReservationGridFragment.this);
*/
                 cancelAppReservation();
            }

            if (v.getId() == R.id.class_delete_btn)
            {


                showUserMessage.showDialogBoxWithYesNoButton("Are you sure you want to delete this classes", new DialogBoxButtonListner() {
                    @Override
                    public void onYesButtonClick(DialogInterface dialog) {
                        String ids = getSelectedIds();

                        deleteClassFromServer(ids);

                    }
                });

            }


////////////////////////////
            if (v.getId() == R.id.add_participant_btn)
            {
                if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER) == false)
                {


                    try {

                    }
                    catch (Exception e)
                    {

                    }

                    AddPartivipentClass addPartivipentClass = new AddPartivipentClass();
                    Bundle bundle = new Bundle();
                    bundle.putString("class_detail_ids" , getSelectedIds());
                    bundle.putSerializable("class_list" , getSelectedReservationItemList());
                    bundle.putSerializable("updateclassListListener" , fragmentBackResponseListener);

                    bundle.putBoolean("isFullClassVailableIList" ,isFullClassVailableIList);
                    addPartivipentClass.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_frame , addPartivipentClass , "").addToBackStack("").commit();

                    getSelectedClassIds();

                }
                else
                {
                   if (classReservationGridAdapter.isUserSignUp == true )
                   {


                       HashSet<String> classIds = getSelectedClassIds();


                         double totalClassCost = 0;

                       Iterator itr = classIds.iterator();
                       while(itr.hasNext())
                       {

                           totalClassCost = totalClassCost + getClassPirceAccordingtoClassId(itr.next()+"");


                                                  }

                       showUserMessage.showDialogBoxWithYesNoButton(sessionManager.getCurrencyCode(getActivity()) + " " + totalClassCost + " will be added in your bill", new DialogBoxButtonListner() {
                           @Override
                           public void onYesButtonClick(DialogInterface dialog) {

                               addMemberOnClass();


                           }
                       });




                   }
                   else
                   {

                       showUserMessage.showDialogBoxWithYesNoButton("Are you sure you want to withdraw from this class?", new DialogBoxButtonListner() {
                           @Override
                           public void onYesButtonClick(DialogInterface dialog) {

                               deleteMemberOnClass();

                           }
                       });

                   }
                }

                            }

        }
    };



    public void inslizedView()
    {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        try {
            updateclassListListener.UpdateView();
            updateclassListListener.onBackFragment();
        }
        catch (Exception e)
        {

        }


        DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.INVISIBLE);
        DirectorFragmentManageActivity.delete_all_btn.setText("Delete All");
        DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.club_events));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    ClassReservationGridAdapter classReservationGridAdapter ;

    public void getDataFromServer()
    {
        HashMap<String , Object> param = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd yyyy");

        endDate = (Calendar) startDate.clone();

        endDate.add(Calendar.DATE , 6);


        dateDurationtv.setText(simpleDateFormat.format(startDate.getTime())+" to "+simpleDateFormat.format(endDate.getTime()));


        param.put("class_uid" , SessionManager.getUser_id(getActivity()));
        param.put("class_date" , simpleDateFormat.format(startDate.getTime()));
        param.put("class_club_id" , SessionManager.getUser_Club_id(getActivity()));

        classReservationArrayList.clear();
        addDaysInList();
        httpRequest.getResponse(getActivity(), WebService.classGridLink, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                try
                {
                    if (jsonObject.getBoolean("status"))
                    {

                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        currentTime.setTime(simpleDateFormat1.parse(jsonObject.getString("time")));

                        JSONArray classGridJsonArray = jsonObject.getJSONArray("Response");

                        for (int i = 0 ; i < classGridJsonArray.length() ;i++)
                        {
                            ArrayList<ClassReservation> classReservationTimeArrayList = new ArrayList<ClassReservation>();



                            ClassReservation classReservation1 = new ClassReservation();
                            classReservation1.setClassColor("");
                            classReservation1.setClassName("");
                            classReservation1.setClassId("");
                            classReservation1.setClassDetailId("");
                            classReservation1.setClassDate("");
                            classReservation1.setClassStartTime("");
                            classReservation1.setClassEndTime("");


                            classReservationTimeArrayList.add(classReservation1);





                            JSONArray timeJsonArray = classGridJsonArray.getJSONObject(i).getJSONArray("time");

                            for (int j = 0 ; j < timeJsonArray.length() ; j++)
                            {
                                JSONObject timeJsonArrayItem = timeJsonArray.getJSONObject(j);
                                ClassReservation classReservation = new ClassReservation();
                                classReservation.setClassColor(timeJsonArrayItem.getString("class_color"));
                                classReservation.setClassName(timeJsonArrayItem.getString("class_name"));
                                classReservation.setClassId(timeJsonArrayItem.getString("class_id"));
                                classReservation.setClassDetailId(timeJsonArrayItem.getString("class_detail_id"));
                                classReservation.setClassDate(timeJsonArrayItem.getString("class_date"));
                                classReservation.setClassStartTime(timeJsonArrayItem.getString("class_startTime"));
                                classReservation.setClassEndTime(timeJsonArrayItem.getString("class_endTime"));
                                classReservation.setClassCost(timeJsonArrayItem.optString("class_cost"));

                                classReservation.setClassNoOfParticipents(Long.parseLong(timeJsonArrayItem.optString("class_noOfParticipents" ,"0")));
                                classReservation.setClassParticipents(Long.parseLong(timeJsonArrayItem.optString("class_participents" ,"0")));

                                classReservation.setClassReserveId(timeJsonArrayItem.optString("class_reserve_id"));
                                classReservation.setItemSelected(false);
                                classReservation.setClassMemberUid(timeJsonArrayItem.optString("class_member_uid"));
                                classReservationTimeArrayList.add(classReservation);


                            }

                            classReservationArrayList.add(classReservationTimeArrayList);


                        }


                         classReservationGridAdapter = new ClassReservationGridAdapter(getActivity() , classReservationArrayList , currentTime );

                        classReservationGridAdapter.setOnItemClickListener(ClassReservationGridFragment.this);
                        tableLayout.setAdapter(classReservationGridAdapter);
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT , HeaderColumnHeigh +rowHeight*(classReservationArrayList.size()-1));
                        tableLayout.setLayoutParams(layoutParams);

                    }
                    else
                    {
                        classReservationGridAdapter = new ClassReservationGridAdapter(getActivity() , classReservationArrayList , currentTime );

                        classReservationGridAdapter.setOnItemClickListener(ClassReservationGridFragment.this);
                        tableLayout.setAdapter(classReservationGridAdapter);
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT , HeaderColumnHeigh +rowHeight*(classReservationArrayList.size()-1));
                        tableLayout.setLayoutParams(layoutParams);
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









    public void refreshDataFromServer()
    {
        HashMap<String , Object> param = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd yyyy");

        endDate = (Calendar) startDate.clone();

        endDate.add(Calendar.DATE , 6);
        param.put("class_uid" , SessionManager.getUser_id(getActivity()));
        param.put("class_date" , simpleDateFormat.format(startDate.getTime()));
        param.put("class_club_id" , SessionManager.getUser_Club_id(getActivity()));
        dateDurationtv.setText(simpleDateFormat.format(startDate.getTime())+" to "+simpleDateFormat.format(endDate.getTime()));

        bottomsLayout.setVisibility(View.INVISIBLE);
        classReservationArrayList.clear();
        addDaysInList();

        httpRequest.getResponse(getActivity(), WebService.classGridLink, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                try
                {
                    if (jsonObject.getBoolean("status"))
                    {

                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        currentTime.setTime(simpleDateFormat1.parse(jsonObject.getString("time")));


                        JSONArray classGridJsonArray = jsonObject.getJSONArray("Response");






                        for (int i = 0 ; i < classGridJsonArray.length() ;i++)
                        {
                            ArrayList<ClassReservation> classReservationTimeArrayList = new ArrayList<ClassReservation>();



                            ClassReservation classReservation1 = new ClassReservation();
                            classReservation1.setClassColor("");
                            classReservation1.setClassName("");
                            classReservation1.setClassId("");
                            classReservation1.setClassDetailId("");
                            classReservation1.setClassDate("");
                            classReservation1.setClassStartTime("");
                            classReservation1.setClassEndTime("");


                            classReservationTimeArrayList.add(classReservation1);





                            JSONArray timeJsonArray = classGridJsonArray.getJSONObject(i).getJSONArray("time");

                            for (int j = 0 ; j < timeJsonArray.length() ; j++)
                            {
                                JSONObject timeJsonArrayItem = timeJsonArray.getJSONObject(j);
                                ClassReservation classReservation = new ClassReservation();
                                classReservation.setClassColor(timeJsonArrayItem.getString("class_color"));
                                classReservation.setClassName(timeJsonArrayItem.getString("class_name"));
                                classReservation.setClassId(timeJsonArrayItem.getString("class_id"));
                                classReservation.setClassDetailId(timeJsonArrayItem.getString("class_detail_id"));
                                classReservation.setClassDate(timeJsonArrayItem.getString("class_date"));
                                classReservation.setClassStartTime(timeJsonArrayItem.getString("class_startTime"));
                                classReservation.setClassEndTime(timeJsonArrayItem.getString("class_endTime"));
                                classReservation.setClassCost(timeJsonArrayItem.optString("class_cost"));

                                classReservation.setClassNoOfParticipents(Long.parseLong(timeJsonArrayItem.optString("class_noOfParticipents" ,"0")));
                                classReservation.setClassParticipents(Long.parseLong(timeJsonArrayItem.optString("class_participents" ,"0")));

                                classReservation.setClassReserveId(timeJsonArrayItem.optString("class_reserve_id"));
                                classReservation.setClassMemberUid(timeJsonArrayItem.optString("class_member_uid"));

                                classReservation.setItemSelected(false);






                                classReservationTimeArrayList.add(classReservation);

                            }

                            classReservationArrayList.add(classReservationTimeArrayList);


                        }



                    }
                    else
                    {
                        ShowUserMessage.showUserMessage(getActivity() ,jsonObject.optString("message" ));
                    }







                    classReservationGridAdapter.isUserSignUp =true;

                    classReservationGridAdapter.firstTime =true;

                    classReservationGridAdapter.setCurrentTime(currentTime);


                    // classReservationGridAdapter.no();
                    try
                    {
                        tableLayout.notifyLayoutChanged();

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tableLayout.scrollTo(80,0);
                                tableLayout.notifyLayoutChanged();


                            }
                        });
                        // classReservationGridAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e)
                    {

                    }
                    //tableLayout.notifyLayoutChanged();

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT , HeaderColumnHeigh +rowHeight*(classReservationArrayList.size()-1));
                    tableLayout.setLayoutParams(layoutParams);


                    //


                }
                catch (Exception e)
                {
                    Utill.showToast(getActivity() , e.getMessage());
                }

            }
        });
    }










    @Override
    public void onClick(View v)
    {

            }

            ClassReservation dayName ;


    public void addDaysInList()
    {
        ArrayList<ClassReservation> dayNameList = new ArrayList<>();


        Calendar blockDate = (Calendar) startDate.clone();
        blockDate.add(Calendar.DAY_OF_MONTH ,0);

        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");




        dayName = new ClassReservation();
        dayName.setDayName("Sunday"+"\n" + simpleDateFormat2.format(blockDate.getTime()));
        dayNameList.add(dayName);


        blockDate.add(Calendar.DAY_OF_MONTH ,0);

        dayName = new ClassReservation();
        dayName.setDayName("Sunday"+"\n" + simpleDateFormat2.format(blockDate.getTime()));
        dayNameList.add(dayName);

        blockDate.add(Calendar.DAY_OF_MONTH ,1);




        dayName = new ClassReservation();
        dayName.setDayName("Monday"+"\n" + simpleDateFormat2.format(blockDate.getTime()));
        dayNameList.add(dayName);

        blockDate.add(Calendar.DAY_OF_MONTH ,1);

        dayName = new ClassReservation();
        dayName.setDayName("Tuesday"+"\n" + simpleDateFormat2.format(blockDate.getTime()));
        dayNameList.add(dayName);



        blockDate.add(Calendar.DAY_OF_MONTH ,1);

        dayName = new ClassReservation();
        dayName.setDayName("Wednesday"+"\n" + simpleDateFormat2.format(blockDate.getTime()));
        dayNameList.add(dayName);



        blockDate.add(Calendar.DAY_OF_MONTH ,1);

        dayName = new ClassReservation();
        dayName.setDayName("Thursday"+"\n" + simpleDateFormat2.format(blockDate.getTime()));
        dayNameList.add(dayName);



        blockDate.add(Calendar.DAY_OF_MONTH ,1);

        dayName = new ClassReservation();
        dayName.setDayName("Friday"+"\n" + simpleDateFormat2.format(blockDate.getTime()));
        dayNameList.add(dayName);


        blockDate.add(Calendar.DAY_OF_MONTH ,1);

        dayName = new ClassReservation();
        dayName.setDayName("Saturday"+"\n" + simpleDateFormat2.format(blockDate.getTime()));
        dayNameList.add(dayName);






        classReservationArrayList.add(dayNameList);

    }


    public int getTotalSelectedItem()
    {
        totalSelectedItem = 0 ;
        for (int i = 0 ; i < classReservationArrayList.size() ;i++)
        {
            for (int j = 0 ; j < classReservationArrayList.get(i).size() ;j++)
            {
                if (classReservationArrayList.get(i).get(j).isItemSelected())
                {
                    totalSelectedItem++;
                }

            }

        }

        return totalSelectedItem ;
    }


   // int totalSelectedItem = 0 ;

    @Override
    public void onItemClick(int row, int column)
    {
    // Utill.showToast(getActivity() , "row = "+row+" , colomn = "+column);
        classReservationGridAdapter.firstTime =false;

        ClassReservation classReservation = classReservationArrayList.get(row).get(column);

        if (classReservation.getClassName().length() != 0 /*|| classReservation.getClassNoOfParticipents().equals(classReservation.getClassParticipents())*/)
        {


            if (classReservation.isOldedDate())
            {
                return;
            }




            if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER) == true)
            {
                classDeleteBtn.setVisibility(View.GONE);

                if (SessionManager.getUser_id(getActivity()).equals(classReservation.getClassMemberUid()) == false )
                {
                    if (classReservation.getClassNoOfParticipents() == (classReservation.getClassParticipents()))
                    {
                      ShowUserMessage.showUserMessage(getActivity() , "Class is full");
                        return;
                    }

                }

                if (classReservationGridAdapter.isUserSignUp == true && classReservation.getClassReserveId().length() != 0)
                {
                    classReservationGridAdapter.isUserSignUp = false ;
                    cancelAppReservation();
                }
                else
                {
                    if (classReservationGridAdapter.isUserSignUp == false && classReservation.getClassReserveId().length() == 0)
                    {
                        classReservationGridAdapter.isUserSignUp = true ;

                        cancelAppReservation();

                    }
                    else
                    {

                    }

                }
                classReservation.setItemSelected(!classReservation.isItemSelected());


                if (classReservationGridAdapter.isUserSignUp)
                {
                    addParticipantBtn.setText("Sign Up");

                }
                else
                {
                    addParticipantBtn.setText("Withdraw");

                }

            }
            else
            {
                classReservation.setItemSelected(!classReservation.isItemSelected());

            }




            if (getTotalSelectedItem() != 0)
            {


             bottomsLayout.setVisibility(View.VISIBLE);
            }
            else
            {

                cancelAppReservation();
                classReservationGridAdapter.isUserSignUp = true ;
                classReservationGridAdapter.firstTime = true ;


            }



            classReservationGridAdapter.notifyDataSetChanged();

        }

    }



    public void deleteClassFromServer(String classDetailId)
    {


        HashMap<String , Object> param = new HashMap<>();
        param.put("class_uid" , SessionManager.getUser_id(getActivity()));
        param.put("class_detail_id" , classDetailId);
        param.put("user_type" , SessionManager.getUser_type(getActivity()));
        param.put("class_club_id" , SessionManager.getUser_Club_id(getActivity()));
        param.put("delete_type" , "2");

        httpRequest.getResponse(getActivity(), WebService.delete_class, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                try
                {
                    Utill.showDialg(jsonObject.getString("message") , getActivity());

                    if (jsonObject.getBoolean("status") == true)
                    {

                        bottomsLayout.setVisibility(View.INVISIBLE);
                        refreshDataFromServer();
                        //getActivity().getSupportFragmentManager().popBackStack();

                    }
                    else
                    {
                        showUserMessage.showDialogOnFragmentWithBack(getActivity() , jsonObject.getString("message"));

                       // fragmentBackResponseListener.onBackFragment();

                    }
                }
                catch (Exception e)
                {

                }
            }
        });

    }

    public void cancelAppReservation()
    {
        bottomsLayout.setVisibility(View.INVISIBLE);

        for (int i = 0 ; i < classReservationArrayList.size() ;i++)
        {
            for (int j = 0 ; j < classReservationArrayList.get(i).size() ;j++)
            {

                classReservationArrayList.get(i).get(j).setItemSelected(false);

            }


        }
        classReservationGridAdapter.notifyDataSetChanged();
    }

    public String getSelectedIds()
    {
        String ids = "";


//////////////////////

        isFullClassVailableIList = false ;


        for (int i = 0 ; i < classReservationArrayList.size() ;i++)
        {
            for (int j = 0 ; j < classReservationArrayList.get(i).size() ;j++)
            {
                ClassReservation classReservation = classReservationArrayList.get(i).get(j) ;

                   if (classReservation.isItemSelected())
                   {
                       if (Validation.isStringNullOrBlank(ids))
                       {
                          ids =  classReservation.getClassDetailId() ;
                       }
                       else
                       {
                           ids = ids+","+ classReservation.getClassDetailId() ;

                       }


                       if (isFullClassVailableIList == false &&classReservation.getClassName().length() != 0 && classReservation.getClassNoOfParticipents() == (classReservation.getClassParticipents()))
                       {
                           isFullClassVailableIList = true ;
                       }
                   }


            }


        }




        return ids ;

    }


    public void addMemberOnClass( )
    {
        HashMap<String , Object> param = new HashMap<>();
        param.put("class_member_uid" ,SessionManager.getUser_id(getActivity()) );
        param.put("user_type" ,SessionManager.getUser_type(getActivity()) );
        param.put("class_detail_ids" ,getSelectedIds() );


        httpRequest.getResponse(getActivity(), WebService.classSignupLink, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {

                Log.e("jsonObject" ,jsonObject+"");
                try
                {
                    if (jsonObject.getBoolean("status"))
                    {


                       JSONArray classJsonArray =    getSelectedClassJsonArrayFrom();


                       for (int i =0 ; i < classJsonArray.length() ;i++)
                       {

                           try
                           {

                               JSONObject classJsonArrayItem = classJsonArray.getJSONObject(i);

                               SimpleDateFormat dateTimeFormate = new SimpleDateFormat("MM-dd-yyyy hh:mm a");

                               Date startTime = dateTimeFormate.parse(classJsonArrayItem.getString("class_startTime"));
                               Date endTime = dateTimeFormate.parse(classJsonArrayItem.getString("class_endTime"));


                               insertDataEventCalender(startTime.getTime() , endTime.getTime() , classJsonArrayItem.getInt("class_detail_id") , classJsonArrayItem.getString("class_name"));
                           }
                           catch (Exception e)
                           {

                           }

                       }






                        refreshDataFromServer();
                    }
                    else
                    {
                        ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("message"));

                    }

                }
                catch (Exception e)
                {

                }


            }
        });
    }

    public void deleteMemberOnClass( )
    {
        HashMap<String , Object> param = new HashMap<>();
        param.put("class_member_uid" ,SessionManager.getUser_id(getActivity()) );
        param.put("user_type" ,SessionManager.getUser_type(getActivity()) );
        param.put("class_detail_ids" ,getSelectedIds() );


        httpRequest.getResponse(getActivity(), WebService.class_member_delete, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {

                Log.e("jsonObject" ,jsonObject+"");
                try
                {
                    if(jsonObject.getBoolean("status"))
                    {

                        String ids[] = getSelectedIds().split(",");


                        for (int i= 0 ; i < ids.length;i++)
                        {
                            deleteReservationFromEntry(ids[i]);
                        }


                        refreshDataFromServer();
                    }
                    else
                    {
                        ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("message"));

                    }

                }
                catch (Exception e)
                {

                }


            }
        });
    }



    public void deleteReservationFromEntry(String classId)
    {
        try {
            Uri eventsUri = Uri.parse("content://com.android.calendar/" + "events");

            long  event_id = sqlListe.getClassReservationEventId(classId);
            sqlListe.withdrowClassByMember((event_id));






            //	getActivity().getApplicationContext().getContentResolver().delete(CalendarContract.Events.CONTENT_URI ,CalendarContract.Reminders.EVENT_ID +" = ?", new String[]{sqlListe.getEventId(court_reservation_id)+""});

        } catch (Exception e) {
            ShowUserMessage.showDialogOnActivity(getActivity(), e.getMessage());
        }
    }



    public View.OnClickListener leftRightBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            Calendar sundayCal = (Calendar) currentTime.clone();
            sundayCal.add( Calendar.DAY_OF_WEEK, -(sundayCal.get(Calendar.DAY_OF_WEEK)-1));
            priviousIv.setVisibility(View.VISIBLE);
            nextIv.setVisibility(View.VISIBLE);


            if (v.getId() == R.id.privious_iv)
            {


                startDate.add(Calendar.DATE , -7);


                long diffDays =     startDate.getTimeInMillis() -sundayCal.getTimeInMillis();
                diffDays = (diffDays/1000)/60/60/24 ;

             //   Toast.makeText(getActivity() , )

                if (diffDays < 0)
                {

                    priviousIv.setVisibility(View.INVISIBLE);

                }
                if (diffDays >=-7)
                {
                    refreshDataFromServer();

                }

            }

            if (v.getId() == R.id.next_iv)
            {

                startDate.add(Calendar.DATE , 7);



                long diffDays =     startDate.getTimeInMillis() -sundayCal.getTimeInMillis();


                diffDays = (diffDays/1000)/60/60/24 ;

                if (diffDays >=28)
                {

                    nextIv.setVisibility(View.INVISIBLE);

                }
                if ((diffDays <=30))
                {
                    refreshDataFromServer();

                }




            }
        }
    };


    public   void showDialogOnFragmentWithBack(final FragmentActivity AppCompatActivity , String msg)
    {

        AlertDialog alertDialog = new AlertDialog.Builder(
                AppCompatActivity).create();

        // Setting Dialog Title
        alertDialog.setTitle(AppCompatActivity.getResources().getString(R.string.app_name));
        alertDialog.setCancelable(false);
        // Setting Dialog Message
        alertDialog.setMessage(msg);

        // Setting Icon to Dialog


        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                AppCompatActivity.getSupportFragmentManager().popBackStack();
                updateclassListListener.onBackFragment();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }



    public ArrayList<ClassReservation> getSelectedReservationItemList()
    {
        selectedclassReservation = new ArrayList<>();

//////////////////////

        isFullClassVailableIList = false ;


        for (int i = 0 ; i < classReservationArrayList.size() ;i++) {
            for (int j = 0; j < classReservationArrayList.get(i).size(); j++) {
                ClassReservation classReservation = classReservationArrayList.get(i).get(j);

                if (classReservation.isItemSelected()) {

                    selectedclassReservation.add(classReservation);

                }


            }

        }


        return selectedclassReservation ;

    }


    FragmentBackResponseListener fragmentBackResponseListener = new FragmentBackResponseListener() {
        @Override
        public void onBackFragment() {
            super.onBackFragment();

            refreshDataFromServer();
        }
    };


    int reminderMinut ;

    public void insertDataEventCalender(final long startTime, final long endTime,  final int classId , final String className) {


        String timeSlot = sessionManager.getAlaramTimeDuration(getActivity());

        try {

            reminderMinut = Integer.parseInt(timeSlot.split(" ")[0]);



        } catch (Exception e) {
            reminderMinut = 0 ;
        }

        try {
            ContentResolver cr = getActivity().getContentResolver();
            ContentValues values = new ContentValues();

            values.put(CalendarContract.Events.DTSTART, startTime);
            values.put(CalendarContract.Events.DTEND, endTime);



            String titel = "Reminder : "+SessionManager.getClubName(getContext())+" "+className+" reservation";

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

            sqlListe.joinedMemberByClass(id, classId);
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


    public JSONArray getSelectedClassJsonArrayFrom()
    {

        JSONArray classJsonArray = new JSONArray();

        for (int i = 0 ; i < classReservationArrayList.size() ;i++)
        {
            for (int j = 0 ; j < classReservationArrayList.get(i).size() ;j++)
            {
                ClassReservation classReservation = classReservationArrayList.get(i).get(j) ;

                if (classReservation.isItemSelected())
                {

                    try
                    {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("class_startTime" , classReservation.getClassDate()+" "+ classReservation.getClassStartTime());
                        jsonObject.put("class_endTime" ,classReservation.getClassDate()+" "+ classReservation.getClassEndTime());
                        jsonObject.put("class_detail_id" , classReservation.getClassDetailId());


                        jsonObject.put("class_name" , classReservation.getClassName());


                        classJsonArray.put(jsonObject);


                    }
                    catch (Exception e)
                    {

                    }



                }


            }


        }
        return classJsonArray;

    }






















    public HashSet<String> getSelectedClassIds()
    {

        HashSet<String> classReservationHashSet = new HashSet<>() ;

//////////////////////



        for (int i = 0 ; i < classReservationArrayList.size() ;i++)
        {
            for (int j = 0 ; j < classReservationArrayList.get(i).size() ;j++)
            {
                ClassReservation classReservation = classReservationArrayList.get(i).get(j) ;

                if (classReservation.isItemSelected())
                {


                    classReservationHashSet.add(classReservation.getClassId());


                }


            }


        }




        return classReservationHashSet ;

    }

    public double getClassPirceAccordingtoClassId(String classId)
    {
        double classCost = 0 ;


        for (int i = 0 ; i < classReservationArrayList.size() ;i++)
        {

            for (int j = 0 ; j < classReservationArrayList.get(i).size() ;j++)

            {
                if (classId.equals(classReservationArrayList.get(i).get(j).getClassId()))
                {
                    try {
                        classCost = Double.parseDouble(classReservationArrayList.get(i).get(j).getClassCost());

                    }
                    catch (Exception e)
                    {

                    }
                }
            }

        }

        return classCost ;
    }





}
