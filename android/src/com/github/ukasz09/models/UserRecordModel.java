package com.github.ukasz09.models;

public class UserRecordModel {
    public String logoUrl;
    public String nick;
    public int travelledMeters;

    public UserRecordModel(String logoUrl, String nick, int travelledMeters) {
        this.logoUrl = logoUrl;
        this.nick = nick;
        this.travelledMeters = travelledMeters;
    }
}
