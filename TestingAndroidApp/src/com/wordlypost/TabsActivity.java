package com.wordlypost;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wordlypost.WordlyPostGoogleAnalytics.TrackerName;
import com.wordlypost.adapters.NavDrawerListAdapter;
import com.wordlypost.beans.NavDrawerItem;

public class TabsActivity extends ActionBarActivity {

	// fileds for slider drawer
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			Tracker t = ((WordlyPostGoogleAnalytics) getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			t.setScreenName(getString(R.string.tabs_activity_screen_name));
			t.send(new HitBuilders.AppViewBuilder().build());

		} catch (Exception e) {
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs_activity);
		try {

			JSONObject obj = new JSONObject(getIntent().getStringExtra("categories"));
			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

			navDrawerItems = new ArrayList<NavDrawerItem>();

			// adding nav drawer items to array
			for (int i = 0; i < obj.getJSONArray("categories").length(); i++) {
				JSONObject category = obj.getJSONArray("categories").getJSONObject(i);
				navDrawerItems.add(new NavDrawerItem(category.getInt("id"), category
						.getInt("post_count"), category.getString("slug"), category
						.getString("title")));
			}

			// setting the nav drawer list adapter
			adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
			mDrawerList.setAdapter(adapter);

			// enabling action bar app icon and behaving it as toggle button
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);

			mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
					R.drawable.drawer_menu_icon, // nav
					// menu
					// toggle
					// icon
					R.string.app_name, // nav drawer open - description for
										// accessibility
					R.string.app_name // nav drawer close - description for
										// accessibility
			) {
				public void onDrawerClosed(View view) {
					// calling onPrepareOptionsMenu() to show action bar icons
					supportInvalidateOptionsMenu();
				}

				public void onDrawerOpened(View drawerView) {
					// calling onPrepareOptionsMenu() to hide action bar icons
					supportInvalidateOptionsMenu();
				}
			};
			mDrawerLayout.setDrawerListener(mDrawerToggle);

			mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class SlideMenuClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	private void displayView(int position) {
		// update the main content by replacing fragments
		try {
			Fragment fragment = new CategoryPostsFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("category", navDrawerItems.get(position));
			fragment.setArguments(bundle);

			if (fragment != null) {
				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
				// update selected item and title, then close the drawer
				mDrawerList.setItemChecked(position, true);
				mDrawerList.setSelection(position);
				mDrawerLayout.closeDrawer(mDrawerList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public boolean isDrawerOpened() {
		return mDrawerLayout.isDrawerOpen(mDrawerList);
	}

	public void closeDrawer() {
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.tabs, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.action_search:
			// startActivity(new Intent(TabsActivity.this, MyProfile.class));
			return true;
		case R.id.share_this_app:
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, "");
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
			return true;
		case R.id.rate_this_app:
			Intent rateIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://play.google.com/store/apps/details?id=com.wordlypost"));
			startActivity(rateIntent);
			return true;
		case R.id.about_us:
			// startActivity(new Intent(TabsActivity.this, MyProfile.class));
			return true;
		case R.id.terms_of_use:
			// startActivity(new Intent(TabsActivity.this,
			// AllCommonAppsCount.class));
			return true;
		case R.id.privacy_policy:
			// startActivity(new Intent(TabsActivity.this, MyProfile.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
