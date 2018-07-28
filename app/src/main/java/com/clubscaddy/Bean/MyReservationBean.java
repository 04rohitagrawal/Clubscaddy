package com.clubscaddy.Bean;

import java.util.ArrayList;

public class MyReservationBean 
{
 
ArrayList<CourtBookBean>court_list = new ArrayList<CourtBookBean>();
 
 String court_reservation_recursiveid;
 String reservedate;
 String memberbookedid;
String recursivedates;
String bookingname;

	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}

	String reservationId ;

public String getBookingname() {
	return bookingname;
}

public void setBookingname(String bookingname) {
	this.bookingname = bookingname;
}

public String getRecursivedates() {
	return recursivedates;
}

public void setRecursivedates(String recursivedates) {
	this.recursivedates = recursivedates;
}

public String getMemberbookedid() {
	return memberbookedid;
}

public void setMemberbookedid(String memberbookedid) {
	this.memberbookedid = memberbookedid;
}

public String getReservedate() {
	return reservedate;
}

public void setReservedate(String reservedate) {
	this.reservedate = reservedate;
}

public String getCourt_reservation_recursiveid() {
	return court_reservation_recursiveid;
}

public void setCourt_reservation_recursiveid(String court_reservation_recursiveid) {
	this.court_reservation_recursiveid = court_reservation_recursiveid;
}

public ArrayList<CourtBookBean> getCourt_list() {
	return court_list;
}

public void setCourt_list(ArrayList<CourtBookBean> court_list) {
	this.court_list = court_list;
}	
	
	
}
