package com.clubscaddy.fragment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.Manifest;
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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v4.content.IntentCompat;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import com.clubscaddy.AsyTask.ConvertImagePathInBitmapStringTask;
import com.clubscaddy.CustomPhotoGalleryActivity;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.ImageToStringConveterListener;
import com.clubscaddy.Interface.MyPermissionGrrantedListner;
import com.clubscaddy.LoginActivity;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.GalleryItemAdapter;
import com.clubscaddy.utility.ImageDecoder;
import com.clubscaddy.custumview.SDCardMemory;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.UserPermision;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.custumview.CircularProgressBar;
import com.clubscaddy.custumview.CustomScrollView;

public class AddNewsFeedFragment extends AppCompatActivity implements OnItemClickListener {

	String Tag = getClass().getName();
	public  ActionBar mActionBar;
	public  ImageButton  backButton, uploadNewsOrEditProfile;
	public 	ImageButton logoutButton ;
	public 	TextView delete_all_btn;
	public  TextView actionbar_titletext;
	public static FragmentManager mFragmentManager;
	public static Fragment mFragment;
	Activity mContext;
	EditText titleET, vedioURLET, descriptionET;
	CustomScrollView add_news_scrolview;
	// RelativeLayout uploadRelative;
	// ImageView newsImage;
	Button doneBTN, addAttachMent;
	Gallery imageGallery;
	ImageView vedio_thumb;
	ImageButton delete_img_btn;
	RelativeLayout vedio_layout;
	public static boolean init = false;
	AQuery aQuery ;


	ConvertImagePathInBitmapStringTask convertImagePathInBitmapStringTask;
	CircularProgressBar circularProgressbar;

	ProgressDialog pd ;
	TextView discription_textview_status;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_news_feed);

		vedio_layout = (RelativeLayout) findViewById(R.id.vedio_layout);
		mActionBar = getSupportActionBar();
		setActionBar();
		add_news_scrolview = (CustomScrollView) findViewById(R.id.add_news_scrolview);

		mContext = this;
		delete_img_btn = (ImageButton) findViewById(R.id.delete_img_btn);

		discription_textview_status = (TextView) findViewById(R.id.discription_textview_status);

		circularProgressbar = (CircularProgressBar) findViewById(R.id.circularProgressbar);


		/*if (DirectorFragmentManageActivity.actionbar_titletext != null) {
			DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.add_news));
		}

		if (DirectorFragmentManageActivity.backButton != null) {
			DirectorFragmentManageActivity.showBackButton();
			DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
		}*/
		pd = new ProgressDialog(AddNewsFeedFragment.this);
		pd.setCancelable(false);
		pd.setMessage("Please wait...");

		initializeView();
		setOnClicks();
		//Utill.showProgress(AddNewsFeedFragment.this);
		imageList = new ArrayList<String>();
		adapter = new GalleryItemAdapter(AddNewsFeedFragment.this, imageList);

		imageGallery.setAdapter(adapter);
		imageGallery.setOnItemClickListener(this);
		Log.e(Tag, "onCreate");
		descriptionET.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.e("action" , event.getAction()+"");


				//	Toast.makeText(getActivity() ,"action  " + event.getAction(),Toast.LENGTH_SHORT ).show();




				if (1 == event.getAction())
				{
					add_news_scrolview.setEnableScrolling(true);

				}
				else
				{

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
			public void afterTextChanged(Editable s)
			{
				discription_textview_status.setText(s.toString().length()+"/1000");
			}
		});
	}



	boolean isVedioAttched(){
		boolean flag = false;
		if(vedio_thumb.getVisibility() == View.VISIBLE && vedioFIle != null){
			flag = true;
		}
		return flag;
	}

	void createNews(JSONArray all_path_json) {
		String titleStr = titleET.getText().toString().trim();
		String detailStr = descriptionET.getText().toString().trim();
		String urlStr = vedioURLET.getText().toString().trim();
		final HashMap<String, Object> params = new HashMap<String, Object>();
		try {
			params.put("news_club_id",/* new StringBody*/(SessionManager.getUser_Club_id(mContext)));
			params.put("news_user_id", /*new StringBody*/(SessionManager.getUser_id(mContext)));
			params.put("news_title", /*new StringBody*/(titleStr));
			params.put("news_details", /*new StringBody*/(detailStr));
			params.put("news_feed_attach_url", /*new StringBody*/(urlStr));
			String allPath = all_path_json.toString();
			params.put("news_attechment", /*new StringBody*/(allPath));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.e("filepath", "ff" + filePath);

		if (Utill.isStringNullOrBlank(titleStr)) {
			Utill.showToast(AddNewsFeedFragment.this, "Please enter news title.");
		} 
		else 
			if(Utill.isNetworkAvailable(AddNewsFeedFragment.this)) {

				pd.show();

				aQuery = new AQuery(getApplicationContext());
				aQuery.ajax(WebService.create_News, params, JSONObject.class, new AjaxCallback<JSONObject>()
				{
					@Override
					public void callback(String url, JSONObject object, AjaxStatus status) {
						// TODO Auto-generated method stub
						super.callback(url, object, status);
						pd.dismiss();
						try {
							//Toast.makeText(getActivity(),  object.getString("message")+"", 1).show();

							if(object.getString("status").equalsIgnoreCase("true"))
							{
								showDialg(object.getString("message")+"", AddNewsFeedFragment.this);
							}
							else
							{
								ShowUserMessage.showDialogOnActivity(AddNewsFeedFragment.this, object.getString("message"));
							}

						}
						catch (Exception e) {

							showDialg(e.getMessage()+"", AddNewsFeedFragment.this);
							// TODO Auto-generated catch block
							//Toast.makeText(getApplicationContext(), e.getMessage(), 1).show();
						}


					}
				});







			} else {
				Utill.showNetworkError(mContext);
			}
	}

	public class addNewsListener {
		public void onSuccess(String msg) {
			Utill.showToast(AddNewsFeedFragment.this, "" + msg);
			Utill.hideProgress();
			DirectorFragmentManageActivity.popBackStackFragment();
			NewsFeedActivity.add_new_cond = true;
		}

		public void onError(String msg) {
			Utill.showToast(mContext, "" + msg);
			Utill.hideProgress();
		}
	}

	/*
	 * void createNews() { String titleStr =
	 * titleET.getText().toString().trim(); String detailStr =
	 * descriptionET.getText().toString().trim(); String urlStr =
	 * vedioURLET.getText().toString().trim(); Map<String, Object> params = new
	 * HashMap<String, Object>(); params.put("news_club_id",
	 * SessionManager.getUser_Club_id(mContext)); params.put("news_user_id",
	 * SessionManager.getUser_id(mContext)); params.put("news_title", titleStr);
	 * params.put("news_details", detailStr); params.put("news_feed_attach_url",
	 * urlStr);
	 * 
	 * Log.e("filepath", "ff" + filePath);
	 * 
	 * 
	 * 
	 * if(Utill.isStringNullOrBlank(titleStr)){ Utill.showToast(mContext,
	 * "Please Enter News Title."); }else if(Utill.isStringNullOrBlank(filePath)
	 * || Utill.isStringNullOrBlank(titleStr)){ Utill.showToast(mContext,
	 * "Please Select Image Or Enter Youtube path."); } else if
	 * (Utill.isNetworkAvailable(mContext)) { File file = null; if (filePath !=
	 * "null") { file = new File(filePath); if (file != null) {
	 * Log.e("image Name", file.getName()); params.put("news_attechment",new
	 * FileBody(file,file.getName())); //params.put("news_attechment","hello");
	 * } } Utill.showProgress(mContext);
	 * GlobalValues.getModelManagerObj(mContext).createNewsFeed(params, new
	 * ModelManagerListener() {
	 * 
	 * @Override public void onSuccess(String json) { // TODO Auto-generated
	 * method stub DirectorFragmentManageActivity.popBackStackFragment();
	 * Utill.hideProgress(); }
	 * 
	 * @Override public void onError(String msg) { // TODO Auto-generated method
	 * stub Utill.hideProgress(); } }); } else {
	 * Utill.showNetworkError(mContext); } }
	 */

	void initializeView() {
		titleET = (EditText) findViewById(R.id.title);
		vedioURLET = (EditText) findViewById(R.id.youtube_url);
		descriptionET = (EditText) findViewById(R.id.description);
		// uploadRelative = (RelativeLayout)
		// view.findViewById(R.id.uploadRelative);
		doneBTN = (Button) findViewById(R.id.done);
		addAttachMent = (Button) findViewById(R.id.add_attachment);
		imageGallery = (Gallery) findViewById(R.id.image_gallery);
		vedio_thumb = (ImageView) findViewById(R.id.vedio_thumb);

	}

	void setOnClicks() {
		addAttachMent.setOnClickListener(onClcks);
		doneBTN.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideSoftKeyboard();

				convertImagePathInBitmapStringTask = new ConvertImagePathInBitmapStringTask(AddNewsFeedFragment.this, imageList, new ImageToStringConveterListener() {
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
						createNews(imageOutPutJsonArray);

					}
				});
				convertImagePathInBitmapStringTask.execute();

				circularProgressbar.setVisibility(View.VISIBLE);
				add_news_scrolview.setEnabled(false);


			}
		});
		delete_img_btn.setOnClickListener(onClcks);
	}

	OnClickListener onClcks = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.done:
				AsyncTask<Void, String, String>task1 = new AsyncTask<Void, String, String>()
				{
					protected void onPreExecute()
					{
						Utill.showProgress(AddNewsFeedFragment.this);
					}
					@Override
					protected String doInBackground(Void... params) 
					{
						// TODO Auto-generated method stub

						return null;
					}
					protected void onPostExecute(String result) 
					{
						Utill.hideProgress();	
					}

				};
				task1.execute();

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
	private static final int CAMERA_REQUEST = 11;
	private static final int SELECT_FILE = 12;
	String picturePath = Environment.getExternalStorageDirectory().getPath();
	String vedioPath = Environment.getExternalStorageDirectory().getPath();
	File vedioFIle;

	public static boolean edit = false;
	public static boolean referesh = true;
	public static boolean localImage = false;

	// public static MemberListBean adminBean;

	CharSequence[] items = null;

	CharSequence[] withVideo = { "Take from camera", "Upload from gallery", "Cancel" };
	CharSequence[] withoutVideo = { "Take from camera", "Upload from gallery", "Cancel" };
	ArrayList<String> cameraPermissionArrayList ;
	private void pickImage() {
		cameraPermissionArrayList = new ArrayList<>();
		cameraPermissionArrayList.add(Manifest.permission.CAMERA);
		cameraPermissionArrayList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		cameraPermissionArrayList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
		if (vedio_thumb.getVisibility() == View.VISIBLE) {
			Utill.showToast(AddNewsFeedFragment.this, "No More Selection For Vedio.");
			return;
		}

		if (imageList.size() == 0) {
			items = withVideo;
		} else {
			items = withoutVideo;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(AddNewsFeedFragment.this);
		builder.setTitle("Select pictures");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int position) {
				try {
					if (items[position].equals("Take from camera"))
					{

						if(imageList.size() <10)
						{
							openCameraByIntent();
						}
						else
						{
							showUserMessage(AddNewsFeedFragment.this, "No more image can be select");	
						}



					} else if (items[position].equals("Upload from gallery")) {
						openGallery();
					} else if (items[position].equals("Select Video")) {
						openVedio();
					} else {
						dialog.dismiss();
					}
				} catch (Exception e) {

					showUserMessage(AddNewsFeedFragment.this, e.toString());
				}
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
							startActivityForResult(intent, CAMERA_REQUEST);
						}
					}
				});
			}

			private void showUserMessage(Activity mContext, String msg) {
				// TODO Auto-generated method stub
				final	AlertDialog alertDialog = new AlertDialog.Builder(
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
		});
		builder.setCancelable(false);
		builder.show();
	}

	public static final int VEDIO_SELECT = 109;

	public void openVedio() {
		Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		pickIntent.setType("video/*");
		startActivityForResult(pickIntent, VEDIO_SELECT);
	}

	public void openGallery() {
		try {

			Intent intent = new Intent(getApplicationContext(), CustomPhotoGalleryActivity.class);
			intent.putExtra("item", imageList.size()+"");
			startActivityForResult(intent, SELECT_FILE);
		} catch (Exception e) {
			// new SendErrorAsync(mContext, e).execute();
		}
	}

	private void savePref(String str) {
		SharedPreferences sf = mContext.getSharedPreferences("LoginUser",
				MODE_PRIVATE);
		SharedPreferences.Editor editor = sf.edit();
		editor.putString("imageURI", str);
		editor.commit();
	}

	private String getImageURI() {
		SharedPreferences sf = mContext.getSharedPreferences("LoginUser", MODE_PRIVATE);
		String uri = sf.getString("imageURI", null);
		return uri;
	}

	ArrayList<String> imageList;
	GalleryItemAdapter adapter;

	public String getRealPathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
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
					String[] filePathColumn = { MediaStore.Video.Media.DATA };
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
						for (int i = 0; i < abs.length; i++)
						{
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

							}
						}

						if (totalUnselectedItem != 0)
						{
							Utill.showDialg(totalUnselectedItem+" images are in wrong format Please upload correct format (Greater than 512*512)"  , AddNewsFeedFragment.this);
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
								 Utill.showDialg("Images are in wrong format Please upload correct format (Greater than 512*512)" , AddNewsFeedFragment.this);
							 }
							 else
							 {
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
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
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

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				AddNewsFeedFragment.this);

		// Setting Dialog Title
		alertDialog.setTitle("Tannis club");

		// Setting Dialog Message
		alertDialog.setMessage("Are you sure you want delete this?");



		// Setting Positive "Yes" Button
		alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				vedio_thumb.setVisibility(View.GONE);
				vedio_layout.setVisibility(View.GONE);
				dialog.cancel();
				// Write your code here to invoke YES event
			}
		});

		// Setting Negative "NO" Button
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,	int which) {
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

			@Oxcvcxvcxverride
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
		Intent intent = new Intent(getApplicationContext() , EventFullImageViewActivity.class);
		intent.putExtra("path_list", imageList);
		intent.putExtra("pos", position+"");

		startActivity(intent);

	}

	public class onClickImageListener {
		public void onClickImage(int position) {
			//showDeleteConfirmationDialogu(position);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		showDeleteConfirmationDialogu(arg2);
	}
	public  void setActionBar() {

		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayUseLogoEnabled(false);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setCustomView(  getLayoutInflater().inflate(R.layout.director_actionbar_header_xml, null),
				new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

		backButton = (ImageButton) mActionBar.getCustomView().findViewById(R.id.actionbar_backbutton);
		logoutButton = (ImageButton) mActionBar.getCustomView().findViewById(R.id.actionbar_logoutbutton);
		logoutButton.setVisibility(View.GONE);
		uploadNewsOrEditProfile = (ImageButton) mActionBar.getCustomView().findViewById(R.id.upload_news);
		actionbar_titletext = (TextView) mActionBar.getCustomView().findViewById(R.id.actionbar_titletext);

		actionbar_titletext.setText("Create News");
		Toolbar parent =(Toolbar) mActionBar.getCustomView().getParent();//first get parent toolbar of current action bar
		parent.setContentInsetsAbsolute(0,0);
		// addToCalender();
		delete_all_btn =(TextView) mActionBar.getCustomView().findViewById(R.id.delete_all_btn);
		logoutButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddNewsFeedFragment.this);

				// Setting Dialog Title
				alertDialog.setTitle(SessionManager.getClubName(AddNewsFeedFragment.this));

				// Setting Dialog Message
				alertDialog.setMessage("Are you sure  you want to Logout?");

				;

				// Setting Positive "Yes" Button
				alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {

						// Write your code here to invoke YES event
						Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP
								| Intent.FLAG_ACTIVITY_CLEAR_TASK);
						intent.putExtra("EXIT", true);
						startActivity(intent);
						SessionManager.clearSharePref(getApplicationContext());
						dialog.dismiss();
					}
				});

				// Setting Negative "NO" Button
				alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,	int which) {
						// Write your code here to invoke NO event

						dialog.cancel();
					}
				});

				// Showing Alert Message
				alertDialog.show();

			}
		});
		//logoutButton.setVisibility(View.INVISIBLE);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});
	}
	public  void showBackButton() {
		if (backButton != null) {
			backButton.setVisibility(View.VISIBLE);
			uploadNewsOrEditProfile.setVisibility(View.GONE);
		}
	}
	public  void hideLogoutButton() {
		uploadNewsOrEditProfile.setVisibility(View.GONE);
		logoutButton.setVisibility(View.VISIBLE);
	}
	public  void hideBackButton() {
		if (backButton != null)
			backButton.setVisibility(View.GONE);
	}


	public void hideSoftKeyboard() {
		if(getCurrentFocus()!=null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	private int imageRoated(File file){
		//Bitmap bitmap = null;
		int rotate =0;
		try {
			ExifInterface exif = new ExifInterface(file.getAbsolutePath());
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
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
			Log.e("Exception", ""+e.toString());
		}

		return rotate;
	}
	@SuppressWarnings("deprecation")
	public  void showDialg(String msg , Context mContext)
	{

		final	AlertDialog alertDialog = new AlertDialog.Builder(
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
				NewsFeedActivity.add_new_cond = true;
				finish();
				alertDialog.dismiss();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}




}
