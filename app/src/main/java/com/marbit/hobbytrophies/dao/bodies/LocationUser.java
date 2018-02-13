package com.marbit.hobbytrophies.dao.bodies;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class LocationUser implements Parcelable{
    private double longitud;
    private double latitud;
    private String countryCode;
    private String locality;

    public LocationUser(Location location) {
        this.longitud = location.getLongitude();
        this.latitud = location.getLatitude();
    }

    public LocationUser(){}

    protected LocationUser(Parcel in) {
        longitud = in.readDouble();
        latitud = in.readDouble();
        countryCode = in.readString();
        locality = in.readString();
    }

    public static final Creator<LocationUser> CREATOR = new Creator<LocationUser>() {
        @Override
        public LocationUser createFromParcel(Parcel in) {
            return new LocationUser(in);
        }

        @Override
        public LocationUser[] newArray(int size) {
            return new LocationUser[size];
        }
    };

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
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
        dest.writeDouble(longitud);
        dest.writeDouble(latitud);
        dest.writeString(countryCode);
        dest.writeString(locality);
    }
}
