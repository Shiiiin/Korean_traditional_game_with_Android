package com.example.shutda.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.shutda.R;

public class Start extends AppCompatActivity {

    Button gameStartButton;
    Button scoreboardButton;
    Button ruleButton;
    Button leaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        gameStartButton = findViewById(R.id.Ingame);
        scoreboardButton = findViewById(R.id.scoreBoard);
        ruleButton = findViewById(R.id.Rule);
        leaveButton = findViewById(R.id.Leave);

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
                startActivity(quit);
            }
        });
    }
}
