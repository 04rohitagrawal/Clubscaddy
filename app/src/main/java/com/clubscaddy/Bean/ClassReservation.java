package com.clubscaddy.Bean;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by administrator on 13/12/17.
 */

@SuppressLint("ParcelCreator")
public class ClassReservation implements Parcelable
{
    String classColor;
    String className;
    String classId;
    String classDetailId;
    String classDate;
    String classStartTime;






    public boolean isOldedDate() {
        return isOldedDate;
    }

    public void setOldedDate(boolean oldedDate) {
        isOldedDate = oldedDate;
    }

    boolean isOldedDate ;

    public String getClassColor() {
        return classColor;
    }

    public void setClassColor(String classColor) {
        this.classColor = classColor;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassDetailId() {
        return classDetailId;
    }

    public void setClassDetailId(String classDetailId) {
        this.classDetailId = classDetailId;
    }

    public String getClassDate() {
        return classDate;
    }

    public void setClassDate(String classDate) {
        this.classDate = classDate;
    }

    public String getClassStartTime() {
        return classStartTime;
    }

    public void setClassStartTime(String classStartTime) {
        this.classStartTime = classStartTime;
    }

    public String getClassEndTime() {
        return classEndTime;
    }

    public void setClassEndTime(String classEndTime) {
        this.classEndTime = classEndTime;
    }




    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    String dayName ;


    public boolean isItemSelected() {
        return isItemSelected;
    }

    public void setItemSelected(boolean itemSelected) {
        isItemSelected = itemSelected;
    }

    boolean isItemSelected ;
    String classEndTime ;

    String classCost ;

    public String getClassCost() {
        return classCost;
    }

    public void setClassCost(String classCost) {
        this.classCost = classCost;
    }

    public long getClassNoOfParticipents() {
        return classNoOfParticipents;
    }

    public void setClassNoOfParticipents(long classNoOfParticipents)
    {
        this.classNoOfParticipents = classNoOfParticipents;
    }

    public long getClassParticipents()
    {
        return classParticipents;
    }

    public void setClassParticipents(long classParticipents)
    {
        this.classParticipents = classParticipents;
    }

    public String getClassReserveId() {
        return classReserveId;
    }

    public void setClassReserveId(String classReserveId) {
        this.classReserveId = classReserveId;
    }

    long classNoOfParticipents ;
    long classParticipents ;
    String classReserveId ;


    public String getClassMemberUid() {
        return classMemberUid;
    }

    public void setClassMemberUid(String classMemberUid) {
        this.classMemberUid = classMemberUid;
    }

    String  classMemberUid ;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }


}
