package com.robinmayo.crossingroads;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;


public class GameView extends View {

    private static final String TAG = "GameView";

    private Bitmap playerInSize;
    private Bitmap carToLeft;
    private Bitmap carToRight;
    private static int carWidth;
    private static int carHeight;
    private int score;
    private int playerX;
    private int playerY;
    private static int playerWidth;
    private static int playerHeight;
    private int minPlayerY, maxPlayerY, minPlayerX, maxPlayerX;
    private int car1X, car1Y;
    private int car2X, car2Y;
    private int carSpeed;

    private boolean touch = false;

    private Bitmap backgroundImage;
    private Paint scorePaint = new Paint();


    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, int difficulty, File background, File toRightCar,
                    File toLeftCar) {
        super(context);

        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;

        Bitmap smurf = BitmapFactory.decodeResource(getResources(), R.drawable.smurf);
        playerInSize = Bitmap.createScaledBitmap(smurf, width / 5,
                height / 5, true);
        playerWidth = playerInSize.getWidth();
        playerHeight = playerInSize.getHeight();

        Bitmap car1InSize = BitmapFactory.decodeFile(toRightCar.getPath());
        carToLeft = Bitmap.createScaledBitmap(car1InSize, width / 5,
                height /5, true);
        carWidth = carToLeft.getWidth();
        carHeight = carToLeft.getHeight();

        Bitmap car2InSize = BitmapFactory.decodeFile(toLeftCar.getPath());
        carToRight = Bitmap.createScaledBitmap(car2InSize, width / 5,
                height /5, true);

        Bitmap backgroundInSize = BitmapFactory.decodeFile(background.getPath());
        backgroundImage = Bitmap.createScaledBitmap(backgroundInSize, width, height, true);

        scorePaint.setColor(Color.YELLOW);
        scorePaint.setTextSize((float)height / 20);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        minPlayerX = 0;
        maxPlayerX = width - playerInSize.getWidth();

        minPlayerY = 0;
        maxPlayerY = height - playerInSize.getHeight();

        playerX = (width / 2) - (playerInSize.getWidth() / 2);
        playerX = (height / 2) - (playerInSize.getHeight() / 2);

        carSpeed = difficulty;
        score = 0;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = this.getWidth();
        //int h = this.getHeight();

        if (playerY > maxPlayerY) {
            playerY = maxPlayerY;
        }
        if (playerY < minPlayerY) {
            playerY = minPlayerY;
        }
        if (playerX > maxPlayerX) {
            playerX = maxPlayerX;
        }
        if (playerX < minPlayerX) {
            playerX = minPlayerX;
        }

        if (collision(car1X, car1Y)) {
            score = score + 1;
            car1X = 99999;
        }
        if (collision(car2X, car2Y)) {
            score = score + 1;
            car2X = -100;
        }

        canvas.drawBitmap(backgroundImage, 0, 0, null);
        canvas.drawText("Score : " + score, 10, 30, scorePaint);

        if(touch) {
            canvas.drawBitmap(playerInSize, playerX, playerY,null);
            touch = false;
        } else {
            canvas.drawBitmap(playerInSize, playerX, playerY,null);
        }

        car1X = car1X + carSpeed;
        if (car1X > w){
            car1X = 0 - 20;
            car1Y = (int) Math.floor(Math.random()* (maxPlayerY - minPlayerY)) + minPlayerY;
        }
        canvas.drawBitmap(carToLeft, car1X, car1Y, null);

        car2X = car2X - carSpeed;
        if (car2X < 0){
            car2X = w + 20;
            car2Y = (int) Math.floor(Math.random() * (maxPlayerY - minPlayerY)) + minPlayerY;
        }
        canvas.drawBitmap(carToRight, car2X, car2Y, null);
    }

    public boolean collision(int carX, int carY) {
        boolean playerIsOnTheRight  = (playerX + playerWidth) < carX;
        boolean playerIsOnTheLeft   = playerX > (carX + carWidth);
        boolean playerIsOnTheTop    = (playerY + playerHeight) > carY;
        boolean playerIsDownstairs  = playerY < (carY + carHeight);
        boolean playerIsOnCarLine   = playerY > carY && playerY < (carY + carHeight);
        boolean playerIsOnCarColumn = playerX > carX && playerX < (carX + carWidth);

        if (playerIsOnCarLine) {
            return !(playerIsOnTheRight || playerIsOnTheLeft);
        } else if (playerIsOnCarColumn) {
            return !(playerIsOnTheTop   || playerIsDownstairs);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE ||
                event.getAction() == MotionEvent.ACTION_DOWN) {
            playerX = (int)(event.getRawX() - playerInSize.getWidth() / 2);
            playerY = (int)(event.getRawY() - playerInSize.getHeight() * 0.50);
        }
        return true;
    }
}
