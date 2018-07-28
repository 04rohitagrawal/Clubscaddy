package com.clubscaddy.Server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.clubscaddy.Bean.BroadcastDetailBean;
import com.clubscaddy.Bean.BroadcastMemberBean;
import com.clubscaddy.Bean.BroadcastPollingBean;
import com.clubscaddy.Bean.CommentBean;
import com.clubscaddy.Bean.CourtData;
import com.clubscaddy.Bean.CourtInfoBean;
import com.clubscaddy.Bean.DropInBean;
import com.clubscaddy.Bean.Dropin_memberBean;
import com.clubscaddy.Bean.EventBean;
import com.clubscaddy.Bean.EventMemberBean;
import com.clubscaddy.Bean.GroupBean;
import com.clubscaddy.Bean.LoginBean;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.Bean.NewsBean;
import com.clubscaddy.Bean.NotificationsBean;
import com.clubscaddy.Bean.ScoreBean;
import com.clubscaddy.Bean.ScoreListBean;
import com.clubscaddy.Bean.ScoreNumberBean;
import com.clubscaddy.Bean.ScoreOpponentBean;
import com.clubscaddy.Bean.UserClub;
import com.clubscaddy.Bean.VerifyResponse;
import com.clubscaddy.instragram.InstagramSession;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.SqlListe;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;

public class JsonUtility {
	public static String getResultFromJson(JSONObject object) {
		try {
			if (getStringValue(object, WebService.KEY_STATUS).equalsIgnoreCase(WebService.VALUE_STATUS_TRUE)) {
				return getStringValue(object, WebService.Response);
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
	}

	/*
	 * CORE FUNCTIONS. DON'T CHANGE THEM.
	 */
	public static String getStringValue(JSONObject obj, String key) {
		try {
			return obj.isNull(key) ? "" : obj.getString(key);
		} catch (JSONException e) {
			return "";
		}
	}

	public static ArrayList<ArrayList<CourtData>> parserReservation(String json) {
		ArrayList<CourtData> subList = new ArrayList<CourtData>();
		ArrayList<ArrayList<CourtData>> mainList = new ArrayList<ArrayList<CourtData>>();
		try {
			JSONObject myJson = new JSONObject(json);
			{

				JSONArray mainArray = myJson.getJSONArray("Response");
				for (int i = 0; i < mainArray.length(); i++) {
					JSONArray subArray = mainArray.getJSONObject(i).getJSONArray("time");
					subList = new ArrayList<CourtData>();
					for (int k = 0; k < subArray.length(); k++) {
						CourtData bean = new CourtData();
						bean.setCourt_name(subArray.getJSONObject(k).getString("court_name"));
						bean.setSlots(subArray.getJSONObject(k).getString("slots"));
						bean.setUser_name(subArray.getJSONObject(k).getString("user_name"));
						bean.setCourt_reservation_id(subArray.getJSONObject(k).getString("court_reservation_id"));
						bean.setCourt_reservation_bulkbooking_id(subArray.getJSONObject(k).getString("court_reservation_bulkbooking_id"));
						bean.setCourt_reservation_user_id(subArray.getJSONObject(k).getString("court_reservation_user_id"));
						bean.setCourt_id(subArray.getJSONObject(k).getString("court_id"));
						bean.setCourt_start_time(subArray.getJSONObject(k).getString("court_reservation_start_time"));
						bean.setCourt_end_time(subArray.getJSONObject(k).getString("court_reservation_end_time"));
						bean.setCourt_reservation_purpuse(subArray.getJSONObject(k).getString("court_reservation_purpuse"));
						bean.setMemberbookedid(subArray.getJSONObject(k).getString("memberbookedid"));
						bean.setRecursive_id(subArray.getJSONObject(k).getString("court_reservation_recursiveid"));
						bean.setCat_name(subArray.getJSONObject(k).getString("cat_name"));
						bean.setCat_id(subArray.getJSONObject(k).getString("cat_id"));
						bean.setCourt_reservation_start_time((subArray.getJSONObject(k).getString("court_reservation_start_time")));
						bean.setCourt_reservation_end_time((subArray.getJSONObject(k).getString("court_reservation_end_time")));
						bean.setCourt_reservation_purpuse(subArray.getJSONObject(k).getString("court_reservation_purpuse"));
						bean.setCat_color(subArray.getJSONObject(k).getString("cat_color"));


						subList.add(bean);
					}
					mainList.add(subList);
				}
				
				
			}
			// else
			{

			}
		} catch (JSONException e) {
			e.printStackTrace();
			Utill.hideProgress();
		}
		return mainList;
	}

	public static ArrayList<MemberListBean> parserMembersList(String json, Context mContext) {
		ArrayList<MemberListBean> alMemberList = new ArrayList<MemberListBean>();
		try {

			JSONObject mObj = new JSONObject(json);
			if (mObj.optString("status").equals("true")) {
				JSONArray mArray = mObj.optJSONArray("response");
				if (mArray != null) {
					for (int i = 0; i < mArray.length(); i++) {
						JSONObject obj = mArray.optJSONObject(i);
						MemberListBean mb = new MemberListBean();
						mb.setUser_id(obj.optString("user_id"));
						mb.setUser_first_name(obj.optString("user_first_name"));
						mb.setUser_last_name(obj.optString("user_last_name"));
						mb.setUser_email(obj.optString("user_email"));
						mb.setUser_email2(obj.optString("user_email2"));
						mb.setUser_phone(obj.optString("user_phone"));
						mb.setUser_gender(obj.optString("user_gender"));
						mb.setUser_profilepic(obj.optString("user_profilepic"));
						mb.setUser_club_id(obj.optString("user_club_id"));
						mb.setUser_type(obj.optString("user_type"));
						mb.setUser_rating(obj.optString("user_rating"));
						mb.setUser_junior(obj.optString("user_junior"));
						mb.setUser_cost_per_hour(obj.optString("user_cost_per_hour"));
						mb.setUser_device_type(obj.optString("user_device_type"));
						mb.setUser_status(obj.optString("user_status"));
						mb.setUser_about_me(obj.optString("addabout"));
						mb.setFace_book_url(obj.optString("facebookurl"));
						mb.setInstragram_url(obj.optString("instagram"));
						mb.setTwitter_url(obj.optString("twitter"));
						mb.setLinkedin_url(obj.optString("linkedin"));
						//addabout
						mb.setMemberSelection(true);
						if (SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER)
								&& mb.getUser_id().equalsIgnoreCase(SessionManager.getUser_id(mContext))) {

						} else {

						}
						alMemberList.add(mb);
					}
				}
			}
		} catch (JSONException e) {

		}
		return alMemberList;
	}
	public static ArrayList<MemberListBean> parserMembersListItem(String json, Context mContext) {
		ArrayList<MemberListBean> alMemberList = new ArrayList<MemberListBean>();
		try {

			JSONObject mObj = new JSONObject(json);
			if (mObj.optString("status").equals("true")) 
			{
				JSONArray mArray = mObj.optJSONArray("response");
				if (mArray != null) {
					for (int i = 0; i < mArray.length(); i++) {
						JSONObject obj = mArray.optJSONObject(i);
						MemberListBean mb = new MemberListBean();
						mb.setUser_id(obj.optString("user_id"));
						mb.setUser_first_name(obj.optString("user_first_name"));
						mb.setUser_last_name(obj.optString("user_last_name"));
						mb.setUser_email(obj.optString("user_email"));
						mb.setUser_email2(obj.optString("user_email2"));
						mb.setUser_phone(obj.optString("user_phone"));
						mb.setUser_gender(obj.optString("user_gender"));
						mb.setUser_profilepic(obj.optString("user_profilepic"));
						mb.setUser_club_id(obj.optString("user_club_id"));
						mb.setUser_type(obj.optString("user_type"));
						mb.setUser_rating(obj.optString("user_rating"));
						mb.setUser_junior(obj.optString("user_junior"));
						mb.setUser_cost_per_hour(obj.optString("user_cost_per_hour"));
						mb.setUser_device_type(obj.optString("user_device_type"));
						mb.setUser_status(obj.optString("user_status"));
						mb.setUser_about_me(obj.optString("addabout"));
						mb.setFace_book_url(obj.optString("facebookurl"));
						mb.setInstragram_url(obj.optString("instagram"));
						mb.setTwitter_url(obj.optString("twitter"));
						mb.setLinkedin_url(obj.optString("linkedin"));
						//addabout
						mb.setMemberSelection(true);
						if (SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER)
								&& mb.getUser_id().equalsIgnoreCase(SessionManager.getUser_id(mContext))) {

						} else {

						}
						alMemberList.add(mb);
					}
				}
			}
			else
			{
				
			}
		} catch (JSONException e) 
		{

		}
		return alMemberList;
	}

	public static ArrayList<CourtInfoBean> parserCourtListString(String json) {
		ArrayList<CourtInfoBean> courtList = new ArrayList<CourtInfoBean>();
		try {
			JSONObject myJson = new JSONObject(json);
			{
				JSONArray mainArray = myJson.getJSONArray("Response");
				for (int i = 0; i < mainArray.length(); i++) {
					CourtInfoBean bean = new CourtInfoBean();
					bean.setCourt_club_id(mainArray.getJSONObject(i).getString("court_club_id"));
					bean.setCourt_id(mainArray.getJSONObject(i).getString("court_id"));
					bean.setCourt_name(mainArray.getJSONObject(i).getString("court_name"));
					bean.setCourt_number(mainArray.getJSONObject(i).getString("court_number"));
					courtList.add(bean);
				}
			}
		} catch (JSONException e) {

			e.printStackTrace();
			Utill.hideProgress();
		}
		return courtList;
	}

	public static ArrayList<String> parserSlotList(String json ,Calendar currentDate) {
		ArrayList<String> courtList = new ArrayList<String>();
		try {
			JSONObject myJson = new JSONObject(json);
			{
				JSONArray mainArray = myJson.getJSONArray("Response");
				currentDate.add(Calendar.HOUR_OF_DAY ,1);
				for (int i = 0; i < mainArray.length(); i++) {

					Calendar slotDate = Calendar.getInstance();







					SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

					try {
						slotDate.setTime(dateFormat.parse(mainArray.getJSONObject(i).getString("slots")));
					} catch (ParseException e) {
						e.printStackTrace();
					}

					int currentHour = currentDate.get(Calendar.HOUR_OF_DAY);

					int slotHour = slotDate.get(Calendar.HOUR_OF_DAY);

					if (currentHour<= slotDate.get(Calendar.HOUR_OF_DAY))
					{
						if (currentDate.get(Calendar.HOUR_OF_DAY)== slotHour)
						{
							if (currentDate.get(Calendar.MINUTE)<= slotDate.get(Calendar.MINUTE))
							{
								courtList.add(mainArray.getJSONObject(i).getString("slots"));
							}
						}
						else
						{
							courtList.add(mainArray.getJSONObject(i).getString("slots"));
						}



					}



				}
			}
		} catch (JSONException e) {

			e.printStackTrace();
			Utill.hideProgress();
		}
		return courtList;
	}














	public static ArrayList<String> parserSlotList(String json ) {
		ArrayList<String> courtList = new ArrayList<String>();
		try {
			JSONObject myJson = new JSONObject(json);
			{
				JSONArray mainArray = myJson.getJSONArray("Response");
				for (int i = 0; i < mainArray.length(); i++) {




					courtList.add(mainArray.getJSONObject(i).getString("slots"));










				}
			}
		} catch (JSONException e) {

			e.printStackTrace();
			Utill.hideProgress();
		}
		return courtList;
	}














	public static ArrayList<VerifyResponse> parserVerifyRespons(String json) {
		ArrayList<VerifyResponse> courtList = new ArrayList<VerifyResponse>();
		try {
			JSONObject myJson = new JSONObject(json);
			{
				JSONArray mainArray = myJson.getJSONArray("bulk_request");
				for (int i = 0; i < mainArray.length(); i++) {
					VerifyResponse bean = new VerifyResponse();
					bean.setBooked(mainArray.getJSONObject(i).getString("booked"));
					bean.setCourt_reservation_club_id(mainArray.getJSONObject(i).getString("court_reservation_club_id"));
					bean.setCourt_reservation_court_id(mainArray.getJSONObject(i).getString("court_reservation_court_id"));
					bean.setCourt_reservation_date(mainArray.getJSONObject(i).getString("court_reservation_date"));
					bean.setStrart_time(mainArray.getJSONObject(i).getString("strart_time"));
					courtList.add(bean);
				}
			}
		} catch (JSONException e) {

			e.printStackTrace();
			Utill.hideProgress();
		}
		return courtList;
	}

	public static ArrayList<GroupBean> parseGroupList(String json) {
		ArrayList<GroupBean> groupList = new ArrayList<GroupBean>();
		try {
			Log.e("ffffffffffff", json+"");
			
			JSONObject groupObj = new JSONObject(json);
			JSONObject mGroup =groupObj.optJSONObject("group");
			
			JSONArray listArray = mGroup.optJSONArray("list");
			if(listArray!=null){
				for(int i=0;i<listArray.length();i++)
				{
					JSONObject mListObj = listArray.optJSONObject(i);
					
					GroupBean bean = new GroupBean();
					bean.setGroup_id(mListObj.getString("group_id"));
					bean.setGroup_name(mListObj.getString("group_name"));
					groupList.add(bean);
					ArrayList<MemberListBean> memeberList = new ArrayList<MemberListBean>();
					if(mListObj.has("members"))
					{
						JSONArray memberArray = mListObj.optJSONArray("members");
						if(memberArray!=null)
						{
							for(int j=0;j<memberArray.length();j++)
							{
								JSONObject memberObj = memberArray.optJSONObject(j);
								MemberListBean memberBean = new MemberListBean();
								memberBean.setUser_id(memberArray.getJSONObject(j).getString("user_id"));
								memberBean.setUser_first_name(memberArray.getJSONObject(j).getString("user_first_name"));
								memberBean.setUser_last_name(memberArray.getJSONObject(j).getString("user_last_name"));
								memberBean.setUser_email(memberArray.getJSONObject(j).getString("user_email"));
								memberBean.setUser_profilepic(memberArray.getJSONObject(j).getString("user_profilepic"));
								memberBean.setUser_phone(memberArray.getJSONObject(j).getString("user_phone"));
								memeberList.add(memberBean);
							}
							bean.setMembersList(memeberList);

							
						}
					}
				}
			}
			
		/*	JSONObject myJson = new JSONObject(json);
			{
			
				myJson = myJson.getJSONObject("group");
				JSONArray mainArray = myJson.getJSONArray("list");
				for (int i = 0; i < mainArray.length(); i++) {
					GroupBean bean = new GroupBean();
					bean.setGroup_id(mainArray.getJSONObject(i).getString("group_id"));
					bean.setGroup_name(mainArray.getJSONObject(i).getString("group_name"));
					
					JSONObject jArrayObj = mainArray.getJSONObject(i);
					Log.e("jArrayObj", jArrayObj.getString("members"));
					JSONArray jArray = new JSONArray(jArrayObj.getString("members")+"");
					ArrayList<MemberListBean> memeberList = new ArrayList<MemberListBean>();
					for (int j = 0; j < jArray.length(); j++) {
						MemberListBean memberBean = new MemberListBean();
						memberBean.setUser_id(jArray.getJSONObject(j).getString("user_id"));
						memberBean.setUser_first_name(jArray.getJSONObject(j).getString("user_first_name"));
						memberBean.setUser_last_name(jArray.getJSONObject(j).getString("user_last_name"));
						memberBean.setUser_email(jArray.getJSONObject(j).getString("user_email"));
						memberBean.setUser_profilepic(jArray.getJSONObject(j).getString("user_profilepic"));
						memberBean.setUser_phone(jArray.getJSONObject(j).getString("user_phone"));
						memeberList.add(memberBean);
					}

					bean.setMembersList(memeberList);

					groupList.add(bean);
				}
			}*/
		} catch (Exception e) {

			e.printStackTrace();
			Utill.hideProgress();
		}
		return groupList;
	}

	
	
	public static ArrayList<GroupBean> parseGroupListItem(String json) {
		ArrayList<GroupBean> groupList = new ArrayList<GroupBean>();
		try {
			Log.e("ffffffffffff", json+"");
			
			JSONObject groupObj = new JSONObject(json);
			JSONObject mGroup =groupObj.optJSONObject("group");
			
			JSONArray listArray = mGroup.optJSONArray("list");
			if(listArray!=null){
				for(int i=0;i<listArray.length();i++){
					JSONObject mListObj = listArray.optJSONObject(i);
					
					GroupBean bean = new GroupBean();
					bean.setGroup_id(mListObj.getString("group_id"));
					bean.setGroup_name(mListObj.getString("group_name"));
					ArrayList<MemberListBean> memeberList = new ArrayList<MemberListBean>();
					if(mListObj.has("members")){
						JSONArray memberArray = mListObj.optJSONArray("members");
						if(memberArray!=null){
							for(int j=0;j<memberArray.length();j++){
								JSONObject memberObj = memberArray.optJSONObject(j);
								MemberListBean memberBean = new MemberListBean();
								memberBean.setUser_id(memberArray.getJSONObject(j).getString("user_id"));
								memberBean.setUser_first_name(memberArray.getJSONObject(j).getString("user_first_name"));
								memberBean.setUser_last_name(memberArray.getJSONObject(j).getString("user_last_name"));
								memberBean.setUser_email(memberArray.getJSONObject(j).getString("user_email"));
								memberBean.setUser_profilepic(memberArray.getJSONObject(j).getString("user_profilepic"));
								memberBean.setUser_phone(memberArray.getJSONObject(j).getString("user_phone"));
								memeberList.add(memberBean);
							}
							bean.setMembersList(memeberList);

							groupList.add(bean);
						}
					}
				}
			}
			
		/*	JSONObject myJson = new JSONObject(json);
			{
			
				myJson = myJson.getJSONObject("group");
				JSONArray mainArray = myJson.getJSONArray("list");
				for (int i = 0; i < mainArray.length(); i++) {
					GroupBean bean = new GroupBean();
					bean.setGroup_id(mainArray.getJSONObject(i).getString("group_id"));
					bean.setGroup_name(mainArray.getJSONObject(i).getString("group_name"));
					
					JSONObject jArrayObj = mainArray.getJSONObject(i);
					Log.e("jArrayObj", jArrayObj.getString("members"));
					JSONArray jArray = new JSONArray(jArrayObj.getString("members")+"");
					ArrayList<MemberListBean> memeberList = new ArrayList<MemberListBean>();
					for (int j = 0; j < jArray.length(); j++) {
						MemberListBean memberBean = new MemberListBean();
						memberBean.setUser_id(jArray.getJSONObject(j).getString("user_id"));
						memberBean.setUser_first_name(jArray.getJSONObject(j).getString("user_first_name"));
						memberBean.setUser_last_name(jArray.getJSONObject(j).getString("user_last_name"));
						memberBean.setUser_email(jArray.getJSONObject(j).getString("user_email"));
						memberBean.setUser_profilepic(jArray.getJSONObject(j).getString("user_profilepic"));
						memberBean.setUser_phone(jArray.getJSONObject(j).getString("user_phone"));
						memeberList.add(memberBean);
					}

					bean.setMembersList(memeberList);

					groupList.add(bean);
				}
			}*/
		} catch (Exception e) {

			e.printStackTrace();
			Utill.hideProgress();
		}
		return groupList;
	}

	
	
	
	
	
	
	
	
	
	public static ArrayList<DropInBean> parseDropInList(String json) {
		ArrayList<DropInBean> dropInList = new ArrayList<DropInBean>();
		try {
			JSONObject myJson = new JSONObject(json);
			{
				JSONArray mainArray = myJson.getJSONArray("dropin_list");
				mainArray = mainArray.getJSONObject(0).getJSONArray("dropindata");
				for (int i = 0; i < mainArray.length(); i++) 
				{
					int invited_user =0;
					int Joined_user =0;
					int declined_user =0;
					int accepted_back_user =0 ;
					int  rejected_user =0;
					int deleted_user =0;
					int open_user =0 ;
					
					DropInBean bean = new DropInBean();
					bean.setDropin_date(mainArray.getJSONObject(i).getString("dropin_date"));
					bean.setDropin_time(mainArray.getJSONObject(i).getString("dropin_time"));
					bean.setDropin_formate(mainArray.getJSONObject(i).getString("dropin_formate"));
					bean.setDropin_id(mainArray.getJSONObject(i).getString("dropin_id"));
					/*JSONArray jArray = mainArray.getJSONObject(i).getJSONArray("member_list");
					ArrayList<Dropin_memberBean> memeberList = new ArrayList<Dropin_memberBean>();
					for (int j = 0; j < jArray.length(); j++) 
					{
						Dropin_memberBean memberBean = new Dropin_memberBean();
						memberBean.setMember_first_name(jArray.getJSONObject(j).getString("member_first_name"));
						memberBean.setMember_last_name(jArray.getJSONObject(j).getString("member_last_name"));
						memberBean.setMember_id(jArray.getJSONObject(j).getString("member_id"));
						memberBean.setParticipants_status(jArray.getJSONObject(j).getString("participants_status"));
					
						if(jArray.getJSONObject(j).getString("participants_status").equals("1"))
						{
							invited_user++;
							//memberBean.setTotal_invited_user(memberBean.getTotal_invited_user()+1);
						}
						if(jArray.getJSONObject(j).getString("participants_status").equals("2"))
						{
							Joined_user++;
							//memberBean.setTotal_joined_user(memberBean.getTotal_joined_user()+1);	
						}
						if(jArray.getJSONObject(j).getString("participants_status").equals("3"))
						{
							declined_user++;
							//memberBean.setDeclined_user(memberBean.getDeclined_user()+1);
						}
						if(jArray.getJSONObject(j).getString("participants_status").equals("4"))
						{
							accepted_back_user++;
						//	memberBean.setTotal_acceptedback_user(memberBean.getTotal_acceptedback_user()+1);	
						}
					
						if(jArray.getJSONObject(j).getString("participants_status").equals("5"))
						{
							rejected_user++;
						//	memberBean.setTotal_rejected_user(memberBean.getTotal_rejected_user()+1);	
						}
						if(jArray.getJSONObject(j).getString("participants_status").equals("6"))
						{
							deleted_user++;
							//memberBean.setDeleted_user(memberBean.getDeleted_user()+1);   //(memberBean.getTotal_invited_user()+1);	
						}//
						
						memeberList.add(memberBean);
					}*/
					bean.setTotal_invited_user(invited_user);
					bean.setTotal_joined_user(Joined_user);
					bean.setTotal_acceptedback_user(accepted_back_user);
					bean.setTotal_rejected_user(rejected_user);
					bean.setDeclined_user(declined_user);
					bean.setDeleted_user(deleted_user);
					
					dropInList.add(bean);
				/*	if(mainArray.getJSONObject(i).getString("dropinopen").equals("1"))
					bean.setOpenstatus("Open");
					else
						bean.setOpenstatus("Closed");
					
				}
			}*/
				}
			}
		} catch (JSONException e) {

			Log.e("Earror ", e.getMessage());
			Utill.hideProgress();
		}
		return dropInList;
	}
	public static ArrayList<DropInBean> parseDropInListItem(String json) {
		ArrayList<DropInBean> dropInList = new ArrayList<DropInBean>();
		try {
			JSONObject myJson = new JSONObject(json);
			{
				JSONArray mainArray = myJson.getJSONArray("dropin_list");
				mainArray = mainArray.getJSONObject(0).getJSONArray("dropindata");
				for (int i = 0; i < mainArray.length(); i++) 
				{
					int invited_user =0;
					int Joined_user =0;
					int declined_user =0;
					int accepted_back_user =0 ;
					int  rejected_user =0;
					int deleted_user =0;
					int open_user =0 ;
					
					DropInBean bean = new DropInBean();
					bean.setDropin_date(mainArray.getJSONObject(i).getString("dropin_date"));
					bean.setDropin_time(mainArray.getJSONObject(i).getString("dropin_time"));
					bean.setDropin_formate(mainArray.getJSONObject(i).getString("dropin_formate"));
					bean.setDropin_id(mainArray.getJSONObject(i).getString("dropin_id"));
					bean.setNum_of_players(mainArray.getJSONObject(i).getString("dropin_number_player"));



					bean.setDropin_desc(mainArray.getJSONObject(i).getString("dropin_desc"));

				JSONArray jArray = mainArray.getJSONObject(i).getJSONArray("member_list");
				
					ArrayList<Dropin_memberBean> memeberList = new ArrayList<Dropin_memberBean>();
					for (int j = 0; j < jArray.length(); j++) 
					{
						Dropin_memberBean memberBean = new Dropin_memberBean();
						memberBean.setMember_first_name(jArray.getJSONObject(j).getString("member_first_name"));
						memberBean.setMember_last_name(jArray.getJSONObject(j).getString("member_last_name"));
						memberBean.setMember_id(jArray.getJSONObject(j).getString("member_id"));
						memberBean.setParticipants_status(jArray.getJSONObject(j).getString("participants_status"));
						memberBean.setUser_profile_pic(jArray.getJSONObject(j).optString("user_profile_pic"));
						if(jArray.getJSONObject(j).getString("participants_status").equals("1"))
						{
							invited_user++;
							//memberBean.setTotal_invited_user(memberBean.getTotal_invited_user()+1);
						}
						if(jArray.getJSONObject(j).getString("participants_status").equals("2"))
						{
							Joined_user++;
							//memberBean.setTotal_joined_user(memberBean.getTotal_joined_user()+1);	
						}
						if(jArray.getJSONObject(j).getString("participants_status").equals("3"))
						{
							declined_user++;
							//memberBean.setDeclined_user(memberBean.getDeclined_user()+1);
						}
						if(jArray.getJSONObject(j).getString("participants_status").equals("4"))
						{
							accepted_back_user++;
						//	memberBean.setTotal_acceptedback_user(memberBean.getTotal_acceptedback_user()+1);	
						}
					
						if(jArray.getJSONObject(j).getString("participants_status").equals("5"))
						{
							rejected_user++;
						//	memberBean.setTotal_rejected_user(memberBean.getTotal_rejected_user()+1);	
						}
						if(jArray.getJSONObject(j).getString("participants_status").equals("6"))
						{
							deleted_user++;
							//memberBean.setDeleted_user(memberBean.getDeleted_user()+1);   //(memberBean.getTotal_invited_user()+1);	
						}//
						
						memeberList.add(memberBean);
						bean.setMemeberList(memeberList);
					}
					bean.setTotal_invited_user(invited_user);
					bean.setTotal_joined_user(Joined_user);
					bean.setTotal_acceptedback_user(accepted_back_user);
					bean.setTotal_rejected_user(rejected_user);
					bean.setDeclined_user(declined_user);
					bean.setDeleted_user(deleted_user);
					
					
					if(mainArray.getJSONObject(i).getString("dropinopen").equals("1"))
					bean.setOpenstatus("Open");
					else
						bean.setOpenstatus("Closed");
					
					
					dropInList.add(bean);
				}
			}
		} catch (JSONException e) {

			Log.e("Earror ", e.getMessage());
			Utill.hideProgress();
		}
		return dropInList;
	}

	public static ArrayList<NotificationsBean> parseNotifications(String json) {
		ArrayList<NotificationsBean> dropInList = new ArrayList<NotificationsBean>();
		try {
			JSONObject myJson = new JSONObject(json);
			{
				JSONArray mainArray = myJson.getJSONArray("dropin_list");
				for (int i = 0; i < mainArray.length(); i++) {
					NotificationsBean bean = new NotificationsBean();
					bean.setNotification_dropin_id(mainArray.getJSONObject(i).getString("notification_dropin_id"));
					bean.setNotification_invitation_status(mainArray.getJSONObject(i).getString("notification_invitation_status"));
					bean.setNotification_message(mainArray.getJSONObject(i).getString("notification_message"));
					bean.setNotification_status(mainArray.getJSONObject(i).getString("notification_status"));
					bean.setNotification_type(mainArray.getJSONObject(i).getString("notification_type"));
					bean.setNotifications_sender_id(mainArray.getJSONObject(i).getString("notifications_sender_id"));
					bean.setSender_name(mainArray.getJSONObject(i).getString("sender_name"));
					bean.setTimeago(mainArray.getJSONObject(i).getString("timeago"));
					bean.setNotifications_id(mainArray.getJSONObject(i).getString("notifications_id"));
					bean.setNotificationScoreId(mainArray.getJSONObject(i).getString("notification_score_id"));
					bean.setNotificationCommonId(mainArray.getJSONObject(i).getString("notification_commonid"));
					// bean.setDropin_member_user_id(mainArray.getJSONObject(i).getString("dropin_member_user_id"));

					bean.setNotifications_action_status(mainArray.getJSONObject(i).getString("notifications_action_status"));
					dropInList.add(bean);
				}
			}
		} catch (JSONException e) {

			e.printStackTrace();
			Utill.hideProgress();
		}
		Collections.reverse(dropInList);
		return dropInList;
	}
	public static	int last_feed_id  =0 ;
	public static	int pre_feed_id  =0 ;
	public static ArrayList<NewsBean> parseNews(String json ,ArrayList<NewsBean> dropInList) {
		
		
		try {
			JSONObject myJson = new JSONObject(json);
			{
				JSONArray mainArray = myJson.getJSONArray("Response");
				 JsonUtility. pre_feed_id =  JsonUtility.last_feed_id;
				for (int i = 0; i < mainArray.length(); i++) {
					NewsBean bean = new NewsBean();
					bean.setCommentCount(mainArray.getJSONObject(i).getString("comment"));
					bean.setLikeCount(mainArray.getJSONObject(i).getString("liked"));
					bean.setUser_profilepic(mainArray.getJSONObject(i).getString("user_profilepic"));
					bean.setUser_name(mainArray.getJSONObject(i).getString("user_name"));


					String newsDetail = mainArray.getJSONObject(i).getString("news_details").replace("\\'","'").replace("\r" ,"").replace("\\\"","\"");
					newsDetail = newsDetail.replace("\\\"","\"");
					bean.setNews_details( newsDetail/*mainArray.getJSONObject(i).getString("news_details").replace("\\'","'").replace("\\r" ,"").replace("\\\"","\"")*/);
				  String 	 mynewsDetail = newsDetail.replace("\n" , " <br/> ");
					String bufferarray[] = mynewsDetail.split(" ");

					String mtCommentString = "<HTML><p>";


					for (int j = 0; j < bufferarray.length; j++)
					{




						{
							if (Patterns.WEB_URL.matcher(bufferarray[j]).matches())
							{
								mtCommentString = mtCommentString +" "+ "<a href=\"" + bufferarray[j] + "\">" + bufferarray[j] + "</a>";

							} else {
								;
								if (bufferarray[j].contains("<") && bufferarray[j].contains("<br/>") == false)
								{
									mtCommentString = mtCommentString +" "+ bufferarray[j].replace("<","");
								}
								else
								{
									mtCommentString = mtCommentString +" "+ bufferarray[j];
								}

							}
						}



					}

					mtCommentString = mtCommentString.replace("\n", "<br />") + "</p></HTML>";




					bean.setNews_details_html_tag(mtCommentString);
                    bean.setNews_feed_attach_type(mainArray.getJSONObject(i).getString("news_feed_id"));
					bean.setNews_feed_attach_url(mainArray.getJSONObject(i).getString("news_feed_attach_url"));
					bean.setVedioUrl(mainArray.getJSONObject(i).getString("news_video"));
                    JSONArray news_attechment_json_array = mainArray.getJSONObject(i).getJSONArray("news_attechment");
					ArrayList<String> newthumbImageList = new ArrayList<String>();
					ArrayList<String> newImageList = new ArrayList<String>();
					ArrayList<String> deleteImageList = new ArrayList<String>();
					
					
					for (int k = 0; k < news_attechment_json_array.length(); k++)
					{
						
						JSONObject news_attechment_item = news_attechment_json_array.getJSONObject(k);
						
						newthumbImageList.add(news_attechment_item.getString("thumb"));
						newImageList.add(news_attechment_item.getString("url"));
						deleteImageList.add(news_attechment_item.getString("id"));
					}
					bean.setNews_thumb_url(newthumbImageList);

					
					bean.setDeleteImageList(deleteImageList);	
					bean.setNewImage(newImageList);

					
					
					
					
					bean.setNews_feed_id(mainArray.getJSONObject(i).getString("news_feed_id"));
					try
					{
						last_feed_id= Integer.parseInt(mainArray.getJSONObject(i).getString("news_feed_id")) ;
							
					}
					catch(Exception e)
					{
						
					}
					bean.setNews_title(mainArray.getJSONObject(i).getString("news_title"));
					bean.setNews_user_id(mainArray.getJSONObject(i).getString("news_user_id"));
					bean.setMyLikeStatus(mainArray.getJSONObject(i).getString("likestatus"));
					JSONArray commentArray = mainArray.getJSONObject(i).getJSONArray("comments");
					
					ArrayList<CommentBean> commentList = new ArrayList<CommentBean>();
					for (int j = 0; j < commentArray.length(); j++) {
						CommentBean commentBean = new CommentBean();
						commentBean.setNews_feed_comment_id(commentArray.getJSONObject(j).getString("news_feed_comment_id"));
						commentBean.setNews_feed_comment_text(commentArray.getJSONObject(j).getString("news_feed_comment_text"));
						commentBean.setUser_name(commentArray.getJSONObject(j).getString("user_name"));
						commentBean.setUser_profilepic(commentArray.getJSONObject(j).getString("user_profilepic"));
						commentBean.setNews_feed_user_id(commentArray.getJSONObject(j).getString("news_feed_user_id"));
						commentBean.setNews_feed_comment_date(commentArray.getJSONObject(j).getString("news_feed_comment_date"));
						
						
						commentList.add(commentBean);
					}
					bean.setCommentBean(commentList);
					dropInList.add(bean);
				}
			}
		} catch (JSONException e) {

			e.printStackTrace();
			Utill.hideProgress();
		}
		
		return dropInList;
	}
 
 
 
 //static ArrayList< ArrayList<String>> event_attach_url = new ArrayList< ArrayList<String>>();
 //static ArrayList<String>event_attach_url_item = new ArrayList<String>();
 
 
 
	public static ArrayList<EventBean> parseAllEventsList(String json ,Context context) {
		ArrayList<EventBean> eventsList = new ArrayList<EventBean>();
		try {
			JSONObject myJson = new JSONObject(json);
			{
				
				JSONArray mainArray = myJson.getJSONArray("response");
				
					
				for (int i = 0; i < mainArray.length(); i++) {
					EventBean bean = new EventBean();
					
				//	JSONArray thumnailList_jsonArray = mainArray.getJSONObject(i).getJSONArray("thumnail");
					/*	ArrayList<String>thumnailList = new ArrayList<String>();
					ArrayList<String> urlList = new ArrayList<String>();
					ArrayList<String> url_ids = new ArrayList<String>();
					JSONArray urlArray = mainArray.getJSONObject(i).getJSONArray("event_attach_url");
				
					
					
					for (int k = 0; k < urlArray.length(); k++) 
					{
						try
						{
							JSONObject jsonObj = new JSONObject(urlArray.getString(k));
							urlList.add(jsonObj.getString("url"));
							thumnailList.add(jsonObj.getString("thumb"));
							url_ids.add(jsonObj.getString("id"));
						
						}
						catch(Exception e)
						{
							
						}
							
					}
					

					
					bean.setUrl_list_ids(url_ids);
					
					bean.setThumnailList(thumnailList);
					bean.setAttach_event_id(mainArray.getJSONObject(i).getString("event_id"));
					
					bean.setEvent_attach_url(urlList);
					//bean.setEvent_attach_id(mainArray.getJSONObject(i).getString("event_attach_id"));
					// bean.setEvent_attach_type(mainArray.getJSONObject(i).getString("event_attach_type"));

					
					JSONArray pdfArray = mainArray.getJSONObject(i).getJSONArray("event_pdf");
					ArrayList<String> pdfList = new ArrayList<String>();
					for (int k = 0; k < pdfArray.length(); k++) {
						pdfList.add(pdfArray.getString(k));
					}
					bean.setPdfURL(pdfList);
					
*/
				//	bean.setEvent_club_id(mainArray.getJSONObject(i).getString("event_club_id"));
					//bean.setEvent_cost(mainArray.getJSONObject(i).getString("event_cost"));
					bean.setEvent_event_name(mainArray.getJSONObject(i).getString("event_event_name"));
					//bean.setEvent_finishdate(mainArray.getJSONObject(i).getString("event_finishdate"));
					bean.setEvent_id(mainArray.getJSONObject(i).getString("event_id"));
				//	bean.setEvent_signup_deadline_date(mainArray.getJSONObject(i).getString("event_signup_deadline_date"));
				//	bean.setEvent_startdate(mainArray.getJSONObject(i).getString("event_startdate"));
					bean.setEvent_user_id(mainArray.getJSONObject(i).getString("event_user_id"));
				//	bean.setTotalusers(mainArray.getJSONObject(i).getString("eventusers"));
					//bean.setEventDescription(mainArray.getJSONObject(i).getString("event_description"));

				/*	ArrayList<EventMemberBean> participantsList = new ArrayList<EventMemberBean>();

					if (mainArray.getJSONObject(i).has("eventusers")) {
						JSONArray subArray = mainArray.getJSONObject(i).getJSONArray("eventusers");
						for (int j = 0; j < subArray.length(); j++) {
							EventMemberBean member = new EventMemberBean();
							member.setUser_id(subArray.getJSONObject(j).getString("user_id"));
							member.setUser_name(subArray.getJSONObject(j).getString("user_name"));
							member.setUser_profilepic(subArray.getJSONObject(j).getString("user_profilepic"));
							participantsList.add(member);
						}
					}*/

				//	bean.setParticipantsList(participantsList);
					eventsList.add(bean);
				}
			}
		} catch (JSONException e) {

			//Toast.makeText(context, e.getMessage(), 1).show();
			Utill.hideProgress();
		}
		return eventsList;
	}
	public static ArrayList<EventBean>parseAllEventsListItem(String json ,Context context) 
	{
		ArrayList<EventBean> eventsList = new ArrayList<EventBean>();
		try {
			JSONObject myJson = new JSONObject(json);
			{
				
				JSONArray mainArray = myJson.getJSONArray("response");
				
					
				for (int i = 0; i < mainArray.length(); i++) {
					EventBean bean = new EventBean();
					
				//	JSONArray thumnailList_jsonArray = mainArray.getJSONObject(i).getJSONArray("thumnail");
					ArrayList<String>thumnailList = new ArrayList<String>();
					ArrayList<String> urlList = new ArrayList<String>();
					//ArrayList<String> url_ids = new ArrayList<String>();
					JSONArray urlArray = mainArray.getJSONObject(i).getJSONArray("event_attach_url");
				
					
					
					for (int k = 0; k < urlArray.length(); k++) 
					{
						try
						{
							//JSONObject jsonObj = new JSONObject(urlArray.getString(k));
							urlList.add(urlArray.getString(k));

							//url_ids.add(jsonObj.getString("id"));
						
						}
						catch(Exception e)
						{
							
						}
							
					}




					JSONArray thumbNailurlArray = mainArray.getJSONObject(i).getJSONArray("thumnail");



					for (int k = 0; k < thumbNailurlArray.length(); k++)
					{
						try
						{
							//JSONObject jsonObj = new JSONObject(urlArray.getString(k));

							thumnailList.add(thumbNailurlArray.getString(k));
							//url_ids.add(jsonObj.getString("id"));

						}
						catch(Exception e)
						{

						}

					}



					

					
					bean.setEvent_attach_url(urlList);
					
					bean.setThumnailList(thumnailList);
					bean.setAttach_event_id(mainArray.getJSONObject(i).getString("event_id"));
					
					bean.setEvent_attach_url(urlList);
					//bean.setEvent_attach_id(mainArray.getJSONObject(i).getString("event_attach_id"));
					// bean.setEvent_attach_type(mainArray.getJSONObject(i).getString("event_attach_type"));

					
					JSONArray pdfArray = mainArray.getJSONObject(i).getJSONArray("event_pdf");
					ArrayList<String> pdfList = new ArrayList<String>();
					for (int k = 0; k < pdfArray.length(); k++) {
						pdfList.add(pdfArray.getString(k));
					}
					bean.setPdfURL(pdfList);
					

				bean.setEvent_club_id(mainArray.getJSONObject(i).getString("event_club_id"));
					bean.setEvent_cost(mainArray.getJSONObject(i).getString("event_cost"));
					bean.setEvent_event_name(mainArray.getJSONObject(i).getString("event_event_name"));
					bean.setEvent_state(mainArray.getJSONObject(i).getInt("event_state"));
					bean.setEvent_type(mainArray.getJSONObject(i).getInt("event_type"));
					bean.setEvent_finishdate(mainArray.getJSONObject(i).getString("event_finishdate"));
					bean.setEvent_id(mainArray.getJSONObject(i).getString("event_id"));
					bean.setEvent_signup_deadline_date(mainArray.getJSONObject(i).getString("event_signup_deadline_date"));
					bean.setEvent_startdate(mainArray.getJSONObject(i).getString("event_startdate"));
					bean.setEvent_user_id(mainArray.getJSONObject(i).getString("event_user_id"));
				bean.setTotalusers(mainArray.getJSONObject(i).getString("eventusers"));


					bean.setEventDescription(mainArray.getJSONObject(i).getString("event_description").replace("\\'","'").replace("\r" ,"").replace("\\\"","\""));
					bean.setEvent_score(mainArray.getJSONObject(i).optInt("event_score"));

					ArrayList<EventMemberBean> participantsList = new ArrayList<EventMemberBean>();

					if (mainArray.getJSONObject(i).has("eventusers")) {
						JSONArray subArray = mainArray.getJSONObject(i).getJSONArray("eventusers");
						for (int j = 0; j < subArray.length(); j++) {
							EventMemberBean member = new EventMemberBean();
							member.setUser_id(subArray.getJSONObject(j).getString("user_id"));
							member.setUser_name(subArray.getJSONObject(j).getString("user_name"));
							member.setUser_profilepic(subArray.getJSONObject(j).getString("user_profilepic"));
							member.setUser_email(subArray.getJSONObject(j).getString("user_email"));
							member.setUser_no(subArray.getJSONObject(j).getString("user_phone"));
							participantsList.add(member);
						}
					}

				bean.setParticipantsList(participantsList);
					eventsList.add(bean);
				}
			}
		} catch (JSONException e) {

			Toast.makeText(context, e.getMessage(), 1).show();
			Utill.hideProgress();
		}
		return eventsList;
	}
	public static ArrayList<EventBean> parseMyEventsList(String json) {
		ArrayList<EventBean> eventsList = new ArrayList<EventBean>();
		try {
			JSONObject myJson = new JSONObject(json);
			{
				JSONArray mainArray = myJson.getJSONArray("response");
				for (int i = 0; i < mainArray.length(); i++) {
					EventBean bean = new EventBean();

					bean.setAttach_event_id(mainArray.getJSONObject(i).getString("event_id"));
					String imageUrl = mainArray.getJSONObject(i).getString("event_attach_url");
					ArrayList<String> urlList = new ArrayList<String>();
					urlList.add(imageUrl);
					bean.setEvent_attach_url(urlList);
					bean.setEvent_club_id(mainArray.getJSONObject(i).getString("event_club_id"));
					bean.setEvent_cost(mainArray.getJSONObject(i).getString("event_cost"));
					bean.setEvent_event_name(mainArray.getJSONObject(i).getString("event_event_name"));
					bean.setEvent_finishdate(mainArray.getJSONObject(i).getString("event_finishdate"));
					bean.setEvent_id(mainArray.getJSONObject(i).getString("event_id"));
					bean.setEvent_signup_deadline_date(mainArray.getJSONObject(i).getString("event_signup_deadline_date"));
					bean.setEvent_startdate(mainArray.getJSONObject(i).getString("event_startdate"));
					bean.setEvent_user_id(mainArray.getJSONObject(i).getString("event_user_id"));
					ArrayList<EventMemberBean> participantsList = new ArrayList<EventMemberBean>();

					if (mainArray.getJSONObject(i).has("eventusers")) {
						JSONArray subArray = mainArray.getJSONObject(i).getJSONArray("eventusers");
						for (int j = 0; j < subArray.length(); j++) {
							EventMemberBean member = new EventMemberBean();
							member.setUser_id(subArray.getJSONObject(j).getString("user_id"));
							member.setUser_name(subArray.getJSONObject(j).getString("user_name"));
							member.setUser_profilepic(subArray.getJSONObject(j).getString("user_profilepic"));
							participantsList.add(member);
						}
					}

					bean.setParticipantsList(participantsList);
					eventsList.add(bean);
				}
			}
		} catch (JSONException e) {

			e.printStackTrace();
			Utill.hideProgress();
		}
		return eventsList;
	}

	 ;
	
	public static ArrayList<ScoreListBean> parseScoreList(String json) 
	{
		ArrayList<ScoreListBean> socre_list	 = new ArrayList<ScoreListBean>();
	try
	{
	JSONObject responseObj = new JSONObject(json);
	
	JSONArray socre_list_json_array = responseObj.getJSONArray("response");
		
	for(int i = 0 ; i <socre_list_json_array.length();i++)
	{
		JSONObject socre_list_json_array_item = socre_list_json_array.getJSONObject(i);
		ScoreListBean bean = new ScoreListBean();
		
		bean.setScore_id(socre_list_json_array_item.getString("score_id"));
		bean.setScore_result(socre_list_json_array_item.getString("score_result"));
		socre_list.add(bean);
	}
	
	}
	catch(Exception e)
	{
		Log.e("message",e.getMessage());
	}
		
		
		return socre_list;
	}
	
	public static ScoreBean parseScoreDetail(String json ,Activity activity) {
		ScoreBean scoreBean = new ScoreBean();
		
		try {
			JSONObject myJson = new JSONObject(json);
			{
				scoreBean.setCopartner(myJson.getJSONObject("Response").getString("copartner"));
				scoreBean.setScor_copartner(myJson.getJSONObject("Response").getString("scor_copartner"));
				scoreBean.setScore_add_date(myJson.getJSONObject("Response").getString("score_add_date"));
				scoreBean.setScore_id(myJson.getJSONObject("Response").getString("score_id"));
				scoreBean.setScore_club_id(myJson.getJSONObject("Response").getString("score_club_id"));
				scoreBean.setScore_user_id(myJson.getJSONObject("Response").getString("score_user_id"));
				scoreBean.setScore_event_type(myJson.getJSONObject("Response").getString("score_event_type"));
				scoreBean.setScore_match_type(myJson.getJSONObject("Response").getString("score_match_type"));
				scoreBean.setScore_add_date(myJson.getJSONObject("Response").getString("score_add_date"));
				scoreBean.setCreater_name(myJson.getJSONObject("Response").getString("creater_name"));
				scoreBean.setScore_event_id(myJson.getJSONObject("Response").getString("score_event_id"));
				//
				
				scoreBean.setEvent_name(myJson.getJSONObject("Response").getString("event_name"));
				ArrayList<ScoreNumberBean> scoreNumberList = new ArrayList<ScoreNumberBean>();
				JSONArray numberArray = myJson.getJSONArray("scores");
				for (int i = 0; i < numberArray.length(); i++) {
					ScoreNumberBean bean = new ScoreNumberBean();
					bean.setMy_score(numberArray.getJSONObject(i).getString("my_score"));
					bean.setScore_opponent1(numberArray.getJSONObject(i).getString("score_oponent_score"));
					scoreNumberList.add(bean);
				}
				scoreBean.setScoreNumberList(scoreNumberList);

				ArrayList<ScoreOpponentBean> scoreOpponentList = new ArrayList<ScoreOpponentBean>();
				JSONArray opponentASrray = myJson.getJSONArray("opponentdetail");
				for (int i = 0; i < opponentASrray.length(); i++) {
					ScoreOpponentBean bean = new ScoreOpponentBean();
					bean.setScore_opponent_Id(opponentASrray.getJSONObject(i).getString("score_opponent_userid"));
					bean.setScore_opponent(opponentASrray.getJSONObject(i).getString("score_opponent1"));
					bean.setScore_reply(opponentASrray.getJSONObject(i).getString("score_reply"));
					scoreOpponentList.add(bean);
				}
				scoreBean.setOpponentList(scoreOpponentList);

			}
		} catch (JSONException e) {

			Utill.showDialg(e.getMessage(), activity);
			Utill.hideProgress();
		}
		return scoreBean;
	}

	public static ArrayList<BroadcastPollingBean> parseBroadCastListDetail(String json) {
		ArrayList<BroadcastPollingBean> broadCastList = new ArrayList<BroadcastPollingBean>();
		try {
			JSONObject myJson = new JSONObject(json);
			JSONArray jArray = myJson.getJSONArray("response");
			for (int i = 0; i < jArray.length(); i++) {
				BroadcastPollingBean bean = new BroadcastPollingBean();
				bean.setBroadcast_id(jArray.getJSONObject(i).getString("broadcast_id"));
				bean.setBroadcast_msg(jArray.getJSONObject(i).getString("broadcast_msg"));
				bean.setBroadcast_type(jArray.getJSONObject(i).getString("broadcast_type"));
				broadCastList.add(bean);
			}
		} catch (JSONException e) {

			e.printStackTrace();
			Utill.hideProgress();
		}
		return broadCastList;
	}

	public static BroadcastDetailBean parseBroadCastDetail(String json,Context mContext) {
		BroadcastDetailBean broadcastDetail = new BroadcastDetailBean();
		try {
			JSONObject myJson = new JSONObject(json);
			broadcastDetail.setBroadcast_id(myJson.getString("broadcast_id"));
			broadcastDetail.setBroadcast_msg(myJson.getString("broadcast_msg"));
			broadcastDetail.setBroadcast_type(myJson.getString("broadcast_type"));
			broadcastDetail.setBroadcast_user_id(myJson.getString("broadcast_user_id"));
			
			ArrayList<BroadcastMemberBean> readList = new ArrayList<BroadcastMemberBean>();
			ArrayList<BroadcastMemberBean> unreadList = new ArrayList<BroadcastMemberBean>();
			ArrayList<BroadcastMemberBean> yesList = new ArrayList<BroadcastMemberBean>();
			ArrayList<BroadcastMemberBean> noBeanList = new ArrayList<BroadcastMemberBean>();
			ArrayList<BroadcastMemberBean> unAnsweredBeanList = new ArrayList<BroadcastMemberBean>();
			  ArrayList<BroadcastMemberBean> readButNoyReplyList = new ArrayList<BroadcastMemberBean>();

			
			
			JSONArray jArray = myJson.getJSONArray("members");
			for (int i = 0; i < jArray.length(); i++) {
				BroadcastMemberBean bean = new BroadcastMemberBean();
				bean.setBroadcastreply_reply(jArray.getJSONObject(i).getString("broadcastreply_reply"));
				bean.setMember_first_name(jArray.getJSONObject(i).getString("member_first_name"));
				bean.setMember_id(jArray.getJSONObject(i).getString("member_id"));
				bean.setMember_last_name(jArray.getJSONObject(i).getString("member_last_name"));
				bean.setReadstatus(jArray.getJSONObject(i).getString("readstatus"));
				bean.setUser_profilepic(jArray.getJSONObject(i).getString("user_profilepic"));
				
				if(bean.getMember_id().equalsIgnoreCase(SessionManager.getUser_id(mContext)) && (bean.getBroadcastreply_reply().equalsIgnoreCase(AppConstants.YES) || bean.getBroadcastreply_reply().equalsIgnoreCase(AppConstants.NO))){
					broadcastDetail.setAnsweredByMe(true);
				}
				
				
				if(bean.getReadstatus().equalsIgnoreCase(AppConstants.READ)&& bean.getBroadcastreply_reply().equalsIgnoreCase(AppConstants.NOTANSWERED) )
				{
					readButNoyReplyList.add(bean);
				}
				
				if(bean.getReadstatus().equalsIgnoreCase(AppConstants.READ)){
					readList.add(bean);
				}else if(bean.getReadstatus().equalsIgnoreCase(AppConstants.UNREAD)){
					unreadList.add(bean);
				}
				
				if(bean.getBroadcastreply_reply().equalsIgnoreCase(AppConstants.YES)){
					yesList.add(bean);
				}else if(bean.getBroadcastreply_reply().equalsIgnoreCase(AppConstants.NO)){
					noBeanList.add(bean);
				}
				else 
					if(bean.getBroadcastreply_reply().equalsIgnoreCase(AppConstants.NOTANSWERED)){
					unAnsweredBeanList.add(bean);
				}
			}
			if(broadcastDetail.getBroadcast_user_id().equalsIgnoreCase(SessionManager.getUser_id(mContext))){
				broadcastDetail.setAnsweredByMe(true);
			}
			
			broadcastDetail.setReadList(readList);
			broadcastDetail.setUnreadList(unreadList);
			broadcastDetail.setYesList(yesList);
			broadcastDetail.setNoList(noBeanList);
			broadcastDetail.setUnAnsweredList(unAnsweredBeanList);
			broadcastDetail.setReadButNoyReplyList(readButNoyReplyList);
			
			//readButNoyReplyList
			
		} 
		catch (JSONException e) 
		{

			Utill.showDialg(e.getMessage(), mContext);
			e.printStackTrace();
			Utill.hideProgress();
		}
			return broadcastDetail;
	}


	public void loginDataParseAndSaveData(JSONObject mObj ,Activity mContext) {

		try {
			if (mObj.optString("status").equals("true")) {

				String accessToken = mObj.optString("insta_key");
				SessionManager sessionManager = new SessionManager(mContext);
				InstagramSession instagramSession = new InstagramSession(mContext);
				instagramSession.storeAccessToken(accessToken);
				sessionManager.setInStragramStatus(false);


				if (Validation.isStringNullOrBlank(accessToken) == false)
				{
					sessionManager.setInStragramStatus(true);
				}


				LoginBean loginBean = new LoginBean();


				loginBean.setUser_profilepic(mObj.optString("user_profilepic"));
				loginBean.setUser_sport_type(mObj.optString("sport_type"));
				loginBean.setRemaining_days(mObj.optString("remaining_days"));
				loginBean.setSixmonth_price(mObj.optString("sixmonth_price"));
				loginBean.setOneyearprice(mObj.optString("oneyearprice"));
				loginBean.setUser_password("");
				loginBean.setUser_email(mObj.optString("user_email"));
				loginBean.setUser_device_token(mObj.optString("user_device_token"));
				loginBean.setUser_device_type(mObj.optString("user_device_type"));
				loginBean.setUserid(mObj.optString("user_id"));
				loginBean.setUser_id(mObj.optString("user_id"));
				loginBean.setUser_club_id(mObj.optString("user_club_id"));
				loginBean.setClub_logo(mObj.optString("club_logo"));
				loginBean.setClub_status_message(mObj.optString("club_status_message"));
				loginBean.setUser_type(mObj.optString("user_type"));
				loginBean.setUser_first_name(mObj.optString("user_first_name"));
				loginBean.setUser_last_name(mObj.optString("user_last_name"));
				loginBean.setClubname(mObj.optString("clubname"));
				loginBean.setUser_login_app(mObj.optString("user_login_app"));
				loginBean.setClubtype(mObj.optString("clubtype"));

				//Toast.makeText(mContext, "sport_type === "+mObj.optString("sport_type"), 1).show();
				SessionManager.onSaveUser(mContext, loginBean);
				try {
					sessionManager.setUserLoginAppInfo(mContext, mObj.optInt("user_login_app"));
					sessionManager.setClubTypeInfo(mContext, mObj.optInt("clubtype"));

				} catch (Exception e) {

				}


				JSONArray userClub_json_array = mObj.getJSONArray("user_club_info");
				SqlListe sqlListe = new SqlListe(mContext);
				sqlListe.deleteAllClub();
				for (int i = 0; i < userClub_json_array.length(); i++) {
					JSONObject userClub_json_array_item = userClub_json_array.getJSONObject(i);
					UserClub userClub = new UserClub();
					userClub.setUser_id(userClub_json_array_item.getInt("user_id"));
					userClub.setUser_club_id(userClub_json_array_item.getInt("user_club_id"));
					userClub.setUser_type(userClub_json_array_item.getInt("user_type"));
					userClub.setUser_first_name(userClub_json_array_item.getString("user_first_name"));
					userClub.setUser_last_name(userClub_json_array_item.getString("user_last_name"));
					userClub.setUser_email(userClub_json_array_item.getString("user_email"));
					userClub.setUser_phone(userClub_json_array_item.getString("user_phone"));
					userClub.setUser_rating(userClub_json_array_item.getString("user_rating"));
					userClub.setUser_gender(userClub_json_array_item.getInt("user_gender"));
					userClub.setUser_junior(userClub_json_array_item.getInt("user_junior"));
					userClub.setUser_profilepic(userClub_json_array_item.getString("user_profilepic"));
					userClub.setUser_cost_per_hour(userClub_json_array_item.getInt("user_cost_per_hour"));
					userClub.setUser_device_token(userClub_json_array_item.getString("user_device_token"));
					userClub.setUser_statusl(userClub_json_array_item.getInt("user_status"));
					userClub.setUser_about(userClub_json_array_item.getString("user_about"));
					userClub.setUser_instagram(userClub_json_array_item.getString("user_instagram"));
					userClub.setUser_facebook(userClub_json_array_item.getString("user_facebook"));
					userClub.setSport_name((userClub_json_array_item.getString("sport_name")));
					userClub.setUser_twitteter(userClub_json_array_item.getString("user_twitteter"));
					userClub.setClubtype(userClub_json_array_item.getInt("clubtype"));
					userClub.setUser_expired_staus(userClub_json_array_item.getInt("user_expired_staus"));
					userClub.setClub_name(userClub_json_array_item.getString("user_club_name"));
					userClub.setUser_registereddate(userClub_json_array_item.getString("user_registereddate"));
					userClub.setUser_changeclub(userClub_json_array_item.getInt("user_changeclub"));
					userClub.setUser_login_app(userClub_json_array_item.getInt("user_login_app"));
					userClub.setClub_id(userClub_json_array_item.getInt("club_id"));
					userClub.setClub_name(userClub_json_array_item.getString("club_name"));
					userClub.setClub_address(userClub_json_array_item.getString("club_address"));
					userClub.setClub_country(userClub_json_array_item.getString("club_country"));
					userClub.setSport_field_name(userClub_json_array_item.getString("sport_field_name"));
					try {
						userClub.setClublat(userClub_json_array_item.getLong("clublat"));
						userClub.setClublong(userClub_json_array_item.getLong("clublong"));
					} catch (Exception e) {

					}
					userClub.setClub_zip_code(userClub_json_array_item.getInt("club_zip_code"));
					userClub.setClub_num_of_courts(userClub_json_array_item.getInt("club_num_of_courts"));
					userClub.setClub_status_message(userClub_json_array_item.getString("club_status_message"));
					userClub.setClub_logo(userClub_json_array_item.getString("club_logo"));
					userClub.setClub_sport_id(userClub_json_array_item.getString("club_sport_id"));
					userClub.setClub_ratting_show(userClub_json_array_item.getInt("club_ratting_show"));
					userClub.setClub_status(userClub_json_array_item.getInt("club_status"));
					userClub.setPurchase_expiring(userClub_json_array_item.getString("purchase_expiring"));
					userClub.setDemo_period(userClub_json_array_item.getInt("demo_period"));
					userClub.setClub_tempreture(userClub_json_array_item.getInt("club_tempreture"));
					userClub.setClub_temp_date(userClub_json_array_item.getString("club_temp_date"));
					userClub.setClub_temp_id(userClub_json_array_item.getInt("club_temp_id"));
					userClub.setClub_temp_opencourts(userClub_json_array_item.getInt("club_temp_opencourts"));
					userClub.setClub_rating(userClub_json_array_item.getString("club_rating"));
					userClub.setClub_rating_type(userClub_json_array_item.getInt("club_rating_type"));
					userClub.setClub_score_view(Integer.parseInt(userClub_json_array_item.getString("club_score_view")));
					userClub.setSport_player(userClub_json_array_item.getInt("sport_player"));

					userClub.setClub_status_change_date(userClub_json_array_item.getString("club_status_change_date"));

					userClub.setCurrencyCode(userClub_json_array_item.optString("currencyCode"));

					userClub.setLastDateOfMemberListUpdation("2015-10-10 10:10:10");


					sqlListe.setUserClubInfo(userClub);

				}


			}


		} catch (Exception e)
		{
         Toast.makeText(mContext , e.getMessage(),1).show();
		}


	}





}
