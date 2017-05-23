package com.marbit.hobbytrophies.dialogs;

/**
 * Created by marcelo on 10/12/16.
 */

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.marbit.hobbytrophies.R;


import java.util.Calendar;

public class DialogAbcdiario extends DialogFragment {



    public static DialogAbcdiario newInstance() {
        DialogAbcdiario arrivalDialog = new DialogAbcdiario();
        return arrivalDialog;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_abcdiario, null);

        builder.setView(view)
                .setTitle("Seleccione inicial")

                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });

        return builder.create();
    }





}
