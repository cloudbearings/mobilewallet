package com.wordlypost;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.wordlypost.adapters.PageViewSwipeAdapter;
import com.wordlypost.beans.PostRowItem;

public class PostViewSwipeActivity extends ActionBarActivity {

	private ViewPager viewPager;
	private PageViewSwipeAdapter mAdapter;
	private List<Fragment> fragments;
	private String postTitle, postUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_view_swipe);
		try {

			getSupportActionBar().setDisplayHomeAsUpEnabled(true);

			fragments = new Vector<Fragment>();

			ArrayList<PostRowItem> items = (ArrayList<PostRowItem>) getIntent()
					.getSerializableExtra("postDetails");
			if (items.size() > 0) {
				generateOffers(items);
			}

			// Initilization
			viewPager = (ViewPager) findViewById(R.id.postViewPager);

			mAdapter = new PageViewSwipeAdapter(super.getSupportFragmentManager(), fragments);

			viewPager.setAdapter(mAdapter);

			showTab(getIntent().getIntExtra("position", 0));

			/**
			 * on swiping the viewpager make respective tab selected
			 * */
			viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

				@Override
				public void onPageSelected(int position) {
					addPostUrl(position);
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generateOffers(List<PostRowItem> posts) {
		try {
			for (PostRowItem post : posts) {
				Fragment fragment = new PostViewFragment();
				Bundle args = new Bundle();

				PostViewFragment of = (PostViewFragment) fragment;
				of.setPostUrl(post.getPost_url());
				of.setPostTitle(post.getTitle());

				args.putSerializable("postView", post);
				fragment.setArguments(args);
				fragments.add(fragment);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showTab(int tabIndex) {
		addPostUrl(tabIndex);
		viewPager.setCurrentItem(tabIndex);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.post_view_swipe, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent tabsIntent = new Intent(PostViewSwipeActivity.this, TabsActivity.class);
			tabsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(tabsIntent);
			return true;
		case R.id.share_icon:
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, postUrl).putExtra(Intent.EXTRA_SUBJECT,
					postTitle);
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void addPostUrl(int tabIndex) {
		Fragment fr = fragments.get(tabIndex);
		PostViewFragment o = (PostViewFragment) fr;
		postUrl = o.getPostUrl();
		postTitle = o.getPostTitle();
	}
}
