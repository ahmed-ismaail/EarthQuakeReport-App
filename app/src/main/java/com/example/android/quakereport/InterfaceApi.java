package com.example.android.quakereport;

import retrofit2.Call;
import retrofit2.http.GET;


interface InterfaceApi {
    String URL = "https://earthquake.usgs.gov/fdsnws/event/1/";

    @GET("query?format=geojson&orderby=time&minmag=2&limit=20")
    Call<String> getEarthquake();

}


