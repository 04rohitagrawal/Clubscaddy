package com.clubscaddy.fragment;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.R.color;
import com.clubscaddy.Adapter.ExpandableListAdapter;
import com.clubscaddy.Adapter.GroupAdapter;
import com.clubscaddy.Adapter.GroupAdapter.SwipeEventItemClickListener;
import com.clubscaddy.Bean.DropInBean;
import com.clubscaddy.Bean.GroupBean;
import com.clubscaddy.utility.GlobalValues;
import com.clubscaddy.Server.JsonUtility;
import com.clubscaddy.Interface.ModelManagerListener;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;

public class DropInGroupHomeFragment extends Fragment {
	
	public static final int DROP_INS_SCREEN = 1;
	public static final int GROUPS_SCREEN = 2;
	public static int CURRENT_SCREEN;

	String Tag = getClass().getName();
	public static FragmentManager mFragmentManager;
	public static Fragment mFragment;
	Context mContext;

	// home views
	LinearLayout dropInsLL, groupsLL;
	TextView dropinText, dropInLine, groupText, GroupsLine;

	ListView groupListView;
	ImageButton addGroup;

	public static Fragment getInstance(FragmentManager mFragManager) {
		if (mFragment == null) {
			mFragment = new DropInGroupHomeFragment();
		}
		return mFragment;
	}

	ArrayList<GroupBean> groupList;

	GroupAdapter groupAdapter;

	public void setGroupAdapter(final ArrayList<GroupBean> list) {
		groupAdapter = null;
		groupListView.setVisibility(View.VISIBLE);
		groupAdapter = new GroupAdapter(mContext, groupList);
		groupAdapter.setEventItemClickListener(new SwipeEventItemClickListener() {

			@Override
			public void OnEditClick(int position) {
				
				 getGroupListItem(groupList.get(position).getGroup_id());
				
				/**/
			}

			@Override
			public void OnBlockClick(int position, int blockStatus) {

				GroupBean bean = groupList.get(position);
				deleteGroup(bean.getGroup_id());
			}
		});

		groupListView.setAdapter(groupAdapter);
		
		//ShowUserMessage.showUserMessage(mContext, "" + groupAdapter.getCount());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e(Tag, "onCreateView");
		
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.drop_in_group_home, container, false);
		mContext = getActivity();

		
		
		if (DirectorFragmentManageActivity.actionbar_titletext != null) {
			DirectorFragmentManageActivity.updateTitle("Tennis Club");
		}

		if (DirectorFragmentManageActivity.backButton != null) {
			DirectorFragmentManageActivity.showBackButton();
			DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
		}
		initializeView(rootView);
		setOnclicks();
		DirectorFragmentManageActivity.showLogoutButton();
		return rootView;
	}

	void initializeView(View view) {
		groupList = new ArrayList<GroupBean>();
		dropInsLL = (LinearLayout) view.findViewById(R.id.dropin_layout);
		groupsLL = (LinearLayout) view.findViewById(R.id.groups_layout);
		dropinText = (TextView) view.findViewById(R.id.dropin_text);
		dropInLine = (TextView) view.findViewById(R.id.dropin_line);
		groupText = (TextView) view.findViewById(R.id.groups_text);
		GroupsLine = (TextView) view.findViewById(R.id.groups_line);
		groupListView = (ListView) view.findViewById(R.id.list);
		addGroup = (ImageButton) view.findViewById(R.id.add_group_drop_in);
		dropInExpandableListView = (ListView) view.findViewById(R.id.expandable_list_view);

	}

	void setOnclicks() {
		dropInsLL.setOnClickListener(viewOnClicks);
		groupsLL.setOnClickListener(viewOnClicks);
		addGroup.setOnClickListener(viewOnClicks);
		//groupListView.setSwipeListViewListener(baseSwipeListener);
		if(CURRENT_SCREEN==GROUPS_SCREEN){
			groupsLL.performClick();
			if (DirectorFragmentManageActivity.actionbar_titletext != null) {
				DirectorFragmentManageActivity.updateTitle(mContext.getString(R.string.groups));
			}
		}
		else{
			dropInsLL.performClick();
			if (DirectorFragmentManageActivity.actionbar_titletext != null) {
				DirectorFragmentManageActivity.updateTitle("Match Makers");
			}
		}
			
	}


	OnClickListener viewOnClicks = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.dropin_layout:
				CURRENT_SCREEN = DROP_INS_SCREEN;
				dropInLine.setVisibility(View.VISIBLE);
				GroupsLine.setVisibility(View.GONE);
				dropinText.setTextColor(mContext.getResources().getColor(color.blue_header));
				groupText.setTextColor(mContext.getResources().getColor(color.black_color));
				addGroup.setVisibility(View.VISIBLE);
				groupListView.setVisibility(View.GONE);
				dropInExpandableListView.setVisibility(View.GONE);
				getDropInList();
				break;
			case R.id.groups_layout:
				CURRENT_SCREEN = GROUPS_SCREEN;
				dropInLine.setVisibility(View.GONE);
				GroupsLine.setVisibility(View.VISIBLE);
				dropinText.setTextColor(mContext.getResources().getColor(color.black_color));
				groupText.setTextColor(mContext.getResources().getColor(color.blue_header));
				groupListView.setVisibility(View.GONE);
				dropInExpandableListView.setVisibility(View.GONE);
				getGroupList();
				break;
			case R.id.add_group_drop_in:
				if(CURRENT_SCREEN == GROUPS_SCREEN){
					AddGroupFragment.groupEdit = null;
					
					DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.AddGroupFragment_id);	
				}else if(CURRENT_SCREEN == DROP_INS_SCREEN){
					DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.AddDropInsFragment_id);
				}
				
				break;
			}
		}
	};

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

	OnClickListener addToBack = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				DirectorFragmentManageActivity.popBackStackFragment();
			} catch (Exception e) {
				ShowUserMessage.showUserMessage(getActivity(), e.toString());
			}
		}
	};

	private void getGroupList() {
		if (Utill.isNetworkAvailable(getActivity())) {
			Utill.showProgress(mContext);
			GlobalValues.getModelManagerObj(mContext).getGroupList(new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					groupList = JsonUtility.parseGroupList(json);
					setGroupAdapter(groupList);


					Utill.hideProgress();
				}

				@Override
				public void onError(String msg) {
					ShowUserMessage.showUserMessage(getActivity(), msg);
					Utill.hideProgress();
				}
			});
		} else {
			Utill.showNetworkError(mContext);
		}
	}
	private void getGroupListItem(String group_id) {
		if (Utill.isNetworkAvailable(getActivity())) {
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("group_id", group_id);
			Utill.showProgress(mContext);
			GlobalValues.getModelManagerObj(mContext).getGroupListItem(new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					Utill.hideProgress();
					groupList = JsonUtility.parseGroupList(json);
					AddGroupFragment.groupEdit = groupList.get(0);
					//DirectorFragmentManageActivity.switchFragment(DirectorFragmentManageActivity.AddGroupFragment_id);

					AddGroupFragment fragment = new AddGroupFragment(AddGroupFragment.groupEdit);
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "fragment").addToBackStack(null).commit();

				}

				@Override
				public void onError(String msg) {
					ShowUserMessage.showUserMessage(getActivity(), msg);
					Utill.hideProgress();
				}
			},params);
		} else {
			Utill.showNetworkError(mContext);
		}
	}
	ListView dropInExpandableListView;
	ArrayList<DropInBean> dropInList = new ArrayList<DropInBean>();
	private void getDropInList() {


		if (Utill.isNetworkAvailable(getActivity())) {
			Utill.showProgress(mContext);
			GlobalValues.getModelManagerObj(mContext).getDropInList(new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					dropInList = JsonUtility.parseDropInList(json);
					Utill.hideProgress();
					ExpandableListAdapter adapter = new ExpandableListAdapter(mContext, dropInList,new onClickDeleteDropIn());
					groupListView.setVisibility(View.GONE);
					dropInExpandableListView.setVisibility(View.VISIBLE);
					dropInExpandableListView.setAdapter(adapter);
					
					
					
					dropInExpandableListView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
							// TODO Auto-generated method stub
							
							
							MatchMakerDetailsFragment fragment = new MatchMakerDetailsFragment();
							fragment.setInstanse(dropInList.get(pos).getDropin_id());
							getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "fragment").addToBackStack(null).commit();
							
						}
					});
					
					
					
					
					
					if (dropInList != null && dropInList.size() >= 1000) {
						addGroup.setVisibility(View.GONE);
					} else {
						addGroup.setVisibility(View.VISIBLE);
					}

					Utill.hideProgress();
				}

				@Override
				public void onError(String msg) {
					ShowUserMessage.showUserMessage(getActivity(), msg);
					Utill.hideProgress();
				}
			});
		} else {
			Utill.showNetworkError(mContext);
		}
	}
	public class onClickDeleteDropIn{
		public void onDelete(int position){
			deleteDropIns(dropInList.get(position));
		}
		public void onDeleteMember(int group,int child){
			//deleteDropIns(dropInList.get(position));
			deleteAcceptedUserFromDrop(group, child);
		}
	}
	

	void deleteGroup(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("group_owner_user_id", SessionManager.getUser_id(mContext));
		params.put("group_id", id);
		if (Utill.isNetworkAvailable(getActivity())) {
			Utill.showProgress(mContext);
			GlobalValues.getModelManagerObj(mContext).deleteGroup(params, new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					Utill.hideProgress();
					getGroupList();
				}

				@Override
				public void onError(String msg) {
					ShowUserMessage.showUserMessage(getActivity(), msg);
					Utill.hideProgress();
				}
			});
		} else {
			Utill.showNetworkError(mContext);
		}

	}
	
	
	void deleteDropIns(DropInBean bean) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dropin_id",bean.getDropin_id());
		params.put("dropin_user_id",SessionManager.getUser_id(mContext));
		if (Utill.isNetworkAvailable(getActivity())) {
			Utill.showProgress(mContext);
			GlobalValues.getModelManagerObj(mContext).deleteDropIn(params, new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					Utill.hideProgress();
					getDropInList();
				}

				@Override
				public void onError(String msg) {
					ShowUserMessage.showUserMessage(getActivity(), msg);
					Utill.hideProgress();
				}
			});
		} else {
			Utill.showNetworkError(mContext);
		}

	}
	
	void deleteAcceptedUserFromDrop(int groupId,int chieldId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("notification_dropin_id",dropInList.get(groupId).getDropin_id());//
		params.put("dropin_member_user_id",dropInList.get(groupId).getMemeberList().get(chieldId).getMember_id());//
		params.put("notifications_sender_id",SessionManager.getUser_id(mContext));
		
	//	params.put("","");
		if (Utill.isNetworkAvailable(getActivity())) {
			Utill.showProgress(mContext);
			GlobalValues.getModelManagerObj(mContext).deleteMemberFromDropIn(params, new ModelManagerListener() {
				@Override
				public void onSuccess(String json) {
					Utill.hideProgress();
					getDropInList();
				}

				@Override
				public void onError(String msg) {
					ShowUserMessage.showUserMessage(getActivity(), msg);
					Utill.hideProgress();
				}
			});
		} else {
			Utill.showNetworkError(mContext);
		}

	}

}
