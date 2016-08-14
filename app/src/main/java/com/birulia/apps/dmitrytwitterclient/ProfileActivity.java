package com.birulia.apps.dmitrytwitterclient;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.dmitrytwitterclient.R;
import com.birulia.apps.dmitrytwitterclient.fragments.UserTimelineFragment;
import com.birulia.apps.dmitrytwitterclient.models.User;
import com.birulia.apps.dmitrytwitterclient.utils.TwitterClient;
import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.tvProfileName) TextView tvProfileName;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.ivProfileBackground) ImageView ivProfileBackground;
    @BindView(R.id.ivProfileAvatar) ImageView ivProfileAvatar;
    @BindView(R.id.tvLocation) TextView tvLocation;
    @BindView(R.id.tvFollowersCount) TextView tvFollowersCount;
    @BindView(R.id.tvFollowingCount) TextView tvFollowingCount;

    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        client = TwitterApplication.getRestClient();
        String screenName = "";
        User user = (User) Parcels.unwrap(getIntent().getParcelableExtra("user"));
        if (user != null){
            screenName = user.getScreenName().substring(1);
        }
        client.getUserProfile(screenName, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    User user = new User(response);
                                    populateUserDetails(user);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    // log exception
                                    Log.w("TwitterApp", "failed to extract profile details");

                                }
                            }
        );

        UserTimelineFragment fragmentUserProfile = UserTimelineFragment.newInstance(screenName);
        if (savedInstanceState == null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserProfile);
            ft.commit();
        }
    }

    private void populateUserDetails(User user){
        tvUserName.setText(user.getScreenName());
        tvProfileName.setText(user.getName());
        tvLocation.setText(user.getLocation());


        tvFollowersCount.setText(humanizeNumber(user.getFollowersCount()));
        tvFollowingCount.setText(humanizeNumber(user.getFollowingCount()));

        Glide.with(getApplicationContext())
                .load(user.getProfileImageURL())
                .bitmapTransform(new RoundedCornersTransformation(getApplicationContext(), 5, 2))
                .error(R.drawable.empty_profile)
                .placeholder(R.drawable.empty_profile)
                .into(ivProfileAvatar);

        if (user.getProfileBackgroundColor() != ""){
            ivProfileBackground.setBackgroundColor(Color.parseColor("#"+user.getProfileBackgroundColor()));
        }

        if (user.getProfileBackgroundImageUrl() != ""){
            Glide.with(getApplicationContext())
                    .load(user.getProfileBackgroundImageUrl())
                    .centerCrop()
                    .into(ivProfileBackground);
        }
    }

    private String humanizeNumber(Integer i){
        DecimalFormat df = new DecimalFormat("###.#");
        if (i > 1000000){
            return df.format(i/1000000) + "M";
        }
        else if (i > 1000){
            return df.format(i/1000) + "K";
        }
        else
        {
            return i.toString();
        }
    }
}
