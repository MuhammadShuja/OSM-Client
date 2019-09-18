package com.osm.API;

public class Event {
    public static final int REGISTER_EVENT = 111;
    public static final int LOGIN_EVENT = 112;
    public static final int LOGOUT_EVENT = 113;

    public static final int FRIEND_INDEX_EVENT = 114;
    public static final int FRIEND_REQUESTS_EVENT = 115;
    public static final int FRIEND_ADD_EVENT = 116;
    public static final int FRIEND_ACCEPT_EVENT = 117;
    public static final int FRIEND_REJECT_EVENT = 118;
    public static final int FRIEND_REMOVE_EVENT = 119;

    public static final int MERGER_INDEX_EVENT = 120;
    public static final int MERGER_REQUESTS_EVENT = 121;
    public static final int MERGER_ADD_EVENT = 122;
    public static final int MERGER_ACCEPT_EVENT = 123;
    public static final int MERGER_REJECT_EVENT = 124;
    public static final int MERGER_REMOVE_EVENT = 125;
    public static final int MERGER_FRIEND_EVENT = 126;

    public static int active = -1;

}
