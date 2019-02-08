package com.robinmayo.crossingroads;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;


public class UnboundedService extends Service {
    MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreat() {
        mediaPlayer = MediaPlayer.create(this, R.raw.on_the_run);
        mediaPlayer.setLooping(false);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        mediaPlayer.start();
        return START_STICKY;
    }
}
