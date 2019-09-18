package com.osm.models;

public class MergerRequestModel {
    private int id;
    private String senderName;
    private String senderUsername;
    private String senderSex;
    private String background;
    private String image;
    private String time;

    public MergerRequestModel(int id, String senderName, String senderUsername, String senderSex, String background, String image, String time) {
        this.id = id;
        this.senderName = senderName;
        this.senderUsername = senderUsername;
        this.senderSex = senderSex;
        this.background = background;
        this.image = image;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getSenderSex() {
        return senderSex;
    }

    public String getBackground() {
        return background;
    }

    public String getImage() {
        return image;
    }

    public String getTime() {
        return time;
    }
}
