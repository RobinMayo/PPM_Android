package com.robinmayo.Blottiere_Boukhelif.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;

import com.robinmayo.Blottiere_Boukhelif.R;
import com.robinmayo.Blottiere_Boukhelif.ScoreList;
import com.robinmayo.Blottiere_Boukhelif.StatisticsAdapter;


public class StatisticsActivity extends ListActivity {

    private static final String TAG = "StatisticsActivity";

    private ArrayAdapter<String> adapter;
    private String[] values;
    private ListView mListView;

    // This is the Adapter being used to display the list's data
    SimpleCursorAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        mListView = findViewById(R.id.list);

        values = new String[ScoreList.getListScore().size()];

        for (int i = 0; i < ScoreList.getListScore().size(); i++) {
            Log.i(TAG, ScoreList.getListScore().get(i).toString());
            values[i] = ScoreList.getListScore().get(i).toString();
        }
        adapter = new StatisticsAdapter(this, R.layout.activity_statistics, values);
        mListView.setAdapter(adapter);
    }
}
