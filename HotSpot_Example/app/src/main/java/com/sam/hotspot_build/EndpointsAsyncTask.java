package com.sam.hotspot_build;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.Toast;

import com.example.sam.myapplication.backend.myApi.MyApi;
import com.example.sam.myapplication.backend.myApi.model.MyBean;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

public class EndpointsAsyncTask extends AsyncTask<Pair<Context, Integer>, Void, String> {
    private static MyApi myApiService = null;
    private Context context;
    private int age;
    @Override
    protected String doInBackground(Pair<Context, Integer>... params) {
        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://keen-answer-95700.appspot.com/_ah/api/");

            myApiService = builder.build();
        }

        context = params[0].first;
        age = params[0].second;

        try {
            MyBean bean = myApiService.setAge(age).execute();
            return bean.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }
}