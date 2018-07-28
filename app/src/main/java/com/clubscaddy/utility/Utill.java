package com.clubscaddy.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.clubscaddy.LoginActivity;
import com.clubscaddy.R;

public class Utill {
	public static Bitmap GetBitmapClippedCircle(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}

		Bitmap output;

		if (bitmap.getWidth() > bitmap.getHeight()) {
			output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Config.ARGB_8888);
		} else {
			output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Config.ARGB_8888);
		}

		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		float r = 0;

		if (bitmap.getWidth() > bitmap.getHeight()) {
			r = bitmap.getHeight() / 2;
		} else {
			r = bitmap.getWidth() / 2;
		}

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle(r, r, r, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	@SuppressWarnings("deprecation")
	public static void showDialgonactivity(String msg, Activity activity) {
		final Dialog alertDialog = new Dialog(activity);
		//LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//View v = inflater.inflate(R.layout.delete_confirmation, null);
		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog.setContentView(R.layout.confirm_dialog_layout);
		TextView doneTV, cancelTV, headerTV;
		doneTV = (TextView) alertDialog.findViewById(R.id.done);

		headerTV = (TextView) alertDialog.findViewById(R.id.title);
		headerTV.setText(msg);
		
		/*
		final CheckBox noshowCheck = (CheckBox) alertDialog.findViewById(R.id.noshow);


		
		if(SessionManager.getUser_type(activity).equalsIgnoreCase(AppConstants.USER_TYPE_MEMBER)){
			noshowCheck.setChecked(false);
			noshowCheck.setVisibility(View.GONE);
		}
		else
		{
			
	//	Toast.makeText(getActivity(), courtData.getCourt_reservation_user_id()+" "+SessionManager.getUser_id(mContext), 1).show();	
			
		if(!courtData.getCourt_reservation_user_id().equalsIgnoreCase(SessionManager.getUser_id(mContext)))
		{
			//
			noshowCheck.setVisibility(View.VISIBLE);	
		}
		}
		boolean exit = false;
		if(SessionManager.getUser_type(mContext).equalsIgnoreCase(AppConstants.USER_TYPE_DIRECTOR)){

			for(int i = 0 ; i < mainList.size();i++)
			{
				for(int j = 0 ; j < mainList.size();j++)
				{

					//	deleteCourtData.getSlots()s
					if(deleteCourtData.getCourt_id().equalsIgnoreCase((mainList.get(i).get(j).getCourt_id())))
					{
						if(deleteCourtData.getSlots().equalsIgnoreCase((mainList.get(i).get(j).getSlots())))


							if(mainList.get(i).get(j).getCourt_reservation_user_id().equalsIgnoreCase(SessionManager.getUserId(mContext)))
							{
								//Toast.makeText(getActivity(),i+" ssss "+j+" "+ deleteCourtData.getCourt_id()+" "+mainList.get(i).get(j).getCourt_id(), 1).show();

								noshowCheck.setChecked(false);
								noshowCheck.setVisibility(View.GONE);
								exit = true;
								break;
							}	
					}


					//court_reservation_user_id
				}

				if(exit)
					break;
				//court_reservation_user_id
			}


		}


		
		cancelTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.cancel();

			}
		});
		headerTV.setText(SessionManager.getClubName(mContext));
			*/
		alertDialog.getWindow().setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.whit_circle_for_dialog));    // (Color.parseColor("#FFFFFFFF"));

		alertDialog.show();
	}

	@SuppressWarnings("deprecation")
	public static void showDialg(String msg, Activity mContext) {

		final AlertDialog alertDialog = new AlertDialog.Builder(
				mContext).create();

// Setting Dialog Title
		alertDialog.setTitle(SessionManager.getClubName(mContext));

// Setting Dialog Message
		alertDialog.setMessage(msg);
		alertDialog.setCancelable(false);

// Setting Icon to Dialog


// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				alertDialog.dismiss();
			}
		});

// Showing Alert Message
		alertDialog.show();
	}



	public static void showDialg(String msg, Context mContext) {

		final AlertDialog alertDialog = new AlertDialog.Builder(
				mContext).create();

// Setting Dialog Title
		alertDialog.setTitle(SessionManager.getClubName(mContext));

// Setting Dialog Message
		alertDialog.setMessage(msg);
		alertDialog.setCancelable(false);

// Setting Icon to Dialog


// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				alertDialog.dismiss();
			}
		});

// Showing Alert Message
		alertDialog.show();
	}


	@SuppressWarnings("deprecation")
	public static void showDialgonActivity(String msg, LoginActivity mContext) {

		final AlertDialog alertDialog = new AlertDialog.Builder(
				mContext).create();

// Setting Dialog Title
		alertDialog.setTitle(SessionManager.getClubName(mContext));

// Setting Dialog Message
		alertDialog.setMessage(msg);

// Setting Icon to Dialog


// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				alertDialog.dismiss();
			}
		});

// Showing Alert Message
		alertDialog.show();
	}


	public static Bitmap generateScaledBitmap(Bitmap bitMap, ImageView imageView) {
		int bmWidth = bitMap.getWidth();
		int bmHeight = bitMap.getHeight();
		int ivWidth = imageView.getWidth();
		int ivHeight = imageView.getHeight();
		int new_width = ivWidth;

		int new_height = (int) Math.floor((double) bmHeight * ((double) new_width / (double) bmWidth));
		Bitmap bitmap = Bitmap.createScaledBitmap(bitMap, new_width, new_height, true);

		if (bitmap == null) {
			return null;
		}

		Bitmap output;

		if (bitmap.getWidth() > bitmap.getHeight()) {
			output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Config.ARGB_8888);
		} else {
			output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Config.ARGB_8888);
		}

		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		float r = 0;

		if (bitmap.getWidth() > bitmap.getHeight()) {
			r = bitmap.getHeight() / 2;
		} else {
			r = bitmap.getWidth() / 2;
		}

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle(r, r, r, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		imageView.setImageBitmap(bitmap);
		return output;
	}

	public static Bitmap GetBitmapClippedCircle(Bitmap bitmap, boolean a) {
		if (bitmap == null) {
			return null;
		}
		int width = 50;
		int height = 50;
		Bitmap output;

		if (bitmap.getWidth() > bitmap.getHeight()) {
			output = Bitmap.createBitmap(50, 50, Config.ARGB_8888);
		} else {
			output = Bitmap.createBitmap(50, 50, Config.ARGB_8888);
		}

		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, 50, 50);

		float r = 0;

		if (bitmap.getWidth() > bitmap.getHeight()) {
			r = 25;// bitmap.getHeight() / 2;
		} else {
			r = 25;// bitmap.getWidth() / 2;
		}

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawCircle(r, r, r, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public static void showSelector(Context mContext, final TextView textView, final String[] dataArray) {
		final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.listview, null);
		ListView listView = (ListView) v.findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.simpletextview, dataArray);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				textView.setText(dataArray[position]);
				alertDialog.dismiss();
			}
		});
		alertDialog.setView(v);
		alertDialog.show();
	}

	public static boolean isStringNullOrBlank(String str) {
		if (str == null) {
			return true;
		} else if (str.equals("null") || str.equals("")) {
			return true;
		}
		return false;
	}

	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	static ProgressDialog progress;

	// This method will be used for showing progress bar.
	public static void showProgress(Context mContext) {
		try {
			if (progress == null)
				progress = new ProgressDialog(mContext);
			progress.setMessage("Please Wait..");
			progress.setCancelable(false);
			progress.show();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				progress = new ProgressDialog(mContext);
				progress.setMessage("Please Wait..");
				progress.setCancelable(false);
				progress.show();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	// This method will show for hiding progressBar.
	public static void hideProgress() {
		if (progress != null) {
			progress.dismiss();
		}
	}

	public static int getMonthNumber(String month) {
		if (month.equalsIgnoreCase("January"))
			return 1;
		else if (month.equalsIgnoreCase("February"))
			return 2;
		else if (month.equalsIgnoreCase("March"))
			return 3;
		else if (month.equalsIgnoreCase("April"))
			return 4;
		else if (month.equalsIgnoreCase("May"))
			return 5;
		else if (month.equalsIgnoreCase("June"))
			return 6;
		else if (month.equalsIgnoreCase("July"))
			return 7;
		else if (month.equalsIgnoreCase("August"))
			return 8;
		else if (month.equalsIgnoreCase("September"))
			return 9;
		else if (month.equalsIgnoreCase("October"))
			return 10;
		else if (month.equalsIgnoreCase("November"))
			return 11;
		else if (month.equalsIgnoreCase("December"))
			return 12;

		return 1;
	}

	public static String formattedDateFromString(String inputFormat, String outputFormat, String inputDate) {
		if (inputFormat.equals("")) { // if inputFormat = "", set a default
			// input format.
			inputFormat = "dd-MM-yyyy";
		}
		if (outputFormat.equals("")) {
			outputFormat = "EEEE d MMMM  yyyy";
			// if inputFormat =
			// "", set a default
			// output format.
		}
		Date parsed = null;
		String outputDate = "";

		SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
		SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

		// You can set a different Locale, This example set a locale of Country
		// Mexico.
		// SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, new
		// Locale("es", "MX"));
		// SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, new
		// Locale("es", "MX"));

		try {
			parsed = df_input.parse(inputDate);
			outputDate = df_output.format(parsed);
		} catch (Exception e) {
			Log.e("formattedDateFromString", "Exception in formateDateFromstring(): " + e.getMessage());
		}
		return outputDate;

	}

	public static String getCurrentDate() {
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = df.format(c.getTime());
		return formattedDate;
	}


	public static String getNextDate() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		System.out.println("Current time => " + c.getTime());
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = df.format(c.getTime());
		return formattedDate;
	}

	public static String getNextDate(Calendar c) {

		c.add(Calendar.DATE, 1);
		System.out.println("Current time => " + c.getTime());
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = df.format(c.getTime());
		return formattedDate;
	}

	public static String getCurrentDateYYMMDD() {
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = df.format(c.getTime());
		return formattedDate;
	}


	public static String getDateDDMMYYYY(Calendar calendar) {
		System.out.println("Current time => " + calendar.getTime());
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = df.format(calendar.getTime());
		return formattedDate;
	}



	public static String reverseDate(String date) {
		String d[] = date.split("-");
		return d[2] + "-" + d[1] + "-" + d[0];
	}

	public static String loadJSONFromAsset(Context mContext) {
		String json = null;
		try {
			InputStream is = mContext.getAssets().open("court.json");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;

	}

	public static void vibrate(Context mContext) {
		try {
			Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
			if (v.hasVibrator())
				v.vibrate(200);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static String getNextDate(String curDate) {
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(curDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		return format.format(calendar.getTime());
	}

	public static String getPreviousDate(String curDate) {
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(curDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1);
		return format.format(calendar.getTime());
	}

	public static String getDateAfterTwoWeek(String curDate) {
		curDate = getCurrentDateYYMMDD();
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(curDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.WEEK_OF_MONTH, 2);
		return format.format(calendar.getTime());
	}

	public static String formattedDateFromString(String inputDate) {
		String inputFormat = "";
		String outputFormat = "";
		if (inputFormat.equals("")) { // if inputFormat = "", set a default
			// input format.
			inputFormat = "yyyy-MM-dd";
		}
		if (outputFormat.equals("")) {
			outputFormat = "EEEE d MMMM ',' yyyy";
			// if inputFormat =
			// "", set a default
			// output format.
		}
		Date parsed = null;
		String outputDate = "";

		SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
		SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

		// You can set a different Locale, This example set a locale of Country
		// Mexico.
		// SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, new
		// Locale("es", "MX"));
		// SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, new
		// Locale("es", "MX"));

		try {
			parsed = df_input.parse(inputDate);
			outputDate = df_output.format(parsed);
		} catch (Exception e) {
			Log.e("formattedDateFromString", "Exception in formateDateFromstring(): " + e.getMessage());
		}
		return outputDate;

	}

	public static Date getDateFromString(String dateStr) {
		// 2015-04-03 03:11 PM
		Calendar cal = Calendar.getInstance();
		int year = 0, monthOfYear = 0, dayOfMonth = 0;
		String d[] = dateStr.split("-");

		year = Integer.parseInt(d[0]);
		monthOfYear = Integer.parseInt(d[1]) - 1;
		dayOfMonth = Integer.parseInt(d[2]);

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, monthOfYear);
		cal.set(Calendar.DATE, dayOfMonth);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.HOUR, 0);
		cal.clear(Calendar.MILLISECOND);

		Date dati = cal.getTime();
		Log.e("Date", dati.toString());
		return cal.getTime();
	}

	public static Date getDateFromString1(String dateStr) {
		// 2015-04-03 03:11 PM
		Calendar cal = Calendar.getInstance();
		int year = 0, monthOfYear = 0, dayOfMonth = 0;
		String d[] = dateStr.split("-");

		year = Integer.parseInt(d[0]);
		monthOfYear = Integer.parseInt(d[1]) - 1;
		dayOfMonth = Integer.parseInt(d[2]);

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, monthOfYear);
		cal.set(Calendar.DATE, dayOfMonth);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MILLISECOND, 0);


		Date dati = cal.getTime();
		Log.e("Date", dati.toString());
		return cal.getTime();
	}

	public static boolean isNetworkAvailable(Activity mContext) {
		NetworkInfo localNetworkInfo = ((ConnectivityManager) mContext.getSystemService("connectivity")).getActiveNetworkInfo();
		return (localNetworkInfo != null) && (localNetworkInfo.isConnected());
	}

	@SuppressWarnings("deprecation")
	public static final void showToast(Context mContext, String msg) {
		// Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
		final AlertDialog alertDialog = new AlertDialog.Builder(
				mContext).create();

// Setting Dialog Title
		alertDialog.setTitle(SessionManager.getClubName(mContext));

// Setting Dialog Message
		alertDialog.setMessage(msg);

// Setting Icon to Dialog


// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				alertDialog.dismiss();
			}
		});

// Showing Alert Message
		alertDialog.show();
	}

	public static final void showNetworkError(Context mContext) {
		// Toast.makeText(mContext, "No network.", Toast.LENGTH_SHORT).show();
		String msg = mContext.getResources().getString(R.string.check_internet_connection);
		ShowUserMessage.showUserMessage(mContext, msg);

	}

	public static final void showNetworkError(Activity mContext) {
		// Toast.makeText(mContext, "No network.", Toast.LENGTH_SHORT).show();
		String msg = mContext.getResources().getString(R.string.check_internet_connection);
		ShowUserMessage.showUserMessage(mContext, msg);

	}

	public static ArrayList<String> getNumberofSundays(String d1, String d2) throws Exception { // object
		ArrayList<String> retrunList = new ArrayList<String>();
		Date date1 = getDate(d1);
		Date date2 = getDate(d2);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);

		clearTimeFromCalendr(c1);
		clearTimeFromCalendr(c2);
		int sundays = 0;
		while (c2.after(c1) || c1.equals(c2)) {
			// System.out.println(" came here ");
			// checks to see if the day1 ....so on next days are sundays if
			// sunday goes inside to increment the counter
			if (c1.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || c1.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
				System.out.println(c1.getTime().toString() + " is a sunday ");
				retrunList.add("" + getDateYYMMDDFromCal(c1));
				sundays++;
			}
			c1.add(Calendar.DATE, 1);
		}
		System.out.println("number of sundays between 2 dates is " + sundays);
		return retrunList;
	}

	// converts string to date
	public static Date getDate(String s) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(s);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	public static void clearTimeFromCalendr(Calendar cal) {
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.HOUR, 0);
		cal.clear(Calendar.MILLISECOND);
	}

	public static String getDateYYMMDDFromCal(Calendar cal) {
		Date date = cal.getTime();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		return ft.format(date);
	}

	public static String getRecurrentFinalDate(int incrementIn, int count, String curDate) {
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(curDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(incrementIn, count);
		return format.format(calendar.getTime());
	}

	public static ArrayList<String> getAllBetweenDatess(String d1, String d2, HashSet<String> weekSelected) throws Exception { // object
		ArrayList<String> retrunList = new ArrayList<String>();
		Date date1 = getDate(d1);
		Date date2 = getDate(d2);

		Log.e("date1 and date2",date1+" "+date2);

		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		clearTimeFromCalendr(c1);
		clearTimeFromCalendr(c2);
		int sundays = 0;

		while (c2.after(c1) ) {
			Iterator<String> it = weekSelected.iterator();
			while (it.hasNext()) {
				int day = Integer.parseInt(it.next());
				if (c1.get(Calendar.DAY_OF_WEEK) == day) {
					System.out.println(c1.getTime().toString() + " is a sunday ");
					retrunList.add("" + getDateYYMMDDFromCal(c1));
				}
			}
			c1.add(Calendar.DATE, 1);
		}
		System.out.println("number of sundays between 2 dates is " + sundays);
		return retrunList;
	}


	/*	public static ArrayList<String> getAllDaysList(String d1, String d2, HashSet<String> weekSelected) throws Exception { // object
            ArrayList<String> retrunList = new ArrayList<String>();
            Date date1 = getDate(d1);
            Date date2 = getDate(d2);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(date1);
            Calendar c2 = Calendar.getInstance();
            c2.setTime(date2);
            clearTimeFromCalendr(c1);
            clearTimeFromCalendr(c2);
            int sundays = 0;

            while (c2.after(c1) || c1.equals(c2))
            {
                Iterator<String> it = weekSelected.iterator();
                while (it.hasNext())
                {
                    int day = Integer.parseInt(it.next());
                    if (c1.get(Calendar.DAY_OF_WEEK) == day)
                    {
                        System.out.println(c1.getTime().toString() + " is a sunday ");
                        retrunList.add("" + getDateYYMMDDFromCal(c1));
                    }
                }
                c1.add(Calendar.DATE, 1);
            }
            System.out.println("number of sundays between 2 dates is " + sundays);
            return retrunList;
        }
    */
	public static String get24TimeFormate(String time) {
		SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
		SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
		Date date = null;
		try {
			date = parseFormat.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(parseFormat.format(date) + " = " + displayFormat.format(date));
		time = displayFormat.format(date);
		return time;
	}

	public static String get12TimeFormate(String time) {
		SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
		SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm");
		Date date = null;
		try {
			date = parseFormat.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(parseFormat.format(date) + " = " + displayFormat.format(date));
		time = displayFormat.format(date);
		return time;
	}

	public static void hideKeybord(Activity activity) {

		try {
			View view = activity.getCurrentFocus();
			if (view != null) {
				InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
		} catch (Exception e) {

		}

	}

	public static void hideKeybord(Activity activity , EditText editText) {

		try {
			InputMethodManager inputManager =
					(InputMethodManager) activity.
							getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(
					editText.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {

		}

	}



	public static void showKeybord(Context activity ,final AutoCompleteTextView editText) {

		try {
		final 	InputMethodManager inputManager =
					(InputMethodManager) activity.
							getSystemService(Context.INPUT_METHOD_SERVICE);
			//if (inputManager.isActive(editText) == false)
			{
				Handler handler = new Handler(activity.getMainLooper());

				handler.postDelayed(new Runnable() {
					@Override
					public void run() {

						inputManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);


					}
				},1000);
			}

		} catch (Exception e) {

		}

	}




	public static void openYouTube(Context mContext, String urlStr) {
		try {
			if (urlStr.startsWith("http://") || urlStr.startsWith("https://")) {

			} else {
				urlStr = "http://" + urlStr;
			}
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlStr));
			mContext.startActivity(browserIntent);
		} catch (Exception e) {
			Utill.showToast(mContext, "Not a valid Url");
		}

	}

	public static void setUnderLine(TextView tv) {
		String mystring = new String(tv.getText().toString());
		SpannableString content = new SpannableString(mystring);
		content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);

		tv.setText(content);
	}

	public static String removeSpace(String firstName) {
		String remoceString = "";

		char[] array = firstName.toCharArray();

		for (int i = 0; i < array.length; i++) {
			if (array[i] != ' ')
				remoceString = remoceString + array[i];
		}
		Log.e("remoceString",remoceString);
		return remoceString ;
	}


	public static boolean isValidLink(String webUrl)
	{
		if (Patterns.WEB_URL.matcher(webUrl).matches())
		{

			return true;
		}
		return false;
	}


	public static boolean isValidLinkWithSpace(String webUrl)
	{
		if (Patterns.WEB_URL.matcher(removeSpace(webUrl)).matches())
		{

			return true;
		}
		return false;
	}


	public static boolean compareDates(Calendar firstDate , Calendar SecondDate)
	{
		if (firstDate.get(Calendar.YEAR) > SecondDate.get(Calendar.YEAR))
		{
			return true ;
		}

		else
		{
			if (firstDate.get(Calendar.YEAR) == SecondDate.get(Calendar.YEAR))
			{
				if (firstDate.get(Calendar.MONTH) > SecondDate.get(Calendar.MONTH))
				{
					return true ;
				}
				else
				{

					{
						if (firstDate.get(Calendar.DATE) > SecondDate.get(Calendar.DATE))
						{
							return true ;
						}
					}

				}


			}
		}


		return false;
	}


	public static boolean compareTwoDate(Calendar firstDate , Calendar SecondDate)
	{
		if (firstDate.get(Calendar.YEAR) > SecondDate.get(Calendar.YEAR))
		{
			return true ;
		}

		else
		{

			Log.e("dateTime",firstDate.get(Calendar.MONTH) +" "+ SecondDate.get(Calendar.MONTH)+"");

			if (firstDate.get(Calendar.YEAR) == SecondDate.get(Calendar.YEAR))
			{
				if (firstDate.get(Calendar.MONTH) > SecondDate.get(Calendar.MONTH))
				{
					return true ;
				}
				else
				{
					if (firstDate.get(Calendar.MONTH) == SecondDate.get(Calendar.MONTH))
					{
						if (firstDate.get(Calendar.DATE) > SecondDate.get(Calendar.DATE))
						{
							return true ;
						}
					}

				}


			}
		}


		return false;
	}


	public static boolean compareTwoTime(Calendar firstDate , Calendar SecondDate)
	{
		Log.e("first date" , firstDate.get(Calendar.HOUR_OF_DAY) +" "+ SecondDate.get(Calendar.HOUR_OF_DAY)) ;


		if (firstDate.get(Calendar.HOUR_OF_DAY) > SecondDate.get(Calendar.HOUR_OF_DAY))
		{
			return true ;
		}

		else
		{
			if (firstDate.get(Calendar.HOUR_OF_DAY) == SecondDate.get(Calendar.HOUR_OF_DAY))
			{
				if (firstDate.get(Calendar.MINUTE) > SecondDate.get(Calendar.MINUTE))
				{
					return true ;
				}
				else
				{
					if (firstDate.get(Calendar.MINUTE) == SecondDate.get(Calendar.MINUTE))
					{
						if (firstDate.get(Calendar.SECOND) > SecondDate.get(Calendar.SECOND))
						{
							return true ;
						}
					}

				}


			}
		}


		return false;
	}




	public static boolean compareTwoHourAndMonute(Calendar firstDate , Calendar SecondDate)
	{
		Log.e("first date" , firstDate.get(Calendar.HOUR_OF_DAY) +" "+ SecondDate.get(Calendar.HOUR_OF_DAY)) ;


		if (firstDate.get(Calendar.HOUR_OF_DAY) > SecondDate.get(Calendar.HOUR_OF_DAY))
		{
			return true ;
		}

		else
		{
			if (firstDate.get(Calendar.HOUR_OF_DAY) == SecondDate.get(Calendar.HOUR_OF_DAY))
			{
				if (firstDate.get(Calendar.MINUTE) > SecondDate.get(Calendar.MINUTE))
				{
					return true ;
				}


			}
		}


		return false;
	}


	public static boolean compareTwoDateAndTime(Calendar firstDate , Calendar SecondDate)
	{
		if (firstDate.get(Calendar.YEAR) > SecondDate.get(Calendar.YEAR))
		{
			return true ;
		}

		else
		{
			if (firstDate.get(Calendar.YEAR) == SecondDate.get(Calendar.YEAR))
			{
				if (firstDate.get(Calendar.MONTH) > SecondDate.get(Calendar.MONTH))
				{
					return true ;
				}
				else
				{
					if (firstDate.get(Calendar.MONTH) == SecondDate.get(Calendar.MONTH))
					{
						if (firstDate.get(Calendar.DATE) > SecondDate.get(Calendar.DATE))
						{
							return true ;
						}
						else
						{
							if (firstDate.get(Calendar.DATE) == SecondDate.get(Calendar.DATE))
							{
								if (firstDate.get(Calendar.HOUR_OF_DAY) > SecondDate.get(Calendar.HOUR_OF_DAY))
								{
									return true ;
								}
								else
								{
									if (firstDate.get(Calendar.HOUR_OF_DAY) == SecondDate.get(Calendar.HOUR_OF_DAY))
									{
										if (firstDate.get(Calendar.MINUTE) > SecondDate.get(Calendar.MINUTE))
										{
											return true ;
										}
										else
										{
											if (firstDate.get(Calendar.MINUTE) == SecondDate.get(Calendar.MINUTE))
											{
												if (firstDate.get(Calendar.SECOND) > SecondDate.get(Calendar.SECOND))
												{
													return true ;
												}
											}

										}
									}
								}
							}

						}
					}

				}


			}
		}


		return false;
	}









	public static boolean isEqualDate(Calendar firstDate , Calendar SecondDate)
	{
		if (firstDate.get(Calendar.YEAR) == SecondDate.get(Calendar.YEAR))
		{
			if (firstDate.get(Calendar.MONTH) == SecondDate.get(Calendar.MONTH))
			{
				if (firstDate.get(Calendar.DATE) == SecondDate.get(Calendar.DATE))
				{
					return true ;
				}
			}
		}




		return false;
	}




	public static boolean isEqualDateAndTime(Calendar firstDate , Calendar SecondDate)
	{
		if (firstDate.get(Calendar.YEAR) == SecondDate.get(Calendar.YEAR))
		{
			if (firstDate.get(Calendar.MONTH) == SecondDate.get(Calendar.MONTH))
			{
				if (firstDate.get(Calendar.DATE) == SecondDate.get(Calendar.DATE))
				{
					if (firstDate.get(Calendar.HOUR_OF_DAY) == SecondDate.get(Calendar.HOUR_OF_DAY))
					{
						if (firstDate.get(Calendar.MINUTE) == SecondDate.get(Calendar.MINUTE))
						{
							return true ;
						}					}
				}
			}
		}




		return false;
	}



	public static String streamToString(InputStream is) throws IOException {
		String str = "";

		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				reader.close();
			} finally {
				is.close();
			}

			str = sb.toString();
		}

		return str;
	}


    public void hideKeyBoard(EditText editText , Activity activity)
	{
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

	}













	}


