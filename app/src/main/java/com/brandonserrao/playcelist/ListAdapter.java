package com.brandonserrao.playcelist;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {
    private String[] mDataset;
    public ListAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    //-------------------testing---------------
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public Button button;
        public MyViewHolder(Button v) {
            super(v);
            button = v;
        }
    }
    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        Button v = (Button) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.button.setText(mDataset[position]);
    }
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
    //---------------------testing------------------

/*    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public MyViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }
    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(mDataset[position]);
    }
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
*/
}
