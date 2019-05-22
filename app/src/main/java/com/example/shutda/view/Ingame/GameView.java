package com.example.shutda.view.Ingame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.shutda.R;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    public GameThread thread;
    private Canvas canvas;

    private CharacterSprite characterSprite = new CharacterSprite(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground));

    public GameView(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new GameThread(getHolder(), this);



        serFocusable(true);
    }



    public void update(){

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if ( canvas != null){
            canvas.drawColor(Color.WHITE);
            Paint paint = new Paint();
            paint.setColor(Color.rgb(250, 0 ,0));
            canvas.drawRect(100, 100, 200, 200, paint);
            characterSprite.draw(canvas);

        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        boolean retry =true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            retry = false;
        }

    }

    private void serFocusable(boolean b) {
    }

}
