package com.robinmayo.crossingroads;

public class LevelDescription {
    private static Level[] levels;
    private static int nbLevels = 0;

    public LevelDescription(int nbLevels) {
        LevelDescription.nbLevels = nbLevels;
        levels = new Level[nbLevels];
    }

    /**
     * @param indice : level position.
     * @return a spacific level.
     *
     * Warning : can return null. Think to test return value.
     */
    public static Level getLevel(int indice) {
        if (indice < nbLevels) {
            return levels[indice];
        }
        return null;
    }

    public static void setLevel(Level level, int indice) {
        if (indice < nbLevels) {
            levels[indice] = level;
        }
    }
}
