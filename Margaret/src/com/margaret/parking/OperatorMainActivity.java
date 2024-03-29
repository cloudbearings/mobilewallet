package com.margaret.parking;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.margaret.parking.activity.ComplaintRegistration;
import com.margaret.parking.adapters.NavDrawerItem;
import com.margaret.parking.adapters.NavDrawerListAdapter;
import com.margaret.parking.fragment.ClampedComplaints;
import com.margaret.parking.fragment.ClosedComplaints;
import com.margaret.parking.fragment.ComplaintsFragment;
import com.margaret.parking.fragment.NewComplaintsFragment;
import com.margaret.parking.fragment.PaidComplaintsFragment;
import com.margaret.parking.fragment.TowedComplaintsFragment;
import com.margaret.parking.fragment.VerifiedComplaints;
import com.margaret.parking.util.PreferenceStore;

public class OperatorMainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	ComplaintsFragment complaintFragment;
	ClosedComplaints closedComplaintsFragment;
	ClampedComplaints clampedComplaintFragment;
	TowedComplaintsFragment towedComplaintFragment;
	PaidComplaintsFragment paidComplaintFragment;
	NewComplaintsFragment newComplaintsFragment;
	VerifiedComplaints verifiedComplaints;

	final int COMPLAINT_NEW_REQUEST = 1;
	final int COMPLAINT_VERIFICATION_REQUEST = 2;
	final int COMPLAINT_CLAMP_REQUEST = 3;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Complaints
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		// New
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		// Verified
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));
		// Clamp
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1)));
		// Towing
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));
		// Payment
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1)));
		// Closed
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons
				.getResourceId(6, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
	}

	/**
	 * Slide menu item click listener
	 */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setQueryHint(getString(R.string.search_hint));

		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
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
				startActivity(new Intent(OperatorMainActivity.this, SearchComplaint.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra(
								"search", query));
				return true;
			}
		};
		searchView.setOnQueryTextListener(textChangeListener);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_logout:
			PreferenceStore.saveLoggedIn(getApplicationContext(), false);
			finish();
			return true;
		case R.id.action_new_complaint:
			final Intent intent = new Intent(getApplicationContext(),
					ComplaintRegistration.class);
			startActivityForResult(intent, COMPLAINT_NEW_REQUEST);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_logout).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			complaintFragment = ComplaintsFragment.newInstance();
			fragment = complaintFragment;
			break;
		case 1:
			newComplaintsFragment = NewComplaintsFragment.getInstance();
			fragment = newComplaintsFragment;
			break;
		case 2:
			verifiedComplaints = VerifiedComplaints.getInstance();
			fragment = verifiedComplaints;
			break;
		case 3:
			clampedComplaintFragment = ClampedComplaints.getInstance();
			fragment = clampedComplaintFragment;
			break;
		case 4:
			towedComplaintFragment = TowedComplaintsFragment.getInstance();
			fragment = towedComplaintFragment;
			break;
		case 5:
			paidComplaintFragment = PaidComplaintsFragment.getInstance();
			fragment = paidComplaintFragment;
			break;
		case 6:
			// fragment = new PagesFragment();
			closedComplaintsFragment = ClosedComplaints.getInstance();
			fragment = closedComplaintsFragment;
			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK
				&& (requestCode == COMPLAINT_NEW_REQUEST
						|| requestCode == COMPLAINT_VERIFICATION_REQUEST
						|| requestCode == COMPLAINT_CLAMP_REQUEST
						|| requestCode == 4 || requestCode == 5)
				&& complaintFragment != null) {

			complaintFragment.refreshUI();

			if (requestCode == COMPLAINT_VERIFICATION_REQUEST
					&& newComplaintsFragment != null) {
				newComplaintsFragment.refreshUI();
			}
			if (requestCode == COMPLAINT_CLAMP_REQUEST
					&& verifiedComplaints != null) {
				verifiedComplaints.refreshUI();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
