package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private QueryUtils() {
    }

    public static List<Earthquake> fetchEarthQuakeData(String requestUrl) {

        URL url = createUrl(requestUrl);
        List<Earthquake> earthquakes = null;

        try {
            String jsonResponse = makeHttpRequest(url);
            earthquakes = extractFeaturesFromJson(jsonResponse);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return  earthquakes;
    }

    public static ArrayList<Earthquake> extractFeaturesFromJson(String jsonResponse) {

        if (jsonResponse == null) {
            return null;
        }

        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        try {

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray features = jsonObject.getJSONArray("features");

            for (int i = 0; i < features.length(); i++) {
                JSONObject properties = features.getJSONObject(i).getJSONObject("properties");

                double magnitude = properties.getDouble("mag");
                String place = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");

                Earthquake earthquake = new Earthquake(magnitude, place, url, time);
                earthquakes.add(earthquake);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return earthquakes;
    }

    private static URL createUrl(String usgsRequestUrl) {

        URL url = null;

        if (usgsRequestUrl == null) {
            return url;
        }
        try {
            url = new URL(usgsRequestUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;


        if (url == null) {
            return jsonResponse;
        }

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) {

        InputStreamReader streamReader = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();

        if (inputStream == null) {
            return null;
        }


        streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        reader = new BufferedReader(streamReader);

        try {
            String line = reader.readLine();

            while (line != null) {

                builder.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

}