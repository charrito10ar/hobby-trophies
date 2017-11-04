package com.marbit.hobbytrophies.adapters.profile;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.marbit.hobbytrophies.fragments.profile.ProfileSalesFragment;
import com.marbit.hobbytrophies.fragments.profile.ProfileSoldFragment;
import com.marbit.hobbytrophies.fragments.profile.ProfileTrophiesFragment;
import com.marbit.hobbytrophies.model.User;


public class PagerProfileAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private ProfileTrophiesFragment tab1;
    private String userName;

    public PagerProfileAdapter(FragmentManager fm, int NumOfTabs, String userName) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.userName = userName;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                tab1 = ProfileTrophiesFragment.newInstance(userName);
                return tab1;
            case 1:
                ProfileSalesFragment tab2 = new ProfileSalesFragment();
                return tab2;
            case 2:
                ProfileSoldFragment tab3 = new ProfileSoldFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
