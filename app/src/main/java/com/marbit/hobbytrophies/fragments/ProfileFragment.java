package com.marbit.hobbytrophies.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.adapters.profile.PagerProfileAdapter;
import com.marbit.hobbytrophies.fragments.profile.ProfileTrophiesFragment;
import com.marbit.hobbytrophies.interfaces.ProfileFragmentView;
import com.marbit.hobbytrophies.model.Game;
import com.marbit.hobbytrophies.model.User;
import com.marbit.hobbytrophies.overwrite.CircleTransform;
import com.marbit.hobbytrophies.overwrite.SecondaryMediumTextView;
import com.marbit.hobbytrophies.utilities.Utilities;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements ProfileFragmentView, ProfileTrophiesFragment.OnListenerProfileTrophiesFragment {

    private static final String ARG_PARAM1 = "USER-PSN-ID";
    private static final String ARG_PARAM2 = "IS-ANOTHER-PROFILE";

    private String userPsnId;
    private ImageView icAvatarProfile;
    private ImageView icPsnPlus;
    private TextView userNameProfile;
    private TextView platinumAmount;
    private TextView goldenAmount;
    private TextView silverAmount;
    private TextView bronzeAmount;
    private TextView totalAmount;
    private ProgressBar levelProgressBar;
    private SecondaryMediumTextView levelTextView;
    private ProfileOnFragmentInteractionListener mListener;
    private boolean isAnotherProfile;
    private PagerProfileAdapter pagerProfileAdapter;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String userPsnId, boolean isAnotherProfile) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userPsnId);
        args.putBoolean(ARG_PARAM2, isAnotherProfile);
        fragment.setArguments(args);
        return fragment;
    }

    public static ProfileFragment newInstance(String userPsnId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userPsnId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userPsnId = getArguments().getString(ARG_PARAM1);
            isAnotherProfile = getArguments().getBoolean(ARG_PARAM2, false);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        String userName = userPsnId;
        this.icAvatarProfile = (ImageView) view.findViewById(R.id.ic_avatar_profile);
        this.icPsnPlus = (ImageView) view.findViewById(R.id.ic_psn_plus_profile);
        this.userNameProfile = (TextView) view.findViewById(R.id.user_name_profile);
        this.platinumAmount = (TextView) view.findViewById(R.id.text_view_amount_platinum);
        this.goldenAmount = (TextView) view.findViewById(R.id.text_view_amount_gold_medal);
        this.silverAmount = (TextView) view.findViewById(R.id.text_view_amount_silver_medal);
        this.bronzeAmount = (TextView) view.findViewById(R.id.text_view_amount_bronze_medal);
        this.totalAmount = (TextView) view.findViewById(R.id.text_view_amount_total_medal);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Trofeos"));
        tabLayout.addTab(tabLayout.newTab().setText("Ventas"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        pagerProfileAdapter = new PagerProfileAdapter(getFragmentManager(), tabLayout.getTabCount(), userName);
        viewPager.setAdapter(pagerProfileAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        this.levelProgressBar = (ProgressBar) view.findViewById(R.id.profile_progress_bar_level);
        this.levelTextView = (SecondaryMediumTextView) view.findViewById(R.id.profile_level_text);

        return view;
    }

    @Override
    public void refreshContentUser(User user) {
        this.levelProgressBar.setProgress(user.getProgress());
        this.levelTextView.setText("Level: " + user.getLevel());
        this.userNameProfile.setText(user.getPsnId());
        Picasso.with(getContext())
                .load(user.getAvatarUrl())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .transform(new CircleTransform())
                .into(this.icAvatarProfile);
        if(user.isPsnPlus()){
            this.icPsnPlus.setVisibility(View.VISIBLE);
        }else {
            this.icPsnPlus.setVisibility(View.INVISIBLE);
        }
        this.platinumAmount.setText(String.valueOf(user.getPlatinum()));
        this.goldenAmount.setText(String.valueOf(user.getGold()));
        this.silverAmount.setText(String.valueOf(user.getSilver()));
        this.bronzeAmount.setText(String.valueOf(user.getBronze()));
        this.totalAmount.setText(Utilities.formatNumber(user.getTotal()));
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.profileOnFragmentInteractionListener(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProfileOnFragmentInteractionListener) {
            mListener = (ProfileOnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ProfileOnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public interface ProfileOnFragmentInteractionListener {
        void profileOnFragmentInteractionListener(Uri uri);
    }
}
