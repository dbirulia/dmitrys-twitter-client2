package com.birulia.apps.dmitrytwitterclient.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.dmitrytwitterclient.R;
import com.birulia.apps.dmitrytwitterclient.ProfileActivity;
import com.birulia.apps.dmitrytwitterclient.TimeLineActivity;
import com.birulia.apps.dmitrytwitterclient.TwitterApplication;
import com.birulia.apps.dmitrytwitterclient.fragments.PostTweetDialogFragment;
import com.birulia.apps.dmitrytwitterclient.models.Tweet;
import com.birulia.apps.dmitrytwitterclient.models.User;
import com.birulia.apps.dmitrytwitterclient.utils.TwitterClient;
import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class TweetsArrayAdapter extends RecyclerView.Adapter<TweetsArrayAdapter.ViewHolder> {

    // Store a member variable for the contacts
    private List<Tweet> tweets;
    // Store the context for easy access
    private Context tweetContext;

    private TwitterClient client;

    // Pass in the contact array into the constructor
    public TweetsArrayAdapter(Context context, List<Tweet> _tweets) {
        tweets = _tweets;
        tweetContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return tweetContext;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView ivProfileImage;
        public TextView tvBody;
        public TextView tvCreatedAt;
        public TextView tvUsername;
        public String imgURL;
        public ImageView ivLiked;
        public ImageView ivRetweeted;
        public ImageView ivReply;
        public TextView tvLikesCount;
        public TextView tvRetweetcCount;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvCreatedAt = (TextView) itemView.findViewById(R.id.tvCreatedAt);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvLikesCount = (TextView) itemView.findViewById(R.id.tvLikesCount);
            tvRetweetcCount = (TextView) itemView.findViewById(R.id.tvRetweetcCount);
            ivLiked = (ImageView) itemView.findViewById(R.id.ivLike);
            ivReply = (ImageView) itemView.findViewById(R.id.ivReply);
            imgURL = "";
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            // Show Fragment with all hte details
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View tweetView = inflater.inflate(R.layout.tweet_item, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final Tweet tweet = tweets.get(position);

        // Set item views based on your views and data model
        TextView tvBody = viewHolder.tvBody;
        TextView tvCreatedAt = viewHolder.tvCreatedAt;
        TextView tvUsername = viewHolder.tvUsername;
        final TextView tvLikesCount = viewHolder.tvLikesCount;
        final TextView tvRetweetcCount = viewHolder.tvRetweetcCount;
        final ImageView ivLiked = viewHolder.ivLiked;
        final ImageView ivReply = viewHolder.ivReply;

        tvBody.setText(tweet.getBody());
        tvCreatedAt.setText(tweet.getCreatedAt());
        tvUsername.setText(tweet.getUser().getScreenName());
        tvLikesCount.setText(tweet.getLikesCount().toString());
        tvRetweetcCount.setText(tweet.getRetweetCount().toString());
        if (tweet.getLiked()){
            Glide.with(getContext())
                    .load(R.drawable.liked)
                    .into(ivLiked);
        }

        ImageView ivProfileImage = viewHolder.ivProfileImage;
        String imageUrl = tweet.getUser().getProfileImageURL();
        Glide.with(getContext())
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(getContext(), 5, 2))
                .error(R.drawable.empty_profile)
                .placeholder(R.drawable.empty_profile)
                .into(ivProfileImage);

        if (client == null){
            client = TwitterApplication.getRestClient();
        }

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = tweet.getUser();
                Intent i = new Intent(tweetContext, ProfileActivity.class);
                i.putExtra("user", Parcels.wrap(user));
                tweetContext.startActivity(i);
            }
        });

        ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch compose tweet fragment and pass username
                User user = tweet.getUser();
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", user);

                FragmentManager fm = ((TimeLineActivity) getContext()).getSupportFragmentManager();
                PostTweetDialogFragment tweetDialogFragment = PostTweetDialogFragment.newInstance("New tweet");
                tweetDialogFragment.setArguments(bundle);
                tweetDialogFragment.setCancelable(false);
                tweetDialogFragment.show(fm, "fragment_post_tweet");
            }
        });

        ivLiked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tweet.getLiked()) {
                    client.unLikeTweet(tweet.getId(), new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            tweet.setLiked(false);
                            tweet.setLikesCount(tweet.getLikesCount()-1);
                            tvLikesCount.setText(tweet.getLikesCount().toString());
                            Glide.with(getContext())
                                    .load(R.drawable.like)
                                    .into(ivLiked);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                        }
                    });
                }
                else {
                    client.likeTweet(tweet.getId(), new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            tweet.setLiked(true);
                            tweet.setLikesCount(tweet.getLikesCount()+1);
                            tvLikesCount.setText(tweet.getLikesCount().toString());
                            Glide.with(getContext())
                                    .load(R.drawable.liked)
                                    .into(ivLiked);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

}
