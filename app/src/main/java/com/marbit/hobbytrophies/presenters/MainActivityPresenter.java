package com.marbit.hobbytrophies.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;
import com.marbit.hobbytrophies.BaseActivity;
import com.marbit.hobbytrophies.MainActivity;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.adapters.permissions.LocationPermissionListener;
import com.marbit.hobbytrophies.interactors.MainActivityInteractor;
import com.marbit.hobbytrophies.interfaces.MainActivityView;

public class MainActivityPresenter {
    private MainActivityView mainActivityView;
    private Context context;
    private PermissionListener dialogPermisionListener;
    private MainActivityInteractor interactor;

    public MainActivityPresenter(Context applicationContext, MainActivityView mainActivityView) {
        this.mainActivityView = mainActivityView;
        this.context = applicationContext;
        this.interactor = new MainActivityInteractor(applicationContext);
    }

    public void handleDeepLinks(Activity activity, Intent intent) {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener(activity, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            String type = deepLink.getQueryParameter("type");
                            switch (type){
                                case "ITEM":
                                    String itemId = deepLink.getQueryParameter("itemId");
                                    mainActivityView.openActivityItemDetail(itemId);
                                    break;
                                case "MEETING":
                                    String meetingId = deepLink.getQueryParameter("meetingId");
                                    mainActivityView.openActivityMeetingDetail(meetingId);
                                    break;
                            }
                        }


                    }
                })
                .addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "getDynamicLink:onFailure", e);
                    }
                });
    }

    public MultiplePermissionsListener getDialogPermissionListener() {
        return new LocationPermissionListener(mainActivityView);
    }

    public void saveUserLocation(Location location) {
        interactor.saveUserLocation(location);
    }
}