package com.clubscaddy;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.CountryCodeAdapter;
import com.clubscaddy.Bean.CountryDatabean;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class Signupactivity extends AppCompatActivity
{
	public static ActionBar mActionBar;
	Activity mContext;
	EditText first_name_edit_text;
	EditText last_name_edit_text;
	EditText emailid_edit_text;
	EditText club_edit_text;
	EditText club_website_edit_text;
	Spinner country_code_spiner;
	EditText contact_number_edit_txt;
	EditText message_edit_txt;
	ImageButton ok_btn;
	AQuery aqury ;
	String phonecode;
	ArrayList<String>sport_type_item_list;
	String country_name ;
	Spinner soprt_type_spinner;
	ArrayList<CountryDatabean>country_list = new ArrayList<CountryDatabean>();
	int selected_item = 1;
	ProgressDialog progress ;
	Handler handler ;
	HttpRequest httpRequest ;
	boolean isEmailEdithasfocus = false ;
	boolean isEmailExits ;
EditText sports_type_edit_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up_xml);
		mContext = Signupactivity.this;

		httpRequest = new HttpRequest(Signupactivity.this);
		
		first_name_edit_text = (EditText) findViewById(R.id.first_name_edit_text);
		last_name_edit_text = (EditText) findViewById(R.id.last_name_edit_text);
		emailid_edit_text = (EditText) findViewById(R.id.emailid_edit_text);

		sports_type_edit_text = (EditText) findViewById(R.id.sports_type_edit_text);

		handler = new Handler(getMainLooper()) ;


		emailid_edit_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v,final boolean hasFocus) {




				if(isEmailEdithasfocus)
				{

					handler.post(new Runnable() {
						@Override
						public void run() {

							if (Validation.isEmailAddress(emailid_edit_text, true))
							{

								sendRequestForEmailValidation();
							}

									}
					});


				}

				isEmailEdithasfocus = hasFocus ;


			}
		});

		
		club_edit_text = (EditText) findViewById(R.id.club_edit_text);
		sport_type_item_list = new ArrayList<String>();
		sport_type_item_list.add("Tennis");
		sport_type_item_list.add("Badminton");
		sport_type_item_list.add("Squash");
		sport_type_item_list.add("Racquetball");
		
		
		club_website_edit_text = (EditText) findViewById(R.id.club_website_edit_text);
		
		country_code_spiner = (Spinner) findViewById(R.id.country_code_spiner);
		
		soprt_type_spinner = (Spinner)findViewById(R.id.soprt_type_spinner);
		
		contact_number_edit_txt = (EditText) findViewById(R.id.contact_number_edit_txt);
		
		ok_btn = (ImageButton) findViewById(R.id.ok_btn);
		
		
		message_edit_txt = (EditText) findViewById(R.id.message_edit_txt);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(Signupactivity.this, R.layout.manage_sport_type_item_layout, sport_type_item_list);
		
		soprt_type_spinner.setAdapter(adapter);

		
		soprt_type_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				// TODO Auto-generated method stub
				selected_item = pos+1;
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		aqury = new AQuery(mContext);
		getCountryCode();
		
		
		
		
		
		ok_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub


				Utill.hideKeybord(Signupactivity.this);
				
				
			if(Validation.isNetworkAvailable(getApplicationContext()))
			{
				if(emailid_edit_text.getText().toString().equals(""))
				{
					showDialgonActivity("Please enter email address", mContext ,getResources().getString(R.string.app_name));
					return ;
				}
				if (!Validation.isEmailAddress(emailid_edit_text, true)) {

					showDialgonActivity(getResources().getString(R.string.correct_email), mContext ,getResources().getString(R.string.app_name));
					return ;
				}
				if (isEmailExits == true) {

					showDialgonActivity("Email already exists.", mContext ,getResources().getString(R.string.app_name));
					return ;
				}

			if(first_name_edit_text.getText().toString().trim().equals(""))
			{
				showDialgonActivity("Please enter first name", mContext ,getResources().getString(R.string.app_name));
			return ;
			}
			if(last_name_edit_text.getText().toString().trim().equals(""))
			{
				showDialgonActivity("Please enter last name", mContext ,getResources().getString(R.string.app_name));
			return ;
			}

			if(club_edit_text.getText().toString().equals(""))	
			{
				showDialgonActivity("Please enter club name", mContext,getResources().getString(R.string.app_name));
				return ;
			}
			//if(Patterns.WEB_URL.matcher(club_website_edit_text.getText().toString()).matches())
			if(!Patterns.WEB_URL.matcher(club_website_edit_text.getText().toString().trim()).matches())
			{
				showDialgonActivity("Please enter valid club website", mContext,getResources().getString(R.string.app_name));
				return ;
			}


				if(sports_type_edit_text.getText().toString().equals(""))
				{
					showDialgonActivity("Please enter sport name", mContext,getResources().getString(R.string.app_name));
					return ;
				}


			if(contact_number_edit_txt.getText().toString().equals(""))	
			{
				showDialgonActivity("Please enter contact number", mContext,getResources().getString(R.string.app_name));
				return ;
			}
			if(contact_number_edit_txt.getText().toString().length() <6 ||contact_number_edit_txt.getText().toString().length() >14)
			{
				showDialgonActivity("Please enter valid  contact number", mContext,getResources().getString(R.string.app_name));
				return ;
			}

			
			
			
			HashMap<String, String>params = new HashMap<String, String>();
			
			
			params.put("user_type", "4");
			params.put("user_first_name", first_name_edit_text.getText().toString().trim());
			params.put("user_last_name", last_name_edit_text.getText().toString().trim());
			params.put("user_email", emailid_edit_text.getText().toString().trim());
			params.put("clubname", club_edit_text.getText().toString().trim());
			params.put("clubweb", club_website_edit_text.getText().toString().trim());
			params.put("user_phone", contact_number_edit_txt.getText().toString().trim());
			params.put("message", message_edit_txt.getText().toString());
			params.put("user_device_type", "android");
			params.put("user_device_token", SessionManager.getUser_Device_Token(getApplicationContext()));
			
			params.put("sport_name", sports_type_edit_text.getText().toString().trim()+"");

				params.put("sport_type", selected_item+"");
			params.put("country", country_name);
			params.put("phonecode", phonecode);
			
		
			progress = new ProgressDialog(Signupactivity.this);
			progress.setMessage("Please Wait..");
			progress.setCancelable(false);
			progress.show();
			
			aqury.ajax(WebService.signupfromweb, params, JSONObject.class, new AjaxCallback<JSONObject>()
					{
				public void callback(String url, JSONObject object, com.androidquery.callback.AjaxStatus status) 
				{
					//{"message":"Email already exist","status":"false"}
					progress.dismiss();
					
					try
					{
					if(object != null)	
					{
						if(object.getString("status").equals("true"))	
					{
							showDialg(object.getString("message")+"", mContext );
							
					}
					else
					{
						showDialgonActivity(object.getString("message")+"", mContext ,getResources().getString(R.string.app_name));
							
					}
					}
					else
					{
						showDialgonActivity("Network earror!", mContext ,getResources().getString(R.string.app_name));
					}
					}
					catch(Exception e)
					{
						
					}
					
				
					
				};
					});
			
			
			}
			else
			{
			Utill.showDialg(getString(R.string.check_internet_connection), mContext);	
			}
				
			}
		});
		
		mActionBar = getSupportActionBar();
		setActionBar();
	}
	public  ImageButton  backButton, uploadNewsOrEditProfile;
	public 	ImageButton logoutButton ;
	public 	TextView delete_all_btn;
	public  TextView actionbar_titletext;
	public  void setActionBar() {
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayUseLogoEnabled(false);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setCustomView(((Activity) mContext).getLayoutInflater().inflate(R.layout.director_actionbar_header_xml, null),
				new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));

		backButton = (ImageButton) mActionBar.getCustomView().findViewById(R.id.actionbar_backbutton);
		logoutButton = (ImageButton) mActionBar.getCustomView().findViewById(R.id.actionbar_logoutbutton);
		uploadNewsOrEditProfile = (ImageButton) mActionBar.getCustomView().findViewById(R.id.upload_news);
		actionbar_titletext = (TextView) mActionBar.getCustomView().findViewById(R.id.actionbar_titletext);
		actionbar_titletext.setText("Try it");
		// addToCalender();
		delete_all_btn =(TextView) mActionBar.getCustomView().findViewById(R.id.delete_all_btn);
		logoutButton.setVisibility(View.GONE);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				Intent intent = new Intent(Signupactivity.this , LoginActivity.class);
				startActivity(intent);
			}
		});
		Toolbar parent =(Toolbar) mActionBar.getCustomView().getParent();//first get parent toolbar of current action bar
		parent.setContentInsetsAbsolute(0,0);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent(Signupactivity.this , LoginActivity.class);
		startActivity(intent);
	}
	@SuppressWarnings("deprecation")
	public  void showDialg(String msg , Context mContext)
	{
		
	final	AlertDialog alertDialog = new AlertDialog.Builder(
                mContext).create();

// Setting Dialog Title
alertDialog.setTitle("Confirm");

// Setting Dialog Message
alertDialog.setMessage(msg);

// Setting Icon to Dialog


// Setting OK Button
alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
        // Write your code here to execute after dialog closed
        	 finish();
        	 Intent intent = new Intent(Signupactivity.this , LoginActivity.class);
				startActivity(intent);
        	alertDialog.dismiss();
        }
});

// Showing Alert Message
alertDialog.show();
	}
	
	
	
	
	
	@SuppressWarnings("deprecation")
	public  void showDialgonActivity(String msg , Activity mContex ,String title)
	{
		
	final	AlertDialog alertDialog = new AlertDialog.Builder(
                mContext ).create();

// Setting Dialog Title
alertDialog.setTitle(title);

// Setting Dialog Message
alertDialog.setMessage(msg);

// Setting Icon to Dialog
        alertDialog.setCancelable(false);

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
	
	
	
	public void getCountryCode()
	{
		
		
		
		CountryDatabean bean = new CountryDatabean();
		bean.setCountry_code_text("Select Country");
		bean.setPhonecode_country("");
		bean.setPhonecode_val("");
		country_list.add(bean);
		 country_code_spiner.setAdapter(new CountryCodeAdapter(country_list, Signupactivity.this , country_code_spiner));

		
		
		if(Validation.isNetworkAvailable(getApplicationContext()))
		{
			
			HashMap<String, String>	params = new HashMap<String, String>();
			Utill.showProgress(Signupactivity.this);
		aqury.ajax(WebService.phonecode, params, JSONObject.class, new AjaxCallback<JSONObject>()
				{
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				// TODO Auto-generated method stub
				super.callback(url, object, status);
				Utill.hideProgress();
				
				
				if(object != null)
				{
					country_list.clear();
					try
					{
				 JSONArray country_array = object.getJSONArray("response");
				 
				 for(int i = 0 ;i < country_array.length() ;i ++)
				 {
					CountryDatabean bean = new CountryDatabean(); 
					
					
					bean.setPhonecode_val(country_array.getJSONObject(i).getString("phonecode_val"));
					bean.setPhonecode_country(country_array.getJSONObject(i).getString("phonecode_country"));
					
					
					bean.setCountry_code_text(country_array.getJSONObject(i).getString("phonecode_country")+"(+"+country_array.getJSONObject(i).getString("phonecode_val")+")");
					country_list.add(bean);
				 
				 }
				 
				 
				 country_name = country_list.get(0).getPhonecode_country();
				 phonecode = country_list.get(0).getPhonecode_val();
				 
				 
				 
				 
				 
				 country_code_spiner.setAdapter(new CountryCodeAdapter(country_list, Signupactivity.this ,country_code_spiner));
						
				 
				 
				 
				 
				 country_code_spiner.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
							// TODO Auto-generated method stub
							 country_name = country_list.get(pos).getPhonecode_country();
							 phonecode = country_list.get(pos).getPhonecode_val();
							
							//Toast.makeText(getApplicationContext(), "ss "+pos, 1).show();;
							
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
							
							}
					});
					}
					catch(Exception e)
					{
						
					}
				}
				else
				{
					
					 country_code_spiner.setAdapter(new CountryCodeAdapter(country_list, Signupactivity.this ,country_code_spiner));
				
					Utill.showDialg("Network error!", mContext);	
						
				}
				
				
			}
				});	
		}
		else
		{
			
			 country_code_spiner.setAdapter(new CountryCodeAdapter(country_list, Signupactivity.this ,country_code_spiner));
				
		Utill.showDialg(getString(R.string.check_internet_connection), mContext);	
		}
	
		
		
	}


	public void sendRequestForEmailValidation()
	{
		HashMap<String ,Object>param = new HashMap<String ,Object>();
		param.put("user_email" , emailid_edit_text.getText().toString().trim());
		//Toast.makeText(getApplicationContext() , "hasFocus "+isEmailEdithasfocus ,1).show();
		httpRequest.getResponseWihhoutPd(Signupactivity.this, WebService.emailid_check, param, new OnServerRespondingListener(Signupactivity.this) {
			public void onSuccess(JSONObject jsonObject)
			{
				//
				try {
					isEmailExits = !jsonObject.getBoolean("status");
					if (jsonObject.getBoolean("status") == false)
					{
						showDialgonActivity(jsonObject.getString("message"), mContext ,getResources().getString(R.string.app_name));

					}

				}
				catch (Exception e)
				{

				}


			}
		});
	}

	
}
