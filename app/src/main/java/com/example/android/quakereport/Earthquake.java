package com.example.android.quakereport;

public class Earthquake {

    private String mLocation, mUrl;
    private double mMagnitude;
    private long mTimeInMilliseconds;


    public Earthquake(double magnitude, String location, long timeInMilliseconds, String url) {
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public void setMagnitude(double magnitude) {
        mMagnitude = magnitude;
    }

    public long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}