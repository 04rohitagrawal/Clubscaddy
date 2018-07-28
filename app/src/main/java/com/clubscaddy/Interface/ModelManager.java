package com.clubscaddy.Interface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.Bean.UserPics;
import com.clubscaddy.LoginActivity;
import com.clubscaddy.shortcutburgerdata.ShortcutBadger;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.DirectorDashboardAdapter;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.fragment.DirectorDashboardFragment;

public class ModelManager {
	private final String TAG = "ModelManager";
	private AQuery aq;
	private Context mContext;

	public ModelManager(Context context) {
		aq = new AQuery(context);
		mContext = context;
	}

	public void getReservation(String date, String court_club_id, final ModelManagerListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("court_club_id", SessionManager.getUser_Club_id(mContext));
		params.put("court_reservation_date", date);
		aq.ajax(WebService.get_reservation, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) 
				{
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR)
				{
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) 
				{
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true"))
						{
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) 
						{
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}

	public void sendReservation(Map<String, Object> params, final ModelManagerListener listener)
	{
		aq.ajax(WebService.send_reservation_request, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				
				
				String ss = object+"";
				
				Log.e("ssss", ss);
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR)
				{
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					try {
						String myStatus = object.getString("status");
						if (myStatus.equalsIgnoreCase("true")) {
							String result = object.toString();
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					listener.onError("Error.");
				}
			}
		});
	}











	public void getMemberDetail(final Activity activity , final Map<String, Object> params, final OnServerRespondingListener listener) {


		if (Validation.isNetworkAvailable(activity) == false)
		{
			listener.internetConnectionProble();
			return;
		}





		aq.ajax(WebService.getMemberDetail, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
				int code = status.getCode();
				 if (code == 200) {
					String myStatus;
					try {


						if (jsonObject.optBoolean("status"))
						{
							JSONObject object = jsonObject.optJSONArray("response").optJSONObject(0);
							MemberListBean bean = new MemberListBean();
							if(object.optString("user_gender").equals("1"))
							{
								bean.setUser_gender("Male");
							}
							else
							{
								bean.setUser_gender("Female");
							}
							bean.setInstragram_url(object.optString("instagram"));
							bean.setUser_type(object.optString("user_type"));
							bean.setUser_status(object.optString("user_status"));
							bean.setLinkedin_url(object.optString("linkedin"));
							bean.setUser_junior(object.optString("user_junior"));
							bean.setUser_club_id(SessionManager.getUser_Club_id(activity));
							bean.setUser_rating(object.optString("user_rating"));
							bean.setUser_profilepic(object.optString("user_profilepic"));
							bean.setUser_email(object.optString("user_email"));
							bean.setTwitter_url(object.optString("twitter"));
							bean.setUser_first_name(object.optString("user_first_name"));
							bean.setUser_about_me(object.optString("addabout"));
							bean.setUser_phone(object.optString("user_phone"));
							bean.setFace_book_url(object.optString("facebookurl"));
							bean.setUser_last_name(object.optString("user_last_name"));
							bean.setUser_id(object.optString("user_id"));
							bean.setInstragramToken(object.optString("insta_key"));


							try
							{
								JSONArray user_pics_json_array = object.getJSONArray("user_pics");

								ArrayList<UserPics> userPicsArrayList = new ArrayList<UserPics>();

								for (int i = 0 ; i < user_pics_json_array.length() ;i++)
								{
									UserPics userPics = new UserPics();
									JSONObject user_pics_json_array_item = user_pics_json_array.getJSONObject(i);
									userPics.setImageid(user_pics_json_array_item.getInt("id"));
									userPics.setImage_thumb(user_pics_json_array_item.getString("thumb"));
									userPics.setImage_url(user_pics_json_array_item.getString("url"));

									userPicsArrayList.add(userPics);
								}
								bean.setUserPicsArrayList(userPicsArrayList);

							}
							catch (Exception e)
							{

							}


							listener.onSuccess(bean);









						}
						else
						{
							listener.onSuccess(jsonObject);

							//ShowUserMessage.showUserMessage(activity , jsonObject.optString("message"));
						}



					} catch (Exception e) {
						listener.onNetWorkError();
					}

				}

			}
		});
	}






/*	public void getGroupList(Map<String, Object> params, final ModelManagerListener listener) {
		aq.ajax(WebService.get_member_list, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}*/
	
	public void CancelBooking(Map<String, Object> params, final ModelManagerListener listener) {
		aq.ajax(WebService.cancel_booking, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				
			
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							String msg = object.getString("message");
							ShowUserMessage.showUserMessage(mContext, msg);
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}else{
					Utill.hideProgress();
				}
				

			}
		});
	}
	public void MoveBooking(Map<String, Object> params, final ModelManagerListener listener) {
		aq.ajax(WebService.move_slot, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				
			
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							String msg = object.getString("message");
							ShowUserMessage.showUserMessage(mContext, msg);
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}else{
					Utill.hideProgress();
				}
				

			}
		});
	}
	public void getCourtList(final ModelManagerListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("court_club_id", SessionManager.getUser_Club_id(mContext));
		aq.ajax(WebService.get_court_list, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	public void getSlotList(final ModelManagerListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("court_club_id", SessionManager.getUser_Club_id(mContext));
		params.put("court_id",SessionManager.getUser_Club_id(mContext));
		
		aq.ajax(WebService.get_slot_list, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	
	public void verifyBulkSlot(JSONObject json,final ModelManagerListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bulk_request",json.toString());
		params.put("court_reservation_user_id",SessionManager.getUser_id(mContext));
		aq.ajax(WebService.bulkbookingVerify, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				if(object!=null)
				Log.e("123",""+object.toString());
				else
					Log.e("error","Null response.");
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	
	public void sendBulkRequest(JSONObject json,final ModelManagerListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bulk_request",json.toString());
		params.put("court_reservation_user_id",SessionManager.getUser_id(mContext));
		params.put("bulkbooking_purpuse","TENNIS CLUB.");
		 
		aq.ajax(WebService.bulkbookingSend, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	
	public void sendRecursiveReuest(Map<String, Object> params,final ModelManagerListener listener) {
		aq.ajax(WebService.recursivebookingRequest, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	
	public void CancelMultipleBooking(Map<String, Object> params, final ModelManagerListener listener) {
		aq.ajax(WebService.deleteMultiple, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				
			
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							String msg = object.getString("message");
							
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}else{
					Utill.hideProgress();
				}
				

			}
		});
	}
	
	public void CancelRecursiveBooking(Map<String, Object> params, final ModelManagerListener listener) {
		aq.ajax(WebService.deleteRecursive, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				
			
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							String msg = object.getString("message");
							//ShowUserMessage.showUserMessage(mContext, msg);
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}else{
					Utill.hideProgress();
				}
				

			}
		});
	}
	public void createDropIn(Map<String, Object> params,final ModelManagerListener listener) {
		
		aq.ajax(WebService.createDropIn, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				
					
				
				
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							String msg = object.getString("message");
							Utill.showToast(mContext,""+msg);
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	
public void createScore(Map<String, Object> params,final ModelManagerListener listener) {
		
		aq.ajax(WebService.createScore, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							String msg = object.getString("message");
							Utill.showToast(mContext,""+msg);
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
public void EditScore(Map<String, Object> params,final ModelManagerListener listener) {
	
	aq.ajax(WebService.editscore, params, JSONObject.class, new AjaxCallback<JSONObject>() {
		@Override
		public void callback(String url, JSONObject object, AjaxStatus status) {
			int code = status.getCode();
			if (code == AjaxStatus.NETWORK_ERROR) {
				listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
				return;
			} else if (code == AjaxStatus.TRANSFORM_ERROR) {
				listener.onError(mContext.getResources().getString(R.string.server_error));
				return;
			} else if (code == 200) {
				String myStatus;
				try {
					myStatus = object.getString("status");
					String result = object.toString();
					Log.e("result:", "" + result);
					if (myStatus.equalsIgnoreCase("true")) {
						String msg = object.getString("message");
						Utill.showToast(mContext,""+msg);
						listener.onSuccess(result);
					} else if (myStatus.equalsIgnoreCase("false")) {
						String msg = object.getString("message");
						listener.onError(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		}
	});
}
	public void addGroup(boolean edit ,Map<String, Object> params,final ModelManagerListener listener) {
		String webService = "";
		if(edit){
			webService = WebService.editgroup;
		}else{
			webService = WebService.addgroup;
		}
		aq.ajax(webService, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							String msg = object.getString("message");
							Utill.showToast(mContext,""+msg);
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	public void getGroupList(final ModelManagerListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("group_club_id",SessionManager.getUser_Club_id(mContext));
		params.put("group_owner_user_id",SessionManager.getUser_id(mContext));
		
		aq.ajax(WebService.get_group_list, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	public void getGroupListItem(final ModelManagerListener listener,Map<String, Object> params) {
		
		params.put("group_club_id",SessionManager.getUser_Club_id(mContext));
		params.put("group_owner_user_id",SessionManager.getUser_id(mContext));
		
		aq.ajax(WebService.get_group_list, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	public void getDropInList(final ModelManagerListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id",SessionManager.getUser_id(mContext));
		params.put("user_club_id",SessionManager.getUser_Club_id(mContext));
		
		aq.ajax(WebService.get_dropin_list, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	public void getDropInList(final ModelManagerListener listener ,Map<String, Object> params) {
		
		params.put("user_id",SessionManager.getUser_id(mContext));
		params.put("user_club_id",SessionManager.getUser_Club_id(mContext));
		
		aq.ajax(WebService.get_dropin_list, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	public void deleteGroup(Map<String, Object> params, final ModelManagerListener listener) {
		aq.ajax(WebService.deleteGroup, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				
			
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							String msg = object.getString("message");
							//ShowUserMessage.showUserMessage(mContext, msg);
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}else{
					Utill.hideProgress();
				}
				

			}
		});
	}
	
	
	public void deleteEvent(Map<String, Object> params, final ModelManagerListener listener) {
		aq.ajax(WebService.deleteEvent, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				
			
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							String msg = object.getString("message");

							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}else{
					Utill.hideProgress();
				}
				

			}
		});
	}
	
	
	public void deleteDropIn(Map<String, Object> params, final ModelManagerListener listener) {
		aq.ajax(WebService.deletedropin, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				
			
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							String msg = object.getString("message");
							//ShowUserMessage.showUserMessage(mContext, msg);
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}else{
					Utill.hideProgress();
				}
				

			}
		});
	}
	
	public void getNotification(final ModelManagerListener listener ,Activity activity) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("notifications_reciever_id",SessionManager.getUser_id(activity));
	//	Toast.makeText(mContext, "id == "+SessionManager.getUser_id(activity)+" "+params.toString(), 1).show();
		
		aq.ajax(WebService.get_notifications, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				
				
				
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	
	public void getNotificationCount(final ModelManagerListener listener,final Activity activity) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("notifications_reciever_id",SessionManager.getUser_id(mContext));
		params.put("user_club_id",SessionManager.getUser_Club_id(mContext));
		
		aq.ajax(WebService.getNotificationCount, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@SuppressWarnings("static-access")
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {

				Log.e("object",object+"");
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();



						
						JSONObject dashboardinfo = object.getJSONObject("dashboardinfo");
					//	Log.e("result:", "" + object);

						SessionManager sessionManager1 = new SessionManager(mContext);
						sessionManager1.setReservationLink(dashboardinfo.getString("reservation_link"));
						sessionManager1.setResmoduleStatus(dashboardinfo.getString("resmodule_status"));
						if (myStatus.equalsIgnoreCase("true")) {
							String unreadCount = object.getString("unread");
							try
							{
								ShortcutBadger.applyCount(mContext, Integer.parseInt(unreadCount) );
							//ShortcutBadger.setBadge(mContext,Integer.parseInt(unreadCount) );	
							}
							catch(Exception e)
							{
						
							}
							SessionManager sessionManager = new SessionManager(mContext);
							sessionManager.setLastDateOfMemberListUpdation(object.getString("last_update_date"));

							AppConstants.setNotificationCount(unreadCount, mContext);
							AppConstants.setClub_temp(object.getJSONObject("dashboardinfo").getString("club_temp"));
							AppConstants.setNumberOfOpenCourts(object.getJSONObject("dashboardinfo").getString("opencourts"));
							AppConstants.setNumberOfBookedCourts(object.getJSONObject("dashboardinfo").getString("bookedcourts"));
							AppConstants.setClub_status_message(object.getJSONObject("dashboardinfo").getString("club_status_message"));
						SessionManager.setClubMessage(mContext, object.getJSONObject("dashboardinfo").getString("club_status_message"));
							try {
								//AppConstants.club_status_change_date = object.getJSONObject("dashboardinfo").getString("club_status_change_date");
                                sessionManager.setClub_status_change_date(mContext ,  object.getJSONObject("dashboardinfo").getString("club_status_change_date"));
								//Log.e("date:", "" + AppConstants.club_status_change_date+"");
								DirectorDashboardFragment.dir_clubMessage.setText(""+AppConstants.getClub_status_message());
								DirectorDashboardFragment.courtTempuratureTV.setText(""+AppConstants.getClub_temp());
								DirectorDashboardFragment.numberOfOpenCourtsTV.setText(""+AppConstants.getNumberOfOpenCourts());
								DirectorDashboardFragment.numberOfBookedCourtsTV.setText(""+AppConstants.getNumberOfBookedCourts());
								DirectorDashboardAdapter.notificationCount.setText(""+unreadCount);
								DirectorDashboardFragment.date_tv.setText("Updated : "+sessionManager.getClub_status_change_date(mContext));
								AppConstants.setUser_news(""+(object.optInt("user_news")));
								AppConstants.setUser_classified(object.getInt("user_classified"));
								AppConstants.setNotificationCount(unreadCount,activity);
								DirectorDashboardFragment.adapter.notifyDataSetChanged(); 
							} catch (Exception e) {
								// TODO: handle exception
							}
							String name = SessionManager.getUser_id(activity);

							boolean isUseridnull = name.equals("")||name == "";
							if(dashboardinfo.getString("user_status").equals("2")&& !isUseridnull)
							{
								
								Intent intent = new Intent(activity, LoginActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.putExtra("EXIT", true);
								activity.finish();
								activity.startActivity(intent);
								SessionManager.clearSharePref(activity);
							}
							//Utill.showToast(mContext, ""+unreadCount);
							
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
						
						
						
						
						
						
						
						
						
						
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	
	
	public void sendReply(Map<String, Object> params, final ModelManagerListener listener) {
		aq.ajax(WebService.send_reply, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				
				Log.e("resule", object+"");
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					try {
						String myStatus = object.getString("status");
						if (myStatus.equalsIgnoreCase("true")) {
							String result = object.toString();
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					listener.onError("Error.");
				}
			}
		});
	}
	

	public void createNewsFeed(Map<String, Object> params,final ModelManagerListener listener) {
		

		aq.ajax(WebService.create_News, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							String msg = object.getString("message");
							Utill.showToast(mContext,""+msg);
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	
	int counter;
	public void getNews(final ModelManagerListener listener ,int counter) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("news_club_id",SessionManager.getUser_Club_id(mContext));
		params.put("news_user_id",SessionManager.getUser_id(mContext));
		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		 densityDpi = dm.densityDpi;
		
		if (densityDpi > 0 && densityDpi <= 160) {
			
			densityDpi = 160;
			
		} else if (densityDpi > 160 && densityDpi <= 240) {
			
			densityDpi = 240;
			
		} else if (densityDpi > 240 && densityDpi <= 320) {
			
			densityDpi = 320;
			
		} else {

			densityDpi = 500; //1020*1920
		}
		if(counter == 0)
		{
			params.put("counter",String.valueOf(""));
		}
		else
		{
			params.put("counter",String.valueOf(counter));	
		}//{density=240, news_user_id=295, news_club_id=63, counter=}
		
		params.put("density",String.valueOf(densityDpi));
		aq.ajax(WebService.get_News, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	public void likeNews(Map<String, Object> params,final ModelManagerListener listener) {
		aq.ajax(WebService.like_News, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	
	public void commentNews(Map<String, Object> params,final ModelManagerListener listener) {
		aq.ajax(WebService.comment_News, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	
	public void deleteMemberFromDropIn(Map<String, Object> params, final ModelManagerListener listener) {
		aq.ajax(WebService.deletedropinUser, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if(object!=null)
				Log.e("result",""+object.toString());
			
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							String msg = object.getString("message");
							ShowUserMessage.showUserMessage(mContext, msg);
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}else{
					Utill.hideProgress();
				}
				

			}
		});
	}
	int densityDpi;
	public void getEventsList(final ModelManagerListener listener ,String more) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("event_club_id",SessionManager.getUser_Club_id(mContext));
		params.put("event_user_id",SessionManager.getUser_id(mContext));
		params.put("more",more);
		params.put("user_type",SessionManager.getUser_type(mContext));

		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		 densityDpi = dm.densityDpi;
		
		if (densityDpi > 0 && densityDpi <= 160) {
			
			densityDpi = 160;
			
		} else if (densityDpi > 160 && densityDpi <= 240) {
			
			densityDpi = 240;
			
		} else if (densityDpi > 240 && densityDpi <= 320) {
			
			densityDpi = 320;
			
		} else {

			densityDpi = 500; 
		}
		
	 String club_id =	SessionManager.getUser_Club_id(mContext);
		//Toast.makeText(mContext,densityDpi+"  " + SessionManager.getUser_Club_id(mContext)+" "+SessionManager.getUser_id(mContext) , 1).show();
		params.put("density",String.valueOf(densityDpi));
		aq.ajax(WebService.get_all_events_list, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				Utill.hideProgress();

				Log.e("result", object.toString());
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						
						Log.e("result1:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) 
						{
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false"))
						{
							//Toast.makeText(mContext, "status "+myStatus, 1).show();
							String msg = object.getString("msg");
							listener.onError(msg);
						}
					} catch (JSONException e) {
					//	Toast.makeText(mContext, densityDpi+" status "+e.getMessage(), 1).show();
						e.printStackTrace();
					}

				}

			}
		});
	}
	public void getEventsList(final ModelManagerListener listener,Map<String, Object> params) {
		
		params.put("event_club_id",SessionManager.getUser_Club_id(mContext));
		params.put("event_user_id",SessionManager.getUser_id(mContext));
		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		 densityDpi = dm.densityDpi;
		
		if (densityDpi > 0 && densityDpi <= 160) {
			
			densityDpi = 160;
			
		} else if (densityDpi > 160 && densityDpi <= 240) {
			
			densityDpi = 240;
			
		} else if (densityDpi > 240 && densityDpi <= 320) {
			
			densityDpi = 320;
			
		} else {

			densityDpi = 500; 
		}
		
	 String club_id =	SessionManager.getUser_Club_id(mContext);
		//Toast.makeText(mContext,densityDpi+"  " + SessionManager.getUser_Club_id(mContext)+" "+SessionManager.getUser_id(mContext) , 1).show();
		params.put("density",String.valueOf(densityDpi));
		aq.ajax(WebService.get_all_events_list, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				
				Log.e("result", object.toString());
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						
						Log.e("result1:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) 
						{
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false"))
						{
							//Toast.makeText(mContext, "status "+myStatus, 1).show();
							String msg = object.getString("msg");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						Toast.makeText(mContext, densityDpi+" status "+e.getMessage(), Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}

				}

			}
		});
	}
	public void getMyEventsList(final ModelManagerListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("event_participants",SessionManager.getUser_id(mContext));
		params.put("event_club_id",SessionManager.getUser_Club_id(mContext));
		aq.ajax(WebService.myevent, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	
	
	public void joinEventReply(Map<String, Object> params, final ModelManagerListener listener) {
		aq.ajax(WebService.join_Event, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					try {
						String myStatus = object.getString("status");
						if (myStatus.equalsIgnoreCase("true")) {
							String result = object.toString();
						//	Utill.showToast(mContext, result)
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
						Utill.hideProgress();
					}
				} else {
					listener.onError("Error.");
				}
			}
		});
	}
	public void WithDrawlFromEvent(Map<String, Object> params, final ModelManagerListener listener) {
		aq.ajax(WebService.WithDrawl, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					try {
						String myStatus = object.getString("status");
						if (myStatus.equalsIgnoreCase("true")) {
							String result = object.toString();
						//	Utill.showToast(mContext, result)
							listener.onSuccess(object.getString("message"));
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
						Utill.hideProgress();
					}
				} else {
					listener.onError("Error.");
				}
			}
		});
	}
	
	
	public void sendNotification(Map<String, Object> params, final ModelManagerListener listener) {
		aq.ajax(WebService.sendNotificationForEvent, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				
			
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							String msg = object.getString("message");
							ShowUserMessage.showUserMessage(mContext, msg);
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}else{
					Utill.hideProgress();
				}
				

			}
		});
	}

	public void getAllMembersOfClub(Map<String, Object> params, final ModelManagerListener listener) {
		aq.ajax(WebService.allMemberList, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				super.callback(url, object, status);
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	
	public void getScoreDetails(Map<String, Object> params, final ModelManagerListener listener) {
		aq.ajax(WebService.get_score_detail, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	
	public void createBroadCast(Map<String, Object> params,final ModelManagerListener listener) {
		
		aq.ajax(WebService.create_brodcast, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				
			
				
				
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							String msg = object.getString("message");
						
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	
	public void getBroadCastList(Map<String, Object> params, final ModelManagerListener listener) {
		aq.ajax(WebService.get_broadcast_list, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	
	public void getBroadCastDetail(String webUrl , Map<String, Object> params, final ModelManagerListener listener) {
		aq.ajax(webUrl, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	
	public void sendPollingReply( Map<String, Object> params, final ModelManagerListener listener) {
		aq.ajax(WebService.pollingreply, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object, AjaxStatus status) {
				int code = status.getCode();
				if (code == AjaxStatus.NETWORK_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.check_internet_connection));
					return;
				} else if (code == AjaxStatus.TRANSFORM_ERROR) {
					listener.onError(mContext.getResources().getString(R.string.server_error));
					return;
				} else if (code == 200) {
					String myStatus;
					try {
						myStatus = object.getString("status");
						String result = object.toString();
						Log.e("result:", "" + result);
						if (myStatus.equalsIgnoreCase("true")) {
							listener.onSuccess(result);
						} else if (myStatus.equalsIgnoreCase("false")) {
							String msg = object.getString("message");
							listener.onError(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}
		});
	}


}
