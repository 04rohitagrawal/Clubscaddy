package com.clubscaddy.Bean;

/**
 * Created by administrator on 6/12/17.
 */

public class Week
{
    String weekName ;

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public boolean isWeekSelected() {
        return isWeekSelected;
    }

    public void setWeekSelected(boolean weekSelected) {
        isWeekSelected = weekSelected;
    }

    boolean isWeekSelected ;


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    int index ;
}
