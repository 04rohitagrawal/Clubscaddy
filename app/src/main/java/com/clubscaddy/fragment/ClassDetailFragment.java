package com.clubscaddy.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clubscaddy.Adapter.ClassDetailListAdapter;
import com.clubscaddy.Bean.ClassDetail;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.DialogBoxButtonListner;
import com.clubscaddy.Interface.FragmentBackResponseListener;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.Interface.RecycleViewItemClickListner;
import com.clubscaddy.R;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by administrator on 8/12/17.
 */

public class ClassDetailFragment extends Fragment
{
    View convertView ;
    RecyclerView classReservationListView ;
    String classDetailId ;

    HttpRequest httpRequest ;
    HashMap<String , Object> param;
    ArrayList<ClassDetail> classDetailArrayList ;
    ClassDetailListAdapter classDetailListAdapter ;
    String priviousHeader ;
    FragmentBackResponseListener fragmentBackResponseListener ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.class_detail_fragment , null);
        httpRequest = new HttpRequest(getActivity());

        classDetailArrayList = new ArrayList<>();
        showUserMessage = new ShowUserMessage(getActivity());


        fragmentBackResponseListener = (FragmentBackResponseListener) getArguments().getSerializable("updateclassListListener");


        priviousHeader = DirectorFragmentManageActivity.actionbar_titletext.getText().toString();


        DirectorFragmentManageActivity.actionbar_titletext.setText(getArguments().getString("header_name"));


        DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.VISIBLE);

        DirectorFragmentManageActivity.uploadNewsOrEditProfile.setImageDrawable(getResources().getDrawable(R.drawable.delete_btn));


        DirectorFragmentManageActivity.uploadNewsOrEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showUserMessage.showDialogBoxWithYesNoButton("Are you sure you want to delete this class?", new DialogBoxButtonListner() {
                    @Override
                    public void onYesButtonClick(DialogInterface dialog)
                    {
                        try
                        {
                            deleteClassFromServer(classDetailArrayList.get(1).getClassDetailId());

                        }
                        catch (Exception e)
                        {

                        }

                    }
                });
            }
        });

        classReservationListView = (RecyclerView) convertView.findViewById(R.id.class_reservation_list_view);
        classDetailListAdapter = new ClassDetailListAdapter(getActivity(), classDetailArrayList, new RecycleViewItemClickListner() {
            @Override
            public void onItemClick(final int pos, int status)
            {

                showUserMessage.showDialogBoxWithYesNoButton("Are you sure you want to delete class reservation?", new DialogBoxButtonListner() {
                    @Override
                    public void onYesButtonClick(DialogInterface dialog)
                    {

                        if (classDetailArrayList.size() == 2)
                        {
                            deleteClassFromServer(classDetailArrayList.get(pos).getClassDetailId() );

                        }
                        else
                        {
                            deleteClassReservationFromServer(classDetailArrayList.get(pos).getClassDetailId() , pos);

                        }


                    }
                });

            }
        });

        classReservationListView.setAdapter(classDetailListAdapter);
        classReservationListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        classDetailId = getArguments().getString("class_detail_id" , "");


        getClassDetailFromServer();

        return convertView;
    }


    public  void getClassDetailFromServer()
    {
        param = new HashMap<>();
        param.put("class_id" , classDetailId);
        param.put("class_uid" , SessionManager.getUser_id(getActivity()));
        param.put("user_type" , SessionManager.getUser_type(getActivity()));
        param.put("class_club_id" , SessionManager.getUser_Club_id(getActivity()));



        httpRequest.getResponse(getActivity(), WebService.class_detail_list, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                try
                {
                    if (jsonObject.getBoolean("status"))
                    {
                        JSONArray classDetailJsonArray = jsonObject.getJSONArray("Response");

                        for (int i = 0 ; i < classDetailJsonArray.length() ;i++ )
                        {
                            JSONObject classDetailJsonArrayItem = classDetailJsonArray.getJSONObject(i);
                            ClassDetail classDetail = new ClassDetail();
                            classDetail.setClassDetailId(classDetailJsonArrayItem.getString("class_detail_id"));
                            classDetail.setClassDate(classDetailJsonArrayItem.getString("class_name"));
                            classDetail.setClassDate(classDetailJsonArrayItem.getString("class_date"));
                            classDetail.setClassStartTime(classDetailJsonArrayItem.getString("class_startTime"));
                            classDetail.setClassEndTime(classDetailJsonArrayItem.getString("class_endTime"));
                            classDetailArrayList.add(classDetail);

                        }
                        classDetailListAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        showUserMessage.showDialogOnFragmentWithBack(getActivity() , jsonObject.getString("message"));
                    }


                }
                catch (Exception e)
                {

                }
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DirectorFragmentManageActivity.actionbar_titletext.setText(priviousHeader);
        DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.GONE);

    }
    ShowUserMessage showUserMessage ;

    public void deleteClassFromServer(String classDetailId)
    {


        HashMap<String , Object> param = new HashMap<>();
        param.put("class_uid" , SessionManager.getUser_id(getActivity()));
        param.put("class_detail_id" , classDetailId);
        param.put("user_type" , SessionManager.getUser_type(getActivity()));
        param.put("class_club_id" , SessionManager.getUser_Club_id(getActivity()));
        param.put("delete_type" , "1");

        httpRequest.getResponse(getActivity(), WebService.delete_class, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                try
                {
                    if (jsonObject.getBoolean("status") == false)
                    {
                        Utill.showDialg(jsonObject.getString("message") , getActivity());

                    }
                    else
                    {
                        showDialogOnFragmentWithBack(getActivity() , jsonObject.getString("message"));


                    }
                }
                catch (Exception e)
                {

                }
            }
        });

    }




    public void deleteClassReservationFromServer(String classDetailId , final  int position)
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
                        classDetailArrayList.remove(position);
                        classDetailListAdapter.notifyDataSetChanged();

                    }

                }
                catch (Exception e)
                {

                }
            }
        });

    }

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
                fragmentBackResponseListener.onBackFragment();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }



}
