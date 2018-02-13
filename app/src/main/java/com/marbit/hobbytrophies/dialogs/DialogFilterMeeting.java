package com.marbit.hobbytrophies.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.marbit.hobbytrophies.R;

public class DialogFilterMeeting extends DialogFragment {

    private static OnDialogFilterMeetingInteractionListener mListener;
    public static final int ALL_MEETINGS = 0;
    public static final int MY_GAMES_MEETINGS = 1;
    public static final int MY_MEETINGS = 2;
    public static final int NEARBY_MEETINGS = 3;
    private static int typeRequest;

    public static DialogFilterMeeting  newInstance(Fragment fragment, int optionSelect) {
        DialogFilterMeeting  dialogFilterMeeting = new DialogFilterMeeting ();
        typeRequest = optionSelect;
        if (fragment instanceof OnDialogFilterMeetingInteractionListener) {
            mListener = (OnDialogFilterMeetingInteractionListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString()
                    + " must implement OnDialogFilterMeetingInteractionListener");
        }
        return dialogFilterMeeting;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.filter_meeting_dialog_title)
                .setSingleChoiceItems(R.array.filter_meeting_options, typeRequest,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                mListener.onDialogFilterMeetingInteraction(ALL_MEETINGS);
                                break;
                            case 1:
                                mListener.onDialogFilterMeetingInteraction(MY_GAMES_MEETINGS);
                                break;
                            case 2:
                                mListener.onDialogFilterMeetingInteraction(MY_MEETINGS);
                                break;
                            case 3:
                                mListener.onDialogFilterMeetingInteraction(NEARBY_MEETINGS);
                                break;
                        }
                        dismiss();
                    }
                });
        return builder.create();
    }

    public interface OnDialogFilterMeetingInteractionListener {
        void onDialogFilterMeetingInteraction(int code);
    }
}