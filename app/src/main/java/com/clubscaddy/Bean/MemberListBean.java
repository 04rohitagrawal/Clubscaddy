package com.clubscaddy.Bean;

import java.util.ArrayList;

public class MemberListBean {
	
	String user_id;
	String user_first_name;
	String user_last_name;
	String user_email;
	String user_email2;
	String user_phone;
	String user_gender;
	String user_profilepic;
	String user_club_id;
	String user_type;
	String user_rating;
	String user_about_me="";
	String face_book_url = "";
	String twitter_url = "";
	ArrayList<UserPics> userPicsArrayList ;


	public ArrayList<UserPics> getUserClubArrayList() {
		return userClubArrayList;
	}

	public void setUserClubArrayList(ArrayList<UserPics> userClubArrayList) {
		this.userClubArrayList = userClubArrayList;
	}



	public String getInstragramToken() {
		return instragramToken;
	}

	public void setInstragramToken(String instragramToken) {
		this.instragramToken = instragramToken;
	}

	String instragramToken = "" ;



	ArrayList<UserPics>userClubArrayList ;




	public String getUser_no_show() {
		return user_no_show;
	}

	public void setUser_no_show(String user_no_show) {
		this.user_no_show = user_no_show;
	}

	String user_no_show ;

	public String getTwitter_url() {
		return twitter_url;
	}
	public void setTwitter_url(String twitter_url) {
		this.twitter_url = twitter_url;
	}
	String linkedin_url = "";
	public String getLinkedin_url() {
		return linkedin_url;
	}
	public void setLinkedin_url(String linkedin_url) {
		this.linkedin_url = linkedin_url;
	}
	String instragram_url = "";
	
	
	public String getInstragram_url() {
		return instragram_url;
	}
	public void setInstragram_url(String instragram_url) {
		this.instragram_url = instragram_url;
	}
	public String getFace_book_url() {
		return face_book_url;
	}
	public void setFace_book_url(String face_book_url) {
		this.face_book_url = face_book_url;
	}
	public String getUser_about_me() {
		return user_about_me;
	}
	public void setUser_about_me(String user_about_me) {
		this.user_about_me = user_about_me;
	}
	String user_junior;
	String user_cost_per_hour;
	String user_device_type;
	String user_status;
	boolean isMemberSelected = false;




	String userName ;




	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_first_name() {
		return user_first_name;
	}
	public void setUser_first_name(String user_first_name) {
		this.user_first_name = user_first_name;
	}
	public String getUser_last_name() {
		return user_last_name;
	}
	public void setUser_last_name(String user_last_name) {
		this.user_last_name = user_last_name;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getUser_email2() {
		return user_email2;
	}
	public void setUser_email2(String user_email2) {
		this.user_email2 = user_email2;
	}
	public String getUser_phone() {
		return user_phone;
	}
	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}
	public String getUser_gender() {
		return user_gender;
	}
	public void setUser_gender(String user_gender) {
		this.user_gender = user_gender;
	}
	public String getUser_profilepic() {
		return user_profilepic;
	}
	public void setUser_profilepic(String user_profilepic) {
		this.user_profilepic = user_profilepic;
	}
	public String getUser_club_id() {
		return user_club_id;
	}
	public void setUser_club_id(String user_club_id) {
		this.user_club_id = user_club_id;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getUser_rating() {
		return user_rating;
	}
	public void setUser_rating(String user_rating) {
		this.user_rating = user_rating;
	}
	public String getUser_junior() {
		return user_junior;
	}
	public void setUser_junior(String user_junior) {
		this.user_junior = user_junior;
	}
	public String getUser_cost_per_hour() {
		return user_cost_per_hour;
	}
	public void setUser_cost_per_hour(String user_cost_per_hour) {
		this.user_cost_per_hour = user_cost_per_hour;
	}
	public String getUser_device_type() {
		return user_device_type;
	}
	public void setUser_device_type(String user_device_type) {
		this.user_device_type = user_device_type;
	}
	public String getUser_status() {
		return user_status;
	}
	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}
	
	public void setMemberSelection(boolean isMemberSelected)
	{
		this.isMemberSelected = isMemberSelected;
	}
	public boolean getMemberSelection()
	{
		return this.isMemberSelected ;
	}



	public ArrayList<UserPics> getUserPicsArrayList() {
		return userClubArrayList;
	}

	public void setUserPicsArrayList(ArrayList<UserPics> userClubArrayList) {
		this.userClubArrayList = userClubArrayList;
	}
	
}
