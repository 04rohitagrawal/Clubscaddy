package com.clubscaddy.Bean;

import java.util.ArrayList;

public class GroupBean {
	private String group_id;
	private String group_name;
	private ArrayList<MemberListBean> membersList;
	
	
	public ArrayList<MemberListBean> getMembersList() {
		return membersList;
	}
	public void setMembersList(ArrayList<MemberListBean> membersList) {
		this.membersList = membersList;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
}
