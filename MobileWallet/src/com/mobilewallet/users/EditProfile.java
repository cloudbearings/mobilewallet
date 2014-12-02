package com.mobilewallet.users;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mobilewallet.R;
import com.mobilewallet.TabsActivity;
import com.mobilewallet.R.array;
import com.mobilewallet.R.id;
import com.mobilewallet.R.layout;
import com.mobilewallet.R.string;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics.TrackerName;
import com.mobilewallet.service.BuildService;
import com.mobilewallet.utils.IncomeInfo;
import com.mobilewallet.utils.OccupationInfo;
import com.mobilewallet.utils.SelectDateFragment;
import com.mobilewallet.utils.Utils;

public class EditProfile extends ActionBarActivity {

	private EditText nameEditText, dobEditText;
	private Spinner genderSpinner, occupationSpinner, incomeSpinner;
	private Button updateButton;
	private boolean clicked = false;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			Tracker tracker = ((MobileWalletGoogleAnalytics) getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			tracker.setScreenName(getString(R.string.edit_profile));
			tracker.send(new HitBuilders.AppViewBuilder().build());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_profile);
		nameEditText = (EditText) findViewById(R.id.name_editText1);
		dobEditText = (EditText) findViewById(R.id.dob_editText2);

		genderSpinner = (Spinner) findViewById(R.id.gender_spinner1);
		genderSpinner.setAdapter(new ArrayAdapter<String>(EditProfile.this, R.layout.spinner_item,
				getResources().getStringArray(R.array.gender_array_items)));

		occupationSpinner = (Spinner) findViewById(R.id.occupation_spinner2);
		occupationSpinner.setAdapter(new ArrayAdapter<String>(EditProfile.this,
				R.layout.spinner_item, getResources()
						.getStringArray(R.array.occupation_array_items)));

		incomeSpinner = (Spinner) findViewById(R.id.income_spinner3);
		incomeSpinner.setAdapter(new ArrayAdapter<String>(EditProfile.this, R.layout.spinner_item,
				getResources().getStringArray(R.array.income_array_items)));

		updateButton = (Button) findViewById(R.id.update_button1);
		updateButton.setTypeface(Utils.getFont(EditProfile.this, getString(R.string.GothamRnd)),
				Typeface.BOLD);
		populateDefaultData();
		addDobEditTextClickListener();
		addUpdateButtonClickListener();

	}

	private void populateDefaultData() {

		try {
			Log.i("user profile", getIntent().getStringExtra("userProfile"));
			JSONObject obj = new JSONObject(getIntent().getStringExtra("userProfile"));
			try {
				String name = obj.getString("name");
				if (!"null".equals(name))
					nameEditText.setText(name);
			} catch (Exception e) {
			}
			try {
				String dob = obj.getString("dob");
				if (!"null".equals(dob))
					dobEditText.setText(dob);
			} catch (Exception e) {
			}

			try {
				String gender = obj.getString("gender");
				if (!"null".equals(gender))
					genderSpinner.setSelection("F".equals(gender) ? 2 : 1);

			} catch (Exception e) {
			}

			try {
				occupationSpinner.setSelection(obj.getInt("occupation"));

			} catch (Exception e) {
			}
			try {
				incomeSpinner.setSelection(obj.getInt("income"));

			} catch (Exception e) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addDobEditTextClickListener() {
		dobEditText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				EditProfile.this.runOnUiThread(new Runnable() {
					public void run() {
						SelectDateFragment selectDateFragment = new SelectDateFragment();
						selectDateFragment.setDobEditText(dobEditText);

						DialogFragment newFragment = selectDateFragment;
						newFragment.show(getSupportFragmentManager(), "DatePicker");
					}
				});

			}
		});

	}

	private void addUpdateButtonClickListener() {
		updateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (validate()) {
					if (!clicked) {
						clicked = true;
						updateButton.setText("Processing..");
						BuildService.build.updateuserProfile(Utils.getUserId(EditProfile.this),
								nameEditText.getText().toString().trim(), dobEditText.getText()
										.toString().trim(), "Male".equals(genderSpinner
										.getSelectedItem().toString()) ? "M" : "F",
								OccupationInfo.occupationMap.get(occupationSpinner
										.getSelectedItem().toString()), IncomeInfo.incomeMap
										.get(incomeSpinner.getSelectedItem().toString()),
								new Callback<String>() {

									@Override
									public void failure(RetrofitError arg0) {
										// TODO Auto-generated method stub
										arg0.printStackTrace();
										clicked = false;
										updateButton.setText(getString(R.string.update));
										showToad("Unable to update user info.");

									}

									@Override
									public void success(String result, Response arg1) {
										// TODO Auto-generated method stub
										try {
											if ("Y".equals(new JSONObject(result)
													.getString("updated"))) {
												clicked = false;
												updateButton.setText(getString(R.string.update));
												showToad("User profile updated.");
											} else {
												clicked = false;
												updateButton.setText(getString(R.string.update));
												showToad("Unable to update user info.");
											}

										} catch (Exception e) {
											e.printStackTrace();
											clicked = false;
											updateButton.setText(getString(R.string.update));
											showToad("Unable to update user info.");
										}

									}
								});

					} else {
						showToad("Please wait.");
					}
				}

			}
		});
	}

	private boolean validate() {

		if ("".equals(nameEditText.getText().toString().trim())) {
			showToad("Select name.");
			return false;
		} else if ("".equals(dobEditText.getText().toString().trim())) {
			showToad("Select date of birth.");
			return false;
		} else if ("Gender".equals(genderSpinner.getSelectedItem().toString())) {
			showToad("Select gender.");
			return false;
		} else if ("Occupation".equals(occupationSpinner.getSelectedItem().toString())) {
			showToad("Select occupation.");
			return false;
		} else if ("Income".equals(incomeSpinner.getSelectedItem().toString())) {
			showToad("Select income.");
			return false;
		} else {
			return true;
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent tabsIntent = new Intent(EditProfile.this, TabsActivity.class);
			tabsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(tabsIntent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showToad(String toad) {
		Toast.makeText(EditProfile.this, toad, Toast.LENGTH_LONG).show();

	}
}
