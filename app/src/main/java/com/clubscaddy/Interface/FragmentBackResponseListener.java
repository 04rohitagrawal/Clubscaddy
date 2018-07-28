package com.clubscaddy.Interface;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by administrator on 30/11/17.
 */

public abstract  class FragmentBackResponseListener implements Serializable , Parcelable
{
    public void onResponse(String responsString , String reservationId)
    {

    }

    public void deleteSuccessFullyListener()
    {

    }

    public void UpdateView()
    {

    }


    public void onBackFragment()
    {

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
