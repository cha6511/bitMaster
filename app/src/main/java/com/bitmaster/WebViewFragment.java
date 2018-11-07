package com.bitmaster;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.bitmaster.EventBus.Events;
import com.bitmaster.EventBus.GlobalBus;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment implements MyWebChromeClient.ProgressListener {

    public static WebViewFragment webViewFragment;

    public static WebViewFragment getInstance() {
        if (webViewFragment == null) {
            webViewFragment = new WebViewFragment();
        }
        return webViewFragment;
    }

    public WebViewFragment() {
        // Required empty public constructor
    }

    WebView webView;
    FloatingActionButton fab;
    View blind;
    ProgressBar progress;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_web_view, container, false);

        progress = v.findViewById(R.id.progress);
        blind = v.findViewById(R.id.blind);

        fab = v.findViewById(R.id.home);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl("about:blank");
                Events.BackToMain backToMain = new Events.BackToMain("goback");
                GlobalBus.getBus().post(backToMain);
            }
        });

        webView = v.findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new MyWebChromeClient(this));
        webView.setWebViewClient(new WebViewClientClass());
        webView.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN)
                    return true;
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        webView.loadUrl("about:blank");
                        Events.BackToMain backToMain = new Events.BackToMain("goback");
                        GlobalBus.getBus().post(backToMain);
                    }
                    return true;
                }
                return false;
            }
        });
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMsg(Events.ControlWebView controlWebView) {
        Log.d("webViewFragment", controlWebView.getUrl());
        webView.loadUrl(controlWebView.getUrl());
    }

    @Override
    public void onUpdateProgress(int progressValue) {
        progress.setProgress(progressValue);
        if (progressValue == 100) {
            progress.setVisibility(View.INVISIBLE);
            blind.setVisibility(View.INVISIBLE);
        }
    }


    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progress.setVisibility(View.VISIBLE);
            blind.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progress.setVisibility(View.GONE);
            blind.setVisibility(View.GONE);
            if(url.contains("about:blank")){
                Events.BackToMain backToMain = new Events.BackToMain("goback");
                GlobalBus.getBus().post(backToMain);
            }
        }
    }


}
