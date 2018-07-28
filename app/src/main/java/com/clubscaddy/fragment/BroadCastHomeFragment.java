package com.clubscaddy.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.clubscaddy.Adapter.CustomSpinnerAdapter;
import com.clubscaddy.Adapter.CustumSpinnerListAdapter;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.FragmentBackResponseListener;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.BroadCastAdapter;
import com.clubscaddy.Bean.BroadcastPollingBean;
import com.clubscaddy.Bean.GroupBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.R.color;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.custumview.CustomScrollView;


public class BroadCastHomeFragment extends Fragment {
	public static final int CREATE_BROADCAST_SCREEN = 1;
	public static final int MY_BROADCAST_SCREEN = 2;
	public static int CURRENT_SCREEN;
	//LinearLayout myBroadcastLL, createBroadcastLL;
	TextView myBroadcastText, myBroadCastLine, createBroadCastText, createBroadCastLine;
	RelativeLayout left_frame;
	String Tag = getClass().getName();
	public static FragmentManager mFragmentManager;
	public static Fragment mFragment;
	Context mContext;
	ImageButton add_btn;
	ListView broadcastPollingLV;
	RelativeLayout spinner_layout;
TextView polling_heading_id;
Button add_group_tv;
RadioButton all_group ,select_group ,broadcast;
	// create broadcast Views
	CustomScrollView createBroadCastView;
	RadioGroup broadCastPollingRG, allOrGroupRG;
	EditText msg;
	TextView discription_textview_status;
	ProgressDialog pd;
	TextView selectGroupTitle;
	Spinner selectGroupSpinnr ;
	ImageButton doneBtn;
AQuery aQuery1;


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

	String status ;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e(Tag, "onCreateView");
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.broadcast_polling, container, false);


		Bundle bundle = getArguments();
		status = bundle.getString("status");

		discription_textview_status = (TextView) rootView.findViewById(R.id.discription_textview_status);

		mContext = getActivity();
		aQuery1 = new AQuery(getActivity());
		polling_heading_id = (TextView) rootView.findViewById(R.id.polling_heading_id);
		
		all_group           = (RadioButton) rootView.findViewById(R.id.all);
		select_group =(RadioButton) rootView.findViewById(R.id.group);
		broadcast =(RadioButton) rootView.findViewById(R.id.broadcast);
		//group
		
		if (DirectorFragmentManageActivity.actionbar_titletext != null) {
			DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.broadcast_poling));
		}

		if (DirectorFragmentManageActivity.backButton != null) {
			DirectorFragmentManageActivity.showBackButton();
			DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
		}
		
		initializeView(rootView);
		setOnClickListener();
		/*if (CURRENT_SCREEN == CREATE_BROADCAST_SCREEN) {
			createBroadcastLL.performClick();
		} else {
			myBroadcastLL.performClick();
		}*/
		DirectorFragmentManageActivity.hideLogout();
		DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.VISIBLE);
		DirectorFragmentManageActivity.delete_all_btn.setBackgroundResource(R.drawable.delete_all_layout);
		
		DirectorFragmentManageActivity.delete_all_btn.setText("Delete All");
		
		
		DirectorFragmentManageActivity.delete_all_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

				// Setting Dialog Title
				alertDialog.setTitle(SessionManager.getClubName(getActivity()));

				// Setting Dialog Message
				alertDialog.setMessage("Are you sure you want to delete all communication?");

				// Setting Icon to Dialog
			
//sendReply(bean, status);
				// Setting Positive "Yes" Button
				alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {
						
						//bean.getNotifications_id()
						pd = new ProgressDialog(getActivity());
						pd.setCancelable(false);
						pd.show();
						//sendReply(bean, "1");
						Map<String, String>params= new HashMap<String, String>();
						params.put("broadcast_user_id", SessionManager.getUser_id(mContext));
						aQuery1.ajax(WebService.deleteallbcast, params, JSONObject.class, new AjaxCallback<JSONObject>() {
							@Override
							public void callback(String url, JSONObject object, AjaxStatus status) {
								
								//broadcast_user_id
								Log.e("msg", object+"");
								pd.dismiss();
								
								int code = status.getCode();
							//	Toast.makeText(getActivity(),object+ " user id "+SessionManager.getUser_id(mContext), 1).show();
								
								
								if (code == AjaxStatus.NETWORK_ERROR) 
								{
									
								Toast.makeText(getActivity(), "NetWork earror!", Toast.LENGTH_LONG).show();
									return;
								} 
								else 
								if(code == AjaxStatus.TRANSFORM_ERROR) 
								{
									//return;
								} 
								else 
								if(code == 200)
								{
								try
								{
								//	Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
									
									//
								if(Boolean.parseBoolean( object.getString("status"))==true)
								{
								//getActivity().getSupportFragmentManager().popBackStack();
									broadcastList.clear();
									ShowUserMessage.showUserMessage(getActivity() ,object.getString("message") );

									broadCastAdapter.notifyDataSetChanged();
								}
									
								}
								catch(Exception e)
								{
									
								}
								}

							}
						});
					// Write your code here to invoke YES event
							}
				});

				// Setting Negative "NO" Button
				alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,	int which) {
					// Write your code here to invoke NO event
						dialog.cancel();
					}
				});

				// Showing Alert Message
				alertDialog.show();
			}
		});
	//	getGroupList();
		return rootView;
	}

	int totalcharcterInMsg = 250 ;

	

	void initializeView(View view) {
		spinner_layout = (RelativeLayout) view.findViewById(R.id.spinner_layout);
		add_btn = (ImageButton) view.findViewById(R.id.add_btn);
		myBroadcastText = (TextView) view.findViewById(R.id.dropin_text);
		myBroadCastLine = (TextView) view.findViewById(R.id.dropin_line);
		createBroadCastText = (TextView) view.findViewById(R.id.groups_text);
		createBroadCastLine = (TextView) view.findViewById(R.id.groups_line);
		broadcastPollingLV = (ListView) view.findViewById(R.id.list);
		left_frame  =(RelativeLayout) view.findViewById(R.id.left_frame);
		// create broadcast view
		createBroadCastView = (CustomScrollView) view.findViewById(R.id.create_broadcast);
		broadCastPollingRG = (RadioGroup) view.findViewById(R.id.broadcast_polling_rg);
		allOrGroupRG = (RadioGroup) view.findViewById(R.id.send_to_rg);
		msg = (EditText) view.findViewById(R.id.msg);
		discription_textview_status.setText("0/250");
		msg.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s)
			{
				discription_textview_status.setText(s.toString().length()+"/"+totalcharcterInMsg);
			}
		});



		msg.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.e("action", event.getAction() + "");


				//	Toast.makeText(getActivity() ,"action  " + event.getAction(),Toast.LENGTH_SHORT ).show();


				if (1 == event.getAction()) {
					createBroadCastView.setEnableScrolling(true);

				} else {

					createBroadCastView.setEnableScrolling(false);


				}

				return false;
			}
		});


		selectGroupTitle = (TextView) view.findViewById(R.id.select_group_title);
		selectGroupSpinnr = (Spinner) view.findViewById(R.id.select_group_tv);
		doneBtn = (ImageButton) view.findViewById(R.id.done);
		
		add_group_tv = (Button) view.findViewById(R.id.add_group_tv);
		
		add_group_tv.setOnClickListener(viewOnClicks);
		
		
		if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER) == false ){
			allOrGroupRG.setVisibility(View.VISIBLE);
			allOrGroupRG.check(R.id.all);
		}else{
			showSelectGroup();
			allOrGroupRG.setVisibility(View.GONE);
		}
		getBroadCastList();
	}

	void setOnClickListener() {
		all_group.setOnClickListener(viewOnClicks);
		select_group.setOnClickListener(viewOnClicks);
		add_btn.setOnClickListener(viewOnClicks);
		doneBtn.setOnClickListener(viewOnClicks);
		broadCastPollingRG.setOnCheckedChangeListener(checkChange);

		broadcastPollingLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				BroadcastPollingBean bean = (BroadcastPollingBean) arg0.getItemAtPosition(arg2);
				BroadCastDetailFrageMent.broadCastId = bean.getBroadcast_id();
				DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.BroadcastDetail_id);
				/*if(bean.getBroadcast_type().equalsIgnoreCase(AppConstants.BROADCAST)){
					
						
				}else if(bean.getBroadcast_type().equalsIgnoreCase(AppConstants.POLLING)){
					DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.PollingDetail_id);
				}*/
			}
		});


		if (status.equals("2"))
		{
			try
			{
				AppConstants.hideSoftKeyboard(getActivity());
			}
			catch(Exception e)
			{

			}
			createBroadCastView.setVisibility(View.VISIBLE);
			left_frame.setVisibility(View.GONE);
			broadcast.setChecked(true);
			all_group.setChecked(false);
			select_group.setChecked(true);
			getGroupList();
		}



	}

	OnClickListener viewOnClicks = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.add_btn:

				BroadCastHomeFragment broadCastHomeFragment = new BroadCastHomeFragment();
				Bundle bundle = new Bundle();
				bundle.putString("status" , "2");
				broadCastHomeFragment.setArguments(bundle);
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, broadCastHomeFragment, "BroadcastPolling_tag").addToBackStack(null).commit();


				break;
			case R.id.dropin_layout:
				CURRENT_SCREEN = MY_BROADCAST_SCREEN;
				
				DirectorFragmentManageActivity.hideLogout();
				DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.VISIBLE);
				DirectorFragmentManageActivity.delete_all_btn.setBackgroundResource(R.drawable.delete_all_layout);
				
				DirectorFragmentManageActivity.delete_all_btn.setText("Delete All");
				myBroadCastLine.setVisibility(View.VISIBLE);
				createBroadCastLine.setVisibility(View.GONE);
				myBroadcastText.setTextColor(mContext.getResources().getColor(color.black_color));
				createBroadCastText.setTextColor(mContext.getResources().getColor(color.black_color));
				createBroadCastView.setVisibility(View.GONE);
				getBroadCastList();
				break;
			case R.id.done:
				validateData();
				break;
			case R.id.select_group_tv:
				if(groupList.size()==0)
					getGroupList();
				else{
					ShowGroup();	
				}
				
			break;
			case R.id.add_group_tv:
				try
				{
					AddGroupFragment.groupEdit.getMembersList().clear();;
					AddGroupFragment.groupEdit = null ;
				}
				catch(Exception e)
				{
					
				}
				
				//DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.AddGroupFragment_id);

				AddGroupFragment addGroupFragment = new AddGroupFragment();
				Bundle bundle1 = new Bundle();
				bundle1.putSerializable("addGroupBackListener" , addGroupBackResponseListener);
				addGroupFragment.setArguments(bundle1);

				getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_frame ,addGroupFragment,"addGroupFragment" ).addToBackStack("addGroupFragment").commit();


				break;
				case R.id.all :
					
					
					add_group_tv.setVisibility(View.GONE);
					spinner_layout.setVisibility(View.GONE);
					/*all_group.setChecked(false);
					select_group.setChecked(true);*/

					break;
				case R.id.group :
					/*select_group.setChecked(false);
					all_group.setChecked(true);*/
					if(spinner_list.size() == 0)
					{
						add_group_tv.setVisibility(View.VISIBLE);
						spinner_layout.setVisibility(View.GONE);
					}
					else
					{
						add_group_tv.setVisibility(View.GONE);
						spinner_layout.setVisibility(View.VISIBLE);	
					}
					break;
				
			}
		}
	};
	void resetView(){
		groupId = "";
		
		msg.setText("");
	}
	
	public void ShowGroup() {
		final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.listview, null);
		ListView listView = (ListView) v.findViewById(R.id.list);
		ArrayList<String> groupL = new ArrayList<String>();
		for(int i=0;i<groupList.size();i++){
			groupL.add(groupList.get(i).getGroup_name());
		}
  Collections.reverse(groupL);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.simpletextview,groupL);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				groupId = groupList.get(position).getGroup_id();
				
				alertDialog.dismiss();
			}
		});
		alertDialog.setView(v);
		alertDialog.show();
	}
	ArrayList<String>spinner_list = new ArrayList<String>();
	ArrayList<GroupBean> groupList = new ArrayList<GroupBean>();
	private void getGroupList() {
		spinner_list.clear();
		if (Utill.isNetworkAvailable(getActivity())) {
			Utill.showProgress(mContext);
			GlobalValues.getModelManagerObj(mContext).getGroupList(new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					groupList = JsonUtility.parseGroupList(json);
					
					DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.GONE);
					
					
					
					CURRENT_SCREEN = CREATE_BROADCAST_SCREEN;
					DirectorFragmentManageActivity.showLogoutButton();;
					
					left_frame.setVisibility(View.GONE);
					
				//	myBroadCastLine.setVisibility(View.GONE);
					//createBroadCastLine.setVisibility(View.VISIBLE);
					//myBroadcastText.setTextColor(mContext.getResources().getColor(color.black_color));
					//createBroadCastText.setTextColor(mContext.getResources().getColor(color.black_color));
					//broadcastPollingLV.setVisibility(View.GONE);
					//;
					createBroadCastView.setVisibility(View.VISIBLE);
					
					
					
					for(int i= 0 ; i < groupList.size();i++)
					{
						spinner_list.add(groupList.get(i).getGroup_name());
					}
					
					
					if(spinner_list.size() == 0)
					{
						add_group_tv.setVisibility(View.VISIBLE);
						spinner_layout.setVisibility(View.GONE);
					}
					else
					{
						add_group_tv.setVisibility(View.GONE);
						spinner_layout.setVisibility(View.VISIBLE);
					}


					CustumSpinnerListAdapter adapter = new CustumSpinnerListAdapter(getActivity() ,spinner_list ,selectGroupSpinnr );


					selectGroupSpinnr.setAdapter(adapter);
					
					
					selectGroupSpinnr.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
							// TODO Auto-generated method stub
							groupId = groupList.get(pos).getGroup_id();
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
							
						}
					});
					left_frame.setVisibility(View.GONE);
				//	resetView();
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
	
	BroadCastAdapter broadCastAdapter ; 
	ArrayList<BroadcastPollingBean> broadcastList = new ArrayList<BroadcastPollingBean>();
	void getBroadCastList(){
		if(Utill.isNetworkAvailable(getActivity())){
			Utill.showProgress(mContext);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("broadcast_clubid",SessionManager.getUser_Club_id(mContext));
			params.put("broadcast_userid",SessionManager.getUser_id(mContext));
			GlobalValues.getModelManagerObj(mContext).getBroadCastList(params, new ModelManagerListener() {
				
				@Override
				public void onSuccess(String json) {
					broadcastPollingLV.setVisibility(View.VISIBLE);
					broadcastList = JsonUtility.parseBroadCastListDetail(json);
					//Collections.reverse(broadcastList);
					broadCastAdapter = new BroadCastAdapter(mContext, broadcastList);
					broadcastPollingLV.setAdapter(broadCastAdapter);
					Utill.hideProgress();
				}
				
				@Override
				public void onError(String msg) {
					Utill.hideProgress();
					
				}
			});
		}else{
			Utill.showNetworkError(mContext);
		}
	}

	String groupId = "";

	void validateData() {
		try
		{
			AppConstants.hideSoftKeyboard(getActivity());
		}
		catch(Exception e)
		{
			
		}
		String msgStr = msg.getText().toString().trim();
		if (Utill.isStringNullOrBlank(msgStr)) {
			Utill.showToast(mContext, "Please Enter some Message.");
		} else if (allOrGroupRG.getCheckedRadioButtonId() == R.id.group && Utill.isStringNullOrBlank(groupId)) {
			Utill.showToast(mContext, "Please Select A Group.");
		}else if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER) && Utill.isStringNullOrBlank(groupId)){
			Utill.showToast(mContext, "Please Select A Group.");
		}
		else if (Utill.isNetworkAvailable(getActivity())) {
			Map<String, Object> params = new HashMap<String, Object>();

			params.put("user_id",SessionManager.getUser_id(mContext));
			params.put("user_club_id",SessionManager.getUser_Club_id(mContext));
			params.put("broadcast_msg", msg.getText().toString());
			//broadcast_msg
			String typeStr = "";
			if(broadCastPollingRG.getCheckedRadioButtonId() == R.id.broadcast)
			{
				typeStr = "1";

			}
			else
			{
				if(broadCastPollingRG.getCheckedRadioButtonId() == R.id.polling)
				{
					typeStr = "2";

				}
				else
				{
					typeStr = "3";

				}
					
				
			}
			params.put("type",typeStr);
			String whoRecieve = "2";
			if(allOrGroupRG.getVisibility()==View.VISIBLE && allOrGroupRG.getCheckedRadioButtonId() == R.id.all){
				whoRecieve = "1";
			}else {
				whoRecieve = "2";
			}
			params.put("who_recieve",whoRecieve);
			params.put("group_id",groupId);
			params.put("broadcast_msg",msgStr);
			
			
			Utill.showProgress(mContext);
			GlobalValues.getModelManagerObj(mContext).createBroadCast(params, new ModelManagerListener() {
				
				@Override
				public void onSuccess(String json) {
					Utill.hideProgress();
					//myBroadcastLL.performClick();
					try
					{
					JSONObject jsonObj = new JSONObject(json);	
					showDialg(jsonObj.getString("message"), mContext);
					}
					catch(Exception e)
					{
						
					}
					//(msg, mContext);
					//showDialg(msg, mContext);
				}
				
				@Override
				public void onError(String msg) {
					showDialg(msg, mContext);
					Utill.hideProgress();
					
				}
			});
		} else {
			Utill.showNetworkError(mContext);
		}
	}
/////
	OnCheckedChangeListener checkChange = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			int id = group.getId();
			if (id == R.id.broadcast_polling_rg)
			{
				if (group.getCheckedRadioButtonId() == R.id.broadcast) 
				{

					totalcharcterInMsg = 250 ;
					polling_heading_id.setText("Please type a message that needs to broadcasted among your group");

					if (msg.getText().length() >250)
					{
						msg.setText(msg.getText().toString().substring(0 , 250));
					}

				}
				else 
					if (group.getCheckedRadioButtonId() == R.id.polling) 
				{
					totalcharcterInMsg = 250 ;
					if (msg.getText().length() >250)
					{
						msg.setText(msg.getText().toString().substring(0 , 250));
					}
					polling_heading_id.setText("Please type a question you want to ask your group that has an YES / NO answer");
				}
					else
					{
						if (group.getCheckedRadioButtonId() == R.id.send_email) 
						{
							totalcharcterInMsg = 1000 ;
							polling_heading_id.setText("Please type a message that needs to email among your group");
						}	
					}



				InputFilter[] fArray = new InputFilter[1];
				fArray[0] = new InputFilter.LengthFilter(totalcharcterInMsg);
				msg.setFilters(fArray);




				discription_textview_status.setText(msg.getText().toString().length()+"/"+totalcharcterInMsg);

			} 
			else 
				if (id == R.id.send_to_rg) 
				{
				if (group.getCheckedRadioButtonId() == R.id.all) {
					hideSelectGroup();
				} else if (group.getCheckedRadioButtonId() == R.id.group) {
					showSelectGroup();
				}
			}

		}
	};

	void showSelectGroup() {
		selectGroupTitle.setVisibility(View.VISIBLE);
		
	}

	void hideSelectGroup() {
		selectGroupTitle.setVisibility(View.GONE);
		
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
		DirectorFragmentManageActivity.showLogoutButton();;
		DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.INVISIBLE);
		
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.e(Tag, "onDestroyView");
		DirectorFragmentManageActivity.showLogoutButton();;
		DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.INVISIBLE);
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
	@SuppressWarnings("deprecation")
	public  void showDialg(String msg , Context mContext)
	{
		
	final	AlertDialog alertDialog = new AlertDialog.Builder(
                mContext).create();

// Setting Dialog Title
alertDialog.setTitle(SessionManager.getClubName(mContext));

// Setting Dialog Message
alertDialog.setMessage(msg);

// Setting Icon to Dialog

		alertDialog.setCancelable(false);
// Setting OK Button
alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
        // Write your code here to execute after dialog closed
        	//getActivity().getSupportFragmentManager().popBackStack();
        	
        	
        	
        	alertDialog.dismiss();

			DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.VISIBLE);


			DirectorFragmentManageActivity.popBackStackFragment();
        }
});

// Showing Alert Message
alertDialog.show();
	}


	public FragmentBackResponseListener addGroupBackResponseListener = new FragmentBackResponseListener() {
		@Override
		public void UpdateView() {
			super.UpdateView();

			getGroupList();
			if (DirectorFragmentManageActivity.actionbar_titletext != null) {
				DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.broadcast_poling));
			}

			if (DirectorFragmentManageActivity.backButton != null) {
				DirectorFragmentManageActivity.showBackButton();
				DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
			}

		}
	};

	
	
	
}
