package io.github.gusandrianos.foxforreddit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.github.gusandrianos.foxforreddit.R;

import static io.github.gusandrianos.foxforreddit.Constants.REDIRECT_URI;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent load = getIntent();
        String url = load.getStringExtra("URL");

        WebView webViewView = findViewById(R.id.webview);
        FoxWebViewClient webViewClient = new FoxWebViewClient();
        webViewView.setWebViewClient(webViewClient);
        webViewView.loadUrl(url);
    }

    class FoxWebViewClient extends WebViewClient {
        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
            Log.i("shouldOverrideUrlL", url);
            if (url.startsWith(REDIRECT_URI)) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", url);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }
}