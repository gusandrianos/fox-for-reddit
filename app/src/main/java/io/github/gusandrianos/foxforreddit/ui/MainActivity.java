package io.github.gusandrianos.foxforreddit.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import foxApiWrapper.lib.RedditRequest;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Post;
import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.PopularFragmentViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.PopularFragmentViewModelFactory;
import io.github.gusandrianos.foxforreddit.viewmodels.TokenViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.TokenViewModelFactory;

public class MainActivity extends AppCompatActivity {

    TextView result;
    Token mToken;
    int LAUNCH_SECOND_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        TokenViewModelFactory factory = InjectorUtils.getInstance().provideTokenViewModelFactory();
//        TokenViewModel viewModel = new ViewModelProvider(this, factory).get(TokenViewModel.class);
//        viewModel.getToken().observe(this, new Observer<Token>() {
//            @Override
//            public void onChanged(Token token) {
//                mToken = token;
//                initializeUI();
//            }
//        });

        MainActivity.super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.result);
    }

    private void initializeUI() {
        PopularFragmentViewModelFactory factory = InjectorUtils.getInstance().providePopularFragmentViewModelFactory();
        PopularFragmentViewModel viewModel = new ViewModelProvider(this, factory).get(PopularFragmentViewModel.class);
        viewModel.getPosts(mToken).observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {

                result.setMovementMethod(new ScrollingMovementMethod());
                result.setText("");

                for (Post post : posts) {
                    result.append("r/" + post.getSubreddit() + "\n");
                    result.append("Posted by u/" + post.getAuthor() + "\n");
                    result.append(post.getTitle() + "\n");
                    result.append(post.isSelf() + "\n");
                    String date = (String) android.text.format.DateUtils.getRelativeTimeSpanString(post.getCreatedUtc() * 1000);
                    result.append(date + "\n");
                    result.append(post.getScore() + "\n\n");
                }
            }
        });
    }

    public void loadWebPage(View view) {
        Intent load = new Intent(this, Main2Activity.class);
        load.putExtra("URL", "https://www.reddit.com/api/v1/authorize.compact?client_id=n1R0bc_lPPTtVg&response_type=code&state=ggfgfgfgga&redirect_uri=https://gusandrianos.github.io/login&duration=permanent&scope=identity,edit,flair,history,mysubreddits,privatemessages,read,report,save,submit,subscribe,vote,wikiread");
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

                getAuthorizedUserToken(code);
            }
        }
    }

    void getAuthorizedUserToken(String c) {
        TokenViewModelFactory factory = InjectorUtils.getInstance().provideTokenViewModelFactory();
        TokenViewModel viewModel = new ViewModelProvider(this, factory).get(TokenViewModel.class);
        viewModel.getToken(c, "https://gusandrianos.github.io/login").observe(this, new Observer<Token>() {
            @Override
            public void onChanged(Token token) {
                mToken = token;
                initializeUI();
            }
        });
    }

    public void me(View view) {
        RedditRequest okHttpHandler = new RedditRequest();
        okHttpHandler.execute("https://oauth.reddit.com/api/v1/me/karma");
    }
}
