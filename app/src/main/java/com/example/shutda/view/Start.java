package com.example.shutda.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.shutda.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Start extends AppCompatActivity {

    Button gameStartButton;
    Button scoreboardButton;
    Button ruleButton;
    Button leaveButton;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        gameStartButton = findViewById(R.id.Ingame);
        scoreboardButton = findViewById(R.id.scoreBoard);
        ruleButton = findViewById(R.id.Rule);
        leaveButton = findViewById(R.id.Leave);
        firebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


        gameStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go2Game =  new Intent(Start.this, MainActivity.class);
                startActivity(go2Game);
            }
        });
        scoreboardButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO 스코어보드 intent
            }
        });

        ruleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO rule intent
            }
        });

        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quit = new Intent(Start.this, LoginActivity.class);

                Map<String, Object> tokenMap = new HashMap<>();

                tokenMap.put("token_id", "");

                mFirestore.collection("Users").document(firebaseAuth.getUid()).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })


                firebaseAuth.signOut();
                startActivity(quit);
            }
        });
    }
}
