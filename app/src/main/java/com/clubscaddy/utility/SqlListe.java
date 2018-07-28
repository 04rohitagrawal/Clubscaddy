package com.clubscaddy.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.Bean.UserClub;
import com.clubscaddy.Interface.MemberListListener;

import java.util.ArrayList;

/**
 * Created by administrator on 30/11/16.
 */

public class SqlListe extends SQLiteOpenHelper {
    public static final String databaseName = "SportCaddy.db";
    Context context;
    public SqlListe(Context context) {
        super(context, databaseName, null, 2);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("CREATE TABLE IF NOT EXISTS userclub(club_id integer primary key," +
                "club_name text ," +
                "club_address text ," +
                "club_country text , " +
                "clublat real ," +
                "clublong real ," +
                "club_num_of_courts int , " +
                "club_status_message text , " +
                "club_logo text ," +
                "club_accounts_email1 text ," +
                " club_sport_id text ," +
                " club_ratting_show int , " +
                "club_status int ," +
                "clubtype int ," +
                "purchase_expiring text ," +
                "demo_period int ," +
                "club_temp_id int ," +
                "club_temp_date text ," +
                "club_tempreture real ," +
                "club_temp_opencourts int ," +
                "user_linkedin text ," +
                "user_login_app text ," +
                "user_type int ," +
                "user_about text ," +
                "user_phone text ," +
                "user_registereddate text ," +
                "user_gender int ," +
                "user_device_token text ," +
                "user_twitteter text ," +
                "user_junior int ," +
                "user_instagram text ," +
                "user_email text ," +
                "user_profilepic text ," +
                "user_first_name text ," +
                "user_facebook text ," +

                "user_id int ," +
                "user_changeclub int ," +
                "user_cost_per_hour int ," +
                "user_expired_staus int ," +
                "user_rating text ," +
                "user_club_id int ," +
                "user_last_name text ," +
                "club_rating_type int ," +
                "club_rating text ,club_score_view int ," +
                "sport_name text ,sport_player int , " +
                "sport_field_name text , " +
                "club_status_change_date text , currencyCode text"+
                ") ");

        db.execSQL("CREATE TABLE IF NOT EXISTS eventlist(event_id integer primary key, court_reservation_court_id int ,court_reservation_club_id int ,court_reservation_id int , memberbookedid int)");


        db.execSQL("CREATE TABLE IF NOT EXISTS eventlistforcochreservation(event_id integer primary key, memberbookedid int)");




        db.execSQL("CREATE TABLE IF NOT EXISTS eventlistforclassrvation(event_id integer primary key, class_id long)");

        db.execSQL("CREATE TABLE IF NOT EXISTS member(user_id integer ," +
                "user_first_name text,user_last_name test,user_email text , user_mobile_no text , profile_pic text" +
                ",user_rating text ,user_junior text ,user_gender text ,user_type text , user_status text  , club_id int)");


        db.execSQL("CREATE TABLE IF NOT EXISTS memberUpdateTime(club_id integer primary key," +
                "lastDateOfMemberListUpdation text)" );

    }

      public void deleteeventlistforcochreservationEvents(int memberbookedid) {
        try
        {
            this.getWritableDatabase().delete("eventlistforcochreservation", "memberbookedid = '" + memberbookedid + "'", null);
        }
        catch (Exception e)
        {
            Toast.makeText(context , e.getMessage() , Toast.LENGTH_LONG).show();
        }
    }

    public ArrayList<Integer> getEventIds(String memberbookedid)
    {
        ArrayList<Integer>eventIdList = new ArrayList<Integer>();

        Cursor cursor = getWritableDatabase().rawQuery("Select *from eventlist where memberbookedid = '"+memberbookedid+"'", null);

        while (cursor.moveToNext())
        {
            Log.e("event_id" ,cursor.getInt(0)+" ");
            eventIdList.add(cursor.getInt(0));

        }


        return  eventIdList;
    }


    public void addEvent(int event_id ,int court_reservation_court_id  ,int court_reservation_club_id  ,int court_reservation_id ,int memberbookedid)
    {

        ContentValues contentValues = new ContentValues();
        contentValues.put("event_id" ,event_id);
        contentValues.put("court_reservation_court_id" ,court_reservation_court_id);
        contentValues.put("court_reservation_club_id" ,court_reservation_club_id);
        contentValues.put("court_reservation_id" ,court_reservation_id);
        contentValues.put("memberbookedid" ,memberbookedid);



        try
        {
            getWritableDatabase().insert("eventlist" , null ,contentValues);
        }
        catch (Exception e)
        {
            ShowUserMessage.showUserMessage(context , e.getMessage());
        }



    }

    public ArrayList<Integer> getCoachReservationEventIds(String memberbookedid)
    {
        ArrayList<Integer>eventIdList = new ArrayList<Integer>();

        Cursor cursor = getWritableDatabase().rawQuery("Select *from eventlistforcochreservation where memberbookedid = '"+memberbookedid+"'", null);

        while (cursor.moveToNext())
        {
            Log.e("event_id" ,cursor.getInt(0)+" ");
            eventIdList.add(cursor.getInt(0));

        }


        return  eventIdList;
    }





    public void addEventForCoachReservation(int event_id   ,int memberbookedid)
    {

        ContentValues contentValues = new ContentValues();
        contentValues.put("event_id" ,event_id);

        contentValues.put("memberbookedid" ,memberbookedid);



        try
        {
            getWritableDatabase().insert("eventlistforcochreservation" , null ,contentValues);
        }
        catch (Exception e)
        {
            ShowUserMessage.showUserMessage(context , e.getMessage());
        }



    }







    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


       //Toast.makeText(context , "upgrade" ,1).show();

        final String DATABASE_ALTER_TEAM_1 = "ALTER TABLE userclub  add COLUMN  currencyCode text DEFAULT 1" ;

        try
        {
            db.execSQL(DATABASE_ALTER_TEAM_1);



        }
        catch (Exception e)
        {

        }


        db.execSQL("CREATE TABLE IF NOT EXISTS eventlistforclassrvation(event_id integer primary key, class_id long)");


        db.execSQL("CREATE TABLE IF NOT EXISTS member(user_id integer," +
                "user_first_name text,user_last_name test,user_email text , user_mobile_no text , profile_pic text" +
                ",user_rating text ,user_junior text ,user_gender text ,user_type text , user_status text , club_id int   )");

        db.execSQL("CREATE TABLE IF NOT EXISTS memberUpdateTime(club_id integer primary key," +
                "lastDateOfMemberListUpdation text)" );

    }


    public void setUserClubInfo(UserClub userClub)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("club_id" ,userClub.getClub_id());
        contentValues.put("club_name" ,userClub.getClub_name());
        contentValues.put("club_address" ,userClub.getClub_address());
        contentValues.put("club_country" ,userClub.getClub_country());
        contentValues.put("clublat" ,userClub.getClublat());
        contentValues.put("clublong" ,userClub.getClublong());
        contentValues.put("club_num_of_courts" ,userClub.getClub_num_of_courts());
        contentValues.put("club_status_message" ,userClub.getClub_status_message());
        contentValues.put("club_logo" ,userClub.getClub_logo());
        contentValues.put("club_accounts_email1" ,userClub.getClub_accounts_email1());
        contentValues.put("club_sport_id" ,userClub.getClub_sport_id());
        contentValues.put("club_ratting_show" ,userClub.getClub_ratting_show());
        contentValues.put("club_status" ,userClub.getClub_status());
        contentValues.put("clubtype" ,userClub.getClubtype());
        contentValues.put("purchase_expiring" ,userClub.getPurchase_expiring());
        contentValues.put("demo_period" ,userClub.getDemo_period());
        contentValues.put("club_temp_id" ,userClub.getClub_temp_id());
        contentValues.put("club_temp_date" ,userClub.getClub_temp_date());
        contentValues.put("club_temp_opencourts" ,userClub.getClub_temp_opencourts());
        contentValues.put("user_login_app" ,userClub.getUser_login_app());
        contentValues.put("user_type" ,userClub.getUser_type());
        contentValues.put("club_address" ,userClub.getClub_address());
        contentValues.put("user_about" ,userClub.getUser_about());
        contentValues.put("user_phone" ,userClub.getUser_phone());
        contentValues.put("user_registereddate" ,userClub.getUser_registereddate());
        contentValues.put("user_gender" ,userClub.getUser_gender());
        contentValues.put("user_device_token" ,userClub.getUser_device_token());
        contentValues.put("user_facebook" ,userClub.getUser_facebook());
        contentValues.put("user_twitteter" ,userClub.getUser_twitteter());
        contentValues.put("user_junior" ,userClub.getUser_junior());
        contentValues.put("user_instagram" ,userClub.getUser_instagram());
        contentValues.put("user_email" ,userClub.getUser_email());
        contentValues.put("user_first_name" ,userClub.getUser_first_name());
        contentValues.put("user_cost_per_hour" ,userClub.getUser_cost_per_hour());
        contentValues.put("user_expired_staus" ,userClub.getUser_expired_staus());
        contentValues.put("user_rating" ,userClub.getUser_rating());
        contentValues.put("user_club_id" ,userClub.getClub_id());
        contentValues.put("user_last_name" ,userClub.getUser_last_name());
        contentValues.put("club_rating_type" , userClub.getClub_rating_type());
        contentValues.put("club_rating" , userClub.getClub_rating());
        contentValues.put("user_id" , userClub.getUser_id());
        contentValues.put("user_profilepic" , userClub.getUser_profilepic());

        contentValues.put("club_score_view" , userClub.getClub_score_view());
        contentValues.put("sport_name" , userClub.getSport_name());
        contentValues.put("sport_player" , userClub.getSport_player());

        contentValues.put("sport_field_name" , userClub.getSport_field_name());
        contentValues.put("club_status_change_date" , userClub.getClub_status_change_date());

        contentValues.put("currencyCode" , userClub.getCurrencyCode());




        getWritableDatabase().insert("userclub" , null ,contentValues);




    }




    public void deleteEvents(int memberbookedid) {
        try
        {
            this.getWritableDatabase().delete("eventlist", "memberbookedid = '" + memberbookedid + "'", null);
        }
        catch (Exception e)
        {

        }
    }




    public void withdrowClassByMember(long event_id) {
        try
        {
            this.getWritableDatabase().delete("eventlist", "event_id = '" + event_id + "'", null);
        }
        catch (Exception e)
        {

        }
    }


    public void joinedMemberByClass(int event_id   ,int class_id)
    {

        ContentValues contentValues = new ContentValues();
        contentValues.put("event_id" ,event_id);

        contentValues.put("class_id" ,class_id);



        try
        {
            getWritableDatabase().insert("eventlistforclassrvation" , null ,contentValues);
        }
        catch (Exception e)
        {
            ShowUserMessage.showUserMessage(context , e.getMessage());
        }



    }


    public long getClassReservationEventId(String classId)
    {

        long eventId =-1;

        Cursor cursor = getWritableDatabase().rawQuery("Select *from eventlistforclassrvation where class_id = '"+classId+"'", null);

        while (cursor.moveToNext())
        {
            Log.e("event_id" ,cursor.getInt(0)+" ");

            eventId = cursor.getLong(0);

        }


        return  eventId;
    }













    public void deleteAllClub()
    {
        getWritableDatabase().delete("userclub" ,null ,null);
    }


    public ArrayList<UserClub> getAllClub()
    {
        ArrayList<UserClub> userClubArrayList = new ArrayList<UserClub>();

        Cursor cursor = getWritableDatabase().rawQuery("Select *from userclub", null);

        while (cursor.moveToNext())
        {
            UserClub userClub = new UserClub();
            userClub.setClub_id(cursor.getInt(0));
            userClub.setClub_name(cursor.getString(1));
            userClub.setClub_address(cursor.getString(2));
            userClub.setClub_country(cursor.getString(3));
            userClub.setClublat(cursor.getLong(4));
            userClub.setClublong(cursor.getLong(5));
            userClub.setClub_num_of_courts(6);
            userClub.setClub_status_message(cursor.getString(7));
            userClub.setClub_logo(cursor.getString(8));
            userClub.setClub_sport_id(cursor.getString(cursor.getColumnIndex("club_sport_id")));
            userClub.setClub_accounts_email1(cursor.getString(cursor.getColumnIndex("club_accounts_email1")));
            userClub.setClub_ratting_show(cursor.getInt(cursor.getColumnIndex("club_ratting_show")));
            userClub.setClub_status(cursor.getInt(cursor.getColumnIndex("club_status")));
            userClub.setClubtype(cursor.getInt(cursor.getColumnIndex("clubtype")));
            userClub.setPurchase_expiring(cursor.getString(cursor.getColumnIndex("purchase_expiring")));
            userClub.setDemo_period(cursor.getInt(cursor.getColumnIndex("demo_period")));
            userClub.setClub_temp_id(cursor.getInt(cursor.getColumnIndex("club_temp_id")));
            userClub.setClub_temp_date(cursor.getString(cursor.getColumnIndex("club_temp_date")));
            userClub.setClub_temp_opencourts(cursor.getInt(cursor.getColumnIndex("club_temp_opencourts")));
            userClub.setUser_login_app(cursor.getInt(cursor.getColumnIndex("user_login_app")));
            userClub.setUser_type(cursor.getInt(cursor.getColumnIndex("user_type")));
            userClub.setClub_address(cursor.getString(cursor.getColumnIndex("club_address")));
            userClub.setUser_about(cursor.getString(cursor.getColumnIndex("user_about")));
            userClub.setUser_phone(cursor.getString(cursor.getColumnIndex("user_phone")));
            userClub.setUser_registereddate(cursor.getString(cursor.getColumnIndex("user_registereddate")));
            userClub.setUser_gender(cursor.getInt(cursor.getColumnIndex("user_gender")));
            userClub.setUser_device_token(cursor.getString(cursor.getColumnIndex("user_device_token")));
            userClub.setUser_twitteter(cursor.getString(cursor.getColumnIndex("user_twitteter")));
            userClub.setUser_junior(cursor.getInt(cursor.getColumnIndex("user_junior")));
            userClub.setUser_instagram(cursor.getString(cursor.getColumnIndex("user_instagram")));
            userClub.setUser_email(cursor.getString(cursor.getColumnIndex("user_email")));
            userClub.setUser_facebook(cursor.getString(cursor.getColumnIndex("user_facebook")));
            userClub.setUser_first_name(cursor.getString(cursor.getColumnIndex("user_first_name")));
            userClub.setUser_last_name(cursor.getString(cursor.getColumnIndex("user_last_name")));
            userClub.setUser_cost_per_hour(cursor.getInt(cursor.getColumnIndex("user_cost_per_hour")));
            userClub.setUser_expired_staus(cursor.getInt(cursor.getColumnIndex("user_expired_staus")));
            userClub.setUser_rating(cursor.getString(cursor.getColumnIndex("user_rating")));
            userClub.setUser_club_id(cursor.getInt(cursor.getColumnIndex("user_club_id")));
            userClub.setClub_rating_type(cursor.getInt(cursor.getColumnIndex("club_rating_type")));
            userClub.setClub_rating(cursor.getString(cursor.getColumnIndex("club_rating")));
            userClub.setUser_id(cursor.getInt(cursor.getColumnIndex("user_id")));
            userClub.setClub_score_view(cursor.getInt(cursor.getColumnIndex("club_score_view")));
            userClub.setSport_name(cursor.getString(cursor.getColumnIndex("sport_name")));
            userClub.setSport_player(cursor.getInt(cursor.getColumnIndex("sport_player")));
            userClub.setUser_profilepic(cursor.getString(cursor.getColumnIndex("user_profilepic")));
            userClub.setSport_field_name(cursor.getString(cursor.getColumnIndex("sport_field_name")));
            userClub.setClub_status_change_date(cursor.getString(cursor.getColumnIndex("club_status_change_date")));

            userClub.setCurrencyCode(cursor.getString(cursor.getColumnIndex("currencyCode")));

        //    userClub.setSport_type(cursor.getString(cursor.getString("")));
          //  userClub.setUser_facebook(cursor.getString(cursor.getColumnIndex("user_registereddate")));

            userClubArrayList.add(userClub);
        }
        return  userClubArrayList ;

    }










    public UserClub getClubById(int club_id)
    {


        Cursor cursor = getWritableDatabase().rawQuery("Select *from userclub where club_id ='"+club_id+"'", null);
        UserClub userClub = new UserClub();
        if (cursor.moveToNext())
        {

            userClub.setClub_id(cursor.getInt(0));
            userClub.setClub_name(cursor.getString(1));
            userClub.setClub_address(cursor.getString(2));
            userClub.setClub_country(cursor.getString(3));
            userClub.setClublat(cursor.getLong(4));
            userClub.setClublong(cursor.getLong(5));
            userClub.setClub_num_of_courts(6);
            userClub.setClub_status_message(cursor.getString(7));
            userClub.setClub_logo(cursor.getString(8));
            userClub.setClub_sport_id(cursor.getString(cursor.getColumnIndex("club_sport_id")));
            userClub.setClub_accounts_email1(cursor.getString(cursor.getColumnIndex("club_accounts_email1")));
            userClub.setClub_ratting_show(cursor.getInt(cursor.getColumnIndex("club_ratting_show")));
            userClub.setClub_status(cursor.getInt(cursor.getColumnIndex("club_status")));
            userClub.setClubtype(cursor.getInt(cursor.getColumnIndex("clubtype")));
            userClub.setPurchase_expiring(cursor.getString(cursor.getColumnIndex("purchase_expiring")));
            userClub.setDemo_period(cursor.getInt(cursor.getColumnIndex("demo_period")));
            userClub.setClub_temp_id(cursor.getInt(cursor.getColumnIndex("club_temp_id")));
            userClub.setClub_temp_date(cursor.getString(cursor.getColumnIndex("club_temp_date")));
            userClub.setClub_temp_opencourts(cursor.getInt(cursor.getColumnIndex("club_temp_opencourts")));
            userClub.setUser_login_app(cursor.getInt(cursor.getColumnIndex("user_login_app")));
            userClub.setUser_type(cursor.getInt(cursor.getColumnIndex("user_type")));
            userClub.setClub_address(cursor.getString(cursor.getColumnIndex("club_address")));
            userClub.setUser_about(cursor.getString(cursor.getColumnIndex("user_about")));
            userClub.setUser_phone(cursor.getString(cursor.getColumnIndex("user_phone")));
            userClub.setUser_registereddate(cursor.getString(cursor.getColumnIndex("user_registereddate")));
            userClub.setUser_gender(cursor.getInt(cursor.getColumnIndex("user_gender")));
            userClub.setUser_device_token(cursor.getString(cursor.getColumnIndex("user_device_token")));
            userClub.setUser_twitteter(cursor.getString(cursor.getColumnIndex("user_twitteter")));
            userClub.setUser_junior(cursor.getInt(cursor.getColumnIndex("user_junior")));
            userClub.setUser_instagram(cursor.getString(cursor.getColumnIndex("user_instagram")));
            userClub.setUser_email(cursor.getString(cursor.getColumnIndex("user_email")));
            userClub.setUser_facebook(cursor.getString(cursor.getColumnIndex("user_facebook")));
            userClub.setUser_first_name(cursor.getString(cursor.getColumnIndex("user_first_name")));
            userClub.setUser_last_name(cursor.getString(cursor.getColumnIndex("user_last_name")));
            userClub.setUser_cost_per_hour(cursor.getInt(cursor.getColumnIndex("user_cost_per_hour")));
            userClub.setUser_expired_staus(cursor.getInt(cursor.getColumnIndex("user_expired_staus")));
            userClub.setUser_rating(cursor.getString(cursor.getColumnIndex("user_rating")));
            userClub.setUser_club_id(cursor.getInt(cursor.getColumnIndex("user_club_id")));
            userClub.setClub_rating_type(cursor.getInt(cursor.getColumnIndex("club_rating_type")));
            userClub.setClub_rating(cursor.getString(cursor.getColumnIndex("club_rating")));
            userClub.setUser_id(cursor.getInt(cursor.getColumnIndex("user_id")));
            userClub.setClub_score_view(cursor.getInt(cursor.getColumnIndex("club_score_view")));
            userClub.setSport_name(cursor.getString(cursor.getColumnIndex("sport_name")));
            userClub.setSport_player(cursor.getInt(cursor.getColumnIndex("sport_player")));
            userClub.setUser_profilepic(cursor.getString(cursor.getColumnIndex("user_profilepic")));
            userClub.setSport_field_name(cursor.getString(cursor.getColumnIndex("sport_field_name")));
            userClub.setClub_status_change_date(cursor.getString(cursor.getColumnIndex("club_status_change_date")));
            userClub.setCurrencyCode(cursor.getString(cursor.getColumnIndex("currencyCode")));

            //    userClub.setSport_type(cursor.getString(cursor.getString("")));
            //  userClub.setUser_facebook(cursor.getString(cursor.getColumnIndex("user_registereddate")));


        }
        return   userClub;

    }







public  void putDataInList(ArrayList<MemberListBean> memberMainList , int club_id)
{

    SQLiteDatabase sqLiteOpenHelper = getWritableDatabase();

    sqLiteOpenHelper.delete("member" , "club_id =?",new String[]{club_id+""});

    for (int i = 0 ;i < memberMainList.size() ;i++)
    {
       ContentValues contentValues = new ContentValues();
        contentValues.put("user_id" , memberMainList.get(i).getUser_id());

        contentValues.put("user_first_name" , memberMainList.get(i).getUser_first_name());

        contentValues.put("user_last_name" , memberMainList.get(i).getUser_last_name());

        contentValues.put("user_email" , memberMainList.get(i).getUser_email());

        contentValues.put("user_mobile_no" , memberMainList.get(i).getUser_phone());
        contentValues.put("user_type" , memberMainList.get(i).getUser_type());


        contentValues.put("profile_pic" , memberMainList.get(i).getUser_profilepic());

        contentValues.put("user_rating" , memberMainList.get(i).getUser_rating());
        contentValues.put("user_junior" , memberMainList.get(i).getUser_junior());

        contentValues.put("user_gender" , memberMainList.get(i).getUser_gender());
        contentValues.put("user_status" , memberMainList.get(i).getUser_status());


        contentValues.put("club_id" , club_id);

        //club_id
        sqLiteOpenHelper.insert("member" , null,contentValues);

// test,
    }
}

 public  ArrayList<MemberListBean> memberListArrayList(int clubsId , int userType)
 {
     ArrayList<MemberListBean>memberListBeanArrayList = new ArrayList<>();

     Cursor cursor = getReadableDatabase().rawQuery("Select *from member where club_id = '"+clubsId+"'", null);


     while (cursor.moveToNext()) {
         MemberListBean memberListBean = new MemberListBean();
         memberListBean.setUser_id(cursor.getString(0));
         memberListBean.setUser_first_name(cursor.getString(1));
         memberListBean.setUser_last_name(cursor.getString(2));
         memberListBean.setUser_email(cursor.getString(3));
         memberListBean.setUser_phone(cursor.getString(4));
         memberListBean.setUser_profilepic(cursor.getString(5));

         memberListBean.setUser_rating(cursor.getString(6));
         memberListBean.setUser_junior(cursor.getString(7));
         memberListBean.setUser_gender(cursor.getString(8));
         memberListBean.setUser_type(cursor.getString(9));
         memberListBean.setUser_status(cursor.getString(10));
/////////////////////////user_id integer ," +
             memberListBean.setMemberSelection(true);

         if (userType == AppConstants.AllMEMBERlIST) {
             memberListBeanArrayList.add(memberListBean);

         } else {
             if (userType == AppConstants.ADMINLIST) {
                 if (memberListBean.getUser_type().equals(AppConstants.USER_TYPE_MOBILE_ADMIN)) {
                     memberListBeanArrayList.add(memberListBean);

                 }
             }
                 else {


                     if (userType == AppConstants.DIRECTORADMINLIST) {
                         if (memberListBean.getUser_type().equals
                                 (AppConstants.USER_TYPE_DIRECTOR) || memberListBean.getUser_type().equals
                                 (AppConstants.USER_TYPE_MOBILE_ADMIN)) {
                             memberListBeanArrayList.add(memberListBean);

                         }
                     }

                 }
             }




     }

     return memberListBeanArrayList;
 }









    public  ArrayList<MemberListBean> memberListArrayList(int clubsId , int userType , String keyWord)
    {
        ArrayList<MemberListBean>memberListBeanArrayList = new ArrayList<>();

        Cursor cursor = getReadableDatabase().rawQuery("Select *from member where club_id = '"+clubsId+"'", null);


        while (cursor.moveToNext()) {
            MemberListBean memberListBean = new MemberListBean();
            memberListBean.setUser_id(cursor.getString(0));
            memberListBean.setUser_first_name(cursor.getString(1));
            memberListBean.setUser_last_name(cursor.getString(2));
            memberListBean.setUser_email(cursor.getString(3));
            memberListBean.setUser_phone(cursor.getString(4));
            memberListBean.setUser_profilepic(cursor.getString(5));

            memberListBean.setUser_rating(cursor.getString(6));
            memberListBean.setUser_junior(cursor.getString(7));
            memberListBean.setUser_gender(cursor.getString(8));
            memberListBean.setUser_type(cursor.getString(9));
            memberListBean.setUser_status(cursor.getString(10));
/////////////////////////user_id integer ," +
            memberListBean.setMemberSelection(true);

            if (userType == AppConstants.AllMEMBERlIST)
            {
                if (memberListBean.getUser_first_name().toLowerCase().startsWith(keyWord.toLowerCase())||
                        memberListBean.getUser_last_name().toLowerCase().startsWith(keyWord.toLowerCase())||
                        memberListBean.getUser_email().toLowerCase().startsWith(keyWord.toLowerCase()))
                memberListBeanArrayList.add(memberListBean);

            } else {
                if (userType == AppConstants.ADMINLIST)
                {
                    if (memberListBean.getUser_type().equals(AppConstants.USER_TYPE_MOBILE_ADMIN))
                    {
                        memberListBeanArrayList.add(memberListBean);

                    }
                }
                else
                {


                    if (userType == AppConstants.DIRECTORADMINLIST)
                    {
                        if (memberListBean.getUser_type().equals
                                (AppConstants.USER_TYPE_DIRECTOR) || memberListBean.getUser_type().equals
                                (AppConstants.USER_TYPE_MOBILE_ADMIN))
                        {
                            memberListBeanArrayList.add(memberListBean);

                        }
                    }

                }
            }




        }

        return memberListBeanArrayList;
    }




   public void updateTimeOfMemberDirectory(int clubsId , String updateTime)
   {
       if (isClubsExitsInList(clubsId))
       {
           ContentValues contentValues = new ContentValues();
           contentValues.put("lastDateOfMemberListUpdation" ,updateTime);

           getWritableDatabase().update("memberUpdateTime" , contentValues , "club_id = ?", new String[]{clubsId+""});
       }
       else
       {
           ContentValues contentValues = new ContentValues();
           contentValues.put("club_id" ,clubsId);
           contentValues.put("lastDateOfMemberListUpdation" ,updateTime);

           getWritableDatabase().insert("memberUpdateTime" , null , contentValues);
       }




   }


    public String getLastTimeUpdate(int myClubsId)
    {
        Cursor cursor = getReadableDatabase().rawQuery("select *from memberUpdateTime where club_id = '"+myClubsId+"'" , null);


        if (cursor.moveToNext())
        {
            return cursor.getString(1);
        }
        else {
            return "";
        }
    }


   public boolean isClubsExitsInList(int myClubsId)
   {
       Cursor cursor = getReadableDatabase().rawQuery("select *from memberUpdateTime" , null);

       while (cursor.moveToNext())
       {
           int clubdId = cursor.getInt(0);

           if (myClubsId == clubdId)
           {
               return true;
           }



       }
       return false;

   }





  /*
db.execSQL("CREATE TABLE IF NOT EXISTS memberUpdateTime(club_id integer primary key," +
                "lastDateOfMemberListUpdation text)" );

memberUpdateTime
    contentValues.put("" , userClub.getClub_rating());*/


    public void deleteAllTable()
    {
        getWritableDatabase().delete("userclub",null,null);
        getWritableDatabase().delete("member",null,null);
        getWritableDatabase().delete("memberUpdateTime",null,null);

    }


AsyncTask<Void ,Void ,ArrayList<MemberListBean>> memberListbeanTask ;
    ProgressDialog progressDialog;
    ArrayList<MemberListBean> memberListBeanArrayList ;
    SessionManager sessionManager ;
   public void getMemberList(final Activity activity , final MemberListListener memberListListener , final int userType ,final String keyWord)
   {
       progressDialog = new ProgressDialog(activity);
       progressDialog.setCancelable(false);

       memberListbeanTask = new AsyncTask<Void, Void, ArrayList<MemberListBean>>()
       {



           @Override
           protected void onPreExecute() {
               super.onPreExecute();
               sessionManager = new SessionManager(activity);
               memberListBeanArrayList = new ArrayList();
               progressDialog.show();


           }

           @Override
           protected ArrayList<MemberListBean> doInBackground(Void... params)
           {
               if (Validation.isStringNullOrBlank(keyWord))
               {
                   memberListBeanArrayList = memberListArrayList(Integer.parseInt(SessionManager.getUser_Club_id(activity)), userType);

               }
               else
               {
                   memberListBeanArrayList = memberListArrayList(Integer.parseInt(SessionManager.getUser_Club_id(activity)), userType ,keyWord);

               }



               return memberListBeanArrayList;
           }

           @Override
           protected void onPostExecute(ArrayList<MemberListBean> memberListBeen) {
               super.onPostExecute(memberListBeen);
               progressDialog.dismiss();
               memberListListener.onSuccess(memberListBeen);

           }
       };
       memberListbeanTask.execute();

   }

    public void changeUserType(String usertype , String userId)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_type" ,usertype);

        getWritableDatabase().update("member" , contentValues , "club_id = ? and user_id = ?",
                new String[]{SessionManager.getUser_Club_id(context), userId });

    }
    public  void updateMenberList(MemberListBean memberListBean )
    {

        SQLiteDatabase sqLiteOpenHelper = getWritableDatabase();



            ContentValues contentValues = new ContentValues();

            contentValues.put("user_first_name" , memberListBean.getUser_first_name());

            contentValues.put("user_last_name" , memberListBean.getUser_last_name());

            contentValues.put("user_email" , memberListBean.getUser_email());

            contentValues.put("user_mobile_no" ,memberListBean.getUser_phone());


            contentValues.put("profile_pic" , memberListBean.getUser_profilepic());

            contentValues.put("user_rating" , memberListBean.getUser_rating());

            contentValues.put("user_gender" , memberListBean.getUser_gender());
            contentValues.put("user_status" , memberListBean.getUser_status());



            //club_id
          sqLiteOpenHelper.update("member",contentValues , "user_id =?",new String[]{memberListBean.getUser_id()+""});

// test,

    }

    public int getTotalMemberInClubds(int clubsId)
    {

        try
        {
         // String query =  "select count(*)from tracks WHERE club_id = '"+clubsId+"'";

            getReadableDatabase().beginTransaction();
            Cursor cursor = getReadableDatabase().rawQuery("Select *from member where club_id = '"+clubsId+"'", null);
            cursor.moveToNext();

            return  cursor.getCount();
        }
        catch (Exception e)
        {
            return  0;

        }



    }


    public  void changeBlockUnBlockStaus(MemberListBean memberListBean )
    {
        SQLiteDatabase sqLiteOpenHelper = getWritableDatabase();



        ContentValues contentValues = new ContentValues();








        contentValues.put("user_status" , memberListBean.getUser_status());



        //club_id
        sqLiteOpenHelper.update("member",contentValues , "user_id =?",new String[]{memberListBean.getUser_id()+""});

    }

}





