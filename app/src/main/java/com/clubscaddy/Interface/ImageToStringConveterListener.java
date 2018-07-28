package com.clubscaddy.Interface;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by administrator on 21/1/17.
 */

public abstract class ImageToStringConveterListener
{
    public abstract void onSuccess(boolean status , ArrayList<String>bitmapString) ;

    public abstract void onUpdate(int update) ;


    public abstract void onError();


    public abstract void onSuccess(boolean status , JSONArray imageOutPutJsonArray) ;



}
