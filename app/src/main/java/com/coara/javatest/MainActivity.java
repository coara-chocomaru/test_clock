package com.coara.javatest;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceError;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
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

        // 完全オフライン環境用の設定
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);  // キャッシュをオフライン用にロード
        webView.getSettings().setAppCacheEnabled(false);  // アプリキャッシュ無効化
        webView.getSettings().setDatabaseEnabled(false);  // データベース無効化
        webView.getSettings().setDomStorageEnabled(false);  // DOMストレージ無効化

        // インターネットアクセスを完全に遮断する
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
                // ローカルHTMLファイルにリンクがある場合のみ読み込む
                if (url.startsWith("file://")) {
                    view.loadUrl(url);
                    return true;
                } else {
                    // 外部URLへのアクセスは無効化
                    return false;
                }
            }
        });

        // ローカルのHTMLファイルを読み込む
        webView.loadUrl("file:///android_asset/index.html");  // オフラインでローカルHTMLを表示
    }

    // 戻るボタンを無効にする
    @Override
    public void onBackPressed() {
        // 戻るボタンが押された時にWebViewが履歴を戻らないようにする
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();  // 履歴がない場合は通常の戻る処理
        }
    }
}
