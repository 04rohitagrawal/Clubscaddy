package com.clubscaddy.BackGroundServies;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.Interface.BackServiceRespondingListener;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.SqlListe;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by administrator on 30/6/18.
 */
public class FetchMemberListInBack extends Service
{


    Context context ;
    HttpRequest httpRequest ;
    String lastUpdateTimeOfLocal ;
    SessionManager sessionManager ;
    int clubsId ;
    String lastUpdateTime ;
    SqlListe sqLiteDatabase ;
    SimpleDateFormat simpleDateFormat ;
    Calendar lastUpdateTimeOfLocalCal;
    Calendar lastUpdateTimeCal ;
    //String currentTime ;
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
//

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {





        try {
            context = getApplicationContext() ;
            httpRequest = new HttpRequest(context);
            sessionManager = new SessionManager(context);
            clubsId =  	Integer.parseInt(SessionManager.getUser_Club_id(context));
            sqLiteDatabase = new SqlListe(context);
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            lastUpdateTimeOfLocalCal = Calendar.getInstance(Locale.ENGLISH);
             lastUpdateTimeOfLocal = sqLiteDatabase.getLastTimeUpdate(clubsId);
            //2018-07-08 00:22:56//2018-07-07 19:02:51
            if (Validation.isStringNullOrBlank(lastUpdateTimeOfLocal))
            {
                lastUpdateTimeOfLocal = "2018-07-23 01:00:00";
            }
            lastUpdateTimeOfLocalCal.setTime(simpleDateFormat.parse(lastUpdateTimeOfLocal));



            lastUpdateTimeCal = Calendar.getInstance(Locale.ENGLISH);
            lastUpdateTime = sessionManager.getLastDateOfMemberListUpdation();
            if (!Validation.isStringNullOrBlank(lastUpdateTime))
            lastUpdateTimeCal.setTime(simpleDateFormat.parse(lastUpdateTime));


            //ArrayList<MemberListBean> alMemberList = sqLiteDatabase.memberListArrayList(clubsId , AppConstants.AllMEMBERlIST);

            if (Validation.isStringNullOrBlank(lastUpdateTime) == false &&
                    Utill.isEqualDateAndTime(lastUpdateTimeOfLocalCal , lastUpdateTimeCal) )
            {

                stopSelf();
            }
            else
            {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("user_type", AppConstants.USER_TYPE_DIRECTOR);
                params.put("user_club_id", SessionManager.getUser_Club_id(context));
                httpRequest.getResponseInBackGround(WebService.allMemberList, params, new BackServiceRespondingListener() {
                    @Override
                    public void onSuccess(final JSONObject jsonObject)
                    {
                        final ArrayList<MemberListBean> alMemberList =
                                JsonUtility.parserMembersList(jsonObject.toString(), context);

                        //currentTime = sessionManager.getCurrentTime();
                       String lastUpdateDate = jsonObject.optString("last_update_date","2018-07-23 01:00:00");
                        sqLiteDatabase.updateTimeOfMemberDirectory(clubsId , lastUpdateDate);

                        sqLiteDatabase.putDataInList(alMemberList,clubsId);

                      //  Toast.makeText(getApplicationContext() , "Contact List Updated",Toast.LENGTH_SHORT).show();

                    }
                });
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }







        return START_STICKY;
    }


    public void getMemberList()
    {

    }



}
