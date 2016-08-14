package com.birulia.apps.dmitrytwitterclient.models;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;


public class User implements Parcelable {

    private String name;
    private Long id;
    private String screenName;
    private String location;
    private String profileImageURL;
    private String profileBackgroundColor;
    private String profileBackgroundImageUrl;
    private Integer followersCount;
    private Integer followingCount;

    public Integer getFollowingCount() {
        return followingCount;
    }

    public String getProfileBackgroundColor() {
        return profileBackgroundColor;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public String getLocation() {
        return location;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public String getScreenName() {
        return screenName;
    }


    public User(JSONObject jsonUser){
        try {
            this.id = jsonUser.getLong("id");
            this.name = jsonUser.getString("name");
            this.screenName = "@" + jsonUser.getString("screen_name");
            this.profileImageURL = jsonUser.getString("profile_image_url");

            this.profileBackgroundColor = jsonUser.getString("profile_background_color");
            if (jsonUser.has("profile_banner_url")) {
                this.profileBackgroundImageUrl = jsonUser.getString("profile_banner_url"); //profile_background_image_url
            }
            else{
                this.profileBackgroundImageUrl = jsonUser.getString("profile_background_image_url");
            }
            this.followersCount = jsonUser.getInt("followers_count");
            this.followingCount = jsonUser.getInt("friends_count");
            this.location = jsonUser.getString("location");
        }
        catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeValue(this.id);
        dest.writeString(this.screenName);
        dest.writeString(this.location);
        dest.writeString(this.profileImageURL);
        dest.writeString(this.profileBackgroundColor);
        dest.writeString(this.profileBackgroundImageUrl);
        dest.writeValue(this.followersCount);
        dest.writeValue(this.followingCount);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.screenName = in.readString();
        this.location = in.readString();
        this.profileImageURL = in.readString();
        this.profileBackgroundColor = in.readString();
        this.profileBackgroundImageUrl = in.readString();
        this.followersCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.followingCount = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
