package com.sam.hotspot_build;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;
import com.sam.hotspot_build.MyItems.MyItem;
import com.sam.hotspot_build.Tabs.Tab1;

import java.util.ArrayList;

public class SplashScreen extends Activity {

    LocationManager manager;
    ArrayList<String> locationArray;
    private double latitude;
    private double longitude;
    private LatLng latLng;
    private String[] places;
    private ClusterManager<MyItem> mClusterManager;
    Location myLocation;
    String provider;
    Criteria criteria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        /**
         * Showing splashscreen while making network calls to download necessary
         * data before launching the app Will use AsyncTask to make http call
         */
        new PrefetchData().execute();

    }

    /**
     * Async Task to make http call
     */
    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls

        }



        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // After completing http call
            // will close this activity and lauch main activity
            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(i);

            // close this activity
            finish();
        }

    }

}