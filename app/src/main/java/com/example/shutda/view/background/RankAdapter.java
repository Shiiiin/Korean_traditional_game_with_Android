package com.example.shutda.view.background;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shutda.R;
import com.example.shutda.view.data.UserForRank;

import java.util.ArrayList;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankViewHolder> {

    private final ArrayList<UserForRank> Userlist;

    public RankAdapter(ArrayList<UserForRank> userlist) { Userlist = userlist; }

    @Override
    public RankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);

        RankAdapter.RankViewHolder vh = new RankAdapter.RankViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RankViewHolder holder, int position) {

        int realPosition = position +1;
        holder.mUserRank.setText(String.valueOf(realPosition+ "등"));
        holder.mUserName.setText(Userlist.get(position).getName());
        holder.mUserScore.setText(String.valueOf(Userlist.get(position).getScore()));
    }

    @Override
    public int getItemCount() { return Userlist.size();  }

    public class RankViewHolder extends RecyclerView.ViewHolder {

        public final TextView mUserRank;
        public final TextView mUserName;
        public final TextView mUserScore;

        public RankViewHolder(View view) {
            super(view);
            mUserRank = view.findViewById(R.id.user_rank);
            mUserName = view.findViewById(R.id.user_name);
            mUserScore = view.findViewById(R.id.user_score);
        }
    }
}
