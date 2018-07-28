package com.clubscaddy.Bean;

/**
 * Created by administrator on 27/4/17.
 */

public class CoachSlot implements Cloneable
{
    public String getCoach_id() {
        return coach_id;
    }

    public void setCoach_id(String coach_id) {
        this.coach_id = coach_id;
    }

    public String getCoach_start_time() {
        return coach_start_time;
    }

    public void setCoach_start_time(String coach_start_time) {
        this.coach_start_time = coach_start_time;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMemberbookedid()
    {
        if (memberbookedid.equals("0"))
        {
            memberbookedid = "";
        }


        return memberbookedid;
    }

    public void setMemberbookedid(String memberbookedid) {
        this.memberbookedid = memberbookedid;
    }

    public String getCoach_user_id() {
        return coach_user_id;
    }

    public void setCoach_user_id(String coach_user_id) {
        this.coach_user_id = coach_user_id;
    }

    public String getCoach_bulkbooking_id() {
        return coach_bulkbooking_id;
    }

    public void setCoach_bulkbooking_id(String coach_bulkbooking_id) {
        this.coach_bulkbooking_id = coach_bulkbooking_id;
    }

    public String getCoach_recursiveid() {

        if (coach_recursiveid.equals("0"))
        {
            coach_recursiveid = "";
        }

        return coach_recursiveid;
    }

    public void setCoach_recursiveid(String coach_recursiveid) {
        this.coach_recursiveid = coach_recursiveid;
    }

    public String getSlots() {
        return slots;
    }

    public void setSlots(String slots) {
        this.slots = slots;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCourt_id() {
        return court_id;
    }

    public void setCourt_id(String court_id) {
        this.court_id = court_id;
    }

    public String getCoach_purpuse() {
        return coach_purpuse;
    }

    public void setCoach_purpuse(String coach_purpuse) {
        this.coach_purpuse = coach_purpuse;
    }

    public String getCoach_end_time() {
        return coach_end_time;
    }

    public void setCoach_end_time(String coach_end_time) {
        this.coach_end_time = coach_end_time;
    }

    String    coach_id ;
    String       coach_start_time ;
    String       user_name ;
    String       memberbookedid ;
    String       coach_user_id ;
    String       coach_bulkbooking_id ;
    String      coach_recursiveid ;
    String      slots ;
    String     date ;
    String       court_id ;
    String        coach_purpuse ;
    String        coach_end_time ;

    public boolean isCourtSelectable() {
        return isCourtSelectable;
    }

    public void setCourtSelectable(boolean courtSelectable) {
        isCourtSelectable = courtSelectable;
    }

    boolean isCourtSelectable;

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}
