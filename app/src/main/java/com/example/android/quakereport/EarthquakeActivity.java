/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EarthquakeActivity extends AppCompatActivity {

    ArrayList<Earthquake> earthquakeArrayList = new ArrayList<>();
    EarthquakeAdapter earthquakeAdapter;
    TextView mEmptyState;
    ProgressBar mProgressBar;
    ListView earthquakeListView;

//    private static final String USGS_REQUEST_URL =
//            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=2&limit=20";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);


        mEmptyState = findViewById(R.id.empty_view);
        mProgressBar = findViewById(R.id.progressBar);
        earthquakeListView = findViewById(R.id.list);


        getJSONResponse();
    }

    private void getJSONResponse() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(InterfaceApi.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        InterfaceApi interfaceApi = retrofit.create(InterfaceApi.class);

        Call<String> call = interfaceApi.getEarthquake();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        //Toast.makeText(EarthquakeActivity.this,"response is successful",Toast.LENGTH_LONG).show();
                        Log.i("onSuccess", response.body());
                        String jsonResponse = response.body();
                        writeListView(jsonResponse);

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
                        Toast.makeText(EarthquakeActivity.this, "Nothing returned", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mEmptyState.setText(R.string.no_internet_connection);
                earthquakeListView.setEmptyView(mEmptyState);
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(EarthquakeActivity.this, "No Internet connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void writeListView(String response) {
        try {
            //getting the whole json object from the response
            JSONObject obj = new JSONObject(response);
            //Toast.makeText(EarthquakeActivity.this,"hey status value == "+obj.getJSONObject("metadata").getInt("status"),Toast.LENGTH_LONG).show();
            if (obj.getJSONObject("metadata").getInt("status") == 200) {

                JSONArray dataArray = obj.getJSONArray("features");

                for (int i = 0; i < dataArray.length(); i++) {

                    Earthquake earthquake = new Earthquake();
                    JSONObject dataObj = dataArray.getJSONObject(i).getJSONObject("properties");

                    earthquake.setLocation(dataObj.getString("place"));
                    earthquake.setMagnitude(dataObj.getDouble("mag"));
                    earthquake.setTime(dataObj.getLong("time"));
                    earthquake.setUrl(dataObj.getString("url"));

                    earthquakeArrayList.add(earthquake);

                }
                //check if there is no earthquakes
                if (earthquakeArrayList == null) {
                    mProgressBar.setVisibility(View.GONE);
                    mEmptyState.setText(R.string.no_earthquake_found);
                    earthquakeListView.setEmptyView(mEmptyState);
                } else {

                    earthquakeAdapter = new EarthquakeAdapter(this, earthquakeArrayList);
                    earthquakeListView = findViewById(R.id.list);

                    // Set the adapter on the {@link ListView}
                    // so the list can be populated in the user interface
                    earthquakeListView.setAdapter(earthquakeAdapter);
                    mProgressBar.setVisibility(View.GONE);

                    earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String url = earthquakeArrayList.get(i).getUrl();

                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                        }
                    });
                }

            }else {
                Toast.makeText(EarthquakeActivity.this, "couldn't connect to server", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
