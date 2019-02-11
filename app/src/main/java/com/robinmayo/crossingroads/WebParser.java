package com.robinmayo.crossingroads;

import android.os.AsyncTask;
import android.util.Log;

import com.robinmayo.crossingroads.interfaces.TaskDelegate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;


public class WebParser extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = "WebParser";
    private static final String FILE_PATH
            = "https://www.lrde.epita.fr/~renault/teaching/ppm/2018/game.txt";
    private static final String SCORE_FILE
            = "https://www.lrde.epita.fr/~renault/teaching/ppm/2018/results.txt";

    private TaskDelegate delegate;
    private static File gameFile;
    private static File scoreFile;
    private File appDir;

    public WebParser(File appDir, File gameFile, File scoreFile, TaskDelegate delegate) {
        this.appDir = appDir;
        WebParser.gameFile = gameFile;
        this.delegate = delegate;
        WebParser.scoreFile = scoreFile;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(TAG, "doInBackground(...)");

        // Download file : "https://www.lrde.epita.fr/~renault/teaching/ppm/2018/game.txt"
        try {
            URL url = new URL(FILE_PATH);
            downloadFile(url, gameFile);
            url = new URL(SCORE_FILE);
            downloadFile(url, gameFile);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }

        if (!affectLevels()) {
            return false;
        }

        return affectScore();
    }

    private void downloadFile(URL url, File file) {
        try {
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(file);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Parse game file and set levels :
    private boolean affectLevels() {
        InputStreamReader inputStreamReader;
        LineNumberReader lineNumberReader;
        String myLine;
        String[] parcedLine;
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(gameFile));
            lineNumberReader = new LineNumberReader(inputStreamReader);

            int indice = 0;
            while ((myLine = lineNumberReader.readLine()) != null) {
                Log.d(TAG, myLine);
                parcedLine = myLine.split("#");
                Log.i(TAG, Arrays.toString(parcedLine));
                saveLevel(parcedLine, indice);
                indice++;
            }
            lineNumberReader.close();
            inputStreamReader.close();

        }catch (Exception e) {
            System.err.println("Error : "+e.getMessage());
            return false;
        }

        return true;
    }

    // Parse score file and set score list :
    private boolean affectScore() {
        InputStreamReader inputStreamReader;
        LineNumberReader lineNumberReader;
        String myLine;
        String[] parcedLine;
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(scoreFile));
            lineNumberReader = new LineNumberReader(inputStreamReader);

            int indice = 0;
            while ((myLine = lineNumberReader.readLine()) != null) {
                Log.d(TAG, myLine);
                parcedLine = myLine.split("#");
                Log.i(TAG, Arrays.toString(parcedLine));
                saveScore(parcedLine, indice);
                indice++;
            }
            lineNumberReader.close();
            inputStreamReader.close();

        }catch (Exception e) {
            System.err.println("Error : "+e.getMessage());
            return false;
        }

        return true;
    }

    private void saveLevel(String[] parcedLine, int indice) {
        if (parcedLine.length < 8) {
            Log.e(TAG, "ERROR in saveLevel(String[] parcedLine) : level can not be created "
                    + "parcedLine.length = " + parcedLine.length + ").");
        } else if (parcedLine.length > 8) {
            Log.w(TAG, "WARNING in saveLevel(String[] parcedLine) : too much information." +
                    "A part of the riding line is ignored.");
        }
        if (parcedLine.length == 8) {
            // We get the name of each file as the last element of the path split by "/".
            String background   = parcedLine[4].split("/")[parcedLine[4].split("/").length - 1];
            String toRightCar   = parcedLine[5].split("/")[parcedLine[5].split("/").length - 1];
            String toLeftCar    = parcedLine[6].split("/")[parcedLine[6].split("/").length - 1];
            String pin          = parcedLine[7].split("/")[parcedLine[7].split("/").length - 1];

            Log.d(TAG, "saveLevel(...) - " + background);

            URL url;
            try {
                url = new URL(parcedLine[4]);
                File backgroundFile = new File(appDir, background);
                downloadFile(url, backgroundFile);
                url = new URL(parcedLine[5]);
                File toRightCarFile = new File(appDir, toRightCar);
                downloadFile(url, toRightCarFile);
                url = new URL(parcedLine[6]);
                File toLeftCarFile = new File(appDir, toLeftCar);
                downloadFile(url, toLeftCarFile);
                url = new URL(parcedLine[7]);
                File pinFile = new File(appDir, pin);
                downloadFile(url, pinFile);

                Level level = new Level(parcedLine[0], parcedLine[1], parcedLine[2],
                        Integer.parseInt(parcedLine[3]) - 1, backgroundFile,
                        toRightCarFile, toLeftCarFile, pinFile);
                LevelDescription.setLevel(level, indice);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveScore(String[] parcedLine, int indice) {
        if (parcedLine.length < 4) {
            Log.e(TAG, "ERROR in saveScore(String[] parcedLine) : score list can not be "
                    + "created parcedLine.length = " + parcedLine.length + ").");
            return;
        } else if (parcedLine.length > 4) {
            Log.w(TAG, "WARNING in saveScore(String[] parcedLine) : too much information." +
                    "A part of the riding line is ignored.");
        }
        // Extract data from line.
        if (parcedLine.length == 4) {
            Log.d(TAG, "saveScore(...) - " + parcedLine[3]);

            Score score = new Score(parcedLine[0], parcedLine[1], parcedLine[2], parcedLine[3]);

            ScoreList.getListScore().add(score);
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        Log.d(TAG, "onPostExecute(...)");

        if (success) {
            Log.d(TAG, "onPostExecute(...) : SUCCESS");
            delegate.taskCompletionResult();
        }
    }

    @Override
    protected void onCancelled() {
    }
}