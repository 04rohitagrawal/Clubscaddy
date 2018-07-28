package com.clubscaddy.fragment;
//EditClassifiedProductDetail

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.TextView;

import com.clubscaddy.Adapter.ClassfiedGalleryItemAdapter;
import com.clubscaddy.AsyTask.ConvertImagePathInBitmapStringTask;
import com.clubscaddy.Bean.ClassifiedItem;
import com.clubscaddy.Bean.ClassifiedItemOtherPicture;
import com.clubscaddy.CustomPhotoGalleryActivity;
import com.clubscaddy.Interface.MyPermissionGrrantedListner;
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
 * Created by administrator on 12/5/17.
 */

public class EditClassifiedProductDetail extends Fragment implements   View.OnClickListener , AdapterView.OnItemClickListener
{
    //implements ,  , AdapterView.OnItemClickListener
    EditText classfieb_title_tv;
    EditText classified_cost_tv;
    EditText classified_description_tv ;
    TextView discription_textview_status;
    Button add_attachment;
    Gallery image_gallery;
    //EditText choose_address_tv;
    Button adminList_addAdmin;
    private static final int CAMERA_REQUEST = 11;
    private static final int SELECT_FILE = 12;

    private static final int CAMERA_REQUEST_PROFILE_IMAGE = 13;
    private static final int SELECT_FILE_PROFILE_IMAGE = 14;
    String picturePath ="";
    View convertView ;
    Activity mContext;
    String absPath;
    ArrayList<String> imageList;
    Bitmap image,imageBitmap;

    ClassfiedGalleryItemAdapter galleryItemAdapter ;
    CustomScrollView scrollView;
    //ImageButton select_btn;


    ConvertImagePathInBitmapStringTask convertImagePathInBitmapStringTask ;
    HttpRequest httpRequest ;

    ;


    Button add_classified_btn;
    ArrayList<ClassifiedItemOtherPicture>classifiedItemOtherPictures ;

    private static final int PLACE_PICKER_REQUEST = 1;

    private GoogleApiClient mGoogleApiClient;
    private Location mStartLocation;
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    CircularProgressBar circularProgressbar;

    ClassifiedItem classifiedItem  ;
    ClassifiedFragment.EditClassifedListener editClassifedListener ;

    public void setInstanse(ClassifiedItem classifiedItem ,ClassifiedFragment.EditClassifedListener editClassifedListener )
    {
        this.classifiedItem = classifiedItem ;

        this.editClassifedListener = editClassifedListener ;
    }
    SessionManager sessionManager ;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.add_classified ,null);
        classfieb_title_tv = (EditText) convertView.findViewById(R.id.classfieb_title_tv);
        classified_cost_tv = (EditText) convertView.findViewById(R.id.classified_cost_tv);
        classified_description_tv = (EditText) convertView.findViewById(R.id.classified_description_tv);
        add_attachment = (Button) convertView.findViewById(R.id.add_attachment);
        adminList_addAdmin = (Button) convertView.findViewById(R.id.adminList_addAdmin);
        image_gallery = (Gallery) convertView.findViewById(R.id.image_gallery);
        scrollView = (CustomScrollView) convertView.findViewById(R.id.scrollView);
        add_attachment.setOnClickListener(this);
        DirectorFragmentManageActivity.updateTitle("Edit Classified");
        DirectorFragmentManageActivity.logoutButton.setVisibility(View.GONE);

        discription_textview_status = (TextView) convertView.findViewById(R.id.discription_textview_status);
       // choose_address_tv = (EditText) convertView.findViewById(R.id.choose_address_tv);
        add_classified_btn = (Button) convertView.findViewById(R.id.add_classified_btn);
       // choose_address_tv.setOnClickListener(this);
       // select_btn = (ImageButton) convertView.findViewById(R.id.select_btn);
        sessionManager = new SessionManager(getActivity());

        TextView  price_tv = (TextView) convertView.findViewById(R.id.price_tv);
        price_tv.setText("Price("+sessionManager.getCurrencyCode(getActivity())+")");

        circularProgressbar = (CircularProgressBar) convertView.findViewById(R.id.circularProgressbar);

        image_gallery.setOnItemClickListener(this);
       // select_btn.setOnClickListener(this);
        add_classified_btn.setOnClickListener(this);
        imageList = new ArrayList<>();
        mContext = getActivity();
        httpRequest = new HttpRequest(getActivity());
        deleteImageList  = new ArrayList<>();



        classifiedItemOtherPictures = new ArrayList<ClassifiedItemOtherPicture>();

        imageList = new ArrayList<>();




        classified_description_tv.addTextChangedListener(new TextWatcher() {
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


        setDataOnView();

        return convertView;
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
    public void onClick(View v)
    {
        if (v.getId() == R.id.add_attachment)
        {
            openAlertDialogForSelectMedia();
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

        if (v.getId() == R.id.add_classified_btn)
        {
            sendRequestOnServer();
        }

    }

    ArrayList<String> cameraPermissionArrayList ;
    private void openAlertDialogForSelectMedia() {
        cameraPermissionArrayList = new ArrayList<>();
        cameraPermissionArrayList.add(Manifest.permission.CAMERA);
        cameraPermissionArrayList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        cameraPermissionArrayList.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        final CharSequence[] items = {"Take from camera", "Upload from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select pictures");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int position) {
                try {
                    if (items[position].equals("Take from camera")) {



                        if (imageList.size() < 10)
                        {
                            openCameraByIntent();

                           }
                            else
                            {
                            Utill.showDialg("You can't upload more then 10 images", getActivity());
                            }


                    }
                    else if (items[position].equals("Upload from gallery"))
                    {
                        openGallery();

                    }
                    else
                    {
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

    public void openCameraByIntent()
    {
        UserPermision.updatePermission(mContext, cameraPermissionArrayList, new MyPermissionGrrantedListner() {
            @Override
            public void isAllPermissionGranted(boolean isPermissionGranted, int code) {

                if (isPermissionGranted)
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File sdCardDir = SDCardMemory.createSDCardDir(mContext);
                    File mFile = SDCardMemory.createImageSubDir(mContext, sdCardDir);
                    File file = new File(mFile, "tennis-img" + System.currentTimeMillis() + ".jpg");
                    absPath = file.getAbsolutePath();
                    Uri photoURI = FileProvider.getUriForFile(mContext,
                            getString(R.string.file_provider_authority),
                            file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    savePref(absPath);
                    getActivity().startActivityForResult(intent, CAMERA_REQUEST);
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
                        int totalUnselectedItem = 0 ;

                        for (int i = 0; i < abs.length; i++) {
                            String str = "";
                            str = abs[i];
                            String imageUri = abs[i];
                            imageUri = imageUri.replaceAll("%20", " ");



                            Bitmap bitmap ;

                            BitmapFactory.Options options = new BitmapFactory.Options();
                            try
                            {
                                bitmap = BitmapFactory.decodeFile(imageUri );

                            }
                            catch (OutOfMemoryError e)
                            {
                                options.inSampleSize = 8;
                                bitmap = BitmapFactory.decodeFile(imageUri ,options);

                            }
                            catch (Exception e)
                            {
                                options.inSampleSize = 8;
                                bitmap = BitmapFactory.decodeFile(imageUri ,options);


                            }


                            if (bitmap.getWidth()<512 || bitmap.getHeight() < 512)
                            {
                                totalUnselectedItem++ ;
                            }
                            else
                            {
                                imageList.add(imageUri);
                                ClassifiedItemOtherPicture classifiedItemOtherPicture = new ClassifiedItemOtherPicture();
                                classifiedItemOtherPicture.setId(-1);
                                classifiedItemOtherPicture.setUrl(imageUri);
                                classifiedItemOtherPicture.setThumb(imageUri);
                                //getClassified_other_pics
                                classifiedItemOtherPictures.add(classifiedItemOtherPicture);

                            }




                        }
                        if (totalUnselectedItem != 0)
                        {
                            Utill.showDialg(totalUnselectedItem+" images are in wrong format Please upload correct format (Greater than 512*512)" , getActivity());
                        }
                        galleryItemAdapter.notifyDataSetChanged();
                    }
                }

            }



            if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK) {
                super.onActivityResult(requestCode, resultCode, data);
                try {
                    String imageUri = getImageURI();
                    try {


                        Bitmap bitmap = BitmapFactory.decodeFile(imageUri);


                        if (bitmap.getWidth()<512 || bitmap.getHeight() < 512)
                        {
                            Utill.showDialg("Images are in wrong format Please upload correct format (Greater than 512*512)" , getActivity());
                        }
                        else
                        {
                            imageList.add(imageUri);

                            ClassifiedItemOtherPicture classifiedItemOtherPicture = new ClassifiedItemOtherPicture();
                            classifiedItemOtherPicture.setId(-1);
                            classifiedItemOtherPicture.setUrl(imageUri);
                            classifiedItemOtherPicture.setThumb(imageUri);
                            //getClassified_other_pics
                            classifiedItemOtherPictures.add(classifiedItemOtherPicture);
                            galleryItemAdapter.notifyDataSetChanged();

                        }




                    } catch (Exception e) {
                        ShowUserMessage.showUserMessage(mContext, e.toString());
                    }
                } catch (Exception e) {
                    ShowUserMessage.showUserMessage(mContext, e.toString());
                }

            }









        } catch (Exception e) {
            ShowUserMessage.showUserMessage(mContext, e.toString());
        }



            //choose_address_tv.setText(name);



    }
    public void openGallery() {
        try {

            Intent intent = new Intent(getActivity(), CustomPhotoGalleryActivity.class);
            intent.putExtra("item", classifiedItemOtherPictures.size() + "");

            getActivity().startActivityForResult(intent, SELECT_FILE);
        } catch (Exception e) {
            // new SendErrorAsync(mContext, e).execute();
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




    ArrayList<String>deleteImageList ;


    double lattitude , longitude ;


    HashMap<String, Object> params = new HashMap<String, Object>();
    public void sendRequestOnServer()
    {

        if (Validation.isStringNullOrBlank(classfieb_title_tv.getText().toString())== true)
        {
            ShowUserMessage.showUserMessage(getActivity() , "Please enter title");
            return;
        }
        if (Validation.isStringNullOrBlank(classified_cost_tv.getText().toString())== true)
        {
            ShowUserMessage.showUserMessage(getActivity() , "Please enter price");
            return;
        }

       /* if (Validation.isStringNullOrBlank(choose_address_tv.getText().toString())== true)
        {
            ShowUserMessage.showUserMessage(getActivity() , "Please enter classified address.");
            return;
        }*/
        if (Validation.isStringNullOrBlank(classified_description_tv.getText().toString())== true)
        {
            ShowUserMessage.showUserMessage(getActivity() , "Please enter description");
            return;
        }







        params.put("classifieds_uid" , classifiedItem.getClassifieds_uid());
        params.put("classifieds_title" , classfieb_title_tv.getText().toString());
        params.put("classifieds_desc" , classified_description_tv.getText().toString());
        params.put("classifieds_cost" , classified_cost_tv.getText().toString());
      //  params.put("classifieds_address" , choose_address_tv.getText().toString());
        params.put("classifieds_lat" , lattitude+"");
        params.put("classifieds_long" , longitude+"");


        if (Validation.isStringNullOrBlank(picturePath))
        {
            params.put("classifieds_profile" , picturePath);
        }
        else
        {
            params.put("classifieds_profile" , new File(picturePath));

        }

        params.put("classifieds_id" ,classifiedItem.getClassifieds_id()+"");

        ArrayList<String>myNewImage = new ArrayList<>();


        for (int i = 0 ; i < classifiedItemOtherPictures.size() ; i++)
        {
            String imagePath = classifiedItemOtherPictures.get(i).getUrl();

            if (Utill.isValidLink(imagePath) == false)
            {
                myNewImage.add(imagePath);
            }


        }

        String deleteImageId = "";
        for (int i = 0 ; i < deleteImageList.size() ; i++)
        {
            if (Validation.isStringNullOrBlank(deleteImageId))
            {
                deleteImageId = deleteImageList.get(i)+"";
            }
            else
            {
                deleteImageId = deleteImageId+","+ deleteImageList.get(i);
            }

        }



        if (classifiedItemOtherPictures.size() == 0 )
        {
            ShowUserMessage.showUserMessage(getActivity() , "Please select atleast one image");
            return;
        }
        params.put("del_id" , deleteImageId);
//

        circularProgressbar.setVisibility(View.VISIBLE);
        convertImagePathInBitmapStringTask = new ConvertImagePathInBitmapStringTask(getActivity(), myNewImage, new ImageToStringConveterListener() {
            @Override
            public void onSuccess(boolean status, ArrayList<String> bitmapString) {
                circularProgressbar.setVisibility(View.GONE);
            }

            @Override
            public void onUpdate(int update)
            {
                circularProgressbar.setProgress(update);
            }

            @Override
            public void onError() {

            }

            @Override
            public void onSuccess(boolean status, JSONArray imageOutPutJsonArray)
            {
                circularProgressbar.setVisibility(View.GONE);
                params.put("classified_other_pics" , imageOutPutJsonArray);

                Log.e("params" ,params.toString());
                httpRequest.getResponse(getActivity(), WebService.editclassified, params, new OnServerRespondingListener(getActivity()) {
                    @Override
                    public void onSuccess(JSONObject resjsonObject)
                    {
                        try
                        {

                            if (resjsonObject.getBoolean("status"))
                            {


                                /*JSONObject jsonObject = resjsonObject.getJSONObject("Response");
                                classifiedItem.setClassifieds_id(jsonObject.getInt("classifieds_id"));
                                classifiedItem.setClassifieds_uid(jsonObject.getInt("classifieds_uid"));
                                classifiedItem.setClassifieds_title(jsonObject.getString("classifieds_title"));
                                classifiedItem.setClassifieds_desc(jsonObject.getString("classifieds_desc"));


                                classifiedItem.setClassifieds_edate(jsonObject.optString("classifieds_edate"));
                                classifiedItem.setClassifieds_cost(jsonObject.optDouble("classifieds_cost"));
                                classifiedItem.setDistance(jsonObject.optDouble("distance"));
                                ArrayList<ClassifiedItemOtherPicture>classifiedItemOtherPictureArrayList = new ArrayList<ClassifiedItemOtherPicture>();
                                JSONArray classified_other_pics_json_array = jsonObject.getJSONArray("classified_other_pics");
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
*/

                                showMessageForFragmeneWithBack(getActivity() , resjsonObject.getString("message"));
                            }
                            else
                            {
                                ShowUserMessage.showUserMessage(getActivity() , resjsonObject.getString("message"));
                            }
                        }
                        catch (Exception e)
                        {
                            ShowUserMessage.showUserMessage(getActivity() , e.getMessage());

                        }
                    }
                });
            }
        });

        convertImagePathInBitmapStringTask.execute();

    }


    public void setDataOnView()
    {

        classfieb_title_tv.setText(classifiedItem.getClassifieds_title());
        //choose_address_tv.setText(classifiedItem.getClassifieds_address());
        for (int i  = 0 ; i < classifiedItemOtherPictures.size() ;i++)
        {
            imageList.add(classifiedItemOtherPictures.get(i).getThumb());
        }
        lattitude = classifiedItem.getClassifieds_lat();
        longitude = classifiedItem.getClassifieds_long();




        if (classifiedItem.getClassifieds_cost() - (int)classifiedItem.getClassifieds_cost() == 0)
        {
            classified_cost_tv.setText((int)classifiedItem.getClassifieds_cost()+"");
        }
        else
        {
            classified_cost_tv.setText(classifiedItem.getClassifieds_cost()+"");
        }




        classifiedItemOtherPictures.addAll(classifiedItem.getClassified_other_pics());

        classified_description_tv.setText(classifiedItem.getClassifieds_desc());
        galleryItemAdapter = new ClassfiedGalleryItemAdapter(getActivity()  , classifiedItemOtherPictures ,deleteImageList);
        image_gallery.setAdapter(galleryItemAdapter);


        image_gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity() , EventFullImageViewActivity.class);

                ArrayList<String>fullmageList = new ArrayList<String>();
                for (int i  = 0 ; i < classifiedItemOtherPictures.size() ;i++)
                {
                    fullmageList.add(classifiedItemOtherPictures.get(i).getUrl());
                }

                intent.putExtra("path_list", fullmageList);
                intent.putExtra("pos", String.valueOf(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DirectorFragmentManageActivity.updateTitle("Classified List");
        DirectorFragmentManageActivity.logoutButton.setVisibility(View.VISIBLE);


    }

    public  void showMessageForFragmeneWithBack(final FragmentActivity fragmentActivity , String msg)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(
                fragmentActivity).create();


        // Setting Dialog Title
        alertDialog.setTitle(SessionManager.getClubName(fragmentActivity));
        alertDialog.setCancelable(false);
        // Setting Dialog Message
        alertDialog.setMessage(msg);

        // Setting Icon to Dialog


        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                fragmentActivity.getSupportFragmentManager().popBackStack();

                editClassifedListener.onSucess();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}

