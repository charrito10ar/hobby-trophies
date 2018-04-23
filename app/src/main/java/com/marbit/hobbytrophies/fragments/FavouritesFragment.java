package com.marbit.hobbytrophies.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.adapters.profile.ItemProfileSalesAdapter;
import com.marbit.hobbytrophies.interfaces.FavouritesFragmentView;
import com.marbit.hobbytrophies.market.ItemDetailActivity;
import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.presenters.FavouritesPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavouritesFragment extends Fragment implements FavouritesFragmentView, ItemProfileSalesAdapter.ItemDetailAdapterListener {

    private FavouritesFragmentListener mListener;
    @BindView(R.id.recycler_view_favourites) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.text_view_empty_list) TextView emptyView;
    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private ItemProfileSalesAdapter itemProfileSalesAdapter;
    private FavouritesPresenter presenter;
    private Unbinder unbinder;


    public FavouritesFragment() {
        // Required empty public constructor
    }

    public static FavouritesFragment newInstance() {
        FavouritesFragment fragment = new FavouritesFragment();
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
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        unbinder = ButterKnife.bind(this, view);
        this.presenter = new FavouritesPresenter(getContext(), this);
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
        presenter.loadFavourites();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FavouritesFragmentListener) {
            mListener = (FavouritesFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FavouritesFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void loadFavouritesSuccessful(List<Item> list) {
        itemProfileSalesAdapter.clearAll();
        itemProfileSalesAdapter.setItemList(list);
        itemProfileSalesAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadFavouritesError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyView() {
        this.emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        this.emptyView.setVisibility(View.GONE);
    }

    @Override
    public void openDetailActivity(Item item, String from) {
        Intent itemIntent = new Intent(getContext(), ItemDetailActivity.class);
        itemIntent.putExtra("ITEM", item);
        itemIntent.putExtra("FROM", from);
        startActivity(itemIntent);
    }

    public interface FavouritesFragmentListener {
    }
}