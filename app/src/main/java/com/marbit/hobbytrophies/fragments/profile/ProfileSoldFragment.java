package com.marbit.hobbytrophies.fragments.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.adapters.profile.ItemProfileSalesAdapter;
import com.marbit.hobbytrophies.interfaces.market.ProfileSoldFragmentView;
import com.marbit.hobbytrophies.market.ItemDetailActivity;
import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.presenters.market.ProfileSoldFragmentPresenter;

import java.util.List;

public class ProfileSoldFragment extends Fragment implements ProfileSoldFragmentView, ItemProfileSalesAdapter.ItemDetailAdapterListener {

    private ProfileSoldFragmentPresenter presenter;
    private RecyclerView recyclerView;
    private ItemProfileSalesAdapter itemProfileSalesAdapter;
    private SwipeRefreshLayout swipeContainer;
    private StaggeredGridLayoutManager gaggeredGridLayoutManager;

    public ProfileSoldFragment() {
    }

    public static ProfileSoldFragment newInstance() {
        ProfileSoldFragment fragment = new ProfileSoldFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_sold, container, false);
        this.presenter = new ProfileSoldFragmentPresenter(getContext(), this);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_profile_sold);
        this.swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.fragment_profile_sold_swipe_refresh);
        this.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadItems();
            }
        });
        this.itemProfileSalesAdapter = new ItemProfileSalesAdapter(getContext(), this);
        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gaggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemProfileSalesAdapter);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        presenter.loadItems();
    }

    @Override
    public void loadItemsSuccess(List<Item> items) {
        itemProfileSalesAdapter.clearAll();
        itemProfileSalesAdapter.setItemList(items);
        itemProfileSalesAdapter.notifyDataSetChanged();
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
    public void openDetailActivity(Item item, String from) {
        Intent itemIntent = new Intent(getContext(), ItemDetailActivity.class);
        itemIntent.putExtra("ITEM", item);
        itemIntent.putExtra("FROM", from);
        startActivity(itemIntent);
    }
}
