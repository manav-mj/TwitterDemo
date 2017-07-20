package in.codingninjas.twitterdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class StartingActivity extends AppCompatActivity implements CustomAdapter.OnTweetClickListener {

    RecyclerView recyclerView;
    ListView listView;
    private CustomAdapter customAdapter;
    private UserTimeline userTimeline;
    private TweetTimelineListAdapter defaultTwitterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        userTimeline = new UserTimeline.Builder()
                .screenName(TwitterCore.getInstance().getSessionManager().getActiveSession().getUserName())
                .build();

        defaultTwitterAdapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .build();

        // custom adapter made to handle onClick on each tweet view
        customAdapter = new CustomAdapter(this, userTimeline, this);

        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(customAdapter);
    }

    @Override
    public void onTweetClicked(int position, Tweet tweet) {
        Toast.makeText(this, tweet.text, Toast.LENGTH_SHORT).show();
    }
}
