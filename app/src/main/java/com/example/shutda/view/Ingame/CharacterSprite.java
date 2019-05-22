package com.example.shutda.view.Ingame;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

// For Tutorial
public class CharacterSprite {

    private Bitmap image;
    private int x, y;
    private int xVelocity = 10;
    private int yVelocity = 5;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public CharacterSprite ( Bitmap bitmap){
        image = bitmap;
        x = 100;
        y = 100;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, 100, 100, null);
    }

    public void update() {

        x += xVelocity; //x속도;
        y += yVelocity; //y속도 ;

        if ( (x > screenWidth - image.getWidth()) || (x < 0 )) {
            xVelocity = xVelocity * -1;
        }
        if( (y > screenHeight - image.getHeight()) || (y < 0)){
            yVelocity = yVelocity * -1;
        }
    }
}
