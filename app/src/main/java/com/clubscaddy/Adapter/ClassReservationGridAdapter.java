package com.clubscaddy.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cleveroad.adaptivetablelayout.LinkedAdaptiveTableAdapter;
import com.cleveroad.adaptivetablelayout.OnItemClickListener;
import com.cleveroad.adaptivetablelayout.ViewHolderImpl;
import com.clubscaddy.Bean.ClassReservation;
import com.clubscaddy.Interface.TableItemClickListener;
import com.clubscaddy.R;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Callable;

/**
 * Created by administrator on 27/11/17.
 */

public class ClassReservationGridAdapter extends LinkedAdaptiveTableAdapter<ViewHolderImpl>
{

    Activity activity ;
    ArrayList<ArrayList<ClassReservation>> classReservationGridItemList ;

   public boolean isUserSignUp = true;

    public boolean firstTime ;


    int rowHeight = 150;
    int HeaderColumnHeigh =100;
    int ColumnWidth =150;

    Calendar currentTime ;

    public void setCurrentTime(Calendar currentTime)
    {
         this.currentTime =  currentTime ;
    }


    public ClassReservationGridAdapter(Activity activity , ArrayList<ArrayList<ClassReservation>> classReservationGridItemList ,Calendar currentTime  )
    {
        this.activity = activity  ;
        isUserSignUp = true;
        firstTime = true ;
        rowHeight = AppConstants.getDeviceHeight(activity) /8 ;
        HeaderColumnHeigh  = AppConstants.getDeviceHeight(activity) /10 ;
        ColumnWidth = (int)(AppConstants.getDeviceWidth(activity) /2.5) ;

        this.classReservationGridItemList = classReservationGridItemList  ;


        this.currentTime = currentTime  ;


    }



    @Override
    public int getRowCount()
    {
        return classReservationGridItemList.size();
    }

    @Override
    public int getColumnCount()
    {
        if (classReservationGridItemList.size() != 0)
        return classReservationGridItemList.get(0).size();
        else
            return 0 ;
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateItemViewHolder(@NonNull ViewGroup parent)
    {
        View view = LayoutInflater.from(activity).inflate(R.layout.class_reservation_item , parent , false);

        return new ClassReservationItemViewHoldeer(view);
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(activity).inflate(R.layout.class_reservation_item_header , parent , false);

        return new ClassReservationItemHeaderViewHoldeer(view);    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateRowHeaderViewHolder(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(activity).inflate(R.layout.class_reservation_item_header , parent , false);

        return new ClassReservationItemHeaderViewHoldeer(view);
    }

    @NonNull
    @Override
    public ViewHolderImpl onCreateLeftTopHeaderViewHolder(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(activity).inflate(R.layout.class_reservation_item_header , parent , false);

        return new ClassReservationItemHeaderViewHoldeer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderImpl viewHolder, final int row, final int column)
    {


        try
        {


        ClassReservationItemViewHoldeer classReservationItemViewHoldeer = (ClassReservationItemViewHoldeer) viewHolder;

        final ClassReservation classReservation = classReservationGridItemList.get(row).get(column);

        Log.e("classReservation" , classReservation+"");


        if (classReservation.getClassMemberUid().equals(SessionManager.getUser_id(activity)))
        {



          classReservationItemViewHoldeer.userReservesImageView.setVisibility(View.VISIBLE);
        }
        else
        {
            classReservationItemViewHoldeer.userReservesImageView.setVisibility(View.INVISIBLE);

        }

        if (classReservation.getClassName().length() == 0)
        {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String date = (classReservation.getClassDate());


            Calendar selectedCal = Calendar.getInstance(Locale.ENGLISH);
            selectedCal.setTime(simpleDateFormat.parse(date));


            boolean isOldDate   =Utill.compareTwoDate(currentTime ,selectedCal );


            if (isOldDate)
            {
                classReservationItemViewHoldeer.reservation_layout.setBackgroundColor(activity.getResources().getColor(R.color.comment_all_text_color));

            }
            else
            {
                classReservationItemViewHoldeer.reservation_layout.setBackgroundColor(Color.parseColor(classReservation.getClassColor()));

            }




        }
        else
        {

            boolean isOldDate = false;


            try
            {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
               String dateTime = (classReservation.getClassDate()+" "+(classReservation.getClassStartTime()));


                Calendar selectedCal = Calendar.getInstance(Locale.ENGLISH);
                selectedCal.setTime(simpleDateFormat.parse(dateTime));

                isOldDate = Utill.compareTwoDateAndTime(currentTime ,selectedCal ) ;
            }
            catch (Exception e)
            {

            }

            classReservation.setOldedDate(isOldDate);


            if (isOldDate)
            {
                classReservationItemViewHoldeer.reservation_layout.setBackgroundColor(activity.getResources().getColor(R.color.comment_all_text_color));

            }
            else
            {
                if(SessionManager.getUser_type(activity).equals(AppConstants.USER_TYPE_MEMBER) == false)
                {
                    if (classReservation.isItemSelected())
                    {
                        if (classReservation.getClassReserveId().length() == 0)
                        {
                            classReservationItemViewHoldeer.reservation_layout.setBackgroundColor(activity.getResources().getColor(R.color.green_selection_color));

                        }
                        else
                        {
                            classReservationItemViewHoldeer.reservation_layout.setBackgroundColor(activity.getResources().getColor(R.color.red));

                        }
                    }
                    else
                    {
                        classReservationItemViewHoldeer.reservation_layout.setBackgroundColor(Color.parseColor(classReservation.getClassColor()));

                    }
                }
                else
                {

                    if (firstTime)
                    {
                        if (classReservation.getClassNoOfParticipents() ==(classReservation.getClassParticipents()))
                        {
                            classReservationItemViewHoldeer.reservation_layout.setBackgroundColor(activity.getResources().getColor(R.color.gray_color));

                        }
                        else
                        {
                            classReservationItemViewHoldeer.reservation_layout.setBackgroundColor(Color.parseColor(classReservation.getClassColor()));

                        }
                    }
                    else
                    {
                        if (isUserSignUp)
                        {
                            if (classReservation.getClassNoOfParticipents()== (classReservation.getClassParticipents()))
                            {
                                classReservationItemViewHoldeer.reservation_layout.setBackgroundColor(activity.getResources().getColor(R.color.gray_color));

                            }
                            else
                            {
                                if (classReservation.getClassReserveId().length() == 0 )
                                {
                                    if (classReservation.isItemSelected())
                                    {
                                        classReservationItemViewHoldeer.reservation_layout.setBackgroundColor(activity.getResources().getColor(R.color.green_selection_color));

                                    }
                                    else
                                    {
                                        classReservationItemViewHoldeer.reservation_layout.setBackgroundColor(Color.parseColor(classReservation.getClassColor()));

                                    }
                                }
                                else
                                {
                                    classReservationItemViewHoldeer.reservation_layout.setBackgroundColor(activity.getResources().getColor(R.color.gray_color));

                                }
                            }




                        }
                        else
                        {
                            if (classReservation.getClassReserveId().length() != 0)
                            {
                                if (classReservation.isItemSelected())
                                {
                                    classReservationItemViewHoldeer.reservation_layout.setBackgroundColor(activity.getResources().getColor(R.color.red));

                                }
                                else
                                {
                                    classReservationItemViewHoldeer.reservation_layout.setBackgroundColor(Color.parseColor(classReservation.getClassColor()));

                                }
                            }
                            else
                            {
                                classReservationItemViewHoldeer.reservation_layout.setBackgroundColor(activity.getResources().getColor(R.color.gray_color));

                            }
                        }

                    }



                }







            }
        }












        if (classReservation.getClassName().length() == 0)
        {
            classReservationItemViewHoldeer.blowLayout.setVisibility(View.VISIBLE);

            classReservationItemViewHoldeer.classReservationDateTv.setVisibility(View.INVISIBLE);

        }
        else
        {
            classReservationItemViewHoldeer.blowLayout.setVisibility(View.INVISIBLE);

            classReservationItemViewHoldeer.classReservationDateTv.setVisibility(View.VISIBLE);


            String className = classReservation.getClassName();


            if (className.length()>15)
            {
                className = className.substring(0 ,15);
                className = className+"...";
            }

            classReservationItemViewHoldeer.classReservationDateTv.setText(classReservation.getClassDate()+"\n"+
                    className/*+"("+classReservation.getClassParticipents()+")"*/+"\n"+
                            classReservation.getClassStartTime()+" to "+ classReservation.getClassEndTime() );
        }





        }
        catch (Exception e)
        {
    //   Utill.showDialg("row "+row +"colomn "+column , activity);
        }


    }

    @Override
    public void onBindHeaderColumnViewHolder(@NonNull ViewHolderImpl viewHolder, int column) {

        ClassReservationItemHeaderViewHoldeer classReservationItemHeaderViewHoldeer = (ClassReservationItemHeaderViewHoldeer) viewHolder;
        classReservationItemHeaderViewHoldeer.header.setText(classReservationGridItemList.get(0).get(column).getDayName());
    }

    @Override
    public void onBindHeaderRowViewHolder(@NonNull ViewHolderImpl viewHolder, int row) {

    }

    @Override
    public void onBindLeftTopHeaderViewHolder(@NonNull ViewHolderImpl viewHolder)
    {

    }




    @Override
    public int getColumnWidth(int column)
    {
        return ColumnWidth;
    }

    @Override
    public int getHeaderColumnHeight() {
        return HeaderColumnHeigh;
    }

    @Override
    public int getRowHeight(int row)
    {
        return rowHeight;
    }


















    @Override
    public int getHeaderRowWidth() {
        return 0;
    }






    public class ClassReservationItemViewHoldeer extends ViewHolderImpl
    {

        TextView classReservationDateTv ;
        RelativeLayout reservation_layout ;
        ImageView userReservesImageView ;
        //TextView classReservationNameTv ;
       // TextView classReservationDurationTv ;

        //LinearLayout upperLayout ;
        TextView blowLayout ;

        public ClassReservationItemViewHoldeer( View itemView) {
            super(itemView);
            classReservationDateTv = (TextView) itemView.findViewById(R.id.class_reservation_date_tv);
           // classReservationNameTv = (TextView) itemView.findViewById(R.id.class_reservation_name_tv);
           // classReservationDurationTv = (TextView) itemView.findViewById(R.id.class_reservation_duration_tv);

           // upperLayout = (LinearLayout) itemView.findViewById(R.id.upper_layout);
            blowLayout = (TextView) itemView.findViewById(R.id.blow_layout);

            userReservesImageView = (ImageView) itemView.findViewById(R.id.user_reserves_image_view);

            reservation_layout = (RelativeLayout) itemView.findViewById(R.id.reservation_layout);

        }
    }

    public class ClassReservationItemHeaderViewHoldeer extends ViewHolderImpl
    {
        TextView header ;


        public ClassReservationItemHeaderViewHoldeer( View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.header);

        }
    }






}
