package com.robinmayo.crossingroads;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;


public class WorldActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "WorldActivity";
    protected static final String GAME_FILE = "game.txt";

    protected FloatingActionButton profileButton;
    protected FloatingActionButton scoreButton;
    protected GoogleMap mMap;
    private static Intent musicIntent;
    private static File gameFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate(Bundle savedInstanceState)");
        setContentView(R.layout.activity_world);

        new Player();

        // getApplicationContext().getFilesDir() is an internal directory in the application.
        gameFile = new File(getApplicationContext().getFilesDir(), GAME_FILE);
        WebParser webParser = new WebParser(gameFile);
        webParser.execute();

        musicIntent = new Intent(this, UnboundedService.class);
        startService(musicIntent);

        // Buttons :
        profileButton = findViewById(R.id.profileButton);
        scoreButton = findViewById(R.id.scoreButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("View.OnClickListener()", "********** goTo : ProfileActivity");
                Intent intent = new Intent(WorldActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("View.OnClickListener()", "********** goTo : StatisticsActivity");
                Intent intent = new Intent(WorldActivity.this,
                        StatisticsActivity.class);
                startActivity(intent);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // The activity is note visible.
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    // The application is note visible.
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");

        this.stopService(musicIntent);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
