package com.clubscaddy.fragment;

import java.sql.Time;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.clubscaddy.Adapter.CustomSpinnerAdapter;
import com.clubscaddy.Adapter.CustumSpinnerListAdapter;
import com.clubscaddy.Adapter.TimeSpinnerAdapter;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.FragmentBackResponseListener;
import com.clubscaddy.R;
import com.clubscaddy.Bean.GroupBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.custumview.CustomScrollView;

public class AddMatchMakerFragment extends Fragment implements OnClickListener {

	String Tag = getClass().getName();
	public static FragmentManager mFragmentManager;
	public static Fragment mFragment;
	Context mContext;
	LinearLayout add_player_layout;
	TextView   selectGroupET,error_text;
	Spinner addGroupET;
	RelativeLayout add_group_relative;
	Spinner select_format_spinner;
	Spinner select_player_qty;
	TextView selectGroupTv,dateET;
	Spinner timeET ;
	RadioGroup radioSelection;
	Button doneBtn;
	SessionManager sessionManager;
	EditText description;
	CustomScrollView dropInScroll;

	LinearLayout parentPanel ;

	Calendar selected_calender;

	Calendar currentTimes ;

	TextView discription_textview_status;
	public static Fragment getInstance(FragmentManager mFragManager) {
		if (mFragment == null) {
			mFragment = new AddMatchMakerFragment();
		}
		return mFragment;
	}


	ArrayList<String> soltArrayList ;

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
		View rootView = inflater.inflate(R.layout.add_drop_ins_xml, container, false);
		mContext = getActivity();
		sessionManager = new SessionManager(getActivity());
		description = (EditText) rootView.findViewById(R.id.description);


		selected_calender = getInstanse();

		soltArrayList = new ArrayList<>();

		parentPanel = (LinearLayout) rootView.findViewById(R.id.parentPanel);

		dropInScroll = (CustomScrollView) rootView.findViewById(R.id.dropInScroll);

		description.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.e("action" , event.getAction()+"");


				//	Toast.makeText(getActivity() ,"action  " + event.getAction(),Toast.LENGTH_SHORT ).show();




				if (1 == event.getAction())
				{
					dropInScroll.setEnableScrolling(true);

				}
				else
				{

					dropInScroll.setEnableScrolling(false);


				}

				return false;
			}
		});









		discription_textview_status = (TextView) rootView.findViewById(R.id.discription_textview_status);

		add_player_layout = (LinearLayout) rootView.findViewById(R.id.add_player_layout);
		if (DirectorFragmentManageActivity.actionbar_titletext != null) {
			DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.drop_ins));
		}

		if (DirectorFragmentManageActivity.backButton != null) {
			DirectorFragmentManageActivity.showBackButton();
			DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
		}
		initializeGroup(rootView);
		setOnClickListeners();
		getGroupList();
		showFormatSelctor();
		getSlotList();
		//ShowGroup();
		DirectorFragmentManageActivity.showLogoutButton();
		return rootView;
	}

	void initializeGroup(View view) {
		select_format_spinner = (Spinner) view.findViewById(R.id.select_format_spinner);
		select_player_qty = (Spinner) view.findViewById(R.id.select_player_qty);
		dateET = (TextView) view.findViewById(R.id.select_date);
		timeET = (Spinner) view.findViewById(R.id.select_time);
		addGroupET = (Spinner) view.findViewById(R.id.add_group);
		selectGroupET = (TextView) view.findViewById(R.id.select_group_et);
		selectGroupTv = (TextView) view.findViewById(R.id.select_group_tv);
		radioSelection = (RadioGroup) view.findViewById(R.id.radio_group);
		error_text = (TextView) view.findViewById(R.id.error_text);
		doneBtn = (Button) view.findViewById(R.id.done);
		add_group_relative = (RelativeLayout) view.findViewById(R.id.add_group_relative);

		description.addTextChangedListener(new AddTextChangeListener());

		radioSelection.setOnCheckedChangeListener(cheackChange);


		Calendar selected_date1 = getInstanse();
		dateET.setText(AppConstants.getAppDate(selected_date1));

		//dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);




		number_of_player_array = new String[sessionManager.getSportPlayerQty(getActivity())];

		for (int i =0 ; i < number_of_player_array.length ;i++ )
		{
			number_of_player_array[i]= (i+1)+"";
		}


		CustumSpinnerListAdapter adapter = new CustumSpinnerListAdapter(getActivity(), number_of_player_array ,select_player_qty);

		select_player_qty.setAdapter(adapter);



		select_player_qty.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				pleyerId = (position+1)+"";
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				pleyerId = 1+"";
			}
		});





	}

	void setOnClickListeners() {



		dateET.setOnClickListener(onClickS);
		//timeET.setOnClickListener(onClickS);
		selectGroupET.setOnClickListener(onClickS);
		selectGroupET.setOnClickListener(onClickS);
		doneBtn.setOnClickListener(onClickS);
	}

	OnClickListener onClickS = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {

				case R.id.select_player_qty:
					showPlayerSelctor();
					break;
				case R.id.select_date:
					showCalendar();
					break;
				case R.id.select_time:
					String date = dateET.getText().toString().trim();
					if(Utill.isStringNullOrBlank(date)){
						Utill.showToast(mContext, "Please Select Date First.");
						return;
					}
					if(AppConstants.getTimeSlots()==null||AppConstants.getTimeSlots().size()==0)
						getSlotList();
					else{
						showTimeSlots();
					}
					break;
				case R.id.done:
					try
					{
						validateData();
					}
					catch (Exception e)
					{

					}

					break;
				case R.id.select_group_et:
					AddGroupFragment.groupEdit = null;
//////

					AddGroupFragment addGroupFragment = new AddGroupFragment();

					Bundle bundle1 = new Bundle();
					bundle1.putSerializable("addGroupBackListener" , addGroupBackResponseListener);
					addGroupFragment.setArguments(bundle1);
					getActivity().getSupportFragmentManager()
							.beginTransaction()
							.add(R.id.content_frame ,
									addGroupFragment,"addGroupFragment" )
							.addToBackStack("addGroupFragment").commit();
					parentPanel.setVisibility(View.GONE);

					//DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.AddGroupFragment_id);

					break;
				case R.id.add_group:
					AddGroupFragment.groupEdit = null;

					addGroupFragment = new AddGroupFragment();

					bundle1 = new Bundle();
					bundle1.putSerializable("addGroupBackListener" , addGroupBackResponseListener);
					addGroupFragment.setArguments(bundle1);
					getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_frame ,addGroupFragment,"addGroupFragment" ).addToBackStack("addGroupFragment").commit();
					break;

				default:
					break;
			}
		}
	};

	void validateData(){


		Utill.hideKeybord(getActivity());
		String formateStr = select_format_spinner.getSelectedItem().toString().trim();
		String dateStr = dateET.getText().toString().trim();
		String timeStr = timeET.getSelectedItem().toString().trim();

		String groupIdStr = selectGroupET.getText().toString().trim();
		String whoReceivedStr = "";
		if(radioSelection.getCheckedRadioButtonId() == R.id.group_radio){
			whoReceivedStr = "2";
		}else{
			whoReceivedStr = "1";
		}
		if(groupList.size()==0){
			Utill.showToast(mContext, "Please Add a group first");
			return;
		}

		groupId = groupList.get((int)addGroupET.getSelectedItemId()).getGroup_id() ;




		/*if(Utill.isStringNullOrBlank(formateStr))
		{
			Utill.showToast(mContext, "Please Select Formate.");
		}else if(Utill.isStringNullOrBlank(dateStr)){
			Utill.showToast(mContext, "Please Select Date.");
		}
		else
			if(Utill.isStringNullOrBlank(timeStr))
		{
			Utill.showToast(mContext, "Please Select Time.");
		}
		else if(radioSelection.getCheckedRadioButtonId() == R.id.group_radio && Utill.isStringNullOrBlank(groupIdStr)){

		}
		else */if(Utill.isNetworkAvailable(getActivity())){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("user_id",SessionManager.getUser_id(mContext));
			params.put("user_club_id",SessionManager.getUser_Club_id(mContext));
			params.put("format","2");
			Calendar myDateCal =   AppConstants.getCalenderFromAppDate(dateStr);
			String myDate = myDateCal.get(Calendar.YEAR)+"-"+(myDateCal.get(Calendar.MONTH)+1)+"-"+myDateCal.get(Calendar.DATE);
			params.put("date",myDate);
			params.put("time",timeStr);
			params.put("who_recieve","2");
			params.put("group_id",groupId);
			params.put("dropin_number_player",select_player_qty.getSelectedItem());
			params.put("group_id",groupId);
			params.put("dropin_desc",description.getText().toString());
			Utill.showProgress(mContext);
			GlobalValues.getModelManagerObj(mContext).createDropIn(params, new ModelManagerListener() {

				@Override
				public void onSuccess(String json) {
					Utill.hideProgress();
					DirectorFragmentManageActivity.popBackStackFragment();
				}

				@Override
				public void onError(String msg) {
					Utill.hideProgress();
					ShowUserMessage.showDialogOnActivity(getActivity() , msg);

				}
			});
		}else{
			Utill.showNetworkError(mContext);
		}
	}


	ArrayList<GroupBean> groupList = new ArrayList<GroupBean>();
	private void getGroupList() {
		if (Utill.isNetworkAvailable(getActivity()))
		{
			Utill.showProgress(mContext);
			GlobalValues.getModelManagerObj(mContext).getGroupList(new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					groupList = JsonUtility.parseGroupList(json);
					AppConstants.setGroupList(groupList);
					if(groupList!=null && groupList.size()>0){
						/*
						*/

						error_text.setVisibility(View.VISIBLE);
						selectGroupET.setVisibility(View.GONE);
						selectGroupTv.setVisibility(View.GONE);
						ArrayList<String> groupL = new ArrayList<String>();
						for(int i=0;i<groupList.size();i++)
						{
							groupL.add(groupList.get(i).getGroup_name());
						}

						if (groupList.size() >=1)
						{
							addGroupET.setVisibility(View.VISIBLE);
							add_group_relative.setVisibility(View.VISIBLE);
							error_text.setVisibility(View.VISIBLE);
							selectGroupET.setVisibility(View.GONE);						}
						else
						{
							addGroupET.setVisibility(View.GONE);
							add_group_relative.setVisibility(View.GONE);
							error_text.setVisibility(View.GONE);
							selectGroupET.setVisibility(View.VISIBLE);
						}

						CustumSpinnerListAdapter adapter = new CustumSpinnerListAdapter(getActivity(), groupL ,addGroupET);

						//dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						addGroupET.setAdapter(adapter);

						addGroupET.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
								// TODO Auto-generated method stub


								groupId = groupList.get(pos).getGroup_id();
								//Toast.makeText(getActivity(), groupId, 1).show();

							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}
						});

					}else{
						addGroupET.setVisibility(View.GONE);
						add_group_relative.setVisibility(View.GONE);
						error_text.setVisibility(View.GONE);
						selectGroupET.setVisibility(View.VISIBLE);
					}
					Utill.hideProgress();
				}

				@Override
				public void onError(String msg) {
					ShowUserMessage.showUserMessage(mContext, msg);
					Utill.hideProgress();
				}
			});
		} else {
			Utill.showNetworkError(mContext);
		}
	}


	void showGroupSelector(){
		selectGroupET.setVisibility(View.VISIBLE);
		selectGroupTv.setVisibility(View.VISIBLE);
		//addGroupET.setVisibility(View.GONE);
	}
	void hideGroupSelector(){
		selectGroupET.setVisibility(View.GONE);
		selectGroupTv.setVisibility(View.GONE);
		//addGroupET.setVisibility(View.GONE);
	}

	OnCheckedChangeListener cheackChange = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if(checkedId == R.id.group_radio){
				if(groupList.size()==0){
					hideGroupSelector();
					//addGroupET.setVisibility(View.VISIBLE);
				}else{
					showGroupSelector();
				}
			}
		}
	};


	@Override
	public void onStart() {
		super.onStart();
		Log.e(Tag, "onStart");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(Tag, "onResume");
		select_player_qty.setSelection(0);
		timeET.setSelection(0);
		description.setText("");
		addGroupET.setSelection(0);
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

	//String formatSlectorArrya[] = { "Singles", "Doubles" };

	ArrayList<String>formatSlectorArrya = new ArrayList<String>();

	public void showFormatSelctor() {
		formatSlectorArrya.clear();
		formatSlectorArrya.add("Singles");


		formatSlectorArrya.add("Doubles");


		CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getActivity() ,formatSlectorArrya);



		//dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		select_format_spinner.setAdapter(customSpinnerAdapter);
		select_format_spinner.setSelection(1);
		add_player_layout.setVisibility(View.VISIBLE);
		//	select_format_spinner.setOnItemSelectedListener(new on);

		select_format_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				formatId = (position+1);



				if(position == 1)
				{
					add_player_layout.setVisibility(View.VISIBLE);
				}
				else
				{
					add_player_layout.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		select_format_spinner.setSelection(1);


			/*listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					*/
		//formatSlectorArrya


		/*final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.listview, null);
		ListView listView = (ListView) v.findViewById(R.id.list);
		listView.setAdapter(adapter);

				alertDialog.dismiss();
			}
		});
		alertDialog.setView(v);
		alertDialog.show();*/
	}
	String number_of_player_array[] = { "1", "2", "3" };
	public void showPlayerSelctor() {


		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.android_spinner_item, number_of_player_array);

		//dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		addGroupET.setAdapter(adapter);


		addGroupET.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				groupId = groupList.get(position).getGroup_id();
				//Toast.makeText(getActivity(), groupId, 1).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});



	}
	String groupId = "";
	int formatId = 0;
	String pleyerId = "1";
	public void ShowGroup() {
		//Toast.makeText(getActivity(), "ssss "+groupList.size(), 1).show();
		/*ArrayList<String> groupL = new ArrayList<String>();
		for(int i=0;i<groupList.size();i++){
			groupL.add(groupList.get(i).getGroup_name());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.android_spinner_item, groupL);

		//dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		addGroupET.setAdapter(adapter);*/

		/*final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.listview, null);
		ListView listView = (ListView) v.findViewById(R.id.list);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.simpletextview,groupL);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				groupId = groupList.get(position).getGroup_id();
				selectGroupET.setText(groupList.get(position).getGroup_name());
				alertDialog.dismiss();
			}
		});
		alertDialog.setView(v);
		alertDialog.show();*/
	}

	private String getTime(int hr, int min) {
		Time tme = new java.sql.Time(hr, min, 0);
		Format formatter;
		formatter = new SimpleDateFormat("h:mm a");
		return formatter.format(tme);
	}







	private void getSlotList() {

		Utill.showProgress(mContext);
		GlobalValues.getModelManagerObj(mContext).getSlotList(new ModelManagerListener() {
			@Override
			public void onSuccess(String json) {

				soltArrayList = JsonUtility.parserSlotList(json ) ;


				showTimeSlots();
				getGroupList();
				//Utill.hideProgress();
			}

			@Override
			public void onError(String msg) {
				ShowUserMessage.showUserMessage(mContext, "" + msg);
				Utill.hideProgress();
			}
		});
	}

	public void showTimeSlots() {


		currentTimes = getInstanse();


		ArrayList<String>	soltArrayList = new ArrayList<>() ;


		currentTimes.add(Calendar.HOUR_OF_DAY ,1);
		for (int i = 0; i < this.soltArrayList.size(); i++) {

			Calendar slotDate = getInstanse();







			SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

			try {
				slotDate.setTime(dateFormat.parse(this.soltArrayList.get(i)));
			} catch (ParseException e) {
				e.printStackTrace();



			}

			Calendar systemDate = getInstanse();
			slotDate.set(Calendar.DATE , systemDate.get(Calendar.DATE));
			slotDate.set(Calendar.MONTH , systemDate.get(Calendar.MONTH));
			slotDate.set(Calendar.YEAR , systemDate.get(Calendar.YEAR));

			//Log.e("Hour" , slotDate.get(Calendar.HOUR_OF_DAY)+" "+currentTime.get(Calendar.HOUR_OF_DAY));
			//Log.e("Minut" , slotDate.get(Calendar.MINUTE)+" "+currentTime.get(Calendar.MINUTE));
			//Log.e("Spce" , "--------");

			boolean isSelected = Utill.compareTwoTime(slotDate , currentTimes);
			Log.e("first date" , isSelected+"") ;


			if (Utill.isEqualDate(currentTimes , selected_calender))
			{
				if(isSelected)
				{
					soltArrayList.add(this.soltArrayList.get(i));

				}
			}
			else
			{
				soltArrayList.add(this.soltArrayList.get(i));

			}




		}



		if (soltArrayList.size() == 0)
		{
			currentTimes.add(Calendar.DATE ,1);
			dateET.setText(AppConstants.getAppDate(currentTimes));
			TimeSpinnerAdapter adapter = new TimeSpinnerAdapter(getActivity(),this.soltArrayList ,timeET);
			timeET.setAdapter(adapter);
		}
		else
		{
			TimeSpinnerAdapter adapter = new TimeSpinnerAdapter(getActivity(),soltArrayList ,timeET);
			timeET.setAdapter(adapter);

//					AppConstants.setTimeSlots(list);
		}


		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.android_spinner_item, R.layout.simpletextview,AppConstants.getTimeSlots());


		/*
		//dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	*/





		/*final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.listview, null);
		ListView listView = (ListView) v.findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.simpletextview,AppConstants.getTimeSlots());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				timeET.setText(AppConstants.getTimeSlots().get(position));
				alertDialog.dismiss();
			}
		});
		alertDialog.setView(v);
		alertDialog.show();*/
	}


	private static final String tag = "MyCalendarActivity";

	private TextView currentMonth;
	private Button selectedDayMonthYearButton;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private GridCellAdapter adapter;
	private Calendar _calendar;
	private int month, year;
	private final DateFormat dateFormatter = new DateFormat();
	private static final String dateTemplate = "MMMM yyyy";

	AlertDialog calendarDialogue;
	// ArrayList<String>
	HashSet<String> selectedDateList;

	public void showCalendar() {
		selectedDateList = new HashSet<String>();

		calendarDialogue = new AlertDialog.Builder(mContext).create();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.my_calendar_view, null);
		initializeCalendar(v );
		calendarDialogue.setView(v);
		calendarDialogue.show();
	}

	void initializeCalendar(View v ) {
		this._calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);
		Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: " + year);

		selectedDayMonthYearButton = (Button) v.findViewById(R.id.selectedDayMonthYear);
		selectedDayMonthYearButton.setText("Selected: ");
		TextView cancelTV = (TextView) v.findViewById(R.id.cancel);
		cancelTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				calendarDialogue.dismiss();
			}
		});
		prevMonth = (ImageView) v.findViewById(R.id.prevMonth);

		prevMonth.setOnClickListener(this);

		currentMonth = (TextView) v.findViewById(R.id.currentMonth);
		currentMonth.setText(DateFormat.format(dateTemplate, _calendar.getTime()));

		nextMonth = (ImageView) v.findViewById(R.id.nextMonth);
		nextMonth.setOnClickListener(this);

		calendarView = (GridView) v.findViewById(R.id.calendar);

		// Initialised
		adapter = new GridCellAdapter(mContext, R.id.calendar_day_gridcell, month, year);
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
		/*
		 * if(CalendarType == RECURRENT_DATE){ TextView cancel = (TextView)
		 * v.findViewById(R.id.cancel); TextView done = (TextView)
		 * v.findViewById(R.id.done); done.setVisibility(View.VISIBLE);
		 * cancel.setOnClickListener(new OnClickListener() {
		 *
		 * @Override public void onClick(View v) { selectedDateList = new
		 * HashSet<String>(); calendarDialogue.dismiss(); } });
		 *
		 * done.setOnClickListener(new OnClickListener() {
		 *
		 * @Override public void onClick(View v) { String msg = ""; for(int
		 * i=0;i<selectedDateList.size();i++){ msg = msg +
		 * selectedDateList.toString(); } Utill.showToast(mContext,
		 * ""+selectedDateList.toString()); calendarDialogue.dismiss();
		 * dateCountTV.setText("You have selected "+selectedDateList.size()+
		 * " days to repeat the trip."); } }); }
		 */
	}

	// Inner Class
	public class GridCellAdapter extends BaseAdapter implements OnClickListener {
		private static final String tag = "GridCellAdapter";
		private final Context _context;

		private final List<String> list;
		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[] { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
		private final String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
				"November", "December" };
		private final int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		private int daysInMonth;
		private int currentDayOfMonth;
		private int currentWeekDay;
		private Button gridcell;
		private TextView num_events_per_day;
		private final HashMap<String, Integer> eventsPerMonthMap;
		private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

		// Days in Current Month
		public GridCellAdapter(Context context, int textViewResourceId, int month, int year) {
			super();
			this._context = context;
			this.list = new ArrayList<String>();
			Log.d(tag, "==> Passed in Date FOR Month: " + month + " " + "Year: " + year);
			Calendar calendar = getInstanse();
			setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
			setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
			Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
			Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
			Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

			// Print Month
			printMonth(month, year);

			// Find Number of Events
			eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
		}

		private String getMonthAsString(int i) {
			return months[i];
		}

		private String getWeekDayAsString(int i) {
			return weekdays[i];
		}

		private int getNumberOfDaysOfMonth(int i) {
			return daysOfMonth[i];
		}

		public String getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		/**
		 * Prints Month
		 *
		 * @param mm
		 * @param yy
		 */
		private void printMonth(int mm, int yy) {
			Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
			int trailingSpaces = 0;
			int daysInPrevMonth = 0;
			int prevMonth = 0;
			int prevYear = 0;
			int nextMonth = 0;
			int nextYear = 0;

			int currentMonth = mm - 1;
			String currentMonthName = getMonthAsString(currentMonth);
			daysInMonth = getNumberOfDaysOfMonth(currentMonth);

			Log.d(tag, "Current Month: " + " " + currentMonthName + " having " + daysInMonth + " days.");

			GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);



			Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

			if (currentMonth == 11) {
				prevMonth = currentMonth - 1;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 0;
				prevYear = yy;
				nextYear = yy + 1;
				Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
			} else if (currentMonth == 0) {
				prevMonth = 11;
				prevYear = yy - 1;
				nextYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				nextMonth = 1;
				Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
			} else {
				prevMonth = currentMonth - 1;
				nextMonth = currentMonth + 1;
				nextYear = yy;
				prevYear = yy;
				daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
				Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
			}

			int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
			trailingSpaces = currentWeekDay;

			Log.d(tag, "Week Day:" + currentWeekDay + " is " + getWeekDayAsString(currentWeekDay));
			Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
			Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

			if (cal.isLeapYear(cal.get(Calendar.YEAR)))
				if (mm == 2)
					++daysInMonth;
				else if (mm == 3)
					++daysInPrevMonth;

			// Trailing Month days
			for (int i = 0; i < trailingSpaces; i++) {
				Log.d(tag,
						"PREV MONTH:= " + prevMonth + " => " + getMonthAsString(prevMonth) + " "
								+ String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
				list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-"
						+ prevYear);
			}

			// Current Month Days
			for (int i = 1; i <= daysInMonth; i++) {
				Log.d(currentMonthName, String.valueOf(i) + " " + getMonthAsString(currentMonth) + " " + yy);
				if (i == getCurrentDayOfMonth()) {
					list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
				} else {
					list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
				}
			}

			// Leading Month days
			for (int i = 0; i < list.size() % 7; i++) {
				Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
				list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
			}
		}

		/**
		 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
		 * ALL entries from a SQLite database for that month. Iterate over the
		 * List of All entries, and get the dateCreated, which is converted into
		 * day.
		 *
		 * @param year
		 * @param month
		 * @return
		 */
		private HashMap<String, Integer> findNumberOfEventsPerMonth(int year, int month) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();

			return map;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.screen_gridcell, parent, false);
			}

			// Get a reference to the Day gridcell
			gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
			gridcell.setOnClickListener(this);

			// ACCOUNT FOR SPACING

			Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
			String[] day_color = list.get(position).split("-");
			String theday = day_color[0];
			String themonth = day_color[2];
			String theyear = day_color[3];
			//	Log.e("data" ,day_color[0] +"   "+day_color[1]+" "+day_color[2]+" "+day_color[]);

			String appDate = theday + "-" + themonth + "-" + theyear;

			if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
				if (eventsPerMonthMap.containsKey(theday)) {
					num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
					Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
					num_events_per_day.setText(numEvents.toString());
				}
			}

			// Set the Day GridCell
			gridcell.setText(theday);
			gridcell.setBackground(getResources().getDrawable(R.drawable.calendar_tile_small));
			gridcell.setTag(theday + "-" + themonth + "-" + theyear);
			Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear);

			if (day_color[1].equals("GREY")) {
				gridcell.setTextColor(getResources().getColor(R.color.gray_color));

			}

			String currentDate = day_color[0]+"-"+day_color[2]+"-"+day_color[3];
			SimpleDateFormat currentDateformat = new SimpleDateFormat("d-MMMM-yyyy");
			String systemCurrentDate = currentDateformat.format(getInstanse().getTime());

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-MMMM-yyyy");
			String	mySelectedDate = simpleDateFormat.format(selected_calender.getTime());

			if (day_color[1].equals("WHITE"))
			{

				if(selected_calender != null)
				{
					//String dateFormate = simpleDateFormat.format(date);
					Log.e("current date" ,mySelectedDate+"  "+ appDate);

					mySelectedDate = currentDateformat.format(selected_calender.getTime());

					if (mySelectedDate.equals(appDate))
					{

						gridcell.setBackground(getResources().getDrawable(R.color.blue_header));
					}
					else
					{
						gridcell.setTextColor(getResources().getColor(R.color.black_color));
					}
				}
				else
				{
					gridcell.setTextColor(getResources().getColor(R.color.black_color));
				}


			}

			if (day_color[1].equals("BLUE") )
			{
				gridcell.setTextColor(getResources().getColor(R.color.white_color));


				if (mySelectedDate.equals(currentDate) == false)
				{
					gridcell.setBackground(getResources().getDrawable(R.drawable.calendar_bg_orange));
				}
				else
				{
					gridcell.setBackground(getResources().getDrawable(R.color.blue_header));
					//  gridcell.setBackground(getResources().getDrawable(R.drawable.calendar_bg_orange));
				}

			}

			return row;
		}
		//String calendarDate = "";
		@Override
		public void onClick(View view) {























			String date_month_year = (String) view.getTag();

			selectedDayMonthYearButton.setText("Selected: " + date_month_year);
			Log.e("Selected date", date_month_year);

			try {
				Date parsedDate = dateFormatter.parse(date_month_year);

				// parsedDate.
				Calendar c = getInstanse();

				System.out.println("Current time => " + c.getTime());

				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				String formattedDate = df.format(c.getTime());

				c.setTime(parsedDate);

				int date = c.get(Calendar.DATE);
				int month = c.get(Calendar.MONTH) + 1;
				int year = c.get(Calendar.YEAR);

				Calendar current_date = getInstanse();

				Calendar selected_date = getInstanse();
				Calendar seven_day_after = getInstanse();
				selected_date.set(year, month-1, date);
				seven_day_after.set(current_date.get(Calendar.YEAR), current_date.get(Calendar.MONTH), current_date.get(Calendar.DAY_OF_MONTH)+7);

				//	seven_day_after.add(Calendar.DAY_OF_MONTH, 7);

				String s = selected_date.getTime()+"  "+seven_day_after.getTime();

				//	Toast.makeText(getActivity(), s+" ssssssss "+selected_date.after(seven_day_after), 1).show();




				String dateStr = "" + date;
				String monthStr = "" + month;
				if (dateStr.length() == 1) {
					dateStr = "0" + dateStr;
				}
				if (monthStr.length() == 1) {
					monthStr = "0" + monthStr;
				}

				String finalDate = dateStr + "-" + monthStr + "-" + year;
				String selectedDate = dateStr + "/" + monthStr + "/" + year;
				// ShowUserMessage.showUserMessage(mContext,selectedDate +
				// formattedDate);

				if (!SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER) == false && getDateFromString(selectedDate).before(getDateFromString(formattedDate)))
				{

					boolean status = getDateFromString(selectedDate).equals(getDateFromString(formattedDate));


					if(!selectedDate.equals(formattedDate))
					{
						//Toast.makeText(_context, status+"", 1).show();

						return;
					}

					//ShowUserMessage.showUserMessage(mContext, "Date passed,Select another.");


				}


				dateET.setText(AppConstants.getAppDate(selected_date)  );
//				dateET.setText(Utill.formattedDateFromString("", "", finalDate));
				finalDate = year + "-" + monthStr + "-" + dateStr;


				Calendar selectedDtae = getInstanse();
				Calendar todayDtae = getInstanse();
				//Toast.makeText(getActivity() ,"d  "+currentTime.get(Calendar.DATE),1).show();

				todayDtae.set(Calendar.DATE ,currentTimes.get(Calendar.DATE));
				try
				{
					selected_calender.set(year,Integer.parseInt(monthStr)-1 ,Integer.parseInt(dateStr));

					selectedDtae.set(year,Integer.parseInt(monthStr)-1 ,Integer.parseInt(dateStr));
				}
				catch(Exception e)
				{

				}
				//currentTime.setTime(parsedDate);

				showTimeSlots();

				if(todayDtae.compareTo(selectedDtae)<=0)
				{
					if (calendarDialogue != null) {
						calendarDialogue.dismiss();
					}
					dateET.setText(AppConstants.getAppDate(selected_date));
				}
				else
				{
					//Toast.makeText(getActivity(), "select a date that is greater than current date", Toast.LENGTH_LONG).show();
				}
				//calendarDate = finalDate;


			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		public int getCurrentDayOfMonth() {
			return currentDayOfMonth;
		}

		private void setCurrentDayOfMonth(int currentDayOfMonth) {
			this.currentDayOfMonth = currentDayOfMonth;
		}

		public void setCurrentWeekDay(int currentWeekDay) {
			this.currentWeekDay = currentWeekDay;
		}

		public int getCurrentWeekDay() {
			return currentWeekDay;
		}
	}

	@Override
	public void onClick(View v) {
		if (v == prevMonth) {
			if (month <= 1) {
				month = 12;
				year--;
			} else {
				month--;
			}
			Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}
		if (v == nextMonth) {
			if (month > 11) {
				month = 1;
				year++;
			} else {
				month++;
			}
			Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}

	}

	private void setGridCellAdapterToDate(int month, int year) {
		adapter = new GridCellAdapter(mContext, R.id.calendar_day_gridcell, month, year);
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth.setText(DateFormat.format(dateTemplate, _calendar.getTime()));
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	Date getDateFromString(String dateStr) {
		// 2015-04-03 03:11 PM
		Calendar cal = getInstanse();
		int year = 0, monthOfYear = 0, dayOfMonth = 0;
		String d[] = dateStr.split("/");

		year = Integer.parseInt(d[2]);
		monthOfYear = Integer.parseInt(d[1]) - 1;
		dayOfMonth = Integer.parseInt(d[0]);

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, monthOfYear);
		cal.set(Calendar.DATE, dayOfMonth);
		Date dati = cal.getTime();
		Log.e("Date", dati.toString());
		return cal.getTime();
	}


	public  class AddTextChangeListener implements TextWatcher
	{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s)
		{
			discription_textview_status.setText(description.getText().toString().length()+"/250");
		}
	};


	public FragmentBackResponseListener addGroupBackResponseListener = new FragmentBackResponseListener() {
		@Override
		public void UpdateView() {
			super.UpdateView();

			getGroupList();
			parentPanel.setVisibility(View.VISIBLE);

			if (DirectorFragmentManageActivity.actionbar_titletext != null)
			{
				DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.drop_ins));
			}

			if (DirectorFragmentManageActivity.backButton != null) {
				DirectorFragmentManageActivity.showBackButton();
				DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
			}

		}
	};


	public  Calendar getInstanse()
	{

		Calendar calendar = Calendar.getInstance(Locale.ENGLISH);

		try
		{

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

			calendar.setTime(simpleDateFormat.parse(sessionManager.getCurrentTime()) );


		}
		catch (Exception e)
		{

		}


		return calendar ;

	}

}
