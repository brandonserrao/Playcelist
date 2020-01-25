package com.brandonserrao.playcelist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.MyViewHolder> {

    private List<Record> mDataset;
    public ListsAdapter(List<Record> myDataset) {
        mDataset = myDataset;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv1;
        public TextView tv2;
        //Todo delete in case we don't want a second line:
        // public TextView tv3;
        public View layout;
        public MyViewHolder(View v) {
            super(v);
            layout = v;
            tv1 = v.findViewById(R.id.tv1);
            tv2 = v.findViewById(R.id.tv2);
            //Todo delete in case we don't want a second line:
            // tv3 = v.findViewById(R.id.tv3);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.lists_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv1.setText(String.valueOf(mDataset.get(position).getUID()));
        holder.tv2.setText(mDataset.get(position).getNAME());
        /*
        Todo delete in case we don't want a second line:
         holder.tv3.setText(String.valueOf(mDataset.get(position).getLIST_ITEMS()));
        */
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
