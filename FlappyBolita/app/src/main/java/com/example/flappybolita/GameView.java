package com.example.flappybolita;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    public static int gapHeight = 500;
    PlayerSprite player;
    private int xV = 2, yV = 2, x = 500, y = 500;
    boolean up = false;
    float touchX, touchY;
    //Aqui van algunas variable relacionadas a la clase que genera y mueve los obstaculos (pipas)
    //Determinamos las variable que establece cual sera el "gap" enttre ambas pipas
    public static int gapH = 500;
    //Velocidad a la que se mueven las pipas
    public static int velocity = 1;
    //Aqui se crean instancias de pipas
    PipeSprite pipe1, pipe2, pipe3;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Manejador de eventos touch
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            up = true;
            player.pY -= yV * 30;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            up = false;
        }
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            //canvas.drawColor(Color.WHITE);
            //Paint paint = new Paint();
            canvas.drawRGB(0,100,250);
            //paint.setColor(Color.rgb(0, 250, 0));
            //canvas.drawCircle(x, y, 90, paint);
            player.draw(canvas);
            pipe1.draw(canvas);
            pipe2.draw(canvas);
            pipe3.draw(canvas);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
        makeLevel();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }
    //Creamos el metodo apra generar el nivel
    public void makeLevel() {
        Bitmap bmp1, bmp2;
        int x, y;
        bmp1 = getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pipe_down), 200
                , Resources.getSystem().getDisplayMetrics().heightPixels / 2);
        bmp2 = getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pipe_up), 200
                , Resources.getSystem().getDisplayMetrics().heightPixels / 2);
        pipe1 = new PipeSprite(bmp1, bmp2, 300, 100);
        pipe2 = new PipeSprite(bmp1, bmp2, 800, 250);
        pipe3 = new PipeSprite(bmp1, bmp2, 1300, 0);
        player = new PlayerSprite(100,100);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

        ///METODO UPDATE, MUY IMPORTANTE AQUI SE DAN TODAS LAS ACTIVIDADES EN TIEMPO REAL DEL JEUGO
    public void update() {
        //Verificamos en que direccion se debe dirigir nuestra bolita cuando tocamos la pantalla
        //Se consideran por separado el movimiento en x y Y;
        player.pY += yV * 5;

        //Se ejecutan los metodos update de PipeSprite en cada pipa
       pipe1.update();
       pipe2.update();
       pipe3.update();
       // Lo activaremos luego, pues ahi se dan las colisiones y reinicios logica();
    }
//p
    //Metodo que me encontre en stackoverflow para cambiar el tamaño de un bitmap y ajustarlo
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap =
                Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public void logica(){
        if (player.pY < pipe1.pY + (screenHeight / 2) - (gapHeight / 2)
                && player.pX + 300 > pipe1.pX && player.pX < pipe1.pX + 500)
        { resetLevel(); }

        if (player.pY < pipe2.pY + (screenHeight / 2) - (gapHeight / 2)
                && player.pX + 300 > pipe2.pX && player.pX < pipe2.pX + 500)
        { resetLevel(); }

        if (player.pY < pipe3.pY + (screenHeight / 2) - (gapHeight / 2)
                && player.pX + 300 > pipe3.pX && player.pX < pipe3.pX + 500)
        { resetLevel(); }
            //Este es diferente, aun no se porque, pero lo separo con el comentario
        if (player.pY + 240 > (screenHeight / 2) + (gapHeight / 2) + pipe1.pY
                && player.pX + 300 > pipe1.pX && player.pX < pipe1.pX + 500)
        { resetLevel(); }

        if (player.pY + 240 > (screenHeight / 2) + (gapHeight / 2) + pipe2.pY
                && player.pX + 300 > pipe2.pX && player.pX < pipe2.pX + 500)
        { resetLevel(); }

        if (player.pY + 240 > (screenHeight / 2) + (gapHeight / 2) + pipe3.pY
                && player.pX + 300 > pipe3.pX && player.pX < pipe3.pX + 500)
        { resetLevel(); }

        //Detect if the character has gone off the
        //bottom or top of the screen
        if (player.pY + 240 < 0) {
            resetLevel(); }
        if (player.pY > screenHeight) {
            resetLevel(); }

        //If the pipe goes off the left of the screen,
        //put it forward at a randomized distance and height
        if (pipe1.pX + 500 < 0) {
            Random r = new Random();
            int value1 = r.nextInt(500);
            int value2 = r.nextInt(500);
            pipe1.pX = screenWidth + value1 + 1000;
            pipe1.pY = value2 - 250;
        }

        if (pipe2.pX + 500 < 0) {
            Random r = new Random();
            int value1 = r.nextInt(500);
            int value2 = r.nextInt(500);
            pipe2.pX = screenWidth + value1 + 1000;
            pipe2.pY = value2 - 250;
        }

        if (pipe3.pX + 500 < 0) {
            Random r = new Random();
            int value1 = r.nextInt(500);
            int value2 = r.nextInt(500);
            pipe3.pX = screenWidth + value1 + 1000;
            pipe3.pY = value2 - 250;
        }
    }

    public void resetLevel(){
            player.pY = 100;
            pipe1.pX = 2000;
            pipe1.pY = 0;
            pipe2.pX = 4500;
            pipe2.pY = 200;
            pipe3.pX = 3200;
            pipe3.pY = 250;
    }
}