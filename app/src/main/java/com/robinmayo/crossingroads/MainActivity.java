package com.robinmayo.crossingroads;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.robinmayo.crossingroads.R;

public class MainActivity extends AppCompatActivity {
    private Intent musicIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world);
        musicIntent = new Intent(this, UnboundedService.class);
        this.startService(musicIntent);
    }

    // The application is note visible at the screen.
    @Override
    protected void onStop() {
        super.onStop();
        this.stopService(musicIntent);
    }
}
