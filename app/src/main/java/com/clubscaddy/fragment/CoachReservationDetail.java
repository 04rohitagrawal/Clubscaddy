package com.clubscaddy.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.clubscaddy.Adapter.CoachReservationListAdapter;
import com.clubscaddy.Bean.CoachReserve;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.DialogBoxButtonListner;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.R;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by administrator on 29/4/17.
 */

public class CoachReservationDetail extends Fragment
{

    View convertView ;
    HttpRequest httpRequest;
    String memberBookedId ;
    ListView reservation_list_view;
    ShowUserMessage showMessage;
    ArrayList<CoachReserve>coachreservatinList ;
    TextView delete_btn;
;
    TextView coach_name_tv;
    String coach_mem_id ;
    String recursiveId;
    TextView cancel_dialog_tv , done_tv;
    String coachName;
    public void setInstanse(String memberBookedId , String recursiveId , String coach_mem_id , String coachName)
    {
        this.memberBookedId = memberBookedId ;
        this.recursiveId = recursiveId ;
        this.coach_mem_id = coach_mem_id ;
        this.coachName = coachName ;
    }


    LinearLayout deleteBtnLayout ;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        httpRequest = new HttpRequest(getActivity());
        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.coach_res_detail_layout , null);
        reservation_list_view = (ListView) convertView.findViewById(R.id.reservation_list_view);
        deleteBtnLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.my_reservation_delete_btn , null);
        delete_btn = (TextView) deleteBtnLayout.findViewById(R.id.delete_btn);
        coach_name_tv = (TextView) convertView.findViewById(R.id.coach_name_tv);
        coach_name_tv.setText(coachName+"");

        reservation_list_view.addFooterView(deleteBtnLayout);
//

        if (DirectorFragmentManageActivity.actionbar_titletext != null) {
            DirectorFragmentManageActivity.updateTitle("Coach Reservation Details");
        }
        coachreservatinList = new ArrayList<>();
        showMessage = new ShowUserMessage(getActivity());

        //

        if (coach_mem_id.equals(SessionManager.getUser_id(getActivity())))
        {
            setCoachReservationList();
        }
        else
        {
            setMyCoachReservationList();
        }




        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage.showDialogBoxWithYesNoButton("Are you sure you want to delete this reservation?", new DialogBoxButtonListner() {
                    @Override
                    public void onYesButtonClick(DialogInterface dialogInterface) {

                        dialogInterface.dismiss();
                        //

                      //  String memberbookid = coachSlotArrayList.get(row).get(cloumn).getMemberbookedid();
                        //   Toast.makeText(getActivity() , "memberbookid "+memberbookid ,1).show();



                        final Dialog dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.confirm_dialog_delete_coach_reser);
                        dialog.setCancelable(false);
                        dialog.getWindow().setLayout(AppConstants.getDeviceWidth(getActivity()), LinearLayout.LayoutParams.WRAP_CONTENT);
                        dialog.show();
                        TextView title_tv = (TextView) dialog.findViewById(R.id.title_tv);
                        title_tv.setText(SessionManager.getClubName(getActivity()));
                        final EditText cancel_reason_msg_edittv = (EditText) dialog.findViewById(R.id.cancel_reason_msg_edittv);

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

                                if (Validation.isStringNullOrBlank(cancel_reason_msg_edittv.getText().toString()))
                                {
                                    ShowUserMessage.showUserMessage(getActivity() , "Please enter reason");
                                    return;
                                }

                                hideKeyBoard(cancel_reason_msg_edittv);

                                dialog.cancel();
                                if (memberBookedId.equals("0") == false)
                                {
                                    deleteReservation(coach_mem_id, memberBookedId , cancel_reason_msg_edittv.getText().toString());                        }
                                else
                                {
                                    deleteRecursiveReservation(coach_mem_id , cancel_reason_msg_edittv.getText().toString());                        }
                            }
                        });
















                    }
                });
            }
        });



        return convertView;
    }

    public void setCoachReservationList()
    {
        HashMap<String , Object>param = new HashMap<String , Object>();

        param.put("coach_club_id", SessionManager.getUser_Club_id(getActivity()));
        if (memberBookedId.equals("0") ==false)
        {
            param.put("memberbookedid", memberBookedId);
        }
        else
        {
            param.put("coach_reservation_recursive_id", recursiveId);
        }

        param.put("coach_mem_id", SessionManager.getUserId(getActivity()));



        httpRequest.getResponse(getActivity(), WebService.mycoachreserve, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                Log.e("jsonObject" ,jsonObject+"");


                try
                {
                    if (jsonObject.getBoolean("status"))
                    {
                        JSONArray coach_reserve_json_array = jsonObject.getJSONArray("book");
                        for (int i = 0 ; i < coach_reserve_json_array.length() ;i++)
                        {
                            JSONObject coach_reserve_json_array_item = coach_reserve_json_array.getJSONObject(i);
                            CoachReserve coachReserve = new CoachReserve();
                            coachReserve.setCoach_reservation_date(coach_reserve_json_array_item.getString("coach_reservation_date"));
                           // coachReserve.setMemberbookedid(coach_reserve_json_array_item.getString("memberbookedid"));
                            //coachReserve.setCoach_reservation_end_datetime(coach_reserve_json_array_item.getString("coach_reservation_end_datetime"));
                            coachReserve.setCoach_reservation_start_datetime(coach_reserve_json_array_item.getString("coach_reservation_time"));
                           // coachReserve.setCoach_coach_id(coach_reserve_json_array_item.getString("coach_coach_id"));
                           // coachReserve.setCoach_reservation_id(coach_reserve_json_array_item.getString("coach_reservation_id"));
                            //coachReserve.setCoach_club_id(coach_reserve_json_array_item.getString("coach_club_id"));
                            //coachReserve.setCoach_mem_id(coach_reserve_json_array_item.getString("coach_mem_id"));
                            //coachReserve.setCoach_name(coach_reserve_json_array_item.getString("coach_name"));
                           // coachName = coach_reserve_json_array_item.getString("coach_name");
                            coachreservatinList.add(coachReserve);
                        }
                        //coach_name_tv.setText(coachName);
                        reservation_list_view.setAdapter(new CoachReservationListAdapter(  coachreservatinList ,getActivity()));
                    }
                    else
                    {
                        ShowUserMessage.showUserMessage(getActivity() ,jsonObject.getString("message") );
                    }




                }
                catch (Exception e)
                {
                    ShowUserMessage.showUserMessage(getActivity() ,e.getMessage() );
                }
            }
        });
    }




    public void setMyCoachReservationList()
    {
        HashMap<String , Object>param = new HashMap<String , Object>();

        param.put("coach_club_id", SessionManager.getUser_Club_id(getActivity()));
        if (memberBookedId.equals("0") == false)
        {
            param.put("memberbookedid", memberBookedId);
        }
        else
        {
            param.put("coach_reservation_recursive_id", recursiveId);
        }
        param.put("coach_coach_id", SessionManager.getUserId(getActivity()));



        httpRequest.getResponse(getActivity(), WebService.coachmemberbookinglist, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                Log.e("jsonObject" ,jsonObject+"");


                try
                {
                    if (jsonObject.getBoolean("status"))
                    {
                        JSONArray coach_reserve_json_array = jsonObject.getJSONArray("book");
                        for (int i = 0 ; i < coach_reserve_json_array.length() ;i++)
                        {
                            JSONObject coach_reserve_json_array_item = coach_reserve_json_array.getJSONObject(i);
                            CoachReserve coachReserve = new CoachReserve();
                            coachReserve.setCoach_reservation_date(coach_reserve_json_array_item.getString("coach_reservation_date"));
                          //  coachReserve.setMemberbookedid(coach_reserve_json_array_item.getString("memberbookedid"));
                            //coachReserve.setCoach_reservation_end_datetime(coach_reserve_json_array_item.getString("coach_reservation_end_datetime"));
                           coachReserve.setCoach_reservation_start_datetime(coach_reserve_json_array_item.getString("coach_reservation_time"));
                           // coachReserve.setCoach_coach_id(coach_reserve_json_array_item.getString("coach_coach_id"));
                          //  coachReserve.setCoach_reservation_id(coach_reserve_json_array_item.getString("coach_reservation_id"));
                          //  coachReserve.setCoach_club_id(coach_reserve_json_array_item.getString("coach_club_id"));
                          //  coachReserve.setCoach_mem_id(coach_reserve_json_array_item.getString("coach_mem_id"));
                         //   coachReserve.setCoach_name(coach_reserve_json_array_item.getString("member_name"));
                            coachreservatinList.add(coachReserve);
                        }
                        //coach_name_tv.setText(coachName);
                        reservation_list_view.setAdapter(new CoachReservationListAdapter(  coachreservatinList ,getActivity()));
                    }
                    else
                    {
                        ShowUserMessage.showUserMessage(getActivity() ,jsonObject.getString("message") );
                    }




                }
                catch (Exception e)
                {
                    ShowUserMessage.showUserMessage(getActivity() ,e.getMessage() );
                }
            }
        });
    }



    public void deleteReservation(String coach_mem_id , String memberbookedid , String reasonMsg)
    {
        HashMap<String , Object>param = new HashMap<String , Object>();
        param.put("coach_user_id" ,SessionManager.getUserId(getActivity()));
        param.put("coach_club_id" ,SessionManager.getUser_Club_id(getActivity()));
        param.put("coach_mem_id" ,coach_mem_id);
        param.put("memberbookedid" ,memberbookedid);
        param.put("reason" ,reasonMsg);



        httpRequest.getResponse(getActivity(), WebService.deletecoachbooking, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                try
                {
                    showMessage.showDialogOnFragmentWithBack(getActivity() , jsonObject.getString("message"));
                    //showMessage(jsonObject.getString("message"));
                }
                catch (Exception e)
                {

                }



                //  ShowUserMessage.showUserMessage(getActivity() ,jsonObject.toString());
            }
        });



    }





    public void deleteRecursiveReservation(String coach_mem_id ,String reasonMsg )
    {
        HashMap<String , Object>param = new HashMap<String , Object>();
        param.put("coach_user_id" ,SessionManager.getUserId(getActivity()));
        param.put("coach_club_id" ,SessionManager.getUser_Club_id(getActivity()));
        param.put("coach_mem_id" ,coach_mem_id);
        param.put("reason" ,reasonMsg);
        param.put("coach_recursiveid", recursiveId);


        httpRequest.getResponse(getActivity(), WebService.coach_deleterecursive, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                try
                {
                    showMessage.showDialogOnFragmentWithBack(getActivity() , jsonObject.getString("message"));
                    //showMessage(jsonObject.getString("message"));
                }
                catch (Exception e)
                {

                }



                //  ShowUserMessage.showUserMessage(getActivity() ,jsonObject.toString());
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


}
