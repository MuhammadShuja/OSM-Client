package com.osm.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.osm.API.APIResponse;
import com.osm.API.OSM;
import com.osm.R;
import com.osm.models.RequestModel;

import java.util.List;
import java.util.Random;

public class RequestsAdapter extends ArrayAdapter<RequestModel> {

    private String[] avatarsM = {"avatar_m1", "avatar_m2", "avatar_m3", "avatar_m4"};
    private String[] avatarsF = {"avatar_f1", "avatar_f2", "avatar_f3", "avatar_f4"};

    private int resource;
    private Context mContext;
    private List<RequestModel> requests;
    private OnUpdateListener listener;

    public RequestsAdapter(Context context, int resource, List<RequestModel> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.requests = objects;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        RequestsAdapter.RequestViewHolder holder = null;

        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(resource, null);

            holder = new RequestViewHolder();
            holder.avatar = (ImageView) view.findViewById(R.id.ivRequestAvatar);
            holder.name = (TextView) view.findViewById(R.id.tvRequestName);
            holder.username = (TextView) view.findViewById(R.id.tvRequestUsername);
            holder.sex = (TextView) view.findViewById(R.id.tvRequestSex);
            holder.btnAccept = (Button) view.findViewById(R.id.btnAcceptRequest);
            holder.btnReject = (Button) view.findViewById(R.id.btnRejectRequest);

            holder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OSM.getInstance(mContext).acceptFriend(requests.get(position).getId(), new APIResponse.FriendListener(){

                        @Override
                        public void onSuccess(String response) {
                            Toast.makeText(mContext, response, Toast.LENGTH_LONG).show();
                            OSM.requestsCount = OSM.requestsCount - 1;
                            listener.onUpdate();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                            listener.onUpdate();
                        }
                    });
                }
            });

            holder.btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OSM.getInstance(mContext).rejectFriend(requests.get(position).getId(), new APIResponse.FriendListener(){

                        @Override
                        public void onSuccess(String response) {
                            Toast.makeText(mContext, response, Toast.LENGTH_LONG).show();
                            OSM.requestsCount = OSM.requestsCount - 1;
                            listener.onUpdate();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                            listener.onUpdate();
                        }
                    });
                }
            });

            view.setTag(holder);
        }
        else{
            holder = (RequestViewHolder) view.getTag();
        }

        holder.name.setText(requests.get(position).getName());
        holder.username.setText("@"+requests.get(position).getUsername());
        holder.sex.setText(requests.get(position).getSex());

        Random rnd = new Random();

        if(requests.get(position).getSex().equals("Male")){
            Glide
                    .with(getContext())
                    .load(getContext().getResources().getIdentifier(avatarsM[rnd.nextInt(3)], "drawable", getContext().getPackageName()))
                    .into(holder.avatar);
        }
        else{
            Glide
                    .with(getContext())
                    .load(getContext().getResources().getIdentifier(avatarsF[rnd.nextInt(3)], "drawable", getContext().getPackageName()))
                    .into(holder.avatar);
        }

        return view;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    public void setOnUpdateListener(OnUpdateListener listener){
        this.listener = listener;
    }

    public interface OnUpdateListener{
        public void onUpdate();
    }

    private class RequestViewHolder{
        private ImageView avatar;
        private TextView name;
        private TextView username;
        private TextView sex;
        private Button btnAccept, btnReject;
    }
}
