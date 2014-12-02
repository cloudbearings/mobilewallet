package com.mobilewallet;

import org.json.JSONArray;
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
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics.TrackerName;
import com.mobilewallet.service.BuildService;
import com.mobilewallet.utils.Utils;

public class Notifications extends ActionBarActivity {

	private CompoundButton offerSwitch;

	@Override
	protected void onResume() {
		super.onResume();
		try {
			Tracker t = ((MobileWalletGoogleAnalytics) getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			t.setScreenName(getString(R.string.notifications_screen_name));
			t.send(new HitBuilders.AppViewBuilder().build());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.notifications);
			offerSwitch = (CompoundButton) findViewById(R.id.notificationsStauts);
			offerSwitch.setTypeface(
					Utils.getFont(Notifications.this, getString(R.string.GothamRnd)),
					Typeface.BOLD);

			populateDefaultData();
			offerSwitchChangedListener();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void populateDefaultData() {
		BuildService.build.notification(Utils.getUserId(Notifications.this),
				new Callback<String>() {

					@Override
					public void success(String result, Response arg1) {
						// TODO Auto-generated method stub
						try {
							Log.i("notification json", result + " result");
							JSONArray array = new JSONArray(result);

							offerSwitch.setChecked("Y".equals((String) array.get(0)));

						} catch (Exception e) {
							e.printStackTrace();
							offerSwitch.setChecked(false);

						}

					}

					@Override
					public void failure(RetrofitError arg0) {
						// TODO Auto-generated method stub
						arg0.printStackTrace();
						offerSwitch.setChecked(false);

					}
				});

	}

	private void offerSwitchChangedListener() {
		offerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				updateNotification(getString(R.string.offer), isChecked ? "Y" : "N");
			}
		});
	}

	private void updateNotification(String type, String status) {
		BuildService.build.notification(Utils.getUserId(Notifications.this), status, type,
				new Callback<String>() {
					@Override
					public void failure(RetrofitError arg0) {
						arg0.printStackTrace();
						showToad(getString(R.string.notifc_failure_msg));
					}

					@Override
					public void success(String result, Response arg1) {
						try {
							if (!"Y".equals(new JSONObject(result).getString("sc"))) {
								showToad(getString(R.string.notifc_failure_msg));
							}
						} catch (Exception e) {
							e.printStackTrace();
							showToad(getString(R.string.notifc_failure_msg));
						}
					}
				});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent tabsIntent = new Intent(Notifications.this, TabsActivity.class);
			tabsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(tabsIntent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showToad(String toad) {
		Toast.makeText(Notifications.this, toad, Toast.LENGTH_LONG).show();

	}
}
