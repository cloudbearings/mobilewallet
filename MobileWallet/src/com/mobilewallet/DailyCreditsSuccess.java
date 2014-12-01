package com.mobilewallet;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics.TrackerName;
import com.mobilewallet.users.BalanceActivity;
import com.mobilewallet.utils.Utils;

public class DailyCreditsSuccess extends ActionBarActivity {

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			Tracker tracker = ((MobileWalletGoogleAnalytics) getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			tracker.setScreenName(getString(R.string.daily_credits_success_screen_name));
			tracker.send(new HitBuilders.AppViewBuilder().build());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daily_credits_success);

		try {
			TextView heading = (TextView) findViewById(R.id.textView1);
			heading.setTypeface(
					Utils.getFont(DailyCreditsSuccess.this, getString(R.string.GothamRnd)),
					Typeface.BOLD);

			Button view_offers = (Button) findViewById(R.id.button1);
			view_offers.setTypeface(
					Utils.getFont(DailyCreditsSuccess.this, getString(R.string.GothamRnd)),
					Typeface.BOLD);

			Button view_balance = (Button) findViewById(R.id.button2);
			view_balance.setTypeface(
					Utils.getFont(DailyCreditsSuccess.this, getString(R.string.GothamRnd)),
					Typeface.BOLD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void viewOffers(View v) {
		startActivity(new Intent(DailyCreditsSuccess.this, TabsActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		finish();
	}

	public void viewBalance(View v) {
		try {
			startActivity(new Intent(this, BalanceActivity.class));
			finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
