package com.wordlypost.adapters;

import java.util.List;

import android.support.v4.app.FixedFragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class PageViewSwipeAdapter extends FixedFragmentStatePagerAdapter {

	private List<Fragment> fragments;

	public PageViewSwipeAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int index) {
		return fragments.get(index);
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return this.fragments.size();
	}
}
