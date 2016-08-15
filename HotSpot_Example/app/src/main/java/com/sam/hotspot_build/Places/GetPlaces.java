package com.sam.hotspot_build.Places;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.sam.hotspot_build.Geofencing.GeofenceTransitionsIntentService;
import com.sam.hotspot_build.MainActivity;
import com.sam.hotspot_build.MyItems.MyItem;
import com.sam.hotspot_build.Tabs.Tab1;

import java.util.ArrayList;
import java.util.List;

public class GetPlaces extends AsyncTask<Void, Void, ArrayList<Place>> implements ResultCallback<Status> {
    private Tab1 tab;
    private String places;
    private MainActivity ma;
    public float geofenceRadius;

    List<Geofence> mGeofenceList = new ArrayList<Geofence>();
    private PendingIntent mGeofencePendingIntent;
    private final String TAG = getClass().getSimpleName();

    public GetPlaces(Tab1 tab, String places, MainActivity ma) {
        this.tab = tab;
        this.places = places;
        this.ma = ma;
    }

    @Override
    protected void onPostExecute(ArrayList<Place> result) {
        super.onPostExecute(result);
        MyItem currentPosition = new MyItem(ma.getMyLocation().getLatitude(), ma.getMyLocation().getLongitude(), "Current Position", "Current Position", "me");
        geofenceRadius = 12;

        //Loops through each Google Place in the "results" arrayList
        //and adds them to the ClusterManager which displays them on screen.
        for (int i = 0; i < result.size(); i++) {
            double placeLatitude = result.get(i).getLatitude();
            double placeLongitude = result.get(i).getLongitude();
            String title = result.get(i).getName();
            String snippet = result.get(i).getVicinity();
            String id = result.get(i).getId();


            if (!result.get(i).getName().contains("Dunkin' Donuts")) {
                MyItem offsetItem = new MyItem(placeLatitude, placeLongitude, title, snippet, id);
                tab.mClusterManager.addItem(offsetItem);

                if (!mGeofenceList.contains(offsetItem.getID())) {

                    mGeofenceList.add(new Geofence.Builder()
                            // Set the request ID of the geofence. This is a string to identify this
                            // geofence.
                            .setRequestId(offsetItem.getTitle())

                            .setCircularRegion(
                                    placeLatitude,
                                    placeLongitude,
                                    geofenceRadius
                            )
                            .setExpirationDuration(Geofence.NEVER_EXPIRE)
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL |
                                    Geofence.GEOFENCE_TRANSITION_ENTER |
                                    Geofence.GEOFENCE_TRANSITION_EXIT)
                            .setLoiteringDelay(10000).build());

                    Circle circle = tab.newMap.addCircle(new CircleOptions()
                            .center(offsetItem.getPosition())
                            .radius(geofenceRadius)
                            .strokeColor(Color.TRANSPARENT)
                            .fillColor(Color.argb(40, 0, 0, 0)));

                }
            } else {
                Log.i("Filtered", "Dunkin' Donuts");

            }
            tab.newMap.animateCamera(CameraUpdateFactory.zoomTo(0));
            // Zoom in the Google Map
            tab.newMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        }


        if (ActivityCompat.checkSelfPermission(ma, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.GeofencingApi.addGeofences(
                ma.mGoogleApiClient,
                getGeofencingRequest(),
                getGeofencePendingIntent()
        ).setResultCallback(this);
    }
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofences(mGeofenceList);
        Log.i("geofencingRequest", "GeofencingREquest reached");
        return builder.build();
    }
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Log.i("Intent", "Intent reached");
        Intent intent = new Intent(tab.getActivity(), GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(tab.getActivity(), 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Place> doInBackground(Void... arg0) {
        PlacesService service = new PlacesService(
                "AIzaSyD2060G_VzDK6rSSjZmd2ka6AwR2oGHwQ0");
        ArrayList<Place> findPlaces = service.findPlaces(ma.getMyLocation().getLatitude(),
                ma.getMyLocation().getLongitude(), places);

        for (int i = 0; i < findPlaces.size(); i++) {

            Place placeDetail = findPlaces.get(i);
            Log.e(TAG, "places : " + placeDetail.getName() + " " + placeDetail.getIcon());
        }
        return findPlaces;
    }

    @Override
    public void onResult(com.google.android.gms.common.api.Status status) {


    }
}

