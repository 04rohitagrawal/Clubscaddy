package com.clubscaddy.Bean;

public class VerifyResponse {
	  private String strart_time;
	  private String end_time;
	  public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	private String court_reservation_date;
	  private String court_reservation_court_id;
	  private String court_reservation_court_name;
	  public String getCourt_reservation_court_name() {
		return court_reservation_court_name;
	}
	public void setCourt_reservation_court_name(String court_reservation_court_name) {
		this.court_reservation_court_name = court_reservation_court_name;
	}
	private String court_reservation_club_id;
	  private String booked;
	public String getStrart_time() {
		return strart_time;
	}
	public void setStrart_time(String strart_time) {
		this.strart_time = strart_time;
	}
	public String getCourt_reservation_date() {
		return court_reservation_date;
	}
	public void setCourt_reservation_date(String court_reservation_date) {
		this.court_reservation_date = court_reservation_date;
	}
	public String getCourt_reservation_court_id() {
		return court_reservation_court_id;
	}
	public void setCourt_reservation_court_id(String court_reservation_court_id) {
		this.court_reservation_court_id = court_reservation_court_id;
	}
	public String getCourt_reservation_club_id() {
		return court_reservation_club_id;
	}
	public void setCourt_reservation_club_id(String court_reservation_club_id) {
		this.court_reservation_club_id = court_reservation_club_id;
	}
	public String getBooked() {
		return booked;
	}
	public void setBooked(String booked) {
		this.booked = booked;
	}
}
