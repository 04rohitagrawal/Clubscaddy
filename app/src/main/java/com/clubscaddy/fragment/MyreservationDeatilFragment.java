package com.clubscaddy.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.util.Util;
import com.clubscaddy.Interface.FragmentBackListener;
import com.clubscaddy.Interface.FragmentBackResponseListener;
import com.clubscaddy.custumview.ExpandableHeightListView;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.Adapter.MyCourtListAdapter;
import com.clubscaddy.Bean.MyReservationBean;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.utility.SqlListe;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class MyreservationDeatilFragment extends Fragment
{
	MyReservationBean bean ;
	TextView booking_name;
	View view ;
	//TableLayout court_booking_table_layout;
	ListView court_list_view;
	TextView booking_name_tv;
	TextView booking_date_tv;
	TextView recursion_tv;
	TextView delete_btn;
	ProgressDialog pd ;
	SqlListe sqlListe;
	AQuery aQuery;
	SessionManager sessionManager ;
	String deleteID ;
	TextView court_name_tv;
	ProgressDialog progressDialog ;
	LinearLayout deleteBtnLayout ;
	TextView edit_btn ;

	FragmentBackResponseListener datepickerFromMyReservationListener ;

	 public void inslizedView(MyReservationBean bean ,FragmentBackResponseListener datepickerFromMyReservationListener)
	 {
	    this.bean = bean;
		 this.datepickerFromMyReservationListener = datepickerFromMyReservationListener;

	 }
	
	
 @Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	 view = inflater.inflate(R.layout.my_reservation_datails_fragment, null);
	 
	// court_booking_table_layout = (TableLayout) view.findViewById(R.id.court_booking_table_layout);

	 deleteBtnLayout = (LinearLayout) LayoutInflater.from(getActivity())
			 .inflate(R.layout.my_reservation_delete_btn , null);

	 aQuery = new AQuery(getActivity());
	 court_name_tv = (TextView) view.findViewById(R.id.court_name_tv);

	 sessionManager = new SessionManager();
	 court_name_tv.setText(sessionManager.getSportFiledName(getActivity())+" Name");

	 sqlListe = new SqlListe(getActivity());
	 progressDialog = new ProgressDialog(getActivity());
	 progressDialog.setCancelable(false);
     progressDialog.setMessage("Please wait....");


	 deleteID =  bean.getMemberbookedid();

	 DirectorFragmentManageActivity.updateTitle(getResources().getString(R.string.my_reservation));
		
	 court_list_view = (ListView) view.findViewById(R.id.court_list_view);
	 
	 booking_name = (TextView) view.findViewById(R.id.booking_name_tv);
	 
	 booking_name.setText(bean.getBookingname());
	 
	 booking_date_tv = (TextView) view.findViewById(R.id.booking_date_tv);
	 
	 recursion_tv =  (TextView) view.findViewById(R.id.recursion_tv);
	 //
	 if(bean.getRecursivedates().equalsIgnoreCase("")|| bean.getRecursivedates()=="")
	 {
		 booking_date_tv.setText(bean.getReservedate()); 
	 }
	 else
	 {
		 booking_date_tv.setText(bean.getRecursivedates()); 
	 }


	 delete_btn =  (TextView) deleteBtnLayout.findViewById(R.id.delete_btn);

	 edit_btn =  (TextView) deleteBtnLayout.findViewById(R.id.edit_btn);
	 edit_btn.setVisibility(View.VISIBLE);

	 edit_btn.setOnClickListener(new OnClickListener() {
		 @Override
		 public void onClick(View v) {


			 getActivity().getSupportFragmentManager().popBackStack();

			 String inputDate = bean.getReservedate() ;

			 String bookingUniqueId ;

			 if (( bean.getMemberbookedid().equals("0") == false))
			 {
				 bookingUniqueId =  bean.getMemberbookedid() ;
			 }
			 else
			 {
				 bookingUniqueId =  bean.getCourt_reservation_recursiveid() ;

			 }


			 try
			 {
				 datepickerFromMyReservationListener.onResponse(Utill.formattedDateFromString("EEEEdd MMMM yyyy" , "d-MMMM-yyyy" , inputDate) , bookingUniqueId);

			 }
            catch (Exception e)
			{

			}



			 		 	 }
	 });

	 
	 delete_btn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			
			
			
			
			
			
			
			
			
			
			
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

			// Setting Dialog Title
			alertDialog.setTitle(SessionManager.getClubName(getActivity()));

			// Setting Dialog Message
			 if(bean.getCourt_reservation_recursiveid().equals("0"))
			 {
				 alertDialog.setMessage("Are you sure you want delete  this reservation?");
					
			 }
			 else
			 {
				 alertDialog.setMessage("This will delete entire recursive reservation. If you want to delete reservation for particular day use reservation screen.");
					
				 }
			

			// Setting Icon to Dialog
		
//sendReply(bean, status);
			// Setting Positive "Yes" Button
			alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int which) {
					
					//bean.getNotifications_id()
					 if(bean.getCourt_reservation_recursiveid().equals("0"))
					 {
						 deletMultipleBooking();
					 }
					 else
					 {
						deleteRecursiveBooking(); 
					 }
					 dialog.cancel();
					
						}
			});

			// Setting Negative "NO" Button
			alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,	int which) {
				// Write your code here to invoke NO event
					
				}
			});

			// Showing Alert Message
			alertDialog.show();
			
			
			
			
			
			
			
			
			
			
		}
	});
	 
	 if(bean.getCourt_reservation_recursiveid().equals("0"))
	 {
		 if(bean.getBookingname().equals("")||bean.getBookingname().equals(""))
			{
			 booking_name.setVisibility(View.GONE);	
			}
			else
			{
				booking_name.setText(bean.getBookingname());	
			}
			//Toast.makeText(getActivity(), bean.getCourt_reservation_recursiveid(), 1).show();
			if(bean.getCourt_reservation_recursiveid().equals("0"))
			{
				recursion_tv.setText(bean.getReservedate().split(" ")[0]);
				booking_date_tv.setText(bean.getReservedate().split(" ")[1]+" "+bean.getReservedate().split(" ")[2]+" "+bean.getReservedate().split(" ")[3]);
			}
			
	 }
	 
	 else
		 recursion_tv.setText("Recursive reservation");
	 court_list_view.setAdapter(new MyCourtListAdapter(bean.getCourt_list(), getActivity()));

	 court_list_view.addFooterView(deleteBtnLayout);
	 // getListViewSize(court_list_view);
	 
	/* if(bean.getCourt_reservation_recursiveid().equals("0"))
		 recursion_tv.setText("Booking");
		 else
			 recursion_tv.setText("Recursive Booking");
	 */
	 
	 
	
	 
	 

	/* for(int i = 0 ; i < bean.getCourt_list().size() ;i++)
	 
	 {
		 
		 TableLayout 
		 
		  Create a new row to be added. 
		 TableRow tr = new TableRow(getActivity());
		 tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
		  Create a Button to be the row-content. 
		 TextView b = new TextView(getActivity());
		 b.setText("Dynamic Button");
		 b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
		  Add Button to row. 
		 tr.addView(b);
		 
		 
		 TableRow tr1 = new TableRow(getActivity());
		 tr1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
	
		 TextView b1 = new TextView(getActivity());
		 tr1.addView(b1);
		  Add row to TableLayout. 
		 //tr.setBackgroundResource(R.drawable.sf_gradient_03);
		 court_booking_table_layout.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
		 court_booking_table_layout.addView(tr1, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
			
		 //court_booking_table_layout.addView(tr1, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
			
		 
		 
		 
		 
	 }*/
	 
	return view;
}
 
   public void deletMultipleBooking()
   {
	  
	   
	   HashMap<String, String>params = new HashMap<String, String>();
	   params.put("memberbookid", bean.getMemberbookedid());
	   params.put("court_reservation_club_id", SessionManager.getUser_Club_id(getActivity()));
	   params.put("court_reservation_user_id", SessionManager.getUser_id(getActivity()));

	   progressDialog.show();

	   aQuery.ajax(WebService.deleteMultiple, params, JSONObject.class, new AjaxCallback<JSONObject>()
			   {
		   @SuppressWarnings("deprecation")
		@Override
		public void callback(String url, JSONObject object, AjaxStatus status) 
		   {
			// TODO Auto-generated method stub
			 
			super.callback(url, object, status);
			   progressDialog.dismiss();



			   if(object != null)
			{
				try
				{
					final	AlertDialog alertDialog = new AlertDialog.Builder(
			                getActivity()).create();


			alertDialog.setTitle(SessionManager.getClubName(getActivity()));
					alertDialog.setCancelable(false);

			alertDialog.setMessage(object.getString("message"));
					alertDialog.setCancelable(false);

			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			        // Write your code here to execute after dialog closed

						deleteDataEventCalender();
			        	alertDialog.dismiss();
			        	getActivity().getSupportFragmentManager().popBackStack();
						datepickerFromMyReservationListener.deleteSuccessFullyListener();

					}
			});

			// Showing Alert Message
			alertDialog.show();
				//Utill.showDialg(object.getString("message"), getActivity());
				}
				catch(Exception e)
				{

				}
			}
			
			
		}
			   });  
	
	   
   }
 
 
   public void  deleteRecursiveBooking()
   {
	   HashMap<String, String>params = new HashMap<String, String>();
	   params.put("user_type", SessionManager.getUser_type(getActivity()));
	   params.put("court_reservation_recursiveid", bean.getCourt_reservation_recursiveid());
	 //  params.put("court_reservation_date", AppConstants.getAppDate(Calendar.getInstance()));
	   progressDialog.show();
	   
	   aQuery.ajax(WebService.deleteRecursive, params, JSONObject.class, new AjaxCallback<JSONObject>()
			   {
		   @SuppressWarnings("deprecation")
		@Override
		public void callback(String url, JSONObject object, AjaxStatus status) 
		   {
			// TODO Auto-generated method stub
			  // Log.e("object", object+"");
			super.callback(url, object, status);
			progressDialog.dismiss();
			
			if(object != null)
			{
				try
				{
					final	AlertDialog alertDialog = new AlertDialog.Builder(
			                getActivity()).create();

			// Setting Dialog Title
			alertDialog.setTitle(SessionManager.getClubName(getActivity()));

			// Setting Dialog Message
			alertDialog.setMessage(object.getString("message"));

			// Setting Icon to Dialog

					alertDialog.setCancelable(false);

			// Setting OK Button
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			        // Write your code here to execute after dialog closed
			        	alertDialog.dismiss();
			        	getActivity().getSupportFragmentManager().popBackStack();

						datepickerFromMyReservationListener.deleteSuccessFullyListener();
			        }
			});

			// Showing Alert Message
			alertDialog.show();
				//Utill.showDialg(object.getString("message"), getActivity());	
				}
				catch(Exception e)
				{
					
				}	
			}
			
			
			
			// {"message":"Reservation cancelled successfully","status":"true","memberbookid":"63"}

		}
			   });  
 
   }
	public void deleteDataEventCalender()
	{
		try
		{
			Uri eventsUri = Uri.parse("content://com.android.calendar/" + "events");

			ArrayList<Integer>eventIds = sqlListe.getEventIds(deleteID);


			for (int i = 0 ; i < eventIds.size() ;i++)
			{
				int event_id = eventIds.get(i);
				getActivity().getApplicationContext() .getContentResolver().delete(ContentUris.withAppendedId(eventsUri,event_id ), null, null);

			}


			sqlListe.deleteEvents(Integer.parseInt(deleteID));

			//	getActivity().getApplicationContext().getContentResolver().delete(CalendarContract.Events.CONTENT_URI ,CalendarContract.Reminders.EVENT_ID +" = ?", new String[]{sqlListe.getEventId(court_reservation_id)+""});

		}
		catch (Exception e)
		{
			ShowUserMessage.showDialogOnActivity(getActivity() , e.getMessage());
		}
	}
	public static void getListViewSize(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem instanceof ViewGroup) {
				listItem.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.WRAP_CONTENT, ListView.LayoutParams.WRAP_CONTENT));
			}
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}
