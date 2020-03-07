package com.example.android.quakereport;

public class Earthquake {


    private double mMagnitude;

    private String mLocation;

    private Long mTime;

    private String mUrl;


    public double getMagnitude() {
        return mMagnitude;
    }

    public void setMagnitude(double mMagnitude) {
        this.mMagnitude = mMagnitude;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public Long getTime() {
        return mTime;
    }

    public void setTime(Long mtime) {
        this.mTime = mtime;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

//    public double getMagnitude(){ return  mMagnitude; }
//
//    public String getLocation(){ return mLocation; }
//
//    public Long getTime(){ return  mtime; }
//
//    public String getUrl(){ return  mUrl; }

}
