package com.github.ukasz09.models;

public class UserRecordModel {
    public String id;
    public String logoUrl;
    public String nick;
    public double time;

    public UserRecordModel(String logoUrl, String nick, double time) {
        this.logoUrl = logoUrl;
        this.nick = nick;
        this.time = time;
    }
}
