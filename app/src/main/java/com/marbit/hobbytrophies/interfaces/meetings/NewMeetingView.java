package com.marbit.hobbytrophies.interfaces.meetings;

import com.google.android.gms.location.places.Place;

public interface NewMeetingView {
    void setLocationLayout(int gameType);
    void showLocationInput();
    void hideLocationInput();
    void setNamePlaceSelected(Place placeSelected);
}
