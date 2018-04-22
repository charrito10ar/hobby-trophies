package com.marbit.hobbytrophies.model.meeting;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Location implements Parcelable{

    @SerializedName("description")
    private String locality;
    private double latitude;
    private double longitude;
    private String countryCode;

    public Location(){
        locality = "";
        latitude = 0;
        longitude = 0;
        countryCode = "";
    }

    public Location(android.location.Location location) {
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
    }

    public Location(Parcel in) {
        locality = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        countryCode = in.readString();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public Location(String address, double latitude, double longitude) {
        this.locality = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(locality);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(countryCode);
    }
}
