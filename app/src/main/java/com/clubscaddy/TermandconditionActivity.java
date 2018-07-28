package com.clubscaddy;

import java.util.HashMap;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.clubscaddy.R;
import com.clubscaddy.Server.WebService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

public class TermandconditionActivity extends Activity
{
	ImageButton cancel_btn;
	WebView webview; 
	ProgressDialog pd;
	AQuery aquery;
	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.termand_condition_layout);
		super.onCreate(savedInstanceState);
		cancel_btn = (ImageButton) findViewById(R.id.cancel_btn);
		
		
		aquery = new AQuery(getApplicationContext());
		
		cancel_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				Intent intent = new Intent(TermandconditionActivity.this , LoginActivity.class);
				startActivity(intent);
			}
		});
		
		webview = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = webview.getSettings();

		 webview.addJavascriptInterface(new WebAppInterface(getApplicationContext()), "Android");
		 webSettings.setJavaScriptEnabled(true);
		 webview.requestFocusFromTouch();
		
		 webview.setWebViewClient(new WebViewClient() {
	            
	            @Override
	            public void onPageStarted(WebView view, String url, Bitmap favicon) {
	                
	                super.onPageStarted(view, url, favicon);
	               
	                pd = new ProgressDialog(TermandconditionActivity.this);
	                pd.setCancelable(false);
	                pd.setMessage("Page Loading....");
	                pd.show();
	            }

	            @Override
	            public void onPageFinished(WebView view, String url) {
	                
	                super.onPageFinished(view, url);
	                pd.dismiss();
	            }
	        });
		
		  pd = new ProgressDialog(TermandconditionActivity.this);
          pd.setCancelable(false);
          pd.setMessage("Page Loading....");
          pd.show();
          HashMap<String,String> map=    new HashMap<String,String>() ;

	 aquery.ajax(WebService.termsandpolicyurls, map, JSONObject.class, new AjaxCallback<JSONObject>()
				 {
			 @Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				// TODO Auto-generated method stub
				super.callback(url, object, status);
			
			 try
			 
			 {
				 pd.dismiss();
					if(object.has("status"))	
					{
					
						if(object.getString("status").equals("true"))
						{
						if(getIntent().getStringExtra("status").equals("1"))
						{
							 webview.loadUrl(object.getString("terms"));
						}
							
						if(getIntent().getStringExtra("status").equals("2"))
						{
							webview.loadUrl(object.getString("policy"));	
						}	
						}
						
					}
			 }
			 catch(Exception e)
			 {
				 
			 }
			 
			
				
			 }
				 });
	}
	public class WebAppInterface {
	    Context mContext;

	    /** Instantiate the interface and set the context */
	    WebAppInterface(Context c) {
	        mContext = c;
	    }
}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent(TermandconditionActivity.this , LoginActivity.class);
		startActivity(intent);
	}
}
