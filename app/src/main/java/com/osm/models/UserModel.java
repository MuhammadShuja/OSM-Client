package com.osm.models;

public class UserModel {

    private int id;
    private String username;
    private String name;
    private String sex;
    private boolean isActive;
    private String lastSeen;

    public UserModel(int id, String username, String name, String sex, boolean isActive, String lastSeen) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.sex = sex;
        this.isActive = isActive;
        this.lastSeen = lastSeen;
    }

    public int getId() {
        return id;
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

    public boolean isActive() {
        return isActive;
    }

    public String getLastSeen() {
        return lastSeen;
    }
}
