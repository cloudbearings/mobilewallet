package com.wordlypost;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wordlypost.WordlyPostGoogleAnalytics.TrackerName;
import com.wordlypost.adapters.PostAdapter;
import com.wordlypost.beans.PostRowItem;
import com.wordlypost.service.BuildService;
import com.wordlypost.utils.Utils;

public class RecentPosts extends ActionBarActivity {

	private ListView recentPostsList;
	private ArrayList<PostRowItem> rowItems;
	private boolean loadingMore = false;
	private long page = 1;
	private PostAdapter adapter;
	private ProgressBar progressBar;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			Tracker t = ((WordlyPostGoogleAnalytics) getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			t.setScreenName(getString(R.string.recent_posts_screen_name));
			t.send(new HitBuilders.AppViewBuilder().build());

		} catch (Exception e) {
			Log.d("TAG", getString(R.string.google_analytics_error));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recent_posts);
		rowItems = new ArrayList<PostRowItem>();
		recentPostsList = (ListView) findViewById(R.id.recentPostsList);
		progressBar = (ProgressBar) findViewById(R.id.recentPostsProgressBar);
		adapter = new PostAdapter(RecentPosts.this, R.layout.post_list_item, rowItems);
		recentPostsList.setAdapter(adapter);

		recentPostsList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
					int totalItemCount) {

				int lastInScreen = firstVisibleItem + visibleItemCount;
				if ((lastInScreen == totalItemCount) && !(loadingMore)) {
					if (Utils.isNetworkAvailable(RecentPosts.this)) {
						loadingMore = true;
						progressBar.setVisibility(View.VISIBLE);

						BuildService.build.recentPosts(page, new Callback<String>() {

							@Override
							public void failure(RetrofitError retofitError) {
								retofitError.printStackTrace();
							}

							@Override
							public void success(String output, Response res) {

								try {
									Log.i("recentPosts", res.getUrl());
									Log.i("recentPosts", output);

									JSONObject obj = new JSONObject(output);

									if (obj.getJSONArray("posts").length() > 0) {
										JSONArray categotyPosts = obj.getJSONArray("posts");

										for (int i = 0; i < categotyPosts.length(); i++) {
											JSONObject categotyPost = categotyPosts
													.getJSONObject(i);

											PostRowItem item = new PostRowItem();

											item.setPost_id(categotyPost.getInt("id"));
											item.setTitle(categotyPost.getString("title"));
											item.setDate(categotyPost.getString("date"));
											item.setPost_icon_url(categotyPost
													.getString("thumbnail"));
											item.setAuthor(categotyPost.getJSONObject("author")
													.getString("name"));
											item.setContent(categotyPost.getString("content"));
											item.setPost_banner(categotyPost
													.getJSONObject("thumbnail_images")
													.getJSONObject("full").getString("url"));
											item.setComment_count(categotyPost
													.getInt("comment_count"));
											item.setPost_url(categotyPost.getString("url"));
											item.setPost_des(categotyPost.getString("excerpt"));

											if (categotyPost.getInt("comment_count") > 0) {
												item.setCommentsArray(categotyPost
														.getJSONArray("comments").toString());
											}

											rowItems.add(item);
										}

										adapter.notifyDataSetChanged();
										loadingMore = false;

										if (page == obj.getInt("pages")) {
											Log.i("loadingmore", "true");
											loadingMore = true;
										} else {
											page = page + 1;
										}
									} else {
										Utils.displayToad(RecentPosts.this,
												getString(R.string.no_posts_error_msg));
									}
									progressBar.setVisibility(View.GONE);

								} catch (Exception e) {
									progressBar.setVisibility(View.GONE);
									e.printStackTrace();
								}
							}
						});
					} else {
						Utils.displayToad(RecentPosts.this, getString(R.string.no_internet));
					}
				}
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
