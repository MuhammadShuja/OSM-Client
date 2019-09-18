package com.osm.models;

import java.util.ArrayList;

/**
 * Created by Muhammad on 28/11/2018.
 */

public class ThreadModel {

    private String username;
    private String name;
    private String sex;
    private String lastSeen;
    private boolean isActive;
    private ArrayList<String> mergers;
    private int count;

    public ThreadModel(String username, String name, String sex, String lastSeen, boolean isActive) {
        this.username = username;
        this.name = name;
        this.sex = sex;
        this.lastSeen = lastSeen;
        this.isActive = isActive;

        this.mergers = new ArrayList<>();
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

    public String getLastSeen() {
        return lastSeen;
    }

    public boolean isActive() {
        return isActive;
    }

    public ArrayList<String> getMergers() {
        return mergers;
    }

    public int getCount() {
        return this.mergers.size();
    }

    public void addMerger(String merger){
        this.mergers.add(merger);
    }
}
