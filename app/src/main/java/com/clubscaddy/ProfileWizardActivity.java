package com.clubscaddy;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.clubscaddy.Adapter.InstragramImageListAdapter;
import com.clubscaddy.Bean.UserPics;
import com.clubscaddy.Interface.ImageResizingListener;
import com.clubscaddy.Interface.MyPermissionGrrantedListner;
import com.clubscaddy.imageutility.CircleBitmapTransformation;
import com.clubscaddy.imageutility.RoundedCornerBitmapTranslation;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.CircleTransform;
import com.clubscaddy.custumview.SDCardMemory;
import com.clubscaddy.utility.ImageDecoder;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.UserPermision;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.utility.RoundedTransformation;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.custumview.HorizontalListView;
import com.clubscaddy.fragment.IstragramFullImageView;
import com.clubscaddy.instragram.InstagramApp;
import com.clubscaddy.instragram.InstagramSession;
import com.clubscaddy.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by administrator on 21/11/16.
 */

public class ProfileWizardActivity extends AppCompatActivity {

    public static final String TAG_DATA = "data";
    public static final String TAG_IMAGES = "images";
    public static final String TAG_THUMBNAIL = "thumbnail";
    public static final String STANDRED_RESOLUTION = "standard_resolution";
    public static final String TAG_URL = "url";
    public static final String TAG_TYPE = "type";
    public final static int Camera_Request_Code_Image1 = 1001;
    public final static int Camera_Request_Code_Image2 = 1002;
    public final static int Camera_Request_Code_Image3 = 1003;
    public final static int Camera_Request_Code_Image4 = 1004;
    public final static int Camera_Request_Code_Image5 = 1005;
    public final static int Gallery_Request_Code_Image1 = 2001;
    public final static int Gallery_Request_Code_Image2 = 2002;
    public final static int Gallery_Request_Code_Image3 = 2003;
    public final static int Gallery_Request_Code_Image4 = 2004;
    public final static int Gallery_Request_Code_Image5 = 2005;
    TextView continue_tv;
    SessionManager sessionManager;
    ImageView back_button;
    TextView skip_btn;
    HorizontalListView istragram_image_list_view;
    HttpRequest httpRequest;
    ShowUserMessage showUserMessage;
    String picturePath = "";
    LinearLayout instragram_tv;
    InstragramImageListAdapter gallaryImageAdapter;
    InstagramSession instagramSession;
    ArrayList<String> imageThumbList;
    ArrayList<String> imageStandredList;
    LinearLayout instraram_image_layout;
    ArrayList<String> profileImageList;
    ImageView profile_image_view_one;
    ImageView add_profile_image_btn_one;
    ImageView delete_profile_image_btn_one;
    ImageView delete_profile_image_btn_two;
    ImageView add_profile_image_btn_two;
    ImageView profile_image_view_two;
    ImageView add_profile_image_btn_three;
    ImageView profile_image_view_three;
    ImageView delete_profile_image_btn_third;
    ImageView profile_image_view_fourth;
    ImageView add_profile_image_btn_fourth;
    ImageView delete_profile_image_btn_fourth;
    ImageView add_profile_image_btn_fifth;
    ImageView delete_profile_image_btn_fifth;
    ImageView profile_image_view_fifth;
    int action;
    String deleteId = "";
    String picId = "";
    ArrayList<String> gallryPermissionArrayList ;
    ArrayList<String> cameraPermissionArrayList ;

    ImageView profile_pic_imageview;
    public View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            profile_image_view_one.setPadding(0, 0, 0, 0);
            profile_image_view_two.setPadding(0, 0, 0, 0);
            profile_image_view_three.setPadding(0, 0, 0, 0);
            profile_image_view_fourth.setPadding(0, 0, 0, 0);
            profile_image_view_fifth.setPadding(0, 0, 0, 0);
            if (v.getId() == R.id.profile_image_view_one) {


                if (Validation.isStringNullOrBlank(profileImageList.get(0)) == false) {
                   /* profile_image_view_one.setBackground(getResources().getDrawable(R.drawable.profile_wizard_image_back_border));
                    profile_image_view_two.setBackground(getResources().getDrawable(R.drawable.profile_wized_image_back));
                    profile_image_view_three.setBackground(getResources().getDrawable(R.drawable.profile_wized_image_back));
                    profile_image_view_fourth.setBackground(getResources().getDrawable(R.drawable.profile_wized_image_back));
                    profile_image_view_fifth.setBackground(getResources().getDrawable(R.drawable.profile_wized_image_back));
                    profile_image_view_one.setPadding(2,2,2,2);*/

                    picturePath = profileImageList.get(0);
                    setPathProfileImageList(profileImageList.get(0), 0);
                }


            }
            if (v.getId() == R.id.profile_image_view_two) {
                if (Validation.isStringNullOrBlank(profileImageList.get(1)) == false) {
                /*profile_image_view_one.setBackground(getResources().getDrawable(R.drawable.profile_wized_image_back));
                profile_image_view_two.setBackground(getResources().getDrawable(R.drawable.profile_wizard_image_back_border));
                profile_image_view_three.setBackground(getResources().getDrawable(R.drawable.profile_wized_image_back));
                profile_image_view_fourth.setBackground(getResources().getDrawable(R.drawable.profile_wized_image_back));
                profile_image_view_fifth.setBackground(getResources().getDrawable(R.drawable.profile_wized_image_back));












                profile_image_view_two.setPadding(2,2,2,2);*/
                    picturePath = profileImageList.get(1);


                    setPathProfileImageList(profileImageList.get(1), 1);

                }


            }
            if (v.getId() == R.id.profile_image_view_three) {

                if (Validation.isStringNullOrBlank(profileImageList.get(2)) == false) {
                  /*  profile_image_view_one.setBackground(getResources().getDrawable(R.drawable.profile_wized_image_back));
                    profile_image_view_two.setBackground(getResources().getDrawable(R.drawable.profile_wized_image_back));
                    profile_image_view_three.setBackground(getResources().getDrawable(R.drawable.profile_wizard_image_back_border));
                    profile_image_view_fourth.setBackground(getResources().getDrawable(R.drawable.profile_wized_image_back));
                    profile_image_view_fifth.setBackground(getResources().getDrawable(R.drawable.profile_wized_image_back));
                    profile_image_view_three.setPadding(2,2,2,2);*/
                    picturePath = profileImageList.get(2);

                    setPathProfileImageList(profileImageList.get(2), 2);

                }
            }
            if (v.getId() == R.id.profile_image_view_fourth) {
                if (Validation.isStringNullOrBlank(profileImageList.get(3)) == false) {
                  /*  profile_image_view_one.setBackground(getResources().getDrawable(R.drawable.profile_wized_image_back));
                    profile_image_view_two.setBackground(getResources().getDrawable(R.drawable.profile_wized_image_back));
                    profile_image_view_three.setBackground(getResources().getDrawable(R.drawable.profile_wized_image_back));
                    profile_image_view_fourth.setBackground(getResources().getDrawable(R.drawable.profile_wizard_image_back_border));
                    profile_image_view_fifth.setBackground(getResources().getDrawable(R.drawable.profile_wized_image_back));
                    profile_image_view_fourth.setPadding(2,2,2,2); */
                    picturePath = profileImageList.get(3);

                    setPathProfileImageList(profileImageList.get(3), 3);

                }
            }
            if (v.getId() == R.id.profile_image_view_fifth) {
                if (Validation.isStringNullOrBlank(profileImageList.get(4)) == false) {
                  /*  profile_image_view_one.setBackground(getResources().getDrawable(R.drawable.profile_wized_image_back));
                    profile_image_view_two.setBackground(getResources().getDrawable(R.drawable.profile_wized_image_back));
                    profile_image_view_three.setBackground(getResources().getDrawable(R.drawable.profile_wized_image_back));
                    profile_image_view_fourth.setBackground(getResources().getDrawable(R.drawable.profile_wized_image_back));
                    profile_image_view_fifth.setBackground(getResources().getDrawable(R.drawable.profile_wizard_image_back_border));
                    profile_image_view_fifth.setPadding(2,2,2,2);*/
                    picturePath = profileImageList.get(4);

                    setPathProfileImageList(profileImageList.get(4), 4);

                }
            }

            if (Validation.isStringNullOrBlank(picturePath) == false) {
                if (Utill.isValidLink(picturePath)) {
                    Glide.with(getApplicationContext()).load(picturePath).transform(new CircleBitmapTransformation(ProfileWizardActivity.this)).into(profile_pic_imageview);
                } else {
                    Glide.with(getApplicationContext()).load(new File(picturePath)).transform(new CircleBitmapTransformation(ProfileWizardActivity.this)).into(profile_pic_imageview);
                }


            }

            return false;
        }
    };
    ArrayList<UserPics> serverImagepath;
    String cameraImagePath;
    String absPath = "";
    public View.OnClickListener firstImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            if (v.getId() == R.id.add_profile_image_btn_one && Validation.isStringNullOrBlank(profileImageList.get(0))) {
                pickImage(Gallery_Request_Code_Image1, Camera_Request_Code_Image1);

            }
            if (v.getId() == R.id.delete_profile_image_btn_one) {
                removePathProfileImageList(0);

            }


        }
    };
    public View.OnClickListener secondImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            if (v.getId() == R.id.add_profile_image_btn_two) {
                pickImage(Gallery_Request_Code_Image2, Camera_Request_Code_Image2);
            }


            if (v.getId() == R.id.delete_profile_image_btn_two) {
                removePathProfileImageList(1);

            }


        }
    };
    public View.OnClickListener thirdImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.add_profile_image_btn_three) {
                pickImage(Gallery_Request_Code_Image3, Camera_Request_Code_Image3);

            }

            if (v.getId() == R.id.delete_profile_image_btn_third) {
                removePathProfileImageList(2);

            }


        }
    };
    public View.OnClickListener fourthImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.add_profile_image_btn_fourth) {
                pickImage(Gallery_Request_Code_Image4, Camera_Request_Code_Image4);
            }


            if (v.getId() == R.id.delete_profile_image_btn_fourth) {
                removePathProfileImageList(3);

            }


        }
    };
    public View.OnClickListener fifthImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.add_profile_image_btn_fifth) {
                pickImage(Gallery_Request_Code_Image5, Camera_Request_Code_Image5);

            }

            if (v.getId() == R.id.delete_profile_image_btn_fifth) {
                removePathProfileImageList(4);

            }
        }
    };
    private InstagramApp mApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_profile_pic_layout);
        continue_tv = (TextView) findViewById(R.id.continue_tv);
        sessionManager = new SessionManager();
        profile_pic_imageview = (ImageView) findViewById(R.id.profile_pic_imageview);
        skip_btn = (TextView) findViewById(R.id.skip_btn);
        instragram_tv = (LinearLayout) findViewById(R.id.instragram_tv);
        httpRequest = new HttpRequest(this);
        instagramSession = new InstagramSession(getApplicationContext());
        imageThumbList = new ArrayList<>();
        imageStandredList = new ArrayList<>();
        instraram_image_layout = (LinearLayout) findViewById(R.id.instraram_image_layout);
        gallaryImageAdapter = new InstragramImageListAdapter(ProfileWizardActivity.this, imageThumbList);
        istragram_image_list_view = (HorizontalListView) findViewById(R.id.istragram_image_list_view);


        gallryPermissionArrayList = new ArrayList<>();
        cameraPermissionArrayList = new ArrayList<>();
        gallryPermissionArrayList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        gallryPermissionArrayList.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        cameraPermissionArrayList.add(Manifest.permission.CAMERA);
        cameraPermissionArrayList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        cameraPermissionArrayList.add(Manifest.permission.READ_EXTERNAL_STORAGE);


        action = getIntent().getIntExtra("action", 1);


        if (action == 1) {
            skip_btn.setVisibility(View.VISIBLE);
        } else {
            skip_btn.setVisibility(View.INVISIBLE);
        }


        serverImagepath = (ArrayList<UserPics>) getIntent().getSerializableExtra("imagepath");


        AppConstants.getDeviceWidth(ProfileWizardActivity.this);


        istragram_image_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(getApplicationContext(), IstragramFullImageView.class);
                intent.putExtra("path_list", imageStandredList);
                intent.putExtra("pos", position + "");
                startActivity(intent);
            }
        });


        profileImageList = new ArrayList<>();


        profileImageList.add("");
        profileImageList.add("");
        profileImageList.add("");
        profileImageList.add("");
        profileImageList.add("");


        profile_image_view_one = (ImageView) findViewById(R.id.profile_image_view_one);
        add_profile_image_btn_one = (ImageView) findViewById(R.id.add_profile_image_btn_one);
        delete_profile_image_btn_one = (ImageView) findViewById(R.id.delete_profile_image_btn_one);
        delete_profile_image_btn_two = (ImageView) findViewById(R.id.delete_profile_image_btn_two);
        add_profile_image_btn_two = (ImageView) findViewById(R.id.add_profile_image_btn_two);
        profile_image_view_two = (ImageView) findViewById(R.id.profile_image_view_two);
        add_profile_image_btn_three = (ImageView) findViewById(R.id.add_profile_image_btn_three);
        profile_image_view_three = (ImageView) findViewById(R.id.profile_image_view_three);
        delete_profile_image_btn_third = (ImageView) findViewById(R.id.delete_profile_image_btn_third);
        profile_image_view_fourth = (ImageView) findViewById(R.id.profile_image_view_fourth);
        add_profile_image_btn_fourth = (ImageView) findViewById(R.id.add_profile_image_btn_fourth);
        delete_profile_image_btn_fourth = (ImageView) findViewById(R.id.delete_profile_image_btn_fourth);
        add_profile_image_btn_fifth = (ImageView) findViewById(R.id.add_profile_image_btn_fifth);
        delete_profile_image_btn_fifth = (ImageView) findViewById(R.id.delete_profile_image_btn_fifth);
        profile_image_view_fifth = (ImageView) findViewById(R.id.profile_image_view_fifth);

        int size = (int) (AppConstants.getDeviceWidth(ProfileWizardActivity.this) / 3.2);

        RelativeLayout first_image_layout = (RelativeLayout) findViewById(R.id.first_image_layout);
        RelativeLayout second_image_layout = (RelativeLayout) findViewById(R.id.second_image_layout);
        RelativeLayout fourth_image_layout = (RelativeLayout) findViewById(R.id.fourth_image_layout);
        RelativeLayout third_image_layout = (RelativeLayout) findViewById(R.id.third_image_layout);
        RelativeLayout fifth_image_layout = (RelativeLayout) findViewById(R.id.fifth_image_layout);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
        first_image_layout.setLayoutParams(layoutParams);
        second_image_layout.setLayoutParams(layoutParams);
        third_image_layout.setLayoutParams(layoutParams);
        fourth_image_layout.setLayoutParams(layoutParams);
        fifth_image_layout.setLayoutParams(layoutParams);


        profile_image_view_one.setOnClickListener(firstImageClickListener);
        add_profile_image_btn_one.setOnClickListener(firstImageClickListener);
        delete_profile_image_btn_one.setOnClickListener(firstImageClickListener);
        delete_profile_image_btn_two.setOnClickListener(secondImageClickListener);
        add_profile_image_btn_two.setOnClickListener(secondImageClickListener);
        profile_image_view_two.setOnClickListener(secondImageClickListener);
        add_profile_image_btn_three.setOnClickListener(thirdImageClickListener);
        profile_image_view_three.setOnClickListener(thirdImageClickListener);
        delete_profile_image_btn_third.setOnClickListener(thirdImageClickListener);
        profile_image_view_fourth.setOnClickListener(fourthImageClickListener);
        add_profile_image_btn_fourth.setOnClickListener(fourthImageClickListener);
        delete_profile_image_btn_fourth.setOnClickListener(fourthImageClickListener);
        add_profile_image_btn_fifth.setOnClickListener(fifthImageClickListener);
        delete_profile_image_btn_fifth.setOnClickListener(fifthImageClickListener);
        profile_image_view_fifth.setOnClickListener(fifthImageClickListener);


        profile_image_view_one.setOnLongClickListener(onLongClickListener);
        profile_image_view_two.setOnLongClickListener(onLongClickListener);
        profile_image_view_three.setOnLongClickListener(onLongClickListener);
        profile_image_view_fourth.setOnLongClickListener(onLongClickListener);
        profile_image_view_fifth.setOnLongClickListener(onLongClickListener);


        istragram_image_list_view.setAdapter(gallaryImageAdapter);
        mApp = new InstagramApp(this, AppConstants.CLIENT_ID,
                AppConstants.CLIENT_SECRET, AppConstants.CALLBACK_URL);
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {
                // tvSummary.setText("Connected as " + mApp.getUserName());

                setInstrgramImageOnHorizontalView();

            }

            @Override
            public void onFail(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT)
                        .show();
            }
        });
        if (Validation.isStringNullOrBlank(instagramSession.getAccessToken())) {

            instraram_image_layout.setVisibility(View.GONE);
            instragram_tv.setVisibility(View.VISIBLE);
        } else {

            setInstrgramImageOnHorizontalView();
        }


        instragram_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApp.authorize();
            }
        });


        back_button = (ImageView) findViewById(R.id.back_button);
        back_button.setVisibility(View.INVISIBLE);

        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setLoginStep(getApplicationContext(), 2);
                Intent intent = new Intent(getApplicationContext(), AddMobileNumberActivity.class);
                startActivity(intent);

            }
        });

        if (!Validation.isStringNullOrBlank(SessionManager.getUserProfilePic(getApplicationContext())) && action == 1) {
            sessionManager.setLoginStep(getApplicationContext(), 2);
            Intent intent = new Intent(getApplicationContext(), AddMobileNumberActivity.class);
            startActivity(intent);
            finish();

            return;
            // Picasso.with(this).load(SessionManager.getClub_Logo(getApplicationContext())).placeholder(R.drawable.logo_profile).error(R.drawable.logo_profile).transform(new CircleTransform()).into(profile_pic_imageview);

        }
        if (serverImagepath != null)

        {
            //Toast.makeText(getApplicationContext() , "Sss "+imagepath.size() ,1).show();


            if (serverImagepath.size() <= 5) {
                for (int i = 0; i < serverImagepath.size(); i++) {
                    profileImageList.set(i, serverImagepath.get(i).getImage_thumb());
                }

            } else {
                for (int i = 0; i < 5; i++) {
                    profileImageList.set(i, serverImagepath.get(i).getImage_thumb());
                }
            }


        }

        if (Validation.isStringNullOrBlank(profileImageList.get(0)) == false) {
            Glide.with(getApplicationContext()).load(profileImageList.get(0)).placeholder(R.drawable.default_img_profile).error(R.drawable.default_img_profile).transform(new CircleBitmapTransformation(ProfileWizardActivity.this))
                    .into(profile_pic_imageview);
        }


        setImageProfileImageView();
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        continue_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Validation.isStringNullOrBlank(profileImageList.get(0))) {


                    AlertDialog alertDialog = new AlertDialog.Builder(
                            ProfileWizardActivity.this).create();

                    // Setting Dialog Title
                    alertDialog.setTitle(getResources().getString(R.string.app_name));

                    // Setting Dialog Message
                    alertDialog.setMessage("Please select profile picture");

                    // Setting Icon to Dialog


                    // Setting OK Button
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();


                    return;
                }


                final HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("user_email", SessionManager.getUser_email(getApplicationContext()));

                params.put("pic_del", deleteId);


                picId = "";

                if (Utill.isValidLink(profileImageList.get(0))) {
                    for (int i = 0; i < serverImagepath.size(); i++) {
                        if (serverImagepath.get(i).getImage_thumb().equals(profileImageList.get(0))) {
                            picId = serverImagepath.get(i).getImageid() + "";

                            break;
                        }
                    }

                }

                params.put("pic_id", picId);


                ArrayList<String> localImagePath = new ArrayList<String>();


                for (int i = 0; i < profileImageList.size(); i++) {

                    if (Validation.isStringNullOrBlank(profileImageList.get(i)) == false) {
                        if (Utill.isValidLink(profileImageList.get(i)) == false) {


                            localImagePath.add(profileImageList.get(i));


                        }


                    }


                }


                if (localImagePath.size() == 0) {
                    sendDataOnServer(params);
                } else {
                    ImageResizingListener imageResizingListener = new ImageResizingListener() {
                        @Override
                        public void onImageResize(String filePath) {

                        }

                        @Override
                        public void onImageResize(ArrayList<String> imagePathList) {

                            for (int i = 0; i < imagePathList.size(); i++) {
                                params.put("user_profilepic" + (i + 1), new File(imagePathList.get(i)));
                            }
                            sendDataOnServer(params);

                        }
                    };
                    ImageDecoder imageDecoder = new ImageDecoder(ProfileWizardActivity.this);
                    imageDecoder.imageResize(localImagePath, 1000, imageResizingListener);
                }


            }
        });


    }

    public void sendDataOnServer(HashMap<String, Object> params) {


        httpRequest.getResponse(ProfileWizardActivity.this, WebService.update_profile, params, new OnServerRespondingListener(ProfileWizardActivity.this) {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    //{"status":"true","message":"Profile Pic updated"}
                    Log.e("jsonObject", jsonObject + "");

                    if (jsonObject.getBoolean("status") == false) {
                        ShowUserMessage.showDialogOnActivity(ProfileWizardActivity.this, jsonObject.getString("message"));
                    } else {


                        if (action == 1) {
                            sessionManager.setLoginStep(getApplicationContext(), 3);
                            Intent intent = new Intent(getApplicationContext(), AddMobileNumberActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            ArrayList<UserPics> userPicsArrayList = new ArrayList<UserPics>();

                            Intent intent = new Intent();


                            try {
                                JSONArray user_pics_json_array = jsonObject.getJSONArray("user_pics");


                                for (int i = 0; i < user_pics_json_array.length(); i++) {
                                    JSONObject user_pics_json_array_item = user_pics_json_array.getJSONObject(i);
                                    UserPics userPics = new UserPics();

                                    userPics.setImage_thumb(user_pics_json_array_item.getString("thumb"));
                                    userPics.setImage_url(user_pics_json_array_item.getString("url"));
                                    userPics.setImageid(user_pics_json_array_item.getInt("id"));

                                    userPicsArrayList.add(userPics);


                                }


                            } catch (Exception e) {

                            }
                            intent.putExtra("userPicsArrayList", userPicsArrayList);

                            setResult(1010, intent);
                            showDialg(jsonObject.getString("message"), ProfileWizardActivity.this);

                        }


                    }


                } catch (Exception e) {

                }


            }
        });


    }

    public void pickImage(final int Gallery_Request_Code, final int Camera_Request_Code) {
        CharSequence[] items = {"Take from camera", "Upload from gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select pictures");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                if (position == 0) {
                    requestForCaptureCamera(Camera_Request_Code);
                    ;
                }


                if (position == 1) {

                    requestForOpenGallery(Gallery_Request_Code);
                }


                if (position == 3) {
                    dialog.cancel();
                }


            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public void requestForOpenGallery(final int Gallery_Request_Code)
    {
        UserPermision.updatePermission(ProfileWizardActivity.this, gallryPermissionArrayList, new MyPermissionGrrantedListner() {
            @Override
            public void isAllPermissionGranted(boolean isPermissionGranted, int code) {


                if (isPermissionGranted)
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, Gallery_Request_Code);
                }
                else
                {
                   requestForOpenGallery(Gallery_Request_Code);
                }

            }
        });

    }

    public void requestForCaptureCamera(final int Camera_Request_Code) {

        UserPermision.updatePermission(ProfileWizardActivity.this, cameraPermissionArrayList, new MyPermissionGrrantedListner() {
            @Override
            public void isAllPermissionGranted(boolean isPermissionGranted, int code) {


                 if (isPermissionGranted)
                 {
                     Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                     File sdCardDir = SDCardMemory.createSDCardDir(getApplicationContext());
                     File mFile = SDCardMemory.createImageSubDir(getApplicationContext(), sdCardDir);
                     File file = new File(mFile, "ClubsCaddy-img" + System.currentTimeMillis() + ".jpg");
                     absPath = file.getAbsolutePath();
                     sessionManager.setCameraPagePath(getApplicationContext(), absPath);
                     Uri photoURI = FileProvider.getUriForFile(ProfileWizardActivity.this,
                             getString(R.string.file_provider_authority),
                             file);
                     intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                     startActivityForResult(intent, Camera_Request_Code);
                 }
                 else
                 {
                     requestForCaptureCamera(Camera_Request_Code);
                 }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //  Toast.makeText(getApplicationContext() , "onActivityResult",1).show();
        if (resultCode == Activity.RESULT_OK) {
            try {
                // When an Image is picked

                if ((requestCode == Gallery_Request_Code_Image1 || requestCode == Gallery_Request_Code_Image2 || requestCode == Gallery_Request_Code_Image3 || requestCode == Gallery_Request_Code_Image4 || requestCode == Gallery_Request_Code_Image5) && resultCode == RESULT_OK
                        && null != data) {
                    // Get the Image from data

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();

                    // Set the Image in ImageView after decoding the String
                    File file = new File(imgDecodableString);


                    picturePath = imgDecodableString;


                } else {
                    if ((requestCode == Camera_Request_Code_Image1 || requestCode == Camera_Request_Code_Image2 || requestCode == Camera_Request_Code_Image3 || requestCode == Camera_Request_Code_Image4 || requestCode == Camera_Request_Code_Image5)) {


                        absPath = sessionManager.getCameraPagePath(getApplicationContext());

                        File finalFile = new File(absPath);


                        picturePath = finalFile.getAbsolutePath();


                    }
                }


            } catch (Exception e) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                        .show();
            }
//file1


            Bitmap bitmap;

            BitmapFactory.Options options = new BitmapFactory.Options();
            try {
                bitmap = BitmapFactory.decodeFile(picturePath);

            } catch (OutOfMemoryError e) {
                options.inSampleSize = 8;
                bitmap = BitmapFactory.decodeFile(picturePath, options);

            } catch (Exception e) {
                options.inSampleSize = 8;
                bitmap = BitmapFactory.decodeFile(picturePath, options);


            }


            if (bitmap == null) {
                ShowUserMessage.showUserMessage(ProfileWizardActivity.this, "Wrong path");
                return;
            }


            if (bitmap.getWidth() < 512 || bitmap.getWidth() < 512) {
                ShowUserMessage.showDialogForImageDia(ProfileWizardActivity.this);
                return;
            }

            if (Validation.isStringNullOrBlank(picturePath) == false) {
                Glide.with(getApplicationContext()).load(new File(picturePath)).transform(new CircleBitmapTransformation(ProfileWizardActivity.this)).into(profile_pic_imageview);
            }


            setPathProfileImageList(picturePath);


        }


    }

    public void setInstrgramImageOnHorizontalView() {

        String url = "https://api.instagram.com/v1/users/self/media/recent/?access_token=" + instagramSession.getAccessToken();
        HashMap<String, Object> param = new HashMap<>();
        httpRequest.getResponse(ProfileWizardActivity.this, url, new OnServerRespondingListener(ProfileWizardActivity.this) {
            @Override
            public void onSuccess(JSONObject jsonObject) {

                try {
                    JSONArray data = jsonObject.getJSONArray(TAG_DATA);
                    for (int data_i = 0; data_i < data.length(); data_i++) {
                        JSONObject data_obj = data.getJSONObject(data_i);

                        String type = data_obj.getString(TAG_TYPE);


                        if (type.equals("image")) {
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

                            imageThumbList.add(str_url);
                            imageStandredList.add(standerd_url);
                        } else {
                            JSONArray carousel_media_json_array = data_obj.getJSONArray("carousel_media");
                            for (int i = 0; i < carousel_media_json_array.length(); i++) {
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

                                imageThumbList.add(str_url);
                                imageStandredList.add(standerd_url);
                            }
                        }
                    }
                } catch (Exception e) {

                }

                gallaryImageAdapter.notifyDataSetChanged();
                Log.e("size", imageThumbList.size() + "");
                instraram_image_layout.setVisibility(View.VISIBLE);
                instragram_tv.setVisibility(View.GONE);


            }
        });
    }

    public void setPathProfileImageList(String imageList) {
        for (int i = 4; i > 0; i--) {
            // if (Validation.isStringNullOrBlank(profileImageList.get(i)) == false)
            {

                profileImageList.set(i, profileImageList.get(i - 1));

            }
        }

        profileImageList.set(0, imageList);

        setImageProfileImageView();


    }

    public void setPathProfileImageList(String imageList, int pos) {
        for (int i = pos; i > 0; i--) {
            // if (Validation.isStringNullOrBlank(profileImageList.get(i)) == false)
            {

                profileImageList.set(i, profileImageList.get(i - 1));

            }
        }

        profileImageList.set(0, imageList);

        setImageProfileImageView();


    }

    public void removePathProfileImageList(int pos) {

        String thumbImage = profileImageList.get(pos);


        if (Utill.isValidLink(thumbImage)) {

            for (int j = 0; j < serverImagepath.size(); j++) {
                if (thumbImage.equals(serverImagepath.get(j).getImage_thumb())) {
                    if (Validation.isStringNullOrBlank(deleteId)) {
                        deleteId = serverImagepath.get(j).getImageid() + "";
                    } else {
                        deleteId = deleteId + "," + serverImagepath.get(j).getImageid();
                    }
                }
            }

        }


        profileImageList.set(pos, "");

        for (int i = 1; i < profileImageList.size(); i++) {
            if (Validation.isStringNullOrBlank(profileImageList.get(i)) == false) {

                if (Validation.isStringNullOrBlank(profileImageList.get(i - 1))) {

                    Collections.swap(profileImageList, i - 1, i);
                }
            }
        }

        setImageProfileImageView();
    }

    public void setImageProfileImageView() {
        /*profile_image_view_one.setPadding(0,0,0,0);
        profile_image_view_two.setPadding(0,0,0,0);
        profile_image_view_three.setPadding(0,0,0,0);
        profile_image_view_fourth.setPadding(0,0,0,0);
        profile_image_view_fifth.setPadding(0,0,0,0);*/
        for (int i = 0; i < profileImageList.size(); i++) {
            if (i == 0) {
                if (Validation.isStringNullOrBlank(profileImageList.get(i))) {
                    delete_profile_image_btn_one.setVisibility(View.INVISIBLE);
                    add_profile_image_btn_one.setVisibility(View.VISIBLE);
                    profile_image_view_one.setImageResource(R.drawable.profile_wized_image_back);

                } else {

                    if (Validation.isStringNullOrBlank(profileImageList.get(1))) {
                        delete_profile_image_btn_one.setVisibility(View.INVISIBLE);
                    } else {
                        delete_profile_image_btn_one.setVisibility(View.VISIBLE);
                    }

                    /*if (picturePath.equals(profileImageList.get(0)))
                    {
                        profile_image_view_one.setPadding(2,2,2,2);
                        profile_image_view_one.setBackground(getResources().getDrawable(R.drawable.profile_wizard_image_back_border));
                    }*/


                    add_profile_image_btn_one.setVisibility(View.INVISIBLE);

                    if (Utill.isValidLink(profileImageList.get(i))) {
                        Glide.with(getApplicationContext()).load(profileImageList.get(i)).transform(new RoundedCornerBitmapTranslation(ProfileWizardActivity.this, 20, 0)).into(profile_image_view_one);

                        if (Validation.isStringNullOrBlank(profileImageList.get(i)) == false)
                            Glide.with(getApplicationContext()).load(profileImageList.get(i)).transform(new CircleBitmapTransformation(ProfileWizardActivity.this)).into(profile_pic_imageview);
                    } else {
                        Glide.with(getApplicationContext()).load(new File(profileImageList.get(i))).transform(new RoundedCornerBitmapTranslation(ProfileWizardActivity.this, 20, 0)).into(profile_image_view_one);

                        if (Validation.isStringNullOrBlank(profileImageList.get(i)) == false)
                            Glide.with(getApplicationContext()).load(new File(profileImageList.get(i))).transform(new CircleBitmapTransformation(ProfileWizardActivity.this)).into(profile_pic_imageview);

                    }

                }
            }


            if (i == 1) {
                if (Validation.isStringNullOrBlank(profileImageList.get(i))) {
                    delete_profile_image_btn_two.setVisibility(View.INVISIBLE);
                    add_profile_image_btn_two.setVisibility(View.VISIBLE);
                    profile_image_view_two.setImageResource(R.drawable.profile_wized_image_back);

                } else {
                    delete_profile_image_btn_two.setVisibility(View.VISIBLE);
                    add_profile_image_btn_two.setVisibility(View.INVISIBLE);


                    if (Utill.isValidLink(profileImageList.get(i))) {
                        Glide.with(getApplicationContext()).load(profileImageList.get(i)).transform(new RoundedCornerBitmapTranslation(ProfileWizardActivity.this, 20, 0)).into(profile_image_view_two);

                    } else {
                        Glide.with(getApplicationContext()).load(new File(profileImageList.get(i))).transform(new RoundedCornerBitmapTranslation(ProfileWizardActivity.this, 20, 0)).into(profile_image_view_two);

                    }


                }
            }


            if (i == 2) {
                if (Validation.isStringNullOrBlank(profileImageList.get(i))) {
                    delete_profile_image_btn_third.setVisibility(View.INVISIBLE);
                    add_profile_image_btn_three.setVisibility(View.VISIBLE);
                    profile_image_view_three.setImageResource(R.drawable.profile_wized_image_back);

                } else {
                    delete_profile_image_btn_third.setVisibility(View.VISIBLE);
                    add_profile_image_btn_three.setVisibility(View.INVISIBLE);


                    if (Utill.isValidLink(profileImageList.get(i))) {
                        Glide.with(getApplicationContext()).load(profileImageList.get(i)).transform(new RoundedCornerBitmapTranslation(ProfileWizardActivity.this, 20, 0)).into(profile_image_view_three);

                    } else {
                        Glide.with(getApplicationContext()).load(new File(profileImageList.get(i))).transform(new RoundedCornerBitmapTranslation(ProfileWizardActivity.this, 20, 0)).into(profile_image_view_three);

                    }

                }
            }


            if (i == 3) {
                if (Validation.isStringNullOrBlank(profileImageList.get(i))) {
                    delete_profile_image_btn_fourth.setVisibility(View.INVISIBLE);
                    add_profile_image_btn_fourth.setVisibility(View.VISIBLE);
                    profile_image_view_fourth.setImageResource(R.drawable.profile_wized_image_back);

                } else {
                    /*if (picturePath.equals(profileImageList.get(3)))
                    {
                        profile_image_view_fourth.setPadding(2,2,2,2);
                        profile_image_view_fourth.setBackground(getResources().getDrawable(R.drawable.profile_wizard_image_back_border));
                    }*/
                    delete_profile_image_btn_fourth.setVisibility(View.VISIBLE);
                    add_profile_image_btn_fourth.setVisibility(View.INVISIBLE);


                    if (Utill.isValidLink(profileImageList.get(i))) {
                        Glide.with(getApplicationContext()).load(profileImageList.get(i)).transform(new RoundedCornerBitmapTranslation(ProfileWizardActivity.this, 20, 0)).into(profile_image_view_fourth);

                    } else {
                        Glide.with(getApplicationContext()).load(new File(profileImageList.get(i))).transform(new RoundedCornerBitmapTranslation(ProfileWizardActivity.this, 20, 0)).into(profile_image_view_fourth);

                    }
                }
            }


            if (i == 4) {
                if (Validation.isStringNullOrBlank(profileImageList.get(i))) {
                    delete_profile_image_btn_fifth.setVisibility(View.INVISIBLE);
                    add_profile_image_btn_fifth.setVisibility(View.VISIBLE);
                    profile_image_view_fifth.setImageResource(R.drawable.profile_wized_image_back);

                } else {
                    delete_profile_image_btn_fifth.setVisibility(View.VISIBLE);
                    add_profile_image_btn_fifth.setVisibility(View.INVISIBLE);
                  /*  if (picturePath.equals(profileImageList.get(4)))
                    {
                        profile_image_view_fifth.setPadding(2,2,2,2);
                        profile_image_view_fifth.setBackground(getResources().getDrawable(R.drawable.profile_wizard_image_back_border));
                    }*/
                    //  Picasso.with(getApplicationContext()).load(new File(profileImageList.get(i))).transform(new RoundedTransformation(20 ,0)).into(profile_image_view_fifth);


                    if (Utill.isValidLink(profileImageList.get(i))) {
                        Glide.with(getApplicationContext()).load(profileImageList.get(i)).transform(new RoundedCornerBitmapTranslation(ProfileWizardActivity.this, 20, 0)).into(profile_image_view_fifth);

                    } else {
                        Glide.with(getApplicationContext()).load(new File(profileImageList.get(i))).transform(new RoundedCornerBitmapTranslation(ProfileWizardActivity.this, 20, 0)).into(profile_image_view_fifth);

                    }

                }
            }


        }

    }

    public void showDialg(String msg, Activity mContext) {

        final AlertDialog alertDialog = new AlertDialog.Builder(
                mContext).create();

// Setting Dialog Title
        alertDialog.setTitle(SessionManager.getClubName(mContext));

// Setting Dialog Message
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);

// Setting Icon to Dialog


// Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                alertDialog.dismiss();
                finish();
            }
        });

// Showing Alert Message
        alertDialog.show();
    }

}
