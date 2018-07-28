package com.clubscaddy.Bean;

/**
 * Created by administrator on 12/6/17.
 */

public class Match
{
    String sno ;
    String date ;
    String time ;

    public int getMatch_id() {
        return match_id;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    int match_id ;

    public String getServerDate() {
        return serverDate;
    }

    public void setServerDate(String serverDate) {
        this.serverDate = serverDate;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    String serverDate ;
    String serverTime ;



    String location ;
    String myAvailablity ;

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMyAvailablity() {
        return myAvailablity;
    }

    public int getListItemPos() {
        return listItemPos;
    }

    public void setListItemPos(int listItemPos) {
        this.listItemPos = listItemPos;
    }

    public void setMyAvailablity(String myAvailablity) {
        this.myAvailablity = myAvailablity;
    }

    int listItemPos = 0 ;

    public String getMatch_opponet() {
        return match_opponet;
    }

    public void setMatch_opponet(String match_opponet) {
        this.match_opponet = match_opponet;
    }

    String match_opponet;


}
