package com.birulia.apps.dmitrytwitterclient.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.birulia.apps.dmitrytwitterclient.adapters.TweetsArrayAdapter;
import com.birulia.apps.dmitrytwitterclient.models.Tweet;
import com.apps.dmitrytwitterclient.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TweetsListFragment extends Fragment {

    private TweetsArrayAdapter tweetsAdapter;
    private ArrayList<Tweet> tweets;
    private RecyclerView rvTweets;
    public long max_id = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweets);
        // Attach the adapter to the recyclerview to populate items
        rvTweets.setAdapter(tweetsAdapter);
        // Set layout manager to position the items
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        // Attach the layout manager to the recycler view
        rvTweets.setHasFixedSize(true);
        rvTweets.setLayoutManager(layoutManager);

        getList().addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                populateTimeline();
            }
        });

        return v;
    }

    public void populateTimeline(){

    }

    public void addAll(List<Tweet> _tweets){
        tweets.addAll(_tweets);
        tweetsAdapter.notifyDataSetChanged();
    }

    public void deleteAll(){
        tweets.clear();
        tweetsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsArrayAdapter(getActivity(), tweets);
        super.onCreate(savedInstanceState);
    }

    public RecyclerView getList(){
        return rvTweets;
    }

    public void updateMaxId(JSONArray jsonTweets){

        for (int i = 0; i < jsonTweets.length(); i++) {
            try{
                JSONObject t = jsonTweets.getJSONObject(i);
                if (t.getLong("id") < max_id){
                    max_id = t.getLong("id");
                }
            }
            catch (JSONException ex){
                ex.printStackTrace();
            }
        }
    }

}
