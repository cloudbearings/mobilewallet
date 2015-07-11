package com.funnyzone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.funnyzone.adapters.CategoryPostsAdapter;
import com.funnyzone.beans.NavDrawerItem;
import com.funnyzone.beans.PostRowItem;
import com.funnyzone.dao.PostsDAO;
import com.funnyzone.gcm.Config;
import com.funnyzone.google.adcontroller.AdController;
import com.funnyzone.googleanalytics.FunnyZoneGoogleAnalytics;
import com.funnyzone.googleanalytics.FunnyZoneGoogleAnalytics.TrackerName;
import com.funnyzone.utils.Utils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.category_posts_fragment,
				container, false);

		try {
			RelativeLayout bannerLayout = (RelativeLayout) view
					.findViewById(R.id.bannerAd);
			AdController.bannerAd(getActivity(), bannerLayout,
					getString(R.string.category_posts_banner_unit_id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Bundle bundle = getArguments();
		if (bundle.getSerializable("category") != null) {
			categoryItem = (NavDrawerItem) bundle.getSerializable("category");
			try {
				Tracker t = ((FunnyZoneGoogleAnalytics) getActivity()
						.getApplication()).getTracker(TrackerName.APP_TRACKER);
				t.setScreenName(categoryItem.getTitle());
				t.send(new HitBuilders.AppViewBuilder().build());

			} catch (Exception e) {
				Log.d("TAG", getString(R.string.google_analytics_error));
			}

			TextView categoryTitle = (TextView) view
					.findViewById(R.id.category_title);
			categoryTitle.setText(Html.fromHtml(categoryItem.getTitle()));

			rowItems = new ArrayList<PostRowItem>();
			categoryPostsList = (ListView) view
					.findViewById(R.id.categoryPostsList);
			progressBar = (ProgressBar) view
					.findViewById(R.id.categoryPostsProgressBar);
			adapter = new CategoryPostsAdapter(getActivity(),
					R.layout.category_posts_list_item, rowItems);

			int spacing = (int) getResources().getDimension(R.dimen.spacing);
			int itemsPerRow = getResources()
					.getInteger(R.integer.items_per_row);
			MultiItemRowListAdapter wrapperAdapter = new MultiItemRowListAdapter(
					getActivity(), adapter, itemsPerRow, spacing);
			categoryPostsList.setAdapter(wrapperAdapter);

			categoryPostsList.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {

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

		new AsyncTask<String, String, String>() {
			@Override
			protected String doInBackground(String... urls) {
				String response = "";

				try {

					URL url = new URL(urls[0]);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					BufferedReader br = new BufferedReader(
							new InputStreamReader((conn.getInputStream())));
					StringBuffer sb = new StringBuffer("");
					String output;
					while ((output = br.readLine()) != null) {
						sb.append(output);
					}
					response = sb.toString();
					conn.disconnect();

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return response;
			}

			@Override
			protected void onPostExecute(String output) {
				try {
					Log.i("CategoriesPosts", output);

					JSONObject obj = new JSONObject(output);

					if (obj.getJSONArray("posts").length() > 0) {
						JSONArray categotyPosts = obj.getJSONArray("posts");
						for (int i = 0; i < categotyPosts.length(); i++) {
							JSONObject categotyPost = categotyPosts
									.getJSONObject(i);

							PostRowItem item = new PostRowItem();

							item.setPost_id(categotyPost.getInt("ID"));
							item.setTitle(categotyPost.getString("title"));
							item.setDate(categotyPost.getString("date"));

							JSONObject thumbnill = categotyPost
									.getJSONObject("attachments");
							
							for (int j = 0; j < thumbnill.length(); j++) {
								//JSONObject image = thumbnill.get;
							}

							/*item.setPost_icon_url(categotyPost
									.getJSONObject("attachments"));*/
							item.setAuthor(categotyPost.getJSONObject("author")
									.getString("name"));
							item.setContent(categotyPost.getString("content"));

							rowItems.add(item);
						}

						adapter.notifyDataSetChanged();
						loadingMore = true;
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
		}.execute(Config.POSTS + "?category=" + categoryItem.getSlug());
	}

	private void getDataFromSqlite() {
		loadingMore = true;
		progressBar.setVisibility(View.VISIBLE);
		postsDAO = new PostsDAO(getActivity());

		if (postsDAO
				.getPostsCount(categoryItem.getId(), categoryItem.getSlug()) > 0) {
			rowItems = postsDAO.getPosts(categoryItem.getId(),
					categoryItem.getSlug());
			adapter = new CategoryPostsAdapter(getActivity(),
					R.layout.category_posts_list_item, rowItems);

			int spacing = (int) getResources().getDimension(R.dimen.spacing);
			int itemsPerRow = getResources()
					.getInteger(R.integer.items_per_row);
			MultiItemRowListAdapter wrapperAdapter = new MultiItemRowListAdapter(
					getActivity(), adapter, itemsPerRow, spacing);
			categoryPostsList.setAdapter(wrapperAdapter);
			adapter.notifyDataSetChanged();
		} else {
			Utils.displayToad(getActivity(),
					getString(R.string.no_posts_error_msg));
		}
		progressBar.setVisibility(View.GONE);
	}

	@Override
	public void onResume() {
		super.onResume();
		AdController.resumeAdView();
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
