package com.brandonserrao.playcelist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.MyViewHolder> {

    private List<Record> mDataset;

    public SongsAdapter(List<Record> myDataset) {
        mDataset = myDataset;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv1;
        public TextView tv2;
        public TextView tv3;
        public TextView tv4;
        public TextView tv5;
        public View layout;

        public MyViewHolder(View v) {
            super(v);
            layout = v;
            tv1 = v.findViewById(R.id.tv1);
            tv2 = v.findViewById(R.id.tv2);
            tv3 = v.findViewById(R.id.tv3);
            tv4 = v.findViewById(R.id.tv4);
            tv5 = v.findViewById(R.id.tv5);
        }
    }

    @Override
    public SongsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.songs_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv1.setText(String.valueOf(mDataset.get(position).getUID()));
        holder.tv2.setText(mDataset.get(position).getNAME());
        holder.tv5.setText(mDataset.get(position).getS_ID());
        holder.tv3.setText(String.valueOf(mDataset.get(position).getLNG()));
        holder.tv4.setText(String.valueOf(mDataset.get(position).getLAT()));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
