package com.clubscaddy.Server;


import android.content.Context;


import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import com.androidquery.AQuery;
import com.clubscaddy.BackGroundServies.BackgroundService;


import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by administrator on 9/6/16.
 */
public class HttpRequest1
{

    Context activity;

    AQuery aQuery;
    public HttpRequest1(Context activity)
    {
        this.activity = activity;
        aQuery = new AQuery(activity);

    }


    public HttpRequest getResponse( String url, HashMap<String, Object> params , final BackgroundService.OnServerRespondingListener listener)
    {




        AjaxCallback<JSONObject> callback = new AjaxCallback<JSONObject>()
        {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                super.callback(url, object, status);

                if (status.getCode() == 200)
                {

                    if(object != null)
                    {
                        listener.onSuccess(object);
                    }
                    else
                    {
                        listener.onNetWorkError();
                    }
                }
                else
                {



                    if (status.getCode() == AjaxStatus.NETWORK_ERROR)
                    {
                        listener.onNetWorkError();
                    }
                    else
                    {
                        if (status.getCode() == AjaxStatus.TRANSFORM_ERROR)
                        {
                            listener.onConnectionError();
                        }
                    }
                }

            }
        };
        ;

        aQuery.ajax(url, params, JSONObject.class, callback);


        return null;
    }













}


