package com.wsb.wsbtracker.wsbtracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.net.CookieManager;

/**
 * Created by jitus_000 on 12/05/2016.
 */
public class SignInWebViewActivity extends Activity{
    private static String EXTRA_REDIRECTED_URL = "com.wsb.wsbtracker.wsbtracker.redirected.url";
    private String mRedirectedUrlString;

    public static Intent newIntene(Context packageContext, String url){
        Intent intent = new Intent(packageContext,SignInWebViewActivity.class);
        intent.putExtra(EXTRA_REDIRECTED_URL,url);
        return intent;
    }


    private WebView mWebView;
    private Button mGetCookieButton;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_signin);
        mWebView=(WebView)findViewById(R.id.webView);
        mGetCookieButton = (Button)findViewById(R.id.getCookie);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mRedirectedUrlString = getIntent().getStringExtra(EXTRA_REDIRECTED_URL);
        mWebView.setWebViewClient(new MyBrowser());
        mWebView.loadUrl(mRedirectedUrlString);
        mGetCookieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
