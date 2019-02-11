package com.robinmayo.crossingroads;

public class Score {
    private String name;
    private String city;
    private String score;
    private String level;

    public Score(String name, String city, String score, String level) {
        this.name = name;
        this.city = city;
        this.score = score;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getScore() {
        return score;
    }

    public String getLevel() {
        return level;
    }
}
