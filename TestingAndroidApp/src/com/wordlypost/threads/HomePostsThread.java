package com.wordlypost.threads;

import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.content.Context;
import android.util.Log;

import com.wordlypost.beans.NavDrawerItem;
import com.wordlypost.dao.CategoriesDAO;
import com.wordlypost.dao.HomeDAO;
import com.wordlypost.service.BuildService;

public class HomePostsThread extends Thread {
	private Context context;

	public HomePostsThread(Context context) {
		this.context = context;
	}

	public void run() {
		try {
			CategoriesDAO categoriesDAO = new CategoriesDAO(context);
			List<NavDrawerItem> categories = categoriesDAO.getRandomCategories();
			for (int i = 0; i < categories.size(); i++) {
				NavDrawerItem category = categories.get(i);
				getCategoryPosts(category.getId(), category.getSlug());
			}
		} catch (Exception e) {
			e.printStackTrace();
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
					HomeDAO homeDAO = new HomeDAO(context);
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
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
