package com.example.shutda.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import com.example.shutda.R;

import java.util.ArrayList;

public class JogboAdapter extends RecyclerView.Adapter<JogboAdapter.ViewHolder> {
    private ArrayList<Data> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView)view.findViewById(R.id.image);
            mTextView = (TextView)view.findViewById(R.id.textview);
        }
    }

    public JogboAdapter(ArrayList<Data> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public JogboAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position).text);
        holder.mImageView.setImageResource(mDataset.get(position).img);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

class Data{
    public String text;
    public int img;
    public Data(String text, int img){
        this.text = text;
        this.img = img;
    }
}

