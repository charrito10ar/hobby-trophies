package com.marbit.hobbytrophies.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.model.Meeting;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marcelo on 5/02/17.
 */

public class DialogEditMeeting extends DialogFragment {
    private RequestQueue requestQueue;
    private Meeting meeting;
    private OnDialogEditMeetingInteractionListener mListener;
    public final static int DELETE_MEETING_ERROR_CODE = 0;
    public final static int DELETE_MEETING_CODE = 1;
    public final static int GENERAL_EDIT_MEETING_CODE = 2;
    public final static int TROPHIES_EDIT_MEETING_CODE = 3;


    public static DialogEditMeeting newInstance(Meeting meeting) {
        DialogEditMeeting dialogEditMeeting = new DialogEditMeeting();
        Bundle args = new Bundle();
        args.putParcelable("MEETING", meeting);
        dialogEditMeeting.setArguments(args);
        return dialogEditMeeting;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            meeting= getArguments().getParcelable("MEETING");
        }
        requestQueue = Volley.newRequestQueue(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.edit_meeting_dialog_title)
                .setItems(R.array.edit_meeting_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                mListener.onDialogEditMeetingInteraction(GENERAL_EDIT_MEETING_CODE);
                                break;
                            case 1:
                                sendRequestDeleteMeeting();
                        }
                    }
                });
        return builder.create();
    }

    private void sendRequestDeleteMeeting() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("meeting-id", meeting.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestQueue.add(getStringPostDeleteMeeting(jsonObject)).setShouldCache(false);
    }

    private JsonObjectRequest getStringPostDeleteMeeting(JSONObject jsonParams){
        return (JsonObjectRequest) new JsonObjectRequest(Request.Method.POST,"http://www.hobbytrophies.com/foros/ps3/delete-meeting.php", jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int responseCode = response.getInt("response-code");
                            if(responseCode == 1){
                                mListener.onDialogEditMeetingInteraction(DELETE_MEETING_CODE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mListener.onDialogEditMeetingInteraction(DELETE_MEETING_ERROR_CODE);
            }
        }).setShouldCache(false);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDialogEditMeetingInteractionListener) {
            mListener = (OnDialogEditMeetingInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDialogEditMeetingInteractionListener");
        }
    }

    public interface OnDialogEditMeetingInteractionListener {
        void onDialogEditMeetingInteraction(int code);
    }



}
