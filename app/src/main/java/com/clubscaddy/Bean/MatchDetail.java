package com.clubscaddy.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by administrator on 19/6/17.
 */

public class MatchDetail implements Parcelable
{
    public int getMatch_id() {
        return match_id;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    public String getMatch_date() {
        return match_date;
    }

    public void setMatch_date(String match_date) {
        this.match_date = match_date;
    }

    public String getMatch_time() {
        return match_time;
    }

    public void setMatch_time(String match_time) {
        this.match_time = match_time;
    }

    public String getMatch_location() {
        return match_location;
    }

    public void setMatch_location(String match_location) {
        this.match_location = match_location;
    }

    public int getMatch_user_status() {
        return match_user_status;
    }

    public void setMatch_user_status(int match_user_status) {
        this.match_user_status = match_user_status;
    }

    public String getMatch_createAt() {
        return match_createAt;
    }

    public void setMatch_createAt(String match_createAt) {
        this.match_createAt = match_createAt;
    }

    public HashMap<String, Integer> getMatchAllUserStatus() {
        return matchAllUserStatus;
    }

    public void setMatchAllUserStatus(HashMap<String, Integer> matchAllUserStatus) {
        this.matchAllUserStatus = matchAllUserStatus;
    }

    int match_id;
    String match_date;
    String match_time;
    String match_location;
    int match_user_status;
    String match_createAt;
    HashMap<String , Integer>matchAllUserStatus ;

    public String getMatch_opponet() {
        return match_opponet;
    }

    public void setMatch_opponet(String match_opponet) {
        this.match_opponet = match_opponet;
    }

    String match_opponet ;


    public int getListItemPos() {
        return listItemPos;
    }

    public void setListItemPos(int listItemPos) {
        this.listItemPos = listItemPos;
    }

    int listItemPos ;



    public ArrayList<LeagueUser> getMatchAllMemberList() {
        return matchAllMemberList;
    }

    public void setMatchAllMemberList(ArrayList<LeagueUser> matchAllMemberList)
    {
        this.matchAllMemberList = matchAllMemberList;
    }

    public void addMatchAllMemberList(LeagueUser leagueUser)
    {
        matchAllMemberList.add(leagueUser);
    }

    ArrayList<LeagueUser> matchAllMemberList ;

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }



    /*  "total_yes": "1",
            "total_no": "0",
            "total_later": "0",
            "total_last_call": "0",
            "total_member": "1",
            "not_responding": "0"*/

}
