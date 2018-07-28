package com.clubscaddy.Bean;

/**
 * Created by administrator on 19/6/17.
 */

public class LeagueUser
{

    int league_user_id;
    String league_user_name;

    public int getLeague_user_id() {
        return league_user_id;
    }

    public void setLeague_user_id(int league_user_id) {
        this.league_user_id = league_user_id;
    }

    public String getLeague_user_name() {
        return league_user_name;
    }

    public void setLeague_user_name(String league_user_name) {
        this.league_user_name = league_user_name;
    }

    public String getLeague_user_profile() {
        return league_user_profile;
    }

    public void setLeague_user_profile(String league_user_profile) {
        this.league_user_profile = league_user_profile;
    }

    public int getMatch_user_status() {
        return match_user_status;
    }

    public void setMatch_user_status(int match_user_status) {
        this.match_user_status = match_user_status;
    }

    String league_user_profile;
    int match_user_status;



}
