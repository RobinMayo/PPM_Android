package com.robinmayo.crossingroads;


public class Level {
    private String levelName;
    private String latitude;
    private String longitude;
    private int difficulty;
    private String background;
    private String toRightCar;
    private String toLeftCar;
    private String pin;

    public Level(String levelName, String latitude, String longitude, int difficulty,
                 String background, String toRightCar, String toLeftCar, String pin) {
        this.levelName = levelName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.difficulty = difficulty;
        this.background = background;
        this.toRightCar = toRightCar;
        this.toLeftCar = toLeftCar;
        this.pin = pin;
    }
}
