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

import com.mobilewallet.service.BuildService;
import com.mobilewallet.utils.Utils;

public class Notifications extends ActionBarActivity {

	private CompoundButton offerSwitch;

	@Override
	protected void onResume() {
		super.onResume();
		Utils.googleAnalyticsTracking(Notifications.this,
				getString(R.string.notifications_screen_name));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.notifications);
			offerSwitch = (CompoundButton) findViewById(R.id.notificationsStauts);
			offerSwitch.setTypeface(Utils.getFont(Notifications.this,
					getString(R.string.GothamRnd)), Typeface.BOLD);

			populateDefaultData();
			offerSwitchChangedListener();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void populateDefaultData() {
		BuildService.build.getNotifications(
				Utils.getUserId(Notifications.this), new Callback<String>() {

					@Override
					public void success(String result, Response arg1) {
						// TODO Auto-generated method stub
						try {
							Log.i("notification json", result + " result");
							JSONArray array = new JSONArray(result);

							offerSwitch.setChecked("Y".equals((String) array
									.get(0)));

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
		offerSwitch
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						updateNotification(getString(R.string.offer),
								isChecked ? "Y" : "N");
					}
				});
	}

	private void updateNotification(String type, String status) {
		BuildService.build.updateNotifications(
				Utils.getUserId(Notifications.this), status, type,
				new Callback<String>() {
					@Override
					public void failure(RetrofitError arg0) {
						arg0.printStackTrace();
						Utils.displayToad(Notifications.this,
								getString(R.string.notifc_failure_msg));
					}

					@Override
					public void success(String result, Response arg1) {
						try {
							if (!"Y".equals(new JSONObject(result)
									.getString("sc"))) {
								Utils.displayToad(Notifications.this,
										getString(R.string.notifc_failure_msg));
							}
						} catch (Exception e) {
							e.printStackTrace();
							Utils.displayToad(Notifications.this,
									getString(R.string.notifc_failure_msg));
						}
					}
				});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent tabsIntent = new Intent(Notifications.this,
					TabsActivity.class);
			tabsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(tabsIntent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
