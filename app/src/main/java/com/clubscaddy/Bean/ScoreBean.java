package com.clubscaddy.Bean;

import java.util.ArrayList;

public class ScoreBean {
	private String score_id;
	private String score_club_id;
	private String score_event_id;
	private String score_user_id;
	private String score_event_type;
	private String score_match_type;
	private String score_add_date;
	private String scor_copartner;
	private String creater_name;
	private String copartner;
	ArrayList<Tournamenteventlistbean>tournament_event_list ;
	private String tournament_type ;
	String event_name;
	
	
	public String getEvent_name() {
		return event_name;
	}
	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}
	public String getTournament_type() {
		return tournament_type;
	}
	public void setTournament_type(String tournament_type) {
		this.tournament_type = tournament_type;
	}
	public ArrayList<Tournamenteventlistbean> getTournament_event_list() {
		return tournament_event_list;
	}
	public void setTournament_event_list(ArrayList<Tournamenteventlistbean> tournament_event_list) {
		this.tournament_event_list = tournament_event_list;
	}
	public String getCopartner() {
		return copartner;
	}
	public void setCopartner(String copartner) {
		this.copartner = copartner;
	}
	public String getCreater_name() {
		return creater_name;
	}
	public void setCreater_name(String creater_name) {
		this.creater_name = creater_name;
	}
	private ArrayList<ScoreNumberBean> scoreNumberList;
	private ArrayList<ScoreOpponentBean> opponentList;
	
	public ArrayList<ScoreNumberBean> getScoreNumberList() {
		return scoreNumberList;
	}
	public void setScoreNumberList(ArrayList<ScoreNumberBean> scoreNumberList) {
		this.scoreNumberList = scoreNumberList;
	}
	public ArrayList<ScoreOpponentBean> getOpponentList() {
		return opponentList;
	}
	public void setOpponentList(ArrayList<ScoreOpponentBean> opponentList) {
		this.opponentList = opponentList;
	}
	public String getScore_id() {
		return score_id;
	}
	public void setScore_id(String score_id) {
		this.score_id = score_id;
	}
	public String getScore_club_id() {
		return score_club_id;
	}
	public void setScore_club_id(String score_club_id) {
		this.score_club_id = score_club_id;
	}
	public String getScore_event_id() {
		return score_event_id;
	}
	public void setScore_event_id(String score_event_id) {
		this.score_event_id = score_event_id;
	}
	public String getScore_user_id() {
		return score_user_id;
	}
	public void setScore_user_id(String score_user_id) {
		this.score_user_id = score_user_id;
	}
	public String getScore_event_type() {
		return score_event_type;
	}
	public void setScore_event_type(String score_event_type) {
		this.score_event_type = score_event_type;
	}
	public String getScore_match_type() {
		return score_match_type;
	}
	public void setScore_match_type(String score_match_type) {
		this.score_match_type = score_match_type;
	}
	public String getScore_add_date() {
		return score_add_date;
	}
	public void setScore_add_date(String score_add_date) {
		this.score_add_date = score_add_date;
	}
	public String getScor_copartner() {
		return scor_copartner;
	}
	public void setScor_copartner(String scor_copartner) {
		this.scor_copartner = scor_copartner;
	}
}
