package com.example.shutda.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.shutda.R;
import com.example.shutda.view.utils.BackPressCloseHandler;
import com.example.shutda.view.utils.JogboAdapter;
import com.example.shutda.view.data.JogboData;

import java.util.ArrayList;

public class JogboActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<JogboData> jogboDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogbo);

        final View decorView = getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);

        mRecyclerView = findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        jogboDataset = new ArrayList<>();
        mAdapter = new JogboAdapter(jogboDataset);
        mRecyclerView.setAdapter(mAdapter);

        jogboDataset.add(new JogboData("1. 삼팔광땡", R.drawable.sampal));
        jogboDataset.add(new JogboData("2. 광땡", R.drawable.gwang));
        jogboDataset.add(new JogboData("3. 땡", R.drawable.ttaeng));
        jogboDataset.add(new JogboData("4. 알리", R.drawable.alli));
        jogboDataset.add(new JogboData("5. 독사", R.drawable.dogsa));
        jogboDataset.add(new JogboData("6. 구삥", R.drawable.gupping));
        jogboDataset.add(new JogboData("7. 장삥", R.drawable.jangpping));
        jogboDataset.add(new JogboData("8. 장사", R.drawable.jangsa));
        jogboDataset.add(new JogboData("9. 세륙", R.drawable.selyug));
        jogboDataset.add(new JogboData("10. 땡잡이", R.drawable.ttaengjabi));
        jogboDataset.add(new JogboData("11. 구사", R.drawable.gusa));
        jogboDataset.add(new JogboData("12. 멍텅구리", R.drawable.meongteong));
    }

}
