package com.vivo.chmusicdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.vivo.chmusicdemo.R;

import androidx.appcompat.app.AppCompatActivity;

public class BlogContentActivity extends AppCompatActivity {
    private static final String TAG = "BlogContentActivity";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogcontent);
        Log.d(TAG, "onCreate");
        Intent intent = getIntent();
        String url = intent.getStringExtra("blogcontent");
//        Log.d(TAG, url);
        WebView webView = (WebView)findViewById(R.id.webview_blogcontent);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        //图片不适配，不好看
        webView.loadData(url, "text/html", null);
    }
}
