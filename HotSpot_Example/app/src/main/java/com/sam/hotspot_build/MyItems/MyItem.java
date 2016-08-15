package com.sam.hotspot_build.MyItems;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;


public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    private String mTitle;
    private String mSnippet;
    private String mID;

    public MyItem(double lat, double lng, String title, String snippet, String id ) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        mID = id;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
    public String getID(){
        return mID;
    }
    public String getTitle(){
        return mTitle;
    }
    public void setTitle(String title){
        mTitle = title;
    }

    public String getSnippet(){
        return mSnippet;
    }
    public void setSnippet(String snippet){
        mSnippet = snippet;
    }


}
