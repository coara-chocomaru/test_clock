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

        // JavaScriptを有効にする
        webView.getSettings().setJavaScriptEnabled(true);

        // キャッシュ設定を無効にする
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); // キャッシュなしで常にネットワークから読み込む
        webView.getSettings().setAppCacheEnabled(false);  // アプリのキャッシュを無効化

        // WebView内でURLを読み込む
        webView.loadUrl("file:///android_asset/index.html");

        // WebViewのエラーを処理
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

        // WebChromeClientを設定（オプション）
        webView.setWebChromeClient(new WebChromeClient());
    }

    // 戻るボタンを無効にする
    @Override
    public void onBackPressed() {
        // 戻るボタンが押された時に何もしない
        // WebViewが戻る動作をしないようにする
        // super.onBackPressed(); は呼び出さない
    }
}
