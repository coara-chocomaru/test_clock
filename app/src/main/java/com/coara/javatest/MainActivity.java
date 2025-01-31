package com.coara.javatest;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // WebViewの設定
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        // WebView内でURLを読み込む
        webView.loadUrl("file:///android_asset/index.html");

        // JavaScriptの動作を許可
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        // エラーハンドリング
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(MainActivity.this, "エラーが発生しました", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 戻るボタンを押した時にWebViewが戻る動作をするように設定
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
