package com.robinmayo.Blottiere_Boukhelif;

import android.util.Log;


public class Player {
    private static final String TAG = "Player";
    private static String name;
    private static String motto;

    public Player() {
        Log.e(TAG, "Player()");
        Player.name = "";
        Player.motto = "";
    }

    public Player(String name, String motto) {
        Log.e(TAG, "Player(String name, String motto)");
        Player.name = name;
        Player.motto = motto;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Player.name = name;
    }

    public static String getMotto() {
        return motto;
    }

    public static void setMotto(String motto) {
        Player.motto = motto;
    }
}
