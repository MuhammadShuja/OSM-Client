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
import com.osm.fragments.MergerRequestsFragment;
import com.osm.models.MergerRequestModel;
import com.osm.models.RequestModel;

import java.util.List;
import java.util.Random;

public class MergerRequestAdapter  extends ArrayAdapter<MergerRequestModel> {

    private String[] avatarsM = {"avatar_m1", "avatar_m2", "avatar_m3", "avatar_m4"};
    private String[] avatarsF = {"avatar_f1", "avatar_f2", "avatar_f3", "avatar_f4"};

    private int resource;
    private Context mContext;
    private List<MergerRequestModel> requests;
    private MergerRequestAdapter.OnUpdateListener listener;

    public MergerRequestAdapter(Context context, int resource, List<MergerRequestModel> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.requests = objects;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        MergerRequestAdapter.MergerRequestViewHolder holder = null;

        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(resource, null);

            holder = new MergerRequestViewHolder();
            holder.avatar = (ImageView) view.findViewById(R.id.ivMergerRequestAvatar);
            holder.name = (TextView) view.findViewById(R.id.tvMergerRequestName);
            holder.username = (TextView) view.findViewById(R.id.tvMergerRequestUsername);
            holder.time = (TextView) view.findViewById(R.id.tvMergerRequestTime);
            holder.btnAccept = (Button) view.findViewById(R.id.btnAcceptMergerRequest);
            holder.btnReject = (Button) view.findViewById(R.id.btnRejectMergerRequest);

            holder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MergerRequestsFragment.REQUEST = MergerRequestsFragment.REQUEST_ACCEPTED;
                    OSM.activeMergerRequest = requests.get(position).getId();
                    listener.onUpdate();
                }
            });

            holder.btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OSM.getInstance(mContext).rejectRequest(requests.get(position).getId(), new APIResponse.MergerListener(){

                        @Override
                        public void onSuccess(String response) {
                            Toast.makeText(mContext, response, Toast.LENGTH_LONG).show();
                            OSM.mergerRequestsCount = OSM.mergerRequestsCount - 1;
                            MergerRequestsFragment.REQUEST = MergerRequestsFragment.REQUEST_REJECTED;
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
            holder = (MergerRequestViewHolder) view.getTag();
        }

        holder.name.setText(requests.get(position).getSenderName());
        holder.username.setText("@"+requests.get(position).getSenderUsername());
        holder.time.setText(requests.get(position).getTime());

        Random rnd = new Random();

        if(requests.get(position).getSenderSex().equals("Male")){
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

    public void setOnUpdateListener(MergerRequestAdapter.OnUpdateListener listener){
        this.listener = listener;
    }

    public interface OnUpdateListener{
        public void onUpdate();
    }

    private class MergerRequestViewHolder{
        private ImageView avatar;
        private TextView name;
        private TextView username;
        private TextView time;
        private Button btnAccept, btnReject;
    }
}