package com.wordlypost;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.wordlypost.adapters.PageViewSwipeAdapter;
import com.wordlypost.beans.PostRowItem;
import com.wordlypost.dao.HomeDAO;
import com.wordlypost.google.adcontroller.AdController;
import com.wordlypost.service.BuildService;
import com.wordlypost.utils.Utils;

public class HomePostsSwipeViewActivity extends ActionBarActivity {

	private static final String TAG = "HomePostsSwipeViewActivity";
	private ViewPager viewPager;
	private PageViewSwipeAdapter mAdapter;
	private List<Fragment> fragments;
	private String postTitle, postUrl;
	private ArrayList<PostRowItem> posts;
	private long page = 1;
	private boolean loadingMore = false;
	private HomeDAO homeDAO;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_post_view);
		try {

			progressBar = (ProgressBar) findViewById(R.id.home_progress_bar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);

			fragments = new Vector<Fragment>();

			getPostsFromServer();
			/*
			 * ArrayList<PostRowItem> rowItems = (ArrayList<PostRowItem>)
			 * getIntent().getSerializableExtra("postDetails"); if
			 * (rowItems.size() > 0) { generateOffers(rowItems); }
			 */

			// Initilization
			viewPager = (ViewPager) findViewById(R.id.postViewPager);

			mAdapter = new PageViewSwipeAdapter(
					super.getSupportFragmentManager(), fragments);

			viewPager.setAdapter(mAdapter);

			posts = new ArrayList<PostRowItem>();

			/**
			 * on swiping the viewpager make respective tab selected
			 * */
			viewPager
					.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

						@Override
						public void onPageSelected(int position) {
							 addPostUrl(position);
							if (position == posts.size()) {
								if (!loadingMore) {
									getPostsFromServer();
								}
							}
						}

						@Override
						public void onPageScrolled(int pos, float arg1, int arg2) {
						}

						@Override
						public void onPageScrollStateChanged(int arg0) {
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getPostsFromServer() {
		if (getIntent().getIntExtra("categoryId", 0) != 0
				&& getIntent().getStringExtra("slug") != null) {
			final int categoryId = getIntent().getIntExtra("categoryId", 0);
			final String sulg = getIntent().getStringExtra("slug");
			homeDAO = new HomeDAO(HomePostsSwipeViewActivity.this);
			if (page == 1
					&& Utils.callPostsUrl(HomePostsSwipeViewActivity.this)
					&& homeDAO.getPostsCount(categoryId, sulg) > 6) {
				posts = homeDAO.getPosts(categoryId, sulg);
				generateOffers(posts);
				showTab(getIntent().getIntExtra("position", 0));
			} else {
				progressBar.setVisibility(View.VISIBLE);
				loadingMore = true;

				BuildService.build.getCategoriesPosts(categoryId, sulg, page,
						new Callback<String>() {

							@Override
							public void success(String output, Response arg1) {
								try {
									Log.i("Home Post View :", arg1.getUrl());
									Log.i("Home Post View :", output);
									JSONObject obj = new JSONObject(output);

									if (obj.getJSONArray("posts").length() > 0) {

										Utils.storePostsLoadedDate(HomePostsSwipeViewActivity.this);

										JSONArray categotyPosts = obj
												.getJSONArray("posts");
										String currentMilliSeconds = Calendar
												.getInstance()
												.getTimeInMillis()
												+ "";

										for (int i = 0; i < categotyPosts
												.length(); i++) {
											JSONObject categotyPost = categotyPosts
													.getJSONObject(i);

											PostRowItem item = new PostRowItem();

											item.setPost_id(categotyPost
													.getInt("id"));
											item.setTitle(categotyPost
													.getString("title"));
											item.setDate(categotyPost
													.getString("date"));
											item.setPost_icon_url(categotyPost
													.getString("thumbnail"));
											item.setAuthor(categotyPost
													.getJSONObject("author")
													.getString("name"));
											item.setContent(categotyPost
													.getString("content"));
											item.setPost_banner(categotyPost
													.getJSONObject(
															"thumbnail_images")
													.getJSONObject("full")
													.getString("url"));
											item.setComment_count(categotyPost
													.getInt("comment_count"));
											item.setPost_url(categotyPost
													.getString("url"));

											if (categotyPost
													.getInt("comment_count") > 0) {
												item.setCommentsArray(categotyPost
														.getJSONArray(
																"comments")
														.toString());
											}
											item.setTagsArray(categotyPost
													.getJSONArray("tags")
													.toString());
											item.setPost_des(categotyPost
													.getString("excerpt"));

											posts.add(item);

											if (page == 1) {
												/*
												 * Inserting category posts in
												 * sqlite.
												 */
												homeDAO.insertPosts(
														categotyPost
																.getInt("id"),
														categotyPost
																.getString("title"),
														categotyPost
																.getString("date"),
														categotyPost
																.getString("thumbnail"),
														categotyPost
																.getJSONObject(
																		"author")
																.getString(
																		"name"),
														categotyPost
																.getString("content"),
														categotyPost
																.getJSONObject(
																		"thumbnail_images")
																.getJSONObject(
																		"full")
																.getString(
																		"url"),
														categotyPost
																.getInt("comment_count"),
														categotyPost
																.getString("url"),
														currentMilliSeconds,
														categotyPost
																.getString("excerpt"),
														categoryId,
														sulg,
														"",
														categotyPost
																.getJSONArray(
																		"comments")
																.toString(),
														categotyPost
																.getJSONArray(
																		"tags")
																.toString());
											}
										}

										if (page == 1) {
											long deleted = homeDAO.deletePosts(
													categoryId, sulg,
													currentMilliSeconds);
											Log.i("Deleted records: ", deleted
													+ "");
										}

										generateOffers(posts);
										progressBar.setVisibility(View.GONE);
										mAdapter.notifyDataSetChanged();
										showTab(getIntent().getIntExtra(
												"position", 0));

										if (page == obj.getInt("pages")) {
											Log.i("loadingmore", "true");
											loadingMore = true;
										} else {
											page = page + 1;
											loadingMore = false;
										}

									} else {
										Utils.displayToad(
												HomePostsSwipeViewActivity.this,
												getString(R.string.no_posts_error_msg));
									}
								} catch (Exception e) {
									Log.d(TAG,
											"Exception raised to get category posts from server.");
									e.printStackTrace();
								}
							}

							@Override
							public void failure(RetrofitError retrofitError) {
								retrofitError.printStackTrace();
							}
						});
			}
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
			Intent tabsIntent = new Intent(HomePostsSwipeViewActivity.this,
					TabsActivity.class);
			tabsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(tabsIntent);
			return true;
		case R.id.share_icon:
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, postUrl).putExtra(
					Intent.EXTRA_SUBJECT, postTitle);
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
