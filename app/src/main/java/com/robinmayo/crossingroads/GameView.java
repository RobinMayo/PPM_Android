package com.robinmayo.crossingroads;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;


public class GameView extends View {

    private static final String TAG = "GameView";

    //private int difficulty;
    //private File background;
    //private File toRightCar;
    //private File toLeftCar;

    private Bitmap playerInSize;
    private Bitmap car1;
    private Bitmap car2;

    private boolean touch = false;

    private Bitmap backgroundImage;
    private Paint scorePaint = new Paint();
    //private Bitmap life[] = new Bitmap[2];


    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, int difficulty, File background, File toRightCar,
                    File toLeftCar) {
        super(context);

        //this.difficulty = difficulty;
        //this.background = background;
        //this.toRightCar = toRightCar;
        //this.toLeftCar = toLeftCar;

        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;

        Bitmap smurf = BitmapFactory.decodeResource(getResources(), R.drawable.smurf);
        playerInSize = Bitmap.createScaledBitmap(smurf, width / 5,
                height / 5, true);

        Bitmap car1InSize = BitmapFactory.decodeFile(toRightCar.getPath());
        car1 = Bitmap.createScaledBitmap(car1InSize, width / 5,
                height /5, true);

        Bitmap car2InSize = BitmapFactory.decodeFile(toLeftCar.getPath());
        car2 = Bitmap.createScaledBitmap(car2InSize, width / 5,
                height /5, true);

        Bitmap backgroundInSize = BitmapFactory.decodeFile(background.getPath());
        backgroundImage = Bitmap.createScaledBitmap(backgroundInSize, width, height, true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
