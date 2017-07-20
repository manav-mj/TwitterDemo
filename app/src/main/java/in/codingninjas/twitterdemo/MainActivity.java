package in.codingninjas.twitterdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.twitter.sdk.android.core.AuthToken;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.OAuthSigning;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.HashMap;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {
    TwitterLoginButton mLoginButton;

    // constants for shared preferences
    public final static String TOKEN = "token";
    public final static String TOKEN_SECRET = "secret";
    public final static String USER_NAME = "user_name";
    public final static String USER_ID = "user_id";
    public final static String SP_NAME = "twitter";

    String token, tokenSecret, userName;
    long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Twitter.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        token = sp.getString(TOKEN, null);

        if (token != null) {

            tokenSecret = sp.getString(TOKEN_SECRET, null);
            userName = sp.getString(USER_NAME, null);
            userId = sp.getLong(USER_ID, -1);

            // set active session with stored token and token_secret
            setActiveSession();

            // skip login and start new activity
            startNewActivity();
        }

        mLoginButton = (TwitterLoginButton) findViewById(R.id.login);

        mLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                // store all the data and start new activity
                TwitterSession session = result.data;
                TwitterAuthToken authToken = session.getAuthToken();

                SharedPreferences sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putLong(USER_ID, session.getUserId());
                editor.putString(USER_NAME, session.getUserName());
                editor.putString(TOKEN, authToken.token);
                editor.putString(TOKEN_SECRET, authToken.secret);
                editor.commit();

                startNewActivity();
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });

//            TwitterAuthConfig authConfig = TwitterCore.getInstance().getAuthConfig();
//            TwitterAuthToken authToken = session.getAuthToken();
//
//            TwitterAuthToken token = TwitterCore.getInstance().getSessionManager().getActiveSession().getAuthToken();
//
//            OAuthSigning authSigning = new OAuthSigning(authConfig, authToken);
//
//            HashMap<String, String> params = new HashMap<>();
//            params.put("id", "23424848");
//
//            String header = authSigning.getAuthorizationHeader("GET", "https://api.twitter.com/1.1/trends/place.json", params);

    }

    private void startNewActivity() {
        startActivity(new Intent(MainActivity.this, StartingActivity.class));
        finish();
    }

    private void setActiveSession() {
        TwitterAuthToken authToken = new TwitterAuthToken(token, tokenSecret);
        TwitterSession session = new TwitterSession(authToken, userId, userName);
        TwitterCore.getInstance().getSessionManager().setActiveSession(session);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mLoginButton.onActivityResult(requestCode, resultCode, data);
    }
}
