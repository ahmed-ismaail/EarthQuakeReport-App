package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private String mUrl;

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    public EarthquakeLoader(Context context, String usgsRequestUrl) {
        super(context);
        mUrl = usgsRequestUrl;
    }

    @Override
    public List<Earthquake> loadInBackground() {
        if(mUrl == null) {
            return null;
        }

        return QueryUtils.fetchEarthQuakeData(mUrl);

    }
}
