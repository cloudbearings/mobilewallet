package com.wordlypost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wordlypost.WordlyPostGoogleAnalytics.TrackerName;
import com.wordlypost.utils.Utils;

public class AboutWordlyPost extends ActionBarActivity {

	String condition = "0";
	String ms;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			Tracker t = ((WordlyPostGoogleAnalytics) getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			t.setScreenName(getString(R.string.about_wordlypost_screen_name));
			t.send(new HitBuilders.AppViewBuilder().build());

		} catch (Exception e) {
			Log.d("TAG", getString(R.string.google_analytics_error));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_wordly_post);

		condition = getIntent().getStringExtra("alert");
		if (condition != null) {
			ms = getIntent().getStringExtra("msg");
			Utils.displayToad(AboutWordlyPost.this, ms);
		}

		((TextView) findViewById(R.id.about_us)).setText(Html
				.fromHtml(getString(R.string.about_wordly_post)));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent tabsIntent = new Intent(AboutWordlyPost.this, TabsActivity.class);
			tabsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(tabsIntent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
