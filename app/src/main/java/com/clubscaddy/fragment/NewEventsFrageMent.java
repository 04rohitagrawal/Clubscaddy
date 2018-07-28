package com.clubscaddy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.clubscaddy.Adapter.ClassListAdapter;
import com.clubscaddy.Bean.ClassBean;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.FragmentBackListener;
import com.clubscaddy.Interface.FragmentBackResponseListener;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.Interface.RecycleViewItemClickListner;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.NewEventAdapter;
import com.clubscaddy.Adapter.NewEventAdapter.SwipeEventItemClickListener;
import com.clubscaddy.Bean.EventBean;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;

import org.json.JSONArray;
import org.json.JSONObject;

public class NewEventsFrageMent extends Fragment {

	String Tag = getClass().getName();
	public static FragmentManager mFragmentManager;
	public static Fragment mFragment;
	Context mContext;
	ListView eventsListView;
	ImageButton addEvent;
	ArrayList<MemberListBean>eventModeratorList ;
	//LinearLayout list_view_bottom_layout ;
	//TextView get_more_event_btn;


	RelativeLayout eventListLayout ;
	RelativeLayout classListLayout ;


	LinearLayout eventTab ;
	LinearLayout classTab ;

	HttpRequest httpRequest ;


	RecyclerView classListView ;

	ImageButton addClassBtv ;
	ArrayList<ClassBean> classArrayList ;


	ClassListAdapter classListAdapter ;

	LinearLayoutManager linearLayoutManager ;



	public static Fragment getInstance(FragmentManager mFragManager) {
		if (mFragment == null) {
			mFragment = new NewEventsFrageMent();
		}
		return mFragment;
	}

	int seemore ;


	LinearLayout event_list_footrt_layout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e(Tag, "onCreateView");
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.neweventlist, container, false);
		mContext = getActivity();
		//list_view_bottom_layout = (LinearLayout) rootView.findViewById(R.id.list_view_bottom_layout);
		eventModeratorList = new ArrayList<>();
		 event_list_footrt_layout= (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.event_list_footer ,null);

		httpRequest = new HttpRequest(getActivity());
		classArrayList = new ArrayList<>();

		classListAdapter = new ClassListAdapter(getActivity(), classArrayList, new RecycleViewItemClickListner() {
			@Override
			public void onItemClick(int pos, int status) {

				ClassDetailFragment classDetailFragment = new ClassDetailFragment();
				Bundle bundle = new Bundle();
				bundle.putString("header_name" , classArrayList.get(pos).getClassName());



				bundle.putSerializable("updateclassListListener" , updateclassListListener);
				bundle.putString("class_detail_id" , classArrayList.get(pos).getClassId());
                classDetailFragment.setArguments(bundle);
				//
				getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_frame , classDetailFragment , "").addToBackStack(null).commit();
			}
		});

		eventTab = (LinearLayout) rootView.findViewById(R.id.event_tab);
		classTab = (LinearLayout) rootView.findViewById(R.id.class_tab);

		eventListLayout = (RelativeLayout) rootView.findViewById(R.id.event_list_layout);
        classListLayout = (RelativeLayout) rootView.findViewById(R.id.class_list_layout);

		eventTab.setOnClickListener(tabItemClickLitemer);
		classTab.setOnClickListener(tabItemClickLitemer);


		linearLayoutManager = new LinearLayoutManager(getActivity());
		classListView = (RecyclerView) rootView.findViewById(R.id.class_list_view);
        classListView.setLayoutManager(linearLayoutManager);
		classListView.setAdapter(classListAdapter);
		//get_more_event_btn = (TextView) event_list_footrt_layout.findViewById(R.id.get_more_event_btn);




		addClassBtv = (ImageButton) rootView.findViewById(R.id.add_class_btv);

		event_list_footrt_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				eventsListView.removeFooterView(event_list_footrt_layout);
				getLastSixMontEventsByClub("3");
			}
		});


		addClassBtv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				classListView.setVisibility(View.GONE);

				eventsListView.setVisibility(View.GONE);

            ClassReservationGridFragment classReservationGridFragment = new ClassReservationGridFragment();
				Bundle bundle = new Bundle();
				bundle.putSerializable("updateclassListListener" , updateclassListListener);
				classReservationGridFragment.setArguments(bundle);
				getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_frame , classReservationGridFragment , "").addToBackStack("").commit();

			}
		});


		/*list_view_bottom_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				try
				{
					eventsListView.removeFooterView(event_list_footrt_layout);
				}
				catch (Exception e)
				{

				}

				list_view_bottom_layout.setVisibility(View.GONE);
				getLastSixMontEventsByClub("3");
			}
		});*/






		if (DirectorFragmentManageActivity.actionbar_titletext != null) {
			DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.club_events));
		}

		if (DirectorFragmentManageActivity.backButton != null) {
			DirectorFragmentManageActivity.showBackButton();
			DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
		}
		DirectorFragmentManageActivity.showLogoutButton();
		initializeView(rootView);
		setOnClickListener();
		if (SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MOBILE_ADMIN)
				|| SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_DIRECTOR)) {
		//	swipe = SwipeListView.SWIPE_MODE_LEFT;
		} else {
			//swipe = SwipeListView.SWIPE_MODE_NONE;
		}
		if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER)== false)
		{
			getAllEventsByClub("2");
		}
		else
		{
			getAllEventsByClub("1");
		}
		

		return rootView;
	}

	void initializeView(View view) {
		eventsListView = (ListView) view.findViewById(R.id.event_list_view);
		addEvent = (ImageButton) view.findViewById(R.id.add_event);


		if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER)== false)
		{
			eventsListView.addFooterView(event_list_footrt_layout);


		}
		if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER))
		{
			addEvent.setVisibility(View.GONE);
		}
		else
		{
			addEvent.setVisibility(View.VISIBLE);
		}

	}

	void setOnClickListener() {
		addEvent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AddEventFragment.edit = false;
				DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.AddEventsFragment_id);
			}
		});
	}

	ArrayList<EventBean> allEventsList;

	private void getAllEventsByClub(final String more)
	{
		if (Utill.isNetworkAvailable(getActivity())) {
			Utill.showProgress(mContext);
			GlobalValues.getModelManagerObj(mContext).getEventsList(new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					Log.e("json", json+"");
					//Toast.makeText(getActivity(), json, 1).show();




					allEventsList = JsonUtility.parseAllEventsList(json,getActivity());
					setALLEventAdapter(allEventsList);


					try
					{
						seemore = new JSONObject(json).getInt("seemore");
					}
					catch (Exception e)
					{

					}
					if (allEventsList.size() == 0 || seemore == 1)
					{
						eventsListView.removeFooterView(event_list_footrt_layout);
					}

                  if (allEventsList.size() == 0)
				  {
					  eventsListView.removeFooterView(event_list_footrt_layout);
				  }

					if (SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER)) {
						addEvent.setVisibility(View.GONE);
					} else {
						addEvent.setVisibility(View.VISIBLE);
					}
					Utill.hideProgress();
				}

				@Override
				public void onError(String msg) {
					//Toast.makeText(mContext, "status "+msg, 1).show();
					//ShowUserMessage.showUserMessage(mContext, msg);
					//list_view_bottom_layout.setVisibility(View.GONE);
					eventsListView.removeFooterView(event_list_footrt_layout);
					if (msg.equalsIgnoreCase("No record found"))
					{
						allEventsList  = new ArrayList<EventBean>();

getLastSixMontEventsByClub("3");

					}
					else
					{
						ShowUserMessage.showUserMessage(getActivity() , msg+"");
					}


					//if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER) == false)
					{

					}


					Utill.hideProgress();
				}
			} ,more);
		} else {
			Utill.showNetworkError(mContext);
		}
	}






	private void getLastSixMontEventsByClub(String more) {
		if (Utill.isNetworkAvailable(getActivity())) {
			Utill.showProgress(mContext);
			GlobalValues.getModelManagerObj(mContext).getEventsList(new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					Log.e("json", json+"");
					//Toast.makeText(getActivity(), json, 1).show();


					allEventsList.addAll( JsonUtility.parseAllEventsList(json,getActivity()));
					setALLEventAdapter(allEventsList);




					Utill.hideProgress();
				}

				@Override
				public void onError(String msg) {
					//Toast.makeText(mContext, "status "+msg, 1).show();
					//ShowUserMessage.showUserMessage(mContext, msg);


					Utill.hideProgress();
				}
			} ,more);
		} else {
			Utill.showNetworkError(mContext);
		}
	}











	private void getAllEventItemsByClub(String event_id) {
		if (Utill.isNetworkAvailable(getActivity())) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("event_id", event_id);
			Utill.showProgress(mContext);
			GlobalValues.getModelManagerObj(mContext).getEventsList(new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					Log.e("json", json+"");
					//Toast.makeText(getActivity(), json, 1).show();
					allEventsList = JsonUtility.parseAllEventsListItem(json,getActivity());
					EventDetailFrageMent.eventBean = allEventsList.get(0);
					//Toast.makeText(getActivity(), "pos == "+EventDetailFrageMent.eventBean.getthumnailList().size(), 1).show();
					EventDetailFrageMent.pos = 0 ;


                 try
				 {
					 JSONObject jsonObject = new JSONObject(json);
					 JSONObject jsonObject1 = jsonObject.getJSONArray("response").getJSONObject(0);

					 eventModeratorList.clear();
					 JSONArray user_list_json_array = jsonObject1.getJSONArray("event_moderator");
					 for(int i = 0 ; i < user_list_json_array.length() ;i++)
					 {
						 JSONObject user_list_json_array_item = user_list_json_array.getJSONObject(i);
						 MemberListBean memberListBean = new MemberListBean();
						 memberListBean.setUser_id(user_list_json_array_item.getString("user_id"));

						 memberListBean.setUser_email(user_list_json_array_item.getString("user_email"));

						 memberListBean.setUser_first_name(user_list_json_array_item.getString("user_name").split(" ")[0]);
						 memberListBean.setUser_last_name(user_list_json_array_item.getString("user_name").split(" ")[1]);

						 memberListBean.setUser_phone(user_list_json_array_item.getString("user_phone"));
						 memberListBean.setUser_profilepic(user_list_json_array_item.getString("user_profilepic"));

						 eventModeratorList.add(memberListBean);
					 }
				 }
				 catch (Exception e)
				 {

				 }







                EventDetailFrageMent eventDetailFrageMent = new EventDetailFrageMent();
				eventDetailFrageMent.setArgemunt(eventModeratorList);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame ,eventDetailFrageMent,"").addToBackStack(null).commit();






					//DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.EventDetail_id);

					Utill.hideProgress();
				}

				@Override
				public void onError(String msg) {
					//Toast.makeText(mContext, "status "+msg, 1).show();
					//ShowUserMessage.showUserMessage(mContext, msg);
					Utill.hideProgress();
				}
			},param);
		} else {
			Utill.showNetworkError(mContext);
		}
	}
	NewEventAdapter groupAdapter;

	public void setALLEventAdapter(final ArrayList<EventBean> list) {
		groupAdapter = null;
		eventsListView.setVisibility(View.VISIBLE);
		groupAdapter = new NewEventAdapter(mContext, list);




		groupAdapter.setEventItemClickListener(new SwipeEventItemClickListener() {

			@Override
			public void OnEditClick(int position) {
				
				
				
				
				
				
				EventDetailFrageMent.eventId = allEventsList.get(position).getEvent_id();
				getAllEventItemsByClub(EventDetailFrageMent.eventId);
				
				
				
				
				/*
				 * AddGroupFragment.groupEdit = groupList.get(position);
				 * DirectorFragmentManageActivity
				 * .switchFragment(DirectorFragmentManageActivity
				 * .AddGroupFragment_id);
				 */
			}

			@Override
			public void OnBlockClick(int position, int blockStatus) {
			}
		});

		eventsListView.setAdapter(groupAdapter);
	
		// ShowUserMessage.showUserMessage(mContext, "" +
		// groupAdapter.getCount());
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

	OnClickListener addToBack = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				Utill.hideKeybord(getActivity());
				DirectorFragmentManageActivity.popBackStackFragment();
			} catch (Exception e) {
				ShowUserMessage.showUserMessage(mContext, e.toString());
			}
		}
	};

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



	public OnClickListener tabItemClickLitemer = new OnClickListener() {
		@Override
		public void onClick(View v)
		{
			if (v.getId() == R.id.event_tab )
			{
				eventListLayout.setVisibility(View.VISIBLE);
				classListLayout.setVisibility(View.GONE);
				eventTab.setBackground(getResources().getDrawable(R.drawable.active_tb_bg));
				classTab.setBackground(getResources().getDrawable(R.color.unselected_tab));

			}

			if (v.getId() == R.id.class_tab )
			{
				eventListLayout.setVisibility(View.GONE);
				classListLayout.setVisibility(View.VISIBLE);

				classTab.setBackground(getResources().getDrawable(R.drawable.active_tb_bg));
				eventTab.setBackground(getResources().getDrawable(R.color.unselected_tab));

			     if (classArrayList.size() == 0)
				 {
					 getClassList();
				 }

			}


		}
	};


	public void getClassList()
	{
		HashMap<String , Object> param = new HashMap<>();
		param.put("class_uid" , SessionManager.getUser_id(getActivity()));
		param.put("class_club_id" , SessionManager.getUser_Club_id(getActivity()));
		param.put("user_type" , SessionManager.getUser_type(getActivity()));


		httpRequest.getResponse(getActivity(), WebService.class_list, param, new OnServerRespondingListener(getActivity()) {
			@Override
			public void onSuccess(JSONObject jsonObject)
			{
				try
				{
					if (jsonObject.getBoolean("status"))
					{
						JSONArray classJsonArray = jsonObject.getJSONArray("Response");

                        for (int i = 0 ; i < classJsonArray.length() ; i++)
						{
							JSONObject classJsonArrayItem = classJsonArray.getJSONObject(i);
							ClassBean classBean = new ClassBean();
							classBean.setClassId(classJsonArrayItem.getString("class_id"));
							classBean.setClassName(classJsonArrayItem.getString("class_name"));
							classBean.setClassUid(classJsonArrayItem.getString("class_uid"));
							classArrayList.add(classBean);
						}
					}
					else
					{
						Utill.showDialg(jsonObject.getString("message") , getActivity());
					}

					classListAdapter.notifyDataSetChanged();


				}
				catch (Exception e)
				{

				}

			}
		});
	}


	FragmentBackResponseListener updateclassListListener = new FragmentBackResponseListener() {
		@Override
		public void onResponse(String responsString, String reservationId) {
			super.onResponse(responsString, reservationId);
		}

		@Override
		public void UpdateView() {
			super.UpdateView();
			classListView.setVisibility(View.VISIBLE);
		}

		@Override
		public void onBackFragment() {
			super.onBackFragment();
			classListView.setVisibility(View.VISIBLE);
			eventsListView.setVisibility(View.VISIBLE);

			classArrayList.clear();

			getClassList();
		}
	};

}
