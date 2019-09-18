package com.osm.API;

import android.content.Context;
import android.support.v4.util.Pair;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.osm.models.CropModel;
import com.osm.models.MergerModel;
import com.osm.models.MergerRequestModel;
import com.osm.models.RequestModel;
import com.osm.models.ThreadModel;
import com.osm.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OSM {
    public static String username = null;
    public static String name = null;
    public static String password = null;
    public static String sex = null;
    public static int id = -1;

    public static ArrayList<UserModel> friends = new ArrayList<>();
    public static ArrayList<RequestModel> requests = new ArrayList<>();
    public static ArrayList<MergerRequestModel> mergerRequests = new ArrayList<>();
    public static ArrayList<MergerModel> mergers = new ArrayList<>();
    public static ArrayList<ThreadModel> threads = new ArrayList<>();
    public static int friendsCount, requestsCount, mergerRequestsCount;
    public static String activeChatName;
    public static String activeChatUsername;
    public static int activeMergerRequest;
    public static String activeMergerView;

    private static Context mContext;

    public static OSM getInstance(Context context) {
        mContext = context;
        return new OSM();
    }

    public void login(String username, String password, final APIResponse.AuthListener listener) {
        Event.active = Event.LOGIN_EVENT;
        OSM.username = username;
        OSM.password = password;
        List<Pair<String, String>> data = new ArrayList<>();
        data.add(new Pair<>("username", OSM.username));
        data.add(new Pair<>("password", OSM.password));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Router.getURL(), Serializer.serializeData(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("success")){
                                OSM.username = response.getString("username");
                                OSM.name = response.getString("name");
                                OSM.sex = response.getString("sex");
                                OSM.id = response.getInt("id");
                                listener.onSuccess(response.getString("message"));
                            }
                            else{
                                listener.onFailure(new Exception(response.getString("message")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onFailure(error);
                    }
                }
        );
        Volley.newRequestQueue(mContext).add(request);
    }

    public void register(String username, String password, String name, String sex, final APIResponse.AuthListener listener) {
        Event.active = Event.REGISTER_EVENT;
        OSM.username = username;
        OSM.password = password;
        OSM.name = name;
        OSM.sex = sex;
        List<Pair<String, String>> data = new ArrayList<>();
        data.add(new Pair<>("username", OSM.username));
        data.add(new Pair<>("password", OSM.password));
        data.add(new Pair<>("name", OSM.name));
        data.add(new Pair<>("sex", OSM.sex));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Router.getURL(), Serializer.serializeData(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("success")){
                                OSM.username = response.getString("username");
                                OSM.name = response.getString("name");
                                OSM.sex = response.getString("sex");
                                OSM.id = response.getInt("id");
                                listener.onSuccess(response.getString("message"));
                            }
                            else{
                                listener.onFailure(new Exception(response.getString("message")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onFailure(error);
                    }
                }
        );
        Volley.newRequestQueue(mContext).add(request);
    }

    public void logout(final APIResponse.AuthListener listener) {
        Event.active = Event.LOGOUT_EVENT;
        List<Pair<String, String>> data = new ArrayList<>();
        data.add(new Pair<>("username", OSM.username));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Router.getURL(), Serializer.serializeData(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("success")){
                                listener.onSuccess(response.getString("message"));
                            }
                            else{
                                listener.onFailure(new Exception(response.getString("message")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onFailure(error);
                    }
                }
        );
        Volley.newRequestQueue(mContext).add(request);
    }

    public void getFriends(final APIResponse.FriendListListener listener) {
        friends.clear();
        Event.active = Event.FRIEND_INDEX_EVENT;
        List<Pair<String, String>> data = new ArrayList<>();
        data.add(new Pair<>("username", OSM.username));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Router.getURL(), Serializer.serializeData(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("success")){
                                ArrayList<UserModel> friendIndex = new ArrayList<>();
                                if(response.getInt("count") > 0) {
                                    JSONArray friendList = response.getJSONArray("friends");
                                    for (int i = 0; i < friendList.length(); i++) {
                                        JSONObject entry = (JSONObject) friendList.get(i);
                                        friendIndex.add(new UserModel(
                                                entry.getInt("id"),
                                                entry.getString("username"),
                                                entry.getString("name"),
                                                entry.getString("sex"),
                                                getStatus(entry.getInt("status")),
                                                entry.getString("last_seen")
                                        ));
                                    }
                                    OSM.friends = friendIndex;
                                    OSM.friendsCount = response.getInt("count");
                                }
                                listener.onSuccess(friendIndex);
                            }
                            else{
                                listener.onFailure(new Exception(response.getString("message")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onFailure(error);
                    }
                }
        );
        Volley.newRequestQueue(mContext).add(request);
    }

    public void getRequests(final APIResponse.FriendRequestListListener listener) {
        requests.clear();
        Event.active = Event.FRIEND_REQUESTS_EVENT;
        List<Pair<String, String>> data = new ArrayList<>();
        data.add(new Pair<>("username", OSM.username));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Router.getURL(), Serializer.serializeData(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("success")){
                                ArrayList<RequestModel> requestIndex = new ArrayList<>();
                                if(response.getInt("count") > 0) {
                                    JSONArray requestList = response.getJSONArray("requests");
                                    for (int i = 0; i < requestList.length(); i++) {
                                        JSONObject entry = (JSONObject) requestList.get(i);
                                        requestIndex.add(new RequestModel(
                                                entry.getString("username"),
                                                entry.getString("name"),
                                                entry.getString("sex"),
                                                entry.getInt("id"),
                                                entry.getString("time")
                                        ));
                                    }
                                    OSM.requests = requestIndex;
                                    OSM.requestsCount = response.getInt("count");
                                }
                                listener.onSuccess(requestIndex);
                            }
                            else{
                                listener.onFailure(new Exception(response.getString("message")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onFailure(error);
                    }
                }
        );
        Volley.newRequestQueue(mContext).add(request);
    }

    public void addFriend(String to, final APIResponse.FriendListener listener) {
        Event.active = Event.FRIEND_ADD_EVENT;
        List<Pair<String, String>> data = new ArrayList<>();
        data.add(new Pair<>("from", OSM.username));
        data.add(new Pair<>("to", to));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Router.getURL(), Serializer.serializeData(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("success")){
                                listener.onSuccess(response.getString("message"));
                            }
                            else{
                                listener.onFailure(new Exception(response.getString("message")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onFailure(error);
                    }
                }
        );
        Volley.newRequestQueue(mContext).add(request);
    }

    public void acceptFriend(int id, final APIResponse.FriendListener listener) {
        Event.active = Event.FRIEND_ACCEPT_EVENT;
        List<Pair<String, String>> data = new ArrayList<>();
        data.add(new Pair<>("id", String.valueOf(id)));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Router.getURL(), Serializer.serializeData(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("success")){
                                listener.onSuccess(response.getString("message"));
                            }
                            else{
                                listener.onFailure(new Exception(response.getString("message")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onFailure(error);
                    }
                }
        );
        Volley.newRequestQueue(mContext).add(request);
    }

    public void rejectFriend(int id, final APIResponse.FriendListener listener) {
        Event.active = Event.FRIEND_REJECT_EVENT;
        List<Pair<String, String>> data = new ArrayList<>();
        data.add(new Pair<>("id", String.valueOf(id)));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Router.getURL(), Serializer.serializeData(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("success")){
                                listener.onSuccess(response.getString("message"));
                            }
                            else{
                                listener.onFailure(new Exception(response.getString("message")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onFailure(error);
                    }
                }
        );
        Volley.newRequestQueue(mContext).add(request);
    }

    public void getMergerIndex(final APIResponse.MergerIndexListener listener) {
        threads.clear();
        Event.active = Event.MERGER_INDEX_EVENT;
        List<Pair<String, String>> data = new ArrayList<>();
        data.add(new Pair<>("username", OSM.username));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Router.getURL(), Serializer.serializeData(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("success")){
                                Map<String, ThreadModel> threadMap = new HashMap<>();
                                ArrayList<ThreadModel> threadIndex = new ArrayList<>();
                                if(response.getInt("count") > 0) {
                                    JSONArray threadList = response.getJSONArray("threads");

                                    ThreadModel threadObject;

                                    for (int i = 0; i < threadList.length(); i++) {
                                        JSONObject entry = (JSONObject) threadList.get(i);
                                        if (!threadMap.containsKey(entry.getString("username"))) {
                                            threadObject = new ThreadModel(
                                                    entry.getString("username"),
                                                    entry.getString("name"),
                                                    entry.getString("sex"),
                                                    entry.getString("last_seen"),
                                                    getStatus(entry.getInt("status"))
                                            );

                                            threadObject.addMerger(entry.getString("merger"));
                                            threadIndex.add(threadObject);
                                            threadMap.put(entry.getString("username"), threadObject);
                                        }
                                        else{
                                            threadObject = threadMap.get(entry.getString("username"));
                                            threadObject.addMerger(entry.getString("merger"));
                                        }
                                    }
                                    OSM.threads = threadIndex;
                                }
                                listener.onSuccess(threadIndex);
                            }
                            else{
                                listener.onFailure(new Exception(response.getString("message")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onFailure(error);
                    }
                }
        );
        Volley.newRequestQueue(mContext).add(request);
    }

    public void getMergerRequests(final APIResponse.MergerRequestListListener listener) {
        mergerRequests.clear();
        Event.active = Event.MERGER_REQUESTS_EVENT;
        List<Pair<String, String>> data = new ArrayList<>();
        data.add(new Pair<>("username", OSM.username));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Router.getURL(), Serializer.serializeData(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("success")){
                                ArrayList<MergerRequestModel> requestIndex = new ArrayList<>();
                                if(response.getInt("count") > 0) {
                                    JSONArray requestList = response.getJSONArray("requests");
                                    for (int i = 0; i < requestList.length(); i++) {
                                        JSONObject entry = (JSONObject) requestList.get(i);
                                        requestIndex.add(new MergerRequestModel(
                                                entry.getInt("id"),
                                                entry.getString("name"),
                                                entry.getString("username"),
                                                entry.getString("sex"),
                                                entry.getString("background"),
                                                entry.getString("image"),
                                                entry.getString("time")
                                        ));
                                    }
                                    OSM.mergerRequests = requestIndex;
                                    OSM.mergerRequestsCount = response.getInt("count");
                                }
                                listener.onSuccess(requestIndex);
                            }
                            else{
                                listener.onFailure(new Exception(response.getString("message")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onFailure(error);
                    }
                }
        );
        Volley.newRequestQueue(mContext).add(request);
    }

    public void getMergers(final APIResponse.MergerFriendListener listener) {
        mergers.clear();
        Event.active = Event.MERGER_FRIEND_EVENT;
        List<Pair<String, String>> data = new ArrayList<>();
        data.add(new Pair<>("username", OSM.username));
        data.add(new Pair<>("friend", OSM.activeChatUsername));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Router.getURL(), Serializer.serializeData(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("success")){
                                ArrayList<MergerModel> mergerIndex = new ArrayList<>();
                                if(response.getInt("count") > 0) {
                                    JSONArray requestList = response.getJSONArray("mergers");
                                    for (int i = 0; i < requestList.length(); i++) {
                                        JSONObject entry = (JSONObject) requestList.get(i);
                                        mergerIndex.add(new MergerModel(
                                                entry.getString("image"),
                                                entry.getString("time"),
                                                entry.getInt("type")
                                        ));
                                    }
                                    OSM.mergers = mergerIndex;
                                }
                                listener.onSuccess(mergerIndex);
                            }
                            else{
                                listener.onFailure(new Exception(response.getString("message")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onFailure(error);
                    }
                }
        );
        Volley.newRequestQueue(mContext).add(request);
    }

    public void requestMerger(final APIResponse.MergerListener listener) {
        Event.active = Event.MERGER_ADD_EVENT;

        MultipartRequest request = new MultipartRequest(Request.Method.POST, Router.getURL(),
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        if(response.statusCode == 200){
                            String resultResponse = new String(response.data);
                            try {
                                JSONObject result = new JSONObject(resultResponse);
                                if(result.getBoolean("success")){
                                    listener.onSuccess(result.getString("message"));
                                }
                                else{
                                    listener.onFailure(new Exception(result.getString("message")));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.onFailure(e);
                            }
                        }
                        else{
                            listener.onFailure(new Exception("Merger Failed, Error Code: "+response.statusCode));
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onFailure(error);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("from", OSM.username);
                params.put("to", OSM.activeChatUsername);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("image", new DataPart("image-"+System.currentTimeMillis()+".png", CropModel.getImageBytes(), "image/png"));
                params.put("bg", new DataPart("bg-"+System.currentTimeMillis()+".png", CropModel.getBackgroundBytes(), "image/png"));

                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(request);
    }

    public void rejectRequest(int id, final APIResponse.MergerListener listener) {
        Event.active = Event.MERGER_REJECT_EVENT;
        List<Pair<String, String>> data = new ArrayList<>();
        data.add(new Pair<>("id", String.valueOf(id)));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Router.getURL(), Serializer.serializeData(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("success")){
                                listener.onSuccess(response.getString("message"));
                            }
                            else{
                                listener.onFailure(new Exception(response.getString("message")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onFailure(error);
                    }
                }
        );
        Volley.newRequestQueue(mContext).add(request);
    }

    public void acceptRequest(final APIResponse.MergerListener listener) {
        Event.active = Event.MERGER_ACCEPT_EVENT;

        MultipartRequest request = new MultipartRequest(Request.Method.POST, Router.getURL(),
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        if(response.statusCode == 200){
                            String resultResponse = new String(response.data);
                            try {
                                JSONObject result = new JSONObject(resultResponse);
                                if(result.getBoolean("success")){
                                    listener.onSuccess(result.getString("message"));
                                }
                                else{
                                    listener.onFailure(new Exception(result.getString("message")));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.onFailure(e);
                            }
                        }
                        else{
                            listener.onFailure(new Exception("Merger Failed, Error Code: "+response.statusCode));
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onFailure(error);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", OSM.activeMergerRequest+"");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("image", new DataPart("image-"+System.currentTimeMillis()+".png", CropModel.getResponseBytes(), "image/png"));

                return params;
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(request);
    }

    private boolean getStatus(int status){
        return status == 1;
    }
}