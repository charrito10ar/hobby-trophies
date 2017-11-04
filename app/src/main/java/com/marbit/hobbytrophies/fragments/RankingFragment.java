package com.marbit.hobbytrophies.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.adapters.RankingAdapter;
import com.marbit.hobbytrophies.dialogs.DialogGeneric;
import com.marbit.hobbytrophies.interfaces.RankingFragmentView;
import com.marbit.hobbytrophies.model.User;
import com.marbit.hobbytrophies.overwrite.CircleTransform;
import com.marbit.hobbytrophies.presenters.RankingFragmentPresenter;
import com.marbit.hobbytrophies.utilities.Preferences;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RankingFragment extends Fragment implements RankingFragmentView{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RankingFragmentPresenter presenter;
    @BindView(R.id.recycler_view_ranking) RecyclerView recyclerView;
    private RankingAdapter rankingAdapter;
    @BindView(R.id.image_last_winner) ImageView imageLastWinner;
    @BindView(R.id.image_second) ImageView imageSecond;
    @BindView(R.id.image_third) ImageView imageThird;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    private String[] months;
    private Unbinder unbinder;

    private String mParam1;
    private String mParam2;

    private OnRankingFragmentInteractionListener mListener;

    public RankingFragment() {
    }

    public static RankingFragment newInstance(String param1, String param2) {
        RankingFragment fragment = new RankingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        this.months = getResources().getStringArray(R.array.months);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        unbinder = ButterKnife.bind(this, view);
        this.presenter = new RankingFragmentPresenter(getContext(), this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        this.rankingAdapter = new RankingAdapter(getContext());
        recyclerView.setAdapter(this.rankingAdapter);
        if(Preferences.isLoadRemoteRanking(getContext())){
            this.presenter.loadRemoteRanking();
        }else {
            this.presenter.loadLocalRanking();
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_meeting_ranking, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help_ranking) {
            DialogGeneric helpDialog = DialogGeneric.newInstance("Reglas para participar:", "- ");
            helpDialog.show(getFragmentManager(), "dialogHelp");
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRankingFragmentInteractionListener) {
            mListener = (OnRankingFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRankingFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setLastWinner(List<User> lastPodium) {
        this.progressBar.setVisibility(View.GONE);
        Picasso.with(getContext()).load(lastPodium.get(0).getAvatarUrl()).placeholder(R.drawable.avatar).transform(new CircleTransform()).into(this.imageLastWinner);
        Picasso.with(getContext()).load(lastPodium.get(1).getAvatarUrl()).placeholder(R.drawable.avatar).transform(new CircleTransform()).into(this.imageSecond);
        Picasso.with(getContext()).load(lastPodium.get(2).getAvatarUrl()).placeholder(R.drawable.avatar).transform(new CircleTransform()).into(this.imageThird);
    }

    @Override
    public void setRanking(List<User> listRanking) {
        this.rankingAdapter.clearAll();
        this.rankingAdapter.setList(listRanking);
        this.rankingAdapter.notifyDataSetChanged();
    }

    public interface OnRankingFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
