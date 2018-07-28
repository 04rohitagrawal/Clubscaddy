package com.clubscaddy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.clubscaddy.Adapter.InstragramImageListAdapter;
import com.clubscaddy.Bean.UserPics;
import com.clubscaddy.Interface.FragmentBackListener;
import com.clubscaddy.Interface.FragmentBackResponseListener;
import com.clubscaddy.custumview.HorizontalListView;
import com.clubscaddy.utility.SqlListe;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.instragram.InstagramSession;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.ScoreListAdapter;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.CircleTransform;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.Server.WebService;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar.LayoutParams;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class UserProfileActivity extends Fragment {
    View convertView;
    MemberListBean bean;
    ImageView profile_pic_imageview;
    TextView first_name_tv;
    TextView last_name_tv;
    TextView email_tv;
    TextView contact_no_tv;
    TextView rating_tv;
    TextView gender_tv;
    TextView about_me_tv;
    ImageView histry_id;
    ProgressDialog pd;
    AQuery aQuery;
    ImageButton face_book_btn;
    ImageButton linkedin_btn;
    ImageButton instagram_btn;
    ImageButton twitter_btn;
    TextView block_btn;
    TextView edit_btn;
    ImageButton send_msg_btn;
    TextView cancel_dialog_btn;
    TextView email_send_btn;
    TextView notification_send_btn;
    EditText email_notification_msg;
    int type;
    RelativeLayout relative_layout_bootom;
    InstagramSession instagramSession ;
    HorizontalListView istragram_image_list_view;
    ArrayList<String>imageThumbList ;

    ArrayList<String>imageStandredList ;

    InstragramImageListAdapter gallaryImageAdapter;

    ProgressBar imageLoaderProgressbar;


    public static final String TAG_DATA = "data";
    public static final String TAG_IMAGES = "images";
    public static final String TAG_THUMBNAIL = "thumbnail";

    public static final String STANDRED_RESOLUTION = "standard_resolution";


    public static final String TAG_URL = "url";

    public static final String TAG_TYPE = "type";
    HttpRequest httpRequest;



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
    OnClickListener socialmediobtnclicklistener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            try {


                if (v.getId() == R.id.face_book_btn) {
                    if (URLUtil.isValidUrl(bean.getFace_book_url())) {
                        String url = bean.getFace_book_url();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } else {
                        Utill.showDialg("No facebook link available", getActivity());
                    }

                }
                if (v.getId() == R.id.twitter_btn) {
                    String url = bean.getTwitter_url();
                    if (URLUtil.isValidUrl(url)) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } else {
                        Utill.showDialg("No twitter link available", getActivity());
                    }


                }
                if (v.getId() == R.id.send_msg_btn) {
                    createDilog();
                }
                if (v.getId() == R.id.linkedin_btn) {
                    String url = bean.getLinkedin_url();
                    if (URLUtil.isValidUrl(url)) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } else {
                        Utill.showDialg("No linkedin link available", getActivity());
                    }
                }

                if (v.getId() == R.id.instagram_btn) {
                    String url = bean.getInstragram_url();
                    if (URLUtil.isValidUrl(url)) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } else {
                        Utill.showDialg("No instagram link available", getActivity());
                    }
                }
                if (v.getId() == R.id.upload_news) {
                    EditMyProfileFragment fragment = new EditMyProfileFragment();
                    fragment.setInstanse(bean, type ,fragmentBackListener);
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_frame, fragment, "fragment").addToBackStack(null).commit();

                }
                if (v.getId() == R.id.edit_btn) {
                    EditMyProfileFragment fragment = new EditMyProfileFragment();
                    fragment.setInstanse(bean, type,fragmentBackListener);
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_frame, fragment, "fragment").addToBackStack(null).commit();

                }



                if (v.getId() == R.id.edit_image_btn) {
                    EditMyProfileFragment fragment = new EditMyProfileFragment();
                    fragment.setInstanse(bean, type,fragmentBackListener);
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_frame, fragment, "fragment").addToBackStack(null).commit();

                }
                if (v.getId() == R.id.block_btn) {


                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.block_member_dialog_layout);
                    dialog.setCancelable(false);


                    final EditText password_edit_txt = (EditText) dialog.findViewById(R.id.password_edit_txt);

                    TextView cancel_btn_dialog = (TextView) dialog.findViewById(R.id.cancel_btn_dialog);
                    cancel_btn_dialog.setBackgroundColor(getActivity().getResources().getColor(R.color.red));
                    cancel_btn_dialog.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            dialog.cancel();
                        }
                    });
                    TextView confirm_btn = (TextView) dialog.findViewById(R.id.confirm_btn);

                    confirm_btn.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub


                            if (password_edit_txt.getText().toString().equalsIgnoreCase("") || password_edit_txt.getText().toString() == "") {
                                Utill.showDialg("Please enter current password", getActivity());
                            } else {
                                bean.getUser_id();
                                bean.getUser_club_id();
                                bean.getUser_type();
                                Utill.hideKeybord(getActivity() , password_edit_txt);

                                HashMap<String, String> params = new HashMap<String, String>();
                                params.put("user_type", bean.getUser_type());
                                params.put("user_id", bean.getUser_id());
                                params.put("user_club_id", bean.getUser_club_id());
                                params.put("user_status", bean.getUser_status());
                                params.put("user_password", password_edit_txt.getText().toString());
                                params.put("current_id", SessionManager.getUser_id(getActivity()));

                                //
                                Utill.showProgress(getActivity());
                                aQuery.ajax(WebService.change_Status, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                                    public void callback(String url, JSONObject object, AjaxStatus status) {
                                        Utill.hideProgress();

                                        dialog.cancel();

                                        if (object == null) {
                                            Utill.showDialg(getString(R.string.check_internet_connection), getActivity());
                                        } else {


                                            try {


                                                Utill.showDialg(object.getString("msg"), getActivity());
                                            } catch (Exception e) {

                                            }

                                            ArrayList<MemberListBean> alMemberList = JsonUtility.parserMembersListItem(object + "", getActivity());


                                            if (alMemberList.size() != 0) {
                                                bean.setUser_status(alMemberList.get(0).getUser_status());
                                                SqlListe sqlListe = new SqlListe(getActivity());
                                                sqlListe.changeBlockUnBlockStaus(bean);
                                                if (alMemberList.get(0).getUser_status().equals("2"))
                                                {
                                                    block_btn.setBackgroundColor(getActivity().getResources().getColor(R.color.green_bg_color));
                                                    block_btn.setText("Unblock");
                                                } else {
                                                    block_btn.setBackgroundColor(getActivity().getResources().getColor(R.color.red));
                                                    block_btn.setText("Block");
                                                }
                                            }


                                        }

                                    }

                                    ;
                                });
                            }


                        }
                    });
                    dialog.show();


                }

            } catch (Exception e)
            {

            }


        }
    };

    public void setInatanse(MemberListBean bean, int type) {
        this.bean = bean;
        this.type = type;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        convertView = inflater.inflate(R.layout.profile_layout, null);


        MembersDirectoryFragment.isClearVariable = false;

        instagramSession = new InstagramSession(getActivity());

        httpRequest = new HttpRequest(getActivity());

        imageThumbList = new ArrayList<>();
        imageStandredList = new ArrayList<>();

      imageLoaderProgressbar = (ProgressBar) convertView.findViewById(R.id.image_loader_progressbar);


        DirectorFragmentManageActivity.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        if (bean.getUserPicsArrayList() != null)
        {
            ArrayList<UserPics> userPicsArrayList = bean.getUserPicsArrayList() ;
            for (int i = 0 ; i < userPicsArrayList.size() ; i++)
            {
                imageThumbList.add(userPicsArrayList.get(i).getImage_thumb());
                imageStandredList.add(userPicsArrayList.get(i).getImage_url());

            }
        }


        istragram_image_list_view = (HorizontalListView) convertView.findViewById(R.id.istragram_image_list_view);
        gallaryImageAdapter = new InstragramImageListAdapter(getActivity() ,imageThumbList );
        istragram_image_list_view.setAdapter(gallaryImageAdapter);

        relative_layout_bootom = (RelativeLayout) convertView.findViewById(R.id.relative_layout_bootom);


        if (DirectorFragmentManageActivity.actionbar_titletext != null) {

            if(type == 1)
            {
                DirectorFragmentManageActivity.updateTitle("View Profile");


            }
            else
            {
                DirectorFragmentManageActivity.updateTitle("View Member Profile");
            }


        }



        istragram_image_list_view.setVisibility(View.VISIBLE);


        if (Validation.isStringNullOrBlank(bean.getInstragramToken()) && imageThumbList.size() ==0)
        {
            istragram_image_list_view.setVisibility(View.GONE);
        }




        istragram_image_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(getContext() , IstragramFullImageView.class);
                intent.putExtra("path_list" , imageStandredList) ;
                intent.putExtra("pos" , position+"");
                startActivity(intent);
            }
        });

        if (Validation.isStringNullOrBlank(bean.getInstragramToken()) == false)
        {
            setInstrgramImageOnHorizontalView();
        }

        if (DirectorFragmentManageActivity.backButton != null) {
            DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
            DirectorFragmentManageActivity.showBackButton();
            DirectorFragmentManageActivity.showLogoutButton();
        }

        if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER) ||SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_COACH))
        {
            relative_layout_bootom.setVisibility(View.GONE);
        }
        aQuery = new AQuery(getActivity());
        profile_pic_imageview = (ImageView) convertView.findViewById(R.id.profile_pic_imageview);

        try {
            String url = "";




            if (type == 2)
            {
                url = bean.getUser_profilepic();
            }
            else
            {
                url = bean.getUserPicsArrayList().get(0).getImage_thumb();
            }

            Log.e("Url", url);
            imageLoaderProgressbar.setVisibility(View.VISIBLE);
                     Picasso.with(getActivity())
                    .load(url).placeholder(getActivity(). getResources().getDrawable( R.drawable.default_img_profile )) // optional
		            .error(R.drawable.default_img_profile)
                    .transform(new CircleTransform())        // optional
                    .into(profile_pic_imageview, new Callback() {
                        @Override
                        public void onSuccess() {
                            imageLoaderProgressbar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {
                            imageLoaderProgressbar.setVisibility(View.GONE);

                        }
                    });

        } catch (Exception e) {
            imageLoaderProgressbar.setVisibility(View.GONE);

            profile_pic_imageview.setBackground(getResources().getDrawable(R.drawable.default_img_profile));

        }


        profile_pic_imageview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( type == 1 && bean.getUserPicsArrayList().size() != 0)
                {
                    Intent intent = new Intent(getActivity() , EventFullImageViewActivity.class);


                    ArrayList<String>pathList = new ArrayList<String>();

                    for (int i = 0 ; i < bean.getUserPicsArrayList().size() ;i++ )
                    {
                        pathList.add(bean.getUserPicsArrayList().get(i).getImage_url());
                    }


                    intent.putExtra("path_list", pathList);
                    intent.putExtra("pos", String.valueOf(0));
                    startActivity(intent);
                }
                else
                {
                    if (Utill.isValidLink(SessionManager.getClub_Logo(getActivity())))
                    {
                        Intent intent = new Intent(getActivity() , FullImageShowFragment.class);

                        intent.putExtra("image_path", bean.getUser_profilepic());
                        intent.putExtra("image_type", String.valueOf(2));
                        startActivity(intent);
                    }

                }

            }
        });

        face_book_btn = (ImageButton) convertView.findViewById(R.id.face_book_btn);
        linkedin_btn = (ImageButton) convertView.findViewById(R.id.linkedin_btn);
        instagram_btn = (ImageButton) convertView.findViewById(R.id.instagram_btn);
        twitter_btn = (ImageButton) convertView.findViewById(R.id.twitter_btn);
        send_msg_btn = (ImageButton) convertView.findViewById(R.id.send_msg_btn);

        if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER)||SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_COACH)) {
            send_msg_btn.setAlpha(0.08f);
            send_msg_btn.setEnabled(false);
        }

        edit_btn = (TextView) convertView.findViewById(R.id.edit_btn);
        block_btn = (TextView) convertView.findViewById(R.id.block_btn);
        DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.VISIBLE);
        DirectorFragmentManageActivity.uploadNewsOrEditProfile.setImageDrawable(getResources().getDrawable(R.drawable.edit_btn));

        /*DirectorFragmentManageActivity.uploadNewsOrEditProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        DirectorFragmentManageActivity.uploadNewsOrEditProfile.setOnClickListener(socialmediobtnclicklistener);



        face_book_btn.setOnClickListener(socialmediobtnclicklistener);
        linkedin_btn.setOnClickListener(socialmediobtnclicklistener);
        instagram_btn.setOnClickListener(socialmediobtnclicklistener);
        twitter_btn.setOnClickListener(socialmediobtnclicklistener);
        edit_btn.setOnClickListener(socialmediobtnclicklistener);
        block_btn.setOnClickListener(socialmediobtnclicklistener);
        DirectorFragmentManageActivity.uploadNewsOrEditProfile.setOnClickListener(socialmediobtnclicklistener);


        if (type == 1) {
            //Utill.showDialg("Type "+type, getActivity());
            DirectorFragmentManageActivity.ad_icon_iv.setVisibility(View.VISIBLE);
            edit_btn.setVisibility(View.INVISIBLE);
            block_btn.setVisibility(View.INVISIBLE);
            Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.point_list_dialog_box);
          //  dialog.show();
            DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.VISIBLE);
            DirectorFragmentManageActivity.ad_icon_iv.setVisibility(View.INVISIBLE);
            //DirectorFragmentManageActivity.updateTitle(title)
        } else {


           if (bean.getUser_type().equals(AppConstants.USER_TYPE_DIRECTOR) &&  SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MOBILE_ADMIN))
           {
               relative_layout_bootom.setVisibility(View.INVISIBLE);
           }

            DirectorFragmentManageActivity.ad_icon_iv.setVisibility(View.INVISIBLE);

            DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.INVISIBLE);
        }

        first_name_tv = (TextView) convertView.findViewById(R.id.first_name_tv);
        rating_tv = (TextView) convertView.findViewById(R.id.rating_tv);

        first_name_tv.setText(bean.getUser_first_name());


        SessionManager sessionManager = new SessionManager();

        if (sessionManager.getUserRatingType(getActivity()) == 2)
            rating_tv.setText(bean.getUser_rating());
        else {
            rating_tv.setText("Not available");
        }

        last_name_tv = (TextView) convertView.findViewById(R.id.last_name_tv);
        last_name_tv.setText(bean.getUser_last_name());


        if (bean.getUser_junior().equals("1")) {

        }
        email_tv = (TextView) convertView.findViewById(R.id.email_tv);


        if (bean.getUser_email().equalsIgnoreCase("") || bean.getUser_email() == "" || bean.getUser_email().equalsIgnoreCase("hidden"))
        {
            email_tv.setText("hidden");
            email_tv.setEnabled(false);
        }
        else
        {
            email_tv.setText(bean.getUser_email());
        }


        if (bean.getUser_status().equals("2")) {
            block_btn.setBackgroundColor(getActivity().getResources().getColor(R.color.green_bg_color));
            block_btn.setText("Unblock");
        } else {
            block_btn.setBackgroundColor(getActivity().getResources().getColor(R.color.red));
            block_btn.setText("Block");
        }

        try {
            if (type == 1) {
                if (Integer.parseInt(bean.getUser_id()) != Integer.parseInt(SessionManager.getUser_id(getActivity()))) {
                    View view = convertView.findViewById(R.id.view);
                    view.setVisibility(View.VISIBLE);
                    send_msg_btn.setVisibility(View.VISIBLE);
                    send_msg_btn.setOnClickListener(socialmediobtnclicklistener);
                }
            } else {
                View view = convertView.findViewById(R.id.view);
                view.setVisibility(View.VISIBLE);
                send_msg_btn.setVisibility(View.VISIBLE);
                send_msg_btn.setOnClickListener(socialmediobtnclicklistener);
            }


        } catch (Exception e) {

        }


        //Toast.makeText(getActivity(), "first Id "+bean.getUser_id()+" sendond id "+SessionManager.getUser_id(getActivity()), Toast.LENGTH_LONG).show();
        email_tv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("text/plain");
                String text = "YOUR TEXT HERE";
                waIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{bean.getUser_email()});
                waIntent.setPackage("com.google.android.gm");

                waIntent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(waIntent, "Share with"));

            }
        });
        contact_no_tv = (TextView) convertView.findViewById(R.id.contact_no_tv);

        if (bean.getUser_phone().equalsIgnoreCase("hidden")) {

            contact_no_tv.setText("hidden");
            contact_no_tv.setEnabled(false);
        } else {
            contact_no_tv.setText(bean.getUser_phone());
        }

        contact_no_tv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


				/* */
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                int screenWidth = display.getWidth();
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.call_meg_layout);
                dialog.getWindow().setLayout((screenWidth * 80) / 100, LayoutParams.WRAP_CONTENT);


                TextView title_tv = (TextView) dialog.findViewById(R.id.title_tv);
                title_tv.setText(SessionManager.getClubName(getActivity()));

                TextView call_btn = (TextView) dialog.findViewById(R.id.call_btn);

                TextView msg_btn = (TextView) dialog.findViewById(R.id.msg_btn);

                TextView cancel_btn = (TextView) dialog.findViewById(R.id.cancel_btn);


                call_btn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        try {
                            String uri = "tel:" + bean.getUser_phone().trim();
                            Intent intent = new Intent(Intent.ACTION_DIAL);

                            intent.setData(Uri.parse(uri));
                            startActivity(intent);
                            dialog.dismiss();
                        } catch (Exception e) {
                            Utill.showDialg("Mobile no is worong", getActivity());
                        }


                    }
                });

                msg_btn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        try {
                            String uri = "tel:" + bean.getUser_phone().trim();
                            Intent intentt = new Intent(Intent.ACTION_VIEW);
                            intentt.setData(Uri.parse("sms:"));
                            intentt.setType("vnd.android-dir/mms-sms");
                            intentt.putExtra(Intent.EXTRA_TEXT, "");
                            intentt.putExtra("address", uri);
                            getActivity().startActivity(intentt);
                            dialog.dismiss();
                        } catch (Exception e) {
                            Utill.showDialg("Mobile no is worong", getActivity());
                        }

                    }
                });
                cancel_btn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });

                dialog.show();

                // startActivity(intent);
            }
        });


        gender_tv = (TextView) convertView.findViewById(R.id.gender_tv);

        // Toast.makeText(getActivity(), "id   "+bean.getUser_rating(), 1).show();

        if (bean.getUser_gender().equals("1") || bean.getUser_gender().equalsIgnoreCase("Male")) {
            gender_tv.setText("Male");
        }
        if (bean.getUser_gender().equals("2") || bean.getUser_gender().equalsIgnoreCase("Female")) {
            gender_tv.setText("Female");
        }
        about_me_tv = (TextView) convertView.findViewById(R.id.about_me_tv);
        about_me_tv.setText(bean.getUser_about_me());

        histry_id = (ImageView) convertView.findViewById(R.id.histry_id);

        histry_id.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("user_id", bean.getUser_id());
                params.put("user_type", SessionManager.getUser_type(getActivity()));
                pd = new ProgressDialog(getActivity());
                pd.setCancelable(false);
                pd.setMessage("Fetching");
                pd.show();
                aQuery.ajax(WebService.scoreresult, params, JSONObject.class, new AjaxCallback<JSONObject>()

                        {
                            @SuppressWarnings("deprecation")
                            public void callback(String url, JSONObject object, AjaxStatus status) {
                                super.callback(url, object, status);
                                pd.dismiss();
                                try {
                                    Log.e("result ", object.toString());
                                    ArrayList<HashMap<String, String>> score_list = new ArrayList<HashMap<String, String>>();

                                    if (Boolean.parseBoolean(object.getString("status"))) {
                                        JSONArray score_list_json_array = new JSONArray(object.getString("response"));
                                        for (int i = 0; i < score_list_json_array.length(); i++) {
                                            JSONObject score_list_json_array_item = score_list_json_array.getJSONObject(i);
                                            HashMap<String, String> score_list_item = new HashMap<String, String>();
                                            score_list_item.put("score_result_score_id", score_list_json_array_item.getString("score_result_score_id"));
                                            score_list_item.put("score_result_winningstatus", score_list_json_array_item.getString("score_result_winningstatus"));
                                            score_list_item.put("score_result", score_list_json_array_item.getString("score_result"));


                                            score_list.add(score_list_item);
                                        }
                                        Dialog dialog = new Dialog(getActivity());
                                        //dialog.setCancelable(false);
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.setContentView(R.layout.score_list_dailog_layout);
                                        ListView score_list_view = (ListView) dialog.findViewById(R.id.score_list_view);
                                        score_list_view.setAdapter(new ScoreListAdapter(score_list, getActivity()));

                                        Log.e("lsi size ", score_list.size() + "");
                                        if (score_list.size() != 0)
                                            dialog.show();
                                    }

                                    if (score_list.size() == 0) {

                                        AlertDialog alertDialog = new AlertDialog.Builder(
                                                getActivity()).create();
                                        // Setting Dialog Title
                                        alertDialog.setTitle(SessionManager.getClubName(getActivity()));

                                        // Setting Dialog Message
                                        alertDialog.setMessage("No score publish till yet");
                                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                // Write your code here to execute after dialog closed
                                            }
                                        });

                                        // Setting OK Button


                                        // Showing Alert Message
                                        alertDialog.show();
                                    }
                                } catch (Exception e) {
                                    //Toast.makeText(getActivity(), e.getMessage(), 1).show();
                                }

                            }
                        }

                );
            }
        });

        return convertView;
    }
   TextView discription_textview_status;
    public void createDilog() {


        final Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.email_notification_layout);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
        discription_textview_status = (TextView) dialog.findViewById(R.id.discription_textview_status);

        email_notification_msg = (EditText) dialog.findViewById(R.id.email_notification_msg);


        email_notification_msg.addTextChangedListener(new TextWatcher() {
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
        cancel_dialog_btn = (TextView) dialog.findViewById(R.id.cancel_dialog_btn);
        email_send_btn = (TextView) dialog.findViewById(R.id.email_send_btn);
        notification_send_btn = (TextView) dialog.findViewById(R.id.notification_send_btn);
        email_notification_msg = (EditText) dialog.findViewById(R.id.email_notification_msg);
        cancel_dialog_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });


        email_send_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (email_notification_msg.getText().toString() != "" && !email_notification_msg.getText().toString().equalsIgnoreCase("")) {
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("sender_id", SessionManager.getUser_id(getActivity()));
                    params.put("user_id", bean.getUser_id());
                    params.put("notify_via", "2");
                    params.put("message", email_notification_msg.getText().toString());

                    pd = new ProgressDialog(getActivity());
                    pd.setCancelable(false);
                    pd.show();


                    aQuery.ajax(WebService.usermail, params, JSONObject.class, new AjaxCallback<JSONObject>() {


                        @Override
                        public void callback(String url, JSONObject jsonObj, AjaxStatus status) {
                            // TODO Auto-generated method stub
                            super.callback(url, jsonObj, status);

                            try {
                                Utill.showDialg(jsonObj.getString("message"), getActivity());
                                //Toast.makeText(getActivity(), jsonObj.getString("message"), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {

                            }
                            dialog.cancel();
                            pd.dismiss();
                        }
                    });

                } else {
                    Utill.showDialg("Enter Message", getActivity());
                    //Toast.makeText(getActivity(), "Enter Message", Toast.LENGTH_LONG).show();
                }

            }
        });


        notification_send_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (email_notification_msg.getText().toString() != "" && !email_notification_msg.getText().toString().equalsIgnoreCase("")) {
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("sender_id", SessionManager.getUser_id(getActivity()));
                    params.put("user_id", bean.getUser_id());
                    params.put("notify_via", "1");
                    params.put("message", email_notification_msg.getText().toString());

                    pd = new ProgressDialog(getActivity());
                    pd.setCancelable(false);
                    pd.show();


                    aQuery.ajax(WebService.usermail, params, JSONObject.class, new AjaxCallback<JSONObject>() {


                        @Override
                        public void callback(String url, JSONObject jsonObj, AjaxStatus status) {
                            // TODO Auto-generated method stub
                            super.callback(url, jsonObj, status);

                            try {
                                String sss = jsonObj + "";
                                Log.e("fdsasf", sss + "");
                                Utill.showDialg(jsonObj.getString("message"), getActivity());
                                //	Toast.makeText(getActivity(), jsonObj.getString("message"), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {

                            }
                            dialog.cancel();
                            pd.dismiss();
                        }
                    });

                } else {
                    Utill.showDialg("Enter Message", getActivity());
                    //Toast.makeText(getActivity(), "Enter Message", Toast.LENGTH_LONG).show();
                }

            }
        });

	/*

*/
    }

    @Override
    public void onDestroy() {
        DirectorFragmentManageActivity.ad_icon_iv.setVisibility(View.GONE);
        DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.GONE);

        try {

            Bundle bundle = getArguments() ;
           FragmentBackResponseListener fragmentBackResponseListener = (FragmentBackResponseListener) bundle.getSerializable("fragmentBackListenerabc");
            fragmentBackResponseListener.UpdateView();
        }
        catch (Exception e)
        {

        }
        super.onDestroy();


    }
    ArrayList<String> intragramImageThumbList = new ArrayList<String>();
    ArrayList<String> intragramimageStandredList = new ArrayList<String>();


    public void setInstrgramImageOnHorizontalView()
    {

        String url = "https://api.instagram.com/v1/users/self/media/recent/?access_token="+bean.getInstragramToken();
        HashMap<String,Object>param = new HashMap<>();
        httpRequest.getResponse(getActivity(), url, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {

                intragramImageThumbList.clear();
                intragramimageStandredList.clear();



                try {
                    JSONArray data = jsonObject.getJSONArray(TAG_DATA);

                     int limit = data.length();

                    if (limit >10)
                    {
                        limit = 10 ;
                    }


                    for (int data_i = 0; data_i < limit; data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);

                        String type = data_obj.getString(TAG_TYPE);


                        if (type.equals("image"))
                        {
                            JSONObject images_obj = data_obj
                                    .getJSONObject(TAG_IMAGES);


                            JSONObject thumbnail_obj = images_obj
                                    .getJSONObject(TAG_THUMBNAIL);


                            JSONObject STANDRED_obj = images_obj
                                    .getJSONObject(STANDRED_RESOLUTION);

                            // String str_height =
                            // thumbnail_obj.getString(TAG_HEIGHT);
                            //
                            // String str_width =
                            // thumbnail_obj.getString(TAG_WIDTH);

                            String str_url = thumbnail_obj.getString(TAG_URL);
                            String standerd_url = STANDRED_obj.getString(TAG_URL);

                            intragramImageThumbList.add(str_url);
                            intragramimageStandredList.add(standerd_url);
                        }
                        else
                        {
                            JSONArray carousel_media_json_array = data_obj.getJSONArray("carousel_media");
                            for (int i = 0 ; i < carousel_media_json_array.length() ;i++)
                            {
                                JSONObject carousel_media_json_array_item = carousel_media_json_array.getJSONObject(i)
                                        .getJSONObject(TAG_IMAGES);


                                JSONObject thumbnail_obj = carousel_media_json_array_item
                                        .getJSONObject(TAG_THUMBNAIL);

                                JSONObject STANDRED_obj = carousel_media_json_array_item
                                        .getJSONObject(STANDRED_RESOLUTION);

                                // String str_height =
                                // thumbnail_obj.getString(TAG_HEIGHT);
                                //
                                // String str_width =
                                // thumbnail_obj.getString(TAG_WIDTH);

                                String str_url = thumbnail_obj.getString(TAG_URL);
                                String standerd_url = STANDRED_obj.getString(TAG_URL);

                                intragramImageThumbList.add(str_url);
                                intragramimageStandredList.add(standerd_url);
                            }
                        }
                    }
                }

                catch (Exception e)
                {

                }

                imageThumbList.addAll(intragramImageThumbList);
                imageStandredList.addAll(intragramimageStandredList);

                gallaryImageAdapter.notifyDataSetChanged();



            }
        });
    }



    public FragmentBackListener fragmentBackListener = new FragmentBackListener() {
        @Override
        public void onBackFragment()
        {
            if (DirectorFragmentManageActivity.actionbar_titletext != null) {

                if(type == 1)
                {
                    DirectorFragmentManageActivity.updateTitle("View Profile");
                    DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.VISIBLE);


                }
                else
                {
                    DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.INVISIBLE);

                    DirectorFragmentManageActivity.updateTitle("View Member Profile");
                }


            }

            if (gallaryImageAdapter != null)
            {
                try {

                    if (bean.getUserPicsArrayList() != null)
                    {
                        imageThumbList.clear();
                        imageStandredList.clear();
                        ArrayList<UserPics> userPicsArrayList = bean.getUserPicsArrayList() ;
                        for (int i = 0 ; i < userPicsArrayList.size() ; i++)
                        {
                            imageThumbList.add(userPicsArrayList.get(i).getImage_thumb());
                            imageStandredList.add(userPicsArrayList.get(i).getImage_url());

                        }

                        imageThumbList.addAll(intragramImageThumbList);
                        imageStandredList.addAll(intragramimageStandredList);

                    }




                    gallaryImageAdapter.notifyDataSetChanged();









                    String url = "";

                 //   imageLoaderProgressbar.setVisibility(View.VISIBLE);

                    url = bean.getUserPicsArrayList().get(0).getImage_thumb();

                    Picasso.with(getActivity())
                            .load(url).placeholder(getActivity(). getResources().getDrawable( R.drawable.default_img_profile )) // optional
                            .error(R.drawable.default_img_profile).transform(new CircleTransform())        // optional
                            .into(profile_pic_imageview, new Callback()
                            {
                                @Override
                                public void onSuccess()
                                {
                                    imageLoaderProgressbar.setVisibility(View.GONE);

                                }

                                @Override
                                public void onError()
                                {
                                    imageLoaderProgressbar.setVisibility(View.GONE);

                                }
                            });




                }
                catch (Exception e)
                {

                }
            }


        }
    };




}