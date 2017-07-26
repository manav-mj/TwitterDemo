package in.codingninjas.twitterdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.util.ArrayList;

import in.codingninjas.twitterdemo.network.ApiClient;
import in.codingninjas.twitterdemo.network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartingActivity extends AppCompatActivity implements CustomAdapter.OnTweetClickListener {

    ListView listView;
    private CustomAdapter customAdapter;
    private UserTimeline userTimeline;
    private TweetTimelineListAdapter defaultTwitterAdapter;

    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        showProgress(true);

        defaultTwitterAdapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .build();

        getHomeTimeline();

        // custom adapter made to handle onClick on each tweet view
//        customAdapter = new CustomAdapter(this, userTimeline, this);

        listView = (ListView) findViewById(R.id.list);

//        listView.setAdapter(customAdapter);


    }

    private void getHomeTimeline() {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<ArrayList<Tweet>> call = apiInterface.getHomeTimeline();
        call.enqueue(new Callback<ArrayList<Tweet>>() {
            @Override
            public void onResponse(Call<ArrayList<Tweet>> call, Response<ArrayList<Tweet>> response) {

                FixedTweetTimeline homeTimeline = new FixedTweetTimeline.Builder()
                        .setTweets(response.body())
                        .build();

                customAdapter = new CustomAdapter(StartingActivity.this, homeTimeline, StartingActivity.this);
                listView.setAdapter(customAdapter);
                showProgress(false);
            }

            @Override
            public void onFailure(Call<ArrayList<Tweet>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onTweetClicked(int position, Tweet tweet) {
        Toast.makeText(this, tweet.text, Toast.LENGTH_SHORT).show();
    }

    public void logout(View view) {
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void showProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
