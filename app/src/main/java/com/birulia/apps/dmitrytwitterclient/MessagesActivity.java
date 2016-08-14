package com.birulia.apps.dmitrytwitterclient;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.apps.dmitrytwitterclient.R;
import com.birulia.apps.dmitrytwitterclient.adapters.MessagesArrayAdapter;
import com.birulia.apps.dmitrytwitterclient.fragments.EndlessRecyclerViewScrollListener;
import com.birulia.apps.dmitrytwitterclient.models.Message;
import com.birulia.apps.dmitrytwitterclient.utils.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MessagesActivity extends TimeLineActivity {

    private TwitterClient client;
    private TextView tvTitle;
    private MessagesArrayAdapter messagesAdapter;
    private ArrayList<Message> messages;
    public long max_id = 0;
    @BindView(R.id.rvMessages) RecyclerView rvMessages;
    @BindView(R.id.actionBar) Toolbar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        ButterKnife.bind(this);

        client = TwitterApplication.getRestClient();

        messages = new ArrayList<>();
        messagesAdapter = new MessagesArrayAdapter(this, messages);
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = actionBar;
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        rvMessages.setAdapter(messagesAdapter);
        // Set layout manager to position the items
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // Attach the layout manager to the recycler view
        rvMessages.setHasFixedSize(true);
        rvMessages.setLayoutManager(layoutManager);

        rvMessages.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                populateMessages();
            }
        });
        populateMessages();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        tvTitle = (TextView) actionBar.getChildAt(0);
        tvTitle.setTextColor(Color.parseColor("#ffffff"));
        tvTitle.setText("Direct Messages");

        boolean res = super.onCreateOptionsMenu(menu);

        MenuItem item = menu.findItem(R.id.messages);
        item.setVisible(false);

        return res;
    }

    public void populateMessages() {
        client.getDirectMessages(max_id, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                messages.addAll(Message.fromJSONArray(response));
                messagesAdapter.notifyDataSetChanged();
                updateMaxId(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // log exception
                Log.w("TwitterApp", "failed to retrieve tweets");
            }
        });
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
