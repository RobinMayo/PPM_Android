package com.robinmayo.crossingroads;


import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;

public class Level {
    private static final String TAG = "Level";

    private String levelName;
    private String latitude;
    private String longitude;
    private LatLng point;
    private int difficulty;
    private File background;
    private File toRightCar;
    private File toLeftCar;
    private File pin;

    Level(String levelName, String latitude, String longitude, int difficulty,
                 File background, File toRightCar, File toLeftCar, File pin) {
        this.levelName = levelName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.difficulty = difficulty;
        this.background = background;
        this.toRightCar = toRightCar;
        this.toLeftCar = toLeftCar;
        this.pin = pin;
        point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
    }

    public File getPin() {
        if (pin != null) {
            return pin;
        } else {
            Log.e(TAG, "ERROR in getPin() - File pin is not initialized.");
            return new File("");
        }
    }

    public LatLng getPoint() {
        if (point != null) {
            return point;
        } else {
            Log.e(TAG, "ERROR in getPoint() - Point is not initialized.");
            return new LatLng(48.866667, 2.333333);
        }
    }

    public int getDifficulty() {
        return difficulty;
    }

    public File getBackground() {
        return background;
    }

    public File getToRightCar() {
        return toRightCar;
    }

    public File getToLeftCar() {
        return toLeftCar;
    }
}
