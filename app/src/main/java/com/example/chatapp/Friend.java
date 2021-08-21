package com.example.chatapp;

public class Friend {

    private String user;
    private String profile;

    public Friend() {}

    public Friend(String user, String profile) {
        this.user = user;
        this.profile = profile;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

}
