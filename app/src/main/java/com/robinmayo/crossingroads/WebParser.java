package com.robinmayo.crossingroads;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;


public class WebParser extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = "WebParser";
    private static final String FILE_PATH
            = "https://www.lrde.epita.fr/~renault/teaching/ppm/2018/game.txt";

    private static File gameFile;

    WebParser(File file) {
        WebParser.gameFile = file;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(TAG, "doInBackground(...)");

        // Download file : "https://www.lrde.epita.fr/~renault/teaching/ppm/2018/game.txt"
        downloadGameFile();

        // Parse game file and set levels :
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
        }

        return true;
    }

    private void downloadGameFile() {
        URL url;
        try {
            url = new URL(FILE_PATH);
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(gameFile);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveLevel(String[] parcedLine, int indice) {
        if (parcedLine.length < 8) {
            Log.e(TAG, "ERROR in saveLevel(String[] parcedLine) : level can not be created.");
        } else if (parcedLine.length > 8) {
            Log.w(TAG, "WARNING in saveLevel(String[] parcedLine) : too much information." +
                    "A part of the riding line is ignored.");
        }
        if (parcedLine.length == 8) {
            Level level = new Level(parcedLine[0], parcedLine[1], parcedLine[2],
                    Integer.parseInt(parcedLine[3]),  parcedLine[4], parcedLine[5], parcedLine[6],
                    parcedLine[7]);
            LevelDescription.setLevel(level, indice);
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        Log.d(TAG, "onPostExecute(...)");

        if (success) {
            Log.d(TAG, "onPostExecute(...) : SUCCESS");
        }
    }

    @Override
    protected void onCancelled() {
    }
}