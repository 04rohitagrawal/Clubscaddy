package com.clubscaddy.fragment;

import java.util.ArrayList;

import com.clubscaddy.R;
import com.clubscaddy.Adapter.PDFpageAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class ShowPdfActivity extends Fragment
{
   View convertView ;
   PDFpageAdapter adapter ;
   ViewPager pdf_list_view_pager;
   ArrayList<String>pdf_list ;
   
      public void setInstanse(ArrayList<String>pdf_list)
      {
    	this.pdf_list = pdf_list;  
      }
   
   
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		convertView = inflater.inflate(R.layout.pdf_show_layout, null);
		//pdf_list_view_pager = (ViewPager) convertView.findViewById(R.id.pdf_list_viewpager);
		Toast.makeText(getActivity(),"pdf file list size"+ pdf_list.size(), 1).show();
		
		adapter = new PDFpageAdapter(getActivity(), this.pdf_list);
		pdf_list_view_pager.setAdapter(adapter);
		return convertView;
	}

}
