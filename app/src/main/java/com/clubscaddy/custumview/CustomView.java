package com.clubscaddy.custumview;


	import android.util.Log;
	import android.view.View;
	import android.view.ViewGroup;
import android.widget.GridView;
	import android.widget.ListAdapter;
import android.widget.ListView;

	public class CustomView {

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
		
		
		
//		public static void getGridViewSize(GridView myGridView,int rowCount) {
//			ListAdapter myListAdapter = myGridView.getAdapter();
//			if (myListAdapter == null) {
//				//do nothing return null
//				return;
//			}
//////			//set listAdapter in loop for getting final size
////			int totalHeight = 0;
////			int maxHeight = 0;//implement new 
////			for (int size = 0; size < myListAdapter.getCount(); size++) {
////				View listItem = myListAdapter.getView(size, null, myGridView);				
////				listItem.measure(0, 0);
////			
////				totalHeight += listItem.getMeasuredHeight()+8;
////				
////				if(maxHeight < listItem.getMeasuredHeight()){//implement new 
////					
////					maxHeight = listItem.getMeasuredHeight()+8;
////					System.out.println(" max height of gridview item<11>  "+size+"  ==  "+maxHeight);
////				}
////				
////				System.out.println(myListAdapter.getCount()+"  height of gridview item<22>  "+size+"  ==  "+listItem.getMeasuredHeight());
////			}
////			
////			//setting listview item in adapter
////			 ViewGroup.LayoutParams params = myGridView.getLayoutParams();
////		
////			if(rowCount > 1)
////				params.height = rowCount*maxHeight * ((myListAdapter.getCount()+1)/2);//implement new
//////				params.height = maxHeight * rowCount;// * ((myListAdapter.getCount()+1));//implement new
////			else
////				params.height = maxHeight;//implement new
//			
//			
//			//setting listview item in adapter
//			 ViewGroup.LayoutParams params = myGridView.getLayoutParams();
//		
//			if(rowCount > 1)
//				params.height = rowCount*160;
////				params.height = maxHeight * rowCount;// * ((myListAdapter.getCount()+1));//implement new
//			else
//				params.height = 160;//implement new
//			 
//			 myGridView.setLayoutParams(params);
//		
//		}
		
		
		public static void getGridViewSize(GridView myGridView,int rowcount) {
			ListAdapter myListAdapter = myGridView.getAdapter();
			if (myListAdapter == null) {
				//do nothing return null
				return;
			}
			//set listAdapter in loop for getting final size
			int totalHeight = 0;
			int maxHeight = 0;//implement new 
			for (int size = 0; size < myListAdapter.getCount(); size++) {
				View listItem = myListAdapter.getView(size, null, myGridView);
				
				listItem.measure(0, 0);
			
				totalHeight += listItem.getMeasuredHeight()+8;
				
				if(maxHeight < listItem.getMeasuredHeight()){//implement new 
					
					maxHeight = listItem.getMeasuredHeight()+8;
					System.out.println(" max height of gridview item<11>  "+size+"  ==  "+maxHeight);
				}
				
				System.out.println(myListAdapter.getCount()+"  height of gridview item<22>  "+size+"  ==  "+listItem.getMeasuredHeight());
			}
			
			//setting listview item in adapter
			 ViewGroup.LayoutParams params = myGridView.getLayoutParams();
		
			if(myListAdapter.getCount() > 1)
//				params.height = maxHeight * ((myListAdapter.getCount()+1)/2);//implement new
				params.height = maxHeight * rowcount;//((myListAdapter.getCount())/2);//implement new
			else
				params.height = maxHeight;//implement new
			 
			 myGridView.setLayoutParams(params);
		
		}

		
	}
