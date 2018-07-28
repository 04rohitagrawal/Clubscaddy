package com.clubscaddy.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import com.clubscaddy.Adapter.RatingSpinnerListAdapter;
import com.clubscaddy.Bean.FilterRating;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.FragmentBackListemers;
import com.clubscaddy.Interface.MemberListListener;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.MemberSwipeviewAdapter;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.custumview.InternetConnection;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.SqlListe;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.utility.Validation;

public class MembersDirectoryFragment extends Fragment {

	String Tag = getClass().getName();
	public static FragmentManager mFragmentManager;
	public static Fragment mFragment;
	Context mContext;
	//Spinner rating_spiner ;;MembersDirectoryFragment.isClearVariable

	public static boolean isClearVariable = false;

	ImageButton addMemberButton;
	ListView membersList_listView;
	EditText searchET;
	TextView crate_group_tv ;
	TextView send_tv;
	public	TextView countr;

	SqlListe sqLiteDatabase ;



	public   Spinner ratting_list_spinner;

	ArrayList<String> user_rating = new ArrayList<String>() ;
	public	TextView crete_counter;
	ArrayList<MemberListBean> memberMainList;
	CheckBox allCB, maleCB, femaleCB, juniorCB;
	TextView cancel_dialog_btn;
	TextView email_send_btn;
	TextView notification_send_btn;
	TextView discription_textview_status;


	EditText email_notification_msg;
	AQuery aQuery;
	//LinearLayout rating_layout;

	SessionManager sessionManager ;
	FilterRating filterRating ;

	ArrayList<String>spinner_list = new ArrayList<String>();
	public  RelativeLayout create_ralative_id ,send_ralative_id ;
	// ImageButton actionbar_backbutton1;
	public int numofselecteditem =0;
	public static Fragment getInstacne(FragmentManager mFragManager) {
		if (mFragment == null) {
			mFragment = new MembersDirectoryFragment();
		}
		return mFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.e(Tag, "onAttach");
	}
	RatingSpinnerListAdapter ratingSpinnerListAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(Tag, "onCreate");
	}
	RelativeLayout ratting_layout;

	ArrayList<FilterRating>rattingArrayList ;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e(Tag, "onCreateView");
		super.onCreateView(inflater, container, savedInstanceState);

		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		filterRating = new FilterRating();

		View rootView = inflater.inflate(R.layout.members_directory_frgment, container, false);
		countr = (TextView) rootView.findViewById(R.id.countr);
		crete_counter = (TextView) rootView.findViewById(R.id.crete_counter);
		send_ralative_id = (RelativeLayout) rootView.findViewById(R.id.send_ralative_id);
		searchET = (EditText) rootView.findViewById(R.id.search_member);

		//rating_layout = (LinearLayout) rootView.findViewById(R.id.rating_layout);;


		sqLiteDatabase = new SqlListe(getActivity());

		ratting_list_spinner = (Spinner) rootView.findViewById(R.id.ratting_list_spinner);

		sessionManager= new SessionManager(getActivity());

		ratting_layout = (RelativeLayout) rootView.findViewById(R.id.ratting_layout);;

		if (sessionManager.getClubRatingType(getActivity()) == 1)
		{
			ratting_layout.setVisibility(View.INVISIBLE);
		}
		else
		{
			ratting_layout.setVisibility(View.VISIBLE);
		}


		if (rattingArrayList  == null)
		{
			rattingArrayList = new ArrayList<>();

			String ratting =	sessionManager.getClubRatting(getActivity());
			Log.e("ratting" ,ratting+"");


			try
			{
				rattingArrayList.clear();
				filterRating = new FilterRating();
				filterRating.setItemSelected(false);
				filterRating.setRating("Filter by level of play");
				rattingArrayList.add(filterRating);

				filterRating = new FilterRating();
				filterRating.setItemSelected(true);
				filterRating.setRating("All");
				rattingArrayList.add(filterRating);
				JSONArray ratting_json_array = new JSONArray(ratting);

				for (int i = 0 ; i < ratting_json_array.length() ;i++)
				{
					filterRating = new FilterRating();
					filterRating.setItemSelected(false);
					filterRating.setRating(ratting_json_array.getString(i));
					rattingArrayList.add(filterRating);

				}
			}
			catch (Exception e)
			{

			}
		}


		ratingSpinnerListAdapter = 	new RatingSpinnerListAdapter(getActivity() ,rattingArrayList , this);

		ratting_list_spinner.setAdapter(ratingSpinnerListAdapter);
		//Utill.showDialg("Sport type "+SessionManager.getSport_type(getActivity()), getActivity());
		

		if(SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER) == true ||SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_COACH))
		{
			send_ralative_id.setVisibility(View.INVISIBLE);
		}
		create_ralative_id = (RelativeLayout) rootView.findViewById(R.id.create_ralative_id);

		user_rating.add("All");

		/*all_rating_cb =  (CheckBox) rootView.findViewById(R.id.all_rating_cb);
		all_rating_cb.setOnCheckedChangeListener (checkboxclickListener);


		two_half__rating_cb =  (CheckBox) rootView.findViewById(R.id.two_half__rating_cb);

		three_half_rating_cb =  (CheckBox) rootView.findViewById(R.id.three_half_rating_cb);
		three_rating_cb =  (CheckBox) rootView.findViewById(R.id.three_rating_cb);





		four_rating_cb =  (CheckBox) rootView.findViewById(R.id.four_rating_cb);
		four_half_rating_cb =  (CheckBox) rootView.findViewById(R.id.four_half_rating_cb);




		five_rating_cb =  (CheckBox) rootView.findViewById(R.id.five_rating_cb);
		five_half_rating_cb =  (CheckBox) rootView.findViewById(R.id.five_half_rating_cb);
*/


		allCB = (CheckBox) rootView.findViewById(R.id.all);
		maleCB = (CheckBox) rootView.findViewById(R.id.male);
		femaleCB = (CheckBox) rootView.findViewById(R.id.female);
		juniorCB = (CheckBox) rootView.findViewById(R.id.junior);



		/*five_rating_cb.setOnCheckedChangeListener(checkboxclickListener);

		two_half__rating_cb.setOnCheckedChangeListener(checkboxclickListener);
		three_half_rating_cb.setOnCheckedChangeListener(checkboxclickListener);
		three_rating_cb.setOnCheckedChangeListener(checkboxclickListener);
		four_rating_cb.setOnCheckedChangeListener(checkboxclickListener);
		four_half_rating_cb.setOnCheckedChangeListener(checkboxclickListener);
		five_half_rating_cb.setOnCheckedChangeListener(checkboxclickListener);*/

		//two_half__rating_cb =  (RadioButton) rootView.findViewById(R.id.two_half__rating_cb);


		//all_rating_cb.setChecked(true);

		aQuery = new AQuery(getActivity());
		crate_group_tv = (TextView) rootView.findViewById(R.id.crate_group_tv);
		send_tv = (TextView) rootView.findViewById(R.id.send_tv);

		crate_group_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ArrayList<MemberListBean>selectionList = new ArrayList<MemberListBean>();
				for(int i =0 ; i< list.size();i++)
				{
					if(list.get(i).getMemberSelection() == true)
					{
						selectionList.add(list.get(i));

					}

				}
				AddGroupFragment fragment = new AddGroupFragment(selectionList ,2);
				//fragment.setInstace(selectionList);
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment,"fragment").addToBackStack(null).commit();
			}
		});
		send_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final	Dialog dialog = new Dialog(getActivity());
				dialog.setCancelable(false);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.email_notification_layout);
				dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

				cancel_dialog_btn = (TextView) dialog.findViewById(R.id.cancel_dialog_btn);
				email_send_btn = (TextView) dialog.findViewById(R.id.email_send_btn);
				notification_send_btn = (TextView) dialog.findViewById(R.id.notification_send_btn);

				discription_textview_status = (TextView) dialog.findViewById(R.id.discription_textview_status);
				email_notification_msg = (EditText) dialog.findViewById(R.id.email_notification_msg);

				email_notification_msg.addTextChangedListener(new TextWatcher() {
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





				cancel_dialog_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				email_send_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String user_id_array = "";
						int size = list.size();

						for(int i =0 ; i< list.size();i++)
						{
							if(list.get(i).getMemberSelection() == true)
							{
								if(user_id_array == ""||user_id_array.equalsIgnoreCase(""))
								{
									user_id_array = list.get(i).getUser_id() ;
								}
								else
								{
									user_id_array =user_id_array+","+ list.get(i).getUser_id() ;
								}	
							}


						}
						Log.e("user_id_array", user_id_array);
						if(email_notification_msg.getText().toString()!=""&&!email_notification_msg.getText().toString().equalsIgnoreCase(""))
						{
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("sender_id", SessionManager.getUser_id(mContext));
							params.put("user_id", user_id_array);
							params.put("notify_via", "2");
							params.put("message", email_notification_msg.getText().toString());

							pd = new ProgressDialog(getActivity());
							pd.setCancelable(false);
							pd.show();




							aQuery.ajax(WebService.usermail, params, JSONObject.class, new AjaxCallback<JSONObject>(){


								@SuppressLint("ShowToast")
								@Override
								public void callback(String url, JSONObject jsonObj, AjaxStatus status) {
									// TODO Auto-generated method stub
									super.callback(url, jsonObj, status);

									try
									{
										Utill.showDialg(jsonObj.getString("message"), getActivity());
										//Toast.makeText(getActivity(), jsonObj.getString("message"), Toast.LENGTH_LONG).show();
									}
									catch(Exception e)
									{

									}
									dialog.cancel();
									pd.dismiss();
								}
							});

						}
						else {
							Utill.showDialg("Enter Message", getActivity());
							//(getActivity(), "Enter Message", Toast.LENGTH_LONG).show();
						}

					}
				});


				notification_send_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String user_id_array = "";
						int size = list.size();
						Log.e("ssss", size+"");
						for(int i =0 ; i< list.size();i++)
						{
							if(list.get(i).getMemberSelection() == true)
							{
								if(user_id_array == ""||user_id_array.equalsIgnoreCase(""))
								{
									user_id_array = list.get(i).getUser_id() ;
								}
								else
								{
									user_id_array =user_id_array+","+ list.get(i).getUser_id() ;
								}	
							}


						}
						Log.e("user_id_array", user_id_array);
						if(email_notification_msg.getText().toString()!=""&&!email_notification_msg.getText().toString().equalsIgnoreCase(""))
						{
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("sender_id", SessionManager.getUser_id(mContext));
							params.put("user_id", user_id_array);
							params.put("notify_via", "1");
							params.put("message", email_notification_msg.getText().toString());

							pd = new ProgressDialog(getActivity());
							pd.setCancelable(false);
							pd.show();




							aQuery.ajax(WebService.usermail, params, JSONObject.class, new AjaxCallback<JSONObject>(){


								@SuppressLint("ShowToast")
								@Override
								public void callback(String url, JSONObject jsonObj, AjaxStatus status) {
									// TODO Auto-generated method stub
									super.callback(url, jsonObj, status);

									try
									{
										String sss = jsonObj+"";
										Log.e("fdsasf", sss+"");
										Utill.showDialg(jsonObj.getString("message"), getActivity());
										//Toast.makeText(getActivity(), jsonObj.getString("message"), Toast.LENGTH_LONG).show();
									}
									catch(Exception e)
									{

									}
									dialog.cancel();
									pd.dismiss();
								}
							});

						}
						else {
							Utill.showDialg("Enter Message", getActivity());
							//Toast.makeText(getActivity(), "Enter Message", Toast.LENGTH_LONG).show();
						}

					}
				});
				dialog.show();
			}
		});

		mContext = getActivity();
		//rating_spiner = (Spinner) rootView.findViewById(R.id.spinner);
		spinner_list.clear();

		spinner_list.add("All");

		spinner_list.add("2.5");
		spinner_list.add("3.0");
		spinner_list.add("3.5");
		spinner_list.add("4.0");
		spinner_list.add("4.5");
		spinner_list.add("5.0");
		spinner_list.add("5.5");
		spinner_list.add("6.0");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_list_item_layout, spinner_list);

		//rating_spiner.setAdapter(adapter);


		/*rating_spiner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				// TODO Auto-generated method stub
				user_rating = spinner_list.get(pos);	

				if(pos == 0)
				{
					filterViaId(-1);
				}
				else
				{
					filterViaId(selectionId);	
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});*/

		/*rating_spiner.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				// TODO Auto-generated method stub

			}
		});*/




		if (DirectorFragmentManageActivity.actionbar_titletext != null) {
			DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.members_directory));
		}
		if (DirectorFragmentManageActivity.backButton != null) {
			DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
			DirectorFragmentManageActivity.showBackButton();
		}
		addMemberButton = (ImageButton) rootView.findViewById(R.id.adminList_addAdmin);
		if(SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER) ||SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_COACH))
		{
			addMemberButton.setVisibility(View.INVISIBLE);
		}
		membersList_listView = (ListView) rootView.findViewById(R.id.example_lv_list);


		allCB.setOnCheckedChangeListener(chaeckChange);
		maleCB.setOnCheckedChangeListener(chaeckChange);
		femaleCB.setOnCheckedChangeListener(chaeckChange);
		juniorCB.setOnCheckedChangeListener(chaeckChange);

		//membersList_listView.setSwipeListViewListener(baseSwipeListener);
		addMemberButton.setOnClickListener(addAdminListener);
		searchET.addTextChangedListener(searchWatcher);
		memberMainList = new ArrayList<MemberListBean>();
		result = new ArrayList<MemberListBean>();
		allCB.setChecked(true);

		getMembersList();
		//all_rating_cb.setChecked(true);
		return rootView;
	}

	OnCheckedChangeListener chaeckChange = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			int id = buttonView.getId();
			boolean check = buttonView.isChecked();
			//searchET.setText("");

			boolean filterStatus = true ;

			switch (id) {
			case R.id.all:
				if (check) {
					maleCB.setChecked(false);
					femaleCB.setChecked(false);
					juniorCB.setChecked(true);
					//juniorCB.setChecked(false);
				} else if (!maleCB.isChecked() && !femaleCB.isChecked() ) {
					allCB.setChecked(true);
				}
				filterStatus = true ;
				break;
			case R.id.male:
				if (check) {
					juniorCB.setChecked(true);

					femaleCB.setChecked(false);
					allCB.setChecked(false);
				} else if (!maleCB.isChecked() && !femaleCB.isChecked()) {
					allCB.setChecked(true);
				}
				filterStatus = true ;

				break;
			case R.id.female:
				if (check)
				{
					juniorCB.setChecked(true);

					maleCB.setChecked(false);
					allCB.setChecked(false);
				} else if (!maleCB.isChecked() && !femaleCB.isChecked()) {
					allCB.setChecked(true);
				}
				filterStatus = true ;

				break;
			case R.id.junior:

				applyFiltringForSelectOrUnselect(check);
				filterStatus = false ;

				break;

			default:
				break;
			}


			if(filterStatus)

			filterList();

		}

	};







	public void applyFiltringForSelectOrUnselect(boolean check)
	{
		if (list != null)
		{


			for (int i  = 0 ;i < list.size() ;i++)
			{
				list.get(i).setMemberSelection(check);
			}

			setEventAdapter(list);
		}

	}












	public static final int JUNIOUR = 1;
	public static final int MALE = 2;
	public static final int FEMALE = 3;
	public static final int JUNIOUR_MALE = 4;
	public static final int JUNIOUR_FEMALE = 5;
public 	 int selectionId = -1;
public 	void filterList() {

		if (allCB.isChecked())
		{
			selectionId = -1;

		} else if (maleCB.isChecked() ) {
			//		Utill.showToast(mContext, "male j");
			selectionId = MALE;
		} else if (femaleCB.isChecked() ) {
			//		Utill.showToast(mContext, "female j");
			selectionId = FEMALE;
		} else if (juniorCB.isChecked()) {
			//		Utill.showToast(mContext, "juniour");
			selectionId = JUNIOUR;
		} else if (maleCB.isChecked()) {
			//		Utill.showToast(mContext, "only male");
			selectionId = MALE;
		} else if (femaleCB.isChecked()) {
			//		Utill.showToast(mContext, "only female");
			selectionId = FEMALE;
		} else {
			//	Utill.showToast(mContext, "none");
		}
		filterViaId(selectionId);
	}
	ArrayList<MemberListBean>copy_list;
	ArrayList<MemberListBean> list;
	ArrayList<MemberListBean> non_filter_list;
	void filterViaId(int id) {
		list = new ArrayList<MemberListBean>();
		//Toast.makeText(getActivity(), "Main list "+memberMainList.size(), 1).show();
		switch (id) {
		case -1:
			list.addAll(memberMainList);
			break;
		case JUNIOUR:
			for (int i = 0; i < memberMainList.size(); i++) {
				String juniour = memberMainList.get(i).getUser_junior();
				if (juniour.equalsIgnoreCase("1")) {
					list.add(memberMainList.get(i));
				}
			}
			break;
		case MALE:
			for (int i = 0; i < memberMainList.size(); i++) {
				String genderM = memberMainList.get(i).getUser_gender();
				if (genderM.equalsIgnoreCase("1")) {
					list.add(memberMainList.get(i));
				}
			}
			break;
		case FEMALE:
			for (int i = 0; i < memberMainList.size(); i++) {
				String genderf = memberMainList.get(i).getUser_gender();
				if (genderf.equalsIgnoreCase("2")) {
					list.add(memberMainList.get(i));
				}
			}
			break;
		case JUNIOUR_MALE:
			for (int i = 0; i < memberMainList.size(); i++) {
				String genderf = memberMainList.get(i).getUser_gender();
				String juniourF = memberMainList.get(i).getUser_junior();
				if (genderf.equalsIgnoreCase("1") && juniourF.equalsIgnoreCase("1")) {
					list.add(memberMainList.get(i));
				}
			}
			break;
		case JUNIOUR_FEMALE:
			for (int i = 0; i < memberMainList.size(); i++) {
				String genderf = memberMainList.get(i).getUser_gender();
				String juniourF = memberMainList.get(i).getUser_junior();

				if (genderf.equalsIgnoreCase("2") && juniourF.equalsIgnoreCase("1")) {
					list.add(memberMainList.get(i));
				}
			}
			break;
		default:
			break;
		}

		copy_list = new ArrayList<MemberListBean>();



		if(rattingArrayList.get(1).isItemSelected() == false)
		{





			for(int i = 0 ;i < list.size() ;i++)
			{
				String rating = "" ;



				for(int j = 1 ;j < rattingArrayList.size();j++)
				{
					if(rattingArrayList.get(j).isItemSelected() == true)
					{
						rating = 	(rattingArrayList.get(j).getRating().toString());
						if(rating.equalsIgnoreCase(list.get(i).getUser_rating()))
						{
							copy_list.add(list.get(i))	;
						}
					}
				}





			}	
		}
		else
		{
			copy_list.addAll(list);	
		}
		non_filter_list = new ArrayList<MemberListBean>();
		non_filter_list.addAll(copy_list);
		/**/

		copy_list = getfilterMemberList(searchET.getText().toString(), copy_list);
		list.clear();
		list.addAll(copy_list);
		setEventAdapter(copy_list);
		result = copy_list;
	}

	TextWatcher searchWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			String str = s.toString();


			ArrayList<MemberListBean>	filters = getfilterMemberList(searchET.getText().toString(), non_filter_list);
			list.clear();
			list.addAll(filters);
			setEventAdapter(filters);
			result = filters;
		}
	};

	ArrayList<MemberListBean> result;

	void filterList(String tag) {
		if (!Utill.isStringNullOrBlank(tag)) 
		{
			result = new ArrayList<MemberListBean>();
			for (int i = 0; i < memberMainList.size(); i++) 
			{
				String name = memberMainList.get(i).getUser_first_name() + " " + memberMainList.get(i).getUser_last_name();
				if (memberMainList.get(i).getUser_first_name().toLowerCase().startsWith((tag.toLowerCase()))||memberMainList.get(i).getUser_last_name().toLowerCase().startsWith((tag.toLowerCase()))||name.toLowerCase().startsWith((tag.toLowerCase())))
				{
					result.add(memberMainList.get(i));
				}
			}
			// membersList_listView.setAdapter(new
			// AdminListAdapter(mContext,result));
			setEventAdapter(result);
		} else {
			//Utill.showToast(mContext, "No text found.");
			// membersList_listView.setAdapter(new
			// AdminListAdapter(mContext,memberMainList));
			result = memberMainList;
			setEventAdapter(memberMainList);
		}
	}

	public ArrayList<MemberListBean> getfilterMemberList(String tag ,ArrayList<MemberListBean> sourceList )
	{
		result = new ArrayList<MemberListBean>();
		if (!Utill.isStringNullOrBlank(tag)) 
		{

			for (int i = 0; i < sourceList.size(); i++) 
			{
				String name = sourceList.get(i).getUser_first_name() + " " + sourceList.get(i).getUser_last_name();
				//String name = mainList.get(i).getUser_first_name()+" "+mainList.get(i).getUser_last_name();
				if (sourceList.get(i).getUser_first_name().toLowerCase().startsWith((tag.toLowerCase()))||sourceList.get(i).getUser_last_name().toLowerCase().startsWith((tag.toLowerCase()))||name.toLowerCase().startsWith((tag.toLowerCase())))
				{
					MemberListBean bean = sourceList.get(i);
					bean.setMemberSelection(true);
					result.add(sourceList.get(i));
				}
			}

		}
		else 
		{

			result.addAll(sourceList);

		}
		return result;

	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e(Tag, "onActivityCreated");


	}

	@Override
	public void onStart() {
		super.onStart();
		if (isClearVariable)
		{
			searchET.setText("");
			//all_rating_cb.setChecked(true);
			allCB.setChecked(true);

			rattingArrayList = new ArrayList<>();

			String ratting = sessionManager.getClubRatting(getActivity());
			Log.e("ratting", ratting + "");


			try {
				rattingArrayList.clear();
				filterRating = new FilterRating();
				filterRating.setItemSelected(false);
				filterRating.setRating("Filter by level of play");
				rattingArrayList.add(filterRating);

				filterRating = new FilterRating();
				filterRating.setItemSelected(true);
				filterRating.setRating("All");
				rattingArrayList.add(filterRating);
				JSONArray ratting_json_array = new JSONArray(ratting);

				for (int i = 0; i < ratting_json_array.length(); i++) {
					filterRating = new FilterRating();
					filterRating.setItemSelected(false);
					filterRating.setRating(ratting_json_array.getString(i));
					rattingArrayList.add(filterRating);

				}
			} catch (Exception e) {

			}
			ratingSpinnerListAdapter = new RatingSpinnerListAdapter(getActivity(), rattingArrayList, this);
			ratting_list_spinner.setAdapter(ratingSpinnerListAdapter);
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

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.e(Tag, "onDestroyView");
		isClearVariable = true;
		try
		{
			getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN |WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

			AppConstants.hideSoftKeyboard(getActivity());
		}
		catch(Exception e)
		{
			
		}
	}

	@Override
	public void onDetach() {

		Log.e(Tag, "onDetach");
		allCB.setChecked(true);
		maleCB.setChecked(false);
		femaleCB.setChecked(false);
		juniorCB.setChecked(false);

		rattingArrayList  = null ;
		//rating_spiner = null;
		super.onDetach();

	}
	int clubsId= 0;
	ProgressDialog pd ;
	private void getMembersList() {



		sqLiteDatabase.getMemberList(getActivity(), new MemberListListener() {
			@Override
			public void onSuccess(ArrayList<MemberListBean> memberListBeanArrayList)
			{
				parsingData(memberListBeanArrayList);

			}
		}, AppConstants.AllMEMBERlIST,"");

		/*

		list = new ArrayList<MemberListBean>();

		final String currentTime = sessionManager.getCurrentTime();


	    clubsId =  	Integer.parseInt(SessionManager.getUser_Club_id(getActivity()));

		String lastUpdateTime = sqLiteDatabase.getLastTimeUpdate(clubsId);

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		try {
			Calendar currentTimeCal = Calendar.getInstance(Locale.ENGLISH);
			currentTimeCal.setTime(simpleDateFormat.parse(currentTime));


			Calendar lastUpdateTimeCal = Calendar.getInstance(Locale.ENGLISH);
			lastUpdateTimeCal.setTime(simpleDateFormat.parse(lastUpdateTime));



			if (Validation.isStringNullOrBlank(lastUpdateTime) == false &&
					Utill.isEqualDate(currentTimeCal , lastUpdateTimeCal))
			{

				ArrayList<MemberListBean> alMemberList = sqLiteDatabase.memberListArrayList(clubsId);


				if (alMemberList.size() != 0)
				{
					parsingData(alMemberList);


					return;
				}


			}


		} catch (ParseException e) {
			e.printStackTrace();
		}



try {
			pd = new ProgressDialog(getActivity());
			pd.setCancelable(false);
			pd.setMessage("Loading....");
			pd.show();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("user_type",SessionManager.getUser_type(getActivity()));
			params.put("user_club_id", SessionManager.getUser_Club_id(mContext));
public void onSuccess(String json) {
					// TODO Auto-generated method stub
 					Log.e("json", json);
					final ArrayList<MemberListBean> alMemberList = JsonUtility.parserMembersList(json, mContext);


					sessionManager.setLastDateOfMemberListUpdation(currentTime);

					sqLiteDatabase.updateTimeOfMemberDirectory(clubsId , currentTime);


					AsyncTask<String,String, String> task =new AsyncTask<String, String, String>() {
						@Override
						protected String doInBackground(String... params) {

							sqLiteDatabase.putDataInList(alMemberList,clubsId);


			GlobalValues.getModelManagerObj(mContext).getallMembers(params, new ModelManagerListener() {

		@Override

							return null;
						}

						@Override
						protected void onPostExecute(String s) {
							super.onPostExecute(s);
							pd.dismiss();;
							parsingData(alMemberList);

						}
					};
				}

				@Override
				public void onError(String msg) {
					// TODO Auto-generated method stub
					pd.dismiss();
				}
			});
		} catch (Exception e) {
			ShowUserMessage.showUserMessage(mContext, e.toString());
		}
	task.execute();

		 */

















	}




	public void parsingData(ArrayList<MemberListBean>alMemberList)
	{
		if (alMemberList.size() > 0) {

			//	Utill.showDialg(SessionManager.getFirstName(mContext)+" "+SessionManager.getLastName(mContext), mContext);
			ArrayList<MemberListBean> result = new ArrayList<MemberListBean>();;
			for(int i = 0 ;i<alMemberList.size();i++){
				String name = alMemberList.get(i).getUser_first_name()+" "+alMemberList.get(i).getUser_last_name();
				if(!name.toLowerCase().equalsIgnoreCase(SessionManager.getFirstName(mContext)+" "+SessionManager.getLastName(mContext)))
				{
					result.add(alMemberList.get(i));
				}
				else
				{
					result.add(alMemberList.get(i));
				}


			}
			alMemberList.clear();
			alMemberList.addAll(result);











			memberMainList = alMemberList;
			result = memberMainList;
			list.addAll(memberMainList);
			//Toast.makeText(mContext, " Rohit "+allCB.isChecked()+"  "+searchET.getText().toString(), 1).show();

			if(rattingArrayList.get(1).isItemSelected()==true&&allCB.isChecked()==true&&(searchET.getText().toString()==""||searchET.getText().toString().equalsIgnoreCase("")))
			{
				non_filter_list = new ArrayList<MemberListBean>();
				non_filter_list.addAll(memberMainList);
				setEventAdapter(alMemberList);
			}
			else
			{
				filterList();
			}


		} else {
			ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.no_record_found));
		}

	}











	OnClickListener addAdminListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {

				AddMemberFragment addMemberFragment = new AddMemberFragment();
				addMemberFragment.setInstanse(new FragmentBackListemers() {
					@Override
					public void onReload(boolean isReload)
					{
					  if (isReload)
					  {
						getMembersList();
					  }

					}
				});
				getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_frame , addMemberFragment , "").addToBackStack("").commit();

			} catch (Exception e) {
				ShowUserMessage.showUserMessage(mContext, e.toString());
			}

		}
	};

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

	/*
	 * OnItemClickListener onItemClickListener = new OnItemClickListener() {
	 * 
	 * @Override public void onItemClick(AdapterView<?> parent, View view, int
	 * position, long id) { //
	 * DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity
	 * .EditAdminFragment_id);
	 * 
	 * AddAdminFragment.edit = false;
	 * DirectorFragmentManageActivity.switchFragment
	 * (DirectorFragmentManageActivity.AddAdminFragment_id); } };
	 */

	public class AdminListListener {
		public void onSuccess(ArrayList<MemberListBean> alMemberList) {
			if (alMemberList.size() > 0) {
				memberMainList = alMemberList;
				// membersList_listView.setAdapter(new
				// AdminListAdapter(mContext, alMemberList));
				// adminList_listView.setOnItemClickListener(onItemClickListener);
				setEventAdapter(alMemberList);
			} else {
				ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.no_record_found));
			}
		}

		public void onError(String message) {
			ShowUserMessage.showUserMessage(mContext, message);
		}
	}

	MemberSwipeviewAdapter swipeAdapter;

	public void setEventAdapter(ArrayList<MemberListBean> list) {

		//Toast.makeText(getActivity(), "slfdsjkfds "+list.size(), Toast.LENGTH_LONG).show();
		/*ArrayList<MemberListBean>copy_list = new ArrayList<MemberListBean>();
		//;Toast.makeText(getActivity(), user_rating+" "+list, 1).show();
		if(!user_rating.contains("All"))
		{


			for(int i = 0 ; i<list.size();i++)
			{
				if(user_rating.equalsIgnoreCase(list.get(i).getUser_rating()))
				{
					copy_list.add(list.get(i))	;
				}	
			}
			//list.clear();
			list.addAll(copy_list);
		}*/
		numofselecteditem = noOfTotalSelectedItem(list);
		try
		{

			if(numofselecteditem == 0)
			{
				send_ralative_id.setVisibility(View.INVISIBLE);
				create_ralative_id.setVisibility(View.INVISIBLE);	
			}
			else
			{
				send_ralative_id.setVisibility(View.VISIBLE);
				create_ralative_id.setVisibility(View.VISIBLE);	
			}
			if(SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER) || SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_COACH))
			{
				send_ralative_id.setVisibility(View.INVISIBLE);
			}
		}
		catch(Exception e)
		{

		}

		countr.setText(String.valueOf(numofselecteditem+""));
		crete_counter.setText(String.valueOf(numofselecteditem+""));

		swipeAdapter = null;
		membersList_listView.setVisibility(View.VISIBLE);//bersd(mContext, list ,this);
		swipeAdapter = new MemberSwipeviewAdapter(mContext, list, this);

		/*swipeAdapter.setEventItemClickListener(new SwipeEventItemClickListener() {

			@Override
			public void OnEditClick(int position) {
				//membersList_listView.closeAnimate(position);
				AddMemberFragment.edit = true;
				AddMemberFragment.adminBean = result.get(position);
				ArrayList<MemberListBean> result1 = result;
				DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.Add_Memeber_Fragment_id);
			}

			@Override
			public void OnBlockClick(int position, int staus) {
				//membersList_listView.closeAnimate(position);
				MemberListBean bean = result.get(position);
				blockUser(bean, "" + staus);
			}
		});*/
		membersList_listView.setAdapter(swipeAdapter);

	}

	void blockUser(MemberListBean bean, String status) {
		/*try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("user_type", AppConstants.USER_TYPE_MEMBER));
			nameValuePairs.add(new BasicNameValuePair("user_club_id", SessionManager.getUser_Club_id(mContext)));
			nameValuePairs.add(new BasicNameValuePair("user_id", bean.getUser_id()));
			nameValuePairs.add(new BasicNameValuePair("user_status", status));
			if (InternetConnection.isInternetOn(mContext)) {
				// new ChangeUserStatus(mContext, nameValuePairs, new
				// BloackMemberListener()).execute();
			} else {
				ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.check_internet_connection));
			}
		} catch (Exception e) {
			ShowUserMessage.showUserMessage(mContext, e.toString());
		}*/
	}

	public class BloackMemberListener {
		public void onSuccess(String msg) {
			ShowUserMessage.showUserMessage(mContext, msg);
			getMembersList();
		}

		public void onError(String msg) {
			ShowUserMessage.showUserMessage(mContext, msg);
		}
	}



	/*public OnCheckedChangeListener checkboxclickListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton view, boolean isChecked) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			
			case R.id.all_rating_cb:
				if(isChecked)
				{
					user_rating.clear();
					user_rating.add(spinner_list.get(0));	

					two_half__rating_cb.setChecked(false);
					three_rating_cb.setChecked(false);
					three_half_rating_cb.setChecked(false);
					four_rating_cb.setChecked(false);
					four_half_rating_cb.setChecked(false);
					five_rating_cb.setChecked(false);
					five_half_rating_cb.setChecked(false);
					filterViaId(-1);
				}
				setInslilizeCheckbox();

				break;
			case R.id.two_half__rating_cb:
				
				
				
				if(isChecked)
				{
					all_rating_cb.setChecked(false);
					user_rating.remove("All");
					user_rating.add(spinner_list.get(1));
					filterViaId(selectionId);   
				}else
				{
					
					setInslilizeCheckbox();
				
					
					filterViaId(selectionId);
				}

				break;
			case R.id.three_rating_cb:
				if(isChecked)
				{
					all_rating_cb.setChecked(false);
					user_rating.remove("All");
					user_rating.add(spinner_list.get(2));
					filterViaId(selectionId);   
				}
				else
				{
					setInslilizeCheckbox();
					filterViaId(selectionId);
				}

				break;
			case R.id.three_half_rating_cb:
				if(isChecked)
				{
					all_rating_cb.setChecked(false);
					user_rating.remove("All");
					user_rating.add(spinner_list.get(3));
					filterViaId(selectionId);   
				}
				else
				{
					filterViaId(selectionId);
					setInslilizeCheckbox();
				}
				break;
			case R.id.four_rating_cb:
				if(isChecked)
				{
					all_rating_cb.setChecked(false);
					user_rating.remove("All");
					user_rating.add(spinner_list.get(4));
					filterViaId(selectionId);	
				}
				else
				{

					filterViaId(selectionId);
					setInslilizeCheckbox();
				}
				
				break;
			case R.id.four_half_rating_cb:
				if(isChecked)
				{
					all_rating_cb.setChecked(false);
					user_rating.remove("All");
					user_rating.add(spinner_list.get(5));
					filterViaId(selectionId);   
				}
				else
				{
					filterViaId(selectionId);
					setInslilizeCheckbox();
				}
				break;
			case R.id.five_rating_cb:
				if(isChecked)
				{
					all_rating_cb.setChecked(false);
					user_rating.remove("All");
					user_rating.add(spinner_list.get(6));
					filterViaId(selectionId);   
				}
				else
				{
					filterViaId(selectionId);
					setInslilizeCheckbox();
				}
				break;
			case R.id.five_half_rating_cb:
				if(isChecked)
				{
					
					all_rating_cb.setChecked(false);
					user_rating.remove("All");
					user_rating.add(spinner_list.get(7));
					filterViaId(selectionId);   
				}
				else
				{
					filterViaId(selectionId);
					setInslilizeCheckbox();
				}
				break;

			default:
				break;
			}
		}
	};
*/

	public OnClickListener checkboxclickListener1 = new OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.all_rating_cb:
				user_rating.add(spinner_list.get(0));
				filterViaId(-1);
				break;
			case R.id.two_half__rating_cb:
				user_rating.add(spinner_list.get(1));
				filterViaId(selectionId);
				break;
			case R.id.three_rating_cb:
				user_rating.add(spinner_list.get(2));
				filterViaId(selectionId);
				break;
			case R.id.three_half_rating_cb:
				user_rating.add(spinner_list.get(3));
				filterViaId(selectionId);
				break;
			case R.id.four_rating_cb:
				user_rating.add(spinner_list.get(4));
				filterViaId(selectionId);
				break;
			case R.id.four_half_rating_cb:
				user_rating.add(spinner_list.get(5));
				filterViaId(selectionId);
				break;
			case R.id.five_rating_cb:
				user_rating.add(spinner_list.get(6));
				filterViaId(selectionId);
				break;
			case R.id.five_half_rating_cb:
				user_rating.add(spinner_list.get(7));
				filterViaId(selectionId);
				break;

			default:
				break;
			}

		}
	};


	public class SelectCheckBox implements OnItemSelectedListener
	{



		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
		{


			if (position == 0)
			{
				return;
			}


			if (position == 1)
			{


				if (rattingArrayList.get(position).isItemSelected() == false)
				{
					rattingArrayList.get(position).setItemSelected(true);
					ratingSpinnerListAdapter.notifyDataSetChanged();
				}

				return;
			}

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	}

	public int noOfTotalSelectedItem(ArrayList<MemberListBean> list)
	{
		int noofcounter =0;
		String current_user_name = SessionManager.getFirstName(mContext)+" "+SessionManager.getLastName(mContext);

		for (int i = 0 ; i < list.size() ;i++)
		{
			if (list.get(i).getMemberSelection() == true)
			{

				String user_name =list.get(i).getUser_first_name()+" "+list.get(i).getUser_last_name().toLowerCase();



				if(! user_name.toLowerCase().equals(current_user_name.toLowerCase()) )
				{
					if (Integer.parseInt(list.get(i).getUser_status()) == 1)
					{
						noofcounter++ ;
					}


				}



			}
		}



		return noofcounter ;
	}



}
