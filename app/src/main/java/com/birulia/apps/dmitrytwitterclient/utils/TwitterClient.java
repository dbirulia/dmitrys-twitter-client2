package com.birulia.apps.dmitrytwitterclient.utils;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CONSUMER_KEY = "KFkQOfNkMBND1ngRLCTvf3md2";
	public static final String REST_CONSUMER_SECRET = "WDu8pJU7Zy59gxHvbB2auB9hehjqa7D6dJLWUgqdSpSUuGPMxN";
	public static final String REST_CALLBACK_URL = "oauth://dmitrytwitterclient";

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}


    public void getHomeTimeLine(long max_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        if (max_id > 0) {
            params.put("max_id", max_id);
        }
        client.get(apiUrl, params, handler);
    }

    public void getMentionesTimeLine(long max_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        if (max_id > 0) {
            params.put("max_id", max_id);
        }
        client.get(apiUrl, params, handler);
    }

    public void getUserTimeLine(long max_id, String screenName, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("/statuses/user_timeline.json");
        RequestParams params = new RequestParams();

        params.put("screen_name", screenName);
        params.put("count", 25);
        if (max_id > 0) {
            params.put("max_id", max_id);
        }
        client.get(apiUrl, params, handler);
    }

    public void createTweet(String bodyStr, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", bodyStr);
        client.post(apiUrl, params, handler);
    }

    public void getUserProfile(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/users/show.json");
        RequestParams params = new RequestParams();
        if (screenName != "") {
            params.put("screen_name", screenName);
        }
        else{
            getMyProfile(handler);
            return;
        }
        client.get(apiUrl, params, handler);
    }

    public void getMyProfile(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("/account/verify_credentials.json");
        client.get(apiUrl, null, handler);
    }

    public void getDirectMessages(long max_id, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("/direct_messages.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        if (max_id > 0) {
            params.put("max_id", max_id);
        }
        client.get(apiUrl, params, handler);
    }

    public void likeTweet(Long id, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("/favorites/create.json");
        RequestParams params = new RequestParams();
        params.put("id", id);
        client.post(apiUrl, params, handler);
    }

    public void unLikeTweet(Long id, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("/favorites/destroy.json");
        RequestParams params = new RequestParams();
        params.put("id", id);
        client.post(apiUrl, params, handler);
    }


}