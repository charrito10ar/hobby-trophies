package com.marbit.hobbytrophies.dialogs;

import android.app.Dialog;

import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.model.market.Filter;

import org.florescu.android.rangeseekbar.RangeSeekBar;

public class DialogFilterItemsMarket extends DialogFragment implements View.OnClickListener, RangeSeekBar.OnRangeSeekBarChangeListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private OnDialogFilterItemsInteractionListener mListener;
    private static final String ARG_PARAM1 = "STEP";

    private Spinner spinnerCategory;
    private Spinner spinnerOrderBy;
    private String[] itemTypes;
    private String[] orderByArray;
    private Filter filter;
    private RangeSeekBar seekbarPrice;
    private TextView textViewRangePrice;
    private CheckBox checkboxBarter;
    private CheckBox checkboxDigital;

    public static DialogFilterItemsMarket newInstance(int step) {
        DialogFilterItemsMarket dialogFilterItemsMarket = new DialogFilterItemsMarket();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, step);
        dialogFilterItemsMarket.setArguments(args);
        return dialogFilterItemsMarket;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getTargetFragment() instanceof OnDialogFilterItemsInteractionListener) {
            mListener = (OnDialogFilterItemsInteractionListener) getTargetFragment();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDialogFilterItemsInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //step = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filer_items_market, null);
        this.filter = new Filter();
        this.spinnerCategory = (Spinner) view.findViewById(R.id.spinner_category);
        this.spinnerOrderBy = (Spinner) view.findViewById(R.id.spinner_order_by);
        this.itemTypes = getResources().getStringArray(R.array.item_types);
        this.orderByArray = getResources().getStringArray(R.array.order_by);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.spinner_item, itemTypes);
        ArrayAdapter adapterOrderBy = new ArrayAdapter(getContext(), R.layout.spinner_item, orderByArray);
        this.spinnerCategory.setAdapter(adapter);
        this.spinnerCategory.setOnItemSelectedListener(this);
        this.spinnerOrderBy.setAdapter(adapterOrderBy);
        this.spinnerOrderBy.setOnItemSelectedListener(this);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterOrderBy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Button buttonCancel = (Button) view.findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(this);
        Button buttonApply = (Button) view.findViewById(R.id.button_apply_filter);
        buttonApply.setOnClickListener(this);
        this.seekbarPrice = (RangeSeekBar) view.findViewById(R.id.seekbar_range_price);
        this.seekbarPrice.setOnRangeSeekBarChangeListener(this);
        this.textViewRangePrice = (TextView) view.findViewById(R.id.text_view_range_price);
        this.checkboxBarter = (CheckBox) view.findViewById(R.id.checkbox_barter);
        this.checkboxBarter.setOnCheckedChangeListener(this);
        this.checkboxDigital = (CheckBox) view.findViewById(R.id.checkbox_digital);
        this.checkboxDigital.setOnCheckedChangeListener(this);
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.translucentBackgroundLogin)));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_cancel:
                dismiss();
                break;
            case R.id.button_apply_filter:
                mListener.applyFilter(filter);
                dismiss();
        }
    }

    @Override
    public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
        this.filter.setMinPrice((int)minValue);
        this.filter.setMaxPrice((int)maxValue);
        textViewRangePrice.setText(getString(R.string.range_price, (Integer)minValue, (Integer)maxValue));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinner_category:
                switch (position){
                    case 0:
                        checkboxDigital.setVisibility(View.INVISIBLE);
                        filter.setItemType(-1);
                        break;
                    case 1:
                        checkboxDigital.setVisibility(View.VISIBLE);
                        filter.setItemType(position);
                        break;
                    default:
                        checkboxDigital.setVisibility(View.INVISIBLE);
                        filter.setItemType(position);
                        break;
                }
                break;
            case R.id.spinner_order_by:
                filter.setOrderBy(position);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.checkbox_barter:
                filter.setBarter(isChecked);
                break;
            case R.id.checkbox_digital:
                filter.setDigital(isChecked);
                break;
        }
    }

    public interface OnDialogFilterItemsInteractionListener {
        void applyFilter(Filter filter);
    }

}
