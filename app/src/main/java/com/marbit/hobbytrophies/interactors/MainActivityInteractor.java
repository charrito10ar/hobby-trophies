package com.marbit.hobbytrophies.interactors;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.marbit.hobbytrophies.dao.LocationDAO;
import com.marbit.hobbytrophies.dao.bodies.LocationUser;
import com.marbit.hobbytrophies.utilities.Preferences;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivityInteractor {
    private Context context;

    public MainActivityInteractor(Context applicationContext) {
        this.context = applicationContext;
    }

    public void saveUserLocation(Location location) {
        LocationUser locationUser = new LocationUser(location);
        Address address = getAddress(location.getLatitude(), location.getLongitude());
        locationUser.setCountryCode(address.getCountryCode());
        locationUser.setLocality(address.getLocality());
        LocationDAO locationDAO = new LocationDAO(context);
        locationDAO.insertLocation(Preferences.getUserId(context), locationUser);
        Preferences.saveUserLocation(context, locationUser);
    }

    public Address getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}