package com.wordlypost.service;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface WordlyPostService {
	@GET("/get_category_index")
	void getCategories(Callback<String> callback);

	@GET("/get_category_posts")
	void getCategoriesPosts(@Query("id") String categoryId, @Query("slug") String slug,
			@Query("page") long page, Callback<String> callback);
}
