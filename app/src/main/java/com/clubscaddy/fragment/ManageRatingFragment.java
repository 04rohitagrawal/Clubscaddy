package com.clubscaddy.fragment;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.Server.WebService;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class ManageRatingFragment extends Fragment 
{
View manage_rating_type_layout;
RadioButton yes_btn ,no_btn;
AQuery aQuery;
int selected =1 ;
Button sport_type_add_type;
ProgressDialog pd ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		manage_rating_type_layout = inflater.inflate(R.layout.manage_rating_xml, null);
		aQuery = new AQuery(getActivity());
		sport_type_add_type =(Button) manage_rating_type_layout.findViewById(R.id.sport_type_add_type);
		
		if (DirectorFragmentManageActivity.actionbar_titletext != null) {
			DirectorFragmentManageActivity.updateTitle("Manage Rating");
		}
		yes_btn = (RadioButton) manage_rating_type_layout.findViewById(R.id.yes_btn);
		no_btn = (RadioButton) manage_rating_type_layout.findViewById(R.id.no_btn);
		
		yes_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selected =1 ;
			}
		});
           no_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selected =2 ;	
			}
		});
		sport_type_add_type.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pd = new ProgressDialog(getActivity());
				pd.setCancelable(false);
				pd.show();
				HashMap<String, String>map = new HashMap<String, String>();
				map.put("club_id", SessionManager.getUser_Club_id(getActivity()));
				map.put("club_ratting_show", selected+"");
				aQuery.ajax(WebService.clubratting, map, JSONObject.class, new AjaxCallback<JSONObject>()
						{
					public void callback(String url, JSONObject object, AjaxStatus status) {
						
						pd.dismiss();
						
						try {
							if(object.getString("status").equalsIgnoreCase("true"))
							{
								Toast.makeText(getActivity(), object.getString("message"), 1).show();	
						getActivity().getSupportFragmentManager().popBackStack();
						//	SessionManager.setSportType(getActivity(), selected_item+"");
							}
							else
							{
								Toast.makeText(getActivity(), object.getString("message"), 1).show();	
									
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						;
						
					};
						}
						);	
			}
		});
		
		return manage_rating_type_layout;
	}

}
