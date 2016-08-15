package com.sam.hotspot_build.Tabs;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.sam.hotspot_build.EndpointsAsyncTask;
import com.sam.hotspot_build.MainActivity;
import com.sam.hotspot_build.MyItems.MyCustomAdapterForItems;
import com.sam.hotspot_build.MyItems.MyItem;
import com.sam.hotspot_build.Places.GetPlaces;
import com.sam.hotspot_build.R;
import com.sam.hotspot_build.SlidingTabSetup.SlidingTabLayout;

import java.util.HashMap;

public class Tab1 extends Fragment implements OnMapReadyCallback, ResultCallback<Status> {

    public GoogleMap newMap;

    private final String TAG = getClass().getSimpleName();
    private String[] places;
    public String placeId;
    private MainActivity ma;
    private LocationManager manager;
    public MyItem clickedClusterItem;
    private View view;
    public ClusterManager<MyItem> mClusterManager;
    SupportMapFragment mapFragment;
    public HashMap<String, Bitmap> hash;
    public boolean clicked;
    public LatLng latLng;
    public SlidingTabLayout slidingTab;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_1, container, false);


        places = getResources().getStringArray(R.array.places);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.the_map);
        mapFragment.getMapAsync(this);

        slidingTab = new SlidingTabLayout(getContext());

        if (newMap != null) {
            setUpMap();
        }
        //new EndpointsAsyncTask().execute(new Pair<Context, Integer>(ma, 20));

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        newMap = map;

        setUpMap();
    }

    public void setUpMap() {

        // Enable MyLocation Layer of Google Map
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        newMap.setMyLocationEnabled(true);

        manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //set map type
        newMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ma.myLocation == null) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            manager.requestLocationUpdates(manager.GPS_PROVIDER, 0, 0, listener);
        } else {

            // Create a LatLng object for the current location
            latLng = new LatLng(ma.getLatitude(), ma.getLongitude());
            // Show the current location in Google Map
            newMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


            mClusterManager = new ClusterManager<MyItem>(getActivity(), newMap);
            //Sets infowindow adapter for CLuster Marker Items.
            addPlaces();
            setUpClusterer();

        }
    }

    public void addPlaces() {
        for (int i = 0; i < places.length; i++) {
            new GetPlaces(this,
                    places[i].toLowerCase().replace(
                            "-", "_").replace(" ", "_"), ma).execute();
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ma = (MainActivity) activity;
    }

    private LocationListener listener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "location update : " + location);

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            manager.removeUpdates(listener);
                }
            };

        private void setUpClusterer () {
            // Initialize the manager with the context and the map.
            // (Activity extends context, so we can pass 'this' in the constructor.)
            // Point the map's listeners at the listeners implemented by the cluster
            // manager.
            newMap.setOnCameraChangeListener(mClusterManager);

            //Expands Cluster to show GPS Locations using Camera
            newMap.setOnMarkerClickListener(mClusterManager);

            mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
                @Override
                public boolean onClusterClick(Cluster<MyItem> cluster) {

                    LatLngBounds.Builder builder = LatLngBounds.builder();
                    for (ClusterItem item : cluster.getItems()) {
                        builder.include(item.getPosition());
                    }
                    final LatLngBounds bounds = builder.build();
                    newMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                    Log.i("Clicked", "OnClusterCLICK");
                    return true;
                }
            });

            mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
                @Override
                public boolean onClusterItemClick(MyItem item) {

                    clickedClusterItem = item;
                    placeId = clickedClusterItem.getID();
                    clicked = true;

                    return false;
                }
            });
            newMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());

            mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MyCustomAdapterForItems(this, ma));
            newMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    ViewPager mPager = ma.getPager();
                    mPager.setCurrentItem(1);


                }
            });
        }

    public MyItem getClickedItem(){
        return clickedClusterItem;
    }


    public String getClickedItemTitle(){
        return clickedClusterItem.getTitle();
    }
    public void animateCamera(){

        LatLngBounds.Builder builder = LatLngBounds.builder();
        final LatLngBounds bounds = builder.build();
        builder.include(latLng);
        newMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));

    }
    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }
    @Override
    public void onResult(Status status) {

    }

}



