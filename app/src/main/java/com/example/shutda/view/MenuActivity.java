package com.example.shutda.view;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.shutda.R;
import com.example.shutda.view.utils.MusicPlayer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MenuActivity extends AppCompatActivity{


    private long backKeyClickTime = 0;
    private Button gameStartButton;
    private Button scoreboardButton;
    private Button ruleButton;
    private Button leaveButton;
    private Button jogboButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore mFirestore;
    private String mUserId;

    private View decorView;
    private int uiOptions;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        firebaseAuth.signOut();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(firebaseAuth.getCurrentUser() != null & isNetworkConnected() == false){
            firebaseAuth.signOut();
            sendBack();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        decorView = getWindow().getDecorView();
        uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
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

        if (firebaseAuth.getCurrentUser() == null) {

            sendBack();

        } else {

            mUserId = firebaseAuth.getCurrentUser().getUid();
        }

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

                Intent go2Rankboard = new Intent(MenuActivity.this, RankActivity.class);
                startActivity(go2Rankboard);

            }
        });

        ruleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO rule intent

                Intent go2Ruleboard = new Intent(MenuActivity.this, RuleActivity.class);
                startActivity(go2Ruleboard);

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

                        firebaseAuth.signOut();
                        sendBack();

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

    private void sendBack() {

        MusicPlayer.getInstance(MenuActivity.this).MusicTurnOff();
        Intent loginIntent = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public void onBackPressed() {

        BackPressed2Login();

    }



  public void BackPressed2Login() {

        if (System.currentTimeMillis() > backKeyClickTime + 2000) { backKeyClickTime = System.currentTimeMillis();
            Toast.makeText(MenuActivity.this, "뒤로가기 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return; }
        if (System.currentTimeMillis() <= backKeyClickTime + 2000) {

                mFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).update("token_id", "").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                public void onSuccess(Void aVoid) {
                        firebaseAuth.signOut();
                        sendBack();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("로그아웃 실패", "오류 로그: "+e);
                    Toast.makeText(MenuActivity.this, "로그아웃 실패", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private boolean isNetworkConnected(){

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}

