package com.mobilewallet.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

public class SelectDateFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {

	private EditText dobEditText;
	private static final long minDate = -1735710800000l;

	public void setDobEditText(EditText dobEditText) {
		this.dobEditText = dobEditText;
	}

	@Override
	public void onDateSet(DatePicker view, int yy, int mm, int dd) {
		// TODO Auto-generated method stub

		populateSetDate(yy, mm + 1, dd);

	}

	@SuppressLint("NewApi")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, 1988, 10, 12);
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				DatePicker datePicker = datePickerDialog.getDatePicker();
				datePicker.setMinDate(minDate);
				datePicker.setMaxDate(new Date().getTime());
				Log.i("SelectDateFragment", "we can set min date");
			} else {
				Log.i("SelectDateFragment", "unable to set min date");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datePickerDialog;
	}

	public void populateSetDate(int year, int month, int day) {
		String date = getDate(month + "/" + day + "/" + year);
		if (date != null) {
			dobEditText.setText(date);
		}
	}

	private String getDate(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
			Date d = sdf.parse(date.trim());
			sdf.applyPattern("dd-MMM-yy");
			return sdf.format(d);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
