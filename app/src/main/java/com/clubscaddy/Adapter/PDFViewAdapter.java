package com.clubscaddy.Adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.clubscaddy.custumview.ExtendedWebView;

import com.clubscaddy.R;

/**
 * @author administrator for Home screen(splash) screen
 */
public class PDFViewAdapter extends PagerAdapter {
	// public static final int
	// Imagess[]={R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher};


	Context mContext;
	ArrayList<String> newsPath;
	private Activity _activity;
	// private ArrayList<String> _imagePaths;
	private LayoutInflater inflater;

	// constructor
	public PDFViewAdapter(Activity activity, Context context, ArrayList<String> path) {
		this.mContext = context;
		this._activity = activity;
		// this.image_load.init(ImageLoaderConfiguration.createDefault(mContext));
		this.newsPath = path;
		// this._imagePaths = imagePaths;
	}

	@Override
	public int getCount() {
		return  newsPath.size();

		// return this._imagePaths.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}

	public static final String googleDoc = "http://docs.google.com/gview?embedded=true&url=";
	public static final String tempStr = "http://www.adobe.com/content/dam/Adobe/en/devnet/acrobat/pdfs/pdf_open_parameters.pdf";
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TouchImageView imgDisplay;
		ExtendedWebView webView;
		ProgressBar progress;
		// Button btnClose;

		inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewLayout = inflater.inflate(R.layout.pdf_item, container, false);

		webView = (ExtendedWebView) viewLayout.findViewById(R.id.webView);
		progress = (ProgressBar) viewLayout.findViewById(R.id.prog);

		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		
		webView.loadUrl(googleDoc+newsPath.get(position));
	//	webView.loadUrl(googleDoc+tempStr);

		((ViewPager) container).addView(viewLayout);
		return viewLayout;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {

	}
}
