package com.clubscaddy.Bean;

import java.util.ArrayList;

public class BroadcastDetailBean {
	  private String broadcast_id;
	  private String broadcast_user_id;
	  private String broadcast_type;
	  private String broadcast_msg;
	  private boolean answeredByMe = false;
	  
	  public boolean isAnsweredByMe() {
		return answeredByMe;
	}
	public void setAnsweredByMe(boolean answeredByMe) {
		this.answeredByMe = answeredByMe;
	}
	ArrayList<BroadcastMemberBean> readList = new ArrayList<BroadcastMemberBean>();
	  ArrayList<BroadcastMemberBean> unreadList = new ArrayList<BroadcastMemberBean>();
	  
	  ArrayList<BroadcastMemberBean> yesList = new ArrayList<BroadcastMemberBean>();
	  ArrayList<BroadcastMemberBean> noList = new ArrayList<BroadcastMemberBean>();
	  ArrayList<BroadcastMemberBean> unAnsweredList = new ArrayList<BroadcastMemberBean>();
	  
	  ArrayList<BroadcastMemberBean> readButNoyReplyList = new ArrayList<BroadcastMemberBean>();

	  
	  
	  

	public ArrayList<BroadcastMemberBean> getReadButNoyReplyList() {
		return readButNoyReplyList;
	}
	public void setReadButNoyReplyList(ArrayList<BroadcastMemberBean> readButNoyReplyList) {
		this.readButNoyReplyList = readButNoyReplyList;
	}
	public ArrayList<BroadcastMemberBean> getReadList() {
		return readList;
	}
	public void setReadList(ArrayList<BroadcastMemberBean> readList) {
		this.readList = readList;
	}
	public ArrayList<BroadcastMemberBean> getUnreadList() {
		return unreadList;
	}
	public void setUnreadList(ArrayList<BroadcastMemberBean> unreadList) {
		this.unreadList = unreadList;
	}
	public ArrayList<BroadcastMemberBean> getYesList() {
		return yesList;
	}
	public void setYesList(ArrayList<BroadcastMemberBean> yesList) {
		this.yesList = yesList;
	}
	public ArrayList<BroadcastMemberBean> getNoList() {
		return noList;
	}
	public void setNoList(ArrayList<BroadcastMemberBean> noList) {
		this.noList = noList;
	}
	public ArrayList<BroadcastMemberBean> getUnAnsweredList() {
		return unAnsweredList;
	}
	public void setUnAnsweredList(ArrayList<BroadcastMemberBean> unAnsweredList) {
		this.unAnsweredList = unAnsweredList;
	}
	public String getBroadcast_id() {
		return broadcast_id;
	}
	public void setBroadcast_id(String broadcast_id) {
		this.broadcast_id = broadcast_id;
	}
	public String getBroadcast_user_id() {
		return broadcast_user_id;
	}
	public void setBroadcast_user_id(String broadcast_user_id) {
		this.broadcast_user_id = broadcast_user_id;
	}
	public String getBroadcast_type() {
		return broadcast_type;
	}
	public void setBroadcast_type(String broadcast_type) {
		this.broadcast_type = broadcast_type;
	}
	public String getBroadcast_msg() {
		return broadcast_msg;
	}
	public void setBroadcast_msg(String broadcast_msg) {
		this.broadcast_msg = broadcast_msg;
	}

}
