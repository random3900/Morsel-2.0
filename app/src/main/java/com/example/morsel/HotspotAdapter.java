package com.example.morsel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HotspotAdapter extends RecyclerView.Adapter<HotspotAdapter.HotspotViewHolder> {
    public static class HotspotViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView avgnumTextView;
        TextView addressTextView;

        public HotspotViewHolder(View v) {
            super(v);
            nameTextView = (TextView) itemView.findViewById(R.id.hotspot_name);
            avgnumTextView = (TextView) itemView.findViewById(R.id.hotspot_avg_num);
            addressTextView = (TextView) itemView.findViewById(R.id.hotspot_address);
        }
    }
    private ArrayList<Hotspot> mCustomHotspot;

    public HotspotAdapter(ArrayList<Hotspot> arrayList) {
        mCustomHotspot = arrayList;
    }

    @NonNull
    @Override
    public HotspotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotspot, parent, false);
        return new HotspotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotspotViewHolder holder, int position) {
        Hotspot h = mCustomHotspot.get(position);
        holder.addressTextView.setText(h.getAddress());
        holder.avgnumTextView.setText(Long.toString(h.getAvgnum()));
        holder.nameTextView.setText(h.getName());
    }

    @Override
    public int getItemCount() {
        return mCustomHotspot.size();
    }


}
