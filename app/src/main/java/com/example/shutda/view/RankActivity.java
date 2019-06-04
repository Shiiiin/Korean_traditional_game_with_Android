package com.example.shutda.view;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shutda.R;
import com.example.shutda.view.utils.BackPressCloseHandler;
import com.example.shutda.view.utils.RankAdapter;
import com.example.shutda.view.data.UserForRank;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RankActivity extends AppCompatActivity {

    private FirebaseFirestore mDB;
    private RankAdapter rankAdapter;
    private ArrayList<UserForRank> userArrayList;
    private ListView mlistview;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onResume() {
        super.onResume();

        updateRankList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        final View decorView = getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);

        mSwipeRefreshLayout = findViewById(R.id.rank_swipe_layout);
        userArrayList = new ArrayList<>();

        mDB = FirebaseFirestore.getInstance();

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        setUpRecyclerView();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);

                updateRankList();

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void updateRankList() {

        if(userArrayList.size() > 0 ){
                userArrayList.clear();
        }

        mDB.collection("Users").orderBy("score", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    for (DocumentSnapshot documentSnapshot : task.getResult()) {

                            Log.d("USER LIST LOAD", documentSnapshot.getId() + " => " + documentSnapshot.getData());

                            //TODO USER로 바꿔보기  UserforRank 지울수 잇나....보자구!

                        UserForRank user = new UserForRank(documentSnapshot.getString("name"), documentSnapshot.getLong("score"));

                        Log.d("USER LIST LOAD", documentSnapshot.getString("name") + " &&& " + documentSnapshot.getLong("score"));

                        userArrayList.add(user);

                        rankAdapter = new RankAdapter(RankActivity.this, R.layout.user_list_item, userArrayList);

                        mlistview.setAdapter(rankAdapter);
                    }

                } else {
                    Log.e("에러", "불러오기 에러", task.getException());
                    Toast.makeText(RankActivity.this,"데이터불러오기 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void setUpRecyclerView() {
        mlistview = findViewById(R.id.rank_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(RankActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    }
}
