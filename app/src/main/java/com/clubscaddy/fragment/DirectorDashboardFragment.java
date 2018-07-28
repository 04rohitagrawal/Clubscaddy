package com.clubscaddy.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.BackGroundServies.FetchMemberListInBack;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.utility.CircleBitmapTranslation;
import com.clubscaddy.utility.CircleTransform;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.DirectorDashboardAdapter;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;


public  class DirectorDashboardFragment extends Fragment {

	String Tag = getClass().getName();
	public static FragmentManager mFragmentManager;
	public static Fragment mFragment;
	Context mContext;

	GridView dir_grideview;
	public static TextView dir_clubMessage;
	ImageView dir_default_logo,editClubMessage;
	// AQuery aq;
	public static TextView date_tv;
	TextView open_court_tv;
	TextView court_in_use_tv;
	public static TextView courtTempuratureTV, numberOfBookedCourtsTV, numberOfOpenCourtsTV;
SessionManager sessionManager ;
	LinearLayout dir_linearlayout_parent;

	LinearLayout courtInUseLayout ,openCourtTvLayout;
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.e(Tag, "onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(Tag, "onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e(Tag, "onCreateView");
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.directordashboad_frgment, container, false);
		mContext = getActivity();
			//DirectorFragmentManageActivity.visibleLogout();

		dir_linearlayout_parent = (LinearLayout) rootView.findViewById(R.id.dir_linearlayout_parent);
		sessionManager = new SessionManager(getActivity());
		ManageGroupFragment.tabIndex = 1;

		courtInUseLayout = (LinearLayout) rootView.findViewById(R.id.court_in_use_layout);
        openCourtTvLayout = (LinearLayout) rootView.findViewById(R.id.open_court_tv_layout);



		if (sessionManager.getResmoduleStatus().equals("2")&& Validation.isStringNullOrBlank(sessionManager.getReservationLink()))
		{
			courtInUseLayout.setVisibility(View.INVISIBLE);
			openCourtTvLayout.setVisibility(View.INVISIBLE);
		}




		/*TextView open_court_tv;
		TextView court_in_use_tv;*/
		open_court_tv = (TextView) rootView.findViewById(R.id.open_court_tv);
		court_in_use_tv = (TextView) rootView.findViewById(R.id.court_in_use_tv);
		open_court_tv.setText("Open "+sessionManager.getSportFiledName(getActivity()));
		court_in_use_tv.setText(sessionManager.getSportFiledName(getActivity())+" in use");

		date_tv  = (TextView) rootView.findViewById(R.id.date_tv);
		date_tv.setText(sessionManager.getClub_status_change_date(getActivity()));

		if (SessionManager.getUser_id(mContext) == null)
		{
			return null;
		}

		if (DirectorFragmentManageActivity.actionbar_titletext != null)
		{


			if (SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_DIRECTOR))
			{
				DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.director_dashboard));

				DirectorFragmentManageActivity.updateTitle("Hello "+SessionManager.getFirstName(mContext)+" "+SessionManager.getLastName(mContext));


			}
			else
			{
					if (SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER))
					{
					DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.member_dashboard));
						DirectorFragmentManageActivity.updateTitle("Hello "+SessionManager.getFirstName(mContext)+" "+SessionManager.getLastName(mContext));

					}

					else
					{


						if (SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MOBILE_ADMIN))
						{
							//Utill.showDialg(getString(R.string.admin_dashboard), mContext);
						DirectorFragmentManageActivity.updateTitle("Admin Dashboard");
					    }
					    else
						{
							if (SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_COACH))
							{
								//Utill.showDialg(getString(R.string.admin_dashboard), mContext);
								DirectorFragmentManageActivity.updateTitle("Coache Dashboard");
							}
						}


						DirectorFragmentManageActivity.updateTitle("Hello "+SessionManager.getFirstName(mContext)+" "+SessionManager.getLastName(mContext));

					}
			}


		}

		if(DirectorFragmentManageActivity.logoutButton!=null){
			DirectorFragmentManageActivity.visibleLogout();
		}


		dir_grideview = (GridView) rootView.findViewById(R.id.dir_grideview);
		dir_clubMessage = (TextView) rootView.findViewById(R.id.dir_clubMessage);
		dir_default_logo = (ImageView) rootView.findViewById(R.id.dir_default_logo);

		dir_default_logo.setOnClickListener(onClickListener);
		courtTempuratureTV = (TextView) rootView.findViewById(R.id.tempurature_value);
		numberOfBookedCourtsTV = (TextView) rootView.findViewById(R.id.court_in_use);
		numberOfOpenCourtsTV = (TextView) rootView.findViewById(R.id.open_court);
		editClubMessage = (ImageView) rootView.findViewById(R.id.editClubMessage);

		courtTempuratureTV.setText(SessionManager.getTempurature(mContext));
		numberOfBookedCourtsTV.setText("0");
		numberOfOpenCourtsTV.setText("0");

		setDirectorAdapter();

		dir_default_logo.setBackgroundResource(0);
		Drawable d = getResources().getDrawable(R.drawable.logo_profile);
		final ProgressBar progress = (ProgressBar) rootView.findViewById(R.id.prog);

		Glide.with(getActivity()).load(SessionManager.getClub_Logo(mContext)).transform(new CircleBitmapTranslation(getActivity())).placeholder(R.drawable.default_club_profile).error(R.drawable.default_club_profile) .listener(new RequestListener<String, GlideDrawable>() {
			@Override
			public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
				return false;
			}

			@Override
			public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
				progress.setVisibility(View.GONE);
				return false;
			}
		}).into(dir_default_logo);






		DirectorFragmentManageActivity.hideBackButton();
		GlobalValues.getModelManagerObj(mContext).getNotificationCount(new ModelManagerListener()
		{

			@Override
			public void onSuccess(String json) {
				// TODO Auto-generated method stub

				try {

					JSONObject jsonObject = new JSONObject(json).getJSONObject("dashboardinfo");
					sessionManager.setCurrentTime(jsonObject.getString("time"));

				}
				catch (Exception e)
				{

				}



				if (sessionManager.getResmoduleStatus().equals("2")&& Validation.isStringNullOrBlank(sessionManager.getReservationLink()))
				{


					courtInUseLayout.setVisibility(View.INVISIBLE);
					openCourtTvLayout.setVisibility(View.INVISIBLE);
				}
				else
				{
					courtInUseLayout.setVisibility(View.VISIBLE);
					openCourtTvLayout.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void onError(String msg) {
				// TODO Auto-generated method stub

			}
		},getActivity());
		DirectorFragmentManageActivity.showLogoutButton();
		if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MOBILE_ADMIN) || SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_DIRECTOR)){
			editClubMessage.setVisibility(View.GONE);
		}else{
			editClubMessage.setVisibility(View.GONE);
		}
		editClubMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.ChangeClubMessaegFragment_id);
			}
		});

		dir_clubMessage.setText(""+SessionManager.getClubMessage(mContext));





		Intent service = new Intent(getActivity() , FetchMemberListInBack.class);
		getActivity().startService(service);
		
		return rootView;
	}

	

	public static DirectorDashboardAdapter adapter;

	private void setDirectorAdapter() {


		dir_linearlayout_parent.post(new Runnable(){
			public void run(){
				int height = dir_linearlayout_parent.getHeight();
				adapter = new DirectorDashboardAdapter(mContext,height);
				dir_grideview.setAdapter(adapter);
			}
		});


		
		
		
		dir_grideview.setOnItemClickListener(onItemClickListener);
		
	}

	OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			switch (position) {
			case 0:

				DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.ShowReservationFragment_id);
				break;
			case 1:
				DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.NotificationsFragment_id);
				break;
			case 2:
				// if
				// (SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER))
				DropInGroupHomeFragment.CURRENT_SCREEN = DropInGroupHomeFragment.DROP_INS_SCREEN;
				DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.DropInsMatchMakerFragment_id);
				break;





			case 3:
				DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.EventsHomeFragment_id);
				break;


                case 4:
                    DropInGroupHomeFragment.CURRENT_SCREEN = DropInGroupHomeFragment.GROUPS_SCREEN;
                    DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.DropInsGroupFragment_id);
                    break;


				case 5:
				DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.NewsFeedFragment_id);
				break;
			case 6:

				mFragment = EditMyProfileFragment.getInstance(mFragmentManager);
				if (mFragment != null) {
					DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.EditMyProfileFragment_id);

				}
				break;

                case 7:
                    DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.MembersDirectoryFragment_id);
                    break;



                case 8:
                    DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.DirectorSettingFragment_id);


                    break;

			case 9:
				DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.BroadcastPolling_id);
				break;
			case 10:
				StaticsFragment sFragment1 = new StaticsFragment();
				sFragment1.setInstanse(getResources().getString(R.string.coaches));
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, sFragment1,"ddd").addToBackStack(null).commit();

				break;
			case 11:
				ClassifiedFragment sFragment = new ClassifiedFragment();
				//sFragment.setInstanse(getResources().getString(R.string.statics));
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, sFragment,"ddd").addToBackStack(null).commit();
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e(Tag, "onActivityCreated");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.e(Tag, "onStart");
	}

	@Override
	public void onResume() {
	//	dir_clubMessage.setText("" + SessionManager.getClubMessage(mContext));
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

	public OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v)
		{




			if (Utill.isValidLink(SessionManager.getClub_Logo(mContext)))
			{
				Intent intent = new Intent(getActivity() , FullImageShowFragment.class);
                intent.putExtra("image_path", SessionManager.getClub_Logo(mContext));
				intent.putExtra("image_type", String.valueOf(1));
				startActivity(intent);
			}



		}
	};



}
