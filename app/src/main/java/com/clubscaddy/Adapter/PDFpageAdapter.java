package com.clubscaddy.Adapter;

import java.util.ArrayList;

import com.clubscaddy.R;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

@SuppressLint("SetJavaScriptEnabled")
public class PDFpageAdapter extends PagerAdapter {
 
    Context mContext;
    ProgressDialog pd ;
    LayoutInflater mLayoutInflater;
   ArrayList<String>path_list ;
    public PDFpageAdapter(Context context , ArrayList<String>path_list) {
    	this.path_list = path_list;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    @Override
    public int getCount() {
        return path_list.size();
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
 
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pdf_item, container, false);
 
      WebView listview = (WebView) itemView.findViewById(R.id.webView);
      listview.getSettings().setJavaScriptEnabled(true);
      listview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
      String doc="<iframe src='http://docs.google.com/gview?embedded=true&url="+path_list.get(position)+"' width='100%' height='100%' style='border: none;'></iframe>";
      
      
      listview.getSettings().setAllowFileAccess(true);
      listview.loadData( doc , "text/html", "UTF-8");
      listview.setWebViewClient(new WebViewClient()
    		  {
    	  public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
    		pd = new ProgressDialog(mContext);
    		pd.show();
    	  }
    	  public void onPageFinished(WebView view, String url) {
    		 pd.dismiss(); 
    	  };
    		  }
    		  );
      
        container.addView(itemView);
 
        return itemView;
    }
 
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
