package com.robinmayo.crossingroads;

public class LevelDescription {
    private Level[] levels;
    private int nbLevels = 0;

    public LevelDescription(int nbLevels) {
        this.nbLevels = nbLevels;
        levels = new Level[nbLevels];
    }

    /**
     * @param indice : level position.
     * @return a spacific level.
     *
     * Warning : can return null. Think to test return value.
     */
    public Level getLevel(int indice) {
        if (indice < nbLevels) {
            return levels[indice];
        }
        return null;
    }

    public void setLevel(Level level, int indice) {
        if (indice < nbLevels) {
            levels[indice] = level;
        }
    }
}
