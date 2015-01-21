package com.wordlypost;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wordlypost.WordlyPostGoogleAnalytics.TrackerName;
import com.wordlypost.adapters.CategoryPostsAdapter;
import com.wordlypost.beans.NavDrawerItem;
import com.wordlypost.beans.PostRowItem;
import com.wordlypost.dao.PostsDAO;
import com.wordlypost.service.BuildService;
import com.wordlypost.utils.Utils;

public class CategoryPostsFragment extends Fragment {

	private ListView categoryPostsList;
	private ArrayList<PostRowItem> rowItems;
	private boolean loadingMore = false;
	private long page = 1;
	private CategoryPostsAdapter adapter;
	private ProgressBar progressBar;
	private NavDrawerItem categoryItem;
	private PostsDAO postsDAO;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.category_posts_fragment, container, false);
		Bundle bundle = getArguments();
		if (bundle.getSerializable("category") != null) {
			categoryItem = (NavDrawerItem) bundle.getSerializable("category");
			try {
				Tracker t = ((WordlyPostGoogleAnalytics) getActivity().getApplication())
						.getTracker(TrackerName.APP_TRACKER);
				t.setScreenName(categoryItem.getTitle());
				t.send(new HitBuilders.AppViewBuilder().build());

			} catch (Exception e) {
				Log.d("TAG", getString(R.string.google_analytics_error));
			}

			TextView categoryTitle = (TextView) view.findViewById(R.id.category_title);
			categoryTitle.setText(Html.fromHtml(categoryItem.getTitle()));

			rowItems = new ArrayList<PostRowItem>();
			categoryPostsList = (ListView) view.findViewById(R.id.categoryPostsList);
			progressBar = (ProgressBar) view.findViewById(R.id.categoryPostsProgressBar);
			adapter = new CategoryPostsAdapter(getActivity(), R.layout.category_posts_list_item,
					rowItems);

			int spacing = (int) getResources().getDimension(R.dimen.spacing);
			int itemsPerRow = getResources().getInteger(R.integer.items_per_row);
			MultiItemRowListAdapter wrapperAdapter = new MultiItemRowListAdapter(getActivity(),
					adapter, itemsPerRow, spacing);
			categoryPostsList.setAdapter(wrapperAdapter);

			categoryPostsList.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
						int totalItemCount) {

					int lastInScreen = firstVisibleItem + visibleItemCount;
					if ((lastInScreen == totalItemCount) && !(loadingMore)) {
						if (Utils.isNetworkAvailable(getActivity())) {
							getDataFromServer();
						} else {
							getDataFromSqlite();
						}
					}
				}
			});
		}
		return view;
	}

	private void getDataFromServer() {
		loadingMore = true;
		progressBar.setVisibility(View.VISIBLE);

		BuildService.build.getCategoriesPosts(categoryItem.getId(), categoryItem.getSlug(), page,
				new Callback<String>() {

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
								JSONArray categotyPosts = obj.getJSONArray("posts");
								String currentMilliSeconds = Calendar.getInstance()
										.getTimeInMillis() + "";

								for (int i = 0; i < categotyPosts.length(); i++) {
									JSONObject categotyPost = categotyPosts.getJSONObject(i);

									PostRowItem item = new PostRowItem();

									item.setPost_id(categotyPost.getInt("id"));
									item.setTitle(categotyPost.getString("title"));
									item.setDate(categotyPost.getString("date"));
									item.setPost_icon_url(categotyPost.getString("thumbnail"));
									item.setAuthor(categotyPost.getJSONObject("author").getString(
											"name"));
									item.setContent(categotyPost.getString("content"));
									item.setPost_banner(categotyPost
											.getJSONObject("thumbnail_images")
											.getJSONObject("full").getString("url"));
									item.setComment_count(categotyPost.getInt("comment_count"));
									item.setPost_url(categotyPost.getString("url"));

									if (categotyPost.getInt("comment_count") > 0) {
										item.setCommentsArray(categotyPost.getJSONArray("comments")
												.toString());
									}
									item.setTagsArray(categotyPost.getJSONArray("tags").toString());

									rowItems.add(item);

									if (page == 1) {
										Log.i("page :", page + "");
										postsDAO = new PostsDAO(getActivity());
										postsDAO.insertPosts(categotyPost.getInt("id"),
												categotyPost.getString("title"), categotyPost
														.getString("date"), categotyPost
														.getString("thumbnail"), categotyPost
														.getJSONObject("author").getString("name"),
												categotyPost.getString("content"), categotyPost
														.getJSONObject("thumbnail_images")
														.getJSONObject("full").getString("url"),
												categotyPost.getInt("comment_count"), categotyPost
														.getString("url"), currentMilliSeconds,
												categoryItem.getId(), categoryItem.getSlug(),
												categotyPost.getJSONArray("comments").toString(),
												categotyPost.getJSONArray("tags").toString());
									}
								}

								if (page == 1) {
									long deleted = postsDAO.deletePosts(categoryItem.getId(),
											categoryItem.getSlug(), currentMilliSeconds);
									Log.i("Deletion Failure: ", deleted + "");
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
	}

	private void getDataFromSqlite() {
		loadingMore = true;
		progressBar.setVisibility(View.VISIBLE);
		postsDAO = new PostsDAO(getActivity());

		if (postsDAO.getPostsCount(categoryItem.getId(), categoryItem.getSlug()) > 0) {
			rowItems = postsDAO.getPosts(categoryItem.getId(), categoryItem.getSlug());
			adapter = new CategoryPostsAdapter(getActivity(), R.layout.category_posts_list_item,
					rowItems);

			int spacing = (int) getResources().getDimension(R.dimen.spacing);
			int itemsPerRow = getResources().getInteger(R.integer.items_per_row);
			MultiItemRowListAdapter wrapperAdapter = new MultiItemRowListAdapter(getActivity(),
					adapter, itemsPerRow, spacing);
			categoryPostsList.setAdapter(wrapperAdapter);
			adapter.notifyDataSetChanged();
		} else {
			Utils.displayToad(getActivity(), getString(R.string.no_posts_error_msg));
		}
		progressBar.setVisibility(View.GONE);
	}
}
