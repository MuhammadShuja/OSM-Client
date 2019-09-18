package com.osm.adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.osm.R;
import com.osm.models.ThreadModel;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Muhammad on 28/11/2018.
 */

public class InboxAdapter extends ArrayAdapter<ThreadModel> {

    private String[] avatarsM = {"avatar_m1", "avatar_m2", "avatar_m3", "avatar_m4"};
    private String[] avatarsF = {"avatar_f1", "avatar_f2", "avatar_f3", "avatar_f4"};

    private Context mContext;
    private ArrayList<ThreadModel> threads;
    private int resource;

    public InboxAdapter(Context mContext, int resource, ArrayList<ThreadModel> threads) {
        super(mContext, resource);
        this.mContext = mContext;
        this.resource = resource;
        this.threads = threads;
    }

    @Override
    public int getCount() {
        return threads.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ThreadViewHolder holder = null;

        if(view == null){
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(resource, null);

            holder = new ThreadViewHolder();
            holder.avatar = (ImageView) view.findViewById(R.id.threadAvatar);
            holder.name = (TextView) view.findViewById(R.id.tvName);
            holder.lastSeen = (TextView) view.findViewById(R.id.tvLastSeen);
            holder.count = (TextView) view.findViewById(R.id.tvImageCount);
            holder.image1 = (ImageView) view.findViewById(R.id.iv1);
            holder.image2 = (ImageView) view.findViewById(R.id.iv2);
            holder.image3 = (ImageView) view.findViewById(R.id.iv3);
            holder.threadControl = (LinearLayout) view.findViewById(R.id.containerControl);

            view.setTag(holder);
        }
        else{
            holder = (ThreadViewHolder) view.getTag();
        }

        holder.name.setText(threads.get(position).getName());
        holder.lastSeen.setText("Last seen: "+threads.get(position).getLastSeen());
        if(threads.get(position).getCount() > 3){
            holder.count.setVisibility(View.VISIBLE);
            holder.count.setText("+"+(threads.get(position).getCount()-3)+" more");
        }

        if(threads.get(position).isActive()){
            holder.threadControl.setBackgroundResource(R.drawable.bg_thread_control_active);
        }

        Random rnd = new Random();

        if(threads.get(position).getSex().equals("Male")){
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
        int count = threads.get(position).getCount();
        if(count == 1){
            holder.image1.setVisibility(View.VISIBLE);
            Glide
                .with(getContext())
                .load(threads.get(position).getMergers().get(0))
                .into(holder.image1);
        }
        else if(count == 2){
            holder.image1.setVisibility(View.VISIBLE);
            holder.image2.setVisibility(View.VISIBLE);
            Glide
                .with(getContext())
                .load(threads.get(position).getMergers().get(0))
                .into(holder.image1);
            Glide
                .with(getContext())
                .load(threads.get(position).getMergers().get(1))
                .into(holder.image2);
        }
        else if(count >= 3){
            holder.image1.setVisibility(View.VISIBLE);
            holder.image2.setVisibility(View.VISIBLE);
            holder.image3.setVisibility(View.VISIBLE);
            Glide
                .with(getContext())
                .load(threads.get(position).getMergers().get(0))
                .into(holder.image1);
            Glide
                .with(getContext())
                .load(threads.get(position).getMergers().get(1))
                .into(holder.image2);
            Glide
                .with(getContext())
                .load(threads.get(position).getMergers().get(2))
                .into(holder.image3);
        }

        return view;
    }

    private class ThreadViewHolder{
        private ImageView avatar;
        private TextView name;
        private ImageView image1;
        private ImageView image2;
        private ImageView image3;
        private TextView count;
        private TextView lastSeen;
        private ImageButton btnMenu;
        private LinearLayout threadControl;
    }
}