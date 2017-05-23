package com.marbit.hobbytrophies;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.marbit.hobbytrophies.dialogs.DialogEditMeeting;
import com.marbit.hobbytrophies.fragments.ProfileFragment;

public class ProfileActivity extends AppCompatActivity implements ProfileFragment.ProfileOnFragmentInteractionListener {
    private String userPsnId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.userPsnId  = getIntent().getStringExtra("USER-PSN-ID");
        Fragment fragment = ProfileFragment.newInstance(this.userPsnId, true);
        String fragmentTag = "Profile";
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_fragment_profile_activity, fragment, fragmentTag);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void profileOnFragmentInteractionListener(Uri uri) {

    }
}
