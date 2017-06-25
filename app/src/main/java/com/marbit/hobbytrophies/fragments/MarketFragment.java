package com.marbit.hobbytrophies.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marbit.hobbytrophies.NewItemActivity;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.adapters.profile.ItemProfileSalesAdapter;
import com.marbit.hobbytrophies.interfaces.market.MarketFragmentView;
import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.presenters.market.MarketFragmentPresenter;

import java.util.List;

public class MarketFragment extends Fragment implements MarketFragmentView, View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    private String mParam2;

    private OnMarketFragmentInteractionListener mListener;
    private MarketFragmentPresenter presenter;
    private RecyclerView recyclerView;
    private ItemProfileSalesAdapter itemProfileSalesAdapter;
    private SwipeRefreshLayout swipeContainer;
    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private FloatingActionButton fabNewItem;


    public MarketFragment() {
        this.presenter = new MarketFragmentPresenter(getContext(), this);
    }

    public static MarketFragment newInstance(String param1, String param2) {
        MarketFragment fragment = new MarketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_profile_sales);
        this.swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.fragment_profile_sales_swipe_refresh);
        this.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadItems();
            }
        });
        this.itemProfileSalesAdapter = new ItemProfileSalesAdapter(getContext());
        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gaggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemProfileSalesAdapter);
        this.fabNewItem = (FloatingActionButton) view.findViewById(R.id.fab_new_item);
        this.fabNewItem.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        presenter.loadItems();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMarketFragmentInteractionListener) {
            mListener = (OnMarketFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMarketFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void showLoading() {
        swipeContainer.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void loadItemSuccess(List<Item> items) {
        itemProfileSalesAdapter.clearAll();
        itemProfileSalesAdapter.setItemList(items);
        itemProfileSalesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_new_item:
                startActivity(new Intent(getContext(), NewItemActivity.class));
        }
    }


    public interface OnMarketFragmentInteractionListener {
        void onFragmentInteraction();
    }
}
