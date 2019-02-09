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


public class WebParser extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = "AsyncMusicPlayer";
    private static final String FILE_PATH
            = "https://www.lrde.epita.fr/~renault/teaching/ppm/2018/game.txt";

    private static File gameFile;

    public WebParser(File file) {
        WebParser.gameFile = file;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d(TAG, "doInBackground(...)");

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

        InputStreamReader inputStreamReader;
        LineNumberReader lineNumberReader;
        String myLine;
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(gameFile));
            lineNumberReader = new LineNumberReader(inputStreamReader);

            while ((myLine = lineNumberReader.readLine()) != null) {
                Log.i(TAG, myLine);
            }
            lineNumberReader.close();
            inputStreamReader.close();
        }catch (Exception e) {
            System.err.println("Error : "+e.getMessage());
        }

        return true;
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