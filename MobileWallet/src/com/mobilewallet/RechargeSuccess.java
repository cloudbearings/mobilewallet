package com.mobilewallet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics.TrackerName;

public class RechargeSuccess extends ActionBarActivity {

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {

			Tracker t = ((MobileWalletGoogleAnalytics) getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			t.setScreenName(getString(R.string.recharge_success_screen_name));
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
		setContentView(R.layout.recharge_success);
		try {
			((TextView) findViewById(R.id.textView2))
					.setText(Html
							.fromHtml("Write to us at <font color=\"black\"><b>freeplusapp@gmail.com</b></font><br/> if your recharge is unsuccessful."));
			((TextView) findViewById(R.id.textView3)).setText("Transaction id is "
					+ getIntent().getStringExtra("status").split(" ")[6].trim());

		} catch (Exception e) {
			e.printStackTrace();
		}

		((Button) findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse("https://play.google.com/store/apps/details?id=com.freeplus")));

			}
		});

		((Button) findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(RechargeSuccess.this, TabsActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				finish();

			}
		});

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
}
