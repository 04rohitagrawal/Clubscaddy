package com.clubscaddy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.FragmentBackResponseListener;
import com.clubscaddy.Interface.MemberListListener;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.GroupMemberAdapter;
import com.clubscaddy.Adapter.GroupMemberAdapter.SwipeEventItemClickListener;
import com.clubscaddy.Adapter.MemberAutoCompleteAdapter;

import com.clubscaddy.Bean.GroupBean;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.SqlListe;
import com.clubscaddy.utility.Utill;
@SuppressLint("ValidFragment")
public class AddGroupFragment extends Fragment{

	

	String Tag = getClass().getName();
	public static FragmentManager mFragmentManager;
	public static Fragment mFragment;
	Context mContext;
	EditText groupNameET;
	AutoCompleteTextView searchET;
	ListView listView;
	Button done;
	static ArrayList<MemberListBean> membersList1;
	 ArrayList<MemberListBean> membersList;
	ArrayList<MemberListBean> filteredList;
	ArrayList<MemberListBean> mainMemberList;
	ListView memberListView;
	static boolean referesh;
	TextView add_member_tv;
	boolean edit =false;
	GroupBean bean;
	SqlListe sqlListe ;
	int type ;
	@SuppressLint("ValidFragment")
	public AddGroupFragment(GroupBean bean)
	{
	this.bean = bean;
	type = 3;
	}
	@SuppressLint("ValidFragment")
	public AddGroupFragment(ArrayList<MemberListBean> membersListn ,int type)
	{
	this.mainMemberList = membersListn;	
	this.type = type;
	}
	
	public static GroupBean groupEdit;
	
	public  AddGroupFragment(){
		mFragment = new AddGroupFragment(membersList1,1);

	}


	FragmentBackResponseListener addGroupBackListener ;



	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e(Tag, "onCreateView");
		 super.onCreateView(inflater, container, savedInstanceState);
		 View rootView = inflater.inflate(R.layout.add_group, container, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		sqlListe = new SqlListe(getActivity());

		Bundle bundle = getArguments();
		try
		{
			addGroupBackListener = (FragmentBackResponseListener) bundle.getSerializable("addGroupBackListener");

		}
		catch (Exception e)
		{

		}


		mContext = getActivity();
		 referesh = true;
		 
		 if(mainMemberList != null && bean == null && type != 2)
		 {
			 mainMemberList.clear();
			 
			
		 }
		 
		 if(DirectorFragmentManageActivity.actionbar_titletext!=null)
		 {
			 DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.add_group));
			 if(bean != null)
			 {
				 DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.edit_group));
				 DirectorFragmentManageActivity.hideLogoutButton();
				 DirectorFragmentManageActivity.delete_all_btn.setText("Delete");
				 DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.VISIBLE);
				 DirectorFragmentManageActivity.delete_all_btn.setOnClickListener(listeber);
			 }
		 }
		 
		 if(DirectorFragmentManageActivity.backButton!=null){
				DirectorFragmentManageActivity.showBackButton();
				DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
			}
		 DirectorFragmentManageActivity.showLogoutButton();
		 initializeView(rootView);
		 setOnClickListener();
		 
	
		 return rootView;
	}
	 String beforname ;
	void initializeView(View view){
		
		
		if(bean != null)
		mainMemberList = new ArrayList<MemberListBean>();
		else
		{
			
		}
		
		/*if(mainMemberList == null)
		{
			
		}
		else
		{
			 AppConstants.setMembersList(mainMemberList);	
		}*/
		groupNameET = (EditText) view.findViewById(R.id.group_name);
		add_member_tv = (TextView) view.findViewById(R.id.add_member_tv);
		
		
		searchET = (AutoCompleteTextView)view.findViewById(R.id.serch_member);
		listView = (ListView) view.findViewById(R.id.list);
		done = (Button) view.findViewById(R.id.done);

		searchET.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if (keyCode == 66)
				{
					Utill.hideKeybord(getActivity() , searchET);
				}
				return false;
			}
		});


		
		searchET.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				beforname = s.toString();
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

				String bookingForStr =s.toString();
				boolean isNameValid = false;
				
				if(s.toString().length() != 0)
				{
					if(s.toString().length() == 1 && beforname.length() == 0)
					{	
						
						getMembersList(s.toString() ,searchET);
					}

					/*if(membersList != null)
					{
						for(int i = 0 ; i <membersList.size();i++ )
						{
							
							String name = membersList.get(i).getUser_first_name()+" "+membersList.get(i).getUser_last_name();
							if (membersList.get(i).getUser_first_name().toLowerCase().startsWith((bookingForStr.toLowerCase()))||membersList.get(i).getUser_last_name().toLowerCase().startsWith((bookingForStr.toLowerCase()))||name.toLowerCase().startsWith((s.toString().toLowerCase())))
							{
								//result.add(mainList.get(i));
								isNameValid	= true;
								MemberListBean place =  membersList.get(i);
								//memberBean = place;
							}
						}
					}*/
				}
			}
		});
		
		

disableEditing();

{
	
	setDataTOView();
	
	
}
		
	}
	
	void enableEditing(){
		done.setVisibility(View.VISIBLE);
		done.setBackground(mContext.getResources().getDrawable(R.drawable.okbutton));
		groupNameET.setEnabled(true);
		groupNameET.setClickable(true);
		searchET.setEnabled(true);
		searchET.setClickable(true);



		groupAdapter.notifyDataSetChanged();
		//DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.group_detail));
	}
	
	
	
	void disableEditing(){
		done.setVisibility(View.VISIBLE);
		done.setBackground(mContext.getResources().getDrawable(R.drawable.okbutton));
		edit = true;
		groupNameET.setEnabled(true);
		groupNameET.setClickable(true);
		searchET.setEnabled(true);
		searchET.setClickable(true);


	}
	
	void setDataTOView(){
				
		
			
		try
		{
			
			
			if(bean != null)
			{
				groupNameET.setText(bean.getGroup_name());
				mainMemberList.clear();	
				mainMemberList.addAll(groupEdit.getMembersList())  ;
			}	
			else
			{
				groupNameET.setText("")	;
			}
		}
		catch(Exception e)
		{
			
		}
		
		if(mainMemberList == null)
		{
			mainMemberList = new ArrayList<MemberListBean>();
		}
		add_member_tv.setText("Add Member("+mainMemberList.size()+" Member)" );
		
		setEventAdapter(mainMemberList);
		filteredList = membersList;
		searchET.setAdapter(new MemberAutoCompleteAdapter(mContext, R.id.textViewItem, membersList ,searchET));
		disableEditing();
	}
	//
	public void setSwipeAdapter()
	{
		setEventAdapter(mainMemberList);
		add_member_tv.setText("Add Member("+mainMemberList.size()+" Member)" );
		filteredList = membersList;	
		enableEditing();
	}
	
	
	
	void getGroupDetail(String groupId){
		
	}
	
	void setOnClickListener(){
		searchET.setAdapter(new MemberAutoCompleteAdapter(mContext, R.id.textViewItem, membersList ,searchET));
	//	listView.setSwipeListViewListener(baseSwipeListener);
		searchET.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				MemberListBean place = (MemberListBean) arg0.getItemAtPosition(arg2);
				searchET.setText("");
				membersList.remove(place);
				mainMemberList.add(place);
				
				//dsa
				setEventAdapter(mainMemberList);
			}
		});
		done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				try
				{
					AppConstants.hideSoftKeyboard(getActivity());
				}
				catch(Exception e)
				{
					
				}
				validateData();
				
			}
		});
	}
	
	void validateData(){  
		//Toast.makeText(getActivity(), "Type  "+type, 1).show();
		String groupName = groupNameET.getText().toString().trim();
		if(Utill.isStringNullOrBlank(groupName)){
			Utill.showToast(mContext, "Please Enter Group Name.");
		}else
			if(type == 1 && (mainMemberList==null || mainMemberList.size()==0))
			{
			Utill.showToast(mContext, "Please select atleast one member for group");
		}
		else if(Utill.isNetworkAvailable(getActivity())){
			if(groupEdit==null)
				addGroup(groupName);
			else{
				editGroup(groupName);
			}
		}else{
			Utill.showNetworkError(mContext);
		}
	}
	
	void editGroup(String groupName){
		JSONObject json = new JSONObject();
		JSONArray jArray = new JSONArray();
		try {
			for(int i=0;i<mainMemberList.size();i++){
				JSONObject jobj = new JSONObject();
				jobj.put("group_user_ids",mainMemberList.get(i).getUser_id());
				jArray.put(jobj);
			}	
			json.put("member", jArray);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("group_id",groupEdit.getGroup_id());
		params.put("group_name",""+groupName);
		params.put("requesti",json);
		if (Utill.isNetworkAvailable(getActivity())) {
			Utill.showProgress(mContext);
			GlobalValues.getModelManagerObj(mContext).addGroup(true,params, new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					Utill.hideProgress();
					try
					{
						bean.setMembersList(mainMemberList);	
					}
					catch(Exception e)
					{
					Utill.showDialg(e.getMessage(), getActivity());	
					}
					
					DirectorFragmentManageActivity.popBackStackFragment();
					
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
	
	
	void addGroup(String groupName){
		JSONObject json = new JSONObject();
		JSONArray jArray = new JSONArray();
		try {
			for(int i=0;i<mainMemberList.size();i++){
				JSONObject jobj = new JSONObject();
				jobj.put("group_user_ids",mainMemberList.get(i).getUser_id());
				jArray.put(jobj);
			}	
			json.put("member", jArray);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("group_club_id",SessionManager.getUser_Club_id(mContext));
		params.put("group_name",""+groupName);
		params.put("group_owner_user_id", SessionManager.getUser_id(mContext));
		params.put("request",json);
		if (Utill.isNetworkAvailable(getActivity())) {
			Utill.showProgress(mContext);
			GlobalValues.getModelManagerObj(mContext).addGroup(false,params, new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					Utill.hideProgress();
					DirectorFragmentManageActivity.popBackStackFragment();
					
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
	
	GroupMemberAdapter groupAdapter;
	public void setEventAdapter(final ArrayList<MemberListBean> list) {
		add_member_tv.setText("Add Member  ("+mainMemberList.size()+" Members) " );
		groupAdapter = null;
		listView.setVisibility(View.VISIBLE);
		groupAdapter = new GroupMemberAdapter(getActivity(), mainMemberList ,fragmentBackListener);
		groupAdapter.setEventItemClickListener(new SwipeEventItemClickListener() {
			
			@Override
			public void OnEditClick(int position) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void OnBlockClick(final int position, int blockfdsfsdfStatus) {
				// TODO Auto-generated method stub
				//Toast.makeText(getActivity(), "Sizee ", 1).show();
				
				
				
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

				// Setting Dialog Title
				alertDialog.setTitle(SessionManager.getClubName(getActivity()));

				// Setting Dialog Message
				alertDialog.setMessage("Are you sure you want remove this member?");

				

				// Setting Positive "Yes" Button
				alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {

						try
						{
							membersList.add(mainMemberList.get(position));	
						}
						catch(Exception e)
						{
							
						}
						
						mainMemberList.remove(mainMemberList.get(position));
						
						setEventAdapter(membersList);
						dialog.cancel();
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
		
		listView.setAdapter(groupAdapter);

		

	}

	MemberAutoCompleteAdapter memberAutoCompleteAdapter ;
	private void getMembersList(String keyWord, final AutoCompleteTextView autocompletetv)
	{
		sqlListe.getMemberList(getActivity(), new MemberListListener() {
			@Override
			public void onSuccess(ArrayList<MemberListBean> memberListBeanArrayList)
			{

				membersList = new ArrayList<MemberListBean>();
				membersList.addAll(memberListBeanArrayList);



				ArrayList<MemberListBean> copymembersList = new ArrayList<MemberListBean>();
				copymembersList.addAll(membersList);
				for(int i = 0 ;i <copymembersList.size();i++ )
				{
					if((SessionManager.getFirstName(getActivity())+" "+SessionManager.getLastName(getActivity())).equalsIgnoreCase(membersList.get(i).getUser_first_name()+" "+membersList.get(i).getUser_last_name()))
					{
						copymembersList.remove(i);
					}
				}


				membersList.clear();


				membersList.addAll(copymembersList);










				//AppConstants.setMembersList(membersList);
				filteredList = membersList;

				for(int i = 0;i<mainMemberList.size();i++){
					for(int j = 0;j<membersList.size();j++){
						if(mainMemberList.get(i).getUser_id().equalsIgnoreCase(membersList.get(j).getUser_id())){
							membersList.remove(j);
							break;
						}
					}
				}

				searchET.setAdapter(new MemberAutoCompleteAdapter(mContext, R.id.textViewItem, membersList,searchET));
				searchET.setText(searchET.getText().toString()+"");
				searchET.setSelection(1);



			}
		},AppConstants.AllMEMBERlIST ,keyWord);
	}


	
	
	

	

	@Override
	public void onStart() {
		super.onStart();
		if(referesh)
			refreshView();
		Log.e(Tag, "onStart");
	}
	
	void refreshView(){
		referesh = false;
		groupNameET.setText("");
		searchET.setText("");
		if(groupEdit!=null && bean != null){
			groupNameET.setText(groupEdit.getGroup_name());
		}
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
		 DirectorFragmentManageActivity.showLogoutButton();
		 DirectorFragmentManageActivity.delete_all_btn.setText("Delete");
		 DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.GONE);



		if (addGroupBackListener != null)
		{
			addGroupBackListener.UpdateView();
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
				getActivity().getSupportFragmentManager().popBackStack();
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
	public OnClickListener listeber = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(v.getId() == R.id.delete_all_btn)
			{
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

				// Setting Dialog Title
				alertDialog.setTitle(SessionManager.getClubName(getActivity()));

				// Setting Dialog Message
				alertDialog.setMessage("Are you sure you want delete this group?");

				

				// Setting Positive "Yes" Button
				alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {

						
						deleteDropIns(bean);
						
							dialog.cancel();
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
			if(v.getId() == R.id.edit_btn)
			{
				AddGroupFragment fragment = new AddGroupFragment(bean);
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "fragment").addToBackStack(null).commit();
				//Toast.makeText(getActivity(), "Size   "+mainMemberList.size(), Toast.LENGTH_LONG).show();
		//	DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.AddGroupFragment_id);
		
			}
			
		}
	};
	void deleteDropIns(GroupBean bean) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("group_id",bean.getGroup_id());
		params.put("group_user_type",SessionManager.getUser_type(getActivity()));
		if (Utill.isNetworkAvailable(getActivity())) {
			Utill.showProgress(getActivity());
			GlobalValues.getModelManagerObj(getActivity()).deleteGroup(params, new ModelManagerListener() {
				
				@Override
				public void onSuccess(String json) {
					// TODO Auto-generated method stub
					Utill.hideProgress();
					
					try
					{
						JSONObject jsonObj = new JSONObject(json);
						showDialg(jsonObj.getString("message"), getActivity());
					}
					catch(Exception e)
					{
						
					}
					
					Log.e("json", json);
				}
				
				@Override
				public void onError(String msg) {
					// TODO Auto-generated method stub
					
					Utill.hideProgress();
					Utill.showDialg(msg, getActivity());
				}
			});
		} else {
			Utill.showNetworkError(getActivity());
		}

	}
	@SuppressWarnings("deprecation")
	public  void showDialg(String msg , Context mContext)
	{
		
	final	AlertDialog alertDialog = new AlertDialog.Builder(
	            mContext).create();

	//Setting Dialog Title
	alertDialog.setTitle(SessionManager.getClubName(mContext));

	//Setting Dialog Message
	alertDialog.setMessage(msg);
		alertDialog.setCancelable(false);

	//Setting Icon to Dialog


	//Setting OK Button
	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int which) {
	    // Write your code here to execute after dialog closed
	    	alertDialog.dismiss();
	    	getActivity().getSupportFragmentManager().popBackStack();
	    	
	    	
	    }
	});

	//Showing Alert Message
	alertDialog.show();
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
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

	}

	FragmentBackResponseListener fragmentBackListener = new FragmentBackResponseListener() {
		@Override
		public void UpdateView()
		{
			super.UpdateView();

			DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.VISIBLE);

			if(DirectorFragmentManageActivity.actionbar_titletext!=null)
			{
				DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.add_group));
				if(bean != null)
				{
					DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.edit_group));
					DirectorFragmentManageActivity.hideLogoutButton();
					DirectorFragmentManageActivity.delete_all_btn.setText("Delete");
					DirectorFragmentManageActivity.delete_all_btn.setVisibility(View.VISIBLE);
					DirectorFragmentManageActivity.delete_all_btn.setOnClickListener(listeber);
				}
			}
		}
	};
}
