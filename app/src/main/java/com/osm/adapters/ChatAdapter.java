package com.osm.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.osm.API.OSM;
import com.osm.R;
import com.osm.activities.MergerViewActivity;
import com.osm.models.MergerModel;

import java.util.List;

/**
 * Created by Muhammad on 30/11/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MERGER_SENT = MergerModel.MERGER_OUTGOING;
    private static final int VIEW_TYPE_MERGER_RECEIVED = MergerModel.MERGER_INCOMING;

    private Context mContext;
    private List<MergerModel> mergers;

    public ChatAdapter(Context mContext, List<MergerModel> mergers) {
        this.mContext = mContext;
        this.mergers = mergers;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MERGER_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_outgoing_row, parent, false);
            return new OutgoingMergerHolder(view);
        } else if (viewType == VIEW_TYPE_MERGER_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_incoming_row, parent, false);
            return new IncomingMergerHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MergerModel merger = (MergerModel) mergers.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MERGER_SENT:
                ((OutgoingMergerHolder) holder).bind(merger);
                break;
            case VIEW_TYPE_MERGER_RECEIVED:
                ((IncomingMergerHolder) holder).bind(merger);
        }
    }

    @Override
    public int getItemViewType(int position) {
        MergerModel merger = (MergerModel) mergers.get(position);

        if (merger.getType() == MergerModel.MERGER_OUTGOING) {
            return VIEW_TYPE_MERGER_SENT;
        } else {
            return VIEW_TYPE_MERGER_RECEIVED;
        }
    }

    @Override
    public int getItemCount() {
        return mergers.size();
    }

    private class OutgoingMergerHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView time;

        public OutgoingMergerHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OSM.activeMergerView = mergers.get(getAdapterPosition()).getImage();
                    mContext.startActivity(new Intent(mContext, MergerViewActivity.class));
                }
            });

            image = (ImageView) itemView.findViewById(R.id.ivMergerOut);
            time = (TextView) itemView.findViewById(R.id.tvTimeOut);
        }

        public void bind(MergerModel merger) {
            Glide
                .with(mContext)
                .load(merger.getImage())
                .into(image);
            time.setText(merger.getTime());
        }
    }

    private class IncomingMergerHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView time;

        public IncomingMergerHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OSM.activeMergerView = mergers.get(getAdapterPosition()).getImage();
                    mContext.startActivity(new Intent(mContext, MergerViewActivity.class));
                }
            });

            image = (ImageView) itemView.findViewById(R.id.ivMergerIn);
            time = (TextView) itemView.findViewById(R.id.tvTimeIn);
        }

        public void bind(MergerModel merger) {
            Glide
                    .with(mContext)
                    .load(merger.getImage())
                    .into(image);
            time.setText(merger.getTime());
        }
    }
}
