package com.marbit.hobbytrophies.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.marbit.hobbytrophies.interfaces.MainActivityView;

public class MainActivityPresenter {
    private MainActivityView mainActivityView;
    private Context context;
    public MainActivityPresenter(Context applicationContext, MainActivityView mainActivityView) {
        this.mainActivityView = mainActivityView;
        this.context = applicationContext;
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

}
