package com.osm.models;

/**
 * Created by Muhammad on 28/11/2018.
 */

public class RequestModel {

    private String username;
    private String name;
    private String sex;
    private int id;
    private String time;

    public RequestModel(String username, String name, String sex, int id, String time) {
        this.username = username;
        this.name = name;
        this.sex = sex;
        this.id = id;
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }
}
