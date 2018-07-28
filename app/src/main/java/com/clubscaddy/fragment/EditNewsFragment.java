package com.clubscaddy.fragment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import com.clubscaddy.AsyTask.ConvertImagePathInBitmapStringTask;
import com.clubscaddy.CustomPhotoGalleryActivity;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.ImageToStringConveterListener;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.EditGalleryItemAdapter;
import com.clubscaddy.Adapter.NewsMainAdapter;
import com.clubscaddy.Bean.NewsBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.ImageDecoder;
import com.clubscaddy.custumview.SDCardMemory;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.custumview.CircularProgressBar;
import com.clubscaddy.custumview.CustomScrollView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class EditNewsFragment extends AppCompatActivity implements OnItemClickListener {

    public static final int VEDIO_SELECT = 109;
    private static final int CAMERA_REQUEST = 11;
    private static final int SELECT_FILE = 12;
    public static FragmentManager mFragmentManager;
    public static boolean init = false;
    public static boolean edit = false;
    public static boolean referesh = true;
    public static boolean localImage = false;
    public ActionBar mActionBar;
    public ImageButton backButton, uploadNewsOrEditProfile;
    public ImageButton logoutButton;
    public TextView delete_all_btn;
    public TextView actionbar_titletext;
    String Tag = getClass().getName();
    Context mContext;
    EditText titleET, vedioURLET, descriptionET;
    ArrayList<NewsBean> newsList;
    CustomScrollView add_news_scrolview;
    AQuery aQuery;
    // RelativeLayout uploadRelative;
    // ImageView newsImage;
    ProgressDialog pd;
    Button doneBTN, addAttachMent;
    @SuppressWarnings("deprecation")
    Gallery imageGallery;
    ImageView vedio_thumb;
    ImageButton delete_img_btn;
    RelativeLayout vedio_layout;
    NewsBean newsBean;
    int position;
    NewsMainAdapter newsMainAdapter;
    boolean isopenGalary = false;
    TextView discription_textview_status;
    ArrayList<String> delete_image_id_list = new ArrayList<String>();
    CircularProgressBar circularProgressbar;
    ConvertImagePathInBitmapStringTask convertImagePathInBitmapStringTask;
    String news_feed_id;//JSONArray all_path_json = new JSONArray();
    OnClickListener addToBack = new OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                DirectorFragmentManageActivity.popBackStackFragment();
            } catch (Exception e) {
                ShowUserMessage.showUserMessage(mContext, e.toString());
            }
        }
    };
    // Camera Code
    Bitmap imageBitmap;
    int flag1;
    Bitmap photo, image;
    String absPath;
    int rotate;
    String filePath = "null";
    Uri cameraImagePath;
    int currentVersion = Build.VERSION.SDK_INT;
    String picturePath = Environment.getExternalStorageDirectory().getPath();
    String vedioPath = Environment.getExternalStorageDirectory().getPath();
    File vedioFIle;
    // public static MemberListBean adminBean;

    CharSequence[] items = null;
    CharSequence[] withVideo = {"Take from camera", "Upload from gallery", "Cancel"};
    CharSequence[] withoutVideo = {"Take from camera", "Upload from gallery", "Cancel"};
    ArrayList<String> imageList;
    OnClickListener onClcks = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.done:
				/**/

                    break;
                case R.id.add_attachment:
                    pickImage();
                    break;
                case R.id.delete_img_btn:
                    removeVedio();
                    break;
                default:
                    break;
            }

        }
    };
    ArrayList<String> mainImageList;
    EditGalleryItemAdapter adapter;

    public void setInstanse(NewsBean newsBean, int position, ArrayList<NewsBean> newsList, NewsMainAdapter newsMainAdapter) {
        this.newsBean = newsBean;
        this.position = position;
        this.newsList = newsList;
        this.newsMainAdapter = newsMainAdapter;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_news_feed);


        mContext = getApplicationContext();
        mActionBar = getSupportActionBar();

        mainImageList = new ArrayList<>();

        add_news_scrolview = (CustomScrollView) findViewById(R.id.add_news_scrolview);

        discription_textview_status = (TextView) findViewById(R.id.discription_textview_status);

        circularProgressbar = (CircularProgressBar) findViewById(R.id.circularProgressbar);

        aQuery = new AQuery(EditNewsFragment.this);
        setActionBar();
        delete_img_btn = (ImageButton) findViewById(R.id.delete_img_btn);
        try {
            this.position = Integer.parseInt(getIntent().getStringExtra("pos"));
            setInstanse(AppConstants.newsList.get(position), position, AppConstants.newsList, newsMainAdapter);
        } catch (Exception e) {

        }
        initializeView();
        pd = new ProgressDialog(EditNewsFragment.this);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);

        setOnClicks();


        descriptionET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.e("action", event.getAction() + "");


                //	Toast.makeText(getActivity() ,"action  " + event.getAction(),Toast.LENGTH_SHORT ).show();


                if (1 == event.getAction()) {
                    add_news_scrolview.setEnableScrolling(true);

                } else {

                    add_news_scrolview.setEnableScrolling(false);


                }

                return false;
            }
        });
        descriptionET.addTextChangedListener(new TextWatcher() {
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



    }

    boolean isVedioAttched() {
        boolean flag = false;
        if (vedio_thumb.getVisibility() == View.VISIBLE && vedioFIle != null) {
            flag = true;
        }
        return flag;
    }

    void createNews(JSONArray all_path_json, HashMap<String, Object> params) {


        pd.show();
        JSONObject oldimage_path_json = new JSONObject();
        JSONArray oldimage_path_json_array = new JSONArray();

        ////
        for (int i = 0; i < mainImageList.size(); i++) {
            JSONObject imageJson = new JSONObject();
            try {
                if (mainImageList.get(i).contains("http"))
                    imageJson.put("image", mainImageList.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            oldimage_path_json_array.put(imageJson);
        }


		/*try {
			oldimage_path_json.put("image",oldimage_path_json_array);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/


        try {
            params.put("news_attechment", all_path_json.toString());
            params.put("oldurl", oldimage_path_json_array);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
        }
		/*if(vedioFIle!=null && vedio_thumb.getVisibility() == View.VISIBLE)
		{
			params.put("news_video", vedioFIle);

		}
		else
		{
			params.put("news_video", "1");
		}*/


        //Toast.makeText(getActivity(), oldimage_path_json.length()+"", 1).show();
        Log.e("old path ", all_path_json.length() + "old_url" + params.toString());
        AQuery aq = new AQuery(getApplicationContext());
        HashMap<String, String> paramss = new HashMap<String, String>();
        aq.ajax(WebService.editnews, params, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                // TODO Auto-generated method stub
                super.callback(url, object, status);
                pd.dismiss();

                try {


                    Log.e("object", object + "");


                    if (object.getString("status").equalsIgnoreCase("true")) {
                        //newsBean.setNews_details(object.getString("news_details"));
                        //newsBean.setNews_feed_attach_url(object.getString("news_feed_attach_url"));
                        //newsBean.setNews_title(object.getString("news_feed_attach_url"));//news_title

                        NewsFeedActivity.edit_new_cond = true;
                        showDialgInActivity1(object.getString("message"), EditNewsFragment.this);

                    } else {
                        showDialgInActivity(object.getString("message"), EditNewsFragment.this);
                    }


                    //Utill.showDialg("sssss "+object, EditNewsFragment.this);
                    //JSONObject jsonObj = new JSONObject();
                    //	Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();

                } catch (Exception e) {

                }
            }

        });
    }

    void initializeView() {
        imageList = new ArrayList<String>();
        titleET = (EditText) findViewById(R.id.title);
        titleET.setText(newsBean.getNews_title());
        vedioURLET = (EditText) findViewById(R.id.youtube_url);
        vedioURLET.setText(newsBean.getNews_feed_attach_url());
        descriptionET = (EditText) findViewById(R.id.description);




        descriptionET.setText(newsBean.getNews_details().replace("\\n","\n"));
        Log.e("data" , newsBean.getNews_details().replace("\\\\","\\"));


        discription_textview_status.setText(newsBean.getNews_details().replace("\\n","\n").length() + "/1000");
        // uploadRelative = (RelativeLayout)
        // view.findViewById(R.id.uploadRelative);
        doneBTN = (Button) findViewById(R.id.done);
        addAttachMent = (Button) findViewById(R.id.add_attachment);
        imageGallery = (Gallery) findViewById(R.id.image_gallery);


        imageList.addAll(newsBean.getNews_thumb_ur());

        mainImageList.addAll(newsBean.getNewImage());

        adapter = new EditGalleryItemAdapter(EditNewsFragment.this, imageList, mainImageList, newsBean);
        imageGallery.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        imageGallery.setOnItemClickListener(this);

        vedio_thumb = (ImageView) findViewById(R.id.vedio_thumb);

    }

    void setOnClicks() {
        addAttachMent.setOnClickListener(onClcks);
        doneBTN.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                try {
                    AppConstants.hideSoftKeyboard(EditNewsFragment.this);
                } catch (Exception e) {

                }
                news_feed_id = newsBean.getNews_feed_id();


                String titleStr = titleET.getText().toString();
                String detailStr = descriptionET.getText().toString();
                String urlStr = vedioURLET.getText().toString();
                Log.e("urlStr", urlStr + "");
                final HashMap<String, Object> params = new HashMap<String, Object>();
                //news_feed_id
                params.put("news_feed_id", news_feed_id);
                params.put("news_club_id", SessionManager.getUser_Club_id(EditNewsFragment.this));
                params.put("news_user_id", SessionManager.getUser_id(EditNewsFragment.this));
                params.put("news_title", titleStr);
                params.put("news_details", detailStr);
                params.put("news_feed_attach_url", urlStr);


                if (Utill.isStringNullOrBlank(titleStr)) {

                    showDialgInActivity("Please enter news title.", EditNewsFragment.this);
                    return;
                }

                if (Utill.isNetworkAvailable(EditNewsFragment.this)) {


                    ArrayList<String> fileimageList = new ArrayList<>();


                    for (int i = 0; i < imageList.size(); i++) {
                        if (!imageList.get(i).contains("http")) {
                            fileimageList.add(imageList.get(i));
                        }
                    }


                    convertImagePathInBitmapStringTask = new ConvertImagePathInBitmapStringTask(EditNewsFragment.this, fileimageList, new ImageToStringConveterListener() {
                        @Override
                        public void onSuccess(boolean status, ArrayList<String> bitmapString) {

                            circularProgressbar.setVisibility(View.GONE);
                            add_news_scrolview.setEnabled(true);

                        }

                        @Override
                        public void onUpdate(int update) {


                            circularProgressbar.setProgress(update);

                        }

                        @Override
                        public void onError() {

                            circularProgressbar.setVisibility(View.GONE);
                            add_news_scrolview.setEnabled(true);

                        }

                        @Override
                        public void onSuccess(boolean status, JSONArray imageOutPutJsonArray) {


                            circularProgressbar.setVisibility(View.GONE);
                            add_news_scrolview.setEnabled(true);
                            createNews(imageOutPutJsonArray, params);

                        }
                    });
                    convertImagePathInBitmapStringTask.execute();

                    circularProgressbar.setVisibility(View.VISIBLE);
                    add_news_scrolview.setEnabled(false);

                } else {
                    Utill.hideProgress();
                    Utill.showNetworkError(mContext);
                }


            }
        });
        delete_img_btn.setOnClickListener(onClcks);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (init) {
            init = false;
            resetValues();
        }
        Log.e(Tag, "onStart");
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

    private void pickImage() {

//eqweq
        if (imageList.size() == 0) {
            items = withVideo;
            vedio_thumb.setVisibility(View.INVISIBLE);
        } else {
            if (newsBean.getVedioUrl() == "" || newsBean.getVedioUrl().equalsIgnoreCase("")) {
                items = withoutVideo;
                vedio_thumb.setVisibility(View.INVISIBLE);
            } else {
                items = withoutVideo;
                vedio_thumb.setVisibility(View.VISIBLE);
            }

        }
        if (vedio_thumb.getVisibility() == View.VISIBLE) {
            Utill.showToast(mContext, "No More Selection For Vedio.");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(EditNewsFragment.this);
        builder.setTitle("Select pictures");
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

                        if (imageList.size() < 10) {
                            startActivityForResult(intent, CAMERA_REQUEST);
                        } else {
                            showDialgInActivity("No more image can be select", EditNewsFragment.this);
                        }

                        //startActivityForResult(intent, CAMERA_REQUEST);
                    } else if (items[position].equals("Upload from gallery")) {
                        openGallery();
                    } else if (items[position].equals("Select Video")) {
                        openVedio();
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

    public void openVedio() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("video/*");
        startActivityForResult(pickIntent, VEDIO_SELECT);
    }

    public void openGallery() {
        try {

            Intent intent = new Intent(getApplicationContext(), CustomPhotoGalleryActivity.class);
            intent.putExtra("item", imageList.size() + "");
            startActivityForResult(intent, SELECT_FILE);
        } catch (Exception e) {
            // new SendErrorAsync(mContext, e).execute();
        }
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

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == VEDIO_SELECT && resultCode == RESULT_OK) {

                if (data != null) {
                    Uri selectedImage = data.getData();
                    vedioPath = getRealPathFromURI(mContext, selectedImage);
                    String[] filePathColumn = {MediaStore.Video.Media.DATA};
                    Cursor cursor = mContext.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    // vedioPath = cursor.getString(columnIndex);
                    Log.e("PicturePath 1 = ", picturePath);
                    if (vedioPath != null) {
                        vedioFIle = new File(vedioPath);
                        vedioPath = vedioFIle.getAbsolutePath();
                        vedio_thumb.setVisibility(View.VISIBLE);
                        vedio_layout.setVisibility(View.VISIBLE);
                        Bitmap bMap = ThumbnailUtils.createVideoThumbnail(vedioFIle.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                        vedio_thumb.setImageBitmap(bMap);
                        // Utill.showToast(mContext, "" + vedioPath);
                    } else {
                        ShowUserMessage.showUserMessage(mContext, "Wrong Path");
                    }
                    cursor.close();
                }

            } else if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {

                if (flag1 == 0) {
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


                            Bitmap bitmap = BitmapFactory.decodeFile(imageUri);


                            if (bitmap.getWidth()<512 || bitmap.getHeight() < 512)
                            {
                                totalUnselectedItem++ ;
                            }
                            else
                            {
                                imageList.add(imageUri);
                                mainImageList.add(imageUri);
                            }








                        }
                        if (totalUnselectedItem != 0)
                        {
                            Utill.showDialg(totalUnselectedItem+" images are in wrong format Please upload correct format (Greater than 512*512)" , EditNewsFragment.this);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }


            } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
                try {
                    String URI = getImageURI();
                    try {
                        File file = new File(URI);
                        Bitmap bitmap = BitmapFactory.decodeFile(URI);


                        if (bitmap.getWidth()<512 || bitmap.getHeight() < 512)
                        {
                            Utill.showDialg("Images are in wrong format Please upload correct format (Greater than 512*512)" , EditNewsFragment.this);
                        }
                        else
                        {
                            mainImageList.add(file.getAbsolutePath());
                            imageList.add(file.getAbsolutePath());
                            adapter.notifyDataSetChanged();

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
    }

    private void ImageOrientation(File file, int rotate) {
        try {
            FileInputStream fis = new FileInputStream(file);
            filePath = file.getAbsolutePath();
            // imageList.add(filePath);
            // adapter.notifyDataSetChanged();
            Bitmap photo = BitmapFactory.decodeStream(fis);
            Matrix matrix = new Matrix();
            matrix.preRotate(rotate); // clockwise by 90 degrees
            photo = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Bitmap bm = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            photo = bm;

            // addadmin_profileImage.setImageBitmap(photo);

            imageBitmap = photo;
            // newsImage.setImageBitmap(photo);
            localImage = true;

        } catch (FileNotFoundException e) {
            ShowUserMessage.showUserMessage(mContext, e.toString());
        } catch (OutOfMemoryError e) {
            ShowUserMessage.showUserMessage(mContext, e.toString());
        }
    }

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

        imageList.add(filePath);
        mainImageList.add(filePath);

        adapter.notifyDataSetChanged();
        Bitmap bitmap = ImageDecoder.decodeFile(filePath);
        if (bitmap != null) {
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            imageBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
            // newsImage.setImageBitmap(imageBitmap);
            localImage = true;
            bitmap.recycle();
        }
    }

    void resetValues() {
        filePath = "null";
        titleET.setText("");
        descriptionET.setText("");
        vedioURLET.setText("");

    }

    void removeVedio() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditNewsFragment.this);

        // Setting Dialog Title
        alertDialog.setTitle("Tannis club");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want delete this?");


        // Setting Positive "Yes" Button
        alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                vedio_thumb.setVisibility(View.GONE);
                vedio_layout.setVisibility(View.GONE);
                dialog.cancel();
                // Write your code here to invoke YES event
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });

        alertDialog.show();

		/**/

    }

    void showDeleteConfirmationDialogu(final int position) {

		/*final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.delete_confirmation, null);
		TextView doneTV, cancelTV, headerTV;
		doneTV = (TextView) v.findViewById(R.id.done);
		cancelTV = (TextView) v.findViewById(R.id.cancel);
		headerTV = (TextView) v.findViewById(R.id.title);
		headerTV.setText("Do you want to remove this image?");
		doneTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageList.remove(position);
				adapter.notifyDataSetChanged();
				alertDialog.cancel();
			}
		});
		cancelTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.cancel();

			}
		});

		alertDialog.setView(v);
		alertDialog.show();*/
        Intent intent = new Intent(getApplicationContext(), FullImageViewActivity.class);
        intent.putExtra("image_pos", String.valueOf(position));
        intent.putExtra("pos", EditNewsFragment.this.position + "");

        intent.putExtra("path_list", imageList);

        startActivity(intent);

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        showDeleteConfirmationDialogu(arg2);
    }

    public void setActionBar() {

        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayUseLogoEnabled(false);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(getLayoutInflater().inflate(R.layout.director_actionbar_header_xml, null),
                new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

        backButton = (ImageButton) mActionBar.getCustomView().findViewById(R.id.actionbar_backbutton);
        logoutButton = (ImageButton) mActionBar.getCustomView().findViewById(R.id.actionbar_logoutbutton);
        uploadNewsOrEditProfile = (ImageButton) mActionBar.getCustomView().findViewById(R.id.upload_news);
        actionbar_titletext = (TextView) mActionBar.getCustomView().findViewById(R.id.actionbar_titletext);

        actionbar_titletext.setText("Edit News");
        Toolbar parent = (Toolbar) mActionBar.getCustomView().getParent();//first get parent toolbar of current action bar
        parent.setContentInsetsAbsolute(0, 0);
        // addToCalender();
        delete_all_btn = (TextView) mActionBar.getCustomView().findViewById(R.id.delete_all_btn);
        delete_all_btn.setText("Delete");


        delete_all_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //aQuery.ajax(WebService., params, type, callback)
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditNewsFragment.this);

                // Setting Dialog Title
                alertDialog.setTitle(SessionManager.getClubName(EditNewsFragment.this));

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want delete this news?");

                // Setting Icon to Dialog
                ;

                // Setting Positive "Yes" Button
                alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("user_id", newsBean.getNews_user_id());
                        params.put("news_id", newsBean.getNews_feed_id());
                        pd.show();
                        aQuery.ajax(WebService.deletenews, params, JSONObject.class, new AjaxCallback<JSONObject>() {
                            @Override
                            public void callback(String url, JSONObject object, AjaxStatus status) {
                                // TODO Auto-generated method stub
                                super.callback(url, object, status);

                                pd.dismiss();
                                if (object != null) {
                                    try {
                                        NewsFeedActivity.edit_new_cond = true;
                                        showDialg(object.getString("message"), EditNewsFragment.this);
                                    } catch (Exception e) {

                                    }


                                } else {
                                    showDialg(getString(R.string.check_internet_connection), EditNewsFragment.this);
                                }

                            }
                        });

                        dialog.cancel();
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
        });


        delete_all_btn.setVisibility(View.VISIBLE);
        logoutButton.setVisibility(View.GONE);
        logoutButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DirectorFragmentManageActivity.doLogout();
            }
        });
        logoutButton.setVisibility(View.INVISIBLE);
        backButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();

            }
        });
    }

    public void showBackButton() {
        if (backButton != null) {
            backButton.setVisibility(View.VISIBLE);
            uploadNewsOrEditProfile.setVisibility(View.GONE);
        }
    }

    public void hideLogoutButton() {
        uploadNewsOrEditProfile.setVisibility(View.GONE);
        logoutButton.setVisibility(View.VISIBLE);
    }

    public void hideBackButton() {
        if (backButton != null)
            backButton.setVisibility(View.GONE);
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @SuppressWarnings("deprecation")
    public void showDialg(String msg, Context mContext) {

        final AlertDialog alertDialog = new AlertDialog.Builder(
                mContext).create();
        alertDialog.setCancelable(false);
// Setting Dialog Title
        alertDialog.setTitle(SessionManager.getClubName(mContext));

// Setting Dialog Message
        alertDialog.setMessage(msg);

// Setting Icon to Dialog


// Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed

                alertDialog.dismiss();
                try {
                    newsList.remove(position);
                    newsMainAdapter.notifyDataSetChanged();

                } catch (Exception e) {

                }

                finish();
            }
        });

// Showing Alert Message
        alertDialog.show();
    }

    @SuppressWarnings("deprecation")
    public void showDialgInActivity(String msg, Activity mContext) {

        final AlertDialog alertDialog = new AlertDialog.Builder(
                mContext).create();

// Setting Dialog Title
        alertDialog.setTitle(SessionManager.getClubName(mContext));

// Setting Dialog Message
        alertDialog.setMessage(msg);

// Setting Icon to Dialog


// Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                alertDialog.dismiss();
            }
        });

// Showing Alert Message
        alertDialog.show();
    }

    @SuppressWarnings("deprecation")
    public void showDialgInActivity1(String msg, Activity mContext) {

        final AlertDialog alertDialog = new AlertDialog.Builder(
                mContext).create();

// Setting Dialog Title
        alertDialog.setCancelable(false);
        alertDialog.setTitle(SessionManager.getClubName(mContext));

// Setting Dialog Message
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);

// Setting Icon to Dialog


// Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                finish();
                alertDialog.dismiss();
            }
        });

// Showing Alert Message
        alertDialog.show();
    }

    private int imageRoated(File file) {
        //Bitmap bitmap = null;
        int rotate = 0;
        try {
            ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    //bitmap = rotateImage(file,rotate);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    //bitmap = rotateImage(file,rotate);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    //bitmap = rotateImage(file,rotate);
                    break;
                case 1:
                    rotate = 0;
                    //	bitmap = rotateImage(file,rotate);
                    break;

                case 2:
                    rotate = 0;
                    //bitmap = rotateImage(file,rotate);
                    break;
                case 4:
                    rotate = 180;
                    //bitmap = rotateImage(file,rotate);
                    break;

                case 0:
                    rotate = 0;
                    //bitmap = rotateImage(file,rotate);
                    break;
            }
        } catch (Exception e) {
            Log.e("Exception", "" + e.toString());
        }

        return rotate;
    }

    public class addNewsListener {
        public void onSuccess(String msg) {
            Utill.showToast(mContext, "" + msg);
            Utill.hideProgress();
            DirectorFragmentManageActivity.popBackStackFragment();
        }

        public void onError(String msg) {
            Utill.showToast(mContext, "" + msg);
            Utill.hideProgress();
        }
    }

    public class onClickImageListener {
        public void onClickImage(int position) {
            //showDeleteConfirmationDialogu(position);
        }
    }


}

