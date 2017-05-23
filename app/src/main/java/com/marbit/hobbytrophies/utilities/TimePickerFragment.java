package com.marbit.hobbytrophies.utilities;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by marcelo on 14/01/17.
 */


public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static TimePickerFragment ourInstance = new TimePickerFragment();

    private int year;
    private int month;
    private int day;
    private TimeDatePickerListener mListener;

    public static TimePickerFragment getInstance() {
        return ourInstance;
    }

    public TimePickerFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (getArguments() != null) {
            this.year = getArguments().getInt("YEAR");
            this.month = getArguments().getInt("MONTH");
            this.day = getArguments().getInt("DAY");
        }

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TimeDatePickerListener) {
            mListener = (TimeDatePickerListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TimeDatePickerListener");
        }
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(this.year, this.month, this.day, hourOfDay, minute);
        mListener.onDateTimeInteraction(calendar);
    }

    public interface TimeDatePickerListener {
        void onDateTimeInteraction(Calendar calendar);
    }
}
