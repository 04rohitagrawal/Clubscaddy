package com.clubscaddy.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.MemberListListener;
import com.clubscaddy.custumview.ExpandableHeightListView;
import com.clubscaddy.custumview.HorizontalListView;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.EventParticipantsAdapter;
import com.clubscaddy.Adapter.GallaryImageAdapter;
import com.clubscaddy.Adapter.MemberAutoCompleteAdapter;
import com.clubscaddy.Adapter.MembersSelectionAdapter;
import com.clubscaddy.Adapter.PDFpageAdapter;
import com.clubscaddy.Bean.EventBean;
import com.clubscaddy.Bean.EventMemberBean;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.custumview.InstantAutoComplete;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.SqlListe;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.custumview.WebViewPager;
import com.clubscaddy.Server.WebService;


public class EventDetailFrageMent extends Fragment {

	String Tag = getClass().getName();
	public static FragmentManager mFragmentManager;
	public static Fragment mFragment;
	Context mContext;
	public static String eventId;
	public static EventBean eventBean;
	AQuery aQuery ;
	ImageView delete_iv;
	TextView delete_tv;
	ImageView notification_iv;
	TextView notification_tv;

	ImageView edit_btn;
	EditText email_notification_msg;

	LinearLayout  deadline_date_layout;
	Dialog modify_event_dialog ;
	ImageButton pdf_image;
	TextView pdf_tv;

	boolean iscrossVisivle = true;

	public static int pos;
	TextView eventName, deadLineDate, eventCoast, descrition, joinWithdrawlTV;
	LinearLayout editLL, deleteLL, addMemeberLL, sendNotificationLL;RelativeLayout pdfFileLL;
	ListView participantsListView;
	//ImageView joinWithdrawlIV;
	boolean isParticipant = false;
	TextView cancel_dialog_btn,email_send_btn , notification_send_btn ,discription_textview_status;
	EventParticipantsAdapter eventParticipantsAdapter ;
	HorizontalListView image_galary_id;
	TextView recently_play_match ;
	;
	ProgressDialog pd ;
	SessionManager sessionManager ;
	TextView event_type_tv;
	TextView event_status_tv;

	LinearLayout edit_mode_linear_layout;

	SqlListe sqlListe;

	boolean isMemberasDirector = false ;

	public static Fragment getInstance(FragmentManager mFragManager ) {
		if (mFragment == null) {
			mFragment = new EventDetailFrageMent();
		}
		return mFragment;
	}


	ArrayList<MemberListBean>eventModeratorList ;
	public void setArgemunt(ArrayList<MemberListBean>eventModeratorList )
	{
		this.eventModeratorList = eventModeratorList ;
	}

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
	TextView edit_tv;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e(Tag, "onCreateView");
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.event_detail, container, false);
		image_galary_id = (HorizontalListView) rootView.findViewById(R.id.image_galary_id);
		aQuery = new AQuery(getActivity());
		mContext = getActivity();

		deadline_date_layout = (LinearLayout) rootView.findViewById(R.id.deadline_date_layout);
		event_status_tv = (TextView) rootView.findViewById(R.id.event_status_tv);
		edit_tv = (TextView) rootView.findViewById(R.id.edit_tv);

		edit_mode_linear_layout = (LinearLayout) rootView.findViewById(R.id.edit_mode_linear_layout);

		deadLineDate = (TextView) rootView.findViewById(R.id.deadline_date);
		recently_play_match = (TextView) rootView.findViewById(R.id.recently_play_match);
		sessionManager = new SessionManager() ;

		sqlListe =new SqlListe(getActivity());


		event_type_tv = (TextView) rootView.findViewById(R.id.event_type);


		edit_btn = (ImageView) rootView.findViewById(R.id.edit_btn);

		delete_iv = (ImageView) rootView.findViewById(R.id.delete_iv);
		delete_tv = (TextView) rootView.findViewById(R.id.delete_tv);


		notification_iv = (ImageView) rootView.findViewById(R.id.notification_iv);
		notification_tv = (TextView) rootView.findViewById(R.id.notification_tv);

		pdf_image = (ImageButton) rootView.findViewById(R.id.image);
		pdf_tv = (TextView) rootView.findViewById(R.id.pdf_tv);







		try
		{
			isMemberasDirector = false ;
			for (int i =0 ; i< eventModeratorList.size() ;i++)
			{
				if(SessionManager.getUser_id(getActivity()).equals(eventModeratorList.get(i).getUser_id()))
				{


					isMemberasDirector = true ;
					break;
				}


			}

		}
		catch (Exception e)
		{

		}

		if (eventBean.getEvent_type() == 1)
		{
			event_type_tv.setText("Public");
			edit_mode_linear_layout.setVisibility(View.VISIBLE);
			//deadline_date_layout.setVisibility(View.VISIBLE);

		}
		else
		{
			//deadline_date_layout.setVisibility(View.GONE);
			deadLineDate.setText("NA");
			if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER)||SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_COACH))
			{
				if (isMemberasDirector == false && isUserExitsInParticipentList(SessionManager.getUser_id(getActivity())) == false)
					edit_mode_linear_layout.setVisibility(View.GONE);
			}


			event_type_tv.setText("Private");
		}
		//

		if(eventBean.getEvent_score() == 1 && eventBean.getEvent_state()  !=1 )
		{
			recently_play_match.setVisibility(View.VISIBLE);
		}
		else
		{
			recently_play_match.setVisibility(View.GONE);
		}



		recently_play_match.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub


				ScoreListFragment scoreListFragment = new ScoreListFragment();
				scoreListFragment.setEventId(eventBean.getEvent_id() ,eventBean.getEvent_event_name() ,eventModeratorList ,eventBean ,isMemberasDirector);
				getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame ,scoreListFragment ,"scoreListFragment").addToBackStack(null).commit();





			}
		});

		if (DirectorFragmentManageActivity.actionbar_titletext != null) {
			DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.event_detail));
		}

		if (DirectorFragmentManageActivity.backButton != null)
		{
			DirectorFragmentManageActivity.showBackButton();
			DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
		}
		String user_id = SessionManager.getUser_id(getActivity());

		isParticipant = false;

		if(eventBean.getParticipantsList() != null)
		{
			for(int i=0;i<eventBean.getParticipantsList().size();i++)
			{
				if(user_id.equalsIgnoreCase(eventBean.getParticipantsList().get(i).getUser_id()))
				{
					isParticipant = true;

					break;
				}
			}
		}


		DirectorFragmentManageActivity.showLogoutButton();
		initializeView(rootView);
		setOnClickListener();
		setDataToView();

		return rootView;
	}

	void setDataToView() {





		if (eventBean != null) {




			if (eventBean.getEvent_state() == 1)
			{
				event_status_tv.setText("Not started");
			}

			if (eventBean.getEvent_state() == 2)
			{
				event_status_tv.setText("Running");
			}



			if (eventBean.getEvent_state() == 3)
			{
				event_status_tv.setText("Closed");
			}

			//


			if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MOBILE_ADMIN) || SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_DIRECTOR) || isMemberasDirector)
			{
				joinWithdrawlTV.setText("SIGN UP");
				joinWithdrawlTV.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.evt_add_member_icon),null,null);

			}else
			{

				if(isParticipant)
				{
					joinWithdrawlTV.setText("WITHDRAW");
					joinWithdrawlTV.setGravity(Gravity.CENTER);
					joinWithdrawlTV.setCompoundDrawablesWithIntrinsicBounds( getResources().getDrawable(R.drawable.withdraw_member_icon),null,null,null);
					//joinWithdrawlIV.setImageResource(R.drawable.withdraw_member_icon);
				}
				else
				{
					joinWithdrawlTV.setText("SIGN UP");
					joinWithdrawlTV.setGravity(Gravity.CENTER);
					joinWithdrawlTV.setCompoundDrawablesWithIntrinsicBounds(  getResources().getDrawable(R.drawable.evt_add_member_icon),null,null,null);

					//joinWithdrawlIV.setImageResource(R.drawable.evt_add_member_icon);
				}
			}
			image_galary_id.setAdapter(new GallaryImageAdapter(getActivity(), eventBean.getthumnailList() ));

			image_galary_id.setOnItemClickListener(new OnItemClickListener()
			{

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3)
				{
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity() , EventFullImageViewActivity.class);
					intent.putExtra("path_list", eventBean.getEvent_attach_url());
					intent.putExtra("pos", String.valueOf(pos));
					startActivity(intent);
				}
			});


			eventName.setText(eventBean.getEvent_event_name());


			if (eventBean.getEvent_type() == 1)
			{
				deadLineDate.setText(eventBean.getEvent_signup_deadline_date());
			}

			eventCoast.setText(eventBean.getEvent_cost() +" ("+sessionManager.getCurrencyCode(getActivity())+")");
			descrition.setText(eventBean.getEventDescription());

			try
			{
				//Comment by andtoid developer for not usable code

				/*Calendar deadLine_date =Calendar.getInstance();
				deadLine_date.set(Calendar.MONTH, AppConstants.getMonthIndex(eventBean.getEvent_signup_deadline_date().split(" ")[2])-1);
				deadLine_date.set(Calendar.DAY_OF_MONTH,Integer.parseInt((eventBean.getEvent_signup_deadline_date().split(" ")[1])) );
				deadLine_date.set(Calendar.YEAR, Integer.parseInt((eventBean.getEvent_signup_deadline_date().split(" ")[3])));
				deadLine_date.set(Calendar.AM_PM, 0);
				deadLine_date.set(Calendar.MINUTE, 0);
				deadLine_date.set(Calendar.HOUR, 0);
				deadLine_date.set(Calendar.HOUR_OF_DAY, 0);
				deadLine_date.set(Calendar.SECOND, 0);
				deadLine_date.set(Calendar.MILLISECOND, 0);




				Calendar today_date =Calendar.getInstance();
				//today_date.set(Calendar.YEAR, AppConstants.getMonthIndex(eventBean.getEvent_signup_deadline_date().split(" ")[3]));
				today_date.set(Calendar.AM_PM, 0);
				today_date.set(Calendar.MINUTE, 0);
				today_date.set(Calendar.HOUR, 0);
				today_date.set(Calendar.HOUR_OF_DAY, 0);
				today_date.set(Calendar.SECOND, 0);
				today_date.set(Calendar.MILLISECOND, 0);
*/

				//Utill.showDialg(today_date.getTime()+"" +deadLine_date.getTime(), mContext);

				if (eventBean.getEvent_type() == 1)
				{



					{
						if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER)||SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_COACH))
						{

							if (isMemberasDirector == true)
							{
								if(eventBean.getEvent_state() == 3)
								{
									iscrossVisivle = false;
									addMemeberLL.setAlpha(0.4f);
									joinWithdrawlTV.setAlpha(0.4f);
									//joinWithdrawlIV.setAlpha(0.4f);
									addMemeberLL.setEnabled(false);
								}
							}
							else
							{
								iscrossVisivle = false;
								if(eventBean.getEvent_state() != 1)
								{

									addMemeberLL.setAlpha(0.4f);
									joinWithdrawlTV.setAlpha(0.4f);
									//joinWithdrawlIV.setAlpha(0.4f);
									addMemeberLL.setEnabled(false);
								}
							}





						}
						else
						{
							if(eventBean.getEvent_state() == 3)
							{
								iscrossVisivle = false;
								addMemeberLL.setAlpha(0.4f);
								joinWithdrawlTV.setAlpha(0.4f);
								//joinWithdrawlIV.setAlpha(0.4f);
								addMemeberLL.setEnabled(false);
							}
						}

					}
				}
				else
				{
					if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER)||SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_COACH))
					{

						if (isMemberasDirector ==true)
						{
							if(eventBean.getEvent_state() == 3)
							{
								iscrossVisivle = false;
								addMemeberLL.setAlpha(0.4f);
								joinWithdrawlTV.setAlpha(0.4f);
								//joinWithdrawlIV.setAlpha(0.4f);
								addMemeberLL.setEnabled(false);
							}
						}
						else
						{
							iscrossVisivle = false;
							if(eventBean.getEvent_state() != 1)
							{

								addMemeberLL.setAlpha(0.4f);
								joinWithdrawlTV.setAlpha(0.4f);
								//joinWithdrawlIV.setAlpha(0.4f);
								addMemeberLL.setEnabled(false);
							}
						}



					}
					else
					{
						if(eventBean.getEvent_state() == 3)
						{
							iscrossVisivle = false;
							addMemeberLL.setAlpha(0.4f);
							joinWithdrawlTV.setAlpha(0.4f);
							//joinWithdrawlIV.setAlpha(0.4f);
							addMemeberLL.setEnabled(false);
						}
					}

				}















			}
			catch(Exception e)
			{

			}
			eventParticipantsAdapter=	new EventParticipantsAdapter(EventDetailFrageMent.this, eventBean.getParticipantsList(), new DeleteMemberListener(),iscrossVisivle , eventBean.getEvent_state());
			participantsListView.setAdapter(eventParticipantsAdapter);
			ExpandableHeightListView.getListViewSize(participantsListView);

			if(eventBean.getPdfURL()!=null)
			{
				if(eventBean.getPdfURL().size()==0)
				{
					pdfFileLL.setAlpha(0.4f);
					pdfFileLL.setEnabled(false);
					pdf_image.setAlpha(0.4f);
					pdf_tv.setAlpha(0.4f);



					pdfFileLL.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							ShowPdfActivity fragment = new ShowPdfActivity();

							fragment.setInstanse(eventBean.getPdfURL());
							//getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment,"fragment").addToBackStack(null).commit();
						}
					});
				}
				else
				{

				}
			}else{


				pdfFileLL.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ShowPdfActivity fragment = new ShowPdfActivity();
						fragment.setInstanse(eventBean.getPdfURL());
						//Toast.makeText(getActivity(),"pdf file list size"+ eventBean.getPdfURL().size(), 1).show();
						//getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment,"fragment").addToBackStack(null).commit();

					}
				});
			}


		}
	}

	public class DeleteMemberListener {
		public void onClickDeleteMember(final int position) {
			HashMap<String, String>map = new HashMap<String, String>();
			final ProgressDialog pd = new ProgressDialog(getActivity());
			pd.setCancelable(false);
			pd.show();
			map.put("event_id",eventBean.getEvent_id());
			map.put("event_user_id",  eventBean.getParticipantsList().get(position).getUser_id());
			Log.e("vvvvvvvvv", "   "+ eventBean.getParticipantsList().get(position).getUser_id()+"   "+eventBean.getEvent_id());
			AQuery aqury = new AQuery(getActivity());
			aqury.ajax(WebService.eventwithdrow, map, JSONObject.class, new AjaxCallback<JSONObject>(){

				//10-01 19:01:32.107: E/vvvvvvvvv(15683): 295   37

				@Override
				public void callback(String url, JSONObject jsonObj, AjaxStatus status) {
					// TODO Auto-generated method stub
					super.callback(url, jsonObj, status);
					pd.dismiss();
					try
					{
						Log.e("aaaa", jsonObj+"");

						Utill.showDialg(jsonObj.getString("message"), getActivity());
						//Toast.makeText(getActivity(), jsonObj.getString("message"), Toast.LENGTH_LONG).show();


						if(jsonObj.getString("status").equalsIgnoreCase("true"))
						{
							eventBean.getParticipantsList().remove(position);
							eventParticipantsAdapter.notifyDataSetChanged();
							ExpandableHeightListView.getListViewSize(participantsListView);
						}

					}
					catch(Exception e)
					{
						//	Toast.makeText(getActivity(), e.getMessage(), 1).show();
					}
				}
			});

		}
	}
	TextView total_pdf_tv;

	void initializeView(View view) {
		memberMainList = new ArrayList<MemberListBean>();
		result = new ArrayList<MemberListBean>();
		eventName = (TextView) view.findViewById(R.id.event_name);


		eventCoast = (TextView) view.findViewById(R.id.event_cost);
		descrition = (TextView) view.findViewById(R.id.description);
		total_pdf_tv= (TextView) view.findViewById(R.id.total_pdf_tv);


		if(eventBean.getPdfURL().size()!=0)
		{
			total_pdf_tv.setVisibility(View.VISIBLE);
			total_pdf_tv.setText(String.valueOf(eventBean.getPdfURL().size()+""));
		}
		else
		{
			total_pdf_tv.setVisibility(View.INVISIBLE);
		}
		sendNotificationLL = (LinearLayout) view.findViewById(R.id.send_notificationLL);
		editLL = (LinearLayout) view.findViewById(R.id.edit);
		deleteLL = (LinearLayout) view.findViewById(R.id.delete);
		addMemeberLL = (LinearLayout) view.findViewById(R.id.add_member);
		pdfFileLL = (RelativeLayout) view.findViewById(R.id.pdf_icon);
		participantsListView = (ListView) view.findViewById(R.id.list);
		//joinWithdrawlIV = (ImageView) view.findViewById(R.id.joinWithdrawlIV);
		joinWithdrawlTV = (TextView) view.findViewById(R.id.join_withdrawl);




		if(SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER) ||SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_COACH) )
		{
			if(isMemberasDirector == false)
			{
				editLL.setVisibility(View.GONE);
				deleteLL.setVisibility(View.GONE);
				sendNotificationLL.setVisibility(View.GONE);
				pdfFileLL.setVisibility(View.GONE);

				LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) addMemeberLL.getLayoutParams();
				layoutParams.setMargins(20,10,20,10);

				addMemeberLL.setPadding(0,15,0,15);
				addMemeberLL.setLayoutParams(layoutParams);
				addMemeberLL.setOrientation(LinearLayout.HORIZONTAL);
				addMemeberLL.setBackgroundDrawable( getResources().getDrawable(R.drawable.blue_rect_back) );
				joinWithdrawlTV.setTextSize(getResources().getDimension(R.dimen.join_text_size));
//


				if (!isUserExitsInParticipentList(SessionManager.getUserId(getActivity())))
				{
					joinWithdrawlTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.evt_add_member_icon, 0, 0, 0);
					//joinWithdrawlTV.setCompoundDrawablesWithIntrinsicBounds(  getResources().getDrawable(R.drawable.evt_add_member_icon),0,0,0);

				}
				else
				{
					//joinWithdrawlTV.setCompoundDrawablesWithIntrinsicBounds( (R.drawable.withdraw_member_icon),0,0,0);

				}



				LinearLayout.LayoutParams tetxtviewParam = (LinearLayout.LayoutParams) joinWithdrawlTV.getLayoutParams();
				tetxtviewParam.setMargins(20,0,0,0);
			}


		}



	}

	void setOnClickListener() {
		if (SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MOBILE_ADMIN) || SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_DIRECTOR) || isMemberasDirector) {




			sendNotificationLL.setOnClickListener(new OnClickListener() {

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
							Utill.hideKeybord(getActivity() ,email_notification_msg );
							dialog.cancel();
						}
					});
					email_send_btn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub


							if(email_notification_msg.getText().toString()!=""&&!email_notification_msg.getText().toString().equalsIgnoreCase(""))
							{
								Map<String, Object> params = new HashMap<String, Object>();
								params.put("user_id", SessionManager.getUser_id(mContext));
								params.put("event_id", eventBean.getEvent_id());
								params.put("notify_via", "2");
								params.put("event_message", email_notification_msg.getText().toString());

								pd = new ProgressDialog(getActivity());
								pd.setCancelable(false);
								pd.show();


								Utill.hideKeybord(getActivity() ,email_notification_msg );


								aQuery.ajax(WebService.eventnotification, params, JSONObject.class, new AjaxCallback<JSONObject>(){


									@Override
									public void callback(String url, JSONObject jsonObj, AjaxStatus status) {
										// TODO Auto-generated method stub
										super.callback(url, jsonObj, status);
										//Toast.makeText(getActivity(), jsonObj.toString(), 1).show();
										try
										{
											//

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
					notification_send_btn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(email_notification_msg.getText().toString()!=""&&!email_notification_msg.getText().toString().equalsIgnoreCase(""))
							{
								Map<String, Object> params = new HashMap<String, Object>();
								params.put("user_id", SessionManager.getUser_id(mContext));
								params.put("event_id", eventBean.getEvent_id());

								params.put("notify_via", "1");
								params.put("event_message", email_notification_msg.getText().toString());

								pd = new ProgressDialog(getActivity());
								pd.setCancelable(false);
								pd.show();
								Utill.hideKeybord(getActivity() ,email_notification_msg );

								aQuery.ajax(WebService.eventnotification, params, JSONObject.class, new AjaxCallback<JSONObject>(){

									//10-01 19:01:32.107: E/vvvvvvvvv(15683): 295   37

									@Override
									public void callback(String url, JSONObject jsonObj, AjaxStatus status) {
										// TODO Auto-generated method stub
										super.callback(url, jsonObj, status);
										//Toast.makeText(getActivity(), jsonObj.toString(), 1).show();
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
								Utill.showDialg( "Enter Message", getActivity());
								//Toast.makeText(getActivity(), "Enter Message", Toast.LENGTH_LONG).show();
							}
						}
					});
					dialog.show();


				}
			});

			editLL.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					/*DisplayMetrics displaymetrics = new DisplayMetrics();
					getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
					int height = displaymetrics.heightPixels;
					int width = displaymetrics.widthPixels;

					 modify_event_dialog = new Dialog(getActivity());
					modify_event_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					modify_event_dialog.setCancelable(false);
					modify_event_dialog.setContentView(R.layout.modify_event_detail_layout);
					modify_event_dialog.getWindow().setLayout(width , LinearLayout.LayoutParams.WRAP_CONTENT);


					TextView header_tv = (TextView) modify_event_dialog.findViewById(R.id.header_tv);
					header_tv.setText(SessionManager.getClubName(getActivity()));
					TextView modify_tv = (TextView) modify_event_dialog.findViewById(R.id.modify_tv);
					TextView edit_tv = (TextView) modify_event_dialog.findViewById(R.id.edit_tv);
					TextView cancel_tv = (TextView) modify_event_dialog.findViewById(R.id.cancel_tv);
					modify_tv.setOnClickListener(new ModifyEventTextClickListener());
					edit_tv.setOnClickListener(new ModifyEventTextClickListener());
					cancel_tv.setOnClickListener(new ModifyEventTextClickListener());


					modify_event_dialog.show();*/





					EditEventFragment fragment = new EditEventFragment();
					fragment.setInstanse(eventBean ,eventModeratorList);
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "fragment").addToBackStack(null).commit();
					AddEventFragment.edit = false;



					
					/**/
					//	DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.AddEventsFragment_id);
				}
			});
			//10-01 19:01:32.107: E/vvvvvvvvv(15683): 295   37

			deleteLL.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub


					AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

					// Setting Dialog Title
					alertDialog.setTitle(SessionManager.getClubName(getActivity()));

					// Setting Dialog Message
					alertDialog.setMessage("Are you sure you want delete this event?");

					// Setting Icon to Dialog


					// Setting Positive "Yes" Button
					alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int which) {


							deleteEvent(eventBean.getEvent_id());
							dialog.cancel();
							// Write your code here to invoke YES event
							//   Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
						}
					});

					// Setting Negative "NO" Button
					alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to invoke NO event
							//  Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
							dialog.cancel();
						}
					});

					// Showing Alert Message
					alertDialog.show();


				}
			});
		}
		else {




			deleteLL.setAlpha(0.4f);
			delete_iv.setAlpha(0.4f);;
			delete_tv.setAlpha(0.4f);;
			delete_tv.setEnabled(false);

			sendNotificationLL.setAlpha(0.4f);
			notification_iv.setAlpha(0.4f);;
			notification_tv.setAlpha(0.4f);;
			sendNotificationLL.setEnabled(false);



			edit_btn.setAlpha(0.4f);
			editLL.setAlpha(0.4f);
			edit_tv.setAlpha(0.4f);
			editLL.setEnabled(false);
			editLL.setEnabled(false);


			
			
		/*	*/
		/*
*/


		}


		try
		{

			if ((SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_DIRECTOR) == true || SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MOBILE_ADMIN) == true) || isMemberasDirector)
			{
				if (eventBean.getEvent_state() == 3)
				{
					edit_btn.setAlpha(0.4f);
					editLL.setAlpha(0.4f);
					edit_tv.setAlpha(0.4f);
					editLL.setEnabled(false);
					editLL.setEnabled(false);
				}
			}


		}
		catch (Exception e)
		{

		}




		//if(eventBean.getEvent_finishdate().split(" ")[2])

		Calendar deadlinedate_date = Calendar.getInstance();


		Calendar today_date = Calendar.getInstance();





		deadlinedate_date.set(Calendar.HOUR, 0);
		deadlinedate_date.set(Calendar.HOUR_OF_DAY, 0);
		deadlinedate_date.set(Calendar.MINUTE, 0);
		deadlinedate_date.set(Calendar.SECOND, 0);
		deadlinedate_date.set(Calendar.MILLISECOND, 0);
		deadlinedate_date.set(Calendar.AM_PM, Calendar.AM);


		today_date.set(Calendar.HOUR, 0);
		today_date.set(Calendar.HOUR_OF_DAY, 0);
		today_date.set(Calendar.MINUTE, 0);
		today_date.set(Calendar.SECOND, 0);
		today_date.set(Calendar.MILLISECOND, 0);
		today_date.set(Calendar.AM_PM, Calendar.AM);



		try
		{
			//	eventBean.getEvent_finishdate()


			deadlinedate_date.set(Integer.parseInt(eventBean.getEvent_signup_deadline_date().split(" ")[3]), getMonthIndex(eventBean.getEvent_signup_deadline_date().split(" ")[2])-1,Integer.parseInt(eventBean.getEvent_signup_deadline_date().split(" ")[1]) );

			//end_date.add(Calendar.DAY_OF_MONTH, 14);


		}
		catch(Exception e)
		{

		}


		int status =deadlinedate_date.getTime().compareTo(today_date.getTime());

		//SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

		//


		if(status == -1)
		{


			//Utill.showDialg(dead_line_date.getTime()+" "+today_date.getTime(), mContext);
		/*	edit_btn.setAlpha(0.4f);
			editLL.setAlpha(0.4f);
			edit_tv.setAlpha(0.4f);
			editLL.setEnabled(false);*/


		}
		addMemeberLL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_DIRECTOR) || SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MOBILE_ADMIN) || isMemberasDirector)
				{
					/*if (memberMainList.size() == 0)
					{
						if(Utill.isNetworkAvailable(getActivity()))
						{
							showMemberSelector();
						}
					} else
					{
						showMemberSelector();
					}	*/

					AddParticipantInEvent addParticipantInEvent = new AddParticipantInEvent();
					addParticipantInEvent.setInstanse(EventDetailFrageMent.this);

					addParticipantInEvent.show(getFragmentManager(),"");

					//getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame , addParticipantInEvent ,null).addToBackStack(null).commit();

				}else{
					if(isParticipant){
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

						// Setting Dialog Title
						alertDialog.setTitle(SessionManager.getClubName(getActivity()));

						// Setting Dialog Message
						alertDialog.setMessage("Are you sure you want withdraw this event?");

						// Setting Icon to Dialog


						// Setting Positive "Yes" Button
						alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int which) {

								WithDrawl(SessionManager.getUser_id(mContext));
								dialog.cancel();
								// Write your code here to invoke YES event
								// Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
							}
						});

						// Setting Negative "NO" Button
						alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// Write your code here to invoke NO event
								// Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
								dialog.cancel();
							}
						});

						// Showing Alert Message
						alertDialog.show();

					}else{

						AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

						// Setting Dialog Title
						alertDialog.setTitle(SessionManager.getClubName(getActivity()));

						// Setting Dialog Message
						alertDialog.setMessage(sessionManager.getCurrencyCode(getActivity())+""+eventBean.getEvent_cost()+" will be added in your bill.");

						// Setting Icon to Dialog


						// Setting Positive "Yes" Button
						alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int which) {


								joinEvntByDirector(SessionManager.getUser_id(mContext));
								dialog.cancel();
								// Write your code here to invoke YES event
							}
						});

						// Setting Negative "NO" Button
						alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// Write your code here to invoke NO event
								dialog.cancel();
							}
						});

						// Showing Alert Message
						alertDialog.show();

					}
				}
			}
		});
		pdfFileLL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPDF(eventBean);
			}
		});
		;
	}
	String beforname ;
	void deleteEvent(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("event_club_id", SessionManager.getUser_Club_id(mContext));
		params.put("event_id", id);
		if (Utill.isNetworkAvailable(getActivity())) {
			Utill.showProgress(mContext);
			GlobalValues.getModelManagerObj(mContext).deleteEvent(params, new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					Utill.hideProgress();
					try
					{
						JSONObject  jsonObject = new JSONObject(json);

						if (jsonObject.getBoolean("status"))
						{
							ShowUserMessage.showMessageForFragmeneWithBack(getActivity() , jsonObject.getString("message"));

						}
						else
						{
							ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("message"));

						}
					}
					catch (Exception e)
					{

					}



				}

				@Override
				public void onError(String msg) {
					//	ShowUserMessage.showUserMessage(mContext, msg);
					Utill.hideProgress();
				}
			});
		} else {
			Utill.showNetworkError(mContext);
		}

	}

	AutoCompleteTextView searchET;

	MembersSelectionAdapter adapter;
	AlertDialog memeberPopUp;
	ArrayList<MemberListBean>memberMainList1 = new ArrayList<MemberListBean>();
	void showMemberSelector() {
		memeberPopUp = new AlertDialog.Builder(mContext).create();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.member_item_layout, null);
		searchET = (AutoCompleteTextView) v.findViewById(R.id.search_member);
		searchET.addTextChangedListener(searchWatcher);


		searchET.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				//Toast.makeText(getActivity(), "KeyCode "+keyCode, 1).show();

				if(keyCode == 66)
				{
					InputMethodManager inputManager =
							(InputMethodManager)getActivity().
									getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(
							searchET.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});
		
/*ArrayList<MemberListBean>copy_memberMainList = new ArrayList<MemberListBean>();
		boolean match = false ;
		memberMainList.clear();
		memberMainList.addAll(memberMainList1);
		for(int i = 0 ; i< memberMainList.size();i++)
		{
			//Toast.makeText(getActivity(), memberMainList.get(i).getUser_first_name()+" "+memberMainList.get(i).getUser_last_name(), 1).show();
			
				
			for(int j = 0 ; j< eventBean.getParticipantsList().size();j++)
			{
					
			if(eventBean.getParticipantsList().get(j).getUser_name().equalsIgnoreCase(memberMainList.get(i).getUser_first_name()+" "+memberMainList.get(i).getUser_last_name()))
			{
				//Toast.makeText(getActivity(), eventBean.getParticipantsList().get(j).getUser_name()+" - "+memberMainList.get(i).getUser_first_name()+" "+memberMainList.get(i).getUser_last_name(), 1).show();
				
				match = true;
				//memberMainList.remove(i);
			}
			}	
			if(match == false)
			copy_memberMainList.add(memberMainList.get(i))	;
			
			match= false;
		}
		
		//copy_memberMainList.addAll(memberMainList);
		
		memberMainList.clear();
		memberMainList.addAll(copy_memberMainList);
		adapter = new MembersSelectionAdapter(mContext, copy_memberMainList);
		//listView.setAdapter(adapter);
		*/


		memeberPopUp.setView(v);
		memeberPopUp.setCancelable(true);
		memeberPopUp.show();
	}

	TextWatcher searchWatcher = new TextWatcher() {

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
				//Toast.makeText(getActivity(), "" +(s.toString().length() == 1 && beforname.length() == 0), 1).show();
				if(s.toString().length() == 1 && beforname.length() == 0)
				{

					Utill.showProgress(mContext);
					getMembersList(s.toString() ,searchET ,AppConstants.AllMEMBERlIST);





					searchET.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
						{
							/**/
							searchET.setText(adpter.getResultList().get(arg2).getUser_first_name()+" "+adpter.getResultList().get(arg2).getUser_last_name());
							joinEvntByDirector(adpter.getResultList().get(arg2).getUser_id());
							memeberPopUp.dismiss();
							//Utill.showDialg(adpter.getResultList().get(arg2).getUser_id(), mContext);
						}
					});
				}


			}


			/*filterList(str);
			getMembersList();*/
		}
	};

	ArrayList<MemberListBean> result;
	ArrayList<MemberListBean> memberMainList;

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
			adapter = new MembersSelectionAdapter(mContext, result);
			//listView.setAdapter(adapter);
			// setEventAdapter(result);
		} else {
			Utill.showToast(mContext, "No text found.");
			result = memberMainList;
			adapter = new MembersSelectionAdapter(mContext, result);
			//listView.setAdapter(adapter);
		}
	}


	void WithDrawl(final String user_id) {

		if (Utill.isNetworkAvailable(getActivity())) {
			Utill.showProgress(mContext);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("event_user_id",user_id);
			params.put("event_id", eventBean.getEvent_id());

			GlobalValues.getModelManagerObj(mContext).WithDrawlFromEvent(params, new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					joinWithdrawlTV.setText("SIGN UP");
					joinWithdrawlTV.setCompoundDrawablesWithIntrinsicBounds( getResources().getDrawable(R.drawable.evt_add_member_icon),null,null ,null);

					//joinWithdrawlIV.setImageResource(R.drawable.evt_add_member_icon);
					isParticipant = false;

					for(int i = 0 ;i < eventBean.getParticipantsList().size() ;i++)
					{
						if(user_id.equalsIgnoreCase(eventBean.getParticipantsList().get(i).getUser_id()))
						{
							eventBean.getParticipantsList().remove(i);
							eventParticipantsAdapter.notifyDataSetChanged();
							ExpandableHeightListView.getListViewSize(participantsListView);
						}
					}


                    if (eventBean.getEvent_type() == 2)
					{
						if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_MEMBER)||SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_COACH))
						{
							if (isMemberasDirector == false && isUserExitsInParticipentList(SessionManager.getUser_id(getActivity())) == false)
								edit_mode_linear_layout.setVisibility(View.GONE);
						}
					}




					ShowUserMessage.showUserMessage(mContext, json);
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
	MemberAutoCompleteAdapter adpter ;


	private void getMembersList(String keyWord, final AutoCompleteTextView autocompletetv , int userType) {
		sqlListe.getMemberList(getActivity(), new MemberListListener() {
			@Override
			public void onSuccess(ArrayList<MemberListBean> alMemberList)
			{
				ArrayList<MemberListBean> tempMemberList = alMemberList;
				ArrayList<EventMemberBean> existMember = eventBean.getParticipantsList();
				alMemberList = tempMemberList;
				if (alMemberList.size() > 0) {
					memberMainList = alMemberList;
					memberMainList1.addAll(memberMainList);
					result = memberMainList;
					adpter = new MemberAutoCompleteAdapter(getActivity(),
							R.id.textViewItem,memberMainList ,autocompletetv);
					autocompletetv.setAdapter(adpter);
					autocompletetv.setText(autocompletetv.getText().toString()+"");
					autocompletetv.setSelection(1);
					//showMemberSelector();
					// setEventAdapter(alMemberList);
				} else {
					ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.no_record_found));
				}

			}
		},userType ,keyWord);
	}


	/*private void getMembersList(String key_name , final AutoCompleteTextView searchEt)
	{
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("user_type",AppConstants.USER_TYPE_SITE_ADMIN);
			params.put("user_club_id", SessionManager.getUser_Club_id(mContext));
			params.put("user_name", key_name);

			GlobalValues.getModelManagerObj(mContext).getAllMembersOfClub(params, new ModelManagerListener() {

				@Override
				public void onSuccess(String json) {
					Utill.hideProgress();
					ArrayList<MemberListBean> alMemberList = JsonUtility.parserMembersList(json, mContext);
					ArrayList<MemberListBean> tempMemberList = alMemberList;
					ArrayList<EventMemberBean> existMember = eventBean.getParticipantsList();
					alMemberList = tempMemberList;
					if (alMemberList.size() > 0) {
						memberMainList = alMemberList;
						memberMainList1.addAll(memberMainList);
						result = memberMainList;
						adpter = new MemberAutoCompleteAdapter(getActivity(), R.id.textViewItem,memberMainList ,searchEt);
						searchEt.setAdapter(adpter);
						searchEt.setText(searchEt.getText().toString()+"");
						searchEt.setSelection(1);
						//showMemberSelector();
						// setEventAdapter(alMemberList);
					} else {
						ShowUserMessage.showUserMessage(mContext, getResources().getString(R.string.no_record_found));
					}
				}

				@Override
				public void onError(String msg) {
					Utill.hideProgress();
				}
			});
		} catch (Exception e) {
			ShowUserMessage.showUserMessage(mContext, e.toString());
		}
	}*/

	void joinEvntByDirector(String userId) {

		if (Utill.isNetworkAvailable(getActivity())) {
			Utill.showProgress(mContext);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("event_club_id", SessionManager.getUser_Club_id(mContext));
			params.put("event_user_id", userId);
			params.put("event_id", eventBean.getEvent_id());
			//	10-03 12:35:12.065: E/update participent user(9206): {"Response":{"user_profilepic":"http:\/\/72.167.41.165\/\/appwebservices\/\/user_images\/1441634730.jpeg","user_name":"yp hd","user_id":"326"},"message":"Join Successfully","status":"true"}

			GlobalValues.getModelManagerObj(mContext).joinEventReply(params, new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					//	ShowUserMessage.showUserMessage(mContext, json);
					Utill.hideProgress();
					EventMemberBean eventBean1 = new EventMemberBean();
					try
					{
						JSONObject jsonObj = new JSONObject(json);
						JSONObject ResponseObj = new JSONObject(jsonObj.getString("response"));
						eventBean1.setUser_id(ResponseObj.getString("user_id"));
						eventBean1.setUser_name(ResponseObj.getString("user_name"));
						eventBean1.setUser_profilepic(ResponseObj.getString("user_profilepic"));
						eventBean1.setUser_email(ResponseObj.getString("user_email"));
						eventBean1.setUser_no(ResponseObj.getString("user_phone"));
						eventBean.getParticipantsList().add(eventBean1)	;
						eventParticipantsAdapter.notifyDataSetChanged();
						ExpandableHeightListView.getListViewSize(participantsListView);

						isParticipant = true;

						if((SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER)||SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_COACH))  && isMemberasDirector == false)
						{
							joinWithdrawlTV.setText("WITHDRAW");
							joinWithdrawlTV.setCompoundDrawablesWithIntrinsicBounds( getResources().getDrawable(R.drawable.withdraw_member_icon),null,null,null);

							//joinWithdrawlIV.setImageResource(R.drawable.withdraw_member_icon);
						}
						Utill.showDialg(jsonObj.getString("message"), getActivity());
						//Toast.makeText(getActivity(), jsonObj.getString("message"), Toast.LENGTH_LONG).show();
					}
					catch(Exception e)
					{
						//Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();	;
					}

					//Log.e("update participent user ", json+"");

					if (memeberPopUp != null) {
						memeberPopUp.cancel();
					}
					// getAllEventsByClub();
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

	OnClickListener onClicks = new OnClickListener() {

		@Override
		public void onClick(View v) {

		}
	};

	public void getEvetnDetail() {

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
				DirectorFragmentManageActivity.popBackStackFragment();
			} catch (Exception e) {
				ShowUserMessage.showUserMessage(mContext, e.toString());
			}
		}
	};

	void showPDF(EventBean bean) {
		try {
			final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar);
			LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			View vi = li.inflate(R.layout.custom_view_pager, null, false);
			dialog.setContentView(vi);
			TextView backButton = (TextView) vi.findViewById(R.id.back_button);
			LinearLayout backLinear = (LinearLayout) vi.findViewById(R.id.backLinear);
			WebViewPager pager = (WebViewPager) vi.findViewById(R.id.view_pager);
			final ImageView sendComment = (ImageView) vi.findViewById(R.id.send_comment);
			final LinearLayout commentLayout = (LinearLayout) vi.findViewById(R.id.comment_view);
			TextView header = (TextView) vi.findViewById(R.id.header);
			header.setText("Event Pdf");
			sendComment.setVisibility(View.GONE);
			commentLayout.setVisibility(View.GONE);
			pager.setVisibility(View.VISIBLE);

			pager.setAdapter(new PDFpageAdapter(getActivity(), bean.getPdfURL())    );
			backButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			backLinear.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			vi.findViewById(R.id.like_btn).setVisibility(View.GONE);
			vi.findViewById(R.id.comment_btn).setVisibility(View.GONE);

			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}





	public int getMonthIndex(String month)
	{

		if(month.equalsIgnoreCase("January"))
		{
			return 1;
		}
		if(month.equalsIgnoreCase("February"))
		{
			return 2;
		}
		if(month.equalsIgnoreCase("March"))
		{
			return 3;
		}
		if(month.equalsIgnoreCase("April"))
		{
			return 4;
		}
		if(month.equalsIgnoreCase("May"))
		{
			return 5;
		}
		if(month.equalsIgnoreCase("June"))
		{
			return 6;
		}
		if(month.equalsIgnoreCase("July"))
		{
			return 7;
		}
		if(month.equalsIgnoreCase("August"))
		{
			return 8;
		}


		if(month.equalsIgnoreCase("September"))
		{
			return 9;
		}
		if(month.equalsIgnoreCase("October"))
		{
			return 10;
		}

		if(month.equalsIgnoreCase("November"))
		{
			return 11;
		}
		if(month.equalsIgnoreCase("December"))
		{
			return 12;
		}
		return 0;
	}

	public boolean isUserExitsInParticipentList(String userId)
	{
		Log.e("Start" , "shdgf");
		boolean isMemberExit = false ;

		for (int i = 0 ; i < eventBean.getParticipantsList().size() ;i++)
		{
			if (eventBean.getParticipantsList().get(i).getUser_id().equals(userId))
			{
				isMemberExit = true ;
				break;
			}
		}

		return isMemberExit ;
	}







}
