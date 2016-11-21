package com.model;

/**
 * Created by yangliu on 21/11/2016.
 */
public class JobSearchAddress {

    private String unit;

    private int maxJobDistance;

    private double longitude;

    private double latitude;


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getMaxJobDistance() {
        return maxJobDistance;
    }

    public void setMaxJobDistance(int maxJobDistance) {
        this.maxJobDistance = maxJobDistance;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
