package com.robinmayo.crossingroads.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.robinmayo.crossingroads.Level;
import com.robinmayo.crossingroads.LevelDescription;
import com.robinmayo.crossingroads.Player;
import com.robinmayo.crossingroads.R;
import com.robinmayo.crossingroads.UnboundedService;
import com.robinmayo.crossingroads.WebParser;
import com.robinmayo.crossingroads.interfaces.TaskDelegate;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class WorldActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMarkerClickListener {

    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final int INITIAL_REQUEST=1337;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

    private static final String TAG = "WorldActivity";
    private static final String GAME_FILE = "game.txt";

    protected FloatingActionButton profileButton;
    protected FloatingActionButton scoreButton;
    protected GoogleMap mMap;
    private static Intent musicIntent;

    private TaskDelegate taskDelegate = new TaskDelegate() {
        @Override
        public void taskCompletionResult() {
            addMarker();
        }
    };

    public static Level currentLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate(Bundle savedInstanceState)");
        setContentView(R.layout.activity_world);

        new Player();

        // getApplicationContext().getFilesDir() is an internal directory in the application.
        getApplicationContext().getFilesDir();
        File gameFile = new File(getApplicationContext().getFilesDir(), GAME_FILE);
        WebParser webParser = new WebParser(getApplicationContext().getFilesDir(), gameFile,
                taskDelegate);
        webParser.execute();

        // Do not play music on the main thread.
        Thread thread = new Thread(){
            @Override
            public void run() {
                musicIntent = new Intent(WorldActivity.this, UnboundedService.class);
                startService(musicIntent);
            }
        };
        thread.start();

        requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);

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
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.e(TAG, "ERROR in onCreate(Bundle savedInstanceState) : can not access to map "
                    + " by mapFragment !");
        }
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        Log.d(TAG, "onPointerCaptureChanged(...)");
    }


    /* **********   Implement OnMapReadyCallback :  ********** */

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
        mMap.setOnMarkerClickListener(this);
        // GoogleMap.OnMapClickListener clickListener = new GoogleMap.OnMapClickListener();
        // mMap.setOnMapClickListener(clickListener);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e(TAG, "ERROR : in onMapReady(GoogleMap googleMap) : can not acces to user" +
                    " position. The game is not playable !");
            Toast.makeText(this, R.string.error_invalid_location_permission,
                    Toast.LENGTH_SHORT).show();
            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
        } else {
            mMap.setMyLocationEnabled(true);

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager != null) {

                // Define a listener that responds to location updates
                LocationListener locationListener = new LocationListener() {

                    public void onLocationChanged(Location location) {
                        // Called when a new location is found by the network location provider.
                        Log.d(TAG, "onLocationChanged(...)");
                        makeUseOfNewLocation(location);
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        Log.d(TAG, "onStatusChanged(...)");
                    }

                    public void onProviderEnabled(String provider) {
                        Log.d(TAG, "onProviderEnabled(...)");
                    }

                    public void onProviderDisabled(String provider) {
                        Log.d(TAG, "onProviderDisabled(...)");
                    }
                };
                // Register the listener with the Location Manager to receive location updates.
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        30000, 0, locationListener);
            } else {
                Log.e(TAG, "ERROR in onMapReady(GoogleMap googleMap) : can not acces to user"
                        + "location by locationManager. The game is not playable !");
            }
        }
    }


    /* **********   Implement GoogleMap.OnMyLocationButtonClickListener :   ********** */

    @Override
    public boolean onMyLocationButtonClick() {
        Log.d(TAG, "onMyLocationButtonClick()");
        return false;
    }


    /* **********   Implement GoogleMap.OnMyLocationClickListener :   ********** */

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Log.d(TAG, "onMyLocationClick(...)");
        makeUseOfNewLocation(location);
    }


    /* **********   GoogleMap.OnMarkerClickListener :   ********** */

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.i(TAG, "onMarkerClick(...) : " + marker.getTitle());

        if (LevelDescription.getLevel(Integer.parseInt(marker.getTitle())) != null) {
            currentLevel = LevelDescription.getLevel(Integer.parseInt(marker.getTitle()));
            new GameActivity(LevelDescription.getLevel(Integer.parseInt(marker.getTitle())));
            Intent intent = new Intent(WorldActivity.this, GameActivity.class);
            startActivity(intent);
        }
        return false;
    }


    /* **********   Utility method :    ********** */

    private void makeUseOfNewLocation(Location location) {
        Log.i(TAG, "makeUseOfNewLocation - User is located at : latitude = " +
                location.getLatitude() + ", longitude " + location.getLongitude() + ".");

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> address = geoCoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            Log.i(TAG, "makeUseOfNewLocation - " + address.get(0).getLocality());
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void addMarker() {
        Log.d(TAG, "onMapReady() - BEFORE - mMap.addMarker(...)");
        Level level;

        for (int i = 0; i < LevelDescription.getNbLevels(); i++) {
            level = LevelDescription.getLevel(i);
            if (LevelDescription.getLevel(i) != null) {
                mMap.addMarker(new MarkerOptions().position(level.getPoint())
                        .icon(BitmapDescriptorFactory.fromPath(level.getPin().getPath())).draggable(false)
                        .alpha(0.7f)
                        .visible(true)
                        .title(String.valueOf(level.getDifficulty()))
                        .snippet("Snippet for this marker"));
            } else {
                Log.w(TAG, "onMapReady() - Level " + i + " not initialized.");
            }
        }
    }
}
