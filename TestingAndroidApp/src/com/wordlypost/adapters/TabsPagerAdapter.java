package com.wordlypost.adapters;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FixedFragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.wordlypost.CategoryPostsFragment;
import com.wordlypost.beans.NavDrawerItem;

public class TabsPagerAdapter extends FixedFragmentStatePagerAdapter {

	private ArrayList<NavDrawerItem> navDrawerItems;

	public TabsPagerAdapter(FragmentManager fm, ArrayList<NavDrawerItem> navDrawerItems) {
		super(fm);
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public Fragment getItem(int index) {

		Fragment category_posts_fragment = new CategoryPostsFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("cid", navDrawerItems.get(index).getId());
		category_posts_fragment.setArguments(bundle);
		return category_posts_fragment;

	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return navDrawerItems.size();
	}
}
