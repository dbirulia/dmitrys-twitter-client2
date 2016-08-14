package com.birulia.apps.dmitrytwitterclient.models;


import android.annotation.TargetApi;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

public class Message {

    private Long id;
    private String body, createdAt;
    private User user;

    public Long getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }


    public Message(JSONObject jsonTweet){
        try {
            this.id = jsonTweet.getLong("id");
            this.body = jsonTweet.getString("text");
            this.createdAt = getRelativeTimeAgo(jsonTweet.getString("created_at"));
            this.user = new User(jsonTweet.getJSONObject("sender"));

        }
        catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    public static ArrayList<Message> fromJSONArray  (JSONArray jsonTweets) {
        ArrayList<Message> messages = new ArrayList<>();

        for (int i = 0; i < jsonTweets.length(); i++) {
            try{
                messages.add(new Message(jsonTweets.getJSONObject(i)));
            }
            catch (JSONException ex){
                ex.printStackTrace();
            }
        }
        return messages;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return relativeDate;
    }

}
