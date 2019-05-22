package com.example.shutda.view.Ingame;

import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.example.shutda.view.MainActivity;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class GameThread extends Thread {

    private static final String TAG = "GameThread";
    private static final int targetFPS = 60;

    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running;
    public  Canvas canvas;


    public GameThread(SurfaceHolder surfaceHolder, GameView gameView) {

        super();

        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;

    }

    @Override
    public void run() {
        long startTime = 0;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long averageFPS;
        long targetTime = 1000 / targetFPS;

        while(running){
            startTime = System.nanoTime();
            canvas = null;
        }

        try {
            canvas = this.surfaceHolder.lockCanvas();
            synchronized (surfaceHolder){
                this.gameView.update();
                this.gameView.draw(canvas);
            }
        }catch (Exception e){        }
        finally {
            if ( canvas != null){

                try{
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        timeMillis = (System.nanoTime() - startTime) / 1000000 ;
        waitTime = targetTime - timeMillis;

        try{
            this.sleep(waitTime);
        }catch (Exception e){
        }

        totalTime += System.nanoTime() - startTime;
        frameCount++;
        if( frameCount == targetFPS){
            averageFPS = 1000 / ((totalTime / frameCount) / 1000000) ;
            frameCount =0;
            totalTime = 0;
            System.out.println("AverageFPS : "+ averageFPS);
        }
    }

    public void setRunning(boolean isRunning){
        running = isRunning;
    }


}
