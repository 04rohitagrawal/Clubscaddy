package com.clubscaddy.fragment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.Bean.UserPics;
import com.clubscaddy.Interface.FragmentBackListener;
import com.clubscaddy.Interface.ImageResizingListener;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.ProfileWizardActivity;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.custumview.CustomScrollView;
import com.clubscaddy.utility.CircleBitmapTranslation;
import com.clubscaddy.Adapter.AppRatingAdapter;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.utility.SqlListe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.clubscaddy.R;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.CircleTransform;
import com.clubscaddy.utility.ImageDecoder;
import com.clubscaddy.custumview.SDCardMemory;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.Server.WebService;

public class EditMyProfileFragment extends Fragment {

    private static final int CAMERA_REQUEST = 11;
    private static final int SELECT_FILE = 12;
    public static FragmentManager mFragmentManager;
    public static Fragment mFragment;
    /* public static boolean edit = false; */
    // public static boolean referesh = true;
    public static boolean localImage = false;
    MemberListBean bean;
    String Tag = getClass().getName();
    Context mContext;
    boolean imageselectedflag = false;
    ImageView addadmin_profileImage;
    ImageButton addadmin_pickfile;
    EditText addadmin_firstname, addadmin_lastname, addadmin_email, addadmin_phone;
    Button adminList_addAdmin;
    ToggleButton addadmin_gender;
    Spinner rating_spinner;
    int gender_type;
    Float isRating;
    TextView ratingTV;
    CharSequence[] items;
    TextView clear_porfile_pic_btn;
    EditText face_book_url_edittxt;
    EditText linkedin_url_edittxt;
    EditText instagram_url_edittxt;
    EditText twitter_url_edittxt;
    EditText addabout_url_edittxt;
    LinearLayout junior_linear_layout;
    //	ImageButton actionbar_backbutton1;
    ProgressBar progress;
    ArrayList<String> spinner_list = new ArrayList<String>();
    // Camera Code
    Bitmap imageBitmap;
    int flag1;
    Bitmap photo, image;
    String absPath;
    int rotate;
    String filePath = "";
    Uri cameraImagePath;
    int currentVersion = Build.VERSION.SDK_INT;
    LinearLayout rating_relative_layout;
    String picturePath = Environment.getExternalStorageDirectory().getPath();
    AlertDialog.Builder builder;
    CustomScrollView custum_croll_view;
    TextView discription_textview_status;


    HttpRequest httpRequest;




    SessionManager sessionManager;
    int type;
    ToggleButton junior_btn;
    String junior_type;



  ShowUserMessage showUserMessage ;
FragmentBackListener fragmentBackListener ;















    OnClickListener addToBack = new OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                Utill.hideKeybord(getActivity());
                 getActivity().getSupportFragmentManager().popBackStack();
            } catch (Exception e) {
                ShowUserMessage.showUserMessage(mContext, e.toString());
            }

        }
    };
    OnClickListener addToAdmin = new OnClickListener() {

        @Override
        public void onClick(View v) {
            addToAdminService();
        }
    };
    OnClickListener addToGender = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (addadmin_gender.isChecked()) {
                gender_type = 1;
            } else {
                gender_type = 2;
            }
        }
    };
    OnClickListener addToPickFile = new OnClickListener() {

        @Override
        public void onClick(View v) {


            if (bean.getUser_id().equals(SessionManager.getUser_id(getActivity())))
            {
                Intent intent = new Intent(getContext(), ProfileWizardActivity.class);
                intent.putExtra("imagepath", bean.getUserPicsArrayList());
                intent.putExtra("action", 2);
                startActivityForResult(intent, 1011);
            } else {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pickImage();
                    }
                });
            }


        }
    };

    public static Fragment getInstance(FragmentManager mFragManager) {
        if (mFragment == null) {
            mFragment = new EditMyProfileFragment();
        }
        // referesh = true;
        localImage = false;
        return mFragment;
    }

    public void setInstanse(MemberListBean bean, int type , FragmentBackListener fragmentBackListener) {
        this.bean = bean;
        this.type = type;
        this.fragmentBackListener = fragmentBackListener;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.e(Tag, "onAttach");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.edit_member_fragment, container, false);

        discription_textview_status = (TextView) rootView.findViewById(R.id.discription_textview_status);

        mContext = getActivity();
        spinner_list.clear();
        showUserMessage = new ShowUserMessage(getActivity());


        DirectorFragmentManageActivity.ad_icon_iv.setVisibility(View.GONE);
        DirectorFragmentManageActivity.uploadNewsOrEditProfile.setVisibility(View.GONE);

        httpRequest = new HttpRequest(getActivity());

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        custum_croll_view = (CustomScrollView) rootView.findViewById(R.id.custum_croll_view);

        clear_porfile_pic_btn = (TextView) rootView.findViewById(R.id.clear_porfile_pic_btn);

        builder = new AlertDialog.Builder(mContext);
        clear_porfile_pic_btn.setVisibility(View.GONE);


        if (Validation.isStringNullOrBlank(bean.getUser_profilepic()) || bean.getUser_profilepic().equals("http://clubscaddy.com/appwebservices//user_images/bydefault.png")) {

            builder.setTitle("Select pictures");
            items = new CharSequence[3];
            items[0] = "Take from camera";
            items[1] = "Upload from gallery";
            items[2] = "Cancel";
        } else {
            builder.setTitle("Edit picture");
            items = new CharSequence[4];
            items[0] = "Take from camera";
            items[1] = "Upload from gallery";
            items[2] = "Delete current picture";
            items[3] = "Cancel";
        }

        sessionManager = new SessionManager();

        try {
            spinner_list.add("None");
            JSONArray jsonArray = new JSONArray(sessionManager.getClubRatting(getActivity()));
            for (int i = 0; i < jsonArray.length(); i++) {
                spinner_list.add(jsonArray.getString(i));
            }
        } catch (Exception e) {

        }

		/*


        spinner_list.add("3.0");
		spinner_list.add("3.5");
		spinner_list.add("4.0");
		spinner_list.add("4.5");
		spinner_list.add("5.0");
		spinner_list.add("5.5");*/


        try {

            if (DirectorFragmentManageActivity.actionbar_titletext != null) {
                DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.edit_profile));
            }


            face_book_url_edittxt = (EditText) rootView.findViewById(R.id.face_book_url_edittxt);
            linkedin_url_edittxt = (EditText) rootView.findViewById(R.id.linkedin_url_edittxt);
            instagram_url_edittxt = (EditText) rootView.findViewById(R.id.instagram_url_edittxt);
            twitter_url_edittxt = (EditText) rootView.findViewById(R.id.twitter_url_edittxt);
            addabout_url_edittxt = (EditText) rootView.findViewById(R.id.addabout_url_edittxt);


            addabout_url_edittxt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    Log.e("action", event.getAction() + "");


                    //	Toast.makeText(getActivity() ,"action  " + event.getAction(),Toast.LENGTH_SHORT ).show();


                    if (1 == event.getAction()) {
                        custum_croll_view.setEnableScrolling(true);

                    } else {

                        custum_croll_view.setEnableScrolling(false);


                    }

                    return false;
                }
            });


            addabout_url_edittxt.addTextChangedListener(new AddTextChangeListener());


            addadmin_firstname = (EditText) rootView.findViewById(R.id.addadmin_firstname);
            addadmin_lastname = (EditText) rootView.findViewById(R.id.addadmin_lastname);
            addadmin_email = (EditText) rootView.findViewById(R.id.addadmin_email);
            addadmin_phone = (EditText) rootView.findViewById(R.id.addadmin_phone);
            adminList_addAdmin = (Button) rootView.findViewById(R.id.adminList_addAdmin);
            addadmin_gender = (ToggleButton) rootView.findViewById(R.id.addadmin_gender);

            rating_spinner = (Spinner) rootView.findViewById(R.id.rating_spinner);
            junior_linear_layout = (LinearLayout) rootView.findViewById(R.id.junior_linear_layout);

            rating_relative_layout = (LinearLayout) rootView.findViewById(R.id.rating_relative_layout);

            AppRatingAdapter adapter = new AppRatingAdapter(getActivity(), spinner_list ,rating_spinner);


            if (sessionManager.getClubRatingType(getActivity()) == 2) {
                rating_relative_layout.setVisibility(View.VISIBLE);
            } else {
                rating_relative_layout.setVisibility(View.GONE);
            }


            rating_spinner.setAdapter(adapter);
            try {
                int index = spinner_list.indexOf(bean.getUser_rating());


                if (index == -1) {
                    rating_spinner.setSelection(0);
                } else {
                    rating_spinner.setSelection(index);
                }


            } catch (Exception ee) {

            }


            addadmin_pickfile = (ImageButton) rootView.findViewById(R.id.addadmin_pickfile);
            addadmin_profileImage = (ImageView) rootView.findViewById(R.id.addadmin_profileImage);
            progress = (ProgressBar) rootView.findViewById(R.id.prog);
            ratingTV = (TextView) rootView.findViewById(R.id.ratting_tv);
            //ratingTV.setVisibility(View.GONE);

            adminList_addAdmin.setOnClickListener(addToAdmin);
            //Utill.showDialg(bean.getUser_gender(), mContext);
            if (bean.getUser_gender().equalsIgnoreCase("Male") || bean.getUser_gender().equalsIgnoreCase("1")) {
                addadmin_gender.setChecked(true);
            } else {
                addadmin_gender.setChecked(false);
            }
            addadmin_gender.setOnClickListener(addToGender);


            addadmin_pickfile.setOnClickListener(addToPickFile);

            junior_btn = (ToggleButton) rootView.findViewById(R.id.junior_btn);

            // addadmin_ratingBar.on

//


            if (sessionManager.getClubRatingType(getActivity()) == 2) {
                rating_relative_layout.setVisibility(View.VISIBLE);
            } else {
                rating_relative_layout.setVisibility(View.GONE);
            }

            //

            setDataToView(bean);

        } catch (Exception e) {
           // ShowUserMessage.showUserMessage(mContext, e.toString());
        }
        DirectorFragmentManageActivity.showLogoutButton();


        return rootView;
    }

    void setDataToView(MemberListBean bean) {
        addadmin_firstname.setText(bean.getUser_first_name());
        addadmin_lastname.setText(bean.getUser_last_name());
        addadmin_phone.setText(bean.getUser_phone());
        addadmin_email.setText(bean.getUser_email());
        addadmin_phone.setText(bean.getUser_phone());
        //addadmin_ratingBar.setVisibility(View.GONE);
        instagram_url_edittxt.setText(bean.getInstragram_url());
        face_book_url_edittxt.setText(bean.getFace_book_url());
        twitter_url_edittxt.setText(bean.getTwitter_url());
        linkedin_url_edittxt.setText(bean.getLinkedin_url());
        addabout_url_edittxt.setText(bean.getUser_about_me());


        if (type == 2) {
            addadmin_email.setEnabled(true);
			/*addadmin_firstname.setEnabled(false);
			addadmin_lastname.setEnabled(false);
			addadmin_phone.setEnabled(false);
			addadmin_email.setEnabled(false);*/

        } else {
            addadmin_email.setEnabled(true);
        }

        //addadmin_email.setKeyListener(null);

        if (bean.getUser_junior().equals("1")) {
            junior_btn.setChecked(true);
        } else {
            junior_btn.setChecked(false);
        }

        String  url = "";

        if (type == 2)
        {
            url = bean.getUser_profilepic();
        }
        else
        {
            url = bean.getUserPicsArrayList().get(0).getImage_thumb();
        }

        progress.setVisibility(View.VISIBLE);

        Glide.with(mContext)
                .load(url)
                .placeholder(mContext.getResources().getDrawable(R.drawable.default_img_profile)) // optional
                .error(R.drawable.default_img_profile).transform(new CircleBitmapTranslation(getActivity()))         // optional
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progress.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(addadmin_profileImage);

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(Tag, "onStart");
		/*
		 * if (!edit && referesh) { referesh = false; clearFields(); }
		 */
    }

    void clearFields() {/*
						 * addadmin_firstname.setText("");
						 * addadmin_lastname.setText("");
						 * addadmin_email.setText("");
						 * addadmin_phone.setText("");
						 * addadmin_ratingBar.setRating(0);
						 * addadmin_profileImage
						 * .setImageDrawable(mContext.getResources
						 * ().getDrawable(R.drawable.defalt_profile));
						 * progress.setVisibility(View.GONE);
						 */
    }

    @Override
    public void onResume() {
        super.onResume();
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(Tag, "onDestroyView");
        try {
            fragmentBackListener.onBackFragment();
            AppConstants.hideSoftKeyboard(getActivity());
        } catch (Exception e) {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(Tag, "onDetach");
    }

    private void addToAdminService() {
        try {
            isRating = Float.parseFloat(rating_spinner.getSelectedItem().toString());
        } catch (Exception e) {
            isRating = 0.0f;
        }
        Utill.hideKeybord(getActivity());
        if (Utill.removeSpace(addadmin_firstname.getText().toString()).equals("")) {
            ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.empty_first_name));

            return;
        }

        if (Utill.removeSpace(addadmin_lastname.getText().toString()).toString().equals("")) {
            ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.empty_last_name));
            return;
        }

        if (Utill.removeSpace(addadmin_email.getText().toString()).equals("")) {
            ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.empty_email));
            return;
        }
        addadmin_email.setText(Utill.removeSpace(addadmin_email.getText().toString()));
        if (!Validation.isEmailAddress(addadmin_email, true)) {
            ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.correct_email));
            return;
        }

        if (Validation.isValidPhoneNumber(addadmin_phone.getText().toString()) == false)
        {
            ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.valid_moble_numver));
            return;
        }


        if (Utill.isValidLinkWithSpace(linkedin_url_edittxt.getText().toString()) == false && linkedin_url_edittxt.getText().toString().length() != 0) {
            ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.valid_linkedin_url));
            return;

        }
        if (Utill.isValidLinkWithSpace(instagram_url_edittxt.getText().toString()) == false && instagram_url_edittxt.getText().toString().length() != 0) {
            ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.valid_instragram_url));
            return;

        }


        if (Utill.isValidLinkWithSpace(face_book_url_edittxt.getText().toString()) == false && face_book_url_edittxt.getText().toString().length() != 0) {
            ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.valid_facebbok_url));
            return;

        }


        if (Utill.isValidLinkWithSpace(twitter_url_edittxt.getText().toString()) == false && twitter_url_edittxt.getText().toString().length() != 0) {
            ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.valid_twitter_url));
            return;

        } else {
            if (Validation.isNetworkAvailable(mContext))
            {

                if (addadmin_gender.isChecked())
                {
                    gender_type = 1;
                }
                else
                {
                    gender_type = 2;
                }
                if (junior_btn.isChecked()) {
                    junior_type = "1";
                } else {
                    junior_type = "2";
                }
             final   HashMap<String , Object> param = new HashMap<>();
                param.put("user_club_id" , bean.getUser_club_id());
                param.put("user_id",  bean.getUser_id());
                param.put("user_first_name", addadmin_firstname.getText().toString());
                param.put("user_last_name", addadmin_lastname.getText().toString());
                param.put("user_phone", addadmin_phone.getText().toString());
                param.put("user_gender", String.valueOf(gender_type));
                param.put("user_device_type", "1");
                param.put("user_device_token", SessionManager.getUser_Device_Token(mContext));
                param.put("user_type", bean.getUser_type());
                param.put("user_phone", Utill.removeSpace(addadmin_phone.getText().toString()));
                param.put("club_id", bean.getUser_club_id());
                String faceBookUrl = face_book_url_edittxt.getText().toString().replace(" ", "");
                String linkedinUrl = linkedin_url_edittxt.getText().toString().replace(" ", "");
                String instagramUrl = instagram_url_edittxt.getText().toString().replace(" ", "");
                String twitterUrl = twitter_url_edittxt.getText().toString().replace(" ", "");
                param.put("facebookurl", Utill.removeSpace(faceBookUrl));
                param.put("linkedin", Utill.removeSpace(linkedinUrl));
                param.put("instagramUrl", Utill.removeSpace(instagramUrl));
                param.put("twitterUrl", Utill.removeSpace(twitterUrl));
                param.put("addabout", addabout_url_edittxt.getText().toString().trim());
                if (rating_spinner.getSelectedItemPosition() == 0)
                {
                    param.put("user_rating", "0");

                } else {
                    String userRatting = spinner_list.get(rating_spinner.getSelectedItemPosition()) + "";
                    param.put("user_rating", userRatting);

                }
                String user_email = Utill.removeSpace(addadmin_email.getText().toString());
                param.put("user_email", user_email);
                param.put("user_junior", junior_type);
                final String Url ;
                if (bean.getUser_id().equals(SessionManager.getUser_id(getActivity())))
                {
                    Url = WebService.update_my_profile;
                }
                else
                {
                    Url = WebService.edit_admin_profile;
                }
                if (Validation.isStringNullOrBlank(filePath) == false)
                {

                        ImageDecoder imageDecoder = new ImageDecoder(getActivity());

                    ImageResizingListener imageResizingListener = new ImageResizingListener() {
                        @Override
                        public void onImageResize(String filePath)
                        {
                            param.put("user_profilepic", new File(filePath));
                            sendDataOnServer(Url , param);

                        }

                        @Override
                        public void onImageResize(ArrayList<String> imagePathList)
                        {

                        }
                    };
                    imageDecoder.imageResize(filePath , 1000 , imageResizingListener);
                }


                else
                {
                    sendDataOnServer(Url , param);

                }




            } else {
                ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.check_internet_connection));
            }
        }


    }
///

    public void sendDataOnServer(String url , HashMap<String , Object> param)
    {

        httpRequest.getResponse(getActivity(), url, param, new OnServerRespondingListener(getActivity()) {
            @Override
            public void onSuccess(JSONObject object)
            {

                try
                {
                    showMessageForFragmeneWithBack(getActivity() , object.getString("msg"));

                    if (object.getBoolean("status"))
                    {
                        MemberListBean bean = new MemberListBean();
                        bean.setUser_id(object.optString("user_id"));
                        bean.setUser_last_name(object.optString("user_last_name"));
                        bean.setUser_first_name(object.optString("user_first_name"));
                        bean.setUser_email(object.optString("user_email"));
                        bean.setUser_phone(object.optString("user_phone"));
                        bean.setUser_type(object.optString("user_type"));
                        bean.setUser_club_id(SessionManager.getUser_Club_id(mContext));
                        bean.setUser_rating(object.optString("user_rating"));
                        bean.setUser_profilepic(object.optString("user_profilepic"));
                        bean.setUser_gender(gender_type+"");
                        if (EditMyProfileFragment.this.bean.getUser_status().equals("true")||EditMyProfileFragment.this.bean.getUser_status().equals("1"))
                        {
                            bean.setUser_status("1");

                        }
                        else {
                            bean.setUser_status("2");

                        }

                        SqlListe sqlListe = new SqlListe(getActivity());
                        sqlListe.updateMenberList(bean);
                    }
                    else
                    {
                        Utill.showDialg(object.getString("message") , getActivity());
                    }


                }
                catch (Exception e)
                {
                   Utill.showDialg(e.getMessage() , getActivity());

                }

            }
        });


    }


    public static void showMessageForFragmeneWithBack(final FragmentActivity fragmentActivity , String msg)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(
                fragmentActivity).create();


        // Setting Dialog Title
        alertDialog.setTitle(SessionManager.getClubName(fragmentActivity));
        alertDialog.setCancelable(false);
        // Setting Dialog Message
        alertDialog.setMessage(msg);


        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                fragmentActivity.getSupportFragmentManager().popBackStack();
                fragmentActivity.getSupportFragmentManager().popBackStack();

                dialog.cancel();
            }
        });


        alertDialog.show();
    }




    private void pickImage() {


        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {
                try {
                    if (items[position].equals("Take from camera")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File sdCardDir = SDCardMemory.createSDCardDir(mContext);
                        File mFile = SDCardMemory.createImageSubDir(mContext, sdCardDir);
                        File file = new File(mFile, "tennis-img" + System.currentTimeMillis() + ".jpg");
                        absPath = file.getAbsolutePath();
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                        savePref(absPath);
                        ((Activity) mContext).startActivityForResult(intent, CAMERA_REQUEST);
                    } else if (items[position].equals("Upload from gallery")) {
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, SELECT_FILE);
                    } else {
                        if (items[position].equals("Delete current picture")) {
                            deleteProfilePic();
                        } else {
                            dialog.dismiss();
                        }


                    }
                } catch (Exception e) {
                    ShowUserMessage.showUserMessage(mContext, e.toString());
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void savePref(String str) {
        SharedPreferences sf = mContext.getSharedPreferences("LoginUser", 1);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString("imageURI", str);
        editor.commit();
    }

    private String getImageURI() {
        SharedPreferences sf = mContext.getSharedPreferences("LoginUser", 1);
        String uri = sf.getString("imageURI", null);
        return uri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {












            if (requestCode == SELECT_FILE && resultCode == getActivity().RESULT_OK)
            {
                if (flag1 == 0) {
                    if (data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = mContext.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        picturePath = cursor.getString(columnIndex);





                        cursor.close();
                    }
                }
            }

            if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK)
                {
                try {
                    String URI = getImageURI();
                    File file = new File(URI);
                    picturePath = file.getAbsolutePath();



                } catch (Exception e) {
                    ShowUserMessage.showUserMessage(mContext, e.toString());
                }

            }


            if ((requestCode == CAMERA_REQUEST || requestCode == SELECT_FILE)&& resultCode == getActivity().RESULT_OK)
            {
                image = ImageDecoder.decodeFile(picturePath);
                if (image != null) {
                    image = Bitmap.createBitmap(image);
                    // image = Bitmap.createScaledBitmap(image, 100,
                    // 100, false);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    Bitmap bm = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
                    // profilePic.setImageBitmap(bm);
                    imageBitmap = bm;
                    // profileImageView.setImageBitmap(imageBitmap);
                    //addadmin_profileImage.setImageBitmap(imageBitmap);

                    if (imageBitmap.getWidth() < 512 || imageBitmap.getHeight() < 512) {

                        ShowUserMessage.showDialogForImageDia(getActivity());
                        return;
                    }


                    try {

                        progress.setVisibility(View.VISIBLE);
                        Uri uri = Uri.fromFile(new File(picturePath));
                        Glide.with(getActivity())
                        .load(uri).placeholder(R.drawable.default_img_profile)
                        .error(R.drawable.default_img_profile)
                        .transform(new CircleBitmapTranslation(getActivity()))
                         .listener(new RequestListener<Uri, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        progress.setVisibility(View.GONE);

                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        progress.setVisibility(View.GONE);

                                        return false;
                                    }
                                })
                                .into(addadmin_profileImage);

                    } catch (Exception e) {
                        Utill.showDialg("Image not supported", mContext);
                    }
                    //Picasso.with(mContext).load(uri).into(imageView);
                    localImage = true;
                    // addadmin_profileImage.setImageBitmazCzxczxcp(imageBitmap);
                    // uploadBT.setBackgroundColor(Color.GREEN);
                    // event_invity_eventpic.setImageBitmap(imageBitmap);
                    Log.e("PicturePath 2 = ", picturePath);
                    // extention=picturePath.split("\\.");
                    // imageselectedflag=true;
                    filePath = picturePath;
                    // addFeedView();
                } else {
                    ShowUserMessage.showUserMessage(mContext, "Wrong Path");
                    imageselectedflag = false;
                }

            }







            if (requestCode == 11 && resultCode == getActivity().RESULT_OK)
            {
                setImageFromSDCard();
            }
        } catch (Exception e) {
            ShowUserMessage.showUserMessage(mContext, e.toString());
        }


        if (requestCode == 1011 && resultCode == 1010) {
            ArrayList<UserPics> userPicsArrayList = (ArrayList<UserPics>) data.getSerializableExtra("userPicsArrayList");
            bean.setUserPicsArrayList(userPicsArrayList);
            try {
                progress.setVisibility(View.VISIBLE);

                Picasso.with(getActivity()).load(userPicsArrayList.get(0).getImage_thumb()).placeholder(R.drawable.default_img_profile).error(R.drawable.default_img_profile).transform(new CircleTransform())
                        .into(addadmin_profileImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                progress.setVisibility(View.GONE);

                            }

                            @Override
                            public void onError() {
                                progress.setVisibility(View.GONE);

                            }
                        });
            } catch (Exception e) {

            }


        }

    }



	/*OnClickListener addToRating = new OnClickListener() {

		@Override
		public void onClick(View v) {
			isRating = addadmin_ratingBar.getRating();
		}
	};*/

    private void setImageFromSDCard() {


        if (currentVersion > 15) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            if (cameraImagePath != null) {
                Cursor imageCursor = mContext.getContentResolver().query(cameraImagePath, filePathColumn, null, null, null);
                if (imageCursor != null && imageCursor.moveToFirst()) {
                    int columnIndex = imageCursor.getColumnIndex(filePathColumn[0]);
                    filePath = imageCursor.getString(columnIndex);

                    imageCursor.close();
                }
            }
        } else {
            String struri = getImageURI();
            filePath = struri;
        }
        Bitmap bitmap = ImageDecoder.decodeFile(filePath);

        if (bitmap != null) {
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            imageBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

            localImage = true;
            bitmap.recycle();
        }
        try {
            Uri uri = Uri.fromFile(new File(filePath));
            Picasso.with(getActivity()).load(uri).transform(new CircleTransform()).into(addadmin_profileImage);

        } catch (Exception e) {
            Utill.showDialg("Image not supported", mContext);
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

    ;

    public void openGallery() {

    }

    public void deleteProfilePic() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle(SessionManager.getClubName(getActivity()));

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to remove profile picture ?");

        // Setting Icon to Dialog


        // Setting Positive "Yes" Button
        alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("user_id", bean.getUser_id());

                httpRequest.getResponse(getActivity(), WebService.delete_profilepic, params, new OnServerRespondingListener(getActivity()) {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        //
                        try {
                            ShowUserMessage.showDialogOnActivity(getActivity(), jsonObject.getString("message"));

                            if (jsonObject.getBoolean("status")) {
                                items = new CharSequence[3];
                                items[0] = "Take from camera";
                                items[1] = "Upload from gallery";
                                items[2] = "Cancel";
                                //clear_porfile_pic_btn.setVisibility(View.GONE);
                                bean.setUser_profilepic("");
                                addadmin_profileImage.setImageDrawable(getResources().getDrawable(R.drawable.default_img_profile));
                            }


                        } catch (Exception e) {

                        }

                    }
                });
                // Write your code here to invoke YES event
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }

    public class UpdateMyProfileListener {
        public void onSuccess(String msg) {
            ShowUserMessage.showUserMessage(mContext, msg);
            getActivity().getSupportFragmentManager().popBackStack();
            getActivity().getSupportFragmentManager().popBackStack();
        }

        public void onError(String message) {
            ShowUserMessage.showUserMessage(mContext, message);
        }
    }

    ;

    public class AddTextChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            discription_textview_status.setText(addabout_url_edittxt.getText().toString().length() + "/1000");
        }
    }


}