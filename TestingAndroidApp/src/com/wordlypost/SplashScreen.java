package com.wordlypost;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wordlypost.WordlyPostGoogleAnalytics.TrackerName;
import com.wordlypost.service.BuildService;
import com.wordlypost.utils.Utils;

public class SplashScreen extends ActionBarActivity {

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			Tracker t = ((WordlyPostGoogleAnalytics) getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			t.setScreenName(getString(R.string.splash_screen_name));
			t.send(new HitBuilders.AppViewBuilder().build());

		} catch (Exception e) {
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.splash_screen);

			BuildService.build.getCategories(new Callback<String>() {

				@Override
				public void success(String output, Response arg1) {
					try {
						Log.i("Categories :", output);
						JSONObject obj = new JSONObject(output);
						if (obj.getString("status").equals(getString(R.string.error))) {
							Utils.displayToad(SplashScreen.this, getString(R.string.task_error_msg));
							finish();
						}
					} catch (Exception e) {
					}

					startActivity(new Intent(SplashScreen.this, TabsActivity.class).addFlags(
							Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("categories", output));
					finish();
				}

				@Override
				public void failure(RetrofitError retrofitError) {
					retrofitError.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
