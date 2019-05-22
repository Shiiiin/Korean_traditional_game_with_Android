package com.example.shutda.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.shutda.view.Ingame.GameView;

public class test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameView gameView = new GameView(this);
        setContentView(gameView);
    }
}
