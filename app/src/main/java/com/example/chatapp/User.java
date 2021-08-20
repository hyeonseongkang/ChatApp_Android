package com.example.chatapp;

public class User {
    private String key;
    private String id;
    private String pw;
    private String profile;
    private String name;
    private String email;
    private String phone;

    public User() {}

    public User(String key, String id, String pw, String profile, String name, String email, String phone) {
        this.key = key;
        this.id = id;
        this.pw = pw;
        this.profile = profile;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getKey() { return key;}

    public void setKey(String key) { this.key = key;}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
