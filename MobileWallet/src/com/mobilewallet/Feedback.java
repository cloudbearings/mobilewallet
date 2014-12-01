package com.mobilewallet;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics.TrackerName;
import com.mobilewallet.service.BuildService;
import com.mobilewallet.utils.Utils;

public class Feedback extends ActionBarActivity {
	private EditText feedBack;
	private Spinner feedbackType;
	private Button sendFeedback;
	private boolean clicked = false;

	@Override
	protected void onResume() {
		super.onResume();
		Tracker t = ((MobileWalletGoogleAnalytics) getApplication())
				.getTracker(TrackerName.APP_TRACKER);
		t.setScreenName(getString(R.string.feedback_screen_name));
		t.send(new HitBuilders.AppViewBuilder().build());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);

		TextView heading = (TextView) findViewById(R.id.textView1);
		heading.setTypeface(Utils.getFont(Feedback.this, getString(R.string.GothamRnd)),
				Typeface.BOLD);

		feedbackType = (Spinner) findViewById(R.id.feedbackTypeSpinner);
		feedbackType.setAdapter(new ArrayAdapter<String>(Feedback.this, R.layout.spinner_item,
				getResources().getStringArray(R.array.feedback_arrays)));

		feedBack = (EditText) findViewById(R.id.feedback);
		sendFeedback = (Button) findViewById(R.id.sendFeedback);
		sendFeedback.setTypeface(Utils.getFont(Feedback.this, getString(R.string.GothamRnd)),
				Typeface.BOLD);

		addSubmitButtonClickListener();

	}

	private void addSubmitButtonClickListener() {
		sendFeedback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (validate()) {

					if (!clicked) {
						clicked = true;
						sendFeedback.setText("Processing..");
						BuildService.build.feedback(Utils.getUserId(Feedback.this), feedbackType
								.getSelectedItem().toString(),
								feedBack.getText().toString().trim(), "", new Callback<String>() {

									@Override
									public void success(String result, Response arg1) {
										// TODO Auto-generated method stub

										try {
											Log.i("feed back result", result);
											if ("Y".equals(new JSONObject(result).getString("sc"))) {
												sendFeedback.setText("SEND");
												clicked = false;
												displayToad("Feedback sent successfully.");
												feedBack.setText("");

											} else {
												sendFeedback.setText("SEND");
												clicked = false;
												displayToad("Unable to send feedback.");
											}

										} catch (Exception e) {
											e.printStackTrace();
											sendFeedback.setText("SEND");
											clicked = false;
											displayToad("Unable to send feedback.");
										}

									}

									@Override
									public void failure(RetrofitError arg0) {
										// TODO Auto-generated method stub
										sendFeedback.setText("SEND");
										clicked = false;
										displayToad("Failed.");

										arg0.printStackTrace();

									}
								});

					} else {
						displayToad("Please wait.");
					}

				}

			}
		});
	}

	private boolean validate() {

		if ("".equals(feedBack.getText().toString().trim())) {
			displayToad("Enter feedback.");
			return false;
		}

		return true;
	}

	private void displayToad(String msg) {
		Toast.makeText(Feedback.this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent tabsIntent = new Intent(Feedback.this, TabsActivity.class);
			tabsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(tabsIntent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
