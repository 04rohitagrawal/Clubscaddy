package com.clubscaddy.fragment;

import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.clubscaddy.Adapter.ClassifiedCommentAdapter;
import com.clubscaddy.Adapter.ClassifiedListAdapter;
import com.clubscaddy.Bean.ClassifiedCommentBean;
import com.clubscaddy.Bean.ClassifiedItem;
import com.clubscaddy.Bean.ClassifiedItemOtherPicture;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.R;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by administrator on 4/5/17.
 */

public class ClassifiedFragment extends Fragment implements View.OnClickListener
{
    View convertView ;
    ImageView add_btn;
    HttpRequest httpRequest ;
    ArrayList<ClassifiedItem>classifiedItemArrayList ;

    //EditText search_edit_tv;

    public EditClassifedListener editClassifedListener ;

    ClassifiedListAdapter classifiedListAdapter ;
    RecyclerView classified_item_listview;

    private GoogleApiClient mGoogleApiClient;
    private Location mStartLocation;
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    LinearLayout classified_list_tab;
    LinearLayout my_classified_list_tab;

    EditText search_edit_tv;

    LinearLayout tab_layout;

    public boolean isWebServicesisCalling = false;
    public String searchingWord = "" ;
    public String minimumValue = "";
    public String maximumValue = "" ;
    public String lastCounterId = "" ;
    public int listIdentifire =1;
    public  int lastCallItemCount =  0 ;
    ProgressBar progressbar;
    TextView no_data_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.classified_frag_layout , null);

        httpRequest = new HttpRequest(getActivity());
        classifiedItemArrayList = new ArrayList<>();



        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //copyClassifiedItemArrayList  = new ArrayList<>();
        classified_item_listview = (RecyclerView) convertView.findViewById(R.id.classified_item_listview);
        progressbar = (ProgressBar) convertView.findViewById(R.id.progressbar);
      //
        listIdentifire =1;
        editClassifedListener = new EditClassifedListener();
        classifiedListAdapter = null ;

        no_data_tv = (TextView) convertView.findViewById(R.id.no_data_tv);


        classified_list_tab = (LinearLayout) convertView.findViewById(R.id.classified_list_tab);
        my_classified_list_tab = (LinearLayout) convertView.findViewById(R.id.my_classified_list_tab);
        classified_list_tab.setOnClickListener(tabClicklistener);
        my_classified_list_tab.setOnClickListener(tabClicklistener);


        DirectorFragmentManageActivity.logoutButton.setVisibility(View.VISIBLE);
        DirectorFragmentManageActivity.logoutButton.setImageDrawable(getResources().getDrawable(R.drawable.filter));

        DirectorFragmentManageActivity.logoutButton.setOnClickListener(filterClickListener);
        DirectorFragmentManageActivity.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().popBackStack();
                Utill.hideKeybord(getActivity());

            }
        });
        DirectorFragmentManageActivity.updateTitle("Classified List");
        DirectorFragmentManageActivity.backButton.setVisibility(View.VISIBLE);
        add_btn = (ImageView) convertView.findViewById(R.id.add_btn);
        add_btn.setOnClickListener(this);



        tab_layout = (LinearLayout) convertView.findViewById(R.id.tab_layout);







        createLocationRequest();
        addTextChangeListener();
        getClassifiedList("" , "" , "" ,"");
        return convertView;
    }

    @Override
    public void onClick(View v)
    {

        if (v.getId() == R.id.add_btn)
        {
            AddClassifiedFragment sFragment = new AddClassifiedFragment();
            EditClassifedListener editClassifedListener = new EditClassifedListener();
            sFragment.setInstanse(user_public_email , user_public_phone , user_public_phone ,editClassifedListener);
            //sFragment.setInstanse(getResources().getString(R.string.statics));
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_frame, sFragment,"ddd").addToBackStack(null).commit();

        }

    }
    @Override
    public void onStart() {
        super.onStart();

        if (mGoogleApiClient != null)
        {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DirectorFragmentManageActivity.updateTitle("Classified List");

    }

    public void addTextChangeListener()
    {

    }


    public void getClassifiedList(String min_price_text , String max_price_text , String search_keyword , final String lastCounterId )
    {
        int densityDpi;
        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        densityDpi = dm.densityDpi;

        if (densityDpi > 0 && densityDpi <= 160) {

            densityDpi = 160;

        } else if (densityDpi > 160 && densityDpi <= 240) {

            densityDpi = 240;

        } else if (densityDpi > 240 && densityDpi <= 320) {

            densityDpi = 320;

        } else {

            densityDpi = 500; //1020*1920
        }
        HashMap<String,Object>parasm = new HashMap<>();
        parasm.put("density" ,densityDpi);
        parasm.put("counter" ,lastCounterId+"");
        parasm.put("dis" ,50+"");
        parasm.put("club_id" ,SessionManager.getUser_Club_id(getActivity())+"");
        parasm.put("user_id" ,SessionManager.getUser_id(getActivity())+"");

        if (Validation.isStringNullOrBlank(max_price_text) == false)
        {
            parasm.put("max_price" ,max_price_text);
        }
        if (Validation.isStringNullOrBlank(min_price_text) == false)
        {
            parasm.put("min_price" ,min_price_text);
        }

        if (Validation.isStringNullOrBlank(search_keyword) == false)
        {
            parasm.put("search_keyword" ,search_keyword);
        }

isWebServicesisCalling = true ;
//
        httpRequest.getResponse(getActivity(), WebService.classifieds_list, parasm, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                isWebServicesisCalling = false ;
                lastCallItemCount = 0;
                try
                {
                    if (Validation.isStringNullOrBlank(lastCounterId))
                {
                    classifiedItemArrayList.clear();
                }


                    if (jsonObject.getBoolean("status"))
                    {
                        no_data_tv.setVisibility(View.GONE);


                        max_price =  jsonObject.getLong("max_price");

                        user_public_email = jsonObject.getInt("user_public_email");
                        user_public_phone = jsonObject.getInt("user_public_phone");
                        member_allow_status = jsonObject.getInt("member_allow_status");


                        selected_price = max_price ;

                        if (member_allow_status == 1 || SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_DIRECTOR)||SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MOBILE_ADMIN))
                        {
                            tab_layout.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            tab_layout.setVisibility(View.GONE);
                        }


                        JSONArray responserJsonArray = jsonObject.getJSONArray("Response") ;


                        if (responserJsonArray.length() == 0)
                        {
                        }
                        else
                        {

                        }


                        lastCallItemCount = responserJsonArray.length();

                        for (int i = 0 ; i < responserJsonArray.length() ;i++ )
                        {
                            JSONObject responserJsonArrayItem = responserJsonArray.getJSONObject(i);
                            ClassifiedItem classifiedItem = new ClassifiedItem();
                            classifiedItem.setClassifieds_id(responserJsonArrayItem.getInt("classifieds_id"));
                            classifiedItem.setClassifieds_uid(responserJsonArrayItem.getInt("classifieds_uid"));
                            classifiedItem.setClassifieds_title(responserJsonArrayItem.getString("classifieds_title"));
                            classifiedItem.setClassifieds_desc(responserJsonArrayItem.getString("classifieds_desc"));
                            //classifiedItem.setClassifieds_profile(responserJsonArrayItem.getString("classifieds_profile"));
                            //classifiedItem.setClassifieds_address(responserJsonArrayItem.getString("classifieds_address"));
                          //  classifiedItem.setClassifieds_lat(responserJsonArrayItem.getDouble("classifieds_lat"));
                          //  classifiedItem.setClassifieds_long(responserJsonArrayItem.getDouble("classifieds_long"));
                            classifiedItem.setClassifieds_edate(responserJsonArrayItem.getString("classifieds_edate"));
                            classifiedItem.setClassifieds_cost(responserJsonArrayItem.getDouble("classifieds_cost"));
                            classifiedItem.setClassifieds_user_contact(responserJsonArrayItem.getString("classifieds_user_contact"));
                          //  classifiedItem.setDistance(responserJsonArrayItem.getDouble("distance"));
                            ArrayList<ClassifiedItemOtherPicture>classifiedItemOtherPictureArrayList = new ArrayList<ClassifiedItemOtherPicture>();
                            JSONArray classified_other_pics_json_array = responserJsonArrayItem.getJSONArray("classified_other_pics");
                            for (int j = 0 ; j <classified_other_pics_json_array.length() ;j++ )
                            {
                                JSONObject classified_other_pics_json_array_item =  classified_other_pics_json_array.getJSONObject(j);
                                ClassifiedItemOtherPicture classifiedItemOtherPicture = new ClassifiedItemOtherPicture();
                                try
                                {
                                    classifiedItemOtherPicture.setId(classified_other_pics_json_array_item.getInt("id"));

                                }
                                catch (Exception e)
                                {

                                }
                                classifiedItemOtherPicture.setThumb(classified_other_pics_json_array_item.getString("thumb"));
                                classifiedItemOtherPicture.setUrl(classified_other_pics_json_array_item.getString("url"));
                                classifiedItemOtherPictureArrayList.add(classifiedItemOtherPicture);

                            }
                            classifiedItem.setClassified_other_pics(classifiedItemOtherPictureArrayList);
                            classifiedItem.setUser_name(responserJsonArrayItem.getString("user_name"));
                            classifiedItem.setClassifieds_profile(responserJsonArrayItem.getString("user_profile_pic"));

                            classifiedItem.setComment_count(Integer.parseInt(responserJsonArrayItem.getString("comment_count")));


                                ArrayList<ClassifiedCommentBean>classifiedCommentBeanArrayList = new ArrayList<ClassifiedCommentBean>();
                                JSONArray commentsJsonArray = responserJsonArrayItem.getJSONArray("comments");

                                for (int commentIndex =0 ; commentIndex < commentsJsonArray.length() ;commentIndex++)
                                {
                                    JSONObject commentsJsonArrayItem = commentsJsonArray.getJSONObject(commentIndex);
                                    ClassifiedCommentBean classifiedCommentBean = new ClassifiedCommentBean();
                                    classifiedCommentBean.setClassifieds_comment_id(Integer.parseInt(commentsJsonArrayItem.getString("classifieds_comment_id")));
                                    classifiedCommentBean.setClassifieds_user_id(Integer.parseInt(commentsJsonArrayItem.getString("classifieds_user_id")));
                                    classifiedCommentBean.setClassifieds_comment_text(commentsJsonArrayItem.getString("classifieds_comment_text"));
                                    classifiedCommentBean.setClassifieds_comment_date(commentsJsonArrayItem.getString("classifieds_comment_date"));
                                    classifiedCommentBean.setUser_name(commentsJsonArrayItem.getString("user_name"));
                                    classifiedCommentBean.setUser_profilepic(commentsJsonArrayItem.getString("user_profilepic"));

                                    classifiedCommentBeanArrayList.add(classifiedCommentBean);



                            }
                            classifiedItem.setClassifiedCommentBeanArrayList(classifiedCommentBeanArrayList);



                            classifiedItemArrayList.add(classifiedItem);
                            //copyClassifiedItemArrayList.add(classifiedItem);
                        }
                    }
                    else
                    {
                        no_data_tv.setVisibility(View.VISIBLE);

                    }


                    if (classifiedListAdapter == null)
                    {
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        classified_item_listview.setLayoutManager(mLayoutManager);
                        classifiedListAdapter = new ClassifiedListAdapter(ClassifiedFragment.this , classifiedItemArrayList );
                        classified_item_listview.setAdapter(classifiedListAdapter);
                    }
                    else
                    {
                        classifiedListAdapter.notifyDataSetChanged();
                    }



                }
                catch (Exception e)
                {
                Toast.makeText(getActivity() , e.getMessage() ,1).show();
                }
            }
        });
    }




    public void getMyClassifiedList(String min_price_text , String max_price_text , String search_keyword , final String lastCounterId)
    {
        int densityDpi;
        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        densityDpi = dm.densityDpi;

        if (densityDpi > 0 && densityDpi <= 160) {

            densityDpi = 160;

        } else if (densityDpi > 160 && densityDpi <= 240) {

            densityDpi = 240;

        } else if (densityDpi > 240 && densityDpi <= 320) {

            densityDpi = 320;

        } else {

            densityDpi = 500; //1020*1920
        }
        HashMap<String,Object>parasm = new HashMap<>();
        parasm.put("density" ,densityDpi);
        parasm.put("counter" ,lastCounterId+"");
        parasm.put("dis" ,50+"");
        parasm.put("club_id" ,SessionManager.getUser_Club_id(getActivity())+"");
        parasm.put("user_id" ,SessionManager.getUser_id(getActivity())+"");

        if (Validation.isStringNullOrBlank(max_price_text) == false)
        {
            parasm.put("max_price" ,max_price_text);
        }
        if (Validation.isStringNullOrBlank(min_price_text) == false)
        {
            parasm.put("min_price" ,min_price_text);
        }

        if (Validation.isStringNullOrBlank(search_keyword) == false)
        {
            parasm.put("search_keyword" ,search_keyword);
        }
        isWebServicesisCalling = true ;
        lastCallItemCount = 0;

//
        httpRequest.getResponse(getActivity(), WebService.myclassifieds_list, parasm, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                isWebServicesisCalling = false ;
                try
                {
                    if (Validation.isStringNullOrBlank(lastCounterId))
                    {
                        classifiedItemArrayList.clear();
                    }




                    if (jsonObject.getBoolean("status"))
                    {




                        no_data_tv.setVisibility(View.GONE);


                        JSONArray responserJsonArray = jsonObject.getJSONArray("Response") ;
                        lastCallItemCount = responserJsonArray.length();
                        for (int i = 0 ; i < responserJsonArray.length() ;i++ )
                        {
                            JSONObject responserJsonArrayItem = responserJsonArray.getJSONObject(i);
                            ClassifiedItem classifiedItem = new ClassifiedItem();
                            classifiedItem.setClassifieds_id(responserJsonArrayItem.getInt("classifieds_id"));
                            classifiedItem.setClassifieds_uid(responserJsonArrayItem.getInt("classifieds_uid"));
                            classifiedItem.setClassifieds_title(responserJsonArrayItem.getString("classifieds_title"));
                            classifiedItem.setClassifieds_desc(responserJsonArrayItem.getString("classifieds_desc"));
                            //classifiedItem.setClassifieds_profile(responserJsonArrayItem.getString("classifieds_profile"));
                            //classifiedItem.setClassifieds_address(responserJsonArrayItem.getString("classifieds_address"));
                            //  classifiedItem.setClassifieds_lat(responserJsonArrayItem.getDouble("classifieds_lat"));
                            //  classifiedItem.setClassifieds_long(responserJsonArrayItem.getDouble("classifieds_long"));
                            classifiedItem.setClassifieds_edate(responserJsonArrayItem.getString("classifieds_edate"));
                            classifiedItem.setClassifieds_cost(responserJsonArrayItem.getDouble("classifieds_cost"));
                            classifiedItem.setClassifieds_user_contact(responserJsonArrayItem.getString("classifieds_user_contact"));
                            //  classifiedItem.setDistance(responserJsonArrayItem.getDouble("distance"));
                            ArrayList<ClassifiedItemOtherPicture>classifiedItemOtherPictureArrayList = new ArrayList<ClassifiedItemOtherPicture>();
                            JSONArray classified_other_pics_json_array = responserJsonArrayItem.getJSONArray("classified_other_pics");
                            for (int j = 0 ; j <classified_other_pics_json_array.length() ;j++ )
                            {
                                JSONObject classified_other_pics_json_array_item =  classified_other_pics_json_array.getJSONObject(j);
                                ClassifiedItemOtherPicture classifiedItemOtherPicture = new ClassifiedItemOtherPicture();
                                classifiedItemOtherPicture.setId(classified_other_pics_json_array_item.getInt("id"));
                                classifiedItemOtherPicture.setThumb(classified_other_pics_json_array_item.getString("thumb"));
                                classifiedItemOtherPicture.setUrl(classified_other_pics_json_array_item.getString("url"));
                                classifiedItemOtherPictureArrayList.add(classifiedItemOtherPicture);

                            }
                            classifiedItem.setClassified_other_pics(classifiedItemOtherPictureArrayList);
                            classifiedItem.setUser_name(responserJsonArrayItem.getString("user_name"));
                            classifiedItem.setClassifieds_profile(responserJsonArrayItem.getString("user_profile_pic"));



                            classifiedItem.setComment_count(Integer.parseInt(responserJsonArrayItem.getString("comment_count")));


                            ArrayList<ClassifiedCommentBean>classifiedCommentBeanArrayList = new ArrayList<ClassifiedCommentBean>();
                            JSONArray commentsJsonArray = responserJsonArrayItem.getJSONArray("comments");

                            for (int commentIndex =0 ; commentIndex < commentsJsonArray.length() ;commentIndex++)
                            {
                                JSONObject commentsJsonArrayItem = commentsJsonArray.getJSONObject(commentIndex);
                                ClassifiedCommentBean classifiedCommentBean = new ClassifiedCommentBean();
                                classifiedCommentBean.setClassifieds_comment_id(Integer.parseInt(commentsJsonArrayItem.getString("classifieds_comment_id")));
                                classifiedCommentBean.setClassifieds_user_id(Integer.parseInt(commentsJsonArrayItem.getString("classifieds_user_id")));
                                classifiedCommentBean.setClassifieds_comment_text(commentsJsonArrayItem.getString("classifieds_comment_text"));
                                classifiedCommentBean.setClassifieds_comment_date(commentsJsonArrayItem.getString("classifieds_comment_date"));
                                classifiedCommentBean.setUser_name(commentsJsonArrayItem.getString("user_name"));
                                classifiedCommentBean.setUser_profilepic(commentsJsonArrayItem.getString("user_profilepic"));

                                classifiedCommentBeanArrayList.add(classifiedCommentBean);



                            }
                            classifiedItem.setClassifiedCommentBeanArrayList(classifiedCommentBeanArrayList);





                            classifiedItemArrayList.add(classifiedItem);
                          //  copyClassifiedItemArrayList.add(classifiedItem);
                        }
                    }
                    else
                    {
                        no_data_tv.setVisibility(View.VISIBLE);

                    }

                   // if (classifiedListAdapter == null)
                    {
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        classified_item_listview.setLayoutManager(mLayoutManager);
                        classifiedListAdapter = new ClassifiedListAdapter(ClassifiedFragment.this , classifiedItemArrayList );
                        classified_item_listview.setAdapter(classifiedListAdapter);
                    }
                   // else
                    {
                       // classifiedListAdapter.notifyDataSetChanged();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity() , e.getMessage() ,Toast.LENGTH_LONG).show();
                }
            }
        });
    }






    public void getMyClassifiedListWithoutPd(String min_price_text , String max_price_text , String search_keyword , final String lastCounterId)
    {
        int densityDpi;
        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        densityDpi = dm.densityDpi;

        if (densityDpi > 0 && densityDpi <= 160) {

            densityDpi = 160;

        } else if (densityDpi > 160 && densityDpi <= 240) {

            densityDpi = 240;

        } else if (densityDpi > 240 && densityDpi <= 320) {

            densityDpi = 320;

        } else {

            densityDpi = 500; //1020*1920
        }
        HashMap<String,Object>parasm = new HashMap<>();
        parasm.put("density" ,densityDpi);
        parasm.put("counter" ,lastCounterId+"");
        parasm.put("dis" ,50+"");
        parasm.put("club_id" ,SessionManager.getUser_Club_id(getActivity())+"");
        parasm.put("user_id" ,SessionManager.getUser_id(getActivity())+"");

        if (Validation.isStringNullOrBlank(max_price_text) == false)
        {
            parasm.put("max_price" ,max_price_text);
        }
        if (Validation.isStringNullOrBlank(min_price_text) == false)
        {
            parasm.put("min_price" ,min_price_text);
        }

        if (Validation.isStringNullOrBlank(search_keyword) == false)
        {
            parasm.put("search_keyword" ,search_keyword);
        }
        isWebServicesisCalling = true ;
        lastCallItemCount = 0;
        progressbar.setVisibility(View.VISIBLE);

//
        httpRequest.getResponseWihhoutPd(getActivity(), WebService.myclassifieds_list, parasm, new OnServerRespondingListener(getActivity()) {


            @Override
            public void onConnectionError() {
                super.onConnectionError();
                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onNetWorkError() {
                super.onNetWorkError();
                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void internetConnectionProble() {
                super.internetConnectionProble();
                progressbar.setVisibility(View.GONE);
            }


            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                progressbar.setVisibility(View.GONE);
                isWebServicesisCalling = false ;
                try
                {
                    if (Validation.isStringNullOrBlank(lastCounterId))
                    {
                        classifiedItemArrayList.clear();
                    }




                    if (jsonObject.getBoolean("status"))
                    {






                        JSONArray responserJsonArray = jsonObject.getJSONArray("Response") ;
                        lastCallItemCount = responserJsonArray.length();
                        for (int i = 0 ; i < responserJsonArray.length() ;i++ )
                        {
                            JSONObject responserJsonArrayItem = responserJsonArray.getJSONObject(i);
                            ClassifiedItem classifiedItem = new ClassifiedItem();
                            classifiedItem.setClassifieds_id(responserJsonArrayItem.getInt("classifieds_id"));
                            classifiedItem.setClassifieds_uid(responserJsonArrayItem.getInt("classifieds_uid"));
                            classifiedItem.setClassifieds_title(responserJsonArrayItem.getString("classifieds_title"));
                            classifiedItem.setClassifieds_desc(responserJsonArrayItem.getString("classifieds_desc"));

                            classifiedItem.setClassifieds_edate(responserJsonArrayItem.getString("classifieds_edate"));
                            classifiedItem.setClassifieds_cost(responserJsonArrayItem.getDouble("classifieds_cost"));
                            classifiedItem.setClassifieds_user_contact(responserJsonArrayItem.getString("classifieds_user_contact"));
                            //  classifiedItem.setDistance(responserJsonArrayItem.getDouble("distance"));
                            ArrayList<ClassifiedItemOtherPicture>classifiedItemOtherPictureArrayList = new ArrayList<ClassifiedItemOtherPicture>();
                            JSONArray classified_other_pics_json_array = responserJsonArrayItem.getJSONArray("classified_other_pics");
                            for (int j = 0 ; j <classified_other_pics_json_array.length() ;j++ )
                            {
                                JSONObject classified_other_pics_json_array_item =  classified_other_pics_json_array.getJSONObject(j);
                                ClassifiedItemOtherPicture classifiedItemOtherPicture = new ClassifiedItemOtherPicture();
                                classifiedItemOtherPicture.setId(classified_other_pics_json_array_item.getInt("id"));
                                classifiedItemOtherPicture.setThumb(classified_other_pics_json_array_item.getString("thumb"));
                                classifiedItemOtherPicture.setUrl(classified_other_pics_json_array_item.getString("url"));
                                classifiedItemOtherPictureArrayList.add(classifiedItemOtherPicture);

                            }
                            classifiedItem.setClassified_other_pics(classifiedItemOtherPictureArrayList);
                            classifiedItem.setUser_name(responserJsonArrayItem.getString("user_name"));
                            classifiedItem.setClassifieds_profile(responserJsonArrayItem.getString("user_profile_pic"));



                            classifiedItem.setComment_count(Integer.parseInt(responserJsonArrayItem.getString("comment_count")));


                            ArrayList<ClassifiedCommentBean>classifiedCommentBeanArrayList = new ArrayList<ClassifiedCommentBean>();
                            JSONArray commentsJsonArray = responserJsonArrayItem.getJSONArray("comments");

                            for (int commentIndex =0 ; commentIndex < commentsJsonArray.length() ;commentIndex++)
                            {
                                JSONObject commentsJsonArrayItem = commentsJsonArray.getJSONObject(commentIndex);
                                ClassifiedCommentBean classifiedCommentBean = new ClassifiedCommentBean();
                                classifiedCommentBean.setClassifieds_comment_id(Integer.parseInt(commentsJsonArrayItem.getString("classifieds_comment_id")));
                                classifiedCommentBean.setClassifieds_user_id(Integer.parseInt(commentsJsonArrayItem.getString("classifieds_user_id")));
                                classifiedCommentBean.setClassifieds_comment_text(commentsJsonArrayItem.getString("classifieds_comment_text"));
                                classifiedCommentBean.setClassifieds_comment_date(commentsJsonArrayItem.getString("classifieds_comment_date"));
                                classifiedCommentBean.setUser_name(commentsJsonArrayItem.getString("user_name"));
                                classifiedCommentBean.setUser_profilepic(commentsJsonArrayItem.getString("user_profilepic"));

                                classifiedCommentBeanArrayList.add(classifiedCommentBean);



                            }
                            classifiedItem.setClassifiedCommentBeanArrayList(classifiedCommentBeanArrayList);


                            classifiedItemArrayList.add(classifiedItem);
                           // copyClassifiedItemArrayList.add(classifiedItem);
                        }
                    }
                    else
                    {

                    }

                    if (classifiedListAdapter == null)
                    {
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        classified_item_listview.setLayoutManager(mLayoutManager);
                        classifiedListAdapter = new ClassifiedListAdapter(ClassifiedFragment.this , classifiedItemArrayList );
                        classified_item_listview.setAdapter(classifiedListAdapter);
                    }
                    else
                    {
                         classifiedListAdapter.notifyDataSetChanged();
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity() , e.getMessage() ,Toast.LENGTH_LONG).show();
                }
            }
        });
    }















    protected void createLocationRequest() {

    }






    @Override
    public void onDestroy() {
        super.onDestroy();
        DirectorFragmentManageActivity.logoutButton.setVisibility(View.GONE);
    }
    CheckBox all_classified_check_box ;
    CheckBox mypost_classified_check_box;
    SeekBar distance_seek_bar ;
    TextView distance_show_tv;

    double selected_distance ;
    double selected_price ;
    TextView selected_cost_tv;
    long max_price  ; ;

    int user_public_email , user_public_phone , member_allow_status;

    TextView max_price_tv;
    Dialog dialog ;

    boolean isAllCheckBox = true ;
    TextView done_btn_tv , cancel_tv;
    EditText minimum_price_edit_tv ;
    EditText maximum_price_edit_tv;
    public View.OnClickListener filterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {


            if (dialog == null)
            {
                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.filter_classified_list_layout);
                dialog.getWindow().setLayout(AppConstants.getDeviceWidth(getActivity()), LinearLayout.LayoutParams.WRAP_CONTENT );
                search_edit_tv = (EditText) dialog.findViewById(R.id.search_edit_tv);
                minimum_price_edit_tv = (EditText) dialog.findViewById(R.id.minimum_price_edit_tv);
                maximum_price_edit_tv = (EditText) dialog.findViewById(R.id.maximum_price_edit_tv);
                minimum_price_edit_tv.setHint("0");
                maximum_price_edit_tv.setHint(max_price+"");
               // minimum_price_edit_tv.setSelection(1);
               // maximum_price_edit_tv.setSelection((max_price+"").length());

            }

            dialog.show();


            //cost_seeek_bar.setRangeValues( 0 ,max_price);





            done_btn_tv = (TextView) dialog.findViewById(R.id.ok);
            cancel_tv = (TextView) dialog.findViewById(R.id.cancel);

            done_btn_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Log.e("Minimum Value" , cost_seeek_bar.getSelectedMinValue()+"");
                    //Log.e("Maximum Value" , cost_seeek_bar.getSelectedMinValue()+"");
                    Utill.hideKeybord(getActivity() , search_edit_tv);
                    Utill.hideKeybord(getActivity() , maximum_price_edit_tv);

                    Utill.hideKeybord(getActivity() , minimum_price_edit_tv);


                    searchingWord = search_edit_tv.getText().toString() ;
                    minimumValue =  minimum_price_edit_tv.getText().toString();
                    maximumValue = maximum_price_edit_tv.getText().toString() ;




                    dialog.dismiss();

                    if (listIdentifire == 1)
                    {
                        getClassifiedList( minimumValue ,  maximumValue , search_edit_tv.getText().toString() , "");
                    }
                    else
                    {
                        getMyClassifiedList( minimumValue ,  maximumValue , search_edit_tv.getText().toString() , "");
                    }


                }
            });



            cancel_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Utill.hideKeybord(getActivity() , search_edit_tv);
                    Utill.hideKeybord(getActivity() , maximum_price_edit_tv);

                    Utill.hideKeybord(getActivity() , minimum_price_edit_tv);
                    dialog.dismiss();

                }
            });




           ;


        }
    };






    public View.OnClickListener tabClicklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            classifiedListAdapter = null ;



            if (search_edit_tv != null)
            {
                search_edit_tv.setText("");
                minimum_price_edit_tv.setText("");
                maximum_price_edit_tv.setText("");
                minimum_price_edit_tv.setHint("0");
                maximum_price_edit_tv.setHint(max_price+"");

                // cost_seeek_bar.setSelectedMinValue(0);
              //  cost_seeek_bar.setSelectedMaxValue(max_price);
            }
            searchingWord =  "" ;
            maximumValue = "" ;
            minimumValue = "" ;
            if (v.getId() == R.id.classified_list_tab)
            {

                progressbar.setVisibility(View.GONE);
                httpRequest.cancelAjax();


                my_classified_list_tab.setBackgroundColor(getResources().getColor(R.color.unselected_tab));
                classified_list_tab.setBackgroundDrawable(getResources().getDrawable(R.drawable.active_tb_bg));

                classifiedListAdapter = new ClassifiedListAdapter(ClassifiedFragment.this , classifiedItemArrayList);
                classified_item_listview.setAdapter(classifiedListAdapter);
                add_btn.setVisibility(View.GONE);
                listIdentifire = 1;

              getClassifiedList("" , "" , "" ,"");



            }
            if (v.getId() == R.id.my_classified_list_tab)
            {

                classified_list_tab.setBackgroundColor(getResources().getColor(R.color.unselected_tab));
                my_classified_list_tab.setBackgroundDrawable(getResources().getDrawable(R.drawable.active_tb_bg));

                listIdentifire = 2;
                add_btn.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);
                httpRequest.cancelAjax();
                getMyClassifiedList("" , "" , "" ,"");
            }


        }
    };









    public void getClassifiedListWithoutPd(String min_price_text , String max_price_text , String search_keyword , final String lastCounterId )
    {
        int densityDpi;
        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        densityDpi = dm.densityDpi;

        if (densityDpi > 0 && densityDpi <= 160) {

            densityDpi = 160;

        } else if (densityDpi > 160 && densityDpi <= 240) {

            densityDpi = 240;

        } else if (densityDpi > 240 && densityDpi <= 320) {

            densityDpi = 320;

        } else {

            densityDpi = 500; //1020*1920
        }
        HashMap<String,Object>parasm = new HashMap<>();
        parasm.put("density" ,densityDpi);
        parasm.put("counter" ,lastCounterId+"");
        parasm.put("dis" ,50+"");
        parasm.put("club_id" ,SessionManager.getUser_Club_id(getActivity())+"");
        parasm.put("user_id" ,SessionManager.getUser_id(getActivity())+"");

        if (Validation.isStringNullOrBlank(max_price_text) == false)
        {
            parasm.put("max_price" ,max_price_text);
        }
        if (Validation.isStringNullOrBlank(min_price_text) == false)
        {
            parasm.put("min_price" ,min_price_text);
        }

        if (Validation.isStringNullOrBlank(search_keyword) == false)
        {
            parasm.put("search_keyword" ,search_keyword);
        }

        isWebServicesisCalling = true ;
//
        progressbar.setVisibility(View.VISIBLE);
        httpRequest.getResponseWihhoutPd(getActivity(), WebService.classifieds_list, parasm, new OnServerRespondingListener(getActivity()) {

            @Override
            public void onConnectionError() {
                super.onConnectionError();
                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onNetWorkError() {
                super.onNetWorkError();
                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void internetConnectionProble() {
                super.internetConnectionProble();
                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                progressbar.setVisibility(View.GONE);
                isWebServicesisCalling = false ;
                lastCallItemCount = 0;
                try
                {
                    if (Validation.isStringNullOrBlank(lastCounterId))
                    {
                        classifiedItemArrayList.clear();
                    }


                    if (jsonObject.getBoolean("status"))
                    {

                        max_price =  jsonObject.getLong("max_price");
                        user_public_email = jsonObject.getInt("user_public_email");
                        user_public_phone = jsonObject.getInt("user_public_phone");
                        member_allow_status = jsonObject.getInt("member_allow_status");


                        selected_price = max_price ;

                        if (member_allow_status == 1 || SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_DIRECTOR)||SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MOBILE_ADMIN))
                        {
                            tab_layout.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            tab_layout.setVisibility(View.GONE);
                        }


                        JSONArray responserJsonArray = jsonObject.getJSONArray("Response") ;

                        lastCallItemCount = responserJsonArray.length();

                        for (int i = 0 ; i < responserJsonArray.length() ;i++ )
                        {
                            JSONObject responserJsonArrayItem = responserJsonArray.getJSONObject(i);
                            ClassifiedItem classifiedItem = new ClassifiedItem();
                            classifiedItem.setClassifieds_id(responserJsonArrayItem.getInt("classifieds_id"));
                            classifiedItem.setClassifieds_uid(responserJsonArrayItem.getInt("classifieds_uid"));
                            classifiedItem.setClassifieds_title(responserJsonArrayItem.getString("classifieds_title"));
                            classifiedItem.setClassifieds_desc(responserJsonArrayItem.getString("classifieds_desc"));
                            //classifiedItem.setClassifieds_profile(responserJsonArrayItem.getString("classifieds_profile"));
                            //classifiedItem.setClassifieds_address(responserJsonArrayItem.getString("classifieds_address"));
                            //  classifiedItem.setClassifieds_lat(responserJsonArrayItem.getDouble("classifieds_lat"));
                            //  classifiedItem.setClassifieds_long(responserJsonArrayItem.getDouble("classifieds_long"));
                            classifiedItem.setClassifieds_edate(responserJsonArrayItem.getString("classifieds_edate"));
                            classifiedItem.setClassifieds_cost(responserJsonArrayItem.getDouble("classifieds_cost"));
                            classifiedItem.setClassifieds_user_contact(responserJsonArrayItem.getString("classifieds_user_contact"));
                            //  classifiedItem.setDistance(responserJsonArrayItem.getDouble("distance"));
                            ArrayList<ClassifiedItemOtherPicture>classifiedItemOtherPictureArrayList = new ArrayList<ClassifiedItemOtherPicture>();
                            JSONArray classified_other_pics_json_array = responserJsonArrayItem.getJSONArray("classified_other_pics");
                            for (int j = 0 ; j <classified_other_pics_json_array.length() ;j++ )
                            {
                                JSONObject classified_other_pics_json_array_item =  classified_other_pics_json_array.getJSONObject(j);
                                ClassifiedItemOtherPicture classifiedItemOtherPicture = new ClassifiedItemOtherPicture();
                                try
                                {
                                    classifiedItemOtherPicture.setId(classified_other_pics_json_array_item.getInt("id"));

                                }
                                catch (Exception e)
                                {

                                }

                                classifiedItemOtherPicture.setThumb(classified_other_pics_json_array_item.getString("thumb"));
                                classifiedItemOtherPicture.setUrl(classified_other_pics_json_array_item.getString("url"));
                                classifiedItemOtherPictureArrayList.add(classifiedItemOtherPicture);

                            }
                            classifiedItem.setClassified_other_pics(classifiedItemOtherPictureArrayList);
                            classifiedItem.setUser_name(responserJsonArrayItem.getString("user_name"));
                            classifiedItem.setClassifieds_profile(responserJsonArrayItem.getString("user_profile_pic"));


                            classifiedItem.setComment_count(Integer.parseInt(responserJsonArrayItem.getString("comment_count")));


                            ArrayList<ClassifiedCommentBean>classifiedCommentBeanArrayList = new ArrayList<ClassifiedCommentBean>();
                            JSONArray commentsJsonArray = responserJsonArrayItem.getJSONArray("comments");

                            for (int commentIndex =0 ; commentIndex < commentsJsonArray.length() ;commentIndex++)
                            {
                                JSONObject commentsJsonArrayItem = commentsJsonArray.getJSONObject(commentIndex);
                                ClassifiedCommentBean classifiedCommentBean = new ClassifiedCommentBean();
                                classifiedCommentBean.setClassifieds_comment_id(Integer.parseInt(commentsJsonArrayItem.getString("classifieds_comment_id")));
                                classifiedCommentBean.setClassifieds_user_id(Integer.parseInt(commentsJsonArrayItem.getString("classifieds_user_id")));
                                classifiedCommentBean.setClassifieds_comment_text(commentsJsonArrayItem.getString("classifieds_comment_text"));
                                classifiedCommentBean.setClassifieds_comment_date(commentsJsonArrayItem.getString("classifieds_comment_date"));
                                classifiedCommentBean.setUser_name(commentsJsonArrayItem.getString("user_name"));
                                classifiedCommentBean.setUser_profilepic(commentsJsonArrayItem.getString("user_profilepic"));

                                classifiedCommentBeanArrayList.add(classifiedCommentBean);



                            }
                            classifiedItem.setClassifiedCommentBeanArrayList(classifiedCommentBeanArrayList);


                            classifiedItemArrayList.add(classifiedItem);
                           // copyClassifiedItemArrayList.add(classifiedItem);
                        }
                    }
                    else
                    {

                    }


                    if (classifiedListAdapter == null)
                    {
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        classified_item_listview.setLayoutManager(mLayoutManager);
                        classifiedListAdapter = new ClassifiedListAdapter(ClassifiedFragment.this , classifiedItemArrayList );
                        classified_item_listview.setAdapter(classifiedListAdapter);
                    }
                    else
                    {
                        classifiedListAdapter.notifyDataSetChanged();
                    }



                }
                catch (Exception e)
                {
                     Toast.makeText(getActivity() , e.getMessage() ,1).show();
                }
            }
        });
    }



    public class EditClassifedListener
    {

        public void onSucess()
        {
            my_classified_list_tab.performClick();
        }

    }








    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DirectorFragmentManageActivity.logoutButton.setVisibility(View.GONE);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        DirectorFragmentManageActivity.isbackPress = false;
    }



    public void sendComment(int classifieds_id , final String classifieds_comment_text , final int pos)
    {
        HashMap<String , Object>param = new HashMap<String , Object>();
        param.put("classifieds_id", classifieds_id+"");
        param.put("classifieds_comment_text",classifieds_comment_text);
        param.put("classifieds_user_id", SessionManager.getUser_id(getActivity()));


        httpRequest.getResponse(getActivity(), WebService.classifieds_comment, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {




                try
                {
                    ClassifiedItem classifiedItem = classifiedItemArrayList.get(pos);

                    if (jsonObject.getBoolean("status"))
                    {

                    }
                    Utill.showDialg(jsonObject.getString("message") , getActivity());



                    JSONArray commentsJsonArray = jsonObject.getJSONArray("comments");

                    ArrayList<ClassifiedCommentBean> classifiedCommentBeanArrayList = classifiedItem.getClassifiedCommentBeanArrayList();

                    for (int commentIndex =0 ; commentIndex < commentsJsonArray.length() ;commentIndex++)
                    {
                        JSONObject commentsJsonArrayItem = commentsJsonArray.getJSONObject(commentIndex);
                        ClassifiedCommentBean classifiedCommentBean = new ClassifiedCommentBean();
                        classifiedCommentBean.setClassifieds_comment_id(Integer.parseInt(commentsJsonArrayItem.getString("classifieds_comment_id")));
                        classifiedCommentBean.setClassifieds_user_id(Integer.parseInt(commentsJsonArrayItem.getString("classifieds_user_id")));
                        classifiedCommentBean.setClassifieds_comment_text(classifieds_comment_text);
                        classifiedCommentBean.setClassifieds_comment_date(commentsJsonArrayItem.getString("classifieds_comment_date"));
                        classifiedCommentBean.setUser_name(commentsJsonArrayItem.getString("user_name"));
                        classifiedCommentBean.setUser_profilepic(commentsJsonArrayItem.getString("user_profilepic"));

                        classifiedCommentBeanArrayList.add(classifiedCommentBean);



                    }

                    if (classifiedListAdapter != null)
                    {
                        classifiedListAdapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {

                }




            }
        });

    }



    public void deleteComment(int classifieds_comment_id , String userId , final int pos , final int cmtpos)
    {



        HashMap<String , Object> param = new HashMap<>();
        param.put("classifieds_comment_id" , classifieds_comment_id+"");
        param.put("user_id" , userId+"");


        httpRequest.getResponse(getActivity(), WebService.delete_classifieds_comment, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
             try
             {
                 if (jsonObject.getBoolean("status"))
                 {
                     ClassifiedItem classifiedItem = classifiedItemArrayList.get(pos);
                     ArrayList<ClassifiedCommentBean>classifiedCommentBeanArrayList = classifiedItem.getClassifiedCommentBeanArrayList();
                     classifiedCommentBeanArrayList.remove(cmtpos);
                     if (classifiedListAdapter != null)
                     {
                         classifiedListAdapter.notifyDataSetChanged();
                     }
                     if (classifiedCommentAdapter != null)
                     {
                         classifiedCommentAdapter.notifyDataSetChanged();
                     }

                 }
                 Utill.showDialg(jsonObject.getString("message") , getActivity());


             }
             catch (Exception e)
             {

             }

            }
        });


    }

  public   Dialog commentDialog ;
    ClassifiedCommentAdapter classifiedCommentAdapter ;
    RecyclerView classified_cmt_list_view;
    ImageView cross_btn_iv;

    public void showClassofiedCommentDialog(ArrayList<ClassifiedCommentBean> classifiedCommentArrayList , boolean isUserAsOwnwe , int itemPos)
    {
        commentDialog = new Dialog(getActivity());
        commentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        commentDialog.setContentView(R.layout.classified_cmt_adapter);
        cross_btn_iv = (ImageView) commentDialog.findViewById(R.id.cross_btn_iv);
        classified_cmt_list_view = (RecyclerView) commentDialog.findViewById(R.id.classified_cmt_list_view);
        commentDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT ,AppConstants.getDeviceHeight(getActivity())-100);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        classified_cmt_list_view.setLayoutManager(mLayoutManager);
        classified_cmt_list_view.setItemAnimator(new DefaultItemAnimator());
        classifiedCommentAdapter = new ClassifiedCommentAdapter(ClassifiedFragment.this , classifiedCommentArrayList ,isUserAsOwnwe ,itemPos);
        cross_btn_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                commentDialog.dismiss();

            }
        });
        classified_cmt_list_view.setAdapter(classifiedCommentAdapter);
        commentDialog.show();


    }







}