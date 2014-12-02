package com.mobilewallet;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics.TrackerName;
import com.mobilewallet.service.BuildService;
import com.mobilewallet.utils.Utils;

public class RechargeConfirmation extends ActionBarActivity {

	private Bundle rechargeInfoBundle;
	private boolean clicked = false;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			Tracker t = ((MobileWalletGoogleAnalytics) getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			t.setScreenName(getString(R.string.recharge_confirmation_screen_name));
			t.send(new HitBuilders.AppViewBuilder().build());

			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);

			getSupportActionBar().setDisplayOptions(
					ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_HOME_AS_UP
							| ActionBar.DISPLAY_SHOW_HOME);
			getSupportActionBar().setCustomView(R.layout.custom_actionbar);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recharge_confirmation);
		showValues();
		addEditButtonONclickListener();
		addConfirmButtonONclickListener();

	}

	private void addEditButtonONclickListener() {
		((Button) findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RechargeConfirmation.this.finish();

			}
		});

	}

	private void addConfirmButtonONclickListener() {
		((Button) findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!clicked) {
					clicked = true;
					Log.i("RechargeConfirmation", "button clicked");

					((Button) v).setText("Processing..");

					((Button) findViewById(R.id.button2)).setOnClickListener(null);
					((Button) findViewById(R.id.button1)).setOnClickListener(null);

					BuildService.build.recharge(
							Utils.getUserId(RechargeConfirmation.this),
							rechargeInfoBundle.getString("operator_id") + "_"
									+ rechargeInfoBundle.getString("operator"),
							rechargeInfoBundle.getString("mobile"),
							rechargeInfoBundle.getString("circle_id"),
							rechargeInfoBundle.getString("amount"),
							Utils.getDeviceId(RechargeConfirmation.this), Utils.isEmulator(), "",
							rechargeInfoBundle.getString("special"), new Callback<String>() {

								@Override
								public void failure(RetrofitError retrofitError) {
									retrofitError.printStackTrace();
									Log.i("RechargeConfirmation", "Recharge failed.");
									Toast.makeText(RechargeConfirmation.this, "Recharge failed.",
											Toast.LENGTH_LONG).show();
									finish();
								}

								@Override
								public void success(String output, Response arg1) {
									Log.i("RechargeConfirmation", "Recharge success.");
									try {
										Log.i("RechargeConfirmation", output);

										JSONObject obj = new JSONObject(output);
										String sc = obj.getString("sc");
										// if (false) {
										if ("N".equals(sc)) {
											Toast.makeText(RechargeConfirmation.this,
													obj.getString("err"), Toast.LENGTH_LONG).show();
											finish();
										} else {

											Utils.storeAmount(
													(Double.parseDouble(Utils
															.getAmount(RechargeConfirmation.this)) - Double
															.parseDouble(rechargeInfoBundle
																	.getString("amount")))
															+ "", RechargeConfirmation.this);

											startActivity(new Intent(RechargeConfirmation.this,
													RechargeSuccess.class).putExtra(
													"status", sc));
											finish();

										}

									} catch (Exception e) {
										e.printStackTrace();
										Toast.makeText(RechargeConfirmation.this,
												"Recharge failed.", Toast.LENGTH_LONG).show();
										finish();
									}

								}
							});

				} else {
					Toast.makeText(RechargeConfirmation.this, "Please wait.", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

	}

	private void showValues() {

		rechargeInfoBundle = getIntent().getBundleExtra("rechargeInfo");
		((TextView) findViewById(R.id.textView2)).setText(rechargeInfoBundle.getString("mobile"));
		((TextView) findViewById(R.id.textView4)).setText(rechargeInfoBundle.getString("operator"));
		((TextView) findViewById(R.id.textView6)).setText(rechargeInfoBundle.getString("circle"));
		((TextView) findViewById(R.id.textView8)).setText("Rs. "
				+ rechargeInfoBundle.getString("amount"));

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {

		finish();
	}

	public void viewBalance(View v) {
		finish();
	}
}
