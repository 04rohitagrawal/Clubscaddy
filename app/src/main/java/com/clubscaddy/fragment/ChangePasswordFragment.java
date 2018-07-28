package com.clubscaddy.fragment;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.R;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.custumview.KeyboadSetting;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;

public class ChangePasswordFragment extends Fragment {

	String Tag = getClass().getName();
	public static FragmentManager mFragmentManager;
	public static Fragment mFragment;
	Context mContext;

	EditText oldPasswordET, newPasswordET, confirmPasswordET;
	Button Done;
    HttpRequest httpRequest ;
	
	public static Fragment getInstacne(FragmentManager mFragManager) {
		if (mFragment == null) {
			mFragment = new ChangePasswordFragment();
		}
		return mFragment;
	}

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
		View rootView = inflater.inflate(R.layout.change_password, container, false);
		mContext = getActivity();
		httpRequest = new HttpRequest(getActivity());
		if (DirectorFragmentManageActivity.actionbar_titletext != null) {
			DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.change_pw));
		}
		initializeView(rootView);
		setOnClickListener();
		DirectorFragmentManageActivity.showLogoutButton();
		return rootView;

	}

	void initializeView(View view) {
		oldPasswordET = (EditText) view.findViewById(R.id.OLD_password);
		newPasswordET = (EditText) view.findViewById(R.id.new_password);
		confirmPasswordET = (EditText) view.findViewById(R.id.confirm_password);
		Done = (Button) view.findViewById(R.id.done);
	}

	void setOnClickListener() {
		Done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				validateData();
			}
		});
	}

	void validateData() {
		try
		{
			AppConstants.hideSoftKeyboard(getActivity());
		}
		catch(Exception e)
		{
			
		}
		KeyboadSetting.hideKeyboard(mContext);
		String oldPasswordStr = oldPasswordET.getText().toString().trim();
		final String newPasswordStr = newPasswordET.getText().toString().trim();
		String ConfirmPasswordStr = confirmPasswordET.getText().toString().trim();
		if (Utill.isStringNullOrBlank(oldPasswordStr)) {
			ShowUserMessage.showUserMessage(mContext,"Enter Old Password.");
		} else if (Utill.isStringNullOrBlank(newPasswordStr)) {
			ShowUserMessage.showUserMessage(mContext, mContext.getResources().getString(R.string.enter_new_password));
		} else if (Utill.isStringNullOrBlank(ConfirmPasswordStr)) {
			ShowUserMessage.showUserMessage(mContext, mContext.getResources().getString(R.string.enter_confirm_password));
		} else if (!(newPasswordStr.equals(ConfirmPasswordStr))) {
			ShowUserMessage.showUserMessage(mContext, mContext.getResources().getString(R.string.password_doesnt_match));
		} else
		{

			HashMap<String , Object> param = new HashMap<>();
			param.put("user_id", SessionManager.getUser_id(mContext));
			param.put("old_password", oldPasswordStr);
			param.put("new_password", newPasswordStr);
			param.put("user_club_id", SessionManager.getUser_Club_id(mContext));
			httpRequest.getResponse(getActivity(), WebService.changepassword, param, new OnServerRespondingListener(getActivity()) {
				@Override
				public void onSuccess(JSONObject jsonObject)
				{
					try
					{
						if (jsonObject.getBoolean("status"))
						{
							oldPasswordET.setText("");
							newPasswordET.setText("");

							confirmPasswordET.setText("");

							SessionManager.setPassword(getActivity() , newPasswordStr);

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

	public class ChangePasswordListener {
		public void onSuccess(String msg) {
			ClearFields();
			ShowUserMessage.showUserMessage(mContext, msg);
			DirectorFragmentManageActivity.popBackStackFragment();
		}

		public void onError(String msg) {
			ShowUserMessage.showUserMessage(mContext, msg);
		}
	}

	void ClearFields() {
		SessionManager.setPassword(getActivity(), newPasswordET.getText().toString());
		oldPasswordET.setText("");
		newPasswordET.setText("");
		confirmPasswordET.setText("");
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
try
{
	AppConstants.hideSoftKeyboard(getActivity());
}
catch(Exception e)
{
	
}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.e(Tag, "onDetach");
	}

}
