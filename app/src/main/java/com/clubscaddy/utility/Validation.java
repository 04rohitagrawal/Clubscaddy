package com.clubscaddy.utility;

import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.EditText;
import android.widget.Toast;

public class Validation {

	// Regular Expression
    // you can change the expression based on your need
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "\\d{3}-\\d{7}";
    // Error Messages
    private static final String REQUIRED_MSG = "required";
    private static final String EMAIL_MSG = "invalid email";
    private static final String PHONE_MSG = "###-#######";
 
    // call this method when you need to check email validation
    public static boolean isEmailAddress(EditText editText, boolean required) {
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
    }
 
    // call this method when you need to check phone number validation
    public static boolean isPhoneNumber(EditText editText, boolean required) {
        return isValid(editText, PHONE_REGEX, PHONE_MSG, required);
    }
 
    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {
        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);
        // text required and editText is blank, so return false
        if ( required && !hasText(editText) ) return false;
        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        };
        return true;
    }
 
    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText) {
        String text = editText.getText().toString().trim();
        editText.setError(null);
        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }
        return true;
    }
    public static boolean isStringBlack(String text)
    {
    	if(text == ""||text.equals(""))
    	{
    		return true;
    	}
    	
    	return false;
    	
    }




    public static boolean isStringNullOrBlank(String str) {

        try{

            if (str == null) {
                return true;
            } else if (str.equals("null") || str.equals("") || (str != null && str.isEmpty())) {
                return true;
            }

        }catch(Exception e){

            e.printStackTrace();
        }
        return false;
    }


    /**
     * this method checks two string that is match or not
     * @param str1
     * @param st2
     * @return
     */
    public static boolean isStringEqual(String str1,String st2) {

        try{

            if (str1 == null) {

                return false;

            }else  if (st2 == null) {

                return false;

            } else if (str1.equalsIgnoreCase(st2)) {

                return true;

            }

        }catch(Exception e){

            e.printStackTrace();
        }
        return false;
    }


   /* public static boolean isStringMismatch(String p1,String p2){

        if(Validation.isStringNullOrBlank(p1) || Validation.isStringNullOrBlank(p2)){

            return true;

        }

        if(p1.equalsIgnoreCase(p2)){

            return false;

        }

        return  true;
    }*/

    public static boolean isNetworkAvailable(Context context) {

        NetworkInfo localNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return (localNetworkInfo != null) && (localNetworkInfo.isConnected());

    }

    public static final String networkError = "No network";

    public static void showNetworkError(Context ctx) {

        try {
            if (ctx != null) {
                Toast.makeText(ctx, networkError, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isValidPhoneNumber(CharSequence phoneNumber) {



        return android.util.Patterns.PHONE.matcher(phoneNumber.toString()).matches();


    }


    public static final void showToast(Context mContext, String msg) {

        Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();

    }



}