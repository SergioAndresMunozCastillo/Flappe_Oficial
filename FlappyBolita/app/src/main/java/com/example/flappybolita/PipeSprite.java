package com.example.flappybolita;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.widget.Toast;

import java.nio.channels.Pipe;

public class PipeSprite {
    private Bitmap img1, img2;
    public int pipeXVel = 10;
    public int pX, pY;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public PipeSprite(Bitmap bmp1, Bitmap bmp2, int x, int y) {
        img1 = bmp1;
        img2 = bmp2;
        pX = x;
        pY = y;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(img1, pX, -(GameView.gapH / 2) + pY, null);
        canvas.drawBitmap(img2, pX, ((screenHeight / 2)
                + (GameView.gapH / 2)) + pY, null);
        System.out.println("Gap " + screenHeight);
    }

    public void update(){
        pX -= GameView.velocity;
    }
}