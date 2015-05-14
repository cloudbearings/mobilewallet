package com.wordlypost;

import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.wordlypost.WordlyPostGoogleAnalytics.TrackerName;
import com.wordlypost.beans.NavDrawerItem;
import com.wordlypost.dao.CategoriesDAO;
import com.wordlypost.dao.HomeDAO;
import com.wordlypost.service.BuildService;
import com.wordlypost.threads.HomePostsThread;
import com.wordlypost.utils.Utils;

public class SplashScreen extends ActionBarActivity {

	private int count;
	private String categories;
	private CategoriesDAO categoriesDAO;
	private static int SPLASH_TIME_OUT = 1500;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			Tracker t = ((WordlyPostGoogleAnalytics) getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			t.setScreenName(getString(R.string.splash_screen_name));
			t.send(new HitBuilders.AppViewBuilder().build());

		} catch (Exception e) {
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.splash_screen);

			TextView appDes1 = (TextView) findViewById(R.id.app_des1);
			appDes1.setTypeface(
					Utils.getFont(SplashScreen.this, getString(R.string.DroidSerif_Bold)),
					Typeface.BOLD);
			TextView appDes2 = (TextView) findViewById(R.id.app_des2);
			appDes2.setTypeface(Utils.getFont(SplashScreen.this, getString(R.string.DroidSerif)));

			if (Utils.isNetworkAvailable(SplashScreen.this)) {
				categoriesDAO = new CategoriesDAO(SplashScreen.this);
				/*
				 * Checking categories are available in sqlite db.
				 */
				if (categoriesDAO.getCategoriesCount() > 0) {
					// getRandomCategories();
					startActivity(new Intent(SplashScreen.this, TabsActivity.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();
				} else {
					BuildService.build.getCategories(new Callback<String>() {

						@Override
						public void success(String output, Response arg1) {
							try {
								Log.i("Categories :", output);
								JSONObject obj = new JSONObject(output);
								if (obj.getString("status").equals(getString(R.string.error))) {
									Utils.displayToad(SplashScreen.this,
											getString(R.string.task_error_msg));
									finish();
								}
							} catch (Exception e) {
							}

							try {
								categories = output;
								JSONObject obj = new JSONObject(output);

								for (int i = 0; i < obj.getJSONArray("categories").length(); i++) {
									JSONObject category = obj.getJSONArray("categories")
											.getJSONObject(i);
									/*
									 * If server categories count is greater
									 * than sqlite db categories count then
									 * server categories data is storing in
									 * sqlite.
									 */
									if (obj.getJSONArray("categories").length() > categoriesDAO
											.getCategoriesCount()) {
										/*
										 * Storing categories in sqlite
										 * database. BestVideosEver category is
										 * not showing in Slider Drawer. If
										 * statement is used to stop storing
										 * BestVideosEver in sqlite database.
										 */
										if (category.getInt("id") != getResources().getInteger(
												R.integer.best_vides_ever)) {
											String isHomeCategory;
											if (category.getInt("id") == getResources().getInteger(
													R.integer.top_news)
													|| i < 5) {
												// Log.i("count", i + "");
												isHomeCategory = getString(R.string.Y);
											} else {
												isHomeCategory = getString(R.string.N);
											}

											categoriesDAO.insertCategories(category.getInt("id"),
													category.getString("title"),
													category.getString("slug"),
													category.getInt("post_count"), isHomeCategory);
										}
									}
								}
								// getRandomCategories();
								openTabsActivity();
							} catch (Exception e) {
							}
						}

						@Override
						public void failure(RetrofitError retrofitError) {
							retrofitError.printStackTrace();
						}
					});
				}
			} else {
				startActivity(new Intent(SplashScreen.this, TabsActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getRandomCategories() {
		/*
		 * Getting Random categories(5) from sqlite categories table.
		 */
		HomeDAO homeDAO = new HomeDAO(SplashScreen.this);
		long count = homeDAO.getTotlaPostsCount();
		if (count > 0) {
			HomePostsThread thread = new HomePostsThread(SplashScreen.this);
			thread.run();
			Log.i("Thread State : ", thread.getState() + "");
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					openTabsActivity();
				}
			}, SPLASH_TIME_OUT);
		} else {
			List<NavDrawerItem> categories = categoriesDAO.getRandomCategories();
			for (int i = 0; i < categories.size(); i++) {
				NavDrawerItem category = categories.get(i);
				getCategoryPosts(category.getId(), category.getSlug());
			}
		}
	}

	private void getCategoryPosts(final int categoryId, final String slug) {
		BuildService.build.getCategoriesPosts(categoryId, slug, 1, new Callback<String>() {

			@Override
			public void failure(RetrofitError retrofitError) {
				retrofitError.printStackTrace();
			}

			@Override
			public void success(String output, Response arg1) {
				try {
					Log.i("Category Posts :", output);
					HomeDAO homeDAO = new HomeDAO(SplashScreen.this);
					JSONObject obj = new JSONObject(output);
					String categoryTitle = obj.getJSONObject("category").getString("title");
					if (obj.getJSONArray("posts").length() > 0) {
						JSONArray categotyPosts = obj.getJSONArray("posts");
						String currentMilliSeconds = Calendar.getInstance().getTimeInMillis() + "";
						for (int i = 0; i < categotyPosts.length(); i++) {
							JSONObject categotyPost = categotyPosts.getJSONObject(i);
							homeDAO.insertPosts(
									categotyPost.getInt("id"),
									categotyPost.getString("title"),
									categotyPost.getString("date"),
									categotyPost.getString("thumbnail"),
									categotyPost.getJSONObject("author").getString("name"),
									categotyPost.getString("content"),
									categotyPost.getJSONObject("thumbnail_images")
											.getJSONObject("full").getString("url"), categotyPost
											.getInt("comment_count"),
									categotyPost.getString("url"), currentMilliSeconds,
									categotyPost.getString("excerpt"), categoryId, slug,
									categoryTitle,
									categotyPost.getJSONArray("comments").toString(), categotyPost
											.getJSONArray("tags").toString());
						}

						long deleted = homeDAO.deletePosts(categoryId, slug, currentMilliSeconds);
						Log.i("Deleted records: ", deleted + "");
						count++;
						if (count == 5) {
							openTabsActivity();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void openTabsActivity() {
		startActivity(new Intent(SplashScreen.this, TabsActivity.class).addFlags(
				Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("categories", categories));
		finish();
	}
}
