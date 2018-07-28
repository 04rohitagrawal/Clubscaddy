package com.clubscaddy.Bean;

import java.util.ArrayList;

/**
 * Created by administrator on 23/5/17.
 */

public class League
        {


            int league_uid ;
            int league_id;
            String league_name;

            public int getLeague_uid() {
                return league_uid;
            }

            public void setLeague_uid(int league_uid) {
                this.league_uid = league_uid;
            }

            public int getLeague_id() {
                return league_id;
            }

            public void setLeague_id(int league_id) {
                this.league_id = league_id;
            }

            public String getLeague_name() {
                return league_name;
            }

            public void setLeague_name(String league_name) {
                this.league_name = league_name;
            }

            ArrayList<LeagueUser>leagueMemberList ;
            ArrayList<MatchDetail>matchDetailArrayList ;






}
