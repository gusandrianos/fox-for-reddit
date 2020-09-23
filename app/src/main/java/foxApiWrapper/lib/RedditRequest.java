package foxApiWrapper.lib;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

import io.github.gusandrianos.foxforreddit.R;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RedditRequest extends AsyncTask<String, Void, String> {

    OkHttpClient client = new OkHttpClient();
    String BEARER = "65888787-I43XH7Q1oiAhNbK9rERrXd1WFac";
    // TODO: This ^^^ is stupid, needs to come from sharePrefs or something

    @Override
    protected String doInBackground(String... strings) {
        if (strings.length == 0) {
            return "No data passed";
        } else {
            String url = strings[0];
            String access_token_url = "https://www.reddit.com/api/v1/access_token";
            Request request;

            if (url.equals(access_token_url)) {
                String redirect_uri = "https://gusandrianos.github.io/login";
                String authString = strings[1];
                String encodedAuthString = Base64.encodeToString(authString.getBytes(),
                        Base64.NO_WRAP);

                RequestBody formBody = new FormBody.Builder()
                        .add("redirect_uri", redirect_uri)
                        .add("grant_type", "authorization_code")
                        .add("code", strings[2])
                        .build();

                request = createLoginRequest(url, encodedAuthString, formBody);
            } else {
                request = createAuthorizedRequest(url);
            }

            Log.i("requesturl", request.toString());

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("token", s);
    }

    private Request createAuthorizedRequest(String url) {
        return new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "thesis_andrianos_dalezios")
                .addHeader("Authorization", " Bearer " + BEARER)
                .build();
    }

    private Request createLoginRequest(String url, String encodedAuthString, RequestBody formBody) {
        return new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "thesis_andrianos_dalezios")
                .addHeader("Authorization", "Basic " + encodedAuthString)
                .post(formBody)
                .build();
    }
}
