package com.clubscaddy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.MemberListListener;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.MemberAutoCompleteAdapter;
import com.clubscaddy.Adapter.MemberListAdapterForManageAdmin;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.custumview.InternetConnection;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.SqlListe;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.Server.WebService;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

public class ManageADminFragment extends Fragment
{

	View manage_admin_fragment ;
	ListView admin_changes_list_view;
	MemberListAdapterForManageAdmin swipeAdapter;
	Activity mContext;
	ArrayList<MemberListBean> membersList;
	ArrayList<MemberListBean> filterList;
	ArrayList<MemberListBean> filter_List;
	ArrayList<MemberListBean> adminList;
	MemberAutoCompleteAdapter auto_complte_adapter ;
	AutoCompleteTextView member_auto_tv;
	AQuery aquery ;
	SqlListe sqlListe;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = getActivity();
		manage_admin_fragment = inflater.inflate(R.layout.manage_admin_alyout, null);
		
		
		DirectorFragmentManageActivity.updateTitle("Manage Admins");
		admin_changes_list_view = 	(ListView) manage_admin_fragment.findViewById(R.id.admin_changes_list_view);

		sqlListe = new SqlListe(getActivity());
		aquery = new AQuery(getActivity());
		member_auto_tv = (AutoCompleteTextView) manage_admin_fragment.findViewById(R.id.auto_complte_adapter);	
		member_auto_tv.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				//Toast.makeText(getActivity(), "KeyCode "+keyCode, 1).show();
				
				if(keyCode == 66)
				{
					InputMethodManager inputManager = 
		        	        (InputMethodManager)getActivity().
		        	            getSystemService(Context.INPUT_METHOD_SERVICE); 
		        	inputManager.hideSoftInputFromWindow(
		        			member_auto_tv.getWindowToken(),
		        	        InputMethodManager.HIDE_NOT_ALWAYS);	
				}
				return false;
			}
		});
		member_auto_tv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				// TODO Auto-generated method stub

				filter_List =	 auto_complte_adapter.getResultList();
				member_auto_tv.setText(filter_List.get(pos).getUser_first_name()+" "+filter_List.get(pos).getUser_last_name());
				member_auto_tv.setSelection(member_auto_tv.getText().length());
				addAdmin(filter_List.get(pos).getUser_id(),filter_List.get(pos));
			}
		});
		
		
		
		
		getMembersList();
		
		//MemberListAdapter
		return manage_admin_fragment;
	}





	private void getMembersList() {
		sqlListe.getMemberList(getActivity(), new MemberListListener() {
			@Override
			public void onSuccess(ArrayList<MemberListBean> memberListBeanArrayList)
			{

				membersList = new ArrayList<MemberListBean>();
				membersList.addAll(memberListBeanArrayList);






				//AppConstants.setMembersList(membersList);

				//	MembersSelectionAdapter adapter = new MembersSelectionAdapter(mContext, membersList);

				filterList = new ArrayList<MemberListBean>();

				adminList = new ArrayList<MemberListBean>();


				for(int i = 0 ;i < membersList.size(); i++ )
				{

					if(membersList.get(i).getUser_type().equals ( AppConstants.USER_TYPE_MOBILE_ADMIN))
					{
						adminList.add(membersList.get(i));
					}

					if(membersList.get(i).getUser_type().equals( AppConstants.USER_TYPE_MEMBER))
					{
						filterList.add(membersList.get(i));
					}


				}
				swipeAdapter = new MemberListAdapterForManageAdmin(adminList,ManageADminFragment. this,true);


				admin_changes_list_view.setAdapter(swipeAdapter);



				auto_complte_adapter = new MemberAutoCompleteAdapter(getActivity(), R.id.textViewItem,filterList ,member_auto_tv);



				member_auto_tv.setAdapter(auto_complte_adapter);



			}
		},AppConstants.AllMEMBERlIST ,"");
	}



	
	public  void addAdmin(String user_id ,final MemberListBean bean)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_type", AppConstants.USER_TYPE_MOBILE_ADMIN);
		params.put("user_id", user_id);	
		Utill.showProgress(getActivity());
		
		if(InternetConnection.isInternetOn(getActivity()))
		{
			aquery.ajax(WebService.changeusertype, params, JSONObject.class, new AjaxCallback<JSONObject>()
					{
				@Override
				public void callback(String url, JSONObject object, AjaxStatus status) {
					// TODO Auto-generated method stub
					super.callback(url, object, status);
					
					//{"msg":"Admin created successfully","status":"true"}
					
					
					Utill.hideProgress();
					
					
					
					try
					{
						member_auto_tv.setText("");
					
						if(object.getString("status").equals("true"))
						{
							filterList.remove(bean);
							adminList.add(0, bean);
							sqlListe.changeUserType(AppConstants.USER_TYPE_MOBILE_ADMIN , bean.getUser_id());
							auto_complte_adapter.notifyDataSetChanged();
							swipeAdapter.notifyDataSetChanged();
						Utill.showDialg(object.getString("msg"), mContext);	
						
						}
						
					}
					catch(Exception e)
					{
						
					}
				}
					});
		}
		else
		{
			
		}
		
		
		
	}
	public  void cancelAdmin(String user_id ,final MemberListBean bean)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_type", AppConstants.USER_TYPE_MEMBER);
		params.put("user_id", user_id);	
		Utill.showProgress(getActivity());
		
		if(InternetConnection.isInternetOn(getActivity()))
		{
			aquery.ajax(WebService.changeusertype, params, JSONObject.class, new AjaxCallback<JSONObject>()
					{
				@Override
				public void callback(String url, JSONObject object, AjaxStatus status) {
					// TODO Auto-generated method stub
					super.callback(url, object, status);
					
					//{"msg":"Admin created successfully","status":"true"}
					
					
					Utill.hideProgress();
					
					
					
					try
					{
					
						if(object.getString("status").equals("true"))
						{
							adminList.remove(bean);
							filterList.add(0, bean);
							sqlListe.changeUserType(AppConstants.USER_TYPE_MEMBER , bean.getUser_id());

							auto_complte_adapter.notifyDataSetChanged();
							swipeAdapter.notifyDataSetChanged();
						Utill.showDialg(object.getString("msg"), mContext);
						
						}
						
					}
					catch(Exception e)
					{
						
					}
				}
					});
		}
		else
		{
			
		}
		
		
		
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		Utill.hideKeybord(getActivity());
	}
}
