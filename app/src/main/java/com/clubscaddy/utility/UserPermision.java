package com.clubscaddy.utility;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.clubscaddy.Interface.MyPermissionGrrantedListner;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deepak Gehlot on 7/7/2018.
 */

public class UserPermision
{
    static  String []notPermittedPermissionList ;

    public static boolean checkPermission(final Activity context , final String exteranalStorage[] , final int exteranalStorageRequestCode)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        ArrayList<String>notPermittedPermissionArrayList ;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            boolean shouldShowRequestPermissionRationale = false ;
            boolean isAnyRequestNotPermitted  = false ;
            notPermittedPermissionArrayList = new ArrayList<>();
            for (String permission : exteranalStorage)
            {
                if (isPermissionGranted(context ,permission) == false)
                {
                    isAnyRequestNotPermitted = true ;
                    notPermittedPermissionArrayList.add(permission);

                }
                if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission))
                {
                    shouldShowRequestPermissionRationale = true ;
                }
            }

            notPermittedPermissionList = new String[notPermittedPermissionArrayList.size()];
            int i = 0;
            for (String notPermittedPermission : notPermittedPermissionArrayList)
            {
                notPermittedPermissionList[i] = notPermittedPermission;
                i++;
            }


            if (isAnyRequestNotPermitted)
            {
                if (shouldShowRequestPermissionRationale)
                {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which)
                        {
                            ActivityCompat.requestPermissions(context, notPermittedPermissionList, exteranalStorageRequestCode);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, notPermittedPermissionList, exteranalStorageRequestCode);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static void updatePermission(Activity activity , ArrayList<String>
            permissionList ,
                final MyPermissionGrrantedListner myPermissionGrrantedListner ,final int code)
    {
        Dexter.withActivity(activity)
                .withPermissions(permissionList)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        myPermissionGrrantedListner.isAllPermissionGranted(report.areAllPermissionsGranted() , code);


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    public static void updatePermission(Activity activity , ArrayList<String>
            permissionList ,
                                        final MyPermissionGrrantedListner
                                                myPermissionGrrantedListner)
    {
        Dexter.withActivity(activity)
                .withPermissions(permissionList)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        myPermissionGrrantedListner.isAllPermissionGranted(report.
                                areAllPermissionsGranted() ,0);


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }



    public static boolean isAllPermissionIsGranted(final Activity context , final ArrayList<String> permissionList )
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            for (String permission : permissionList) {
                if (!isPermissionGranted(context, permission))
                {
                  return false ;
                }

            }


        }

        return true ;

    }






    public static boolean checkPermission(final Activity context , final String exteranalStorage , final int exteranalStorageRequestCode)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        ArrayList<String>notPermittedPermissionArrayList ;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            boolean shouldShowRequestPermissionRationale = false ;
            boolean isAnyRequestNotPermitted  = false ;
            notPermittedPermissionArrayList = new ArrayList<>();


            if (isPermissionGranted(context ,exteranalStorage) == false)
            {
                isAnyRequestNotPermitted = true ;
                notPermittedPermissionArrayList.add(exteranalStorage);

            }


            notPermittedPermissionList = new String[notPermittedPermissionArrayList.size()];
            int i = 0;
            for (String notPermittedPermission : notPermittedPermissionArrayList)
            {
                notPermittedPermissionList[i] = notPermittedPermission;
                i++;
            }


            if (isAnyRequestNotPermitted)
            {
                if (shouldShowRequestPermissionRationale)
                {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which)
                        {
                            ActivityCompat.requestPermissions(context, notPermittedPermissionList, exteranalStorageRequestCode);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, notPermittedPermissionList, exteranalStorageRequestCode);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public static boolean isPermissionGranted(Activity activity , String exteranalStorage)
    {
        if (ContextCompat.checkSelfPermission(activity, exteranalStorage) == PackageManager.PERMISSION_GRANTED)
        {
            return true;

        }
        else
        {
            return false;

        }
    }
}