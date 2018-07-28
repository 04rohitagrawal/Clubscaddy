package com.clubscaddy.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.R;
import com.clubscaddy.TermandconditionActivity;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;

/**
 * Created by administrator on 27/3/18.
 */

public class ReservationWebView extends Fragment
{
    WebView webview;
    ProgressDialog pd;
    View convertView ;
    SessionManager sessionManager ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        convertView = inflater.inflate(R.layout.reservation_web_view , null);

        sessionManager = new SessionManager(getActivity());

        webview = (WebView) convertView.findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        DirectorFragmentManageActivity.updateTitle(sessionManager.getSportFiledName(getActivity()) + " Reservation");

        if (DirectorFragmentManageActivity.backButton != null) {
            DirectorFragmentManageActivity.backButton.setOnClickListener(addToBack);
            DirectorFragmentManageActivity.showBackButton();
        }
        webSettings.setJavaScriptEnabled(true);
        webview.requestFocusFromTouch();

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);

                pd = new ProgressDialog(getActivity());
                pd.setCancelable(false);
                pd.setMessage("Page Loading....");
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
                pd.dismiss();
            }
        });

        webview.loadUrl(sessionManager.getReservationLink());

        return convertView;
    }
    View.OnClickListener addToBack = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                DirectorFragmentManageActivity.popBackStackFragment();
            } catch (Exception e) {
                ShowUserMessage.showUserMessage(getActivity(), e.toString());
            }
        }
    };
}
