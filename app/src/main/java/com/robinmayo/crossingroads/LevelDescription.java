package com.robinmayo.crossingroads;

import android.util.Log;

class LevelDescription {
    private static final String TAG = "LevelDescription";
    private static int nbLevels = 5;
    private static Level[] levels = new Level[nbLevels];

    LevelDescription(int nbLevels) {
        Log.w(TAG, "new LevelDescription(int nbLevels)");

        LevelDescription.nbLevels = nbLevels;
        levels = new Level[nbLevels];
    }

    /**
     * @param indice : level position.
     * @return a spacific level.
     *
     * Warning : can return null. Think to test return value.
     */
    static Level getLevel(int indice) {
        if (indice < nbLevels) {
            return levels[indice];
        }
        return null;
    }

    static void setLevel(Level level, int indice) {
        if (indice < nbLevels) {
            levels[indice] = level;
        }
    }

    static int getNbLevels() {
        return nbLevels;
    }
}
