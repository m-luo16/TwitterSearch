package com.vouchr.michelle.vouchrtwitter;

/**
 * Created by Michelle Luo on 2018-05-11
 * */

public class ListTweet {
    private String name;
    private String content;
    private String screenName;
    private String profileImageURL;

    public ListTweet(String screenName, String name, String content, String profileImageURL) {
        this.screenName = screenName;
        this.name = name;
        this.content = content;
        this.profileImageURL = profileImageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String username) {
        this.name = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getScreenName() {
        return screenName;
    }
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }
}
