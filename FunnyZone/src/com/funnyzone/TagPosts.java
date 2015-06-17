package com.funnyzone;

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
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.funnyzone.beans.PostRowItem;
import com.funnyzone.dao.TagPostsDAO;
import com.funnyzone.googleanalytics.FunnyZoneGoogleAnalytics;
import com.funnyzone.googleanalytics.FunnyZoneGoogleAnalytics.TrackerName;
import com.funnyzone.service.BuildService;
import com.funnyzone.utils.Utils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class TagPosts extends ActionBarActivity {

	private ListView tagPostsList;
	private ArrayList<PostRowItem> rowItems;
	private boolean loadingMore = false;
	private long page = 1;
	private PostAdapter adapter;
	private ProgressBar progressBar;
	private TextView tagTitle;
	private TagPostsDAO tagPostsDAO;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			Tracker t = ((FunnyZoneGoogleAnalytics) getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			t.setScreenName(getString(R.string.tag_posts_screen_name));
			t.send(new HitBuilders.AppViewBuilder().build());

		} catch (Exception e) {
			Log.d("TAG", getString(R.string.google_analytics_error));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tag_posts);
		if (getIntent().getStringExtra(getString(R.string.tagSlug)) != null
				&& !"".equals(getIntent().getStringExtra(
						getString(R.string.tagSlug)))) {

			tagTitle = (TextView) findViewById(R.id.tag_title);
			rowItems = new ArrayList<PostRowItem>();
			tagPostsList = (ListView) findViewById(R.id.tagPostsList);
			progressBar = (ProgressBar) findViewById(R.id.tagPostsProgressBar);
			adapter = new PostAdapter(TagPosts.this, R.layout.post_list_item,
					rowItems);
			tagPostsList.setAdapter(adapter);

			tagPostsList.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {

					int lastInScreen = firstVisibleItem + visibleItemCount;
					if ((lastInScreen == totalItemCount) && !(loadingMore)) {
						if (Utils.isNetworkAvailable(TagPosts.this)) {
							getDataFromServer();
						} else {
							getDataFromSqlite();
						}
					}
				}
			});
		}

		tagPostsList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startActivity(new Intent(TagPosts.this,
						PostViewSwipeActivity.class)
						.putExtra("postDetails", rowItems)
						.putExtra("position", position)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			}
		});
	}

	private void getDataFromServer() {
		loadingMore = true;
		progressBar.setVisibility(View.VISIBLE);
		// args[0] : id, args[1] : slug
		String[] args = getIntent().getStringExtra(getString(R.string.tagSlug))
				.split("\\$");

		BuildService.build.tagPosts(Integer.parseInt(args[0]), args[1], page,
				new Callback<String>() {

					@Override
					public void failure(RetrofitError retofitError) {
						retofitError.printStackTrace();
					}

					@Override
					public void success(String output, Response res) {

						try {
							Log.i("tagPosts", res.getUrl());
							Log.i("tagPosts", output);

							JSONObject obj = new JSONObject(output);
							tagTitle.setText(Html.fromHtml(obj.getJSONObject(
									"tag").getString("title")));

							if (obj.getJSONArray("posts").length() > 0) {
								JSONArray tagPosts = obj.getJSONArray("posts");

								String currentMilliSeconds = Calendar
										.getInstance().getTimeInMillis() + "";

								for (int i = 0; i < tagPosts.length(); i++) {
									JSONObject tagPost = tagPosts
											.getJSONObject(i);

									PostRowItem item = new PostRowItem();

									item.setPost_id(tagPost.getInt("id"));
									item.setTitle(tagPost.getString("title"));
									item.setDate(tagPost.getString("date"));
									item.setPost_icon_url(tagPost
											.getString("thumbnail"));
									item.setAuthor(tagPost.getJSONObject(
											"author").getString("name"));
									item.setContent(tagPost
											.getString("content"));
									item.setPost_banner(tagPost
											.getJSONObject("thumbnail_images")
											.getJSONObject("full")
											.getString("url"));
									item.setComment_count(tagPost
											.getInt("comment_count"));
									item.setPost_url(tagPost.getString("url"));
									item.setPost_des(tagPost
											.getString("excerpt"));

									if (tagPost.getInt("comment_count") > 0) {
										item.setCommentsArray(tagPost
												.getJSONArray("comments")
												.toString());
									}
									item.setTagsArray(tagPost.getJSONArray(
											"tags").toString());

									rowItems.add(item);

									if (page == 1) {
										tagPostsDAO = new TagPostsDAO(
												TagPosts.this);
										tagPostsDAO.insertTagPosts(
												tagPost.getInt("id"),
												tagPost.getString("title"),
												tagPost.getString("date"),
												tagPost.getString("thumbnail"),
												tagPost.getJSONObject("author")
														.getString("name"),
												tagPost.getString("content"),
												tagPost.getJSONObject(
														"thumbnail_images")
														.getJSONObject("full")
														.getString("url"),
												tagPost.getInt("comment_count"),
												tagPost.getString("url"),
												currentMilliSeconds,
												tagPost.getString("excerpt"),
												tagPost.getJSONArray("comments")
														.toString(), tagPost
														.getJSONArray("tags")
														.toString());
									}
								}

								if (page == 1) {
									long deleted = tagPostsDAO
											.deleteTagPosts(currentMilliSeconds);
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
								tagTitle.setText(R.string.no_posts_error_msg);
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
		tagPostsDAO = new TagPostsDAO(TagPosts.this);

		if (tagPostsDAO.getTagPostsCount() > 0) {
			rowItems = tagPostsDAO.getTagPosts();
			adapter = new PostAdapter(TagPosts.this, R.layout.post_list_item,
					rowItems);

			tagPostsList.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		} else {
			Utils.displayToad(TagPosts.this,
					getString(R.string.no_posts_error_msg));
		}
		progressBar.setVisibility(View.GONE);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent tabsIntent = new Intent(TagPosts.this, TabsActivity.class);
			tabsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(tabsIntent);
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
