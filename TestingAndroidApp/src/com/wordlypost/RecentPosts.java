package com.wordlypost;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wordlypost.WordlyPostGoogleAnalytics.TrackerName;
import com.wordlypost.adapters.PostAdapter;
import com.wordlypost.beans.PostRowItem;
import com.wordlypost.dao.RecentPostsDAO;
import com.wordlypost.google.adcontroller.AdController;
import com.wordlypost.service.BuildService;
import com.wordlypost.utils.Utils;

public class RecentPosts extends ActionBarActivity {

	private ListView recentPostsList;
	private ArrayList<PostRowItem> rowItems;
	private boolean loadingMore = false;
	private long page = 1;
	private PostAdapter adapter;
	private ProgressBar progressBar;
	private RecentPostsDAO recentPostsDAO;
	private AdController adController;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {

			if (adController != null) {
				adController.resumeAdView();
			} else {
				new AdController().resumeAdView();
			}

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

		try {
			RelativeLayout bannerLayout = (RelativeLayout) findViewById(R.id.recentBannerAd);
			new AdController().bannerAd(RecentPosts.this, bannerLayout,
					getString(R.string.recent_posts_banner_unit_id));
		} catch (Exception e) {
			e.printStackTrace();
		}

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
						getDataFromServer();
					} else {
						getDataFromSqlite();
					}
				}
			}
		});

		recentPostsList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startActivity(new Intent(RecentPosts.this, PostViewSwipeActivity.class)
						.putExtra("postDetails", rowItems).putExtra("position", position)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			}
		});
	}

	private void getDataFromServer() {
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

						String currentMilliSeconds = Calendar.getInstance().getTimeInMillis() + "";

						for (int i = 0; i < categotyPosts.length(); i++) {
							JSONObject categotyPost = categotyPosts.getJSONObject(i);

							PostRowItem item = new PostRowItem();

							item.setPost_id(categotyPost.getInt("id"));
							item.setTitle(categotyPost.getString("title"));
							item.setDate(categotyPost.getString("date"));
							item.setPost_icon_url(categotyPost.getString("thumbnail"));
							item.setAuthor(categotyPost.getJSONObject("author").getString("name"));
							item.setContent(categotyPost.getString("content"));
							item.setPost_banner(categotyPost.getJSONObject("thumbnail_images")
									.getJSONObject("full").getString("url"));
							item.setComment_count(categotyPost.getInt("comment_count"));
							item.setPost_url(categotyPost.getString("url"));
							item.setPost_des(categotyPost.getString("excerpt"));

							if (categotyPost.getInt("comment_count") > 0) {
								item.setCommentsArray(categotyPost.getJSONArray("comments")
										.toString());
							}
							item.setTagsArray(categotyPost.getJSONArray("tags").toString());

							rowItems.add(item);

							if (page == 1) {
								recentPostsDAO = new RecentPostsDAO(RecentPosts.this);
								recentPostsDAO.insertRecentPosts(categotyPost.getInt("id"),
										categotyPost.getString("title"), categotyPost
												.getString("date"), categotyPost
												.getString("thumbnail"), categotyPost
												.getJSONObject("author").getString("name"),
										categotyPost.getString("content"),
										categotyPost.getJSONObject("thumbnail_images")
												.getJSONObject("full").getString("url"),
										categotyPost.getInt("comment_count"), categotyPost
												.getString("url"), currentMilliSeconds,
										categotyPost.getString("excerpt"), categotyPost
												.getJSONArray("comments").toString(), categotyPost
												.getJSONArray("tags").toString());
							}
						}

						if (page == 1) {
							long deleted = recentPostsDAO.deleteRecentPosts(currentMilliSeconds);
							Log.i("Deleted Records: ", deleted + "");
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
						Utils.displayToad(RecentPosts.this, getString(R.string.no_posts_error_msg));
					}
					progressBar.setVisibility(View.GONE);

				} catch (Exception e) {
					progressBar.setVisibility(View.GONE);
					e.printStackTrace();
				}
			}
		});
	}

	private void getDataFromSqlite() {
		loadingMore = true;
		progressBar.setVisibility(View.VISIBLE);
		recentPostsDAO = new RecentPostsDAO(RecentPosts.this);

		if (recentPostsDAO.getRecentPostsCount() > 0) {
			rowItems = recentPostsDAO.getRecentPosts();
			adapter = new PostAdapter(RecentPosts.this, R.layout.post_list_item, rowItems);

			recentPostsList.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		} else {
			Utils.displayToad(RecentPosts.this, getString(R.string.no_posts_error_msg));
		}
		progressBar.setVisibility(View.GONE);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent tabsIntent = new Intent(RecentPosts.this, TabsActivity.class);
			tabsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(tabsIntent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (adController != null) {
			adController.pauseAdView();
		} else {
			new AdController().pauseAdView();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (adController != null) {
			adController.destroyAdView();
		} else {
			new AdController().destroyAdView();
		}
	}
}
