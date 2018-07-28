package com.clubscaddy.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clubscaddy.Adapter.GalleryItemAdapter;
import com.clubscaddy.AsyTask.ConvertImagePathInBitmapStringTask;
import com.clubscaddy.CustomPhotoGalleryActivity;
import com.clubscaddy.Interface.MyPermissionGrrantedListner;
import com.clubscaddy.ProfileWizardActivity;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.custumview.SDCardMemory;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.UserPermision;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.ImageToStringConveterListener;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.R;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.custumview.CircularProgressBar;
import com.clubscaddy.custumview.CustomScrollView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * implements
 */

public class AddClassifiedFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final int CAMERA_REQUEST = 11;
    private static final int SELECT_FILE = 12;
    private static final int CAMERA_REQUEST_PROFILE_IMAGE = 13;
    private static final int SELECT_FILE_PROFILE_IMAGE = 14;
    private static final int PLACE_PICKER_REQUEST = 1;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10;
    //implements ,  , AdapterView.OnItemClickListener
    EditText classfieb_title_tv;
    EditText classified_cost_tv;
    EditText classified_description_tv;
    TextView discription_textview_status;
    Button add_attachment;
    Gallery image_gallery;
    //EditText choose_address_tv;
    Button adminList_addAdmin;
    View convertView;
    Context mContext;
    String absPath;
    //ImageButton select_btn;
    // ImageView profile_image_view;
    ArrayList<String> imageList;
    Bitmap image, imageBitmap;
    ClassifiedFragment.EditClassifedListener editClassifedListener;
    GalleryItemAdapter galleryItemAdapter;
    CustomScrollView scrollView;
    ConvertImagePathInBitmapStringTask convertImagePathInBitmapStringTask;
    HttpRequest httpRequest;
    TextView price_tv;
    Button add_classified_btn;
    CircularProgressBar circularProgressbar;
    int user_public_email, user_public_phone, member_allow_status;
    SessionManager sessionManager;
    double lattitude, longitude;
    HashMap<String, Object> params = new HashMap<String, Object>();
    CheckBox show_profile_cb;
    CheckBox manual_fill_cb;
    TextView email_tv, phone_tv;
    EditText email_edit_tv, mobile_edit_tv;
    ArrayList<String> gallryPermissionArrayList ;
    ArrayList<String> cameraPermissionArrayList ;
    public View.OnClickListener checkBoxClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.show_profile_cb) {
                show_profile_cb.setChecked(true);
                manual_fill_cb.setChecked(false);

                email_tv.setVisibility(View.GONE);
                phone_tv.setVisibility(View.GONE);
                email_edit_tv.setVisibility(View.GONE);
                mobile_edit_tv.setVisibility(View.GONE);


            }

            if (v.getId() == R.id.manual_fill_cb) {
                show_profile_cb.setChecked(false);
                manual_fill_cb.setChecked(true);
                email_tv.setVisibility(View.VISIBLE);
                phone_tv.setVisibility(View.VISIBLE);
                email_edit_tv.setVisibility(View.VISIBLE);
                mobile_edit_tv.setVisibility(View.VISIBLE);


            }


        }
    };
    TextView cancelTV, okTV;
    private GoogleApiClient mGoogleApiClient;
    private Location mStartLocation;

    public void setInstanse(int user_public_email, int user_public_phone, int member_allow_status, ClassifiedFragment.EditClassifedListener editClassifedListener) {
        this.user_public_email = user_public_email;
        this.user_public_phone = user_public_phone;
        this.member_allow_status = member_allow_status;
        this.editClassifedListener = editClassifedListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.add_classified, null);
        classfieb_title_tv = (EditText) convertView.findViewById(R.id.classfieb_title_tv);
        classified_cost_tv = (EditText) convertView.findViewById(R.id.classified_cost_tv);
        classified_description_tv = (EditText) convertView.findViewById(R.id.classified_description_tv);
        add_attachment = (Button) convertView.findViewById(R.id.add_attachment);
        adminList_addAdmin = (Button) convertView.findViewById(R.id.adminList_addAdmin);
        image_gallery = (Gallery) convertView.findViewById(R.id.image_gallery);
        scrollView = (CustomScrollView) convertView.findViewById(R.id.scrollView);
        add_attachment.setOnClickListener(this);
        discription_textview_status = (TextView) convertView.findViewById(R.id.discription_textview_status);

        add_classified_btn = (Button) convertView.findViewById(R.id.add_classified_btn);
        DirectorFragmentManageActivity.logoutButton.setVisibility(View.GONE);

        //select_btn = (ImageButton) convertView.findViewById(R.id.select_btn);
        sessionManager = new SessionManager(getActivity());

        price_tv = (TextView) convertView.findViewById(R.id.price_tv);
        price_tv.setText("Price(" + sessionManager.getCurrencyCode(getActivity()) + ")");


        circularProgressbar = (CircularProgressBar) convertView.findViewById(R.id.circularProgressbar);
        DirectorFragmentManageActivity.updateTitle("Add Classified");
        image_gallery.setOnItemClickListener(this);
        // select_btn.setOnClickListener(this);
        add_classified_btn.setOnClickListener(this);
        imageList = new ArrayList<>();
        mContext = getActivity();
        httpRequest = new HttpRequest(getActivity());
        galleryItemAdapter = new GalleryItemAdapter(getActivity(), imageList);
        image_gallery.setAdapter(galleryItemAdapter);


        classified_description_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                discription_textview_status.setText(s.toString().length() + "/1000");
            }
        });


        classified_description_tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.e("action", event.getAction() + "");


                //	Toast.makeText(getActivity() ,"action  " + event.getAction(),Toast.LENGTH_SHORT ).show();


                if (1 == event.getAction()) {
                    scrollView.setEnableScrolling(true);

                } else {

                    scrollView.setEnableScrolling(false);


                }

                return false;
            }
        });


        gallryPermissionArrayList = new ArrayList<>();
        cameraPermissionArrayList = new ArrayList<>();
        gallryPermissionArrayList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        gallryPermissionArrayList.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        cameraPermissionArrayList.add(Manifest.permission.CAMERA);
        cameraPermissionArrayList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        cameraPermissionArrayList.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        return convertView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_attachment) {
            pickImage(2);
        }

       /* if (v.getId() == R.id.select_btn)
        {
            pickImage(1);
        }

        if (v.getId() == R.id.choose_address_tv)
        {
           openPlacePicker();
        }
*/

        if (v.getId() == R.id.add_classified_btn) {
            checkValidation();
        }

    }

    private void pickImage(final int type) {
        final CharSequence[] items = {"Take from camera", "Upload from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select pictures");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {
                try {
                    if (items[position].equals("Take from camera")) {

                        openCameraByIntent(type);

                    } else if (items[position].equals("Upload from gallery")) {


                        if (type == 1)
                        {
                            openMobileGallery();
                        } else {
                            openCustomGallery();
                        }


                    } else {
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    ShowUserMessage.showUserMessage(mContext, e.toString());
                }
            }
        });
        builder.setCancelable(false);
        builder.show();


    }
    private void openCameraByIntent(final int type)
    {
        if (imageList.size() < 10)
        {

            UserPermision.updatePermission(getActivity(), cameraPermissionArrayList, new MyPermissionGrrantedListner() {
                @Override
                public void isAllPermissionGranted(boolean isPermissionGranted, int code) {

                    if (isPermissionGranted)
                    {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File sdCardDir = SDCardMemory.createSDCardDir(mContext);
                        File mFile = SDCardMemory.createImageSubDir(mContext, sdCardDir);
                        File file = new File(mFile, "ClubsCaddy-img" + System.currentTimeMillis() + ".jpg");
                        absPath = file.getAbsolutePath();
                        Uri photoURI = FileProvider
                                .getUriForFile(getActivity(),
                                getString(R.string.file_provider_authority),
                                file);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                        savePref(absPath);
                        if (type == 1) {
                            getActivity().startActivityForResult(intent, CAMERA_REQUEST_PROFILE_IMAGE);
                        } else {
                            getActivity().startActivityForResult(intent, CAMERA_REQUEST);
                        }
                    }
                    else
                    {
                        openCameraByIntent(type);
                    }
                }
            });


        } else {
            Utill.showDialg("You can't upload more then 10 images", getActivity());
        }

    }

    private void openCustomGallery() {
        try {

            Intent intent = new Intent(getActivity(), CustomPhotoGalleryActivity.class);
            intent.putExtra("item", imageList.size() + "");
            getActivity().startActivityForResult(intent, SELECT_FILE);
        } catch (Exception e) {
            // new SendErrorAsync(mContext, e).execute();
        }
    }

    private void openMobileGallery() {

        UserPermision.updatePermission(getActivity(), gallryPermissionArrayList, new MyPermissionGrrantedListner() {
            @Override
            public void isAllPermissionGranted(boolean isPermissionGranted, int code) {

                if (isPermissionGranted)
                {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, SELECT_FILE_PROFILE_IMAGE);
                }
                else
                {
                  openMobileGallery();
                }


            }
        });



    }

    private void savePref(String str) {
        SharedPreferences sf = mContext.getSharedPreferences("LoginUser",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString("imageURI", str);
        editor.commit();
    }

    private String getImageURI() {
        SharedPreferences sf = mContext.getSharedPreferences("LoginUser",
                Context.MODE_PRIVATE);
        String uri = sf.getString("imageURI", null);
        return uri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode == SELECT_FILE && resultCode == getActivity().RESULT_OK) {
                super.onActivityResult(requestCode, resultCode, data);

                {
                    if (data != null) {
                        String uri = data.getStringExtra("uri");
                        String absUri[] = uri.split("\\|");
                        String s = data.getStringExtra("data");
                        String abs[] = s.split("\\|");
                        int totalUnselectedItem = 0;

                        for (int i = 0; i < abs.length; i++) {
                            String str = "";
                            str = abs[i];
                            String imageUri = abs[i];
                            imageUri = imageUri.replaceAll("%20", " ");


                            Bitmap bitmap;

                            BitmapFactory.Options options = new BitmapFactory.Options();
                            try {
                                bitmap = BitmapFactory.decodeFile(imageUri);

                            } catch (OutOfMemoryError e) {
                                options.inSampleSize = 8;
                                bitmap = BitmapFactory.decodeFile(imageUri, options);

                            } catch (Exception e) {
                                options.inSampleSize = 8;
                                bitmap = BitmapFactory.decodeFile(imageUri, options);


                            }


                            if (bitmap.getWidth() < 512 || bitmap.getHeight() < 512) {
                                totalUnselectedItem++;
                            } else {
                                imageList.add(imageUri);

                            }

                        }
                        if (totalUnselectedItem != 0) {
                            Utill.showDialg(totalUnselectedItem + " images are in wrong format Please upload correct format (Greater than 512*512)", getActivity());
                        }

                        galleryItemAdapter.notifyDataSetChanged();
                    }
                }

            }
            if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK) {
                super.onActivityResult(requestCode, resultCode, data);
                try {
                    String URI = getImageURI();
                    try {
                        File file = new File(URI);


                        Bitmap bitmap = BitmapFactory.decodeFile(URI);


                        if (bitmap.getWidth() < 512 || bitmap.getHeight() < 512) {
                            Utill.showDialg("Images are in wrong format Please upload correct format (Greater than 512*512)", getActivity());
                        } else {
                            imageList.add(file.getAbsolutePath());
                            galleryItemAdapter.notifyDataSetChanged();

                        }


                    } catch (Exception e) {
                        ShowUserMessage.showUserMessage(mContext, e.toString());
                    }
                } catch (Exception e) {
                    ShowUserMessage.showUserMessage(mContext, e.toString());
                }

            }


            if (requestCode == CAMERA_REQUEST_PROFILE_IMAGE && resultCode == getActivity().RESULT_OK) {
                super.onActivityResult(requestCode, resultCode, data);


            }


        } catch (Exception e) {
            ShowUserMessage.showUserMessage(mContext, e.toString());
        }


    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
        //showDeleteConfirmationDialogu(arg2);

        Intent intent = new Intent(getActivity(), EventFullImageViewActivity.class);
        intent.putExtra("path_list", imageList);
        intent.putExtra("pos", String.valueOf(pos));
        startActivity(intent);
    }

    public void checkValidation() {
        if (Validation.isStringNullOrBlank(classfieb_title_tv.getText().toString()) == true) {
            ShowUserMessage.showUserMessage(getActivity(), "Please enter title");
            return;
        }
        if (Validation.isStringNullOrBlank(classified_cost_tv.getText().toString()) == true) {
            ShowUserMessage.showUserMessage(getActivity(), "Please enter price");
            return;
        }


        if (Validation.isStringNullOrBlank(classified_description_tv.getText().toString()) == true) {
            ShowUserMessage.showUserMessage(getActivity(), "Please enter description");
            return;
        }


        if (imageList.size() == 0) {
            ShowUserMessage.showUserMessage(getActivity(), "Please select atleast one image");
            return;
        }

        if (user_public_email == 2 && user_public_phone == 2) {
            showDialogForContactDetail();
        } else {
            sendRequestOnServer(2);
        }

    }

    public void sendRequestOnServer(int user_profile_status) {


        params.put("classifieds_uid", SessionManager.getUserId(getActivity()));
        params.put("classifieds_title", classfieb_title_tv.getText().toString());

        if (user_profile_status == 2 && (user_public_email == 2 && user_public_phone == 2)) {
            if (manual_fill_cb.isChecked()) {
                String discription = "";
                if (user_public_email == 2) {
                    if (Validation.isStringNullOrBlank(email_edit_tv.getText().toString()) == false) {
                        discription = "\nEmail id" + " - " + email_edit_tv.getText().toString();
                    }
                }
                if (user_public_phone == 2) {
                    if (Validation.isStringNullOrBlank(mobile_edit_tv.getText().toString()) == false) {
                        discription = discription + " \n" + "Contact no" + " - " + mobile_edit_tv.getText().toString();
                    }


                }


                params.put("classifieds_user_contact", discription);
            }


        }


        params.put("classifieds_desc", classified_description_tv.getText().toString());

        params.put("classifieds_cost", classified_cost_tv.getText().toString());
        params.put("classifieds_club_id", SessionManager.getUser_Club_id(getActivity()));
        params.put("user_profile_status", user_profile_status);


        // params.put("classifieds_lat" , lattitude+"");
        //params.put("classifieds_long" , longitude+"");
        //params.put("classifieds_profile" , new File(picturePath));


        circularProgressbar.setVisibility(View.VISIBLE);
        convertImagePathInBitmapStringTask = new ConvertImagePathInBitmapStringTask(getActivity(), imageList, new ImageToStringConveterListener() {
            @Override
            public void onSuccess(boolean status, ArrayList<String> bitmapString) {
                circularProgressbar.setVisibility(View.GONE);
            }

            @Override
            public void onUpdate(int update) {
                circularProgressbar.setProgress(update);
            }

            @Override
            public void onError() {

            }

            @Override
            public void onSuccess(boolean status, JSONArray imageOutPutJsonArray) {
                circularProgressbar.setVisibility(View.GONE);
                params.put("classified_other_pics", imageOutPutJsonArray);

                Log.e("params", params.toString());
                httpRequest.getResponse(getActivity(), WebService.create_classified, params, new OnServerRespondingListener(getActivity()) {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        try {
                            if (jsonObject.getBoolean("status")) {
                                showMessageForFragmeneWithBack(getActivity(), jsonObject.getString("message"));
                            } else {
                                ShowUserMessage.showUserMessage(getActivity(), jsonObject.getString("message"));
                            }
                        } catch (Exception e) {

                        }
                    }
                });
            }
        });

        convertImagePathInBitmapStringTask.execute();

    }

    public void showMessageForFragmeneWithBack(final FragmentActivity fragmentActivity, String msg) {
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
                editClassifedListener.onSucess();
                dialog.cancel();
            }
        });


        alertDialog.show();
    }

    public void showDialogForContactDetail() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.contact_detail_classification_prodoct);
        dialog.getWindow().setLayout(AppConstants.getDeviceWidth(getActivity()), LinearLayout.LayoutParams.WRAP_CONTENT);
        show_profile_cb = (CheckBox) dialog.findViewById(R.id.show_profile_cb);
        manual_fill_cb = (CheckBox) dialog.findViewById(R.id.manual_fill_cb);
        email_tv = (TextView) dialog.findViewById(R.id.email_tv);
        phone_tv = (TextView) dialog.findViewById(R.id.phone_tv);
        email_edit_tv = (EditText) dialog.findViewById(R.id.email_edit_tv);
        mobile_edit_tv = (EditText) dialog.findViewById(R.id.mobile_edit_tv);
        cancelTV = (TextView) dialog.findViewById(R.id.cancel);
        okTV = (TextView) dialog.findViewById(R.id.ok);
        show_profile_cb.setOnClickListener(checkBoxClickListener);
        manual_fill_cb.setOnClickListener(checkBoxClickListener);
        show_profile_cb.setChecked(true);
        manual_fill_cb.setChecked(false);

        email_tv.setVisibility(View.GONE);
        email_edit_tv.setVisibility(View.GONE);
        phone_tv.setVisibility(View.GONE);
        mobile_edit_tv.setVisibility(View.GONE);
        /* */


        okTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utill.hideKeybord(getActivity(), email_edit_tv);
                Utill.hideKeybord(getActivity(), mobile_edit_tv);
                if (manual_fill_cb.isChecked()) {

                    if (Validation.isStringNullOrBlank(email_edit_tv.getText().toString()) && Validation.isStringNullOrBlank(mobile_edit_tv.getText().toString())) {
                        ShowUserMessage.showUserMessage(getActivity(), "Please enter email address or mobile no.");
                        return;
                    }

                    if (Validation.isStringNullOrBlank(email_edit_tv.getText().toString()) == false) {
                        if (Validation.isEmailAddress(email_edit_tv, true) == false) {
                            ShowUserMessage.showUserMessage(getActivity(), "Please enter valid email address.");
                            return;
                        }
                    }


                    if (Validation.isStringNullOrBlank(mobile_edit_tv.getText().toString()) == false) {
                        if (Validation.isValidPhoneNumber(mobile_edit_tv.getText().toString()) == false) {
                            ShowUserMessage.showUserMessage(getActivity(), "Please enter valid mobile no.");
                            return;
                        }
                    }


                    dialog.dismiss();
                    sendRequestOnServer(2);
                }
                if (show_profile_cb.isChecked()) {
                    dialog.dismiss();
                    sendRequestOnServer(1);
                }

            }
        });

        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utill.hideKeybord(getActivity(), email_edit_tv);
                Utill.hideKeybord(getActivity(), mobile_edit_tv);

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DirectorFragmentManageActivity.updateTitle("Classified List");
        DirectorFragmentManageActivity.logoutButton.setVisibility(View.VISIBLE);


    }


}
