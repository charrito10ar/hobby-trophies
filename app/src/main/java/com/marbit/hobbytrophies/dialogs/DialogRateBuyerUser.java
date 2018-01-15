package com.marbit.hobbytrophies.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.market.model.Rate;
import com.marbit.hobbytrophies.market.model.UserMarket;
import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.utilities.Preferences;

public class DialogRateBuyerUser extends DialogFragment implements TextWatcher, RatingBar.OnRatingBarChangeListener {

    private static final String ARG_PARAM1 = "ITEM";
    private static final String ARG_PARAM2 = "BUYER";
    private Item item;
    private UserMarket buyer;
    private TextView textViewTitle;
    private EditText comment;
    private RatingBar ratingBar;
    private Rate rate;
    private OnDialogRateBuyerUserListener mListener;

    public static DialogRateBuyerUser newInstance(Item item, UserMarket userMarket) {
        DialogRateBuyerUser dialogGeneric = new DialogRateBuyerUser();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, item);
        args.putParcelable(ARG_PARAM2, userMarket);
        dialogGeneric.setArguments(args);
        return dialogGeneric;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDialogRateBuyerUserListener) {
            mListener = (OnDialogRateBuyerUserListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDialogRateBuyerUserListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            item = getArguments().getParcelable(ARG_PARAM1);
            buyer = getArguments().getParcelable(ARG_PARAM2);
        }
        this.rate = new Rate();
        this.rate.setAuthor(Preferences.getUserId(getActivity()));
        this.rate.setUserId(buyer.getId());
        this.rate.setItemId(item.getId());
        this.rate.setType("BUYER");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_rate_buyer, null);
        comment = view.findViewById(R.id.edit_text_comment);
        comment.addTextChangedListener(this);
        ratingBar = view.findViewById(R.id.rating_bar);
        ratingBar.setOnRatingBarChangeListener(this);

        builder.setView(view)
                .setTitle("Califica a " + buyer.getName());

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(ratingBar.getNumStars() > 0)
                            mListener.onDialogRateBuyerInteraction(rate, item, buyer);
                        else
                            Toast.makeText(getActivity(), "Por califique con estrellas al comprador.", Toast.LENGTH_LONG).show();
                    }
                });

        return builder.create();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        rate.setComment(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        rate.setValue((int) v);
    }

    public interface OnDialogRateBuyerUserListener {
        void onDialogRateBuyerInteraction(Rate rate, Item item, UserMarket userMarket);
    }
}