package com.clubscaddy.Bean;

import java.util.ArrayList;

public class DropInBean {
	private String dropin_id;
	private String dropin_date;
	private String dropin_time;
	private String dropin_formate;
	ArrayList<Dropin_memberBean> memeberList;

	public String getDropin_desc() {
		return dropin_desc;
	}

	public void setDropin_desc(String dropin_desc) {
		this.dropin_desc = dropin_desc;
	}

	String dropin_desc;

	public String getUser_profile_pic() {
		return user_profile_pic;
	}

	public void setUser_profile_pic(String user_profile_pic) {
		this.user_profile_pic = user_profile_pic;
	}

	String user_profile_pic ;

	
	int total_invited_user ;
	 int deleted_user ;
		public int getDeleted_user() {
		return deleted_user;
	}
	public void setDeleted_user(int deleted_user) {
		this.deleted_user = deleted_user;
	}
		public int getTotal_invited_user() {
			return total_invited_user;
		}

		public void setTotal_invited_user(int total_invited_user) {
			this.total_invited_user = total_invited_user;
		}

		public int getTotal_joined_user() {
			return total_joined_user;
		}

		public void setTotal_joined_user(int total_joined_user) {
			this.total_joined_user = total_joined_user;
		}

		public int getTotal_acceptedback_user() {
			return total_acceptedback_user;
		}

		public void setTotal_acceptedback_user(int total_acceptedback_user) {
			this.total_acceptedback_user = total_acceptedback_user;
		}

		public int getTotal_rejected_user() {
			return total_rejected_user;
		}

		public void setTotal_rejected_user(int total_rejected_user) {
			this.total_rejected_user = total_rejected_user;
		}

		public int getTotal_Status_user() {
			return total_Status_user;
		}

		public void setTotal_Status_user(int total_Status_user) {
			this.total_Status_user = total_Status_user;
		}

		public String getOpenstatus() {
			return openstatus;
		}

		public void setOpenstatus(String openstatus) {
			this.openstatus = openstatus;
		}
		String num_of_players;

		public String getNum_of_players() {
			return num_of_players;
		}
		public void setNum_of_players(String num_of_players) {
			this.num_of_players = num_of_players;
		}
		int total_joined_user ;
		int total_acceptedback_user ;
		int total_rejected_user ;
	  	int total_Status_user ;
	  	int declined_user ;
		public int getDeclined_user() {
			return declined_user;
		}
		public void setDeclined_user(int declined_user) {
			this.declined_user = declined_user;
		}
		String openstatus ;

	public ArrayList<Dropin_memberBean> getMemeberList() {
		return memeberList;
	}

	public void setMemeberList(ArrayList<Dropin_memberBean> memeberList) {
		this.memeberList = memeberList;
	}

	public String getDropin_id() {
		return dropin_id;
	}

	public void setDropin_id(String dropin_id) {
		this.dropin_id = dropin_id;
	}

	public String getDropin_date() {
		return dropin_date;
	}

	public void setDropin_date(String dropin_date) {
		this.dropin_date = dropin_date;
	}

	public String getDropin_time() {
		return dropin_time;
	}

	public void setDropin_time(String dropin_time) {
		this.dropin_time = dropin_time;
	}

	public String getDropin_formate() {
		return dropin_formate;
	}

	public void setDropin_formate(String dropin_formate) {
		this.dropin_formate = dropin_formate;
	}
}
