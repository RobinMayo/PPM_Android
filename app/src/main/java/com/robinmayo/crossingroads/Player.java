package com.robinmayo.crossingroads;

public class Player {
    private static String name;
    private static String motto;
    private static int level;

    public Player() {
        Player.name = "";
        Player.motto = "";
        Player.level = 0;
    }

    public Player(String name, String motto) {
        Player.name = name;
        Player.motto = motto;
        Player.level = 0;
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

    public static int getLevel() {
        return level;
    }

    public static void setLevel(int level) {
        Player.level = level;
    }
}
