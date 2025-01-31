package com.coara.javatest;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceError;
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

        // APIレベルによるエラーハンドリング
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                // APIレベル23以上の場合
                super.onReceivedError(view, request, error);
                Toast.makeText(MainActivity.this, "エラーが発生しました: " + error.getDescription(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // APIレベル22以下の場合
                super.onReceivedError(view, errorCode, description, failingUrl);
                Toast.makeText(MainActivity.this, "エラーが発生しました: " + description, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 戻るボタンを無効にする
    @Override
    public void onBackPressed() {
        // 戻るボタンが押された時に何もしない
        // WebViewが戻る動作をしないようにする
        // super.onBackPressed(); は呼び出さない
    }
}
