package com.clubscaddy.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;

import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.DialogBoxButtonListner;
import com.clubscaddy.R;

public class ShowUserMessage {

	public static void showUserMessage(Activity mContext,String message){
		/*Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();*/
		Utill.showDialg(message, mContext);
	}
	Activity activity ;
	public  ShowUserMessage(Activity activity)
	{
		this.activity = activity;
	}
	public  ShowUserMessage()
	{
		this.activity = activity;
	}

	public static void showUserMessage(Context mContext,String message){
		/*Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();*/
		Utill.showDialg(message, mContext);
	}



	public static  void showDialogOnActivity(Activity AppCompatActivity ,String msg)
	{

		AlertDialog alertDialog = new AlertDialog.Builder(
				AppCompatActivity).create();

		// Setting Dialog Title
		alertDialog.setTitle(AppCompatActivity.getResources().getString(R.string.app_name));

		// Setting Dialog Message
		alertDialog.setMessage(msg);

		// Setting Icon to Dialog


		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();

	}






	public   void showDialogOnFragment(Activity AppCompatActivity ,String msg)
	{

		AlertDialog alertDialog = new AlertDialog.Builder(
				AppCompatActivity).create();

		// Setting Dialog Title
		alertDialog.setTitle(AppCompatActivity.getResources().getString(R.string.app_name));

		// Setting Dialog Message
		alertDialog.setMessage(msg);

		// Setting Icon to Dialog


		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();

	}




	public   void showDialogOnFragmentWithBack(final FragmentActivity AppCompatActivity , String msg)
	{

 		AlertDialog alertDialog = new AlertDialog.Builder(
				AppCompatActivity).create();

		// Setting Dialog Title
		alertDialog.setTitle(AppCompatActivity.getResources().getString(R.string.app_name));
		alertDialog.setCancelable(false);
		// Setting Dialog Message
		alertDialog.setMessage(msg);

		// Setting Icon to Dialog


		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				AppCompatActivity.getSupportFragmentManager().popBackStack();
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();

	}





	public static  void showDialogForImageDia(Activity AppCompatActivity )
	{

		AlertDialog alertDialog = new AlertDialog.Builder(
				AppCompatActivity).create();

		// Setting Dialog Title
		alertDialog.setTitle(AppCompatActivity.getResources().getString(R.string.app_name));

		// Setting Dialog Message
		alertDialog.setMessage("Height and width of image should be greater than 512");

		// Setting Icon to Dialog


		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();

	}


	public static void showMessageForFragmeneWithBack(final FragmentActivity fragmentActivity ,String msg)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(
				fragmentActivity).create();


		// Setting Dialog Title
		alertDialog.setTitle(SessionManager.getClubName(fragmentActivity));
		alertDialog.setCancelable(false);
		// Setting Dialog Message
		alertDialog.setMessage(msg);


		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				fragmentActivity.getSupportFragmentManager().popBackStack();
				dialog.cancel();
			}
		});


		alertDialog.show();
	}




	public  void showDialogBoxWithYesNoButton(String msg , final DialogBoxButtonListner dialogBoxbuttonListener)
	{




		AlertDialog.Builder alertdialobBuilder = new AlertDialog.Builder(activity);
		alertdialobBuilder.setMessage(msg);

		alertdialobBuilder.setTitle(SessionManager.getClubName(activity));
		alertdialobBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialogBoxbuttonListener.onNoButtonClick(dialog);

			}
		});


		alertdialobBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialogBoxbuttonListener.onYesButtonClick(dialog);
			}
		});

		alertdialobBuilder.show();
	}






	public  void showVersionControlDialog(final Activity fragmentActivity )
	{
		AlertDialog alertDialog = new AlertDialog.Builder(
				fragmentActivity).create();


		// Setting Dialog Title
		alertDialog.setTitle(fragmentActivity.getResources().getString(R.string.app_name));
		alertDialog.setCancelable(false);
		// Setting Dialog Message
		alertDialog.setMessage("Clubs Caddy has some new exciting features. To make use of them you need to upgrade your app. Please download the latest version of \"Clubs Caddy\" app from Google Play Store.");




		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				dialog.cancel();
				fragmentActivity.finish();



				final String appPackageName = fragmentActivity.getPackageName(); // getPackageName() from Context or Activity object
				try {
					fragmentActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
				} catch (android.content.ActivityNotFoundException anfe) {
					fragmentActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
				}


			}
		});


		alertDialog.show();
	}



	public  void showDialogBoxWithYesNoButtonForVersionControl(final Activity activity ,final DialogBoxButtonListner dialogBoxButtonListner)
	{




		AlertDialog.Builder alertdialobBuilder = new AlertDialog.Builder(activity);
		alertdialobBuilder.setMessage("Clubs Caddy has some new exciting features. To make use of them you need to upgrade your app. Please download the latest version of \"Clubs Caddy\" app from Google Play Store.");

		alertdialobBuilder.setTitle(SessionManager.getClubName(activity));
		alertdialobBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {


				dialog.cancel();
				activity.finish();



				final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
				try {
					activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
				} catch (android.content.ActivityNotFoundException anfe) {
					activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
				}



			}
		});


		alertdialobBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				dialogBoxButtonListner.onNoButtonClick(dialog);

			}
		});

		alertdialobBuilder.show();
	}





}
