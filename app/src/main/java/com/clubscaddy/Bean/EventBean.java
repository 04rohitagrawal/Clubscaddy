package com.clubscaddy.Bean;

import java.util.ArrayList;

import android.R.array;

public class EventBean {
	 private String event_id;
	 private String event_user_id;
	 private String event_club_id;
	 private String event_event_name;
	 private String event_signup_deadline_date;
	 private String event_startdate;
	 private String event_finishdate;
	 private String event_cost;
	 private String event_attach_id;
	 private String attach_event_id;
//	 private String event_attach_type;
	 private ArrayList<String> event_attach_url;
	 private ArrayList<String> pdfURL;

	public int getEvent_type() {
		return event_type;
	}

	public void setEvent_type(int event_type) {
		this.event_type = event_type;
	}

	int event_type ;

	 
	 /*ArrayList<String> Url_list_ids;*/

	public int getEvent_state() {
		return event_state;
	}

	public void setEvent_state(int event_state) {
		this.event_state = event_state;
	}

	int event_state;

	public int getEvent_score() {
		return event_score;
	}

	public void setEvent_score(int event_score) {
		this.event_score = event_score;
	}

	int event_score;
	 
	 
	 
	 /*public ArrayList<String> getUrl_list_ids() {
		return Url_list_ids;
	}*/
	/*public void setUrl_list_ids(ArrayList<String> url_list_ids) {
		Url_list_ids = url_list_ids;
	}*/
	public ArrayList<String> getPdfURL() {
		return pdfURL;
	}
	public void setPdfURL(ArrayList<String> pdfURL) {
		this.pdfURL = pdfURL;
	}
	private String totalusers;
	 private String eventDescription = "";
	 ArrayList<EventMemberBean> participantsList;
	 ArrayList<String> thumnailList;
	public String getEventDescription() {
		return eventDescription;
	}
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	public String getEvent_id() {
		return event_id;
	}
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}
	public String getEvent_user_id() {
		return event_user_id;
	}
	public void setEvent_user_id(String event_user_id) {
		this.event_user_id = event_user_id;
	}
	public String getEvent_club_id() {
		return event_club_id;
	}
	public void setEvent_club_id(String event_club_id) {
		this.event_club_id = event_club_id;
	}
	public String getEvent_event_name() {
		return event_event_name;
	}
	public void setEvent_event_name(String event_event_name) {
		this.event_event_name = event_event_name;
	}
	public String getEvent_signup_deadline_date() {
		return event_signup_deadline_date;
	}
	public void setEvent_signup_deadline_date(String event_signup_deadline_date) {
		this.event_signup_deadline_date = event_signup_deadline_date;
	}
	public String getEvent_startdate() {
		return event_startdate;
	}
	public void setEvent_startdate(String event_startdate) {
		this.event_startdate = event_startdate;
	}
	public String getEvent_finishdate() {
		return event_finishdate;
	}
	public void setEvent_finishdate(String event_finishdate) {
		this.event_finishdate = event_finishdate;
	}
	public String getEvent_cost() {
		return event_cost;
	}
	public void setEvent_cost(String event_cost) {
		this.event_cost = event_cost;
	}
	public String getEvent_attach_id() {
		return event_attach_id;
	}
	public void setEvent_attach_id(String event_attach_id) {
		this.event_attach_id = event_attach_id;
	}
	public String getAttach_event_id() {
		return attach_event_id;
	}
	public void setAttach_event_id(String attach_event_id) {
		this.attach_event_id = attach_event_id;
	}
	public void setThumnailList( ArrayList<String> thumnailList)
	{
		this. thumnailList =  thumnailList; 
	}
/*	public String getEvent_attach_type() {
		return event_attach_type;
	}
	public void setEvent_attach_type(String event_attach_type) {
		this.event_attach_type = event_attach_type;
	}*/
	public ArrayList<String> getEvent_attach_url() {
		return event_attach_url;
	}
	public void setEvent_attach_url(ArrayList<String> event_attach_url) {
		this.event_attach_url = event_attach_url;
	}
	public String getTotalusers() {
		return totalusers;
	}
	public void setTotalusers(String totalusers) {
		this.totalusers = totalusers;
	}
	public ArrayList<EventMemberBean> getParticipantsList() {
		return participantsList;
	}
	public ArrayList<String> getthumnailList() {
		return thumnailList;
	}
	public void setParticipantsList(ArrayList<EventMemberBean> participantsList) {
		this.participantsList = participantsList;
	}
}
