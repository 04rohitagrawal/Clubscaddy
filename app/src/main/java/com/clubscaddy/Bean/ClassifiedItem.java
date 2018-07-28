package com.clubscaddy.Bean;

import java.util.ArrayList;

/**
 * Created by administrator on 6/5/17.
 */

public class ClassifiedItem {

    int classifieds_id;


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    double distance;


    public String getClassifieds_user_contact() {
        return classifieds_user_contact;
    }

    public void setClassifieds_user_contact(String classifieds_user_contact) {
        this.classifieds_user_contact = classifieds_user_contact;
    }

    String classifieds_user_contact;

    public int getClassifieds_id() {
        return classifieds_id;
    }

    public void setClassifieds_id(int classifieds_id) {
        this.classifieds_id = classifieds_id;
    }

    public int getClassifieds_uid() {
        return classifieds_uid;
    }

    public void setClassifieds_uid(int classifieds_uid) {
        this.classifieds_uid = classifieds_uid;
    }

    public String getClassifieds_title() {
        return classifieds_title;
    }

    public void setClassifieds_title(String classifieds_title) {
        this.classifieds_title = classifieds_title;
    }

    public String getClassifieds_desc() {
        return classifieds_desc;
    }

    public void setClassifieds_desc(String classifieds_desc) {
        this.classifieds_desc = classifieds_desc;
    }

    public String getClassifieds_profile() {
        return classifieds_profile;
    }

    public void setClassifieds_profile(String classifieds_profile) {
        this.classifieds_profile = classifieds_profile;
    }

    public ArrayList<ClassifiedItemOtherPicture> getClassified_other_pics() {
        return classified_other_pics;
    }

    public void setClassified_other_pics(ArrayList<ClassifiedItemOtherPicture> classified_other_pics) {
        this.classified_other_pics = classified_other_pics;
    }

    public double getClassifieds_cost() {
        return classifieds_cost;
    }

    public void setClassifieds_cost(double classifieds_cost) {
        this.classifieds_cost = classifieds_cost;
    }

    public double getClassifieds_lat() {
        return classifieds_lat;
    }

    public void setClassifieds_lat(double classifieds_lat) {
        this.classifieds_lat = classifieds_lat;
    }

    public double getClassifieds_long() {
        return classifieds_long;
    }

    public void setClassifieds_long(double classifieds_long) {
        this.classifieds_long = classifieds_long;
    }

    public String getClassifieds_address() {
        return classifieds_address;
    }

    public void setClassifieds_address(String classifieds_address) {
        this.classifieds_address = classifieds_address;
    }

    public String getClassifieds_edate() {
        return classifieds_edate;
    }

    public void setClassifieds_edate(String classifieds_edate) {
        this.classifieds_edate = classifieds_edate;
    }

    int classifieds_uid;
    String classifieds_title;
    String classifieds_desc;
    String classifieds_profile;
    ArrayList<ClassifiedItemOtherPicture> classified_other_pics;
    double classifieds_cost;
    double classifieds_lat;
    double classifieds_long;
    String classifieds_address;
    String classifieds_edate ;
    String user_name;
    String user_email;

    public String getUser_profile_pic() {
        return user_profile_pic;
    }

    public void setUser_profile_pic(String user_profile_pic) {
        this.user_profile_pic = user_profile_pic;
    }

    String user_profile_pic;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    String user_phone;


    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    int comment_count;

    public ArrayList<ClassifiedCommentBean> getClassifiedCommentBeanArrayList() {
        return classifiedCommentBeanArrayList;
    }

    public void setClassifiedCommentBeanArrayList(ArrayList<ClassifiedCommentBean> classifiedCommentBeanArrayList) {
        this.classifiedCommentBeanArrayList = classifiedCommentBeanArrayList;
    }

    ArrayList<ClassifiedCommentBean>classifiedCommentBeanArrayList ;


    public String getCmtText() {
        return cmtText;
    }

    public void setCmtText(String cmtText) {
        this.cmtText = cmtText;
    }

    String cmtText = "" ;





}
