package com.osm.models;

/**
 * Created by Muhammad on 30/11/2018.
 */

public class MergerModel {

    public static final int MERGER_INCOMING = 1;
    public static final int MERGER_OUTGOING = 2;

    private String image;
    private String time;
    private int type;

    public MergerModel(String image, String time, int type) {

        this.image = image;
        this.time = time;
        this.type = type;
    }

    public static int getMergerIncoming() {
        return MERGER_INCOMING;
    }

    public static int getMergerOutgoing() {
        return MERGER_OUTGOING;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
