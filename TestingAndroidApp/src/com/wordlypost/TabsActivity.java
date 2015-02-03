package com.wordlypost;

import java.util.ArrayList;
import java.util.List;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wordlypost.WordlyPostGoogleAnalytics.TrackerName;
import com.wordlypost.adapters.NavDrawerListAdapter;
import com.wordlypost.beans.NavDrawerItem;
import com.wordlypost.dao.CategoriesDAO;
import com.wordlypost.gcm.GcmRegistration;

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
		new GcmRegistration(TabsActivity.this).register();
		setContentView(R.layout.tabs_activity);
		try {

			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

			navDrawerItems = new ArrayList<NavDrawerItem>();

			// adding nav drawer items to array
			if (getIntent().getStringExtra("categories") != null) {
				JSONObject obj = new JSONObject(getIntent().getStringExtra("categories"));
				// JsonObject is not null data is coming from server
				setNavigationItems(1, 20, "home", getString(R.string.title_activity_home_fragment));

				for (int i = 0; i < obj.getJSONArray("categories").length(); i++) {
					JSONObject category = obj.getJSONArray("categories").getJSONObject(i);
					if (!category.getString("id").equals("174")) {
						if (category.getString("id").equals("120")) {
							navDrawerItems.add(
									1,
									new NavDrawerItem(category.getInt("id"), category
											.getInt("post_count"), category.getString("slug"),
											category.getString("title")));
						} else {
							setNavigationItems(category.getInt("id"),
									category.getInt("post_count"), category.getString("slug"),
									category.getString("title"));
						}
					}
				}
			} else {
				/*
				 * obj is equal to null categories data is getting from sqlite
				 * database.
				 */
				setNavigationItems(1, 20, "home", getString(R.string.title_activity_home_fragment));

				CategoriesDAO categoriesDAO = new CategoriesDAO(TabsActivity.this);
				if (categoriesDAO.getCategoriesCount() > 0) {
					List<NavDrawerItem> items = categoriesDAO.getCategories();
					for (NavDrawerItem item : items) {
						if (item.getId() != 174) {
							if (item.getId() == 120) {
								navDrawerItems.add(
										1,
										new NavDrawerItem(item.getId(), item.getPost_count(), item
												.getSlug(), item.getTitle()));
							} else {
								setNavigationItems(item.getId(), item.getPost_count(),
										item.getSlug(), item.getTitle());
							}
						}
					}
				}
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

			if (savedInstanceState == null) {
				// on first time display view for first nav item
				displayView(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setNavigationItems(int id, int postCount, String slug, String title) {
		navDrawerItems.add(new NavDrawerItem(id, postCount, slug, title));
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
			Fragment fragment = null;
			if (position == 0) {
				fragment = new HomeFragment();
			} else {
				fragment = new CategoryPostsFragment();
				Bundle bundle = new Bundle();
				bundle.putSerializable("category", navDrawerItems.get(position));
				fragment.setArguments(bundle);
			}

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

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		searchView.setQueryHint(getString(R.string.search_hint));

		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false);

		SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextChange(String newText) {
				// this is your adapter that will be filtered
				return true;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				// this is your adapter that will be filtered
				Log.i("on query submit: ", query);
				startActivity(new Intent(TabsActivity.this, SearchPost.class).addFlags(
						Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("search", query));
				return true;
			}
		};
		searchView.setOnQueryTextListener(textChangeListener);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.recent_posts:
			startActivity(new Intent(TabsActivity.this, RecentPosts.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		case R.id.share_this_app:
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND)
					.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject))
					.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text))
					.setType("text/plain");
			startActivity(sendIntent);
			return true;
		case R.id.rate_this_app:
			Intent rateIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(getString(R.string.playstore_url)));
			startActivity(rateIntent);
			return true;
		case R.id.about_us:
			startActivity(new Intent(TabsActivity.this, AboutWordlyPost.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		case R.id.privacy_policy:
			startActivity(new Intent(TabsActivity.this, PrivacyPolicy.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
