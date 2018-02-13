package com.marbit.hobbytrophies.interfaces;

import com.karumi.dexter.PermissionToken;

public interface MainActivityView {

    void openActivityItemDetail(String itemId);

    void openActivityMeetingDetail(String meetingId);

    void showPermissionGranted(String permissionName);

    void showPermissionDenied(String permissionName, boolean permanentlyDenied);

    void showPermissionRationale(PermissionToken token);
}
