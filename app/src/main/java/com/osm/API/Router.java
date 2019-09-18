package com.osm.API;

public class Router {

    public static final String API_HOST = "http://192.168.100.18:8000/public";
    public static final String URL_REGISTER = API_HOST+"/api/auth/register";
    public static final String URL_LOGIN = API_HOST+"/api/auth/login";
    public static final String URL_LOGOUT = API_HOST+"/api/auth/logout";

    public static final String URL_FRIEND_INDEX = API_HOST+"/api/friend/index";
    public static final String URL_FRIEND_REQUESTS = API_HOST+"/api/friend/requests";
    public static final String URL_FRIEND_ADD = API_HOST+"/api/friend/add";
    public static final String URL_FRIEND_ACCEPT = API_HOST+"/api/friend/accept";
    public static final String URL_FRIEND_REJECT = API_HOST+"/api/friend/reject";
    public static final String URL_FRIEND_REMOVE = API_HOST+"/api/friend/remove";

    public static final String URL_MERGER_INDEX = API_HOST+"/api/merger/index";
    public static final String URL_MERGER_REQUESTS = API_HOST+"/api/merger/requests";
    public static final String URL_MERGER_ADD = API_HOST+"/api/merger/add";
    public static final String URL_MERGER_ACCEPT = API_HOST+"/api/merger/accept";
    public static final String URL_MERGER_REJECT = API_HOST+"/api/merger/reject";
    public static final String URL_MERGER_REMOVE = API_HOST+"/api/merger/remove";
    public static final String URL_MERGER_FRIEND = API_HOST+"/api/merger/friend";



    public static String getURL(){
        switch (Event.active){
            case Event.REGISTER_EVENT:
                return  URL_REGISTER;
            case Event.LOGIN_EVENT:
                return  URL_LOGIN;
            case Event.LOGOUT_EVENT:
                return  URL_LOGOUT;
            case Event.FRIEND_INDEX_EVENT:
                return  URL_FRIEND_INDEX;
            case Event.FRIEND_REQUESTS_EVENT:
                return  URL_FRIEND_REQUESTS;
            case Event.FRIEND_ADD_EVENT:
                return  URL_FRIEND_ADD;
            case Event.FRIEND_ACCEPT_EVENT:
                return  URL_FRIEND_ACCEPT;
            case Event.FRIEND_REJECT_EVENT:
                return  URL_FRIEND_REJECT;
            case Event.FRIEND_REMOVE_EVENT:
                return  URL_FRIEND_REMOVE;
            case Event.MERGER_INDEX_EVENT:
                return  URL_MERGER_INDEX;
            case Event.MERGER_REQUESTS_EVENT:
                return  URL_MERGER_REQUESTS;
            case Event.MERGER_ADD_EVENT:
                return  URL_MERGER_ADD;
            case Event.MERGER_ACCEPT_EVENT:
                return  URL_MERGER_ACCEPT;
            case Event.MERGER_REJECT_EVENT:
                return  URL_MERGER_REJECT;
            case Event.MERGER_REMOVE_EVENT:
                return  URL_MERGER_REMOVE;
            case Event.MERGER_FRIEND_EVENT:
                return  URL_MERGER_FRIEND;
        }
        return null;
    }
}
