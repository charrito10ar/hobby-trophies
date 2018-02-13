package com.marbit.hobbytrophies.adapters.permissions;

import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.marbit.hobbytrophies.interfaces.MainActivityView;

import java.util.List;


public class LocationPermissionListener implements MultiplePermissionsListener {

    private MainActivityView activity;

    public LocationPermissionListener(MainActivityView activity) {
        this.activity = activity;
    }

    @Override
    public void onPermissionsChecked(MultiplePermissionsReport report) {
        for(PermissionGrantedResponse permission: report.getGrantedPermissionResponses()){
            activity.showPermissionGranted(permission.getPermissionName());
        }
        for(PermissionDeniedResponse permissionDeniedResponse: report.getDeniedPermissionResponses()){
            activity.showPermissionDenied(permissionDeniedResponse.getPermissionName(), permissionDeniedResponse.isPermanentlyDenied());
        }
    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
        activity.showPermissionRationale(token);
    }
}
