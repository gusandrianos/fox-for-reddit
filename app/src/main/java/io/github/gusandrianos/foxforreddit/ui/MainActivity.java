package io.github.gusandrianos.foxforreddit.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import foxApiWrapper.lib.RedditRequest;
import io.github.gusandrianos.foxforreddit.R;

public class MainActivity extends AppCompatActivity {

    TextView bt;
    int LAUNCH_SECOND_ACTIVITY = 1;
    String authString = "n1R0bc_lPPTtVg" + ":";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loadWebPage(View view) {
        Intent load = new Intent(this, Main2Activity.class);
        load.putExtra("URL", "https://www.reddit.com/api/v1/authorize.compact?client_id=n1R0bc_lPPTtVg&response_type=code&state=ggfgfgfgga&redirect_uri=https://gusandrianos.github.io/login&duration=temporary&scope=identity,mysubreddits");
        startActivityForResult(load, LAUNCH_SECOND_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                String[] inputs = result.split("\\?")[1].split("&");
                String code = inputs[1].split("=")[1];
                String state = inputs[0].split("=")[1];
                Toast.makeText(this, state + " " + code, Toast.LENGTH_LONG).show();

                loadWebPage(code);
            }
        }
    }

    void loadWebPage(String c) {
        RedditRequest okHttpHandler = new RedditRequest();
        okHttpHandler.execute("https://www.reddit.com/api/v1/access_token", authString, c);
    }

    public void me(View view) {
        RedditRequest okHttpHandler = new RedditRequest();
        okHttpHandler.execute("https://oauth.reddit.com/api/v1/me/karma");
    }
}
