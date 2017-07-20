package in.codingninjas.twitterdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

public class TweetViewActivity extends AppCompatActivity {

    private LinearLayout tweetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_view);

        long id = getIntent().getLongExtra("id",-1);

        tweetView = (LinearLayout) findViewById(R.id.main_layout);

        TweetUtils.loadTweet(id, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                TweetView tv = new TweetView(TweetViewActivity.this, result.data);
                tweetView.addView(tv);
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });

    }
}
