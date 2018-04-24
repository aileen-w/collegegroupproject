package com.example.asus.vca;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
        device = device + "-"+android_id;

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
