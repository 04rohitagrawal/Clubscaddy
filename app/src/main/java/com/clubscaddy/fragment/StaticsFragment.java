package com.clubscaddy.fragment;

import com.clubscaddy.Adapter.CoachMemberListDialogAdapter;
import com.clubscaddy.Adapter.CouchReservationListAdapter;
import com.clubscaddy.Bean.CoachReserve;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.R;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class StaticsFragment extends Fragment implements AdapterView.OnItemClickListener
{
	View view ;


	String title;
	LinearLayout tab_layout;
    ListView coach_lesson_list_view;
	ListView book_lesson_list_view;

	ArrayList<CoachReserve>coachmemberbookingArrayList;
	ArrayList<CoachReserve>mycoachReserveArrayList;

	ArrayList<MemberListBean>membersList ,clubsmembersList;


	LinearLayout my_lesson_tab;

	LinearLayout book_lesson_tab;


	ImageView book_coachtime_btn;
	//LinearLayout tab_item;

	boolean isCoachAsMember = false;

	HttpRequest httpRequest;
	public void setInstanse(String title)
	{
		this.title = title ;
	}





	//


@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	view = inflater.inflate(R.layout.statics_fragment_layout, null);





	if (DirectorFragmentManageActivity.actionbar_titletext != null) {
		DirectorFragmentManageActivity.updateTitle("Upcoming Lessons");
	}
	httpRequest = new HttpRequest(getActivity());
	if (DirectorFragmentManageActivity.backButton != null) {
		DirectorFragmentManageActivity.showBackButton();
		
	}
	DirectorFragmentManageActivity.showLogoutButton();

	DirectorFragmentManageActivity.backButton.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v)
		{
			getActivity().getSupportFragmentManager().popBackStack();
		}
	});
	coachmemberbookingArrayList = new ArrayList<>();
	mycoachReserveArrayList = new ArrayList<>();
	my_lesson_tab = (LinearLayout) view.findViewById(R.id.my_lesson_tab);

	//tab_item = (LinearLayout) view.findViewById(R.id.tab_item);
	clubsmembersList = new ArrayList<MemberListBean>();
	book_lesson_tab = (LinearLayout) view.findViewById(R.id.book_lesson_tab);

	my_lesson_tab.setOnClickListener(tabClickListener);
	book_lesson_tab.setOnClickListener(tabClickListener);





	coach_lesson_list_view = (ListView) view.findViewById(R.id.coach_lesson_list_view);
	book_lesson_list_view = (ListView) view.findViewById(R.id.book_lesson_list_view);



	book_coachtime_btn = (ImageView) view.findViewById(R.id.book_coachtime_btn);

	book_lesson_list_view = (ListView) view.findViewById(R.id.book_lesson_list_view);

	coach_lesson_list_view = (ListView) view.findViewById(R.id.coach_lesson_list_view);


	book_lesson_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			CoachReservationDetail coachReservationDetail = new CoachReservationDetail();
			coachReservationDetail.setInstanse(mycoachReserveArrayList.get(position).getMemberbookedid() ,mycoachReserveArrayList.get(position).getCoach_reservation_recursive_id() , mycoachReserveArrayList.get(position).getCoach_mem_id() , mycoachReserveArrayList.get(position).getCoach_name() );
			getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame ,coachReservationDetail ,"content_frame").addToBackStack(null).commit();

		}
	});
	//

	coach_lesson_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			CoachReservationDetail coachReservationDetail = new CoachReservationDetail();
			coachReservationDetail.setInstanse(coachmemberbookingArrayList.get(position).getMemberbookedid() ,coachmemberbookingArrayList.get(position).getCoach_reservation_recursive_id()  , coachmemberbookingArrayList.get(position).getCoach_mem_id() , coachmemberbookingArrayList.get(position).getCoach_name());
			getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame ,coachReservationDetail ,"content_frame").addToBackStack(null).commit();

		}
	});

	book_coachtime_btn.setOnClickListener(tabClickListener);


	/*if (SessionManager.getUser_type(getActivity()).equals(AppConstants.USER_TYPE_COACH))
	{

	}
	else
	{


	}*/

	isMemberAsCoach();



	return view;
}
 public View.OnClickListener tabClickListener = new View.OnClickListener() {
	 @Override
	 public void onClick(View v)
	 {
		 if (my_lesson_tab.getId() == v.getId())
		 {

			 book_lesson_list_view.setVisibility(View.GONE);

			book_coachtime_btn.setVisibility(View.GONE);
			 my_lesson_tab.setAlpha(1.0f);
			 book_lesson_tab.setAlpha(0.6f);
			 getCoachMemberBookingList();

		 }

		 if (book_lesson_tab.getId() == v.getId())
		 {
			 my_lesson_tab.setAlpha(0.6f);
			 book_lesson_tab.setAlpha(1.0f);

			 book_lesson_list_view.setVisibility(View.VISIBLE);

			 book_coachtime_btn.setVisibility(View.VISIBLE);
			 getMyCoachReserve();
		 }

		 if (book_coachtime_btn.getId() == v.getId())
		 {

			 if (isCoachAsMember)
			 {
				 CoachSloatBookFragment coachSloatBookFragment = new CoachSloatBookFragment();
				 coachSloatBookFragment.setCoach_id(SessionManager.getUser_id(getActivity()) ,SessionManager.getFirstName(getActivity())+" "+SessionManager.getLastName(getActivity()),isCoachAsMember);
				 getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame ,coachSloatBookFragment ,"content_frame").addToBackStack("content_frame").commit();

			 }
			 else
			 {
				 setClubCoachList();
			 }


		 }

	 }
 };


	public void setClubCoachList()
	{
		clubsmembersList.clear();

		HashMap<String , Object> params = new   HashMap<String , Object>();
		params.put("club_id" , SessionManager.getUser_Club_id(getActivity()));
		params.put("user_type" , "1");

		httpRequest.getResponse(getActivity(), WebService.coach_list, params, new OnServerRespondingListener(getActivity()) {
			@Override
			public void onSuccess(JSONObject jsonObject)
			{
				try
				{
					Log.e("jsonObject",jsonObject+"");

                    JSONArray coachJsonArray = jsonObject.getJSONArray("coach");
                    for (int i = 0  ;i < coachJsonArray.length() ;i++)
                    {
                        JSONObject coachJsonArrayItem = coachJsonArray.getJSONObject(i);
                        MemberListBean memberListBean = new MemberListBean();
                        memberListBean.setUser_first_name(coachJsonArrayItem.getString("user_first_name"));
                        memberListBean.setUser_last_name(coachJsonArrayItem.getString("user_last_name"));
                        memberListBean.setUser_id(coachJsonArrayItem.getString("user_id"));
                        memberListBean.setUser_profilepic(coachJsonArrayItem.getString("user_profilepic"));
                        clubsmembersList.add(memberListBean);
                    }
                    if (clubsmembersList.size() != 0)
					{
						showDialog();

					}
					else
					{
						Utill.showDialg(jsonObject.getString("msg") , getActivity());
					}

				}
				catch (Exception e)
				{

				}
			}
		});
	}


	Dialog dialog ;
	public void showDialog()
	{
		 dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.coach_list_dialog);
		dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);;

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = AppConstants.getDeviceWidth(getActivity());
		lp.height = AppConstants.getDeviceHeight(getActivity());
		dialog.getWindow().setAttributes(lp);
		dialog.show();
		ListView coach_list_view = (ListView) dialog.findViewById(R.id.coach_list_view);
		coach_list_view.setOnItemClickListener(StaticsFragment.this);
		coach_list_view.setAdapter(new CoachMemberListDialogAdapter(getActivity() , clubsmembersList));

	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		dialog.dismiss();
		CoachSloatBookFragment coachSloatBookFragment = new CoachSloatBookFragment();
		coachSloatBookFragment.setCoach_id(clubsmembersList.get(position).getUser_id() ,clubsmembersList.get(position).getUser_first_name()+" "+clubsmembersList.get(position).getUser_last_name(),isCoachAsMember);
		getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame ,coachSloatBookFragment ,"content_frame").addToBackStack("content_frame").commit();
	}



	public void getMyCoachReserve()
	{





		HashMap<String , Object>param = new HashMap<String , Object>();
		param.put("coach_user_id" ,SessionManager.getUserId(getActivity()));
		param.put("coach_mem_id" ,SessionManager.getUserId(getActivity()));
		param.put("coach_club_id" ,SessionManager.getUser_Club_id(getActivity()));



		httpRequest.getResponse(getActivity(), WebService.mycoachreserve, param, new OnServerRespondingListener(getActivity()) {
			@Override
			public void onSuccess(JSONObject jsonObject)
			{
             Log.e("jsonObject" ,jsonObject+"");

				book_lesson_list_view.setVisibility(View.VISIBLE);
				coach_lesson_list_view.setVisibility(View.GONE);
				mycoachReserveArrayList.clear();
				try
				{
					if (jsonObject.getBoolean("status"))
					{
						JSONArray coach_reserve_json_array = jsonObject.getJSONArray("response");
						for (int i = 0 ; i < coach_reserve_json_array.length() ;i++)
						{
							JSONObject coach_reserve_json_array_item = coach_reserve_json_array.getJSONObject(i);
							CoachReserve coachReserve = new CoachReserve();
							coachReserve.setCoach_reservation_date(coach_reserve_json_array_item.getString("coach_reservation_date"));
							coachReserve.setMemberbookedid(coach_reserve_json_array_item.getString("memberbookedid"));
							coachReserve.setCoach_reservation_end_datetime(coach_reserve_json_array_item.getString("coach_reservation_end_datetime"));
							coachReserve.setCoach_reservation_start_datetime(coach_reserve_json_array_item.getString("coach_reservation_start_datetime"));
							coachReserve.setCoach_coach_id(coach_reserve_json_array_item.getString("coach_coach_id"));
							coachReserve.setCoach_reservation_id(coach_reserve_json_array_item.getString("coach_reservation_id"));
							coachReserve.setCoach_club_id(coach_reserve_json_array_item.getString("coach_club_id"));
							coachReserve.setCoach_mem_id(coach_reserve_json_array_item.getString("coach_mem_id"));
							coachReserve.setCoach_name(coach_reserve_json_array_item.getString("coach_name"));
							coachReserve.setCoach_reservation_recursive_id(coach_reserve_json_array_item.getString("coach_reservation_recursive_id"));
							mycoachReserveArrayList.add(coachReserve);
						}


					}
					else
					{
						ShowUserMessage.showUserMessage(getActivity() ,jsonObject.getString("message") );
					}

					book_lesson_list_view.setAdapter(new CouchReservationListAdapter(getActivity() , mycoachReserveArrayList));


				}
				catch (Exception e)
				{

				}
			}
		});


	}









	public void getCoachMemberBookingList()
	{

		coach_lesson_list_view.setVisibility(View.VISIBLE);
		book_lesson_list_view.setVisibility(View.GONE);
		HashMap<String , Object>param = new HashMap<String , Object>();
		param.put("coach_coach_id" ,SessionManager.getUserId(getActivity()));
		param.put("coach_club_id" ,SessionManager.getUser_Club_id(getActivity()));



		httpRequest.getResponse(getActivity(), WebService.coachmemberbookinglist, param, new OnServerRespondingListener(getActivity()) {



			@Override
			public void onNetWorkError() {
				super.onNetWorkError();
				coachmemberbookingArrayList.clear();
				coach_lesson_list_view.setAdapter(new CouchReservationListAdapter(getActivity() , coachmemberbookingArrayList));

			}

			@Override
			public void onConnectionError() {
				super.onConnectionError();
				coachmemberbookingArrayList.clear();
				coach_lesson_list_view.setAdapter(new CouchReservationListAdapter(getActivity() , coachmemberbookingArrayList));

			}

			@Override
			public void internetConnectionProble() {
				super.internetConnectionProble();
				coachmemberbookingArrayList.clear();
				coach_lesson_list_view.setAdapter(new CouchReservationListAdapter(getActivity() , coachmemberbookingArrayList));

			}



			@Override
			public void onSuccess(JSONObject jsonObject)
			{
				Log.e("jsonObject" ,jsonObject+"");
				coachmemberbookingArrayList.clear();
				try
				{
					if (jsonObject.getBoolean("status"))
					{
						JSONArray coach_reserve_json_array = jsonObject.getJSONArray("response");
						for (int i = 0 ; i < coach_reserve_json_array.length() ;i++)
						{
							JSONObject coach_reserve_json_array_item = coach_reserve_json_array.getJSONObject(i);
							CoachReserve coachReserve = new CoachReserve();
							coachReserve.setCoach_reservation_date(coach_reserve_json_array_item.getString("coach_reservation_date"));
							coachReserve.setMemberbookedid(coach_reserve_json_array_item.getString("memberbookedid"));
							coachReserve.setCoach_reservation_end_datetime(coach_reserve_json_array_item.getString("coach_reservation_end_datetime"));
							coachReserve.setCoach_reservation_start_datetime(coach_reserve_json_array_item.getString("coach_reservation_start_datetime"));
							coachReserve.setCoach_coach_id(coach_reserve_json_array_item.getString("coach_coach_id"));
							coachReserve.setCoach_reservation_id(coach_reserve_json_array_item.getString("coach_reservation_id"));
							coachReserve.setCoach_club_id(coach_reserve_json_array_item.getString("coach_club_id"));
							coachReserve.setCoach_mem_id(coach_reserve_json_array_item.getString("coach_mem_id"));
							coachReserve.setCoach_reservation_recursive_id(coach_reserve_json_array_item.getString("coach_reservation_recursive_id"));
							coachReserve.setCoach_name(coach_reserve_json_array_item.getString("member_name"));


							coachmemberbookingArrayList.add(coachReserve);
						}

					}
					else
					{
						ShowUserMessage.showUserMessage(getActivity() ,jsonObject.getString("message") );
					}

					coach_lesson_list_view.setAdapter(new CouchReservationListAdapter(getActivity() , coachmemberbookingArrayList));

				}
				catch (Exception e)
				{
					Toast.makeText(getActivity() , e.getMessage() ,Toast.LENGTH_SHORT).show();
				}
			}
		});


	}



	public void  isMemberAsCoach()
	{
		HashMap<String , Object>params = new  HashMap<String , Object>();
		params.put("user_type", "1");
		params.put("user_id", SessionManager.getUser_id(getActivity()));
		params.put("club_id", SessionManager.getUser_Club_id(getActivity()));

		//
		httpRequest.getResponse(getActivity(), WebService.coach_list , params, new OnServerRespondingListener(getActivity()) {
			@Override
			public void onSuccess(JSONObject jsonObject)
			{

				try
				{
					if (jsonObject.getBoolean("status")== true)
					{
						//tab_item.setVisibility(View.VISIBLE);
						isCoachAsMember = true ;
						//book_coachtime_btn.setVisibility(View.GONE);
						getCoachMemberBookingList();
					}
					else
					{
						//tab_item.setVisibility(View.GONE);
						isCoachAsMember = false ;
						getMyCoachReserve();
						//ShowUserMessage.showUserMessage(getActivity() , jsonObject.getString("message"));
					}
				}
				catch (Exception e)
				{

				}



			}
		});

	}


}
