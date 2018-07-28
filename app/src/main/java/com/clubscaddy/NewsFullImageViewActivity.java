package com.clubscaddy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clubscaddy.custumview.CustomScrollView;
import com.clubscaddy.R;

public class NewsFullImageViewActivity  extends AppCompatActivity
{

//CustomScrollView event_add;
EditText quoteTextArea;
	Button adminList_addAdmin;
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		//event_add = (CustomScrollView) findViewById(R.id.event_add);
		quoteTextArea = (EditText) findViewById(R.id.quoteTextArea);


		adminList_addAdmin = (Button) findViewById(R.id.adminList_addAdmin);
		adminList_addAdmin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String data = quoteTextArea.getText().toString();
				Log.e("data" , data);
			}
		});






	//	focusOnView();

	}
	private final void focusOnView(){

	}
}
