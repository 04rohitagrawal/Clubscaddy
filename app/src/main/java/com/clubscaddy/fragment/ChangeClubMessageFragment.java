package com.clubscaddy.fragment;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.R;
import com.clubscaddy.custumview.KeyboadSetting;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;

public class ChangeClubMessageFragment extends Fragment {

	String Tag = getClass().getName();
	public static FragmentManager mFragmentManager;
	public static Fragment mFragment;
	Context mContext;
    HttpRequest httpRequest ;
	EditText messageClub;
	Button Done;
	TextView club_msg_status_tv;



	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.e(Tag, "onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(Tag, "onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e(Tag, "onCreateView");
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.change_club_message, container, false);
		mContext = getActivity();
		httpRequest = new HttpRequest(getActivity());
		club_msg_status_tv = (TextView) rootView.findViewById(R.id.club_msg_status_tv);

		if (DirectorFragmentManageActivity.actionbar_titletext != null) {
			DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.change_club_message_h));
		}
		initializeView(rootView);
		setOnClickListener();
		
	
		messageClub.setText(""+SessionManager.getClubMessage(mContext));
		DirectorFragmentManageActivity.showLogoutButton();

		return rootView;

	}

	void initializeView(View view) {
		messageClub = (EditText) view.findViewById(R.id.club_message);
		
		Done = (Button) view.findViewById(R.id.done);



		messageClub.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				club_msg_status_tv.setText(s.toString().length()+"/180");

			}
		});

	}

	void setOnClickListener() {
		Done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				validateData();
			}
		});
	}

	void validateData()
	{
		KeyboadSetting.hideKeyboard(mContext);
		Utill.hideKeybord(getActivity() , messageClub);
		String oldPasswordStr = messageClub.getText().toString().trim();
		if (Utill.isStringNullOrBlank(oldPasswordStr))
		{
			ShowUserMessage.showUserMessage(mContext, "Enter Club Message");
		}
		else
		{
			HashMap<String , Object> param = new HashMap<>();
			param.put("user_club_id", SessionManager.getUser_Club_id(mContext));
			param.put("club_status_message", oldPasswordStr);

			param.put("user_id", SessionManager.getUser_id(getActivity()));

			httpRequest.getResponse(getActivity(), WebService.changeClubMessage, param, new OnServerRespondingListener(getActivity()) {
				@Override
				public void onSuccess(JSONObject jsonObject)
				{
				try
				{
					if (jsonObject.getBoolean("status"))
					{
						ShowUserMessage.showMessageForFragmeneWithBack(getActivity() ,jsonObject.getString("msg") );
					}
					else
					{
						Utill.showDialg(jsonObject.getString("msg") , getActivity());

					}

				}
				catch (Exception e)
				{

				}

				}
			});


		}

	}


	
	




	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.e(Tag, "onActivityCreated");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.e(Tag, "onStart");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(Tag, "onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.e(Tag, "onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.e(Tag, "onStop");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e(Tag, "onDestroy");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.e(Tag, "onDestroyView");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.e(Tag, "onDetach");
	}

}
