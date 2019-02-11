package com.robinmayo.Blottiere_Boukhelif.activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.robinmayo.Blottiere_Boukhelif.GameView;
import com.robinmayo.Blottiere_Boukhelif.Level;

import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";

    private GameView gameView;
    private Handler handler = new Handler();
    private Level level;
    private final static long stepTime = 20;


    public GameActivity() {
        this.level = WorldActivity.currentLevel;
        Log.d(TAG, "new GameActivity() - level = " + level.getDifficulty());
    }

    public GameActivity(Level level) {
        Log.d(TAG, "new GameActivity(..) - level = " + level.getDifficulty());
        this.level = level;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new GameView(this.getApplicationContext(), level.getDifficulty(),
                level.getBackground(), level.getToLeftCar(), level.getToRightCar());
        setContentView(gameView);

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        gameView.invalidate();
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, stepTime);
    }
}
