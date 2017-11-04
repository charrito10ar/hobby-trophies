package com.marbit.hobbytrophies.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.marbit.hobbytrophies.wishes.NewWishActivity;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.wishes.adapters.WishAdapter;
import com.marbit.hobbytrophies.interfaces.wishes.WishListFragmentView;
import com.marbit.hobbytrophies.wishes.model.Wish;
import com.marbit.hobbytrophies.presenters.wishes.WishListFragmentPresenter;

import java.util.List;


public class WishListFragment extends Fragment implements WishListFragmentView, View.OnClickListener {
    private FloatingActionButton floatingNewWish;
    private WishListFragmentPresenter presenter;
    private RecyclerView recyclerView;
    private WishAdapter wishAdapter;
    private RelativeLayout layoutEmpty;

    public WishListFragment() {
    }

    public static WishListFragment newInstance() {
        WishListFragment fragment = new WishListFragment();
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
        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);
        wishAdapter = new WishAdapter(getContext());
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_wish_list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.setAdapter(wishAdapter);
        this.presenter = new WishListFragmentPresenter(getContext(), this);
        this.floatingNewWish = (FloatingActionButton) view.findViewById(R.id.fab_new_wish);
        this.floatingNewWish.setOnClickListener(this);
        this.layoutEmpty = (RelativeLayout) view.findViewById(R.id.layout_empty_list);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        presenter.loadWishList();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_new_wish:
                startActivity(new Intent(getContext(), NewWishActivity.class));
                break;

        }
    }

    @Override
    public void loadWishListSuccessful(List<Wish> wishList) {
        this.wishAdapter.clearAll();
        this.wishAdapter.setList(wishList);
        this.wishAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEmptyListMessage() {
        this.layoutEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyListMessage() {
        this.layoutEmpty.setVisibility(View.GONE);
    }
}
