package com.example.android.quakereport;

public class Earthquake {


    private double mMagnitude;

    private String mLocation;

    private Long mtime;

    private String mUrl;

    public Earthquake(double magnitude,String location,String url,Long time){
     mMagnitude = magnitude;
     mLocation = location;
     mUrl = url;
     mtime = time;
    }

    public double getMagnitude(){ return  mMagnitude; }

    public String getLocation(){ return mLocation; }

    public Long getTime(){ return  mtime; }

    public String getUrl(){ return  mUrl; }

}
