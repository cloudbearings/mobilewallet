package com.mobilewallet;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.mobilewallet.adapters.TabsAdapter;
import com.mobilewallet.utils.Utils;

public class TabsActivity extends ActionBarActivity implements
		android.support.v7.app.ActionBar.TabListener {

	private ActionBar actionBar;
	private TabsAdapter mAdapter;
	private String tabs[] = { "LEARN GK", "INVITE", "RECHARGE", "MORE" };
	private ViewPager viewPager;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs);

		if (android.os.Build.VERSION.SDK_INT > 10) {
			// Adding custom action bar
			getSupportActionBar().setDisplayOptions(
					ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
			getSupportActionBar().setCustomView(R.layout.tabs_actionbar);

			try {
				View homeIcon = findViewById(android.R.id.home);
				((View) homeIcon.getParent()).setVisibility(View.GONE);
			} catch (Exception e) {

			}
		} else {
			getSupportActionBar().setDisplayOptions(
					ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
			getSupportActionBar().setCustomView(R.layout.custom_actionbar);
		}

		// Adding custom actionbar
		getSupportActionBar().setCustomView(R.layout.custom_actionbar);

		TextView activity_title = (TextView) findViewById(R.id.actionbar_title);
		activity_title.setTypeface(Utils.getFont(TabsActivity.this, getString(R.string.Helvetica)),
				Typeface.BOLD);
		activity_title.setText(getString(R.string.app_name));

		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getSupportActionBar();
		mAdapter = new TabsAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		if (getIntent().getStringExtra(getString(R.string.recharge_tab)) != null
				&& getIntent().getStringExtra(getString(R.string.recharge_tab)).equals(
						getString(R.string.show_true))) {
			// showRechargeTab string from Balance Activity
			changetab(1);
			getIntent().removeExtra(getString(R.string.recharge_tab));
		}

	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
	}

	public void changetab(int tabIndex) {
		viewPager.setCurrentItem(tabIndex);
	}
}
