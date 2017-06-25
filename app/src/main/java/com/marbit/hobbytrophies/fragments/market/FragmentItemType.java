package com.marbit.hobbytrophies.fragments.market;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.interfaces.market.FragmentItemTypeView;
import com.marbit.hobbytrophies.presenters.market.FragmentItemTypePresenter;
import com.marbit.hobbytrophies.utilities.Constants;


public class FragmentItemType extends Fragment implements View.OnClickListener, FragmentItemTypeView{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RelativeLayout gameLayout;
    private RelativeLayout consoleLayout;
    private RelativeLayout accesoriesLayout;
    private RelativeLayout othersLayout;
    private FragmentItemTypePresenter presenter;

    private OnFragmentItemTypeInteractionListener mListener;

    public FragmentItemType() {
    }

    public static FragmentItemType newInstance(String param1, String param2) {
        FragmentItemType fragment = new FragmentItemType();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_item_type, container, false);
        this.presenter = new FragmentItemTypePresenter(getContext(), this);
        this.gameLayout = (RelativeLayout) view.findViewById(R.id.layout_item_game);
        this.gameLayout.setOnClickListener(this);
        this.consoleLayout= (RelativeLayout) view.findViewById(R.id.layout_item_console);
        this.consoleLayout.setOnClickListener(this);
        this.accesoriesLayout= (RelativeLayout) view.findViewById(R.id.layout_item_accesories);
        this.accesoriesLayout.setOnClickListener(this);
        this.othersLayout = (RelativeLayout) view.findViewById(R.id.layout_item_others);
        this.othersLayout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentItemTypeInteractionListener) {
            mListener = (OnFragmentItemTypeInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentItemTypeInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_item_game:
                this.selectType(Constants.PREFERENCE_ITEM_CATEGORY_GAME);
                break;
            case R.id.layout_item_console:
                this.selectType(Constants.PREFERENCE_ITEM_CATEGORY_CONSOLE);
                break;
            case R.id.layout_item_accesories:
                this.selectType(Constants.PREFERENCE_ITEM_CATEGORY_ACCESSORIES);
                break;
            case R.id.layout_item_others:
                this.selectType(Constants.PREFERENCE_ITEM_CATEGORY_OTHER);
                break;
        }
    }

    @Override
    public void selectType(int itemType) {
       mListener.onSelectType(itemType);

    }

    public interface OnFragmentItemTypeInteractionListener {
        void onSelectType(int itemType);
    }
}
