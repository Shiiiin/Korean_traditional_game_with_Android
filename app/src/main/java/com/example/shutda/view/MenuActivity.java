package com.example.shutda.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shutda.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    Button gameStartButton;
    Button scoreboardButton;
    Button ruleButton;
    Button leaveButton;
    Button jogboButton;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final View decorView = getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);

        gameStartButton = findViewById(R.id.play_button);
        scoreboardButton = findViewById(R.id.rank_button);
        ruleButton = findViewById(R.id.rule_button);
        jogboButton = findViewById(R.id.answer_button);
        leaveButton = findViewById(R.id.logout_button);
        firebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


        gameStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go2Game =  new Intent(MenuActivity.this, MainActivity.class);
                startActivity(go2Game);
            }
        });
        scoreboardButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO 스코어보드 intent
                Intent go2Rankboard = new Intent(MenuActivity.this, RankActivity.class);
                startActivity(go2Rankboard);

            }
        });

        ruleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO rule intent
            }
        });

        jogboButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go2jogbo = new Intent(MenuActivity.this, JogboActivity.class);
                startActivity(go2jogbo);
            }
        });

        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).update("token_id", "").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent quit = new Intent(MenuActivity.this, LoginActivity.class);
                        firebaseAuth.signOut();
                        startActivity(quit);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("로그아웃 실패", "오류 로그: "+e);
                        Toast.makeText(MenuActivity.this, "로그아웃 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
