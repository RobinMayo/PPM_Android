package com.robinmayo.crossingroads;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;


public class UnboundedService extends Service {
    private static final String TAG = "UnboundedService";
    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreat()");

        listRaw();
        //Field[] fields = R.raw.class.getFields();
        Log.e(TAG, "!!!!!!!!!! "+getPackageName());
        //mediaPlayer = MediaPlayer.create(this, R.raw.on_the_run);

        //FileInputStream fis = getResources().openRawResource(R.raw.on_the_run);

        //Uri songPath = Uri.parse("android.resource://com.robinmayo.crossingRoads/raw/on_the_run");
        Uri songPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.on_the_run);
        //Uri songPath = Uri.parse("file:///android_asset/on_the_run");
        //String sPath = "android.resource://" + getPackageName() + "/" + R.raw.on_the_run;
        //String sPath = "android.resource://" + getPackageName() + "/raw/on_the_run.mp3";

        Log.e(TAG, songPath.getPath());

        mediaPlayer = new MediaPlayer();
        //File f = new File(sPath);
        //FileDescriptor fd = new FileDescriptor();
        try {
            //mediaPlayer.setDataSource(fis.getFD());
            mediaPlayer.setDataSource(this, songPath);
        } catch (IOException e) {
            Log.e(TAG, "\t\t !!!!!!!!!!");
            Toast.makeText(this, "mp3 not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
        mediaPlayer.setLooping(true);
    }

    public void listRaw() {
        Field[] fields=R.raw.class.getFields();
        for(int count=0; count < fields.length; count++){
            Log.i("Raw Asset: ", fields[count].getName());
        }
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        Log.d(TAG, "onStartCommand(Intent intent, int flags, int startID)");
        mediaPlayer.start();
        return START_STICKY;
    }
}
