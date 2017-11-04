package com.marbit.hobbytrophies.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.SignUpActivity;

public class DialogAlertLogin extends DialogFragment {

    public static DialogAlertLogin newInstance() {
        DialogAlertLogin dialogAlertLogin = new DialogAlertLogin();
        return dialogAlertLogin;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_alert_login, null);

        builder.setView(view)
                .setTitle("Debes loguearte")

                .setMessage("Para poder realizar esta acción debes loguearte.")

                .setPositiveButton("Realizar Login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(getContext(), SignUpActivity.class));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });

        return builder.create();
    }





}
