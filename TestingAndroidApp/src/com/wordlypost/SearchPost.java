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
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wordlypost.WordlyPostGoogleAnalytics.TrackerName;
import com.wordlypost.adapters.PostAdapter;
import com.wordlypost.beans.PostRowItem;
import com.wordlypost.dao.SearchPostsDAO;
import com.wordlypost.service.BuildService;
import com.wordlypost.utils.Utils;

public class SearchPost extends ActionBarActivity {

	private ListView searchPostsList;
	private ArrayList<PostRowItem> rowItems;
	private boolean loadingMore = false;
	private long page = 1;
	private PostAdapter adapter;
	private ProgressBar progressBar;
	private SearchPostsDAO searchPostsDAO;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			Tracker t = ((WordlyPostGoogleAnalytics) getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			t.setScreenName(getString(R.string.search_posts_screen_name));
			t.send(new HitBuilders.AppViewBuilder().build());

		} catch (Exception e) {
			Log.d("TAG", getString(R.string.google_analytics_error));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_post);
		rowItems = new ArrayList<PostRowItem>();
		searchPostsList = (ListView) findViewById(R.id.searchPostsList);
		progressBar = (ProgressBar) findViewById(R.id.searchPostsProgressBar);
		adapter = new PostAdapter(SearchPost.this, R.layout.post_list_item,
				rowItems);
		searchPostsList.setAdapter(adapter);

		searchPostsList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				int lastInScreen = firstVisibleItem + visibleItemCount;
				if ((lastInScreen == totalItemCount) && !(loadingMore)) {
					if (Utils.isNetworkAvailable(SearchPost.this)) {
						getDataFromServer();
					} else {
						getDataFromSqlite();
					}
				}
			}
		});

		searchPostsList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startActivity(new Intent(SearchPost.this,
						PostViewSwipeActivity.class)
						.putExtra("postDetails", rowItems)
						.putExtra("position", position)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			}
		});
	}

	private void getDataFromServer() {
		if (getIntent().getStringExtra("search") != null) {
			loadingMore = true;
			progressBar.setVisibility(View.VISIBLE);

			BuildService.build.searchPosts(
					getIntent().getStringExtra("search"), page,
					new Callback<String>() {

						@Override
						public void failure(RetrofitError retofitError) {
							retofitError.printStackTrace();
						}

						@Override
						public void success(String output, Response res) {

							try {
								Log.i("searchPosts", res.getUrl());
								Log.i("searchPosts", output);

								JSONObject obj = new JSONObject(output);

								if (obj.getJSONArray("posts").length() > 0) {
									JSONArray categotyPosts = obj
											.getJSONArray("posts");
									String currentMilliSeconds = Calendar
											.getInstance().getTimeInMillis()
											+ "";

									for (int i = 0; i < categotyPosts.length(); i++) {
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
										item.setPost_des(categotyPost
												.getString("excerpt"));

										if (categotyPost
												.getInt("comment_count") > 0) {
											item.setCommentsArray(categotyPost
													.getJSONArray("comments")
													.toString());
										}
										item.setTagsArray(categotyPost
												.getJSONArray("tags")
												.toString());

										rowItems.add(item);

										if (page == 1) {
											searchPostsDAO = new SearchPostsDAO(
													SearchPost.this);
											searchPostsDAO.insertSearchPosts(
													categotyPost.getInt("id"),
													categotyPost
															.getString("title"),
													categotyPost
															.getString("date"),
													categotyPost
															.getString("thumbnail"),
													categotyPost.getJSONObject(
															"author")
															.getString("name"),
													categotyPost
															.getString("content"),
													categotyPost
															.getJSONObject(
																	"thumbnail_images")
															.getJSONObject(
																	"full")
															.getString("url"),
													categotyPost
															.getInt("comment_count"),
													categotyPost
															.getString("url"),
													currentMilliSeconds,
													categotyPost
															.getString("excerpt"),
													categotyPost.getJSONArray(
															"comments")
															.toString(),
													categotyPost.getJSONArray(
															"tags").toString());
										}
									}

									if (page == 1) {
										long deleted = searchPostsDAO
												.deleteSearchPosts(currentMilliSeconds);
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
									TextView errorMsg = (TextView) findViewById(R.id.error_msg);
									errorMsg.setVisibility(View.VISIBLE);
									errorMsg.setText(R.string.no_posts_error_msg);
								}
								progressBar.setVisibility(View.GONE);

							} catch (Exception e) {
								progressBar.setVisibility(View.GONE);
								e.printStackTrace();
							}
						}
					});
		}
	}

	private void getDataFromSqlite() {
		loadingMore = true;
		progressBar.setVisibility(View.VISIBLE);
		searchPostsDAO = new SearchPostsDAO(SearchPost.this);

		if (searchPostsDAO.getSearchPostsCount() > 0) {
			rowItems = searchPostsDAO.getSearchPosts();
			adapter = new PostAdapter(SearchPost.this, R.layout.post_list_item,
					rowItems);

			searchPostsList.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		} else {
			Utils.displayToad(SearchPost.this,
					getString(R.string.no_posts_error_msg));
		}
		progressBar.setVisibility(View.GONE);
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
