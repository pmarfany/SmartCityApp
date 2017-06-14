package ptin.smartcity.services;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

import ptin.smartcity.smartcityapp.R;

/**
 * Created by PauMarfany on 17/5/17.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private static AppCompatActivity activity;

    public void setActivity(AppCompatActivity act) { this.activity = act; }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Button birth_button = (Button) activity.findViewById(R.id.config_birthday);

        birth_button.setText(day + "/" + month + "/" + year);
        birth_button.setTextColor( ContextCompat.getColor(this.activity, R.color.black) );
    }
}
