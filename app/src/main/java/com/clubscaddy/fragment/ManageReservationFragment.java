package com.clubscaddy.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.clubscaddy.Adapter.CustumSpinnerListAdapter;
import com.clubscaddy.Adapter.TimeSloatAdapter;
import com.clubscaddy.Bean.CourtData;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by administrator on 12/1/17.
 */

public class ManageReservationFragment extends Fragment
{
    View convertView ;
   Button okbutton;
    CheckBox on_radio_cb;
    CheckBox off_radio_cb;
    SessionManager sessionManager;
    EditText reservation_edit_tv;
    TextView discription_textview_status;
    Spinner time_list_spinner;
    LinearLayout layout;
    ArrayList<String> timeSloatList;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        convertView = inflater.inflate(R.layout.manage_reservation ,null);
        okbutton = (Button) convertView.findViewById(R.id.okbutton);
        on_radio_cb = (CheckBox) convertView.findViewById(R.id.on_radio_cb);
        off_radio_cb = (CheckBox) convertView.findViewById(R.id.off_radio_cb);
        layout = (LinearLayout) convertView.findViewById(R.id.layout);
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if (DirectorFragmentManageActivity.actionbar_titletext != null) {
            DirectorFragmentManageActivity.updateTitle("Calendar Settings");
        }


        sessionManager = new SessionManager();

        off_radio_cb.setOnClickListener(onClickListener);
        on_radio_cb.setOnClickListener(onClickListener);
        okbutton.setOnClickListener(onClickListener);


        if (sessionManager.isSaveEventInCaleneder(getActivity()))
        {
            on_radio_cb.setChecked(true);
            layout.setVisibility(View.VISIBLE);
        }
        else
        {
            off_radio_cb.setChecked(true);
            layout.setVisibility(View.GONE);
        }






          reservation_edit_tv = (EditText) convertView.findViewById(R.id.reservation_edit_tv);

        reservation_edit_tv.setText(sessionManager.getCalenderEntryTitle(getActivity()));

         discription_textview_status = (TextView) convertView.findViewById(R.id.discription_textview_status);
        reservation_edit_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                discription_textview_status.setText(s.toString().length() + "/50");
            }
        });
          time_list_spinner = (Spinner) convertView.findViewById(R.id.time_list_spinner);
          timeSloatList = new ArrayList<>();
        timeSloatList.add("None");
        timeSloatList.add("30 min before");
        timeSloatList.add("1 hour before");
        timeSloatList.add("2 hour before");
        timeSloatList.add("1 day before");

        time_list_spinner.setAdapter(new CustumSpinnerListAdapter(getActivity(), timeSloatList , time_list_spinner));

        try
        {
            int  reminderMinut = Integer.parseInt(sessionManager.getAlaramTimeDuration(getActivity()).split(" ")[0]);

            if (reminderMinut == 0)
            {
                time_list_spinner.setSelection(0);
            }
            if (reminderMinut == 30)
            {
                time_list_spinner.setSelection(1);
            }
            if (reminderMinut == 60)
            {
                time_list_spinner.setSelection(2);
            }
            if (reminderMinut == 120)
            {
                time_list_spinner.setSelection(3);
            }
            if (reminderMinut == 1440)
            {
                time_list_spinner.setSelection(4);
            }
        }
        catch (Exception e)
        {

        }




















        return convertView;
    }
     public View.OnClickListener onClickListener = new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             if (v.getId() == R.id.okbutton)
             {
                 if (on_radio_cb.isChecked())
                 {


                     String timeSlot = "30 Minut";
                     if (time_list_spinner.getSelectedItemPosition() == 0) {
                         timeSlot = "0 Minut";
                     }
                     if (time_list_spinner.getSelectedItemPosition() == 1) {
                         timeSlot = "30 Minut";
                     }
                     if (time_list_spinner.getSelectedItemPosition() == 2) {
                         timeSlot = "60 Minut";
                     }
                     if (time_list_spinner.getSelectedItemPosition() == 3) {
                         timeSlot = "120 Minut";
                     }
                     if (time_list_spinner.getSelectedItemPosition() == 4) {
                         timeSlot = "1440 Minut";
                     }


                     sessionManager.setSaveEventInCaleneder(getActivity() , true ,reservation_edit_tv.getText().toString() ,timeSlot );

                 }
                 else
                 {
                     sessionManager.setSaveEventInCaleneder(getActivity() , false  ,reservation_edit_tv.getText().toString() ,0+"") ;

                 }
                 getActivity().getSupportFragmentManager().popBackStack();
             }
             if (v.getId() == R.id.on_radio_cb)
             {
                 on_radio_cb.setChecked(true);
                 off_radio_cb.setChecked(false);
                 layout.setVisibility(View.VISIBLE);

             }
             if (v.getId() == R.id.off_radio_cb)
             {
                 on_radio_cb.setChecked(false);
                 off_radio_cb.setChecked(true);
                 layout.setVisibility(View.GONE);

             }

         }
     };
    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // Log.e(Tag, "onDestroyView");
       // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        DirectorFragmentManageActivity.isbackPress = false;
    }

/*
    public void showRecursiveSelector(final CourtData courtBeab) {

        final Dialog alertDialog = new Dialog(getActivity());
        //LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.listview);

        //	final AlertDialog alertDialog = new AlertDialog.Builder(mContext,R.style.CustomDialog).create();

        ListView listView = (ListView) alertDialog.findViewById(R.id.list);

        TextView header = (TextView) alertDialog.findViewById(R.id.header);
        header.setText(SessionManager.getClubName(getActivity()));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.simpletextview, deleteRecursiveArray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        alertDialog.getWindow().setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.white_circle_back));     *//*(Color.parseColor("#FFFFFFFF"))*//*
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
        params.put("user_type", SessionManager.getUser_type(getActivity()));
        params.put("court_reservation_recursiveid", bean.getRecursive_id());









        JSONArray jArray = new JSONArray();
        JSONObject slotjson = new JSONObject();



        params.put("slots", "");

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
    }*/


}
