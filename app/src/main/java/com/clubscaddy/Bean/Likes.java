package com.clubscaddy.Bean;

/**
 * Created by administrator on 23/12/16.
 */

public class Likes {


    int news_feed_like_id;
    int news_feed_like_user_id;
    int news_feed_id;
    String user_name;
    String user_profilepic;

    public int getNews_feed_like_id() {
        return news_feed_like_id;
    }

    public void setNews_feed_like_id(int news_feed_like_id) {
        this.news_feed_like_id = news_feed_like_id;
    }

    public int getNews_feed_like_user_id() {
        return news_feed_like_user_id;
    }

    public void setNews_feed_like_user_id(int news_feed_like_user_id) {
        this.news_feed_like_user_id = news_feed_like_user_id;
    }

    public int getNews_feed_id() {
        return news_feed_id;
    }

    public void setNews_feed_id(int news_feed_id) {
        this.news_feed_id = news_feed_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_profilepic() {
        return user_profilepic;
    }

    public void setUser_profilepic(String user_profilepic) {
        this.user_profilepic = user_profilepic;
    }
}
