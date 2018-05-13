package com.example.asus.vca;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CalendarActivity extends AppCompatActivity {

    String device = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_game);
        WebView webview = (WebView) findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl("file:///android_asset/calendar/index.html");
//        webview.loadUrl("javascript:setUser('TheValue')");

        String manufacturer = Build.MANUFACTURER;
        device = Build.MODEL;
        device = (manufacturer) + "-" + device;
        device = device.toUpperCase();
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        device = device + "-" + android_id;

        webview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                //Here you want to use .loadUrl again
                //on the webview object and pass in
                //"javascript:<your javaScript function"
                view.loadUrl("javascript:setUser('" + device.toString() + "')");
            }
        });
    }

}
