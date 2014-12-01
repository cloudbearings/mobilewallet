package com.mobilewallet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
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

public class DailyCredits extends ActionBarActivity {

	private SharedPreferences prefs;
	private Button loginCreditButton;

	@Override
	protected void onResume() {
		super.onResume();
		try {
			Tracker tracker = ((MobileWalletGoogleAnalytics) getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			tracker.setScreenName(getString(R.string.daily_credits_screen_name));
			tracker.send(new HitBuilders.AppViewBuilder().build());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daily_credits);
		try {

			TextView heading = (TextView) findViewById(R.id.textView1);
			heading.setTypeface(Utils.getFont(DailyCredits.this, getString(R.string.GothamRnd)),
					Typeface.BOLD);

			TextView des = (TextView) findViewById(R.id.textView2);
			des.setText(Html.fromHtml(getString(R.string.daily_credits_des)));

			loginCreditButton = (Button) findViewById(R.id.claimLoginCredit);
			loginCreditButton.setTypeface(
					Utils.getFont(DailyCredits.this, getString(R.string.GothamRnd)), Typeface.BOLD);

			prefs = getApplicationContext().getSharedPreferences(
					DailyCredits.class.getSimpleName(), Context.MODE_PRIVATE);

			Calendar c = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
			if (prefs.getString("date", null) != null
					&& prefs.getString("date", null).equals(df.format(c.getTime()))) {
				loginCreditButton.setText(getString(R.string.already_climed));
				loginCreditButton.setEnabled(false);
			} else {

				loginCreditButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						try {

							if (Utils.isNetworkAvailable(DailyCredits.this)) {
								loginCreditButton.setText(getString(R.string.title_processing));

								BuildService.build.dailyCredits(Utils.getUserId(DailyCredits.this),
										Utils.getDeviceId(DailyCredits.this),
										Utils.getGcmId(DailyCredits.this), new Callback<String>() {

											@Override
											public void failure(RetrofitError arg0) {
												arg0.printStackTrace();
											}

											@Override
											public void success(String output, Response arg1) {
												Log.i("", output);
												try {
													JSONObject obj = new JSONObject(output);

													if ("Y".equals(obj.getString("sc"))) {

														SharedPreferences.Editor editor = prefs
																.edit();

														Calendar c = Calendar.getInstance();
														SimpleDateFormat df = new SimpleDateFormat(
																"dd-MMM-yyyy", Locale.ENGLISH);

														editor.putString("date",
																df.format(c.getTime()));
														editor.commit();

														Utils.storeAmount(
																(Double.parseDouble(Utils
																		.getAmount(DailyCredits.this)) + 0.07)
																		+ "", DailyCredits.this);

														startActivity(new Intent(DailyCredits.this,
																DailyCreditsSuccess.class));
														finish();
													} else {
														loginCreditButton
																.setText(getString(R.string.already_climed));
														loginCreditButton.setEnabled(false);
														displayToad(getString(R.string.already_climed));
													}

												} catch (Exception e) {
													e.printStackTrace();
												}

											}

										});

							} else {
								displayToad(getString(R.string.no_internet));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent tabsIntent = new Intent(DailyCredits.this, TabsActivity.class);
			tabsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(tabsIntent);
			finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void displayToad(String msg) {
		Toast.makeText(DailyCredits.this, msg, Toast.LENGTH_LONG).show();
	}
}
