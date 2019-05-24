//package com.example.shutda.view.Ingame;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.widget.ImageButton;
//
//import com.example.shutda.R;
//
//public class GameView extends SurfaceView implements SurfaceHolder.Callback {
//
//    public GameThread thread;
//    private Bitmap test;
//    private int canvasWidth;
//    private int canvasHeight;
//
//    private ImageButton button;
//
//    private int testX = 10;
//    private int testY;
//    private int testSpeed;
//
//    private boolean touch_flag = false;
//
//    public GameView(Context context) {
//        super(context);
//        thread = new GameThread(getHolder(), this);
//
//        test = BitmapFactory.decodeResource(getResources(), R.drawable.schooltest);
//
//        button = new ImageButton(context);
//
//        button.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("아아아ButtonClick아아아");
//            }
//        });
//
//        getHolder().addCallback(this);
//
//        setFocusable(true);
//
//
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//
//
////        characterSprite = new CharacterSprite(BitmapFactory.decodeResource(getResources(), R.drawable.schooltest));
//        thread.setRunning(true);
//        thread.start();
//
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//
//
//        boolean retry = true;
//        while (retry) {
//            try {
//                thread.setRunning(false);
//                thread.join();
//            }catch (InterruptedException e){
//                e.printStackTrace();
//            }
//            retry = false;
//        }
//
//    }
//
//    public void update(){
////        characterSprite.update();
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
//
//
//        canvasWidth = canvas.getWidth();
//        canvasHeight = canvas.getHeight();
//        testSpeed += 2;
//
//        if( touch_flag){
//
//        }
//
//        button.draw(canvas);
//
//        canvas.drawBitmap(test, 0 , 0, null);
//
//        int minBirdY = test.getHeight();
//        int maxBirdY = canvasHeight - test.getHeight() *3;
//
//
//    }
//
//
//    @Override
//    public void draw(Canvas canvas) {
//        super.draw(canvas);
//
//            canvas.drawColor(Color.WHITE);
//            button.draw(canvas);
////            characterSprite.draw(canvas);
//            Paint paint = new Paint();
//            paint.setColor(Color.rgb(250, 0 ,0));
//            canvas.drawRect(100, 100, 200, 200, paint);
//
//    }
//
//
//}
