package com.clubscaddy.custumview;



	import android.os.Build;
	import android.util.Log;
	import android.view.LayoutInflater;
	import android.view.View;
import android.view.ViewGroup;
	import android.view.ViewTreeObserver;
	import android.widget.GridView;
	import android.widget.LinearLayout;
	import android.widget.ListAdapter;
import android.widget.ListView;

	import com.clubscaddy.R;

public class ExpandableHeightListView {

		public static void getListViewSize(ListView myListView) {
			ListAdapter myListAdapter = myListView.getAdapter();
			if (myListAdapter == null) {
				//do nothing return null
				return;
			}
			//set listAdapter in loop for getting final size
			int totalHeight = 0;
			for (int size = 0; size < myListAdapter.getCount(); size++) {
				View listItem = myListAdapter.getView(size, null, myListView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
			//setting listview item in adapter
			ViewGroup.LayoutParams params = myListView.getLayoutParams();
			params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
			myListView.setLayoutParams(params);
			// print height of adapter on log
			Log.i("height of listItem:", String.valueOf(totalHeight));
		}

	static LinearLayout	headerLayout ;
	static int totalHeight = 0;
		public static void getListViewWithheaderSize(final  ListView myListView , int layoutHeight) {
			final ListAdapter myListAdapter = myListView.getAdapter();
			if (myListAdapter == null) {
				//do nothing return null
				return;
			}
			//set listAdapter in loop for getting final size
			 totalHeight = 0;
			for (int size = 0; size < myListAdapter.getCount(); size++) {
				View listItem = myListAdapter.getView(size, null, myListView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
			//setting listview item in adapter
		 	headerLayout = (LinearLayout) LayoutInflater.from(myListView.getContext()).inflate(R.layout.schdule_list_headed , null);




			ViewGroup.LayoutParams params = myListView.getLayoutParams();
			params.height =layoutHeight+
					totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
			myListView.setLayoutParams(params);



			// print height of adapter on log
			Log.i("height of listItem:", String.valueOf(totalHeight+50));
		}


		
		public  void getGridViewSize(GridView myGridView) {
			ListAdapter myListAdapter = myGridView.getAdapter();
			if (myListAdapter == null) {
				//do nothing return null
				return;
			}
			//set listAdapter in loop for getting final size
			int totalHeight = 0;

			int count = myListAdapter.getCount() ;

			 if (count%2 == 0)
			 {
				 count = count/2 ;
			 }
			 else
			 {
				 count = count/2 +1;
			 }

			for (int size = 0; size < count; size++) {
				View listItem = myListAdapter.getView(size, null, myGridView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
			//setting listview item in adapter
			ViewGroup.LayoutParams params = myGridView.getLayoutParams();
			params.height = totalHeight ;
			myGridView.setLayoutParams(params);
			// print height of adapter on log
			Log.i("height of listItem:", String.valueOf(totalHeight));
		}












	}
