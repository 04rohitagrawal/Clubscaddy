package com.clubscaddy.fragment;

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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.custumview.ExpandableHeightListView;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.MemberPicNameAdapter;
import com.clubscaddy.Bean.BroadcastDetailBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.custumview.CustomScrollView;

public class BroadCastDetailFrageMent extends Fragment{
	CustomScrollView scollview ;

	String Tag = getClass().getName();
	public static FragmentManager mFragmentManager;
	public static Fragment mFragment;
	Context mContext;
	TextView unread_list_count_tv;
	LinearLayout delete_linear_layout,yes_no_btn_linear_layout;
//	public static BroadcastPollingBean broadCastBean;
  Button yes_btn ,no_ntn;
  static int status;
  TextView delete_btn;
	public static String  broadCastId;
	AQuery aQuery ;
	LinearLayout mainView,yesNoLL;
	TextView messageTV,readTitleTV,unreadTitleTV,notAnsweredTV,yesTV,noTV;
	ListView readLV,unreadLV,notAnswedLV,unreadble_match_list;
	
	LinearLayout answerButton;
	TextView unreadListTitle;
	//TextView unreadble_heading;
	
ProgressDialog pd ;
	
	public static Fragment getInstance(FragmentManager mFragManager ,int status){
		BroadCastDetailFrageMent.status = status;
		if(mFragment == null){
			mFragment = new BroadCastDetailFrageMent();
		}
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
		Log.e(Tag, "onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e(Tag, "onCreateView");
		 super.onCreateView(inflater, container, savedInstanceState);
		 View rootView = inflater.inflate(R.layout.broadcastdetail, container, false);
		 mContext = getActivity();
		 aQuery = new AQuery(getActivity());
		 if(DirectorFragmentManageActivity.actionbar_titletext!=null){
			// if(broadCastBean.getBroadcast_type().equalsIgnoreCase(AppConstants.BROADCAST))
				 DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.broadcast));
			 /*else{
				 DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.polling));
			 }*/
		 }
		 scollview =  (CustomScrollView) rootView.findViewById(R.id.scollview);
		 delete_linear_layout = (LinearLayout) rootView.findViewById(R.id.delete_linear_layout);
		 delete_btn = (TextView) rootView.findViewById(R.id.delete_btn);
		 yes_no_btn_linear_layout = (LinearLayout) rootView.findViewById(R.id.yes_no_btn_linear_layout);
	
		 unread_list_count_tv = (TextView) rootView.findViewById(R.id.unread_list_count_tv);
		 
		 if(status ==2)
		 {
			 delete_linear_layout.setVisibility(View.GONE);	 
		 }
		// if(status ==2)
		 {
			 yes_no_btn_linear_layout.setVisibility(View.GONE);
		 }
		 delete_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				
				
				
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

				// Setting Dialog Title
				alertDialog.setTitle(SessionManager.getClubName(getActivity()));

				// Setting Dialog Message
				alertDialog.setMessage("Are you sure you want delete this "+ DirectorFragmentManageActivity.actionbar_titletext.getText().toString().replace(" Info", "")+"?");

				// Setting Icon to Dialog
				
				
				

				// Setting Positive "Yes" Button
				alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int which) {

						
						
						dialog.cancel();
						
						pd = new ProgressDialog(getActivity());
						pd.setCancelable(false);
						pd.setMessage("Please wait...");
						pd.show();
						
						
						Map<String, String>params= new HashMap<String, String>();
						params.put("broadcast_id", broadCastId);
						aQuery.ajax(WebService.deletebroadcast, params, JSONObject.class, new AjaxCallback<JSONObject>() {
							@Override
							public void callback(String url, JSONObject object, AjaxStatus status) {
								
								
								pd.dismiss();
								int code = status.getCode();
								
								
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
									
									Utill.showDialg(object.getString("message"), getActivity());
								//	Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();
									
									//
								if(Boolean.parseBoolean( object.getString("status"))==true)
								{
								getActivity().getSupportFragmentManager().popBackStack();	
								}
									
								}
								catch(Exception e)
								{
									
								}
								}

							}
						});
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
					// Write your code here to invoke YES event
					//Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
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
		});
		 // delete_linear_layout,yes_no_btn_linear_layout
		 if(DirectorFragmentManageActivity.backButton!=null){
				DirectorFragmentManageActivity.showBackButton();
				DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
			}
		 DirectorFragmentManageActivity.showLogoutButton();
		 initializeView(rootView);
		 setOnClickListener();
		 getBroadCastDetail();
		 return rootView;
	}
	
	BroadcastDetailBean broadCastDetailBean;
	void getBroadCastDetail(){
		if (Utill.isNetworkAvailable(getActivity())) {
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("broadcast_id",broadCastId);
			params.put("user_id",SessionManager.getUser_id(mContext));
			Utill.showProgress(mContext);
			String webUrl = WebService.get_broadcast_detail;
			webUrl = WebService.get_broadcast_detail;
			/*if(broadCastBean.getBroadcast_type().equalsIgnoreCase(AppConstants.BROADCAST)){
				webUrl = WebService.get_broadcast_detail;
			}else{
				webUrl = WebService.get_pollingdetail;
			}
		sff	*/GlobalValues.getModelManagerObj(mContext).getBroadCastDetail(webUrl,params,new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					broadCastDetailBean = JsonUtility.parseBroadCastDetail(json,mContext);
					setDataToView(broadCastDetailBean);
					Log.e("json", json+"");
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
	
	void pollingReplly(String status){
		if (Utill.isNetworkAvailable(getActivity())) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("broadcast_id",broadCastId);
			params.put("user_id",SessionManager.getUser_id(mContext));
			params.put("broadcast_reply",status);
			Utill.showProgress(mContext);
		
			
			GlobalValues.getModelManagerObj(mContext).sendPollingReply(params,new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					getBroadCastDetail();
				//	Utill.hideProgress();
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
	
	
	void setDataToView(BroadcastDetailBean detailBean){
		messageTV.setText(detailBean.getBroadcast_msg());
		if(detailBean.isAnsweredByMe() || detailBean.getBroadcast_type().equalsIgnoreCase(AppConstants.BROADCAST)){
			answerButton.setVisibility(View.GONE);
		}else{
			answerButton.setVisibility(View.VISIBLE);
		}
		
		
		
		if(detailBean.getBroadcast_type().equalsIgnoreCase(AppConstants.BROADCAST))
		{
			DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.broadcast));
			MemberPicNameAdapter adapter = new MemberPicNameAdapter(BroadCastDetailFrageMent.this, detailBean.getReadList());
			readLV.setAdapter(adapter);
			MemberPicNameAdapter adapter1 = new MemberPicNameAdapter(BroadCastDetailFrageMent.this, detailBean.getUnreadList());
			
			
			unreadLV.setAdapter(adapter1);
			
			if( detailBean.getReadList().size() != 0)
			ExpandableHeightListView.getListViewSize(readLV);
			if( detailBean.getUnreadList().size() != 0)
			ExpandableHeightListView.getListViewSize(unreadLV);
			
			String readStr = "<font color=#f4b407>"+"    ("+detailBean.getReadList().size()+" Members)"+"</font>";
			readTitleTV.setText("READ ("+    +detailBean.getReadList().size()+" Members)");
			readStr = "<font color=#f4b407>"+"    ("+detailBean.getUnreadList().size()+" Members)"+"</font>";
			unreadTitleTV.setText(("UNREAD ("+ detailBean.getUnreadList().size()+" Members)"));
			notAnsweredTV.setVisibility(View.GONE);
			unread_list_count_tv.setVisibility(View.GONE);
		}
		else
		{
			DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.polling));
			
			readTitleTV.setText(("YES ("+ detailBean.getYesList().size()+" Members)"));
			MemberPicNameAdapter adapter = new MemberPicNameAdapter(BroadCastDetailFrageMent.this, detailBean.getYesList());
			readLV.setAdapter(adapter);
			if(detailBean.getYesList().size() != 0)
				ExpandableHeightListView.getListViewSize(readLV);
			
			
			unreadTitleTV.setText("NO"+ " ("+detailBean.getNoList().size()+" Members)");
			MemberPicNameAdapter adapter1 = new MemberPicNameAdapter(BroadCastDetailFrageMent.this, detailBean.getNoList() );
			unreadLV.setAdapter(adapter1);
			if(detailBean.getNoList().size() != 0)
				ExpandableHeightListView.getListViewSize(unreadLV);
		
			
			
				notAnsweredTV.setText(Html.fromHtml("READ BUT NO REPLY"+ " ("+detailBean.getReadButNoyReplyList().size()+" Members)"));
				MemberPicNameAdapter adapter2 = new MemberPicNameAdapter(BroadCastDetailFrageMent.this,detailBean.getReadButNoyReplyList());
				notAnswedLV.setAdapter(adapter2);
				if(detailBean.getReadList().size() !=0)
					ExpandableHeightListView.getListViewSize(notAnswedLV);
				
			
				
				
				
				
				MemberPicNameAdapter adapter3 = new MemberPicNameAdapter(BroadCastDetailFrageMent.this, detailBean.getUnreadList());
				unreadble_match_list.setAdapter(adapter3);
				unread_list_count_tv.setText("UNREAD("+ detailBean.getUnreadList().size()+")");
				if(detailBean.getUnreadList().size() !=0)
					ExpandableHeightListView.getListViewSize(unreadble_match_list);
				
			
			/*ArrayList<BroadcastMemberBean> readable = new ArrayList<BroadcastMemberBean>();
			for(int i = 0 ; i<detailBean.getNoList().size();i++ )
			{
			if(detailBean.getNoList().get(i).getReadstatus().equals("1"))
			{
				readable.add(detailBean.getNoList().get(i));
			}
			}
			notAnsweredTV.setText(Html.fromHtml("READ BUT NO REPLY"+ " ("+readable.size()+" Members)"));
			
				
			*/
			
			
			
			
			
			/**/
			
			
			
			
			
				
					
			
			/*	
		
			
			
			
			ArrayList<BroadcastMemberBean> unreadable = new ArrayList<BroadcastMemberBean>();
			//Toast.makeText(getActivity(), " "+detailBean.getUnreadList().size()+"", 1).show();
			
			for(int i = 0 ; i<detailBean.getNoList().size();i++ )
			{
				
			if(detailBean.getNoList().get(i).getReadstatus().equals("2"))
			{
				unreadable.add(detailBean.getNoList().get(i));
			}
			}
			
			//unreadble_match_list.setAdapter(adapter2);
			//unreadble_match_list
			
			//ExpandableHeightListView.getListViewSize(unreadble_match_list);
			String readStr = "<font color=#f4b407>"+"    ("+detailBean.getYesList().size()+" Members)"+"</font>";
			readStr = "<font color=#f4b407>"+"    ("+detailBean.getNoList().size()+" Members)"+"</font>";
			readStr = "<font color=#f4b407>"+"    ("+detailBean.getReadList().size()+" Members)"+"</font>";
			
			
			
			String unreadStr = "<font color=#f4b407>"+"    ("+detailBean.getUnreadList().size()+" Members)"+"</font>";
		*/	
			//unreadble_heading.setText("UNREAD ("+ +detailBean.getUnreadList().size()+" Members)");
		}
		
		
		
		mainView.setVisibility(View.VISIBLE);
		scollview.fullScroll(ScrollView.FOCUS_UP);
		
	}
	
	void initializeView(View view){
		mainView = (LinearLayout) view.findViewById(R.id.mainView);
		messageTV = (TextView) view.findViewById(R.id.msg);




	/*	messageTV.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.e("action", event.getAction() + "");


				//	Toast.makeText(getActivity() ,"action  " + event.getAction(),Toast.LENGTH_SHORT ).show();


				if (1 == event.getAction()) {
					scollview.setEnableScrolling(true);

				} else {

					scollview.setEnableScrolling(false);


				}

				return false;
			}
		});*/













		readTitleTV = (TextView) view.findViewById(R.id.readListTitle);
		unreadTitleTV = (TextView) view.findViewById(R.id.unreadListTitle);
		notAnsweredTV = (TextView) view.findViewById(R.id.notAnswerListTitle);
		readLV = (ListView) view.findViewById(R.id.read_list);
		unreadLV = (ListView) view.findViewById(R.id.unread_list);
		notAnswedLV = (ListView) view.findViewById(R.id.notanswer_list);
		yesTV = (TextView) view.findViewById(R.id.yes);
		noTV = (TextView) view.findViewById(R.id.no);
		unreadListTitle = (TextView) view.findViewById(R.id.unreadListTitle);
		//unreadble_heading =(TextView) view.findViewById(R.id.unreadble_heading);
		//unreadListTitle
		answerButton = (LinearLayout) view.findViewById(R.id.answerBtns);
		unreadble_match_list = (ListView) view.findViewById(R.id.unreadble_match_list);
		yesNoLL  = (LinearLayout) view.findViewById(R.id.answerBtns);
	}
	void setOnClickListener(){
		yesTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pollingReplly("1");
			}
		});
		noTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pollingReplly("2");
			}
		});
		
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
	
}
