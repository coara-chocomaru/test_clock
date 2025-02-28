package com.coara.javatest;

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceError;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.widget.Toast;
import android.webkit.CookieManager;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    
        webView = findViewById(R.id.webView);

        
        webView.getSettings().setJavaScriptEnabled(true);

    
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  // キャッシュを無効化


    
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } 
        

        
        webView.getSettings().setDatabaseEnabled(false); 
        webView.getSettings().setDomStorageEnabled(false); 

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(MainActivity.this, "エラーが発生しました: " + error.getDescription(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Toast.makeText(MainActivity.this, "エラーが発生しました: " + description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("file://")) {
                    view.loadUrl(url);
                    return true;
                } else {
                    return false;
                }
            }
        });

        webView.loadUrl("file:///android_asset/index.html"); 
    }

    @Override
    public void onBackPressed() {
    }
}
