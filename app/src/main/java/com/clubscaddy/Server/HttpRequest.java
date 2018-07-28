package com.clubscaddy.Server;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;


import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import com.androidquery.AQuery;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.Interface.BackServiceRespondingListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.fragment.UserProfileActivity;
import com.clubscaddy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by administrator on 9/6/16.
 */
public class HttpRequest
{
 ProgressDialog progressDialog;
    Activity activity;

    AQuery aQuery;

    Context context ;

    public HttpRequest(Activity activity)
    {
     this.activity = activity;
        aQuery = new AQuery(activity);
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

    }
    public HttpRequest(Context context)
    {
        this.context = context;
        aQuery = new AQuery(context);


    }

    public void getResponse(Activity activity, String url, HashMap<String, Object> params , final OnServerRespondingListener listener)
    {






        AjaxCallback<JSONObject> callback = new AjaxCallback<JSONObject>()
        {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                super.callback(url, object, status);
                progressDialog.dismiss();
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

        if (!Validation.isNetworkAvailable(activity))
        {
            listener.internetConnectionProble();

        }
        else
        {
            progressDialog.show();

            aQuery.ajax(url, params, JSONObject.class, callback);
        }





    }

    public void getResponse(Activity activity, String url , final OnServerRespondingListener listener)
    {
























        AjaxCallback<String> callback = new AjaxCallback<String>()
        {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                super.callback(url, object, status);
                progressDialog.dismiss();
                if (status.getCode() == 200)
                {
                    try {
                        listener.onSuccess(new JSONObject(object));
                    } catch (JSONException e) {
                        e.printStackTrace();
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

        if (!Validation.isNetworkAvailable(activity))
        {
            listener.internetConnectionProble();

        }
        else
        {
            progressDialog.show();

            aQuery.ajax(url , String.class , callback);


        }





    }





    public void getResponseWihhoutPd(Activity activity, String url, HashMap<String, Object> params , final OnServerRespondingListener listener)
    {

        if (!Validation.isNetworkAvailable(activity))
        {
            listener.internetConnectionProble();

        }



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






    }



    public void cancelAjax()
    {


        if (aQuery != null)
        aQuery.ajaxCancel();


    }












    public void getResponseInBackGround(String url, HashMap<String, Object> params , final BackServiceRespondingListener listener)
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
                       // listener.onNetWorkError();
                    }
                }
                else
                {



                    if (status.getCode() == AjaxStatus.NETWORK_ERROR)
                    {
                       // listener.onNetWorkError();
                    }
                    else
                    {
                        if (status.getCode() == AjaxStatus.TRANSFORM_ERROR)
                        {
                          //  listener.onConnectionError();
                        }
                    }
                }

            }
        };
        ;

        if (!Validation.isNetworkAvailable(context))
        {
            //listener.internetConnectionProble();

        }
        else
        {

            aQuery.ajax(url, params, JSONObject.class, callback);
        }





    }



}
