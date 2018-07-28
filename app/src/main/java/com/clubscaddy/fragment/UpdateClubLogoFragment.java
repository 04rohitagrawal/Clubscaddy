package com.clubscaddy.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.Interface.ImageResizingListener;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.imageutility.ShowImage;
import com.clubscaddy.utility.DeviceInfo;
import com.clubscaddy.utility.ImageBitmapTransformationAccordingHeight;
import com.clubscaddy.utility.ImageBitmapTranslation;
import com.clubscaddy.utility.ImageTransformation;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.utility.ImageDecoder;
import com.clubscaddy.custumview.InternetConnection;
import com.clubscaddy.custumview.SDCardMemory;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class UpdateClubLogoFragment extends Fragment {

	String Tag = getClass().getName();
	public static FragmentManager mFragmentManager;
	public static Fragment mFragment;
	Context mContext;
	boolean imageselectedflag = false;
	ImageView addadmin_profileImage;
	ImageButton addadmin_pickfile;
	HttpRequest httpRequest ;
    SessionManager sessionManager ;
	Button adminList_addAdmin;

	int gender_type;
	Float isRating;

	//ImageButton actionbar_backbutton1;


	// Camera Code
	Bitmap imageBitmap;
	int flag1;
	Bitmap photo, image;
	String absPath;
	int rotate;
	//String filePath = "null";
	Uri cameraImagePath;
	int currentVersion = Build.VERSION.SDK_INT;
	private static final int CAMERA_REQUEST = 11;
	private static final int SELECT_FILE = 12;
	String picturePath =  "";
	public static boolean edit = false;
	public static boolean referesh = true;
	public static boolean localImage = false;


	ProgressBar progress_bar ;

//	public static MemberListBean adminBean;


	public static Fragment getInstance(FragmentManager mFragManager) {
		if (mFragment == null) {
			mFragment = new UpdateClubLogoFragment();
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	int screenWidth ,screenHeight;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.club_logo, container, false);
		Log.e(Tag, "onCreateView");
		mContext = getActivity();

		httpRequest = new HttpRequest(getActivity());
		sessionManager = new SessionManager(getActivity());
		progress_bar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

		try {
			DirectorFragmentManageActivity.hideLogout();
			if (DirectorFragmentManageActivity.actionbar_titletext != null) {
				DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.manage_club_logo_icons));
			}
			if(DirectorFragmentManageActivity.backButton!=null){
				DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
				DirectorFragmentManageActivity.showBackButton();
			}
			adminList_addAdmin = (Button) rootView.findViewById(R.id.adminList_addAdmin);
			addadmin_pickfile = (ImageButton) rootView.findViewById(R.id.addadmin_pickfile);
			addadmin_profileImage = (ImageView) rootView.findViewById(R.id.addadmin_profileImage);

			adminList_addAdmin.setOnClickListener(addToAdmin);
			addadmin_pickfile.setOnClickListener(addToPickFile);



			String pic  =SessionManager.getClub_Logo (getActivity());



			try
			{
				progress_bar.setVisibility(View.VISIBLE);

				Picasso.with(getActivity()).load(pic).placeholder(R.drawable.default_pic).placeholder(R.drawable.default_pic).transform(new ImageTransformation(DeviceInfo.getDeviceWidth(getActivity())/2)).into(addadmin_profileImage, new Callback() {
					@Override
					public void onSuccess() {
						progress_bar.setVisibility(View.GONE);
					}

					@Override
					public void onError() {
						progress_bar.setVisibility(View.GONE);

					}
				});

			}
			catch (Exception e)
			{

			}





		} catch (Exception e) {
			ShowUserMessage.showUserMessage(mContext, e.toString());
		}

		return rootView;
	}



	@Override
	public void onStart() {
		super.onStart();
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

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.e(Tag, "onDestroyView");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.e(Tag, "onDetach");
	}


	ProgressDialog progressDialog ;

	private void addToAdminService() {

		if (InternetConnection.isInternetOn(mContext))
		{

			try {

				if (InternetConnection.isInternetOn(mContext)) {


					if (Validation.isStringNullOrBlank(picturePath) == false)
					{
						ImageDecoder imageDecoder = new ImageDecoder(getActivity());
						ImageResizingListener imageResizingListener = new ImageResizingListener() {
							@Override
							public void onImageResize(String filePath)
							{
								File file ;

								file = new File(picturePath);
								sendDataOnServer(file);
							}
							@Override
							public void onImageResize(ArrayList<String> imagePathList) {

							}
						};
						imageDecoder.imageResize(picturePath , 600 , imageResizingListener);


					}else{
						ShowUserMessage.showUserMessage(mContext, "Please select an image.");
						return;
					}


				} else {
					ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.check_internet_connection));
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.check_internet_connection));
		}
	}


	 public void sendDataOnServer(File file)
	 {
		 HashMap<String , Object> param = new HashMap<>();
		 param.put("user_club_id",SessionManager.getUser_Club_id(mContext));

		 if (file != null)
		 {
			param.put("club_logo", file);
		 }


		 httpRequest.getResponse(getActivity(), WebService.update_logo,param, new OnServerRespondingListener(getActivity()) {
			 @Override
			 public void onSuccess(JSONObject jsonObject) {

				 Log.e("Respone" , jsonObject+"");
				 try
				 {
					 Utill.showDialg(jsonObject.getString("msg") , getActivity());

					 if (jsonObject.getBoolean("status"))
					 {
						 SessionManager.setClubLogo(getActivity() , jsonObject.getString("club_logo"));
					 }

				 }
				 catch (Exception e)
				 {

				 }
			 }
		 });


	 }




	private void pickImage() {
		final CharSequence[] items = { "Take from camera", "Upload from gallery", "Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
						((Activity) mContext).startActivityForResult(intent, CAMERA_REQUEST);
					} else if (items[position].equals("Upload from gallery")) {
						Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(i, SELECT_FILE);
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
			if (requestCode == SELECT_FILE && resultCode == getActivity().RESULT_OK) {
				if (flag1 == 0) {
					if (data != null) {
						Uri selectedImage = data.getData();
						String[] filePathColumn = { MediaStore.Images.Media.DATA };
						Cursor cursor = mContext.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
						cursor.moveToFirst();
						int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
						picturePath = cursor.getString(columnIndex);

						cursor.close();
					}
				}
			}
			if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK) {
				try {
					String URI = getImageURI();
					try {
						File file = new File(URI);
						picturePath = file.getAbsolutePath() ;

					} catch (Exception e) {
						ShowUserMessage.showUserMessage(mContext, e.toString());
					}
				} catch (Exception e) {
					ShowUserMessage.showUserMessage(mContext, e.toString());
				}

			}

			if ((requestCode == CAMERA_REQUEST || requestCode == SELECT_FILE ) && resultCode == getActivity().RESULT_OK)
			{
				image = ImageDecoder.decodeFile(picturePath);
				if (image != null)
				{

					if (image.getWidth() < 512 || image.getHeight() < 512)
					{

						ShowUserMessage.showDialogForImageDia(getActivity() );
						return;
					}
					progress_bar.setVisibility(View.VISIBLE);
					Glide.with(getActivity()).load(new File(picturePath)).asBitmap().listener(new RequestListener<File, Bitmap>() {
						@Override
						public boolean onException(Exception e, File model, Target<Bitmap> target, boolean isFirstResource) {

							progress_bar.setVisibility(View.GONE);



							return false;
						}

						@Override
						public boolean onResourceReady(Bitmap resource, File model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
							progress_bar.setVisibility(View.GONE);

							return false;
						}
					}).transform(new ImageBitmapTransformationAccordingHeight(getActivity() , DeviceInfo.getDeviceHeight(getActivity())/2)).placeholder(R.drawable.default_pic).placeholder(R.drawable.default_pic).into(addadmin_profileImage);


				}
				else
				{
					ShowUserMessage.showUserMessage(mContext, "Wrong Path");
					imageselectedflag = false;
				}
			}



		} catch (Exception e) {
			ShowUserMessage.showUserMessage(mContext, e.toString());
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

	OnClickListener addToPickFile = new OnClickListener() {

		@Override
		public void onClick(View v) {
			((Activity) mContext).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					pickImage();
					//pickImage();
				}
			});
		}
	};

	public class UpdateLogoListener {
		public void onSuccess(String msg) {
			ShowUserMessage.showUserMessage(mContext, msg);
		//	DirectorFragmentManageActivity.popBackStackFragment();
		}

		public void onError(String message) {
			ShowUserMessage.showUserMessage(mContext, message);
		}
	}

	public void onSaveInstanceState(Bundle outState) {
		// outState.put
		// ShowUserMessage.showUserMessage(mContext, "onSaveInstance");
	};

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		// ShowUserMessage.showUserMessage(mContext, "onSaveInstance restore");
		super.onViewStateRestored(savedInstanceState);
	}

















}
