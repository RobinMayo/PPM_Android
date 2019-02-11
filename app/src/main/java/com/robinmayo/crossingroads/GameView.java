package com.robinmayo.crossingroads;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;


public class GameView extends View {

    private static final String TAG = "GameView";

    private Bitmap playerInSize;
    private Bitmap carToLeft;
    private Bitmap carToRight;
    private static int carWidth;
    private static int carHeight;
    private int life;
    private int playerX;
    private int playerY;
    private static int playerWidth;
    private static int playerHeight;
    private int minPlayerY, maxPlayerY, minPlayerX, maxPlayerX;
    private int car1X, car1Y;
    private int car2X, car2Y;
    private int carSpeed;
    private int difficulty;
    private Context context;

    private boolean touch = false;

    private Bitmap backgroundImage;
    private Paint scorePaint = new Paint();
    private Paint levelPaint = new Paint();


    public GameView(Context context) {
        super(context);
        this.context = context;
    }

    public GameView(Context context, int difficulty, File background, File toRightCar,
                    File toLeftCar) {
        super(context);

        this.context = context;

        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        this.difficulty = difficulty;

        Bitmap smurf = BitmapFactory.decodeResource(getResources(), R.drawable.smurf);
        playerInSize = Bitmap.createScaledBitmap(smurf, width / 5,
                height / 5, true);
        playerWidth = playerInSize.getWidth();
        playerHeight = playerInSize.getHeight();

        Bitmap car1InSize = BitmapFactory.decodeFile(toRightCar.getPath());
        carToLeft = Bitmap.createScaledBitmap(car1InSize, width / 5,
                height /5, true);
        carWidth = (int) (carToLeft.getWidth() / 0.9);
        carHeight = (int) (carToLeft.getHeight() / 0.9);

        Bitmap car2InSize = BitmapFactory.decodeFile(toLeftCar.getPath());
        carToRight = Bitmap.createScaledBitmap(car2InSize, width / 5,
                height /5, true);

        Bitmap backgroundInSize = BitmapFactory.decodeFile(background.getPath());
        backgroundImage = Bitmap.createScaledBitmap(backgroundInSize, width, height, true);

        scorePaint.setColor(Color.BLACK);
        scorePaint.setTextSize((float)height / 20);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        levelPaint.setColor(Color.BLACK);
        levelPaint.setTextSize((float)height / 20);
        levelPaint.setTypeface(Typeface.DEFAULT_BOLD);
        levelPaint.setAntiAlias(true);

        minPlayerX = 0;
        maxPlayerX = width - playerInSize.getWidth();

        minPlayerY = 0;
        maxPlayerY = height - playerInSize.getHeight();

        playerX = (width / 2) - (playerInSize.getWidth() / 2);
        playerX = (height / 2) - (playerInSize.getHeight() / 2);

        carSpeed = difficulty;
        life = 3;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = this.getWidth();
        //int h = this.getHeight();
        int margine = 50;

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
            life = life - 1;
            car1X = 0;
        }
        if (collision(car2X, car2Y)) {
            life = life - 1;
            car2X = w;
        }
        if (life < 0) {
            //Thread.interrupted();
        }

        canvas.drawBitmap(backgroundImage, 0, 0, null);
        String levelName = "Level : " + difficulty;
        String remainingLives = "Life : " + life;
        canvas.drawText(levelName, margine, margine, scorePaint);
        canvas.drawText(remainingLives, w - (scorePaint.measureText(remainingLives) + margine),
                margine, scorePaint);

        if(touch) {
            touch = false;
        }
        canvas.drawBitmap(playerInSize, playerX, playerY,null);

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
        boolean playerIsOnCarLine   = (playerY > carY && playerY < (carY + carHeight))
                || ((playerY + carHeight) > carY && (playerY + carHeight) < (carY + carHeight));
        boolean playerIsOnCarColumn = (playerX > carX && playerX < (carX + carWidth))
                || ((playerX + playerWidth) > carX && (playerX + playerWidth) < (carX + carWidth));

        if (playerIsOnCarLine) {
            //Log.d(TAG, "collision(...) - playerIsOnCarLine");
            return !(playerIsOnTheRight || playerIsOnTheLeft);
        } else if (playerIsOnCarColumn) {
            Log.d(TAG, "collision(...) - playerIsOnCarColumn");
            return !(playerIsOnTheTop   || playerIsDownstairs);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE ||
                event.getAction() == MotionEvent.ACTION_DOWN) {
            playerX = (int)(event.getRawX() - playerInSize.getWidth() * 0.5);
            playerY = (int)(event.getRawY() - playerInSize.getHeight() * 0.20);
        }
        return true;
    }
}
