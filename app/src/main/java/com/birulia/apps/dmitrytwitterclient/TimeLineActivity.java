package com.birulia.apps.dmitrytwitterclient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.dmitrytwitterclient.R;
import com.apps.dmitrytwitterclient.databinding.ActivityTimeLineBinding;
import com.birulia.apps.dmitrytwitterclient.fragments.HomeTimelineFragment;
import com.birulia.apps.dmitrytwitterclient.fragments.MentionesTimelineFragment;
import com.birulia.apps.dmitrytwitterclient.fragments.PostTweetDialogFragment;
import com.birulia.apps.dmitrytwitterclient.fragments.TweetsListFragment;
import com.birulia.apps.dmitrytwitterclient.models.User;
import com.birulia.apps.dmitrytwitterclient.utils.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class TimeLineActivity extends AppCompatActivity {

    private TwitterClient client;
    private ActivityTimeLineBinding binding;
    private TweetsListFragment tweetsListFragment;
    private Toolbar toolbar;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_time_line);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_time_line);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = binding.actionBar;
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        client = TwitterApplication.getRestClient();

        // Get ViewPager
        ViewPager vpPager = binding.vpPager;
        // Set adapter
        TweetsPagerAdapter pagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(pagerAdapter);
        // find tabstrip
        TabLayout tabLayout = binding.tabs;
        // attach tabstrip to viewpager
        tabLayout.setupWithViewPager(vpPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(pagerAdapter.getTabView(i));
        }

        if (!isNetworkAvailable() || !isOnline()){
            Toast.makeText(this, "Please make sure that you are connected to Internet",
                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.twitter_client_header, menu);

        toolbar = binding.actionBar;
        tvTitle = (TextView) toolbar.getChildAt(0);
        tvTitle.setTextColor(Color.parseColor("#ffffff"));

        client.getMyProfile(new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    User myself = new User(response);
                                    tvTitle.setText(myself.getScreenName());
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    // log exception
                                    Log.w("TwitterApp", "failed to extract profile details");

                                }
                            }
        );

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi){

        int id = mi.getItemId();
        return super.onOptionsItemSelected(mi);
    }

    public void showPostTweetFragment(MenuItem mi){
        FragmentManager fm = getSupportFragmentManager();
        PostTweetDialogFragment tweetDialogFragment = PostTweetDialogFragment.newInstance("New tweet");
        tweetDialogFragment.setCancelable(false);
        tweetDialogFragment.show(fm, "fragment_post_tweet");
    }

    public void showUserProfile(MenuItem mi){
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    public void showMessages(MenuItem mi){
        Intent i = new Intent(this, MessagesActivity.class);
        startActivity(i);
    }

    // Return the order of fragments in ViewPager
    class TweetsPagerAdapter extends FragmentStatePagerAdapter {
        private String[] tabTitles = {"Home", "Mentiones"};
        private int tabIcons[] = {R.drawable.home, R.drawable.mention};

        public TweetsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position ==0){
                return new HomeTimelineFragment();
            }
            else if (position == 1){
                return new MentionesTimelineFragment();
            }
            else{
                return null;
            }
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) v.findViewById(R.id.tvTabName);
            tv.setText(tabTitles[position]);
            ImageView img = (ImageView) v.findViewById(R.id.ivTabIcon);
            img.setImageResource(tabIcons[position]);
            return v;
        }

    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }
}
