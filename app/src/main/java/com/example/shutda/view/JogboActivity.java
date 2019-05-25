package com.example.shutda.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.shutda.R;

import java.util.ArrayList;

public class JogboActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Data> jogboDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogbo);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        jogboDataset = new ArrayList<>();
        mAdapter = new JogboAdapter(jogboDataset);
        mRecyclerView.setAdapter(mAdapter);

        jogboDataset.add(new Data("1. 삼팔광땡", R.drawable.sampal));
        jogboDataset.add(new Data("2. 광땡", R.drawable.gwang));
        jogboDataset.add(new Data("3. 땡", R.drawable.ttaeng));
        jogboDataset.add(new Data("4. 알리", R.drawable.alli));
        jogboDataset.add(new Data("5. 독사", R.drawable.dogsa));
        jogboDataset.add(new Data("6. 구삥", R.drawable.gupping));
        jogboDataset.add(new Data("7. 장삥", R.drawable.jangpping));
        jogboDataset.add(new Data("8. 장사", R.drawable.jangsa));
        jogboDataset.add(new Data("9. 세륙", R.drawable.selyug));
        jogboDataset.add(new Data("10. 땡잡이", R.drawable.ttaengjabi));
        jogboDataset.add(new Data("11. 구사", R.drawable.gusa));
        jogboDataset.add(new Data("12. 멍텅구리", R.drawable.meongteong));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
