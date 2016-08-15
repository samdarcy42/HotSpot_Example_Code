package com.sam.hotspot_build.Places;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlacesService {

    private String API_KEY = "AIzaSyD2060G_VzDK6rSSjZmd2ka6AwR2oGHwQ0";

    public PlacesService(String apikey) {
        this.API_KEY = apikey;
    }

    public ArrayList<Place> findPlaces(double latitude, double longitude,
                                       String placeSpecification) {

        String urlString = makeUrl(latitude, longitude, placeSpecification);

        try {
            String json = getJSON(urlString);

            System.out.println(json);
            JSONObject object = new JSONObject(json);
            JSONArray array = object.getJSONArray("results");

            ArrayList<Place> arrayList = new ArrayList<Place>();
            for (int i = 0; i < array.length(); i++) {
                try {
                    Place place = Place
                            .jsonToPointReference((JSONObject) array.get(i));
                    Log.v("Places Services ", "" + place);
                    arrayList.add(place);
                } catch (Exception e) {
                }
            }
            return arrayList;
        } catch (JSONException ex) {
            Logger.getLogger(PlacesService.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        return null;
    }

    private String makeUrl(double latitude, double longitude, String place) {
        StringBuilder urlString = new StringBuilder(
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?");

        if (place.equals("")) {
            urlString.append("&location=");
            urlString.append(Double.toString(latitude));
            urlString.append(",");
            urlString.append(Double.toString(longitude));
            urlString.append("&radius=500");
            urlString.append("&sensor=false&key=" + API_KEY);
        } else {
            urlString.append("&location=");
            urlString.append(Double.toString(latitude));
            urlString.append(",");
            urlString.append(Double.toString(longitude));
            urlString.append("&radius=5000");
            urlString.append("&types=" + place);
            urlString.append("&sensor=false&key=" + API_KEY);
        }
        return urlString.toString();
    }

    protected String getJSON(String url) {
        return getUrlContents(url);
    }

    private String getUrlContents(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()), 8);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}

