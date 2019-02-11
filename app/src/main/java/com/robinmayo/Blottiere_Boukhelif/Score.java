package com.robinmayo.Blottiere_Boukhelif;

public class Score {
    private String name;
    private String city;
    private String score;
    private String level;

    Score(String name, String city, String score, String level) {
        this.name = name;
        this.city = city;
        this.score = score;
        this.level = level;
    }

    public String toString() {
        return name + "\t\t" + city + "\t\t" + score + "\t\t" + level;
    }

    public String getName() {
        return name;
    }
}
