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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.clubscaddy.Interface.FragmentBackListemers;
import com.clubscaddy.Interface.FragmentBackListener;
import com.clubscaddy.Interface.ImageResizingListener;
import com.clubscaddy.Interface.MyPermissionGrrantedListner;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.imageutility.ShowImage;
import com.clubscaddy.utility.UserPermision;
import com.clubscaddy.utility.Utill;

import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.ImageDecoder;
import com.clubscaddy.custumview.SDCardMemory;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Validation;


public class AddMemberFragment extends Fragment {
	public static int VIEW_TYPE = -1;
	public static final int DETAIL = 1;
	public static final int EDIT = 2;
	public static final int ADD = 3;

	ArrayList<String>spinner_list = new ArrayList<String>();



	String Tag = getClass().getName();
	public static FragmentManager mFragmentManager;
	public static Fragment mFragment;
	Activity mContext;
	boolean imageselectedflag = false;
	ImageView addadmin_profileImage;
	ImageButton addadmin_pickfile;
	EditText addadmin_firstname, addadmin_lastname, addadmin_email, addadmin_phone;
	Button adminList_addAdmin;
	ToggleButton addadmin_gender,juniourToggele;
	LinearLayout juniorView;

	LinearLayout rating_relative_layout;

	int gender_type;
	int juniour_type;
	String isRating = "";

//	ImageButton actionbar_backbutton1;
	ProgressBar progress ;
	Spinner rating_spiner;
	// Camera Code
	Bitmap imageBitmap;
	int flag1;
	Bitmap photo, image;
	ImageButton spinner_arrow_btn;
	String absPath;
	int rotate;
	String filePath = "";
	Uri cameraImagePath;
	int currentVersion = Build.VERSION.SDK_INT;
	private static final int CAMERA_REQUEST = 11;
	private static final int SELECT_FILE = 12;
	String picturePath = Environment.getExternalStorageDirectory().getPath();
	public static boolean edit = false;
	public static boolean referesh = true;
	public static boolean localImage = false;

	HttpRequest httpRequest ;

	public static MemberListBean adminBean;

SessionManager sessionManager;
	public static Fragment getInstance(FragmentManager mFragManager) {
		if (mFragment == null) {
			mFragment = new AddMemberFragment();
		}
		referesh = true;
		localImage = false;
		return mFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		Log.e(Tag, "onAttach");
	}

	FragmentBackListemers fragmentBackListene ;
	 public void setInstanse(FragmentBackListemers fragmentBackListener)
	 {
		 this.fragmentBackListene = fragmentBackListener ;
	 }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.add_member_xml, container, false);
		Log.e(Tag, "onCreateView");


		mContext = getActivity();
		httpRequest = new HttpRequest(getActivity());

		try {
			sessionManager = new SessionManager();

			//DirectorFragmentManageActivity.hideLogout();
			if (DirectorFragmentManageActivity.actionbar_titletext != null) {
				DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.add_member));
			}
			if(DirectorFragmentManageActivity.backButton!=null){
				DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
				DirectorFragmentManageActivity.showBackButton();
			}



			rating_relative_layout = (LinearLayout) rootView.findViewById(R.id.rating_relative_layout);

			addadmin_firstname = (EditText) rootView.findViewById(R.id.addadmin_firstname);
			addadmin_lastname = (EditText) rootView.findViewById(R.id.addadmin_lastname);
			addadmin_email = (EditText) rootView.findViewById(R.id.addadmin_email);
			addadmin_phone = (EditText) rootView.findViewById(R.id.addadmin_phone);
			adminList_addAdmin = (Button) rootView.findViewById(R.id.adminList_addAdmin);
			addadmin_gender = (ToggleButton) rootView.findViewById(R.id.addadmin_gender);
			//addadmin_ratingBar = (RatingBar) rootView.findViewById(R.id.addadmin_ratingBar);
			addadmin_pickfile = (ImageButton) rootView.findViewById(R.id.addadmin_pickfile);
			addadmin_profileImage = (ImageView) rootView.findViewById(R.id.addadmin_profileImage);
			progress = (ProgressBar) rootView.findViewById(R.id.prog);
			juniorView = (LinearLayout) rootView.findViewById(R.id.juniour_view);
			juniourToggele = (ToggleButton) rootView.findViewById(R.id.juniour_seniour);
			rating_spiner = (Spinner) rootView.findViewById(R.id.spinner);

			spinner_arrow_btn = (ImageButton) rootView.findViewById(R.id.spinner_arrow_btn);

			juniorView.setVisibility(View.GONE);

			adminList_addAdmin.setOnClickListener(addToAdmin);
			addadmin_gender.setOnClickListener(addToGender);
		//	addadmin_ratingBar.setOnClickListener(addToRating);

			addadmin_pickfile.setOnClickListener(addToPickFile);
			// addadmin_ratingBar.on

			setDataToView();

		} catch (Exception e) {
			ShowUserMessage.showUserMessage(mContext, e.toString());
		}

		return rootView;
	}

	void setViewsAccordingToID(){
		switch (VIEW_TYPE) {
		case ADD:

			break;
		case EDIT:

			break;
		case DETAIL:

			break;

		default:
			break;
		}
	}

	void setDataToView() {



		spinner_list.clear();


		sessionManager = new SessionManager();

		try
		{
			JSONArray jsonArray = new JSONArray(sessionManager.getClubRatting(getActivity()));
			for (int i = 0 ; i < jsonArray.length() ;i++ )
			{
				spinner_list.add(jsonArray.getString(i));
			}
		}
		catch (Exception e)
		{

		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_list_item_layout, spinner_list);


		if (sessionManager.getClubRatingType(getActivity()) == 2)
		{
			rating_spiner.setAdapter(adapter);
			rating_relative_layout.setVisibility(View.VISIBLE);
		}
		else
		{
			rating_relative_layout.setVisibility(View.INVISIBLE);
		}



		Drawable d = getResources().getDrawable(R.drawable.default_pic);



	}

	@Override
	public void onStart() {
		super.onStart();
		Log.e(Tag, "onStart");
		if (!edit && referesh) {
			referesh = false;
			clearFields();
		}

	}

	void clearFields() {
		addadmin_firstname.setText("");
		addadmin_lastname.setText("");
		addadmin_email.setText("");
		addadmin_phone.setText("");
	//	addadmin_ratingBar.setRating(0);
		//Picasso.with(getActivity()).load("sfdf'ds").transform(new CircleTransform()).into(addadmin_profileImage);
		//addadmin_profileImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_pic));
		 Bitmap bm = BitmapFactory.decodeResource(getResources(),
				  R.drawable.default_img);

		 addadmin_profileImage.setImageBitmap(getCircleBitmap(bm));
		 spinner_list.clear();


		sessionManager = new SessionManager();

		try
		{
			JSONArray jsonArray = new JSONArray(sessionManager.getClubRatting(getActivity()));
			for (int i = 0 ; i < jsonArray.length() ;i++ )
			{
				spinner_list.add(jsonArray.getString(i));
			}
		}
		catch (Exception e)
		{

		}


			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_list_item_layout, spinner_list);

		if (sessionManager.getClubRatingType(getActivity()) == 2)
		{
			rating_spiner.setAdapter(adapter);
		}
		else
		{
			rating_relative_layout.setVisibility(View.INVISIBLE);
		}
		progress.setVisibility(View.GONE);


		spinner_arrow_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rating_spiner.performClick();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();

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
		try
		{
			AppConstants.hideSoftKeyboard(getActivity());
		}
		catch(Exception e)
		{

		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.e(Tag, "onDetach");
	}

	private void addToAdminService()
	{
		try
		{
			isRating = spinner_list.get(rating_spiner.getSelectedItemPosition())   ;
		}catch(Exception e)
		{
			;
		}

		if (addadmin_firstname.getText().toString().equals("")) {
			ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.empty_first_name));
		} else if (addadmin_lastname.getText().toString().equals("")) {
			ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.empty_last_name));
		} else if (addadmin_email.getText().toString().equals("")) {
			ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.empty_email));
		} else if (!Validation.isEmailAddress(addadmin_email, true)) {
			ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.correct_email));
		} else {
			if (Validation.isNetworkAvailable(mContext)) {

				if (addadmin_gender.isChecked()) {
					gender_type = 1;
				}else{
					gender_type = 2;
				}
				if(juniourToggele.isChecked()){
					juniour_type = 1;
				}else{
					juniour_type = 0;
				}

				final HashMap<String , Object> param = new HashMap<>();
				param.put("user_type" , AppConstants.USER_TYPE_MEMBER);
				param.put("user_first_name", addadmin_firstname.getText().toString());
				param.put("user_last_name", addadmin_lastname.getText().toString());
				param.put("user_email", addadmin_email.getText().toString());
				param.put("user_phone",addadmin_phone.getText().toString());
				param.put("user_rating", "" + isRating);
				param.put("user_device_type", "1");
				param.put("user_device_token", SessionManager.getUser_Device_Token(mContext));
				param.put("user_gender", String.valueOf(gender_type));
				param.put("user_junior", String.valueOf(juniour_type));
				param.put("user_club_id", SessionManager.getUser_Club_id(mContext));



				if (Validation.isStringNullOrBlank(filePath))
				{
					sendDataOnServer(param);
				}
				else
				{
					ImageResizingListener imageResizingListener = new ImageResizingListener() {
						@Override
						public void onImageResize(String filePath)
						{
							param.put("user_profilepic", new File(filePath));
							sendDataOnServer(param);
						}
						@Override
						public void onImageResize(ArrayList<String> imagePathList) {

						}
					};

					ImageDecoder imageDecoder = new ImageDecoder(getActivity());
					imageDecoder.imageResize(filePath , 600 , imageResizingListener);

				}






			} else {
				ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.check_internet_connection));
			}
		}
	}
	ArrayList<String> cameraPermissionArrayList ;
	ArrayList<String> gallryPermissionArrayList ;

	private void pickImage() {
		gallryPermissionArrayList = new ArrayList<>();
		cameraPermissionArrayList = new ArrayList<>();
		gallryPermissionArrayList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		gallryPermissionArrayList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
		cameraPermissionArrayList.add(Manifest.permission.CAMERA);
		cameraPermissionArrayList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		cameraPermissionArrayList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
		final CharSequence[] items = { "Take Photo", "Choose from Gallery", "Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Select pictures");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int position) {
				try
				{
					if (items[position].equals("Take Photo"))
					{
						openCameraByIntent();
					}
					else if(items[position].equals("Choose from Gallery"))
					{
						openMobileGallry();
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
	private void openMobileGallry()
	{
		UserPermision.updatePermission(mContext, gallryPermissionArrayList, new MyPermissionGrrantedListner() {
			@Override
			public void isAllPermissionGranted(boolean isPermissionGranted, int code) {

				if (isPermissionGranted)
				{
					Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(i, SELECT_FILE);
				}

			}
		});

	}
	private void openCameraByIntent()
	{
		UserPermision.updatePermission(mContext, cameraPermissionArrayList, new MyPermissionGrrantedListner() {
			@Override
			public void isAllPermissionGranted(boolean isPermissionGranted, int code)
			{
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
					((Activity) mContext).startActivityForResult(intent, CAMERA_REQUEST);
				}


			}
		});



	}

	private void savePref(String str) {
		SharedPreferences sf = mContext.getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sf.edit();
		editor.putString("imageURI", str);
		editor.commit();
	}

	private String getImageURI() {
		SharedPreferences sf = mContext.getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
		String uri = sf.getString("imageURI", null);
		return uri;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (requestCode == SELECT_FILE && resultCode == getActivity().RESULT_OK) {
				if (flag1 == 0) {
					if (data != null)
					{
						Uri selectedImage = data.getData();
						String[] filePathColumn = { MediaStore.Images.Media.DATA };
						Cursor cursor = mContext.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
						cursor.moveToFirst();
						int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
						picturePath = cursor.getString(columnIndex);
						Log.e("PicturePath 1 = ", picturePath);
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
							// profileImageView.setImageBitmap(imageBitmap);rewrwer
							//addadmin_profileImage.setImageBitmap(getCircleBitmap(imageBitmap));
							localImage = true;
							// addadmin_profileImage.setImageBitmap(imageBitmap);
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
						cursor.close();
					}
				}
			} else if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK) {
					picturePath = getImageURI();


			}


			if (Validation.isStringNullOrBlank(picturePath)==false)
			{
				ShowImage showImage = new ShowImage(getActivity());
				showImage.showImageInCircleView(new File(picturePath) , addadmin_profileImage);
			}



		} catch (Exception e) {
			ShowUserMessage.showUserMessage(mContext, e.toString());
		}
	}

	private void ImageOrientation(File file, int rotate) {
		try {
			FileInputStream fis = new FileInputStream(file);
			filePath = file.getAbsolutePath();
			Bitmap photo = BitmapFactory.decodeStream(fis);
			Matrix matrix = new Matrix();
			matrix.preRotate(rotate); // clockwise by 90 degrees
			photo = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
			Bitmap bm = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
			photo = bm;

//			addadmin_profileImage.setImageBitmap(photo);

			imageBitmap = photo;

			//addadmin_profileImage.setImageBitmap(getCircleBitmap(photo));
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
		Bitmap bitmap = ImageDecoder.decodeFile(filePath);
		if (bitmap != null) {
			bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
			imageBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
			addadmin_profileImage.setImageBitmap(getCircleBitmap(imageBitmap) );
			localImage = true;
			bitmap.recycle();
		}
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
			((Activity) mContext).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					pickImage();
				}
			});
		}
	};

	public class AddMemberListener {
		public void onSuccess(String msg) {
			ShowUserMessage.showUserMessage(mContext, msg);
			DirectorFragmentManageActivity.popBackStackFragment();
		}

		public void onError(String message) {
			ShowUserMessage.showUserMessage(mContext, message);
		}
	}

	public void onSaveInstanceState(Bundle outState) {
		// outState.put
	//	ShowUserMessage.showUserMessage(mContext, "onSaveInstance");
	};

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
	//	ShowUserMessage.showUserMessage(mContext, "onSaveInstance restore");
		super.onViewStateRestored(savedInstanceState);
	}


	private Bitmap getCircleBitmap(Bitmap bitmap) {
		 final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
		  bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		 final Canvas canvas = new Canvas(output);

		 final int color = Color.RED;
		 final Paint paint = new Paint();
		 final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		 final RectF rectF = new RectF(rect);

		 paint.setAntiAlias(true);
		 canvas.drawARGB(0, 0, 0, 0);
		 paint.setColor(color);
		 canvas.drawOval(rectF, paint);

		 paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		 canvas.drawBitmap(bitmap, rect, rect, paint);

		 bitmap.recycle();

		 return output;
		}


		public  void sendDataOnServer(HashMap<String , Object> param)
		{

			httpRequest.getResponse(getActivity(), WebService.add_admin, param, new OnServerRespondingListener(getActivity()) {
				@Override
				public void onSuccess(JSONObject jsonObject)
				{
                  try
				  {
					  if (jsonObject.getBoolean("status"))
					  {
						  fragmentBackListene.onReload(true);
						  ShowUserMessage.showMessageForFragmeneWithBack(getActivity() , jsonObject.getString("message"));

					  }
					  else
					  {
						  Utill.showDialg(jsonObject.getString("message") , getActivity());

					  }

				  }
				  catch (Exception e)
				  {

				  }


				}
			});
		}



}
