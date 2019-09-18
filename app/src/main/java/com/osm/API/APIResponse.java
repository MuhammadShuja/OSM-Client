package com.osm.API;

import com.osm.models.MergerModel;
import com.osm.models.MergerRequestModel;
import com.osm.models.RequestModel;
import com.osm.models.ThreadModel;
import com.osm.models.UserModel;

import java.util.List;

public class APIResponse {

    public interface AuthListener{
        public void onSuccess(String response);
        public void onFailure(Exception e);
    }

    public interface FriendListListener{
        public void onSuccess(List<UserModel> friends);
        public void onFailure(Exception e);
    }

    public interface FriendRequestListListener{
        public void onSuccess(List<RequestModel> requests);
        public void onFailure(Exception e);
    }

    public interface FriendListener {
        public void onSuccess(String response);
        public void onFailure(Exception e);
    }

    public interface MergerIndexListener{
        public void onSuccess(List<ThreadModel> requests);
        public void onFailure(Exception e);
    }

    public interface MergerFriendListener{
        public void onSuccess(List<MergerModel> requests);
        public void onFailure(Exception e);
    }

    public interface MergerRequestListListener{
        public void onSuccess(List<MergerRequestModel> requests);
        public void onFailure(Exception e);
    }

    public interface MergerListener{
        public void onSuccess(String response);
        public void onFailure(Exception e);
    }
}
