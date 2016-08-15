package com.sam.hotspot_build.MyItems;

import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.sam.hotspot_build.MainActivity;
import com.sam.hotspot_build.R;
import com.sam.hotspot_build.Tabs.Tab1;

public class MyCustomAdapterForItems implements GoogleMap.InfoWindowAdapter {
    private ViewGroup myContentsView;
    private Tab1 tab;
    private MainActivity ma;
    private Marker currentMarker;
    public ImageView popUpImage;
    public TextView tvTitle;
    public TextView tvLat;

    public MyCustomAdapterForItems(Tab1 tab, final MainActivity ma) {
        this.tab = tab;
        this.ma = ma;
        myContentsView = (ViewGroup) this.tab.getActivity().getLayoutInflater().inflate(
                R.layout.info_window_layout, null);

        tvTitle = (TextView) myContentsView.findViewById(R.id.tv_title);
        tvLat = (TextView) myContentsView.findViewById(R.id.tv_snippet);
        popUpImage = (ImageView) myContentsView.findViewById(R.id.popUp);



    }


    @Override
    public View getInfoWindow(Marker marker) {
        currentMarker = marker;
        tvTitle.setText(tab.clickedClusterItem.getTitle());
        tvLat.setText(tab.clickedClusterItem.getSnippet());

         if(!currentMarker.isInfoWindowShown()){

             placePhotosAsync(tab.placeId);
             Log.i("async reached", "Reached Async");
        }

        return myContentsView;

    }


    @Override
    public View getInfoContents(final Marker marker) {

        return null;
    }


    private ResultCallback<PlacePhotoResult> mDisplayPhotoResultCallback
            = new ResultCallback<PlacePhotoResult>() {
        @Override
        public void onResult(PlacePhotoResult placePhotoResult) {
            if (!placePhotoResult.getStatus().isSuccess()) {
                Log.i("Picture Failed", "Failed");

                popUpImage.setImageResource(R.drawable.flame);
                return;
            }
            Log.i("Picture Sucess", " It Worked");

            popUpImage.setImageBitmap(placePhotoResult.getBitmap());
            //tab.popUpImage.invalidate();
            currentMarker.showInfoWindow();
        }
    };

    /**
     * Load a bitmap from the photos API asynchronously
     * by using buffers and result callbacks.
     */
    private void placePhotosAsync(String placeid) {
        final String placeId = placeid;
        Places.GeoDataApi.getPlacePhotos(ma.getGoogleApiClient(), placeId)
                .setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {


                    @Override
                    public void onResult(PlacePhotoMetadataResult photos) {
                        Log.i("Before if", "Before if statement error");
                        Log.i("status", "" +photos.getStatus());
                        if (!photos.getStatus().isSuccess()) {

                            Log.i("Error on photo", "Could not retrieve photo");
                            return;
                        }

                        PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                        if (photoMetadataBuffer.getCount() > 0) {
                            // Display the first bitmap in an ImageView in the size of the view
                            photoMetadataBuffer.get(0)
                                    .getScaledPhoto(ma.getGoogleApiClient(), tvLat.getWidth(), 500
                                    )
                                    .setResultCallback(mDisplayPhotoResultCallback);
                        }else{
                            popUpImage.setImageBitmap(BitmapFactory.decodeResource(ma.getResources(),
                                    R.drawable.flame));
                            currentMarker.showInfoWindow();
                        }
                        photoMetadataBuffer.release();
                    }
                });
    }
}
