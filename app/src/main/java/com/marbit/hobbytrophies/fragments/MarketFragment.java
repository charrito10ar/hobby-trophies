package com.marbit.hobbytrophies.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.marbit.hobbytrophies.dialogs.DialogAlertLogin;
import com.marbit.hobbytrophies.market.ItemDetailActivity;
import com.marbit.hobbytrophies.market.NewItemActivity;
import com.marbit.hobbytrophies.wishes.NewWishActivity;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.adapters.profile.ItemProfileSalesAdapter;
import com.marbit.hobbytrophies.dialogs.DialogFilterItemsMarket;
import com.marbit.hobbytrophies.interfaces.market.MarketFragmentView;
import com.marbit.hobbytrophies.model.market.Filter;
import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.presenters.market.MarketFragmentPresenter;
import com.marbit.hobbytrophies.utilities.Constants;
import com.marbit.hobbytrophies.utilities.Preferences;

import java.util.List;

public class MarketFragment extends Fragment implements MarketFragmentView, View.OnClickListener, DialogFilterItemsMarket.OnDialogFilterItemsInteractionListener, ItemProfileSalesAdapter.ItemDetailAdapterListener {

    private OnMarketFragmentInteractionListener mListener;
    private MarketFragmentPresenter presenter;
    private RecyclerView recyclerView;
    private ItemProfileSalesAdapter itemProfileSalesAdapter;
    private SwipeRefreshLayout swipeContainer;
    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private FloatingActionButton fabNewItem;
    private FrameLayout buttonAddWishList;
    private RelativeLayout layoutEmptyList;
    private boolean isFilter;

    public MarketFragment() {

    }

    public static MarketFragment newInstance() {
        return new MarketFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);
        this.presenter = new MarketFragmentPresenter(getContext(), this);
        this.recyclerView = view.findViewById(R.id.recycler_view_profile_sales);
        this.layoutEmptyList = view.findViewById(R.id.layout_empty_list);
        this.swipeContainer = view.findViewById(R.id.fragment_profile_sales_swipe_refresh);
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
        this.fabNewItem = view.findViewById(R.id.fab_new_item);
        this.fabNewItem.setOnClickListener(this);
        this.buttonAddWishList = view.findViewById(R.id.button_add_wish_list);
        this.buttonAddWishList.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume(){
        super.onResume();
        presenter.loadItems();
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
        checkEmptyList(items);
        itemProfileSalesAdapter.clearAll();
        itemProfileSalesAdapter.setItemList(items);
        itemProfileSalesAdapter.notifyDataSetChanged();
        checkButtonWishList();
    }

    private void checkEmptyList(List<Item> items) {
        if(items.size()<= 0){
            showEmptyLayout();
        }else {
            hideEmptyLayout();
        }
    }

    @Override
    public void checkButtonWishList() {
        if(isFilter){
            buttonAddWishList.setVisibility(View.VISIBLE);
            isFilter = false;
        }else{
            buttonAddWishList.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEmptyLayout() {
        layoutEmptyList.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyLayout() {
        layoutEmptyList.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if(Preferences.getBoolean(getContext(), Constants.PREFERENCE_IS_USER_LOGIN)){
            switch (v.getId()){
                case R.id.fab_new_item:
                    startActivity(new Intent(getContext(), NewItemActivity.class));
                    break;
                case R.id.button_add_wish_list:
                    startActivity(new Intent(getContext(), NewWishActivity.class));
                    break;
            }
        }else {
            DialogAlertLogin dialogAlertLogin = DialogAlertLogin.newInstance();
            dialogAlertLogin.show(getFragmentManager(), "dialogAlertLogin");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_market, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                DialogFilterItemsMarket dialogFilterItemsMarket = DialogFilterItemsMarket.newInstance();
                dialogFilterItemsMarket.setTargetFragment(this, 10);
                dialogFilterItemsMarket.show(getFragmentManager(), "DialogFilterItemsMarket");
        }
        return true;
    }

    @Override
    public void applyFilter(Filter filter) {
        presenter.applyFilter(filter);
        this.isFilter = true;
    }

    @Override
    public void openDetailActivity(Item item, String from) {
        Intent itemIntent = new Intent(getContext(), ItemDetailActivity.class);
        itemIntent.putExtra("ITEM", item);
        itemIntent.putExtra("FROM", from);
        startActivity(itemIntent);
    }


    public interface OnMarketFragmentInteractionListener {
        void onFragmentInteraction();
    }
}
