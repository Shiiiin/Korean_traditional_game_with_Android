package com.example.shutda.view.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shutda.R;
import com.example.shutda.view.data.UserForRank;

import java.util.ArrayList;

public class RankAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private final ArrayList<UserForRank> Userlist;
    private int layout;

    public RankAdapter(Context context, int layout, ArrayList<UserForRank> userlist)
    { this.Userlist = userlist;
      this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      this.layout = layout;
    }

    @Override
    public int getCount() {
        return Userlist.size();
    }

    @Override
    public Object getItem(int position) {
        return Userlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            TextView mUserRank;
            TextView mUserName;
            TextView mUserScore;

            if(convertView == null){
                    convertView = inflater.inflate(layout,parent, false);
                }

            mUserRank = convertView.findViewById(R.id.user_rank);
            mUserName = convertView.findViewById(R.id.user_name);
            mUserScore = convertView.findViewById(R.id.user_score);

        int realPosition = position +1;
        //TODO 여기서부터 시작해야함~!~! 카드뷰 이미지 넣기 (이미지뷰로 대체도 생각!)

        mUserRank.setText(String.valueOf(realPosition+ "등"));
        mUserName.setText(Userlist.get(position).getName());
        mUserScore.setText(String.valueOf(Userlist.get(position).getScore()));

        if(mUserRank.getText().equals("1등")){
            mUserRank.setBackgroundResource(R.drawable.goldmedal);
        }
        else if(mUserRank.getText().equals("2등") ){
            mUserRank.setBackgroundResource(R.drawable.silvermedal);
        }
        else if(mUserRank.getText().equals("3등")){
            mUserRank.setBackgroundResource(R.drawable.bronzemedal);
        }
        else{
            mUserRank.setBackgroundResource(0);
        }

        return convertView;
    }

}
