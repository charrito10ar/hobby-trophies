package com.marbit.hobbytrophies.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.marbit.hobbytrophies.R;

public class DialogGeneric extends DialogFragment {


    private static final String ARG_PARAM1 = "TITLE";
    private static final String ARG_PARAM2 = "MESSAGE";
    private static final String ARG_PARAM3 = "POSITIVE-ACTION-TEXT";
    private static final String ARG_PARAM4 = "POSITIVE-ACTION";
    private String title;
    private String message;
    private String positiveButton;
    private int amountButtons;
    private int positiveAction;
    private OnDialogGenericInteractionListener mListener;

    public static DialogGeneric newInstance(String title, String message, String positiveButton, int positiveAction) {
        DialogGeneric dialogGeneric = new DialogGeneric();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, message);
        args.putString(ARG_PARAM3, positiveButton);
        args.putInt(ARG_PARAM4, positiveAction);
        dialogGeneric.amountButtons = 3;
        dialogGeneric.setArguments(args);
        return dialogGeneric;
    }

    public static DialogGeneric newInstance(String param1, String param2) {
        DialogGeneric dialogGeneric = new DialogGeneric();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        dialogGeneric.amountButtons = 2;
        dialogGeneric.setArguments(args);
        return dialogGeneric;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDialogGenericInteractionListener) {
            mListener = (OnDialogGenericInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDialogGenericInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
            message = getArguments().getString(ARG_PARAM2);
            positiveButton = getArguments().getString(ARG_PARAM3);
            positiveAction = getArguments().getInt(ARG_PARAM4);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_alert_login, null);

        builder.setView(view)
                .setTitle(title)
                .setMessage(message);

        switch (this.amountButtons){
            case 2:
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dismiss();
                }
            });
                break;
            case 3:
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dismiss();
                }
            });
                builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mListener.onDialogGenericInteraction(positiveAction);
                }
            });
        }

        return builder.create();
    }


    public interface OnDialogGenericInteractionListener {
        void onDialogGenericInteraction(int code);
    }


}
