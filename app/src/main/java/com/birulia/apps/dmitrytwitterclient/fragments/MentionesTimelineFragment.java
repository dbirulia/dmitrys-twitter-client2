package com.birulia.apps.dmitrytwitterclient.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.birulia.apps.dmitrytwitterclient.TwitterApplication;
import com.birulia.apps.dmitrytwitterclient.models.Tweet;
import com.birulia.apps.dmitrytwitterclient.utils.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MentionesTimelineFragment extends TweetsListFragment {

    private TwitterClient client;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Add the scroll listener
        populateTimeline();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void populateTimeline() {

        client.getMentionesTimeLine(max_id, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                addAll(Tweet.fromJSONArray(response));
                updateMaxId(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // log exception
                Log.w("TwitterApp", "failed to retrieve tweets");
            }
        });
    }
}
