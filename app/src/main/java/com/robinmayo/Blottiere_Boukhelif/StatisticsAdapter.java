package com.robinmayo.Blottiere_Boukhelif;

import android.content.Context;
import android.widget.ArrayAdapter;

public class StatisticsAdapter extends ArrayAdapter<String> {

    private String[] values;
    private Context context;

    public StatisticsAdapter(Context context, int position, String[] values) {
        super(context, position);

        this.values = values;
        this.context = context;
    }

    public void getView() {
    }
}
