package com.clubscaddy.fragment;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.MemberAutoCompleteAdapter;
import com.clubscaddy.Bean.EventBean;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.Bean.ScoreBean;
import com.clubscaddy.Bean.ScoreNumberBean;
import com.clubscaddy.Bean.ScoreOpponentBean;
import com.clubscaddy.Bean.Tournamenteventlistbean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import android.widget.RadioGroup.OnCheckedChangeListener;

public class EditScoreFragment extends Fragment implements OnClickListener

{
	String event_type= "";
	Button done;
	ArrayList<String> setsScore;
	int totalMatch = 0 ;
	boolean isWiner = false;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private GridView calendarView;
	private GridCellAdapter adapter;
	LinearLayout set5_linear_layout;
	LinearLayout set4_linear_layout ;
	private TextView currentMonth;
	Spinner selectSetET;
	Spinner set1MeET;
	Spinner set1OppET,set2MeET, set2OppET, set3MeET, set3OppET, set4MeET;
	TextView coPartnerTV ,opponent2TV,opponent1TV;
	Spinner   set4OppET, set5MeET, set5OppET;
	EditText tournament_et;
	View convertView ;
	ScoreBean scoreBean ;
	 RadioButton single;
	 RadioButton doublee;
	 ArrayList<String> selectedMembers;
	 private Button selectedDayMonthYearButton;
	 RadioGroup singleORdoubleRG;
	 TextView set4TV, set5TV;
	//Spinner scoreOwner ;
	AutoCompleteTextView copartner_ed ;
	AutoCompleteTextView player_et;
	EditText date_edit_txt ;
	private Calendar _calendar;
	private int month, year;
	AutoCompleteTextView opponent1_et ;
	AutoCompleteTextView opponent2_et;
	AlertDialog calendarDialogue;
	HashSet<String> selectedDateList;
	Calendar selected_calender;
	ArrayList<MemberListBean> membersList;
	ArrayList<MemberListBean> filteredList;
	
	ArrayList<String> numberOfSetsList;
	private static final String dateTemplate = "MMMM yyyy";
	String score_event_id = "";;
	String score_event_type = "1";
	String groupId = "-1";
	String coPartnerId = "";
	String opponent1Id = "";
	String opponent2Id = "";
	String playerId = "";
	String beforname ;
	String directorID = "";
	int selecteditempos =0;
	EventBean eventBean;
	public void setInstanse(ScoreBean scoreBean ,EventBean eventBean)
	{
	this.scoreBean = scoreBean;
		this.eventBean = eventBean;
	}
	
 @Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
 {
	// TODO Auto-generated method stub
	 convertView = inflater.inflate(R.layout.edit_score_layoyt, null);
	 tournament_et = (EditText) convertView.findViewById(R.id.tournament_et);
	
	 Tournamenteventlistbean bean = new Tournamenteventlistbean();
	 bean.setEvent_id("");
	 bean.setEvent_name("Not part of any tournament");
	 //tournamet_list.add(bean);
	 selected_calender = Calendar.getInstance();
	 
	 DirectorFragmentManageActivity.updateTitle("Edit Score");
	 
	/* TextView scoreOwner ;
		TextView copartnerTV ;*/
	 //TextView coPartnerTV ,opponent2TV,opponent1TV;
	 selectedMembers = new ArrayList<String>();
		
		
	 coPartnerTV = (TextView) convertView.findViewById(R.id.co_partner_tv);
	 opponent1TV = (TextView) convertView.findViewById(R.id.opponent1_tv);
	 opponent2TV = (TextView) convertView.findViewById(R.id.opponent2_tv);
	 
	 done = (Button) convertView.findViewById(R.id.done);
	 done.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			validateData();
		}
	});
	 
	 single = (RadioButton) convertView.findViewById(R.id.single);
	 doublee = (RadioButton) convertView.findViewById(R.id.doublee);
	 
	 singleORdoubleRG = (RadioGroup) convertView.findViewById(R.id.single_double_rg);
	 
	 singleORdoubleRG.setOnCheckedChangeListener(cheackChangeListener);
	 
	 player_et = (AutoCompleteTextView) convertView.findViewById(R.id.player_et);
	 copartner_ed = (AutoCompleteTextView) convertView.findViewById(R.id.co_partner_et);
	 opponent1_et = (AutoCompleteTextView) convertView.findViewById(R.id.opponent1_et);
	 opponent2_et = (AutoCompleteTextView) convertView.findViewById(R.id.opponent2_et);
	 setsScore = new ArrayList<String>();
	 mContext = getActivity();
	 if (SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_DIRECTOR)
				|| SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MOBILE_ADMIN)) {
		 player_et.setEnabled(true);
		 player_et.setClickable(true);
			directorID = SessionManager.getUser_id(mContext);
		} else {
			player_et.setEnabled(false);
			player_et.setClickable(false);
		}
	 
	 selectSetET = (Spinner) convertView.findViewById(R.id.set_selection_et);
	 selectSetET.setEnabled(false);
	 set1MeET = (Spinner) convertView.findViewById(R.id.set1_me);
		showSelector(set1MeET);
		set1OppET = (Spinner) convertView.findViewById(R.id.set1_opposite);
		showSelector(set1OppET);
		set2MeET = (Spinner) convertView.findViewById(R.id.set2_me);
		showSelector(set2MeET);
		set2OppET = (Spinner) convertView.findViewById(R.id.set2_opposite);
		showSelector(set2OppET);
		
		
		
		set3MeET = (Spinner) convertView.findViewById(R.id.set3_me);
		showSelector(set3MeET ,0);
		set3OppET = (Spinner) convertView.findViewById(R.id.set3_opposite);
		showSelector(set3OppET ,0);
		
		
		
		set4MeET = (Spinner) convertView.findViewById(R.id.set4_me);
		showSelector(set4MeET);
		set4OppET = (Spinner) convertView.findViewById(R.id.set4_opposite);
		showSelector(set4OppET);
		set5MeET = (Spinner) convertView.findViewById(R.id.set5_me);
		showSelector(set5MeET);
		set5OppET = (Spinner) convertView.findViewById(R.id.set5_opposite);
		showSelector(set5OppET);
		numberOfSetsList = new ArrayList<String>();
		numberOfSetsList.add("3");
		numberOfSetsList.add("5");
		showNoOfSetSelector();
		set5_linear_layout = (LinearLayout) convertView.findViewById(R.id.set5_linear_layout);
		set4_linear_layout = (LinearLayout) convertView.findViewById(R.id.set4_linear_layout);
set4TV = (TextView) convertView.findViewById(R.id.set4tv);
		
		set5TV = (TextView) convertView.findViewById(R.id.set5tv);
	

	 date_edit_txt = (EditText) convertView.findViewById(R.id.date_edit_txt);
		date_edit_txt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showCalendar();
			}
		});
		getMyEventList();
	 
	 player_et.addTextChangedListener(new TextWatcher() {
			
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
						
						getMembersList(s.toString() ,player_et ,3);
					}
					
				}
				
				String  name = SessionManager.getFirstName(getActivity())+" "+SessionManager.getLastName(getActivity());
					
				if(name.equalsIgnoreCase(beforname) )
				{
					
						
					player_et.setText("");	
				}
				
				
				
				
				
			}
		});
		 
	 copartner_ed.addTextChangedListener(new TextWatcher() {
			
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
				if(s.toString().length() != 0)
				{
					if(s.toString().length() == 1 && beforname.length() == 0)
					{	
						
						getMembersList(s.toString() ,copartner_ed,0);
					}
					
				}
				String coPatnerName = scoreBean.getCopartner();
				if(coPatnerName.equalsIgnoreCase(beforname)&& !Utill.isStringNullOrBlank(coPatnerName) )
				{
					try
					{
						copartner_ed.setText("");		
					}
					catch(StackOverflowError e)
					{
						
					}
					catch(Exception e)
					{
						
					}
						
					
				}
			}
		});

	 opponent1_et.addTextChangedListener(new TextWatcher() {
	
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
		if(s.toString().length() != 0)
		{
			if(s.toString().length() == 1 && beforname.length() == 0)
			{	
				
				getMembersList(s.toString() ,opponent1_et,1);
			}
			
		}
		String opponent1 = scoreBean.getOpponentList().get(0).getScore_opponent();
		if(opponent1.equalsIgnoreCase(beforname) )
		{
			
				
			opponent1_et.setText("");	
		}
	}
});

	 opponent2_et.addTextChangedListener(new TextWatcher() {
	
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
		if(s.toString().length() != 0)
		{
			if(s.toString().length() == 1 && beforname.length() == 0)
			{	
				
				getMembersList(s.toString() ,opponent2_et,2);
			}
			
		}
		String opponent2 = " ";
		if(scoreBean.getOpponentList().size()==2)
		{
			opponent2 = scoreBean.getOpponentList().get(1).getScore_opponent();	
		}
		 
		if(opponent2.equalsIgnoreCase(beforname) )
		{
			try
			{
				opponent2_et.setText("");		
			}
			catch(StackOverflowError e)
			{
				
			}
			catch(Exception e)
			{
				
			}
				
				
			
		}
	}
});
		
		
		
	 

	 copartner_ed.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				MemberListBean bean = (MemberListBean) arg0.getItemAtPosition(arg2);
				String name = bean.getUser_first_name() + " " + bean.getUser_last_name();
				copartner_ed.setText(name);
				selectedMembers.set(0, name);
				coPartnerId = bean.getUser_id();
			}
		});
	 opponent1_et.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				MemberListBean bean = (MemberListBean) arg0.getItemAtPosition(arg2);
				String name = bean.getUser_first_name() + " " + bean.getUser_last_name();
				opponent1_et.setText(name);
				selectedMembers.set(1, name);
				opponent1Id = bean.getUser_id();
			}
		});
	 opponent1_et.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				MemberListBean bean = (MemberListBean) arg0.getItemAtPosition(arg2);
				String name = bean.getUser_first_name() + " " + bean.getUser_last_name();
				opponent2_et.setText(name);
				selectedMembers.set(2, name);
				opponent2Id = bean.getUser_id();
			}
		});
	 
	 
	 
	return convertView;
}
 public void showCalendar() {
		selectedDateList = new HashSet<String>();

		calendarDialogue = new AlertDialog.Builder(getActivity()).create();
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.my_calendar_view, null);
		initializeCalendar(v);
		calendarDialogue.setView(v);
		calendarDialogue.show();
	}
 void initializeCalendar(View v) {
		_calendar = Calendar.getInstance(Locale.getDefault());
		month = _calendar.get(Calendar.MONTH) + 1;
		year = _calendar.get(Calendar.YEAR);
		Log.d("sss", "Calendar Instance:= " + "Month: " + month + " " + "Year: " + year);

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
		adapter = new GridCellAdapter(getActivity(), R.id.calendar_day_gridcell, month, year);
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
		
	}
 void setDataToView(final ScoreBean scoreBean) {


	 tournament_et.setEnabled(false);

	 tournament_et.setText(scoreBean.getEvent_name());
	 score_event_type = "2";
	 score_event_id = scoreBean.getScore_event_id();


	 player_et.setText(scoreBean.getCreater_name());
	// Utill.showDialg("Size  ", getActivity());
	 ArrayList<ScoreOpponentBean> opponents = scoreBean.getOpponentList();
		String ownerName = "";
		String opponent1 = "";
		String opponent2 = "";
		String copartner = "";
		ownerName = scoreBean.getCreater_name();
		copartner = scoreBean.getCopartner();
		selectedMembers.add(copartner);
		date_edit_txt.setText(scoreBean.getScore_add_date());
		
		if (Utill.isStringNullOrBlank(copartner)) {
			//copartnerTV.setVisibility(View.GONE);
			//myEnd.setVisibility(View.GONE);
		} else {
			copartner_ed.setText("" + copartner);
		}
		
		if (Utill.isStringNullOrBlank(copartner)) {
			
			single.setChecked(true);
			//copartnerTV.setVisibility(View.GONE);
			//myEnd.setVisibility(View.GONE);
		} else {
			doublee.setChecked(true);
		}
		
		
		if (opponents != null) {
			opponent1 = opponents.get(0).getScore_opponent();
			opponent1_et.setText(opponent1);
			if (opponents.size() > 1) {
				opponent2 = opponents.get(1).getScore_opponent();
				opponent2_et.setText(opponent2);
				//opponentEnd.setVisibility(View.VISIBLE);
				//opponent2_et.setVisibility(View.VISIBLE);
			} else {
				//opponentEnd.setVisibility(View.GONE);
				//opponent2TV.setVisibility(View.GONE);
			}
		}
		selectedMembers.add(opponent1+"");
		
		selectedMembers.add(opponent2+"");
		selectedMembers.add("" +ownerName);

		ArrayList<ScoreNumberBean> scoreNumbers = scoreBean.getScoreNumberList();
		
		
		if (scoreNumbers.size() == 3) {
			set4MeET.setVisibility(View.GONE);
			set4TV.setVisibility(View.GONE);
			set4OppET.setVisibility(View.GONE);
			set5MeET.setVisibility(View.GONE);
			set5TV.setVisibility(View.GONE);
			set5OppET.setVisibility(View.GONE);
			set5_linear_layout.setVisibility(View.GONE);
			set4_linear_layout.setVisibility(View.GONE);
			selectSetET.setSelection(0);
			
		} else  {
			set4MeET.setVisibility(View.VISIBLE);
			set4TV.setVisibility(View.VISIBLE);
			set4OppET.setVisibility(View.VISIBLE);
			set5MeET.setVisibility(View.VISIBLE);
			set5TV.setVisibility(View.VISIBLE);
			set5OppET.setVisibility(View.VISIBLE);
			set5_linear_layout.setVisibility(View.VISIBLE);
			set4_linear_layout.setVisibility(View.VISIBLE);
			selectSetET.setSelection(1);
		}
	
		for (int i = 0; i < scoreNumbers.size(); i++) {
			switch (i) {
			case 0:
				 //Toast.makeText(getActivity(),setsScore.indexOf(scoreNumbers.get(i).getMy_score())+ " sssssssss "+setsScore.indexOf(scoreNumbers.get(i).getScore_opponent1()), 1).show();
				set1MeET.setSelection(setsScore.indexOf(scoreNumbers.get(i).getMy_score()));
				set1OppET.setSelection(setsScore.indexOf(scoreNumbers.get(i).getScore_opponent1()));
				/*set1MeET.setText(scoreNumbers.get(i).getMy_score());
				set1OppET.setText(scoreNumbers.get(i).getScore_opponent1());*/
				break;
			case 1:
				set2MeET.setSelection(setsScore.indexOf(scoreNumbers.get(i).getMy_score()));
				set2OppET.setSelection(setsScore.indexOf(scoreNumbers.get(i).getScore_opponent1()));
				/*set2MeET.setText(scoreNumbers.get(i).getMy_score());
				set2OppET.setText(scoreNumbers.get(i).getScore_opponent1());*/
				break;
			case 2:
				set3MeET.setSelection(setsScore.indexOf(scoreNumbers.get(i).getMy_score()));
				set3OppET.setSelection(setsScore.indexOf(scoreNumbers.get(i).getScore_opponent1()));
			
				/*set3MeET.setText(scoreNumbers.get(i).getMy_score());
				set3OppET.setText(scoreNumbers.get(i).getScore_opponent1());
			*/	break;
			case 3:
				set4TV.setVisibility(View.VISIBLE);
				set4MeET.setVisibility(View.VISIBLE);
				set4OppET.setVisibility(View.VISIBLE);
				
				/*set4MeET.setText(scoreNumbers.get(i).getMy_score());
				set4OppET.setText(scoreNumbers.get(i).getScore_opponent1());*/
				set4MeET.setSelection(setsScore.indexOf(scoreNumbers.get(i).getMy_score()));
				set4MeET.setSelection(setsScore.indexOf(scoreNumbers.get(i).getScore_opponent1()));
			
				break;
			case 4:
				set5TV.setVisibility(View.VISIBLE);
				set5MeET.setVisibility(View.VISIBLE);
				set5OppET.setVisibility(View.VISIBLE);
				set5MeET.setSelection(setsScore.indexOf(scoreNumbers.get(i).getMy_score()));
				set5MeET.setSelection(setsScore.indexOf(scoreNumbers.get(i).getScore_opponent1()));
			
				/*set5MeET.setText(scoreNumbers.get(i).getMy_score());
				set5OppET.setText(scoreNumbers.get(i).getScore_opponent1());;*/
				break;
			}
		}

		/*
		
		score_show_creator_tv.setText(scoreBean.getCreater_name());
		
		if (Utill.isStringNullOrBlank(copartner)) {
			copartnerTV.setVisibility(View.GONE);
			opponent_end_symbol.setVisibility(View.GONE);
			own_end_symbol.setVisibility(View.GONE);
			score_show_copartnerTV.setVisibility(View.GONE);
			score_show_opponent_end_symbol.setVisibility(View.GONE);
		} else {
			score_show_copartnerTV.setText("" + copartner);
		}
		if (opponents != null) {
			opponent1 = opponents.get(0).getScore_opponent();
			score_show_opponent1_tv.setText(opponent1);
			if (opponents.size() > 1) {
				opponent2 = opponents.get(1).getScore_opponent();
				score_show_opponent2_tv.setText(opponent2);
				//opponentEnd.setVisibility(View.VISIBLE);
				score_show_opponent2_tv.setVisibility(View.VISIBLE);
			} else {
				score_show_own_end_symbol.setVisibility(View.GONE);
				score_show_opponent2_tv.setVisibility(View.GONE);
				
				
				score_show_own_end_symbol.setVisibility(View.GONE);
				score_show_opponent2_tv.setVisibility(View.GONE);
			}
		}
		
		
	
		*/

	}
 //ArrayList<Tournamenteventlistbean>tournamet_list = new ArrayList<Tournamenteventlistbean>();
	public void getMyEventList()
	{
	
	HashMap<String, String>	param = new HashMap<String, String>();
	param.put("event_participants", SessionManager.getUser_id(getActivity()));
	param.put("event_club_id", SessionManager.getUser_Club_id(getActivity()));
	

	if (Utill.isNetworkAvailable(getActivity())) {
		Utill.showProgress(getActivity());
		GlobalValues.getModelManagerObj(getActivity()).getMyEventsList(new ModelManagerListener() {
			@Override
			public void onSuccess(String json) {
				Utill.hideProgress();
				
				Log.e("rult", json);
				ArrayList<EventBean> my_event_list =	 JsonUtility.parseMyEventsList(json);
				
			
				for(int i =0 ;i < my_event_list.size() ; i++)
				{
					Tournamenteventlistbean bean = new Tournamenteventlistbean();
					bean.setEvent_id(my_event_list.get(i).getEvent_id());
					bean.setEvent_name(my_event_list.get(i).getEvent_event_name());
					//tournamet_list.add(bean);
					
				}
				playerId = scoreBean.getScore_user_id();
				coPartnerId = scoreBean.getScor_copartner();
				//opponent1Id = scoreBean.getOpponentList()
				//Utill.showDialg("Size  "+scoreBean.getOpponentList().size(), getActivity());
				
				if(scoreBean.getOpponentList().size()==1)
				{
				opponent1Id = scoreBean.getOpponentList().get(0).getScore_opponent_Id();	
				//Utill.showDialg("opponent1Id  "+opponent1Id, getActivity());
				}
				if(scoreBean.getOpponentList().size()==2)
				{
					opponent1Id = scoreBean.getOpponentList().get(0).getScore_opponent_Id();	
					//Utill.showDialg("opponent1Id  "+opponent1Id, getActivity());
				
					opponent2Id = scoreBean.getOpponentList().get(1).getScore_opponent_Id();
					//Utill.showDialg("opponent2Id  "+opponent2Id, getActivity());
				}
				
				//scoreBean.setTournament_event_list(tournamet_list);
				setDataToView(scoreBean);
				//
				
				/*ArrayList<GroupBean> groupList = JsonUtility.parseGroupList(json);
				GroupBean bean = new GroupBean();
				bean.setGroup_id("-1");
				bean.setGroup_name("Not part of any event");
				groupList.add(0, bean);
				AppConstants.setGroupList(groupList);

				ShowGroup();*/
				
			}

			@Override
			public void onError(String msg) {
				ShowUserMessage.showUserMessage(getActivity(), msg);
				Utill.hideProgress();
			}
		});
	} else {
		Utill.showNetworkError(getActivity());
	}
		
	}
	private void getMembersList(String keyName , final AutoCompleteTextView autoTV ,final int type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_type", AppConstants.USER_TYPE_SITE_ADMIN);
		params.put("user_club_id", SessionManager.getUser_Club_id(mContext));
		params.put("user_name",keyName);
		params.put("event_id", score_event_id);
		
		if (Utill.isNetworkAvailable(mContext)) {
			Utill.showProgress(mContext);
			GlobalValues.getModelManagerObj(mContext).getAllMembersOfClub(params, new ModelManagerListener() {

				@Override
				public void onSuccess(String json) {
					Utill.hideProgress();
					membersList = JsonUtility.parserMembersList(json, mContext);
				//	Utill.showDialg("Size   "+membersList.size(), mContext);
					
					int size = membersList.size();
					
					
					
					
					//Utill.showDialg("Size   "+membersList.size()+"   befire "+size, mContext);
					//AppConstants.setMembersList(membersList);
					filteredList = membersList;
					
					autoTV.setAdapter(new MemberAutoCompleteAdapter(mContext, R.id.textViewItem, membersList,autoTV));
				
					autoTV.setText(autoTV.getText().toString()+"");
					autoTV.setSelection(1);
					
					
					
					autoTV.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							
							AppConstants.hideSoftKeyboard(getActivity());
							
							
							MemberListBean bean = (MemberListBean) arg0.getItemAtPosition(arg2);
							String name = bean.getUser_first_name() + " " + bean.getUser_last_name();
							
							autoTV.setText(name);
							autoTV.setSelection(name.length());
							selectedMembers.set(type, name);
							if(type == 3)
							{
								playerId = bean.getUser_id();	
							}
							
							if(type==1)
							{
								opponent1Id = bean.getUser_id();
							}
							if(type == 0)
							{
							coPartnerId	 = bean.getUser_id();
							}
							if(type == 2)
							{
								opponent2Id	 = bean.getUser_id();
							}
						}
					});
					
					
					/*opponent1ET.setAdapter(new MemberAutoCompleteAdapter(mContext, R.id.textViewItem, membersList));
					opponent2ET.setAdapter(new MemberAutoCompleteAdapter(mContext, R.id.textViewItem, membersList));
					playerName.setAdapter(new MemberAutoCompleteAdapter(mContext, R.id.textViewItem, membersList));
			*/
				
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
	OnCheckedChangeListener cheackChangeListener = new RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			int id = checkedId;
			switch (id) {
			case R.id.single:
				copartner_ed.setVisibility(View.GONE);
				coPartnerTV.setVisibility(View.GONE);
				opponent2_et.setVisibility(View.GONE);
				opponent2TV.setVisibility(View.GONE);
				opponent1TV.setText("Opponent");
				opponent1_et.setVisibility(View.VISIBLE);
				opponent1TV.setVisibility(View.VISIBLE);
				event_type = "1";
				
				break;
			case R.id.doublee:
				copartner_ed.setVisibility(View.VISIBLE);
				coPartnerTV.setVisibility(View.VISIBLE);
				opponent2_et.setVisibility(View.VISIBLE);
				opponent2TV.setVisibility(View.VISIBLE);
				opponent1TV.setText("Opponent-1");
				opponent1_et.setVisibility(View.VISIBLE);
				opponent1TV.setVisibility(View.VISIBLE);
				event_type = "2";
				break;

			default:
				break;
			}

		}
	};
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public class GridCellAdapter extends BaseAdapter implements OnClickListener
	{
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
			Calendar calendar = Calendar.getInstance();
			setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
			setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
			
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

	
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.screen_gridcell, parent, false);
			}

			// Get a reference to the Day gridcell
			gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
			gridcell.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String[] day_color = list.get(position).split("-");
					String theday = day_color[0];
					String themonth = day_color[2];
					String theyear = day_color[3];
					Calendar currrnt_date_cal = Calendar.getInstance();
					Calendar selecred_date_cal = Calendar.getInstance();
					selecred_date_cal.set(Integer.parseInt(theyear), Utill.getMonthNumber(themonth)-1, Integer.parseInt(theday));
			
					currrnt_date_cal.set(Calendar.AM_PM, 0);
					currrnt_date_cal.set(Calendar.MINUTE, 0);
					currrnt_date_cal.set(Calendar.HOUR, 0);
					currrnt_date_cal.set(Calendar.HOUR_OF_DAY, 0);
					currrnt_date_cal.set(Calendar.SECOND, 0);
					currrnt_date_cal.set(Calendar.MILLISECOND, 0);
					
					
					selecred_date_cal.set(Calendar.AM_PM, 0);
					selecred_date_cal.set(Calendar.MINUTE, 0);
					selecred_date_cal.set(Calendar.HOUR, 0);
					selecred_date_cal.set(Calendar.HOUR_OF_DAY, 0);
					selecred_date_cal.set(Calendar.SECOND, 0);
					selecred_date_cal.set(Calendar.MILLISECOND, 0);


					Calendar eventStartedDate  = AppConstants.getCalenderFromAppDate(eventBean.getEvent_startdate());


					eventStartedDate.set(Calendar.AM_PM, 0);
					eventStartedDate.set(Calendar.MINUTE, 0);
					eventStartedDate.set(Calendar.HOUR, 0);
					eventStartedDate.set(Calendar.HOUR_OF_DAY, 0);
					eventStartedDate.set(Calendar.SECOND, 0);
					eventStartedDate.set(Calendar.MILLISECOND, 0);


					
					
				if(selecred_date_cal.compareTo(currrnt_date_cal)<=0)
				{

					if(selecred_date_cal.compareTo(eventStartedDate)<0)
					{
						Utill.showDialg("Please select current date or next date ", getActivity());

					}
					else
					{
						date_edit_txt.setText(AppConstants.getAppDate(selecred_date_cal));
						selected_calender.set(selecred_date_cal.get(Calendar.YEAR) , selecred_date_cal.get(Calendar.MONTH), selecred_date_cal.get(Calendar.DATE));

						calendarDialogue.cancel();
					}


					date_edit_txt.setText( AppConstants.getAppDate(selecred_date_cal) /*selecred_date_cal.get(Calendar.YEAR)+"/"+(selecred_date_cal.get(Calendar.MONTH)+1)+"/"+selecred_date_cal.get(Calendar.DATE)*/);
				calendarDialogue.cancel();	
				}
				else
				{
					
					Utill.showDialg("Please select current date or privious date " ,getActivity());
					//Toast.makeText(, , 1).show();
						
				}
				
				}
			
			
			});

			// ACCOUNT FOR SPACING

			//Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
			String[] day_color = list.get(position).split("-");
			String theday = day_color[0];
			String themonth = day_color[2];
			String theyear = day_color[3];
			String date = theday + "-" + Utill.getMonthNumber(themonth) + "-" + theyear;
		//	Toast.makeText(getActivity(),date,1).show();
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
			if (day_color[1].equals("WHITE")) {
				gridcell.setTextColor(getResources().getColor(R.color.black_color));
			}
			String currentDate = day_color[0]+"-"+day_color[2]+"-"+day_color[3];
			SimpleDateFormat currentDateformat = new SimpleDateFormat("d-MMMM-yyyy");
			String systemCurrentDate = currentDateformat.format(Calendar.getInstance().getTime());

			if (day_color[1].equals("BLUE") )
			{
				Log.e("current date" , systemCurrentDate+" "+currentDate);
				if (systemCurrentDate.equals(currentDate))
				{
					gridcell.setTextColor(getResources().getColor(R.color.white_color));
					gridcell.setBackground(getResources().getDrawable(R.drawable.calendar_bg_orange));
				}
				else
				{
					gridcell.setTextColor(getResources().getColor(R.color.black_color));
					//  gridcell.setBackground(getResources().getDrawable(R.drawable.calendar_bg_orange));
				}

			}
			else
			{

				if(selected_calender != null)
				{
					Log.e("Selected date", "with");
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d-M-yyyy");
					String  mySelectedDate = simpleDateFormat.format(selected_calender.getTime());
					if (mySelectedDate.equals(date))
					{
						gridcell.setBackground(getResources().getDrawable(R.color.blue_header));
					}
				}
			}




			return row;
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

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			String date_month_year = (String) view.getTag();
			
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
			//Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}
		if (v == nextMonth) {
			if (month > 11) {
				month = 1;
				year++;
			} else {
				month++;
			}
			//Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
			setGridCellAdapterToDate(month, year);
		}

		
	}

	private void setGridCellAdapterToDate(int month, int year) {
		adapter = new GridCellAdapter(getActivity(), R.id.calendar_day_gridcell, month, year);
		_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
		currentMonth.setText(DateFormat.format(dateTemplate, _calendar.getTime()));
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}
	
	
	public void showSelector(Spinner spinner_view) {
		
		setsScore.clear();
		int maxvalue =0 ;
		int counter =0 ;
		try
		{
			maxvalue =Integer.parseInt(SessionManager.getSport_type(getActivity())) ;
			if(maxvalue ==1)
			{
				counter	=7 ;
			}
			if(maxvalue ==2)
			{
				counter	= 30 ;
			}
			if(maxvalue ==3)
			{
				counter	= 99;
			}
			if(maxvalue ==4)
			{
				counter	=15;
			}
		}
		catch(Exception e)
		{
			
		}
		
		
		for (int i = 0; i <= counter; i++) {
			setsScore.add("" + i);
		}
		
		
		final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.listview, null);
		ListView listView = (ListView) v.findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, setsScore);
		spinner_view.setAdapter(adapter);
		
		//alertDialog.setView(v);
		//alertDialog.show();
	}
	
	
	public void showNoOfSetSelector() {
		final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.listview, null);
		ListView listView = (ListView) v.findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, numberOfSetsList);
		selectSetET.setAdapter(adapter);
		
		selectSetET.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				if (numberOfSetsList.get(position).equalsIgnoreCase("3")) {
					set4MeET.setVisibility(View.GONE);
					set4TV.setVisibility(View.GONE);
					set4OppET.setVisibility(View.GONE);
					set5MeET.setVisibility(View.GONE);
					set5TV.setVisibility(View.GONE);
					set5OppET.setVisibility(View.GONE);
					set5_linear_layout.setVisibility(View.GONE);
					set4_linear_layout.setVisibility(View.GONE);
					
				} else if (numberOfSetsList.get(position).equalsIgnoreCase("5")) {
					set4MeET.setVisibility(View.VISIBLE);
					set4TV.setVisibility(View.VISIBLE);
					set4OppET.setVisibility(View.VISIBLE);
					set5MeET.setVisibility(View.VISIBLE);
					set5TV.setVisibility(View.VISIBLE);
					set5OppET.setVisibility(View.VISIBLE);
					set5_linear_layout.setVisibility(View.VISIBLE);
					set4_linear_layout.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		/*selectSetET.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				if (numberOfSetsList.get(position).equalsIgnoreCase("3")) {
					set4MeET.setVisibility(View.GONE);
					set4TV.setVisibility(View.GONE);
					set4OppET.setVisibility(View.GONE);
					set5MeET.setVisibility(View.GONE);
					set5TV.setVisibility(View.GONE);
					set5OppET.setVisibility(View.GONE);
				} else if (numberOfSetsList.get(position).equalsIgnoreCase("5")) {
					set4MeET.setVisibility(View.VISIBLE);
					set4TV.setVisibility(View.VISIBLE);
					set4OppET.setVisibility(View.VISIBLE);
					set5MeET.setVisibility(View.VISIBLE);
					set5TV.setVisibility(View.VISIBLE);
					set5OppET.setVisibility(View.VISIBLE);
				}
			}
		});*/
		
		/*selectSetET.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				
				alertDialog.cancel();
			}
		});*/
		alertDialog.setView(v);
		//alertDialog.show();
	}
	Activity mContext ;
	
	void validateData() {
		mContext = getActivity();
		String playerStr = player_et.getText().toString();
		String opponent1Str = opponent1_et.getText().toString();
		String opponent2Str = opponent2_et.getText().toString();
		String copartnerStr = copartner_ed.getText().toString();
		String numberOfSetsStr = selectSetET.getSelectedItem().toString().trim();
		
		
		totalMatch =0 ;
		
		isWiner = false;
		
		
		
		
		if(selectSetET.getSelectedItem().toString().equals("3"))
		{
			  	
			 if(Integer.parseInt(set1MeET.getSelectedItem().toString())>Integer.parseInt(set1OppET.getSelectedItem().toString())) 
			{
				totalMatch++; 
			}
			 if(Integer.parseInt(set2MeET.getSelectedItem().toString())>Integer.parseInt(set2OppET.getSelectedItem().toString())) 
			{
				 totalMatch++; 		 
			}
			 if(Integer.parseInt(set3MeET.getSelectedItem().toString())>Integer.parseInt(set3OppET.getSelectedItem().toString())) 
			{
				 totalMatch++; 			 
			}
			 
			 if(totalMatch >=2)
			 {
				 isWiner = true;
			 }
		}
		else
		{
			 if(Integer.parseInt(set1MeET.getSelectedItem().toString())>Integer.parseInt(set1OppET.getSelectedItem().toString())) 
				{
				 totalMatch++; 	 
				}
				 if(Integer.parseInt(set2MeET.getSelectedItem().toString())>Integer.parseInt(set2OppET.getSelectedItem().toString())) 
				{
					 totalMatch++; 		 
				}
				 if(Integer.parseInt(set3MeET.getSelectedItem().toString())>Integer.parseInt(set3OppET.getSelectedItem().toString())) 
				{
					 totalMatch++; 			 
				}	
				 if(Integer.parseInt(set4MeET.getSelectedItem().toString())>Integer.parseInt(set4OppET.getSelectedItem().toString())) 
				{
					 totalMatch++; 			 
				}
				 if(Integer.parseInt(set5MeET.getSelectedItem().toString())>Integer.parseInt(set5OppET.getSelectedItem().toString())) 
				{
					 totalMatch++; 			 
				}	
				 if(totalMatch >=3)
				 {
					 isWiner = true;
				 }
		}
		
		//Toast.makeText(getActivity(), "Match is win "+isWiner, 1).show();
		
		
		
		
		
		
		if (Utill.isNetworkAvailable(mContext)) {
			Map<String, Object> params = new HashMap<String, Object>();
			if (Utill.isStringNullOrBlank(groupId)) {
				Utill.showToast(mContext, "Please select a Tournament.");
				return;
			} else if (Utill.isStringNullOrBlank(playerStr)) {
				Utill.showToast(mContext, "Please enter player name");
			}

			else 
				if(singleORdoubleRG.getCheckedRadioButtonId() == R.id.doublee && Utill.isStringNullOrBlank(copartnerStr)) 
				{
				Utill.showToast(mContext, "Please select valid score partner.");
			} 
				else 
					if (Utill.isStringNullOrBlank(opponent1Str)) 
			{
				if (singleORdoubleRG.getCheckedRadioButtonId() == R.id.doublee)
				{
					Utill.showToast(mContext, "Opponent-1 is not valid Member.");	
				}
				else
				{
					Utill.showToast(mContext, "Opponent is not valid Member.");	
				}
			} 
			else 
				if (!opponent1Str.equalsIgnoreCase(selectedMembers.get(1))) 
				{
					if (singleORdoubleRG.getCheckedRadioButtonId() == R.id.doublee)
					{
						Utill.showToast(mContext, "Opponent-1 is not valid Member.");	
					}
					else
					{
						Utill.showToast(mContext, "Opponent is not valid Member.");	
					}
					
			
			    }
				else 
					if(singleORdoubleRG.getCheckedRadioButtonId() == R.id.doublee && Utill.isStringNullOrBlank(opponent2Str)) {
				Utill.showToast(mContext, "Please Enter Opponent-2");
			} else if (singleORdoubleRG.getCheckedRadioButtonId() == R.id.doublee && !opponent2Str.equalsIgnoreCase(selectedMembers.get(2))) {
				Utill.showToast(mContext, "Opponent-2 is not valid Member.");
			} else 
				if (singleORdoubleRG.getCheckedRadioButtonId() == R.id.doublee && opponent1Str.equalsIgnoreCase(opponent2Str)) {
				Utill.showToast(mContext, "Both opponent can not be same.");
				/*	} else if (Utill.isStringNullOrBlank(set1MeET.getSelectedItem().toString())) {
				Utill.showToast(mContext, "Select your score in set-1");
			} else if (Utill.isStringNullOrBlank(set1OppET.getSelectedItem().toString())) {
				Utill.showToast(mContext, "Select Opponent score in set-1");
			} else if (Utill.isStringNullOrBlank(set2MeET.getSelectedItem().toString())) {
				Utill.showToast(mContext, "Select your score in set-2");
			} else if (Utill.isStringNullOrBlank(set2OppET.getSelectedItem().toString())) {
				Utill.showToast(mContext, "Select Opponent score in set-2");
			} else if (Utill.isStringNullOrBlank(set3MeET.getSelectedItem().toString())) {
				Utill.showToast(mContext, "Select your score in set-3");
			} else if (Utill.isStringNullOrBlank(set3OppET.getSelectedItem().toString())) {
				Utill.showToast(mContext, "Select Opponent score in set-3");
			} else if (numberOfSetsStr.equalsIgnoreCase("5") && Utill.isStringNullOrBlank(set4MeET.getSelectedItem().toString())) {
				Utill.showToast(mContext, "Select your score in set-4");
			} else if (numberOfSetsStr.equalsIgnoreCase("5") && Utill.isStringNullOrBlank(set4OppET.getSelectedItem().toString())) {
				Utill.showToast(mContext, "Select Opponent score in set-4");
			} else if (numberOfSetsStr.equalsIgnoreCase("5") && Utill.isStringNullOrBlank(set5MeET.getSelectedItem().toString())) {
				Utill.showToast(mContext, "Select your score in set-5");
			} else if (numberOfSetsStr.equalsIgnoreCase("5") && Utill.isStringNullOrBlank(set5OppET.getSelectedItem().toString())) {
				Utill.showToast(mContext, "Select Opponent score in set-5");*/
			} else {


				if (singleORdoubleRG.getCheckedRadioButtonId() == R.id.single) {
					if (playerId.equalsIgnoreCase(opponent1Id)) {
						Utill.showToast(mContext, "Both Player Can Not be same");
						return;
					}

					// if(playerId)
				} else if (singleORdoubleRG.getCheckedRadioButtonId() == R.id.doublee) {
					if (!Utill.isStringNullOrBlank(coPartnerId)) {
						if (coPartnerId.equalsIgnoreCase(playerId) || coPartnerId.equalsIgnoreCase(opponent1Id)
								|| coPartnerId.equalsIgnoreCase(opponent2Id)) {
							Utill.showToast(mContext, "Player Can Not be same");
							return;
						}
					}

					if (playerId.equalsIgnoreCase(coPartnerId) || playerId.equalsIgnoreCase(opponent1Id)
							|| playerId.equalsIgnoreCase(opponent2Id)) {
						Utill.showToast(mContext, "Player Can Not be same");
						return;
					}
					if (opponent1Id.equalsIgnoreCase(playerId) || opponent1Id.equalsIgnoreCase(coPartnerId)
							|| opponent1Id.equalsIgnoreCase(opponent2Id)) {
						Utill.showToast(mContext, "Player Can Not be same");
						return;
					}
					if (opponent2Id.equalsIgnoreCase(playerId) || opponent2Id.equalsIgnoreCase(coPartnerId)
							|| opponent2Id.equalsIgnoreCase(opponent1Id)) {
						Utill.showToast(mContext, "Player Can Not be same");
						return;
					}
				}


				params.put("event_name", scoreBean.getEvent_name()+"");
					params.put("score_creator_id", playerId);
				params.put("score_event_id", scoreBean.getScore_event_id());
				params.put("score_event_type", "2");
                params.put("score_id", scoreBean.getScore_id());
				params.put("score_user_id", SessionManager.getUser_id(mContext));
					params.put("sport_type", SessionManager. getSport_type( mContext));

				String match_type = "1";
				if (singleORdoubleRG.getCheckedRadioButtonId() == R.id.doublee) {
					match_type = "2";
					params.put("score_opponent2", opponent2Id);
				}
				params.put("score_match_type", match_type);

				if (singleORdoubleRG.getCheckedRadioButtonId() == R.id.doublee && !Utill.isStringNullOrBlank(copartnerStr)) {
					params.put("scor_copartner", coPartnerId);
				}

				params.put("score_opponent1", opponent1Id);

				JSONObject json = new JSONObject();
				JSONArray jArray = new JSONArray();
				JSONObject tempJson = new JSONObject();
				try {
					tempJson = new JSONObject();
					tempJson.put("my_score", set1MeET.getSelectedItem().toString());
					tempJson.put("opponent_score", set1OppET.getSelectedItem().toString());
					jArray.put(tempJson);
					tempJson = new JSONObject();
					tempJson.put("my_score", set2MeET.getSelectedItem().toString());
					tempJson.put("opponent_score", set2OppET.getSelectedItem().toString());
					jArray.put(tempJson);
					tempJson = new JSONObject();
					tempJson.put("my_score", set3MeET.getSelectedItem().toString());
					tempJson.put("opponent_score", set3OppET.getSelectedItem().toString());
					jArray.put(tempJson);
					if (numberOfSetsStr.equalsIgnoreCase("5")) {
						
						
						
						
						
						tempJson = new JSONObject();
						tempJson.put("my_score", set4MeET.getSelectedItem().toString());
						tempJson.put("opponent_score", set4OppET.getSelectedItem().toString());
						jArray.put(tempJson);
						tempJson = new JSONObject();
						tempJson.put("my_score", set5MeET.getSelectedItem().toString());
						tempJson.put("opponent_score", set5OppET.getSelectedItem().toString());
						jArray.put(tempJson);
					}
					json.put("score", jArray);
					
					String finalSetData = json.toString();
					params.put("score_scores", finalSetData);
					Log.e("finalSetData", finalSetData);
					//Utill.showToast(mContext, finalSetData);
					int win = whoWin();
					if (win == I_WON) {

					} else if (win == OPP_WON) {

					} else if (win == MATCH_DRAW) {
						Utill.showToast(mContext, "Match Draw Could'nt Shared");
						return;
					}

					
					
					
					if(date_edit_txt.getText().toString().equalsIgnoreCase("")||date_edit_txt.getText().toString()=="")
					{
						Utill.showToast(mContext, "Score date is empty");	
					}
					else
					{


					Calendar scoreDtae =	AppConstants.getCalenderFromAppDate(date_edit_txt.getText().toString());
						String score_date_str = scoreDtae.get(Calendar.YEAR)+"/"+(scoreDtae.get(Calendar.MONTH)+1)+"/"+scoreDtae.get(Calendar.DATE) ;

						params.put("score_date",score_date_str );
						
					if(isWiner)
					{
						Utill.showProgress(mContext);
						GlobalValues.getModelManagerObj(mContext).EditScore(params, new ModelManagerListener() {

							@Override
							public void onSuccess(String json) {
								
							getActivity().getSupportFragmentManager().popBackStack();
								Utill.hideProgress();
								// DirectorFragmentManageActivity.popBackStackFragment();
							}

							@Override
							public void onError(String msg) {
								Log.e("json", msg+"");
								if(msg.equals("You have won the match in first 2 sets, Ignore the 3rd set score?"))
								{
									AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

									// Setting Dialog Title
									alertDialog.setTitle(SessionManager.getClubName(getActivity()));

									// Setting Dialog Message
									alertDialog.setMessage(msg);

									// Setting Icon to Dialog
									
									// Setting Positive "Yes" Button
									alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,int which) {

										
										
										
										set3MeET.setSelection(0);
										set3OppET.setSelection(0);
										dialog.cancel();
										validateData();
										}
									});

									// Setting Negative "NO" Button
									alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,	int which) {
										// Write your code here to invoke NO event
										//Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
										dialog.cancel();
										}
									});

									// Showing Alert Message
									alertDialog.show();
								}
								else
								{
									Utill.showDialg(msg, mContext);	
								}
								Utill.hideProgress();
							}
						});		
					}
					else
					{
						Utill.showDialg("To avoid duplicate score, we allow only winner to report the scores" ,getActivity());
						
						//Toast.makeText(getActivity(), , Toast.LENGTH_LONG).show();
					}
						
					
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		} else {
			Utill.showNetworkError(mContext);
		}
	}
	public static final int I_WON = 100;
	public static final int OPP_WON = 200;
	public static final int MATCH_DRAW = 300;

	int whoWin() {
		int Iwin = 0;
		int oppWin = 0;
		String no = selectSetET.getSelectedItem().toString();
		int set1_ME = Integer.parseInt(set1MeET.getSelectedItem().toString());
		int set1_OP = Integer.parseInt(set1OppET.getSelectedItem().toString());
		if (set1_ME > set1_OP) {
			Iwin++;
		} else if (set1_ME < set1_OP) {
			oppWin++;
		}

		int set2_ME = Integer.parseInt(set2MeET.getSelectedItem().toString());
		int set2_OP = Integer.parseInt(set2OppET.getSelectedItem().toString());
		if (set2_ME > set2_OP) {
			Iwin++;
		} else if (set2_ME < set2_OP) {
			oppWin++;
		}

		int set3_ME = Integer.parseInt(set3MeET.getSelectedItem().toString());
		int set3_OP = Integer.parseInt(set3OppET.getSelectedItem().toString());
		if (set3_ME > set3_OP) {
			Iwin++;
		} else if (set3_ME < set3_OP) {
			oppWin++;
		}

		if (no.equalsIgnoreCase("5")) {
			int set4_ME = Integer.parseInt(set4MeET.getSelectedItem().toString());
			int set4_OP = Integer.parseInt(set4OppET.getSelectedItem().toString());
			if (set4_ME > set4_OP) {
				Iwin++;
			} else if (set4_ME < set4_OP) {
				oppWin++;
			}

			int set5_ME = Integer.parseInt(set5MeET.getSelectedItem().toString());
			int set5_OP = Integer.parseInt(set5OppET.getSelectedItem().toString());
			if (set5_ME > set5_OP) {
				Iwin++;
			} else if (set5_ME < set5_OP) {
				oppWin++;
			}
		}

		if (Iwin > oppWin) {
			return I_WON;
		} else if (Iwin < oppWin) {
			return OPP_WON;
		} else {
			return MATCH_DRAW;
		}
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	
		try
		{
			AppConstants.hideSoftKeyboard(getActivity());
		}
		catch(Exception e)
		{
			
		}
	}
public void showSelector(Spinner spinner_view , int counter) {
		
		setsScore.clear();
		int sport_type =0 ;
		
		
		ArrayList<String>score_list = new ArrayList<String>();
		
		
		
		try
		{
			sport_type =Integer.parseInt(SessionManager.getSport_type(mContext)) ;
			
			
			
			if(sport_type ==1)
			{
				counter	=7 ;
			}
			if(sport_type ==2)
			{
				counter	= 30 ;
			}
			if(sport_type ==3)
			{
				counter	= 99;
			}
			if(sport_type ==4)
			{
				counter	=11;
			}
		}
		catch(Exception e)
		{
			
		}
		
		//Toast.makeText(getActivity(), "counter   "+counter, 1).show();
		for (int i = 0; i <= counter; i++) {
			score_list.add("" + i);
		}
		
		
		final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.listview, null);
		ListView listView = (ListView) v.findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item, score_list);
		spinner_view.setAdapter(adapter);
		
		
		
		//alertDialog.setView(v);
		//alertDialog.show();
	}
}
