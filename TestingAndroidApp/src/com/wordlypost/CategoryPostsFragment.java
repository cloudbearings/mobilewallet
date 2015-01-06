package com.wordlypost;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wordlypost.WordlyPostGoogleAnalytics.TrackerName;
import com.wordlypost.adapters.CategoryPostsAdapter;
import com.wordlypost.beans.CategoryPostsRowItem;
import com.wordlypost.beans.NavDrawerItem;
import com.wordlypost.service.BuildService;
import com.wordlypost.utils.Utils;

public class CategoryPostsFragment extends Fragment {

	private ListView feedList;
	private List<CategoryPostsRowItem> rowItems;
	private boolean loadingMore = false;
	private long page = 1;
	private CategoryPostsAdapter adapter;
	private ProgressBar progressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.category_posts_fragment, container, false);
		Bundle bundle = getArguments();
		if (bundle.getSerializable("category") != null) {
			final NavDrawerItem item = (NavDrawerItem) bundle.getSerializable("category");
			try {
				Tracker t = ((WordlyPostGoogleAnalytics) getActivity().getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				t.setScreenName(item.getTitle());
				t.send(new HitBuilders.AppViewBuilder().build());

			} catch (Exception e) {
				Log.d("TAG", getString(R.string.google_analytics_error));
			}

			rowItems = new ArrayList<CategoryPostsRowItem>();
			feedList = (ListView) view.findViewById(R.id.categoryPostsList);
			progressBar = (ProgressBar) view.findViewById(R.id.categoryPostsProgressBar);
			adapter = new CategoryPostsAdapter(getActivity(), R.layout.category_posts_list_item,
					rowItems);

			int spacing = (int) getResources().getDimension(R.dimen.spacing);
			int itemsPerRow = getResources().getInteger(R.integer.items_per_row);
			MultiItemRowListAdapter wrapperAdapter = new MultiItemRowListAdapter(getActivity(),
					adapter, itemsPerRow, spacing);
			feedList.setAdapter(wrapperAdapter);

			feedList.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
						int totalItemCount) {

					int lastInScreen = firstVisibleItem + visibleItemCount;
					if ((lastInScreen == totalItemCount) && !(loadingMore)) {
						if (Utils.isNetworkAvailable(getActivity())) {
							loadingMore = true;
							progressBar.setVisibility(View.VISIBLE);

							BuildService.build.getCategoriesPosts(item.getId() + "",
									item.getSlug(), page, new Callback<String>() {

										@Override
										public void failure(RetrofitError retofitError) {
											retofitError.printStackTrace();
										}

										@Override
										public void success(String output, Response arg1) {

											try {
												Log.i("CategoriesPosts", arg1.getUrl());
												Log.i("CategoriesPosts", output);

												JSONObject obj = new JSONObject(output);

												if (obj.getJSONArray("posts").length() > 0) {
													JSONArray categotyPosts = obj
															.getJSONArray("posts");

													for (int i = 0; i < categotyPosts.length(); i++) {
														JSONObject categotyPost = categotyPosts
																.getJSONObject(i);

														CategoryPostsRowItem item = new CategoryPostsRowItem();

														item.setPost_id(categotyPost.getInt("id"));
														item.setTitle(categotyPost
																.getString("title"));
														item.setDate(categotyPost.getString("date"));
														item.setPost_icon_url(categotyPost
																.getString("thumbnail"));
														item.setAuthor(categotyPost.getJSONObject(
																"author").getString("name"));
														item.setContent(categotyPost
																.getString("content"));
														item.setPost_banner(categotyPost
																.getJSONObject("thumbnail_images")
																.getJSONObject("full")
																.getString("url"));
														item.setComment_count(categotyPost
																.getString("comment_count"));
														item.setPost_url(categotyPost
																.getString("url"));

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
													Utils.displayToad(getActivity(),
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
							Utils.displayToad(getActivity(), getString(R.string.no_internet));
						}
					}
				}
			});
		}
		return view;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}