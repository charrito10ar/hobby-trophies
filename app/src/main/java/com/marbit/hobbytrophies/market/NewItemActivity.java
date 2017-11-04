package com.marbit.hobbytrophies.market;

import android.support.v4.app.*;
import android.support.v7.app.*;
import android.os.Bundle;
import android.view.MenuItem;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.fragments.AllGamesFragment;
import com.marbit.hobbytrophies.fragments.market.FragmentItemDetail;
import com.marbit.hobbytrophies.fragments.market.FragmentItemType;


public class NewItemActivity extends AppCompatActivity implements FragmentItemType.OnFragmentItemTypeInteractionListener,
        FragmentItemDetail.OnFragmentItemDetailInteractionListener, AllGamesFragment.OnAllGamesFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        setFirstFragment(FragmentItemType.newInstance("",""));
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setFirstFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onSelectType(int itemType) {
        setFragment(FragmentItemDetail.newInstance(itemType));
    }


    @Override
    public void onFragmentInteraction() {

    }
}
