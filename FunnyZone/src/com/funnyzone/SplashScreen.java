package com.funnyzone;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.funnyzone.dao.CategoriesDAO;
import com.funnyzone.service.BuildService;
import com.funnyzone.utils.Utils;

public class SplashScreen extends ActionBarActivity {

	private static final String TAG = "SplashScreen";
	private String categories;
	private CategoriesDAO categoriesDAO;
	private static int SPLASH_TIME_OUT = 1500;

	@Override
	protected void onResume() {
		super.onResume();
		Utils.googleAnalaticsTracking(SplashScreen.this, getString(R.string.splash_screen_name));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.splash_screen);

			if (Utils.isNetworkAvailable(SplashScreen.this)) {
				categoriesDAO = new CategoriesDAO(SplashScreen.this);
				/*
				 * Checking categories are available in sqlite db.
				 */
				if (categoriesDAO.getCategoriesCount() > 0) {
					showTabsActivity();
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
										 * database.
										 */

										String isHomeCategory;
										if (i < 5) {
											// Log.i("count", i + "");
											isHomeCategory = getString(R.string.Y);
										} else {
											isHomeCategory = getString(R.string.N);
										}

										categoriesDAO.insertCategories(category.getInt("ID"),
												category.getString("name"),
												category.getString("slug"),
												category.getInt("post_count"), isHomeCategory);

									}
								}
								openTabsActivity();
							} catch (Exception e) {
							}
						}

						@Override
						public void failure(RetrofitError retrofitError) {
							Log.i("Categories Url:", retrofitError.getUrl());
							retrofitError.printStackTrace();
						}
					});
				}
			} else {
				showTabsActivity();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showTabsActivity() {
		try {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					startActivity(new Intent(SplashScreen.this, TabsActivity.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					finish();
				}
			}, SPLASH_TIME_OUT);
		} catch (Exception e) {
			Log.d(TAG, "Exception raised in showTabsActivity()");
			e.printStackTrace();
		}
	}

	private void openTabsActivity() {
		startActivity(new Intent(SplashScreen.this, TabsActivity.class).addFlags(
				Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("categories", categories));
		finish();
	}
}
