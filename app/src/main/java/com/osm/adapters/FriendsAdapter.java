package com.osm.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.osm.R;
import com.osm.models.UserModel;

import java.util.List;
import java.util.Random;

/**
 * Created by Muhammad on 29/11/2018.
 */

public class FriendsAdapter extends ArrayAdapter<UserModel> {

    private String[] avatarsM = {"avatar_m1", "avatar_m2", "avatar_m3", "avatar_m4"};
    private String[] avatarsF = {"avatar_f1", "avatar_f2", "avatar_f3", "avatar_f4"};

    private int resource;

    private List<UserModel> friends;

    public FriendsAdapter(Context context, int resource, List<UserModel> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.friends = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        FriendViewHolder holder = null;

        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(resource, null);

            holder = new FriendViewHolder();
            holder.avatar = (ImageView) view.findViewById(R.id.ivMergerRequestAvatar);
            holder.name = (TextView) view.findViewById(R.id.tvFriendName);
            holder.lastSeen = (TextView) view.findViewById(R.id.tvFriendLastSeen);
            holder.status = (ImageView) view.findViewById(R.id.ivStatus);

            view.setTag(holder);
        }
        else{
            holder = (FriendViewHolder) view.getTag();
        }

        holder.name.setText(friends.get(position).getName());
        holder.lastSeen.setText(friends.get(position).getLastSeen());

        if(friends.get(position).isActive()){
            holder.status.setColorFilter(ContextCompat.getColor(getContext(), R.color.dark_mint), PorterDuff.Mode.SRC_IN);
        }

        Random rnd = new Random();

        if(friends.get(position).getSex().equals("Male")){
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

    private class FriendViewHolder{
        private ImageView avatar;
        private TextView name;
        private TextView lastSeen;
        private ImageView status;
    }
}
