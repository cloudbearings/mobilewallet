package com.wordlypost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wordlypost.WordlyPostGoogleAnalytics.TrackerName;
import com.wordlypost.google.adcontroller.AdController;

public class PrivacyPolicy extends ActionBarActivity {

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			AdController.resumeAdView();

			Tracker t = ((WordlyPostGoogleAnalytics) getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			t.setScreenName(getString(R.string.privacy_policy_screen_name));
			t.send(new HitBuilders.AppViewBuilder().build());

		} catch (Exception e) {
			Log.d("TAG", getString(R.string.google_analytics_error));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.privacy_policy);

		try {
			RelativeLayout bannerLayout = (RelativeLayout) findViewById(R.id.privacyBannerAd);
			AdController.bannerAd(PrivacyPolicy.this, bannerLayout,
					getString(R.string.privacy_policy_banner_unit_id));
		} catch (Exception e) {
			e.printStackTrace();
		}

		((TextView) findViewById(R.id.privacy_policy)).setText(Html
				.fromHtml(getString(R.string.wordly_post_privacy_policy)));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent tabsIntent = new Intent(PrivacyPolicy.this, TabsActivity.class);
			tabsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(tabsIntent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		AdController.pauseAdView();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AdController.destroyAdView();
	}
}
