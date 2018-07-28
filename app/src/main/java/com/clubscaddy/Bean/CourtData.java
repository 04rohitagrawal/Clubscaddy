package com.clubscaddy.Bean;

public class CourtData implements Cloneable
{
	


	private String 	recursive_id;
	private String 	memberbookedid;
	private String court_start_time;
	private String court_end_time;
	private String user_name;
	private String court_name;
	private String court_id;
	private String slots;
	private String court_reservation_id;
	private String court_reservation_user_id;
	private String court_reservation_bulkbooking_id;

	public boolean isItemRemoved() {
		return isItemRemoved;
	}

	public void setItemRemoved(boolean itemRemoved) {
		isItemRemoved = itemRemoved;
	}

	boolean isItemRemoved =true ;


	String cat_name;
	String cat_id;
	String court_reservation_end_time;
	String court_reservation_start_time;
	String court_reservation_purpuse;
	String cat_color;

	public boolean isCourtSelectable() {
		return isCourtSelectable;
	}

	public void setCourtSelectable(boolean courtSelectable) {
		isCourtSelectable = courtSelectable;
	}

	boolean isCourtSelectable ;



	public String getCourt_reservation_start_time() {
		return court_reservation_start_time;
	}

	public void setCourt_reservation_start_time(String court_reservation_start_time) {
		this.court_reservation_start_time = court_reservation_start_time;
	}

	public String getCat_name() {
		return cat_name;
	}

	public void setCat_name(String cat_name) {
		this.cat_name = cat_name;
	}

	public String getCat_id() {
		return cat_id;
	}

	public void setCat_id(String cat_id) {
		this.cat_id = cat_id;
	}

	public String getCourt_reservation_end_time() {
		return court_reservation_end_time;
	}

	public void setCourt_reservation_end_time(String court_reservation_end_time) {
		this.court_reservation_end_time = court_reservation_end_time;
	}

	public String getCat_color() {
		return cat_color;
	}

	public void setCat_color(String cat_color) {
		this.cat_color = cat_color;
	}




	public String getMemberbookedid() {
		return memberbookedid;
	}

	public void setMemberbookedid(String memberbookedid) {
		this.memberbookedid = memberbookedid;
	}

	public String getCourt_reservation_purpuse() {
		return court_reservation_purpuse;
	}

	public void setCourt_reservation_purpuse(String court_reservation_purpuse) {
		this.court_reservation_purpuse = court_reservation_purpuse;
	}



	public String getCourt_start_time() {
		return court_start_time;
	}

	public String getRecursive_id() {
		return recursive_id;
	}

	public void setRecursive_id(String recursive_id) {
		this.recursive_id = recursive_id;
	}

	public void setCourt_start_time(String court_start_time) {
		this.court_start_time = court_start_time;
	}

	public String getCourt_end_time() {
		return court_end_time;
	}

	public void setCourt_end_time(String court_end_time) {
		this.court_end_time = court_end_time;
	}



	public String getCourt_id() {
		return court_id;
	}

	public void setCourt_id(String court_id) {
		this.court_id = court_id;
	}


	
	public String getCourt_reservation_bulkbooking_id() {
		return court_reservation_bulkbooking_id;
	}

	public void setCourt_reservation_bulkbooking_id(String court_reservation_bulkbooking_id) {
		this.court_reservation_bulkbooking_id = court_reservation_bulkbooking_id;
	}



	public String getSlots() {
		return slots;
	}

	public void setSlots(String slots) {
		this.slots = slots;
	}

	public String getCourt_reservation_id() {
		return court_reservation_id;
	}

	public void setCourt_reservation_id(String court_reservation_id) {
		this.court_reservation_id = court_reservation_id;
	}

	public String getCourt_reservation_user_id() {
		return court_reservation_user_id;
	}

	public void setCourt_reservation_user_id(String court_reservation_user_id) {
		this.court_reservation_user_id = court_reservation_user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getCourt_name() {
		return court_name;
	}

	public void setCourt_name(String court_name) {
		this.court_name = court_name;
	}
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
