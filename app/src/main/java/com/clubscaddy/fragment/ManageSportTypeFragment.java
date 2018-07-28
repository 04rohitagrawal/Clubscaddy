package com.clubscaddy.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.SportTypeSpinnerAdapter;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.Server.WebService;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class ManageSportTypeFragment extends Fragment
{
	View manage_sport_type_layout ;
	Spinner soprt_type_spinner ;
	ArrayAdapter<String>spinner_adapter ;
	SportTypeSpinnerAdapter adapter ;
	ArrayList<String>sport_type_item_list;
	ImageButton   open_spinner_btn;
	static Fragment mFragment;
	AQuery aQuery;
	ProgressDialog pd;
	Button sport_type_add_type;
	String sport_type_item_array[];
	int selected_item = 1 ;
	public static Fragment getInstance(FragmentManager mFrgManager) {
		if (mFragment == null) {
			mFragment = new ManageSportTypeFragment();
		}
		return mFragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		manage_sport_type_layout = inflater.inflate(R.layout.manage_sport_type_layout, null);
		
		if (DirectorFragmentManageActivity.actionbar_titletext != null) {
			DirectorFragmentManageActivity.updateTitle("Manage sport Type");
		}
		aQuery = new AQuery(getActivity());
		open_spinner_btn = (ImageButton) manage_sport_type_layout.findViewById(R.id.open_spinner_btn);
		sport_type_item_list = new ArrayList<String>();
		sport_type_item_list.add("Tennis");
		sport_type_item_list.add("Badminton");
		sport_type_item_list.add("Squash");
		sport_type_item_list.add("Racquetball");
		
		soprt_type_spinner = (Spinner) manage_sport_type_layout.findViewById(R.id.soprt_type_spinner);
		sport_type_add_type =(Button) manage_sport_type_layout.findViewById(R.id.sport_type_add_type);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, sport_type_item_list);
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
		
		
		open_spinner_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				soprt_type_spinner.performClick();
			}
		});
		
		soprt_type_spinner.setAdapter(adapter);
		
		try
		{
				
			int index =Integer.parseInt(SessionManager.getSport_type(getActivity()))-1;
			soprt_type_spinner.setSelection(index);
			
			
		}
		catch(Exception e)
		{
			
		}
		
		sport_type_add_type.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pd = new ProgressDialog(getActivity());
				pd.setCancelable(false);
				pd.show();
				HashMap<String, String>map = new HashMap<String, String>();
				map.put("club_id", SessionManager.getUser_Club_id(getActivity()));
				map.put("sport_type", selected_item+"");
				aQuery.ajax(WebService.sporttype, map, JSONObject.class, new AjaxCallback<JSONObject>()
						{
					public void callback(String url, JSONObject object, AjaxStatus status) {
						
						pd.dismiss();
						
						try {
							if(object.getString("status").equalsIgnoreCase("true"))
							{
								Utill.showDialg(object.getString("message"), getActivity());
								//Toast.makeText(getActivity(), object.getString("message"), 1).show();	
						getActivity().getSupportFragmentManager().popBackStack();
							SessionManager.setSportType(getActivity(), selected_item+"");
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
		
		return manage_sport_type_layout;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

}
