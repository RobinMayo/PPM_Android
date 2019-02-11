package com.robinmayo.crossingroads.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.robinmayo.crossingroads.R;
import com.robinmayo.crossingroads.ScoreList;


public class StatisticsActivity extends AppCompatActivity {

    private static final String TAG = "StatisticsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        int i = 0;
        while (ScoreList.getListScore().get(i) != null) {
            Log.i(TAG, ScoreList.getListScore().get(i).getName());
        }
    }
}
