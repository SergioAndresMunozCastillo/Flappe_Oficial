package com.example.flappybolita;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class PlayerSprite {
    private Bitmap img1, img2;
    public int pipeXVel = 10;
    public int pX, pY;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public PlayerSprite(int x, int y) {
        pX = x;
        pY = y;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(250, 0, 0));
        canvas.drawCircle(pX, pY, 90, paint);
        System.out.println("Gap " + screenHeight);
    }

    public void update(){
        pX -= GameView.velocity;
    }
}
