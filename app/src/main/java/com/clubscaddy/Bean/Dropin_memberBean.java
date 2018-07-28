package com.clubscaddy.Bean;

public class Dropin_memberBean {
	 private String member_id;
	 public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getMember_first_name() {
		return member_first_name;
	}
	public void setMember_first_name(String member_first_name) {
		this.member_first_name = member_first_name;
	}
	public String getMember_last_name() {
		return member_last_name;
	}
	public void setMember_last_name(String member_last_name) {
		this.member_last_name = member_last_name;
	}
	public String getParticipants_status() {
		return participants_status;
	}
	public void setParticipants_status(String participants_status) {
		this.participants_status = participants_status;
	}
	private String member_first_name;
	 private String member_last_name;
	 private String participants_status;

	public String getUser_profile_pic() {
		return user_profile_pic;
	}

	public void setUser_profile_pic(String user_profile_pic) {
		this.user_profile_pic = user_profile_pic;
	}

	public String user_profile_pic ;
	 
	 
	 
	 
	 
	 
	 
	 
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

		int total_joined_user ;
		int total_acceptedback_user ;
		int total_rejected_user ;
	  	int total_Status_user ;
	  	int declined_user ;
	  	int number_of_player ;
		public int getNumber_of_player() {
			return number_of_player;
		}
		public void setNumber_of_player(int number_of_player) {
			this.number_of_player = number_of_player;
		}
		public int getDeclined_user() {
			return declined_user;
		}
		public void setDeclined_user(int declined_user) {
			this.declined_user = declined_user;
		}
		String openstatus ;
}
