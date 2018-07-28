package com.clubscaddy.fragment;


import java.util.HashMap;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.custumview.InternetConnection;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.Server.WebService;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ContactSupporortFRagment extends Fragment
{
	AQuery aqQuery;
	EditText enter_name_edit_tv;	
	EditText discription_edit_text ;
	Button send_btn;
	TextView link_text_tv;
	TextView phone_no_text_tv;
	ImageButton face_book_image_btn ;
	ImageButton twitter_image_btn;
	ImageButton linkin_image_btn ;
	AQuery aQuery;
	String phoneno="";
	String twitter_url ="";
	String phone_url ="";
	String linkedin_url="" ;
	String web_url ="";
	String facebook_url = "";

	View contact_view ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		contact_view = inflater.inflate(R.layout.contact_view_layout, null);

		aqQuery = new AQuery(getActivity());

		DirectorFragmentManageActivity.updateTitle("Contact Support");
		enter_name_edit_tv = (EditText) contact_view.findViewById(R.id.enter_name_edit_tv);

		discription_edit_text = (EditText) contact_view.findViewById(R.id.discription_edit_text);

		send_btn = (Button) contact_view.findViewById(R.id.send_btn);
		send_btn.setOnClickListener(clicklisener);

		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


		face_book_image_btn = (ImageButton) contact_view.findViewById(R.id.face_book_image_btn);
		face_book_image_btn.setOnClickListener(clicklisener);


		phone_no_text_tv = (TextView) contact_view.findViewById(R.id.phone_no_text_tv);
		phone_no_text_tv.setOnClickListener(clicklisener);

		twitter_image_btn = (ImageButton) contact_view.findViewById(R.id.twitter_image_btn);
		twitter_image_btn.setOnClickListener(clicklisener);


		link_text_tv =  (TextView) contact_view.findViewById(R.id.link_text_tv);
		link_text_tv.setOnClickListener(clicklisener);

		linkin_image_btn =  (ImageButton) contact_view.findViewById(R.id.linkin_image_btn);
		linkin_image_btn.setOnClickListener(clicklisener);




		//linkin_image_btn

		


		
		phone_no_text_tv.setOnClickListener(clicklisener);
		//	enter_name_edit_tv.setText(text);



		if(Utill.isNetworkAvailable(getActivity()))
		{
			Utill.showProgress(getActivity());
			HashMap<String, String>params = new HashMap<String, String>();
			aqQuery.ajax(WebService.urls, params, JSONObject.class, new AjaxCallback<JSONObject>()
			{
				@Override
				public void callback(String url, JSONObject object, AjaxStatus status) {
					// TODO Auto-generated method stub
					super.callback(url, object, status);
					Utill.hideProgress();

					Log.e("object", object+"");


					try
					{

						if(object.getString("status").equals("true"))
						{

							twitter_url = 	object.getString("twitter");
							phoneno  = 	object.getString("phone");
							linkedin_url = 	object.getString("linkedin");
							web_url = 	object.getString("web_url");
							twitter_url = 	object.getString("twitter");
							facebook_url = object.getString("facebook");
							
							SpannableString	content = new SpannableString(phoneno);
							content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
							phone_no_text_tv.setText(content);
							
							SpannableString content1 = new SpannableString(web_url);
							//content.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
							link_text_tv.setText(web_url);
							Linkify.addLinks(link_text_tv, Linkify.WEB_URLS);

							
							if(Validation.isStringBlack(phone_no_text_tv.getText().toString()))
							{
								phone_no_text_tv.setVisibility(View.INVISIBLE);	
							}
							if(Validation.isStringBlack(link_text_tv.getText().toString()))
							{
								link_text_tv.setVisibility(View.INVISIBLE);	
							}
							
							if(Validation.isStringBlack(facebook_url))
							{
								face_book_image_btn.setVisibility(View.INVISIBLE);	
							}
							
							if(Validation.isStringBlack(twitter_url))
							{
								twitter_image_btn.setVisibility(View.INVISIBLE);	
							}
							if(Validation.isStringBlack(linkedin_url))
							{
								linkin_image_btn.setVisibility(View.INVISIBLE);	
							}
							
							
							
							
							
							
							
						}
						else
						{
							ShowUserMessage.showDialogOnActivity(getActivity() , object.getString("message"));
						}


					}
					catch(Exception e)
					{
						Log.e("excepton",e.getMessage());
                    Toast.makeText(getActivity(), e.getMessage(), 1).show();
					}




				}
			});

		}
		else
		{
			Utill.showDialg(getString(R.string.check_internet_connection), getActivity());	
		}


		return contact_view;
	}

	public OnClickListener clicklisener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			switch (v.getId()) {
			case R.id.send_btn:

				try
				{
					AppConstants.hideSoftKeyboard(getActivity());
				}
				catch(Exception e)
				{

				}

				if(enter_name_edit_tv.getText().toString().equals("") )
				{
					Utill.showDialg("Please enter subject", getActivity());
					return;
				}
				if(discription_edit_text.getText().toString().equals(""))
				{
					Utill.showDialg("Please enter discription", getActivity());
					return;
				}
				if(InternetConnection.isInternetOn(getActivity()))
				{
					setToServerContect();	
				}
				else
				{
					Utill.showDialg(getString(R.string.check_internet_connection), getActivity());	
				}

				break;

			case R.id.face_book_image_btn:


				SwichWebpageByIntent(facebook_url);

				break;
			case R.id.twitter_image_btn:


				SwichWebpageByIntent(twitter_url);

				break;
			case R.id.link_text_tv:
				//Toast.makeText(getActivity(), "sss", 1).show();
				SwichWebpageByIntent(linkedin_url);

				break;
			case R.id.phone_no_text_tv:
				try
				{
					String uri = "tel:" + phone_no_text_tv.getText().toString().trim() ;
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse(uri));
					startActivity(intent);
				}
				catch (Exception e) {
					// TODO: handle exception
				}
				break;

			case R.id.linkin_image_btn:
				SwichWebpageByIntent(linkedin_url);
				break;
			}

		}
	};


	public void setToServerContect()
	{
		HashMap<String, String>params = new HashMap<String, String>();

		params.put("sender_id", SessionManager.getUser_id(getActivity()));
		params.put("message", discription_edit_text.getText().toString());
		params.put("subject", enter_name_edit_tv.getText().toString());

		Utill.showProgress(getActivity());
		aqQuery.ajax(WebService.support, params, JSONObject.class, new AjaxCallback<JSONObject>()
		{
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				// TODO Auto-generated method stub
				super.callback(url, object, status);
				Utill.hideProgress();

				int 	code = status.getCode();

				if (code == AjaxStatus.NETWORK_ERROR) 
				{

					ShowUserMessage.showDialogOnActivity(getActivity() ,"NetWork earror!" );
					Toast.makeText(getActivity(), "NetWork earror!", Toast.LENGTH_LONG).show();
					return;
				} 
				else 
					if(code == AjaxStatus.TRANSFORM_ERROR) 
					{
						//return;
						ShowUserMessage.showDialogOnActivity(getActivity() ,"NetWork earror!" );
					} 
					else 
						if(code == 200)
						{
							try
							{
								//Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_LONG).show();


								Utill.showDialg( object.getString("message"), getActivity());

								//
								if(Boolean.parseBoolean( object.getString("status"))==true)
								{
									getActivity().getSupportFragmentManager().popBackStack();	
								}

							}
							catch(Exception e)
							{

							}
						}



			}
		});	;
	}


	public void SwichWebpageByIntent(String url)
	{

		try
		{

			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);	
		}
		catch(Exception e)
		{
			Utill.showDialg(e.getMessage(), getActivity());	
		}

	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();

		try
		{
			getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

			AppConstants.hideSoftKeyboard(getActivity());
		}
		catch(Exception e)
		{

		}
	}


}
