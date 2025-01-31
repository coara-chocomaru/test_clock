package com.coara.javatest;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private Button toggleButton;
    private boolean is24Hour = false; // 24時間表示か12時間表示かを切り替えるフラグ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        toggleButton = findViewById(R.id.toggleButton);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/index.html");

        // 12/24時間切り替えボタン
        toggleButton.setOnClickListener(v -> {
            is24Hour = !is24Hour;
            webView.evaluateJavascript("changeTimeFormat(" + is24Hour + ");", null);
        });
    }
}
