package com.example.shutda.view.Ingame;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class GameThread extends Thread {


    private Handler handler = new Handler();
    private static Queue<Object> messeageQueue;

    public GameThread() {


    }

    Runnable r1 = new Runnable() {
        @Override
        public void run() {


        }
    };
}
