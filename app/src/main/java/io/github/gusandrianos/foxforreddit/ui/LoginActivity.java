package io.github.gusandrianos.foxforreddit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.github.gusandrianos.foxforreddit.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent load = getIntent();
        String url = load.getStringExtra("URL");

        class MyWebViewClient extends WebViewClient {

            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);
                Log.i("shouldOverrideUrlL", url);
                if (url.startsWith("https://gusandrianos.github.io/login")) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", url);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        }

        WebView mywev = findViewById(R.id.webview);
        MyWebViewClient mywebc = new MyWebViewClient();
        mywev.setWebViewClient(mywebc);
        mywev.loadUrl(url);

    }
}